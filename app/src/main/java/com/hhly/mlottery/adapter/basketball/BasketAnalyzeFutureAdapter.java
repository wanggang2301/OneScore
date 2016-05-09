package com.hhly.mlottery.adapter.basketball;

import android.content.Context;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.BasketDetails.BasketAnalyzeMoreFutureBean;
import com.hhly.mlottery.bean.basket.BasketDetails.BasketAnalyzeMoreRecentHistoryBean;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * Created by A on 2016/4/5.
 */
public class BasketAnalyzeFutureAdapter extends CommonAdapter<BasketAnalyzeMoreFutureBean> {

    private Context mContext;

    public BasketAnalyzeFutureAdapter(Context context, List<BasketAnalyzeMoreFutureBean> datas, int layoutId) {
        super(context, datas, layoutId);

        this.mContext = context;
    }

    @Override
    public void convert(ViewHolder holder, BasketAnalyzeMoreFutureBean historyBean) {

        holder.setText(R.id.basket_team_name ,historyBean.getLeagueName());
        holder.setText(R.id.basket_data , historyBean.getDate());
        holder.setText(R.id.basket_guest_name , historyBean.getGuestTeam());
        holder.setText(R.id.basket_home_name , historyBean.getHomeTeam());
        holder.setText(R.id.basket_space , historyBean.getDiffDays() + mContext.getResources().getText(R.string.basket_analyze_day));

        L.d("BasketAnalyzeFutureAdapter : ", historyBean.getDate() + "");
    }
}
