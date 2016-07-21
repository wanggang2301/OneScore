package com.hhly.mlottery.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.hhly.mlottery.R;

/**
 * Created by 马东运 155 on 2016/7/15 11:15.
 * <p/>
 * 描述：足球分析界面的折线图
 */
public class LineChartView extends View{

    /**自定义控件的宽高*/
    private int width;
    private int height;
    private Canvas mCanvas;
    private Paint mPaint=new Paint();

    private Bitmap bitmapRed;//rc
    private Bitmap bitmapYellow;//黄牌


    public LineChartView(Context context) {
        this(context,null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        bitmapRed = BitmapFactory.decodeResource(getResources(), R.mipmap.rc);
        bitmapYellow = BitmapFactory.decodeResource(getResources(), R.mipmap.yc);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        }


        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCanvas=canvas;
        
    }


}
