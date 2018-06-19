package com.rbtm.reconstruction.Converters;

import com.rbtm.reconstruction.Constants;
import com.rbtm.reconstruction.DataObjects.DataShape;
import com.rbtm.reconstruction.DataObjects.H5.H5FloatObject;

import com.rbtm.reconstruction.DataObjects.IMatDatasetObject;
import com.rbtm.reconstruction.DataObjects.ImagesDataset.ImagesDataset;
import com.rbtm.reconstruction.Utils.Timer;
import org.apache.commons.io.FileUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/*
Class for import h5 file to set of images
 */
public class H5ToImgsConverter {
    private String outputDir;
    private String h5filePath;
    private String objName;

    public H5ToImgsConverter(String inputDir, String outputDir, String h5FileName) throws IOException {
        this.outputDir = outputDir;
        this.h5filePath = inputDir + "/" + h5FileName;
        this.objName = h5FileName.split("\\.")[0];
    }

    private String outputImgFormat(int i) {
        return outputDir +
                "/img_" + i +
                "." + Constants.PNG_FORMAT;
    }

    private void printInfo() {
        System.out.println(
                "Converting with next parameters: \n" +
                "h5 store path: " + Constants.OBJ_PATH + "\n" +
                "output path: " + Constants.TEMP_IMG_PATH + "/" + objName + " \n" +
                "object name: " + objName);
    }

    private void cleanCache() throws IOException {
        File outputDirFile = new File(outputDir);
        if (!outputDirFile.exists()) {
            outputDirFile.mkdir();
        } else {
            FileUtils.cleanDirectory(outputDirFile);

        }
    }
    private boolean isFoundInCache(DataShape shape) {
        File f = new File(outputDir);
        return f.exists() && f.isDirectory() && f.listFiles().length == shape.getNum();
    }

    private IMatDatasetObject convertT(boolean isCheckCache) throws Exception {
        printInfo();

        Timer timer = new Timer();

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        timer.startStage("init h5Object");

        H5FloatObject h5Obj =
                new H5FloatObject(h5filePath, Constants.H5_OBJECT, Constants.NUM_OF_BLOCKS, Constants.RESIZE_STEP);

        DataShape shape = h5Obj.getShape();
        int blockSize = h5Obj.getBlockSize();

        System.out.println("Data shape is: " + shape);

        if(isCheckCache && isFoundInCache(shape)) {
            System.out.println("Converting resut for " + objName + " is exist in cache. Skip converting");
            return new ImagesDataset(
                    Constants.TEMP_IMG_PATH + "/" + objName,
                    Constants.NUM_OF_BLOCKS);
        }

        cleanCache();

        timer.endStage();

        for(int i = 0; i<h5Obj.getNumOfBlocks(); ++i) {
            System.out.println("Process block: " + i);
            int blockI = i;
            List<Mat> matArr = h5Obj.getBlockMat(i);

            timer.startStage("safe mat array to imgs");

            ExecutorService executor = Executors.newFixedThreadPool(Constants.THREAD_NUM);
            for (int mi = 0; mi < matArr.size(); ++mi) {
                int finalMi = mi;
                executor.submit( () -> {
                    Mat buff = matArr.get(finalMi);
                    Core.bitwise_and(buff, buff, buff);
                    Imgcodecs.imwrite(outputImgFormat(blockI * blockSize + finalMi), buff);
                });
            }

            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            timer.endStage();
        }

        System.out.println("Finish converting");
        return new ImagesDataset(outputDir, Constants.NUM_OF_BLOCKS);
    }

    public IMatDatasetObject convert() throws Exception {
        return convertT(true);
    }

    public IMatDatasetObject forceConvert() throws Exception {
        return convertT(false);
    }
}
