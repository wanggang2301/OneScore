package com.hhly.mlottery.adapter.bettingadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.bettingbean.BettingListDataBean;
import com.hhly.mlottery.mvp.bettingmvp.mvpview.FootballBettingIssueFragment;
import com.hhly.mlottery.mvp.bettingmvp.mvpview.MvpBettingRecommendActivity;
import com.hhly.mlottery.util.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 * 内页推介列表adapter
 */

public class BettingRecommendDetailsMvpAdapter extends BaseQuickAdapter<BettingListDataBean.PromotionData.BettingListData> {

    private Context mContext;
    private List<BettingListDataBean.PromotionData.BettingListData> mData;

    public BettingRecommendDetailsMvpAdapter(Context context, List<BettingListDataBean.PromotionData.BettingListData> data) {
        super(R.layout.betting_recommend_issue_item, data);
        this.mContext = context;
        this.mData = data;
    }

    public void updateData(List<BettingListDataBean.PromotionData.BettingListData> data){
        this.mData = data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final BettingListDataBean.PromotionData.BettingListData data) {

        ImageView icon = holder.getView(R.id.betting_issue_portrait_img);
        String imgUrl = data.getPhotoUrl();
        if (mContext != null) {
            ImageLoader.load(mContext,imgUrl,R.mipmap.center_head).into(icon);
        }

        holder.setText(R.id.betting_issue_specialist_name , filtraNull(data.getNickname()));
        holder.setText(R.id.betting_issue_specialist_grade , filtraNull(data.getExpert()));
//        holder.setText(R.id.betting_league_name , filtraNull(data.getLeagueName()));
//        holder.setText(R.id.betting_date , filtraNull(data.getMatchDateTime()));
        holder.setText(R.id.betting_issue_buy_num , filtraNull(data.getTypeStr()));
//        holder.setText(R.id.betting_home_name , filtraNull(data.getHomeName()));
//        holder.setText(R.id.betting_guest_name , filtraNull(data.getGuestName()));
        holder.setText(R.id.betting_issue_price , "￥ " + filtraNull(data.getPrice()));
        holder.setText(R.id.betting_issue_recommended_reason , filtraNull(data.getContext()));
//        holder.setText(R.id.betting_buy_num , filtraNull(data.getCountOrder()));

        int winPoint = 0;//胜场
        int errPoint = 0;//负场

        try {
            winPoint = Integer.parseInt(data.getWinPoint());
            errPoint = Integer.parseInt(data.getErrPoint());
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        int allPoint = winPoint + errPoint;

        holder.setText(R.id.betting_issue_lately_accuracy , mContext.getResources().getString(R.string.betting_item_jin) + allPoint + mContext.getResources().getString(R.string.betting_item_zhong) + winPoint);

        boolean lookStatus;
        if (data.getLookStatus() == null) {
            lookStatus = false;
            holder.setText(R.id.betting_issue_textView11 , "--");
        }else{
            lookStatus = true;
            if (data.getLookStatus().equals("2")) {
                holder.setText(R.id.betting_issue_textView11 , mContext.getResources().getString(R.string.betting_txt_buy));
            }else{
                holder.setText(R.id.betting_issue_textView11 , mContext.getResources().getString(R.string.betting_txt_check));
            }
        }

        LinearLayout mBuyOrCheck = holder.getView(R.id.betting_issue_tobuy_or_check);
        LinearLayout mSpecialistLay = holder.getView(R.id.betting_issue_specialist_lay);
        LinearLayout mGameDetailsLay = holder.getView(R.id.betting_issue_game_details_lay);

        /**
         * 购买（查看）点击
         */

        if (lookStatus) {
            mBuyOrCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (mBuyClick != null){
//                        mBuyClick.BuyOnClick(v , data);
//                    }
                    if (mFragBuyClick != null) {
                        mFragBuyClick.FragBuyOnClick(v , data);
                    }
                }
            });
        }

        /**
         * 专家点击
         */
        mSpecialistLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mSpecialistClick != null) {
//                    mSpecialistClick.SpecialistOnClick(v , data);
//                }
                if (mFragSpecialistClick != null) {
                    mFragSpecialistClick.FragSpecialistOnClick(v , data);
                }
            }
        });
//        mGameDetailsLay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mGameDetailsClick != null) {
//                    mGameDetailsClick.GameDetailsOnClick(v , data.getId());
//                }
//            }
//        });
    }


//    /**
//     * 购买（查看）监听
//     */
//    private MvpBettingRecommendActivity.BettingBuyClickListener mBuyClick; //关注监听回掉
//    public void setmBuyClick(MvpBettingRecommendActivity.BettingBuyClickListener mBuyClick) {
//        this.mBuyClick = mBuyClick;
//    }
    /**
     * 购买（查看）监听(内页)
     */
    private FootballBettingIssueFragment.BettingBuyClickListenerFrag mFragBuyClick; //关注监听回掉
    public void setmFragBuyClick(FootballBettingIssueFragment.BettingBuyClickListenerFrag mFragBuyClick) {
        this.mFragBuyClick = mFragBuyClick;
    }
//    /**
//     * 专家详情监听
//     */
//    private MvpBettingRecommendActivity.BettingSpecialistClickListener mSpecialistClick; //关注监听回掉
//    public void setmSpecialistClick(MvpBettingRecommendActivity.BettingSpecialistClickListener mSpecialistClick) {
//        this.mSpecialistClick = mSpecialistClick;
//    }
    /**
     * 专家详情监听
     */
    private FootballBettingIssueFragment.BettingSpecialistClickListenerFrag mFragSpecialistClick; //关注监听回掉
    public void setmFragSpecialistClick(FootballBettingIssueFragment.BettingSpecialistClickListenerFrag mFragSpecialistClick) {
        this.mFragSpecialistClick = mFragSpecialistClick;
    }

//    /**
//     * 内页详情监听
//     */
//    private MvpBettingRecommendActivity.BettingGameDetailsClickListener mGameDetailsClick;
//    public void setmGameDetailsClick(MvpBettingRecommendActivity.BettingGameDetailsClickListener mGameDetailsClick){
//        this.mGameDetailsClick = mGameDetailsClick;
//    }

    @Override
    public int getItemViewType(int position) {return super.getItemViewType(position);}

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        return super.getItemView(layoutResId, parent);
    }

    private String filtraNull(String str){

        if (str == null) {
            return "--";
        }else{
            return str;
        }
    }
}
