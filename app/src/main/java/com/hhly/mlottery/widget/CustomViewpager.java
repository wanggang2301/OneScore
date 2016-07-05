package com.hhly.mlottery.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author wang gang
 * @date 2016/6/20 10:26
 * @des Viewpager禁止滑动 false or 可以滑动 true
 */
public class CustomViewpager extends ViewPager {

    private boolean isScrollable = true;


    public CustomViewpager(Context context) {
        super(context);
    }

    public CustomViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (isScrollable == false) {
            return false;
        } else {
            return super.onTouchEvent(ev);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScrollable == false) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }

    }

    public void setIsScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }
}