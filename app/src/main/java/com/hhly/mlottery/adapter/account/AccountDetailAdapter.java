package com.hhly.mlottery.adapter.account;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;

import java.util.List;

import data.bean.AccountDetailBean;

/**
 * 描    述：
 * 作    者：mady@13322.com
 * 时    间：2017/6/5
 */
public class AccountDetailAdapter extends BaseQuickAdapter<AccountDetailBean.DataEntity.RecordEntity> {

    private Context mContext;
    public AccountDetailAdapter( List<AccountDetailBean.DataEntity.RecordEntity> data ,Context context) {
        super(R.layout.item_account_detail, data);
        this.mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, AccountDetailBean.DataEntity.RecordEntity recordEntity) {

//        holder.setText(R.id.account_trade_name,recordEntity.);
    }
}
