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
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class H5ToImgsConverter implements Converter {
    private String outputDir;
    private String h5filePath;

    public H5ToImgsConverter(String inputDir, String outputDir, String h5FileName) throws IOException {
        this.outputDir = outputDir;
        this.h5filePath = inputDir + "/" + h5FileName;

        File outputDirFile = new File(outputDir);
        if (!outputDirFile.exists()) {
            outputDirFile.mkdir();
        } else {
            FileUtils.cleanDirectory(outputDirFile);

        }
    }

    private String outputImgFormat(int i) {
        return outputDir +
                "/img_" + i +
                "." + Constants.PNG_FORMAT;
    }

    @Override
    public IMatDatasetObject convert() throws Exception {
        Timer timer = new Timer();

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        timer.startStage("init h5Object");

        H5FloatObject h5Obj =
                new H5FloatObject(h5filePath, Constants.H5_OBJECT, Constants.NUM_OF_BLOCKS, Constants.RESIZE_STEP);

        DataShape shape = h5Obj.getShape();
        int blockSize = h5Obj.getBlockSize();

        timer.endStage();

        System.out.println("Data shape is: " + shape);

        for(int i = 0; i<h5Obj.getNumOfBlocks(); ++i) {
            System.out.println("Process block: " + i);
            int blockI = i;
            List<Mat> matArr = h5Obj.getBlockMat(i);

            timer.startStage("safe mat array to imgs");

            ExecutorService executor = Executors.newFixedThreadPool(Constants.THREAD_NUM);
            for (int mi = 0; mi < matArr.size(); ++mi) {
                int finalMi = mi;
                executor.submit( () -> {
                    //Imgproc.threshold(matArr.get(mi), matArr.get(mi), 127, 255, Imgproc.THRESH_BINARY);
                    Imgcodecs.imwrite(outputImgFormat(blockI * blockSize + finalMi), matArr.get(finalMi));
                });
            }

            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            timer.endStage();
        }

        return new ImagesDataset(outputDir, Constants.NUM_OF_BLOCKS);
    }
}
