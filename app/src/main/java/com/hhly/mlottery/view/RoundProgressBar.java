package com.hhly.mlottery.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.StringFormatUtils;

/**
 * description: 自定义的ProgressBar
 * author: yixq
 * Created by A on 2016/7/8.
 */
public class RoundProgressBar extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;
    private int roundProgressColor2;
    private int roundProgressColor3;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private double progress1;
    private double progress2;
    private double progress3;
    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    /**
     *  进度数据
     */
    private String prossgressData1;
    private String prossgressData2;
    private String prossgressData3;

    public void setDatas(String data1 , String data2 , String data3){
        this.prossgressData1 = data1;
        this.prossgressData2 = data2;
        this.prossgressData3 = data3;
    }



    /**
     * 是否显示进度值
     */
    public boolean isprogress = false;
    public boolean isprogress() {
        return isprogress;
    }

    public void setIsprogress(boolean isprogress) {
        this.isprogress = isprogress;
    }


    public static final int STROKE = 0;
    public static final int FILL = 1;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();


        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, getResources().getColor(R.color.home_item_bg));
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.RED);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.RED);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, getResources().getDimensionPixelOffset(R.dimen.text_size_13));
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 10);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);

        mTypedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外层的大圆环
         */
        int centerX = getWidth() / 2; //获取圆心的x坐标

        int radius = (int) (centerX - roundWidth / 2); //圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centerX, centerX, radius, paint); //画出圆环

        /**
         * 画进度百分比
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
        String percent = StringFormatUtils.toPercentString(progress1 / (float) max);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        float textWidth = paint.measureText(percent);   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float textCenterVerticalBaselineY =
                centerX - fontMetrics.bottom + (fontMetrics.bottom - fontMetrics.top) / 2;
        if (textIsDisplayable && style == STROKE) {
            canvas.drawText(percent, centerX - textWidth / 2, textCenterVerticalBaselineY, paint); //画出进度百分比
        }

        /**
         * 画圆弧 ，画圆环的进度 (1)
         */
        //设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centerX - radius, centerX - radius, centerX
                + radius, centerX + radius);  //用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                if (progress1 > 0) {
                    canvas.drawArc(oval, 270, (360 * (int)(progress1) / max) +2, false, paint);  //根据进度画圆弧  （从270°开始，0°顺时针开始计算）
                }
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress1 != 0)
                    canvas.drawArc(oval, 0, (360 * (int)(progress1 + 0.5) / max), true, paint);  //根据进度画圆弧
                break;
            }
        }
        /**
         * 画圆弧 ，画圆环的进度 (2)
         */
        //设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor2);  //设置进度的颜色
        RectF oval2 = new RectF(centerX - radius, centerX - radius, centerX
                + radius, centerX + radius);  //用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                if (progress2 > 0) {
                    canvas.drawArc(oval2, 270+(360 * (int)(progress1 + 0.5) / 100),  (360 * (int)(progress2) / max) +2, false, paint);  //根据进度画圆弧  （从270°开始，0°顺时针开始计算）
                }
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress2 != 0)
                    canvas.drawArc(oval2, 0, (360 * (int)(progress2 + 0.5) / max), true, paint);  //根据进度画圆弧
                break;
            }
        }
        /**
         * 画圆弧 ，画圆环的进度 (3)
         */
        //设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor3);  //设置进度的颜色
        RectF oval3 = new RectF(centerX - radius, centerX - radius, centerX
                + radius, centerX + radius);  //用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                if (progress3 > 0) {
                    canvas.drawArc(oval3, 270 + (360 * ((int)(progress1 + 0.5) + (int)(progress2 + 0.5)) / 100),  (360 * (int)(progress3) / max) +2, false, paint);  //根据进度画圆弧  （从270°开始，0°顺时针开始计算）
                }
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress3 != 0)
                    canvas.drawArc(oval3, 0, (360 * (int)(progress3 + 0.5) / max), true, paint);  //根据进度画圆弧
                break;
            }
        }


        /**
         * 话进度条上的内容
         */
        if (isprogress) {
            paint.setStrokeWidth(0);
            paint.setColor(getResources().getColor(R.color.home_item_bg));
            paint.setTextSize(18);
            paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体

            float r = (int) (centerX - roundWidth / 2);//半径

            double d = (360 * progress1 / 100) / 2; //得到角度的一半（进度中间显示）
            double d2 = 360 * (progress1 + progress2/2) / 100; //得到角度的一半（进度中间显示）
            double d3 = (360 * (progress1 + progress2 + progress3/2)/ 100); //得到角度的一半（进度中间显示）

            Paint.FontMetrics fontMetrics2 = paint.getFontMetrics();
            float centerY = centerX - fontMetrics2.bottom + (fontMetrics2.bottom - fontMetrics2.top) / 2;

            double tx = centerX + r * Math.cos((d-90) * 3.14 /180);
            double ty = centerY + r * Math.sin((d-90) * 3.14 /180);

            double tx2 = centerX + r * Math.cos((d2-90) * 3.14 /180);
            double ty2 = centerY + r * Math.sin((d2-90) * 3.14 /180);

            double tx3 = centerX + r * Math.cos((d3-90) * 3.14 /180);
            double ty3 = centerY + r * Math.sin((d3-90) * 3.14 /180);

            if (prossgressData1 != null && !prossgressData1.equals("") && !prossgressData1.equals("0")) {
                float textWidths = paint.measureText(prossgressData1);
                canvas.drawText(prossgressData1, (float) tx - textWidths/2, (float) ty, paint);//画出进度百分比
            }
            if (prossgressData2 != null && !prossgressData2.equals("") && !prossgressData2.equals("0")) {
                float textWidths2 = paint.measureText(prossgressData2);
                canvas.drawText(prossgressData2, (float) tx2 - textWidths2/2, (float) ty2, paint);//画出进度百分比
            }
            if (prossgressData3 != null && !prossgressData3.equals("") && !prossgressData3.equals("0")) {
                float textWidths3 = paint.measureText(prossgressData3);
                canvas.drawText(prossgressData3, (float) tx3 - textWidths3/2, (float) ty3, paint);//画出进度百分比
            }
        }
    }


    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized double getProgress() {
        return progress1;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(double progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress值不能小于0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress1 = progress;
            postInvalidate();
        }
    }
    public synchronized void setProgress2(double progress2) {
        if (progress2 < 0) {
            throw new IllegalArgumentException("progress值不能小于0");
        }
        if (progress2 > max) {
            progress2 = max;
        }
        if (progress2 <= max) {
            this.progress2 = progress2;
            postInvalidate();
        }
    }
    public synchronized void setProgress3(double progress3) {
        if (progress3 < 0) {
            throw new IllegalArgumentException("progress值不能小于0");
        }
        if (progress3 > max) {
            progress3 = max;
        }
        if (progress1 <= max) {
            this.progress3 = progress3;
            postInvalidate();
        }
    }
    public synchronized void setProgressAll(double progr1 , double progr2 , double progr3){

        if (progr1 < 0) {
            throw new IllegalArgumentException("progress值不能小于0");
        }
        if (progr1 > max) {
            progr1 = max;
        }
        if (progr1 <= max){
            this.progress1 = progr1;
            postInvalidate();
        }

        if (progr2 < 0) {
            throw new IllegalArgumentException("progress值不能小于0");
        }
        if (progr2 > max) {
            progr2 = max;
        }
        if (progr2 <= max){
            this.progress2 = progr2;
            postInvalidate();
        }

        if (progr3 < 0) {
            throw new IllegalArgumentException("progress值不能小于0");
        }
        if (progr3 > max) {
            progr3 = max;
        }
        if (progr3 <= max){
            this.progress3 = progr3;
            postInvalidate();
        }
    }


    public int getCircleColor() {
        return roundColor;
    }

    public void setCircleColor(int circleColor) {
        this.roundColor = circleColor;
    }

    public int getCircleProgressColor() {
        return roundProgressColor;
    }

    public void setCircleProgressColor(int circleProgressColor) {
        this.roundProgressColor = circleProgressColor;
    }

    public int getCircleProgressColor2() {
        return roundProgressColor2;
    }

    public void setCircleProgressColor2(int circleProgressColor2) {
        this.roundProgressColor2 = circleProgressColor2;
    }

    public int getCircleProgressColor3() {
        return roundProgressColor3;
    }

    public void setCircleProgressColor3(int circleProgressColor3) {
        this.roundProgressColor3 = circleProgressColor3;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public void setTextIsDisplayable(boolean b) {
        this.textIsDisplayable = b;
        postInvalidate();
    }

}