package com.hhly.mlottery.adapter.basketball;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

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
    private BasketBallTextLiveAdapter.PullUpLoading mPullUpLoading;


    public BasketBallTextLiveAdapter(int layoutResId, List<String> data, Activity activity) {
        super(layoutResId, data);
        list = data;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int positions) {
        super.onBindViewHolder(holder, positions);
        if (positions == getItemCount() - 1) {//已经到达列表的底部
            if (mPullUpLoading != null) {
                mPullUpLoading.onPullUpLoading();
            }
        }
    }


    public void setPullUpLoading(BasketBallTextLiveAdapter.PullUpLoading pullUpLoading) {
        mPullUpLoading = pullUpLoading;
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {

        baseViewHolder.setText(R.id.tv, s + "----" + getViewHolderPosition(baseViewHolder));
    }

    public interface PullUpLoading {
        void onPullUpLoading();
    }

}
