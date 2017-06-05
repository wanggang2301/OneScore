package com.hhly.mlottery.adapter.custom;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.mvptask.data.model.RecommendArticlesBean;

import java.util.List;
import java.util.Random;

/**
 * @author: Wangg
 * @name：xxx
 * @description: 推介文章Adapter
 * @created on:2017/6/2  11:27.
 */

public class RecommendArticlesAdapter extends BaseQuickAdapter<RecommendArticlesBean.PublishPromotionsBean.ListBean> {
    //private List<RecommendArticlesBean.PublishPromotionsBean.ListBean> listBeanList;


    public RecommendArticlesAdapter(List<RecommendArticlesBean.PublishPromotionsBean.ListBean> data) {
        super(R.layout.recommend_articles_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, RecommendArticlesBean.PublishPromotionsBean.ListBean r) {
        baseViewHolder.setText(R.id.betting_league_name, r.getLeagueName());
        baseViewHolder.setVisible(R.id.betting_round, false);
        baseViewHolder.setText(R.id.betting_date, r.getMatchDate());
        baseViewHolder.setText(R.id.betting_week, r.getMatchTime());
        baseViewHolder.setText(R.id.betting_home_name, r.getHomeName());
        baseViewHolder.setText(R.id.betting_guest_name, r.getGuestName());
        baseViewHolder.setText(R.id.betting_price, String.valueOf("￥ " + r.getPrice() + ".00"));
        baseViewHolder.setText(R.id.betting_buy_num, String.valueOf(getBuyNum(r.getCount())) + "人已购买");

        if (0 == r.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, "竞彩单关");
        } else if (1 == r.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, "亚盘");
        } else if (2 == r.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, "大小球");
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
        baseViewHolder.setText(R.id.betting_recommended_reason, "推荐理由:" + (TextUtils.isEmpty(r.getContext()) ? "" : r.getContext()));
    }

    int nums[] = {3, 5, 10};

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
    }
}