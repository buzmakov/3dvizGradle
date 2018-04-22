package com.rbtm.reconstruction.Utils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.*;
import java.util.stream.IntStream;

public class ImageUtils {
    public static BufferedImage Mat2BufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        System.out.println(
                "img channels: " + m.channels() + "\n" +
                "img cols: " + m.cols() + "\n" +
                "img rows: " + m.rows()
        );

        int bufferSize = m.channels()*m.cols()*m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }


    public static Mat matrix2Mat(float[][] matrix) {
        int h = matrix.length;
        int w = matrix[0].length;

        Mat resultMat = new Mat(h, w, CvType.CV_32F);
        IntStream.range(0, h).parallel().forEach(i -> resultMat.put(i, 0, matrix[i]));

        return resultMat;
    }


    public static Mat arr2Mat(double[] imgFlatArr, int height, int width) {
        Mat resultMat = new Mat(height, width, CvType.CV_32F);
        double max = Arrays.stream(imgFlatArr).max().getAsDouble();
        double min = Arrays.stream(imgFlatArr).min().getAsDouble();
        double diff = max-min;

        IntStream.range(0, height*width).parallel().forEach(i -> resultMat.put(i/height, i%width, (imgFlatArr[i] - min)*255/(diff)));

        return resultMat;
    }

    public static List<Mat> arrDouble2arrMat(float[] imgFlatArr, int blockSize, int height, int width) {
        ArrayList<Mat> arrMat = new ArrayList<>(Collections.nCopies(blockSize, new Mat(height, width, CvType.CV_32F)));

        float max = Utils.getMax(imgFlatArr);
        float min = Utils.getMin(imgFlatArr);
        float diff = max-min;
        int imgSize = height*width;

        IntStream.range(0, imgFlatArr.length).parallel().forEach( i ->
                arrMat.get(i/imgSize).put(
                i/height,i%width,(imgFlatArr[i] - min)*255/(diff)));

        return arrMat;
    }
}
