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

//        if (holder.getPosition()%2 == 0) {
//            holder.setBackgroundRes(R.id.football_analyze_details_item, R.color.home_item_bg);
//        }

//        L.d("Position _----------------","Position + ==" +holder.getPosition()%2 + "");

        holder.setText(R.id.football_details_team_name, historyBean.getMatchType());
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
                    holder.setText(R.id.football_details_big_small , historyBean.getCtotScore() + mContext.getResources().getString(R.string.basket_handicap_big));
                }else if(historyBean.getTot().equals("2")){
                    holder.setText(R.id.football_details_big_small , historyBean.getCtotScore() + mContext.getResources().getString(R.string.basket_handicap_small));
                }else if(historyBean.getTot().equals("0")){
                    holder.setText(R.id.football_details_big_small , historyBean.getCtotScore() + mContext.getResources().getString(R.string.basket_handicap_zou));
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
                    holder.setText(R.id.football_details_concede ,historyBean.getCasLetGoal() + mContext.getResources().getString(R.string.football_analyze_details_win));
                }else if (historyBean.getLet().equals("2")) {
                    holder.setText(R.id.football_details_concede ,historyBean.getCasLetGoal() + mContext.getResources().getString(R.string.football_analyze_details_lose));
                }else if(historyBean.getLet().equals("0")){
                    holder.setText(R.id.football_details_concede ,historyBean.getCasLetGoal() + mContext.getResources().getString(R.string.basket_handicap_zou));
                }
            }
        }

    }
}
