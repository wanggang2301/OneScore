package com.hhly.mlottery.widget;

import android.widget.GridView;

/**
 * @author wang gang
 * @date 2016/7/15 20:14
 * @des ${}
 */
public class MyGridViewInfo extends GridView {
    public MyGridViewInfo(android.content.Context context,
                          android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
