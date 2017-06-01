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
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.hhly.mlottery.bean.footballDetails.database.DataBaseBean;
import com.hhly.mlottery.bean.websocket.WebSocketStadiumKeepTime;
import com.hhly.mlottery.bean.websocket.WebSocketStadiumLiveTextEvent;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BaseUserTopics;
import com.hhly.mlottery.config.FootBallDetailTypeEnum;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.ShareFragment;
import com.hhly.mlottery.frame.chartBallFragment.ChartBallFragment;
import com.hhly.mlottery.frame.footballframe.AnalyzeParentFragment;
import com.hhly.mlottery.frame.footballframe.BettingIssueFragment;
import com.hhly.mlottery.frame.footballframe.LiveFragment;
import com.hhly.mlottery.frame.footballframe.OddsFragment;
import com.hhly.mlottery.frame.footballframe.eventbus.ScoresMatchFocusEventBusEntity;
import com.hhly.mlottery.mvptask.bowl.BowlFragment;
import com.hhly.mlottery.util.CountDown;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.FocusUtils;
import com.hhly.mlottery.util.FootballLiveTextComparator;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.NetworkUtils;
import com.hhly.mlottery.util.StadiumUtils;
import com.hhly.mlottery.util.StringUtils;
import com.hhly.mlottery.util.immersionbar.ImmersionBar;
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

import static com.hhly.mlottery.config.FootBallTypeEnum.ATTACK;
import static com.hhly.mlottery.config.FootBallTypeEnum.ATTACK1;
import static com.hhly.mlottery.config.FootBallTypeEnum.CORNER;
import static com.hhly.mlottery.config.FootBallTypeEnum.CORNER1;
import static com.hhly.mlottery.config.FootBallTypeEnum.DANGERATTACK;
import static com.hhly.mlottery.config.FootBallTypeEnum.DANGERATTACK1;
import static com.hhly.mlottery.config.FootBallTypeEnum.DIANQIU;
import static com.hhly.mlottery.config.FootBallTypeEnum.DIANQIU1;
import static com.hhly.mlottery.config.FootBallTypeEnum.FIRSTHALF;
import static com.hhly.mlottery.config.FootBallTypeEnum.HALFTIME;
import static com.hhly.mlottery.config.FootBallTypeEnum.MATCHFINISH;
import static com.hhly.mlottery.config.FootBallTypeEnum.RED_CARD;
import static com.hhly.mlottery.config.FootBallTypeEnum.RED_CARD1;
import static com.hhly.mlottery.config.FootBallTypeEnum.SCORE;
import static com.hhly.mlottery.config.FootBallTypeEnum.SCORE1;
import static com.hhly.mlottery.config.FootBallTypeEnum.SHOOT;
import static com.hhly.mlottery.config.FootBallTypeEnum.SHOOT1;
import static com.hhly.mlottery.config.FootBallTypeEnum.SHOOTASIDE;
import static com.hhly.mlottery.config.FootBallTypeEnum.SHOOTASIDE1;
import static com.hhly.mlottery.config.FootBallTypeEnum.SHOOTASIDE12;
import static com.hhly.mlottery.config.FootBallTypeEnum.SHOOTASIDE2;
import static com.hhly.mlottery.config.FootBallTypeEnum.SUBSTITUTION;
import static com.hhly.mlottery.config.FootBallTypeEnum.SUBSTITUTION1;
import static com.hhly.mlottery.config.FootBallTypeEnum.YELLOW_CARD;
import static com.hhly.mlottery.config.FootBallTypeEnum.YELLOW_CARD1;
import static com.hhly.mlottery.config.FootBallTypeEnum.YTORED;
import static com.hhly.mlottery.config.FootBallTypeEnum.YTORED1;

/**
 * @author wang gang
 * @date 2016/6/2 16:53
 * @des 足球内页改版
 */
public class FootballMatchDetailActivity extends BaseWebSocketActivity implements View.OnClickListener, ExactSwipeRefreshLayout.OnRefreshListener {

    private final static int ERROR = -1;//访问失败
    private final static int SUCCESS = 0;// 访问成功
    private final static int STARTLOADING = 1;// 正在加载中
    //直播状态 liveStatus
    private final static String BEFOURLIVE = "0";//直播前
    private final static String ONLIVE = "1";//直播中
    private final static String LIVEENDED = "-1";//直播结束

    private final static String MATCH_TYPE = "1"; //足球
    private static final String baseUrl = "http://pic.13322.com/bg/";

    private final static int PERIOD_20 = 1000 * 60 * 20;//刷新周期二十分钟
    private final static int PERIOD_5 = 1000 * 60 * 5;//刷新周期五分钟
    private final static int GIFPERIOD_2 = 1000 * 5;//刷新周期两分钟

    private final static int MILLIS_INFuture = 3000;//倒计时3秒

    private int current_tab = -1;
    private TextView tv_home_corner, tv_home_rc, tv_home_yc;
    private TextView tv_guest_corner, tv_guest_rc, tv_guest_yc;
    private FrameLayout fl_odds_loading;
    private FrameLayout fl_odds_net_error_details;
    public ExactSwipeRefreshLayout mRefreshLayout; //下拉刷新
    public ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabsAdapter mTabsAdapter;
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
     * 保存上一场的状态
     */
    private String mPreStatus;
    public final static String BUNDLE_PARAM_THIRDID = "thirdId";

