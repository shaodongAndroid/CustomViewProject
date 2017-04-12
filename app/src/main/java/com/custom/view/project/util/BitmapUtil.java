package com.custom.view.project.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.graphics.BitmapFactory.decodeFile;

/**
 * Created by 少东 on 2016/11/15.
 */

public class BitmapUtil {

    public static int readPictureDegree(String path){

        int degree = 0 ;

        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return degree ;
    }

    /**
     * drawable 转换成 Bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable){
        Bitmap bitmap ;

        if(drawable instanceof BitmapDrawable){
            bitmap = ((BitmapDrawable) drawable).getBitmap();
            if(bitmap != null)
                return bitmap ;
        }

        int width = drawable.getIntrinsicWidth() ;
        int height = drawable.getIntrinsicHeight() ;

        if(width < 0 || height < 0){
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap ;
    }

    private static final int TIME_OUT = 10000 ;

    public static Bitmap loadBitmapFromNetwork(String imageUrl, String imagePath){
        Bitmap bitmap = null ;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIME_OUT);
            conn.setReadTimeout(TIME_OUT);
            conn.setRequestMethod("GET");

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream is = conn.getInputStream();
//                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                byte[] buffer = new byte[10240];
//                int length ;
//                while ((length = is.read(buffer)) > -1){
//                    outputStream.write(buffer, 0, buffer.length);
//                }

                File file = new File(imagePath);
                if(file.exists()){
                    file.delete();
                }
                file.createNewFile();

                // 为了防止文件过大导致OOM， 这里直接把数据下载到文件中
                FileOutputStream fos = new FileOutputStream(file);
                /**
                 * 内存只是不够用并不是不能用了，我们能不能分出5M的内存，先把数据下载到这快内存里面，当这块内存装满时，把这5M的数据拿出来保存到文件里，当然可以，这就是 BufferedOutputStream
                 * 那这个5M怎么来的呢？这个大小取决与你要加载的图片大小分布，目的就是为了在内存允许的情况下让大部分的图片只需要一次写操作就能搞定，
                 * 尽可能的减小由于写操作带来的速度影响。 例如，你的APP加载的图片大部分是1M以内，只有极少数大于1M，那这个大小就是1M。
                 */
                BufferedOutputStream bos = new BufferedOutputStream(fos, 5 * 1024 * 1024);

                byte[] buffer = new byte[10240]; // download 10kb every time
                int length = 0;
                while((length = is.read(buffer)) > -1){
                    // 没有OOM 但是下载耗时了很多，原因是每次下载10kb数据就需要写一次文件，IO操作相比内存操作是很耗时的。动作太慢没感觉
//                    fos.write(buffer, 0 ,buffer.length);
                    bos.write(buffer, 0, buffer.length);
                }
                bos.flush();
                bos.close();
                fos.close();
//                outputStream.flush();
                is.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }

    public static Bitmap getBitmapFromFile(String filePath, int reqWidth, int reqHeight){
        Bitmap bitmap ;
        File file = new File(filePath);
        if(!file.exists()){
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true ;
        decodeFile(file.getName(), options);
        options.inSampleSize = calculateScaleSize(options.outWidth, options.outHeight, reqWidth, reqHeight );

        options.inJustDecodeBounds = true ;
        bitmap = BitmapFactory.decodeFile(file.getName(), options);
//        options.inPreferredConfig = Bitmap.Config.RGB_565 ;

        return bitmap ;
    }

    private static int calculateScaleSize(int outWidth, int outHeight, int reqWidth, int reqHeight) {
        /**
         * 假如图片原始大小480x800，你要显示的大小是250x420，你得到的 inSampleSize 将会是 1，因为 int/int 是取整的，不会对图片做缩放，这可能就是OOM的隐患。
         */
        int scaleWidth = (int) Math.ceil(outWidth * 1.0f / reqWidth);
        int scaleHeight = (int) Math.ceil(outHeight * 1.0f / reqHeight);

        int inSampleSize = Math.max(scaleWidth, scaleHeight);

        return inSampleSize ;
    }

}
