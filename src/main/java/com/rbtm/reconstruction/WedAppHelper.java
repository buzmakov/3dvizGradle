package com.rbtm.reconstruction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.rbtm.reconstruction.Converters.DatasetToMarshConverter;
import com.rbtm.reconstruction.Converters.H5ToImgsConverter;
import com.rbtm.reconstruction.DataObjects.FilterEntity;
import com.rbtm.reconstruction.DataObjects.IMatDatasetObject;
import com.rbtm.reconstruction.DataProcessing.Circle.CircleDetector;
import com.rbtm.reconstruction.DataProcessing.Circle.CirleDiagramEntity;
import com.rbtm.reconstruction.DataProcessing.ImgProcessor;
import lombok.Getter;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;


public class WedAppHelper {
    @Getter private List<String> objList;
    @Getter private String currentObject;
    @Getter private IMatDatasetObject datasetObj = null;
    @Getter private List<FilterEntity> filters = new ArrayList<>();
    @Getter private int currentId;
    @Getter private Mat currentImg;
    @Getter private CircleDetector currentDiagram;

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
        currentObject = "";
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

    private Mat getProcessImg(int id) {
        Mat img = datasetObj.getSlice(id);
        if (filters.isEmpty()) {
            System.out.println("Filters not found.");
            return img;
        }
        return ImgProcessor.process(img, filters);
    }

    public File getSlice(int id) {
        Mat img = (id == currentId)? currentImg : getProcessImg(id);
        String buffImgPath = Constants.TEMP_DIR_PATH + "/bufferImg." + Constants.PNG_FORMAT;
        Imgcodecs.imwrite(buffImgPath, img);
        System.out.println("Buff img updated: " + buffImgPath);

        if(id != currentId) {
            currentId = id;
            currentImg = img;
            currentDiagram = null;
        }

        return new File(buffImgPath);
    }

    public void setFiltersFromJson(String json) {
        System.out.println("body is: " + json);

        Gson gson = new Gson();
        FilterEntity[] filterEntities = gson.fromJson(json, FilterEntity[].class);
        System.out.println("Parsed body: " + Arrays.toString(filterEntities));

        this.filters = new ArrayList<>(Arrays.asList(filterEntities));
        this.currentId = -1;
        this.currentDiagram = null;
    }

    public List<CirleDiagramEntity> getCircleDiagram(int id) {
        if(currentDiagram != null) {
            return currentDiagram.getDiagram();
        }
        Mat img = (id == currentId)? currentImg : getProcessImg(id);

        CircleDetector cd = new CircleDetector(img);
        currentDiagram = cd;
        return currentDiagram.getDiagram();
    }

    public File getObjFile() throws IOException, InterruptedException {
        DatasetToMarshConverter d2mConverter = new DatasetToMarshConverter(datasetObj, Constants.TEMP_DIR_PATH + "/buffObjFile.obj", filters);
        return d2mConverter.convert(new float[]{3,3,3}, 100);

        //return  new File(Constants.TEMP_DIR_PATH + "/arrayDump.obj");

    }
}
