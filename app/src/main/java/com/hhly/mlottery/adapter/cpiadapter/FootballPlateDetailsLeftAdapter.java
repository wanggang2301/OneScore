package com.hhly.mlottery.adapter.cpiadapter;

import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.oddsbean.OddsDataInfo;

import java.util.List;

/**
 * 足球详情、指数详情、左侧列表适配器
 * <p/>
 * Created by loshine on 2016/6/29.
 */
public class FootballPlateDetailsLeftAdapter extends BaseQuickAdapter<OddsDataInfo.ListOddEntity> {

    int grey;
    int white;
    int blue;
    int black;

    public FootballPlateDetailsLeftAdapter(List<OddsDataInfo.ListOddEntity> items) {
        super(R.layout.item_odds_left, items);
    }

    @Override
    protected void convert(BaseViewHolder holder, OddsDataInfo.ListOddEntity listOddEntity) {
        maybeInitColor();
        holder.setText(R.id.odds_left_txt, listOddEntity.getName())
                .setTextColor(R.id.odds_left_txt, listOddEntity.isChecked() ? blue : black)
                .setVisible(R.id.left_circle, listOddEntity.isChecked())
                .setBackgroundColor(R.id.container, listOddEntity.isChecked() ? white : grey);
    }

    private void maybeInitColor() {
        if (grey == 0) {
            grey = ContextCompat.getColor(mContext, R.color.whitesmoke);
        }
        if (white == 0) {
            white = ContextCompat.getColor(mContext, R.color.white);
        }
        if (blue == 0) {
            blue = ContextCompat.getColor(mContext, R.color.colorPrimary);
        }
        if (black == 0) {
            black = ContextCompat.getColor(mContext, R.color.content_txt_black);
        }
    }
}
