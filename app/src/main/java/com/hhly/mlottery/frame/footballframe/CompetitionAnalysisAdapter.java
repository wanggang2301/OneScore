package com.hhly.mlottery.frame.footballframe;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;

import java.util.List;

/**
 * Created by yuely198 on 2017/3/17.
 * 竞彩分析 Adapter
 */

public class CompetitionAnalysisAdapter extends BaseQuickAdapter<String> {


    Context mContext;

    public CompetitionAnalysisAdapter(Context mContext, int layout, List<String> datas) {
        super(layout, datas);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {

        baseViewHolder.setText(R.id.iv_time,s);

    }
}
