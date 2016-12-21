package com.hhly.mlottery.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by chenml on 2016/2/2.
 * 手势只有在往下的时候才出现下拉的球
 * 准确度高的下拉刷新。
 */
public class ExactSwipeRefreshLayout extends SwipeRefreshLayout {

    private final static String TAG = "ExactSwipeRefrashLayout";

    private int mTouchSlop;
    private float mPrevX;

    public ExactSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public ExactSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = event.getX();
                break;

        case MotionEvent.ACTION_MOVE:
        final float eventX = event.getX();
        float xDiff = Math.abs(eventX - mPrevX);
//                L.d("refresh", "move----" + eventX + "   " + mPrevX + "   " + mTouchSlop);
        // 增加60的容差，让下拉刷新在竖直滑动时就可以触发
        if (xDiff > mTouchSlop + 60) {
            return false;
        }
    }

    return super.onInterceptTouchEvent(event);
    }
}
