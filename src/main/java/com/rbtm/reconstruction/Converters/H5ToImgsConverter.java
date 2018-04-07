package com.rbtm.reconstruction.Converters;

import com.rbtm.reconstruction.Constants;
import com.rbtm.reconstruction.POJOs.DataShape;
import com.rbtm.reconstruction.POJOs.H5FloatObject;
import com.rbtm.reconstruction.Utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;


/*
Class for import h5 file to set of images
 */
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class H5ToImgsConverter implements Converter {
    private String outputDir;
    private String h5filePath;

    private String outputImgFormat(int i) {
        return outputDir + "/img_" + i + "." + Constants.PNG_FORMAT;
    }

    public H5ToImgsConverter(String inputDir, String outputDir, String h5FileName) {
        this.outputDir = outputDir;
        this.h5filePath = inputDir + "/" + h5FileName;
    }

    @Override
    public void convert() throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        H5FloatObject h5FloatObj =
                new H5FloatObject(h5filePath, Constants.H5_OBJECT);

        DataShape shape = h5FloatObj.getShape();
        System.out.println("Data shape is: " + shape);

        int[] blockDimensions = {1, shape.getHeight(), shape.getWidth()};
        float[][] imgMatrix;
        for (int i=0; i<shape.getNum(); ++i) {
            imgMatrix = h5FloatObj.getSliceMatrix(i, blockDimensions);

            Mat fMat = ImageUtils.matrix2Mat(imgMatrix);
            BufferedImage img = ImageUtils.Mat2BufferedImage(fMat);

            File outputfile = new File(outputImgFormat(i));
            ImageIO.write(img, Constants.PNG_FORMAT, outputfile);
        }
    }
}
