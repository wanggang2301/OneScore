package com.hhly.mlottery.adapter.basketball;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;

import java.util.List;

/**
 * 描    述：
 * 作    者：mady@13322.com
 * 时    间：2016/11/23
 */
public class BasketPlayerNameAdapter extends BaseQuickAdapter<String> {
    public BasketPlayerNameAdapter(List<String> data) {
        super(R.layout.item_basket_player_name,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        if(getViewHolderPosition(holder)%2==0){
            holder.setBackgroundColor(R.id.basket_player_name, MyApp.getContext().getResources().getColor(R.color.mdy_ddd));
        }else{
            holder.setBackgroundColor(R.id.basket_player_name,MyApp.getContext().getResources().getColor(R.color.usercenter_body_bg));
        }

        holder.setText(R.id.basket_player_name,s);


    }
}
