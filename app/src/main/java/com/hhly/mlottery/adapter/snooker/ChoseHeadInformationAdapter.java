package com.hhly.mlottery.adapter.snooker;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.snookerbean.SnookerRaceListitemBean;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * Created by yuely198 on 2017/2/18.
 */

public class ChoseHeadInformationAdapter extends CommonAdapter<String> {



    List<String> datas;
    Context mContext;
    private int clickStatus = 0;
    private RelativeLayout relativeLayout;
    private TextView textView;
    List<SnookerRaceListitemBean.DataBean.StageMapBean.StageInfoBean> stageInfo;
    String currentstagenum;

    public ChoseHeadInformationAdapter(Context context, List<SnookerRaceListitemBean.DataBean.StageMapBean.StageInfoBean> info, String currentstagenum, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
        this.mContext=context;
        this.datas=datas;
        this.stageInfo=info;
        this.currentstagenum=currentstagenum;

    }
    @Override
    public void convert(ViewHolder holder, String s) {

    }


    public void setSeclection(int position) {
        clickStatus = position;
    }

    @Override
    public void newconvert(ViewHolder holder, String s, int position) {

        relativeLayout = holder.getView(R.id.snooker_item_re);
        textView = holder.getView(R.id.snnoker_heard_item_tv);
        textView.setText(s);
        if (currentstagenum.equals(stageInfo.get(position).getNum())){
            relativeLayout.setBackgroundColor(Color.BLUE);
            //textView.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if (clickStatus==holder.getPosition()) {
            relativeLayout.setBackgroundColor(Color.parseColor("#0085e1"));
            textView.setTextColor(Color.parseColor("#FFFFFF"));
        }else {
            relativeLayout.setBackgroundColor(Color.parseColor("#00000000"));
            textView.setTextColor(Color.parseColor("#4A4A4A"));
        }


    }
}
