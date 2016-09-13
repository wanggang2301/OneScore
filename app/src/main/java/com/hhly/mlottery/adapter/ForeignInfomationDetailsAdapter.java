package com.hhly.mlottery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;

import java.util.List;

/**
 * 描述:  ${TODO}
 * 作者:  wangg@13322.com
 * 时间:  2016/9/12 18:16
 */
public class ForeignInfomationDetailsAdapter extends BaseQuickAdapter<Integer> {
    private Context mContext;
    private List<Integer> list;

    public ForeignInfomationDetailsAdapter(Context context, List<Integer> data) {
        super(R.layout.item_counsel_comment, data);
        this.mContext = context;
        this.list = data;
    }


    @Override
    public int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return super.getViewHolderPosition(viewHolder);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Integer b) {


        viewHolder.setText(R.id.tv_nickname, "奥斯特洛夫斯基");
        viewHolder.setText(R.id.tv_time, b + "分钟以前");
        viewHolder.setText(R.id.tv_content, " 我们有理由庆祝，生日快乐我的爱！");


    }

}

