package com.hhly.mlottery.adapter.football;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.BottomOddsDetailsItem;
import com.hhly.mlottery.util.HandicapUtils;

import java.util.List;

/**
 * @author wang gang
 * @date 2016/6/7 11:41
 * @des ${}
 */
public class BottomOddsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

/*
    private static final int ASIA = 1;
    private static final int BIG_SMALL_BALL = 3;

    private static final int EU = 2;*/

    //亚盘
    private static final int ALET = 1;
    private static final int EUR = 2;
    private static final int ASIZE = 3;  //大小球

    private List<BottomOddsDetailsItem> list;
    private Context mContext;

    private int mType;

    public BottomOddsAdapter(Context context, List<BottomOddsDetailsItem> datas, int type) {
        this.mContext = context;
        this.list = datas;
        this.mType = type;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //1 升 0无 -1降

        BottomOddsHolder hold = (BottomOddsHolder) holder;
        if (!isNULLOrEmpty(list.get(position).getTime())) {
            hold.item_time.setText(list.get(position).getTime() + "'");
        } else {
            hold.item_time.setText("-");
        }


        if (position < list.size() - 1 && !list.get(position).getScore().equals(list.get(position + 1).getScore())) {
            hold.item_score.setText(list.get(position).getScore());
            hold.item_score.setTextColor(mContext.getResources().getColor(R.color.white));
            hold.item_score.setBackgroundResource(R.color.analyze_left);

        } else {
            hold.item_score.setText(list.get(position).getScore());
            hold.item_score.setTextColor(mContext.getResources().getColor(R.color.home_logo_color));
            hold.item_score.setBackgroundResource(R.color.white);
        }


        if ("-".equals(list.get(position).getOdd().getLeft()) || "-".equals(list.get(position).getOdd().getMiddle()) || "-".equals(list.get(position).getOdd().getRight())) {

            hold.item_home.setText("");

            hold.item_handicap.setText(mContext.getResources().getString(R.string.fragme_home_fengpan_text));
            hold.item_handicap.setTextColor(mContext.getResources().getColor(R.color.white));
            hold.item_handicap.setBackgroundResource(R.color.analyze_left);

            hold.item_guest.setText("");


        } else if (isNULLOrEmpty(list.get(position).getOdd().getLeft()) || isNULLOrEmpty(list.get(position).getOdd().getMiddle()) || isNULLOrEmpty(list.get(position).getOdd().getRight())) {
            hold.item_home.setText("-");
            hold.item_home.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
            hold.item_home.setBackgroundResource(R.color.white);

            hold.item_handicap.setText("-");
            hold.item_handicap.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
            hold.item_handicap.setBackgroundResource(R.color.white);


            hold.item_guest.setText("-");
            hold.item_guest.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
            hold.item_guest.setBackgroundResource(R.color.white);

        } else {
            hold.item_home.setText(list.get(position).getOdd().getLeft());
            setTextViewColor(hold.item_home, 0, list.get(position).getOdd().getLeftUp());
            if (mType == ALET) {
                hold.item_handicap.setText(HandicapUtils.changeHandicap(list.get(position).getOdd().getMiddle()));
                setTextViewColor(hold.item_handicap, 1, list.get(position).getOdd().getMiddleUp());
            } else if (mType == ASIZE) {//大小球
                hold.item_handicap.setText(HandicapUtils.changeHandicapByBigLittleBall(list.get(position).getOdd().getMiddle()));
                setTextViewColor(hold.item_handicap, 1, list.get(position).getOdd().getMiddleUp());
            } else if (mType == EUR) {  //欧赔
                hold.item_handicap.setText(list.get(position).getOdd().getMiddle());
                setTextViewColor(hold.item_handicap, 0, list.get(position).getOdd().getMiddleUp());
            }
            hold.item_guest.setText(list.get(position).getOdd().getRight());
            setTextViewColor(hold.item_guest, 0, list.get(position).getOdd().getRightUp());
        }

    }

    private void setTextViewColor(TextView textView, int flag, String b) {
        textView.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
        textView.setBackgroundResource(R.color.white);
        if ("1".equals(b)) {
            if (flag == 0) {
                textView.setTextColor(mContext.getResources().getColor(R.color.odds_details));
            } else {
                textView.setTextColor(mContext.getResources().getColor(R.color.white));
                textView.setBackgroundResource(R.color.analyze_left);
            }
        } else if ("-1".equals(b)) {
            if (flag == 0) {
                textView.setTextColor(mContext.getResources().getColor(R.color.odds_left));
            } else {
                textView.setTextColor(mContext.getResources().getColor(R.color.white));
                textView.setBackgroundResource(R.color.odds_left);
            }
        } else if ("0".equals(b)) {
            textView.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
            textView.setBackgroundResource(R.color.white);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_bottom_odds, parent, false);
        BottomOddsHolder BottomOddsHolder = new BottomOddsHolder(view);
        return BottomOddsHolder;
    }

    static class BottomOddsHolder extends RecyclerView.ViewHolder {
        TextView item_time;
        TextView item_score;
        TextView item_home;
        TextView item_handicap;
        TextView item_guest;

        public BottomOddsHolder(final View itemView) {
            super(itemView);
            item_time = (TextView) itemView.findViewById(R.id.time);
            item_score = (TextView) itemView.findViewById(R.id.score);
            item_home = (TextView) itemView.findViewById(R.id.host_team);
            item_handicap = (TextView) itemView.findViewById(R.id.handicap);
            item_guest = (TextView) itemView.findViewById(R.id.guest_team);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private boolean isNULLOrEmpty(String s) {
        return s == null || "".equals(s);
    }
}
