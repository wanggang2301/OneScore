package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.text.Html;
import android.view.View;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdetails.OddsDataEntity;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * Created by A on 2016/4/5.
 */
public class BasketOddsDetailsAdapter extends CommonAdapter<OddsDataEntity> {

//    private Context mContext;
    private boolean isEuro;
    private List<OddsDataEntity> mDatas;

    public BasketOddsDetailsAdapter(Context context, List<OddsDataEntity> datas, int layoutId ,boolean isEuro) {
        super(context, datas, layoutId);

//        this.mContext = context;
        this.isEuro = isEuro;
        this.mDatas = datas;
    }

    public void upDataList(List<OddsDataEntity> datas){
        this.mDatas = datas;

    }
    @Override
    public void convert(ViewHolder holder, OddsDataEntity oddBean) {

            holder.setText(R.id.basket_odds_details_time , oddBean.getUpdateTime());

        //holder.setText(R.id.basket_odds_details_guestwin , oddBean.getLeftOdds());
//        holder.setText(R.id.basket_odds_details_homewin , oddBean.getRightOdds());

        if (oddBean.getLeftOddsTrend() == 1) {
//            holder.setText(R.id.basket_odds_details_guestwin, Html.fromHtml("<font color='#FF1F1F'><b>" + oddBean.getLeftOdds() + "</b></font>"));
            holder.setText(R.id.basket_odds_details_guestwin, oddBean.getLeftOdds());
            holder.setTextColorRes(R.id.basket_odds_details_guestwin ,R.color.odds_up);
        }else if(oddBean.getLeftOddsTrend() == -1){
//            holder.setText(R.id.basket_odds_details_guestwin, Html.fromHtml("<font color='#21b11e'><b>" + oddBean.getLeftOdds() + "</b></font>"));
            holder.setText(R.id.basket_odds_details_guestwin , oddBean.getLeftOdds());
            holder.setTextColorRes(R.id.basket_odds_details_guestwin, R.color.odds_down);
        }else{
//            holder.setText(R.id.basket_odds_details_guestwin, Html.fromHtml("<font color='#FFFFFF'><b>" + oddBean.getLeftOdds() + "</b></font>"));
            holder.setText(R.id.basket_odds_details_guestwin , oddBean.getLeftOdds());
            holder.setTextColorRes(R.id.basket_odds_details_guestwin, R.color.mdy_333);
        }

        if (oddBean.getRightOddsTrend() == 1) {
//            holder.setText(R.id.basket_odds_details_homewin, Html.fromHtml("<font color='#FF1F1F'><b>" + oddBean.getRightOdds() + "</b></font>"));
            holder.setText(R.id.basket_odds_details_homewin, oddBean.getRightOdds());
            holder.setTextColorRes(R.id.basket_odds_details_homewin, R.color.odds_up);

        }else if (oddBean.getRightOddsTrend() == -1) {
//            holder.setText(R.id.basket_odds_details_homewin, Html.fromHtml("<font color='#21b11e'><b>" + oddBean.getRightOdds() + "</b></font>"));
            holder.setText(R.id.basket_odds_details_homewin, oddBean.getRightOdds());
            holder.setTextColorRes(R.id.basket_odds_details_homewin, R.color.odds_down);
        }else{
//            holder.setText(R.id.basket_odds_details_homewin, Html.fromHtml("<font color='#FFFFFF'><b>" + oddBean.getRightOdds() + "</b></font>"));
            holder.setText(R.id.basket_odds_details_homewin, oddBean.getRightOdds());
            holder.setTextColorRes(R.id.basket_odds_details_homewin, R.color.mdy_333);
        }


            if (oddBean.getHandicapValue() == null) {
                if (!isEuro) {
                    holder.setVisible(R.id.basket_odds_handicp_ll ,true);
                    holder.setText(R.id.basket_odds_details_handicap ,  Html.fromHtml("<font color='#999999'><b>" + "--" + "</b></font>"));
                    holder.setVisible(R.id.basket_odds_details_image , false);
                }
            }else {
                if (!isEuro) {
                        holder.setVisible(R.id.basket_odds_handicp_ll ,true);

                        if (oddBean.getHandicapValueTrend() == 1) {
                            holder.setBackgroundRes(R.id.basket_odds_details_image, R.mipmap.red);
//                            holder.setText(R.id.basket_odds_details_handicap, Html.fromHtml("<font color='#FF1F1F'><b>" + oddBean.getHandicapValue() + "</b></font>"));
                            holder.setText(R.id.basket_odds_details_handicap , oddBean.getHandicapValue());
                            holder.setBackgroundColorRes(R.id.basket_odds_details_handicap, R.color.odds_up);
                            holder.setVisible(R.id.basket_odds_details_image, View.INVISIBLE);
                        } else if (oddBean.getHandicapValueTrend() == -1) {
                            holder.setBackgroundRes(R.id.basket_odds_details_image, R.mipmap.green);
//                            holder.setText(R.id.basket_odds_details_handicap, Html.fromHtml("<font color='#21b11e'><b>" + oddBean.getHandicapValue() + "</b></font>"));
                            holder.setText(R.id.basket_odds_details_handicap , oddBean.getHandicapValue());
                            holder.setBackgroundColorRes(R.id.basket_odds_details_handicap, R.color.odds_down);
                            holder.setVisible(R.id.basket_odds_details_image, View.INVISIBLE);
                        } else {
                            holder.setVisible(R.id.basket_odds_details_image, View.INVISIBLE);
//                            holder.setText(R.id.basket_odds_details_handicap, Html.fromHtml("<font color='#FFFFFF'><b>" + oddBean.getHandicapValue() + "</b></font>"));
                            holder.setText(R.id.basket_odds_details_handicap , oddBean.getHandicapValue());
                            holder.setBackgroundColorRes(R.id.basket_odds_details_handicap, R.color.whitesmoke);
                        }
                } else {
                    holder.setVisible(R.id.basket_odds_handicp_ll ,false);
                }
            }
    }
}
