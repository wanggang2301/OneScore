package com.hhly.mlottery.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.BasePagerAdapter;
import com.hhly.mlottery.adapter.football.FragmentAdapter;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.bean.footballDetails.MatchDetail;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;
import com.hhly.mlottery.bean.footballDetails.MatchTimeLiveBean;
import com.hhly.mlottery.bean.footballDetails.MathchStatisInfo;
import com.hhly.mlottery.bean.footballDetails.PreLiveText;
import com.hhly.mlottery.bean.websocket.WebSocketStadiumKeepTime;
import com.hhly.mlottery.bean.websocket.WebSocketStadiumLiveTextEvent;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.footframe.AnalyzeFragment;
import com.hhly.mlottery.frame.footframe.OldAnalyzeFragment;
import com.hhly.mlottery.frame.footframe.DetailsRollballFragment;
import com.hhly.mlottery.frame.footframe.LiveHeadInfoFragment;
import com.hhly.mlottery.frame.footframe.OddsFragment;
import com.hhly.mlottery.frame.footframe.PreHeadInfoFrament;
import com.hhly.mlottery.frame.footframe.StatisticsFragmentTest;
import com.hhly.mlottery.frame.footframe.TalkAboutBallFragment;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.FootballLiveTextComparator;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.StadiumUtils;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.websocket.HappySocketClient;

import org.java_websocket.drafts.Draft_17;
import org.json.JSONException;
import org.json.JSONObject;

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

/**
 * @author wang gang
 * @date 2016/6/2 16:53
 * @des 足球内页改版
 */
public class FootballMatchDetailActivityTest extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, SwipeRefreshLayout.OnRefreshListener, HappySocketClient.SocketResponseErrorListener, HappySocketClient.SocketResponseCloseListener, HappySocketClient.SocketResponseMessageListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FragmentManager fragmentManager;
    private ViewPager mHeadviewpager;
    private ViewPager mViewPager;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private FragmentAdapter fragmentAdapter;
    private BasePagerAdapter basePagerAdapter;
    private TabLayout mTabLayout;
    private TabsAdapter mTabsAdapter;
    private Toolbar toolbar;

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

    private final static String TAG = "FootballMatchDetailActivity";
    private String[] titles = {"滚球", "聊球", "分析", "指数", "统计"};

    private boolean isMatchStart = false;  //判断比赛推送时间状态

    private PreHeadInfoFrament mPreHeadInfoFrament;
    private LiveHeadInfoFragment mLiveHeadInfoFragment;

    private DetailsRollballFragment mDetailsRollballFragment; //滚球

    private TalkAboutBallFragment mTalkAboutBallFragment;

    private AnalyzeFragment mAnalyzeFragment;  //分析
    private OddsFragment mOddsFragment;         //指数

    private StatisticsFragmentTest mStatisticsFragment;  //统计


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


        mPreHeadInfoFrament = PreHeadInfoFrament.newInstance();
        mLiveHeadInfoFragment = new LiveHeadInfoFragment().newInstance();

        basePagerAdapter.addFragments(mPreHeadInfoFrament, mLiveHeadInfoFragment);
        mHeadviewpager.setAdapter(basePagerAdapter);
        mHeadviewpager.setCurrentItem(0);

