package com.hhly.mlottery.adapter.football.teaminfoadapter;

import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.MyApp;
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

        if (bean.getIsHome() == 0) {
            switch (bean.getColor()){
                case 1:
                    holder.setTextColor(R.id.tv_team_name_guest, ContextCompat.getColor(mContext, R.color.foot_team_name_score1));
                    break;
                case 2:
                    holder.setTextColor(R.id.tv_team_name_guest, ContextCompat.getColor(mContext, R.color.foot_team_name_score2));
                    break;
                case 3:
                    holder.setTextColor(R.id.tv_team_name_guest, ContextCompat.getColor(mContext, R.color.foot_team_name_score3));
                    break;
            }
            holder.setTextColor(R.id.tv_team_name_home, ContextCompat.getColor(mContext, R.color.content_txt_black));
        } else if (bean.getIsHome() == 1) {
            switch (bean.getColor()){
                case 1:
                    holder.setTextColor(R.id.tv_team_name_home, ContextCompat.getColor(mContext, R.color.foot_team_name_score1));
                    break;
                case 2:
                    holder.setTextColor(R.id.tv_team_name_home, ContextCompat.getColor(mContext, R.color.foot_team_name_score2));
                    break;
                case 3:
                    holder.setTextColor(R.id.tv_team_name_home, ContextCompat.getColor(mContext, R.color.foot_team_name_score3));
                    break;
            }
            holder.setTextColor(R.id.tv_team_name_guest, ContextCompat.getColor(mContext, R.color.content_txt_black));
        }

        ImageView homeIcon = holder.getView(R.id.iv_team_icon_home);
        ImageView guestIcon = holder.getView(R.id.iv_team_icon_guest);

        Glide.with(MyApp.getContext()).load(bean.getHomeIcon()).error(R.mipmap.home_score_item_icon_def).into(homeIcon);
        Glide.with(MyApp.getContext()).load(bean.getGuestIcon()).error(R.mipmap.home_score_item_icon_def).into(guestIcon);

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
                holder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.fragme_home_shangbanchang_text));
                holder.setTextColor(R.id.tv_match_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
            case "2":// 中场
                holder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.fragme_home_zhongchang_text));
                holder.setTextColor(R.id.tv_match_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
            case "3":// 下半场
                holder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.fragme_home_xiabanchang_text));
                holder.setTextColor(R.id.tv_match_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
            case "4":// 加时
                holder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.fottball_home_jiashi));
                holder.setTextColor(R.id.tv_match_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
            case "5":// 点球
                holder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.fottball_home_dianqiu));
                holder.setTextColor(R.id.tv_match_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
            case "-10":// 取消
                holder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.fottball_home_quxiao));
                holder.setTextColor(R.id.tv_match_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
            case "-11":// 待定
                holder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.fottball_home_daiding));
                holder.setTextColor(R.id.tv_match_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
            case "-12":// 腰斩
                holder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.fottball_home_yaozhan));
                holder.setTextColor(R.id.tv_match_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
            case "-13":// 中断
                holder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.fottball_home_zhongduan));
                holder.setTextColor(R.id.tv_match_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
            case "-14":// 推迟
                holder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.fottball_home_tuichi));
                holder.setTextColor(R.id.tv_match_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
            case "-100":// 取消直播
                holder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.fottball_home_quxiaozhibo));
                holder.setTextColor(R.id.tv_match_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
        }
    }
}
