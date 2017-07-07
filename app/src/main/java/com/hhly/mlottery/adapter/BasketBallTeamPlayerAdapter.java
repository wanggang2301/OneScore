package com.hhly.mlottery.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.view.CircleImageView;

import java.util.List;

import data.bean.BasketTeamPlayerBean;

/**
 * 描    述：篮球球队阵容球员adapter
 * 作    者：mady@13322.com
 * 时    间：2017/6/25
 */
public class BasketBallTeamPlayerAdapter extends BaseQuickAdapter<BasketTeamPlayerBean> {
    private Context mContext;
    public BasketBallTeamPlayerAdapter( List<BasketTeamPlayerBean> data,Context context) {
        super(R.layout.item_basket_team_player, data);
        this.mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, BasketTeamPlayerBean bean) {
        CircleImageView headIcon=holder.getView(R.id.basket_team_player_icon);
        ImageLoader.load(mContext,bean.getPlayerImg(),R.mipmap.center_head).into(headIcon);

        holder.setText(R.id.basket_team_player_name,bean.getPlayerName());
        holder.setText(R.id.basket_team_player_num,null==bean.getPalyerShirtNum()?mContext.getString(R.string.basket_team_player_num):bean.getPalyerShirtNum()
                +mContext.getString(R.string.basket_team_player_num));
    }
}
