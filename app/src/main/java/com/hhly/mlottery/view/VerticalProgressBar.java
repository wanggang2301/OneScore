package com.hhly.mlottery.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by Administrator on 2016/8/6.
 */

public class VerticalProgressBar extends ProgressBar {

    public VerticalProgressBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public VerticalProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public VerticalProgressBar(Context context)
    {
        super(context);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas)
    {
        canvas.rotate(-90);//反转90度，将水平ProgressBar竖起来
        canvas.translate(-getHeight(), 0);//    后宽高值互换
        super.onDraw(canvas);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());//互换宽高值
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldw, oldh);//互换宽高值
    }
}
