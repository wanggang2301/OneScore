package com.hhly.mlottery.adapter.snooker;

import android.content.Context;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.snookerbean.SnookerSuccessBean;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;


/**
 * @ClassName: OneScore
 * @author:Administrator luyao
 * @Description:  资料库第一列表数据adapter
 * @data: 2016/3/30 11:40
 */
public class InformationDataAdapter extends CommonAdapter<SnookerSuccessBean.DataBean> {


    private final Context mContext;

    private int layoutId;
    List<SnookerSuccessBean.DataBean> mDatas;
    public InformationDataAdapter(Context context, List<SnookerSuccessBean.DataBean> datas, int layoutId) {
        super(context, datas, layoutId);
        mContext = context;
        this.mDatas=datas;
        this.layoutId=layoutId;


    }

    @Override
    public void convert(ViewHolder holder, SnookerSuccessBean.DataBean dataBean) {
        holder.setText(R.id.live_item_day_txt,dataBean.getMatchDate());
        holder.setText(R.id.snooker_left_name,dataBean.getPlayerNameAcn());
        holder.setText(R.id.snooker_race_total_score,dataBean.getScore());
        holder.setText(R.id.snooker_right_name,dataBean.getPlayerNameBcn());
    }


    public void updateDatas(List<SnookerSuccessBean.DataBean> datas) {
        this.mDatas = datas;
    }


}
