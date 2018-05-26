package com.rbtm.reconstruction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rbtm.reconstruction.Converters.H5ToImgsConverter;
import com.rbtm.reconstruction.DataObjects.IMatDatasetObject;
import com.rbtm.reconstruction.DataObjects.ImagesDataset.ImagesDataset;
import lombok.Getter;
import lombok.Setter;


public class WebAppHealper {
    @Getter private List<String> objList;
    @Getter @Setter private String currentObject;
    @Getter private IMatDatasetObject datasetObj = null;

    private static List<String> parseObjList() {
        System.out.println("Objects store path: " + Constants.OBJ_PATH);
        File [] objStoreFiles = new File(Constants.OBJ_PATH).listFiles();

        if (objStoreFiles == null) {
            return new ArrayList<>();
        }

        List<String> result = new ArrayList<>(objStoreFiles.length);
        for(File obj: objStoreFiles) {
            String objName = obj.getName();
            System.out.println("H5 file found: " + objName);
            result.add(objName.split("\\.")[0]);
        }

        return result;
    }

    WebAppHealper() {
        objList = parseObjList();
        currentObject = objList.get(0);
    }


    public boolean isInit(){
        return datasetObj != null;
    }


    private boolean objectIsConverting() {
        if(isInit()) {
            System.out.println("This object was init before. Skip converting");
            return true;
        }
        File f = new File(Constants.TEMP_IMG_PATH, currentObject);
        if (f.exists() && f.isDirectory() && f.listFiles().length>0) {
            System.out.println("Converting resut is exist in cash. Skip converting");
        }
    }

    public boolean convert() throws Exception {
        if(objectIsConverting()) {
            datasetObj = new ImagesDataset(Constants.TEMP_IMG_PATH, Constants.NUM_OF_BLOCKS);
            return true;
        }

        return forceConvert();
    }

    public boolean forceConvert() throws Exception {
        H5ToImgsConverter converter = new H5ToImgsConverter(
                Constants.OBJ_PATH,
                Constants.TEMP_IMG_PATH + "/" + currentObject,
                currentObject + ".hdf5");

        datasetObj = converter.convert();
        return true;
    }
}
