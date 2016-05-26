package com.hhly.mlottery.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * @author wang gang
 * @date 2016/5/25 9:36
 * @des ${TODO}
 */
public class RollBallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private View mView;


    public RollBallAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.item_football_roll, viewGroup, false);
        RollViewHolder rollViewHolder = new RollViewHolder(mView);
        return rollViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 20;
    }


    static class RollViewHolder extends RecyclerView.ViewHolder{


        CardView cardView;

        public RollViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.roll_card_view);
        }
    }
}
