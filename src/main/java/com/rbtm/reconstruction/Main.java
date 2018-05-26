package com.rbtm.reconstruction;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import javax.imageio.ImageIO;
import java.io.File;
import java.io.OutputStream;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        // Configure Spark
        /*
        port(4567);
        staticFiles.location("/public");
        staticFiles.expireTime(600L);
        */
        ObjectController wah = new ObjectController();

        path("/objects", () -> {
            get("/all", (req, res) ->
                    wah.getObjList());

            path("/current", () -> {

                get("", (req, res) ->
                        wah.getCurrentObject());

                post("", (req, res) -> {
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

                get("/shape", (req, res) -> {
                    if(!wah.isInit()) {
                        return "Fail. Firstly please use */objects/init path";
                    }

                    return wah.getDatasetObj().getShape();
                });

                path("/slice", () -> {
                    get("/:id", (req, res) -> {
                        if(!wah.isInit()) {
                            return "Fail. Firstly please use /objects/init path";
                        }

                        File imgFile = wah.getDatasetObj().getSliceAsFile(Integer.parseInt(req.params(":id")));

                        res.raw().setContentType("image/"+ Constants.PNG_FORMAT);

                        try (OutputStream out = res.raw().getOutputStream()) {
                            ImageIO.write(ImageIO.read(imgFile), Constants.PNG_FORMAT, out);
                            return out;
                        }
                    });
                });
            });
        });
    }
}
