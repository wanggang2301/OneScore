package com.hhly.mlottery.adapter.bettingadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.bettingbean.BettingIssueBean;
import com.hhly.mlottery.bean.bettingbean.BettingIssueFabuPalyBean;
import com.hhly.mlottery.mvp.bettingmvp.mvpview.FootballBettingIssueFragment;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * Created by：XQyi on 2017/6/19 11:57
 * Use: 内页发布推介的adapter
 */
public class BettingIssueAdapter extends CommonAdapter<BettingIssueFabuPalyBean.PromotionTypeVo.PromotionTypeListVo> {

    private String playId;

    public BettingIssueAdapter(Context context, List<BettingIssueFabuPalyBean.PromotionTypeVo.PromotionTypeListVo> datas, String playId , int layoutId) {
        super(context, datas, layoutId);
        this.playId = playId;

    }

    @Override
    public void convert(final ViewHolder holder, final BettingIssueFabuPalyBean.PromotionTypeVo.PromotionTypeListVo listVo) {

        holder.setTag(R.id.betting_issue_item_checkbox , listVo.getTypeName());
        holder.setText(R.id.betting_issue_item_checkbox , listVo.getTypeName());

        if (playId.equals(listVo.getTypeName())) {
            holder.setChecked(R.id.betting_issue_item_checkbox , true);
            holder.setBackgroundRes(R.id.betting_issue_item_img ,R.mipmap.checked_blue);
        }else{
            holder.setChecked(R.id.betting_issue_item_checkbox , false);
            holder.setBackgroundRes(R.id.betting_issue_item_img ,R.mipmap.unchecked_grey);
        }

        holder.setOnCheckedChangeListener(R.id.betting_issue_item_checkbox, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!playId.equals(buttonView.getTag())) {
                        playId = (String)buttonView.getTag();
                    }
                }else{
                    if (playId.equals(buttonView.getTag())) {
                        Toast.makeText(mContext, mContext.getResources().getText(R.string.issue_check_singlg), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        holder.setOnClickListener(R.id.betting_issue_item_checkbox, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (issueClickChangeListener != null) {
                    issueClickChangeListener.onClick((CompoundButton)v , listVo.getTypeName() , holder.getPosition());
                }
            }
        });
        holder.setClickable(R.id.betting_issue_item_checkbox , true);
    }

    private IssueClickChangeListener issueClickChangeListener;
    public void setIssueClickChangeListener(IssueClickChangeListener issueListener){
        this.issueClickChangeListener = issueListener;
    }

    public interface IssueClickChangeListener{
        void onClick(CompoundButton buttonView , String typeName , int position);
    }
}
