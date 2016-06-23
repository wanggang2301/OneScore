package com.hhly.mlottery.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.BasePagerAdapter;
import com.hhly.mlottery.adapter.football.FragmentAdapter;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.bean.ShareBean;
import com.hhly.mlottery.bean.footballDetails.MatchDetail;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;
import com.hhly.mlottery.bean.footballDetails.MatchTimeLiveBean;
import com.hhly.mlottery.bean.footballDetails.MathchStatisInfo;
import com.hhly.mlottery.bean.footballDetails.PreLiveText;
import com.hhly.mlottery.bean.websocket.WebSocketStadiumKeepTime;
import com.hhly.mlottery.bean.websocket.WebSocketStadiumLiveTextEvent;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.ShareFragment;
import com.hhly.mlottery.frame.footframe.AnalyzeFragment;
import com.hhly.mlottery.frame.footframe.DetailsRollballFragment;
import com.hhly.mlottery.frame.footframe.FocusFragment;
import com.hhly.mlottery.frame.footframe.ImmediateFragment;
import com.hhly.mlottery.frame.footframe.LiveHeadInfoFragment;
import com.hhly.mlottery.frame.footframe.OddsFragment;
import com.hhly.mlottery.frame.footframe.PreHeadInfoFrament;
import com.hhly.mlottery.frame.footframe.ResultFragment;
import com.hhly.mlottery.frame.footframe.ScheduleFragment;
import com.hhly.mlottery.frame.footframe.StatisticsFragmentTest;
import com.hhly.mlottery.frame.footframe.TalkAboutBallFragment;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.FootballLiveTextComparator;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.StadiumUtils;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.websocket.HappySocketClient;
import com.hhly.mlottery.widget.CustomViewpager;
import com.hhly.mlottery.widget.DepthPageTransformer;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;

import org.java_websocket.drafts.Draft_17;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * @author wang gang
 * @date 2016/6/2 16:53
 * @des 足球内页改版
 */
public class FootballMatchDetailActivityTest extends AppCompatActivity implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener, ExactSwipeRefrashLayout.OnRefreshListener, HappySocketClient.SocketResponseErrorListener, HappySocketClient.SocketResponseCloseListener, HappySocketClient.SocketResponseMessageListener {


    private final static String TAG = "FootballMatchDetailActivityTest";

    private final static String URL_DEFAULT = "http://m.13322.com";

    private final int IMMEDIA_FRAGMENT = 0;
    private final int RESULT_FRAGMENT = 1;
    private final int SCHEDULE_FRAGMENT = 2;
    private final int FOCUS_FRAGMENT = 3;


    private final int BANNER_PLAY_TIME = 5000; //头部5秒轮播
    private final int BANNER_ANIM_TIME = 1000; //轮播动画时间


    private final int ERROR = -1;//访问失败
    private final int SUCCESS = 0;// 访问成功
    private final int STARTLOADING = 1;// 正在加载中

    private FrameLayout fl_odds_loading;

    private FrameLayout fl_odds_net_error_details;


    private ExactSwipeRefrashLayout mRefreshLayout; //下拉刷新

    private FragmentManager fragmentManager;
    private CustomViewpager mHeadviewpager;
    private ViewPager mViewPager;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private FragmentAdapter fragmentAdapter;
    private BasePagerAdapter basePagerAdapter;
    private TabLayout mTabLayout;
    private TabsAdapter mTabsAdapter;
    private Toolbar toolbar;

    private CircleIndicator mIndicator;

    //直播状态 liveStatus
    private static final String BEFOURLIVE = "0";//直播前
    private static final String ONLIVE = "1";//直播中
    private static final String LIVEENDED = "-1";//直播结束

    //赛事进行状态。matchStatus
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


    private MathchStatisInfo mathchStatisInfo;


    //  滑动到顶部显示的内容
    private String toolbarTitle;

    private Context mContext;


    /**
     * 赛事id
     */
    public String mThirdId;


    public int mtype = 0;

    private int currentFragmentId = 0;

    private MatchDetail mMatchDetail;

    private List<MatchTextLiveBean> matchLive;

    private List<Integer> allMatchLiveMsgId;

    private List<MatchTimeLiveBean> xMatchLive;


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

    private HappySocketClient hSocketClient;
    private URI hSocketUri = null;


    public final static String BUNDLE_PARAM_THIRDID = "thirdId";


    private boolean isMatchStart = false;  //判断比赛推送时间状态

    private PreHeadInfoFrament mPreHeadInfoFrament;
    private LiveHeadInfoFragment mLiveHeadInfoFragment;

    private DetailsRollballFragment mDetailsRollballFragment; //滚球

    private TalkAboutBallFragment mTalkAboutBallFragment;

    private AnalyzeFragment mAnalyzeFragment;  //分析
    private OddsFragment mOddsFragment;         //指数

    private StatisticsFragmentTest mStatisticsFragment;  //统计

    private int mStartTime;// 获取推送的开赛时间
    private TextView reLoading; //赔率请求失败重新加载


    /***
     * 走势图
     */
    private ArrayList<Integer> homeCorners = new ArrayList<>();
    private ArrayList<Integer> guestCorners = new ArrayList<>();
    private ArrayList<Integer> homeDangers = new ArrayList<>();
    private ArrayList<Integer> guestDangers = new ArrayList<>();

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

    /**
     * 赛前轮询周期
     */
    private int mReloadPeriod = PERIOD_20;

