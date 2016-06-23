package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.text.Html;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.BasketDetails.BasketAnalyzeMoreRecentHistoryBean;
import com.hhly.mlottery.bean.footballDetails.FootballAnaylzeHistoryRecent;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * Created by A on 2016/4/5.
 */
public class FootballAnalyzeAdapter extends CommonAdapter<FootballAnaylzeHistoryRecent> {

    private Context mContext;

    public FootballAnalyzeAdapter(Context context, List<FootballAnaylzeHistoryRecent> datas, int layoutId) {
        super(context, datas, layoutId);

        this.mContext = context;
    }
    public void updateDatas(List<FootballAnaylzeHistoryRecent> datas) {
        mDatas = datas;
    }

    @Override
    public void convert(ViewHolder holder, FootballAnaylzeHistoryRecent historyBean) {


        holder.setText(R.id.football_details_team_name ,historyBean.getMatchType());
        holder.setText(R.id.football_details_data , historyBean.getTime());

        if (historyBean.isHomeGround()) {
            if (historyBean.getResult() == 1) {
                holder.setTextColorRes(R.id.football_details_home_name , R.color.football_analyze_win_color);
            }else if (historyBean.getResult() == 0) {
                holder.setTextColorRes(R.id.football_details_home_name , R.color.football_analyze_draw_color);
            }else if (historyBean.getResult() == -1) {
                holder.setTextColorRes(R.id.football_details_home_name , R.color.football_analyze_lose_color);
            }
            holder.setText(R.id.football_details_home_name , historyBean.getHome());

            holder.setTextColorRes(R.id.football_details_guest_name, R.color.football_analyze_default_color);
            holder.setText(R.id.football_details_guest_name, historyBean.getGuest());
        }else{
            if (historyBean.getResult() == 1) {
                holder.setTextColorRes(R.id.football_details_guest_name , R.color.football_analyze_win_color);
            }else if (historyBean.getResult() == 0) {
                holder.setTextColorRes(R.id.football_details_guest_name , R.color.football_analyze_draw_color);
            }else if (historyBean.getResult() == -1) {
                holder.setTextColorRes(R.id.football_details_guest_name , R.color.football_analyze_lose_color);
            }
            holder.setText(R.id.football_details_guest_name , historyBean.getGuest());

            holder.setTextColorRes(R.id.football_details_home_name , R.color.football_analyze_default_color);
            holder.setText(R.id.football_details_home_name , historyBean.getHome());
        }

        holder.setText(R.id.football_details_score , historyBean.getHomeScore() + "-" + historyBean.getGuestScore());

        if (historyBean.getCtotScore() == null) {
            holder.setText(R.id.football_details_big_small , "--");
        }else{
            if (historyBean.getTot() == null) {
                holder.setText(R.id.football_details_big_small , historyBean.getCtotScore() + "--");
            }else{
                if (historyBean.getTot().equals("1")) {
                    holder.setText(R.id.football_details_big_small , historyBean.getCtotScore() + "大");
                }else if(historyBean.getTot().equals("2")){
                    holder.setText(R.id.football_details_big_small , historyBean.getCtotScore() + "小");
                }else if(historyBean.getTot().equals("0")){
                    holder.setText(R.id.football_details_big_small , historyBean.getCtotScore() + "走");
                }
            }
        }

        if (historyBean.getCasLetGoal() == null) {
            holder.setText(R.id.football_details_concede , "--");
        }else{
            if (historyBean.getLet() == null) {
                holder.setText(R.id.football_details_concede ,historyBean.getCasLetGoal() + "--");
            }else{
                if (historyBean.getLet().equals("1")) {
                    holder.setText(R.id.football_details_concede ,historyBean.getCasLetGoal() + "赢");
                }else if (historyBean.getLet().equals("2")) {
                    holder.setText(R.id.football_details_concede ,historyBean.getCasLetGoal() + "输");
                }else if(historyBean.getLet().equals("0")){
                    holder.setText(R.id.football_details_concede ,historyBean.getCasLetGoal() + "走");
                }
            }
        }
//        /**
//         * HTML 设置字体颜色 （防字体色与背景色冲突变色）
//         */
////        holder.setText(R.id.basket_details_home_name , Html.fromHtml("<font color='#FF1F1F'><b>" + historyBean.getHomeTeam() + "</b></font>"));
//
//        if (historyBean.isHomeGround()) {
//            if(historyBean.getResult() == 1){
//                holder.setText(R.id.football_details_home_name , Html.fromHtml("<font color='#FF1F1F'><b>" + historyBean.getHomeTeam() + "</b></font>"));
//            }else{
//                holder.setText(R.id.football_details_home_name , Html.fromHtml("<font color='#21B11E'><b>" + historyBean.getHomeTeam() + "</b></font>"));
//            }
//
//            holder.setText(R.id.football_details_guest_name , historyBean.getGuestTeam());
//        }else{
//            holder.setText(R.id.basket_details_home_name , historyBean.getHomeTeam());
//
//            if(historyBean.getResult() == 1){
//                holder.setText(R.id.football_details_guest_name , Html.fromHtml("<font color='#FF1F1F'><b>" + historyBean.getGuestTeam() + "</b></font>"));
//            }else{
//                holder.setText(R.id.football_details_guest_name , Html.fromHtml("<font color='#21B11E'><b>" + historyBean.getGuestTeam() + "</b></font>"));
//            }
//        }
//
//
////        if(historyBean.getResult() == 1){
////            holder.setText(R.id.basket_details_home_name , Html.fromHtml("<font color='#FF1F1F'><b>" + historyBean.getHomeTeam() + "</b></font>"));
////        }else{
////            holder.setText(R.id.basket_details_home_name , Html.fromHtml("<font color='#21B11E'><b>" + historyBean.getHomeTeam() + "</b></font>"));
////        }
//
//        if(historyBean.getScore().equals(":") || historyBean.getScore().equals("") ||  historyBean.getScore() == null){
//            holder.setText(R.id.football_details_score, "--");
//        }else{
//            holder.setText(R.id.football_details_score, historyBean.getScore());
//        }
////        holder.setText(R.id.basket_details_score, "105:105");
//
//        if (historyBean.getHighLow().equals("") || historyBean.getHighLow() == null) {
//            holder.setText(R.id.football_details_big_small , "--");
//        }else if (historyBean.getHighLow().equals("1")){
////            holder.setText(R.id.basket_details_big_small, getCount().getText(R.string.china_id_txt));
//            holder.setText(R.id.football_details_big_small,R.string.basket_handicap_big);
//        }else if(historyBean.getHighLow().equals("0")) {
//            holder.setText(R.id.football_details_big_small, R.string.basket_handicap_small);
//        }else if(historyBean.getHighLow().equals("-1")){
//            holder.setText(R.id.football_details_big_small, R.string.basket_handicap_zou);
//        }
//        else{
//            holder.setText(R.id.football_details_big_small , "--"); // 1 大  0 小 -1 走水 其它不显示
//        }
//
//        if (historyBean.getConcede().equals("") || historyBean.getConcede() == null) {
//            holder.setText(R.id.football_details_concede ,"--");
//        }else{
//            holder.setText(R.id.football_details_concede, historyBean.getConcede());
////            holder.setText(R.id.football_details_concede , "+12.5");
//        }
//
////        L.d("BasketAnalyzeAdapter : ", historyBean.getDate() + "");

    }
}
