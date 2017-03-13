package com.hhly.mlottery.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.infoCenterBean.IntelligencesEntity;
import com.hhly.mlottery.util.DateUtil;

import java.util.List;

/**
 * 描  述：
 * 作  者：tangrr@13322.com
 * 时  间：2016/9/6
 */

public class InfoCenterSelectAdapter extends BaseQuickAdapter<IntelligencesEntity> {


    public InfoCenterSelectAdapter(int layoutResId, List<IntelligencesEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, IntelligencesEntity intelligencesEntity) {
        baseViewHolder.setText(R.id.tv_pw_week, DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(intelligencesEntity.date)));
        baseViewHolder.setText(R.id.tv_pw_date, DateUtil.convertDateToNationMD(intelligencesEntity.date.substring(5,intelligencesEntity.date.length())));
        baseViewHolder.setText(R.id.tv_pw_count, "(" + intelligencesEntity.count + ")");

        if(intelligencesEntity.isSelect){
            baseViewHolder.setTextColor(R.id.tv_pw_week,mContext.getResources().getColor(R.color.more_record) );
            baseViewHolder.setTextColor(R.id.tv_pw_date, mContext.getResources().getColor(R.color.more_record));
            baseViewHolder.setTextColor(R.id.tv_pw_count, mContext.getResources().getColor(R.color.more_record));
        }else{
            baseViewHolder.setTextColor(R.id.tv_pw_week,mContext.getResources().getColor(R.color.black_details_ball_textcolor) );
            baseViewHolder.setTextColor(R.id.tv_pw_date,mContext.getResources().getColor(R.color.black_details_ball_textcolor) );
            baseViewHolder.setTextColor(R.id.tv_pw_count,mContext.getResources().getColor(R.color.black_details_ball_textcolor) );
        }
    }

}
