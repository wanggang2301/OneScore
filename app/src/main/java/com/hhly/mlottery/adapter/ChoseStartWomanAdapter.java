package com.hhly.mlottery.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.ChoseStartBean;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.ListDatasSaveUtils;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * Created by yuely198 on 2016/11/17.
 */

public class ChoseStartWomanAdapter extends CommonAdapter<ChoseStartBean.DataBean.FemaleBean> {


    private ImageView chose_head_child;
    private ImageView imageView4;
    Context mContext;
    private int clickStatus = -1;
    private  ViewHolder holder;
    List<ChoseStartBean.DataBean.FemaleBean> datas;
    public ChoseStartWomanAdapter(Context context, List<ChoseStartBean.DataBean.FemaleBean> datas, int layoutId) {
        super(context, datas, layoutId);
        this.mContext=context;
        this.datas=datas;

    }
  //定义一个判断postion的公共方法
    public void setSeclection(int position) {
        clickStatus = position;
    }

    public void  reshSeclection(){
        UiUtils.toast(mContext,"清除女头像");
        holder.setBackgroundRes(R.id.imageView4, R.color.transparency);

    }

    @Override
    public void newconvert(ViewHolder holder, ChoseStartBean.DataBean.FemaleBean femaleBean, int position) {
         this.holder=holder;
        chose_head_child = holder.getView(R.id.chose_head_child);
        imageView4 = holder.getView(R.id.imageView4);
        Glide.with(MyApp.getContext()).load(femaleBean.getHeadIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(chose_head_child);
      //  ImageLoader.load(mContext, femaleBean.getHeadIcon(), R.mipmap.center_head).into(chose_head_child);

        if (clickStatus==position) {
            holder.setBackgroundRes(R.id.imageView4, R.mipmap.right2x);
        }else {
            holder.setBackgroundRes(R.id.imageView4, R.color.transparency);
        }

    }

    @Override
    public void convert(final ViewHolder holder, final ChoseStartBean.DataBean.FemaleBean femaleBean) {

    }
}
