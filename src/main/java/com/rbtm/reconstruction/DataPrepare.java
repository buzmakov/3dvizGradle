package com.rbtm.reconstruction;


import ch.systemsx.cisd.base.mdarray.MDByteArray;
import ch.systemsx.cisd.hdf5.HDF5Factory;
import ch.systemsx.cisd.hdf5.IHDF5ByteWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.*;



public class DataPrepare {
    private static final int NUM_EXECUTORS=4;

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

        int x = imgs.size();
        int y = bimg.getWidth();
        int z = bimg.getHeight();

        return new int[]{x, y, z};
    }



    public static void main(String[] args) throws IOException {
        String sourceDirPath = args[0];
        List<File> images= getImageFileList(sourceDirPath);

        int[] dataSize = getDataSetSize(images);

        int x = dataSize[0];
        int y = dataSize[1];
        int z = dataSize[2];

        System.out.println(Arrays.toString(dataSize));



        IHDF5ByteWriter writer = HDF5Factory.open("myfile.h5").int8();

        writer.createMatrix("Results", x, y*z, 1, y*z);
        byte[][] imgBytes = new byte[1][y*z];

        long startTime = System.nanoTime();
        for (int i=0; i< x; ++i){
            BufferedImage bufferedImage = ImageIO.read(images.get(i));

            imgBytes[0] = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();

            try {
                writer.writeMatrixBlock("Results", imgBytes, i, 0);
            } catch (Exception e) {
                System.out.println("Error while write to h5 file");
                e.printStackTrace();
            }
        }


        long endTime = System.nanoTime();
        System.out.println((double)(endTime - startTime)/1000000000.0);
    }
}