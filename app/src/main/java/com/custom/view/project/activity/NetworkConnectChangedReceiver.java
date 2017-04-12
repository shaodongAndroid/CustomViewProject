package com.custom.view.project.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by 少东 on 2017/1/13.
 */

public class NetworkConnectChangedReceiver extends BroadcastReceiver {

    private static final String TAG = "info";

    @Override
    public void onReceive(Context context, Intent intent) {
        // 监听wifi的打开与关闭
        if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            Log.d(TAG, "onReceive: wifiState = "+wifiState);
            switch (wifiState){
                case WifiManager.WIFI_STATE_DISABLED: // 未链接
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLED: // 链接成功
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    break;
            }
        }
    }
}
