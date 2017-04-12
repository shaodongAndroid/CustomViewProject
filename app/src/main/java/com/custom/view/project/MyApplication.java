package com.custom.view.project;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by ${dsd} on 2017/3/28.
 */

public class MyApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
        if(LeakCanary.isInAnalyzerProcess(this)){
            return ;
        }

        LeakCanary.install(this);
    }
}
