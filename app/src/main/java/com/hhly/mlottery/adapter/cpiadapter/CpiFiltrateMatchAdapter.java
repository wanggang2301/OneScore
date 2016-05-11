package com.hhly.mlottery.adapter.cpiadapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.LeagueCup;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.LinkedList;
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
        //设置tag
//        holder.setTag(R.id.item_filtrate_checkbox, fileterTagsBean.getLeagueId());
        //设置联赛名称和场次
        holder.setText(R.id.item_filtrate_checkbox, fileterTagsBean.getLeagueName() + "[" + fileterTagsBean.getMatchsInLeague() + "]");
        //设置联赛颜色
        holder.setTextColorString(R.id.item_filtrate_checkbox, fileterTagsBean.getLeagueColor());
        if (mCheckedIds.contains(fileterTagsBean.getLeagueId())) {
            holder.setChecked(R.id.item_filtrate_checkbox, true);
        } else {
            holder.setChecked(R.id.item_filtrate_checkbox, false);
        }

        Log.d("CpiFiltrateMatchAdapter","fileterTagsBean.getLeagueId() = "+fileterTagsBean.getLeagueId());

        holder.setOnCheckedChangeListener(R.id.item_filtrate_checkbox, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onItemCheckedChangedListener != null) {
                    onItemCheckedChangedListener.onCheckedChanged(buttonView, isChecked, fileterTagsBean);
                }

            }
        });
    }

    private OnItemCheckedChangedListener onItemCheckedChangedListener;

    public void setOnItemCheckedChangedListener(OnItemCheckedChangedListener onItemCheckedChangedListener) {
        this.onItemCheckedChangedListener = onItemCheckedChangedListener;
    }


    public interface OnItemCheckedChangedListener {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked, NewOddsInfo.FileterTagsBean fileterTagsBean);
    }


}
