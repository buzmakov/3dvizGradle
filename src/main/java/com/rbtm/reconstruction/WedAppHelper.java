package com.rbtm.reconstruction;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.rbtm.reconstruction.Converters.H5ToImgsConverter;
import com.rbtm.reconstruction.DataObjects.FilterEntity;
import com.rbtm.reconstruction.DataObjects.IMatDatasetObject;
import com.rbtm.reconstruction.DataProcessing.ImgProcessor;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Getter;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;


public class WedAppHelper {
    @Getter private List<String> objList;
    @Getter private String currentObject;
    @Getter private IMatDatasetObject datasetObj = null;
    @Getter private List<FilterEntity> filters = new ArrayList<>();

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

    public File getSlice(int id, String jsonFilters) throws IOException {
        if (filters.isEmpty()) {
            System.out.println("Filters not found.");
            return datasetObj.getSliceAsFile(id);
        }

        Mat img = datasetObj.getSlice(id);
        img = ImgProcessor.process(img, filters);

        String buffImgPath = Constants.TEMP_DIR_PATH + "/bufferImg." + Constants.PNG_FORMAT;
        Imgcodecs.imwrite(buffImgPath, img);
        System.out.println("Buff img updated: " + buffImgPath);
        return new File(buffImgPath);
    }

    public void setFiltersFromJson(String json) {
        System.out.println("body is: " + json);

        Gson gson = new Gson();
        FilterEntity[] filterEntities = gson.fromJson(json, FilterEntity[].class);
        System.out.println("Parsed body: " + Arrays.toString(filterEntities));

        this.filters = new ArrayList<>(Arrays.asList(filterEntities));
    }
}
