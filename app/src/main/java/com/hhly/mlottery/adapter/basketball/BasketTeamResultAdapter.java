package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.util.ImageLoader;

import java.util.List;

import data.bean.BasketTeamResultBean;

/**
 * 描    述：篮球球队赛程赛果适配器
 * 作    者：mady@13322.com
 * 时    间：2017/6/22
 */
public class BasketTeamResultAdapter extends BaseQuickAdapter<BasketTeamResultBean.TeamMatchDataEntity>{

    Context mContext;

    public BasketTeamResultAdapter(List<BasketTeamResultBean.TeamMatchDataEntity> data ,Context mContext) {
        super(R.layout.item_basket_team_result, data);
        this.mContext=mContext;
    }

    @Override
    protected void convert(BaseViewHolder holder, BasketTeamResultBean.TeamMatchDataEntity entity) {
        holder.setText(R.id.item_basket_team_league_type,entity.getLeagueName());
        String date=entity.getMatchTime().substring(5,16);
        holder.setText(R.id.item_basket_team_result_date,date);
        holder.setText(R.id.item_basket_team_guest_name,entity.getGuestTeamName());
        holder.setText(R.id.item_basket_team_home_name,entity.getHomeTeamName());
        ImageView homeIcon=holder.getView(R.id.item_basket_result_home_icon);
        ImageView guestIcon=holder.getView(R.id.item_basket_result_guest_icon);

        ImageLoader.load(mContext,entity.getHomeLogoUrl(),R.mipmap.basket_default).into(homeIcon);
        ImageLoader.load(mContext,entity.getGuestLogoUrl(),R.mipmap.basket_default).into(guestIcon);


        TextView homeText=holder.getView(R.id.item_result_home_score);
        TextView guestText=holder.getView(R.id.item_result_guest_score);

        if(entity.getMatchStatus().equals("0")){ //未開賽
            homeText.setText("");
            guestText.setText("");
            holder.setText(R.id.item_result_vs,"VS");

        }else {
            int homeScore=Integer.parseInt(entity.getHomeScore()==null?"0":entity.getHomeScore());
            int guestScore=Integer.parseInt(entity.getGuestScore()==null?"0":entity.getGuestScore());
            homeText.setText(entity.getHomeScore()==null?"－":entity.getHomeScore());
            guestText.setText(entity.getGuestScore()==null?"－":entity.getGuestScore());
            holder.setText(R.id.item_result_vs,"－");
            if(homeScore>guestScore){
                guestText.setTextColor(ContextCompat.getColor(mContext,R.color.basket_defeat));
                homeText.setTextColor(ContextCompat.getColor(mContext,R.color.betting_recommend_issue_balance_color));
            }else if(homeScore<guestScore){
                guestText.setTextColor(ContextCompat.getColor(mContext,R.color.betting_recommend_issue_balance_color));
                homeText.setTextColor(ContextCompat.getColor(mContext,R.color.basket_defeat));
            }else {
                guestText.setTextColor(ContextCompat.getColor(mContext,R.color.basket_equals));
                homeText.setTextColor(ContextCompat.getColor(mContext,R.color.basket_equals));
            }
        }


    }

}
