package com.hhly.mlottery.adapter.account;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.util.net.UnitsUtil;

import java.util.List;

import data.bean.AccountDetailBean;
import data.bean.RechargeBean;
import data.utils.L;

/**
 * 描    述：账户详情
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
        holder.setText(R.id.account_status_name,recordEntity.getStatusName());

        String [] date=recordEntity.getFinishTime().split(" ");
        holder.setText(R.id.account_date,date[0]);
        holder.setText(R.id.account_time,date[1]);
        String tradeType="+";
        if(recordEntity.getTradeType()==1||recordEntity.getTradeType()==4){ //收入
            tradeType="+";
        }else{
            tradeType ="-";
        }
        holder.setText(R.id.account_trade_amount, tradeType+UnitsUtil.fenToYuan(recordEntity.getTradeAmount()+""));
    }
}
