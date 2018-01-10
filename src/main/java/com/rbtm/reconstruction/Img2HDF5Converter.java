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


public class Img2HDF5Converter {
    private static List<File> getImageFileList(String dirPath){
        File sourceDir = new File(dirPath);

        if(!sourceDir.isDirectory()) {
            throw new IllegalArgumentException(dirPath + " is not a directory");
        } else if(sourceDir.listFiles() == null) {
            throw new IllegalArgumentException(dirPath + " is empty directory");
        }

        ArrayList listOfFiles = new ArrayList(Arrays.asList(sourceDir.listFiles()));

        listOfFiles.sort(Comparator.comparing(File::getName));

        return listOfFiles;
    }

    //return array [x, y, z]. For y, x component use first image
    private static int[] getDataSetSize(List<File> imgs) throws IOException {
        BufferedImage bimg = ImageIO.read(imgs.get(0));

        int x = bimg.getWidth();
        int y = bimg.getHeight();
        int z = imgs.size();

        return new int[]{x, y, z};
    }

    public void convert(String inputDirPath, String outputH5Path) throws IOException {
        List<File> images= getImageFileList(inputDirPath);

        int[] dataSize = getDataSetSize(images);

        int x = dataSize[0];
        int y = dataSize[1];
        int z = dataSize[2];

        System.out.println(Arrays.toString(dataSize));

        IHDF5IntWriter writer = HDF5Factory.open(outputH5Path).int32();

        writer.createMDArray(Constants.H5_OBJECT, new long[]{z, y, x}, new int[]{1, y, x});
        int[][][] buffer = new int[1][y][x];

        ExecutorService executor = Executors.newFixedThreadPool(Constants.THREAD_NUM);
        long startTime = System.nanoTime();
        for (int i=0; i< z; ++i){
            int finalI = i;
            executor.submit(() -> {
                try {
                    BufferedImage bufferedImage = ImageIO.read(images.get(finalI));
                    buffer[0] = ImgTransformations.imageToPixels(bufferedImage);
                    MDIntArray mdBuffer = new MDIntArray(buffer[0], new int[]{1, y, x});
                    writer.writeMDArrayBlock("Results", mdBuffer, new long[]{finalI, 0, 0});

                    long endTime = System.nanoTime();

                    System.out.println((double)(endTime - startTime)/1000000000.0);
                } catch (Exception e) {
                    System.out.println("Error while write to h5 file");
                    e.printStackTrace();
                }
            });



        }
    }

    //args: inputDirPath, outputH5Path
    public static void main(String[] args) throws IOException {
        Img2HDF5Converter test = new Img2HDF5Converter();

        long startTime = System.nanoTime();
        test.convert(args[0], args[1]);
        long endTime = System.nanoTime();

        System.out.println((double)(endTime - startTime)/1000000000.0);
    }
}