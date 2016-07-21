package com.hhly.mlottery.adapter.basketball;

import android.content.Context;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.BasketDatabase.BasketDatabaseHandicapDetailsBean;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * description:
 * author: yixq
 * Created by A on 2016/7/18.
 */
public class BasketDatabaseDetailsAdapter extends CommonAdapter<BasketDatabaseHandicapDetailsBean> {

    public BasketDatabaseDetailsAdapter(Context context, List<BasketDatabaseHandicapDetailsBean> datas, int layoutId) {
        super(context, datas, layoutId);

        this.mContext = context;
//        this.mDatas = datas;
    }

    public void updateDatas(List<BasketDatabaseHandicapDetailsBean> datas) {
        this.mDatas = datas;
    }
    @Override
    public void convert(ViewHolder holder, BasketDatabaseHandicapDetailsBean bean) {

        holder.setText(R.id.basket_database_details_ranking_name, bean.getRanking() + " " + bean.getTeamName() + "");
        holder.setText(R.id.basket_database_details_finished, bean.getFinished() + "");
        holder.setText(R.id.basket_database_details_over, bean.getOver() + "");
        holder.setText(R.id.basket_database_details_under, bean.getUnder() + "");
        holder.setText(R.id.basket_database_details_win_lose_draw, bean.getWin() + "/" + bean.getDraw() + "/" + bean.getLose());
    }
}
