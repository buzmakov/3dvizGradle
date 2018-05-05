package com.rbtm.reconstruction.DataObjects;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImagesObject implements IMatDatasetObject{
    @Getter File imgDir;
    @Getter List<File> imgFiles;
    @Getter DataShape shape;
    @Getter int numOfBlocks;
    @Getter int blockSize;
    @Getter int currentBlock;

    private DataShape getDimension(List<File> imgFiles) throws IOException {
        if(imgFiles.size() == 0) {
            return new DataShape(0,0,0);
        }
        BufferedImage bimg = ImageIO.read(imgFiles.get(0));
        return new DataShape(imgFiles.size(), bimg.getHeight(), bimg.getWidth());
    }

    private void updateInfo(File imgDir, int numOfBlocks) throws IOException {
        this.imgFiles = Arrays.asList(imgDir.listFiles());
        this.shape = getDimension(imgFiles);
        this.numOfBlocks = numOfBlocks;
        this.blockSize = shape.getNum()/numOfBlocks;
        this.currentBlock = 0;
    }

    public ImagesObject(String imgDirPath, int numOfBlocks) throws IOException {
        this.imgDir = new File(imgDirPath);

        if (!this.imgDir.exists()) {
            throw new IOException("inputDir not exists");
        }

        updateInfo(imgDir, numOfBlocks);
    }

    @Override
    public boolean hasNextBlock() {
        return currentBlock < numOfBlocks;
    }

    @Override
    public List<Mat> getNextBlockMat() {
        ArrayList<Mat> arrMat = new ArrayList<>(blockSize);
        imgFiles.subList(currentBlock*blockSize, (currentBlock+1)*blockSize).forEach(it -> {
            arrMat.add(Imgcodecs.imread(it.getAbsolutePath()));
        });

        return arrMat;
    }

    @Override
    public List<Mat> getBlockMat(int i) {
        ArrayList<Mat> arrMat = new ArrayList<>(blockSize);
        imgFiles.subList(i*blockSize, (i+1)*blockSize).forEach(it -> {
            arrMat.add(Imgcodecs.imread(it.getAbsolutePath()));
        });

        return arrMat;
    }
/*
    @Override
    public void clearDataIfExists() throws IOException {
        if (imgFiles.size() == 0) {
            return;
        }

        FileUtils.cleanDirectory(imgDir);
        updateInfo(imgDir, numOfBlocks);
    }

    @Override
    public boolean addBlock(List<Mat> block) {
        return false;
    }

    @Override
    public boolean safeToFileSystem() {
        return false;
    }
    */
}
