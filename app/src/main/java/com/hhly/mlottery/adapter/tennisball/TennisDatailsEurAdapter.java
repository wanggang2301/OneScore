package com.hhly.mlottery.adapter.tennisball;

import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.tennisball.datails.odds.TennisDataBean;

import java.util.List;

/**
 * desc:网球内页欧赔adapter
 * Created by 107_tangrr on 2017/3/26 0026.
 */

public class TennisDatailsEurAdapter extends BaseQuickAdapter<TennisDataBean> {

    public TennisDatailsEurAdapter(int layoutResId, List<TennisDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TennisDataBean dataBean) {
        if (dataBean == null) return;

        baseViewHolder.setText(R.id.tennis_datails_eur_company_name, dataBean.getName() == null ? "" : dataBean.getName());
        if (dataBean.getDetails() != null) {
            double firstHomeOdd = 0;// 主胜  初盘
            double firstGuestOdd = 0;// 客胜  初盘
            double nowHomeOdd = 0;// 主胜  即盘
            double nowGuestOdd = 0;// 客胜  即盘
            for (int i = 0; i < dataBean.getDetails().size(); i++) {
                if (i == 0) {
                    // 初盘
                    firstHomeOdd = dataBean.getDetails().get(i).getHomeOdd();
                    firstGuestOdd = dataBean.getDetails().get(i).getGuestOdd();
                    baseViewHolder.setText(R.id.tennis_datails_eur_first_zs, String.valueOf(firstHomeOdd));
                    baseViewHolder.setText(R.id.tennis_datails_eur_first_ks, String.valueOf(firstGuestOdd));
                } else if (i == 1) {
                    // 即盘
                    nowHomeOdd = dataBean.getDetails().get(i).getHomeOdd();
                    nowGuestOdd = dataBean.getDetails().get(i).getGuestOdd();
                    baseViewHolder.setText(R.id.tennis_datails_eur_now_zs, String.valueOf(nowHomeOdd));
                    baseViewHolder.setText(R.id.tennis_datails_eur_now_ks, String.valueOf(nowGuestOdd));
                }
            }
            // 红升绿降
            if (firstHomeOdd == nowHomeOdd) {
                baseViewHolder.setTextColor(R.id.tennis_datails_eur_now_zs, ContextCompat.getColor(mContext, R.color.res_name_color));
            } else if (firstHomeOdd > nowHomeOdd) {
                baseViewHolder.setTextColor(R.id.tennis_datails_eur_now_zs, ContextCompat.getColor(mContext, R.color.number_green));
            } else {
                baseViewHolder.setTextColor(R.id.tennis_datails_eur_now_zs, ContextCompat.getColor(mContext, R.color.number_red));
            }
            if (firstGuestOdd == nowGuestOdd) {
                baseViewHolder.setTextColor(R.id.tennis_datails_eur_now_ks, ContextCompat.getColor(mContext, R.color.res_name_color));
            } else if (firstGuestOdd > nowGuestOdd) {
                baseViewHolder.setTextColor(R.id.tennis_datails_eur_now_ks, ContextCompat.getColor(mContext, R.color.number_green));
            } else {
                baseViewHolder.setTextColor(R.id.tennis_datails_eur_now_ks, ContextCompat.getColor(mContext, R.color.number_red));
            }
        }
    }
}
