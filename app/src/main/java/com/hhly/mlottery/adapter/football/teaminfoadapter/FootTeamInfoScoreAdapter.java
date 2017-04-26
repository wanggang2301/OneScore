package com.hhly.mlottery.adapter.football.teaminfoadapter;

import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballteaminfo.FootTeamHistoryMatchBean;

import java.util.List;

/**
 * desc:足球球队赛程赛果适配器
 * Created by 107_tangrr on 2017/4/21 0021.
 */

public class FootTeamInfoScoreAdapter extends BaseQuickAdapter<FootTeamHistoryMatchBean.MatchListBean> {

    public FootTeamInfoScoreAdapter(int layoutResId, List<FootTeamHistoryMatchBean.MatchListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, FootTeamHistoryMatchBean.MatchListBean bean) {
        if (bean == null) {
            return;
        }

        holder.setText(R.id.tv_match_name, bean.getLeague() == null ? "" : bean.getLeague());
        holder.setText(R.id.tv_match_data, bean.getDate() == null ? "" : bean.getDate());
        holder.setText(R.id.tv_team_name_home, bean.getHomeTeam() == null ? "" : bean.getHomeTeam());
        holder.setText(R.id.tv_team_name_guest, bean.getGuestTeam() == null ? "" : bean.getGuestTeam());

        ImageView homeIcon = holder.getView(R.id.iv_team_icon_home);
        ImageView guestIcon = holder.getView(R.id.iv_team_icon_guest);

        Glide.with(mContext).load(bean.getHomeIcon()).error(R.mipmap.home_score_item_icon_def).into(homeIcon);
        Glide.with(mContext).load(bean.getGuestIcon()).error(R.mipmap.home_score_item_icon_def).into(guestIcon);

        // 比分
        switch (String.valueOf(bean.getState())) {
            case "-1":// 完场
                holder.setText(R.id.tv_match_state, bean.getMatchResult() == null ? "" : bean.getMatchResult());
                holder.setTextColor(R.id.tv_match_state, ContextCompat.getColor(mContext, R.color.score_red));
                break;
            case "0":// 未开
                holder.setText(R.id.tv_match_state, "VS");
                holder.setTextColor(R.id.tv_match_state, ContextCompat.getColor(mContext, R.color.content_txt_light_grad));
                break;
            case "1":// 上半场
            case "2":// 中场
            case "3":// 下半场
            case "4":// 加时
            case "5":// 点球
            case "-10":// 取消
            case "-11":// 待定
            case "-12":// 腰斩
            case "-13":// 中断
            case "-14":// 推迟
            case "-100":// 取消直播
                holder.setText(R.id.tv_match_state, bean.getMatchResult() == null ? "" : bean.getMatchResult());
                holder.setTextColor(R.id.tv_match_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
        }
    }
}
