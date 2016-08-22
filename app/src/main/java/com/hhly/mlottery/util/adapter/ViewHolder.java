package com.hhly.mlottery.util.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewHolder{
	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	private Context mContext;
	private int mLayoutId;

	public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		mContext = context;
		mLayoutId = layoutId;
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}
	}

	public int getPosition() {
		return mPosition;
	}

	public int getLayoutId() {
		return mLayoutId;
	}

	/**
	 * 通过viewId获取控件
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 设置TextView的值
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}
	
	public ViewHolder setText(int viewId, Spanned text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}
	
	public ViewHolder setText(int viewId, int textId) {
		TextView tv = getView(viewId);
		tv.setText(textId);
		return this;
	}
	
	public ViewHolder callOnClick(int viewId) {
		View v = getView(viewId);
		v.callOnClick();
		return this;
	}

	public ViewHolder setImageResource(int viewId, int resId) {
		ImageView view = getView(viewId);
		view.setImageResource(resId);
		return this;
	}

	public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bitmap);
		return this;
	}

	public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
		ImageView view = getView(viewId);
		view.setImageDrawable(drawable);
		return this;
	}
	
	public Drawable getDrawable(int viewId) {
		ImageView view = getView(viewId);
		return view.getDrawable();
	}

	public ViewHolder setBackgroundColor(int viewId, int color) {
		View view = getView(viewId);
		view.setBackgroundColor(color);
		return this;
	}
	
	public ViewHolder setBackgroundColorRes(int viewId, int color) {
		View view = getView(viewId);
		view.setBackgroundColor(mContext.getResources().getColor(color));
		return this;
	}

	public ViewHolder setBackgroundRes(int viewId, int backgroundRes) {
		View view = getView(viewId);
		view.setBackgroundResource(backgroundRes);
		return this;
	}

	public ViewHolder setTextColor(int viewId, int textColor) {
		TextView view = getView(viewId);
		view.setTextColor(textColor);
		
		return this;
	}
	
	public ViewHolder setTextColor(int viewId, ColorStateList colors) {
		TextView view = getView(viewId);
		view.setTextColor(colors);
		
		return this;
	}
	
	
	public ViewHolder setFakeBoldText(int viewId, boolean isBold) {
		TextView view = getView(viewId);
		TextPaint tp = view.getPaint(); 
		tp.setFakeBoldText(isBold);
		return this;
	}
	
	public ViewHolder setTextColorString(int viewId, String textColor) {
		TextView view = getView(viewId);
		
		view.setTextColor(Color.parseColor(textColor));
		return this;
	}

	public ViewHolder setTextColorRes(int viewId, int textColorRes) {
		TextView view = getView(viewId);
		view.setTextColor(mContext.getResources().getColor(textColorRes));
		return this;
	}

	@SuppressLint("NewApi")
	public ViewHolder setAlpha(int viewId, float value) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getView(viewId).setAlpha(value);
		} else {
			// Pre-honeycomb hack to set Alpha value
			AlphaAnimation alpha = new AlphaAnimation(value, value);
			alpha.setDuration(0);
			alpha.setFillAfter(true);
			getView(viewId).startAnimation(alpha);
		}
		return this;
	}

	public ViewHolder setAnimation(int viewId, Animation anim) {
		View view = getView(viewId);
		view.setAnimation(anim);
		return this;
	}
	
	public Animation getAnimation(int viewId) {
		View view = getView(viewId);
		return view.getAnimation();
	}
	
	public ViewHolder startAnimation(int viewId, Animation anim) {
		View view = getView(viewId);
		view.startAnimation(anim);
		return this;
	}

	public ViewHolder cancelAnimation(int viewId) {
		View view = getView(viewId);
		
		if(view.getAnimation()!=null){
			view.getAnimation().cancel();
		}
		

		return this;
	}

	public ViewHolder setVisible(int viewId, boolean visible) {
		View view = getView(viewId);
		view.setVisibility(visible ? View.VISIBLE : View.GONE);
		return this;
	}

	public ViewHolder setVisible(int viewId, int visible) {
		View view = getView(viewId);
		view.setVisibility(visible);
		return this;
	}

	public ViewHolder linkify(int viewId) {
		TextView view = getView(viewId);
		Linkify.addLinks(view, Linkify.ALL);
		return this;
	}

	public ViewHolder setTypeface(Typeface typeface, int... viewIds) {
		for (int viewId : viewIds) {
			TextView view = getView(viewId);
			view.setTypeface(typeface);
			view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		}
		return this;
	}

	public ViewHolder setProgress(int viewId, int progress) {
		ProgressBar view = getView(viewId);
		view.setProgress(progress);
		return this;
	}

	public ViewHolder setProgress(int viewId, int progress, int max) {
		ProgressBar view = getView(viewId);
		view.setMax(max);
		view.setProgress(progress);
		return this;
	}

	public ViewHolder setMax(int viewId, int max) {
		ProgressBar view = getView(viewId);
		view.setMax(max);
		return this;
	}

	public ViewHolder setRating(int viewId, float rating) {
		RatingBar view = getView(viewId);
		view.setRating(rating);
		return this;
	}

	public ViewHolder setRating(int viewId, float rating, int max) {
		RatingBar view = getView(viewId);
		view.setMax(max);
		view.setRating(rating);
		return this;
	}

	public ViewHolder setTag(int viewId, Object tag) {
		View view = getView(viewId);
		view.setTag(tag);
		return this;
	}

	public Object getTag(int viewId) {
		View view = getView(viewId);
		return view.getTag();
	}

	public ViewHolder setTag(int viewId, int key, Object tag) {
		View view = getView(viewId);
		view.setTag(key, tag);
		return this;
	}

	public ViewHolder setChecked(int viewId, boolean checked) {
		Checkable view = getView(viewId);
		view.setChecked(checked);
		return this;
	}
	
	public ViewHolder setClickable(int viewId, boolean clickable) {
		CheckBox view = getView(viewId);
		view.setClickable(clickable);
		return this;
	}
	
	public ViewHolder setCheckBoxGravity(int viewId, int gravity) {
		CheckBox view = getView(viewId);
		view.setGravity(gravity);
		return this;
	}

	/**
	 * 关于事件的
	 */
	public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
		View view = getView(viewId);
		view.setOnClickListener(listener);
		return this;
	}

	public ViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
		View view = getView(viewId);
		view.setOnTouchListener(listener);
		return this;
	}

	public ViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
		View view = getView(viewId);
		view.setOnLongClickListener(listener);
		return this;
	}

	public ViewHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
		View view = getView(viewId);

		if (view instanceof CompoundButton) {
			((CompoundButton) view).setOnCheckedChangeListener(listener);
		}

		return this;
	}
	
	public ViewHolder setPadding(int viewId,int left,int top,int right,int bottom) {
		View view = getView(viewId);
		view.setPadding(left, top, right, bottom);
		return this;
	}
	
	public ViewHolder setRelativeLayoutParams(int viewId,RelativeLayout.LayoutParams params) {
		View view = getView(viewId);
		view.setLayoutParams(params);
		return this;
	}
	
	public ViewHolder setAbsListViewLayoutParams(AbsListView.LayoutParams params) {
		View view = getConvertView();
		view.setLayoutParams(params);
		return this;
	}
	
	public int getItemHeight() {
		View view = getConvertView();
		return view.getLayoutParams().height;
	}

}
