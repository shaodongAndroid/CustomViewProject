package com.custom.view.project.pattern.strategy;

import android.util.Log;

import static com.custom.view.project.common.Event.TAG;

/**
 * Created by Administrator on 2017/3/28.
 */

public class Quack implements QuackBehavior {
    @Override public void quack() {
        Log.d(TAG, "quack: 呱呱叫");
    }
}
