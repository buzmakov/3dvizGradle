package com.rbtm.reconstruction;

import org.opencv.core.CvType;

import java.util.HashMap;

public final class Constants {

    //get num of processors in system need for concurency
    public static final int THREAD_NUM = Runtime.getRuntime().availableProcessors(); // количество используемых процессов, по умолчанию используются все


    public static final double NANO_IN_SEC = 1000000000.0;
    public static final String PNG_FORMAT = "png";

    //properties for h5 dataset object
    public static final int RESIZE_STEP = 1; //во сколько раз делать разряжение. После переноса в фронтенд не используется
    public static final int NUM_OF_BLOCKS = 4; //количество блоков, на которые разбивать исходный датасет
    public static final int DEFAULT_MAT_TYPE = CvType.CV_32F; //тип Mat, который используется во всем изображении
    public static final String H5_OBJECT = "Results"; //путь внутри h5 архива, где лежит объект

    public static final String OBJ_PATH = "/home/alop0715/diplom/h5_samples"; //директория, где хранятся h5 архивы
    public static final String TEMP_IMG_PATH = "/home/alop0715/diplom/tool_result"; //буфер, где хранятся изображения датасета
    public static final String TEMP_DIR_PATH = "/home/alop0715/diplom/tmp"; // директория, где хранятся технические файлы, созданые во время исполнения скрипта

    //some operations like
    public static final int MAX_NUM_OF_REQUESTS = 4; //количиство одновременно обрабатываемых запросов


}
