package com.hhly.mlottery.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhly107 on 2016/1/28.
 * 自定义折线走势图
 */
public class MyLineChart extends View {

    private Context mContext;
    // X、Y轴字体大小
    private int mTextSize = 30;
    // 绘制线条宽度
    private int mLineWidth = 3;
    // 绘制圆点大小
    private int mCircleSize = 8;
    // 背景色
    private int mBrackColor = Color.WHITE;
    // XY轴颜色
    private int mLineXYColor = Color.BLACK;
    // XY轴刻度字体颜色
    private int mXYTextColor = Color.BLACK;
    // 网格颜色
    private int mGridColor = Color.GRAY;
    // 第一条线颜色
    private int mOneLineColor = Color.RED;
    // 第二条线颜色
    private int mTwoLineColor = Color.BLUE;
    // 默认边距
    private int Margin = 50;
    // 原点坐标
    private int Xpoint;
    private int Ypoint;
    // X,Y轴的单位长度
    private int Xscale = 20;
    private int Yscale = 20;
    // X,Y轴上面的显示文字
    private String[] Xlabel = {"0", "", "", "", "","", "","", "","45'", "", "","", "","", "","", "", "90'"};
    private String[] Ylabel = {"0", "2", "4", "6", "8"};
    // 标题文本
    //private String Title;
    // 曲线数据
    //private int[] Data = {0, 0, 0, 6, 4, 5, 1 };
    //private int[] Data2 = {0, 4, 2, 8, 5, 4, 3 };
    private List<Integer> Data = new ArrayList<>();
    private List<Integer> Data2 = new ArrayList<>();

    public MyLineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public MyLineChart(Context context) {
        this(context, null, 0);
    }

    public MyLineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    // 初始化数据值
    public void init() {
        Xpoint = this.Margin;
        Ypoint = this.getHeight() - this.Margin;
        Xscale = (this.getWidth() - 3 * this.Margin) / (this.Xlabel.length - 1);// 设置X轴箭头多出的距离
        Yscale = (this.getHeight() - 3 * this.Margin)
                / (this.Ylabel.length - 1);// 设置Y轴箭头多出的距离
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(this.mBrackColor);
        Paint p1 = new Paint();
        p1.setStyle(Paint.Style.STROKE);
        p1.setAntiAlias(true);
        p1.setColor(this.mLineXYColor);
        p1.setStrokeWidth(2);
        init();
        this.drawXLine(canvas, p1);
        this.drawYLine(canvas, p1);
        this.drawTable(canvas);
        this.drawData(canvas);
        this.drawData2(canvas);
    }

    // 画表格
    private void drawTable(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(this.mGridColor);
        Path path = new Path();
        //PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
        //paint.setPathEffect(effects);
        // 纵向线
        for (int i = 1; i * Xscale <= (this.getWidth() - this.Margin); i++) {
            int startX = Xpoint + i * Xscale;
            int startY = Ypoint;
            int stopY = Ypoint - (this.Ylabel.length - 1) * Yscale;
            path.moveTo(startX, startY);
            path.lineTo(startX, stopY);
            canvas.drawPath(path, paint);

        }
        // 横向线
        for (int i = 1; (Ypoint - i * Yscale) >= this.Margin; i++) {
            int startX = Xpoint;
            int startY = Ypoint - i * Yscale;
            int stopX = Xpoint + (this.Xlabel.length - 1) * Xscale;
            path.moveTo(startX, startY);
            path.lineTo(stopX, startY);
            paint.setColor(this.mGridColor);
            canvas.drawPath(path, paint);

            paint.setColor(this.mXYTextColor);// Y轴刻度颜色
            paint.setTextSize(this.mTextSize);// 设置X轴刻度字体大小
            paint.setAntiAlias(true);// 消除锯齿
            canvas.drawText(this.Ylabel[i], this.Margin / 7, startY
                    + this.Margin / 4, paint);
        }
        // 画X轴刻度
        for (int i = 0; i * Xscale <= (this.getWidth() - this.Margin); i++) {
            int startX = Xpoint + i * Xscale;

            paint.setColor(this.mXYTextColor);
            paint.setTextSize(this.mTextSize);
            paint.setAntiAlias(true);// 消除锯齿
            canvas.drawText(this.Xlabel[i], startX - this.Margin / 4,
                    this.getHeight() - this.Margin / 4, paint);
        }
    }

    // 画横纵轴
    private void drawXLine(Canvas canvas, Paint p) {
        canvas.drawLine(Xpoint, Ypoint, this.Margin, this.Margin, p);
        canvas.drawLine(Xpoint, this.Margin, Xpoint - Xpoint / 3, this.Margin
                + this.Margin / 3, p);
        canvas.drawLine(Xpoint, this.Margin, Xpoint + Xpoint / 3, this.Margin
                + this.Margin / 3, p);
    }

