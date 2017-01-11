package com.hhly.mlottery.adapter.homePagerAdapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;

import java.util.List;

/**
 * desc:香港详情页图表适配器
 * Created by 107_tangrr on 2017/1/11 0011.
 */

public class HKLotteryInfoChartAdapter extends BaseQuickAdapter<String> {
    private Context mContext;

    public HKLotteryInfoChartAdapter(Context context, int layoutResId, List<String> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tv_chart_title,s);
        baseViewHolder.setText(R.id.tv_chart_sub_title,"test");
    }
}
