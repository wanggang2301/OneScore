package com.hhly.mlottery.adapter.football.teaminfoadapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballteaminfo.FootTeamInfoBean;

import java.util.List;

/**
 * desc:足球球队数据适配器
 * Created by 107_tangrr on 2017/4/21 0021.
 */

public class FootTeamInfoDataAdapter extends BaseQuickAdapter<FootTeamInfoBean> {

    public FootTeamInfoDataAdapter(int layoutResId, List<FootTeamInfoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, FootTeamInfoBean bean) {
        if (bean == null) {
            return;
        }
        // 联赛名
        holder.setText(R.id.tv_team_data_title, bean.getLgName() == null ? "" : bean.getLgName());
        if (bean.getData() != null) {
            // 排名
            if (bean.getData().getRank() != null) {
                String[] split = bean.getData().getRank().split(",");
                if (split.length >= 3) {
                    holder.setText(R.id.tv_data_rank_total, split[0]);
                    holder.setText(R.id.tv_data_rank_zu, split[1]);
                    holder.setText(R.id.tv_data_rank_ke, split[2]);
                }
            }
            // 积分
            if (bean.getData().getScore() != null) {
                String[] split = bean.getData().getScore().split(",");
                if (split.length >= 3) {
                    holder.setText(R.id.tv_data_score_total, split[0]);
                    holder.setText(R.id.tv_data_score_zu, split[1]);
                    holder.setText(R.id.tv_data_score_ke, split[2]);
                }
            }
            // 比赛场次
            if (bean.getData().getMatchCount() != null) {
                String[] split = bean.getData().getMatchCount().split(",");
                if (split.length >= 3) {
                    holder.setText(R.id.tv_data_count_total, split[0]);
                    holder.setText(R.id.tv_data_count_zu, split[1]);
                    holder.setText(R.id.tv_data_count_ke, split[2]);
                }
            }
            // 胜
            if (bean.getData().getWin() != null) {
                String[] split = bean.getData().getWin().split(",");
                if (split.length >= 3) {
                    holder.setText(R.id.tv_data_win_total, split[0]);
                    holder.setText(R.id.tv_data_win_zu, split[1]);
                    holder.setText(R.id.tv_data_win_ke, split[2]);
                }
            }
            // 平
            if (bean.getData().getEqu() != null) {
                String[] split = bean.getData().getEqu().split(",");
                if (split.length >= 3) {
                    holder.setText(R.id.tv_data_equ_total, split[0]);
                    holder.setText(R.id.tv_data_equ_zu, split[1]);
                    holder.setText(R.id.tv_data_equ_ke, split[2]);
                }
            }
            // 负
            if (bean.getData().getLose() != null) {
                String[] split = bean.getData().getLose().split(",");
                if (split.length >= 3) {
                    holder.setText(R.id.tv_data_lose_total, split[0]);
                    holder.setText(R.id.tv_data_lose_zu, split[1]);
                    holder.setText(R.id.tv_data_lose_ke, split[2]);
                }
            }
            // 进球
            if (bean.getData().getGoal() != null) {
                String[] split = bean.getData().getGoal().split(",");
                if (split.length >= 3) {
                    holder.setText(R.id.tv_data_goal_total, split[0]);
                    holder.setText(R.id.tv_data_goal_zu, split[1]);
                    holder.setText(R.id.tv_data_goal_ke, split[2]);
                }
            }
            // 失球
            if (bean.getData().getLoseGoal() != null) {
                String[] split = bean.getData().getLoseGoal().split(",");
                if (split.length >= 3) {
                    holder.setText(R.id.tv_data_lose_goal_total, split[0]);
                    holder.setText(R.id.tv_data_lose_goal_zu, split[1]);
                    holder.setText(R.id.tv_data_lose_goal_ke, split[2]);
                }
            }
            // 均场进球
            if (bean.getData().getGoalAvg() != null) {
                String[] split = bean.getData().getGoalAvg().split(",");
                if (split.length >= 3) {
                    holder.setText(R.id.tv_data_goal_avg_total, split[0]);
                    holder.setText(R.id.tv_data_goal_avg_zu, split[1]);
                    holder.setText(R.id.tv_data_goal_avg_ke, split[2]);
                }
            }
            // 均场失球
            if (bean.getData().getLoseAvg() != null) {
                String[] split = bean.getData().getLoseAvg().split(",");
                if (split.length >= 3) {
                    holder.setText(R.id.tv_data_lose_avg_total, split[0]);
                    holder.setText(R.id.tv_data_lose_avg_zu, split[1]);
                    holder.setText(R.id.tv_data_lose_avg_ke, split[2]);
                }
            }
        }
    }
}
