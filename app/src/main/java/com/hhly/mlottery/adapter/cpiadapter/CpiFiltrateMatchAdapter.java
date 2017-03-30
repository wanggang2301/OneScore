package com.hhly.mlottery.adapter.cpiadapter;

import android.content.Context;
import android.view.View;
import android.widget.Checkable;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * Created by 103TJL on 2016/5/9.
 * 新版指数筛选
 */
public class CpiFiltrateMatchAdapter extends CommonAdapter<NewOddsInfo.FileterTagsBean> {
    private Context mContext;

    private List<String> mCheckedIds;

    public void setCheckedIds(List<String> mCheckedIds) {
        this.mCheckedIds = mCheckedIds;
    }

    public CpiFiltrateMatchAdapter(Context context, List<NewOddsInfo.FileterTagsBean> fileterTags, List<String> checkedIds, int layoutId) {
        super(context, fileterTags, layoutId);
        mContext = context;
        mCheckedIds = checkedIds;

    }

    @Override
    public void convert(ViewHolder holder, final NewOddsInfo.FileterTagsBean fileterTagsBean) {
        holder.setText(R.id.item_cpi_filtrate_checkbox, fileterTagsBean.getLeagueName() + "[" + fileterTagsBean.getMatchsInLeague() + "]");
        //设置联赛颜色
        if (fileterTagsBean.getLeagueColor() != null) {
            holder.setTextColorString(R.id.item_cpi_filtrate_checkbox, fileterTagsBean.getLeagueColor());
        }
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
        void onClick(View buttonView, boolean isChecked, NewOddsInfo.FileterTagsBean fileterTagsBean);
    }


}
