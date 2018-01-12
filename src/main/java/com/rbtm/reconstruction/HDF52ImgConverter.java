package com.rbtm.reconstruction;

import ch.systemsx.cisd.base.mdarray.MDFloatArray;
import ch.systemsx.cisd.base.mdarray.MDIntArray;
import ch.systemsx.cisd.hdf5.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.toIntExact;


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

    private DataShape getDimension(IHDF5Reader reader) throws Exception {
        IHDF5ObjectReadOnlyInfoProviderHandler infoReader = reader.object();

        int dataRank = infoReader.getRank(Constants.H5_OBJECT);
        if (dataRank != 3){
            throw new Exception("data is not 3d matrix");
        }
        long [] demension = infoReader.getDimensions(Constants.H5_OBJECT);
        int num = toIntExact(demension[0]);
        int height = toIntExact(demension[1]);
        int width = toIntExact(demension[2]);

        return new DataShape(num, height, width);
    }

    public void convert() throws Exception {
        IHDF5Reader reader = HDF5Factory.openForReading(new File(inputH5Path));
        DataShape shape = getDimension(reader);

        IHDF5FloatReader fReader = reader.float32();
        for (int i=0; i<shape.getNum(); ++i) {
            MDFloatArray mdArr = fReader.readMDArrayBlockWithOffset(Constants.H5_OBJECT, new int[]{1, shape.getHeight(), shape.getWidth()}, new long[]{i, 0, 0});
            float[][] imgArr = mdArr.toMatrix();
            BufferedImage img = ImgTransformations.pixelsToImage(imgArr);
            File outputfile = new File(outputDirPath + "/" + i +  ".png");
            ImageIO.write(img, "png", outputfile);
        }
    }
}