    private boolean isRquestSuccess = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_match_details_test);
        this.mContext = getApplicationContext();
        if (getIntent().getExtras() != null) {
            mThirdId = getIntent().getExtras().getString(BUNDLE_PARAM_THIRDID, "1300");
            currentFragmentId = getIntent().getExtras().getInt("currentFragmentId");
        }

        L.e(TAG, "mThirdId = " + mThirdId);

        initView();


        /***
         * 足球内页头部ViewPager
         */
        mPreHeadInfoFrament = PreHeadInfoFrament.newInstance();
        mLiveHeadInfoFragment = new LiveHeadInfoFragment().newInstance();
        basePagerAdapter.addFragments(mPreHeadInfoFrament, mLiveHeadInfoFragment);
        mHeadviewpager.setAdapter(basePagerAdapter);
        mIndicator.setViewPager(mHeadviewpager);
        basePagerAdapter.registerDataSetObserver(mIndicator.getDataSetObserver());
        mHeadviewpager.setCurrentItem(0);

        mHeadviewpager.setIsScrollable(false);
        mIndicator.setVisibility(View.GONE);

        mDetailsRollballFragment = DetailsRollballFragment.newInstance();
        mTalkAboutBallFragment = new TalkAboutBallFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param1", mThirdId);
        mTalkAboutBallFragment.setArguments(bundle);
        //分析
        mAnalyzeFragment = AnalyzeFragment.newInstance(mThirdId, "");
        //指数
        mOddsFragment = OddsFragment.newInstance("", "");
        //统计
        mStatisticsFragment = StatisticsFragmentTest.newInstance();

        mTabsAdapter.addFragments(mDetailsRollballFragment, mTalkAboutBallFragment, mAnalyzeFragment, mOddsFragment, mStatisticsFragment);
        mViewPager.setOffscreenPageLimit(4);//设置预加载页面的个数。
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);


        loadData();

        try {
            hSocketUri = new URI(BaseURLs.WS_SERVICE);
            System.out.println(">>>>>" + hSocketUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void initView() {

        // String[] titles = mContext.getResourceName(R.attr.foot_details_tabs);


        String[] titles = mContext.getResources().getStringArray(R.array.foot_details_tabs);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mRefreshLayout = (ExactSwipeRefrashLayout) findViewById(R.id.refresh_layout_details);
        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);


        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        mHeadviewpager = new CustomViewpager(mContext);

        mHeadviewpager = (CustomViewpager) findViewById(R.id.headviewpager);


        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        appBarLayout.addOnOffsetChangedListener(this);

        mIndicator = (CircleIndicator) findViewById(R.id.indicator);
        fragmentManager = getSupportFragmentManager();


        basePagerAdapter = new BasePagerAdapter(fragmentManager);

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

        setScroller();


    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if ((-verticalOffset) == appBarLayout.getTotalScrollRange()) {
            head_home_name.setVisibility(View.VISIBLE);
            head_guest_name.setVisibility(View.VISIBLE);
            head_score.setVisibility(View.VISIBLE);
        } else {
            head_home_name.setVisibility(View.GONE);
            head_guest_name.setVisibility(View.GONE);
            head_score.setVisibility(View.GONE);
        }

        if (mCollapsingToolbarLayout.getHeight() + verticalOffset < mHeadviewpager.getHeight()) {
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
        L.d("10299", "下拉刷新");

        L.d(TAG, "下拉刷新");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                // if (mMatchDetail != null && !"-1".equals(mMatchDetail.getLiveStatus())) {
                if (mMatchDetail != null) {
                    L.d(TAG, "下拉刷新未开赛和正在比赛的");
                    loadData();

                    mAnalyzeFragment.initData();//分析下拉刷新

                    mOddsFragment.oddPlateRefresh(); //指数刷新

                    //走势图
                    mStatisticsFragment.initData(mMatchDetail.getLiveStatus());
                    //统计图
                    mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());
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
            mReloadTimer.schedule(reloadTimerTask, 2000, mReloadPeriod);
            isStartTimer = true;
        }
    }


    private void loadData() {

        mHandler.sendEmptyMessage(STARTLOADING);
        Map<String, String> params = new HashMap<>();
        params.put("thirdId", mThirdId);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_INFO, params, new VolleyContentFast.ResponseSuccessListener<MatchDetail>() {
            @Override
            public void onResponse(MatchDetail matchDetail) {
                //  L.d("10299", matchDetail.getResult());

               /* if ("200".equals(matchDetail.getResult())) {
                    mHandler.sendEmptyMessage(ERROR);

                    return;
                }*/

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
                        L.d("99999","刷新賽前");

                        mPreHeadInfoFrament.initData(matchDetail,true);
                        mPreHeadInfoFrament.setScoreText("VS");

                        head_home_name.setText(matchDetail.getHomeTeamInfo().getName());
                        head_guest_name.setText(matchDetail.getGuestTeamInfo().getName());
                        head_score.setText("VS");
                        mDetailsRollballFragment.refreshMatch(matchDetail, 0);

                    } else {


                        //头部Viewpager赛中和赛后头初始化数据

                        L.d(TAG, "头部刷新");
                        L.d("99999","刷新賽後");

                        mPreHeadInfoFrament.initData(matchDetail,true);
                        mLiveHeadInfoFragment.initData(matchDetail);

                        matchLive = mMatchDetail.getMatchInfo().getMatchLive();
                        allMatchLiveMsgId = new ArrayList<>();


                        //完场
                        if (LIVEENDED.equals(mMatchDetail.getLiveStatus())) {
                            mPreHeadInfoFrament.setScoreText(mMatchDetail.getHomeTeamInfo().getScore() + ":" + mMatchDetail.getGuestTeamInfo().getScore());
                            mPreHeadInfoFrament.setScoreClolor(mContext.getResources().getColor(R.color.score));
                            head_home_name.setText(matchDetail.getHomeTeamInfo().getName());
                            head_guest_name.setText(matchDetail.getGuestTeamInfo().getName());
                            head_score.setText(mMatchDetail.getHomeTeamInfo().getScore() + ":" + mMatchDetail.getGuestTeamInfo().getScore());
                            mLiveHeadInfoFragment.initMatchOverData(mMatchDetail);
                            mKeepTime = "5400000";//90分钟的毫秒数


                        } else if (ONLIVE.equals(mMatchDetail.getLiveStatus())) {//未完场头部


                            L.d(TAG, "未完场" + mMatchDetail.getLiveStatus());

                            initMatchSatisInfo();

                            head_home_name.setText(matchDetail.getHomeTeamInfo().getName());
                            head_guest_name.setText(matchDetail.getGuestTeamInfo().getName());
                            head_score.setText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score());

                            mPreHeadInfoFrament.setScoreText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score() + "");

                            mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                            mLiveHeadInfoFragment.initFootBallEventData(mMatchDetail);

                            String state = matchLive.get(0).getState();//获取最后一个的比赛状态
                            mKeepTime = matchLive.get(0).getTime();//获取时间

                            if (Integer.parseInt(mKeepTime) >= (45 * 60 * 1000)) {
                                if (state.equals(FIRSTHALF) || state.equals(HALFTIME)) {//上半场补时中场时间轴不变
                                    mKeepTime = 45 * 60 * 1000 + "";//时间继续赋值为45分钟
                                }
                            }
                            mLiveHeadInfoFragment.showTimeView(mKeepTime);
                            if (NOTOPEN.equals(state)) {
                                //未开state=0
                                mLiveHeadInfoFragment.setKeepTime(mContext.getResources().getString(R.string.not_start_txt));
                                mLiveHeadInfoFragment.setfrequencyText("");
                                isMatchStart = false;

                            } else if (FIRSTHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) <= 45) {
                                //上半场state=1
                                mLiveHeadInfoFragment.setKeepTime(StadiumUtils.convertStringToInt(mKeepTime) + "");
                                mLiveHeadInfoFragment.setfrequencyText("'");
                                isMatchStart = true;
                            } else if (FIRSTHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) > 45) {
                                mLiveHeadInfoFragment.setKeepTime("45+");
                                mLiveHeadInfoFragment.setfrequencyText("'");
                                isMatchStart = false;

                            } else if (HALFTIME.equals(state)) {
                                //中场state=2
                                mLiveHeadInfoFragment.setKeepTime(mContext.getResources().getString(R.string.pause_txt));
                                mLiveHeadInfoFragment.setfrequencyText("");
                                isMatchStart = false;

                            } else if (SECONDHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) <= 90) {
                                //下半场state=3
                                mLiveHeadInfoFragment.setKeepTime(StadiumUtils.convertStringToInt(mKeepTime) + "");
                                mLiveHeadInfoFragment.setfrequencyText("'");
                                isMatchStart = true;

                            } else if (SECONDHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) > 90) {
                                mLiveHeadInfoFragment.setKeepTime("90+");
                                mLiveHeadInfoFragment.setfrequencyText("'");
                                isMatchStart = false;
                            } else {
                                mLiveHeadInfoFragment.setKeepTime(StadiumUtils.convertStringToInt(mKeepTime) + "");
                            }

                            if ("0".equals(mPreStatus) && "1".equals(matchDetail.getLiveStatus()) && !isFinishing()) {
                                mDetailsRollballFragment.activateMatch();

                                //滑动
                                mHeadviewpager.setIsScrollable(true);
                                mIndicator.setVisibility(View.VISIBLE);

                                mReloadTimer.cancel();
                                mPreStatus = "1";
                            }


                            mStatisticsFragment.setList(homeCorners, guestCorners, homeDangers, guestDangers);
                            mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);

                        }

                        mDetailsRollballFragment.refreshMatch(matchDetail, 2);                        //2只代表不是未开赛

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

                if (isInitedViewPager) {

                    if (BEFOURLIVE.equals(mMatchDetail.getLiveStatus()) || ONLIVE.equals(mMatchDetail.getLiveStatus())) {
                        startWebsocket();
                        computeWebSocket();
                    }
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

        L.d(TAG, "状态" + matchDetail.getLiveStatus());

        if ("0".equals(matchDetail.getLiveStatus())) { //赛前

            mHeadviewpager.setIsScrollable(false);
            mIndicator.setVisibility(View.GONE);


            L.d("99999","賽前");
/*

            mPreHeadInfoFrament.initData(matchDetail,false);
            mPreHeadInfoFrament.setScoreText("VS");

            head_home_name.setText(matchDetail.getHomeTeamInfo().getName());
            head_guest_name.setText(matchDetail.getGuestTeamInfo().getName());
            head_score.setText("VS");


            mDetailsRollballFragment.setMatchData(DetailsRollballFragment.DETAILSROLLBALL_TYPE_PRE, matchDetail);
*/



        } else {

            mHeadviewpager.setIsScrollable(true);
            mIndicator.setVisibility(View.VISIBLE);
            mHeadviewpager.setPageTransformer(true, new DepthPageTransformer());


            //头部Viewpager赛中和赛后头初始化数据

            L.d("99999","賽后");

            mPreHeadInfoFrament.initData(matchDetail,true);
            mLiveHeadInfoFragment.initData(matchDetail);


            matchLive = mMatchDetail.getMatchInfo().getMatchLive();
            allMatchLiveMsgId = new ArrayList<>();


            //完场
            if (LIVEENDED.equals(mMatchDetail.getLiveStatus())) {


                mPreHeadInfoFrament.setScoreText(mMatchDetail.getHomeTeamInfo().getScore() + ":" + mMatchDetail.getGuestTeamInfo().getScore());
                mPreHeadInfoFrament.setScoreClolor(mContext.getResources().getColor(R.color.score));

                head_home_name.setText(matchDetail.getHomeTeamInfo().getName());
                head_guest_name.setText(matchDetail.getGuestTeamInfo().getName());
                head_score.setText(mMatchDetail.getHomeTeamInfo().getScore() + ":" + mMatchDetail.getGuestTeamInfo().getScore());


                //  toolbarTitle = mMatchDetail.getHomeTeamInfo().getName() + " " + mMatchDetail.getHomeTeamInfo().getScore() + "-" + mMatchDetail.getGuestTeamInfo().getScore() + " " + mMatchDetail.getGuestTeamInfo().getName();
                mLiveHeadInfoFragment.initMatchOverData(mMatchDetail);
                mKeepTime = "5400000";//90分钟的毫秒数
            } else if (ONLIVE.equals(mMatchDetail.getLiveStatus())) {//未完场头部


                L.d(TAG, "统计数据" + mMatchDetail.getLiveStatus());
                initMatchSatisInfo();


                head_home_name.setText(matchDetail.getHomeTeamInfo().getName());
                head_guest_name.setText(matchDetail.getGuestTeamInfo().getName());
                head_score.setText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score());

                mPreHeadInfoFrament.setScoreText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score() + "");

                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                mLiveHeadInfoFragment.initFootBallEventData(mMatchDetail);

                String state = matchLive.get(0).getState();//获取最后一个的比赛状态
                mKeepTime = matchLive.get(0).getTime();//获取时间

                if (Integer.parseInt(mKeepTime) >= (45 * 60 * 1000)) {
                    if (state.equals(FIRSTHALF) || state.equals(HALFTIME)) {//上半场补时中场时间轴不变
                        mKeepTime = 45 * 60 * 1000 + "";//时间继续赋值为45分钟
                    }
                }

                mLiveHeadInfoFragment.showTimeView(mKeepTime);
                if (NOTOPEN.equals(state)) {
                    //未开state=0
                    mLiveHeadInfoFragment.setKeepTime(mContext.getResources().getString(R.string.not_start_txt));
                    mLiveHeadInfoFragment.setfrequencyText("");
                    isMatchStart = false;

                } else if (FIRSTHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) <= 45) {
                    //上半场state=1
                    mLiveHeadInfoFragment.setKeepTime(StadiumUtils.convertStringToInt(mKeepTime) + "");
                    mLiveHeadInfoFragment.setfrequencyText("'");
                    isMatchStart = true;
                } else if (FIRSTHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) > 45) {
                    mLiveHeadInfoFragment.setKeepTime("45+");
                    mLiveHeadInfoFragment.setfrequencyText("'");
                    isMatchStart = false;

                } else if (HALFTIME.equals(state)) {
                    //中场state=2
                    mLiveHeadInfoFragment.setKeepTime(mContext.getResources().getString(R.string.pause_txt));
                    mLiveHeadInfoFragment.setfrequencyText("");
                    isMatchStart = false;

                } else if (SECONDHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) <= 90) {
                    //下半场state=3
                    mLiveHeadInfoFragment.setKeepTime(StadiumUtils.convertStringToInt(mKeepTime) + "");
                    mLiveHeadInfoFragment.setfrequencyText("'");
                    isMatchStart = true;

                } else if (SECONDHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) > 90) {
                    mLiveHeadInfoFragment.setKeepTime("90+");
                    mLiveHeadInfoFragment.setfrequencyText("'");
                    isMatchStart = false;
                } else {
                    mLiveHeadInfoFragment.setKeepTime(StadiumUtils.convertStringToInt(mKeepTime) + "");
                }

                mStatisticsFragment.setList(homeCorners, guestCorners, homeDangers, guestDangers);
                mStatisticsFragment.initData(mMatchDetail.getLiveStatus());


                //统计图
                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());
            }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHeadviewpager.setCurrentItem(1, true);
                }
            }, BANNER_PLAY_TIME);
            //赛后赛中下面赔率显示

            mDetailsRollballFragment.setMatchData(DetailsRollballFragment.DETAILSROLLBALL_TYPE_ING, matchDetail);


        }

        mStatisticsFragment.setType(matchDetail.getLiveStatus());


