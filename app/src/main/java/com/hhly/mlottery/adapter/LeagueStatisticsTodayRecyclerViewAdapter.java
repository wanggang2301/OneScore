package com.hhly.mlottery.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewAdapter;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewHolder;
import com.hhly.mlottery.bean.LeagueStatisticsTodayChildBean;

import java.util.List;

/**
 * 描述:  足球賽事提點Adapter
 * 作者:  wangg@13322.com
 * 时间:  2016/9/7 15:42
 */
public class LeagueStatisticsTodayRecyclerViewAdapter extends BaseRecyclerViewAdapter {
    private List<LeagueStatisticsTodayChildBean> datas;
    private Context mContext;
    private int handicap;

    public LeagueStatisticsTodayRecyclerViewAdapter(Context mContext, List<LeagueStatisticsTodayChildBean> data, int handicap) {
        this.mContext = mContext;
        this.datas = data;
        this.handicap = handicap;
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        LinearLayout ll = viewHolder.findViewById(R.id.ll);
        LinearLayout ll_league_bg = viewHolder.findViewById(R.id.ll_league_bg);
        TextView tv_league_rank = viewHolder.findViewById(R.id.tv_league_rank);
        TextView tv_league_name = viewHolder.findViewById(R.id.tv_league_name);
        TextView tv_league_date = viewHolder.findViewById(R.id.tv_league_date);
        TextView tv_league_finish = viewHolder.findViewById(R.id.tv_league_finish);
        TextView tv_league_win_percent = viewHolder.findViewById(R.id.tv_league_win_percent);
        TextView tv_league_win_num = viewHolder.findViewById(R.id.tv_league_win_num);
        TextView tv_league_flat_percent = viewHolder.findViewById(R.id.tv_league_flat_percent);
        TextView tv_league_flat_num = viewHolder.findViewById(R.id.tv_league_flat_num);
        TextView tv_league_loss_percent = viewHolder.findViewById(R.id.tv_league_loss_percent);
        TextView tv_league_loss_num = viewHolder.findViewById(R.id.tv_league_loss_num);

        if (position % 2 == 0) {
            ll.setBackgroundResource(R.color.white);
        } else {
            ll.setBackgroundResource(R.color.home_item_bg);
        }

        tv_league_rank.setText(datas.get(position).getNum());
        tv_league_name.setText(datas.get(position).getMatchName());

        tv_league_date.setText(datas.get(position).getDate());

        ll_league_bg.setBackgroundColor(Color.parseColor(datas.get(position).getColor()));

        tv_league_finish.setText(datas.get(position).getTotal());
        tv_league_win_percent.setText(datas.get(position).getWinPercent() + "%");
        tv_league_win_num.setText(datas.get(position).getWinCount());

        if (handicap == 0) {
            tv_league_flat_percent.setText(datas.get(position).getDrawPercent() + "%");
            tv_league_flat_num.setText(datas.get(position).getDrawCount());
            tv_league_loss_percent.setText(datas.get(position).getLostPercent() + "%");
            tv_league_loss_num.setText(datas.get(position).getLostCount());
        } else {
            tv_league_flat_percent.setText(datas.get(position).getLostPercent() + "%");
            tv_league_flat_num.setText(datas.get(position).getLostCount());
            tv_league_loss_percent.setText(datas.get(position).getDrawPercent() + "%");
            tv_league_loss_num.setText(datas.get(position).getDrawCount());
        }


    }


    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.item_fragment_league_statistics_today};
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 1;
    }

}

