package com.custom.view.project.mvp;

/**
 * Created by 少东 on 2016/12/11.
 */

public abstract class BasePresenter<T> {

    public T mView ;

    public void attach(T view){
        this.mView = view ;
    }

    public void dettach(){
        this.mView = null ;
    }
}
