package com.rbtm.reconstruction.Utils;

import java.io.File;
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
}
