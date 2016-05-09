package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.text.Html;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.BasketDetails.BasketAnalyzeContentBean;
import com.hhly.mlottery.bean.basket.BasketDetails.BasketAnalyzeMoreRecentHistoryBean;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * Created by A on 2016/4/5.
 */
public class BasketAnalyzeAdapter extends CommonAdapter<BasketAnalyzeMoreRecentHistoryBean> {

    private Context mContext;

    public BasketAnalyzeAdapter(Context context, List<BasketAnalyzeMoreRecentHistoryBean> datas, int layoutId) {
        super(context, datas, layoutId);

        this.mContext = context;
    }
    public void updateDatas(List<BasketAnalyzeMoreRecentHistoryBean> datas) {
        mDatas = datas;
    }

    @Override
    public void convert(ViewHolder holder, BasketAnalyzeMoreRecentHistoryBean historyBean) {

        holder.setText(R.id.basket_details_team_name ,historyBean.getLeagueName());
        holder.setText(R.id.basket_details_data , historyBean.getDate());
        holder.setText(R.id.basket_details_guest_name , historyBean.getGuestTeam());
//        holder.setText(R.id.basket_details_team_name ,"乙蓝甲乙蓝甲乙蓝");
//        holder.setText(R.id.basket_details_data , "16-03-31");
//        holder.setText(R.id.basket_details_guest_name , "乙蓝甲乙蓝甲乙蓝");

        /**
         * HTML 设置字体颜色 （防字体色与背景色冲突变色）
         */
//        holder.setText(R.id.basket_details_home_name , Html.fromHtml("<font color='#FF1F1F'><b>" + historyBean.getHomeTeam() + "</b></font>"));


        if(historyBean.getResult() == 1){
//            holder.setTextColor(R.id.basket_details_home_name , R.color.basket_win_color);
            holder.setText(R.id.basket_details_home_name , Html.fromHtml("<font color='#FF1F1F'><b>" + historyBean.getHomeTeam() + "</b></font>"));
//            holder.setText(R.id.basket_details_home_name , Html.fromHtml("<font color='#FF1F1F'><b>" + "乙蓝甲乙蓝甲乙蓝" + "</b></font>"));
        }else{
//            holder.setTextColor(R.id.basket_details_home_name , R.color.tabtitle);
            holder.setText(R.id.basket_details_home_name , Html.fromHtml("<font color='#21B11E'><b>" + historyBean.getHomeTeam() + "</b></font>"));
//            holder.setText(R.id.basket_details_home_name , Html.fromHtml("<font color='#21B11E'><b>" + "乙蓝甲乙蓝甲乙蓝" + "</b></font>"));
        }

        if(historyBean.getScore().equals(":")){
            holder.setText(R.id.basket_details_score, "--");
        }else{
            holder.setText(R.id.basket_details_score, historyBean.getScore());
        }
//        holder.setText(R.id.basket_details_score, "105:105");

        if (historyBean.getHighLow().equals("")) {
            holder.setText(R.id.basket_details_big_small , "--");
        }else if (historyBean.getHighLow().equals("1")){
            holder.setText(R.id.basket_details_big_small , "大");
        }else if(historyBean.getHighLow().equals("0")) {
            holder.setText(R.id.basket_details_big_small, "小");
        }
        //TODO---------
        else{
//            holder.setText(R.id.basket_details_big_small , historyBean.getHighLow());
            holder.setText(R.id.basket_details_big_small , "--");
        }
        if (historyBean.getConcede().equals("")) {
            holder.setText(R.id.basket_details_concede ,"--");
        }else{
            holder.setText(R.id.basket_details_concede, historyBean.getConcede());
//            holder.setText(R.id.basket_details_concede , "+12.5");
        }

        L.d("BasketAnalyzeAdapter : ", historyBean.getDate() + "");

    }
}
