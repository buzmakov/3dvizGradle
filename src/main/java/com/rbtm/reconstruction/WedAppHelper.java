package com.rbtm.reconstruction;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.rbtm.reconstruction.Converters.H5ToImgsConverter;
import com.rbtm.reconstruction.DataObjects.IMatDatasetObject;
import lombok.Getter;

import javax.imageio.ImageIO;


public class WedAppHelper {
    @Getter private List<String> objList;
    @Getter private String currentObject;
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

    private H5ToImgsConverter initConverter() throws IOException {
        return new H5ToImgsConverter(
                Constants.OBJ_PATH,
                Constants.TEMP_IMG_PATH + "/" + currentObject,
                currentObject + ".hdf5");
    }

    public void convert() throws Exception {
        System.out.println("Start converting...");
        datasetObj = initConverter().convert();
    }

    public boolean forceConvert() throws Exception {
        System.out.println("Start force converting...");
        datasetObj = initConverter().forceConvert();
        return true;
    }

    public boolean isInit() {
        return datasetObj != null;
    }

    public void setCurrentObject(String currentObject) throws Exception {
        System.out.println("New objName is: " + currentObject);
        this.currentObject = currentObject;
        if (!objList.contains(currentObject)) {
            System.out.println("Object "+ currentObject +" is not found");
            throw new Exception("Object " + currentObject + " is not found");
        }
        convert();
    }

}
