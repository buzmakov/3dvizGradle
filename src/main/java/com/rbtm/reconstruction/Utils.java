package com.rbtm.reconstruction;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Utils {
    public static List<File> getImageFileList(String dirPath){
        File sourceDir = new File(dirPath);

        if(!sourceDir.isDirectory()) {
            throw new IllegalArgumentException(dirPath + " is not a directory");
        } else if(sourceDir.listFiles() == null) {
            throw new IllegalArgumentException(dirPath + " is empty directory");
        }

        ArrayList listOfFiles = new ArrayList(Arrays.asList(sourceDir.listFiles()));

        listOfFiles.sort(Comparator.comparing(File::getName));

        return listOfFiles;
    }

    //return array [x, y, z]. For y, x component use first image
    public static int[] getDataSetSize(List<File> imgs) throws IOException {
        BufferedImage bimg = ImageIO.read(imgs.get(0));

        int x = bimg.getWidth();
        int y = bimg.getHeight();
        int z = imgs.size();

        return new int[]{x, y, z};
    }
}
