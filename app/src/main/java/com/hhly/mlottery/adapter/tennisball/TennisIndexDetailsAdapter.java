package com.hhly.mlottery.adapter.tennisball;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.enums.TennisOddsTypeEnum;
import com.hhly.mlottery.bean.tennisball.tennisindex.TennisIndexDetailsBean;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.HandicapUtils;

import java.util.List;

/**
 * 描    述：网球指数详情页adapter
 * 作    者：mady@13322.com
 * 时    间：2017/4/8
 */
public class TennisIndexDetailsAdapter extends BaseQuickAdapter<TennisIndexDetailsBean.DataEntity.OddsDataEntity>{

    private String oddType;
    private Context mContext;
    public TennisIndexDetailsAdapter(Context context, List<TennisIndexDetailsBean.DataEntity.OddsDataEntity> data,String oddType) {
        super(R.layout.item_odds_details_child, data);
        this.oddType = oddType;
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TennisIndexDetailsBean.DataEntity.OddsDataEntity oddsDataBean) {
        baseViewHolder.setText(R.id.odds_details_home_txt, oddsDataBean.getLeft());
        baseViewHolder.setText(R.id.odds_details_guest_txt, oddsDataBean.getRight());

        if (oddType.equals(TennisOddsTypeEnum.ASIALET)) {
            baseViewHolder.getView(R.id.odds_details_dish_layout).setVisibility(View.VISIBLE);

            baseViewHolder.setText(R.id.odds_details_dish_txt, HandicapUtils.changeHandicap(oddsDataBean.getMiddle()));

        } else if (oddType.equals(TennisOddsTypeEnum.ASIASIZE)) {
            baseViewHolder.getView(R.id.odds_details_dish_layout).setVisibility(View.VISIBLE);

            baseViewHolder.setText(R.id.odds_details_dish_txt, HandicapUtils.changeHandicapByBigLittleBall(oddsDataBean.getMiddle() + ""));

        } else if (oddType.equals(TennisOddsTypeEnum.EURO)) {
            baseViewHolder.getView(R.id.odds_details_dish_layout).setVisibility(View.GONE);
        }


        if (getViewHolderPosition(baseViewHolder) == getItemCount() - 1) { //最后一项不用颜色
            baseViewHolder.setText(R.id.odds_details_timeAndscore_txt, mContext.getResources().getString(R.string.frame_cpi_chupan_txt));

            baseViewHolder.setTextColor(R.id.odds_details_home_txt, mContext.getResources().getColor(R.color.content_txt_dark_grad));
            baseViewHolder.setTextColor(R.id.odds_details_dish_txt, mContext.getResources().getColor(R.color.content_txt_dark_grad));
            baseViewHolder.setTextColor(R.id.odds_details_guest_txt, mContext.getResources().getColor(R.color.content_txt_dark_grad));

        } else {
            if(oddsDataBean.getOddDate()!=null){
//                String s[]=new String[10];
//                s=oddsDataBean.getOddDate().split("-");
//                String s1=s[1];String s2=s[2];
                String s= DateUtil.convertDateToNationMD(oddsDataBean.getOddDate().substring(5,10));
                String date = s + "\n" + oddsDataBean.getOddTime();
                baseViewHolder.setText(R.id.odds_details_timeAndscore_txt, date);
            }



            setColor((TextView) baseViewHolder.getView(R.id.odds_details_home_txt), oddsDataBean.getLeftStatus());
            setColor((TextView) baseViewHolder.getView(R.id.odds_details_dish_txt), oddsDataBean.getMiddleStatus());
            setColor((TextView) baseViewHolder.getView(R.id.odds_details_guest_txt), oddsDataBean.getRightStatus());

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
