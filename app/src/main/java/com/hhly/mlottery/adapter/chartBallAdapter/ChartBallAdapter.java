package com.hhly.mlottery.adapter.chartBallAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewAdapter;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewHolder;

import java.util.List;

/**
 * desc:聊球记录adapter
 * Created by 107_tangrr on 2016/12/7 0007.
 */

public class ChartBallAdapter extends BaseRecyclerViewAdapter {
    Context mContext;
    List<String> mData;

    public ChartBallAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.mData = data;
        System.out.println("xxxxx 初始化adapter");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        RecyclerView.ViewHolder holder = null;
        System.out.println("xxxxx viewType: "+viewType);
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_char_ball_content, parent, false);
                holder = new ViewHolderMsg(view);
                break;
            case 1:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_char_ball_content_me, parent, false);
                holder = new ViewHolderMe(view);
                break;
            case 2:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_char_ball_content_time, parent, false);
                holder = new ViewHolderTime(view);
                break;
        }
        return holder;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int[] getItemLayouts() {
        return new int[0];
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                ViewHolderMsg viewHolderMsg = (ViewHolderMsg) holder;
                viewHolderMsg.tv_name.setText(mData.get(position));

                break;
            case 1:
                ViewHolderMe viewHolderMe = (ViewHolderMe) holder;
                viewHolderMe.tv_nickname_me.setText(mData.get(position));
                break;
            case 2:
                ViewHolderTime viewHolderTime = (ViewHolderTime) holder;
                viewHolderTime.tv_time.setText("2016-12-12 18:30");
                break;
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        // TODO 显示返回item类型  如果为自己的id，则返回1
        if (position == 0 || position == 5) {
            return 1;
        }else if(position == 6){
            return 2;
        } else {
            return 0;
        }
    }

//    @Override
//    public int getItemViewType(int position) {
//
//    }

    // 其它消息ViewHolder
    class ViewHolderMsg extends RecyclerView.ViewHolder {
        TextView tv_name;

        public ViewHolderMsg(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_nickname);
        }
    }

    // 我的消息ViewHolder
    class ViewHolderMe extends RecyclerView.ViewHolder {
        TextView tv_nickname_me;

        public ViewHolderMe(View itemView) {
            super(itemView);
            tv_nickname_me = (TextView) itemView.findViewById(R.id.tv_nickname_me);
        }
    }

    // 时间消息ViewHolder
    class ViewHolderTime extends RecyclerView.ViewHolder {
        TextView tv_time;
        public ViewHolderTime(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