/*
        //聊球
        mTalkAboutBallFragment = new TalkAboutBallFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param1", mThirdId);
        mTalkAboutBallFragment.setArguments(bundle);

        //分析
        mAnalyzeFragment = AnalyzeFragment.newInstance(mThirdId, "");
        //指数
        mOddsFragment = OddsFragment.newInstance("", "");

        //统计
        mStatisticsFragment = StatisticsFragmentTest.newInstance(mMatchDetail.getLiveStatus());

        mTabsAdapter.addFragments(mDetailsRollballFragment, mTalkAboutBallFragment, mAnalyzeFragment, mOddsFragment, mStatisticsFragment);
        mViewPager.setOffscreenPageLimit(1);//设置预加载页面的个数。
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);*/
        isInitedViewPager = true;

/*
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffsetPixels == 0) {

                    L.d(TAG, "onPageScrolled=" + position);
                    switch (position) {
                        case 4:


                            //走势图
                            mStatisticsFragment.setList(homeCorners, guestCorners, homeDangers, guestDangers);
                            mStatisticsFragment.initData(mMatchDetail.getLiveStatus());


                            //统计图
                            mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                            mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());


                            break;
                    }
                }
            }
        });*/

    }

    /**
     * 统计赛中红黄牌等信息
     */
    private MathchStatisInfo initMatchSatisInfo() {

        L.d("hhhhhh", "fdfffff" + mMatchDetail.getLiveStatus());
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

        homeCorners.clear();
        homeDangers.clear();
        guestCorners.clear();
        guestDangers.clear();
        homeCorners = initMatchTrend("1025", "1050", StadiumUtils.convertStringToInt(matchLive.get(0).getTime()));//通过最后一个事件来获取时间
        homeDangers = initMatchTrend("1026", null, StadiumUtils.convertStringToInt(matchLive.get(0).getTime()));
        guestCorners = initMatchTrend("2049", "2074", StadiumUtils.convertStringToInt(matchLive.get(0).getTime()));
        guestDangers = initMatchTrend("2050", null, StadiumUtils.convertStringToInt(matchLive.get(0).getTime()));
        homeCorners.add(0, 0);
        homeDangers.add(0, 0);
        guestCorners.add(0, 0);
        guestDangers.add(0, 0);

        return mathchStatisInfo;
    }


    /**
     * 按时间段统计走势图的数据
     *
     * @param code  进球的code
     * @param code2 取消的code
     * @param time  开赛的当前时间
     * @return 统计好的数据集合
     */
    private ArrayList<Integer> initMatchTrend(String code, String code2, int time) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        int time15 = 0, time30 = 0, time45 = 0, time60 = 0, time75 = 0, time90 = 0;

        for (MatchTextLiveBean matchTextLiveBean : matchLive) {
            Integer dd = StadiumUtils.convertStringToInt(matchTextLiveBean.getTime());
            if (code.equals(matchTextLiveBean.getCode())) {  //主队角球
                if (FIRSTHALF.equals(matchTextLiveBean.getState()) || HALFTIME.equals(matchTextLiveBean.getState())) {// 上半场和中场
                    if (dd >= 0 && dd <= 15 && time >= 15) {
                        time15++;
                    }
                    if (dd > 15 && dd <= 30 && time >= 30) {
                        time30++;
                    }
                    if (dd > 30 && time >= 45) {
                        time45++;
                    }
                } else if (SECONDHALF.equals(matchTextLiveBean.getState())) {// 下半场
                    if (dd >= 0 && dd <= 60 && time >= 60) {
                        time60++;
                    }
                    if (dd > 60 && dd <= 75 && time >= 75) {
                        time75++;
                    }
                    if (dd > 75 && time >= 90) {
                        time90++;
                    }
                }
            }
        }

        if (code2 != null) {
            for (MatchTextLiveBean matchTextLiveBean : matchLive) {
                if (code2.equals(matchTextLiveBean.getCode())) {  //主队角球
                    if (FIRSTHALF.equals(matchTextLiveBean.getState()) || HALFTIME.equals(matchTextLiveBean.getState())) {// 上半场和中场
                        if (time >= 0 && time <= 15) {
                            time15--;
                        }
                        if (time > 15 && time <= 30) {
                            time30--;
                        }
                        if (time > 30) {
                            time45--;
                        }
                    } else if (SECONDHALF.equals(matchTextLiveBean.getState())) {// 下半场
                        if (time >= 0 && time <= 60) {
                            time60--;
                        }
                        if (time > 60 && time <= 75) {
                            time75--;
                        }
                        if (time > 75) {
                            time90--;
                        }
                    }
                }
            }
        }


        if (time >= 15) {
            list.add(time15);
        }
        if (time >= 30) {
            list.add(time30 + time15);
        }
        if (time >= 45) {
            list.add(time45 + time30 + time15);
        }
        if (time >= 60) {
            list.add(time60 + time45 + time30 + time15);
        }
        if (time >= 75) {
            list.add(time75 + time60 + time45 + time30 + time15);
        }
        if (time >= 90) {
            list.add(time90 + time75 + time60 + time45 + time30 + time15);
        }

        return list;

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (computeWebSocketConnTimer != null) {
            computeWebSocketConnTimer.cancel();
        }

        if (hSocketClient != null) {
            hSocketClient.close();
        }

        if (mReloadTimer != null) {
            mReloadTimer.cancel();
        }
    }


    /***
     * 开始推送socket
     */
    private synchronized void startWebsocket() {

        L.d(TAG, "---onMessage---开始推送" + mThirdId);

        if (hSocketClient != null) {
            if (!hSocketClient.isClosed()) {
                hSocketClient.close();
            }

            hSocketClient = new HappySocketClient(hSocketUri, new Draft_17());
            hSocketClient.setSocketResponseMessageListener(this);
            hSocketClient.setSocketResponseCloseListener(this);
            hSocketClient.setSocketResponseErrorListener(this);
            try {
                hSocketClient.connect();
            } catch (IllegalThreadStateException e) {
                hSocketClient.close();
            }
        } else {
            hSocketClient = new HappySocketClient(hSocketUri, new Draft_17());
            hSocketClient.setSocketResponseMessageListener(this);
            hSocketClient.setSocketResponseCloseListener(this);
            hSocketClient.setSocketResponseErrorListener(this);
            try {
                hSocketClient.connect();
            } catch (IllegalThreadStateException e) {
                hSocketClient.close();
            }
        }
    }

    //事件推送

    @Override
    public void onMessage(String message) {
        L.d(TAG, "---onMessage---推送比赛thirdId==" + mThirdId);

        pushStartTime = System.currentTimeMillis(); // 记录起始时间
        L.d(TAG, "心跳时间" + pushStartTime);
        if (message.startsWith("CONNECTED")) {
            String id = "android" + DeviceInfo.getDeviceId(mContext);
            id = MD5Util.getMD5(id);
            if (mContext == null) {
                return;
            }
            hSocketClient.send("SUBSCRIBE\nid:" + id + "\ndestination:/topic/USER.topic.liveEvent." + mThirdId + "." + appendLanguage() + "\n\n");
            L.d(TAG, "CONNECTED");
            return;
        } else if (message.startsWith("MESSAGE")) {

            String[] msgs = message.split("\n");
            String ws_json = msgs[msgs.length - 1];

            String type = "";
            try {
                JSONObject jsonObject = new JSONObject(ws_json);
                type = jsonObject.getString("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (!"".equals(type)) {
                Message msg = Message.obtain();
                msg.obj = ws_json;
                msg.arg1 = Integer.parseInt(type);

                mSocketHandler.sendMessage(msg);
            }
        }
        hSocketClient.send("\n");

    }

    //心跳时间
    private long pushStartTime;

    private Timer computeWebSocketConnTimer = new Timer();

    /***
     * 计算推送Socket断开重新连接
     */
    private void computeWebSocket() {
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                L.d(TAG, df.format(new Date()) + "---监听socket连接状态:Open=" + hSocketClient.isOpen() + ",Connecting=" + hSocketClient.isConnecting() + ",Close=" + hSocketClient.isClosed() + ",Closing=" + hSocketClient.isClosing());
                long pushEndTime = System.currentTimeMillis();
                if ((pushEndTime - pushStartTime) >= 30000) {
                    L.d(TAG, "重新启动socket");
                    startWebsocket();
                }
            }
        };
        computeWebSocketConnTimer.schedule(tt, 15000, 15000);
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
                L.d(TAG, "===直播事件===" + "msgId=" + webSocketStadiumLiveTextEvent.getData().get("msgId") + ",,,時間" + StadiumUtils.convertStringToInt(webSocketStadiumLiveTextEvent.getData().get("time")) + ",,,msgText=" + webSocketStadiumLiveTextEvent.getData().get("msgText"));


                Map<String, String> data = webSocketStadiumLiveTextEvent.getData();
                MatchTextLiveBean currMatchTextLiveBean = new MatchTextLiveBean(data.get("code"), data.get("msgId"), data.get("msgPlace"), data.get("showId"), data.get("fontStyle"), data.get("time"), data.get("msgText"), data.get("cancelEnNum"), data.get("enNum"), data.get("state"), data.get("homeScore"), data.get("guestScore"));
                if (currMatchTextLiveBean != null) {
                    isLostMsgId(currMatchTextLiveBean.getMsgId());
                }
                allMatchLiveMsgId.add(0, Integer.parseInt(currMatchTextLiveBean.getMsgId()));

                if (currMatchTextLiveBean != null) {
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

        Iterator<MatchTimeLiveBean> iterator = null;
        if (xMatchLive != null && xMatchLive.size() > 0) {
            iterator = xMatchLive.iterator();//时间轴直播数据的iterator
        }
        liveTextTime = StadiumUtils.convertStringToInt(matchTextLiveBean.getTime());

        if (NOTOPEN.equals(matchTextLiveBean.getState())) { //未开
            mLiveHeadInfoFragment.setKeepTime(mContext.getResources().getString(R.string.not_start_txt));
            mDetailsRollballFragment.setLiveTime(mContext.getResources().getString(R.string.not_start_txt));

            isMatchStart = true;
        } else if (FIRSTHALF.equals(matchTextLiveBean.getState()) && StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) <= 45) {
            if (liveTextTime >= matchKeepTime) {
                mLiveHeadInfoFragment.setKeepTime(liveTextTime + "");
                mDetailsRollballFragment.setLiveTime(liveTextTime + "");
            } else {
                if (matchKeepTime > 45) {
                    mLiveHeadInfoFragment.setKeepTime("45+");
                    mDetailsRollballFragment.setLiveTime("45+");
                } else {
                    mLiveHeadInfoFragment.setKeepTime(matchKeepTime + "");
                    mDetailsRollballFragment.setLiveTime(matchKeepTime + "");
                }
            }
            mLiveHeadInfoFragment.setfrequencyText("'");
            isMatchStart = true;
        } else if (FIRSTHALF.equals(matchTextLiveBean.getState()) && StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 45) {
            mLiveHeadInfoFragment.setKeepTime("45+");  //上半场45+
            mDetailsRollballFragment.setLiveTime("45+");  //上半场45+
            mLiveHeadInfoFragment.setfrequencyText("'");
            isMatchStart = false;
        } else if (HALFTIME.equals(matchTextLiveBean.getState())) {  //中场
            mLiveHeadInfoFragment.setKeepTime(mContext.getResources().getString(R.string.pause_txt));
            mDetailsRollballFragment.setLiveTime(mContext.getResources().getString(R.string.pause_txt));
            mLiveHeadInfoFragment.setfrequencyText("");
            isMatchStart = false;
        } else if (SECONDHALF.equals(matchTextLiveBean.getState()) && StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) <= 90) {
            if (liveTextTime >= matchKeepTime) {
                mLiveHeadInfoFragment.setKeepTime(liveTextTime + "");
                mDetailsRollballFragment.setLiveTime(liveTextTime + "");
            } else {
                if (matchKeepTime > 90) {
                    mLiveHeadInfoFragment.setKeepTime("90+");
                    mDetailsRollballFragment.setLiveTime("90+");
                } else {
                    mLiveHeadInfoFragment.setKeepTime(matchKeepTime + "");
                    mDetailsRollballFragment.setLiveTime(matchKeepTime + "");
                }
            }
            mLiveHeadInfoFragment.setfrequencyText("'");
            isMatchStart = true;
        } else if (SECONDHALF.equals(matchTextLiveBean.getState()) && StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 90) {
            mLiveHeadInfoFragment.setKeepTime("90+");
            mDetailsRollballFragment.setLiveTime("90+");
            mLiveHeadInfoFragment.setfrequencyText("'");
            isMatchStart = false;
        }

        switch (matchTextLiveBean.getCode()) {
            case "0":
            case "10":
            case "11":  //开始上半场，开球：
                break;
            case "1"://上半场结束
                //  halfScore.setVisibility(View.VISIBLE);
                //  halfScore.setText("(" + tv_home_score.getText() + ":" + tv_guest_score.getText() + ")");
                mLiveHeadInfoFragment.setfrequencyText("");
                isMatchStart = false;


                // 获取上半场的走势图数据
                homeCorners.clear();
                guestCorners.clear();
                homeDangers.clear();
                guestDangers.clear();
                homeCorners = initMatchTrend("1025", "1050", mStartTime);
                guestCorners = initMatchTrend("2049", "2074", mStartTime);
                homeDangers = initMatchTrend("1026", null, mStartTime);
                guestDangers = initMatchTrend("2050", null, mStartTime);
                homeCorners.add(0, 0);
                guestCorners.add(0, 0);
                homeDangers.add(0, 0);
                guestDangers.add(0, 0);
                mStatisticsFragment.setList(homeCorners, guestCorners, homeDangers, guestDangers);// 刷新攻防走势图
                mStatisticsFragment.initData(mMatchDetail.getLiveStatus());// 刷新攻防走势图

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                break;
            case "12":
            case "13":  //下半场开始
                mLiveHeadInfoFragment.setfrequencyText("'");
                break;
            case "3": //结束下半场
                //socket关闭
                mLiveHeadInfoFragment.setKeepTime(mContext.getResources().getString(R.string.finish_txt));
                mPreHeadInfoFrament.setScoreClolor(mContext.getResources().getColor(R.color.score));
                mLiveHeadInfoFragment.setfrequencyText("");
                // 赛场后更新走势图数据
              /*    isStart = true;
                trendFragment.initData();// 刷新攻防走势图
                statisticsFragment.initData();// 刷新统计




                //点赞不可点
                ((FootballMatchDetailActivity) getActivity()).finishMatch();
*/

                // 获取上半场的走势图数据
                homeCorners.clear();
                guestCorners.clear();
                homeDangers.clear();
                guestDangers.clear();
                homeCorners = initMatchTrend("1025", "1050", mStartTime);
                guestCorners = initMatchTrend("2049", "2074", mStartTime);
                homeDangers = initMatchTrend("1026", null, mStartTime);
                guestDangers = initMatchTrend("2050", null, mStartTime);
                homeCorners.add(0, 0);
                guestCorners.add(0, 0);
                homeDangers.add(0, 0);
                guestDangers.add(0, 0);
                mStatisticsFragment.setList(homeCorners, guestCorners, homeDangers, guestDangers);// 刷新攻防走势图
                mStatisticsFragment.initData("-1");// 刷新攻防走势图

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson("-1");// 刷新统计


                if (hSocketClient != null) {
                    if (!hSocketClient.isClosed()) {
                        hSocketClient.close();
                    }
                }

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
                mPreHeadInfoFrament.setScoreText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score());
                head_score.setText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score());

                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                //x时间轴
                mLiveHeadInfoFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveHeadInfoFragment.showFootballEventByState();
