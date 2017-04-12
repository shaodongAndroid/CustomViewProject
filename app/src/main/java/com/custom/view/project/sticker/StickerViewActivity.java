package com.custom.view.project.sticker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.custom.view.project.R;

import java.io.File;

public class StickerViewActivity extends AppCompatActivity {

    private StickerView mStickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_view);

        mStickerView = (StickerView) findViewById(R.id.sticker_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mStickerView.setBackgroundColor(Color.WHITE);
        mStickerView.setLooked(false);

        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            toolbar.inflateMenu(R.menu.menu_save);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.item_save) {
                        File file = FileUtil.getNewFile(StickerViewActivity.this, "Sticker");
                        if (file != null) {
                            mStickerView.save(file);
                            Toast.makeText(StickerViewActivity.this, "saved in " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(StickerViewActivity.this, "the file is null", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return false;
                }
            });
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
        } else {
            loadData(R.drawable.help_main_page, R.drawable.help_my, R.drawable.help_my_analyse);
        }
    }

    private void loadData(int id1, int id2, int id3) {
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.haizewang_2);
//        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.haizewang_23);
//        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.haizewang_150);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id1);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), id2);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), id3);
//        mStickerView.addSticker(bitmap);
//        mStickerView.addSticker(bitmap1);
//        mStickerView.addSticker(bitmap2);
        mStickerView.addSticker(new DialogDrawable());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 110
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadData(R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3);
        }
    }
}
