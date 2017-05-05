package com.hhly.mlottery.adapter.bettingadapter;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.bettingbean.BettingSettingItenDataBean;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * Created by：yxq on 2017/4/24 15:21
 * Use:
 */
public class BettingRecommendSettingAdapter extends CommonAdapter<BettingSettingItenDataBean> {

    private List<String> chickedList;
    public BettingRecommendSettingAdapter(Context context,List<String> chickedList,List<BettingSettingItenDataBean> datas,int layoutId) {
        super(context, datas, layoutId);
        this.chickedList = chickedList;
    }

    @Override
    public void convert(ViewHolder holder, BettingSettingItenDataBean dataBean) {

        holder.setTag(R.id.betting_set_checkbox , dataBean.getId());
        holder.setText(R.id.betting_set_checkbox , dataBean.getName());
        if (chickedList.contains(dataBean.getId())) {
            holder.setChecked(R.id.betting_set_checkbox , true);
        }else{
            holder.setChecked(R.id.betting_set_checkbox , false);
        }

        holder.setOnCheckedChangeListener(R.id.betting_set_checkbox, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!chickedList.contains(buttonView.getTag())) {
                        chickedList.add((String) buttonView.getTag());
                    }
                }else{
                    if (chickedList.contains(buttonView.getTag())) {
                        chickedList.remove(buttonView.getTag());
                    }
                }
            }
        });
        holder.setOnClickListener(R.id.betting_set_checkbox, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickChangeListener != null) {
                    clickChangeListener.onClick((CompoundButton) v);
                }
            }
        });
        holder.setClickable(R.id.betting_set_checkbox , true);
    }


    private BettingClickChangeListener clickChangeListener;
    public void setClickChangeListener(BettingClickChangeListener clickChangeListener) {
        this.clickChangeListener = clickChangeListener;
    }
    public interface BettingClickChangeListener {
        void onClick(CompoundButton buttonView);
    }
}
