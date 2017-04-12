package com.custom.view.project.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by 少东 on 2017/2/24.
 */

public class AnswerStatusView extends View{

    private int horizontal_padding ;

    /**
     * 每一个item的高度
     */
    private int itemHeight ;

    private int lineColor = 0xFFDEDCDD ;

    private int innerColor =  0xFF3CA6FB ;

    private int outerColor =  0x803CA6FB ;

    private int textColor = 0xFF0999FF ;
    private int defaultColor = 0xFF969696 ;
    private Paint mPaint ;

    private Paint mTextPaint ;

    private Paint mTimePaint ;
    private int radius ;

    private int textSize ;

    private Path mPath;
    private String[] answers = {"您的问题已经发送给答疑老师，请耐心等待", "答疑老师正在解答您的问题", "已收到答疑微课，可在“我的”“答疑”中查看"};
    private String[] status = {"2017.2.22 10:00", "暂未完成", "暂未完成"};
    public AnswerStatusView(Context context) {
        this(context, null);
    }

    public AnswerStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        radius = dp2Px(4);
        horizontal_padding = dp2Px(30);
        textSize = dp2Px(13);
        itemHeight = dp2Px(50);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(4);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(dp2Px(13));
        mTextPaint.setColor(defaultColor);

        mTimePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimePaint.setStyle(Paint.Style.FILL);
        mTimePaint.setTextSize(dp2Px(12));
        mTimePaint.setColor(defaultColor);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(lineColor);
        canvas.drawLine(radius*2, textSize * 1.0f / 2 + dp2Px(2), radius*2, itemHeight * 3, mPaint);

        canvas.drawCircle(radius*2, textSize * 1.0f / 2 + dp2Px(2), radius, mPaint);

//        canvas.drawCircle(0, dp2Px(50), dp2Px(4), mPaint);
        canvas.drawCircle(radius*2, itemHeight * 2 + textSize * 1.0f / 2  + dp2Px(2), radius, mPaint);

        mPaint.setColor(outerColor);
        canvas.drawCircle(radius*2, itemHeight + textSize * 1.0f / 2 + dp2Px(2), radius * 2, mPaint);
        mPaint.setColor(innerColor);
        canvas.drawCircle(radius*2, itemHeight + textSize * 1.0f / 2 + dp2Px(2), radius * 3 / 2, mPaint);

        canvas.drawLine(0, itemHeight*2+textSize * 1.0f / 2 + dp2Px(2), 300, itemHeight*2 + textSize * 1.0f / 2 + dp2Px(2), mPaint);

        drawAnswerText(canvas);
    }

    private void drawAnswerText(Canvas canvas) {
        canvas.drawText(answers[0], horizontal_padding + radius * 2, textSize, mTextPaint);
        canvas.drawText(status[0], horizontal_padding + radius * 2, textSize*2 + dp2Px(3), mTimePaint);

        canvas.drawText(answers[1], horizontal_padding + radius * 2, textSize + itemHeight, mTextPaint);
        canvas.drawText(status[1], horizontal_padding + radius * 2, textSize*2 + itemHeight + dp2Px(3), mTimePaint);

        canvas.drawText(answers[2], horizontal_padding + radius * 2, textSize + itemHeight*2, mTextPaint);
        canvas.drawText(status[2], horizontal_padding + radius * 2, textSize*2 + itemHeight*2 + dp2Px(3), mTimePaint);
    }

    public int dp2Px(int dp){
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
        return value ;
    }
}
