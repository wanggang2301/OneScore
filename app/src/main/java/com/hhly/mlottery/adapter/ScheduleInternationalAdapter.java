package com.hhly.mlottery.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.scheduleBean.AsiaLet;
import com.hhly.mlottery.bean.scheduleBean.AsiaSize;
import com.hhly.mlottery.bean.scheduleBean.Euro;
import com.hhly.mlottery.bean.scheduleBean.SchMatch;
import com.hhly.mlottery.bean.scheduleBean.ScheduleMatchOdd;
import com.hhly.mlottery.frame.footframe.ScheduleFragment.SchFocusClickListener;
import com.hhly.mlottery.util.HandicapUtils;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

public class ScheduleInternationalAdapter extends CommonAdapter<SchMatch> {

	private Context mContext;

	public SchFocusClickListener schfocusClickListener;
	
	private int mItemPaddingRight = -1;

	public void setSchfocusClickListener(SchFocusClickListener schfocusClickListener) {
		this.schfocusClickListener = schfocusClickListener;
	}

	public void setItemPaddingRight(int mItemPaddingRight) {
		this.mItemPaddingRight = mItemPaddingRight;
	}

	private final static String FOCUS_ISD = "focus_ids";

	public ScheduleInternationalAdapter(Context context, List<SchMatch> datas, int layoutId) {
		super(context, datas, layoutId);
		this.mContext = context;
	}
	
	
	public void updateDatas(List<SchMatch> mList) {
		this.mDatas = mList;
	}

