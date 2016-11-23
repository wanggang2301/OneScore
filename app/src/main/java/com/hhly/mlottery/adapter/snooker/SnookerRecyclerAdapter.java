package com.hhly.mlottery.adapter.snooker;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchOddsBean;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchesBean;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;

import java.util.List;

/**
 * Created by yixq on 2016/11/16.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerRecyclerAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<SnookerMatchesBean> mData;



    public SnookerRecyclerAdapter(Context context ,List<SnookerMatchesBean> data){
        this.mContext = context;
        this.mData = data;
    }

    public void updateDatas(List<SnookerMatchesBean> data) {
        this.mData = data;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch(viewType){
            case 0:
                    //日期
                    view = LayoutInflater.from(mContext).inflate(R.layout.snooker_list_activity_date_item , parent , false);
                    holder = new ViewHolderDate(view);
                break;
            case 1:
                    //赛事
                    view = LayoutInflater.from(mContext).inflate(R.layout.snooker_list_activity_title_item , parent , false);
                    holder = new ViewHolderTitle(view);
                break;
            case 2:
                    //比赛
                    view = LayoutInflater.from(mContext).inflate(R.layout.snooker_list_activity_item , parent , false);
                    holder = new ViewHolderList(view);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (mData == null) {
            return;
        }
        
        SnookerMatchesBean DetailsData = mData.get(position);
        
        switch(getItemViewType(position)){
            case 0:
                //日期
                ViewHolderDate viewHolderDate = (ViewHolderDate) holder;
//                viewHolderDate.mSnookerDate.setText("2016-11-16 星期三" + position);
                viewHolderDate.mSnookerDate.setText(DetailsData.getItemDate());
                break;
            case 1:
                //赛事
                ViewHolderTitle viewHolderTitle = (ViewHolderTitle)holder;
//                viewHolderTitle.mSnookerTitle.setText("2016年世界斯洛克英格兰公开赛 " + position);
                viewHolderTitle.mSnookerTitle.setText(DetailsData.getItemLeaguesName());
                break;
            case 2:
                //比赛
                ViewHolderList viewHolderList = (ViewHolderList) holder;

                viewHolderList.mSnookerRound.setText(DetailsData.getSubLgName());
                viewHolderList.mSnookerTime.setText(DetailsData.getTime());
                viewHolderList.mSnookerNameLeft.setText(DetailsData.getHomeTeam());
                viewHolderList.mSnookerNameRight.setText(DetailsData.getGuestTeam());
                viewHolderList.mSnookerInning.setText("(" + DetailsData.getMatchStyle() + ")");
                /**
                 * 比赛状态
                 3 进行中
                 0 暂停
                 1 未开始
                 2 结束
                 */
                switch(DetailsData.getStatus()){
                    case "0":
                        viewHolderList.mSnookerStatus.setText("暂停");
                        break;
                    case "1":
                        viewHolderList.mSnookerStatus.setText("未开赛");
                        break;
                    case "2":
                        viewHolderList.mSnookerStatus.setText("结束");
                        break;
                    case "3":
                        viewHolderList.mSnookerStatus.setText("进行中");
                        break;
                    default:
                        viewHolderList.mSnookerStatus.setText("待定 :" + DetailsData.getStatus()); // TODO********
                        break;

                }

                if (DetailsData.getMatchScore() == null) {
                    viewHolderList.mSnookerScoreLeft.setText("--");
                    viewHolderList.mSnookerScoreRight.setText("--");
                }else{
                    viewHolderList.mSnookerScoreLeft.setText(DetailsData.getMatchScore().getPlayerOnewin());
                    viewHolderList.mSnookerScoreRight.setText(DetailsData.getMatchScore().getPlayerTwowin());
                }

                /**
                 *  设置赔率初始化值
                 */
                boolean alet = PreferenceUtil.getBoolean(MyConstants.SNOOKER_ALET, true); //亚盘 \ 标准盘
                boolean eur = PreferenceUtil.getBoolean(MyConstants.SNOOKER_EURO, false);//欧赔 \ 独赢
                boolean asize = PreferenceUtil.getBoolean(MyConstants.SNOOKER_ASIZE, false); //大小盘
                boolean singleTwins = PreferenceUtil.getBoolean(MyConstants.SNOOKER_SINGLETWIN , false);//单双
                boolean noshow = PreferenceUtil.getBoolean(MyConstants.SNOOKER_NOTSHOW, false);//不显示

                SnookerMatchOddsBean mOdds = DetailsData.getMatchOdds();
                if (mOdds == null) {
                    setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                }else{
                    /**
                     * 亚盘 -- 浩博
                     */
                    if (alet) {
                        if (mOdds.getStandard() == null) {
                            setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                        }else if(mOdds.getStandard().getHb() == null){
                            setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                        }else{
                            SnookerMatchOddsBean.SnookerMatchOddsDetailsBean.SnookerMatchOddsDataBean aletOdds = mOdds.getStandard().getHb();
                            viewHolderList.mSnookerOddsLeft.setText(aletOdds.getLeftOdds());
                            viewHolderList.mSnookerOddsMiddle.setText(aletOdds.getHandicapValue());
                            viewHolderList.mSnookerOddsRight.setText(aletOdds.getRightOdds());
                        }
                    }
                    /**
                     * 欧赔 -- 浩博
                     */
                    if (eur) {
                        if (mOdds.getOnlywin() == null) {
                            setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                        }else if(mOdds.getOnlywin().getHb() == null){
                            setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                        }else{
                            SnookerMatchOddsBean.SnookerMatchOddsDetailsBean.SnookerMatchOddsDataBean eurOdds = mOdds.getOnlywin().getHb();
                            viewHolderList.mSnookerOddsLeft.setText(eurOdds.getLeftOdds());
                            viewHolderList.mSnookerOddsMiddle.setText("胜负");
                            viewHolderList.mSnookerOddsRight.setText(eurOdds.getRightOdds());
                        }
//                        SnookerMatchOddsBean.SnookerMatchOddsDetailsBean.SnookerMatchOddsDataBean eurOdds = mOdds.getOnlywin().getHb();
//                        viewHolderList.mSnookerOddsLeft.setText("a" + position);
//                        viewHolderList.mSnookerOddsMiddle.setText("欧赔" + position);
//                        viewHolderList.mSnookerOddsRight.setText("c" + position);
                    }
                    /**
                     * 大小盘 -- 浩博
                     */
                    if (asize) {
                        if (mOdds.getBigsmall() == null) {
                            setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                        }else if(mOdds.getBigsmall().getHb() == null){
                            setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                        }else{
                            SnookerMatchOddsBean.SnookerMatchOddsDetailsBean.SnookerMatchOddsDataBean asizeOdds = mOdds.getBigsmall().getHb();
                            viewHolderList.mSnookerOddsLeft.setText(asizeOdds.getLeftOdds());
                            viewHolderList.mSnookerOddsMiddle.setText("总" + asizeOdds.getHandicapValue());
                            viewHolderList.mSnookerOddsRight.setText(asizeOdds.getRightOdds());
                        }
                    }
                    /**
                     * 单双 -- 浩博--> 利记
                     */
                    if (singleTwins) {
                        if (mOdds.getOnetwo() == null) {
                            setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                        }else if(mOdds.getOnetwo().getHb() == null){
                            if (mOdds.getOnetwo().getLj() == null) {
                                setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                            }else{
                                SnookerMatchOddsBean.SnookerMatchOddsDetailsBean.SnookerMatchOddsDataBean onetwoLj = mOdds.getOnetwo().getLj();
                                viewHolderList.mSnookerOddsLeft.setText(onetwoLj.getLeftOdds());
                                viewHolderList.mSnookerOddsMiddle.setText("单双");
                                viewHolderList.mSnookerOddsRight.setText(onetwoLj.getRightOdds());
                            }
                        }else{
                            SnookerMatchOddsBean.SnookerMatchOddsDetailsBean.SnookerMatchOddsDataBean singleTwinsOdds = mOdds.getOnetwo().getHb();
                            viewHolderList.mSnookerOddsLeft.setText(singleTwinsOdds.getLeftOdds());
                            viewHolderList.mSnookerOddsMiddle.setText(singleTwinsOdds.getHandicapValue());
                            viewHolderList.mSnookerOddsRight.setText(singleTwinsOdds.getRightOdds());
                        }
                    }
                    if (noshow) {
                        setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                    }
                }
                break;
        }
    }

    /**
     * 无赔率时设值
     * @param left 左赔率
     * @param middle 盘口
     * @param right 右赔率
     */
    private void setOddsNull(TextView left , TextView middle ,TextView right){
        left.setText("");
        middle.setText("");
        right.setText("");
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ViewHolderDate extends RecyclerView.ViewHolder{
        TextView mSnookerDate;
        public ViewHolderDate(View itemView) {
            super(itemView);
            mSnookerDate = (TextView) itemView.findViewById(R.id.snooker_date);
        }
    }
    class ViewHolderList extends RecyclerView.ViewHolder{
        private final CardView cardView;
        private final TextView mSnookerRound;
        private final TextView mSnookerTime;
        private final TextView mSnookerNameLeft;
        private final TextView mSnookerNameRight;
        private final TextView mSnookerStatus;
        private final TextView mSnookerScoreLeft;
        private final TextView mSnookerInning;
        private final TextView mSnookerScoreRight;
        private final TextView mSnookerOddsLeft;
        private final TextView mSnookerOddsMiddle;
        private final TextView mSnookerOddsRight;

        public ViewHolderList(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.snooker_card_view);
            mSnookerRound =  (TextView) itemView.findViewById(R.id.snooker_round);
            mSnookerTime = (TextView) itemView.findViewById(R.id.snooker_time);
            mSnookerNameLeft = (TextView) itemView.findViewById(R.id.snooker_name_left);
            mSnookerNameRight = (TextView) itemView.findViewById(R.id.snooker_name_right);
            mSnookerStatus = (TextView) itemView.findViewById(R.id.snooker_status);
            mSnookerScoreLeft = (TextView) itemView.findViewById(R.id.snooker_score_left);
            mSnookerInning = (TextView) itemView.findViewById(R.id.snooker_inning);
            mSnookerScoreRight = (TextView) itemView.findViewById(R.id.snooker_score_right);
            mSnookerOddsLeft = (TextView) itemView.findViewById(R.id.snooker_odds_left);
            mSnookerOddsMiddle = (TextView) itemView.findViewById(R.id.snooker_odds_middle);
            mSnookerOddsRight = (TextView) itemView.findViewById(R.id.snooker_odds_right);
        }
    }

    class ViewHolderTitle extends RecyclerView.ViewHolder{
        TextView mSnookerTitle;
        public ViewHolderTitle(View itemView) {
            super(itemView);
            mSnookerTitle = (TextView) itemView.findViewById(R.id.snooker_title);
        }
    }

}
