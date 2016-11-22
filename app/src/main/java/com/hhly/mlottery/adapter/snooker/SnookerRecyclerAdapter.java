package com.hhly.mlottery.adapter.snooker;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchesBean;

import java.util.List;

/**
 * Created by yixq on 2016/11/16.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerRecyclerAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<SnookerMatchesBean> mData;



    public SnookerRecyclerAdapter(Context context ,List<SnookerMatchesBean> data){
        this.mContext = context;
        this.mData = data;
    }

    public void updateDatas(List<SnookerMatchesBean> data) {
        this.mData = data;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData == null && mData.size() == 0) {
            return super.getItemViewType(position);
        }else{
            return mData.get(position).getItemType();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch(viewType){
            case 0:
                    //日期
                    view = LayoutInflater.from(mContext).inflate(R.layout.snooker_list_activity_date_item , parent , false);
                    holder = new ViewHolderDate(view);
                break;
            case 1:
                    //赛事
                    view = LayoutInflater.from(mContext).inflate(R.layout.snooker_list_activity_title_item , parent , false);
                    holder = new ViewHolderTitle(view);
                break;
            case 2:
                    //比赛
                    view = LayoutInflater.from(mContext).inflate(R.layout.snooker_list_activity_item , parent , false);
                    holder = new ViewHolderList(view);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch(getItemViewType(position)){
            case 0:
                //日期
                ViewHolderDate viewHolderDate = (ViewHolderDate) holder;
                viewHolderDate.mSnookerDate.setText("2016-11-16 星期三" + position);
                break;
            case 1:
                //赛事
                ViewHolderTitle viewHolderTitle = (ViewHolderTitle)holder;
                viewHolderTitle.mSnookerTitle.setText("2016年世界斯洛克英格兰公开赛 " + position);
                break;
            case 2:
                //比赛
                ViewHolderList viewHolderList = (ViewHolderList) holder;
//                viewHolderList.mSnookerRound.setText("第"+ position + "轮");
                viewHolderList.mSnookerRound.setText(mData.get(position).getSubLgName());
                viewHolderList.mSnookerTime.setText(mData.get(position).getTime());
                viewHolderList.mSnookerNameLeft.setText(mData.get(position).getHomeTeam());
                viewHolderList.mSnookerNameRight.setText(mData.get(position).getGuestTeam());
                viewHolderList.mSnookerStatus.setText(mData.get(position).getStatus());
//                viewHolderList.mSnookerScoreLeft.setText(mData.get(position).getMatchScore().getPlayerOnewin());
                viewHolderList.mSnookerScoreLeft.setText(position+"");
                viewHolderList.mSnookerInning.setText(mData.get(position).getMatchStyle());
//                viewHolderList.mSnookerScoreRight.setText(mData.get(position).getMatchScore().getPlayerTwowin());
                viewHolderList.mSnookerScoreRight.setText(position + "");

                viewHolderList.mSnookerOddsLeft.setText("a" + position);
                viewHolderList.mSnookerOddsMiddle.setText("b" + position);
                viewHolderList.mSnookerOddsRight.setText("c" + position);

                break;
        }

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ViewHolderDate extends RecyclerView.ViewHolder{
        TextView mSnookerDate;
        public ViewHolderDate(View itemView) {
            super(itemView);
            mSnookerDate = (TextView) itemView.findViewById(R.id.snooker_date);
        }
    }
    class ViewHolderList extends RecyclerView.ViewHolder{
        private final CardView cardView;
        private final TextView mSnookerRound;
        private final TextView mSnookerTime;
        private final TextView mSnookerNameLeft;
        private final TextView mSnookerNameRight;
        private final TextView mSnookerStatus;
        private final TextView mSnookerScoreLeft;
        private final TextView mSnookerInning;
        private final TextView mSnookerScoreRight;
        private final TextView mSnookerOddsLeft;
        private final TextView mSnookerOddsMiddle;
        private final TextView mSnookerOddsRight;

        public ViewHolderList(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.snooker_card_view);
            mSnookerRound =  (TextView) itemView.findViewById(R.id.snooker_round);
            mSnookerTime = (TextView) itemView.findViewById(R.id.snooker_time);
            mSnookerNameLeft = (TextView) itemView.findViewById(R.id.snooker_name_left);
            mSnookerNameRight = (TextView) itemView.findViewById(R.id.snooker_name_right);
            mSnookerStatus = (TextView) itemView.findViewById(R.id.snooker_status);
            mSnookerScoreLeft = (TextView) itemView.findViewById(R.id.snooker_score_left);
            mSnookerInning = (TextView) itemView.findViewById(R.id.snooker_inning);
            mSnookerScoreRight = (TextView) itemView.findViewById(R.id.snooker_score_right);
            mSnookerOddsLeft = (TextView) itemView.findViewById(R.id.snooker_odds_left);
            mSnookerOddsMiddle = (TextView) itemView.findViewById(R.id.snooker_odds_middle);
            mSnookerOddsRight = (TextView) itemView.findViewById(R.id.snooker_odds_right);
        }
    }

    class ViewHolderTitle extends RecyclerView.ViewHolder{
        TextView mSnookerTitle;
        public ViewHolderTitle(View itemView) {
            super(itemView);
            mSnookerTitle = (TextView) itemView.findViewById(R.id.snooker_title);
        }
    }

}
