package com.hhly.mlottery.frame.footballframe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballAnalyzeDetailsActivity;
import com.hhly.mlottery.activity.FootballDatabaseDetailsActivity;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.football.AnalyzeAsiaAdapter;
import com.hhly.mlottery.bean.footballDetails.NewAnalyzeBean;
import com.hhly.mlottery.bean.footballDetails.database.DataBaseBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.RoundProgressBar;
import com.hhly.mlottery.widget.LineChartView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 足球分析界面
 * Created by madongyun 155
 * date :2016-06-13
 */
public class AnalyzeFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private View mView;
    private Context mContext;// 上下文对象
    private String mHomeName;
    private String mGuestName;


    /**
     * 历史交锋
     */
    private ProgressBar mProgressBar;
    private TextView mProgressHomeWin;
    private TextView mProgressGuestWin;
    private TextView mProgressDraw;
    /**
     * 近期战绩
     */
    private ImageView mHomeRecent1;
    private ImageView mHomeRecent2;
    private ImageView mHomeRecent3;
    private ImageView mHomeRecent4;
    private ImageView mHomeRecent5;
    private ImageView mHomeRecent6;

    private ImageView mGuestRecent1;
    private ImageView mGuestRecent2;
    private ImageView mGuestRecent3;
    private ImageView mGuestRecent4;
    private ImageView mGuestRecent5;
    private ImageView mGuestRecent6;
    //未来比赛
    private LinearLayout mLinearFutureMatch;
    private TextView mFutureNodata;
    /**
     * 主队未来比赛距离天数
     */
    private TextView mHomeFutureDate;
    private TextView mHomeFutureName;
    private ImageView mHomeFutureLogo;

    private TextView mGuestFutureDate;
    private TextView mGuestFutureName;
    private ImageView mGuestFutureLogo;
    /**
     * 更多战绩
     */
    private TextView mTextMoreGame;

    //积分排名
    private LinearLayout mLinearRanking;
    private TextView mRankingNodata;
    private TextView mHomeRank;
    private TextView mHomeHasGame;
    private TextView mHomeWinOrLose;
    private TextView mHomeGoalOrLose;
    private TextView mHomeGoalDifference;
    private TextView mHomeIntegral;//积分

    private TextView mGuestRank;
    private TextView mGuestHasGame;
    private TextView mGuestWinOrLose;
    private TextView mGuestGoalOrLose;
    private TextView mGuestGoalDifference;
    private TextView mGuestIntegral;//积分
    //完整积分榜
    private TextView mIntegralTable;

    //攻防对比
    private LinearLayout mLinearAttack;
    private TextView mAttackNodata;
    private TextView mHomeGoal;
    private TextView mHomeLose;
    private TextView mGuestGoal;
    private TextView mGuestLose;
    private TextView mSizeOfBet;
    //球员信息
    private TextView mHomeTeamName;
    private TextView mGuestTeamName;
//    private LinearLayout ll_rosters_homeTeam;// 主队名单容器
//    private LinearLayout ll_rosters_visitingTeam;// 客队名单容器
//    private FrameLayout fl_firsPlayers_not;// 暂无首发容器
//    private LinearLayout fl_firsPlayers_content;// 首发内容容器
    //心水推荐
    private TextView mRecommend;
    private TextView mRecommendNoData;
    private RelativeLayout mLiRecommend;
    //亚盘走势
    private LinearLayout mllLet;
    private TextView mTextLet1;
    private TextView mTextLet2;
    private TextView mTextLet3;
    private ListView mLetListView;
    private LinearLayout mLinearLetHistory; //亚盘的历史交锋布局
    private LinearLayout mLinearLetRecent; //近期对比
    private RadioGroup mLetRg;
    private TextView mLetMore;
    private LineChartView mChartLetHistory;
    private LineChartView mChartLetHome;
    private LineChartView mChartLetGuest;
    private TextView mLetHomeTeam;
    private TextView mLetGuestTeam;
    private TextView mLetNodata1;
    private TextView mLetNodata2;
    private TextView mLetNodata3;
    private TextView mLetNodata4;
    private TextView mLetAllNodata;
    private RoundProgressBar mLetHistoryProgressBar;
    private TextView mLetHistoryVsCount;
    private TextView mLetHistoryHomeWin;
    private TextView mLetHistoryGuestWin;
    private TextView mLetHistoryDraw;
    private RoundProgressBar mLetRecentHomeProgress;
    private RoundProgressBar mLetRecentGuestProgress;
    private TextView mLetRecentHomeWinRate;
    private TextView mLetRecentGuestWinRate;
    private TextView mLetRecentHomeWinText;// 皇马赢盘率
    private TextView mLetRecentGuestWinText;
    //比分红色为主队
    TextView mLetDescription;


    //大小球走势
    private LinearLayout mllSize;
    private TextView mTextSize1;
    private TextView mTextSize2;
    private TextView mTextSize3;
    private ListView mSizeListView;
    private LinearLayout mLinearSizeHistory;
    private LinearLayout mLinearSizeRecent;
    private RadioGroup mSizeRg;
    private TextView mSizeMore;
    private LineChartView mChartSizeHistory;
    private LineChartView mChartSizeHome;
    private LineChartView mChartSizeGuest;
    private TextView mSizeHomeTeam;
    private TextView mSizeGuestTeam;
    private TextView mSizeNodata1;
    private TextView mSizeNodata2;
    private TextView mSizeNodata3;
    private TextView mSizeNodata4;
    private TextView mSizeAllNodata;
    private RoundProgressBar mSizeHistoryProgressBar;
    private TextView mSizeHistoryVsCount;
    private TextView mSizeHistoryBigRate;
    private TextView mSizeHistorySmallRate;
    private TextView mSizeHistoryDraw;
    private RoundProgressBar mSizeRecentHomeProgressBar;
    private TextView mSizeRecentHomeBigRate;
    private TextView mSizeRecentHomeSmallRate;
    private TextView mSizeRecentHomeDraw;
    private TextView mSizeRecentHomeVsCount;
    private RoundProgressBar mSizeRecentGuestProgressBar;
    private TextView mSizeRecentGuestBigRate;
    private TextView mSizeRecentGuestSmallRate;
    private TextView mSizeRecentGuestDraw;
    private TextView mSizeRecentGuestVsCount;
    TextView mSizeDescription;


    /**
     * 亚盘里的listView
     */
    private AnalyzeAsiaAdapter mLetAdapter;
    private AnalyzeAsiaAdapter mSizeAdapter;

    private NewAnalyzeBean mAnalyzeBean;

