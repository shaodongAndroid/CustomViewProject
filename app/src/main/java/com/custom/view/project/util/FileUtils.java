package com.custom.view.project.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
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

    static String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }

    static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String realPath = cursor.getString(index);
            cursor.close();
            return realPath;
        }
    }

    static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
