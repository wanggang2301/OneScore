package com.hhly.mlottery.adapter.football;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.productadvice.ProductAdviceBean;
import com.hhly.mlottery.util.ImageLoader;

import java.util.List;

/**
 * 描    述：
 * 作    者：mady@13322.com
 * 时    间：2016/12/9
 */
public class AdviceAdapter extends BaseQuickAdapter<ProductAdviceBean.DataEntity> {

    private Context context;
    List<ProductAdviceBean.DataEntity> data;
    public AdviceAdapter(List<ProductAdviceBean.DataEntity> data,Context context) {
        super(R.layout.item_advice, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, ProductAdviceBean.DataEntity dataEntity) {
        ImageView icon=holder.getView(R.id.advice_user_icon);

        holder.setText(R.id.advice_user_name,dataEntity.getNickName());
        holder.setText(R.id.advice_user_time,dataEntity.getSendTime());
        holder.setText(R.id.advice_user_content,dataEntity.getContent());

        if(context!=null){
            ImageLoader.load(context,dataEntity.getUserImg(),R.mipmap.basket_default).into(icon);
        }

        if(dataEntity.getReplyContent()!=null&&dataEntity.getReplyContent()!=""){
            holder.setText(R.id.advice_product_reply,dataEntity.getReplyContent());
        }else{
            holder.getView(R.id.ll_product_has_answer).setVisibility(View.GONE);
        }

    }


}