	@Override
	public void convert(ViewHolder holder, final SchMatch match) {
		// TODO Auto-generated method stub
		
		if (Build.VERSION.SDK_INT <= 16 && mItemPaddingRight != -1) {// 4.1不支持负的padding值，需要在代码强制设置
			try {
				holder.setPadding(R.id.item_football_international_content_ll, 0, 0, mItemPaddingRight, 0);
			} catch (NumberFormatException e) {
			}
		}
		
		if(mItemPaddingRight == -1){//如果不使用侧滑，请设置为-1，隐藏
			holder.setVisible(R.id.item_football_international_layout_right, false);
		}
		
		holder.setText(R.id.item_football_international_racename, match.getRacename());
		holder.setTextColor(R.id.item_football_international_racename, Color.parseColor(match.getRaceColor()));
		holder.setText(R.id.item_football_international_time, match.getTime());
		holder.setTextColorRes(R.id.item_football_international_time, R.color.content_txt_light_grad);

		
		holder.setVisible(R.id.item_football_international_frequency, View.INVISIBLE);
		
		holder.setVisible(R.id.item_football_international_halfscore, View.INVISIBLE);
		holder.setVisible(R.id.item_football_international_homescore,View.INVISIBLE);
		holder.setVisible(R.id.item_football_international_guestscore, View.INVISIBLE);
		
		
		holder.setVisible(R.id.item_football_international_home_yc, View.INVISIBLE);
		holder.setVisible(R.id.item_football_international_home_rc,View.INVISIBLE);
		
		
		holder.setVisible(R.id.item_football_international_guest_rc, View.INVISIBLE);
		holder.setVisible(R.id.item_football_international_guest_yc, View.INVISIBLE);

		holder.setText(R.id.item_football_international_homename, match.getHometeam());
		
		
		holder.setText(R.id.item_football_international_guestname, match.getGuestteam());

		ScheduleMatchOdd scheduleMatchOdd = match.getMatchOdds();
		if (scheduleMatchOdd != null) {

			boolean asize = PreferenceUtil.getBoolean(MyConstants.rbSizeBall, false);
			boolean eur = PreferenceUtil.getBoolean(MyConstants.RBOCOMPENSATE, false);
			boolean alet = PreferenceUtil.getBoolean(MyConstants.RBSECOND, true);
			boolean noshow = PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false);

		
			// 大小球
			if (asize) {
				AsiaSize asiaSize = scheduleMatchOdd.getAsiaSize();

				if (asiaSize != null) {
					
					holder.setText(R.id.item_football_international_handicap1, "O");
					holder.setText(R.id.item_football_international_odd_top,  asiaSize.getLeftOdds());
					
					
					holder.setText(R.id.item_football_international_handicap2, "");
					holder.setText(R.id.item_football_international_odd_middle, HandicapUtils.changeHandicapByBigLittleBall(asiaSize.getHandicapValue()));
					
					holder.setText(R.id.item_football_international_handicap3, "U");
					holder.setText(R.id.item_football_international_odd_bottom, asiaSize.getRightOdds());
					
					
				} else {
					holder.setText(R.id.item_football_international_handicap1, "");
					holder.setText(R.id.item_football_international_odd_top, "");
					
					
					holder.setText(R.id.item_football_international_handicap2, "");
					holder.setText(R.id.item_football_international_odd_middle, "");
					
					holder.setText(R.id.item_football_international_handicap3, "");
					holder.setText(R.id.item_football_international_odd_bottom, "");

				}

			}

			// 欧赔
			if (eur) {
				Euro euro = scheduleMatchOdd.getEuro();
				if (euro != null) {

					holder.setText(R.id.item_football_international_handicap1, "W");
					holder.setText(R.id.item_football_international_odd_top, euro.getLeftOdds());
					
					
					holder.setText(R.id.item_football_international_handicap2, "D");
					holder.setText(R.id.item_football_international_odd_middle, euro.getMediumOdds());
					
					holder.setText(R.id.item_football_international_handicap3, "L");
					holder.setText(R.id.item_football_international_odd_bottom, euro.getRightOdds());
					
					
					
				} else {
					holder.setText(R.id.item_football_international_handicap1, "");
					holder.setText(R.id.item_football_international_odd_top, "");
					
					
					holder.setText(R.id.item_football_international_handicap2, "");
					holder.setText(R.id.item_football_international_odd_middle, "");
					
					holder.setText(R.id.item_football_international_handicap3, "");
					holder.setText(R.id.item_football_international_odd_bottom, "");
				}
			}

			// 亚赔
			if (alet) {
				AsiaLet asiaLet = scheduleMatchOdd.getAsiaLet();
			
				if (asiaLet != null) {
				
					
					String[] handicaps=HandicapUtils.interChangeHandicap(asiaLet.getHandicapValue());
					
					
					holder.setText(R.id.item_football_international_handicap1, handicaps[0]);
					holder.setText(R.id.item_football_international_odd_top, asiaLet.getLeftOdds());
					
					
					holder.setText(R.id.item_football_international_handicap2, "");
					holder.setText(R.id.item_football_international_odd_middle, "");
					
					holder.setText(R.id.item_football_international_handicap3, handicaps[1]);
					holder.setText(R.id.item_football_international_odd_bottom, asiaLet.getRightOdds());

				} else {
					holder.setText(R.id.item_football_international_handicap1, "");
					holder.setText(R.id.item_football_international_odd_top, "");
					
					
					holder.setText(R.id.item_football_international_handicap2, "");
					holder.setText(R.id.item_football_international_odd_middle, "");
					
					holder.setText(R.id.item_football_international_handicap3, "");
					holder.setText(R.id.item_football_international_odd_bottom, "");
				}
			}

			if (noshow) {

				holder.setText(R.id.item_football_international_handicap1, "");
				holder.setText(R.id.item_football_international_odd_top, "");
				
				
				holder.setText(R.id.item_football_international_handicap2, "");
				holder.setText(R.id.item_football_international_odd_middle, "");
				
				holder.setText(R.id.item_football_international_handicap3, "");
				holder.setText(R.id.item_football_international_odd_bottom, "");
			}
		}

	/*	*/
		String focIds = PreferenceUtil.getString("focus_ids", "");
		String[] arrayIds = focIds.split("[,]");

		for (String s : arrayIds) {
			if (s.equals(match.getThirdId())) {

				//holder.setImageResource(R.id.item_football_internationl_focus_btn, R.drawable.article_like_hover);
				holder.setText(R.id.item_football_internationl_focus_btn, R.string.cancel_favourite);
				holder.setTag(R.id.item_football_internationl_focus_btn, true);
				break;
			} else {
				//holder.setImageResource(R.id.item_football_focus_btn, R.drawable.article_like);
				holder.setText(R.id.item_football_internationl_focus_btn, R.string.favourite);
				holder.setTag(R.id.item_football_internationl_focus_btn, false);
			}
		}
		
		holder.setOnClickListener(R.id.item_football_internationl_focus_btn, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (schfocusClickListener != null) {
					schfocusClickListener.onClick(v, match);
				}
			}
		});

//		holder.getView(R.id.item_football_internationl_focus_btn).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (schfocusClickListener != null) {
//					schfocusClickListener.onClick(v, match);
//				}
//			}
//		});
	}
}
