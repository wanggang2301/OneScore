package com.hhly.mlottery.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;

import java.util.List;

/**
 * desc:聊球记录adapter
 * Created by 107_tangrr on 2016/12/7 0007.
 */

public class ChartBallAdapter extends BaseQuickAdapter<String> {
    Context mContext;

    public ChartBallAdapter(Context context, int layoutResId, List<String> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tv_nickname,s);
    }
}
