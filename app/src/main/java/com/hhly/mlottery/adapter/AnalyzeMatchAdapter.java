package com.hhly.mlottery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.AnalyzeBean;
import com.hhly.mlottery.bean.footballDetails.Battels;
import com.hhly.mlottery.bean.snookerbean.snookerDetail.SnookerAnalyzeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 2016/3/2.
 */
public class AnalyzeMatchAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;

    /**
     * 历史对战数据
     */
    private List<SnookerAnalyzeBean.SnookerEntity> mHistoryInfo;

    public AnalyzeMatchAdapter(Context context, List< SnookerAnalyzeBean.SnookerEntity> mHistoryInfo) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        if (mHistoryInfo == null) {
            mHistoryInfo = new ArrayList<>();
        }
        this.mHistoryInfo = mHistoryInfo;

    }

    @Override
    public int getCount() {
        return mHistoryInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mHistoryInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_analyze_history_white, parent, false);
            holder = new ViewHolder();
            if (position % 2 == 1) {
                convertView.setBackgroundResource(R.color.home_item_bg);
            }

            holder.date = (TextView) convertView.findViewById(R.id.item_history_white_date);
            holder.match = (TextView) convertView.findViewById(R.id.item_history_white_match);
            holder.home = (TextView) convertView.findViewById(R.id.item_history_white_home);
            holder.score = (TextView) convertView.findViewById(R.id.item_history_white_score);
            holder.guest = (TextView) convertView.findViewById(R.id.item_history_white_guest);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

            List<SnookerAnalyzeBean.SnookerEntity> list=  mHistoryInfo;
            holder.date.setText(list.get(position).getDate());
            holder.match.setText(list.get(position).getMatchName());
            holder.home.setText(list.get(position).getHomePlayer());
            holder.guest.setText(list.get(position).getGuestPlayer());
            if (list.get(position).getMarkPlayer() == 1) { //主队
                if (list.get(position).getPlayerColor() == 3) {//胜 红色
                    holder.home.setTextColor(context.getResources().getColor(R.color.win_color));
                } else if (list.get(position).getPlayerColor() == 1) {// 平 绿色
                    holder.home.setTextColor(context.getResources().getColor(R.color.equ_color));
                } else {
                    holder.home.setTextColor(context.getResources().getColor(R.color.defeat_color));
                }
                holder.guest.setTextColor(context.getResources().getColor(R.color.team_name_color));
            } else {
                if (list.get(position).getPlayerColor() == 3) {//胜 红色
                    holder.guest.setTextColor(context.getResources().getColor(R.color.win_color));
                } else if (list.get(position).getPlayerColor() == 1) {// 平 绿色
                    holder.guest.setTextColor(context.getResources().getColor(R.color.equ_color));
                } else {
                    holder.guest.setTextColor(context.getResources().getColor(R.color.defeat_color));
                }
                holder.home.setTextColor(context.getResources().getColor(R.color.team_name_color));
            }
//            String array[] = list.get(position).getResult().split(":");
//            String score = array[0] + ":" + array[1];
            holder.score.setText(list.get(position).getScore());

//        } else if (mHistoryInfo.get(position) instanceof AnalyzeBean.TeamRecentEntity.HomeEntity.BattlesEntity) {
//            List<AnalyzeBean.TeamRecentEntity.HomeEntity.BattlesEntity> list = (List<AnalyzeBean.TeamRecentEntity.HomeEntity.BattlesEntity>) mHistoryInfo;
//            holder.date.setText(list.get(position).getTime());
//            holder.match.setText(list.get(position).getMatchType());
//            holder.home.setText(list.get(position).getHome());
//            holder.guest.setText(list.get(position).getGuest());
//            if (list.get(position).getMarkTeam() == 1) { //主队
//                if (list.get(position).getTeamColor() == 3) {//胜 红色
//                    holder.home.setTextColor(context.getResources().getColor(R.color.win_color));
//                } else if (list.get(position).getTeamColor() == 1) {// 平 绿色
//                    holder.home.setTextColor(context.getResources().getColor(R.color.equ_color));
//                } else {
//                    holder.home.setTextColor(context.getResources().getColor(R.color.defeat_color));
//                }
//                holder.guest.setTextColor(context.getResources().getColor(R.color.team_name_color));
//            } else {
//                if (list.get(position).getTeamColor() == 3) {//胜 红色
//                    holder.guest.setTextColor(context.getResources().getColor(R.color.win_color));
//                } else if (list.get(position).getTeamColor() == 1) {// 平 绿色
//                    holder.guest.setTextColor(context.getResources().getColor(R.color.equ_color));
//                } else {
//                    holder.guest.setTextColor(context.getResources().getColor(R.color.defeat_color));
//                }
//                holder.home.setTextColor(context.getResources().getColor(R.color.team_name_color));
//            }
//
//            String array[] = list.get(position).getResult().split(":");
//            String score = array[0] + ":" + array[1];
//            holder.score.setText(score);
//        } else {
//            List<AnalyzeBean.TeamRecentEntity.GuestEntity.BattlesEntity> list = (List<AnalyzeBean.TeamRecentEntity.GuestEntity.BattlesEntity>) mHistoryInfo;
//            holder.date.setText(list.get(position).getTime());
//            holder.match.setText(list.get(position).getMatchType());
//            holder.home.setText(list.get(position).getHome());
//            holder.guest.setText(list.get(position).getGuest());
//            if (list.get(position).getMarkTeam() == 1) { //主队
//                if (list.get(position).getTeamColor() == 3) {//胜 红色
//                    holder.home.setTextColor(context.getResources().getColor(R.color.win_color));
//                } else if (list.get(position).getTeamColor() == 1) {// 平 绿色
//                    holder.home.setTextColor(context.getResources().getColor(R.color.equ_color));
//                } else {
//                    holder.home.setTextColor(context.getResources().getColor(R.color.defeat_color));
//                }
//                holder.guest.setTextColor(context.getResources().getColor(R.color.team_name_color));
//            } else {
//                if (list.get(position).getTeamColor() == 3) {//胜 红色
//                    holder.guest.setTextColor(context.getResources().getColor(R.color.win_color));
//                } else if (list.get(position).getTeamColor() == 1) {// 平 绿色
//                    holder.guest.setTextColor(context.getResources().getColor(R.color.equ_color));
//                } else {
//                    holder.guest.setTextColor(context.getResources().getColor(R.color.defeat_color));
//                }
//                holder.home.setTextColor(context.getResources().getColor(R.color.team_name_color));
//            }
        return convertView;

    }

    class ViewHolder {
        private TextView date;
        private TextView match;
        private TextView home;
        private TextView score;
        private TextView guest;

    }
}
