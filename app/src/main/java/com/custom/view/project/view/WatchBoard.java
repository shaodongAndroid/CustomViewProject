package com.custom.view.project.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.custom.view.project.util.Log;
import java.util.Calendar;

/**
 * Created by dsd on 2017/6/21.
 */

public class WatchBoard extends View{

    private float mRadius ; // 外圆半径

    private float mPadding ; // 边距

    private float mTextSize ; // 文字大小

    private float mHourPointWidth ; // 时针宽度

    private float mMinutePointWidth ; // 分针宽度

    private float mSecondPointWidth ; // 秒针宽度

    private float mPointRadius ; // 指针圆角

    private float mPointEndLength ; // 指针末尾长度

    private int mColorLong ; // 长线颜色

    private int mColorShort ; // 短线颜色

    private int mHourPointColor ; // 时针的颜色

    private int mMinutePointColor ; // 分针的颜色

    private int mSecondPointColor ; // 秒针的颜色

    private Paint mPaint ;


    public WatchBoard(Context context) {
        super(context);
    }

    public WatchBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        obtainStyledAttrs(attrs); //获取自定义的属性
        init(); //初始化画笔
    }

    private void obtainStyledAttrs(AttributeSet attrs) {

        mPadding = dptoPx(10);
        mTextSize = dptoPx(16);
        mHourPointWidth = dptoPx(5);
        mMinutePointWidth = dptoPx(3);
        mSecondPointWidth = dptoPx(2);
        mPointRadius = (int) dptoPx(10);
        mPointEndLength = dptoPx(10);

        mColorLong = Color.argb(225, 0, 0, 0);
        mColorShort = Color.argb(125, 0, 0, 0);
        mHourPointColor = Color.BLACK;
        mMinutePointColor = Color.BLACK;
        mSecondPointColor = Color.RED;

        // TypedArray array = null;
        // try {
        //     array = getContext().obtainStyledAttributes(attrs, R.styleable.WatchBoard);
        //     mPadding = array.getDimension(R.styleable.WatchBoard_wb_padding, DptoPx(10));
        //     mTextSize = array.getDimension(R.styleable.WatchBoard_wb_text_size, SptoPx(16));
        //     mHourPointWidth = array.getDimension(R.styleable.WatchBoard_wb_hour_pointer_width, DptoPx(5));
        //     mMinutePointWidth = array.getDimension(R.styleable.WatchBoard_wb_minute_pointer_width, DptoPx(3));
        //     mSecondPointWidth = array.getDimension(R.styleable.WatchBoard_wb_second_pointer_width, DptoPx(2));
        //     mPointRadius = (int) array.getDimension(R.styleable.WatchBoard_wb_pointer_corner_radius, DptoPx(10));
        //     mPointEndLength = array.getDimension(R.styleable.WatchBoard_wb_pointer_end_length, DptoPx(10));
        //
        //     mColorLong = array.getColor(R.styleable.WatchBoard_wb_scale_long_color, Color.argb(225, 0, 0, 0));
        //     mColorShort = array.getColor(R.styleable.WatchBoard_wb_scale_short_color, Color.argb(125, 0, 0, 0));
        //     mHourPointColor = array.getColor(R.styleable.WatchBoard_wb_hour_pointer_color, Color.BLACK);
        //     mMinutePointColor = array.getColor(R.styleable.WatchBoard_wb_minute_pointer_color, Color.BLACK);
        //     mSecondPointColor = array.getColor(R.styleable.WatchBoard_wb_second_pointer_color, Color.RED);
        // } catch (Exception e) {
        //     //一旦出现错误全部使用默认值
        //     mPadding = DptoPx(10);
        //     mTextSize = SptoPx(16);
        //     mHourPointWidth = DptoPx(5);
        //     mMinutePointWidth = DptoPx(3);
        //     mSecondPointWidth = DptoPx(2);
        //     mPointRadius = (int) DptoPx(10);
        //     mPointEndLength = DptoPx(10);
        //
        //     mColorLong = Color.argb(225, 0, 0, 0);
        //     mColorShort = Color.argb(125, 0, 0, 0);
        //     mMinutePointColor = Color.BLACK;
        //     mSecondPointColor = Color.RED;
        // } finally {
        //     if (array != null) {
        //         array.recycle();
        //     }
        // }

    }

    //Dp转px
    private float dptoPx(float value) {
        return Math.round(getResources().getDisplayMetrics().density * value);
    }

    //画笔初始化
    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }


    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 1000 ;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED ||
                heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED){
            try {
                throw new NoDetermineSizeException("宽度高度至少有一个确定的值,不能同时为wrap_content");
            } catch (NoDetermineSizeException e) {
                e.printStackTrace();
            }
        }

        if(widthMode == MeasureSpec.EXACTLY){
            width = Math.min(width, widthSize);
        }

        if(heightMode == MeasureSpec.EXACTLY){
            width = Math.min(width, heightSize);
        }

        setMeasuredDimension(width, width);
    }


    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = (Math.min(w, h) - getPaddingLeft() - getPaddingRight()) / 2;
        mPointEndLength = mRadius / 6; //尾部指针默认为半径的六分之一
    }


    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save() ;

        canvas.translate(getWidth()/2, getHeight()/2);
        paintCircle(canvas);

        paintScale(canvas);
        paintPointer(canvas);

        canvas.restore();

        postInvalidateDelayed(1000);
    }

    //绘制外圆背景
    public void paintCircle(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, mRadius, mPaint);
    }

    //绘制刻度
    private void paintScale(Canvas canvas) {
        mPaint.setStrokeWidth(dptoPx(1));
        int lineWidth ;
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) { //整点
                mPaint.setStrokeWidth(dptoPx(1.5f));
                mPaint.setColor(mColorLong);
                lineWidth = 40;
                mPaint.setTextSize(mTextSize);
                String text = ((i / 5) == 0 ? 12 : (i / 5)) + "";
                Rect textBound = new Rect();
                mPaint.getTextBounds(text, 0, text.length(), textBound);
                mPaint.setColor(Color.BLACK);
                canvas.save();
                canvas.translate(0, -mRadius + dptoPx(5) + lineWidth + mPadding + (textBound.bottom - textBound.top) / 2);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.rotate(-6 * i);
                canvas.drawText(text, -(textBound.right + textBound.left) / 2, -(textBound.bottom + textBound.top) / 2, mPaint);
                canvas.restore();
            } else { //非整点
                lineWidth = 30;
                mPaint.setColor(mColorShort);
                mPaint.setStrokeWidth(dptoPx(1));
            }
            canvas.drawLine(0, -mRadius + mPadding, 0, -mRadius + mPadding + lineWidth, mPaint);
            canvas.rotate(6);
        }
    }

    private void paintPointer(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY); //时
        int minute = calendar.get(Calendar.MINUTE); //分
        int second = calendar.get(Calendar.SECOND); //秒
        int angleHour = (hour % 12) * 360 / 12; //时针转过的角度
        int angleMinute = minute * 360 / 60; //分针转过的角度
        int angleSecond = second * 360 / 60; //秒针转过的角度
        Log.d("---------hour = "+hour);
        Log.d("---------minute = "+minute);
        Log.d("---------second = "+second);
        //绘制时针
        canvas.save();
        canvas.rotate(angleHour); //旋转到时针的角度
        RectF rectFHour = new RectF(-mHourPointWidth / 2, -mRadius * 3 / 5, mHourPointWidth / 2, mPointEndLength);
        mPaint.setColor(mHourPointColor); //设置指针颜色
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mHourPointWidth); //设置边界宽度
        canvas.drawRoundRect(rectFHour, mPointRadius, mPointRadius, mPaint); //绘制时针
        canvas.restore();
        //绘制分针
        canvas.save();
        canvas.rotate(angleMinute);
        RectF rectFMinute = new RectF(-mMinutePointWidth / 2, -mRadius * 3.5f / 5, mMinutePointWidth / 2, mPointEndLength);
        mPaint.setColor(mMinutePointColor);
        mPaint.setStrokeWidth(mMinutePointWidth);
        canvas.drawRoundRect(rectFMinute, mPointRadius, mPointRadius, mPaint);
        canvas.restore();
        //绘制秒针
        canvas.save();
        canvas.rotate(angleSecond);
        RectF rectFSecond = new RectF(-mSecondPointWidth / 2, -mRadius + 15, mSecondPointWidth / 2, mPointEndLength);
        mPaint.setColor(mSecondPointColor);
        mPaint.setStrokeWidth(mSecondPointWidth);
        canvas.drawRoundRect(rectFSecond, mPointRadius, mPointRadius, mPaint);
        canvas.restore();
        //绘制中心小圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mSecondPointColor);
        canvas.drawCircle(0, 0, mSecondPointWidth * 4, mPaint);
    }

    class NoDetermineSizeException extends Exception {
        public NoDetermineSizeException(String message) {
            super(message);
        }
    }
}
