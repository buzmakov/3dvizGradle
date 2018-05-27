package com.rbtm.reconstruction.Utils;

import spark.Filter;
import spark.Request;
import spark.Response;


public class Filters {
    public static Filter addTrailingSlashes = (Request request, Response response) -> {
        if (!request.pathInfo().endsWith("/")) {
            response.redirect(request.pathInfo() + "/");
        }
    };

    public static Filter addGzipHeader = (Request request, Response response) -> {
        response.header("Content-Encoding", "gzip");
    };

    public static Filter addAccessControlHeader = (Request request, Response response) -> {
        response.header("Access-Control-Allow-Origin", "*");
    };

}
