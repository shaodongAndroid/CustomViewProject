package com.custom.view.project.recyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.custom.view.project.util.Log;

/**
 * Created by dsd on 2017/7/3.
 */

public class SlideOnItemTouchListener implements RecyclerView.OnItemTouchListener {

    public Context mContext ;

    private int mTouchSlop ;

    private boolean startScroll ;

    private int mInitialTouchX , mInitialTouchY;

    private int mLastTouchX, mLastTouchY;


    public SlideOnItemTouchListener(Context context){
        this.mContext = context ;
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        // 需要返回true
        return true;
    }


    @Override public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        int action = e.getAction();

        boolean canScrollHorizontally = rv.getLayoutManager().canScrollHorizontally() ;
        boolean canScrollVertically = rv.getLayoutManager().canScrollHorizontally() ;


        View view ;
        switch (action){
            case MotionEvent.ACTION_DOWN:

                mInitialTouchX = mLastTouchX = (int) (e.getX() + 0.5);
                mInitialTouchY = mLastTouchY = (int) (e.getY() + 0.5);
                view = rv.findChildViewUnder(e.getX(), e.getY());
                rv.getChildAdapterPosition(view);
                break;
            case MotionEvent.ACTION_MOVE:

                final int x = (int) (e.getX() + 0.5f);
                final int y = (int) (e.getY() + 0.5f);

                int dx = x - mLastTouchX ;
                int dy = y - mLastTouchX ;

                if(canScrollHorizontally && Math.abs(x) > mTouchSlop){
                    if (dx > 0) {
                        dx -= mTouchSlop;
                    } else {
                        dx += mTouchSlop;
                    }
                    startScroll = true ;
                }

                if(canScrollVertically && Math.abs(y) > mTouchSlop){
                    if(dy > 0){
                        dy -= mTouchSlop ;
                    } else {
                        dy += mTouchSlop ;
                    }
                    startScroll = true ;
                }

                if(startScroll){
                    scrollByInternal(canScrollHorizontally ? dx : 0,
                        canScrollVertically ? dy : 0, rv);
                }

                mLastTouchX = x ;
                mLastTouchY = y ;

                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
    }


    private void scrollByInternal(int x, int y, RecyclerView rv) {
        Log.d("---------x = "+x);
        if(x != 0){
            // rv.findChildViewUnder(x, y);
            rv.scrollBy(x, 0);
        }
        if(y != 0){
            // rv.smoothScrollBy(0, y);
            // rv.getLayoutManager().scrollHorizontallyBy(x, rv.getRecycledViewPool())
        }
    }


    @Override public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
