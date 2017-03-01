package com.hhly.mlottery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.AnalyzeBean;
import com.hhly.mlottery.bean.footballDetails.RankAndGoal;
import com.hhly.mlottery.bean.snookerbean.snookerDetail.SnookerAnalyzeBean;
import com.hhly.mlottery.config.StaticValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 2016/3/8.
 */
public class AnalyzeRankAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<SnookerAnalyzeBean.ProfessionDataEntity> mScoreRankList;

    public AnalyzeRankAdapter(Context context, List<SnookerAnalyzeBean.ProfessionDataEntity> mScoreRankList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        if (mScoreRankList == null) {
            mScoreRankList = new ArrayList<>();
        }
        this.mScoreRankList = mScoreRankList;

    }

    @Override
    public int getCount() {
        return mScoreRankList.size();
    }

    @Override
    public Object getItem(int position) {
        return mScoreRankList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_analyze_rank, parent, false);
            holder = new ViewHolder();

            holder.home = (TextView) convertView.findViewById(R.id.analyze_rank_home);
            holder.guest = (TextView) convertView.findViewById(R.id.analyze_rank_guest);
            holder.title = (TextView) convertView.findViewById(R.id.analyze_rank_title);
            holder.pro = (ProgressBar) convertView.findViewById(R.id.analyze_pro);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }




        SnookerAnalyzeBean.ProfessionDataEntity entity=mScoreRankList.get(position);
        if(entity.getDataType().equals(StaticValues.BIRTH)){
            holder.title.setText(R.string.snooker_analyze_birth);
        }else if(entity.getDataType().equals(StaticValues.RANKING)){
            holder.title.setText(R.string.snooker_analyze_rank);
            holder.pro.setProgress((100-setIntPercent(entity)));
        }
        else if(entity.getDataType().equals(StaticValues.PRO_SEASON)){

            holder.title.setText(R.string.snooker_analyze_season);
            holder.pro.setProgress(setIntPercent(entity));

        }
        else if(entity.getDataType().equals(StaticValues.TOTAL_BONUS)){
            holder.title.setText(R.string.snooker_analyze_bonus);
            holder.pro.setProgress(setIntPercent(entity));
        }
        else if(entity.getDataType().equals(StaticValues.FULLSCORE)){
            holder.title.setText(R.string.snooker_analyze_full_score);
            holder.pro.setProgress(setIntPercent(entity));
        }
        else if(entity.getDataType().equals(StaticValues.TOTAL_CHAMPION)){
            holder.title.setText(R.string.snooker_analyze_total_champion);
            holder.pro.setProgress(setIntPercent(entity));
        }
        else if(entity.getDataType().equals(StaticValues.HUNDRED_TIMES)){
            holder.title.setText(R.string.snooker_analyze_hundred_times);
            holder.pro.setProgress(setIntPercent(entity));
        }
        else if(entity.getDataType().equals(StaticValues.HUNDRED_PERCENT)){
            holder.title.setText(R.string.snooker_analyze_hundred_percent);
            holder.pro.setProgress(setDoublePercent(entity));

        }
        else if(entity.getDataType().equals(StaticValues.TOTAL_TIMES)){
            holder.title.setText(R.string.snooker_analyze_total_times);
            holder.pro.setProgress(setIntPercent(entity));
        }
        else if(entity.getDataType().equals(StaticValues.WIN)){
            holder.title.setText(R.string.snooker_analyze_win);
            holder.pro.setProgress(setIntPercent(entity));
        }
        else if(entity.getDataType().equals(StaticValues.WIN_PERCENT)){
            holder.title.setText(R.string.snooker_analyze_win_percent);
            holder.pro.setProgress(setDoublePercent(entity));

        }
        else if(entity.getDataType().equals(StaticValues.TOTAL_NUMBERS)){
            holder.title.setText(R.string.snooker_analyze_total_numbers);
            holder.pro.setProgress(setIntPercent(entity));
        }
        else if(entity.getDataType().equals(StaticValues.TOTAL_WINS)){
            holder.title.setText(R.string.snooker_analyze_total_wins);
            holder.pro.setProgress(setIntPercent(entity));
        }
        else if(entity.getDataType().equals(StaticValues.BOARD_WINS_PERSCENT)){
            holder.title.setText(R.string.snooker_analyze_wins_percent);
            holder.pro.setProgress(setDoublePercent(entity));
        }
        holder.home.setText(entity.getHomeData()==null?"--":entity.getHomeData());
        holder.guest.setText(entity.getGuestData()==null?"--":entity.getGuestData());
        return convertView;
    }
    private int setIntPercent( SnookerAnalyzeBean.ProfessionDataEntity entity){

        double progress=50;
        if(entity.getHomeData()==null&&entity.getGuestData()==null){
            progress=50;
        }else if(entity.getHomeData()==null){ //主队为null。客队不为null
            progress=0;
        }else if(entity.getGuestData()==null){
            progress=100;
        }else {
            double home=Double.parseDouble(entity.getHomeData());
            double guest=Double.parseDouble(entity.getGuestData());
            progress=home*100/(home+guest);
        }
        return (int)progress;

    }

    /**
     * 帶%的
     */
    private int setDoublePercent( SnookerAnalyzeBean.ProfessionDataEntity entity){

        double progress=50;
        if(entity.getHomeData()==null&&entity.getGuestData()==null){
            progress=50;
        }else if(entity.getHomeData()==null){ //主队为null。客队不为null
            progress=0;
        }else if(entity.getGuestData()==null){
            progress=100;
        }else {
            String guest[]=entity.getGuestData().split("%");
            String home[]=entity.getHomeData().split("%");
            progress=Double.parseDouble(home[0])*100/(Double.parseDouble(home[0])+Double.parseDouble(guest[0]));
        }
        return (int)progress;
    }


    protected class ViewHolder {
        private TextView home;
        private TextView title;
        private TextView guest;
        private ProgressBar pro;

    }
}
