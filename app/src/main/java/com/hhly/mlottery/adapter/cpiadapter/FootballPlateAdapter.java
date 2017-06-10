package com.hhly.mlottery.adapter.cpiadapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.enums.OddsTypeEnum;
import com.hhly.mlottery.bean.oddsbean.OddsDataInfo;
import com.hhly.mlottery.util.HandicapUtils;

import java.util.List;
import java.util.Locale;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/6/28.
 */
public class FootballPlateAdapter extends BaseQuickAdapter<OddsDataInfo.ListOddEntity> {

    int red;
    int black;
    int green;
    int white;

    @OddsTypeEnum.OddsType
    private String type;

    public FootballPlateAdapter(@OddsTypeEnum.OddsType String type,
                                List<OddsDataInfo.ListOddEntity> items) {
        super(R.layout.item_odds_football, items);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, OddsDataInfo.ListOddEntity item) {
        maybeInitColor(holder.getConvertView().getContext());

        List<OddsDataInfo.ListOddEntity.DetailsEntity> details = item.getDetails();
        // 初赔
        OddsDataInfo.ListOddEntity.DetailsEntity preOdds = details.get(0);
        // 即赔
        OddsDataInfo.ListOddEntity.DetailsEntity currentOdds = details.get(1);

        // 公司名
        holder.setText(R.id.company, item.getName());

        // 右边
        holder.setTextColor(R.id.plate_homeOdds_txt2, black);
        setOddsColor(holder, currentOdds.getHomeOdd(), preOdds.getHomeOdd(), R.id.plate_homeOdds_txt);
        holder.setText(R.id.plate_homeOdds_txt2, String.format(Locale.US, "%.2f", preOdds.getHomeOdd()));
        holder.setText(R.id.plate_homeOdds_txt, String.format(Locale.US, "%.2f", currentOdds.getHomeOdd()));

        // 中间
        holder.setTextColor(R.id.plate_dish_txt2, black);
        setOddsColor(holder, currentOdds.getHand(), preOdds.getHand(), R.id.plate_dish_txt);
        if (!OddsTypeEnum.OP.equals(type)) {
            setCenterOddsColor(holder, currentOdds.getHand(), preOdds.getHand(), R.id.plate_dish_txt);
        }

        if ("0".equals(item.getId())) {
            holder.setText(R.id.plate_dish_txt, String.valueOf(currentOdds.getHand()));
            holder.setText(R.id.plate_dish_txt2, String.valueOf(preOdds.getHand()));
        } else {
            setHandicap(holder, currentOdds, preOdds);
        }

        // 左边
        holder.setTextColor(R.id.plate_guestOdds_txt2, black);
        setOddsColor(holder, currentOdds.getGuestOdd(), preOdds.getGuestOdd(), R.id.plate_guestOdds_txt);
        holder.setText(R.id.plate_guestOdds_txt2, String.format(Locale.US, "%.2f", preOdds.getGuestOdd()));
        holder.setText(R.id.plate_guestOdds_txt, String.format(Locale.US, "%.2f", currentOdds.getGuestOdd()));
    }

    /**
     * 设置盘口数字
     *
     * @param holder      holder
     * @param currentOdds currentOdds
     * @param preOdds     preOdds
     */
    private void setHandicap(BaseViewHolder holder,
                             OddsDataInfo.ListOddEntity.DetailsEntity currentOdds,
                             OddsDataInfo.ListOddEntity.DetailsEntity preOdds) {
        String preOddsText = preOdds.getHand() + "";
        String currentOddsText = currentOdds.getHand() + "";
        if (OddsTypeEnum.PLATE.equals(type)) {
            preOddsText = HandicapUtils.changeHandicap(preOddsText);
            currentOddsText = HandicapUtils.changeHandicap(currentOddsText);
        } else if (OddsTypeEnum.BIG.equals(type)) {
            preOddsText = HandicapUtils.changeHandicapByBigLittleBall(preOddsText);
            currentOddsText = HandicapUtils.changeHandicapByBigLittleBall(currentOddsText);
        } else if (OddsTypeEnum.OP.equals(type)) {
            preOddsText = String.format(Locale.US, "%.2f", preOdds.getHand());
            currentOddsText = String.format(Locale.US, "%.2f", currentOdds.getHand());
        }

        holder.setText(R.id.plate_dish_txt, currentOddsText)
                .setText(R.id.plate_dish_txt2, preOddsText);
    }

    /**
     * 设置即时赔率的颜色
     *
     * @param holder  holder
     * @param current 即赔
     * @param pre     初赔
     * @param idRes   idRes
     */
    private void setOddsColor(BaseViewHolder holder, double current, double pre, @IdRes int idRes) {
        int homeColor;
        if (current > pre) {
            homeColor = red;
        } else if (current < pre) {
            homeColor = green;
        } else {
            homeColor = black;
        }
        holder.setTextColor(idRes, homeColor);
    }

    /**
     * 设置盘口颜色
     *
     * @param holder  holder
     * @param current 即赔
     * @param pre     初赔
     * @param idRes   idRes
     */
    private void setCenterOddsColor(BaseViewHolder holder, double current, double pre, int idRes) {
        if (current == pre) {
            holder.setTextColor(idRes, black);
            holder.setBackgroundColor(idRes, white);
        } else {
            int color;
            if (current > pre) {
                color = red;
            } else {
                color = green;
            }
            holder.setBackgroundColor(idRes, color);
            holder.setTextColor(idRes, white);
        }
    }

    /**
     * 初始化获取颜色
     */
    private void maybeInitColor(Context context) {
        if (red == 0) {
            red = ContextCompat.getColor(context, R.color.odds_details);
        }
        if (black == 0) {
            black = ContextCompat.getColor(context, R.color.content_txt_black);
        }
        if (green == 0) {
            green = ContextCompat.getColor(context, R.color.odds_down_bg);
        }
        if (white == 0) {
            white = ContextCompat.getColor(context, R.color.white);
        }
    }
}
