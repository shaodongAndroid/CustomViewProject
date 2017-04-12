package com.custom.view.project.pattern.strategy;

import android.util.Log;

/**
 * Created by dsd on 2017/3/28.
 */

public abstract class Duck {
    private static final String TAG = "info" ;

    FlyBehavior flyBehavior ;
    QuackBehavior quackBehavior ;

    public Duck(){
    }

    public void swim(){
        Log.d(TAG, "swim: I CAN SWIM");
    }

    public abstract void display();

    public void preformQuack(){
        quackBehavior.quack();
    }

    public void preformFly(){
        flyBehavior.fly();
    }


    /**
     * 动态改变算法族
     * @param fly
     */
    public void setFlyBehavior(FlyBehavior fly){
        this.flyBehavior = fly ;
    }
}
