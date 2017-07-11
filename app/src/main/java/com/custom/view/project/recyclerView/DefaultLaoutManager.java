package com.custom.view.project.recyclerView;

import android.graphics.PointF;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/**
 * Created by dsd on 2017/6/29.
 */

public class DefaultLaoutManager extends RecyclerView.LayoutManager  implements
    ItemTouchHelper.ViewDropHandler, RecyclerView.SmoothScroller.ScrollVectorProvider {

    DefaultLaoutManager(){

    }


    @Override public Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }


    @Override public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return null;
    }


    @Override public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
    }


    @Override public int getChildCount() {
        return super.getChildCount();
    }


    @Override public PointF computeScrollVectorForPosition(int targetPosition) {
        return null;
    }


    @Override public void prepareForDrop(View view, View target, int x, int y) {

    }


    @Override public int computeHorizontalScrollOffset(RecyclerView.State state) {
        return super.computeHorizontalScrollOffset(state);
    }


    @Override public int computeVerticalScrollExtent(RecyclerView.State state) {
        return super.computeVerticalScrollExtent(state);
    }
}
