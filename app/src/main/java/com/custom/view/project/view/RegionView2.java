package com.custom.view.project.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 少东 on 2017/2/23.
 */

public class RegionView2 extends View {

    private Region circleRegion ;

    private Path circlePath ;

    private Paint mPaint ;

    private int mWidth , mHeight ;

    private Path mPath1, mPath2, mPath3 ;

    private float downX = 0;
    private float downY = 0 ;
    public RegionView2(Context context) {
        this(context, null);
    }

    public RegionView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        circleRegion = new Region();
        circlePath = new Path();
        mPath1 = new Path();
        mPath2 = new Path();
        mPath3 = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w ;
        mHeight = h ;

        circlePath.addCircle(mWidth, mHeight / 2, mWidth / 2, Path.Direction.CW);

        // 将剪裁边界设置为视图大小
        Region globalRegion = new Region(-mWidth, -mHeight, mWidth, mHeight);
        // 将 Path 添加到 Region 中
        circleRegion.setPath(circlePath, globalRegion);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float[] pts = {downX, downY};

        canvas.drawCircle(0, 0 , 20, mPaint);

        canvas.translate(mWidth * 1.0f / 2, mHeight * 1.0f / 2);

        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(0, 0 , 20, mPaint);

        mPaint.setColor(Color.RED);

        if (pts[0] == -1 && pts[1] == -1) return;          // 如果没有就返回
        // 获得当前矩阵的逆矩阵
        Matrix matrix = new Matrix();
        canvas.getMatrix().invert(matrix);
        // ▼ 使用 mapPoints 将触摸位置转换为画布坐标
        matrix.mapPoints(pts);

        canvas.drawCircle(pts[0], pts[1], 20, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // 打开硬件加速
//                downX =  event.getX();
//                downY =  event.getY();

                // ▼ 注意此处使用 getRawX，而不是 getX  关闭硬件加速
                downX = event.getRawX();
                downY = event.getRawY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                downX = downY = -1 ;
                invalidate();
                break;
        }

        return true;
    }
}