//    private boolean isLoading = false;// 是否已加载过数据

    public AnalyzeFragment() {
        // Required empty public constructor
    }


    public static AnalyzeFragment newInstance() {
        return new AnalyzeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_analyze_fragment, container, false);
        mAnalyzeBean = new NewAnalyzeBean();
        initView();
        initData();
        setListener();
        return mView;
    }

    private void initView() {
        mProgressBar = (ProgressBar) mView.findViewById(R.id.football_analyze__progressbar);
        mProgressBar.setSecondaryProgress(50);//默认 显示一半绿色
        mProgressHomeWin = (TextView) mView.findViewById(R.id.football_progressbar_home);
        mProgressGuestWin = (TextView) mView.findViewById(R.id.football_progressbar_guest);
        mProgressDraw = (TextView) mView.findViewById(R.id.football_progressbar_draw);
        //近期比赛
        mHomeRecent1 = (ImageView) mView.findViewById(R.id.football_img_recent_home1);
        mHomeRecent2 = (ImageView) mView.findViewById(R.id.football_img_recent_home2);
        mHomeRecent3 = (ImageView) mView.findViewById(R.id.football_img_recent_home3);
        mHomeRecent4 = (ImageView) mView.findViewById(R.id.football_img_recent_home4);
        mHomeRecent5 = (ImageView) mView.findViewById(R.id.football_img_recent_home5);
        mHomeRecent6 = (ImageView) mView.findViewById(R.id.football_img_recent_home6);

        mGuestRecent1 = (ImageView) mView.findViewById(R.id.football_img_recent_guest1);
        mGuestRecent2 = (ImageView) mView.findViewById(R.id.football_img_recent_guest2);
        mGuestRecent3 = (ImageView) mView.findViewById(R.id.football_img_recent_guest3);
        mGuestRecent4 = (ImageView) mView.findViewById(R.id.football_img_recent_guest4);
        mGuestRecent5 = (ImageView) mView.findViewById(R.id.football_img_recent_guest5);
        mGuestRecent6 = (ImageView) mView.findViewById(R.id.football_img_recent_guest6);
        //未来比赛
        mLinearFutureMatch = (LinearLayout) mView.findViewById(R.id.ll_football_analyze_future);
        mFutureNodata = (TextView) mView.findViewById(R.id.football_analyze_nodata1);
        mHomeFutureDate = (TextView) mView.findViewById(R.id.football_home_future_date);
        mHomeFutureName = (TextView) mView.findViewById(R.id.football_home_future_name);
        mHomeFutureLogo = (ImageView) mView.findViewById(R.id.football_home_future_logo);
        mGuestFutureDate = (TextView) mView.findViewById(R.id.football_guest_future_date);
        mGuestFutureName = (TextView) mView.findViewById(R.id.football_guest_future_name);
        mGuestFutureLogo = (ImageView) mView.findViewById(R.id.football_guest_future_logo);
        //更多战绩
        mTextMoreGame = (TextView) mView.findViewById(R.id.football_analyze_more_record);
        //积分榜
        mLinearRanking = (LinearLayout) mView.findViewById(R.id.ll_football_analyze_ranking);
        mRankingNodata = (TextView) mView.findViewById(R.id.football_analyze_nodata2);
        mHomeRank = (TextView) mView.findViewById(R.id.football_analyze__home_rank);
        mHomeHasGame = (TextView) mView.findViewById(R.id.football_analyze_home_hadgame);
        mHomeWinOrLose = (TextView) mView.findViewById(R.id.football_analyze_home_win_or_lose);
        mHomeGoalOrLose = (TextView) mView.findViewById(R.id.football_analyze_home_goal_or_lose);
        mHomeGoalDifference = (TextView) mView.findViewById(R.id.football_analyze_home_goal_difference);
        mHomeIntegral = (TextView) mView.findViewById(R.id.football_analyze_home_integral);

        mGuestRank = (TextView) mView.findViewById(R.id.football_analyze__guest_rank);
        mGuestHasGame = (TextView) mView.findViewById(R.id.football_analyze_guest_hadgame);
        mGuestWinOrLose = (TextView) mView.findViewById(R.id.football_analyze_guest_win_or_lose);
        mGuestGoalOrLose = (TextView) mView.findViewById(R.id.football_analyze_guest_goal_or_lose);
        mGuestGoalDifference = (TextView) mView.findViewById(R.id.football_analyze_guest_goal_difference);
        mGuestIntegral = (TextView) mView.findViewById(R.id.football_analyze_guest_integral);
        //完整积分榜
        mIntegralTable = (TextView) mView.findViewById(R.id.football_analyze_integral_table);
        //攻防对比
        mLinearAttack = (LinearLayout) mView.findViewById(R.id.ll_football_analyze_attack);
        mAttackNodata = (TextView) mView.findViewById(R.id.football_analyze_nodata3);
        mHomeGoal = (TextView) mView.findViewById(R.id.home_goal);
        mHomeLose = (TextView) mView.findViewById(R.id.home_lose);
        mGuestGoal = (TextView) mView.findViewById(R.id.guest_goal);
        mGuestLose = (TextView) mView.findViewById(R.id.guest_lose);
        mSizeOfBet = (TextView) mView.findViewById(R.id.football_analyze_size_of_bet);
        //球员信息
        mHomeTeamName = (TextView) mView.findViewById(R.id.lineup_home_team);
        mGuestTeamName = (TextView) mView.findViewById(R.id.lineup_guest_team);
//        mListView= (NestedListView) mView.findViewById(R.id.listview_player_message);

        //心水推荐
        mRecommend = (TextView) mView.findViewById(R.id.tv_analyze_recommend);
        mRecommendNoData = (TextView) mView.findViewById(R.id.football_recommend_nodata);
        mLiRecommend = (RelativeLayout) mView.findViewById(R.id.ll_recommend);

        //亚盘走势
        mllLet = (LinearLayout) mView.findViewById(R.id.ll_analyze_let);
        mTextLet1 = (TextView) mView.findViewById(R.id.tv_analyze_let1);
        mTextLet2 = (TextView) mView.findViewById(R.id.tv_analyze_let2);
        mTextLet3 = (TextView) mView.findViewById(R.id.tv_analyze_let3);
        mLetListView = (ListView) mView.findViewById(R.id.lv_analyze_let);
        mLetListView.setFocusable(false);
        mLetListView.setDivider(getActivity().getResources().getDrawable(R.color.mdy_999));
        mLetListView.setDividerHeight(1);
        mLinearLetHistory = (LinearLayout) mView.findViewById(R.id.ll_analyze_let_history);
        mLinearLetRecent = (LinearLayout) mView.findViewById(R.id.ll_analyze_let_recent);
        mLetRg = (RadioGroup) mView.findViewById(R.id.radio_group_let);
        mLetMore = (TextView) mView.findViewById(R.id.tv_analyze_more_message1);
        mChartLetHistory = (LineChartView) mView.findViewById(R.id.chart_let_history);
        mChartLetHome = (LineChartView) mView.findViewById(R.id.chart_let_home);
        mChartLetGuest = (LineChartView) mView.findViewById(R.id.chart_let_guest);
        mLetHomeTeam = (TextView) mView.findViewById(R.id.let_home_team);
        mLetGuestTeam = (TextView) mView.findViewById(R.id.let_guest_team);
        mLetNodata1 = (TextView) mView.findViewById(R.id.let_nodata1);
        mLetNodata2 = (TextView) mView.findViewById(R.id.let_nodata2);
        mLetNodata3 = (TextView) mView.findViewById(R.id.let_nodata3);
        mLetNodata4 = (TextView) mView.findViewById(R.id.let_nodata4);
        mLetAllNodata = (TextView) mView.findViewById(R.id.let_all_nodata);
        mLetHistoryProgressBar = (RoundProgressBar) mView.findViewById(R.id.analyze_let_history_progress);
        mLetHistoryVsCount = (TextView) mView.findViewById(R.id.analyze_let_history_vs_count);
        mLetHistoryHomeWin = (TextView) mView.findViewById(R.id.analyze_let_history_home_win_percent);
        mLetHistoryGuestWin = (TextView) mView.findViewById(R.id.analyze_let_history_guest_win_percent);
        mLetHistoryDraw = (TextView) mView.findViewById(R.id.analyze_let_history_draw_percent);
        mLetRecentHomeProgress = (RoundProgressBar) mView.findViewById(R.id.analyze_let_recent_home_progress);
        mLetRecentHomeWinRate = (TextView) mView.findViewById(R.id.analyze_let_recent_home_vs_count);
        mLetRecentHomeWinText = (TextView) mView.findViewById(R.id.analyze_let_recent_home_win_text);
        mLetRecentGuestProgress = (RoundProgressBar) mView.findViewById(R.id.analyze_let_recent_guest_progress);
        mLetRecentGuestWinRate = (TextView) mView.findViewById(R.id.analyze_let_recent_guest_vs_count);
        mLetRecentGuestWinText = (TextView) mView.findViewById(R.id.analyze_let_recent_guest_win_text);
        mLetDescription = (TextView) mView.findViewById(R.id.analyze_asia_description);

        //大小球走势
        mllSize = (LinearLayout) mView.findViewById(R.id.ll_analyze_size);
        mTextSize1 = (TextView) mView.findViewById(R.id.tv_analyze_size1);
        mTextSize2 = (TextView) mView.findViewById(R.id.tv_analyze_size2);
        mTextSize3 = (TextView) mView.findViewById(R.id.tv_analyze_size3);
        mSizeListView = (ListView) mView.findViewById(R.id.lv_analyze_size);
        mSizeListView.setFocusable(false);
        mSizeListView.setDivider(getActivity().getResources().getDrawable(R.color.mdy_999));
        mSizeListView.setDividerHeight(1);
        mLinearSizeHistory = (LinearLayout) mView.findViewById(R.id.ll_analyze_size_history);
        mLinearSizeRecent = (LinearLayout) mView.findViewById(R.id.ll_analyze_size_recent);
        mSizeRg = (RadioGroup) mView.findViewById(R.id.radio_group_size);
        mSizeMore = (TextView) mView.findViewById(R.id.tv_analyze_more_message2);
        mChartSizeHistory = (LineChartView) mView.findViewById(R.id.chart_size_history);
        mChartSizeHome = (LineChartView) mView.findViewById(R.id.chart_size_home);
        mChartSizeGuest = (LineChartView) mView.findViewById(R.id.chart_size_guest);
        mSizeHomeTeam = (TextView) mView.findViewById(R.id.size_home_team);
        mSizeGuestTeam = (TextView) mView.findViewById(R.id.size_guest_team);
        mSizeNodata1 = (TextView) mView.findViewById(R.id.size_nodata1);
        mSizeNodata2 = (TextView) mView.findViewById(R.id.size_nodata2);
        mSizeNodata3 = (TextView) mView.findViewById(R.id.size_nodata3);
        mSizeNodata4 = (TextView) mView.findViewById(R.id.size_nodata4);
        mSizeAllNodata = (TextView) mView.findViewById(R.id.size_all_nodata);
        mSizeHistoryProgressBar = (RoundProgressBar) mView.findViewById(R.id.analyze_size_history_progress);
        mSizeHistoryBigRate = (TextView) mView.findViewById(R.id.analyze_size_history_big_ball_rate);
        mSizeHistorySmallRate = (TextView) mView.findViewById(R.id.analyze_size_history_small_ball_rate);
        mSizeHistoryDraw = (TextView) mView.findViewById(R.id.analyze_size_history_draw);
        mSizeHistoryVsCount = (TextView) mView.findViewById(R.id.analyze_size_history_vs_count);
        //recent
        mSizeRecentHomeProgressBar = (RoundProgressBar) mView.findViewById(R.id.analyze_size_recent_home_progress);
        mSizeRecentHomeBigRate = (TextView) mView.findViewById(R.id.analyze_size_recent_home_big_ball);
        mSizeRecentHomeSmallRate = (TextView) mView.findViewById(R.id.analyze_size_recent_home_small_ball);
        mSizeRecentHomeDraw = (TextView) mView.findViewById(R.id.analyze_size_recent_home_draw_percent);
        mSizeRecentHomeVsCount = (TextView) mView.findViewById(R.id.analyze_size_recent_home_vs_count);
        mSizeRecentGuestProgressBar = (RoundProgressBar) mView.findViewById(R.id.analyze_size_recent_guest_progress);
        mSizeRecentGuestBigRate = (TextView) mView.findViewById(R.id.analyze_size_recent_guest_big_ball);
        mSizeRecentGuestSmallRate = (TextView) mView.findViewById(R.id.analyze_size_recent_guest_small_ball);
        mSizeRecentGuestDraw = (TextView) mView.findViewById(R.id.analyze_size_recent_guest_draw_percent);
        mSizeRecentGuestVsCount = (TextView) mView.findViewById(R.id.analyze_size_recent_guest_vs_count);
        mSizeDescription = (TextView) mView.findViewById(R.id.analyze_size_description);

        mLetHistoryProgressBar.setTextIsDisplayable(false);
        mLetRecentHomeProgress.setTextIsDisplayable(false);
        mLetRecentGuestProgress.setTextIsDisplayable(false);
        mSizeHistoryProgressBar.setTextIsDisplayable(false);
        mSizeRecentGuestProgressBar.setTextIsDisplayable(false);
        mSizeRecentHomeProgressBar.setTextIsDisplayable(false);

        mLetHistoryProgressBar.setIsprogress(true);
        mLetRecentHomeProgress.setIsprogress(true);
        mLetRecentGuestProgress.setIsprogress(true);
        mSizeHistoryProgressBar.setIsprogress(true);
        mSizeRecentGuestProgressBar.setIsprogress(true);
        mSizeRecentHomeProgressBar.setIsprogress(true);

    }

    public void initData() {
        Map<String, String> params = new HashMap<>();
        params.put("thirdId", getActivity() != null ? ((FootballMatchDetailActivity)getActivity()).mThirdId : null);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_NEW_ANALYZE, params, new VolleyContentFast.ResponseSuccessListener<NewAnalyzeBean>() {
            @Override
            public void onResponse(NewAnalyzeBean analyzeBean) {
                if (analyzeBean.getResult().equals("200")) {
                    mAnalyzeBean = analyzeBean;
                    loadData(mAnalyzeBean);
                } else {
                    mLinearRanking.setVisibility(View.GONE);
                    mRankingNodata.setVisibility(View.VISIBLE);
                    mLinearAttack.setVisibility(View.GONE);
                    mAttackNodata.setVisibility(View.VISIBLE);
                    mllLet.setVisibility(View.GONE);
                    mllSize.setVisibility(View.GONE);
                    mLetAllNodata.setVisibility(View.VISIBLE);
                    mllSize.setVisibility(View.GONE);
                    mSizeAllNodata.setVisibility(View.VISIBLE);
                    mLiRecommend.setVisibility(View.GONE);
                    mRecommendNoData.setVisibility(View.VISIBLE);
                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mLinearRanking.setVisibility(View.GONE);
                mRankingNodata.setVisibility(View.VISIBLE);
                mLinearAttack.setVisibility(View.GONE);
                mAttackNodata.setVisibility(View.VISIBLE);
                mllLet.setVisibility(View.GONE);
                mllSize.setVerticalGravity(View.GONE);
                mLetAllNodata.setVisibility(View.VISIBLE);
                mllSize.setVisibility(View.GONE);
                mSizeAllNodata.setVisibility(View.VISIBLE);
                mLiRecommend.setVisibility(View.GONE);
                mRecommendNoData.setVisibility(View.VISIBLE);
            }
        }, NewAnalyzeBean.class);

    }

    /**
     * 设置监听事件
     */
    private void setListener() {
        mTextMoreGame.setOnClickListener(this);
        mIntegralTable.setOnClickListener(this);
        mLetMore.setOnClickListener(this);
        mSizeMore.setOnClickListener(this);
        mllLet.setOnClickListener(this);
        mllSize.setOnClickListener(this);

        mLetRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.let_rb_history:
                        mLinearLetHistory.setVisibility(View.VISIBLE);
                        mLinearLetRecent.setVisibility(View.GONE);
                        mLetDescription.setVisibility(View.VISIBLE);
                        //亞盤
                        if (mAnalyzeBean.getAsiaTrend() != null && mAnalyzeBean.getAsiaTrend().getBattleHistory() != null && mAnalyzeBean.getAsiaTrend().getBattleHistory().getStatistics() != null) {
                            setLetHistoryText(mAnalyzeBean.getAsiaTrend().getBattleHistory().getStatistics());
                        } else {
                            mTextLet1.setText("");
                            mTextLet2.setText("");
                            mTextLet3.setText("");
                        }

                        break;
                    case R.id.let_rb_recent:
                        mLinearLetRecent.setVisibility(View.VISIBLE);
                        mLetDescription.setVisibility(View.INVISIBLE);
                        mLinearLetHistory.setVisibility(View.GONE);
                        //亞盤
                        if (mAnalyzeBean.getAsiaTrend() != null && mAnalyzeBean.getAsiaTrend().getHomeRecent() != null && mAnalyzeBean.getAsiaTrend().getHomeRecent().getStatistics() != null) {
                            setLetRecentHomeText(mAnalyzeBean.getAsiaTrend().getHomeRecent().getStatistics());
                        } else {
                            mTextLet1.setText("");
                            mTextLet2.setText("");
                        }
                        if (mAnalyzeBean.getAsiaTrend() != null && mAnalyzeBean.getAsiaTrend().getGuestRecent() != null && mAnalyzeBean.getAsiaTrend().getGuestRecent().getStatistics() != null) {
                            setLetRecentGuestText(mAnalyzeBean.getAsiaTrend().getGuestRecent().getStatistics());
                        } else {
                            mTextLet1.setText("");
                            mTextLet3.setText("");
                        }


                        break;
                }
            }
        });

        mSizeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.size_rb_history:
                        mLinearSizeHistory.setVisibility(View.VISIBLE);
                        mLinearSizeRecent.setVisibility(View.GONE);
                        mSizeDescription.setVisibility(View.VISIBLE);
                        //大小球
                        if (mAnalyzeBean.getSizeTrend() != null && mAnalyzeBean.getSizeTrend().getBattleHistory() != null && mAnalyzeBean.getSizeTrend().getBattleHistory().getStatistics() != null) {
                            setSizeHistoryText(mAnalyzeBean.getSizeTrend().getBattleHistory().getStatistics());
                        } else {
                            mTextSize1.setText("");
                            mTextSize2.setText("");
                            mTextSize3.setText("");
                        }
                        break;
                    case R.id.size_rb_recent:
                        mLinearSizeRecent.setVisibility(View.VISIBLE);
                        mLinearSizeHistory.setVisibility(View.GONE);
                        mSizeDescription.setVisibility(View.INVISIBLE);
                        //大小球
                        if (mAnalyzeBean.getSizeTrend() != null && mAnalyzeBean.getSizeTrend().getHomeRecent() != null && mAnalyzeBean.getSizeTrend().getHomeRecent().getStatistics() != null) {
                            setSizeRecentHomeText(mAnalyzeBean.getSizeTrend().getHomeRecent().getStatistics());
                        } else {
                            mTextSize1.setText("");
                            mTextSize2.setText("");
                        }
                        if (mAnalyzeBean.getSizeTrend() != null && mAnalyzeBean.getSizeTrend().getGuestRecent() != null && mAnalyzeBean.getSizeTrend().getGuestRecent().getStatistics() != null) {
                            setSizeRecentGuestText(mAnalyzeBean.getSizeTrend().getGuestRecent().getStatistics());
                        } else {
                            mTextSize1.setText("");
                            mTextSize3.setText("");
                        }
                        break;
                }
            }
        });
    }


    /**
     * 加载数据
     */
    private void loadData(NewAnalyzeBean analyzeBean) {

        mHomeName = analyzeBean.getHomeTeam() == null ? "" : analyzeBean.getHomeTeam();
        mGuestName = analyzeBean.getGuestTeam() == null ? "" : analyzeBean.getGuestTeam();
        mHomeTeamName.setText(mHomeName);
        mGuestTeamName.setText(mGuestName);
        mLetHomeTeam.setText(mHomeName);
        mSizeHomeTeam.setText(mHomeName);
        mLetGuestTeam.setText(mGuestName);
        mSizeGuestTeam.setText(mGuestName);

        if (getActivity() != null) {
            if (mAnalyzeBean.getAsiaTrend() != null && mAnalyzeBean.getAsiaTrend().getHomeRecent() != null && mAnalyzeBean.getAsiaTrend().getHomeRecent().getStatistics() != null) {
                setLetRecentHomeText(mAnalyzeBean.getAsiaTrend().getHomeRecent().getStatistics());
            }
            if (mAnalyzeBean.getAsiaTrend() != null && mAnalyzeBean.getAsiaTrend().getGuestRecent() != null && mAnalyzeBean.getAsiaTrend().getGuestRecent().getStatistics() != null) {
                setLetRecentGuestText(mAnalyzeBean.getAsiaTrend().getGuestRecent().getStatistics());
            }
            if (mAnalyzeBean.getSizeTrend() != null && mAnalyzeBean.getSizeTrend().getHomeRecent() != null && mAnalyzeBean.getSizeTrend().getHomeRecent().getStatistics() != null) {
                setSizeRecentHomeText(mAnalyzeBean.getSizeTrend().getHomeRecent().getStatistics());
            }
            if (mAnalyzeBean.getSizeTrend() != null && mAnalyzeBean.getSizeTrend().getGuestRecent() != null && mAnalyzeBean.getSizeTrend().getGuestRecent().getStatistics() != null) {
                setSizeRecentGuestText(mAnalyzeBean.getSizeTrend().getGuestRecent().getStatistics());
            }
        }

        int progress;
        int secondaryProgress;
        if (analyzeBean.getBothRecord() != null && analyzeBean.getBothRecord().getHome() != null && getActivity() != null) {

            int homeWin = analyzeBean.getBothRecord().getHome().getHistoryWin();
            int guestWin = analyzeBean.getBothRecord().getGuest().getHistoryWin();
            int draw = analyzeBean.getBothRecord().getHome().getHistoryDraw();

            //无 “平”情况
            if (draw == 0) {
                if (homeWin == 0 && guestWin == 0) {
                    progress = 50;
                } else if (homeWin == 0) {
                    progress = 0;
                } else if (guestWin == 0) {
                    progress = 100;
                } else {
                    progress = homeWin * 100 / (guestWin + homeWin);
                }
                mProgressBar.setProgress(progress);
                mProgressBar.setSecondaryProgress(progress);
            } else {
                // 有打平时显示二次进度。。
                progress = (homeWin + draw) * 100 / (guestWin + homeWin + draw);//主胜+平  得到的是主胜和平的和值
                secondaryProgress = homeWin * 100 / (guestWin + homeWin + draw);// 二次覆盖前次身下平的概率
                mProgressBar.setProgress(progress);
                mProgressBar.setSecondaryProgress(secondaryProgress);
            }

            if (getActivity() != null) {
                mProgressHomeWin.setText(homeWin + getActivity().getResources().getString(R.string.analyze_win));
                mProgressGuestWin.setText(guestWin + getActivity().getResources().getString(R.string.analyze_win));
                mProgressDraw.setText(draw + getActivity().getResources().getString(R.string.analyze_equ));
            }
        }

        //近期战绩
        if (analyzeBean.getBothRecord() != null && analyzeBean.getBothRecord().getHome() != null) {
            List<Integer> homeRecent = analyzeBean.getBothRecord().getHome().getRecentRecord();
            if (homeRecent.size() != 0) {
                setRecent(mHomeRecent1, homeRecent.get(0));
                setRecent(mHomeRecent2, homeRecent.get(1));
                setRecent(mHomeRecent3, homeRecent.get(2));
                setRecent(mHomeRecent4, homeRecent.get(3));
                setRecent(mHomeRecent5, homeRecent.get(4));
                setRecent(mHomeRecent6, homeRecent.get(5));
            }
        }
        if (analyzeBean.getBothRecord() != null && analyzeBean.getBothRecord().getGuest() != null) {
            List<Integer> guestRecent = analyzeBean.getBothRecord().getGuest().getRecentRecord();

            if (guestRecent.size() != 0) {
                setRecent(mGuestRecent1, guestRecent.get(0));
                setRecent(mGuestRecent2, guestRecent.get(1));
                setRecent(mGuestRecent3, guestRecent.get(2));
                setRecent(mGuestRecent4, guestRecent.get(3));
                setRecent(mGuestRecent5, guestRecent.get(4));
                setRecent(mGuestRecent6, guestRecent.get(5));
            }
        }

        //未来三场
        if (getActivity() != null && analyzeBean.getBothRecord() != null && analyzeBean.getBothRecord().getHome() != null && analyzeBean.getBothRecord().getHome().getFutureMatch() != null && getActivity() != null) {
            mHomeFutureDate.setText(analyzeBean.getBothRecord().getHome().getFutureMatch().getDiffDays() + getActivity().getResources().getString(R.string.number_hk_dd));
            mHomeFutureName.setText(analyzeBean.getBothRecord().getHome().getFutureMatch().getTeam());
            ImageLoader.load(getActivity(), analyzeBean.getBothRecord().getHome().getFutureMatch().getLogoUrl(), R.mipmap.football_analyze_default).into(mHomeFutureLogo);

        }
        if (getActivity() != null && analyzeBean.getBothRecord() != null && analyzeBean.getBothRecord().getGuest() != null && analyzeBean.getBothRecord().getGuest().getFutureMatch() != null && getActivity() != null) {
            mGuestFutureDate.setText(analyzeBean.getBothRecord().getGuest().getFutureMatch().getDiffDays() + getActivity().getResources().getString(R.string.number_hk_dd));
            mGuestFutureName.setText(analyzeBean.getBothRecord().getGuest().getFutureMatch().getTeam());
            ImageLoader.load(getActivity(), analyzeBean.getBothRecord().getGuest().getFutureMatch().getLogoUrl(), R.mipmap.football_analyze_default).into(mGuestFutureLogo);

        }

        //积分排名
        if (analyzeBean.getScoreRank().getHome() == null && analyzeBean.getScoreRank().getGuest() == null) {
            mLinearRanking.setVisibility(View.GONE);
            mRankingNodata.setVisibility(View.VISIBLE);
        } else {
            mLinearRanking.setVisibility(View.VISIBLE);
            mRankingNodata.setVisibility(View.GONE);
            NewAnalyzeBean.ScoreRankEntity entity = analyzeBean.getScoreRank();
            if (entity.getHome() != null) {
                mHomeRank.setText(entity.getHome().getRank() + entity.getHome().getTeam());
                mHomeHasGame.setText(entity.getHome().getVsCount() + "");
                mHomeWinOrLose.setText(entity.getHome().getWin() + "/" + entity.getHome().getDraw() + "/" + entity.getHome().getLose());
                mHomeGoalOrLose.setText(entity.getHome().getGoal() + "/" + entity.getHome().getMiss());
                mHomeGoalDifference.setText(entity.getHome().getGoalDiff() + "");
                mHomeIntegral.setText(entity.getHome().getIntegral() + "");

            }
            if (entity.getGuest() != null) {
                mGuestRank.setText(entity.getGuest().getRank() + entity.getGuest().getTeam());
                mGuestHasGame.setText(entity.getGuest().getVsCount() + "");
                mGuestWinOrLose.setText(entity.getGuest().getWin() + "/" + entity.getGuest().getDraw() + "/" + entity.getGuest().getLose());
                mGuestGoalOrLose.setText(entity.getGuest().getGoal() + "/" + entity.getGuest().getMiss());
                mGuestGoalDifference.setText(entity.getGuest().getGoalDiff() + "");
                mGuestIntegral.setText(entity.getGuest().getIntegral() + "");
            }

        }
//        //球员信息
//        mHomeTeamName.setText(mHomeName);
//        mGuestTeamName.setText(mGuestName);

        //是否显示积分榜
        if (analyzeBean.getFullScoreRank() == 1) { //有完整积分榜
            mIntegralTable.setVisibility(View.VISIBLE);
        } else if (analyzeBean.getFullScoreRank() == 0) {
            mIntegralTable.setVisibility(View.GONE);
        }

        NewAnalyzeBean.AttackDefenseEntity defense = analyzeBean.getAttackDefense();
        //攻防对比
        if (defense.getGuestFieldGoal() == null && defense.getGuestFieldLose() == null && defense.getHomeFieldGoal() == null && defense.getHomeFieldLose() == null) {
            mLinearAttack.setVisibility(View.GONE);
            mAttackNodata.setVisibility(View.VISIBLE);
        } else {
            mLinearAttack.setVisibility(View.VISIBLE);
            mAttackNodata.setVisibility(View.GONE);
            mHomeGoal.setText(analyzeBean.getAttackDefense().getHomeFieldGoal());
            mHomeLose.setText(analyzeBean.getAttackDefense().getHomeFieldLose());
            mGuestGoal.setText(analyzeBean.getAttackDefense().getGuestFieldGoal());
            mGuestLose.setText(analyzeBean.getAttackDefense().getGuestFieldLose());
            mSizeOfBet.setText(analyzeBean.getAttackDefense().getSizeHandicap() == null ? "--" : analyzeBean.getAttackDefense().getSizeHandicap());
        }

//        List<NewAnalyzeBean.LineUpEntity.PlayerInfo> homeLineUpList = analyzeBean.getLineUp().getHomeLineUp();//主队队员
//        List<NewAnalyzeBean.LineUpEntity.PlayerInfo> guestLineUpList = analyzeBean.getLineUp().getGuestLineUp();//客队队员
//        List<NewAnalyzeBean.LineUpEntity.PlayerInfo> homeLineUpList=new ArrayList<>();//主队队员
//        List<NewAnalyzeBean.LineUpEntity.PlayerInfo> guestLineUpList=new ArrayList<>();//客队队员
//        for(int i=0;i<11;i++){
//            homeLineUpList.add(new NewAnalyzeBean.LineUpEntity.PlayerInfo("梅西"+i));
//        }
//        for(int i=0;i<11;i++){
//            guestLineUpList.add(new NewAnalyzeBean.LineUpEntity.PlayerInfo("C罗"+i));
//        }

//        if (getActivity() != null) {
//            mContext = getActivity();
//            if (homeLineUpList != null && guestLineUpList != null) {
//                if (homeLineUpList.size() > 0) {
//                    // 显示首发内容
//                    fl_firsPlayers_not.setVisibility(View.GONE); ///sd
//                    fl_firsPlayers_content.setVisibility(View.VISIBLE);
//
//                    int dip5 = DisplayUtil.dip2px(mContext, 5);
//                    int dip10 = DisplayUtil.dip2px(mContext, 10);
//
//                    if (ll_rosters_homeTeam.getChildCount() != 0) {  //刷新时清空上次add的textview
//                        ll_rosters_homeTeam.removeAllViews();
//                    }
//                    if (ll_rosters_visitingTeam.getChildCount() != 0) {
//                        ll_rosters_visitingTeam.removeAllViews();
//                    }
//                    // 添加主队名单
//                    for (int i = 0, len = homeLineUpList.size(); i < len; i++) {
//                        TextView tv_homeTeams = new TextView(mContext);
//                        tv_homeTeams.setText(homeLineUpList.get(i).getName());
//                        if (i == 0) {
//                            tv_homeTeams.setPadding(dip5, 0, 0, dip10);
//                        } else {
//                            tv_homeTeams.setPadding(dip5, 0, 0, dip10);
//                        }
//                        tv_homeTeams.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
//                        ll_rosters_homeTeam.addView(tv_homeTeams);
//                    }
//
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                    params.gravity = Gravity.RIGHT;// 设置靠右对齐
//                    // 添加客队名单
//                    for (int i = 0, len = guestLineUpList.size(); i < len; i++) {
//                        TextView tv_visitingTeams = new TextView(mContext);
//                        tv_visitingTeams.setText(guestLineUpList.get(i).getName());
//                        tv_visitingTeams.setLayoutParams(params);
//                        if (i == 0) {
//                            tv_visitingTeams.setPadding(0, 0, dip5, dip10);
//                        } else {
//                            tv_visitingTeams.setPadding(0, 0, dip5, dip10);
//                        }
//                        tv_visitingTeams.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
//                        ll_rosters_visitingTeam.addView(tv_visitingTeams);
//                    }
//
//                } else {
//                    // 显示暂无首发提示
//                    fl_firsPlayers_not.setVisibility(View.VISIBLE);
//                    fl_firsPlayers_content.setVisibility(View.GONE);
//                }
//
//            } else {
//                // 显示暂无首发提示
//                fl_firsPlayers_not.setVisibility(View.VISIBLE);
//                fl_firsPlayers_content.setVisibility(View.GONE);
//            }
//        }

        //心水推荐
        if (analyzeBean.getRecommend() != null && analyzeBean.getRecommend() != "") {
            mRecommend.setText(Html.fromHtml(analyzeBean.getRecommend()));
            mRecommendNoData.setVisibility(View.GONE);
        } else {
            mLiRecommend.setVisibility(View.GONE);
            mRecommendNoData.setVisibility(View.VISIBLE);
        }

        //亚盘走势
        if (analyzeBean.getAsiaTrend() != null) {

            mllLet.setVisibility(View.VISIBLE);
            mLetAllNodata.setVisibility(View.GONE);
            if (getActivity() != null && analyzeBean.getAsiaTrend().getBattleHistory() != null && analyzeBean.getAsiaTrend().getBattleHistory().getPointList() != null &&
                    analyzeBean.getAsiaTrend().getBattleHistory().getPointList().size() != 0) {
                mLetAdapter = new AnalyzeAsiaAdapter(getActivity(), analyzeBean.getAsiaTrend().getBattleHistory().getPointList(), analyzeBean);
                mLetListView.setAdapter(mLetAdapter);
                mLetNodata1.setVisibility(View.GONE);
                mLetNodata2.setVisibility(View.GONE);
            } else {
                mLetNodata1.setVisibility(View.VISIBLE);
                mLetNodata2.setVisibility(View.VISIBLE);
            }

            //亚盘历史交锋
            List<List<Integer>> asiaHistoryList = new ArrayList<>();
            if (analyzeBean.getAsiaTrend().getBattleHistory() != null && analyzeBean.getAsiaTrend().getBattleHistory().getLetList() != null) {
                if (analyzeBean.getAsiaTrend().getBattleHistory().getLetList().size() != 0) {
                    for (NewAnalyzeBean.AsiaTrendEntity.BattleHistoryEntity.LetListEntity entity : analyzeBean.getAsiaTrend().getBattleHistory().getLetList()) {

                        List<Integer> list1 = new ArrayList<>();
                        list1.add(entity.getLet());
                        list1.add(entity.isHomeGround() ? 1 : 2); //主队是1 客队是2
                        asiaHistoryList.add(list1);
                    }
                    mChartLetHistory.setLineChartList(asiaHistoryList);
                }
                mLetNodata1.setVisibility(View.GONE);
            } else {
                mLetNodata1.setVisibility(View.VISIBLE);
            }
            //亚盘立交交锋饼状图
            if (getActivity() != null && analyzeBean.getAsiaTrend().getBattleHistory() != null && analyzeBean.getAsiaTrend().getBattleHistory().getStatistics() != null) {
                NewAnalyzeBean.AsiaTrendEntity.Statistics statistics = analyzeBean.getAsiaTrend().getBattleHistory().getStatistics();
                mLetHistoryHomeWin.setText(statistics.getWinPercent());
                mLetHistoryGuestWin.setText(statistics.getLosePercent());
                mLetHistoryDraw.setText(statistics.getDrawPercent());
                mLetHistoryVsCount.setText(statistics.getVsCount() + "");
                String a[] = statistics.getWinPercent().split("%");
                String b[] = statistics.getLosePercent().split("%");
                String c[] = statistics.getDrawPercent().split("%");
                int winPercent = Integer.parseInt(a[0]);
                int losePercent = Integer.parseInt(b[0]);
                int drawPercent = Integer.parseInt(c[0]);

                mLetHistoryProgressBar.setProgress(winPercent);
                mLetHistoryProgressBar.setProgress2(losePercent);
                mLetHistoryProgressBar.setProgress3(drawPercent);
                mLetHistoryProgressBar.setCircleProgressColor(getResources().getColor(R.color.football_analyze_win_color));
                mLetHistoryProgressBar.setCircleProgressColor2(getResources().getColor(R.color.football_analyze_lose_color));
                mLetHistoryProgressBar.setCircleProgressColor3(getResources().getColor(R.color.football_analyze_draw_color));
                mLetHistoryProgressBar.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_width));
                mLetHistoryProgressBar.setDatas(statistics.getWin() + "", statistics.getLose() + "", statistics.getDraw() + "");
            }

            //亚盘近期对比主队
            List<List<Integer>> asiaHomeList = new ArrayList<>();
            if (analyzeBean.getAsiaTrend().getHomeRecent() != null && analyzeBean.getAsiaTrend().getHomeRecent().getTrendList().size() != 0) {
                for (NewAnalyzeBean.AsiaTrendEntity.TrendListEntity entity : analyzeBean.getAsiaTrend().getHomeRecent().getTrendList()) {

                    List<Integer> list1 = new ArrayList<>();
                    list1.add(entity.getLet());
                    list1.add(entity.isHomeGround() ? 1 : 2);
                    asiaHomeList.add(list1);
                }
                mChartLetHome.setLineChartList(asiaHomeList);
                mLetNodata3.setVisibility(View.GONE);
            } else {
                mLetNodata3.setVisibility(View.VISIBLE);
            }

            if (getActivity() != null && analyzeBean.getAsiaTrend().getHomeRecent() != null && analyzeBean.getAsiaTrend().getHomeRecent().getStatistics() != null) {
                NewAnalyzeBean.AsiaTrendEntity.Statistics statistics = analyzeBean.getAsiaTrend().getHomeRecent().getStatistics();
                mLetRecentHomeWinText.setText(mHomeName + getActivity().getResources().getString(R.string.new_analyze_yingpanlv));
                mLetRecentHomeWinRate.setText(statistics.getWinPercent());
                String a[] = statistics.getWinPercent().split("%");
                int winPercent = Integer.parseInt(a[0]);
                mLetRecentHomeProgress.setProgress(winPercent);
                mLetRecentHomeProgress.setCircleProgressColor(getResources().getColor(R.color.football_analyze_win_color));
                mLetRecentHomeWinRate.setText(statistics.getWinPercent());
                mLetRecentHomeProgress.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_width));
            }


            //亚盘近期对比客队
            List<List<Integer>> asiaGuestList = new ArrayList<>();
            if (analyzeBean.getAsiaTrend().getGuestRecent() != null && analyzeBean.getAsiaTrend().getGuestRecent().getTrendList().size() != 0) {
                for (NewAnalyzeBean.AsiaTrendEntity.TrendListEntity entity : analyzeBean.getAsiaTrend().getGuestRecent().getTrendList()) {
                    List<Integer> list = new ArrayList<>();
                    list.add(entity.getLet() == 1 ? 2 : entity.getLet() == 2 ? 1 : entity.getLet()); //因为主队是赢走输 客队是输走赢 。是对称的。所以客队的在这里直接 赢变输，输变赢。然后控件中就可以不处理了。
                    list.add(entity.isHomeGround() ? 1 : 2);
                    asiaGuestList.add(list);
                }
                mChartLetGuest.setLineChartList(asiaGuestList);
                mLetNodata4.setVisibility(View.GONE);
            } else {
                mLetNodata4.setVisibility(View.VISIBLE);
            }

            if (getActivity() != null && analyzeBean.getAsiaTrend().getGuestRecent() != null && analyzeBean.getAsiaTrend().getGuestRecent().getStatistics() != null) {
                NewAnalyzeBean.AsiaTrendEntity.Statistics statistics = analyzeBean.getAsiaTrend().getGuestRecent().getStatistics();
                mLetRecentGuestWinText.setText(mGuestName + getActivity().getResources().getString(R.string.new_analyze_yingpanlv));
                mLetRecentGuestWinRate.setText(statistics.getWinPercent());
                String a[] = statistics.getWinPercent().split("%");
                int winPercent = Integer.parseInt(a[0]);
                mLetRecentGuestProgress.setProgress(winPercent);
                mLetRecentGuestProgress.setCircleProgressColor(getResources().getColor(R.color.football_analyze_lose_color));
                mLetRecentGuestWinRate.setText(statistics.getWinPercent());
                mLetRecentGuestProgress.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_width));
            }


        } else {
            mllLet.setVisibility(View.GONE);
            mLetAllNodata.setVisibility(View.VISIBLE);
        }

        //大小球走势
        if (analyzeBean.getSizeTrend() != null) {

            mllSize.setVisibility(View.VISIBLE);
            mSizeAllNodata.setVisibility(View.GONE);
            if (getActivity() != null && analyzeBean.getSizeTrend().getBattleHistory() != null && analyzeBean.getSizeTrend().getBattleHistory().getPointList() != null
                    && analyzeBean.getSizeTrend().getBattleHistory().getPointList().size() != 0) {
                mSizeAdapter = new AnalyzeAsiaAdapter(getActivity(), analyzeBean.getSizeTrend().getBattleHistory().getPointList(), analyzeBean);
                mSizeListView.setAdapter(mSizeAdapter);
            } else {
                mSizeNodata1.setVisibility(View.VISIBLE);
                mSizeNodata2.setVisibility(View.VISIBLE);
            }

            //大小球历史交锋
            List<List<Integer>> sizeHistoryList = new ArrayList<>();
            if (analyzeBean.getSizeTrend().getBattleHistory() != null && analyzeBean.getSizeTrend().getBattleHistory().getTotList() != null) {
                if (analyzeBean.getSizeTrend().getBattleHistory().getTotList().size() != 0) {
                    for (NewAnalyzeBean.SizeTrendEntity.BattleHistoryEntity.TotListEntity entity : analyzeBean.getSizeTrend().getBattleHistory().getTotList()) {

                        List<Integer> list1 = new ArrayList<>();
                        list1.add(entity.getTot());
                        list1.add(entity.isHomeGround() ? 1 : 2); //主队是1 客队是2
                        sizeHistoryList.add(list1);
                    }
                    mChartSizeHistory.setLineChartList(sizeHistoryList);
                }
                mSizeNodata1.setVisibility(View.GONE);
            } else {
                mSizeNodata1.setVisibility(View.VISIBLE);
            }
            if (getActivity() != null && analyzeBean.getSizeTrend().getBattleHistory() != null && analyzeBean.getSizeTrend().getBattleHistory().getStatistics() != null) {
                NewAnalyzeBean.SizeTrendEntity.Statistics statistics = analyzeBean.getSizeTrend().getBattleHistory().getStatistics();
                mSizeHistoryBigRate.setText(statistics.getBigPercent());
                mSizeHistorySmallRate.setText(statistics.getSmallPercent());
                mSizeHistoryDraw.setText(statistics.getDrawPercent());
                mSizeHistoryVsCount.setText(statistics.getVsCount() + "");
                String a[] = statistics.getBigPercent().split("%");
                String b[] = statistics.getSmallPercent().split("%");
                String c[] = statistics.getDrawPercent().split("%");
                int bigPercent = Integer.parseInt(a[0]);
                int smallPercent = Integer.parseInt(b[0]);
                int drawPercent = Integer.parseInt(c[0]);

                mSizeHistoryProgressBar.setProgress(bigPercent);
                mSizeHistoryProgressBar.setProgress2(smallPercent);
                mSizeHistoryProgressBar.setProgress3(drawPercent);
                mSizeHistoryProgressBar.setCircleProgressColor(getResources().getColor(R.color.football_analyze_win_color));
                mSizeHistoryProgressBar.setCircleProgressColor2(getResources().getColor(R.color.football_analyze_lose_color));
                mSizeHistoryProgressBar.setCircleProgressColor3(getResources().getColor(R.color.football_analyze_draw_color));
                mSizeHistoryProgressBar.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_width));
                mSizeHistoryProgressBar.setDatas(statistics.getBig() + "", statistics.getSmall() + "", statistics.getDraw() + "");

            }

            //大小球近期对比主队
            List<List<Integer>> sizeHomeList = new ArrayList<>();
            if (analyzeBean.getSizeTrend().getHomeRecent() != null && analyzeBean.getSizeTrend().getHomeRecent().getTrendList().size() != 0) {
                for (NewAnalyzeBean.SizeTrendEntity.TrendListEntity entity : analyzeBean.getSizeTrend().getHomeRecent().getTrendList()) {

                    List<Integer> list1 = new ArrayList<>();
                    list1.add(entity.getTot());
                    list1.add(entity.isHomeGround() ? 1 : 2);
                    sizeHomeList.add(list1);
                }
                mChartSizeHome.setLineChartList(sizeHomeList);
            } else {
                mSizeNodata3.setVisibility(View.VISIBLE);
            }
            if (getActivity() != null && analyzeBean.getSizeTrend().getHomeRecent() != null && analyzeBean.getSizeTrend().getHomeRecent().getStatistics() != null) {
                NewAnalyzeBean.SizeTrendEntity.Statistics statistics = analyzeBean.getSizeTrend().getHomeRecent().getStatistics();
                mSizeRecentHomeBigRate.setText(statistics.getBigPercent());
                mSizeRecentHomeSmallRate.setText(statistics.getSmallPercent());
                mSizeRecentHomeDraw.setText(statistics.getDrawPercent());
                mSizeRecentHomeVsCount.setText(statistics.getVsCount() + "");
                String a[] = statistics.getBigPercent().split("%");
                String b[] = statistics.getSmallPercent().split("%");
                String c[] = statistics.getDrawPercent().split("%");
                int bigPercent = Integer.parseInt(a[0]);
                int smallPercent = Integer.parseInt(b[0]);
                int drawPercent = Integer.parseInt(c[0]);

                mSizeRecentHomeProgressBar.setProgress(bigPercent);
                mSizeRecentHomeProgressBar.setProgress2(smallPercent);
                mSizeRecentHomeProgressBar.setProgress3(drawPercent);
                mSizeRecentHomeProgressBar.setCircleProgressColor(getResources().getColor(R.color.football_analyze_win_color));
                mSizeRecentHomeProgressBar.setCircleProgressColor2(getResources().getColor(R.color.football_analyze_lose_color));
                mSizeRecentHomeProgressBar.setCircleProgressColor3(getResources().getColor(R.color.football_analyze_draw_color));
                mSizeRecentHomeProgressBar.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_width));
                mSizeRecentHomeProgressBar.setDatas(statistics.getBig() + "", statistics.getSmall() + "", statistics.getDraw() + "");

            }

            //大小球近期对比客队
            List<List<Integer>> sizeGuestList = new ArrayList<>();
            if (analyzeBean.getSizeTrend().getGuestRecent() != null && analyzeBean.getSizeTrend().getGuestRecent().getTrendList().size() != 0) {
                for (NewAnalyzeBean.SizeTrendEntity.TrendListEntity entity : analyzeBean.getSizeTrend().getGuestRecent().getTrendList()) {
                    List<Integer> list = new ArrayList<>();
                    list.add(entity.getTot() == 1 ? 2 : entity.getTot() == 2 ? 1 : entity.getTot()); //因为主队是赢走输 客队是输走赢 。是对称的。所以客队的在这里直接 赢变输，输变赢。然后控件中就可以不处理了。
                    list.add(entity.isHomeGround() ? 1 : 2);
                    sizeGuestList.add(list);
                }
                mChartSizeGuest.setLineChartList(sizeGuestList);
            } else {
                mSizeNodata4.setVisibility(View.VISIBLE);
            }
            if (getActivity() != null && analyzeBean.getSizeTrend().getGuestRecent() != null && analyzeBean.getSizeTrend().getGuestRecent().getStatistics() != null) {
                NewAnalyzeBean.SizeTrendEntity.Statistics statistics = analyzeBean.getSizeTrend().getGuestRecent().getStatistics();
                mSizeRecentGuestBigRate.setText(statistics.getBigPercent());
                mSizeRecentGuestSmallRate.setText(statistics.getSmallPercent());
                mSizeRecentGuestDraw.setText(statistics.getDrawPercent());
                mSizeRecentGuestVsCount.setText(statistics.getVsCount() + "");
                String a[] = statistics.getBigPercent().split("%");
                String b[] = statistics.getSmallPercent().split("%");
                String c[] = statistics.getDrawPercent().split("%");
                int bigPercent = Integer.parseInt(a[0]);
                int smallPercent = Integer.parseInt(b[0]);
                int drawPercent = Integer.parseInt(c[0]);

                mSizeRecentGuestProgressBar.setProgress(bigPercent);
                mSizeRecentGuestProgressBar.setProgress2(smallPercent);
                mSizeRecentGuestProgressBar.setProgress3(drawPercent);
                mSizeRecentGuestProgressBar.setCircleProgressColor(getResources().getColor(R.color.football_analyze_win_color));
                mSizeRecentGuestProgressBar.setCircleProgressColor2(getResources().getColor(R.color.football_analyze_lose_color));
                mSizeRecentGuestProgressBar.setCircleProgressColor3(getResources().getColor(R.color.football_analyze_draw_color));
                mSizeRecentGuestProgressBar.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_width));
                mSizeRecentGuestProgressBar.setDatas(statistics.getBig() + "", statistics.getSmall() + "", statistics.getDraw() + "");

            }
        } else {
            mllSize.setVisibility(View.GONE);
            mSizeAllNodata.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 亚盘历史交锋的文字描述
     *
     * @param statistics
     */
    private void setLetHistoryText(NewAnalyzeBean.AsiaTrendEntity.Statistics statistics) {
        String text1 = getActivity().getString(R.string.new_analyze_liangduijin) + statistics.getVsCount() + getActivity().getString(R.string.new_analyze_changjiaofeng);
        mTextLet1.setText(text1);

        String text2 = mHomeName + "<font color='#dd2f1c'><b>" + statistics.getWin() + getActivity().getString(R.string.new_analyze_ciyingpan) + "</b></font>"
                + "," + mGuestName + "<font color='#dd2f1c'><b>" + statistics.getLose() + getActivity().getString(R.string.new_analyze_ciyingpan) + "</b></font>" +
                "," + "<font color='#0090ff'><b>" + statistics.getDraw() + getActivity().getString((R.string.new_analyze_cizoupan)) + "</b></font>";
        String text3 = mHomeName + getActivity().getString(R.string.new_analyze_yingpanlv) + "<font color='#dd2f1c'><b>" + statistics.getWinPercent() + "</b></font>"
                + "," + mGuestName + getActivity().getString(R.string.new_analyze_yingpanlv) + "<font color='#dd2f1c'><b>" + statistics.getLosePercent() + "</b></font>";
        mTextLet2.setText(Html.fromHtml(text2));
        mTextLet3.setText(Html.fromHtml(text3));
    }

    /**
     * 近期对比主队 text2
     *
     * @param statistics
     */
    private void setLetRecentHomeText(NewAnalyzeBean.AsiaTrendEntity.Statistics statistics) {
        String douhao = ",";
        String text1 = getActivity().getString(R.string.new_analyze_recent_game);
        mTextLet1.setText(text1);
        String text2 = mHomeName + "<font color='#dd2f1c'><b>" + statistics.getWin() + getActivity().getString(R.string.new_analyze_ciyingpan) + "</b></font>"
                + douhao + "<font color='#21b11e'><b>" + statistics.getLose() + getActivity().getString(R.string.new_analyze_cishupan) + "</b></font>" +
                douhao + "<font color='#0090ff'><b>" + statistics.getDraw() + getActivity().getString((R.string.new_analyze_cizoupan)) + "</b></font>";
        mTextLet2.setText(Html.fromHtml(text2));
    }

    /**
     * 近期对比客队 text3
     *
     * @param statistics
     */
    private void setLetRecentGuestText(NewAnalyzeBean.AsiaTrendEntity.Statistics statistics) {
        String douhao = ",";
        String text3 = mGuestName + "<font color='#dd2f1c'><b>" + statistics.getWin() + getActivity().getString(R.string.new_analyze_ciyingpan) + "</b></font>"
                + douhao + "<font color='#21b11e'><b>" + statistics.getLose() + getActivity().getString(R.string.new_analyze_cishupan) + "</b></font>" +
                douhao + "<font color='#0090ff'><b>" + statistics.getDraw() + getActivity().getString((R.string.new_analyze_cizoupan)) + "</b></font>";
        mTextLet3.setText(Html.fromHtml(text3));
    }

    /**
     * 大小球歷史交鋒
     *
     * @param statistics
     */
    private void setSizeHistoryText(NewAnalyzeBean.SizeTrendEntity.Statistics statistics) {
        String text1 = getActivity().getString(R.string.new_analyze_liangduijin) + statistics.getVsCount() + getActivity().getString(R.string.new_analyze_changjiaofeng);
        String text2 = "<font color='#DD2F1C'><b>"
                + statistics.getBig() + getActivity().getString(R.string.new_analyze_cidaqiu) + "</b></font> " +
                "," + "<font color='#21b11e'><b>" + statistics.getSmall() + getActivity().getString(R.string.new_analyze_cixiaoqiu) + "</b></font> " + "," +
                "<font color='#0090ff'><b>" + statistics.getDraw() + getActivity().getString(R.string.new_analyze_cizoupan) + "</b></font> " + ";";
        mTextSize1.setText(Html.fromHtml(text1));
        mTextSize2.setText(Html.fromHtml(text2));
        mTextSize3.setText("");
    }

    /**
     * 大小球近期對比
     *
     * @param statistics
     */
    private void setSizeRecentHomeText(NewAnalyzeBean.SizeTrendEntity.Statistics statistics) {
        String douhao = ",";
        String text1 = getActivity().getString(R.string.new_analyze_recent_game);
        mTextSize1.setText(text1);
        String text2 = mHomeName + "<font color='#dd2f1c'><b>" + statistics.getBig() + getActivity().getString(R.string.new_analyze_cidaqiu) + "</b></font>"
                + douhao + "<font color='#21b11e'><b>" + statistics.getSmall() + getActivity().getString(R.string.new_analyze_cixiaoqiu) + "</b></font>" +
                douhao + "<font color='#0090ff'><b>" + statistics.getDraw() + getActivity().getString((R.string.new_analyze_cizoupan)) + "</b></font>";
        mTextSize2.setText(Html.fromHtml(text2));
    }

    private void setSizeRecentGuestText(NewAnalyzeBean.SizeTrendEntity.Statistics statistics) {
        String douhao = ",";
        String text3 = mGuestName + "<font color='#dd2f1c'><b>" + statistics.getBig() + getActivity().getString(R.string.new_analyze_cidaqiu) + "</b></font>"
                + douhao + "<font color='#21b11e'><b>" + statistics.getSmall() + getActivity().getString(R.string.new_analyze_cixiaoqiu) + "</b></font>" +
                douhao + "<font color='#0090ff'><b>" + statistics.getDraw() + getActivity().getString((R.string.new_analyze_cizoupan)) + "</b></font>";
        mTextSize3.setText(Html.fromHtml(text3));
    }


//    /**
//     * 设置队员信息的主客队队名
//     * @param home
//     * @param guest
//     */
//    public void setTeamName(String home,String guest){
//
////        mHomeName=home;
////        mGuestName=guest;
//    }

    /**
     * 设置近期战绩图片  胜平负
     *
     * @param mImage
     * @param recent
     */
    private void setRecent(ImageView mImage, int recent) {
        if (recent == 0) { //平
            mImage.setBackgroundResource(R.mipmap.basket_draw);
        }
        if (recent == 2) { //输
            mImage.setBackgroundResource(R.mipmap.basket_lose);
        }
        if (recent == 1) { //赢
            mImage.setBackgroundResource(R.mipmap.basket_win);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.football_analyze_more_record:
            case R.id.tv_analyze_more_message1:
            case R.id.tv_analyze_more_message2:
            case R.id.ll_analyze_let:
            case R.id.ll_analyze_size:
                Intent intent = new Intent(getActivity(), FootballAnalyzeDetailsActivity.class);
                intent.putExtra("analyzeId", getActivity() != null ? ((FootballMatchDetailActivity)getActivity()).mThirdId : null);
                startActivity(intent);
                break;
            case R.id.football_analyze_integral_table:
//                Intent intent1=new Intent(getActivity(), FootballInformationActivity.class);
//                intent1.putExtra("lid",mAnalyzeBean.getLeagueId()+"");
//                intent1.putExtra("leagueType",mAnalyzeBean.getLeagueType()+"");
//                startActivity(intent1);

                Intent intent1 = new Intent(getActivity(), FootballDatabaseDetailsActivity.class);

                DataBaseBean bean = new DataBaseBean();
                bean.setKind(mAnalyzeBean.getLeagueType() + "");
                bean.setLeagueId(mAnalyzeBean.getLeagueId() + "");
                intent1.putExtra("league", bean);
                intent1.putExtra("isIntegral", true);
                startActivity(intent1);
                break;
        }
    }
}
