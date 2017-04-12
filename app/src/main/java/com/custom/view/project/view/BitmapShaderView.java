package com.custom.view.project.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.custom.view.project.R;

/**
 * Created by 少东 on 2017/1/18.
 */

public class BitmapShaderView extends View {

    private BitmapShader mBitmapShader ;

    private Paint mPaint ;

    public BitmapShaderView(Context context) {
        this(context, null);
    }

    public BitmapShaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapShaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(200f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dog);

//        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        mPaint.setShader(mBitmapShader);
//        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);

        canvas.drawText("你的名字", 0, getHeight()/2, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //与 getAction() 类似，多点触控必须使用这个方法获取事件类型。
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN: //第一个 手指 初次接触到屏幕 时触发。
                break;
            case MotionEvent.ACTION_POINTER_DOWN: //有非主要的手指按下(即按下之前已经有手指在屏幕上)。
                break;
            case MotionEvent.ACTION_UP: //最后一个 手指 离开屏幕 时触发。
                break;
            case MotionEvent.ACTION_POINTER_UP: //有非主要的手指抬起(即抬起之后仍然有手指在屏幕上)。
                break;
        }

        return super.onTouchEvent(event);
    }
}