//                if (statisticsFragment.isVisible()) {
//                    statisticsFragment.initData();
//                }

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

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

                mPreHeadInfoFrament.setScoreText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score());
                head_score.setText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score());


                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                //取消进球时间，重绘
                mLiveHeadInfoFragment.cancelFootBallEvent(iterator, matchTextLiveBean);
                mLiveHeadInfoFragment.showFootballEventByState();
                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计


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

                mPreHeadInfoFrament.setScoreText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score());
                head_score.setText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score());


                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                //x时间轴
                mLiveHeadInfoFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveHeadInfoFragment.showFootballEventByState();


                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

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

                mPreHeadInfoFrament.setScoreText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score());
                head_score.setText(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score());

                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                //取消进球时间，重绘
                mLiveHeadInfoFragment.cancelFootBallEvent(iterator, matchTextLiveBean);
                mLiveHeadInfoFragment.showFootballEventByState();


                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                break;

            case "1025": //主队角球
                mathchStatisInfo.setHome_corner(mathchStatisInfo.getHome_corner() + 1);
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                //x时间轴
                mLiveHeadInfoFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveHeadInfoFragment.showFootballEventByState();

                break;

            case "1050": //取消主队角球

                if (mathchStatisInfo.getHome_corner() > 0) {
                    mathchStatisInfo.setHome_corner(mathchStatisInfo.getHome_corner() - 1);
                }
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                //取消进球时间，重绘
                mLiveHeadInfoFragment.cancelFootBallEvent(iterator, matchTextLiveBean);
                mLiveHeadInfoFragment.showFootballEventByState();
                break;

            case "2049": //客队角球
                mathchStatisInfo.setGuest_corner(mathchStatisInfo.getGuest_corner() + 1);
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                //x时间轴
                mLiveHeadInfoFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveHeadInfoFragment.showFootballEventByState();

                break;

            case "2074": //取消客队角球
                if (mathchStatisInfo.getGuest_corner() > 0) {
                    mathchStatisInfo.setGuest_corner(mathchStatisInfo.getGuest_corner() - 1);
                }
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                //取消进球时间，重绘
                mLiveHeadInfoFragment.cancelFootBallEvent(iterator, matchTextLiveBean);
                mLiveHeadInfoFragment.showFootballEventByState();

                break;

            case "1034":   //主队黄牌
                mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() + 1);

                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                //x时间轴
                mLiveHeadInfoFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveHeadInfoFragment.showFootballEventByState();
                break;

            case "1048": //取消 主队黄牌
                if (mathchStatisInfo.getHome_yc() > 0) {
                    mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() - 1);
                }

                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                //取消进球时间，重绘
                mLiveHeadInfoFragment.cancelFootBallEvent(iterator, matchTextLiveBean);
                mLiveHeadInfoFragment.showFootballEventByState();
                break;

            case "1045": //主队两黄变一红

                mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() + 1);
                mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() + 1);
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                //x时间轴
                mLiveHeadInfoFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveHeadInfoFragment.showFootballEventByState();
                break;

            case "1046": //取消主队黄牌过度到红牌
                if (mathchStatisInfo.getHome_yc() > 0 && mathchStatisInfo.getHome_rc() > 0) {
                    mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() - 1);
                    mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() - 1);
                }
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                //取消进球时间，重绘
                mLiveHeadInfoFragment.cancelFootBallEvent(iterator, matchTextLiveBean);
                mLiveHeadInfoFragment.showFootballEventByState();
                break;


            case "2058":  //客队黄牌
                mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() + 1);
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                //x时间轴
                mLiveHeadInfoFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveHeadInfoFragment.showFootballEventByState();
                break;

            case "2072":  //取消客队黄牌
                if (mathchStatisInfo.getGuest_yc() > 0) {
                    mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() - 1);
                }
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                //取消进球时间，重绘
                mLiveHeadInfoFragment.cancelFootBallEvent(iterator, matchTextLiveBean);
                mLiveHeadInfoFragment.showFootballEventByState();
                break;


            case "2069": //客队两黄变一红
                mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() + 1);
                mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() + 1);
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                //x时间轴
                mLiveHeadInfoFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveHeadInfoFragment.showFootballEventByState();
                break;

            case "2070": //取消客队两黄变一红

                if (mathchStatisInfo.getGuest_yc() > 0 && mathchStatisInfo.getGuest_rc() > 0) {
                    mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() - 1);
                    mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() - 1);
                }
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                //取消进球时间，重绘
                mLiveHeadInfoFragment.cancelFootBallEvent(iterator, matchTextLiveBean);
                mLiveHeadInfoFragment.showFootballEventByState();
                break;

            case "1032":  //主队红牌
                mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() + 1);
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                //x时间轴
                mLiveHeadInfoFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveHeadInfoFragment.showFootballEventByState();
                break;
            case "1047": //取消主队红牌
                if (mathchStatisInfo.getHome_rc() > 0) {
                    mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() - 1);
                }
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                //取消进球时间，重绘
                mLiveHeadInfoFragment.cancelFootBallEvent(iterator, matchTextLiveBean);
                mLiveHeadInfoFragment.showFootballEventByState();

                break;

            case "2056":  //客队红牌
                mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() + 1);
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                //x时间轴
                mLiveHeadInfoFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveHeadInfoFragment.showFootballEventByState();
                break;

            case "2071":  //取消客队红牌
                if (mathchStatisInfo.getGuest_rc() > 0) {
                    mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() - 1);
                }
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                //取消进球时间，重绘
                mLiveHeadInfoFragment.cancelFootBallEvent(iterator, matchTextLiveBean);
                mLiveHeadInfoFragment.showFootballEventByState();
                break;

            case "1026":    //主队危险进攻
                mathchStatisInfo.setHome_danger(mathchStatisInfo.getHome_danger() + 1);
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                break;

            case "2050":  //客队危进攻
                mathchStatisInfo.setGuest_danger(mathchStatisInfo.getGuest_danger() + 1);
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                break;

            case "1039":    //主队射正球门

                mathchStatisInfo.setHome_shoot_correct(mathchStatisInfo.getHome_shoot_correct() + 1);

                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());


                //客队扑救=主队射正-主队比分
                mathchStatisInfo.setGuest_rescue(mathchStatisInfo.getHome_shoot_correct() - mathchStatisInfo.getHome_score());



               /* if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }*/


                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                break;


            case "1024":
                mathchStatisInfo.setHome_attack(mathchStatisInfo.getHome_attack() + 1);
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);
                break;

            case "2048":
                mathchStatisInfo.setGuest_attack(mathchStatisInfo.getGuest_attack() + 1);
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);
                break;

            case "2063":  //客队射正球门
                mathchStatisInfo.setGuest_shoot_correct(mathchStatisInfo.getGuest_shoot_correct() + 1);
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                mathchStatisInfo.setGuest_shoot_door(mathchStatisInfo.getGuest_shoot_correct() + mathchStatisInfo.getGuest_shoot_miss());

                //主队扑救=客队射正-客队比分
               /* mathchStatisInfo.setHome_rescue(mathchStatisInfo.getGuest_shoot_correct() - mathchStatisInfo.getGuest_score());
                if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }*/


                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                break;

            case "1040":    //主队射偏球门
                mathchStatisInfo.setHome_shoot_miss(mathchStatisInfo.getHome_shoot_miss() + 1);


                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());

               /* if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }*/

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计


                //如果有变动就刷新统计数据
                break;
            case "1041":    //主队射偏球门
                mathchStatisInfo.setHome_shoot_miss(mathchStatisInfo.getHome_shoot_miss() + 1);
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());

               /* if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }*/

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                //如果有变动就刷新统计数据
                break;


            case "2064":  //客队射偏球门
                mathchStatisInfo.setGuest_shoot_miss(mathchStatisInfo.getGuest_shoot_miss() + 1);

                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                mathchStatisInfo.setGuest_shoot_door(mathchStatisInfo.getGuest_shoot_correct() + mathchStatisInfo.getGuest_shoot_miss());

               /* if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }*/

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计


                //如果有变动就刷新统计数据
                break;
            case "2065":  //客队门框
                mathchStatisInfo.setGuest_shoot_miss(mathchStatisInfo.getGuest_shoot_miss() + 1);

                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                mathchStatisInfo.setGuest_shoot_door(mathchStatisInfo.getGuest_shoot_correct() + mathchStatisInfo.getGuest_shoot_miss());

                /*if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }
                */

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                //如果有变动就刷新统计数据
                break;


            case "1055"://主队换人
                //addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                // showFootballEvent();//显示事件
                // showFootballEventByState();
                break;

            case "2079": //客队换人
                // addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                // showFootballEvent();//显示事件
                // showFootballEventByState();
                break;


            case "1043"://主队越位
                mathchStatisInfo.setHome_away(mathchStatisInfo.getHome_away() + 1);
               /* if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }*/

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计


                //如果有变动就刷新统计数据
                break;

            case "1027": //主队任意球
                mathchStatisInfo.setHome_free_kick(mathchStatisInfo.getHome_free_kick() + 1);
               /* if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }*/

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计


                //如果有变动就刷新统计数据
                break;

            case "2067":  //客队越位
                mathchStatisInfo.setGuest_away(mathchStatisInfo.getGuest_away() + 1);
               /* if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }*/

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                //如果有变动就刷新统计数据
                break;
            case "2051"://客队任意球
                mathchStatisInfo.setGuest_free_kick(mathchStatisInfo.getGuest_free_kick() + 1);
               /* if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据*/

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                break;

            case "1042"://主队犯规
                mathchStatisInfo.setHome_foul(mathchStatisInfo.getHome_foul() + 1);
               /* if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据*/

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                break;
            case "1054"://主队界外球
                mathchStatisInfo.setHome_lineOut(mathchStatisInfo.getHome_lineOut() + 1);
             /*   if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据*/

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                break;
            case "2066"://客队犯规
                mathchStatisInfo.setGuest_foul(mathchStatisInfo.getGuest_foul() + 1);
              /*  if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据*/

                mStatisticsFragment.setMathchStatisInfo(mathchStatisInfo);
                mStatisticsFragment.initJson(mMatchDetail.getLiveStatus());// 刷新统计

                break;
            case "2078"://客队界外球
                mathchStatisInfo.setGuest_lineOut(mathchStatisInfo.getGuest_lineOut() + 1);
              /*  if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据*/

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
            matchLive.add(0, new MatchTextLiveBean("", "", "0", "0", "4", "99999999", mContext.getResources().getString(R.string.matchFinished_txt), "", "", "0", "", ""));
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

        mLiveHeadInfoFragment.showTimeView(mKeepTime); //刷新时间轴

        if (isMatchStart) {
            if (liveTextTime >= matchKeepTime) {
                mLiveHeadInfoFragment.setKeepTime(liveTextTime + "");
            } else {
                if (FIRSTHALF.equals(state) && matchKeepTime <= 45) {
                    mLiveHeadInfoFragment.setKeepTime(matchKeepTime + "");
                } else if (FIRSTHALF.equals(state) && matchKeepTime > 45) {
                    mLiveHeadInfoFragment.setKeepTime("45+");

                } else if (SECONDHALF.equals(state) && matchKeepTime <= 90) {
                    mLiveHeadInfoFragment.setKeepTime(matchKeepTime + "");

                } else if (SECONDHALF.equals(state) && matchKeepTime > 90) {
                    mLiveHeadInfoFragment.setKeepTime("90+");
                }
            }

            mStartTime = StadiumUtils.convertStringToInt(data.get("time"));
            switch (mStartTime) {
                case 15:
                case 30:
                case 45:
                case 60:
                case 75:
                case 90:
                    homeCorners.clear();
                    guestCorners.clear();
                    homeDangers.clear();
                    guestDangers.clear();
                    homeCorners = initMatchTrend("1025", "1050", mStartTime);
                    guestCorners = initMatchTrend("2049", "2074", mStartTime);
                    homeDangers = initMatchTrend("1026", null, mStartTime);
                    guestDangers = initMatchTrend("2050", null, mStartTime);
                    homeCorners.add(0, 0);
                    guestCorners.add(0, 0);
                    homeDangers.add(0, 0);
                    guestDangers.add(0, 0);
                    mStatisticsFragment.setList(homeCorners, guestCorners, homeDangers, guestDangers);// 刷新攻防走势图
                    mStatisticsFragment.initData(mMatchDetail.getLiveStatus());
                    break;
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
                eventBusPost();

                // setResult(Activity.RESULT_OK);
                finish();
                break;

            case R.id.iv_setting:  //关注、分享
                popWindow(iv_setting);
                break;
            case R.id.reLoading_details:
                loadData();
                break;
            default:
                break;
        }
    }

    private void popWindow(View v) {
        final View mView = View.inflate(getApplicationContext(), R.layout.football_details, null);

        boolean isFocus = FocusFragment.isFocusId(mThirdId);

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

        popupWindow.showAsDropDown(v);


        (mView.findViewById(R.id.football_item_focus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "足球！", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                if (FocusFragment.isFocusId(mThirdId)) {
                    FocusFragment.deleteFocusId(mThirdId);
                    ((ImageView) mView.findViewById(R.id.football_item_focus_iv)).setImageResource(R.mipmap.head_nomal);
                    ((TextView) mView.findViewById(R.id.football_item_focus_tv)).setText(getString(R.string.foot_details_focus));

                } else {
                    FocusFragment.addFocusId(mThirdId);
                    ((ImageView) mView.findViewById(R.id.football_item_focus_iv)).setImageResource(R.mipmap.head_focus);
                    ((TextView) mView.findViewById(R.id.football_item_focus_tv)).setText(getString(R.string.foot_details_focused));

                }
            }
        });


        iv_share = mView.findViewById(R.id.football_item_share);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

                if (mMatchDetail == null) {
                    return;
                }

                ShareBean shareBean = new ShareBean();
                String title = mMatchDetail.getHomeTeamInfo().getName() + " VS " + mMatchDetail.getGuestTeamInfo().getName();
                String summary = getString(R.string.share_summary);
                shareBean.setTitle(title != null ? title : getString(R.string.share_to_qq_app_name));
                shareBean.setSummary(summary);
                shareBean.setTarget_url(URL_DEFAULT);
                shareBean.setImage_url(shareHomeIconUrl != null ? shareHomeIconUrl : "");
                shareBean.setCopy(URL_DEFAULT);
                mShareFragment = ShareFragment.newInstance(shareBean);
                mShareFragment.show(getSupportFragmentManager(), "bottomShare");
            }
        });

    }


    @Override
    public void onClose(String message) {
    }

    @Override
    public void onError(Exception exception) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            eventBusPost();

            // setResult(Activity.RESULT_OK);

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void eventBusPost() {
        if (currentFragmentId == IMMEDIA_FRAGMENT) {
            if (ImmediateFragment.imEventBus != null) {
                ImmediateFragment.imEventBus.post("");
            }
        } else if (currentFragmentId == RESULT_FRAGMENT) {
            if (ResultFragment.resultEventBus != null) {
                ResultFragment.resultEventBus.post("");
            }
        } else if (currentFragmentId == SCHEDULE_FRAGMENT) {
            if (ScheduleFragment.schEventBus != null) {
                ScheduleFragment.schEventBus.post("");
            }
        } else if (currentFragmentId == FOCUS_FRAGMENT) {
            if (FocusFragment.focusEventBus != null) {
                FocusFragment.focusEventBus.post("");
            }
        }
    }


    public void setScroller() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            MyScroller mScroller = new MyScroller(mHeadviewpager.getContext(),
                    new AccelerateInterpolator());
            mField.set(mHeadviewpager, mScroller);
        } catch (Exception ee) {
            L.d("Exception: " + ee.getMessage());
        }
    }

    class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context);
        }

        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy,
                                int duration) {
            super.startScroll(startX, startY, dx, dy, BANNER_ANIM_TIME);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, BANNER_ANIM_TIME);
        }
    }

}
