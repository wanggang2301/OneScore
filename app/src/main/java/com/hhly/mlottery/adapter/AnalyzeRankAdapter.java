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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 2016/3/8.
 */
public class AnalyzeRankAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<? extends RankAndGoal> mScoreRankList;

    public AnalyzeRankAdapter(Context context, List<? extends RankAndGoal> mScoreRankList) {
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
        if (mScoreRankList.get(position) instanceof AnalyzeBean.ScoreRankEntity) {//积分排行
            List<AnalyzeBean.ScoreRankEntity> list = (List<AnalyzeBean.ScoreRankEntity>) mScoreRankList;
            AnalyzeBean.ScoreRankEntity entity = list.get(position);
            AnalyzeBean.ScoreRankEntity.ObjEntity obj = list.get(position).getObj();

            if (entity.getType() == 1) {
                holder.title.setText(R.string.rank);
                String[] s = obj.getHome().trim().split(" ");
                String[] s1 = obj.getGuest().trim().split(" ");
                String homeRank = s[s.length - 1];//不管是文字加数字还是纯数字，取到的都是数字
                String guestRank = s1[s1.length - 1];
                holder.home.setText(obj.getHome().equals("-2") ? "--" : obj.getHome());//没有数据就是--
                holder.guest.setText(obj.getGuest().equals("-2") ? "--" : obj.getGuest());
                int progress;
                if (Integer.parseInt(homeRank) == 0 && Integer.parseInt(guestRank) == 0||obj.getHome().equals("-2")&&obj.getGuest().equals("-2")) {
                    progress = 50;
                }
                else if(obj.getHome().equals("-2")){
                    progress=0;
                }
                else if(obj.getGuest().equals("-2")){
                    progress=100;
                }
                else {
                    progress = Integer.parseInt(guestRank) * 100 / (Integer.parseInt(homeRank) + Integer.parseInt(guestRank));
                }
                holder.pro.setProgress(progress);
            } else if (entity.getType() == 2) {


                holder.title.setText(R.string.recordconstract);
                holder.home.setText(obj.getH_win() + "" + context.getString(R.string.analyze_win) + obj.getH_equ() + ""
                        + context.getString(R.string.analyze_equ) + obj.getH_defeat() + "" + context.getString(R.string.analyze_defeat));
                holder.guest.setText(obj.getG_win() + "" + context.getString(R.string.analyze_win) + obj.getG_equ() + "" + context.getString(R.string.analyze_equ)
                        + obj.getG_defeat() + "" + context.getString(R.string.analyze_defeat));
                int progress;
                if (obj.getH_total() == 0 && obj.getG_total() == 0) {
                    progress = 50;
                } else {
                    progress = obj.getH_total() * 100 / (obj.getH_total() + obj.getG_total());
                }

                holder.pro.setProgress(progress);
            } else {
                holder.title.setText(R.string.homeguestconstract);
                holder.home.setText(obj.getH_win() + "" + context.getString(R.string.analyze_win) + obj.getH_equ() + ""
                        + context.getString(R.string.analyze_equ) + obj.getH_defeat() + "" + context.getString(R.string.analyze_defeat));
                holder.guest.setText(obj.getG_win() + "" + context.getString(R.string.analyze_win) + obj.getG_equ() + "" + context.getString(R.string.analyze_equ)
                        + obj.getG_defeat() + "" + context.getString(R.string.analyze_defeat));
                int progress;
                if (obj.getH_total() == 0 && obj.getG_total() == 0) {
                    progress = 50;
                } else {
                    progress = obj.getH_total() * 100 / (obj.getH_total() + obj.getG_total());
                }

                holder.pro.setProgress(progress);
            }

        } else {//得失球
            List<AnalyzeBean.GoalAndLossEntity> list = (List<AnalyzeBean.GoalAndLossEntity>) mScoreRankList;
            AnalyzeBean.GoalAndLossEntity entity = list.get(position);
            AnalyzeBean.GoalAndLossEntity.ObjEntity obj = list.get(position).getObj();
            int progress;
            if (Double.parseDouble(obj.getGuest()) == 0 && Double.parseDouble(obj.getHome()) == 0 || (obj.getHome().equals("-2") && obj.getGuest().equals("-2"))) {
                progress = 50;
            } else if (obj.getHome().equals("-2")) {//-2表示没有数据，用--显示
                progress = 0;
            } else if (obj.getGuest().equals("-2")) {
                progress = 100;
            } else {
                progress = (int) (Double.parseDouble(obj.getHome()) * 100 / (Double.parseDouble(obj.getHome()) + Double.parseDouble(obj.getGuest())));
            }

            if (entity.getType() == 4) {
                holder.title.setText(R.string.GoalsperGame);
                holder.home.setText(obj.getHome().equals("-2") ? "--" : obj.getHome());
                holder.guest.setText(obj.getGuest().equals("-2") ? "--" : obj.getGuest());

                holder.pro.setProgress(progress);
            } else if (entity.getType() == 5) {
                holder.title.setText(R.string.homeguestconstract);
                holder.home.setText(obj.getHome().equals("-2") ? "--" : obj.getHome());
                holder.guest.setText(obj.getGuest().equals("-2") ? "--" : obj.getGuest());
                holder.pro.setProgress(progress);
            } else if (entity.getType() == 6) {
                holder.title.setText(R.string.losspergame);
                holder.home.setText(obj.getHome().equals("-2") ? "--" : obj.getHome());
                holder.guest.setText(obj.getGuest().equals("-2") ? "--" : obj.getGuest());

                holder.pro.setProgress(progress);
            } else {
                holder.title.setText(R.string.homeguestconstract);
                holder.home.setText(obj.getHome().equals("-2") ? "--" : obj.getHome());
                holder.guest.setText(obj.getGuest().equals("-2") ? "--" : obj.getGuest());

                holder.pro.setProgress(progress);
            }
        }
        return convertView;
    }


    protected class ViewHolder {
        private TextView home;
        private TextView title;
        private TextView guest;
        private ProgressBar pro;

    }
}
