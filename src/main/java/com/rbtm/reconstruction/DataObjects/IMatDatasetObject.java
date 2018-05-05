package com.rbtm.reconstruction.DataObjects;

import org.opencv.core.Mat;

import java.io.IOException;
import java.util.List;

public interface IMatDatasetObject {

    int getNumOfBlocks();

    int getBlockSize();

    int getCurrentBlock();

    DataShape getShape();

    boolean hasNextBlock();

    List<Mat> getNextBlockMat();

    List<Mat>  getBlockMat(int i);

/*TODO: for converting to different format
    void clearDataIfExists() throws IOException;

    boolean addBlock(List<Mat> block);

    boolean safeToFileSystem();
*/
}
