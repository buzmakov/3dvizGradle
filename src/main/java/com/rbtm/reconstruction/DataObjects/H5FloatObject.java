package com.rbtm.reconstruction.DataObjects;

import com.rbtm.reconstruction.Constants;
import com.rbtm.reconstruction.Exceptions.DimensionMismatchException;


import ch.systemsx.cisd.base.mdarray.MDFloatArray;
import ch.systemsx.cisd.hdf5.*;
import com.rbtm.reconstruction.Utils.CustomArrayUtils;
import com.rbtm.reconstruction.Utils.Timer;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


public class H5FloatObject extends H5Object{
    private IHDF5FloatReader reader;

    public H5FloatObject(String h5FilePath, String ObjectName, int numOfBlocks, int step) throws DimensionMismatchException {
        super(h5FilePath, ObjectName, numOfBlocks, step);
        this.reader = abstractReader.float32();
    }

    public H5FloatObject(String h5FilePath, String ObjectName, int numOfBlocks) throws DimensionMismatchException {
        this(h5FilePath, ObjectName, numOfBlocks, 1);
    }

    private List<Mat> arrFloat2arrMat(float[] imgFlatArr) {
        Timer timer = new Timer();

        int imgSize = shape.getHeight()*shape.getWidth();

        timer.startStage("creating mat array");
        List<Mat> arrMat = new ArrayList<>(blockSize);
        IntStream.range(0, blockSize).forEach(i -> arrMat.add(new Mat(shape.getHeight(), shape.getWidth(), Constants.DEFAULT_MAT_TYPE)));

        timer.nextStage("filling mat array");

        float max = CustomArrayUtils.getMax(imgFlatArr);
        float min = CustomArrayUtils.getMin(imgFlatArr);
        float diff = max-min;


        if (step == 1) {
            IntStream.range(0, imgFlatArr.length)
                    .parallel()
                    .forEach(i -> {
                        arrMat.get(i/imgSize).put(
                                (i/shape.getHeight())%shape.getHeight(),
                                i%shape.getWidth(),
                                (imgFlatArr[i] - min)*255/(diff));
                    });
        } else {
            int[] indexArr = IntStream.range(0, imgFlatArr.length)
                    .parallel()
                    .filter(i ->
                            i/(shape.getHeight()*step)%step==0 &&
                            i%(shape.getWidth()*step)%step==0 &&
                            i/(imgSize*step*step)%step == 0)
                    .toArray();

            IntStream.range(0, imgSize*blockSize)
                    .parallel()
                    .forEach(i -> {
                        arrMat.get(i/imgSize).put(
                                (i/shape.getHeight())%shape.getHeight(),
                                i%shape.getWidth(),
                                (imgFlatArr[indexArr[i]] - min)*255/(diff));
                    });
        }

        timer.endStage();

        return arrMat;
    }

    private MDFloatArray getMdArray(int i) {
        long[] offsetArr = {i, 0, 0};
        return reader.readMDArrayBlockWithOffset(this.objectName, blockDimensions, offsetArr);
    }

    private float[] getBlockArray(int i){
        MDFloatArray mdArr = getMdArray(i);
        return mdArr.getAsFlatArray();
    }

    private float[] getNextBlock(){
        return getBlockArray(currentBlock++*blockSize*step);
    }

    public List<Mat> getNextBlockMat(){
        float[] imgFlatArr = getNextBlock();
        return arrFloat2arrMat(imgFlatArr);
    }

    public List<Mat>  getBlockMat(int i) {
        float[] imgFlatArr = getBlockArray(i*blockSize*step);
        return arrFloat2arrMat(imgFlatArr);
    }
}
