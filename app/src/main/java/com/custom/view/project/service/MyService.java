package com.custom.view.project.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

/**
 * android:process=":remote" --> 普通的Service转换成远程Service
 * 使用了远程Service后，MyService已经在另外一个进程当中运行了，所以只会阻塞该进程中的主线程，并不会影响到当前的应用程序。
 *
 */

public class MyService extends Service {

    private static final String TAG = "info";

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "------------onCreate()");
        Log.d(TAG, "-----------service-Process.myPid() = " + Process.myPid());
        // 如果没有配置 android:process=":remote" 会导致程序 无响应 ANR的提示框
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }


    /**
     * onStartCommand使用时，返回的是一个(int)整形。 这个整形可以有四个返回值：
     * start_sticky、start_no_sticky、START_REDELIVER_INTENT、START_STICKY_COMPATIBILITY。
     * 它们的含义分别是：
     * 1):START_STICKY： 如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。
     * 随后系统会尝试重新创建service，由 于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。
     * 如果在此期间没有任何启动命令被传 递到service，那么参数Intent将为null。
     * 2):START_NOT_STICKY：“非粘性的”。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务
     * 3):START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，
     * 系统会自动重启该服务，并将Intent的值传入。
     * 4):START_STICKY_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "-------------onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
//        return new MyBinder();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "-------------onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "-----------------onRebind()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "-------------onDestroy()");
    }

    public void loadWay(){
        Log.d(TAG, "------------调用service中方法");
    }

    private boolean isFlag ;

    MyAidlInterface.Stub mBinder = new MyAidlInterface.Stub() {
        @Override
        public int plus(int a, int b) throws RemoteException {
            return a + b;
        }

        @Override
        public String toUpperCase(String str) throws RemoteException {
            if(!TextUtils.isEmpty(str)){
                return str.toUpperCase() ;
            }
            return null;
        }
    };

//    class MyBinder extends Binder {
//
//        public void startDownLoad(){
//            Log.d("info","----------isFlag = "+ isFlag);
//        }
//
//        public void loadOtherWay(){
//            loadWay();
//        }
//    }

}
