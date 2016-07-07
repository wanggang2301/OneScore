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

    int yellow;
    int transparent;

    public FootballPlateDetailsLeftAdapter(List<OddsDataInfo.ListOddEntity> items) {
        super(R.layout.item_odds_left, items);
    }

    @Override
    protected void convert(BaseViewHolder holder, OddsDataInfo.ListOddEntity listOddEntity) {
        maybeInitColor();
        holder.setText(R.id.odds_left_txt, listOddEntity.getName());
        holder.setBackgroundColor(R.id.odds_left_txt, listOddEntity.isChecked() ? yellow : transparent);
    }

    private void maybeInitColor() {
        if (yellow == 0) {
            yellow = ContextCompat.getColor(mContext, R.color.yellow);
        }
    }
}
