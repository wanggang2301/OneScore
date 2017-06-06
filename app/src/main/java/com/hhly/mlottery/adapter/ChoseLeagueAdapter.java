package com.hhly.mlottery.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.ChoseStartBean;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * Created by yuely198 on 2017/6/5.
 */

public class ChoseLeagueAdapter  extends CommonAdapter<String> {


    Context mContext;
    private int clickStatus = -1;
    private ViewHolder holder;
    List<String> datas;
    private TextView league_date;

    public ChoseLeagueAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
        this.mContext=context;
        this.datas=datas;

    }

    @Override
    public void convert(ViewHolder holder, String s) {

    }

    @Override
    public void newconvert(ViewHolder holder, String s,int position) {

        this.holder=holder;
        league_date = holder.getView(R.id.league_date);
        league_date.setText(s);

    }


    public void  reshSeclection(){

        holder.setBackgroundRes(R.id.imageView4, R.color.transparency);

    }

   /* @Override
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

    }*/

}
