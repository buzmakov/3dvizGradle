package com.rbtm.reconstruction.DataObjects;

import com.rbtm.reconstruction.Constants;
import com.rbtm.reconstruction.Exceptions.DimensionMismatchException;
import com.rbtm.reconstruction.Utils.ArrayUtils;


import ch.systemsx.cisd.base.mdarray.MDFloatArray;
import ch.systemsx.cisd.hdf5.*;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


public class H5FloatObject extends H5Object{
    private IHDF5FloatReader reader;

    public H5FloatObject(String h5FilePath, String ObjectName, int numOfBlocks) throws DimensionMismatchException {
        super(h5FilePath, ObjectName, numOfBlocks);
        this.reader = abstractReader.float32();
    }
    /*TODO: separate to block for better concourency
    private static List<Mat> initMatArray(float[] imgFlatArr, ArrayList<Mat> arrMat, int blockSize, int height, int width) {
        int coreNums = Constants.THREAD_NUM;
        float max = ArrayUtils.getMax(imgFlatArr);
        float min = ArrayUtils.getMin(imgFlatArr);
        float diff = max-min;
        int imgSize = height*width;

        int standartBlockCapacity = blockSize/coreNums;
        int lastBlockCapacity = blockSize/coreNums;

        ArrayList<ArrayList<Mat>> threadArrMap = new ArrayList<>(coreNums);

        IntStream.range(0, coreNums).forEach(i -> {
            int blockCapacity = (i == coreNums - 1)?
                    lastBlockCapacity:
                    standartBlockCapacity;

            ArrayList<Mat> tempArr = new ArrayList<>(blockCapacity);
            threadArrMap.add(tempArr);
        });


        IntStream.range(0, coreNums).parallel().forEach(i -> {
                    int blockCapacity = (i == coreNums - 1) ?
                            lastBlockCapacity :
                            standartBlockCapacity;
                    IntStream.range(0, blockCapacity * imgSize).forEach(j ->
                            threadArrMap.get(i).get(j / imgSize).put(
                                    (j / height) % height, j % width, (imgFlatArr[i * standartBlockCapacity + j] - min) * 255 / (diff))
                    );
        });

        IntStream.range(0, coreNums).forEach(i -> {
            arrMat.addAll(threadArrMap.get(i));
        });
        return arrMat;

    }
    */
    private static List<Mat> arrFloat2arrMat(float[] imgFlatArr, int height, int width) {
        long startTime;
        long endTime;
        int blockSize = imgFlatArr.length/(height*width);

        startTime = System.currentTimeMillis();
        List<Mat> arrMat = new ArrayList<>(blockSize);
        IntStream.range(0, blockSize).forEach(i -> arrMat.add(new Mat(height, width, Constants.DEFAULT_MAT_TYPE)));

        endTime = System.currentTimeMillis();
        System.out.println("creating mat array: " + (endTime - startTime)/1000 + " s");

        startTime = System.currentTimeMillis();

        float max = ArrayUtils.getMax(imgFlatArr);
        float min = ArrayUtils.getMin(imgFlatArr);
        float diff = max-min;
        int imgSize = height*width;
        IntStream.range(0, imgFlatArr.length).parallel().forEach(i -> {
            //System.out.printf("%d %d %d %d %f\n", i, i/imgSize, i/height, i%width, imgFlatArr[i]);
            arrMat.get(i/imgSize).put(
                    (i/height)%height,i%width,(imgFlatArr[i] - min)*255/(diff));
        });


        //arrMat = initMatArray(imgFlatArr, arrMat, blockSize, height, width);

        endTime = System.currentTimeMillis();

        System.out.println("filling mat array: " + (endTime - startTime)/1000 + " s");

        return arrMat;
    }

    private MDFloatArray getMdArray(int i, int[] blockDimensions) {
        long[] offsetArr = {i, 0, 0};
        return reader.readMDArrayBlockWithOffset(this.objectName, blockDimensions, offsetArr);
    }

    private float[] getBlockArray(int i, int[] blockDimensions){
        MDFloatArray mdArr = getMdArray(i, blockDimensions);
        return mdArr.getAsFlatArray();
    }

    private float[] getNextBlock(){
        return getBlockArray(currentBlock++*blockSize, blockDimensions);
    }

    public List<Mat> getNextBlockMat(){
        float[] imgFlatArr = getNextBlock();
        return arrFloat2arrMat(imgFlatArr, shape.getHeight(), shape.getWidth());
    }

    public List<Mat>  getBlockMat(int i) {
        float[] imgFlatArr = getBlockArray(i*blockSize, blockDimensions);
        return arrFloat2arrMat(imgFlatArr, shape.getHeight(), shape.getWidth());
    }
}
