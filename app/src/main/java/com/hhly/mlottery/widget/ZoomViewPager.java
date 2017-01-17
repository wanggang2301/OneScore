package com.hhly.mlottery.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 描 述：处理图片缩放过程中过快偶崩的问题
 * 作 者：tangjl@13322.com
 * 时 间：2016/12/5
 */

public class ZoomViewPager extends ViewPager {

    private boolean isLocked;

    public ZoomViewPager(Context context) {
        super(context);
        isLocked =false;
    }

    public ZoomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        isLocked =false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        if(!isLocked){
            try{
                return super.onInterceptTouchEvent(ev);
            }catch(IllegalArgumentException e){
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return !isLocked && super.onTouchEvent(event);
    }

}
