package com.hhly.mlottery.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.ForeignInfomationDetailsActivity;
import com.hhly.mlottery.activity.PicturePreviewActivity;

import java.util.List;

/**
 * 描述:  境外资讯Adapter
 * 作者:  wangg@13322.com
 * 时间:  2016/9/12 12:11
 */
public class ForeignInfomationAdapter extends BaseQuickAdapter<Integer> {
    private Context mContext;
    private List<Integer> list;
    private int num = 0;

    public ForeignInfomationAdapter(Context context, List<Integer> data) {
        super(R.layout.item_foreign_infomation, data);
        this.mContext = context;
        this.list = data;
    }


    @Override
    public int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return super.getViewHolderPosition(viewHolder);
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, Integer b) {

        LinearLayout linearLayout = viewHolder.getView(R.id.item_ll);

        viewHolder.setText(R.id.tv_time, b + "分钟以前");


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ForeignInfomationDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("focusable", false);
                mContext.startActivity(intent);
            }
        });


        linearLayout.findViewById(R.id.ll_cmt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ForeignInfomationDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("focusable", false);
                mContext.startActivity(intent);
            }
        });

        linearLayout.findViewById(R.id.iv_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://hiphotos.baidu.com/%B3%F5%BC%B6%BE%D1%BB%F7%CA%D6/pic/item/929b56443840bfc6b3b7dc64.jpg";
                Intent intent = new Intent(mContext, PicturePreviewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url", url);
                mContext.startActivity(intent);
            }
        });


        linearLayout.findViewById(R.id.ll_zan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.setText(R.id.tv_tight, (num++) + "");

            }
        });


    }

}

