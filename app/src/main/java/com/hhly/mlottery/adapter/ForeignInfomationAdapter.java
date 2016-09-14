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
 * 描述:  ${TODO}
 * 作者:  wangg@13322.com
 * 时间:  2016/9/12 12:11
 */
public class ForeignInfomationAdapter extends BaseQuickAdapter<Integer> {
    private Context mContext;
    private List<Integer> list;

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
    protected void convert(BaseViewHolder viewHolder, Integer b) {

        LinearLayout linearLayout = viewHolder.getView(R.id.item_ll);

        viewHolder.setText(R.id.tv_time, b + "分钟以前");


        linearLayout.findViewById(R.id.tv_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ForeignInfomationDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        linearLayout.findViewById(R.id.iv_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://bbs.tuwan.com/data/attachment/forum/201504/03/164410mfb6fr6c80a68gfu.png";
                Intent intent = new Intent(mContext, PicturePreviewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url", url);


                mContext.startActivity(intent);
            }
        });


    }

}

