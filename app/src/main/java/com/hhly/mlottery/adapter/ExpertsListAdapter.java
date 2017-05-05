package com.hhly.mlottery.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.MostExpertBean;

import java.util.List;

/**
 * Created by yuely198 on 2017/4/12.
 */

public class ExpertsListAdapter extends BaseQuickAdapter<MostExpertBean.InfoArrayBean> {


    Context mContext;

    public ExpertsListAdapter(Context context, int layoutResId, List<MostExpertBean.InfoArrayBean> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MostExpertBean.InfoArrayBean infoArrayBean) {

        if (infoArrayBean.getSubTitle() != null) {
            baseViewHolder.setVisible(R.id.tv_tj,true);
            baseViewHolder.setText(R.id.ex_tuijian, infoArrayBean.getSubTitle());
        } else {
            baseViewHolder.setVisible(R.id.tv_tj,false);
            baseViewHolder.setText(R.id.ex_tuijian, "");
        }

        baseViewHolder.setText(R.id.ex_title, infoArrayBean.getTitle());

        baseViewHolder.setText(R.id.ex_from, infoArrayBean.getInfoSource());

        Glide.with(MyApp.getContext()).load(infoArrayBean.getPicUrl()).into((ImageView) baseViewHolder.getView(R.id.ex_image));


    }
}