    //    private RecommendFragment mRecommendFragment;  //推介
    //private DetailsRollballFragment mDetailsRollballFragment; //滚球

    private BettingIssueFragment mBettingIssueFragment;
    private BowlFragment mBowlFragment;


    private LiveFragment mLiveFragment;  //直播
    private OddsFragment mOddsFragment;         //指数
    private AnalyzeParentFragment mAnalyzeParentFragment;

    private TextView reLoading; //赔率请求失败重新加载
    private String shareHomeIconUrl;
    private ImageView iv_back;
    private ImageView iv_setting;
    private View iv_share;
    private ShareFragment mShareFragment;
    /**
     * 赛前轮询周期
     */
    private int mReloadPeriod = PERIOD_20;
    private String matchStartTime;
    private ChartBallFragment mChartBallFragment;
    //头部
    private ImageView iv_home_icon;
    private ImageView iv_guest_icon;
    private TextView tv_homename;
    private TextView tv_guestname;
    private TextView score;
    private TextView date;
    private LinearLayout mMatchTypeLayout;
    private TextView mMatchType1;
    private TextView mMatchType2;
    private FrameLayout fl_head;
    private LinearLayout btn_showGif;
    private BarrageView barrage_view;
    private Timer gifTimer;
    private TimerTask gifTimerTask;
    private boolean isFirstShowGif = true;
    private RelativeLayout rl_gif_notice;
    private int gifCount = 0;
    private CountDown countDown;
    private ImageView barrage_switch;
    boolean barrage_isFocus = false;
    private RelativeLayout mLayoutScore;
    //动画WebView
//    private WebView mWebView;
    private LinearLayout ll_Webview;
    private TextView mHalfScore;
    private String url;
    private boolean isAddFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        ImmersionBar.with(this)
//                .statusBarColor(R.color.colorPrimary)
//                .init();
        if (getIntent() != null && getIntent().getExtras() != null) {
            mThirdId = getIntent().getExtras().getString(BUNDLE_PARAM_THIRDID, "-1");
            currentFragmentId = getIntent().getExtras().getInt("currentFragmentId");
            current_tab = getIntent().getIntExtra(FootBallDetailTypeEnum.CURRENT_TAB_KEY, FootBallDetailTypeEnum.FOOT_DETAIL_DEFAULT);
        }
        EventBus.getDefault().register(this);
        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic(BaseUserTopics.footballLive + "." + mThirdId + "." + appendLanguage());
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_football_match_details_test);

        this.mContext = getApplicationContext();

        L.e(TAG, "mThirdId = " + mThirdId);
        url = BaseURLs.URL_FOOTBALLDETAIL_H5 + "?thirdId=" + mThirdId + "&lang=" + appendLanguage();
        initHeadView();
        initView();
        loadData(0);
        initEvent();
        loadAnim();
    }


    private void initView() {
        String[] titles = mContext.getResources().getStringArray(R.array.foot_details_tabs);
        mRefreshLayout = (ExactSwipeRefreshLayout) findViewById(R.id.refresh_layout_details);
        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        //底部ViewPager(滚球、指数等)
        mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
        mTabsAdapter.setTitles(titles);

        fl_odds_loading = (FrameLayout) findViewById(R.id.fl_odds_loading_details);
        fl_odds_net_error_details = (FrameLayout) findViewById(R.id.fl_odds_networkError_details);
        reLoading = (TextView) findViewById(R.id.reLoading_details);
        reLoading.setOnClickListener(this);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_setting = (ImageView) findViewById(R.id.iv_setting);
        mLayoutScore = (RelativeLayout) findViewById(R.id.ll_football_score);

        iv_back.setOnClickListener(this);
        iv_setting.setOnClickListener(this);

        barrage_view = (BarrageView) findViewById(R.id.barrage_view);
        barrage_switch = (ImageView) findViewById(R.id.barrage_switch);
        barrage_switch.setOnClickListener(this);
        //动画WebView
//        tv_nopage = (TextView) findViewById(R.id.tv_nopage);
//        mWebView = (WebView) findViewById(R.id.webview);
        ll_Webview = (LinearLayout) findViewById(R.id.ll_webview);
        mHalfScore = (TextView) findViewById(R.id.half_score);

    }

    public void onEventMainThread(BarrageBean barrageBean) {
        barrage_view.setDatas(barrageBean.getUrl(), barrageBean.getMsg().toString());
    }

    WebView mWebView;

    /**
     * 动画直播
     */
    private void loadAnim() {
        mWebView = new WebView(getApplicationContext());
        ll_Webview.addView(mWebView);
        mWebView.setHorizontalScrollBarEnabled(false);

        WebSettings webSettings = mWebView.getSettings();
        // 不用缓存
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false);
        mWebView.setBackgroundColor(Color.parseColor("#00000000"));//去掉webView白条

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                ll_Webview.setVisibility(View.GONE);
                L.e("AAAA", "error?");
            }
        });

        mWebView.loadUrl(url);
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
                    loadData(1);

                    //走势图
                    mLiveFragment.initChartData(mMatchDetail.getLiveStatus());
                    //统计图
                    mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());

                    if (ONLIVE.equals(mMatchDetail.getLiveStatus())) {
                        connectWebSocket();
                    }

