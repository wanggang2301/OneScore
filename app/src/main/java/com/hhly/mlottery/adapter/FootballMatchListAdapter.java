package com.hhly.mlottery.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.infoCenterBean.ListEntity;

import java.util.List;

/**
 * Created by yuely198 on 2017/3/16.
 * 足球竞彩列表Adapter
 */

public class FootballMatchListAdapter extends BaseQuickAdapter<ListEntity> {

    Context mContext;

    public FootballMatchListAdapter(Context context, int layoutResId, List<ListEntity> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ListEntity listEntity) {
        if(getViewHolderPosition(baseViewHolder)==0){
            baseViewHolder.setVisible(R.id.tv_back, false) ;
        }else{
            baseViewHolder.setVisible(R.id.tv_back, true) ;
        }
        baseViewHolder.setText(R.id.match_home, listEntity.homeName);
        baseViewHolder.setText(R.id.match_guest, listEntity.guestName);


    }

}
