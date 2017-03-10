package com.hhly.mlottery.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.bean.BarrageBean;
import com.hhly.mlottery.bean.ShareBean;
import com.hhly.mlottery.bean.footballDetails.DetailsCollectionCountBean;
import com.hhly.mlottery.bean.footballDetails.MatchDetail;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;
import com.hhly.mlottery.bean.footballDetails.MatchTimeLiveBean;
import com.hhly.mlottery.bean.footballDetails.MathchStatisInfo;
import com.hhly.mlottery.bean.footballDetails.PreLiveText;
import com.hhly.mlottery.bean.multiplebean.MultipleByValueBean;
import com.hhly.mlottery.bean.websocket.WebSocketStadiumKeepTime;
import com.hhly.mlottery.bean.websocket.WebSocketStadiumLiveTextEvent;
import com.hhly.mlottery.callback.FootballLiveGotoChart;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.ShareFragment;
import com.hhly.mlottery.frame.chartBallFragment.ChartBallFragment;
import com.hhly.mlottery.frame.footballframe.AnalyzeFragment;
import com.hhly.mlottery.frame.footballframe.DetailsRollballFragment;
import com.hhly.mlottery.frame.footballframe.IntelligenceFragment;
import com.hhly.mlottery.frame.footballframe.OddsFragment;
import com.hhly.mlottery.frame.footballframe.StatisticsFragment;
import com.hhly.mlottery.frame.footballframe.eventbus.ScoresMatchFocusEventBusEntity;
import com.hhly.mlottery.util.CountDown;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.FocusUtils;
import com.hhly.mlottery.util.FootballLiveTextComparator;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.NetworkUtils;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.StadiumUtils;
import com.hhly.mlottery.util.StringUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.BarrageView;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * @author wang gang
 * @date 2016/6/2 16:53
 * @des 足球内页改版
 */
public class FootballMatchDetailActivity extends BaseWebSocketActivity implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener, ExactSwipeRefreshLayout.OnRefreshListener {

    private final static int ERROR = -1;//访问失败
    private final static int SUCCESS = 0;// 访问成功
    private final static int STARTLOADING = 1;// 正在加载中
    //直播状态 liveStatus
    private final static String BEFOURLIVE = "0";//直播前
    private final static String ONLIVE = "1";//直播中
    private final static String LIVEENDED = "-1";//直播结束

    private final static String MATCH_TYPE = "1"; //足球

    private final static int ROLLBALL_FG = 0;
    private final static int LIVE_FG = 1;
    private final static int ANALYZE_FG = 2;
    private final static int STATISTICS_FG = 3;
    private final static int ODDS_FG = 4;
    private final static int TALKBALL_FG = 5;
    private int infoCenter = -1;// 情报中心中转标记
    private int chartBallView = -1;// 聊球界面转标记

    //事件直播
    //主队事件
    private static final String SCORE = "1029";//主队进球
    private static final String RED_CARD = "1032";
    private static final String YELLOW_CARD = "1034";
    private static final String SUBSTITUTION = "1055";
    private static final String CORNER = "1025";
    private static final String YTORED = "1045";//两黄变一红
    private static final String DIANQIU = "1031";

    //客队事件
    private static final String SCORE1 = "2053";//客队进球
    private static final String RED_CARD1 = "2056";
    private static final String YELLOW_CARD1 = "2058";
    private static final String SUBSTITUTION1 = "2079";
    private static final String CORNER1 = "2049";
    private static final String YTORED1 = "2069";//两黄变一红
    private static final String DIANQIU1 = "2055";

    //走势图
    private static final String SHOOT = "1039";
    private static final String SHOOTASIDE = "1040";
    private static final String SHOOTASIDE2 = "1041";
    private static final String DANGERATTACK = "1026";
    private static final String ATTACK = "1024";

    private static final String SHOOT1 = "2063";
    private static final String SHOOTASIDE1 = "2064";
    private static final String SHOOTASIDE12 = "2065";
    private static final String DANGERATTACK1 = "2050";
    private static final String ATTACK1 = "2048";


    /**
     * 未开
     */
    private static final String NOTOPEN = "0";
    /**
     * 上半场
     */
    private static final String FIRSTHALF = "1";
    /**
     * 中场
     */
    private static final String HALFTIME = "2";
    /**
     * 下半场
     */
    private static final String SECONDHALF = "3";
    /**
     * 完场
     **/
    private static final String MATCHFINISH = "-1";


    private FrameLayout fl_odds_loading;
    private FrameLayout fl_odds_net_error_details;


    public ExactSwipeRefreshLayout mRefreshLayout; //下拉刷新

    private FragmentManager fragmentManager;
    private ViewPager mViewPager;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    public AppBarLayout appBarLayout;
    private TabLayout mTabLayout;
    private TabsAdapter mTabsAdapter;
    private Toolbar toolbar;
    private Context mContext;

    /**
     * 赛事id
     */
    public String mThirdId;

    public int mtype = 0;
    private int currentFragmentId = 0;

    private MatchDetail mMatchDetail;

    private MathchStatisInfo mathchStatisInfo;

    private List<MatchTextLiveBean> matchLive;
    private List<Integer> allMatchLiveMsgId;

    private List<MatchTimeLiveBean> eventMatchTimeLiveList; //赛中文字直播事件帅选List

    private List<MatchTextLiveBean> trendChartList; //赛中文字直播事件帅选List


    /**
     * 判断ViewPager是否已经初始化过
     */
    private boolean isInitedViewPager = false;

    /**
     * 比赛时间
     */
    private String mKeepTime;


    /**
     * 文字直播最新一条推送时间
     */
    private int liveTextTime;

    private int matchKeepTime;


    /**
     * 保存上一场的状态
     */
    private String mPreStatus;
    public final static String BUNDLE_PARAM_THIRDID = "thirdId";

    private boolean isMatchStart = false;  //判断比赛推送时间状态

    private DetailsRollballFragment mDetailsRollballFragment; //滚球

    private AnalyzeFragment mAnalyzeFragment;  //分析
    private OddsFragment mOddsFragment;         //指数

    private StatisticsFragment mStatisticsFragment;  //统计
    private IntelligenceFragment mIntelligenceFragment; // 情报

    private TextView reLoading; //赔率请求失败重新加载

    private String shareHomeIconUrl;

    private ImageView iv_back;
    private ImageView iv_setting;
    private TextView head_score;

    private TextView head_home_name;
    private TextView head_guest_name;
    private View iv_share;

    private ShareFragment mShareFragment;

    private final static int PERIOD_20 = 1000 * 60 * 20;//刷新周期二十分钟
    private final static int PERIOD_5 = 1000 * 60 * 5;//刷新周期五分钟

    private final static int GIFPERIOD_2 = 1000 * 5;//刷新周期两分钟

    /**
     * 赛前轮询周期
     */
    private int mReloadPeriod = PERIOD_20;

    private boolean isRquestSuccess = true;

    private FootballLiveGotoChart mFootballLiveGotoChart;

    private String matchStartTime;
    private ChartBallFragment mChartBallFragment;


    //头部
    private ImageView iv_home_icon;
    private ImageView iv_guest_icon;
    private ImageView iv_bg;

    private TextView tv_homename;
    private TextView tv_guestname;


    private TextView score;

    private TextView date;

    private LinearLayout mMatchTypeLayout;

    private TextView tv_addMultiView;

    private int mType = 0;

    private TextView mMatchType1;

    private TextView mMatchType2;

    private static final String baseUrl = "http://pic.13322.com/bg/";

    private FrameLayout fl_head;

    private LinearLayout btn_showGif;
    private BarrageView barrage_view;


    private Timer gifTimer;

    private TimerTask gifTimerTask;

    private boolean isFirstShowGif = true;

    private RelativeLayout rl_gif_notice;

    private int gifCount = 0;

    private CountDown countDown;
    private final static int MILLIS_INFuture = 3000;//倒计时3秒
    private final static String FOOTBALL_GIF = "football_gif";

    private ImageView barrage_switch;

    boolean barrage_isFocus = false;
    private View view_red;