//                    mAnalyzeParentFragment.initData();
                    mOddsFragment.oddPlateRefresh(); // 指数刷新
                    mChartBallFragment.onRefresh();// 聊球
                }
            }
        }, 500);

    }

    private Timer mReloadTimer;
    private TimerTask reloadTimerTask;
    private boolean isStartTimer = false;

    private void startReloadTimer() {
        if (!isStartTimer && !isFinishing()) {//已经开启则不需要再开启
            reloadTimerTask = new TimerTask() {
                @Override
                public void run() {
                    L.d(TAG, "TimerTask run....");
                    loadData(1);
                }
            };
            mReloadTimer = new Timer();
            mReloadTimer.schedule(reloadTimerTask, mReloadPeriod, mReloadPeriod);
            isStartTimer = true;
        }
    }


    private void loadData(int type) {
        if (type != 1) {
            mHandler.sendEmptyMessage(STARTLOADING);
        }
        Map<String, String> params = new HashMap<>();
        params.put("thirdId", mThirdId);

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_INFO_FIRST, params, new VolleyContentFast.ResponseSuccessListener<MatchDetail>() {
            @Override
            public void onResponse(MatchDetail matchDetail) {

                if (!"200".equals(matchDetail.getResult())) {
                    mHandler.sendEmptyMessage(ERROR);
                    return;
                }

                mHandler.sendEmptyMessage(SUCCESS);

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
                    if (BEFOURLIVE.equals(mPreStatus) && BEFOURLIVE.equals(matchDetail.getLiveStatus()) && !isFinishing()) { //赛前

                        initPreData(matchDetail);
                        setScoreText("VS");

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

                            L.d("ssddff", "完场_______________");
                            if (mMatchDetail.getMatchInfo().getMatchTimeLive() != null && mMatchDetail.getMatchInfo().getMatchTimeLive().size() > 0) {

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

                                mKeepTime = "5400000";//90分钟的毫秒数
                                //是否显示精彩瞬间
                                getCollectionCount();

                            }
                        } else if (ONLIVE.equals(mMatchDetail.getLiveStatus())) {//未完场头部


                            L.d(TAG, "未完场" + mMatchDetail.getLiveStatus());

                            initMatchSatisInfo();

                            initMatchNowData(mathchStatisInfo);

                            String state = matchLive.get(0).getState();//获取最后一个的比赛状态
                            mKeepTime = matchLive.get(0).getTime();//获取时间

                            if (Integer.parseInt(mKeepTime) >= (45 * 60 * 1000)) {
                                if (state.equals(FIRSTHALF) || state.equals(HALFTIME)) {//上半场补时中场时间轴不变
                                    mKeepTime = 45 * 60 * 1000 + "";//时间继续赋值为45分钟
                                }
                            }

                            if ("0".equals(mPreStatus) && "1".equals(matchDetail.getLiveStatus()) && !isFinishing()) {
                                if (mReloadTimer != null) {
                                    mReloadTimer.cancel();
                                }

                                mPreStatus = "1";
                            }

                            Collections.reverse(eventMatchTimeLiveList);

                            //是否显示精彩瞬间
                            pollingGifCount();
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
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(ERROR);
            }
        }, MatchDetail.class);
    }


    private void initViewPager(MatchDetail matchDetail) {
        if (BEFOURLIVE.equals(matchDetail.getLiveStatus())) { //赛前
            mLayoutScore.setVisibility(View.VISIBLE);

            setCurrentShowTab(matchDetail.getLiveStatus());

            matchStartTime = matchDetail.getMatchInfo().getStartTime();
            initPreData(matchDetail);
            setScoreText("VS");

        } else {

            setCurrentShowTab(matchDetail.getLiveStatus());

            initPreData(matchDetail);

            matchLive = mMatchDetail.getMatchInfo().getMatchLive();
            allMatchLiveMsgId = new ArrayList<>();

            //完场
            if (LIVEENDED.equals(mMatchDetail.getLiveStatus())) {
                if (matchDetail.getHomeTeamInfo() != null && matchDetail.getGuestTeamInfo() != null) {
                    mHalfScore.setText("(" + matchDetail.getHomeTeamInfo().getHalfScore() + ":" + matchDetail.getGuestTeamInfo().getHalfScore() + ")");
                }
                mLayoutScore.setVisibility(View.VISIBLE);
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

                mKeepTime = "5400000";//90分钟的毫秒数

                //精彩瞬间
                getOverMatchCollectionCount();

            } else if (ONLIVE.equals(mMatchDetail.getLiveStatus())) { //未完场头部
                mLayoutScore.setVisibility(View.GONE);
                //place  1主队 2客队
                initMatchSatisInfo();

                initMatchNowData(mathchStatisInfo);

                setScoreText(mathchStatisInfo.getHome_score() + " : " + mathchStatisInfo.getGuest_score() + "");
                //精彩瞬间
                pollingGifCount();


                Collections.reverse(eventMatchTimeLiveList);

                String state = matchLive.get(0).getState();//获取最后一个的比赛状态
                mKeepTime = matchLive.get(0).getTime();//获取时间

                if (Integer.parseInt(mKeepTime) >= (45 * 60 * 1000)) {
                    if (state.equals(FIRSTHALF) || state.equals(HALFTIME)) {//上半场补时中场时间轴不变
                        mKeepTime = 45 * 60 * 1000 + "";//时间继续赋值为45分钟
                    }
                }
            }
        }

        if (!isInitedViewPager) {

            if (ONLIVE.equals(mMatchDetail.getLiveStatus())) {
                L.d(TAG, "第一次启动socket");
                L.d("456789", "第一次启动socket");
                connectWebSocket();
            }
        }

        isInitedViewPager = true;
    }


    /***
     * 跳转到足球内页显示哪个Tab
     */
    public void setCurrentShowTab(String matchstatus) {
        switch (current_tab) {
            case FootBallDetailTypeEnum.FOOT_DETAIL_ROLL:
                mViewPager.setCurrentItem(FootBallDetailTypeEnum.FOOT_DETAIL_ROLL, false);
                break;
            case FootBallDetailTypeEnum.FOOT_DETAIL_LIVE:
                mViewPager.setCurrentItem(FootBallDetailTypeEnum.FOOT_DETAIL_LIVE, false);
                break;
            case FootBallDetailTypeEnum.FOOT_DETAIL_ANALYSIS:
                mViewPager.setCurrentItem(FootBallDetailTypeEnum.FOOT_ANALYSE_MERGE, false);
                break;
            case FootBallDetailTypeEnum.FOOT_DETAIL_INFOCENTER:
                mViewPager.setCurrentItem(FootBallDetailTypeEnum.FOOT_ANALYSE_MERGE, false);
                break;
            case FootBallDetailTypeEnum.FOOT_DETAIL_INDEX:
                mViewPager.setCurrentItem(FootBallDetailTypeEnum.FOOT_DETAIL_INDEX, false);
                break;
            case FootBallDetailTypeEnum.FOOT_DETAIL_CHARTBALL:
                mViewPager.setCurrentItem(FootBallDetailTypeEnum.FOOT_DETAIL_CHARTBALL, false);
                break;
            default: //默认 用比赛状态来做跳转判断跟外部进来没有关系  赛前:分析   其他:直播
                if (BEFOURLIVE.equals(matchstatus)) {
                    mViewPager.setCurrentItem(FootBallDetailTypeEnum.FOOT_ANALYSE_MERGE, false);
                } else {
                    mViewPager.setCurrentItem(FootBallDetailTypeEnum.FOOT_DETAIL_LIVE, false);
                }
                break;
        }
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
        if (mWebView != null) {
            mWebView.onPause();
            mWebView.destroy();
            mWebView = null;

        }
        if (ll_Webview != null) {
            ll_Webview.removeAllViews();
        }

        if (mReloadTimer != null) {
            mReloadTimer.cancel();
            mReloadTimer.purge();
            mReloadTimer = null;
        }

        if (reloadTimerTask != null) {
            reloadTimerTask.cancel();
            reloadTimerTask = null;
        }

        closePollingGifCount();

        EventBus.getDefault().unregister(this);
        System.gc();
        super.onDestroy();
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

        switch (matchTextLiveBean.getCode()) {
            case "0":
            case "10":
            case "11":  //开始上半场，开球：
                mLayoutScore.setVisibility(View.GONE);
                break;
            case "1"://上半场结束
                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());

                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计

                break;
            case "12":
            case "13":  //下半场开始
                break;
            case "3": //结束下半场
                mLayoutScore.setVisibility(View.VISIBLE);

                mHalfScore.setText("(" + mathchStatisInfo.getHome_half_score() + ":" + mathchStatisInfo.getGuest_half_score()
                        + ")");
                //socket关闭
                // 获取上半场的走势图数据
                setScoreClolor(getApplicationContext().getResources().getColor(R.color.score));

                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());

                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics("-1");// 刷新统计

                closeWebSocket();

                break;

            case "18": //点球状态
            case "19":
                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());

                break;

            case "14": //加时赛
            case "15":
                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());

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

                initMatchNowData(mathchStatisInfo);

                //x时间轴
                mLiveFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveFragment.showFootballEventByState();

                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());

                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计

                mLiveFragment.addTrendChartEvent(matchTextLiveBean);

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

                initMatchNowData(mathchStatisInfo);

                mLiveFragment.cancelFootBallEvent(matchTextLiveBean);
                mLiveFragment.showFootballEventByState();

                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());

                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计

                mLiveFragment.cancelTrendChartEvent(matchTextLiveBean);

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

                initMatchNowData(mathchStatisInfo);

                //x时间轴
                mLiveFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveFragment.showFootballEventByState();

                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());

                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计

                mLiveFragment.addTrendChartEvent(matchTextLiveBean);

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

                initMatchNowData(mathchStatisInfo);

                mLiveFragment.cancelFootBallEvent(matchTextLiveBean);
                mLiveFragment.showFootballEventByState();
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());

                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计
                mLiveFragment.cancelTrendChartEvent(matchTextLiveBean);

                break;

            case "1025": //主队角球
                mathchStatisInfo.setHome_corner(mathchStatisInfo.getHome_corner() + 1);

                //x时间轴
                //事件放入并展示
                initMatchNowData(mathchStatisInfo);

                //x时间轴
                mLiveFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveFragment.showFootballEventByState();


                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());

                mLiveFragment.addTrendChartEvent(matchTextLiveBean);

                break;

            case "1050": //取消主队角球

                if (mathchStatisInfo.getHome_corner() > 0) {
                    mathchStatisInfo.setHome_corner(mathchStatisInfo.getHome_corner() - 1);
                }

                initMatchNowData(mathchStatisInfo);

                //取消进球时间，重绘
                //取消进球时间，重绘
                mLiveFragment.cancelFootBallEvent(matchTextLiveBean);
                mLiveFragment.showFootballEventByState();
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());

                mLiveFragment.cancelFootBallEvent(matchTextLiveBean);

                break;

            case "2049": //客队角球
                mathchStatisInfo.setGuest_corner(mathchStatisInfo.getGuest_corner() + 1);

                initMatchNowData(mathchStatisInfo);

                //x时间轴
                mLiveFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveFragment.showFootballEventByState();

                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());
                mLiveFragment.addTrendChartEvent(matchTextLiveBean);

                break;

            case "2074": //取消客队角球
                if (mathchStatisInfo.getGuest_corner() > 0) {
                    mathchStatisInfo.setGuest_corner(mathchStatisInfo.getGuest_corner() - 1);
                }
                initMatchNowData(mathchStatisInfo);

                //取消进球时间，重绘
                mLiveFragment.cancelFootBallEvent(matchTextLiveBean);
                mLiveFragment.showFootballEventByState();

                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());
                mLiveFragment.cancelTrendChartEvent(matchTextLiveBean);

                break;

            case "1031"://主队点球
                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "2055"://客队点球
                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "1034":   //主队黄牌
                mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() + 1);

                initMatchNowData(mathchStatisInfo);

                //x时间轴
                mLiveFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveFragment.showFootballEventByState();

                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "1048": //取消 主队黄牌
                if (mathchStatisInfo.getHome_yc() > 0) {
                    mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() - 1);
                }

                initMatchNowData(mathchStatisInfo);

                //取消进球时间，重绘
                mLiveFragment.cancelFootBallEvent(matchTextLiveBean);
                mLiveFragment.showFootballEventByState();

                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "1045": //主队两黄变一红

                mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() + 1);
                mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() + 1);

                initMatchNowData(mathchStatisInfo);

                //x时间轴
                mLiveFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveFragment.showFootballEventByState();

                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "1046": //取消主队黄牌过度到红牌
                if (mathchStatisInfo.getHome_yc() > 0 && mathchStatisInfo.getHome_rc() > 0) {
                    mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() - 1);
                    mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() - 1);
                }
                initMatchNowData(mathchStatisInfo);

                //取消进球时间，重绘
                mLiveFragment.cancelFootBallEvent(matchTextLiveBean);
                mLiveFragment.showFootballEventByState();

                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());

                break;


            case "2058":  //客队黄牌
                mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() + 1);
                //x时间轴
                mLiveFragment.addFootballEvent(matchTextLiveBean);

                initMatchNowData(mathchStatisInfo);

                //事件放入并展示
                mLiveFragment.showFootballEventByState();

                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "2072":  //取消客队黄牌
                if (mathchStatisInfo.getGuest_yc() > 0) {
                    mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() - 1);
                }

                initMatchNowData(mathchStatisInfo);

                //取消进球时间，重绘
                mLiveFragment.cancelFootBallEvent(matchTextLiveBean);
                mLiveFragment.showFootballEventByState();

                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());
                break;


            case "2069": //客队两黄变一红
                mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() + 1);
                mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() + 1);
                initMatchNowData(mathchStatisInfo);

                //x时间轴
                mLiveFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveFragment.showFootballEventByState();

                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "2070": //取消客队两黄变一红

                if (mathchStatisInfo.getGuest_yc() > 0 && mathchStatisInfo.getGuest_rc() > 0) {
                    mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() - 1);
                    mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() - 1);
                }
                initMatchNowData(mathchStatisInfo);
                //取消进球时间，重绘
                mLiveFragment.cancelFootBallEvent(matchTextLiveBean);
                mLiveFragment.showFootballEventByState();
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "1032":  //主队红牌
                mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() + 1);
                initMatchNowData(mathchStatisInfo);

                //x时间轴
                mLiveFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveFragment.showFootballEventByState();
                break;
            case "1047": //取消主队红牌
                if (mathchStatisInfo.getHome_rc() > 0) {
                    mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() - 1);
                }
                initMatchNowData(mathchStatisInfo);

                //取消进球时间，重绘
                mLiveFragment.cancelFootBallEvent(matchTextLiveBean);
                mLiveFragment.showFootballEventByState();


                break;

            case "2056":  //客队红牌
                mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() + 1);
                initMatchNowData(mathchStatisInfo);

                //x时间轴
                mLiveFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveFragment.showFootballEventByState();

                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "2071":  //取消客队红牌
                if (mathchStatisInfo.getGuest_rc() > 0) {
                    mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() - 1);
                }
                initMatchNowData(mathchStatisInfo);

                //取消进球时间，重绘
                mLiveFragment.cancelFootBallEvent(matchTextLiveBean);
                mLiveFragment.showFootballEventByState();
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "1026":    //主队危险进攻
                mathchStatisInfo.setHome_danger(mathchStatisInfo.getHome_danger() + 1);
                initMatchNowData(mathchStatisInfo);

                mLiveFragment.addTrendChartEvent(matchTextLiveBean);

                break;

            case "2050":  //客队危进攻
                mathchStatisInfo.setGuest_danger(mathchStatisInfo.getGuest_danger() + 1);
                initMatchNowData(mathchStatisInfo);

                mLiveFragment.addTrendChartEvent(matchTextLiveBean);

                break;

            case "1039":    //主队射正球门

                mathchStatisInfo.setHome_shoot_correct(mathchStatisInfo.getHome_shoot_correct() + 1);
                mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());


                //客队扑救=主队射正-主队比分
                mathchStatisInfo.setGuest_rescue(mathchStatisInfo.getHome_shoot_correct() - mathchStatisInfo.getHome_score());
                initMatchNowData(mathchStatisInfo);

                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计
                mLiveFragment.addTrendChartEvent(matchTextLiveBean);
                break;
            case "1024": //主队进攻
                mathchStatisInfo.setHome_attack(mathchStatisInfo.getHome_attack() + 1);
                initMatchNowData(mathchStatisInfo);

                mLiveFragment.addTrendChartEvent(matchTextLiveBean);
                break;
            case "2048": //客队进攻
                mathchStatisInfo.setGuest_attack(mathchStatisInfo.getGuest_attack() + 1);
                initMatchNowData(mathchStatisInfo);

                mLiveFragment.addTrendChartEvent(matchTextLiveBean);

                break;

            case "2063":  //客队射正球门
                mathchStatisInfo.setGuest_shoot_correct(mathchStatisInfo.getGuest_shoot_correct() + 1);
                mathchStatisInfo.setGuest_shoot_door(mathchStatisInfo.getGuest_shoot_correct() + mathchStatisInfo.getGuest_shoot_miss());
                initMatchNowData(mathchStatisInfo);

                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计
                mLiveFragment.addTrendChartEvent(matchTextLiveBean);

                break;

            case "1040":    //主队射偏球门
                mathchStatisInfo.setHome_shoot_miss(mathchStatisInfo.getHome_shoot_miss() + 1);
                mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());
                initMatchNowData(mathchStatisInfo);

                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计
                mLiveFragment.addTrendChartEvent(matchTextLiveBean);

                //如果有变动就刷新统计数据
                break;
            case "1041":    //主队射偏球门
                mathchStatisInfo.setHome_shoot_miss(mathchStatisInfo.getHome_shoot_miss() + 1);
                mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());
                initMatchNowData(mathchStatisInfo);

                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计

                mLiveFragment.addTrendChartEvent(matchTextLiveBean);
                //如果有变动就刷新统计数据
                break;


            case "2064":  //客队射偏球门
                mathchStatisInfo.setGuest_shoot_miss(mathchStatisInfo.getGuest_shoot_miss() + 1);
                mathchStatisInfo.setGuest_shoot_door(mathchStatisInfo.getGuest_shoot_correct() + mathchStatisInfo.getGuest_shoot_miss());

                initMatchNowData(mathchStatisInfo);

                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计

                mLiveFragment.addTrendChartEvent(matchTextLiveBean);


                //如果有变动就刷新统计数据
                break;
            case "2065":  //客队门框
                mathchStatisInfo.setGuest_shoot_miss(mathchStatisInfo.getGuest_shoot_miss() + 1);
                mathchStatisInfo.setGuest_shoot_door(mathchStatisInfo.getGuest_shoot_correct() + mathchStatisInfo.getGuest_shoot_miss());
                initMatchNowData(mathchStatisInfo);

                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计

                mLiveFragment.addTrendChartEvent(matchTextLiveBean);
                //如果有变动就刷新统计数据
                break;


            case "1055"://主队换人
                //x时间轴
                mLiveFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveFragment.showFootballEventByState();
                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());
                break;

            case "2079": //客队换人
                //x时间轴
                mLiveFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveFragment.showFootballEventByState();
                mLiveFragment.addVerticalFootBallEvent(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());
                break;
            case "1043"://主队越位
                mathchStatisInfo.setHome_away(mathchStatisInfo.getHome_away() + 1);
                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计
                //如果有变动就刷新统计数据
                break;

            case "1027": //主队任意球
                mathchStatisInfo.setHome_free_kick(mathchStatisInfo.getHome_free_kick() + 1);
                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计

                //如果有变动就刷新统计数据
                break;

            case "2067":  //客队越位
                mathchStatisInfo.setGuest_away(mathchStatisInfo.getGuest_away() + 1);
                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计

                //如果有变动就刷新统计数据
                break;
            case "2051"://客队任意球
                mathchStatisInfo.setGuest_free_kick(mathchStatisInfo.getGuest_free_kick() + 1);
                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计

                break;

            case "1042"://主队犯规
                mathchStatisInfo.setHome_foul(mathchStatisInfo.getHome_foul() + 1);
                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计

                break;
            case "1054"://主队界外球
                mathchStatisInfo.setHome_lineOut(mathchStatisInfo.getHome_lineOut() + 1);
                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计

                break;
            case "2066"://客队犯规
                mathchStatisInfo.setGuest_foul(mathchStatisInfo.getGuest_foul() + 1);
                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计

                break;
            case "2078"://客队界外球
                mathchStatisInfo.setGuest_lineOut(mathchStatisInfo.getGuest_lineOut() + 1);
                mLiveFragment.setMathchStatisInfo(mathchStatisInfo);
                mLiveFragment.initLiveStatics(mMatchDetail.getLiveStatus());// 刷新统计

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
                mLiveFragment.setPlayInfo(matchTextLiveBean);
                mLiveFragment.updateRecycleView(matchTextLiveBean.getState());
                break;
            default:
                break;
        }

        if ("0".equals(matchTextLiveBean.getShowId()) && !"1".equals(matchTextLiveBean.getShowId())) {

            matchLive.add(0, matchTextLiveBean);
            Collections.sort(matchLive, new FootballLiveTextComparator()); //排序
            mLiveFragment.setLiveTextDetails(matchLive);
        }

        //自己推送一条结束消息     -1 完场
        if (MATCHFINISH.equals(matchTextLiveBean.getState()) || "20".equals(matchTextLiveBean.getCode())) {
            matchLive.add(0, new MatchTextLiveBean("", "", "0", "0", "4", "99999999", mContext.getResources().getString(R.string.matchFinished_txt), "", "", "0", "", "", "", ""));
            mLiveFragment.setLiveTextDetails(matchLive);
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
        String state = data.get("state");//比赛的状态
        if (Integer.parseInt(mKeepTime) > (45 * 60 * 1000)) {
            if (state.equals(FIRSTHALF) || state.equals(HALFTIME)) {//上半场补时中场时间轴不变
                mKeepTime = 45 * 60 * 1000 + "";//时间继续赋值为45分钟
            }
        }

        mLiveFragment.showTimeView(mKeepTime); //刷新时间轴

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
                    fl_odds_loading.setVisibility(View.VISIBLE);
                    fl_odds_net_error_details.setVisibility(View.GONE);
                    mViewPager.setVisibility(View.GONE);
                    mRefreshLayout.setEnabled(false);
                    break;
                case SUCCESS:// 加载成功
                    fl_odds_loading.setVisibility(View.GONE);
                    fl_odds_net_error_details.setVisibility(View.GONE);
                    mViewPager.setVisibility(View.VISIBLE);
                    mRefreshLayout.setEnabled(true);

                    if (!isAddFragment) {
                        // 传值到直播Fragment
                        //推介
//                    mRecommendFragment = RecommendFragment.newInstance();
                        //滚球
                        // mDetailsRollballFragment = DetailsRollballFragment.newInstance(mThirdId);
                        mBettingIssueFragment = BettingIssueFragment.newInstance(mThirdId);

                        mBowlFragment = BowlFragment.newInstance(mThirdId);

                        //直播
                        mLiveFragment = LiveFragment.newInstance(mThirdId, mMatchDetail, mathchStatisInfo, eventMatchTimeLiveList, trendChartList, mKeepTime);
                        //指数
                        mOddsFragment = OddsFragment.newInstance();
                        //分析、情报
                        mAnalyzeParentFragment = AnalyzeParentFragment.newInstance(mThirdId, current_tab);
                        // 聊球
                        mChartBallFragment = ChartBallFragment.newInstance(0, mThirdId);

                        mTabsAdapter.addFragments(mBettingIssueFragment, mBowlFragment, mLiveFragment, mOddsFragment, mAnalyzeParentFragment, mChartBallFragment);
                        mViewPager.setOffscreenPageLimit(5);//设置预加载页面的个数。
                        mViewPager.setAdapter(mTabsAdapter);
                        mTabLayout.setupWithViewPager(mViewPager);
                        isAddFragment = true;
                    }
                    break;
                case ERROR:// 加载失败
                    if (isInitedViewPager) {
                        Toast.makeText(getApplicationContext(), R.string.exp_net_status_txt, Toast.LENGTH_SHORT).show();
                    } else {

                        L.d("10299", "错误");
                        fl_odds_loading.setVisibility(View.GONE);
                        fl_odds_net_error_details.setVisibility(View.VISIBLE);
                        mViewPager.setVisibility(View.GONE);
                        mRefreshLayout.setEnabled(false);
                    }
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
                this.finish();
                break;
            case R.id.iv_setting:  //关注、分享
                popWindow(iv_setting);
                break;
            case R.id.reLoading_details:
                mHandler.sendEmptyMessage(STARTLOADING);
                loadData(0);
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

            case R.id.barrage_switch:

                if (barrage_isFocus) {
                    barrage_switch.setImageResource(R.mipmap.danmu_open);
                    barrage_isFocus = false;
                    barrage_view.setAlpha(1);
                } else {
                    barrage_switch.setImageResource(R.mipmap.danmu_close);
                    barrage_isFocus = true;
                    barrage_view.setAlpha(0);
                }
                break;
            case R.id.iv_home_icon:// 主队logo
                if (mMatchDetail.getHomeTeamInfo().getId() != null) {
                    Intent homeIntent = new Intent(this, FootballTeamInfoActivity.class);
                    homeIntent.putExtra("TEAM_ID", mMatchDetail.getHomeTeamInfo().getId());
                    homeIntent.putExtra("TITLE_TEAM_NAME", mMatchDetail.getHomeTeamInfo().getName());
                    startActivity(homeIntent);
                }
                break;
            case R.id.iv_guest_icon:// 客队logo
                if (mMatchDetail.getGuestTeamInfo().getId() != null) {
                    Intent guestIntent = new Intent(this, FootballTeamInfoActivity.class);
                    guestIntent.putExtra("TEAM_ID", mMatchDetail.getGuestTeamInfo().getId());
                    guestIntent.putExtra("TITLE_TEAM_NAME", mMatchDetail.getGuestTeamInfo().getName());
                    startActivity(guestIntent);
                }
                break;
            default:
                break;
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

        if (DeviceInfo.isZH()) {
            iv_share.setVisibility(View.VISIBLE);
        } else {
            iv_share.setVisibility(View.GONE);
        }

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
                if (position != FootBallDetailTypeEnum.FOOT_DETAIL_CHARTBALL) {// 聊球界面禁用下拉刷新
                    MyApp.getContext().sendBroadcast(new Intent("CLOSE_INPUT_ACTIVITY"));
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
        mWebView.onResume();
        ImmersionBar.with(this).transparentStatusBar().init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        mWebView.onPause();
    }

    private void initHeadView() {

        fl_head = (FrameLayout) findViewById(R.id.fl_head);

        iv_home_icon = (ImageView) findViewById(R.id.iv_home_icon);
        iv_guest_icon = (ImageView) findViewById(R.id.iv_guest_icon);
//        iv_bg = (ImageView) findViewById(R.id.iv_bg);

        iv_home_icon.setOnClickListener(this);
        iv_guest_icon.setOnClickListener(this);


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


        tv_home_corner = (TextView) findViewById(R.id.tv_home_corner);
        tv_home_rc = (TextView) findViewById(R.id.tv_home_rc);
        tv_home_yc = (TextView) findViewById(R.id.tv_home_yc);
        tv_guest_corner = (TextView) findViewById(R.id.tv_guest_corner);
        tv_guest_rc = (TextView) findViewById(R.id.tv_guest_rc);
        tv_guest_yc = (TextView) findViewById(R.id.tv_guest_yc);
        //跳转到足球资料库内页
        mMatchTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FootballMatchDetailActivity.this, FootballDatabaseDetailsActivity.class);
                if (mMatchDetail != null) {
                    intent.putExtra("league", new DataBaseBean(mMatchDetail.getLeagueType() + "", mMatchDetail.getLeagueId() + "", "", ""));
                }
                intent.putExtra("isIntegral", false);
                startActivity(intent);
            }
        });
    }

    private void initPreData(MatchDetail mMatchDetail) {
        // if (flag) {
        int random = new Random().nextInt(20);
        String url = baseUrl + random + ".png";
//        ImageLoader.load(mContext, url, R.color.colorPrimary).into(iv_bg);

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

        initMatchOverData(mMatchDetail);
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


    /**
     * 完场进行的处理
     *
     * @param mMatchDetail 赛事总信息
     */
    private void initMatchOverData(MatchDetail mMatchDetail) {
        //时间轴的处理
        //完场直接有数据
        if (BEFOURLIVE.equals(mMatchDetail.getLiveStatus())) {
            tv_home_corner.setText("0");
            tv_home_rc.setText("0");
            tv_home_yc.setText("0");
            tv_guest_corner.setText("0");
            tv_guest_rc.setText("0");
            tv_guest_yc.setText("0");
        } else if (LIVEENDED.equals(mMatchDetail.getLiveStatus())) {
            tv_home_corner.setText(mMatchDetail.getHomeTeamInfo().getCorner());
            tv_home_rc.setText(mMatchDetail.getHomeTeamInfo().getRc());
            tv_home_yc.setText(mMatchDetail.getHomeTeamInfo().getYc());
            tv_guest_corner.setText(mMatchDetail.getGuestTeamInfo().getCorner());
            tv_guest_rc.setText(mMatchDetail.getGuestTeamInfo().getRc());
            tv_guest_yc.setText(mMatchDetail.getGuestTeamInfo().getYc());
        }
    }

    /**
     * 比赛中的统计信息
     *
     * @param mathchStatisInfo
     */
    private void initMatchNowData(MathchStatisInfo mathchStatisInfo) {

        tv_home_corner.setText(String.valueOf(mathchStatisInfo.getHome_corner()));
        tv_home_rc.setText(String.valueOf(mathchStatisInfo.getHome_rc()));
        tv_home_yc.setText(String.valueOf(mathchStatisInfo.getHome_yc()));
        tv_guest_corner.setText(String.valueOf(mathchStatisInfo.getGuest_corner()));
        tv_guest_rc.setText(String.valueOf(mathchStatisInfo.getGuest_rc()));
        tv_guest_yc.setText(String.valueOf(mathchStatisInfo.getGuest_yc()));
    }

}
