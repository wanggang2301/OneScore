package com.hhly.mlottery.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * Created by yuely198 on 2016/11/30.
 */

public class ChoseFailStartWomanAdapter extends CommonAdapter<String> {


    private ImageView chose_head_child;
    private ImageView imageView4;
    List<String> datas;
    Context mContext;
    private int clickStatus = -1;

    public ChoseFailStartWomanAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
        this.mContext=context;
        this.datas=datas;

    }
    public void setSeclection(int position) {
        clickStatus = position;
    }

    @Override
    public void convert(ViewHolder holder, String s) {

        chose_head_child = holder.getView(R.id.chose_head_child);
        imageView4 = holder.getView(R.id.imageView4);
        Glide.with(MyApp.getContext()).load(s).diskCacheStrategy(DiskCacheStrategy.ALL).into(chose_head_child);

        if (clickStatus==holder.getPosition()) {
            holder.setBackgroundRes(R.id.imageView4, R.mipmap.right2x);
        }else {
            holder.setBackgroundRes(R.id.imageView4, R.color.transparency);
        }
    }

}
