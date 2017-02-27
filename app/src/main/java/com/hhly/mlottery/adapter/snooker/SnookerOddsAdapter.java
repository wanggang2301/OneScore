package com.hhly.mlottery.adapter.snooker;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.snookerbean.snookerDetail.SnookerOddsBean;

import java.util.List;

/**
 * 描    述：斯诺克亚盘适配器
 * 作    者：mady@13322.com
 * 时    间：2017/2/24
 */
public class SnookerOddsAdapter extends BaseQuickAdapter<SnookerOddsBean.ListOddEntity>{

    public SnookerOddsAdapter( List<SnookerOddsBean.ListOddEntity> data) {
        super(R.layout.item_snooker_odds, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, SnookerOddsBean.ListOddEntity entity) {
        holder.setText(R.id.snooker_company_txt,entity.getName());
        holder.setText(R.id.snooker_homeOdds1,entity.getDetails().get(0).getHomeOdd()+"");
        holder.setText(R.id.snooker_handicap1,entity.getDetails().get(0).getHand()+"");
        holder.setText(R.id.snooker_guestOdds1,entity.getDetails().get(0).getGuestOdd()+"");

        holder.setText(R.id.snooker_homeOdds2,entity.getDetails().get(1).getHomeOdd()+"");
        holder.setText(R.id.snooker_handicap2,entity.getDetails().get(1).getHand()+"");
        holder.setText(R.id.snooker_guestOdds2,entity.getDetails().get(1).getGuestOdd()+"");
    }
}
