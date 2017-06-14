package com.hhly.mlottery.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.RecommendedExpertDetailsActivity;
import com.hhly.mlottery.bean.FootballLotteryBean;
import com.hhly.mlottery.bean.RecommendationExpertBean;
import com.hhly.mlottery.bean.bettingbean.BettingListDataBean;

import java.util.List;


/**
 * Created by yuely198 on 2017/6/10.
 */

public class RecomenHeadAdapter extends BaseQuickAdapter<RecommendationExpertBean.ExpertPromotionsBean.ListBean> {

    private Context mContext;

    List<RecommendationExpertBean.ExpertPromotionsBean.ListBean> mData;

    public RecomenHeadAdapter(Context context, List<RecommendationExpertBean.ExpertPromotionsBean.ListBean> data) {
        super(R.layout.recomenhead_articles_item, data);
        this.mContext = context;
        this.mData = data;
    }

    public void updateData(List<RecommendationExpertBean.ExpertPromotionsBean.ListBean> data) {
        this.mData = data;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final RecommendationExpertBean.ExpertPromotionsBean.ListBean r) {


        baseViewHolder.setText(R.id.betting_league_name, r.getLeagueName());
        baseViewHolder.setVisible(R.id.betting_round, false);
        baseViewHolder.setText(R.id.betting_date, r.getMatchDate());
        baseViewHolder.setText(R.id.betting_week, r.getMatchTime());
        baseViewHolder.setText(R.id.betting_home_name, r.getHomeName());
        baseViewHolder.setText(R.id.betting_guest_name, r.getGuestName());
        baseViewHolder.setText(R.id.betting_price, String.valueOf("￥ " + r.getPrice() + ".00"));
        baseViewHolder.setText(R.id.betting_buy_num, String.valueOf(r.getCount()) + mContext.getResources().getString(R.string.yigoumai_txt));
        baseViewHolder.setText(R.id.betting_concede_points_spf , r.getTypeStr());
        boolean lookStatus = false;
        switch (r.getStatus()) {
            case 1:
                lookStatus = true;
                baseViewHolder.setVisible(R.id.iv, true);
                baseViewHolder.setVisible(R.id.betting_tobuy_or_check, false);
                baseViewHolder.setImageResource(R.id.iv, R.mipmap.jingcai_icon_zhong);
                break;
            case 2:
                lookStatus = true;
                baseViewHolder.setVisible(R.id.iv, true);
                baseViewHolder.setVisible(R.id.betting_tobuy_or_check, false);
                baseViewHolder.setImageResource(R.id.iv, R.mipmap.jingcai_icon_shi);
                break;
            case 6:
                lookStatus = true;
                baseViewHolder.setVisible(R.id.iv, true);
                baseViewHolder.setVisible(R.id.betting_tobuy_or_check, false);
                baseViewHolder.setImageResource(R.id.iv, R.mipmap.jingcai_icon_zou);
                break;
            default:

                    if (r.getLookStatus() == 2) {
                        baseViewHolder.setVisible(R.id.betting_tobuy_or_check, true);
                        baseViewHolder.setVisible(R.id.iv, false);
                        baseViewHolder.setText(R.id.textView11, mContext.getResources().getString(R.string.betting_txt_buy));
                    } else {
                        baseViewHolder.setVisible(R.id.betting_tobuy_or_check, true);
                        baseViewHolder.setVisible(R.id.iv, false);
                        baseViewHolder.setText(R.id.textView11, mContext.getResources().getString(R.string.betting_txt_check));
                    }

                break;

        }

        LinearLayout mBuyOrCheck = baseViewHolder.getView(R.id.betting_tobuy_check);

        baseViewHolder.setText(R.id.betting_recommended_reason,  (TextUtils.isEmpty(r.getContext()) ? "" : r.getContext()));


        /**
         * 购买（查看）点击
         */


            mBuyOrCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBuyClick != null) {
                        mBuyClick.BuyOnClick(v, r.getId());
                    }
                }
            });


    }

    /**
     * 购买（查看）监听
     */
    private RecommendedExpertDetailsActivity.BettingBuyClickListener mBuyClick; //关注监听回掉

    public void setmBuyClick(RecommendedExpertDetailsActivity.BettingBuyClickListener mBuyClick) {
        this.mBuyClick = mBuyClick;
    }
}