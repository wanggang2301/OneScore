package com.hhly.mlottery.adapter.basketball;

import android.content.Context;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseHandicapDetailsBean;
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

        if (bean != null) {

            if (holder.getPosition() < 3) {
                holder.setText(R.id.basket_database_details_ranking , bean.getRanking());
                holder.setBackgroundRes(R.id.basket_database_details_ranking, R.drawable.basket_databae_round_dra);
            }else{
                holder.setBackgroundColorRes(R.id.basket_database_details_ranking, R.color.transparent);
                holder.setText(R.id.basket_database_details_ranking , bean.getRanking());
            }
//            holder.setText(R.id.basket_database_details_ranking , bean.getRanking());
            holder.setText(R.id.basket_database_details_name , bean.getTeamName());

            holder.setText(R.id.basket_database_details_finished, bean.getFinished());
            holder.setText(R.id.basket_database_details_over, bean.getOver());
            holder.setText(R.id.basket_database_details_under, bean.getUnder());
            holder.setText(R.id.basket_database_details_win_lose_draw, bean.getWin() + "/" + bean.getDraw() + "/" + bean.getLose());
        }
    }
}
