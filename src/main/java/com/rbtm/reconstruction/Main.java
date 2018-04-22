package com.rbtm.reconstruction;

import com.rbtm.reconstruction.Converters.H5ToImgsConverter;

public class Main {
    public static void main(String[] args) throws Exception {

        String inputDir = args[0];
        String outputDir = args[1];
        String h5file = args[2];

        long startTime = System.currentTimeMillis();

        H5ToImgsConverter c = new H5ToImgsConverter(inputDir, outputDir, h5file);

        c.convert();

        long endTime = System.currentTimeMillis();

        System.out.println("That took " + (endTime - startTime)/1000 + " s");
    }
}
