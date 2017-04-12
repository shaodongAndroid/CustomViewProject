package com.custom.view.project.common;

import android.app.Dialog;
import android.content.Context;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by 少东 on 2017/3/7.
 */

public abstract class RxObserver<T> implements Observer<T> {
    private Context mContext ;
    private String msg ;

    private Dialog mDialog ;

    public RxObserver(Context context, String msg){
        this.mContext = context ;
        this.msg = msg ;
    }

    public RxObserver(Context context){
        this.mContext = context ;
        this.msg = "请稍后...";
    }

    @Override
    public void onSubscribe(Disposable d) {
        // 显示dialog
    }

    @Override
    public void onNext(T value) {
        onNexted(value);
    }

    @Override
    public void onError(Throwable e) {
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        onError(e.getMessage());
    }

    @Override
    public void onComplete() {
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    public abstract void onNexted(T t);
    public abstract void onError(String message);
}
