package com.hhly.mlottery.adapter.custom;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import data.bean.SubsRecordBean;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/6/2  10:59.
 */

public class SubsRecordAdapter extends BaseQuickAdapter<SubsRecordBean.PurchaseRecordsBean.ListBean> {


    public SubsRecordAdapter(int layoutResId, List<SubsRecordBean.PurchaseRecordsBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SubsRecordBean.PurchaseRecordsBean.ListBean b) {

    }


}