    boolean isAddMultiViewHide = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getExtras() != null) {
            mThirdId = getIntent().getExtras().getString(BUNDLE_PARAM_THIRDID, "1300");
            currentFragmentId = getIntent().getExtras().getInt("currentFragmentId");
            infoCenter = getIntent().getExtras().getInt("info_center");
            isAddMultiViewHide = getIntent().getExtras().getBoolean("isAddMultiViewHide");
            chartBallView = getIntent().getExtras().getInt("chart_ball_view");
        }
        EventBus.getDefault().register(this);

        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic("USER.topic.liveEvent." + mThirdId + "." + appendLanguage());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_match_details_test);

        /**当前Activity不统计*/
        MobclickAgent.openActivityDurationTrack(false);

        this.mContext = getApplicationContext();

        L.e(TAG, "mThirdId = " + mThirdId);
        initHeadView();
        initView();
        initEvent();


        /***
         * 足球内页头部ViewPager
         */

        mDetailsRollballFragment = DetailsRollballFragment.newInstance(mThirdId);

        //分析
        mAnalyzeFragment = AnalyzeFragment.newInstance(mThirdId, "", "");
        //指数
        mOddsFragment = OddsFragment.newInstance();
        //统计
        mStatisticsFragment = StatisticsFragment.newInstance();
        // 情报
        mIntelligenceFragment = IntelligenceFragment.newInstance(mThirdId);
        // 聊球
        mChartBallFragment = ChartBallFragment.newInstance(0, mThirdId);

        mTabsAdapter.addFragments(mDetailsRollballFragment, mStatisticsFragment, mAnalyzeFragment, mIntelligenceFragment, mOddsFragment, mChartBallFragment);
        mViewPager.setOffscreenPageLimit(5);//设置预加载页面的个数。
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mFootballLiveGotoChart = new FootballLiveGotoChart() {
            @Override
            public void onClick() {
                mViewPager.setCurrentItem(TALKBALL_FG, false);
            }
        };

        mHandler.sendEmptyMessage(STARTLOADING);
        loadData();
    }


    private void initView() {
        String[] titles = mContext.getResources().getStringArray(R.array.foot_details_tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRefreshLayout = (ExactSwipeRefreshLayout) findViewById(R.id.refresh_layout_details);
        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (position != 5) {
////                    appBarLayout.setExpanded(false);
//                    MyApp.getContext().sendBroadcast(new Intent("closeself"));
//                }
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        appBarLayout.addOnOffsetChangedListener(this);

        fragmentManager = getSupportFragmentManager();

        //底部ViewPager(滚球、指数等)
        mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
        mTabsAdapter.setTitles(titles);


        fl_odds_loading = (FrameLayout) findViewById(R.id.fl_odds_loading_details);
        fl_odds_net_error_details = (FrameLayout) findViewById(R.id.fl_odds_networkError_details);
        reLoading = (TextView) findViewById(R.id.reLoading_details);
        reLoading.setOnClickListener(this);

        head_score = (TextView) findViewById(R.id.head_score);
        head_home_name = (TextView) findViewById(R.id.head_home_name);
        head_guest_name = (TextView) findViewById(R.id.head_guest_name);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_setting = (ImageView) findViewById(R.id.iv_setting);

        iv_back.setOnClickListener(this);
        iv_setting.setOnClickListener(this);

        barrage_view = (BarrageView) findViewById(R.id.barrage_view);
        barrage_switch = (ImageView) findViewById(R.id.barrage_switch);
        barrage_switch.setOnClickListener(this);

        tv_addMultiView.setOnClickListener(this);
    }

    public void onEventMainThread(BarrageBean barrageBean) {
        barrage_view.setDatas(barrageBean.getUrl(), barrageBean.getMsg().toString());
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if ((-verticalOffset) > appBarLayout.getTotalScrollRange() * 3 / 5) {
            head_home_name.setVisibility(View.VISIBLE);
            head_guest_name.setVisibility(View.VISIBLE);
            head_score.setVisibility(View.VISIBLE);
        } else {
            head_home_name.setVisibility(View.GONE);
            head_guest_name.setVisibility(View.GONE);
            head_score.setVisibility(View.GONE);
        }

        if (mCollapsingToolbarLayout.getHeight() + verticalOffset < fl_head.getHeight()) {
            mRefreshLayout.setEnabled(false);   //收缩
        } else {

            if (isRquestSuccess) {
                mRefreshLayout.setEnabled(true); //展开
            } else {
                mRefreshLayout.setEnabled(false); //展开
            }
        }
    }

    @Override
    public void onRefresh() {
        L.d(TAG, "下拉刷新");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                // if (mMatchDetail != null && !"-1".equals(mMatchDetail.getLiveStatus())) {
                if (mMatchDetail != null) {
                    L.d(TAG, "下拉刷新未开赛和正在比赛的");
                    loadData();

                    //走势图
                    mStatisticsFragment.initChartData(mMatchDetail.getLiveStatus());
                    //统计图
                    mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());

                    if (ONLIVE.equals(mMatchDetail.getLiveStatus())) {
                        connectWebSocket();
                    }


                    mAnalyzeFragment.initData();// 分析下拉刷新
                    mOddsFragment.oddPlateRefresh(); // 指数刷新
                    mChartBallFragment.onRefresh();// 聊球
                }
            }
        }, 1000);
    }

    private Timer mReloadTimer;
    private boolean isStartTimer = false;

    private void startReloadTimer() {
        if (!isStartTimer && !isFinishing()) {//已经开启则不需要再开启
            TimerTask reloadTimerTask = new TimerTask() {
                @Override
                public void run() {
                    L.d(TAG, "TimerTask run....");
                    loadData();
                }
            };
            mReloadTimer = new Timer();
            mReloadTimer.schedule(reloadTimerTask, mReloadPeriod, mReloadPeriod);
            isStartTimer = true;
        }
    }


    private void loadData() {
        Map<String, String> params = new HashMap<>();
        params.put("thirdId", mThirdId);

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_INFO_FIRST, params, new VolleyContentFast.ResponseSuccessListener<MatchDetail>() {
            @Override
            public void onResponse(MatchDetail matchDetail) {

                if (!"200".equals(matchDetail.getResult())) {
                    mHandler.sendEmptyMessage(ERROR);
                    return;
                }

                mMatchDetail = matchDetail;

                shareHomeIconUrl = matchDetail.getHomeTeamInfo().getUrl();
                mtype = Integer.parseInt(matchDetail.getLiveStatus());

                if (!isInitedViewPager) {//如果ViewPager未初始化
                    initViewPager(matchDetail);
                    mPreStatus = matchDetail.getLiveStatus();
                } else {

                    L.i(TAG, "不是初始化=" + isInitedViewPager + "pre=" + mPreStatus + "getLiveStatus=" + matchDetail.getLiveStatus() + "isFinishing=" + isFinishing());

                    if (isFinishing()) {
                        return;
                    }

                    //下拉刷新
                    if ("0".equals(mPreStatus) && "0".equals(matchDetail.getLiveStatus()) && !isFinishing()) { //赛前


                        initPreData(matchDetail);
                        setScoreText("VS");

                        head_home_name.setText(matchDetail.getHomeTeamInfo().getName());
                        head_guest_name.setText(matchDetail.getGuestTeamInfo().getName());
                        head_score.setText("VS");
                        mDetailsRollballFragment.refreshMatch(matchDetail, DetailsRollballFragment.DETAILSROLLBALL_TYPE_PRE);


                    } else {

                        initPreData(matchDetail);

                        matchLive = mMatchDetail.getMatchInfo().getMatchLive();
                        allMatchLiveMsgId = new ArrayList<>();

                        //完场
                        if (LIVEENDED.equals(mMatchDetail.getLiveStatus())) {

                            String halfScore = mMatchDetail.getHomeTeamInfo().getHalfScore() + " : " + mMatchDetail.getGuestTeamInfo().getHalfScore();
                            String finishScore = mMatchDetail.getHomeTeamInfo().getScore() + " : " + mMatchDetail.getGuestTeamInfo().getScore();

                            //获取完场事件直播
                            eventMatchTimeLiveList = new ArrayList<>();
                            for (MatchTimeLiveBean m : mMatchDetail.getMatchInfo().getMatchTimeLive()) {
                                if ("2".equals(m.getState()) && "1".equals(m.getCode())) {   //2在完场时间直播中为中场，中场加入中场比分halfScore  放在isHome字段位置
                                    eventMatchTimeLiveList.add(new MatchTimeLiveBean(m.getTime(), m.getCode(), halfScore, m.getMsgId(), m.getState(), m.getPlayInfo(), m.getEnNum(), 0));
                                } else {
                                    eventMatchTimeLiveList.add(new MatchTimeLiveBean(m.getTime(), m.getCode(), m.isHome(), m.getMsgId(), m.getState(), m.getPlayInfo(), m.getEnNum(), 0));
                                }
                            }
                            //99999999任意置值 加入完场状态
                            eventMatchTimeLiveList.add(new MatchTimeLiveBean("99999999", "3", finishScore, "99999", "-1", "", "", 0));
                            setScoreText(mMatchDetail.getHomeTeamInfo().getScore() + " : " + mMatchDetail.getGuestTeamInfo().getScore());
                            setScoreClolor(getApplicationContext().getResources().getColor(R.color.score));


                            head_home_name.setText(matchDetail.getHomeTeamInfo().getName());
                            head_guest_name.setText(matchDetail.getGuestTeamInfo().getName());
                            head_score.setText(mMatchDetail.getHomeTeamInfo().getScore() + ":" + mMatchDetail.getGuestTeamInfo().getScore());
                            mKeepTime = "5400000";//90分钟的毫秒数
                            //是否显示精彩瞬间
                            getCollectionCount();

                            mDetailsRollballFragment.refreshMatch(matchDetail, DetailsRollballFragment.DETAILSROLLBALL_TYPE_ED);


//                            mTalkAboutBallFragment.setClickableLikeBtn(false);


                        } else if (ONLIVE.equals(mMatchDetail.getLiveStatus())) {//未完场头部


                            L.d(TAG, "未完场" + mMatchDetail.getLiveStatus());

                            initMatchSatisInfo();

                            head_home_name.setText(matchDetail.getHomeTeamInfo().getName());
                            head_guest_name.setText(matchDetail.getGuestTeamInfo().getName());
                            head_score.setText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score());

                            String state = matchLive.get(0).getState();//获取最后一个的比赛状态
                            mKeepTime = matchLive.get(0).getTime();//获取时间

                            if (Integer.parseInt(mKeepTime) >= (45 * 60 * 1000)) {
                                if (state.equals(FIRSTHALF) || state.equals(HALFTIME)) {//上半场补时中场时间轴不变
                                    mKeepTime = 45 * 60 * 1000 + "";//时间继续赋值为45分钟
                                }
                            }
                            if (NOTOPEN.equals(state)) {
                                //未开state=0
                                isMatchStart = false;

                            } else if (FIRSTHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) <= 45) {
                                //上半场state=1
                                isMatchStart = true;
                            } else if (FIRSTHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) > 45) {
                                isMatchStart = false;

                            } else if (HALFTIME.equals(state)) {
                                //中场state=2
                                isMatchStart = false;

                            } else if (SECONDHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) <= 90) {
                                //下半场state=3
                                isMatchStart = true;

                            } else if (SECONDHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) > 90) {
                                isMatchStart = false;
                            } else {
                            }

                            if ("0".equals(mPreStatus) && "1".equals(matchDetail.getLiveStatus()) && !isFinishing()) {
                                mDetailsRollballFragment.activateMatch(matchDetail, DetailsRollballFragment.DETAILSROLLBALL_TYPE_ING);


                                if (mReloadTimer != null) {
                                    mReloadTimer.cancel();
                                }

                                mPreStatus = "1";
                            }


                            //是否显示精彩瞬间
                            pollingGifCount();

                            mDetailsRollballFragment.refreshMatch(matchDetail, DetailsRollballFragment.DETAILSROLLBALL_TYPE_ING);

//                            mTalkAboutBallFragment.setClickableLikeBtn(true);

                            Collections.reverse(eventMatchTimeLiveList);
                            //直播事件
                            mStatisticsFragment.setEventMatchLive(mMatchDetail.getLiveStatus(), eventMatchTimeLiveList);

                            //统计图
                            mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                            //走势图表
                            mStatisticsFragment.setTrendChartList(trendChartList);

                        }
                    }
                }

                if ("0".equals(matchDetail.getLiveStatus())) {//Viewpager为初始化和赛前状态，需要开启定时器
                    String serverTime = matchDetail.getMatchInfo().getServerTime();
                    String startTime = matchDetail.getMatchInfo().getStartTime();

                    Date startDate = DateUtil.yyyyMMddhhmmToDate(startTime);

                    long startTimestamp = startDate.getTime();
                    long serverTimestamp = 0;
                    try {
                        serverTimestamp = Long.parseLong(serverTime);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    if (startTimestamp - serverTimestamp > (1000 * 60 * 60)) {//大于一个小时
                        mReloadPeriod = PERIOD_20;
                    } else {
                        if (mReloadPeriod == PERIOD_20) {//如果之前的频率是20分钟，转到5分钟，则需要关闭之前的定时器，从新开启
                            isStartTimer = false;
                            if (mReloadTimer != null) {
                                mReloadTimer.cancel();
                            }
                        }
                        mReloadPeriod = PERIOD_5;
                    }
                    startReloadTimer();
                }
                mHandler.sendEmptyMessage(SUCCESS);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(ERROR);
            }
        }, MatchDetail.class);
    }


    private void initViewPager(MatchDetail matchDetail) {
        if ("0".equals(matchDetail.getLiveStatus())) { //赛前

            if (chartBallView == 1) {
                // 聊球
                mViewPager.setCurrentItem(TALKBALL_FG, false);
            } else if (infoCenter == 1) {
                // 情报
                mViewPager.setCurrentItem(STATISTICS_FG, false);
            } else {
                //赛前进入分析
                mViewPager.setCurrentItem(ANALYZE_FG, false);
            }

            matchStartTime = matchDetail.getMatchInfo().getStartTime();
            initPreData(matchDetail);
            setScoreText("VS");

            head_home_name.setText(matchDetail.getHomeTeamInfo().getName());
            head_guest_name.setText(matchDetail.getGuestTeamInfo().getName());
            head_score.setText("VS");

            mDetailsRollballFragment.setMatchData(DetailsRollballFragment.DETAILSROLLBALL_TYPE_PRE, matchDetail);
            mStatisticsFragment.setEventMatchLive(mMatchDetail.getLiveStatus(), null);
            mStatisticsFragment.setmFootballLiveGotoChart(mFootballLiveGotoChart);
        } else {

            if (chartBallView == 1) {
                // 聊球
                mViewPager.setCurrentItem(TALKBALL_FG, false);
            } else if (infoCenter == 1) {
                // 情报
                mViewPager.setCurrentItem(STATISTICS_FG, false);
            } else {
                mViewPager.setCurrentItem(ROLLBALL_FG, false);
            }

            initPreData(matchDetail);

            matchLive = mMatchDetail.getMatchInfo().getMatchLive();
            allMatchLiveMsgId = new ArrayList<>();


            //完场
            if (LIVEENDED.equals(mMatchDetail.getLiveStatus())) {
                String halfScore = mMatchDetail.getHomeTeamInfo().getHalfScore() + " : " + mMatchDetail.getGuestTeamInfo().getHalfScore();
                String finishScore = mMatchDetail.getHomeTeamInfo().getScore() + " : " + mMatchDetail.getGuestTeamInfo().getScore();

                //获取完场事件直播
                eventMatchTimeLiveList = new ArrayList<>();

                if (mMatchDetail.getMatchInfo().getMatchTimeLive() != null && mMatchDetail.getMatchInfo().getMatchTimeLive().size() > 0) {
                    for (MatchTimeLiveBean m : mMatchDetail.getMatchInfo().getMatchTimeLive()) {
                        if ("2".equals(m.getState()) && "1".equals(m.getCode())) {   //2在完场时间直播中为中场，中场加入中场比分halfScore  放在isHome字段位置
                            eventMatchTimeLiveList.add(new MatchTimeLiveBean(m.getTime(), m.getCode(), halfScore, m.getMsgId(), m.getState(), m.getPlayInfo(), m.getEnNum(), 0));
                        } else {
                            eventMatchTimeLiveList.add(new MatchTimeLiveBean(m.getTime(), m.getCode(), m.isHome(), m.getMsgId(), m.getState(), m.getPlayInfo(), m.getEnNum(), 0));
                        }
                    }
                }
                //99999999任意置值 加入完场状态
                eventMatchTimeLiveList.add(new MatchTimeLiveBean("99999999", "3", finishScore, "99999", "-1", "", "", 0));


                setScoreText(mMatchDetail.getHomeTeamInfo().getScore() + " : " + mMatchDetail.getGuestTeamInfo().getScore());
                setScoreClolor(getApplicationContext().getResources().getColor(R.color.score));


                head_home_name.setText(matchDetail.getHomeTeamInfo().getName());
                head_guest_name.setText(matchDetail.getGuestTeamInfo().getName());
                head_score.setText(mMatchDetail.getHomeTeamInfo().getScore() + ":" + mMatchDetail.getGuestTeamInfo().getScore());

                mKeepTime = "5400000";//90分钟的毫秒数

                //精彩瞬间
                getOverMatchCollectionCount();

                //直播事件
                mStatisticsFragment.setEventMatchLive(mMatchDetail.getLiveStatus(), eventMatchTimeLiveList);
                //走势图
                mStatisticsFragment.finishMatchRequest();
//                mTalkAboutBallFragment.setClickableLikeBtn(false);
                mDetailsRollballFragment.setMatchData(DetailsRollballFragment.DETAILSROLLBALL_TYPE_ED, matchDetail);

            } else if (ONLIVE.equals(mMatchDetail.getLiveStatus())) { //未完场头部


                //place  1主队 2客队
                initMatchSatisInfo();


                head_home_name.setText(matchDetail.getHomeTeamInfo().getName());
                head_guest_name.setText(matchDetail.getGuestTeamInfo().getName());
                head_score.setText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score());

                setScoreText(mathchStatisInfo.getHome_score() + " : " + mathchStatisInfo.getGuest_score() + "");


                String state = matchLive.get(0).getState();//获取最后一个的比赛状态
                mKeepTime = matchLive.get(0).getTime();//获取时间

                if (Integer.parseInt(mKeepTime) >= (45 * 60 * 1000)) {
                    if (state.equals(FIRSTHALF) || state.equals(HALFTIME)) {//上半场补时中场时间轴不变
                        mKeepTime = 45 * 60 * 1000 + "";//时间继续赋值为45分钟
                    }
                }

                if (NOTOPEN.equals(state)) {
                    //未开state=0
                    isMatchStart = false;

                } else if (FIRSTHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) <= 45) {
                    //上半场state=1
                    isMatchStart = true;
                } else if (FIRSTHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) > 45) {
                    isMatchStart = false;

                } else if (HALFTIME.equals(state)) {
                    //中场state=2
                    isMatchStart = false;

                } else if (SECONDHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) <= 90) {
                    //下半场state=3
                    isMatchStart = true;

                } else if (SECONDHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) > 90) {
                    isMatchStart = false;
                } else {
                }

                //精彩瞬间
                pollingGifCount();

                mDetailsRollballFragment.setMatchData(DetailsRollballFragment.DETAILSROLLBALL_TYPE_ING, matchDetail);


