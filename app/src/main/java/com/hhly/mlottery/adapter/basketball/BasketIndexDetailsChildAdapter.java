package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.index.BasketIndexDetailsBean;
import com.hhly.mlottery.bean.enums.BasketOddsTypeEnum;
import com.hhly.mlottery.util.HandicapUtils;

import java.util.List;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/3/23  14:26.
 */

public class BasketIndexDetailsChildAdapter extends BaseQuickAdapter<BasketIndexDetailsBean.OddsDataBean> {

    private String oddType;
    private Context mContext;

    public BasketIndexDetailsChildAdapter(Context context, List<BasketIndexDetailsBean.OddsDataBean> data, String oddType) {
        super(R.layout.item_odds_details_child, data);
        this.oddType = oddType;
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BasketIndexDetailsBean.OddsDataBean oddsDataBean) {


        baseViewHolder.setText(R.id.odds_details_home_txt, oddsDataBean.getLeftOdds());
        baseViewHolder.setText(R.id.odds_details_guest_txt, oddsDataBean.getRightOdds());

        if (oddType.equals(BasketOddsTypeEnum.ASIALET)) {
            baseViewHolder.getView(R.id.odds_details_dish_txt).setVisibility(View.VISIBLE);

            baseViewHolder.setText(R.id.odds_details_dish_txt, HandicapUtils.changeHandicap(oddsDataBean.getHandicapValue()));

        } else if (oddType.equals(BasketOddsTypeEnum.ASIASIZE)) {
            baseViewHolder.getView(R.id.odds_details_dish_txt).setVisibility(View.VISIBLE);

            baseViewHolder.setText(R.id.odds_details_dish_txt, HandicapUtils.changeHandicapByBigLittleBall(oddsDataBean.getHandicapValue() + ""));

        } else if (oddType.equals(BasketOddsTypeEnum.EURO)) {
            baseViewHolder.getView(R.id.odds_details_dish_txt).setVisibility(View.GONE);
        }


        if (getViewHolderPosition(baseViewHolder) == getItemCount() - 1) { //最后一项不用颜色
            baseViewHolder.setText(R.id.odds_details_timeAndscore_txt, mContext.getResources().getString(R.string.frame_cpi_chupan_txt));

            baseViewHolder.setTextColor(R.id.odds_details_home_txt, mContext.getResources().getColor(R.color.content_txt_dark_grad));
            baseViewHolder.setTextColor(R.id.odds_details_dish_txt, mContext.getResources().getColor(R.color.content_txt_dark_grad));
            baseViewHolder.setTextColor(R.id.odds_details_guest_txt, mContext.getResources().getColor(R.color.content_txt_dark_grad));

        } else {

            String date = oddsDataBean.getUpdateTime().split(" ")[0] + "\n" + oddsDataBean.getUpdateTime().split(" ")[1];
            baseViewHolder.setText(R.id.odds_details_timeAndscore_txt, date);

            setColor((TextView) baseViewHolder.getView(R.id.odds_details_home_txt), oddsDataBean.getLeftOddsTrend());
            setColor((TextView) baseViewHolder.getView(R.id.odds_details_dish_txt), oddsDataBean.getHandicapValueTrend());
            setColor((TextView) baseViewHolder.getView(R.id.odds_details_guest_txt), oddsDataBean.getRightOddsTrend());

        }


    }


    private void setColor(TextView view, int trend) {
        if (trend > 0) {
            view.setTextColor(mContext.getResources().getColor(R.color.analyze_left));
        } else if (trend < 0) {
            view.setTextColor(mContext.getResources().getColor(R.color.fall_color));
        } else {
            view.setTextColor(mContext.getResources().getColor(R.color.content_txt_dark_grad));
        }

    }
}
