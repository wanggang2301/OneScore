package com.hhly.mlottery.adapter.cpiadapter.basket;

import android.content.Context;
import android.view.View;
import android.widget.Checkable;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.index.BasketIndexBean;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * @author: Wangg
 * @Name：BasketIndexFiltrateMatchAdapter
 * @Description:
 * @Created on:2017/3/21  14:49.
 */

public class BasketIndexFiltrateMatchAdapter extends CommonAdapter<BasketIndexBean.DataBean.FileterTagsBean> {
    private Context mContext;

    private List<String> mCheckedIds;

    public void setCheckedIds(List<String> mCheckedIds) {
        this.mCheckedIds = mCheckedIds;
    }

    public BasketIndexFiltrateMatchAdapter(Context context, List<BasketIndexBean.DataBean.FileterTagsBean> fileterTags, List<String> checkedIds, int layoutId) {
        super(context, fileterTags, layoutId);
        mContext = context;
        mCheckedIds = checkedIds;

    }

    @Override
    public void convert(ViewHolder holder, final BasketIndexBean.DataBean.FileterTagsBean fileterTagsBean) {
        holder.setText(R.id.item_cpi_filtrate_checkbox, fileterTagsBean.getLeagueName() + "[" + fileterTagsBean.getCount() + "]");
        //设置联赛颜色
        if (mCheckedIds.contains(fileterTagsBean.getLeagueId())) {
            holder.setChecked(R.id.item_cpi_filtrate_checkbox, true);
        } else {
            holder.setChecked(R.id.item_cpi_filtrate_checkbox, false);
        }

        holder.setOnClickListener(R.id.item_cpi_filtrate_checkbox, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListenerListener != null && v instanceof Checkable) {
                    onItemClickListenerListener.onClick(v, ((Checkable) v).isChecked(), fileterTagsBean);
                }
            }
        });

    }

    private OnItemClickListenerListener onItemClickListenerListener;

    public void setOnItemCheckedChangedListener(OnItemClickListenerListener onItemClickListenerListener) {
        this.onItemClickListenerListener = onItemClickListenerListener;
    }

    public interface OnItemClickListenerListener {
        void onClick(View buttonView, boolean isChecked, BasketIndexBean.DataBean.FileterTagsBean fileterTagsBean);
    }

}

