package com.hhly.mlottery.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by yxq
 * on 2016/7/26.
 */
public class ScrollTouchListView extends ListView {
    public ScrollTouchListView(Context context) {
        super(context);
    }

    public ScrollTouchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollTouchListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}