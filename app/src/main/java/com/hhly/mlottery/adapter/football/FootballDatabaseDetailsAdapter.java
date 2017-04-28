package com.hhly.mlottery.adapter.football;

import android.content.Context;
import android.view.View;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.DatabaseBigSmallBean;
import com.hhly.mlottery.frame.footballframe.FootballDatabaseBigSmallFragment;
import com.hhly.mlottery.frame.footballframe.FootballDatabaseHandicapFragment;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * description:
 * author: yixq
 * Created by A on 2016/10/10.
 */
public class FootballDatabaseDetailsAdapter extends CommonAdapter<DatabaseBigSmallBean> {

    private int type;
    public FootballDatabaseDetailsAdapter(Context context, List<DatabaseBigSmallBean> datas, int layoutId , int type) {
        super(context, datas, layoutId);
        this.mContext = context;
        this.type = type;
    }
    private FootballDatabaseHandicapFragment.FootballTeamHandicpDetailsClickListener footballTeamHandicpDetailsClickListener;
    public void setFootballTeamIntegralDetailsClickListener(FootballDatabaseHandicapFragment.FootballTeamHandicpDetailsClickListener footballTeamHandicpDetailsClickListener){
        this.footballTeamHandicpDetailsClickListener = footballTeamHandicpDetailsClickListener;
    }
    private FootballDatabaseBigSmallFragment.FootballTeamBigSmallDetailsClickListener footballTeamBigSmallDetailsClickListener;
    public void setFootballTeamBigSmallDetailsClickListener(FootballDatabaseBigSmallFragment.FootballTeamBigSmallDetailsClickListener footballTeamBigSmallDetailsClickListener){
        this.footballTeamBigSmallDetailsClickListener = footballTeamBigSmallDetailsClickListener;
    }


    public void updateDatas(List<DatabaseBigSmallBean> datas) {
        this.mDatas = datas;
    }
    @Override
    public void convert(ViewHolder holder, final DatabaseBigSmallBean bean) {

        if (holder.getPosition() < 3) {
            holder.setText(R.id.football_database_datails_ranking_big_small , bean.getRank());
            holder.setBackgroundRes(R.id.football_database_datails_ranking_big_small, R.drawable.basket_databae_round_dra);
            holder.setTextColorRes(R.id.football_database_datails_ranking_big_small, R.color.basket_text_color);

            holder.setText(R.id.football_database_details_name_big_small, bean.getTeamName());
            holder.setTextColorRes(R.id.football_database_details_name_big_small , R.color.content_txt_black);
        }else{
            holder.setText(R.id.football_database_datails_ranking_big_small , bean.getRank());
            holder.setBackgroundColorRes(R.id.football_database_datails_ranking_big_small, R.color.transparent);
            holder.setTextColorRes(R.id.football_database_datails_ranking_big_small, R.color.live_text1);

            holder.setText(R.id.football_database_details_name_big_small, bean.getTeamName());
            holder.setTextColorRes(R.id.football_database_details_name_big_small , R.color.content_txt_black);
        }

        holder.setText(R.id.football_database_details_finished_big_small, bean.getMatchCount());
        holder.setText(R.id.football_database_dzx , bean.getLeft()+ "/" + bean.getMidd() + "/" + bean.getRight());

        if (bean.getLvg() != null) {
            String left = bean.getLvg().trim();
            holder.setText(R.id.football_database_details_big , left.substring(0 , left.length()-1));
        }else{
            holder.setText(R.id.football_database_details_big, "-");
        }
        if (bean.getMvg() != null) {
            String midd = bean.getMvg().trim();
            holder.setText(R.id.football_database_details_draw, midd.substring(0 , midd.length()-1));
        }else{
            holder.setText(R.id.football_database_details_draw, "-");
        }
        if (bean.getRvg() != null) {
            String right = bean.getRvg().trim();
            holder.setText(R.id.football_database_details_small, right.substring(0 , right.length()-1));
        }else{
            holder.setText(R.id.football_database_details_small, "-");
        }

        if (type == 0) {
            holder.setOnClickListener(R.id.football_database_details_name_big_small, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (footballTeamHandicpDetailsClickListener != null) {
                        footballTeamHandicpDetailsClickListener.HandicpDetailsOnClick(v , bean);
                    }
                }
            });
        }else if(type == 1){
            holder.setOnClickListener(R.id.football_database_details_name_big_small, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (footballTeamBigSmallDetailsClickListener != null) {
                        footballTeamBigSmallDetailsClickListener.BigSmallDetailsOnClick(v,bean);
                    }
                }
            });
        }
    }
}
