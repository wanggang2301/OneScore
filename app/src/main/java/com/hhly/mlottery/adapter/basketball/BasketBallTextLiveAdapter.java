package com.hhly.mlottery.adapter.basketball;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;

import java.util.List;

/**
 * @author: Wangg
 * @Name：BasketBallTextLiveAdapter
 * @Description:
 * @Created on:2016/11/16  16:05.
 */

public class BasketBallTextLiveAdapter extends BaseQuickAdapter<String> {

    private List<String> list;


    public BasketBallTextLiveAdapter(int layoutResId, List<String> data) {

        super(layoutResId, data);
        list = data;

    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }


    @Override
    public int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return super.getViewHolderPosition(viewHolder);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {

        baseViewHolder.setText(R.id.tv, s + getViewHolderPosition(baseViewHolder));

    }
}
