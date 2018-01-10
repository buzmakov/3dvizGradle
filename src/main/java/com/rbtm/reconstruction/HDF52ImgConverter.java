package com.rbtm.reconstruction;

import java.io.IOException;


/*
Class for import h5 file to set of images
 */
public class HDF52ImgConverter {
    private String inputH5Path;
    private String outputDirPath;

    public HDF52ImgConverter(String inputH5Path, String outputDirPath) {
        this.inputH5Path = inputH5Path;
        this.outputDirPath = outputDirPath;
    }

    public void convert() throws IOException {

    }
}
