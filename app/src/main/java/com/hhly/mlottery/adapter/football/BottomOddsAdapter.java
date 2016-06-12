package com.hhly.mlottery.adapter.football;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.BottomOdds;

import java.util.List;

/**
 * @author wang gang
 * @date 2016/6/7 11:41
 * @des ${TODO}
 */
public class BottomOddsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<BottomOdds> list;
    private Context mContext;

    public BottomOddsAdapter(Context context, List<BottomOdds> datas) {
        this.mContext = context;
        this.list = datas;


    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BottomOddsHolder  hold = (BottomOddsHolder) holder;

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
        return 50;
    }
}
