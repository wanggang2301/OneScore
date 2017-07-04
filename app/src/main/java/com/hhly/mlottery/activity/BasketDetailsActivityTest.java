package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.bean.BarrageBean;
import com.hhly.mlottery.bean.basket.BasketballDetailsBean;
import com.hhly.mlottery.bean.basket.basketdetails.BasketEachTextLiveBean;
import com.hhly.mlottery.bean.footballDetails.DetailsCollectionCountBean;
import com.hhly.mlottery.bean.multiplebean.MultipleByValueBean;
import com.hhly.mlottery.bean.websocket.DataEntity;
import com.hhly.mlottery.bean.websocket.WebSocketBasketBallDetails;
import com.hhly.mlottery.callback.BasketTeamParams;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BaseUserTopics;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.basketballframe.BasketAnalyzeFragment;
import com.hhly.mlottery.frame.basketballframe.BasketFocusEventBus;
import com.hhly.mlottery.frame.basketballframe.BasketLiveFragment;
import com.hhly.mlottery.frame.basketballframe.BasketOddsFragment;
import com.hhly.mlottery.frame.basketballframe.BasketTextLiveEvent;
import com.hhly.mlottery.frame.basketballframe.MyRotateAnimation;
import com.hhly.mlottery.frame.chartBallFragment.ChartBallFragment;
import com.hhly.mlottery.frame.footballframe.eventbus.BasketDetailsEventBusEntity;
import com.hhly.mlottery.util.CountDown;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.FocusUtils;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.NetworkUtils;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.CustomDetailsEvent;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.BarrageView;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


/**
 * @author yixq
 *         Created by A on 2016/3/21.
 * @Description: 篮球详情的 Activity
 */
public class BasketDetailsActivityTest extends BaseWebSocketActivity implements ExactSwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    public final static String BASKET_THIRD_ID = "thirdId";
    public final static String BASKET_MATCH_STATUS = "MatchStatus";
    public final static String BASKET_MATCH_LEAGUEID = "leagueId";
    public final static String BASKET_MATCH_MATCHTYPE = "matchType";

    /**
     * 欧赔
     */
    public static final String ODDS_EURO = "euro";
    /**
     * 亚盘
     */
    public static final String ODDS_LET = "asiaLet";
    /**
     * 大小球
     */
    public static final String ODDS_SIZE = "asiaSize";
    public static String mThirdId = "936707";
    private Context mContext;


    BasketLiveFragment mBasketLiveFragment;

    BasketAnalyzeFragment mAnalyzeFragment = new BasketAnalyzeFragment();
    ChartBallFragment mChartBallFragment;

    BasketOddsFragment mOddsEuro;
    BasketOddsFragment mOddsLet;
    BasketOddsFragment mOddsSize;


    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabsAdapter mTabsAdapter;
    private String[] TITLES;

    // 状部view
    private ImageView mHeadImage;
    private ImageView mHomeIcon;
    private ImageView mGuestIcon;
    private TextView mHomeTeam;
    private TextView mGuestTeam;
    private TextView mHomeRanking;
    private TextView mGuestRanking;
    private TextView mLeagueName;
    private TextView mVS;
    private TextView mHomeScore;
    private TextView mGuestScore;
    private TextView mMatchState;
    private TextView mRemainTime;
    private TextView mApos;
    private TextView mGuest1;
    private TextView mGuest2;
    private TextView mGuest3;
    private TextView mGuest4;
    private TextView mHome1;
    private TextView mHome2;
    private TextView mHome3;
    private TextView mHome4;
    private LinearLayout mLayoutOt1;
    private LinearLayout mLayoutOt2;
    private LinearLayout mLayoutOt3;
    private TextView mGuestOt1;
    private TextView mGuestOt2;
    private TextView mGuestOt3;
    private TextView mHomeOt1;
    private TextView mHomeOt2;
    private TextView mHomeOt3;
    private TextView mSmallGuestScore;
    private TextView mSmallHomeScore;
    private LinearLayout btn_showGif;

    /**
     * 收藏按钮
     */
    private ImageView mCollect;

    private int mCurrentId;
    private final int IMMEDIA_FRAGMENT = 0;
    private final int RESULT_FRAGMENT = 1;
    private final int SCHEDULE_FRAGMENT = 2;
    private final int FOCUS_FRAGMENT = 3;
    private final int CUSTOM_FRAGMENT = 4;

    private ExactSwipeRefreshLayout mRefreshLayout; //下拉刷新

    private String mLeagueId; // 联赛ID
    private Integer mMatchType; //联赛类型

    private static final String LEAGUEID_NBA = "1";


    private boolean isNBA = false;

    public static String homeIconUrl;
    public static String guestIconUrl;


    private Timer gifTimer;

    private TimerTask gifTimerTask;

    private boolean isFirstShowGif = true;

    private RelativeLayout rl_gif_notice;

    private int gifCount = 0;

    private CountDown countDown;
    private final int MILLIS_INFuture = 3000;//倒计时3秒
    private final String MATCH_TYPE = "2"; //篮球
    private final int GIFPERIOD_2 = 1000 * 5;//刷新周期两分钟

    private BarrageView barrage_view;
    private ImageView barrage_switch;
    boolean barrage_isFocus = false;
    private int chartBallView = -1;// 聊球界面转标记

    @BindView(R.id.rl_iv)
    RelativeLayout mLayoutGuestIcon;
    @BindView(R.id.ll_guest)
    LinearLayout mLayoutGuestName;
    @BindView(R.id.rl_iv2)
    RelativeLayout mLayoutHomeIcon;
    @BindView(R.id.linearLayout2)
    LinearLayout mLayoutHomeName;


    /**
     * 请求数据之后展示
     */
    //    0:未开赛,1:一节,2:二节,5:1'OT，以此类推