    private void drawYLine(Canvas canvas, Paint p) {
        canvas.drawLine(Xpoint, Ypoint, this.getWidth() - this.Margin, Ypoint,
                p);
        canvas.drawLine(this.getWidth() - this.Margin, Ypoint, this.getWidth()
                - this.Margin - this.Margin / 3, Ypoint - this.Margin / 3, p);
        canvas.drawLine(this.getWidth() - this.Margin, Ypoint, this.getWidth()
                - this.Margin - this.Margin / 3, Ypoint + this.Margin / 3, p);
    }

    // 画数据
    private void drawData(Canvas canvas) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(this.mOneLineColor);
        p.setTextSize(this.Margin / 2);
        p.setStrokeWidth(this.mLineWidth);// 设置线条的宽度

        for (int i = 1, len = Data.size(); i < len; i++) {
            int startX = Xpoint + i * Xscale;

            canvas.drawCircle(startX, calY(Data.get(i)), this.mCircleSize, p);// 设置圆点的大小
            canvas.drawLine(Xpoint + (i - 1) * Xscale, calY(Data.get(i - 1)), startX, calY(Data.get(i)), p);
        }
    }

    // 画数据2
    private void drawData2(Canvas canvas) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(this.mTwoLineColor);// 设置第二条线的颜色
        p.setTextSize(this.Margin / 2);
        p.setStrokeWidth(this.mLineWidth);// 设置线条的宽度

        for (int i = 1, len = Data2.size(); i < len; i++) {
            int startX = Xpoint + i * Xscale;

            canvas.drawCircle(startX, calY(Data2.get(i)), this.mCircleSize, p);// 设置圆点的大小
            canvas.drawLine(Xpoint + (i - 1) * Xscale, calY(Data2.get(i - 1)), startX, calY(Data2.get(i)), p);
        }
    }

    /**
     * @param y
     * @return
     */
    private int calY(int y) {
        int y0 = 0;
        int y1 = 0;
        //	Log.i("zzzz", "y:"+y);
        try {
            y0 = Integer.parseInt(Ylabel[0]);
            //		Log.i("zzzz", "y0"+y0);
            y1 = Integer.parseInt(Ylabel[1]);
            //		Log.i("zzzz","y1"+y1);
        } catch (Exception e) {
            //		Log.i("zzzz", "string changed is err");
            return 0;
        }
        try {
            //		Log.i("zzzz", "返回数据"+(Ypoint-(y-y0)*Yscale/(y1-y0)) );
            return Ypoint - ((y - y0) * Yscale / (y1 - y0));
        } catch (Exception e) {
            //	Log.i("zzzz", "return is err");
            return 0;
        }
    }

    public List<Integer> getData() {
        return Data;
    }

    public void setData(List<Integer> data) {
        Data = data;
    }

    public List<Integer> getData2() {
        return Data2;
    }

    public void setData2(List<Integer> data2) {
        Data2 = data2;
    }

    public int getmBrackColor() {
        return mBrackColor;
    }

    public void setmBrackColor(int mBrackColor) {
        this.mBrackColor = mBrackColor;
    }

    public int getmLineXYColor() {
        return mLineXYColor;
    }

    public void setmLineXYColor(int mLineXYColor) {
        this.mLineXYColor = mLineXYColor;
    }

    public int getmXYTextColor() {
        return mXYTextColor;
    }

    public void setmXYTextColor(int mXYTextColor) {
        this.mXYTextColor = mXYTextColor;
    }

    public int getmGridColor() {
        return mGridColor;
    }

    public void setmGridColor(int mGridColor) {
        this.mGridColor = mGridColor;
    }

    public int getmOneLineColor() {
        return mOneLineColor;
    }

    public void setmOneLineColor(int mOneLineColor) {
        this.mOneLineColor = mOneLineColor;
    }

    public int getmTwoLineColor() {
        return mTwoLineColor;
    }

    public void setmTwoLineColor(int mTwoLineColor) {
        this.mTwoLineColor = mTwoLineColor;
    }

    public int getMargin() {
        return Margin;
    }

    public void setMargin(int margin) {
        Margin = margin;
    }

    public int getmTextSize() {
        return mTextSize;
    }

    public void setmTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }

    public int getmLineWidth() {
        return mLineWidth;
    }

    public void setmLineWidth(int mLineWidth) {
        this.mLineWidth = mLineWidth;
    }

    public int getmCircleSize() {
        return mCircleSize;
    }

    public void setmCircleSize(int mCircleSize) {
        this.mCircleSize = mCircleSize;
    }

    public int getXpoint() {
        return Xpoint;
    }

    public void setXpoint(int xpoint) {
        Xpoint = xpoint;
    }

    public int getYpoint() {
        return Ypoint;
    }

    public void setYpoint(int ypoint) {
        Ypoint = ypoint;
    }

    public int getXscale() {
        return Xscale;
    }

    public void setXscale(int xscale) {
        Xscale = xscale;
    }

    public int getYscale() {
        return Yscale;
    }

    public void setYscale(int yscale) {
        Yscale = yscale;
    }

    public String[] getXlabel() {
        return Xlabel;
    }

    public void setXlabel(String[] xlabel) {
        Xlabel = xlabel;
    }

    public String[] getYlabel() {
        return Ylabel;
    }

    public void setYlabel(String[] ylabel) {
        Ylabel = ylabel;
    }


}
