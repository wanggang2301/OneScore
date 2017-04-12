package com.hhly.mlottery.adapter.snooker;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchOddsBean;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchesBean;
import com.hhly.mlottery.bean.snookerbean.snookerschedulebean.SnookerEventsBean;
import com.hhly.mlottery.bean.snookerbean.snookerschedulebean.SnookerOddsMatchBean;
import com.hhly.mlottery.callback.DateOnClickListener;
import com.hhly.mlottery.callback.RecyclerViewItemClickListener;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.ResultDateUtil;

import java.util.List;

/**
 * Created by yixq on 2016/11/16.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerListAdapter extends BaseQuickAdapter<SnookerEventsBean> {

    private Context mContext;
    private List<SnookerEventsBean> mData;

    /**
     * 赛事item click
     */
    private RecyclerViewItemClickListener mOnItemClickListener = null;

    public void setmOnItemClickListener(RecyclerViewItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * 日期选择
     */
    private DateOnClickListener dateOnClickListener = null;

    public void setDateOnClickListener(DateOnClickListener dateOnClickListener) {
        this.dateOnClickListener = dateOnClickListener;
    }

    public SnookerListAdapter(Context context , List<SnookerEventsBean> data){
        super(R.layout.snooker_list_activity_item, data);
        this.mContext = context;
        this.mData = data;
    }

    public void updateDatas(List<SnookerEventsBean> data) {
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
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        BaseViewHolder holder = null;
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
        if (viewType == 0) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dateOnClickListener != null) {
                        dateOnClickListener.onClick(view);
                    }
                }
            });
        }else if (viewType == 2) {
            //将创建的View注册点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, (String) v.getTag());
                    }
                }
            });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (mData == null) {
            return;
        }

        SnookerEventsBean DetailsData = mData.get(position);

        switch(getItemViewType(position)){
            case 0:
                //日期
                ViewHolderDate viewHolderDate = (ViewHolderDate) holder;
                viewHolderDate.mSnookerDate.setText(DetailsData.getItemDate());
                viewHolderDate.mSnookerWeek.setText(ResultDateUtil.getWeekOfDate(DateUtil.parseDate(ResultDateUtil.getDate(0,DetailsData.getItemDate()))));
                break;
            case 1:
                //赛事
                ViewHolderTitle viewHolderTitle = (ViewHolderTitle)holder;
                viewHolderTitle.mSnookerTitle.setText(DetailsData.getItemLeaguesName());
                break;
            case 2:
                //比赛
                ViewHolderList viewHolderList = (ViewHolderList) holder;

                viewHolderList.itemView.setTag(DetailsData.getMatchId());//记录当前ID

                viewHolderList.mSnookerRound.setText(DetailsData.getRound());
                viewHolderList.mSnookerTime.setText(DetailsData.getTime());
                viewHolderList.mSnookerNameLeft.setText(DetailsData.getHomeTeam());
                viewHolderList.mSnookerNameRight.setText(DetailsData.getGuestTeam());

                /**
                 * 比赛状态
                 3 进行中
                 0 暂停
                 1 未开始
                 2 结束
                 两种情况  ① MatchScore为null时，即 没有比分数据时，比赛状态去外层Status，
                          ② MatchScore不为为null时，即 有比分数据(进行中 或 完场)，比赛状态取MatchScore里面的 Status状态
                        （ps：由于推送时，更新的是MatchScore里面的状态，未开赛==>开赛MatchScore为null，会出现状态无法更新情况）

                 */
                String currentStatus = "";
                if (DetailsData.getMatchScore() == null){
                    /**
                      情况①
                     */
                    if (DetailsData.getStatus() == null) {
                        currentStatus = "null";
                    }else{
                        currentStatus = DetailsData.getStatus();
                    }
                }else{
                    /**
                      情况②
                     */
                    if (DetailsData.getMatchScore().getStatus() == null) {
                        currentStatus = "null";
                    }else{
                        currentStatus = DetailsData.getMatchScore().getStatus();
                    }
                }

                switch(currentStatus){
                    case "0":
                        viewHolderList.mSnookerStatus.setText(mContext.getString(R.string.snooker_state_pause));
                        viewHolderList.mSnookerStatus.setTextColor(mContext.getResources().getColor(R.color.snooker_fulltime_textcolor));
                        viewHolderList.mSnookerStatus.setVisibility(View.VISIBLE);
                        if (DetailsData.getMatchScore() == null || DetailsData.getMatchScore().equals("")) {
                            viewHolderList.mSnookerInning.setText("-");
                        }else{
                            if (DetailsData.getMatchScore().getTotalGames() == null || DetailsData.getMatchScore().getTotalGames().equals("")) {
                                viewHolderList.mSnookerInning.setText("-");
                            }else{
                                viewHolderList.mSnookerInning.setText("(" + DetailsData.getMatchScore().getTotalGames() + ")");
                            }
                        }
                        if (DetailsData.getMatchScore() == null || DetailsData.getMatchScore().equals("")) {
                            viewHolderList.mPoscore.setText("-");
                            viewHolderList.mPtScore.setText("-");
                        }else{
                            if (DetailsData.getMatchScore().getPoScore() == null || DetailsData.getMatchScore().getPoScore().equals("")) {
                                viewHolderList.mPoscore.setText("-");
                            }else{
                                viewHolderList.mPoscore.setText(DetailsData.getMatchScore().getPoScore());
                            }
                            if (DetailsData.getMatchScore().getPtScore() == null || DetailsData.getMatchScore().getPtScore().equals("")) {
                                viewHolderList.mPtScore.setText("-");
                            }else{
                                viewHolderList.mPtScore.setText(DetailsData.getMatchScore().getPtScore());
                            }
                        }
                        if (DetailsData.getMatchScore() == null) {
                            viewHolderList.mSnookerScoreLeft.setText("--");
                            viewHolderList.mSnookerScoreRight.setText("--");
                        }else{
                            if (DetailsData.getMatchScore().getPlayerOnewin() == null || DetailsData.getMatchScore().getPlayerOnewin().equals("")) {
                                viewHolderList.mSnookerScoreLeft.setText("--");
                            }else{
                                viewHolderList.mSnookerScoreLeft.setText(DetailsData.getMatchScore().getPlayerOnewin());
                            }
                            if (DetailsData.getMatchScore().getPlayerTwowin() == null || DetailsData.getMatchScore().getPlayerTwowin().equals("")) {
                                viewHolderList.mSnookerScoreRight.setText("--");
                            }else{
                                viewHolderList.mSnookerScoreRight.setText(DetailsData.getMatchScore().getPlayerTwowin());
                            }
                        }
                        viewHolderList.mSnookerScoreLeft.setTextColor(mContext.getResources().getColor(R.color.snooker_score_textcolor));
                        viewHolderList.mSnookerScoreRight.setTextColor(mContext.getResources().getColor(R.color.snooker_score_textcolor));
                        setRETstatus(viewHolderList , false);
                        break;
                    case "1":
                        viewHolderList.mSnookerStatus.setText(mContext.getString(R.string.snooker_state_no_start));
                        viewHolderList.mSnookerStatus.setTextColor(mContext.getResources().getColor(R.color.snooker_odds_textcolor));
                        viewHolderList.mSnookerStatus.setVisibility(View.VISIBLE);
                        viewHolderList.mSnookerInning.setText("(VS)");
                        viewHolderList.mPoscore.setText("0");
                        viewHolderList.mPtScore.setText("0");
                        viewHolderList.mSnookerScoreLeft.setText("0");
                        viewHolderList.mSnookerScoreRight.setText("0");
                        viewHolderList.mSnookerScoreLeft.setTextColor(mContext.getResources().getColor(R.color.snooker_score_textcolor));
                        viewHolderList.mSnookerScoreRight.setTextColor(mContext.getResources().getColor(R.color.snooker_score_textcolor));
                        setRETstatus(viewHolderList , false);
                        break;
                    case "2":
                        viewHolderList.mSnookerStatus.setText(mContext.getString(R.string.snooker_state_over_game));
                        viewHolderList.mSnookerStatus.setTextColor(mContext.getResources().getColor(R.color.snooker_fulltime_textcolor));
                        viewHolderList.mSnookerStatus.setVisibility(View.VISIBLE);
                        if (DetailsData.getMatchScore() == null || DetailsData.getMatchScore().equals("")) {
                            viewHolderList.mSnookerInning.setText("-");
                        }else{
                            if (DetailsData.getMatchScore().getTotalGames() == null || DetailsData.getMatchScore().getTotalGames().equals("")) {
                                viewHolderList.mSnookerInning.setText("-");
                            }else{
                                viewHolderList.mSnookerInning.setText("(" + DetailsData.getMatchScore().getTotalGames() + ")");
                            }
                        }
                        if (DetailsData.getMatchScore() == null || DetailsData.getMatchScore().equals("")) {
                            viewHolderList.mPoscore.setText("-");
                            viewHolderList.mPtScore.setText("-");
                        }else{
                            if (DetailsData.getMatchScore().getPoScore() == null || DetailsData.getMatchScore().getPoScore().equals("")) {
                                viewHolderList.mPoscore.setText("-");
                            }else{
                                viewHolderList.mPoscore.setText(DetailsData.getMatchScore().getPoScore());
                            }
                            if (DetailsData.getMatchScore().getPtScore() == null || DetailsData.getMatchScore().getPtScore().equals("")) {
                                viewHolderList.mPtScore.setText("-");
                            }else{
                                viewHolderList.mPtScore.setText(DetailsData.getMatchScore().getPtScore());
                            }
                        }
                        if (DetailsData.getMatchScore() == null) {
                            viewHolderList.mSnookerScoreLeft.setText("--");
                            viewHolderList.mSnookerScoreRight.setText("--");
                        }else{
                            String oneWin = "";
                            String twoWin = "";
                            int oneNumWin = 0;
                            int twoNumWin = 0;

                            if (DetailsData.getMatchScore().getPlayerOnewin() == null || DetailsData.getMatchScore().getPlayerOnewin().equals("")) {
                                viewHolderList.mSnookerScoreLeft.setText("--");
                            }else{
                                viewHolderList.mSnookerScoreLeft.setText(DetailsData.getMatchScore().getPlayerOnewin());
                                oneWin = DetailsData.getMatchScore().getPlayerOnewin();
                            }
                            if (DetailsData.getMatchScore().getPlayerTwowin() == null || DetailsData.getMatchScore().getPlayerTwowin().equals("")) {
                                viewHolderList.mSnookerScoreRight.setText("--");
                            }else{
                                viewHolderList.mSnookerScoreRight.setText(DetailsData.getMatchScore().getPlayerTwowin());
                                twoWin = DetailsData.getMatchScore().getPlayerTwowin();
                            }

                            try {
                                oneNumWin = Integer.parseInt(oneWin);
                                twoNumWin = Integer.parseInt(twoWin);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                            if (oneNumWin > twoNumWin) {
                                viewHolderList.mSnookerScoreLeft.setTextColor(mContext.getResources().getColor(R.color.snooker_fulltime_textcolor));
                                viewHolderList.mSnookerScoreRight.setTextColor(mContext.getResources().getColor(R.color.snooker_score_textcolor));
                            }else if(oneNumWin < twoNumWin){
                                viewHolderList.mSnookerScoreLeft.setTextColor(mContext.getResources().getColor(R.color.snooker_score_textcolor));
                                viewHolderList.mSnookerScoreRight.setTextColor(mContext.getResources().getColor(R.color.snooker_fulltime_textcolor));
                            }else{
                                viewHolderList.mSnookerScoreLeft.setTextColor(mContext.getResources().getColor(R.color.snooker_score_textcolor));
                                viewHolderList.mSnookerScoreRight.setTextColor(mContext.getResources().getColor(R.color.snooker_score_textcolor));
                            }
                        }
                        setRETstatus(viewHolderList , false);
                        break;
                    case "3":
                        viewHolderList.mSnookerStatus.setText(mContext.getString(R.string.snooker_state_have_ing));
                        viewHolderList.mSnookerStatus.setTextColor(mContext.getResources().getColor(R.color.snooker_status));
                        viewHolderList.mSnookerStatus.setVisibility(View.VISIBLE);
                        if (DetailsData.getMatchScore() == null || DetailsData.getMatchScore().equals("")) {
                            viewHolderList.mSnookerInning.setText("-");
                        }else{
                            if (DetailsData.getMatchScore().getTotalGames() == null || DetailsData.getMatchScore().getTotalGames().equals("")) {
                                viewHolderList.mSnookerInning.setText("-");
                            }else{
                                viewHolderList.mSnookerInning.setText("(" + DetailsData.getMatchScore().getTotalGames() + ")");
                            }
                        }

                        if (DetailsData.getMatchScore() == null || DetailsData.getMatchScore().equals("")) {
                            viewHolderList.mPoscore.setText("-");
                            viewHolderList.mPtScore.setText("-");
                        }else{
                            if (DetailsData.getMatchScore().getPoScore() == null || DetailsData.getMatchScore().getPoScore().equals("")) {
                                viewHolderList.mPoscore.setText("-");
                            }else{
                                viewHolderList.mPoscore.setText(DetailsData.getMatchScore().getPoScore());
                            }
                            if (DetailsData.getMatchScore().getPtScore() == null || DetailsData.getMatchScore().getPtScore().equals("")) {
                                viewHolderList.mPtScore.setText("-");
                            }else{
                                viewHolderList.mPtScore.setText(DetailsData.getMatchScore().getPtScore());
                            }
                        }
                        if (DetailsData.getMatchScore() == null) {
                            viewHolderList.mSnookerScoreLeft.setText("--");
                            viewHolderList.mSnookerScoreRight.setText("--");
                        }else{
                            if (DetailsData.getMatchScore().getPlayerOnewin() == null || DetailsData.getMatchScore().getPlayerOnewin().equals("")) {
                                viewHolderList.mSnookerScoreLeft.setText("--");
                            }else{
                                viewHolderList.mSnookerScoreLeft.setText(DetailsData.getMatchScore().getPlayerOnewin());
                            }
                            if (DetailsData.getMatchScore().getPlayerTwowin() == null || DetailsData.getMatchScore().getPlayerTwowin().equals("")) {
                                viewHolderList.mSnookerScoreRight.setText("--");
                            }else{
                                viewHolderList.mSnookerScoreRight.setText(DetailsData.getMatchScore().getPlayerTwowin());
                            }
                        }
                        viewHolderList.mSnookerScoreLeft.setTextColor(mContext.getResources().getColor(R.color.snooker_score_textcolor));
                        viewHolderList.mSnookerScoreRight.setTextColor(mContext.getResources().getColor(R.color.snooker_score_textcolor));
                        setRETstatus(viewHolderList , false);
                        break;
                    case "4":
                        viewHolderList.mSnookerStatus.setText(mContext.getString(R.string.snooker_state_resting));
                        viewHolderList.mSnookerStatus.setTextColor(mContext.getResources().getColor(R.color.snooker_fulltime_textcolor));
                        viewHolderList.mSnookerStatus.setVisibility(View.VISIBLE);
                        if (DetailsData.getMatchScore() == null || DetailsData.getMatchScore().equals("")) {
                            viewHolderList.mSnookerInning.setText("-");
                        }else{
                            if (DetailsData.getMatchScore().getTotalGames() == null || DetailsData.getMatchScore().getTotalGames().equals("")) {
                                viewHolderList.mSnookerInning.setText("-");
                            }else{
                                viewHolderList.mSnookerInning.setText("(" + DetailsData.getMatchScore().getTotalGames() + ")");
                            }
                        }
                        if (DetailsData.getMatchScore() == null || DetailsData.getMatchScore().equals("")) {
                            viewHolderList.mPoscore.setText("-");
                            viewHolderList.mPtScore.setText("-");
                        }else{
                            if (DetailsData.getMatchScore().getPoScore() == null || DetailsData.getMatchScore().getPoScore().equals("")) {
                                viewHolderList.mPoscore.setText("-");
                            }else{
                                viewHolderList.mPoscore.setText(DetailsData.getMatchScore().getPoScore());
                            }
                            if (DetailsData.getMatchScore().getPtScore() == null || DetailsData.getMatchScore().getPtScore().equals("")) {
                                viewHolderList.mPtScore.setText("-");
                            }else{
                                viewHolderList.mPtScore.setText(DetailsData.getMatchScore().getPtScore());
                            }
                        }
                        if (DetailsData.getMatchScore() == null) {
                            viewHolderList.mSnookerScoreLeft.setText("--");
                            viewHolderList.mSnookerScoreRight.setText("--");
                        }else{
                            if (DetailsData.getMatchScore().getPlayerOnewin() == null || DetailsData.getMatchScore().getPlayerOnewin().equals("")) {
                                viewHolderList.mSnookerScoreLeft.setText("--");
                            }else{
                                viewHolderList.mSnookerScoreLeft.setText(DetailsData.getMatchScore().getPlayerOnewin());
                            }
                            if (DetailsData.getMatchScore().getPlayerTwowin() == null || DetailsData.getMatchScore().getPlayerTwowin().equals("")) {
                                viewHolderList.mSnookerScoreRight.setText("--");
                            }else{
                                viewHolderList.mSnookerScoreRight.setText(DetailsData.getMatchScore().getPlayerTwowin());
                            }
                        }
                        viewHolderList.mSnookerScoreLeft.setTextColor(mContext.getResources().getColor(R.color.snooker_score_textcolor));
                        viewHolderList.mSnookerScoreRight.setTextColor(mContext.getResources().getColor(R.color.snooker_score_textcolor));
                        setRETstatus(viewHolderList , false);
                        break;
                    case "-5":
                    case "-6":
                        viewHolderList.mSnookerStatus.setText("");
                        viewHolderList.mSnookerStatus.setTextColor(mContext.getResources().getColor(R.color.snooker_fulltime_textcolor));
                        viewHolderList.mSnookerStatus.setVisibility(View.INVISIBLE);
                        if (DetailsData.getMatchScore() == null || DetailsData.getMatchScore().equals("")) {
                            viewHolderList.mSnookerInning.setText("-");
                        }else{
                            if (DetailsData.getMatchScore().getTotalGames() == null || DetailsData.getMatchScore().getTotalGames().equals("")) {
                                viewHolderList.mSnookerInning.setText("-");
                            }else{
                                viewHolderList.mSnookerInning.setText("(" + DetailsData.getMatchScore().getTotalGames() + ")");
                            }
                        }
                        if (DetailsData.getMatchScore() == null || DetailsData.getMatchScore().equals("")) {
                            viewHolderList.mPoscore.setText("-");
                            viewHolderList.mPtScore.setText("-");
                        }else{
                            if (DetailsData.getMatchScore().getPoScore() == null || DetailsData.getMatchScore().getPoScore().equals("")) {
                                viewHolderList.mPoscore.setText("-");
                            }else{
                                viewHolderList.mPoscore.setText(DetailsData.getMatchScore().getPoScore());
                            }
                            if (DetailsData.getMatchScore().getPtScore() == null || DetailsData.getMatchScore().getPtScore().equals("")) {
                                viewHolderList.mPtScore.setText("-");
                            }else{
                                viewHolderList.mPtScore.setText(DetailsData.getMatchScore().getPtScore());
                            }
                        }
                        if (DetailsData.getMatchScore() == null) {
                            viewHolderList.mSnookerScoreLeft.setText("--");
                            viewHolderList.mSnookerScoreRight.setText("--");
                        }else{
                            if (DetailsData.getMatchScore().getPlayerOnewin() == null || DetailsData.getMatchScore().getPlayerOnewin().equals("")) {
                                viewHolderList.mSnookerScoreLeft.setText("--");
                            }else{
                                viewHolderList.mSnookerScoreLeft.setText(DetailsData.getMatchScore().getPlayerOnewin());
                            }
                            if (DetailsData.getMatchScore().getPlayerTwowin() == null || DetailsData.getMatchScore().getPlayerTwowin().equals("")) {
                                viewHolderList.mSnookerScoreRight.setText("--");
                            }else{
                                viewHolderList.mSnookerScoreRight.setText(DetailsData.getMatchScore().getPlayerTwowin());
                            }
                        }
                        viewHolderList.mSnookerScoreLeft.setTextColor(mContext.getResources().getColor(R.color.snooker_score_textcolor));
                        viewHolderList.mSnookerScoreRight.setTextColor(mContext.getResources().getColor(R.color.snooker_score_textcolor));

                        if (currentStatus.equals("-5")) {
                            viewHolderList.mGuestRET.setText("");
                            viewHolderList.mHomeRET.setText(mContext.getResources().getString(R.string.score_ret));
                        }else if(currentStatus.equals("-6")){
                            viewHolderList.mGuestRET.setText(mContext.getResources().getString(R.string.score_ret));
                            viewHolderList.mHomeRET.setText("");
                        }
                        setRETstatus(viewHolderList , true);
                        break;
                    default:
                        viewHolderList.mSnookerStatus.setText("--");
                        viewHolderList.mSnookerStatus.setVisibility(View.VISIBLE);
                        viewHolderList.mSnookerInning.setText("-");
                        viewHolderList.mPoscore.setText("-");
                        viewHolderList.mPtScore.setText("-");
                        viewHolderList.mSnookerScoreLeft.setText("-");
                        viewHolderList.mSnookerScoreRight.setText("-");
                        viewHolderList.mSnookerScoreLeft.setTextColor(mContext.getResources().getColor(R.color.snooker_score_textcolor));
                        viewHolderList.mSnookerScoreRight.setTextColor(mContext.getResources().getColor(R.color.snooker_score_textcolor));
                        setRETstatus(viewHolderList , false);
                        break;

                }

//                if (DetailsData.getMatchScore() == null) {
//                    viewHolderList.mSnookerScoreLeft.setText("--");
//                    viewHolderList.mSnookerScoreRight.setText("--");
//                }else{
//                    viewHolderList.mSnookerScoreLeft.setText(DetailsData.getMatchScore().getPlayerOnewin());
//
//                    viewHolderList.mSnookerScoreRight.setText(DetailsData.getMatchScore().getPlayerTwowin());
//                }

                /**
                 *  设置赔率初始化值
                 */
                boolean noshow = PreferenceUtil.getBoolean(MyConstants.SNOOKER_NOTSHOW, false);//不显示
                boolean alet = PreferenceUtil.getBoolean(MyConstants.SNOOKER_ALET, true); //亚盘 \ 标准盘
                boolean eur = PreferenceUtil.getBoolean(MyConstants.SNOOKER_EURO, false);//欧赔 \ 独赢
                boolean asize = PreferenceUtil.getBoolean(MyConstants.SNOOKER_ASIZE, false); //大小盘
                boolean singleTwins = PreferenceUtil.getBoolean(MyConstants.SNOOKER_SINGLETWIN , false);//单双

                SnookerOddsMatchBean mOdds = DetailsData.getMatchOdds();
                if (mOdds == null) {
                    setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                }else{
                    /**
                     * 亚盘 -- 浩博
                     */
                    if (alet) {
                        if (mOdds.getAsiaLet() == null) {
                            setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                        }else if(mOdds.getAsiaLet().getSBO() == null){
                            setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                        }else{
                            SnookerOddsMatchBean.SnookerMatchOddsDetailsBean.SnookerMatchOddsDataBean aletOdds = mOdds.getAsiaLet().getSBO();
                            viewHolderList.mSnookerOddsLeft.setText(aletOdds.getLeftOdds());
                            viewHolderList.mSnookerOddsMiddle.setText(aletOdds.getHandicapValue());
                            viewHolderList.mSnookerOddsRight.setText(aletOdds.getRightOdds());
                        }
                    }
                    /**
                     * 欧赔 -- 浩博
                     */
                    if (eur) {
                        if (mOdds.getOnlyWin() == null) {
                            setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                        }else if(mOdds.getOnlyWin().getSBO() == null){
                            setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                        }else{
                            SnookerOddsMatchBean.SnookerMatchOddsDetailsBean.SnookerMatchOddsDataBean eurOdds = mOdds.getOnlyWin().getSBO();
                            viewHolderList.mSnookerOddsLeft.setText(eurOdds.getLeftOdds());
                            viewHolderList.mSnookerOddsMiddle.setText(mContext.getString(R.string.snooker_state_victory_defeat));
                            viewHolderList.mSnookerOddsRight.setText(eurOdds.getRightOdds());
                        }
                    }
                    /**
                     * 大小盘 -- 浩博
                     */
                    if (asize) {
                        if (mOdds.getAsiaSize() == null) {
                            setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                        }else if(mOdds.getAsiaSize().getSBO() == null){
                            setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                        }else{
                            SnookerOddsMatchBean.SnookerMatchOddsDetailsBean.SnookerMatchOddsDataBean asizeOdds = mOdds.getAsiaSize().getSBO();
                            viewHolderList.mSnookerOddsLeft.setText(asizeOdds.getLeftOdds());
                            viewHolderList.mSnookerOddsMiddle.setText(mContext.getString(R.string.snooker_state_zong) + asizeOdds.getHandicapValue());
                            viewHolderList.mSnookerOddsRight.setText(asizeOdds.getRightOdds());
                        }
                    }
                    /**
                     * 单双 -- 浩博--> 利记
                     */
                    if (singleTwins) {
                        if (mOdds.getOneTwo() == null) {
                            setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                        }else if(mOdds.getOneTwo().getSBO() == null){
                            if (mOdds.getOneTwo().getVinBet() == null) {
                                setOddsNull(viewHolderList.mSnookerOddsLeft , viewHolderList.mSnookerOddsMiddle , viewHolderList.mSnookerOddsRight);
                            }else{
                                SnookerOddsMatchBean.SnookerMatchOddsDetailsBean.SnookerMatchOddsDataBean onetwoLj = mOdds.getOneTwo().getVinBet();
                                viewHolderList.mSnookerOddsLeft.setText(onetwoLj.getLeftOdds());
                                viewHolderList.mSnookerOddsMiddle.setText(onetwoLj.getHandicapValue().equals("0.0")?mContext.getString(R.string.snooker_state_single_double):onetwoLj.getHandicapValue());
                                viewHolderList.mSnookerOddsRight.setText(onetwoLj.getRightOdds());
                                //aletOdds.getHandicapValue().equals("0.0")?mContext.getString(R.string.number_bjsc_ds):
                            }
                        }else{
                            SnookerOddsMatchBean.SnookerMatchOddsDetailsBean.SnookerMatchOddsDataBean singleTwinsOdds = mOdds.getOneTwo().getSBO();
                            viewHolderList.mSnookerOddsLeft.setText(singleTwinsOdds.getLeftOdds());
                            viewHolderList.mSnookerOddsMiddle.setText(singleTwinsOdds.getHandicapValue().equals("0.0")?mContext.getString(R.string.snooker_state_single_double):singleTwinsOdds.getHandicapValue());
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

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SnookerEventsBean snookerMatchesBean) {
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

    class ViewHolderDate extends BaseViewHolder{
        TextView mSnookerDate;
        TextView mSnookerWeek;

        public ViewHolderDate(View itemView) {
            super(itemView);
            mSnookerDate = (TextView) itemView.findViewById(R.id.snooker_date);
            mSnookerWeek = (TextView) itemView.findViewById(R.id.snooker_week);
        }
    }

    /**
     * 退赛提示（退赛不显示赔率盘口）
     * @param holderlist
     * @param isRET 是否退赛
     */
    private void setRETstatus(ViewHolderList holderlist , boolean isRET){

        if (isRET){
            holderlist.mPlaceholderitemView.setVisibility(View.GONE);
            holderlist.mOddsShow.setVisibility(View.GONE);
            holderlist.mHomeRET.setVisibility(View.VISIBLE);
            holderlist.mGuestRET.setVisibility(View.VISIBLE);
        }else{
            holderlist.mPlaceholderitemView.setVisibility(View.VISIBLE);
            holderlist.mOddsShow.setVisibility(View.VISIBLE);
            holderlist.mHomeRET.setVisibility(View.GONE);
            holderlist.mGuestRET.setVisibility(View.GONE);
        }

    }
    class ViewHolderList extends BaseViewHolder{
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
        private final TextView mPoscore;
        private final TextView mPtScore;
        private final View mPlaceholderitemView;
        private final LinearLayout mOddsShow;
        private final TextView mHomeRET;
        private final TextView mGuestRET;

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
            mPoscore = (TextView)itemView.findViewById(R.id.snooker_poScore);
            mPtScore = (TextView)itemView.findViewById(R.id.snooker_ptScore);

            //新增状态
            mPlaceholderitemView =  itemView.findViewById(R.id.placeholder_view);
            mOddsShow = (LinearLayout) itemView.findViewById(R.id.odds_show);
            mHomeRET = (TextView) itemView.findViewById(R.id.home_ret);
            mGuestRET = (TextView)  itemView.findViewById(R.id.guest_ret);
        }
    }

    class ViewHolderTitle extends BaseViewHolder{
        TextView mSnookerTitle;
        public ViewHolderTitle(View itemView) {
            super(itemView);
            mSnookerTitle = (TextView) itemView.findViewById(R.id.snooker_title);
        }
    }

}
