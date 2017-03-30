package com.hhly.mlottery.adapter.tennisball;

import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.tennisball.datails.odds.TennisDataBean;
import com.hhly.mlottery.util.L;

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
            for (int i = 0; i < dataBean.getDetails().size(); i++) {
                double homeOdd = dataBean.getDetails().get(i).getHomeOdd();
                double guestOdd = dataBean.getDetails().get(i).getGuestOdd();
                if (i == 0) {
                    // 初盘
                    baseViewHolder.setText(R.id.tennis_datails_eur_first_zs, String.valueOf(homeOdd));
                    baseViewHolder.setText(R.id.tennis_datails_eur_first_ks, String.valueOf(guestOdd));
                } else if (i == 1) {
                    // 即盘
                    baseViewHolder.setText(R.id.tennis_datails_eur_now_zs, String.valueOf(homeOdd));
                    baseViewHolder.setText(R.id.tennis_datails_eur_now_ks, String.valueOf(guestOdd));
                    if (homeOdd == guestOdd) {
                        baseViewHolder.setTextColor(R.id.tennis_datails_eur_now_zs, ContextCompat.getColor(mContext, R.color.res_name_color));
                        baseViewHolder.setTextColor(R.id.tennis_datails_eur_now_ks, ContextCompat.getColor(mContext, R.color.res_name_color));
                    } else if (homeOdd > guestOdd) {
                        baseViewHolder.setTextColor(R.id.tennis_datails_eur_now_zs, ContextCompat.getColor(mContext, R.color.number_red));
                        baseViewHolder.setTextColor(R.id.tennis_datails_eur_now_ks, ContextCompat.getColor(mContext, R.color.number_green));
                    } else {
                        baseViewHolder.setTextColor(R.id.tennis_datails_eur_now_zs, ContextCompat.getColor(mContext, R.color.number_green));
                        baseViewHolder.setTextColor(R.id.tennis_datails_eur_now_ks, ContextCompat.getColor(mContext, R.color.number_red));
                    }
                }
            }
        }
    }
}
