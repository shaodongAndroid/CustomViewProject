package com.custom.view.project.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.custom.view.project.R;

/**
 * 如果既有startService 也有 bindService 销毁时 先解除绑定unBindService(connection)(此时，不走onDestroy())，
 * 在stopService()-->onDestroy()
 * BIND_AUTO_CREATE 在Activity和Service建立关联后自动创建Service 此时走onCreate()方法， 不走但onStartCommand()方法
 *
 */
public class ServiceActivity extends AppCompatActivity {

    private static final String TAG = "info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sercice);
        Log.d(TAG, "-----------activity-Process.myPid() = " + Process.myPid());
    }

    public void onClick(View view){
        Intent intent ;
        switch (view.getId()){
            case R.id.btn_start:
                intent = new Intent(this, MyService.class);
                startService(intent);
                break;
            case R.id.btn_stop:
                intent = new Intent(this, MyService.class);
                stopService(intent) ;
                break;
            case R.id.btn_bind:
                try{
                    intent = new Intent(this, MyService.class);
                    // BIND_AUTO_CREATE表示在Activity和Service建立关联后自动创建Service，这会使得MyService中的onCreate()方法得到执行，但onStartCommand()方法不会执行
                    bindService(intent,connection,BIND_AUTO_CREATE);
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.btn_unBind:
                // 解除绑定后 service 执行 onUnbind() --> onDestroy()
                unbindService(connection);
                break;
            case R.id.btn_start_intent_service:
                /**
                 * 不需要使用ServiceConnection BIND_AUTO_CREATE 不在走onStartCommend() 方法，
                 *
                 * why? --> 看IntentService的源码
                 */
                intent = new Intent(this, TestIntentService.class);
                // bindService(intent, connection, BIND_AUTO_CREATE);
                startService(intent);
                break;
            case R.id.btn_stop_intent_service:
                intent = new Intent(this, TestIntentService.class);
                stopService(intent);
                break;
        }
    }

    /**
     * （android:process=":remote" ）使用 AIDL的方式进行bind
     */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyAidlInterface mBinder = MyAidlInterface.Stub.asInterface(service);
            try{
                String upperCase = mBinder.toUpperCase("abcdefgh");
                Log.d(TAG, "onServiceConnected: upperCase = "+upperCase);
                int sum = mBinder.plus(5, 10) ;
                Log.d(TAG, "onServiceConnected: sum = "+sum);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    /**
     * 不使用android:process=":remote" service 的属性，可以直接进行绑定，加上该属性，不能直接进行绑定 （可以使用AIDL的方式进行绑定，但是有点大才小用）
     */
//    private ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            Log.d(TAG, "onServiceConnected: 成功链接");
//            MyService.MyBinder myBinder = (MyService.MyBinder) service;
//            myBinder.startDownLoad();
//            myBinder.loadOtherWay();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Log.d(TAG, "onServiceConnected: 断开链接");
//        }
//    };
}
