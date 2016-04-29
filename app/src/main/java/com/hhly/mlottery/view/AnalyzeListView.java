package com.hhly.mlottery.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by A on 2016/3/30.
 */
public class AnalyzeListView extends ListView {
    public AnalyzeListView(Context context) {
        super(context);
    }

    public AnalyzeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnalyzeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
