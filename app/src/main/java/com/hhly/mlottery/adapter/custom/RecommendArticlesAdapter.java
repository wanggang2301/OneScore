package com.hhly.mlottery.adapter.custom;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.mvptask.data.model.RecommendArticlesBean;

import java.util.List;

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

        baseViewHolder.setText(R.id.betting_home_name, r.getHomeName());


    }
}