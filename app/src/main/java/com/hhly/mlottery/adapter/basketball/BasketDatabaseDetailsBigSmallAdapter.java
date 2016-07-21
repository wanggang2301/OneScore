package com.hhly.mlottery.adapter.basketball;

import android.content.Context;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.BasketDatabase.BasketDatabaseBigSmallDetailsBean;
import com.hhly.mlottery.bean.basket.BasketDatabase.BasketDatabaseHandicapDetailsBean;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * description:
 * author: yixq
 * Created by A on 2016/7/18.
 */
public class BasketDatabaseDetailsBigSmallAdapter extends CommonAdapter<BasketDatabaseBigSmallDetailsBean> {

    public BasketDatabaseDetailsBigSmallAdapter(Context context, List<BasketDatabaseBigSmallDetailsBean> datas, int layoutId) {
        super(context, datas, layoutId);

        this.mContext = context;
//        this.mDatas = datas;
    }

    public void updateDatas(List<BasketDatabaseBigSmallDetailsBean> datas) {
        this.mDatas = datas;
    }
    @Override
    public void convert(ViewHolder holder, BasketDatabaseBigSmallDetailsBean bean) {

        holder.setText(R.id.basket_database_details_ranking_name_big_small, bean.getRanking() + " " + bean.getTeamName() + "");
        holder.setText(R.id.basket_database_details_finished_big_small, bean.getFinished() + "");
        holder.setText(R.id.basket_database_details_big, bean.getHigh() + "");
        holder.setText(R.id.basket_database_details_draw, bean.getDraw() + "");
        holder.setText(R.id.basket_database_details_small, bean.getLow() + "");
    }
}
