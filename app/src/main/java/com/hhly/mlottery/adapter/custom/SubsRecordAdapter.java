package com.hhly.mlottery.adapter.custom;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.mvptask.data.model.SubsRecordBean;

import java.util.List;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/6/2  10:59.
 */

public class SubsRecordAdapter extends BaseQuickAdapter<SubsRecordBean.PurchaseRecordsBean.ListBean> {


    public SubsRecordAdapter(List<SubsRecordBean.PurchaseRecordsBean.ListBean> data) {
        super(R.layout.betting_recommend_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SubsRecordBean.PurchaseRecordsBean.ListBean b) {

        baseViewHolder.setText(R.id.betting_home_name, b.getHomeName());

    }


}
