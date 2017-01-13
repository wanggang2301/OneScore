package com.hhly.mlottery.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hhly.mlottery.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 马东运 155 on 2016/7/15 11:15.
 * <p/>
 * 描述：足球分析界面的折线图
 */
public class LineChartView extends View{

    /**自定义控件的宽高*/
    private int width;
    private int height;
    private Canvas mCanvas=new Canvas();
    private Paint mPaint=new Paint();
    /**第一个图的起始横坐标 六分之一宽度*/
    private float startX;
    /**第一个图标的纵坐标*/
    private float startY;

    /**三角图标 主队*/
    private Bitmap bitmapTriangle;
    /**圆形图标 客队*/
    private Bitmap bitmapCircular;
    private Bitmap bitMap=BitmapFactory.decodeResource(getResources(), R.mipmap.big_triangle);

    private List<List<Integer>> eventList;

    public LineChartView(Context context) {
        this(context,null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs){
        bitmapTriangle = BitmapFactory.decodeResource(getResources(), R.mipmap.big_triangle);
        bitmapCircular = BitmapFactory.decodeResource(getResources(), R.mipmap.big_circular);
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
        startX=width/12; //下方listview的中间来看。是12份
        startY=height/4;

        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.login_et_color));
        mPaint.setStrokeWidth(1);

        drawLine();

        if(eventList!=null){
            drawTriangleAndCircular(eventList);
        }

    }

    /**
     * 三条虚线
     */
    private void drawLine() {

        Paint paint=new Paint();
        paint.setColor(getResources().getColor(R.color.login_et_color));
        paint.setStyle(Paint.Style.STROKE);
        Path path1=new Path();
        path1.moveTo(0,startY);
        path1.lineTo(width,startY);

        Path path2=new Path();
        path2.moveTo(0,2*startY);
        path2.lineTo(width,2*startY);

        Path path3=new Path();
        path3.moveTo(0,3*startY);
        path3.lineTo(width,3*startY);

        PathEffect effect=new DashPathEffect(new float[]{10,10,10,10},1);
        paint.setPathEffect(effect);

        mCanvas.drawPath(path1,paint);
        mCanvas.drawPath(path2,paint);
        mCanvas.drawPath(path3,paint);
    }

    /**
     * 三角形与圆形的绘制
     */
    private void drawTriangleAndCircular(List<List<Integer>>eventList) {
//        //三条横虚线

        int j;

        for(int i=0;i<eventList.size();i++){

            if(eventList.get(i).get(1)==1){ //判断主客队 主队是1 客队是2
                bitMap=bitmapTriangle;
            }
            else{
                bitMap=bitmapCircular;
            }
            float bitMapHeight=bitMap.getHeight()/2;
            //横坐标位置。（2i+1）减去bitmap的宽度的一半
            float x=(2*i+1)*startX-bitMap.getWidth()/2; //图片为了让中点在中点。所以减去一半。
            float x2=(2*i+1)*startX; //但是线条就是准确位置。所以不减去这一半
            if(i==eventList.size()-1){ //最后一个点不画线了
                j=i;
            }
            else{
                j=i+1; //画到下一个点
            }
            float x3=(2*j+1)*startX;
           if(eventList.get(i).get(0)==0){ //走

               //判断下一个点是赢走输？
               if(eventList.get(j).get(0)==0){ // 走
                   mCanvas.drawLine(x2,2*startY,x3,2*startY,mPaint);
               }
               else if(eventList.get(j).get(0)==1){ //赢
                   mCanvas.drawLine(x2,2*startY,x3,1*startY,mPaint);
               }else{
                   mCanvas.drawLine(x2,2*startY,x3,3*startY,mPaint);
               }
               mCanvas.drawBitmap(bitMap,x,2*startY-bitMapHeight,mPaint); //图
           }
            if(eventList.get(i).get(0)==1){ //赢


                if(eventList.get(j).get(0)==0){ // 走
                    mCanvas.drawLine(x2,1*startY,x3,2*startY,mPaint);
                }
                else if(eventList.get(j).get(0)==1){ //赢
                    mCanvas.drawLine(x2,1*startY,x3,1*startY,mPaint);
                }else{
                    mCanvas.drawLine(x2,1*startY,x3,3*startY,mPaint);
                }
                mCanvas.drawBitmap(bitMap,x,1*startY-bitMapHeight,mPaint);
            }
            if(eventList.get(i).get(0)==2){ //输

                if(eventList.get(j).get(0)==0){ // 走
                    mCanvas.drawLine(x2,3*startY,x3,2*startY,mPaint);
                }
                else if(eventList.get(j).get(0)==1){ //赢
                    mCanvas.drawLine(x2,3*startY,x3,1*startY,mPaint);
                }else{
                    mCanvas.drawLine(x2,3*startY,x3,3*startY,mPaint);
                }
                mCanvas.drawBitmap(bitMap,x,3*startY-bitMapHeight,mPaint);
            }
        }

    }

    /**
     * 所画界面的数据集合
     */
    public void setLineChartList(List<List<Integer>> eventList){
        this.eventList=eventList;

        invalidate();
    }
}
