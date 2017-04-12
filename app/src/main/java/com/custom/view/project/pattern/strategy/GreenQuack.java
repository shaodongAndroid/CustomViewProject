package com.custom.view.project.pattern.strategy;

import com.custom.view.project.util.Log;

/**
 * Created by dsd on 2017/3/28.
 */

public class GreenQuack extends Duck {

    public GreenQuack(){
        flyBehavior = new FlyWithWings();
        quackBehavior = new Quack();
    }

    @Override public void display() {
        Log.d("---------display = 绿色鸭子");
    }
}
