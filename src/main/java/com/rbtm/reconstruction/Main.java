package com.rbtm.reconstruction;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rbtm.reconstruction.Converters.H5ToImgsConverter;
import com.rbtm.reconstruction.DataObjects.IMatDatasetObject;

import java.util.concurrent.atomic.AtomicReference;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        WebAppHealper wah = new WebAppHealper();

        path("/objects", () -> {
            get("/all", (req, res) ->
                    wah.getObjList());

            get("/current", (req, res) ->
                    wah.getCurrentObject());

            post("/current", (req, res) -> {
                JsonElement root = new JsonParser().parse(req.body());
                String newObjName = root.getAsJsonObject().get("objName").toString();
                wah.setCurrentObject(newObjName);
                return "Success";
            });

            post("/init", (req, res) -> {
                if(wah.convert()) {
                    return "Success";
                } else {
                    return "Fail";
                }
            });

            post("/forceInit", (req, res) -> {
                if(wah.forceConvert()) {
                    return "Success";
                } else {
                    return "Fail";
                }
            });

            get("/current/shape", (req, res) -> {
                if(!wah.isInit()) {
                    return "Fail. Firstly please use */objects/init path";
                }

                return wah.getDatasetObj().getShape();
            });
        });


        /*
        path("/slice", () -> {
            get("/:id", (req, res) -> {
                if(datasetObject == null) {
                    return "Fail. Firstly please use /objects/init path";
                }

                res.
            });
        });
        */



    }
}