//

        loadData();

        try {
            hSocketUri = new URI(BaseURLs.WS_SERVICE);
            System.out.println(">>>>>" + hSocketUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mHeadviewpager = (ViewPager) findViewById(R.id.headviewpager);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        appBarLayout.addOnOffsetChangedListener(this);
        fragmentManager = getSupportFragmentManager();


        basePagerAdapter = new BasePagerAdapter(fragmentManager);

        //底部ViewPager(滚球、指数等)
        mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
        mTabsAdapter.setTitles(titles);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if ((-verticalOffset) == appBarLayout.getTotalScrollRange()) {
            mCollapsingToolbarLayout.setTitle(toolbarTitle);
            mCollapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_HORIZONTAL);
        } else {
            mCollapsingToolbarLayout.setTitle("");
        }

        if (mCollapsingToolbarLayout.getHeight() +
                verticalOffset < mHeadviewpager.getHeight()) {
            mSwipeRefreshLayout.setEnabled(false);
        } else {
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    @Override
    public void onRefresh() {

        L.d("www", "刷新");
    }

    private void loadData() {
        Map<String, String> params = new HashMap<>();
        params.put("thirdId", mThirdId);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_INFO, params, new VolleyContentFast.ResponseSuccessListener<MatchDetail>() {
            @Override
            public void onResponse(MatchDetail matchDetail) {

                if (!"200".equals(matchDetail.getResult())) {
                    //mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
                    return;
                }

                mMatchDetail = matchDetail;

                if (!isInitedViewPager) {//如果ViewPager未初始化
                    initViewPager(matchDetail);
                    mPreStatus = matchDetail.getLiveStatus();
                } else {


                    if ("0".equals(mPreStatus) && "0".equals(matchDetail.getLiveStatus()) && !isFinishing()) {//如果上一个状态是赛前，目前状态也是赛前，更新页面数据即可
                        // mStadiumFragment.setMatchDetail(matchDetail);
                        // mStadiumFragment.initPreData();
                    } else if ("0".equals(mPreStatus) && "1".equals(matchDetail.getLiveStatus()) && !isFinishing()) {//从赛前跳到赛中
                        // mStadiumFragment.setMatchDetail(matchDetail);
                        //  mStadiumFragment.activateMatch();
                        //  mReloadTimer.cancel();
                    } else if ("1".equals(mPreStatus) && "1".equals(matchDetail.getLiveStatus()) && !isFinishing()) {
                        // mStadiumFragment.refreshStadiumData(matchDetail);
                    }
                }

              /*  mHomeNameText.setText(matchDetail.getHomeTeamInfo().getName());
                mHomeLikeCount.setText(matchDetail.getHomeTeamInfo().getGas());
                mHeaderHomeNameText.setText(matchDetail.getHomeTeamInfo().getName());

                if (matchDetail.getMatchType1() == null && matchDetail.getMatchType2() == null) {
                    mMatchTypeLayout.setVisibility(View.GONE);
                    mVSText.setVisibility(View.VISIBLE);
                } else {

                    if (StringUtils.isEmpty(matchDetail.getMatchType1())) {
                        mMatchType1.setVisibility(View.GONE);
                    }

                    if (StringUtils.isEmpty(matchDetail.getMatchType2())) {
                        mMatchType2.setVisibility(View.GONE);
                    }
                    mMatchType1.setText(StringUtils.nullStrToEmpty(matchDetail.getMatchType1()));
                    mMatchType2.setText(StringUtils.nullStrToEmpty(matchDetail.getMatchType2()));
                    mMatchTypeLayout.setVisibility(View.VISIBLE);
                    mVSText.setVisibility(View.GONE);
                }
                mGuestNameText.setText(matchDetail.getGuestTeamInfo().getName());
                mGuestLikeCount.setText(matchDetail.getGuestTeamInfo().getGas());
                mHeaderGuestNameText.setText(matchDetail.getGuestTeamInfo().getName());

                shareHomeIconUrl = matchDetail.getHomeTeamInfo().getUrl();

                loadImage(matchDetail.getHomeTeamInfo().getUrl(), mHomeEmblem);
                loadImage(matchDetail.getGuestTeamInfo().getUrl(), mGuestEmblem);
                mTab1.setClickable(true);
                mTab2.setClickable(true);
                mTab3.setClickable(true);

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

                if ("-1".equals(matchDetail.getLiveStatus())) {
                    setClickableLikeBtn(false);
                } else {
                    setClickableLikeBtn(true);
                }

                mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);*/

                if (BEFOURLIVE.equals(mMatchDetail.getLiveStatus()) || ONLIVE.equals(mMatchDetail.getLiveStatus())) {
                    startWebsocket();
                    computeWebSocket();
                }


            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                //   mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
            }
        }, MatchDetail.class);
    }


    private void initViewPager(MatchDetail matchDetail) {

        //初始化头部ViewPager
        // mViewPager = (ViewPager) findViewById(R.id.headviewpager);


        //  mPreHeadInfoFrament = PreHeadInfoFrament.newInstance(matchDetail, 1);

        if ("0".equals(matchDetail.getLiveStatus())) { //赛前

            mPreHeadInfoFrament.initData(matchDetail);




          /*  basePagerAdapter.addFragments(mPreHeadInfoFrament);
            mHeadviewpager.setAdapter(basePagerAdapter);
            mHeadviewpager.setCurrentItem(0);*/

            mPreHeadInfoFrament.setScoreText("VS");
            toolbarTitle = matchDetail.getHomeTeamInfo().getName() + " VS " + matchDetail.getGuestTeamInfo().getName();


            mDetailsRollballFragment = DetailsRollballFragment.newInstance(DetailsRollballFragment.DETAILSROLLBALL_TYPE_PRE, matchDetail);

        } else {
            //赛中和赛后头部
            mPreHeadInfoFrament.initData(matchDetail);
            mLiveHeadInfoFragment.initData(matchDetail);


            matchLive = mMatchDetail.getMatchInfo().getMatchLive();
            allMatchLiveMsgId = new ArrayList<>();


            //完场
            if (LIVEENDED.equals(mMatchDetail.getLiveStatus())) {


                mPreHeadInfoFrament.setScoreText(mMatchDetail.getHomeTeamInfo().getScore() + ":" + mMatchDetail.getGuestTeamInfo().getScore());
                mPreHeadInfoFrament.setScoreClolor(mContext.getResources().getColor(R.color.score));
                toolbarTitle = mMatchDetail.getHomeTeamInfo().getName() + " " + mMatchDetail.getHomeTeamInfo().getScore() + "-" + mMatchDetail.getGuestTeamInfo().getScore() + " " + mMatchDetail.getGuestTeamInfo().getName();
                mLiveHeadInfoFragment.initMatchOverData(mMatchDetail);
                mKeepTime = "5400000";//90分钟的毫秒数
            } else {//未完场头部
                initMatchSatisInfo();
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
                    mLiveHeadInfoFragment.setfrequencyText("");
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
            }

            //赛后赛中下面赔率显示
            mDetailsRollballFragment = DetailsRollballFragment.newInstance(DetailsRollballFragment.DETAILSROLLBALL_TYPE_ING, matchDetail);
        }

        mTalkAboutBallFragment = new TalkAboutBallFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param1", mThirdId);
        mTalkAboutBallFragment.setArguments(bundle);


        mAnalyzeFragment = AnalyzeFragment.newInstance(mThirdId,"");
        mOddsFragment = OddsFragment.newInstance("", "");
        mStatisticsFragment = StatisticsFragmentTest.newInstance();

        mTabsAdapter.addFragments(mDetailsRollballFragment,mTalkAboutBallFragment , mAnalyzeFragment, mOddsFragment, mStatisticsFragment);
        mViewPager.setOffscreenPageLimit(2);//设置预加载页面的个数。
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        isInitedViewPager = true;

    }

    /**
     * 统计赛中红黄牌等信息
     */
    private MathchStatisInfo initMatchSatisInfo() {
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
                default:
                    break;
            }
        }

        mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());
        mathchStatisInfo.setGuest_shoot_door(mathchStatisInfo.getGuest_shoot_correct() + mathchStatisInfo.getGuest_shoot_miss());
        mathchStatisInfo.setHome_rescue(mathchStatisInfo.getGuest_shoot_correct() - mathchStatisInfo.getGuest_score());
        mathchStatisInfo.setGuest_rescue(mathchStatisInfo.getHome_shoot_correct() - mathchStatisInfo.getHome_score());

        return mathchStatisInfo;
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        computeWebSocketConnTimer.cancel();

        if (hSocketClient != null) {
            hSocketClient.close();
        }
    }

    /***
     * 开始socket
     */
    private synchronized void startWebsocket() {
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
        L.i("1029", "---onMessage---推送比赛thirdId==" + mThirdId);

        pushStartTime = System.currentTimeMillis(); // 记录起始时间
        L.i("1029", "心跳时间" + pushStartTime);
        if (message.startsWith("CONNECTED")) {
            String id = "android" + DeviceInfo.getDeviceId(mContext);
            id = MD5Util.getMD5(id);
            if (mContext == null) {
                return;
            }
            hSocketClient.send("SUBSCRIBE\nid:" + id + "\ndestination:/topic/USER.topic.liveEvent." + mThirdId + "." + appendLanguage() + "\n\n");
            L.i("1029", "CONNECTED");
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

    private void computeWebSocket() {
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                L.i("1029", df.format(new Date()) + "---监听socket连接状态:Open=" + hSocketClient.isOpen() + ",Connecting=" + hSocketClient.isConnecting() + ",Close=" + hSocketClient.isClosed() + ",Closing=" + hSocketClient.isClosing());
                long pushEndTime = System.currentTimeMillis();
                if ((pushEndTime - pushStartTime) >= 30000) {
                    L.i("1029", "重新启动socket");
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

                L.i("1029", "=======直播時間=====" + StadiumUtils.convertStringToInt(webSocketStadiumKeepTime.getData().get("time")));

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

                //  L.i("1029",ws_json);
                L.i("1029", "===直播事件===" + "msgId=" + webSocketStadiumLiveTextEvent.getData().get("msgId") + ",,,時間" + StadiumUtils.convertStringToInt(webSocketStadiumLiveTextEvent.getData().get("time")) + ",,,msgText=" + webSocketStadiumLiveTextEvent.getData().get("msgText"));


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
            /*    homeCorners.clear();
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
                trendFragment.initData();// 刷新攻防走势图
                statisticsFragment.initData();// 刷新统计*/
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
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);




                //x时间轴
                mLiveHeadInfoFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveHeadInfoFragment.showFootballEventByState();
//                if (statisticsFragment.isVisible()) {
//                    statisticsFragment.initData();
//                }
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
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                //取消进球时间，重绘
                mLiveHeadInfoFragment.cancelFootBallEvent(iterator, matchTextLiveBean);
                mLiveHeadInfoFragment.showFootballEventByState();


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
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                //x时间轴
                mLiveHeadInfoFragment.addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                mLiveHeadInfoFragment.showFootballEventByState();
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
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);

                //取消进球时间，重绘
                mLiveHeadInfoFragment.cancelFootBallEvent(iterator, matchTextLiveBean);
                mLiveHeadInfoFragment.showFootballEventByState();
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

                break;

            case "1040":    //主队射偏球门
                mathchStatisInfo.setHome_shoot_miss(mathchStatisInfo.getHome_shoot_miss() + 1);


                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());

               /* if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }*/

                //如果有变动就刷新统计数据
                break;
            case "1041":    //主队射偏球门
                mathchStatisInfo.setHome_shoot_miss(mathchStatisInfo.getHome_shoot_miss() + 1);
                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());

               /* if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }*/

                //如果有变动就刷新统计数据
                break;


            case "2064":  //客队射偏球门
                mathchStatisInfo.setGuest_shoot_miss(mathchStatisInfo.getGuest_shoot_miss() + 1);

                mLiveHeadInfoFragment.initMatchNowData(mathchStatisInfo);


                mathchStatisInfo.setGuest_shoot_door(mathchStatisInfo.getGuest_shoot_correct() + mathchStatisInfo.getGuest_shoot_miss());

               /* if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }*/


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


                //如果有变动就刷新统计数据
                break;

            case "1027": //主队任意球
                mathchStatisInfo.setHome_free_kick(mathchStatisInfo.getHome_free_kick() + 1);
               /* if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }*/


                //如果有变动就刷新统计数据
                break;

            case "2067":  //客队越位
                mathchStatisInfo.setGuest_away(mathchStatisInfo.getGuest_away() + 1);
               /* if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }*/

                //如果有变动就刷新统计数据
                break;
            case "2051"://客队任意球
                mathchStatisInfo.setGuest_free_kick(mathchStatisInfo.getGuest_free_kick() + 1);
               /* if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据*/
                break;

            case "1042"://主队犯规
                mathchStatisInfo.setHome_foul(mathchStatisInfo.getHome_foul() + 1);
               /* if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据*/
                break;
            case "1054"://主队界外球
                mathchStatisInfo.setHome_lineOut(mathchStatisInfo.getHome_lineOut() + 1);
             /*   if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据*/
                break;
            case "2066"://客队犯规
                mathchStatisInfo.setGuest_foul(mathchStatisInfo.getGuest_foul() + 1);
              /*  if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据*/
                break;
            case "2078"://客队界外球
                mathchStatisInfo.setGuest_lineOut(mathchStatisInfo.getGuest_lineOut() + 1);
              /*  if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据*/
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
}
