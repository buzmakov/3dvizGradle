package com.rbtm.reconstruction;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        String inputImg = args[0];
        String tempH5 = args[1];
        String outImg = args[2];

        Img2HDF5Converter i2h = new Img2HDF5Converter(inputImg, tempH5);
        HDF52ImgConverter h2i = new HDF52ImgConverter(tempH5, outImg);
        //i2h.convert();
        h2i.convert();

    }
}