//                mTalkAboutBallFragment.setClickableLikeBtn(true);

                Collections.reverse(eventMatchTimeLiveList);
                //直播事件
                mStatisticsFragment.setEventMatchLive(mMatchDetail.getLiveStatus(), eventMatchTimeLiveList);

                //走势图表
                mStatisticsFragment.setTrendChartList(trendChartList);

                mStatisticsFragment.initChartData(mMatchDetail.getLiveStatus());


                //统计图
                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());
            }


        }

        mStatisticsFragment.setChartName(mMatchDetail.getHomeTeamInfo().getName(), mMatchDetail.getGuestTeamInfo().getName());


        if (!isInitedViewPager) {

            if (ONLIVE.equals(mMatchDetail.getLiveStatus())) {
                L.d(TAG, "第一次启动socket");
                L.d("456789", "第一次启动socket");
                connectWebSocket();
            }
        }


        isInitedViewPager = true;
    }


    /**
     * 统计赛中红黄牌等信息
     */
    private void initMatchSatisInfo() {

        mathchStatisInfo = new MathchStatisInfo();


        for (MatchTextLiveBean m : matchLive) {
            switch (m.getCode().trim()) {
                case "1029": //主队进球
                    mathchStatisInfo.setHome_score(mathchStatisInfo.getHome_score() + 1);
                    mathchStatisInfo.setHome_shoot_correct(mathchStatisInfo.getHome_shoot_correct() + 1);

                    break;
                case "1030"://取消主队进球
                    mathchStatisInfo.setHome_score(mathchStatisInfo.getHome_score() - 1);
                    mathchStatisInfo.setHome_shoot_correct(mathchStatisInfo.getHome_shoot_correct() - 1);
                    break;
                case "1025"://主队角球
                    mathchStatisInfo.setHome_corner(mathchStatisInfo.getHome_corner() + 1);
                    break;
                case "1050"://取消主队角球
                    mathchStatisInfo.setHome_corner(mathchStatisInfo.getHome_corner() - 1);
                    break;
                case "1032":  //主队红牌
                    mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() + 1);
                    break;
                case "1047"://取消主队红牌
                    mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() - 1);
                    break;
                case "1034": //主队黄牌
                    mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() + 1);
                    break;
                case "1048": //取消主队红牌
                    mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() - 1);
                    break;
                case "1045"://主队两黄变一红
                    mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() + 1);
                    mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() + 1);
                    break;
                case "1046"://取消两黄变一红
                    mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() - 1);
                    mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() - 1);
                    break;
                case "1026"://主队危险进攻
                    mathchStatisInfo.setHome_danger(mathchStatisInfo.getHome_danger() + 1);
                    break;
                case "1040"://主队射偏
                    mathchStatisInfo.setHome_shoot_miss(mathchStatisInfo.getHome_shoot_miss() + 1);
                    break;
                case "1041"://主队中门框事件
                    mathchStatisInfo.setHome_shoot_miss(mathchStatisInfo.getHome_shoot_miss() + 1);
                    break;
                case "1039"://主队射正
                    mathchStatisInfo.setHome_shoot_correct(mathchStatisInfo.getHome_shoot_correct() + 1);
                    break;
                case "1043"://主队越位
                    mathchStatisInfo.setHome_away(mathchStatisInfo.getHome_away() + 1);
                    break;
                case "1027"://主队任意球
                    mathchStatisInfo.setHome_free_kick(mathchStatisInfo.getHome_free_kick() + 1);
                    break;
                case "1042": //主队犯规
                    mathchStatisInfo.setHome_foul(mathchStatisInfo.getHome_foul() + 1);
                    break;
                case "1054": //主队界外球
                    mathchStatisInfo.setHome_lineOut(mathchStatisInfo.getHome_lineOut() + 1);
                    break;
                case "2053": //可队进球
                    mathchStatisInfo.setGuest_score(mathchStatisInfo.getGuest_score() + 1);
                    mathchStatisInfo.setGuest_shoot_correct(mathchStatisInfo.getGuest_shoot_correct() + 1);
                    break;
                case "2054"://取消可队进球
                    mathchStatisInfo.setGuest_score(mathchStatisInfo.getGuest_score() - 1);
                    mathchStatisInfo.setGuest_shoot_correct(mathchStatisInfo.getGuest_shoot_correct() - 1);
                    break;
                case "2049"://客队角球
                    mathchStatisInfo.setGuest_corner(mathchStatisInfo.getGuest_corner() + 1);
                    break;
                case "2074"://取消客队角球
                    mathchStatisInfo.setGuest_corner(mathchStatisInfo.getGuest_corner() - 1);
                    break;
                case "2056":  //ke队红牌
                    mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() + 1);
                    break;
                case "2071"://取消ke队红牌
                    mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() - 1);
                    break;
                case "2058": //客队黄牌
                    mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() + 1);
                    break;
                case "2072": //qu xiao ke dui huang pai
                    mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() - 1);
                    break;
                case "2069"://2黄1红
                    mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() + 1);
                    mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() + 1);
                    break;
                case "2070"://取消2黄1红
                    mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() - 1);
                    mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() - 1);
                    break;
                case "2050"://危险
                    mathchStatisInfo.setGuest_danger(mathchStatisInfo.getGuest_danger() + 1);
                    break;
                case "2064"://射偏
                    mathchStatisInfo.setGuest_shoot_miss(mathchStatisInfo.getGuest_shoot_miss() + 1);
                    break;
                case "2065"://射偏
                    mathchStatisInfo.setGuest_shoot_miss(mathchStatisInfo.getGuest_shoot_miss() + 1);
                    break;
                case "2063"://射正
                    mathchStatisInfo.setGuest_shoot_correct(mathchStatisInfo.getGuest_shoot_correct() + 1);
                    break;
                case "2067":
                    mathchStatisInfo.setGuest_away(mathchStatisInfo.getGuest_away() + 1);
                    break;
                case "2051":
                    mathchStatisInfo.setGuest_free_kick(mathchStatisInfo.getGuest_free_kick() + 1);
                    break;
                case "1"://半场结束获取半场比分
                    mathchStatisInfo.setHome_half_score(Integer.parseInt(m.getHomeScore()));
                    mathchStatisInfo.setGuest_half_score(Integer.parseInt(m.getGuestScore()));
                    break;
                case "2066": //客队犯规
                    mathchStatisInfo.setGuest_foul(mathchStatisInfo.getGuest_foul() + 1);
                    break;
                case "2078": //客队界外球
                    mathchStatisInfo.setGuest_lineOut(mathchStatisInfo.getGuest_lineOut() + 1);
                    break;
                case "1024": //主队进攻
                    mathchStatisInfo.setHome_attack(mathchStatisInfo.getHome_attack() + 1);
                    break;
                case "2048": //主队进攻
                    mathchStatisInfo.setGuest_attack(mathchStatisInfo.getGuest_attack() + 1);
                    break;
                default:
                    break;
            }
        }

        mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());
        mathchStatisInfo.setGuest_shoot_door(mathchStatisInfo.getGuest_shoot_correct() + mathchStatisInfo.getGuest_shoot_miss());
        mathchStatisInfo.setHome_rescue(mathchStatisInfo.getGuest_shoot_correct() - mathchStatisInfo.getGuest_score());
        mathchStatisInfo.setGuest_rescue(mathchStatisInfo.getHome_shoot_correct() - mathchStatisInfo.getHome_score());


        eventMatchTimeLiveList = new ArrayList<>();

        trendChartList = new ArrayList<>();

        if (matchLive == null) {
            return;
        }
        Iterator<MatchTextLiveBean> iterator1 = matchLive.iterator();
        while (iterator1.hasNext()) {
            MatchTextLiveBean bean1 = iterator1.next();
            if (bean1.getCode().equals(SCORE) || bean1.getCode().equals(SCORE1) || bean1.getCode().equals(DIANQIU) || bean1.getCode().equals(DIANQIU1) ||
                    bean1.getCode().equals(RED_CARD) || bean1.getCode().equals(RED_CARD1) ||
                    bean1.getCode().equals(YTORED) || bean1.getCode().equals(YTORED1) ||
                    bean1.getCode().equals(YELLOW_CARD) || bean1.getCode().equals(YELLOW_CARD1) ||
                    bean1.getCode().equals(CORNER) || bean1.getCode().equals(CORNER1) ||
                    bean1.getCode().equals(SUBSTITUTION) || bean1.getCode().equals(SUBSTITUTION1) ||
                    (HALFTIME.equals(bean1.getState()) && "1".equals(bean1.getCode()))) {     //把中场结束的状态也加进来  注意state
                String place = bean1.getMsgPlace();
                if (place.equals("2")) {//客队
                    place = "0";//isHome=0客队
                }

                MatchTimeLiveBean timeLiveBean = null;

                if (HALFTIME.equals(bean1.getState()) && "1".equals(bean1.getCode())) {
                    timeLiveBean = new MatchTimeLiveBean(bean1.getTime() + "", bean1.getCode(), bean1.getHomeScore() + " : " + bean1.getGuestScore(), bean1.getMsgId(), bean1.getState(), bean1.getPlayInfo(), bean1.getEnNum(), 0);
                } else {
                    timeLiveBean = new MatchTimeLiveBean(bean1.getTime() + "", bean1.getCode(), place, bean1.getMsgId(), bean1.getState(), bean1.getPlayInfo(), bean1.getEnNum(), 0);
                }
                eventMatchTimeLiveList.add(timeLiveBean);
            }


            if (bean1.getCode().equals(SCORE) || bean1.getCode().equals(SCORE1) || bean1.getCode().equals(CORNER) ||
                    bean1.getCode().equals(CORNER1) || bean1.getCode().equals(SHOOT) || bean1.getCode().equals(SHOOT1) ||
                    bean1.getCode().equals(SHOOTASIDE) || bean1.getCode().equals(SHOOTASIDE2) || bean1.getCode().equals(SHOOTASIDE1) || bean1.getCode().equals(SHOOTASIDE12) ||
                    bean1.getCode().equals(DANGERATTACK) || bean1.getCode().equals(DANGERATTACK1) || bean1.getCode().equals(ATTACK) || bean1.getCode().equals(ATTACK1)) {

                trendChartList.add(bean1);
            }


        }

        for (MatchTextLiveBean bean : matchLive) {
            Iterator<MatchTimeLiveBean> iterator2 = eventMatchTimeLiveList.iterator();
            while (iterator2.hasNext()) {
                MatchTimeLiveBean bean2 = iterator2.next();


                if (bean2.getEnNum().equals(bean.getCancelEnNum())) {
                    iterator2.remove();//去掉已经取消的
                }
            }

            //除去换人的playInfo是跟换人消息一起的,其余playInfo都是根据code=261来获取的
            if ("261".equals(bean.getCode())) {
                while (iterator2.hasNext()) {
                    MatchTimeLiveBean bean2 = iterator2.next();
                    if (bean2.getEnNum().equals(bean.getEventReNum())) {
                        bean2.setPlayInfo(bean.getPlayInfo());
                    }
                }
            }


            //统计走势图数据
            Iterator<MatchTextLiveBean> trendIterator = trendChartList.iterator();
            while (trendIterator.hasNext()) {
                MatchTextLiveBean m = trendIterator.next();
                if (m.getEnNum().equals(bean.getCancelEnNum())) {
                    trendIterator.remove();
                }

            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (mReloadTimer != null) {
            mReloadTimer.cancel();
            mReloadTimer.purge();
        }

        closePollingGifCount();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onTextResult(String text) {
        String type = "";
        try {
            JSONObject jsonObject = new JSONObject(text);
            type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!"".equals(type)) {
            Message msg = Message.obtain();
            msg.obj = text;
            msg.arg1 = Integer.parseInt(type);

            mSocketHandler.sendMessage(msg);
        }
    }

    @Override
    protected void onConnectFail() {

    }

    @Override
    protected void onDisconnected() {

    }

    @Override
    protected void onConnected() {

    }

    /**
     * 直播时间推送，更新比赛即时时间和时间轴、文字直播
     */
    Handler mSocketHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //时间推送
            if (msg.arg1 == 7) {
                String ws_json = (String) msg.obj;
                WebSocketStadiumKeepTime webSocketStadiumKeepTime = null;
                try {
                    webSocketStadiumKeepTime = JSON.parseObject(ws_json, WebSocketStadiumKeepTime.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    webSocketStadiumKeepTime = JSON.parseObject(ws_json, WebSocketStadiumKeepTime.class);
                }

                L.d(TAG, "=======直播時間=====" + StadiumUtils.convertStringToInt(webSocketStadiumKeepTime.getData().get("time")));

                updatePushKeepTime(webSocketStadiumKeepTime);

            } else if (msg.arg1 == 6) { //直播事件

                String ws_json = (String) msg.obj;
                WebSocketStadiumLiveTextEvent webSocketStadiumLiveTextEvent = null;
                try {
                    webSocketStadiumLiveTextEvent = JSON.parseObject(ws_json, WebSocketStadiumLiveTextEvent.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    webSocketStadiumLiveTextEvent = JSON.parseObject(ws_json, WebSocketStadiumLiveTextEvent.class);
                }

                //  L.d(TAG,ws_json);
                // L.d(TAG, "===直播事件===" + "msgId=" + webSocketStadiumLiveTextEvent.getData().get("msgId") + ",,,時間" + StadiumUtils.convertStringToInt(webSocketStadiumLiveTextEvent.getData().get("time")) + ",,,msgText=" + webSocketStadiumLiveTextEvent.getData().get("msgText"));
                L.d(TAG, "===事件===" + "msgId=" + webSocketStadiumLiveTextEvent.getData().get("msgId") + ",,,時間" + StadiumUtils.convertStringToInt(webSocketStadiumLiveTextEvent.getData().get("time")) + ",,,statw=" + webSocketStadiumLiveTextEvent.getData().get("state") + "playe=" + webSocketStadiumLiveTextEvent.getData().get("playInfo"));


                Map<String, String> data = webSocketStadiumLiveTextEvent.getData();
                MatchTextLiveBean currMatchTextLiveBean = new MatchTextLiveBean(data.get("code"), data.get("msgId"), data.get("msgPlace"), data.get("showId"), data.get("fontStyle"), data.get("time"), data.get("msgText"), data.get("cancelEnNum"), data.get("enNum"), data.get("state"), data.get("homeScore"), data.get("guestScore"), data.get("playInfo"), data.get("eventReNum"));
                if (currMatchTextLiveBean != null) {
                    isLostMsgId(currMatchTextLiveBean.getMsgId());
                    allMatchLiveMsgId.add(0, Integer.parseInt(currMatchTextLiveBean.getMsgId()));
                    updatePushData(currMatchTextLiveBean);
                }
            }
        }
    };

    /**
     * 比赛数据的推送
     *
     * @param matchTextLiveBean
     */
    private synchronized void updatePushData(MatchTextLiveBean matchTextLiveBean) {

        liveTextTime = StadiumUtils.convertStringToInt(matchTextLiveBean.getTime());

        if (NOTOPEN.equals(matchTextLiveBean.getState())) { //未开
            mDetailsRollballFragment.setLiveTime(mContext.getResources().getString(R.string.not_start_txt));

            isMatchStart = true;
        } else if (FIRSTHALF.equals(matchTextLiveBean.getState()) && StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) <= 45) {
            if (liveTextTime >= matchKeepTime) {
                mDetailsRollballFragment.setLiveTime(liveTextTime + "");
            } else {
                if (matchKeepTime > 45) {
                    mDetailsRollballFragment.setLiveTime("45+");
                } else {
                    mDetailsRollballFragment.setLiveTime(matchKeepTime + "");
                }
            }
            isMatchStart = true;
        } else if (FIRSTHALF.equals(matchTextLiveBean.getState()) && StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 45) {
            mDetailsRollballFragment.setLiveTime("45+");  //上半场45+
            isMatchStart = false;
        } else if (HALFTIME.equals(matchTextLiveBean.getState())) {  //中场
            mDetailsRollballFragment.setLiveTime(mContext.getResources().getString(R.string.pause_txt));
            isMatchStart = false;
        } else if (SECONDHALF.equals(matchTextLiveBean.getState()) && StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) <= 90) {
            if (liveTextTime >= matchKeepTime) {
                mDetailsRollballFragment.setLiveTime(liveTextTime + "");
            } else {
                if (matchKeepTime > 90) {
                    mDetailsRollballFragment.setLiveTime("90+");
                } else {
                    mDetailsRollballFragment.setLiveTime(matchKeepTime + "");
                }
            }
            isMatchStart = true;
        } else if (SECONDHALF.equals(matchTextLiveBean.getState()) && StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 90) {
            mDetailsRollballFragment.setLiveTime("90+");
            isMatchStart = false;
        }

        switch (matchTextLiveBean.getCode()) {
            case "0":
            case "10":
            case "11":  //开始上半场，开球：
                break;
            case "1"://上半场结束
                isMatchStart = false;
                mStatisticsFragment.addFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());


                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                break;
            case "12":
            case "13":  //下半场开始
                break;
            case "3": //结束下半场
                //socket关闭
                // 获取上半场的走势图数据
                setScoreClolor(getApplicationContext().getResources().getColor(R.color.score));

                mStatisticsFragment.addFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());


                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson("-1");// 刷新统计

                closeWebSocket();

                break;

            case "1029": //主队进球

                if ("".equals(matchTextLiveBean.getHomeScore()) || "".equals(matchTextLiveBean.getGuestScore())) {
                    mathchStatisInfo.setHome_score(mathchStatisInfo.getHome_score() + 1);
                } else {
                    mathchStatisInfo.setHome_score(Integer.parseInt(matchTextLiveBean.getHomeScore()));
                    mathchStatisInfo.setGuest_score(Integer.parseInt(matchTextLiveBean.getGuestScore()));
                }
                mathchStatisInfo.setHome_shoot_correct(mathchStatisInfo.getHome_shoot_correct() + 1);
                mathchStatisInfo.setGuest_rescue(mathchStatisInfo.getHome_shoot_correct() - mathchStatisInfo.getHome_score());
                setScoreText(mathchStatisInfo.getHome_score() + " : " + mathchStatisInfo.getGuest_score());

                head_score.setText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score());

                mStatisticsFragment.addFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                mStatisticsFragment.addTrendChartEvent(matchTextLiveBean);

                break;
            case "1030"://取消主队进球

                if (mathchStatisInfo.getHome_score() > 0) {
                    if ("".equals(matchTextLiveBean.getHomeScore()) || "".equals(matchTextLiveBean.getGuestScore())) {
                        mathchStatisInfo.setHome_score(mathchStatisInfo.getHome_score() - 1);
                    } else {

                        mathchStatisInfo.setHome_score(Integer.parseInt(matchTextLiveBean.getHomeScore()));
                        mathchStatisInfo.setGuest_score(Integer.parseInt(matchTextLiveBean.getGuestScore()));

                    }
                }
                if (mathchStatisInfo.getHome_shoot_correct() > 0) {
                    mathchStatisInfo.setHome_shoot_correct(mathchStatisInfo.getHome_shoot_correct() - 1);
                }
                mathchStatisInfo.setGuest_rescue(mathchStatisInfo.getHome_shoot_correct() - mathchStatisInfo.getHome_score());
                setScoreText(mathchStatisInfo.getHome_score() + " : " + mathchStatisInfo.getGuest_score());

                head_score.setText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score());

                mStatisticsFragment.cancelFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                mStatisticsFragment.cancelTrendChartEvent(matchTextLiveBean);


                break;
            case "2053"://客队进球


                if ("".equals(matchTextLiveBean.getHomeScore()) || "".equals(matchTextLiveBean.getGuestScore())) {
                    mathchStatisInfo.setGuest_score(mathchStatisInfo.getGuest_score() + 1);
                } else {
                    mathchStatisInfo.setHome_score(Integer.parseInt(matchTextLiveBean.getHomeScore()));
                    mathchStatisInfo.setGuest_score(Integer.parseInt(matchTextLiveBean.getGuestScore()));
                }

                mathchStatisInfo.setGuest_shoot_correct(mathchStatisInfo.getGuest_shoot_correct() + 1);
                mathchStatisInfo.setHome_rescue(mathchStatisInfo.getGuest_shoot_correct() - mathchStatisInfo.getGuest_score());
                setScoreText(mathchStatisInfo.getHome_score() + " : " + mathchStatisInfo.getGuest_score());

                head_score.setText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score());

                mStatisticsFragment.addFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                mStatisticsFragment.addTrendChartEvent(matchTextLiveBean);


                break;

            case "2054"://取消客队进球
                if (mathchStatisInfo.getGuest_score() > 0) {
                    if ("".equals(matchTextLiveBean.getHomeScore()) || "".equals(matchTextLiveBean.getGuestScore())) {
                        mathchStatisInfo.setGuest_score(mathchStatisInfo.getGuest_score() - 1);
                    } else {
                        mathchStatisInfo.setHome_score(Integer.parseInt(matchTextLiveBean.getHomeScore()));
                        mathchStatisInfo.setGuest_score(Integer.parseInt(matchTextLiveBean.getGuestScore()));

                    }
                }
                if (mathchStatisInfo.getGuest_shoot_correct() > 0) {
                    mathchStatisInfo.setGuest_shoot_correct(mathchStatisInfo.getGuest_shoot_correct() - 1);
                }

                mathchStatisInfo.setHome_rescue(mathchStatisInfo.getGuest_shoot_correct() - mathchStatisInfo.getGuest_score());
                setScoreText(mathchStatisInfo.getHome_score() + " : " + mathchStatisInfo.getGuest_score());

                head_score.setText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score());


                mStatisticsFragment.cancelFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                mStatisticsFragment.cancelTrendChartEvent(matchTextLiveBean);


                break;

            case "1025": //主队角球
                mathchStatisInfo.setHome_corner(mathchStatisInfo.getHome_corner() + 1);

                //x时间轴
                //事件放入并展示


                mStatisticsFragment.addFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());

                mStatisticsFragment.addTrendChartEvent(matchTextLiveBean);

                break;

            case "1050": //取消主队角球

                if (mathchStatisInfo.getHome_corner() > 0) {
                    mathchStatisInfo.setHome_corner(mathchStatisInfo.getHome_corner() - 1);
                }

                //取消进球时间，重绘

                mStatisticsFragment.cancelFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());

                mStatisticsFragment.cancelFootBallEvent(matchTextLiveBean);


                break;

            case "2049": //客队角球
                mathchStatisInfo.setGuest_corner(mathchStatisInfo.getGuest_corner() + 1);
                mStatisticsFragment.addFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());
                mStatisticsFragment.addTrendChartEvent(matchTextLiveBean);

                break;

            case "2074": //取消客队角球
                if (mathchStatisInfo.getGuest_corner() > 0) {
                    mathchStatisInfo.setGuest_corner(mathchStatisInfo.getGuest_corner() - 1);
                }

                mStatisticsFragment.cancelFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());
                mStatisticsFragment.cancelTrendChartEvent(matchTextLiveBean);

                break;

            case "1031"://主队点球
                mStatisticsFragment.addFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "2055"://客队点球
                mStatisticsFragment.addFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "1034":   //主队黄牌
                mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() + 1);
                mStatisticsFragment.addFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "1048": //取消 主队黄牌
                if (mathchStatisInfo.getHome_yc() > 0) {
                    mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() - 1);
                }


                mStatisticsFragment.cancelFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "1045": //主队两黄变一红

                mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() + 1);
                mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() + 1);
                mStatisticsFragment.addFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "1046": //取消主队黄牌过度到红牌
                if (mathchStatisInfo.getHome_yc() > 0 && mathchStatisInfo.getHome_rc() > 0) {
                    mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() - 1);
                    mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() - 1);
                }

                mStatisticsFragment.cancelFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());

                break;


            case "2058":  //客队黄牌
                mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() + 1);

                mStatisticsFragment.addFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "2072":  //取消客队黄牌
                if (mathchStatisInfo.getGuest_yc() > 0) {
                    mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() - 1);
                }
                mStatisticsFragment.cancelFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());
                break;


            case "2069": //客队两黄变一红
                mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() + 1);
                mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() + 1);
                mStatisticsFragment.addFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "2070": //取消客队两黄变一红

                if (mathchStatisInfo.getGuest_yc() > 0 && mathchStatisInfo.getGuest_rc() > 0) {
                    mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() - 1);
                    mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() - 1);
                }

                mStatisticsFragment.cancelFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "1032":  //主队红牌
                mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() + 1);
                break;
            case "1047": //取消主队红牌
                if (mathchStatisInfo.getHome_rc() > 0) {
                    mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() - 1);
                }

                break;

            case "2056":  //客队红牌
                mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() + 1);
                mStatisticsFragment.addFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "2071":  //取消客队红牌
                if (mathchStatisInfo.getGuest_rc() > 0) {
                    mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() - 1);
                }

                mStatisticsFragment.cancelFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "1026":    //主队危险进攻
                mathchStatisInfo.setHome_danger(mathchStatisInfo.getHome_danger() + 1);
                mStatisticsFragment.addTrendChartEvent(matchTextLiveBean);

                break;

            case "2050":  //客队危进攻
                mathchStatisInfo.setGuest_danger(mathchStatisInfo.getGuest_danger() + 1);
                mStatisticsFragment.addTrendChartEvent(matchTextLiveBean);

                break;

            case "1039":    //主队射正球门

                mathchStatisInfo.setHome_shoot_correct(mathchStatisInfo.getHome_shoot_correct() + 1);
                mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());


                //客队扑救=主队射正-主队比分
                mathchStatisInfo.setGuest_rescue(mathchStatisInfo.getHome_shoot_correct() - mathchStatisInfo.getHome_score());
                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计
                mStatisticsFragment.addTrendChartEvent(matchTextLiveBean);
                break;
            case "1024": //主队进攻
                mathchStatisInfo.setHome_attack(mathchStatisInfo.getHome_attack() + 1);
                mStatisticsFragment.addTrendChartEvent(matchTextLiveBean);
                break;
            case "2048": //客队进攻
                mathchStatisInfo.setGuest_attack(mathchStatisInfo.getGuest_attack() + 1);
                mStatisticsFragment.addTrendChartEvent(matchTextLiveBean);

                break;

            case "2063":  //客队射正球门
                mathchStatisInfo.setGuest_shoot_correct(mathchStatisInfo.getGuest_shoot_correct() + 1);
                mathchStatisInfo.setGuest_shoot_door(mathchStatisInfo.getGuest_shoot_correct() + mathchStatisInfo.getGuest_shoot_miss());
                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计
                mStatisticsFragment.addTrendChartEvent(matchTextLiveBean);

                break;

            case "1040":    //主队射偏球门
                mathchStatisInfo.setHome_shoot_miss(mathchStatisInfo.getHome_shoot_miss() + 1);
                mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计
                mStatisticsFragment.addTrendChartEvent(matchTextLiveBean);

                //如果有变动就刷新统计数据
                break;
            case "1041":    //主队射偏球门
                mathchStatisInfo.setHome_shoot_miss(mathchStatisInfo.getHome_shoot_miss() + 1);
                mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                mStatisticsFragment.addTrendChartEvent(matchTextLiveBean);
                //如果有变动就刷新统计数据
                break;


            case "2064":  //客队射偏球门
                mathchStatisInfo.setGuest_shoot_miss(mathchStatisInfo.getGuest_shoot_miss() + 1);
                mathchStatisInfo.setGuest_shoot_door(mathchStatisInfo.getGuest_shoot_correct() + mathchStatisInfo.getGuest_shoot_miss());


                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                mStatisticsFragment.addTrendChartEvent(matchTextLiveBean);


                //如果有变动就刷新统计数据
                break;
            case "2065":  //客队门框
                mathchStatisInfo.setGuest_shoot_miss(mathchStatisInfo.getGuest_shoot_miss() + 1);
                mathchStatisInfo.setGuest_shoot_door(mathchStatisInfo.getGuest_shoot_correct() + mathchStatisInfo.getGuest_shoot_miss());
                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                mStatisticsFragment.addTrendChartEvent(matchTextLiveBean);
                //如果有变动就刷新统计数据
                break;


            case "1055"://主队换人
                mStatisticsFragment.addFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "2079": //客队换人
                mStatisticsFragment.addFootBallEvent(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());
                break;
            case "1043"://主队越位
                mathchStatisInfo.setHome_away(mathchStatisInfo.getHome_away() + 1);
                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计
                //如果有变动就刷新统计数据
                break;

            case "1027": //主队任意球
                mathchStatisInfo.setHome_free_kick(mathchStatisInfo.getHome_free_kick() + 1);
                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计


                //如果有变动就刷新统计数据
                break;

            case "2067":  //客队越位
                mathchStatisInfo.setGuest_away(mathchStatisInfo.getGuest_away() + 1);
                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                //如果有变动就刷新统计数据
                break;
            case "2051"://客队任意球
                mathchStatisInfo.setGuest_free_kick(mathchStatisInfo.getGuest_free_kick() + 1);
                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                break;

            case "1042"://主队犯规
                mathchStatisInfo.setHome_foul(mathchStatisInfo.getHome_foul() + 1);
                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                break;
            case "1054"://主队界外球
                mathchStatisInfo.setHome_lineOut(mathchStatisInfo.getHome_lineOut() + 1);
                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                break;
            case "2066"://客队犯规
                mathchStatisInfo.setGuest_foul(mathchStatisInfo.getGuest_foul() + 1);
                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                break;
            case "2078"://客队界外球
                mathchStatisInfo.setGuest_lineOut(mathchStatisInfo.getGuest_lineOut() + 1);
                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                break;
            case "256": //取消（不显示，指定消息取消）
            case "257"://清除（多个指定消息不显示）
                if (matchTextLiveBean.getCancelEnNum() != null && !"".equals(matchTextLiveBean.getCancelEnNum())) {
                    List<String> list = Arrays.asList(matchTextLiveBean.getCancelEnNum().split(":"));
                    if (list.size() > 0) {
                        for (MatchTextLiveBean m : matchLive) {
                            if (matchTextLiveBean.getEnNum().equals(m.getEnNum())) {
                                m.setShowId("1");
                            }
                            for (String ennum : list) {
                                if (ennum.equals(m.getEnNum())) {
                                    m.setShowId("1");
                                }
                            }
                        }
                    }
                }

                break;

            case "261":
                mStatisticsFragment.setPlayInfo(matchTextLiveBean);
                mStatisticsFragment.updateRecycleView(matchTextLiveBean.getState());
                break;
            default:
                break;
        }

        if ("0".equals(matchTextLiveBean.getShowId()) && !"1".equals(matchTextLiveBean.getShowId())) {

            matchLive.add(0, matchTextLiveBean);
            Collections.sort(matchLive, new FootballLiveTextComparator()); //排序
            mDetailsRollballFragment.setLiveText(matchLive.get(0).getMsgText());
            mDetailsRollballFragment.setLiveTextDetails(matchLive);


            //liveTextFragment.getTimeAdapter().notifyDataSetChanged();
        }

        //自己推送一条结束消息     -1 完场
        if (MATCHFINISH.equals(matchTextLiveBean.getState())) {
            matchLive.add(0, new MatchTextLiveBean("", "", "0", "0", "4", "99999999", mContext.getResources().getString(R.string.matchFinished_txt), "", "", "0", "", "", "", ""));
            mDetailsRollballFragment.setLiveText(matchLive.get(0).getMsgText());
            mDetailsRollballFragment.setLiveTextDetails(matchLive);
        }
    }


    /***
     * 检查是否推送消息有漏
     *
     * @param currMsgId
     */
    private void isLostMsgId(String currMsgId) {
        if (allMatchLiveMsgId == null) {
            return;
        }
        synchronized (allMatchLiveMsgId) {
            if (allMatchLiveMsgId != null && allMatchLiveMsgId.size() > 0) {
                Collections.sort(allMatchLiveMsgId, Collections.reverseOrder());

                if ((Integer.parseInt(currMsgId) - allMatchLiveMsgId.get(0) >= 2)) {
                    L.i("1028", "------" + currMsgId + "---" + allMatchLiveMsgId.get(0));
                    isRequestMsgIdRepeat(String.valueOf(allMatchLiveMsgId.get(0)), currMsgId);
                } else {
                    for (int i = 0; i < allMatchLiveMsgId.size() - 1; i++) {
                        boolean flag = false;
                        if ((allMatchLiveMsgId.get(i) - allMatchLiveMsgId.get(i + 1)) >= 2) {
                            L.i("1028", allMatchLiveMsgId.get(i) + "---" + allMatchLiveMsgId.get(i + 1));
                            isRequestMsgIdRepeat(String.valueOf(allMatchLiveMsgId.get(i + 1)), String.valueOf(allMatchLiveMsgId.get(i)));
                            flag = true;
                        }
                        if (flag) {
                            break;
                        }
                    }
                }
            }
        }
    }


    /***
     * 推送有漏消息请求
     *
     * @param preMsgId
     * @param msgId
     */
    private void isRequestMsgIdRepeat(String preMsgId, String msgId) {

        Map<String, String> msgIdParams = new HashMap<>();
        if (mContext == null) {
            return;
        }
        msgIdParams.put("thirdId", mThirdId);
        msgIdParams.put("startTextId", String.valueOf(Integer.parseInt(preMsgId) + 1));
        msgIdParams.put("endTextId", String.valueOf(Integer.parseInt(msgId) - 1));


        String url = BaseURLs.URL_FOOTBALL_IS_LOST_MSG_INFO;

        VolleyContentFast.requestJsonByPost(url, msgIdParams, new VolleyContentFast.ResponseSuccessListener<PreLiveText>() {
            @Override
            public void onResponse(PreLiveText jsonObject) {
                // 访问成功
                List<MatchTextLiveBean> liveTextList;
                L.i("1028", "请求漏消息");
                if (jsonObject != null && "200".equals(jsonObject.getResult())) {
                    liveTextList = new ArrayList<MatchTextLiveBean>();
                    liveTextList = jsonObject.getLive();// 获取当前页的下一页数据
                    L.i("1028", "漏消息个数：" + liveTextList.size());

                    if (liveTextList.size() > 0) {
                        for (MatchTextLiveBean m : liveTextList) {
                            L.i("1028", "漏消息msgId=" + m.getMsgId());
                            boolean isRepeat = true;
                            for (Integer matchTextLiveBean : allMatchLiveMsgId) {
                                if (m.getMsgId().equals(matchTextLiveBean)) {
                                    isRepeat = false;
                                }
                            }
                            if (isRepeat) {
                                allMatchLiveMsgId.add(0, Integer.parseInt(m.getMsgId()));

                                updatePushData(m);
                            }
                        }
                    }
                } else {
                    L.i("1028", "请求漏消息失败");

                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        }, PreLiveText.class);
    }

    /**
     * 比赛时间的推送
     *
     * @param webSocketStadiumKeepTime
     */
    private void updatePushKeepTime(WebSocketStadiumKeepTime webSocketStadiumKeepTime) {
        Map<String, String> data = webSocketStadiumKeepTime.getData();
        mKeepTime = data.get("time");
        matchKeepTime = StadiumUtils.convertStringToInt(data.get("time"));
        String state = data.get("state");//比赛的状态
        if (Integer.parseInt(mKeepTime) > (45 * 60 * 1000)) {
            if (state.equals(FIRSTHALF) || state.equals(HALFTIME)) {//上半场补时中场时间轴不变
                mKeepTime = 45 * 60 * 1000 + "";//时间继续赋值为45分钟
            }
        }

        if (isMatchStart) {
            if (liveTextTime >= matchKeepTime) {
            } else {
                if (FIRSTHALF.equals(state) && matchKeepTime <= 45) {
                } else if (FIRSTHALF.equals(state) && matchKeepTime > 45) {

                } else if (SECONDHALF.equals(state) && matchKeepTime <= 90) {

                } else if (SECONDHALF.equals(state) && matchKeepTime > 90) {
                }
            }


        }
    }


    /**
     * 根据选择语言，改变推送接口语言环境
     *
     * @return
     */
    private String appendLanguage() {
        String lang = "zh";//默认中文
        if (MyApp.isLanguage.equals("rCN")) {
            // 如果是中文简体的语言环境
            lang = BaseURLs.LANGUAGE_SWITCHING_CN;
        } else if (MyApp.isLanguage.equals("rTW")) {
            // 如果是中文繁体的语言环境
            lang = BaseURLs.LANGUAGE_SWITCHING_TW;
        } else if (MyApp.isLanguage.equals("rEN")) {
            // 如果是英文环境
            lang = BaseURLs.LANGUAGE_SWITCHING_EN;
        } else if (MyApp.isLanguage.equals("rKO")) {
            // 如果是韩语环境
            lang = BaseURLs.LANGUAGE_SWITCHING_KO;
        } else if (MyApp.isLanguage.equals("rID")) {
            // 如果是印尼语
            lang = BaseURLs.LANGUAGE_SWITCHING_ID;
        } else if (MyApp.isLanguage.equals("rTH")) {
            // 如果是泰语
            lang = BaseURLs.LANGUAGE_SWITCHING_TH;
        } else if (MyApp.isLanguage.equals("rVI")) {
            // 如果是越南语
            lang = BaseURLs.LANGUAGE_SWITCHING_VI;
        }

        return lang.trim();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case STARTLOADING:// 正在加载中
                    //  mRefreshLayout.setRefreshing(false);
                    isRquestSuccess = false;
                    fl_odds_loading.setVisibility(View.VISIBLE);
                    fl_odds_net_error_details.setVisibility(View.GONE);
                    mViewPager.setVisibility(View.GONE);
                    mRefreshLayout.setEnabled(false);
                    break;
                case SUCCESS:// 加载成功

                    isRquestSuccess = true;
                    fl_odds_loading.setVisibility(View.GONE);
                    fl_odds_net_error_details.setVisibility(View.GONE);
                    mViewPager.setVisibility(View.VISIBLE);
                    mRefreshLayout.setEnabled(true);

                    break;
                case ERROR:// 加载失败
                    if (isInitedViewPager) {
                        Toast.makeText(getApplicationContext(), R.string.exp_net_status_txt, Toast.LENGTH_SHORT).show();
                    } else {

                        L.d("10299", "错误");
                        // mRefreshLayout.setVisibility(View.GONE);

                        isRquestSuccess = false;
                        fl_odds_loading.setVisibility(View.GONE);
                        fl_odds_net_error_details.setVisibility(View.VISIBLE);
                        mViewPager.setVisibility(View.GONE);
                        mRefreshLayout.setEnabled(false);
                    }

                    // isHttpData = false;
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:  //返回
                MobclickAgent.onEvent(mContext, "Football_MatchDataInfo_Exit");
                eventBusPost();
                MyApp.getContext().sendBroadcast(new Intent("CLOSE_INPUT_ACTIVITY"));
                // setResult(Activity.RESULT_OK);
                finish();
                break;

            case R.id.iv_setting:  //关注、分享
                popWindow(iv_setting);
                break;
            case R.id.reLoading_details:
                mHandler.sendEmptyMessage(STARTLOADING);

                loadData();
                break;
            case R.id.btn_showGif:
                if (NetworkUtils.isConnected(getApplicationContext())) {
                    int type = com.hhly.mlottery.util.NetworkUtils.getCurNetworkType(getApplicationContext());
                    if (type == 1) {
                        L.d("zxcvbn", "WIFI");
                        Intent intent = new Intent(FootballMatchDetailActivity.this, PlayHighLightActivity.class);
                        intent.putExtra("thirdId", mThirdId);
                        intent.putExtra("match_type", MATCH_TYPE);
                        startActivity(intent);
                        //wifi
                    } else if (type == 2 || type == 3 || type == 4) {//2G  3G  4G
                        L.d("zxcvbn", "移动网络-" + type + "G");
                        promptNetInfo();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.about_net_failed), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_addMultiView:
                enterMultiScreenView();
                break;

            case R.id.barrage_switch:


                if (barrage_isFocus) {
                    barrage_switch.setImageResource(R.mipmap.danmu_open);
                    barrage_isFocus = false;
                    // barrage_view.setVisibility(View.VISIBLE);
                    barrage_view.setAlpha(1);
                } else {
                    barrage_switch.setImageResource(R.mipmap.danmu_close);
                    barrage_isFocus = true;
                    // barrage_view.setVisibility(View.GONE);
                    barrage_view.setAlpha(0);
                }
                break;
            default:
                break;
        }
    }


    private void enterMultiScreenView() {
        if (PreferenceUtil.getBoolean("introduce", true)) {
            Intent intent = new Intent(FootballMatchDetailActivity.this, MultiScreenIntroduceActivity.class);
            intent.putExtra("thirdId", new MultipleByValueBean(1, mThirdId));
            startActivity(intent);
            PreferenceUtil.commitBoolean("introduce", false);
        } else {
            Intent intent = new Intent(FootballMatchDetailActivity.this, MultiScreenViewingListActivity.class);
            intent.putExtra("thirdId", new MultipleByValueBean(1, mThirdId));
            startActivity(intent);
        }
    }


    /**
     * 当前连接的网络提示
     */
    private void promptNetInfo() {
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(FootballMatchDetailActivity.this, R.style.AppThemeDialog);
            builder.setCancelable(false);// 设置对话框以外不可点击
            builder.setTitle(getApplicationContext().getResources().getString(R.string.to_update_kindly_reminder));// 提示标题
            builder.setMessage(getApplicationContext().getResources().getString(R.string.video_high_light_reminder_comment));// 提示内容
            builder.setPositiveButton(getApplicationContext().getResources().getString(R.string.video_high_light_continue_open), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(FootballMatchDetailActivity.this, PlayHighLightActivity.class);
                    intent.putExtra("thirdId", mThirdId);
                    intent.putExtra("match_type", MATCH_TYPE);
                    startActivity(intent);

                }
            });
            builder.setNegativeButton(getApplicationContext().getResources().getString(R.string.basket_analyze_dialog_cancle), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            android.support.v7.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    // 评论登录跳转
    public void talkAboutBallLoginFoot() {
        //跳转登录界面
        Intent intent1 = new Intent(mContext, LoginActivity.class);
        startActivityForResult(intent1, CyUtils.JUMP_COMMENT_QUESTCODE);
    }

    // 发表评论跳转
    public void talkAboutBallSendFoot() {
        Intent intent2 = new Intent(mContext, ChartballActivity.class);
        intent2.putExtra("thirdId", mThirdId);
        startActivityForResult(intent2, CyUtils.JUMP_COMMENT_QUESTCODE);
    }

    private void popWindow(View v) {
        final View mView = View.inflate(getApplicationContext(), R.layout.football_details, null);

        boolean isFocus = FocusUtils.isFocusId(mThirdId);

        if (isFocus) {
            ((ImageView) mView.findViewById(R.id.football_item_focus_iv)).setImageResource(R.mipmap.head_focus);
            ((TextView) mView.findViewById(R.id.football_item_focus_tv)).setText(getString(R.string.foot_details_focused));

        } else {
            ((ImageView) mView.findViewById(R.id.football_item_focus_iv)).setImageResource(R.mipmap.head_nomal);
            ((TextView) mView.findViewById(R.id.football_item_focus_tv)).setText(getString(R.string.foot_details_focus));
        }

        final PopupWindow popupWindow = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        int[] location = new int[2];
        v.getLocationOnScreen(location);

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - v.getWidth() - v.getPaddingRight(), location[1] + v.getHeight());         //  popupWindow.showAsDropDown(v,-10,0);


        (mView.findViewById(R.id.football_item_focus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "足球！", Toast.LENGTH_SHORT).show();
                MobclickAgent.onEvent(mContext, "Football_MatchDataInfo_Focus");
                popupWindow.dismiss();
                if (FocusUtils.isFocusId(mThirdId)) {
                    FocusUtils.deleteFocusId(mThirdId);
                    ((ImageView) mView.findViewById(R.id.football_item_focus_iv)).setImageResource(R.mipmap.head_nomal);
                    ((TextView) mView.findViewById(R.id.football_item_focus_tv)).setText(getString(R.string.foot_details_focus));

                } else {
                    FocusUtils.addFocusId(mThirdId);
                    ((ImageView) mView.findViewById(R.id.football_item_focus_iv)).setImageResource(R.mipmap.head_focus);
                    ((TextView) mView.findViewById(R.id.football_item_focus_tv)).setText(getString(R.string.foot_details_focused));
                }
            }
        });


        iv_share = mView.findViewById(R.id.football_item_share);
        iv_share.setVisibility(View.GONE);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "Football_MatchDataInfo_Share");
                popupWindow.dismiss();

                if (mMatchDetail == null) {
                    return;
                }

                String title = mMatchDetail.getHomeTeamInfo().getName() + " VS " + mMatchDetail.getGuestTeamInfo().getName();
                String summary = getString(R.string.share_summary_new);

                if ("0".equals(mPreStatus)) {  //赛前
                    title += " " + matchStartTime.split(" ")[1] + getString(R.string.football_analyze_details_game);
                    summary = "[" + getString(R.string.share_match_pre) + "]" + summary;
                } else if ("1".equals(mPreStatus)) {//赛中
                    title += " " + getString(R.string.share_match_live2);
                    summary = "[" + getString(R.string.share_match_live) + "]" + summary;
                } else if ("-1".equals(mPreStatus)) {//赛后
                    title += " " + getString(R.string.share_match_browse);
                    summary = "[" + getString(R.string.share_match_over) + "]" + summary;
                }

                ShareBean shareBean = new ShareBean();

                shareBean.setTitle(title != null ? title : getString(R.string.share_to_qq_app_name));
                shareBean.setSummary(summary);
                shareBean.setTarget_url(BaseURLs.URL_FOOTBALL_DETAIL_INFO_SHARE + mThirdId);
                shareBean.setImage_url(shareHomeIconUrl != null ? shareHomeIconUrl : "");
                shareBean.setCopy(BaseURLs.URL_FOOTBALL_DETAIL_INFO_SHARE + mThirdId);
                mShareFragment = ShareFragment.newInstance(shareBean);
                mShareFragment.show(getSupportFragmentManager(), "bottomShare");
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            eventBusPost();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void eventBusPost() {
        EventBus.getDefault().post(new ScoresMatchFocusEventBusEntity(currentFragmentId));
    }


    /**
     * 初始化事件监听
     */
    private void initEvent() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 5) {// 聊球界面禁用下拉刷新
                    MyApp.getContext().sendBroadcast(new Intent("CLOSE_INPUT_ACTIVITY"));
                } else {
                    if (view_red != null) {
                        view_red.setVisibility(View.GONE);
                    }
                    mRefreshLayout.setEnabled(true); //展开
                    appBarLayout.setExpanded(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //新gif倒计时countDown
        countDown = new CountDown(MILLIS_INFuture, 1000, new CountDown.CountDownCallback() {
            @Override
            public void onFinish() {
                rl_gif_notice.setVisibility(View.GONE);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                //  L.d("zxcvbn", "countdown===" + millisUntilFinished / 1000 + "秒");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initHeadView() {

        fl_head = (FrameLayout) findViewById(R.id.fl_head);

        iv_home_icon = (ImageView) findViewById(R.id.iv_home_icon);
        iv_guest_icon = (ImageView) findViewById(R.id.iv_guest_icon);
        iv_bg = (ImageView) findViewById(R.id.iv_bg);


        tv_homename = (TextView) findViewById(R.id.tv_home_name);
        tv_guestname = (TextView) findViewById(R.id.tv_guest_name);
        score = (TextView) findViewById(R.id.score);
        date = (TextView) findViewById(R.id.date);
        mMatchTypeLayout = (LinearLayout) findViewById(R.id.matchType);
        mMatchType1 = (TextView) findViewById(R.id.football_match_detail_matchtype1);
        mMatchType2 = (TextView) findViewById(R.id.football_match_detail_matchtype2);
        btn_showGif = (LinearLayout) findViewById(R.id.btn_showGif);
        btn_showGif.setOnClickListener(this);

        rl_gif_notice = (RelativeLayout) findViewById(R.id.rl_gif_notice);
        tv_addMultiView = (TextView) findViewById(R.id.tv_addMultiView);

        if (isAddMultiViewHide) {
            tv_addMultiView.setVisibility(View.GONE);
        }
    }


    private void initPreData(MatchDetail mMatchDetail) {

        // if (flag) {
        int random = new Random().nextInt(20);
        String url = baseUrl + random + ".png";
        ImageLoader.load(mContext, url, R.color.colorPrimary).into(iv_bg);

        loadImage(mMatchDetail.getHomeTeamInfo().getUrl(), iv_home_icon);
        loadImage(mMatchDetail.getGuestTeamInfo().getUrl(), iv_guest_icon);
        tv_homename.setText(mMatchDetail.getHomeTeamInfo().getName());
        tv_guestname.setText(mMatchDetail.getGuestTeamInfo().getName());


        //赛事类型

        if (mMatchDetail.getMatchType1() == null && mMatchDetail.getMatchType2() == null) {
            mMatchTypeLayout.setVisibility(View.INVISIBLE);
        } else {

            if (StringUtils.isEmpty(mMatchDetail.getMatchType1())) {
                mMatchType1.setVisibility(View.INVISIBLE);
            }

            if (StringUtils.isEmpty(mMatchDetail.getMatchType2())) {
                mMatchType2.setVisibility(View.INVISIBLE);
            }
            mMatchType1.setText(StringUtils.nullStrToEmpty(mMatchDetail.getMatchType1()));
            mMatchType2.setText(StringUtils.nullStrToEmpty(mMatchDetail.getMatchType2()));
            mMatchTypeLayout.setVisibility(View.VISIBLE);
        }

        if (mMatchDetail.getMatchInfo().getStartTime() != null) {
            String startTime = mMatchDetail.getMatchInfo().getStartTime();
            if (date == null) {
                return;
            }
            if (!StringUtils.isEmpty(startTime) && startTime.length() == 16) {
                date.setText(DateUtil.convertDateToNationHM(startTime));
            } else {
                date.setText("");//开赛时间
            }
        }
    }


    /**
     * load internet image
     *
     * @param imageUrl
     * @param imageView
     */
    private void loadImage(String imageUrl, final ImageView imageView) {
        VolleyContentFast.requestImage(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    private void setScoreText(String msg) {
        score.setText(msg);
    }

    private void setScoreClolor(int id) {
        score.setTextColor(id);
    }


    /**
     * start polling
     */
    private void pollingGifCount() {
        if (gifTimer == null) {
            gifTimer = new Timer();
            gifTimerTask = new TimerTask() {
                @Override
                public void run() {
                    getCollectionCount();
                }
            };

            gifTimer.schedule(gifTimerTask, 1000, GIFPERIOD_2);
            //gifTimer.schedule(gifTimerTask, 5000, 20000);
        }
    }

    /**
     * close polling
     */
    private void closePollingGifCount() {
        if (gifTimer != null) {
            gifTimerTask.cancel();
            gifTimer.cancel();
            gifTimer.purge();
            gifTimerTask = null;
            gifTimer = null;
        }
    }


    private void getOverMatchCollectionCount() {
        Map<String, String> map = new HashMap<>();
        map.put("matchType", MATCH_TYPE);
        map.put("thirdId", mThirdId);  //399381
        //  map.put("thirdId", mThirdId);
        VolleyContentFast.requestJsonByGet(BaseURLs.FOOTBALL_DETAIL_COLLECTION_COUNT, map, new VolleyContentFast.ResponseSuccessListener<DetailsCollectionCountBean>() {
            @Override
            public void onResponse(DetailsCollectionCountBean jsonObject) {
                if (200 == jsonObject.getResult()) {
                    if (jsonObject.getData() != 0) {
                        btn_showGif.setVisibility(View.VISIBLE);
                    } else {
                        btn_showGif.setVisibility(View.GONE);
                        // }
                    }
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                //   if (isFirstShowGif) {
                //      btn_showGif.setVisibility(View.GONE);
                //  }
            }
        }, DetailsCollectionCountBean.class);
    }

    /**
     * get gifs and videos quantity
     */
    private void getCollectionCount() {
        Map<String, String> map = new HashMap<>();
        map.put("matchType", MATCH_TYPE);
        map.put("thirdId", mThirdId);  //399381
        //  map.put("thirdId", mThirdId);
        VolleyContentFast.requestJsonByGet(BaseURLs.FOOTBALL_DETAIL_COLLECTION_COUNT, map, new VolleyContentFast.ResponseSuccessListener<DetailsCollectionCountBean>() {
            @Override
            public void onResponse(DetailsCollectionCountBean jsonObject) {
                if (200 == jsonObject.getResult()) {
                    if (jsonObject.getData() != 0) {
                        btn_showGif.setVisibility(View.VISIBLE);
                        if (isFirstShowGif) {  //第一次显示
                            L.d("zxcvbn", "第一次进入------------");
                            gifCount = jsonObject.getData();
                            isFirstShowGif = false;
                        } else {
                            L.d("zxcvbn", "第二次进入------------");
                            if (jsonObject.getData() > gifCount) { //有新的gif出現
                                L.d("zxcvbn", "有新的gif出現------------");
                                gifCount = jsonObject.getData();
                                rl_gif_notice.setVisibility(View.VISIBLE);
                                countDown.start();
                            }
                        }
                    } else {
                        L.d("zxcvbn", "没有gif------------");
                        isFirstShowGif = false;
                        gifCount = 0;
                        btn_showGif.setVisibility(View.GONE);
                        // }
                    }
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                //   if (isFirstShowGif) {
                //      btn_showGif.setVisibility(View.GONE);
                //  }
            }
        }, DetailsCollectionCountBean.class);
    }

}
