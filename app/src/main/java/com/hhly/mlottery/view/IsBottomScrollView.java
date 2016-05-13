package com.hhly.mlottery.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @author lzf
 * @ClassName:
 * @Description: 监听滚动到底部的scrollview
 * @date
 */
public class IsBottomScrollView extends ScrollView {
    private OnScrollToBottomListener onScrollToBottom;

    public IsBottomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IsBottomScrollView(Context context) {
        super(context);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
                                  boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if(scrollY != 0 && null != onScrollToBottom){
            onScrollToBottom.onScrollBottomListener(clampedY);
        }
    }

    public void setOnScrollToBottomLintener(OnScrollToBottomListener listener){
        onScrollToBottom = listener;
    }

    public interface OnScrollToBottomListener{
        public void onScrollBottomListener(boolean isBottom);
    }
}
