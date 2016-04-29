package com.hhly.mlottery.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public class OddsDetailsListView extends ListView{

	public OddsDetailsListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}


	public OddsDetailsListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = getMeasuredHeight();
		int width = 0;

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		if(widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		}else {
			if(widthMode == MeasureSpec.AT_MOST) {
				final int childCount = getChildCount();
				for(int i=0;i<childCount;i++) {
					View view = getChildAt(i);
					measureChild(view, widthMeasureSpec, heightMeasureSpec);
					width = Math.max(width, view.getMeasuredWidth());
				}
			}
		}

		setMeasuredDimension(width, height);
	}
}