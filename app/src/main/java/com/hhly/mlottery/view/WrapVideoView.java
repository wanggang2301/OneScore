package com.hhly.mlottery.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class WrapVideoView extends VideoView {

	public WrapVideoView(Context context) {
		super(context);
		requestFocus();
	}

	public WrapVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WrapVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = getDefaultSize(getWidth(), widthMeasureSpec);
		int height = getDefaultSize(getHeight(), heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
}
