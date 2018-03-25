package com.rbtm.reconstruction.Converters;

import com.rbtm.reconstruction.Constants;
import com.rbtm.reconstruction.POJOs.DataShape;
import com.rbtm.reconstruction.POJOs.H5FloatObject;
import com.rbtm.reconstruction.Utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import org.opencv.core.Core;
import org.opencv.core.MatOfFloat;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;


/*
Class for import h5 file to set of images
 */
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class H5ToImgsConverter implements Converter {
    private String inputDir;
    private String outputDir;
    private String h5FileName;

    private String outputImgFormat(int i) {
        return outputDir + "/img_" + i + "." + Constants.PNG_FORMAT;
    }

    @Override
    public void convert() throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        H5FloatObject h5FloatObj =
                new H5FloatObject(inputDir + "/" + h5FileName, Constants.H5_OBJECT);

        DataShape shape = h5FloatObj.getShape();

        int[] blockDimensions = {1, shape.getHeight(), shape.getWidth()};
        float[] imgArr;
        for (int i=0; i<shape.getNum(); ++i) {
            imgArr = h5FloatObj.getSliceArray(i, blockDimensions);

            MatOfFloat fMat = new MatOfFloat(imgArr);
            BufferedImage img = ImageUtils.Mat2BufferedImage(fMat);

            File outputfile = new File(outputImgFormat(i));
            ImageIO.write(img, Constants.PNG_FORMAT, outputfile);
        }
    }
}
