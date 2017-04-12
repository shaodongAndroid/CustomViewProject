package com.custom.view.project.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by 少东 on 2016/12/9.
 */

/**
 * IntentService 用来处理异步请求，该Service会在需要的时候创建，当完成所有的任务以后自己关闭，且请求是在工作线程处理的。
 *
 * IntentService最起码有两个好处，一方面不需要自己去new Thread了；另一方面不需要考虑在什么时候关闭该Service了。
 *
 */
public class TestIntentService extends IntentService {
    private static final String TAG = "info" ;
    public TestIntentService() {
        super("TestIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "----------------onCreate()");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "----------------onHandleIntent()");
        if(intent != null){

        }
        handlerImage();
    }

    private void handlerImage() {
        try{
            Thread.sleep(10000);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "-----------------onStart()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "--------------------onStartCommand() ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "-----------------onDestroy()");
    }
}
