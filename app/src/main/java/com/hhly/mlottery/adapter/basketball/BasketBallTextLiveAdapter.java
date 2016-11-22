package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.bean.basket.basketdetails.BasketEachTextLiveBean;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.view.CircleImageView;

import java.util.List;

/**
 * @author: Wangg
 * @Name：BasketBallTextLiveAdapter
 * @Description:篮球文字直播
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
                mPullUpLoading.onPullUpLoading();
            }
        }
    }

    public void setPullUpLoading(BasketBallTextLiveAdapter.PullUpLoading pullUpLoading) {
        mPullUpLoading = pullUpLoading;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BasketEachTextLiveBean b) {

        if (getViewHolderPosition(baseViewHolder) == 0) {
            baseViewHolder.getView(R.id.tv_top_line).setVisibility(View.INVISIBLE);
        } else {
            baseViewHolder.getView(R.id.tv_top_line).setVisibility(View.VISIBLE);
        }

        CircleImageView circleImageView = baseViewHolder.getView(R.id.ci_icon);
        String text = b.getEventContent();

        if ((b.getEventId() + "").startsWith("1", 0)) {  //公共事件 1开头
            circleImageView.setVisibility(View.GONE);
            baseViewHolder.getView(R.id.tv_remainTime).setVisibility(View.GONE);
            baseViewHolder.setText(R.id.tv_remainTime, "");
            baseViewHolder.getView(R.id.iv_topsanjiao).setVisibility(View.GONE);
            baseViewHolder.getView(R.id.tv_eventContent).setBackgroundResource(0);
            ((TextView) baseViewHolder.getView(R.id.tv_eventContent)).setTextColor(mContext.getResources().getColor(R.color.black_details_textcolor));
        } else {
            text += "  " + "<font color='#21b11e'>" + b.getGuestScore() + "-" + b.getHomeScore() + "</font>";
            circleImageView.setVisibility(View.VISIBLE);

            if (1 == b.getTeamType()) { //主队
                ImageLoader.load(mContext, BasketDetailsActivityTest.homeIconUrl, R.mipmap.basket_default).into(circleImageView);
            } else if (2 == b.getTeamType()) { //客队
                ImageLoader.load(mContext, BasketDetailsActivityTest.guestIconUrl, R.mipmap.basket_default).into(circleImageView);
            }

            if (b.getRemainTime() == null || "".equals(b.getRemainTime())) {
                baseViewHolder.getView(R.id.iv_topsanjiao).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_remainTime).setVisibility(View.GONE);
                baseViewHolder.setText(R.id.tv_remainTime, "");
            } else {
                baseViewHolder.getView(R.id.iv_topsanjiao).setVisibility(View.VISIBLE);
                baseViewHolder.getView(R.id.tv_remainTime).setVisibility(View.VISIBLE);
                baseViewHolder.setText(R.id.tv_remainTime, b.getRemainTime());
            }

            baseViewHolder.getView(R.id.tv_eventContent).setBackgroundResource(R.drawable.item_basket_eventontent_bg);
            ((TextView) baseViewHolder.getView(R.id.tv_eventContent)).setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
        }
        baseViewHolder.setText(R.id.tv_eventContent, Html.fromHtml(text));
    }

    public interface PullUpLoading {
        void onPullUpLoading();
    }
}
