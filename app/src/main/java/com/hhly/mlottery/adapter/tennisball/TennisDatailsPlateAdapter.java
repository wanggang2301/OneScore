package com.hhly.mlottery.adapter.tennisball;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;

import java.util.List;

/**
 * desc:网球内页亚盘adapter
 * Created by 107_tangrr on 2017/3/26 0026.
 */

public class TennisDatailsPlateAdapter extends BaseQuickAdapter<String>{

    public TennisDatailsPlateAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tennis_datails_plate_company_name,s);
        baseViewHolder.setText(R.id.tennis_datails_plate_now_zs,s);
        baseViewHolder.setText(R.id.tennis_datails_plate_now_pk,s);
        baseViewHolder.setText(R.id.tennis_datails_plate_now_ks,s);
        baseViewHolder.setText(R.id.tennis_datails_plate_first_zs,s);
        baseViewHolder.setText(R.id.tennis_datails_plate_first_pk,s);
        baseViewHolder.setText(R.id.tennis_datails_plate_first_ks,s);
    }
}
