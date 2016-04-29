package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.text.Html;
import android.view.View;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.BasketDetails.BasketAnalyzeMoreFutureBean;
import com.hhly.mlottery.bean.basket.BasketDetails.BasketDetailOddsDetailsBean;
import com.hhly.mlottery.bean.basket.BasketDetails.OddsDataEntity;
import com.hhly.mlottery.util.L;
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
            holder.setText(R.id.basket_odds_details_guestwin , oddBean.getLeftOdds());
            holder.setText(R.id.basket_odds_details_homewin , oddBean.getRightOdds());


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
                            holder.setText(R.id.basket_odds_details_handicap, Html.fromHtml("<font color='#FF1F1F'><b>" + oddBean.getHandicapValue() + "</b></font>"));
                        } else if (oddBean.getHandicapValueTrend() == -1) {
                            holder.setBackgroundRes(R.id.basket_odds_details_image, R.mipmap.green);
                            holder.setText(R.id.basket_odds_details_handicap, Html.fromHtml("<font color='#21b11e'><b>" + oddBean.getHandicapValue() + "</b></font>"));
                        } else {
                            holder.setVisible(R.id.basket_odds_details_image, View.INVISIBLE);
                            holder.setText(R.id.basket_odds_details_handicap, Html.fromHtml("<font color='#999999'><b>" + oddBean.getHandicapValue() + "</b></font>"));
                        }
                } else {
                    holder.setVisible(R.id.basket_odds_handicp_ll ,false);
//                    holder.setVisible(R.id.basket_odds_details_handicap, View.GONE);
//                    holder.setVisible(R.id.basket_odds_details_image, false);
                }
            }
    }
}
