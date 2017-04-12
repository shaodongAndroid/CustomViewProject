package com.custom.view.project.pattern.strategy;

import android.util.Log;

import static com.custom.view.project.common.Event.TAG;

/**
 * Created by Administrator on 2017/3/28.
 */

public class FlyWithWings implements FlyBehavior {
    @Override public void fly() {
        Log.d(TAG, "fly: I can fly with wings");
    }
}
