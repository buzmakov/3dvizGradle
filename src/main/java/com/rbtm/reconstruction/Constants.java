package com.rbtm.reconstruction;

import org.opencv.core.CvType;

import java.util.HashMap;

public final class Constants {

    //get num of processors in system need for concurency
    public static final int THREAD_NUM = Runtime.getRuntime().availableProcessors();


    public static final double NANO_IN_SEC = 1000000000.0;
    public static final String PNG_FORMAT = "png";

    //properties for h5 dataset object
    public static final int RESIZE_STEP = 1;
    public static final int NUM_OF_BLOCKS = 1;
    public static final int DEFAULT_MAT_TYPE = CvType.CV_32F;
    public static final String H5_OBJECT = "Results";
    public static final String H5_OBJECT_2 = "Result";

    public static final String OBJ_PATH = "/home/alop0715/diplom/h5_samples";
    public static final String TEMP_IMG_PATH = "/home/alop0715/diplom/tool_result";
    public static final String TEMP_DIR_PATH = "/home/alop0715/diplom/tmp";

}
