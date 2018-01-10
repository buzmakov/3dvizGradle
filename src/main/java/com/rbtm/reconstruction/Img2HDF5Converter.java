package com.rbtm.reconstruction;

import ch.systemsx.cisd.base.mdarray.MDIntArray;
import ch.systemsx.cisd.hdf5.HDF5Factory;
import ch.systemsx.cisd.hdf5.IHDF5IntWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/*
Class for convert set of images to h5 file
 */
public class Img2HDF5Converter {
    private String inputDirPath;
    private String outputH5Path;

    public Img2HDF5Converter(String inputDirPath, String outputH5Path) {
        this.inputDirPath = inputDirPath;
        this.outputH5Path = outputH5Path;
    }

    public void convert() throws IOException {
        List<File> images= Utils.getImageFileList(inputDirPath);

        int[] dataSize = Utils.getDataSetSize(images);

        int x = dataSize[0];
        int y = dataSize[1];
        int z = dataSize[2];

        System.out.println(Arrays.toString(dataSize));

        IHDF5IntWriter writer = HDF5Factory.open(outputH5Path).int32();

        writer.createMDArray(Constants.H5_OBJECT, new long[]{z, y, x}, new int[]{1, y, x});
        int[][][] buffer = new int[1][y][x];

        ExecutorService executor = Executors.newFixedThreadPool(Constants.THREAD_NUM);
        for (int i=0; i< z; ++i){
            int finalI = i;
            executor.submit(() -> {
                try {
                    BufferedImage bufferedImage = ImageIO.read(images.get(finalI));
                    buffer[0] = ImgTransformations.imageToPixels(bufferedImage);
                    MDIntArray mdBuffer = new MDIntArray(buffer[0], new int[]{1, y, x});
                    writer.writeMDArrayBlock("Results", mdBuffer, new long[]{finalI, 0, 0});
                } catch (Exception e) {
                    System.out.println("Error while write to h5 file");
                    e.printStackTrace();
                }
            });
        }

        try {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //args: inputDirPath, outputH5Path
    public static void main(String[] args) throws IOException {
        Img2HDF5Converter test = new Img2HDF5Converter(args[0], args[1]);

        long startTime = System.nanoTime();
        test.convert();
        long endTime = System.nanoTime();

        System.out.println((double)(endTime - startTime)/Constants.NANO_IN_SEC);
    }
}