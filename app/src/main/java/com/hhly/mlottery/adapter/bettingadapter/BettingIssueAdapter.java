package com.hhly.mlottery.adapter.bettingadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.bettingbean.BettingIssueBean;
import com.hhly.mlottery.frame.footballframe.FootballBettingIssueFragment;

import java.util.List;

/**
 * Created by：XQyi on 2017/5/11 11:57
 * Use: 内页推荐的adapter
 */
public class BettingIssueAdapter extends BaseQuickAdapter<BettingIssueBean> {

    private Context mContext;
    private List<BettingIssueBean> mData;

    private FootballBettingIssueFragment.IssueBuyClickListener mBuyClick;
    public void setmBuyClick(FootballBettingIssueFragment.IssueBuyClickListener mBuyClicks){
        this.mBuyClick = mBuyClicks;
    }

    public BettingIssueAdapter(Context context, List<BettingIssueBean> data) {
        super(R.layout.betting_recommend_issue_item, data);
        this.mContext = context;
        this.mData = data;
    }
    public void setData(List<BettingIssueBean> data){
        this.mData = data;
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(mContext).inflate(R.layout.betting_recommend_issue_item, parent, false);
        BaseViewHolder holder = new ViewHolderData(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int positions) {
        super.onBindViewHolder(holder, positions);
        if (mData == null) {
            return;
        }
        final ViewHolderData viewHolderData = (ViewHolderData) holder;
        viewHolderData.mSpecialistName.setText(mData.get(positions).getName());

        viewHolderData.toBuyCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBuyClick != null) {
                    mBuyClick.BuyOnClick(v , mData.get(positions).getName());
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        return super.getItemView(layoutResId, parent);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BettingIssueBean bettingIssueBean) {

    }


    class ViewHolderData extends BaseViewHolder{

        private final TextView mSpecialistName;
        private final LinearLayout toBuyCheck;

        public ViewHolderData(View itemView) {
            super(itemView);
            mSpecialistName = (TextView) itemView.findViewById(R.id.betting_issue_specialist_name);
            toBuyCheck = (LinearLayout) itemView.findViewById(R.id.betting_issue_tobuy_or_check);

        }
    }
}
