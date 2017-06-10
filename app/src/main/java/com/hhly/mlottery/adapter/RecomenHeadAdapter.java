package com.hhly.mlottery.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.RecommendedExpertDetailsActivity;
import com.hhly.mlottery.bean.FootballLotteryBean;
import com.hhly.mlottery.bean.RecommendationExpertBean;

import java.util.List;


/**
 * Created by yuely198 on 2017/6/10.
 */

public class RecomenHeadAdapter  extends BaseQuickAdapter<RecommendationExpertBean.ExpertPromotionsBean.ListBean> {

    private Context mContext;
    List<RecommendationExpertBean.ExpertPromotionsBean.ListBean> datas;


    public RecomenHeadAdapter(Context context, int layoutResId, List<RecommendationExpertBean.ExpertPromotionsBean.ListBean> data) {
        super(layoutResId,data);
        this.datas = data;
        this.mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, RecommendationExpertBean.ExpertPromotionsBean.ListBean r) {
        baseViewHolder.setText(R.id.betting_league_name, r.getLeagueName());
        baseViewHolder.setVisible(R.id.betting_round, false);
        baseViewHolder.setText(R.id.betting_date, r.getMatchDate());
        baseViewHolder.setText(R.id.betting_week, r.getMatchTime());
        baseViewHolder.setText(R.id.betting_home_name, r.getHomeName());
        baseViewHolder.setText(R.id.betting_guest_name, r.getGuestName());
        baseViewHolder.setText(R.id.betting_price, String.valueOf("￥ " + r.getPrice() + ".00"));
        baseViewHolder.setText(R.id.betting_buy_num, String.valueOf(r.getCount()) + mContext.getResources().getString(R.string.yigoumai_txt));

        if (0 == r.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, mContext.getResources().getString(R.string.jingcaidanguan_txt));
        } else if (1 == r.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, mContext.getResources().getString(R.string.yapan_txt));
        } else if (2 == r.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, mContext.getResources().getString(R.string.daxiaoqiu_txt));
        }
        switch (r.getStatus()) {
            case 1:
                baseViewHolder.setImageResource(R.id.iv, R.mipmap.jingcai_icon_zhong);
                break;
            case 2:
                baseViewHolder.setImageResource(R.id.iv, R.mipmap.jingcai_icon_shi);
                break;
            case 6:
                baseViewHolder.setImageResource(R.id.iv, R.mipmap.jingcai_icon_zou);
                break;
            default:
                break;

        }
        baseViewHolder.setText(R.id.betting_recommended_reason, mContext.getResources().getString(R.string.tuijianliyou_txt) + (TextUtils.isEmpty(r.getContext()) ? "" : r.getContext()));
    }

/*    int nums[] = {3, 5, 10};

    private int getBuyNum(String bugNum) {
        if (TextUtils.isEmpty(bugNum)) {
            return nums[new Random().nextInt(3)];
        }

        if (Integer.parseInt(bugNum) == 0) {
            return nums[new Random().nextInt(3)];
        } else if (Integer.parseInt(bugNum) < 10 && Integer.parseInt(bugNum) > 0) {
            return Integer.parseInt(bugNum) * 10;
        } else {
            return Integer.parseInt(bugNum);
        }
    }*/
}