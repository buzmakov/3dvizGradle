package com.rbtm.reconstruction.DataProcessing;

import com.rbtm.reconstruction.DataObjects.FilterEntity;
import org.opencv.core.Mat;

import java.util.List;

import org.opencv.core.Size;
import  org.opencv.imgproc.Imgproc;


public class ImgprocBlocksWraper {
    public static List<Mat> blockMedian(List<Mat> block, int val) {
        block.stream().parallel().forEach(i -> {
                    Imgproc.medianBlur(i, i, val);
                }
        );
        return block;
    }

    public static List<Mat> blockGaussian(List<Mat> block, int val) {
        block.stream().parallel().forEach(i -> {
                    Imgproc.GaussianBlur(i, i, new Size(val, val), 0, 0);
                }
        );
        return block;
    }

    public static List<Mat> blockErosion(List<Mat> block, int val) {
        Mat element = Imgproc.getStructuringElement(
                Imgproc.MORPH_RECT,
                new Size(val, val));
        block.stream().parallel().forEach(i -> {
                    Imgproc.erode(i, i, element);
                }
        );
        return block;
    }

    public static List<Mat> blockDilatation(List<Mat> block, int val) {
        Mat element = Imgproc.getStructuringElement(
                Imgproc.MORPH_RECT,
                new Size(val, val));
        block.stream().parallel().forEach(i -> {
                    Imgproc.erode(i, i, element);
                }
        );
        return block;
    }

    public static List<Mat> blockOpening(List<Mat> block, int val) {
        Mat element = Imgproc.getStructuringElement(
                Imgproc.MORPH_RECT,
                new Size(val, val));
        block.stream().parallel().forEach(i -> {
                    Imgproc.morphologyEx(i, i, Imgproc.MORPH_OPEN, element);
                }
        );
        return block;
    }

    public static List<Mat> blockClosing(List<Mat> block, int val) {
        Mat element = Imgproc.getStructuringElement(
                Imgproc.MORPH_RECT,
                new Size(val, val));
        block.stream().parallel().forEach(i -> {
                    Imgproc.morphologyEx(i, i, Imgproc.MORPH_CLOSE, element);
                }
        );
        return block;
    }

    public static List<Mat> blockThreshold(List<Mat> block, int val) {
        block.stream().parallel().forEach(i -> {
                    Imgproc.threshold(i, i, val, 255, Imgproc.THRESH_BINARY);
                }
        );
        return block;
    }

    public static List<Mat> blockResize(List<Mat> block, int val) {
        block.stream().parallel().forEach(i -> {
            Imgproc.resize(i, i, new Size(i.cols()/val, i.rows()/val));
                }
        );
        return block;
    }
}
