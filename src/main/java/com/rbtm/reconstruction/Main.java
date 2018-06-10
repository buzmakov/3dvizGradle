package com.rbtm.reconstruction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbtm.reconstruction.Utils.Filters;
import com.rbtm.reconstruction.Utils.JsonUtil;
import org.apache.commons.io.FileUtils;


import javax.imageio.ImageIO;
import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        // Configure Spark
        System.out.println("aaa");
        staticFiles.location("/public");
        staticFiles.expireTime(600L);

        WedAppHelper wah = new WedAppHelper();
        AtomicInteger numOfRunningReq = new AtomicInteger();


        //before("*", Filters.addTrailingSlashes);


        path("/objects/", () -> {
            get("/all/", (req, res) -> {
                res.header("Access-Control-Allow-Origin", "*");
                return  JsonUtil.dataToJson(wah.getObjList());
            });

            path("/current/", () -> {

                get("", (req, res) -> {
                    res.header("Access-Control-Allow-Origin", "*");
                    return wah.getCurrentObject();
                });

                post("", (req, res) -> {
                    res.header("Access-Control-Allow-Origin", "*");

                    System.out.println("request body is: " + req.body());

                    HashMap<String,Object> result =
                            new ObjectMapper().readValue(req.body(), HashMap.class);
                    String requestObj = result.get("objName").toString();
                    System.out.println(requestObj);
                    wah.setCurrentObject(requestObj);

                    return "Success";
                });

                post("/forceInit/", (req, res) -> {
                    res.header("Access-Control-Allow-Origin", "*");

                    if(wah.forceConvert()) {
                        return "Success";
                    } else {
                        return "Fail";
                    }
                });

                get("/shape/", (req, res) -> {
                    res.header("Access-Control-Allow-Origin", "*");

                    if(wah.isInit()) {
                        return JsonUtil.dataToJson(wah.getDatasetObj().getShape());
                    }

                    return "Fail";
                });

                path("/slice/", () -> {
                    post("/filters/", (req, res) -> {
                        while(numOfRunningReq.get() >= Constants.MAX_NUM_OF_REQUESTS){
                            System.out.println("Too much requests. Sleep 1sec ...");
                            Thread.sleep(1000);
                        }
                        numOfRunningReq.incrementAndGet();
                        res.header("Access-Control-Allow-Origin", "*");
                        numOfRunningReq.decrementAndGet();
                        wah.setFiltersFromJson(req.body());
                        return res;
                    });

                    get("/:id/circleDiagram/", (req, res) -> {
                        while(numOfRunningReq.get() >= Constants.MAX_NUM_OF_REQUESTS){
                            System.out.println("Too much requests. Sleep 1sec ...");
                            Thread.sleep(1000);
                        }
                        numOfRunningReq.incrementAndGet();
                        res.header("Access-Control-Allow-Origin", "*");
                        numOfRunningReq.decrementAndGet();
                        return JsonUtil.dataToJson(wah.getCircleDiagram(Integer.parseInt(req.params(":id"))));
                    });

                    get("/:id/", (req, res) -> {
                        if(wah.isInit()) {
                            File imgFile = wah.getSlice(Integer.parseInt(req.params(":id")));

                            res.raw().setContentType("image/"+ Constants.PNG_FORMAT);
                            res.header("Access-Control-Allow-Origin", "*");
                            try (OutputStream out = res.raw().getOutputStream()) {
                                ImageIO.write(ImageIO.read(imgFile), Constants.PNG_FORMAT, out);
                                return out;
                            }
                        }

                        return "Fail";
                    });
                });


                get("/objFile/", (req, res) -> {
                    res.header("Access-Control-Allow-Origin", "*");

                    if(wah.isInit()) {
                        File objFile = wah.getObjFile();

                        res.raw().setContentType("text/plain");
                        res.header("Access-Control-Allow-Origin", "*");
                        String out = FileUtils.readFileToString(objFile);
                        return out;
                    }

                    return "Fail";

                });

            });
        });

        after("*", Filters.addGzipHeader);
    }
}
