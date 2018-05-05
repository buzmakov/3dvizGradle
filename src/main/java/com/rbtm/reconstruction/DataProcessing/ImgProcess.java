package com.rbtm.reconstruction.DataProcessing;

import org.opencv.core.Mat;

import java.util.List;

import  org.opencv.imgproc.Imgproc;


public class ImgProcess {
    public static List<Mat> test(List<Mat> block) {
        for(Mat slice: block) {
            Imgproc.threshold(slice, slice, 0.5, 255, Imgproc.THRESH_BINARY);
        }

        return block;
    }
}
