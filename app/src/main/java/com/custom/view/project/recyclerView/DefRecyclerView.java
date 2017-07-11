package com.custom.view.project.recyclerView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by dsd on 2017/6/30.
 */

public class DefRecyclerView extends RecyclerView {

    public DefRecyclerView(Context context) {
        super(context);

    }


    public DefRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }


    @Override public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }
}
