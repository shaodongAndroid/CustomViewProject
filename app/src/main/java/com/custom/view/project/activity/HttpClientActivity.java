package com.custom.view.project.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.custom.view.project.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClientActivity extends AppCompatActivity {

    private static final String TAG = "info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_client);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_get_data:
                getNetworkData();
                break;
            case R.id.btn_upload_file:
                uploadFile();
                break;
            case R.id.btn_cache_data:
                cacheData();
                break;
        }
    }

    private void cacheData() {
        File sDir = Environment.getExternalStorageDirectory();

        String dir = sDir.toString() + File.separator + "okHttpClient" + File.separator + "cache" + File.separator;

        File file = new File(dir);
        if(!file.exists()){
            file.mkdirs();
        }

        int cacheSize = 10 * 1024 * 1024 ;
        Cache cache = new Cache(file, cacheSize);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.cache(cache);
        OkHttpClient httpClient = builder.build();

/*        httpClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS);
        httpClient.newBuilder().readTimeout(10, TimeUnit.SECONDS);
        httpClient.newBuilder().writeTimeout(10, TimeUnit.SECONDS);*/
        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("info", "----------- result = "+response.body().toString());
            }
        });
    }

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    private void uploadFile() {
        OkHttpClient mHttpClient = new OkHttpClient();
        File file = new File("/sdcard/mnt/demo.txt");

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, file);
        Request request = new Request.Builder().url("http//www.baidu.com").post(requestBody).build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    private void getNetworkData() {
        OkHttpClient httpClient = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("name", "liming")
                .add("school", "beida")
                .build();
        Request request = new Request.Builder()
                .url("http://write.blog.csdn.net/postlist/0/0/enabled/1")
                .post(requestBody)
                .build();

//        Response response = null ;
//        try {
//            同步执行（需要起线程）
//            response = httpClient.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d(TAG, "onResponse: result = "+result);
            }
        });
    }
}
