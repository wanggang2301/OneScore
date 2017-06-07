package com.hhly.mlottery.adapter.account;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;

import java.util.List;

import data.bean.AccountDetailBean;
import data.bean.RechargeBean;

/**
 * 描    述：
 * 作    者：mady@13322.com
 * 时    间：2017/6/5
 */
public class AccountDetailAdapter extends BaseQuickAdapter<RechargeBean> {

    private Context mContext;
    public AccountDetailAdapter(List<RechargeBean> data , Context context) {
        super(R.layout.item_account_detail, data);
        this.mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, RechargeBean recordEntity) {

        holder.setText(R.id.account_trade_name,recordEntity.getRechargeName());
        holder.setText(R.id.account_recharge_name,recordEntity.getTradeName());

        String [] date=recordEntity.getFinishTime().split(" ");
        holder.setText(R.id.account_date,date[0]);
        holder.setText(R.id.account_time,date[1]);
        holder.setText(R.id.account_trade_amount,recordEntity.getTradeAmount()+"");
    }
}
