package com.rbtm.reconstruction;

import org.opencv.core.CvType;

public final class Constants {
    public static final String H5_OBJECT = "Results";
    public static final int THREAD_NUM = Runtime.getRuntime().availableProcessors();
    public static final double NANO_IN_SEC = 1000000000.0;
    public static final String PNG_FORMAT = "png";
    public static final int NUM_OF_BLOCKS = 4;
    public static final int DEFAULT_MAT_TYPE = CvType.CV_32F;
}
