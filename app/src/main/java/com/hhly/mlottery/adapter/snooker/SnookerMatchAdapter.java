package com.hhly.mlottery.adapter.snooker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.SnookerEventPageActivity;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchBean;
import com.hhly.mlottery.util.ImageLoader;

import java.util.List;

/**
 * @author: Wangg
 * @Name：SnookerMatchAdapter
 * @Description:斯洛克资料库赛事Adapter
 * @Created on:2017/2/20  14:51.
 */

public class SnookerMatchAdapter extends BaseQuickAdapter<SnookerMatchBean.DataBean> {
    private Context mContext;
    private List<SnookerMatchBean.DataBean> dataBeanList;

    public SnookerMatchAdapter(Context context, List<SnookerMatchBean.DataBean> data) {
        super(R.layout.item_snooker_match, data);
        mContext = context;
        dataBeanList = data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final SnookerMatchBean.DataBean dataBean) {
        RelativeLayout rl_match = (RelativeLayout) holder.getView(R.id.rl_match);

        if (dataBean.getLeagueLogo() != null) {
            ImageLoader.load(mContext, dataBean.getLeagueLogo(),R.mipmap.snooker_default).into((ImageView) holder.getView(R.id.snooker_iv_logo));

            // Glide.with(mContext).load(dataBean.getLeagueLogo()).into((ImageView) holder.getView(R.id.snooker_iv_logo));
        } else {
            holder.setImageResource(R.id.snooker_iv_logo, R.mipmap.snooker_default);
        }

        holder.setText(R.id.snooker_race_name, dataBean.getLeagueName());
        rl_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SnookerEventPageActivity.class);
                intent.putExtra("leagueId", dataBean.getLeagueId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return super.getViewHolderPosition(viewHolder);
    }
}