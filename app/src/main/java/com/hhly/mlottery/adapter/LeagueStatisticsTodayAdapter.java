package com.hhly.mlottery.adapter;

import android.content.Context;
import android.graphics.Color;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.LeagueStatisticsTodayChildBean;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;
import java.util.Random;

/**
 * 描述:  ${TODO}
 * 作者:  wangg@13322.com
 * 时间:  2016/9/2 10:48
 */
public class LeagueStatisticsTodayAdapter extends CommonAdapter<LeagueStatisticsTodayChildBean> {

    private List<LeagueStatisticsTodayChildBean> data;

    private Integer[] colors = {R.color.light_blue, R.color.yellow, R.color.red, R.color.blue, R.color.orange, R.color.gray, R.color.green, R.color.light_blue};

    public LeagueStatisticsTodayAdapter(Context context, List<LeagueStatisticsTodayChildBean> datas, int layoutId) {
        super(context, datas, layoutId);

        this.mContext = context;
        this.data = datas;
    }


    @Override
    public void convert(ViewHolder holder, LeagueStatisticsTodayChildBean bean) {
        if (holder.getPosition() % 2 == 0) {
            holder.setBackgroundColorRes(R.id.ll, R.color.white);
        } else {
            holder.setBackgroundColorRes(R.id.ll, R.color.home_item_bg);
        }

        holder.setText(R.id.tv_league_rank, bean.getNum());
        holder.setBackgroundColorRes(R.id.ll_league_bg, Color.parseColor(bean.getColor()));
        holder.setText(R.id.tv_league_finish, new Random().nextInt(100) + "");
        holder.setText(R.id.tv_league_win_percent, new Random().nextInt(100) + "%");
        holder.setText(R.id.tv_league_win_num, new Random().nextInt(200) + "");
        holder.setText(R.id.tv_league_flat_percent, new Random().nextInt(100) + "%");
        holder.setText(R.id.tv_league_flat_num, new Random().nextInt(80) + "");
        holder.setText(R.id.tv_league_loss_percent, new Random().nextInt(100) + "%");
        holder.setText(R.id.tv_league_loss_num, new Random().nextInt(150) + "");
    }
}

