package com.custom.view.project.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by 少东 on 2017/2/14.
 */

public class StorageUtil {

    /**
     * 检测外部存储是否可用
     * @return
     */
    public static boolean isExternalStorageAvailable(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return true ;
        } else {
            return false ;
        }
    }

    /**
     * 获取外部存储目录
     * @return
     */
    public static String getSDPath(){

        File sdDir ;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        } else {
            sdDir = null ;
        }

        return sdDir.toString() ;
    }

}
