package com.hhly.mlottery.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.LeagueCup;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

public class FiltrateMatchAdapter extends CommonAdapter<LeagueCup> {

	private final static String TAG = "FiltrateMatchAdapter";

	private Context mContext;

	private LinkedList<String> mCheckedIds;
//	private List<String> mHotList;

	public LinkedList<String> getmCheckedIds() {
		return mCheckedIds;
	}

	public void setCheckedIds(LinkedList<String> mCheckedIds) {
		this.mCheckedIds = mCheckedIds;
	}

	public FiltrateMatchAdapter(Context context, List<LeagueCup> cups, LinkedList<String> checkedIds, int layoutId) {
		super(context, cups, layoutId);
		mContext = context;
		mCheckedIds = checkedIds;
//		mHotList = hotList;

	}

	@Override
	public void convert(ViewHolder holder, LeagueCup t) {
//		if (t.getType() == LeagueCup.TYPE_TITLE || t.getType() == LeagueCup.TYPE_BLANK) {//标题和灰色的地方
//			// holder.setVisible(R.id.item_filtrate_checkbox, View.GONE);
//			holder.setText(R.id.item_filtrate_checkbox, t.getRacename());
//			// holder.setBackgroundRes(R.id.item_filtrate_checkbox, R.);
//			holder.setTextColorRes(R.id.item_filtrate_checkbox, R.color.content_txt_dark_grad);
//			holder.setBackgroundColorRes(R.id.item_filtrate_checkbox, android.R.color.transparent);
//			holder.setBackgroundColorRes(R.id.item_filtrate_checkbox_layout, R.color.home_item_bg);
//			holder.setClickable(R.id.item_filtrate_checkbox, false);
//			holder.setCheckBoxGravity(R.id.item_filtrate_checkbox, Gravity.LEFT);
//
//
//		} else if (t.getType() == LeagueCup.TYPE_CUP) {//筛选选项

			holder.setVisible(R.id.item_filtrate_checkbox, View.VISIBLE);
			holder.setTag(R.id.item_filtrate_checkbox, t.getRaceId());
//			holder.setText(R.id.item_filtrate_checkbox, t.getRacename() + "[" + t.getCount() + "]");
			holder.setText(R.id.item_filtrate_checkbox, Html.fromHtml("<span>"+t.getRacename()+"[</span><span style='font-size:10px;'>"+t.getCount()+"</span><span>]</span>"));
			holder.setCheckBoxGravity(R.id.item_filtrate_checkbox, Gravity.CENTER);
			// if(mHotList.contains(t.getRaceId())){
			// holder.setBackgroundRes(R.id.item_filtrate_checkbox,
			// R.drawable.filtrate_match_checkbox_hot_bg);
			// if(mContext!=null){
			// holder.setTextColor(R.id.item_filtrate_checkbox,
			// mContext.getResources().getColorStateList(R.drawable.filtrate_match_hot_textcolor));
			// }
			// }else{
			holder.setBackgroundRes(R.id.item_filtrate_checkbox, R.drawable.filtrate_match_checkbox_defualt_bg);
			holder.setBackgroundColorRes(R.id.item_filtrate_checkbox_layout, android.R.color.transparent);
//			if (mContext != null) {
//				holder.setTextColor(R.id.item_filtrate_checkbox, mContext.getResources().getColorStateList(R.drawable.filtrate_match_defualt_textcolor));
//			}
			// }

			if (mCheckedIds.contains(t.getRaceId())) {
				holder.setChecked(R.id.item_filtrate_checkbox, true);
			} else {
				holder.setChecked(R.id.item_filtrate_checkbox, false);
			}

			holder.setOnCheckedChangeListener(R.id.item_filtrate_checkbox, new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					//Log.i(TAG, "________onCheckedChanged________isChecked = " + isChecked);

					if (isChecked) {
						if (!mCheckedIds.contains(buttonView.getTag())) {
							mCheckedIds.add((String) buttonView.getTag());
						}
					} else {
						if (mCheckedIds.contains(buttonView.getTag())) {
							mCheckedIds.remove((String) buttonView.getTag());
						}
					}
					// checkedChangeListener.onChanged(buttonView, isChecked);
				}
			});

			holder.setOnClickListener(R.id.item_filtrate_checkbox, new OnClickListener() {

				@Override
				public void onClick(View v) {
					//Log.i(TAG, "............click...........");

					if (clickChangeListener != null) {
						clickChangeListener.onClick((CompoundButton) v);
					}

				}
			});
			holder.setClickable(R.id.item_filtrate_checkbox, true);
//		}
//		else if(t.getType() == LeagueCup.TYPE_BLANK_AFTER_CUP){//选项后面多出来的空白处
//			holder.setText(R.id.item_filtrate_checkbox, t.getRacename());
//			holder.setTextColorRes(R.id.item_filtrate_checkbox, R.color.content_txt_dark_grad);
//			holder.setBackgroundColorRes(R.id.item_filtrate_checkbox, android.R.color.transparent);
//			holder.setBackgroundColorRes(R.id.item_filtrate_checkbox_layout, R.color.home_item_bg);
//			holder.setClickable(R.id.item_filtrate_checkbox, false);
//
//			holder.setBackgroundColorRes(R.id.item_filtrate_checkbox_layout, android.R.color.transparent);
//		}

	}

	private CheckedChangeListener checkedChangeListener;

	public CheckedChangeListener getCheckedChangeListener() {
		return checkedChangeListener;
	}

	public void setCheckedChangeListener(CheckedChangeListener checkedChangeListener) {
		this.checkedChangeListener = checkedChangeListener;
	}

	private ClickChangeListener clickChangeListener;

	public ClickChangeListener getClickChangeListener() {
		return clickChangeListener;
	}

	public void setClickChangeListener(ClickChangeListener clickChangeListener) {
		this.clickChangeListener = clickChangeListener;
	}

	public interface ClickChangeListener {
		public void onClick(CompoundButton buttonView);
	}

	public interface CheckedChangeListener {
		public void onChanged(CompoundButton buttonView, boolean isChecked);
	}

}
