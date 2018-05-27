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


public class WedAppHelper {
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

    WedAppHelper() {
        objList = parseObjList();
        currentObject = objList.get(0);
    }


    public boolean isInit(){
        if (datasetObj != null) {
            System.out.println("This object was init before. Skip converting");
            return true;
        } else {
            return false;
        }
    }


    private boolean objectIsConverted() {

        if(isInit()) {
            return true;
        }

        File f = new File(Constants.TEMP_IMG_PATH, currentObject);
        if (f.exists() && f.isDirectory() && f.listFiles().length>0) {
            System.out.println("Converting resut for " + currentObject + " is exist in cash. Skip converting");
            return true;
        }

        return false;
    }

    public boolean convert() throws Exception {
        if(objectIsConverted()) {
            datasetObj = new ImagesDataset(
                    Constants.TEMP_IMG_PATH + "/" + currentObject,
                    Constants.NUM_OF_BLOCKS);
            System.out.println("Dataset has next shape: " + datasetObj.getShape());
            return true;
        }

        return forceConvert();
    }

    public boolean forceConvert() throws Exception {
        System.out.println("Converting with next parameters: \n" +
                "h5 store path: " + Constants.OBJ_PATH + "\n" +
                "output path: " + Constants.TEMP_IMG_PATH + "/" + currentObject + " \n" +
                "object name: " + currentObject);

        H5ToImgsConverter converter = new H5ToImgsConverter(
                Constants.OBJ_PATH,
                Constants.TEMP_IMG_PATH + "/" + currentObject,
                currentObject + ".hdf5");

        datasetObj = converter.convert();
        System.out.println("Finish converting");
        return true;
    }
}
