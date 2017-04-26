package com.hhly.mlottery.adapter.basketball;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketstatistics.BasketPlayerStatisticsBean;

import java.util.List;

/**
 * 描    述：篮球直播界面（NBA）球员数据统计
 * 作    者：mady@13322.com
 * 时    间：2016/11/17
 */
public class BasketPlayerStatisticsAdapter extends BaseQuickAdapter<BasketPlayerStatisticsBean.PlayerStatsEntity> {


    public BasketPlayerStatisticsAdapter(List<BasketPlayerStatisticsBean.PlayerStatsEntity> data) {
        super(R.layout.item_player_layout, data);
    }




    @Override
    protected void convert(BaseViewHolder holder, BasketPlayerStatisticsBean.PlayerStatsEntity entity) {

        if(getViewHolderPosition(holder)%2==0){
            holder.setBackgroundColor(R.id.ll_basket_player, MyApp.getContext().getResources().getColor(R.color.mdy_ddd));
        }else{
            holder.setBackgroundColor(R.id.ll_basket_player,MyApp.getContext().getResources().getColor(R.color.usercenter_body_bg));
        }

//        holder.setText(R.id.basket_player_score1,entity.getPlayTime()+"");
        holder.setText(R.id.basket_player_score2,entity.getScore()+"");
        holder.setText(R.id.basket_player_score3,entity.getShootHit()+"-"+entity.getShoot());
        holder.setText(R.id.basket_player_score4,(entity.getDefensiveRebound()+entity.getOffensiveRebound())+"");
        holder.setText(R.id.basket_player_score5,entity.getAssist()+"");
        holder.setText(R.id.basket_player_score6,entity.getThreePointShotHit()+"-"+entity.getThreePointShot());
        holder.setText(R.id.basket_player_score7,entity.getSteal()+"");
        holder.setText(R.id.basket_player_score8,entity.getBlockShot()+"");
        holder.setText(R.id.basket_player_score9,entity.getTurnover()+"");
        holder.setText(R.id.basket_player_score10,entity.getFoul()+"");

    }

}
