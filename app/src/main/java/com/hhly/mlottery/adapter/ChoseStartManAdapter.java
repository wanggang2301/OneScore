package com.hhly.mlottery.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.ChoseStartBean;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * Created by yuely198 on 2016/11/18.
 */

public class ChoseStartManAdapter extends CommonAdapter<ChoseStartBean.DataBean.MaleBean> {


    private ImageView chose_head_child;
    private ImageView imageView4;
    Context mContext;
    List<ChoseStartBean.DataBean.MaleBean> datas;
    public ChoseStartManAdapter(Context context, List<ChoseStartBean.DataBean.MaleBean> datas, int layoutId) {
        super(context, datas, layoutId);
        this.mContext=context;
        this.datas=datas;
    }

    @Override
    public void convert(final ViewHolder holder, final ChoseStartBean.DataBean.MaleBean femaleBean) {
        chose_head_child = holder.getView(R.id.chose_head_child);
        imageView4 = holder.getView(R.id.imageView4);
        ImageLoader.load(mContext,femaleBean.getHeadIcon(),R.mipmap.center_head).into(chose_head_child);


        if (femaleBean.isChecked()) {//不选中->选中
            //imageView4.setVisibility(View.VISIBLE);
            holder.setBackgroundRes(R.id.imageView4, R.mipmap.right2x);
        } else {//选中->不选中
            // imageView4.setVisibility(View.GONE);
            holder.setBackgroundRes(R.id.imageView4, R.color.transparency);
        }

        holder.setOnClickListener(R.id.startPhoto, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!femaleBean.isChecked()) {//不选中->选中
                    //imageView4.setVisibility(View.VISIBLE);
                    holder.setBackgroundRes(R.id.imageView4 , R.mipmap.right2x);
                } else {//选中->不选中
                    // imageView4.setVisibility(View.GONE);
                    holder.setBackgroundRes(R.id.imageView4, R.color.transparency);
                }
                onCheckListener.onCheck(femaleBean);
            }

        });
    }

    private ChoseStartManAdapter.OnCheckListener onCheckListener;

    public void setOnCheckListener(ChoseStartManAdapter.OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }


    public interface OnCheckListener {
        void onCheck(ChoseStartBean.DataBean.MaleBean mFilter);
    }
}
