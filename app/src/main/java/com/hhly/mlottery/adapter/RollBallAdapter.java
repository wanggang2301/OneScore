package com.hhly.mlottery.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.Match;

import java.util.List;

/**
 * @author wang gang
 * @date 2016/5/25 9:36
 * @des ${TODO}
 */
public class RollBallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private View mView;
    private List<Match> mMatchs;// 所有比赛


    public RollBallAdapter(Context context, List<Match> matches) {
        this.mContext = context;
        this.mMatchs = matches;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.item_football_roll, viewGroup, false);
        RollViewHolder rollViewHolder = new RollViewHolder(mView);
        return rollViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RollViewHolder rh = (RollViewHolder) holder;
        Match match = mMatchs.get(position);
        rh.racename.setText(match.getRacename());
        rh.hometeam.setText(match.getHometeam());
        rh.guestteam.setText(match.getGuestteam());


    }

    @Override
    public int getItemCount() {
        return mMatchs.size();
    }


    static class RollViewHolder extends RecyclerView.ViewHolder {


        CardView cardView;


        TextView keeptime;

        TextView racename;
        /*   TextView item_football_time;
           TextView item_football_half_score;
           TextView item_football_frequency;
           TextView item_football_home_yc;
           TextView item_football_home_rc;
           TextView item_football_guest_rc;
           TextView item_football_guest_yc;*/
        TextView hometeam;
        TextView home_score;
        //   TextView item_football_full_score;
        TextView guestteam;
        TextView guest_score;

       /* TextView item_football_left_odds;
        TextView item_football_handicap_value;
        TextView item_football_right_odds;*/

        public RollViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.roll_card_view);
            racename = (TextView) itemView.findViewById(R.id.race_name);
            hometeam = (TextView) itemView.findViewById(R.id.home_name);
            home_score = (TextView) itemView.findViewById(R.id.home_score);


            guestteam = (TextView) itemView.findViewById(R.id.guest_name);
            guest_score = (TextView) itemView.findViewById(R.id.guest_score);

        }
    }
}
