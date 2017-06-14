package com.hhly.mlottery.adapter.custom;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.config.ConstantPool;
import com.hhly.mlottery.mvp.bettingmvp.mvpview.MvpBettingPayDetailsActivity;

import java.util.List;

import data.bean.RecommendArticlesBean;

/**
 * @author: Wangg
 * @name：xxx
 * @description: 推介文章Adapter
 * @created on:2017/6/2  11:27.
 */

public class RecommendArticlesAdapter extends BaseQuickAdapter<RecommendArticlesBean.PublishPromotionsBean.ListBean> {
    //private List<RecommendArticlesBean.PublishPromotionsBean.ListBean> listBeanList;

    private Context mContext;

    public RecommendArticlesAdapter(Context context, List<RecommendArticlesBean.PublishPromotionsBean.ListBean> data) {
        super(R.layout.recommend_articles_item, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final RecommendArticlesBean.PublishPromotionsBean.ListBean r) {
        baseViewHolder.setText(R.id.betting_league_name, r.getLeagueName());
        baseViewHolder.setVisible(R.id.betting_round, false);
        baseViewHolder.setText(R.id.betting_date, r.getMatchDate());
        baseViewHolder.setText(R.id.betting_week, r.getMatchTime());
        baseViewHolder.setText(R.id.betting_home_name, r.getHomeName());
        baseViewHolder.setText(R.id.betting_guest_name, r.getGuestName());
        baseViewHolder.setText(R.id.betting_price, String.valueOf("￥ " + r.getPrice() + ".00"));
        baseViewHolder.setText(R.id.betting_buy_num, String.valueOf(r.getCount()) + mContext.getResources().getString(R.string.yigoumai_txt));

       /* if (0 == r.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, mContext.getResources().getString(R.string.jingcaidanguan_txt));
        } else if (1 == r.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, mContext.getResources().getString(R.string.yapan_txt));
        } else if (2 == r.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, mContext.getResources().getString(R.string.daxiaoqiu_txt));
        }*/

        baseViewHolder.setText(R.id.betting_concede_points_spf, r.getTypeStr());


        switch (r.getStatus()) {
            case 1:
                baseViewHolder.setImageResource(R.id.iv, R.mipmap.jingcai_icon_zhong);
                baseViewHolder.setVisible(R.id.iv, true);
                baseViewHolder.setVisible(R.id.textView11, false);
                break;
            case 2:
                baseViewHolder.setImageResource(R.id.iv, R.mipmap.jingcai_icon_shi);
                baseViewHolder.setVisible(R.id.iv, true);
                baseViewHolder.setVisible(R.id.textView11, false);
                break;
            case 6:
                baseViewHolder.setImageResource(R.id.iv, R.mipmap.jingcai_icon_zou);
                baseViewHolder.setVisible(R.id.iv, true);
                baseViewHolder.setVisible(R.id.textView11, false);
                break;
            default:
                baseViewHolder.setVisible(R.id.iv, false);
                baseViewHolder.setVisible(R.id.textView11, true);
                break;

        }

        baseViewHolder.getView(R.id.ll_recommenditem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, MvpBettingPayDetailsActivity.class);
                intent.putExtra(ConstantPool.TO_DETAILS_PROMOTION_ID, r.getId());
                mContext.startActivity(intent);
            }
        });

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