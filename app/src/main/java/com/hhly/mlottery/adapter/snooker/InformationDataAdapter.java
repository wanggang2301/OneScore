package com.hhly.mlottery.adapter.snooker;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.snookerbean.SnookerSuccessBean;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;


/**
 * @ClassName: OneScore
 * @author:Administrator luyao
 * @Description: 资料库第一列表数据adapter
 * @data: 2016/3/30 11:40
 */
public class InformationDataAdapter extends CommonAdapter<SnookerSuccessBean.DataBean> {


    private final Context mContext;
    private boolean clickStatus;

    private int layoutId;
    List<SnookerSuccessBean.DataBean> mDatas;

    public InformationDataAdapter(Context context, List<SnookerSuccessBean.DataBean> datas, int layoutId) {
        super(context, datas, layoutId);
        mContext = context;
        this.mDatas = datas;
        this.layoutId = layoutId;


    }

    @Override
    public void convert(final ViewHolder holder, SnookerSuccessBean.DataBean dataBean) {
        holder.setText(R.id.live_item_day_txt, dataBean.getMatchDate());
        holder.setText(R.id.snooker_left_name, dataBean.getPlayerNameAcn());
        holder.setText(R.id.snooker_race_total_score, dataBean.getScore());
        holder.setText(R.id.snooker_right_name, dataBean.getPlayerNameBcn());
        holder.setOnClickListener(R.id.iconfont, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickStatus) {
                    holder.setImageResource(R.id.iconfont, R.mipmap.iconfont_xiala_2);
                    clickStatus = false;
                }else{
                    holder.setImageResource(R.id.iconfont, R.mipmap.iconfont_xiala_1);
                    clickStatus = true;
                }
            }
        });

    }

    public void updateDatas(List<SnookerSuccessBean.DataBean> datas) {
        this.mDatas = datas;
    }


}
