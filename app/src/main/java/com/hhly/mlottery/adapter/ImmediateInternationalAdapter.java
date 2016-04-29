package com.hhly.mlottery.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.Match;
import com.hhly.mlottery.callback.FocusClickListener;
import com.hhly.mlottery.util.ImmediateUtils;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

/**
 * @ClassName: ImmediateInternationalAdapter
 * @Description: 即时 国际化适配器
 * @author chenml
 * @date
 */
public class ImmediateInternationalAdapter extends CommonAdapter<Match> {

	private final static String TAG = "ImmediateInternationalAdapter";

	private Context mContext;

	private int mItemPaddingRight = -1;

	public void setItemPaddingRight(int mItemPaddingRight) {
		this.mItemPaddingRight = mItemPaddingRight;
	}

	private int handicap = 1;// 盘口 1.亚盘 2.大小球 3.欧赔

	/**
	 * 
	 * @param context
	 * @param datas
	 * @param layoutId
	 *            在不用侧滑的listview，请把值设置为-1
	 */
	public ImmediateInternationalAdapter(Context context, List<Match> datas, int layoutId) {
		super(context, datas, layoutId);
		mContext = context;
	}

	public void updateDatas(List<Match> datas) {
		this.mDatas = datas;
	}

	private FocusClickListener focusClickListener;

	public FocusClickListener getFocusClickListener() {
		return focusClickListener;
	}

	public void setFocusClickListener(FocusClickListener focusClickListener) {
		this.focusClickListener = focusClickListener;
	}


	@Override
	public void convert(ViewHolder holder, Match match) {
		if (PreferenceUtil.getBoolean(MyConstants.RBSECOND, true)) {
			handicap = 1;
		} else if (PreferenceUtil.getBoolean(MyConstants.rbSizeBall, false)) {
			handicap = 2;
		} else if (PreferenceUtil.getBoolean(MyConstants.RBOCOMPENSATE, false)) {
			handicap = 3;
		} else if (PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false)) {
			handicap = 4;
		}
		ImmediateUtils.interConvert(holder, match, mContext, handicap, focusClickListener, mItemPaddingRight);

	}

	/**
	 * 还原赔率部分view
	 * 
	 * @param holder
	 */
	private void resetOddsView(ViewHolder holder) {
		holder.setVisible(R.id.item_football_odds_layout, true);
		holder.setVisible(R.id.item_football_left_odds, true);
		holder.setVisible(R.id.item_football_handicap_value, true);
		holder.setVisible(R.id.item_football_right_odds, true);
		holder.setTextColor(R.id.item_football_handicap_value, Color.BLACK);
	}

	private void setNullViewGone(ViewHolder holder, int viewId, String textValue) {
		if ("".equals(textValue) || "0".equals(textValue)) {
			holder.setVisible(viewId, false);
		} else {
			holder.setText(viewId, textValue);
		}
	}

}
