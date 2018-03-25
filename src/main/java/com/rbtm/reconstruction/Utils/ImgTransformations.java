package com.rbtm.reconstruction.Utils;

import com.rbtm.reconstruction.Constants;

import java.awt.image.*;
import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.imageio.*;


public class ImgTransformations{
    private int[][] myPixels;

    public ImgTransformations(File file) {
        BufferedImage img;
        try {
            img = ImageIO.read(file);
        } catch (Exception e) {
            System.out.println("Incorrect File ");
            return;
        }
        if (img.getType() == BufferedImage.TYPE_INT_RGB) {
            myPixels = imageToPixels(img);
        } else {
            BufferedImage tmpImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            tmpImage.createGraphics().drawImage(img, 0, 0, null);
            myPixels = imageToPixels(tmpImage);
        }
    }

    public int[][] getPixels() {
        return myPixels;
    }

    public static BufferedImage pixelsToImage(int[][] pixels) throws IllegalArgumentException {

        if (pixels == null) {
            throw new IllegalArgumentException();
        }

        int width = pixels[0].length;
        int height = pixels.length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < height; row++) {
            image.setRGB(0, row, width, 1, pixels[row], 0, width);
        }
        return image;
    }

    public static int[][] imageToPixels(BufferedImage image) throws IllegalArgumentException {
        if (image == null) {
            throw new IllegalArgumentException();
        }

        int width = image.getWidth();
        int height = image.getHeight();
        int[][] pixels = new int[height][width];

        ExecutorService executor = Executors.newFixedThreadPool(Constants.THREAD_NUM);
        for (int row = 0; row < height; row++) {
            int finalRow = row;
            executor.submit(() -> {
                image.getRGB(0, finalRow, width, 1, pixels[finalRow], 0, width);
                    });
        }

        try {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.toString(pixels[10]));
        return pixels;
    }
}