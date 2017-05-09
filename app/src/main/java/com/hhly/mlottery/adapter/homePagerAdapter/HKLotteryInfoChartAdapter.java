package com.hhly.mlottery.adapter.homePagerAdapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.numbersBean.LotteryInfoHKChartBean;

import java.util.List;

/**
 * desc:香港详情页图表适配器
 * Created by 107_tangrr on 2017/1/11 0011.
 */

public class HKLotteryInfoChartAdapter extends BaseQuickAdapter<LotteryInfoHKChartBean.DataBean> {
    private Context mContext;
    private List<LotteryInfoHKChartBean.DataBean> mData;

    public HKLotteryInfoChartAdapter(Context context, int layoutResId, List<LotteryInfoHKChartBean.DataBean> data) {
        super(layoutResId, data);
        this.mContext = context;
        this.mData = data;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LotteryInfoHKChartBean.DataBean dataBean) {
        ImageView lotteryImg = baseViewHolder.getView(R.id.iv_chart_icon);
        try {
            Glide.with(MyApp.getContext()).load(dataBean.getPicUrl()).error(mContext.getResources().getDrawable(R.mipmap.home_data_info_def)).into(lotteryImg);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        baseViewHolder.setText(R.id.tv_chart_title,dataBean.getTitle());
        baseViewHolder.setText(R.id.tv_chart_sub_title,dataBean.getSummary());
    }
}