//            -1:完场,-2:待定,-3:中断,-4:取消,-5:推迟,50中场
    private final static int PRE_MATCH = 0;//赛前
    private final static int FIRST_QUARTER = 1;
    private final static int SECOND_QUARTER = 2;
    private final static int THIRD_QUARTER = 3;
    private final static int FOURTH_QUARTER = 4;
    private final static int OT1 = 5;
    private final static int END = -1;
    private final static int OT2 = 6;
    private final static int OT3 = 7;
    private final static int DETERMINED = -2;//待定
    private final static int GAME_CUT = -3;
    private final static int GAME_CANCLE = -4;
    private final static int GAME_DELAY = -5;
    private final static int HALF_GAME = 50;

    private int mGuestNum = 0;
    private int mHomeNum = 0;
    BasketballDetailsBean.MatchEntity mMatch;

    private TextView tv_addMultiView;
    private boolean isAddMultiViewHide = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent() != null && getIntent().getExtras() != null) {
            mThirdId = getIntent().getExtras().getString(BASKET_THIRD_ID);
            mLeagueId = getIntent().getExtras().getString(BASKET_MATCH_LEAGUEID);
            mMatchType = getIntent().getExtras().getInt(BASKET_MATCH_MATCHTYPE);
            chartBallView = getIntent().getExtras().getInt("chart_ball_view");

            if (LEAGUEID_NBA.equals(mLeagueId)) {
                isNBA = true;
            }

            if (isNBA) {
                mBasketLiveFragment = BasketLiveFragment.newInstance();
            }

            mOddsEuro = BasketOddsFragment.newInstance(mThirdId, ODDS_EURO);
            mOddsLet = BasketOddsFragment.newInstance(mThirdId, ODDS_LET);
            mOddsSize = BasketOddsFragment.newInstance(mThirdId, ODDS_SIZE);
            mChartBallFragment = ChartBallFragment.newInstance(1, mThirdId);

            mCurrentId = getIntent().getExtras().getInt("currentfragment");
        }
        EventBus.getDefault().register(this);
        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic(BaseUserTopics.basketballScore + "." + mThirdId + "." + appendLanguage());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_details_activity_test);
        mContext = this;

        ButterKnife.bind(this);

        initView();
        loadData();
        initEvent();
        pollingGifCount();
    }

    public String getmThirdId() {
        return mThirdId;
    }

    public String getmLeagueId() {
        return mLeagueId;
    }

    public Integer getmMatchType() {
        return mMatchType;
    }

    /**
     * 初始化界面
     */
    private void initView() {
        if (isNBA) {
            TITLES = new String[]{getResources().getString(R.string.basket_live), getResources().getString(R.string.basket_analyze), getResources().getString(R.string.basket_alet), getResources().getString(R.string.basket_analyze_sizeof), getResources().getString(R.string.basket_eur), getResources().getString(R.string.basket_details_talkable)};
        } else {
            TITLES = new String[]{getResources().getString(R.string.basket_analyze), getResources().getString(R.string.basket_alet), getResources().getString(R.string.basket_analyze_sizeof), getResources().getString(R.string.basket_eur), getResources().getString(R.string.basket_details_talkable)};
        }
        mViewPager = (ViewPager) findViewById(R.id.basket_details_view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.basket_details_tab_layout);
        mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
        mTabsAdapter.setTitles(TITLES);

        mViewPager.setOffscreenPageLimit(TITLES.length);//设置预加载页面的个数。
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
//        TabLayout.Tab tabAt;
        if (isNBA) {  //是NBA
            mTabsAdapter.addFragments(mBasketLiveFragment, mAnalyzeFragment, mOddsLet, mOddsSize, mOddsEuro, mChartBallFragment);

            if (chartBallView == 1) {
                mViewPager.setCurrentItem(5, false);
            }
        } else {
            mTabsAdapter.addFragments(mAnalyzeFragment, mOddsLet, mOddsSize, mOddsEuro, mChartBallFragment);

            if (chartBallView == 1) {
                mViewPager.setCurrentItem(4, false);
            }
        }

        mRefreshLayout = (ExactSwipeRefreshLayout) findViewById(R.id.basket_details_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));
        findViewById(R.id.basket_details_back).setOnClickListener(this);

        rl_gif_notice = (RelativeLayout) findViewById(R.id.rl_gif_notice);
        tv_addMultiView = (TextView) findViewById(R.id.tv_addMultiView);
        if (isAddMultiViewHide) {
            tv_addMultiView.setVisibility(View.GONE);
        }

        mCollect = (ImageView) this.findViewById(R.id.basket_details_collect);
        mCollect.setOnClickListener(this);

        boolean isFocus = FocusUtils.isBasketFocusId(mThirdId);
        if (isFocus) {
            mCollect.setImageResource(R.mipmap.basketball_collected);
        } else {
            mCollect.setImageResource(R.mipmap.basketball_collect);
        }

        barrage_view = (BarrageView) findViewById(R.id.barrage_view);
        barrage_switch = (ImageView) findViewById(R.id.barrage_switch);
        barrage_switch.setOnClickListener(this);

        // 头部view
        mHeadImage = (ImageView) findViewById(R.id.image_background);
        mHomeIcon = (ImageView) findViewById(R.id.basket_details_home_icon);
        mGuestIcon = (ImageView) findViewById(R.id.basket_details_guest_icon);
        mHomeTeam = (TextView) findViewById(R.id.basket_details_home_name);
        mGuestTeam = (TextView) findViewById(R.id.basket_details_guest_name);
        mHomeRanking = (TextView) findViewById(R.id.basket_details_home_Ranking);
        mGuestRanking = (TextView) findViewById(R.id.basket_details_guest_Ranking);
        mLeagueName = (TextView) findViewById(R.id.basket_details_matches_name);
        mVS = (TextView) findViewById(R.id.basket_score_maohao);
        mHomeScore = (TextView) findViewById(R.id.basket_details_home_all_score);
        mGuestScore = (TextView) findViewById(R.id.basket_details_guest_all_score);
        mMatchState = (TextView) findViewById(R.id.basket_details_state);
        mRemainTime = (TextView) findViewById(R.id.basket_details_remain_time);
        mApos = (TextView) findViewById(R.id.backetball_details_apos);
        mGuest1 = (TextView) findViewById(R.id.basket_details_guest_first);
        mGuest2 = (TextView) findViewById(R.id.basket_details_guest_second);
        mGuest3 = (TextView) findViewById(R.id.basket_details_guest_third);
        mGuest4 = (TextView) findViewById(R.id.basket_details_guest_fourth);
        mHome1 = (TextView) findViewById(R.id.basket_details_home_first);
        mHome2 = (TextView) findViewById(R.id.basket_details_home_second);
        mHome3 = (TextView) findViewById(R.id.basket_details_home_third);
        mHome4 = (TextView) findViewById(R.id.basket_details_home_fourth);
        mLayoutOt1 = (LinearLayout) findViewById(R.id.basket_details_llot1);
        mLayoutOt2 = (LinearLayout) findViewById(R.id.basket_details_llot2);
        mLayoutOt3 = (LinearLayout) findViewById(R.id.basket_details_llot3);
        mGuestOt1 = (TextView) findViewById(R.id.basket_details_guest_ot1);
        mGuestOt2 = (TextView) findViewById(R.id.basket_details_guest_ot2);
        mGuestOt3 = (TextView) findViewById(R.id.basket_details_guest_ot3);
        mHomeOt1 = (TextView) findViewById(R.id.basket_details_home_ot1);
        mHomeOt2 = (TextView) findViewById(R.id.basket_details_home_ot2);
        mHomeOt3 = (TextView) findViewById(R.id.basket_details_home_ot3);
        mSmallGuestScore = (TextView) findViewById(R.id.basket_details_guest_small_total);
        mSmallHomeScore = (TextView) findViewById(R.id.basket_details_home_small_total);
        btn_showGif = (LinearLayout) findViewById(R.id.btn_showGif);


        tv_addMultiView.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() { //关闭socket
        super.onDestroy();
        closePollingGifCount();
        EventBus.getDefault().unregister(this);
        closeWebSocket();
        mSocketHandler.removeCallbacksAndMessages(null);
        barrage_view.delHandler();
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
            // 如果是越南语（）
            lang = BaseURLs.LANGUAGE_SWITCHING_VI;
        }

        return lang.trim();
    }

    @Override
    protected void onTextResult(String text) {
        L.d("socket", "socket==" + text);

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
            L.e(TAG, type + "____________________");

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
     * 设置监听
     */
    private void initEvent() {
        countDown = new CountDown(MILLIS_INFuture, 1000, new CountDown.CountDownCallback() {
            @Override
            public void onFinish() {
                rl_gif_notice.setVisibility(View.GONE);
            }

            @Override
            public void onTick(long millisUntilFinished) {
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (!isNBA) {
                    if (position != 4) {// 聊球界面禁用下拉刷新
                        MyApp.getContext().sendBroadcast(new Intent("CLOSE_INPUT_ACTIVITY"));
                    }
                } else {
                    if (position != 5) {// 聊球界面禁用下拉刷新
                        MyApp.getContext().sendBroadcast(new Intent("CLOSE_INPUT_ACTIVITY"));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btn_showGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isConnected(mContext)) {
                    int type = com.hhly.mlottery.util.NetworkUtils.getCurNetworkType(mContext);
                    if (type == 1) {
                        L.d("zxcvbn", "WIFI");
                        Intent intent = new Intent(mContext, PlayHighLightActivity.class);
                        intent.putExtra("thirdId", mThirdId);
                        intent.putExtra("match_type", MATCH_TYPE);

                        startActivity(intent);
                        //wifi
                    } else if (type == 2 || type == 3 || type == 4) {//2G  3G  4G
                        L.d("zxcvbn", "移动网络-" + type + "G");
                        promptNetInfo();
                    }
                } else {
                    Toast.makeText(mContext, getResources().getString(R.string.about_net_failed), Toast.LENGTH_SHORT).show();
                }

            }
        });

        mLayoutGuestIcon.setOnClickListener(this);
        mLayoutGuestName.setOnClickListener(this);
        mLayoutHomeIcon.setOnClickListener(this);
        mLayoutHomeName.setOnClickListener(this);
    }

    /**
     * 当前连接的网络提示
     */
    private void promptNetInfo() {
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext, R.style.AppThemeDialog);
            builder.setCancelable(false);// 设置对话框以外不可点击
            builder.setTitle(MyApp.getContext().getResources().getString(R.string.to_update_kindly_reminder));// 提示标题
            builder.setMessage(MyApp.getContext().getResources().getString(R.string.video_high_light_reminder_comment));// 提示内容
            builder.setPositiveButton(MyApp.getContext().getResources().getString(R.string.video_high_light_continue_open), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(mContext, PlayHighLightActivity.class);
                    intent.putExtra("thirdId", mThirdId);
                    intent.putExtra("match_type", MATCH_TYPE);

                    startActivity(intent);
                }
            });
            builder.setNegativeButton(MyApp.getContext().getResources().getString(R.string.basket_analyze_dialog_cancle), new DialogInterface.OnClickListener() {
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


    /**
     * 请求网络数据
     */
    private void loadData() {
        Map<String, String> params = new HashMap<>();
        params.put("thirdId", mThirdId);
        L.d("456789", mThirdId);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_BASKET_DETAILS, params, new VolleyContentFast.ResponseSuccessListener<BasketballDetailsBean>() {
            @Override
            public void onResponse(BasketballDetailsBean basketDetailsBean) {
                if (basketDetailsBean != null && basketDetailsBean.getMatch() != null) {

                    setHeadViewData(basketDetailsBean);

                    homeIconUrl = basketDetailsBean.getMatch().getHomeLogoUrl();
                    guestIconUrl = basketDetailsBean.getMatch().getGuestLogoUrl();
                    if (basketDetailsBean.getMatch().getMatchStatus() != END) {
                        connectWebSocket();
                    }
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        }, BasketballDetailsBean.class);
    }

    /**
     * 设置头部数据
     */
    private void setHeadViewData(BasketballDetailsBean bean) {

        // 启动秒闪烁
        setApos();

        BasketballDetailsBean.MatchEntity.MatchScoreEntity score = bean.getMatch().getMatchScore();//比分
        mMatch = bean.getMatch();

        if (score != null) {
            mGuestNum = score.getGuestScore();
            mHomeNum = score.getHomeScore();
        }

        //联赛名
        mLeagueName.setText(mMatch.getLeagueName());
        mLeagueName.setTextColor(Color.parseColor(mMatch.getLeagueColor()));

        mHomeTeam.setText(mMatch.getHomeTeam());
        mGuestTeam.setText(mMatch.getGuestTeam());
        if (mMatch.getHomeRanking().equals("")) {
            mHomeRanking.setText("");
        } else {
            mHomeRanking.setText("[ " + mMatch.getHomeRanking() + " ]");
        }
        if (mMatch.getGuestRanking().equals("")) {
            mGuestRanking.setText("");
        } else {
            mGuestRanking.setText("[ " + mMatch.getGuestRanking() + " ]");
        }

        //图标
        if (mContext != null) {
            ImageLoader.load(mContext, mMatch.getHomeLogoUrl(), R.mipmap.basket_default).into(mHomeIcon);

            ImageLoader.load(mContext, mMatch.getGuestLogoUrl(), R.mipmap.basket_default).into(mGuestIcon);

            ImageLoader.load(mContext, bean.getBgUrl(), R.color.black).into(mHeadImage);
        }

        if (mMatch.getSection() == 2) { //只有上下半场
            mGuest2.setVisibility(View.INVISIBLE);
            mGuest4.setVisibility(View.INVISIBLE);
            mHome2.setVisibility(View.INVISIBLE);
            mHome4.setVisibility(View.INVISIBLE);
        }

        if (mContext != null) {

            switch (mMatch.getMatchStatus()) {
                case PRE_MATCH: ///赛前
                case DETERMINED://待定
                case GAME_CANCLE: //比赛取消
                case GAME_CUT: //比赛中断
                case GAME_DELAY: //比赛推迟
                    //赛前显示 客队 VS  主队
                    mGuestScore.setText("");
                    mHomeScore.setText("");

//                    mVS.setText("VS");
                    mVS.setText(R.string.games_no_start);
                    mVS.setTextSize(TypedValue.COMPLEX_UNIT_PX,MyApp.getContext().getResources().getDimension(R.dimen.text_size_12));
                    if (mMatch.getMatchStatus() == PRE_MATCH) {
                        mMatchState.setText(DateUtil.convertDateToNation(bean.getMatch().getDate()) + "  " + bean.getMatch().getTime() + "   " + MyApp.getContext().getResources().getString(R.string.basket_begin_game));
                    } else if (mMatch.getMatchStatus() == DETERMINED) {
                        mMatchState.setText(R.string.basket_undetermined);
                    } else if (mMatch.getMatchStatus() == GAME_CANCLE) {
                        mMatchState.setText(R.string.basket_cancel);
                    } else if (mMatch.getMatchStatus() == GAME_CUT) {
                        mMatchState.setText(R.string.basket_interrupt);
                    } else {
                        mMatchState.setText(R.string.basket_postpone);
                    }
                    mApos.setVisibility(View.GONE);
                    mRemainTime.setText("");
                    if (mMatch.getMatchStatus() == PRE_MATCH) {
                        mChartBallFragment.setClickableLikeBtn(true);
                    }
                    break;
                case END://完场
                    mChartBallFragment.setClickableLikeBtn(false);

                    mGuestScore.setText(score.getGuestScore() + "");
                    mHomeScore.setText(score.getHomeScore() + "");
                    mMatchState.setText(R.string.finished_txt);
                    mGuest1.setText(score.getGuest1() + "");
                    mGuest2.setText(score.getGuest2() + "");
                    mGuest3.setText(score.getGuest3() + "");
                    mGuest4.setText(score.getGuest4() + "");
                    mHome1.setText(score.getHome1() + "");
                    mHome2.setText(score.getHome2() + "");
                    mHome3.setText(score.getHome3() + "");
                    mHome4.setText(score.getHome4() + "");


                    mSmallGuestScore.setText(score.getGuestScore() + "");
                    mSmallHomeScore.setText(score.getHomeScore() + "");
                    mVS.setText(":");
                    mVS.setTextSize(TypedValue.COMPLEX_UNIT_PX,MyApp.getContext().getResources().getDimension(R.dimen.text_size_30));
                    if (score.getAddTime() == 3) {//三个加时
                        mLayoutOt3.setVisibility(View.VISIBLE);
                        mLayoutOt2.setVisibility(View.VISIBLE);
                        mLayoutOt1.setVisibility(View.VISIBLE);
                        mGuestOt1.setText(score.getGuestOt1() + "");
                        mHomeOt1.setText(score.getHomeOt1() + "");
                        mGuestOt2.setText(score.getGuestOt2() + "");
                        mHomeOt2.setText(score.getHomeOt2() + "");
                        mGuestOt3.setText(score.getGuestOt3() + "");
                        mHomeOt3.setText(score.getHomeOt3() + "");
                    } else if (score.getAddTime() == 2) {
                        mLayoutOt2.setVisibility(View.VISIBLE);
                        mLayoutOt1.setVisibility(View.VISIBLE);
                        mGuestOt1.setText(score.getGuestOt1() + "");
                        mHomeOt1.setText(score.getHomeOt1() + "");
                        mGuestOt2.setText(score.getGuestOt2() + "");
                        mHomeOt2.setText(score.getHomeOt2() + "");
                    } else if (score.getAddTime() == 1) {
                        mLayoutOt1.setVisibility(View.VISIBLE);
                        mGuestOt1.setText(score.getGuestOt1() + "");
                        mHomeOt1.setText(score.getHomeOt1() + "");
                    }
                    mApos.setVisibility(View.GONE);
                    mRemainTime.setText("");
                    break;
                case OT3:
                    mLayoutOt3.setVisibility(View.VISIBLE);
                    setScore(score.getGuestOt3(), mGuestOt3, score.getHomeOt3(), mHomeOt3);
                case OT2:
                    mLayoutOt2.setVisibility(View.VISIBLE);
                    setScore(score.getGuestOt2(), mGuestOt2, score.getHomeOt2(), mHomeOt2);

                case OT1:
                    mLayoutOt1.setVisibility(View.VISIBLE);
                    setScore(score.getGuestOt1(), mGuestOt1, score.getHomeOt1(), mHomeOt1);

                case FOURTH_QUARTER:
                    setScore(score.getGuest4(), mGuest4, score.getHome4(), mHome4);

                case THIRD_QUARTER:
                    setScore(score.getGuest3(), mGuest3, score.getHome3(), mHome3);

                case HALF_GAME: //中场
                case SECOND_QUARTER:
                    setScore(score.getGuest2(), mGuest2, score.getHome2(), mHome2);
                case FIRST_QUARTER:
                    setScore(score.getGuest1(), mGuest1, score.getHome1(), mHome1);
                    //不管是第几节都设置总比分,设置剩余时间
                    setScore(score.getGuestScore(), mGuestScore, score.getHomeScore(), mHomeScore);
                    setScore(score.getGuestScore(), mSmallGuestScore, score.getHomeScore(), mSmallHomeScore);
                    mVS.setText(":");
                    mVS.setTextSize(TypedValue.COMPLEX_UNIT_PX,MyApp.getContext().getResources().getDimension(R.dimen.text_size_30));
                    mChartBallFragment.setClickableLikeBtn(true); //聊球可点赞

                    //设置比赛时间及状态
                    if (mMatch.getMatchStatus() == FIRST_QUARTER) {
                        if (mMatch.getSection() == 2) {
                            mMatchState.setText("1st half  ");
                        } else {
                            mMatchState.setText("1st  ");
                        }
                        mApos.setVisibility(View.VISIBLE);
                    } else if (mMatch.getMatchStatus() == SECOND_QUARTER) {
                        if (mMatch.getSection() == 2) {
                            mMatchState.setText("1st half  ");
                        } else {
                            mMatchState.setText("2nd  ");
                        }
                        mApos.setVisibility(View.VISIBLE);
                    } else if (mMatch.getMatchStatus() == HALF_GAME) {
                        mMatchState.setText("half time  ");
                        mApos.setVisibility(View.GONE);
                    } else if (mMatch.getMatchStatus() == THIRD_QUARTER) {
                        if (mMatch.getSection() == 2) {
                            mMatchState.setText("2nd half");
                        } else {
                            mMatchState.setText("3rd  ");
                        }
                        mApos.setVisibility(View.VISIBLE);
                    } else if (mMatch.getMatchStatus() == FOURTH_QUARTER) {
                        if (mMatch.getSection() == 2) {
                            mMatchState.setText("2nd half  ");
                        } else {
                            mMatchState.setText("4th  ");
                        }
                        mApos.setVisibility(View.VISIBLE);
                    } else if (mMatch.getMatchStatus() == OT1) {
                        mMatchState.setText("OT1  ");
                        mApos.setVisibility(View.VISIBLE);
                    } else if (mMatch.getMatchStatus() == OT2) {
                        mMatchState.setText("OT2  ");
                        mApos.setVisibility(View.VISIBLE);
                    } else {
                        mMatchState.setText("OT3  ");
                        mApos.setVisibility(View.VISIBLE);
                    }

                    mRemainTime.setText(score.getRemainTime());//剩余时间
                    if (mMatch.getMatchStatus() == HALF_GAME) {
                        mRemainTime.setText("");//中场时无剩余时间。。后台可能中场也给时间。没办法
                    }
                    if (score.getRemainTime() == null || score.getRemainTime().equals("")) {//没有剩余时间的时候
                        mApos.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    /**
     * 秒闪烁
     */
    private void setApos() {
        mApos.setText("\'");

        final AlphaAnimation anim1 = new AlphaAnimation(1, 1);
        anim1.setDuration(500);
        final AlphaAnimation anim2 = new AlphaAnimation(0, 0);
        anim2.setDuration(500);
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mApos.startAnimation(anim2);
            }
        });
        anim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mApos.startAnimation(anim1);
            }
        });
        mApos.startAnimation(anim1);
    }

    /**
     * 设置比分
     *
     * @param guestScore 客队得分
     * @param guest      客队显示比分的textview
     * @param homeScore  主队比分
     * @param home       主队显示比分的textview
     */
    private void setScore(int guestScore, TextView guest, int homeScore, TextView home) {
        guest.setText(guestScore + "");
        home.setText(homeScore + "");
        if (guestScore > homeScore) {//得分少的用灰色
            guest.setTextColor(MyApp.getContext().getResources().getColor(R.color.basket_score_white));
            home.setTextColor(MyApp.getContext().getResources().getColor(R.color.basket_score_gray));
        } else if (guestScore < homeScore) {
            guest.setTextColor(MyApp.getContext().getResources().getColor(R.color.basket_score_gray));
            home.setTextColor(MyApp.getContext().getResources().getColor(R.color.basket_score_white));
        } else {
            guest.setTextColor(MyApp.getContext().getResources().getColor(R.color.basket_score_white));
            home.setTextColor(MyApp.getContext().getResources().getColor(R.color.basket_score_white));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.basket_details_back:
                MobclickAgent.onEvent(MyApp.getContext(), "BasketDetailsActivity_Exit");
                MyApp.getContext().sendBroadcast(new Intent("CLOSE_INPUT_ACTIVITY"));
                setResult(Activity.RESULT_OK);

                eventBusPost();

                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.basket_details_collect:
                MobclickAgent.onEvent(MyApp.getContext(), "BasketDetailsActivity_Attention");
                if (FocusUtils.isBasketFocusId(mThirdId)) {
                    FocusUtils.deleteBasketFocusId(mThirdId);
                    mCollect.setImageResource(R.mipmap.basketball_collect);
                } else {
                    FocusUtils.addBasketFocusId(mThirdId);
                    mCollect.setImageResource(R.mipmap.basketball_collected);
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
            case R.id.rl_iv: //客队图标
            case R.id.ll_guest : //客队队名
                Intent intent=new Intent(this,BasketballTeamActivity.class);
                intent.putExtra(BasketTeamParams.LEAGUE_ID,mLeagueId);
                intent.putExtra(BasketTeamParams.TEAM_ID,mMatch.getHomeTeamId());
                startActivity(intent);
                break;
            case R.id.rl_iv2:
            case R.id.linearLayout2:
                Intent intent1=new Intent(this,BasketballTeamActivity.class);
                intent1.putExtra(BasketTeamParams.LEAGUE_ID,mLeagueId);
                intent1.putExtra(BasketTeamParams.TEAM_ID,mMatch.getGuestTeamId());
                startActivity(intent1);
                break;

            case R.id.tv_addMultiView:
                enterMultiScreenView();
                break;

        }
    }


    private void enterMultiScreenView() {
        if (PreferenceUtil.getBoolean("introduce", true)) {
            Intent intent = new Intent(BasketDetailsActivityTest.this, MultiScreenIntroduceActivity.class);
            intent.putExtra("thirdId", new MultipleByValueBean(2, mThirdId));
            startActivity(intent);
            PreferenceUtil.commitBoolean("introduce", false);
        } else {
            Intent intent = new Intent(BasketDetailsActivityTest.this, MultiScreenViewingListActivity.class);
            intent.putExtra("thirdId", new MultipleByValueBean(2, mThirdId));
            startActivity(intent);
        }
    }


    public void onEventMainThread(BarrageBean barrageBean) {

        barrage_view.setDatas(barrageBean.getUrl(), barrageBean.getMsg().toString());

    }
    public void onEventMainThread(BasketDetailsEventBusEntity event) {

    }

    // 评论登录跳转
    public void talkAboutBallLoginBasket() {
        //跳转登录界面
        Intent intent1 = new Intent(mContext, LoginActivity.class);
        startActivityForResult(intent1, CyUtils.JUMP_COMMENT_QUESTCODE);
    }

    // 发表评论跳转
    public void talkAboutBallSendBasket() {
        Intent intent2 = new Intent(mContext, ChartballActivity.class);
        intent2.putExtra("thirdId", mThirdId);
        startActivityForResult(intent2, CyUtils.JUMP_COMMENT_QUESTCODE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResult(Activity.RESULT_OK);

            eventBusPost();

            finish();
            overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    Handler mSocketHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            L.e(TAG, "__handleMessage__");
            L.e(TAG, "msg.arg1 = " + msg.arg1);
            if (msg.arg1 == 100) {  //type 为100 ==> 比分推送

                String ws_json = (String) msg.obj;
                L.e(TAG, "ws_json = " + ws_json);
                WebSocketBasketBallDetails mBasketDetails = null;
                try {
                    mBasketDetails = JSON.parseObject(ws_json, WebSocketBasketBallDetails.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    mBasketDetails = JSON.parseObject(ws_json, WebSocketBasketBallDetails.class);
                }
                //  L.e();
                updateData(mBasketDetails);
                //文字直播推送
                if (mBasketDetails.getData().getTextLiveEntity() != null) {
                    updateTextLive(mBasketDetails.getData().getTextLiveEntity());
                }
            }
        }
    };


    /**
     * 接受推送，更新数据
     *
     * @param basketBallDetails 推送过来消息封装的实体类
     */
    private void updateData(WebSocketBasketBallDetails basketBallDetails) {
        DataEntity score = basketBallDetails.getData();

        switch (score.getMatchStatus()) {
            case DETERMINED://待定
            case GAME_CANCLE: //比赛取消
            case GAME_CUT: //比赛中断
            case GAME_DELAY: //比赛推迟
                if (mMatch.getMatchStatus() == DETERMINED) {
                    mMatchState.setText(R.string.basket_undetermined);
                } else if (mMatch.getMatchStatus() == GAME_CANCLE) {
                    mMatchState.setText(R.string.basket_cancel);
                } else if (mMatch.getMatchStatus() == GAME_CUT) {
                    mMatchState.setText(R.string.basket_interrupt);
                } else {
                    mMatchState.setText(R.string.basket_postpone);
                }
                mApos.setVisibility(View.GONE);
                mRemainTime.setText("");
                mChartBallFragment.setClickableLikeBtn(false);
                break;

            case END://完场
                mChartBallFragment.setClickableLikeBtn(false);
                mApos.setVisibility(View.GONE);
                mGuestScore.setText(score.getGuestScore() + "");
                mGuestScore.setTextColor(getResources().getColor(R.color.score_color_white));
                mHomeScore.setText(score.getHomeScore() + "");
                mHomeScore.setTextColor(getResources().getColor(R.color.score_color_white));
                mMatchState.setText(R.string.finished_txt);
                mGuest1.setText(score.getGuest1() + "");
                mGuest1.setTextColor(getResources().getColor(R.color.score_color_white));
                mGuest2.setText(score.getGuest2() + "");
                mGuest2.setTextColor(getResources().getColor(R.color.score_color_white));
                mGuest3.setText(score.getGuest3() + "");
                mGuest3.setTextColor(getResources().getColor(R.color.score_color_white));
                mGuest4.setText(score.getGuest4() + "");
                mGuest4.setTextColor(getResources().getColor(R.color.score_color_white));
                mHome1.setText(score.getHome1() + "");
                mHome1.setTextColor(getResources().getColor(R.color.score_color_white));
                mHome2.setText(score.getHome2() + "");
                mHome2.setTextColor(getResources().getColor(R.color.score_color_white));
                mHome3.setText(score.getHome3() + "");
                mHome3.setTextColor(getResources().getColor(R.color.score_color_white));
                mHome4.setText(score.getHome4() + "");
                mHome4.setTextColor(getResources().getColor(R.color.score_color_white));

                mSmallGuestScore.setText(score.getGuestScore() + "");
                mSmallGuestScore.setTextColor(getResources().getColor(R.color.score_color_white));
                mSmallHomeScore.setText(score.getHomeScore() + "");
                mSmallHomeScore.setTextColor(getResources().getColor(R.color.score_color_white));
                mVS.setText(":");
                mVS.setTextSize(TypedValue.COMPLEX_UNIT_PX,MyApp.getContext().getResources().getDimension(R.dimen.text_size_30));
                if (score.getAddTime() == 3) {//三个加时
                    mLayoutOt3.setVisibility(View.VISIBLE);
                    mLayoutOt2.setVisibility(View.VISIBLE);
                    mLayoutOt1.setVisibility(View.VISIBLE);
                    mGuestOt1.setText(score.getGuestOt1() + "");
                    mGuestOt1.setTextColor(getResources().getColor(R.color.score_color_white));
                    mHomeOt1.setText(score.getHomeOt1() + "");
                    mHomeOt1.setTextColor(getResources().getColor(R.color.score_color_white));
                    mGuestOt2.setText(score.getGuestOt2() + "");
                    mGuestOt2.setTextColor(getResources().getColor(R.color.score_color_white));
                    mHomeOt2.setText(score.getHomeOt2() + "");
                    mHomeOt2.setTextColor(getResources().getColor(R.color.score_color_white));
                    mGuestOt3.setText(score.getGuestOt3() + "");
                    mGuestOt3.setTextColor(getResources().getColor(R.color.score_color_white));
                    mHomeOt3.setText(score.getHomeOt3() + "");
                    mHomeOt3.setTextColor(getResources().getColor(R.color.score_color_white));
                } else if (score.getAddTime() == 2) {
                    mLayoutOt2.setVisibility(View.VISIBLE);
                    mLayoutOt1.setVisibility(View.VISIBLE);
                    mGuestOt1.setText(score.getGuestOt1() + "");
                    mGuestOt1.setTextColor(getResources().getColor(R.color.score_color_white));
                    mHomeOt1.setText(score.getHomeOt1() + "");
                    mHomeOt1.setTextColor(getResources().getColor(R.color.score_color_white));
                    mGuestOt2.setText(score.getGuestOt2() + "");
                    mGuestOt2.setTextColor(getResources().getColor(R.color.score_color_white));
                    mHomeOt2.setText(score.getHomeOt2() + "");
                    mHomeOt2.setTextColor(getResources().getColor(R.color.score_color_white));
                } else if (score.getAddTime() == 1) {
                    mLayoutOt1.setVisibility(View.VISIBLE);
                    mGuestOt1.setText(score.getGuestOt1() + "");
                    mGuestOt1.setTextColor(getResources().getColor(R.color.score_color_white));
                    mHomeOt1.setText(score.getHomeOt1() + "");
                    mHomeOt1.setTextColor(getResources().getColor(R.color.score_color_white));
                }
                mRemainTime.setText("");//完场无剩余时间
                break;
            case OT3:
                mLayoutOt3.setVisibility(View.VISIBLE);
                setScore(score.getGuestOt3(), mGuestOt3, score.getHomeOt3(), mHomeOt3);
            case OT2:
                mLayoutOt2.setVisibility(View.VISIBLE);
                setScore(score.getGuestOt2(), mGuestOt2, score.getHomeOt2(), mHomeOt2);
            case OT1:
                mLayoutOt1.setVisibility(View.VISIBLE);
                setScore(score.getGuestOt1(), mGuestOt1, score.getHomeOt1(), mHomeOt1);
            case FOURTH_QUARTER:
                setScore(score.getGuest4(), mGuest4, score.getHome4(), mHome4);
            case THIRD_QUARTER:
                setScore(score.getGuest3(), mGuest3, score.getHome3(), mHome3);

            case HALF_GAME: //中场
            case SECOND_QUARTER:
                setScore(score.getGuest2(), mGuest2, score.getHome2(), mHome2);

            case FIRST_QUARTER:
                setScore(score.getGuest1(), mGuest1, score.getHome1(), mHome1);
                //不管是第几节都设置总比分.推送過來的話比分有变化要翻转

                mVS.setText(":");
                mVS.setTextSize(TypedValue.COMPLEX_UNIT_PX,MyApp.getContext().getResources().getDimension(R.dimen.text_size_30));

                if (mGuestNum != score.getGuestScore()) {
                    scoreAnimation(mGuestScore);
                    mGuestNum = score.getGuestScore();
                }
                if (mHomeNum != score.getHomeScore()) {
                    scoreAnimation(mHomeScore);
                    mHomeNum = score.getHomeScore();
                }
                mChartBallFragment.setClickableLikeBtn(true);//聊球可点赞
                setScore(score.getGuestScore(), mGuestScore, score.getHomeScore(), mHomeScore);// 动画有毒，最后在设一下比分

                setScore(score.getGuestScore(), mSmallGuestScore, score.getHomeScore(), mSmallHomeScore);


                //设置比赛时间及状态
                if (score.getMatchStatus() == FIRST_QUARTER) {
                    if (mMatch.getSection() == 2) {
                        mMatchState.setText("1st half ");
                    } else {
                        mMatchState.setText("1st  ");
                    }
                    mApos.setVisibility(View.VISIBLE);
                } else if (score.getMatchStatus() == SECOND_QUARTER) {
                    if (mMatch.getSection() == 2) {
                        mMatchState.setText("1st half ");
                    } else {
                        mMatchState.setText("2nd  ");
                    }
                    mApos.setVisibility(View.VISIBLE);
                } else if (score.getMatchStatus() == HALF_GAME) {
                    mMatchState.setText("half time  ");
                    mApos.setVisibility(View.GONE);
                } else if (score.getMatchStatus() == THIRD_QUARTER) {
                    if (mMatch.getSection() == 2) {
                        mMatchState.setText("2nd half ");
                    } else {
                        mMatchState.setText("3rd  ");
                    }
                    mApos.setVisibility(View.VISIBLE);
                } else if (score.getMatchStatus() == FOURTH_QUARTER) {
                    if (mMatch.getSection() == 2) {
                        mMatchState.setText("2nd half  ");
                    } else {
                        mMatchState.setText("4th  ");
                    }
                    mApos.setVisibility(View.VISIBLE);
                } else if (score.getMatchStatus() == OT1) {
                    mMatchState.setText("OT1  ");
                    mApos.setVisibility(View.VISIBLE);
                } else if (score.getMatchStatus() == OT2) {
                    mMatchState.setText("OT2  ");
                    mApos.setVisibility(View.VISIBLE);
                } else {
                    mMatchState.setText("OT3  ");
                    mApos.setVisibility(View.VISIBLE);
                }

                //设置剩余时间
                mRemainTime.setText(score.getRemainTime() == null ? "" : score.getRemainTime());//为空的话就设置为空字符

                if (score.getMatchStatus() == HALF_GAME) {
                    mRemainTime.setText("");//中场时无剩余时间。。后台可能中场也给时间。没办法
                }

                if (score.getRemainTime() == null || score.getRemainTime().equals("")) {
                    mApos.setVisibility(View.GONE);
                }
                break;
        }
    }

    /**
     * 设置比分变化时的的翻转动画
     */
    private void scoreAnimation(final TextView changeText) {
        float cX = changeText.getWidth() / 2.0f;
        float cY = changeText.getHeight() / 2.0f;

        MyRotateAnimation rotateAnim = new MyRotateAnimation(cX, cY, MyRotateAnimation.ROTATE_DECREASE);

        rotateAnim.setFillAfter(true);

        changeText.startAnimation(rotateAnim);

    }

    /**
     * 接受文字直播推送，更新数据
     */
    private void updateTextLive(BasketEachTextLiveBean basketEachTextLiveBean) {
        EventBus.getDefault().post(new BasketTextLiveEvent(basketEachTextLiveBean));
    }

    private void eventBusPost() {
        if (mCurrentId == IMMEDIA_FRAGMENT) {
            EventBus.getDefault().post(new BasketDetailsEventBusEntity(mCurrentId + ""));
        } else if (mCurrentId == RESULT_FRAGMENT) {
            EventBus.getDefault().post(new BasketDetailsEventBusEntity(mCurrentId + ""));
        } else if (mCurrentId == SCHEDULE_FRAGMENT) {
            EventBus.getDefault().post(new BasketDetailsEventBusEntity(mCurrentId + ""));
        } else if (mCurrentId == FOCUS_FRAGMENT) {
            EventBus.getDefault().post(new BasketFocusEventBus());
        } else if (mCurrentId == CUSTOM_FRAGMENT) {
            EventBus.getDefault().post(new CustomDetailsEvent(""));
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                loadData();
                //直播刷新
                if (isNBA) {
                    if (mBasketLiveFragment != null) {
                        mBasketLiveFragment.refresh();
                    }
                }
            }
        }, 500);
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


    /**
     * 开启轮询
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
        }
    }

    /**
     * 关闭轮询
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

    /**
     * 获取gif数量
     */
    private void getCollectionCount() {
        Map<String, String> map = new HashMap<>();
        map.put("matchType", MATCH_TYPE);
        map.put("thirdId", mThirdId);  //399381
        L.d("zxcvbn", "[-----------------------------------------------------]");

        VolleyContentFast.requestJsonByGet(BaseURLs.FOOTBALL_DETAIL_COLLECTION_COUNT, map, new VolleyContentFast.ResponseSuccessListener<DetailsCollectionCountBean>() {
            @Override
            public void onResponse(DetailsCollectionCountBean jsonObject) {
                if (200 == jsonObject.getResult()) {
                    if (jsonObject.getData() != 0) {
                        btn_showGif.setVisibility(View.VISIBLE);
                        if (isFirstShowGif) {  //第一次显示

                            gifCount = jsonObject.getData();
                            L.d("zxcvbn", "第一次进入------------gifCount==" + gifCount);

                            isFirstShowGif = false;
                        } else {
                            L.d("zxcvbn", "第二次进入------------gifCount=" + gifCount);
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
                    }
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        }, DetailsCollectionCountBean.class);
    }

}
