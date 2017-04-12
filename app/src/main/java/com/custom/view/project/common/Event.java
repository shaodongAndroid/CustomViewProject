package com.custom.view.project.common;


import com.custom.view.project.util.StorageUtil;

import java.io.File;

/**
 * Created by 少东 on 2017/2/14.
 */

public class Event {

    public static final String TAG = "info";

    private static String CACHE_NAME = "CustomViewProject";
    public static String IMAGE_PATH = StorageUtil.getSDPath() + File.separator + CACHE_NAME
            + File.separator + "imageCache" + File.separator ;

    public static String TEMP_PATH = StorageUtil.getSDPath() + File.separator + CACHE_NAME
            + File.separator + "temp" + File.separator ;

}
