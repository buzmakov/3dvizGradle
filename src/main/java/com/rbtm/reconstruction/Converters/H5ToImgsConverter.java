package com.rbtm.reconstruction.Converters;

import com.rbtm.reconstruction.Constants;
import com.rbtm.reconstruction.POJOs.DataShape;
import com.rbtm.reconstruction.POJOs.H5DoubleObject;
import com.rbtm.reconstruction.POJOs.H5FloatObject;
import com.rbtm.reconstruction.Utils.ImageUtils;


import org.opencv.core.Core;
import org.opencv.core.Mat;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.List;
import java.util.stream.IntStream;


/*
Class for import h5 file to set of images
 */
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class H5ToImgsConverter implements Converter {
    private String outputDir;
    private String h5filePath;

    private String outputImgFormat(int i) {
        return outputDir +
                "/img_" +
                i +
                "_" +
                Thread.currentThread().getName() +
                "." +
                Constants.PNG_FORMAT;
    }

    public H5ToImgsConverter(String inputDir, String outputDir, String h5FileName) {
        this.outputDir = outputDir;
        this.h5filePath = inputDir + "/" + h5FileName;
    }

    @Override
    public void convert() throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        H5FloatObject h5Obj =
                new H5FloatObject(h5filePath, Constants.H5_OBJECT);

        DataShape shape = h5Obj.getShape();
        int blockSize = shape.getNum()/Constants.numOfBlocks;
        int[] blockDimensions = {blockSize, shape.getHeight(), shape.getWidth()};

        System.out.println("Data shape is: " + shape);
        System.out.println("Block size is: " + blockSize);

        IntStream.range(0, Constants.numOfBlocks).parallel().forEach( i -> {
            float[] imgFlatArr = h5Obj.getSliceArray(i*blockSize, blockDimensions);
            List<Mat> matArr = ImageUtils.arrDouble2arrMat(imgFlatArr, blockSize, shape.getHeight(), shape.getWidth());
            for(Mat m: matArr){
                Imgcodecs.imwrite(outputImgFormat(i), m);
            }
        });

    }
}
