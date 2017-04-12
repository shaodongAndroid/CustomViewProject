package com.custom.view.project.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.custom.view.project.R;
import com.custom.view.project.common.Event;
import com.custom.view.project.util.FileUtils;
import com.custom.view.project.view.MosaicView;
import com.intsig.scanner.ScannerSDK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;

import static com.custom.view.project.R.id.iv_crop;
import static com.custom.view.project.R.id.iv_paint;

public class MosaicActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "info";

    public static final int TYPE_PICK = 1 ;
    public static final int TYPE_CAPTURE = 2 ;


    private static final int GET_IMAGE_FROM_LOCAL = 100;
    private static final int GET_IMAGE_BY_CAPTURE = 101;

    private enum EditStatus {
        CROP,MOSAIC
    }

    LinearLayout llBrushSizeContainer ;

    ImageView ivCrop ;

    ImageView ivPaint ;

    MosaicView mMosaicView ;

    private ScannerSDK mScannerSDK ;
    private int mType = TYPE_PICK;

    private EditStatus mEditStatus ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosaic);
        ButterKnife.bind(this);
        try {
            mScannerSDK = new ScannerSDK(this);
        } catch (ScannerSDK.IllegalAppException e) {
            Log.d(TAG, "onCreate: -----------IllegalAppException = "+e.getMessage());
        }

        initView();
        initData();
    }

    private void initView() {

        llBrushSizeContainer = (LinearLayout) findViewById(R.id.ll_paint_container);

        mMosaicView = (MosaicView) findViewById(R.id.mosaic_View);
        ivCrop = (ImageView) findViewById(iv_crop);
        ivCrop.setOnClickListener(this);
        ivPaint = (ImageView) findViewById(iv_paint);
        ivPaint.setOnClickListener(this);
        findViewById(R.id.tv_clear_paint).setOnClickListener(this);
        findViewById(R.id.tv_use_original_image).setOnClickListener(this);
        findViewById(R.id.iv_left_rotate).setOnClickListener(this);
        findViewById(R.id.iv_right_rotate).setOnClickListener(this);
    }

    private void initData() {

        mEditStatus = EditStatus.CROP;

        File file = new File(Event.TEMP_PATH) ;
        if(!file.exists()){
            file.mkdirs();
        }
        getImageFromLocal();
    }

    private void getImageFromLocal() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GET_IMAGE_FROM_LOCAL);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case iv_crop: // 裁剪
                llBrushSizeContainer.setVisibility(View.GONE);
                ivPaint.setImageResource(R.drawable.btn_mosaic_normal);
                ivCrop.setImageResource(R.drawable.btn_crop_pressed);
                mEditStatus = EditStatus.CROP;
                mMosaicView.setMode(MosaicView.Mode.NORMAL);
                break;
            case R.id.iv_paint: // 画笔

                if(mEditStatus == EditStatus.MOSAIC){
                    mMosaicView.setMode(MosaicView.Mode.PATH);
                    return;
                }

                if(mEditStatus == EditStatus.CROP){
                    llBrushSizeContainer.setVisibility(View.VISIBLE);
                    ivCrop.setImageResource(R.drawable.btn_crop_normal);
                    ivPaint.setImageResource(R.drawable.btn_mosaic_pressed);

                    mEditStatus = EditStatus.MOSAIC ;
                    mMosaicView.setMode(MosaicView.Mode.PATH);

                    if(mMosaicView.saveWithPath(Event.TEMP_PATH + "temp_test.jpg", false)){
//                        mMosaicView.setCropName(mCropedFileName);
                        Log.d(TAG, "onClick: ----------true");
                        mMosaicView.setCropImagePath(mType, Event.TEMP_PATH + "temp_test.jpg");
                    } else {
                        Log.d(TAG, "onClick: ----------false");
                    }
                }

                break;
            case R.id.tv_clear_paint: // 清除
                mMosaicView.clearPath();
                break;
            case R.id.tv_use_original_image: // 使用原图
