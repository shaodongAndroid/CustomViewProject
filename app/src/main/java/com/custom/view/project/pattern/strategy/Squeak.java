package com.custom.view.project.pattern.strategy;

import android.util.Log;
import com.custom.view.project.common.Event;

/**
 * Created by Administrator on 2017/3/28.
 */

public class Squeak implements QuackBehavior {
    @Override public void quack() {
        Log.d(Event.TAG, "quack: 吱吱叫");
    }
}
