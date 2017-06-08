package com.hhly.mlottery.adapter.bettingadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.bettingbean.BettingListDataBean;
import com.hhly.mlottery.mvp.bettingmvp.mvpview.MvpBettingRecommendActivity;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.view.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class BettingRecommendMvpAdapter extends BaseQuickAdapter<BettingListDataBean.PromotionData.BettingListData> {

    private Context mContext;
    private List<BettingListDataBean.PromotionData.BettingListData> mData;

    public BettingRecommendMvpAdapter(Context context, List<BettingListDataBean.PromotionData.BettingListData> data) {
        super(R.layout.betting_recommend_item, data);
        this.mContext = context;
        this.mData = data;
    }

    public void updateData(List<BettingListDataBean.PromotionData.BettingListData> data){
        this.mData = data;
    }
    /**
     * 购买（查看）监听
     */
    private MvpBettingRecommendActivity.BettingBuyClickListener mBuyClick; //关注监听回掉
    public void setmBuyClick(MvpBettingRecommendActivity.BettingBuyClickListener mBuyClick) {
        this.mBuyClick = mBuyClick;
    }
    /**
     * 专家详情监听
     */
    private MvpBettingRecommendActivity.BettingSpecialistClickListener mSpecialistClick; //关注监听回掉
    public void setmSpecialistClick(MvpBettingRecommendActivity.BettingSpecialistClickListener mSpecialistClick) {
        this.mSpecialistClick = mSpecialistClick;
    }

    /**
     * 内页详情监听
     */
    private MvpBettingRecommendActivity.BettingGameDetailsClickListener mGameDetailsClick;
    public void setmGameDetailsClick(MvpBettingRecommendActivity.BettingGameDetailsClickListener mGameDetailsClick){
        this.mGameDetailsClick = mGameDetailsClick;
    }

    @Override
    public int getItemViewType(int position) {return super.getItemViewType(position);}

//    @Override
//    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View  view = LayoutInflater.from(mContext).inflate(R.layout.betting_recommend_item, parent, false);
//        BaseViewHolder holder = new ViewHolderData(view);
//        return holder;
//    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int positions) {
//        if (mData == null) {
//            return;
//        }
//        final ViewHolderData viewHolderData = (ViewHolderData) holder;
//
//        final BettingListDataBean.PromotionData.BettingListData data = mData.get(positions);
//
//        String imgUrl = data.getPhotoUrl();
//        ImageLoader.load(mContext,imgUrl,R.mipmap.football_analyze_default).into(viewHolderData.mBettingPortrait);
//
//        viewHolderData.mSpecialistName.setText(data.getUserid() + "");
//        viewHolderData.mSpecialistGrade.setText(data.getExpert() + "");
//        viewHolderData.mLeagueName.setText(data.getLeagueName() + "");
//        viewHolderData.mBettingDate.setText(data.getReleaseDate() + "");
//        viewHolderData.mBettingSPF.setText(data.getTypeStr() + "");
//        viewHolderData.mHomeName.setText(data.getHomeName() + "");
//        viewHolderData.mGuestName.setText(data.getGuestName() + "");
//        viewHolderData.mPrice.setText("￥ " + data.getPrice());
//        viewHolderData.mRecommendedReason.setText(data.getContext() + "");
//
//        /**
//         * 购买（查看）点击
//         */
//        viewHolderData.mBuyOrCheck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mBuyClick != null){
//                    mBuyClick.BuyOnClick(v , data.getId());
//                }
//            }
//        });
//        /**
//         * 专家点击
//         */
//        viewHolderData.mSpecialistLay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mSpecialistClick != null) {
//                    mSpecialistClick.SpecialistOnClick(v , data.getId());
//                }
//            }
//        });
//        viewHolderData.mGameDetailsLay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mGameDetailsClick != null) {
//                    mGameDetailsClick.GameDetailsOnClick(v , data.getId());
//                }
//            }
//        });
//    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        return super.getItemView(layoutResId, parent);
    }

    @Override
    protected void convert(BaseViewHolder holder, final BettingListDataBean.PromotionData.BettingListData data) {
//        if (mData == null) {
//            return;
//        }
//        final ViewHolderData viewHolderData = (ViewHolderData) holder;

//        final BettingListDataBean.PromotionData.BettingListData data = mData.get(positions);

        ImageView icon = holder.getView(R.id.betting_portrait_img);
        String imgUrl = data.getPhotoUrl();
        if (mContext != null) {
            ImageLoader.load(mContext,imgUrl,R.mipmap.football_analyze_default).into(icon);
        }

        holder.setText(R.id.betting_specialist_name , filtraNull(data.getUserid()));
        holder.setText(R.id.betting_specialist_grade , filtraNull(data.getExpert()));
        holder.setText(R.id.betting_league_name , filtraNull(data.getLeagueName()));
        holder.setText(R.id.betting_date , filtraNull(data.getReleaseDate()));
        holder.setText(R.id.betting_concede_points_spf , filtraNull(data.getTypeStr()));
        holder.setText(R.id.betting_home_name , filtraNull(data.getHomeName()));
        holder.setText(R.id.betting_guest_name , filtraNull(data.getGuestName()));
        holder.setText(R.id.betting_price , "￥ " + filtraNull(data.getPrice()));
        holder.setText(R.id.betting_recommended_reason , filtraNull(data.getContext()));
        holder.setText(R.id.betting_buy_num , filtraNull(data.getCountOrder()));

        int winPoint = 0;//胜场
        int errPoint = 0;//负场

        try {
            winPoint = Integer.parseInt(data.getWinPoint());
            errPoint = Integer.parseInt(data.getErrPoint());
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        int allPoint = winPoint + errPoint;

        holder.setText(R.id.betting_lately_accuracy , mContext.getResources().getString(R.string.betting_item_jin) + allPoint + mContext.getResources().getString(R.string.betting_item_zhong) + winPoint);

        boolean lookStatus;
        if (data.getLookStatus() == null) {
            lookStatus = false;
            holder.setText(R.id.textView11 , "--");
        }else{
            lookStatus = true;
            if (data.getLookStatus().equals("2")) {
                holder.setText(R.id.textView11 , mContext.getResources().getString(R.string.betting_txt_buy));
            }else{
                holder.setText(R.id.textView11 , mContext.getResources().getString(R.string.betting_txt_check));
            }
        }

//        viewHolderData.mSpecialistName.setText(data.getUserid() + "");
//        viewHolderData.mSpecialistGrade.setText(data.getExpert() + "");
//        viewHolderData.mLeagueName.setText(data.getLeagueName() + "");
//        viewHolderData.mBettingDate.setText(data.getReleaseDate() + "");
//        viewHolderData.mBettingSPF.setText(data.getTypeStr() + "");
//        viewHolderData.mHomeName.setText(data.getHomeName() + "");
//        viewHolderData.mGuestName.setText(data.getGuestName() + "");
//        viewHolderData.mPrice.setText("￥ " + data.getPrice());
//        viewHolderData.mRecommendedReason.setText(data.getContext() + "");

        LinearLayout mBuyOrCheck = holder.getView(R.id.betting_tobuy_or_check);
        LinearLayout mSpecialistLay = holder.getView(R.id.betting_specialist_lay);
        LinearLayout mGameDetailsLay = holder.getView(R.id.betting_game_details_lay);

        /**
         * 购买（查看）点击
         */

        if (lookStatus) {
            mBuyOrCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBuyClick != null){
                        mBuyClick.BuyOnClick(v , data);
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
                if (mSpecialistClick != null) {
                    mSpecialistClick.SpecialistOnClick(v , data.getId());
                }
            }
        });
        mGameDetailsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGameDetailsClick != null) {
                    mGameDetailsClick.GameDetailsOnClick(v , data.getId());
                }
            }
        });
    }

    private String filtraNull(String str){

        if (str == null) {
            return "--";
        }else{
            return str;
        }
    }

