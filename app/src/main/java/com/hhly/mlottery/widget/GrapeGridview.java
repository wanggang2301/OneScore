package com.hhly.mlottery.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by A on 2016/1/12.
 */
public class GrapeGridView extends GridView {

    public GrapeGridView(Context context) {
        super(context);
    }

    public GrapeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GrapeGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     *  重写dispatchTouchEvent方法禁止GridView滑动
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_MOVE){

            return true;//禁止Gridview进行滑动
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 重写 onMeasure 方法， 重新绘制子view高度  用域在scoreview中嵌套 gridview时，使gridview全部展示
     *
     * makeMeasureSpec函数中第一个函数决定布局空间的大小，第二个参数是布局模式
     * MeasureSpec.AT_MOST的意思就是子控件需要多大的控件就扩展到多大的空间
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){

        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, mExpandSpec); //返回 新高度
    }
}