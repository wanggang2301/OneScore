package com.hhly.mlottery.adapter.custom;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;

import java.util.List;
import java.util.Random;

/**
 * @author: Wangg
 * @name：xxx
 * @description: 推介文章Adapter
 * @created on:2017/6/2  11:27.
 */

public class RecommendArticlesAdapter extends BaseQuickAdapter<String> {


    public RecommendArticlesAdapter(List<String> data) {
        super(R.layout.recommend_articles_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {


        switch (new Random().nextInt(3)) {

            case 0:
                baseViewHolder.setImageResource(R.id.iv, R.mipmap.jingcai_icon_shi);

                break;
            case 1:
                baseViewHolder.setImageResource(R.id.iv, R.mipmap.jingcai_icon_zou);

                break;
            case 2:
                baseViewHolder.setImageResource(R.id.iv, R.mipmap.jingcai_icon_zhong);

                break;


        }

    }
}