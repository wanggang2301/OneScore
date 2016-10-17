package com.hhly.mlottery.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.hhly.mlottery.R;

/**
 * description:
 * author: yixq
 * Created by A on 2016/10/12.
 * 带文字显示的progress
 */

public class TextProgressBar extends ProgressBar {

    private String str = " ";
    private String strTo = " ";
    private Paint mPaint; //绘1
    private Paint mPaintTo;//绘2
    private int mProgress;

    public TextProgressBar(Context context) {
        super(context);
        initText();
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initText();
    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initText();
    }

    @Override
    public void setProgress(int progress)
    {
        super.setProgress(progress);
        this.mProgress = progress;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.str, 0, this.str.length(), rect);
//        int x = (getWidth() / 2) - rect.centerX();// 让现实的字体处于中心位置;
        int x = ((getWidth()*mProgress/100) / 2) - rect.centerX(); // 前进度的中间值
        int y = (getHeight() / 2) - (rect.centerY()*2);// 让显示的字体处于中心位置;
        this.mPaint.setTextSize(getResources().getDimensionPixelOffset(R.dimen.text_size_10));
        canvas.drawText(this.str, x, y, this.mPaint);

        Rect rectTo = new Rect();
        this.mPaintTo.getTextBounds(this.strTo,0,this.strTo.length(),rectTo);
        int xTo = (getWidth()*mProgress/100) + ((getWidth() - getWidth()*mProgress/100)/2)- rect.centerX();//后进度的中间值（前进度+后进度一半=后值位置）
        int yTo = (getHeight() / 2) - (rect.centerY()*2);
        this.mPaintTo.setTextSize(getResources().getDimensionPixelOffset(R.dimen.text_size_10));
        canvas.drawText(this.strTo , xTo , yTo , this.mPaintTo);
    }

    // 初始化，画笔
    private void initText()
    {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);// 设置抗锯齿;;;;
        this.mPaint.setColor(getResources().getColor(R.color.white));

        this.mPaintTo = new Paint();
        this.mPaintTo.setAntiAlias(true);// 设置抗锯齿;;;;
        this.mPaintTo.setColor(getResources().getColor(R.color.white));
    }

    // 设置文字内容
    public void setText(String textl , String text2)
    {
        if (textl == null) {
            this.str = "-";
        }else{
            this.str = textl;
        }

        if (text2 == null) {
            this.strTo = "-";
        }else{
            this.strTo = text2;
        }
    }
}
