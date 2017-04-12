package com.custom.view.project.pattern.strategy;

import android.util.Log;

import static com.custom.view.project.common.Event.TAG;

/**
 * Created by dsd on 2017/3/28.
 */

public class FlyNoWay implements FlyBehavior {
    @Override public void fly() {
        Log.d(TAG, "fly: I can not fly");
    }
}