//    class ViewHolderData extends BaseViewHolder{
//
//        private final CircleImageView mBettingPortrait;
//        private final TextView mSpecialistName;
//        private final TextView mSpecialistGrade;
//        private final TextView mLianzhong;
//        private final TextView mLatelyAccuracy;
//        private final TextView mLeagueName;
//        private final TextView mBettingRound;
//        private final TextView mBettingDate;
//        private final TextView mBettingWeek;
//        private final TextView mBettingSPF;
//        private final TextView mHomeName;
//        private final TextView mVS;
//        private final TextView mGuestName;
//        private final TextView mPrice;
//        private final TextView mBuyNum;
//        private final LinearLayout mBuyOrCheck;
//        private final TextView mRecommendedReason;
//        private final LinearLayout mSpecialistLay;
//        private final LinearLayout mGameDetailsLay;
//
//        public ViewHolderData(View itemView) {
//            super(itemView);
//            mBettingPortrait = (CircleImageView)itemView.findViewById(R.id.betting_portrait_img);
//            mSpecialistName = (TextView) itemView.findViewById(R.id.betting_specialist_name);
//            mSpecialistGrade = (TextView) itemView.findViewById(R.id.betting_specialist_grade);
//            mLianzhong = (TextView) itemView.findViewById(R.id.betting_lainzhong);
//            mLatelyAccuracy = (TextView) itemView.findViewById(R.id.betting_lately_accuracy);
//            mLeagueName = (TextView) itemView.findViewById(R.id.betting_league_name);
//            mBettingRound = (TextView) itemView.findViewById(R.id.betting_round);
//            mBettingDate = (TextView) itemView.findViewById(R.id.betting_date);
//            mBettingWeek = (TextView) itemView.findViewById(R.id.betting_week);
//            mBettingSPF = (TextView) itemView.findViewById(R.id.betting_concede_points_spf);
//            mHomeName = (TextView) itemView.findViewById(R.id.betting_home_name);
//            mVS = (TextView) itemView.findViewById(R.id.betting_vs);
//            mGuestName = (TextView) itemView.findViewById(R.id.betting_guest_name);
//            mPrice = (TextView) itemView.findViewById(R.id.betting_price);
//            mBuyNum = (TextView) itemView.findViewById(R.id.betting_buy_num);
//            mBuyOrCheck = (LinearLayout) itemView.findViewById(R.id.betting_tobuy_or_check);
//            mRecommendedReason = (TextView) itemView.findViewById(R.id.betting_recommended_reason);
//            mSpecialistLay = (LinearLayout) itemView.findViewById(R.id.betting_specialist_lay);
//            mGameDetailsLay = (LinearLayout) itemView.findViewById(R.id.betting_game_details_lay);
//
//        }
//    }
}
