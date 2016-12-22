package com.hhly.mlottery.adapter.snooker;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.snookerbean.SnookerListBean;

import java.util.List;

/**
 * Created by yixq on 2016/11/16.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerRecyclerMoreloadAdapter extends BaseQuickAdapter<SnookerListBean> {

    private Context mContext;
    private List<SnookerListBean> mData;

    public SnookerRecyclerMoreloadAdapter(int layoutResId, List<SnookerListBean> data) {
        super(layoutResId, data);
    }

    public SnookerRecyclerMoreloadAdapter(List<SnookerListBean> data) {
        super(data);
    }

    public SnookerRecyclerMoreloadAdapter(View contentView, List<SnookerListBean> data) {
        super(contentView, data);
    }

    public SnookerRecyclerMoreloadAdapter(Context context ,List<SnookerListBean> data){
        super(R.layout.snooker_list_activity_item , data);
        this.mContext = context;
        this.mData = data;
    }
    @Override
    public int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return super.getViewHolderPosition(viewHolder);
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
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        BaseViewHolder holder = null;
        switch(viewType){
            case 0:
                view = LayoutInflater.from(mContext).inflate(R.layout.snooker_list_activity_date_item , parent , false);
                holder = new ViewHolderDate(view);
                break;
            case 1:
                view = LayoutInflater.from(mContext).inflate(R.layout.snooker_list_activity_item , parent , false);
                holder = new ViewHolderList(view);
                break;
            case 2:
                view = LayoutInflater.from(mContext).inflate(R.layout.snooker_list_activity_title_item , parent , false);
                holder = new ViewHolderTitle(view);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch(getItemViewType(position)){
            case 0:
                ViewHolderDate viewHolderDate = (ViewHolderDate) holder;
                viewHolderDate.mSnookerDate.setText("2016-11-16 星期三" + position);
                break;
            case 1:
                ViewHolderList viewHolderList = (ViewHolderList) holder;
                viewHolderList.mSnookerRound.setText("第"+ position + "轮");
                break;
            case 2:
                ViewHolderTitle viewHolderTitle = (ViewHolderTitle)holder;
                viewHolderTitle.mSnookerTitle.setText("2016年世界斯洛克英格兰公开赛 " + position);
                break;
        }

    }



    class ViewHolderDate extends BaseViewHolder{
        TextView mSnookerDate;
        public ViewHolderDate(View itemView) {
            super(itemView);
            mSnookerDate = (TextView) itemView.findViewById(R.id.snooker_date);
        }
    }
    class ViewHolderList extends BaseViewHolder{
        private final CardView cardView;
        TextView mSnookerRound;
        public ViewHolderList(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.snooker_card_view);
            mSnookerRound =  (TextView) itemView.findViewById(R.id.snooker_round);
        }
    }

    class ViewHolderTitle extends BaseViewHolder{
        TextView mSnookerTitle;
        public ViewHolderTitle(View itemView) {
            super(itemView);
            mSnookerTitle = (TextView) itemView.findViewById(R.id.snooker_title);
        }
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SnookerListBean snookerListBean) {

    }
}
