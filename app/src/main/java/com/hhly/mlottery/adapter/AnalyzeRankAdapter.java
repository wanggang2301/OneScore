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
            holder.title.setText("生日");
        }else if(entity.getDataType().equals(StaticValues.RANKING)){
            holder.title.setText("世界排名");
        }
        else if(entity.getDataType().equals(StaticValues.PRO_SEASON)){
            holder.title.setText("职业赛季");
        }
        else if(entity.getDataType().equals(StaticValues.TOTAL_BONUS)){
            holder.title.setText("总奖金");
        }
        else if(entity.getDataType().equals(StaticValues.FULLSCORE)){
            holder.title.setText("147满分杆");
        }
        else if(entity.getDataType().equals(StaticValues.TOTAL_CHAMPION)){
            holder.title.setText("冠军数");
        }
        else if(entity.getDataType().equals(StaticValues.HUNDRED_TIMES)){
            holder.title.setText("破百数");
        }
        else if(entity.getDataType().equals(StaticValues.HUNDRED_PERCENT)){
            holder.title.setText("破败率");
        }
        else if(entity.getDataType().equals(StaticValues.TOTAL_TIMES)){
            holder.title.setText("总场数");
        }
        else if(entity.getDataType().equals(StaticValues.WIN)){
            holder.title.setText("场胜数");
        }
        else if(entity.getDataType().equals(StaticValues.WIN_PERCENT)){
            holder.title.setText("场胜率");
        }
        else if(entity.getDataType().equals(StaticValues.TOTAL_NUMBERS)){
            holder.title.setText("总局数");
        }
        else if(entity.getDataType().equals(StaticValues.TOTAL_WINS)){
            holder.title.setText("局胜数");
        }
        else if(entity.getDataType().equals(StaticValues.BOARD_WINS_PERSCENT)){
            holder.title.setText("局胜率");
        }



        holder.home.setText(entity.getHomeData()==null?"0":entity.getHomeData());
        holder.guest.setText(entity.getGuestData()==null?"0":entity.getGuestData());
        return convertView;
    }


    protected class ViewHolder {
        private TextView home;
        private TextView title;
        private TextView guest;
        private ProgressBar pro;

    }
}
