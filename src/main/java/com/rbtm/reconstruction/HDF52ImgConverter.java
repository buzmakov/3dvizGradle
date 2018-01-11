package com.rbtm.reconstruction;

import ch.systemsx.cisd.base.mdarray.MDIntArray;
import ch.systemsx.cisd.hdf5.HDF5Factory;
import ch.systemsx.cisd.hdf5.IHDF5IntReader;
import ch.systemsx.cisd.hdf5.IHDF5ObjectReadOnlyInfoProviderHandler;
import ch.systemsx.cisd.hdf5.IHDF5Reader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;


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

    public void convert() throws Exception {
        IHDF5Reader reader = HDF5Factory.openForReading(inputH5Path);

        IHDF5ObjectReadOnlyInfoProviderHandler infoReader = reader.object();
        int dataRank = infoReader.getArrayRank(Constants.H5_OBJECT);
        if (dataRank != 3){
            throw new Exception("data is not 3d matrix");
        }
        int[] demension = infoReader.getArrayDimensions(Constants.H5_OBJECT);
        int num = demension[0]; //num of images
        int height = demension[1]; //heigth
        int width = demension[2]; //width

        IHDF5IntReader intReader = reader.int32();
        for (int i=0; i<num; ++i) {
            MDIntArray mdArr = intReader.readMDArrayBlockWithOffset(Constants.H5_OBJECT, new int[]{1, height, width}, new long[]{i, 0, 0});
            int[][] imgArr = mdArr.toMatrix();
            BufferedImage img = ImgTransformations.pixelsToImage(imgArr);
            File outputfile = new File(outputDirPath + "/" + i +  ".png");
            ImageIO.write(img, "png", outputfile);
        }
    }
}
