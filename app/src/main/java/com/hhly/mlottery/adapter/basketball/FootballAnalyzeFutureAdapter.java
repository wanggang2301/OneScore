package com.hhly.mlottery.adapter.basketball;

import android.content.Context;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.FootballAnalyzeFuture;
import com.hhly.mlottery.bean.footballDetails.FootballAnalyzeFutureMatch;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * Created by A on 2016/4/5.
 */
public class FootballAnalyzeFutureAdapter extends CommonAdapter<FootballAnalyzeFuture> {

    private Context mContext;

    public FootballAnalyzeFutureAdapter(Context context, List<FootballAnalyzeFuture> datas, int layoutId) {
        super(context, datas, layoutId);

        this.mContext = context;
    }

    @Override
    public void convert(ViewHolder holder, FootballAnalyzeFuture historyBean) {

//        if (holder.getPosition()%2 == 0) {
//            holder.setBackgroundRes(R.id.football_analyze_details_future_item, R.color.home_item_bg);
//        }

        holder.setText(R.id.football_team_name ,historyBean.getLeagueName());
        holder.setText(R.id.football_data , DateUtil.convertDateToNation(historyBean.getDate()));
        holder.setText(R.id.football_guest_name , historyBean.getGuestName());
        holder.setText(R.id.football_home_name , historyBean.getHomeName());
        holder.setText(R.id.football_space, historyBean.getDiffDays() + mContext.getText(R.string.basket_analyze_day));

    }
}
