package com.hhly.mlottery.adapter.cpiadapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.enums.OddsTypeEnum;
import com.hhly.mlottery.bean.oddsbean.OddsDataInfo;

import java.util.List;

/**
 * 足球详情、指数详情、左侧列表适配器
 * <p/>
 * Created by loshine on 2016/6/29.
 */
public class FootballPlateDetailsLeftAdapter extends BaseQuickAdapter<OddsDataInfo.ListOddEntity> {

    public FootballPlateDetailsLeftAdapter(List<OddsDataInfo.ListOddEntity> items) {
        super(R.layout.item_odds_left, items);
    }

    @Override
    protected void convert(BaseViewHolder holder, OddsDataInfo.ListOddEntity listOddEntity) {
        holder.setText(R.id.odds_left_txt, listOddEntity.getName());
    }
}
