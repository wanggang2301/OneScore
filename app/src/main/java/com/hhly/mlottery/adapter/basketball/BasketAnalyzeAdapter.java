package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.text.Html;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdetails.BasketAnalyzeMoreRecentHistoryBean;
import com.hhly.mlottery.util.DateUtil;
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
        holder.setText(R.id.basket_details_data , DateUtil.convertDateToNationYY(historyBean.getDate()));
        holder.setText(R.id.basket_details_guest_name , historyBean.getGuestTeam());
//        holder.setText(R.id.basket_details_team_name ,"乙蓝甲乙蓝甲乙蓝");
//        holder.setText(R.id.basket_details_data , "16-03-31");
//        holder.setText(R.id.basket_details_guest_name , "乙蓝甲乙蓝甲乙蓝");

        /**
         * HTML 设置字体颜色 （防字体色与背景色冲突变色）
         */
//        holder.setText(R.id.basket_details_home_name , Html.fromHtml("<font color='#FF1F1F'><b>" + historyBean.getHomeTeam() + "</b></font>"));

        if (historyBean.isHomeGround()) {
            if(historyBean.getResult() == 1){
                holder.setText(R.id.basket_details_home_name , Html.fromHtml("<font color='#FF1F1F'><b>" + historyBean.getHomeTeam() + "</b></font>"));
            }else{
                holder.setText(R.id.basket_details_home_name , Html.fromHtml("<font color='#21B11E'><b>" + historyBean.getHomeTeam() + "</b></font>"));
            }

            holder.setText(R.id.basket_details_guest_name , historyBean.getGuestTeam());
        }else{
            holder.setText(R.id.basket_details_home_name , historyBean.getHomeTeam());

            if(historyBean.getResult() == 1){
                holder.setText(R.id.basket_details_guest_name , Html.fromHtml("<font color='#FF1F1F'><b>" + historyBean.getGuestTeam() + "</b></font>"));
            }else{
                holder.setText(R.id.basket_details_guest_name , Html.fromHtml("<font color='#21B11E'><b>" + historyBean.getGuestTeam() + "</b></font>"));
            }
        }
        
        
//        if(historyBean.getResult() == 1){
//            holder.setText(R.id.basket_details_home_name , Html.fromHtml("<font color='#FF1F1F'><b>" + historyBean.getHomeTeam() + "</b></font>"));
//        }else{
//            holder.setText(R.id.basket_details_home_name , Html.fromHtml("<font color='#21B11E'><b>" + historyBean.getHomeTeam() + "</b></font>"));
//        }

        if(historyBean.getScore().equals(":") || historyBean.getScore().equals("") ||  historyBean.getScore() == null){
            holder.setText(R.id.basket_details_score, "--");
        }else{
            holder.setText(R.id.basket_details_score, historyBean.getScore());
        }
//        holder.setText(R.id.basket_details_score, "105:105");

        if (historyBean.getHighLow().equals("") || historyBean.getHighLow() == null) {
            holder.setText(R.id.basket_details_big_small , "--");
        }else if (historyBean.getHighLow().equals("1")){
//            holder.setText(R.id.basket_details_big_small, getCount().getText(R.string.china_id_txt));
            holder.setText(R.id.basket_details_big_small,R.string.basket_handicap_big);
        }else if(historyBean.getHighLow().equals("0")) {
            holder.setText(R.id.basket_details_big_small, R.string.basket_handicap_small);
        }else if(historyBean.getHighLow().equals("-1")){
            holder.setText(R.id.basket_details_big_small, R.string.basket_handicap_zou);
        }
        else{
            holder.setText(R.id.basket_details_big_small , "--"); // 1 大  0 小 -1 走水 其它不显示
        }

        if (historyBean.getConcede().equals("") || historyBean.getConcede() == null) {
            holder.setText(R.id.basket_details_concede ,"--");
        }else{
            holder.setText(R.id.basket_details_concede, historyBean.getConcede());
//            holder.setText(R.id.basket_details_concede , "+12.5");
        }

        L.d("BasketAnalyzeAdapter : ", historyBean.getDate() + "");

    }
}
