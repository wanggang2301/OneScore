package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdetails.BasketEachTextLiveBean;

import java.util.List;

/**
 * @author: Wangg
 * @Name：BasketBallTextLiveAdapter
 * @Description:
 * @Created on:2016/11/16  16:05.
 */

public class BasketBallTextLiveAdapter extends BaseQuickAdapter<BasketEachTextLiveBean> {

    private Context mContext;
    private BasketBallTextLiveAdapter.PullUpLoading mPullUpLoading;


    public BasketBallTextLiveAdapter(int layoutResId, List<BasketEachTextLiveBean> data, Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int positions) {
        super.onBindViewHolder(holder, positions);
        if (positions == getItemCount() - 1) {//已经到达列表的底部
            if (mPullUpLoading != null) {
                //获取id
                mPullUpLoading.onPullUpLoading(((BasketEachTextLiveBean) getData().get(positions)).getId() + "");
            }
        }
    }

    public void setPullUpLoading(BasketBallTextLiveAdapter.PullUpLoading pullUpLoading) {
        mPullUpLoading = pullUpLoading;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BasketEachTextLiveBean b) {
        baseViewHolder.setText(R.id.tv_teamName, b.getTeamName() + "----" + getViewHolderPosition(baseViewHolder));
        baseViewHolder.setText(R.id.tv_content, b.getEventContent());
    }

    public interface PullUpLoading {
        void onPullUpLoading(String id);
    }



    //

}
