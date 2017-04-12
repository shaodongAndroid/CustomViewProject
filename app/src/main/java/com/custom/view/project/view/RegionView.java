package com.custom.view.project.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by 少东 on 2017/2/23.
 */

public class RegionView extends View {

    private Region circleRegion ;

    private Path circlePath ;

    private Paint mPaint ;

    private Path mPath ;
    public RegionView(Context context) {
        this(context, null);
    }

    public RegionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        circleRegion = new Region();
        circlePath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);

        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        circlePath.addCircle(w/2, h/2, w/2, Path.Direction.CW);

        // 将剪裁边界设置为视图大小
        Region globalRegion = new Region(-w, -h, w, h);
        // 将 Path 添加到 Region 中
        circleRegion.setPath(circlePath, globalRegion);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // ▼注意此处将全局变量转化为局部变量，方便 GC 回收 canvas
        Path path = circlePath ;
        canvas.drawPath(path, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();
                if(circleRegion.contains(x, y)){
                    Toast.makeText(getContext(), "circle", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return true;
    }
}
