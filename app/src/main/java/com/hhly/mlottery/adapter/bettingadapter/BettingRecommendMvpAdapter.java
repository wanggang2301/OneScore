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
import com.hhly.mlottery.activity.BettingMvp.MvpBettingRecommendActivity;
import com.hhly.mlottery.view.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class BettingRecommendMvpAdapter extends BaseQuickAdapter<String> {

    private Context mContext;
    private List<String> mData;

    public BettingRecommendMvpAdapter(Context context, List<String> data) {
        super(R.layout.betting_recommend_item, data);
        this.mContext = context;
        this.mData = data;
    }

    public void updateData(List<String> data){
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

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(mContext).inflate(R.layout.betting_recommend_item, parent, false);
        BaseViewHolder holder = new ViewHolderData(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int positions) {
        if (mData == null) {
            return;
        }
        final ViewHolderData viewHolderData = (ViewHolderData) holder;
        final String data = mData.get(positions);
        viewHolderData.mSpecialistName.setText(data);

        /**
         * 购买（查看）点击
         */
        viewHolderData.mBuyOrCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBuyClick != null){
                    mBuyClick.BuyOnClick(v , data);
                }
            }
        });
        /**
         * 专家点击
         */
        viewHolderData.mSpecialistLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpecialistClick != null) {
                    mSpecialistClick.SpecialistOnClick(v , data);
                }
            }
        });
        viewHolderData.mGameDetailsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGameDetailsClick != null) {
                    mGameDetailsClick.GameDetailsOnClick(v , data);
                }
            }
        });
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        return super.getItemView(layoutResId, parent);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {

    }

    class ViewHolderData extends BaseViewHolder{

        private final CircleImageView mBettingPortrait;
        private final TextView mSpecialistName;
        private final TextView mSpecialistGrade;
        private final TextView mLianzhong;
        private final TextView mLatelyAccuracy;
        private final TextView mLeagueName;
        private final TextView mBettingRound;
        private final TextView mBettingDate;
        private final TextView mBettingWeek;
        private final TextView mBettingSPF;
        private final TextView mHomeName;
        private final TextView mVS;
        private final TextView mGuestName;
        private final TextView mPrice;
        private final TextView mBuyNum;
        private final LinearLayout mBuyOrCheck;
        private final TextView mRecommendedReason;
        private final LinearLayout mSpecialistLay;
        private final LinearLayout mGameDetailsLay;

        public ViewHolderData(View itemView) {
            super(itemView);
            mBettingPortrait = (CircleImageView)itemView.findViewById(R.id.betting_portrait_img);
            mSpecialistName = (TextView) itemView.findViewById(R.id.betting_specialist_name);
            mSpecialistGrade = (TextView) itemView.findViewById(R.id.betting_specialist_grade);
            mLianzhong = (TextView) itemView.findViewById(R.id.betting_lainzhong);
            mLatelyAccuracy = (TextView) itemView.findViewById(R.id.betting_lately_accuracy);
            mLeagueName = (TextView) itemView.findViewById(R.id.betting_league_name);
            mBettingRound = (TextView) itemView.findViewById(R.id.betting_round);
            mBettingDate = (TextView) itemView.findViewById(R.id.betting_date);
            mBettingWeek = (TextView) itemView.findViewById(R.id.betting_week);
            mBettingSPF = (TextView) itemView.findViewById(R.id.betting_concede_points_spf);
            mHomeName = (TextView) itemView.findViewById(R.id.betting_home_name);
            mVS = (TextView) itemView.findViewById(R.id.betting_vs);
            mGuestName = (TextView) itemView.findViewById(R.id.betting_guest_name);
            mPrice = (TextView) itemView.findViewById(R.id.betting_price);
            mBuyNum = (TextView) itemView.findViewById(R.id.betting_buy_num);
            mBuyOrCheck = (LinearLayout) itemView.findViewById(R.id.betting_tobuy_or_check);
            mRecommendedReason = (TextView) itemView.findViewById(R.id.betting_recommended_reason);
            mSpecialistLay = (LinearLayout) itemView.findViewById(R.id.betting_specialist_lay);
            mGameDetailsLay = (LinearLayout) itemView.findViewById(R.id.betting_game_details_lay);

        }
    }
}
