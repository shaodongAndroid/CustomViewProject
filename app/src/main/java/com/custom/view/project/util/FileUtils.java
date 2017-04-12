package com.custom.view.project.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 少东 on 2017/2/14.
 */

public class FileUtils {

    /**
     * 复制单个文件
     * @param sourcePath
     * @param newPath
     * @return
     */
    public static boolean copyFile(String sourcePath, String newPath){
        File sPath = new File(sourcePath);
        if(!sPath.exists()){
            Log.i("----------FileUtils 路径不存在");
            return false;
        }

        deleteFile(newPath);

        try {
            FileInputStream inputStream = new FileInputStream(sPath);
            FileOutputStream outputStream = new FileOutputStream(newPath);
            byte[] buffer = new byte[1024];

            while (inputStream.read(buffer) != -1){
                outputStream.write(buffer, 0, buffer.length);
            }

            inputStream.close();
            outputStream.close();

            return true ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false ;
    }

    public static boolean deleteFile(String path){
        File file = new File(path);
        if(!file.exists()){
            Log.w("the file is not exist while delete the file");
            return false;
        }
        if (!file.canRead() || !file.canWrite()) {
            Log.w("has no permission to can or write while delete the file");
            return false;
        }
        if (!file.isFile()) {
            Log.w("the file does not represents a file on the underlying file system");
            return false;
        }

        boolean success = file.delete() ;
        return success ;
    }

    /**
     * 删除文件夹下的所有的文件
     * @return
     */
    public static void deleteAllFileForDir(File file){
        if(file.exists()){
            if(file.isFile()){
                file.delete();
            } else if(file.isDirectory()){
                File files[] = file.listFiles();
                for(File file1 : files){
                    deleteAllFileForDir(file1);
                }
            }
        }
    }

}
