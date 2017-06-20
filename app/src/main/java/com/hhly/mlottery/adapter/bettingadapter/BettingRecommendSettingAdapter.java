package com.hhly.mlottery.adapter.bettingadapter;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.bettingbean.BettingListDataBean;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * Created by：yxq on 2017/4/24 15:21
 * Use:推荐设置页面adapter
 */
public class BettingRecommendSettingAdapter extends CommonAdapter<BettingListDataBean.LeagueNameData> {

    private List<String> chickedList;
    private boolean singleCheck;
    public BettingRecommendSettingAdapter(Context context, List<String> chickedList, List<BettingListDataBean.LeagueNameData> datas, int layoutId , boolean singleCheck) {
        super(context, datas, layoutId);
        this.chickedList = chickedList;
        this.singleCheck = singleCheck;
    }

    @Override
    public void convert(ViewHolder holder, BettingListDataBean.LeagueNameData dataBean) {

        holder.setTag(R.id.betting_set_checkbox , dataBean.getKey());
        holder.setText(R.id.betting_set_checkbox , dataBean.getLeagueName());
        if (chickedList.contains(dataBean.getKey())) {
            holder.setChecked(R.id.betting_set_checkbox , true);
        }else{
            holder.setChecked(R.id.betting_set_checkbox , false);
        }

        holder.setOnCheckedChangeListener(R.id.betting_set_checkbox, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (singleCheck) { //单选
                    if (isChecked) {
                        if (!chickedList.contains(buttonView.getTag())) {
                            chickedList.clear();
                            chickedList.add((String) buttonView.getTag());
                        }
                    }else{
                        if (chickedList.contains(buttonView.getTag())) {
                            Toast.makeText(mContext, mContext.getResources().getText(R.string.betting_paly_choose_txt), Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{ //多选
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



//TODO***************************************************

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
