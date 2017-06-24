package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.view.View;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseBigSmallDetailsBean;
import com.hhly.mlottery.frame.basketballframe.BasketDatabaseBigSmallFragment;
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
    private BasketDatabaseBigSmallFragment.BasketballBigSmallDetailsClickListener basketballBigSmallDetailsClickListener;
    public void setBasketballBigSmallDetailsClickListener(BasketDatabaseBigSmallFragment.BasketballBigSmallDetailsClickListener basketballBigSmallDetailsClickListener){
        this.basketballBigSmallDetailsClickListener = basketballBigSmallDetailsClickListener;
    }

    public void updateDatas(List<BasketDatabaseBigSmallDetailsBean> datas) {
        this.mDatas = datas;
    }
    @Override
    public void convert(ViewHolder holder, final BasketDatabaseBigSmallDetailsBean bean) {

        if (holder.getPosition() < 3) {
            holder.setText(R.id.basket_database_datails_ranking_big_small , bean.getRanking());
            holder.setBackgroundRes(R.id.basket_database_datails_ranking_big_small, R.drawable.basket_databae_round_dra);
            holder.setTextColorRes(R.id.basket_database_datails_ranking_big_small, R.color.basket_text_color);

            holder.setText(R.id.basket_database_details_name_big_small, bean.getTeamName());
            holder.setTextColorRes(R.id.basket_database_details_name_big_small , R.color.content_txt_black);
        }else{
            holder.setText(R.id.basket_database_datails_ranking_big_small , bean.getRanking());
            holder.setBackgroundColorRes(R.id.basket_database_datails_ranking_big_small, R.color.transparent);
            holder.setTextColorRes(R.id.basket_database_datails_ranking_big_small, R.color.live_text1);

            holder.setText(R.id.basket_database_details_name_big_small, bean.getTeamName());
            holder.setTextColorRes(R.id.basket_database_details_name_big_small , R.color.content_txt_black);
        }

//        holder.setText(R.id.basket_database_datails_ranking_big_small , bean.getRanking());
//        holder.setText(R.id.basket_database_details_name_big_small, bean.getTeamName());

        holder.setText(R.id.basket_database_details_finished_big_small, bean.getFinished());
        holder.setText(R.id.basket_database_details_big, bean.getHigh());
        holder.setText(R.id.basket_database_details_draw, bean.getDraw());
        holder.setText(R.id.basket_database_details_small, bean.getLow());

        holder.setOnClickListener(R.id.basket_database_details_ll, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (basketballBigSmallDetailsClickListener != null) {
                    basketballBigSmallDetailsClickListener.IntegralDetailsOnClick(v,bean);
                }
            }
        });
    }
}