//                mMosaicView.setCropImagePath(mType, Event.TEMP_PATH + "test3.jpg");
            case R.id.tv_back:
                finish();
                break;
            case R.id.iv_left_rotate:
                mMosaicView.rotateLeftBitmap();
                break;
            case R.id.iv_right_rotate:
                mMosaicView.rotateRightBitmap();
                break;
            case R.id.tv_save:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK){
            return ;
        }

        if(requestCode == GET_IMAGE_FROM_LOCAL){
            Uri uri = data.getData();
            Log.d(TAG, "onActivityResult: uri.getPath() = "+uri.getPath());
            Log.d(TAG, "onActivityResult: uri.getScheme() = "+uri.getScheme());

            if(uri == null){
                return ;
            }

            String imagePath = null;
            if(uri.getScheme().compareTo("content") == 0){
                ContentResolver cr = getContentResolver();
                String[] projection = { MediaStore.Images.Media.DATA };
                //
                Cursor cursor1 = managedQuery(uri, projection, null, null, null);
                if(cursor1 != null){
                    int index = cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    Log.d(TAG, "onActivityResult: cursor1.getString(index) = "+cursor1.getString(index));
                }
                cursor1.close();
                //4.2.2之后
                Cursor cursor = cr.query(uri, projection, null, null, null);
                if(cursor != null){
                    while(cursor.moveToFirst()){
                        int imageIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        imagePath = cursor.getString(imageIndex);
                        Log.d(TAG, "onActivityResult: imagePath = "+imagePath);
                    }
                }
                cursor.close();
            } else if(uri.getScheme().compareTo("file") == 0){
                imagePath = uri.getPath();
            }

            if(!TextUtils.isEmpty(imagePath)){
                long time = System.currentTimeMillis();
                mosaicImage(imagePath);
                Log.d(TAG, "onActivityResult: time1 = "+(System.currentTimeMillis() - time));
                FileUtils.copyFile(imagePath, Event.TEMP_PATH+"test1.jpg");
                Log.d(TAG, "onActivityResult: time2 = "+(System.currentTimeMillis() - time));

                mMosaicView.setImagePath(Event.TEMP_PATH + "test3.jpg", mType, true);
                mMosaicView.doPretreatment(mScannerSDK);
                mMosaicView.reFreshView();
            }

            return ;
        }
    }

    private void mosaicImage(String imagePath) {
        File file = new File(imagePath);
        if(!file.exists()){
            return ;
        }
        Log.d(TAG, "decodeImagePath: imagePath = "+imagePath);
        Uri uri = Uri.fromFile(file);
        Bitmap bitmap = decodeImagePath(uri);
        if(bitmap != null){
            Log.d(TAG, "mosaicImage: --------bitmap != null");
            try {
                FileOutputStream outputStream = new FileOutputStream(Event.TEMP_PATH + "test3.jpg");
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
                bitmap.recycle();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "mosaicImage: --------bitmap == null");
        }
    }

    private Bitmap decodeImagePath(Uri uri) {
        Bitmap bitmap ;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true ;
//        BitmapFactory.decodeFile(imagePath, options);
        BitmapFactory.decodeFile(uri.getPath(), options);

        Log.d(TAG, "decodeImagePath: getWidth() = "+options.outWidth);
        Log.d(TAG, "decodeImagePath: getHeight() = "+options.outHeight);

        options.inJustDecodeBounds = false ;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true ;
        options.inSampleSize = 2 ;

        Log.d(TAG, "decodeImagePath: uri = "+uri.toString());
        try {
           bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null ;
        } catch (OutOfMemoryError e){
            e.printStackTrace();
            return null;
        }

        return bitmap ;
    }

    @Override
    protected void onDestroy() {
        FileUtils.deleteAllFileForDir(new File(Event.TEMP_PATH));
        super.onDestroy();
    }
}
