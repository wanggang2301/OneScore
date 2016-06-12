package com.hhly.mlottery.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.StadiumUtils;


/**
 * Created by madongyun 155 on 2015/12/31.
 */
public class TimeView extends View {

    private Context mContext;

    private Canvas mCanvas;
    /**自定义控件的宽高*/
    private int width;
    private int height;
    /**中间黑色的宽度*/
    private float midBlackWidth;
    private int proportion;//比例，占多少分之一

    private static final int TOTAL_LINE_NUM = 90;//总的刻度
    private static final int HALF_LINE_NUM = 15;//半场的线数


    private static final int LONG_LINE = 5;//每四条一个长线

    private float mTextSize;

    private Paint mPaint = new Paint();

    public TimeView(Context context) {
        this(context, null);
    }

    public TimeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);

    }


    private void init(AttributeSet attrs) {

        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.TimeView);
        //通过xml设置中间字体的大小
        mTextSize=ta.getDimension(R.styleable.TimeView_time_textsize,30.0f);
        proportion=ta.getInteger(R.styleable.TimeView_time_midwidth_proportion,30);
        ta.recycle();//回收

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
        this.mCanvas = canvas;
        //中间黑色是总长度的20分之一
        midBlackWidth=width/proportion;
        float leftWidth = (width - midBlackWidth) / 2;
        float rightWidth = leftWidth;
        Paint mPaintGreen = new Paint();
        mPaintGreen.setColor(getResources().getColor(R.color.timeviewcolor));

        if(mTime<=45){
            mCanvas.drawRect(0, 0,(width - midBlackWidth) / TOTAL_LINE_NUM * mTime, height, mPaintGreen);
        }
        if(mTime>45){
            mCanvas.drawRect(0, 0,leftWidth+midBlackWidth+2*leftWidth/TOTAL_LINE_NUM*(mTime-45), height, mPaintGreen);
        }

        mPaint.setColor(getResources().getColor(R.color.timeLineColor));//画笔颜色
        mPaint.setStrokeWidth(2);//线的粗细

        //左侧
        for (int i = 1; i <= HALF_LINE_NUM; i++) {
            float x = leftWidth / HALF_LINE_NUM * i;
            mCanvas.drawLine(x, 2 * height / 3, x, height, mPaint);
            if (i % LONG_LINE == 0) {
                mCanvas.drawLine(x, 0, x, height, mPaint);
            }
        }
        //右侧
        for (int i = 0; i < HALF_LINE_NUM; i++) {

            float x = (leftWidth + midBlackWidth) + rightWidth / HALF_LINE_NUM * i;
            mCanvas.drawLine(x, 2 * height / 3, x, height, mPaint);
            if (i % LONG_LINE == 0) {
                mCanvas.drawLine(x, 0, x, height, mPaint);
            }
        }

        //中间部分

        final Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.midtimeblack));
        mCanvas.drawRect(leftWidth, 0, leftWidth + midBlackWidth, height, paint);

        paint.setColor(getResources().getColor(R.color.halfH));

        paint.setTextSize(mTextSize);//单位是px
        //居中
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;//一系列计算只为让你在中间
        //让字体处于中间
        mCanvas.drawText("H", leftWidth + midBlackWidth/2 , baseline, paint);

    }


    private float mTime;

    /**
     * 根据时间进度
     *
     * @param time
     */
    public void updateTime(int time) {
        this.mTime = StadiumUtils.convertStringToInt(time+"");
        invalidate();
    }
}



