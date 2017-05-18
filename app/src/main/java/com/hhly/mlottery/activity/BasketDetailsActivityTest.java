package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.BasePagerAdapter;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.bean.BarrageBean;
import com.hhly.mlottery.bean.GoneBarrage;
import com.hhly.mlottery.bean.OpenBarrage;
import com.hhly.mlottery.bean.basket.BasketballDetailsBean;
import com.hhly.mlottery.bean.basket.basketdetails.BasketEachTextLiveBean;
import com.hhly.mlottery.bean.footballDetails.DetailsCollectionCountBean;
import com.hhly.mlottery.bean.multiplebean.MultipleByValueBean;
import com.hhly.mlottery.bean.websocket.WebSocketBasketBallDetails;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BaseUserTopics;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.basketballframe.BasketAnalyzeFragment;
import com.hhly.mlottery.frame.basketballframe.BasketAnimLiveFragment;
import com.hhly.mlottery.frame.basketballframe.BasketDetailsHeadFragment;
import com.hhly.mlottery.frame.basketballframe.BasketFocusEventBus;
import com.hhly.mlottery.frame.basketballframe.BasketLiveFragment;
import com.hhly.mlottery.frame.basketballframe.BasketOddsFragment;
import com.hhly.mlottery.frame.basketballframe.BasketTextLiveEvent;
import com.hhly.mlottery.frame.chartBallFragment.ChartBallFragment;
import com.hhly.mlottery.frame.footballframe.eventbus.BasketDetailsEventBusEntity;
import com.hhly.mlottery.util.CountDown;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.FocusUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.CustomDetailsEvent;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.BarrageView;
import com.hhly.mlottery.widget.CustomViewpager;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import me.relex.circleindicator.CircleIndicator;

/**
 * @author yixq
 *         Created by A on 2016/3/21.
 * @Description: 篮球详情的 Activity
 */
public class BasketDetailsActivityTest extends BaseWebSocketActivity implements ExactSwipeRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener, View.OnClickListener {
    public final static String BASKET_THIRD_ID = "thirdId";
    public final static String BASKET_MATCH_STATUS = "MatchStatus";
    public final static String BASKET_MATCH_LEAGUEID = "leagueId";
    public final static String BASKET_MATCH_MATCHTYPE = "matchType";
    //    0:未开赛,1:一节,2:二节,5:1'OT，以此类推
//            -1:完场,-2:待定,-3:中断,-4:取消,-5:推迟,50中场
    private final static int END = -1;
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
    //    public static String mMatchStatus;
    private Context mContext;


    BasketLiveFragment mBasketLiveFragment;

    BasketAnalyzeFragment mAnalyzeFragment = new BasketAnalyzeFragment();
    ChartBallFragment mChartBallFragment;

    BasketOddsFragment mOddsEuro;
    BasketOddsFragment mOddsLet;
    BasketOddsFragment mOddsSize;


    private ViewPager mViewPager;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
//    public AppBarLayout appBarLayout;
    private TabLayout mTabLayout;
    private TabsAdapter mTabsAdapter;
//    private Toolbar toolbar;
    private String[] TITLES;


    /**
     * 返回按钮
     */
    private ImageView mBack;
    /**
     * 收藏按钮
     */
    private ImageView mCollect;

    LinearLayout headLayout;// 小头部
    private int mCurrentId;
    private final int IMMEDIA_FRAGMENT = 0;
    private final int RESULT_FRAGMENT = 1;
    private final int SCHEDULE_FRAGMENT = 2;
    private final int FOCUS_FRAGMENT = 3;
    private final int CUSTOM_FRAGMENT = 4;

    private ExactSwipeRefreshLayout mRefreshLayout; //下拉刷新

    private String mLeagueId; // 联赛ID
    private Integer mMatchType; //联赛类型
    private CustomViewpager mHeadviewpager;
    private BasketDetailsHeadFragment mBasketDetailsHeadFragment;
    //    private BasketAnimLiveFragment mBasketAnimLiveFragment;
    private CircleIndicator mIndicator;


    private BasePagerAdapter basePagerAdapter;
    private FragmentManager fragmentManager;

    private static final String LEAGUEID_NBA = "1";


    private boolean isNBA = false;

    //  private int matchStatus;

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
    //private final static int GIFPERIOD_2 = 1000 * 15;//刷新周期15秒

    //    private final static String BASKETBALL_GIF = "basketball_gif";
    private BarrageView barrage_view;
    private ImageView barrage_switch;
    boolean barrage_isFocus = false;
    //    private View view_red;
    private int chartBallView = -1;// 聊球界面转标记

//    private TextView tv_addMultiView;
//    private boolean isAddMultiViewHide = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getExtras() != null) {
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
        setTopic(BaseUserTopics.basketballScore +"." + mThirdId + "."+appendLanguage());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_details_activity_test);
        mContext = this;


        initView();
        mBasketDetailsHeadFragment = BasketDetailsHeadFragment.newInstance();


        //篮球动画直播暂时取掉
       /* if (LEAGUEID_NBA.equals(mLeagueId)) {
            mBasketAnimLiveFragment = BasketAnimLiveFragment.newInstance(mThirdId);
        }*/
        basePagerAdapter.addFragments(mBasketDetailsHeadFragment);
        mIndicator.setVisibility(View.GONE);

        /*if (LEAGUEID_NBA.equals(mLeagueId)) {
            basePagerAdapter.addFragments(mBasketAnimLiveFragment);
            mIndicator.setVisibility(View.VISIBLE);
        } else {
            mIndicator.setVisibility(View.GONE);
        }*/


        mHeadviewpager.setAdapter(basePagerAdapter);
        mHeadviewpager.setOffscreenPageLimit(1);
        mIndicator.setViewPager(mHeadviewpager);
        basePagerAdapter.registerDataSetObserver(mIndicator.getDataSetObserver());

        mHeadviewpager.setCurrentItem(0, false);
        // mHeadviewpager.setIsScrollable(false);

        setListener();
        mLoadHandler.postDelayed(mRun, 0);
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

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mHeadviewpager = new CustomViewpager(mContext);
        mHeadviewpager = (CustomViewpager) findViewById(R.id.headviewpager);
        mIndicator = (CircleIndicator) findViewById(R.id.indicator);

        mViewPager = (ViewPager) findViewById(R.id.basket_details_view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.basket_details_tab_layout);
        mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
        mTabsAdapter.setTitles(TITLES);

        mViewPager.setOffscreenPageLimit(5);//设置预加载页面的个数。
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

        fragmentManager = getSupportFragmentManager();
        basePagerAdapter = new BasePagerAdapter(fragmentManager);

        headLayout = (LinearLayout) findViewById(R.id.basket_details_header_layout);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (!isNBA) {
                    if (position != 4) {// 聊球界面禁用下拉刷新
                        MyApp.getContext().sendBroadcast(new Intent("CLOSE_INPUT_ACTIVITY"));
                    } else {
//                        if (view_red != null) {
//                            view_red.setVisibility(View.GONE);
//                        }
                        mRefreshLayout.setEnabled(true); //展开
                    }
                } else {
                    if (position != 5) {// 聊球界面禁用下拉刷新
                        MyApp.getContext().sendBroadcast(new Intent("CLOSE_INPUT_ACTIVITY"));
                    } else {
//                        if (view_red != null) {
//                            view_red.setVisibility(View.GONE);
//                        }
                        mRefreshLayout.setEnabled(true); //展开
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRefreshLayout = (ExactSwipeRefreshLayout) findViewById(R.id.basket_details_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));
//        mTitleHome = (TextView) this.findViewById(R.id.title_home_score);
//        mTitleGuest = (TextView) this.findViewById(R.id.title_guest_score);
//        mTitleVS = (TextView) this.findViewById(R.id.title_vs);
        mBack = (ImageView) this.findViewById(R.id.basket_details_back);
        mBack.setOnClickListener(this);

        rl_gif_notice = (RelativeLayout) findViewById(R.id.rl_gif_notice);
//        tv_addMultiView = (TextView) findViewById(R.id.tv_addMultiView);
//        if (isAddMultiViewHide) {
//            tv_addMultiView.setVisibility(View.GONE);
//        }

//        mTitleScore = (RelativeLayout) this.findViewById(R.id.ll_basket_title_score);
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

//        tv_addMultiView.setOnClickListener(this);

    }

    public void onEventMainThread(BarrageBean barrageBean) {
        L.d("xxxxx barrageBean: " + barrageBean.getMsg());
        barrage_view.setDatas(barrageBean.getUrl(), barrageBean.getMsg().toString());
    }

    public void onEventMainThread(GoneBarrage barrageBean) {
        barrage_view.setVisibility(View.GONE);

    }

    public void onEventMainThread(OpenBarrage barrageBean) {
        barrage_view.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onDestroy() { //关闭socket
        super.onDestroy();
        closePollingGifCount();
        EventBus.getDefault().unregister(this);
        closeWebSocket();
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
    private void setListener() {
        mBack.setOnClickListener(this);
        mCollect.setOnClickListener(this);

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
    /**
     * 子线程 处理数据加载
     */
    Handler mLoadHandler = new Handler();
    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            loadData();
        }
    };

    /**
     * 请求网络数据
     */
    public void loadData() {
        Map<String, String> params = new HashMap<>();
        params.put("thirdId", mThirdId);
        L.d("456789", mThirdId);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_BASKET_DETAILS, params, new VolleyContentFast.ResponseSuccessListener<BasketballDetailsBean>() {
            @Override
            public void onResponse(BasketballDetailsBean basketDetailsBean) {
                if (basketDetailsBean != null && basketDetailsBean.getMatch() != null) {
                    if(mChartBallFragment != null){
                        mBasketDetailsHeadFragment.initData(basketDetailsBean, mChartBallFragment);
                    }

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
//

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
                    // barrage_view.setVisibility(View.VISIBLE);
                    barrage_view.setAlpha(1);
                } else {
                    barrage_switch.setImageResource(R.mipmap.danmu_close);
                    barrage_isFocus = true;
                    //barrage_view.setVisibility(View.GONE);
                    barrage_view.setAlpha(0);
                }
                break;

//            case R.id.tv_addMultiView:
//                enterMultiScreenView();
//                break;
        }
    }


//    private void enterMultiScreenView() {
//        if (PreferenceUtil.getBoolean("introduce", true)) {
//            Intent intent = new Intent(BasketDetailsActivityTest.this, MultiScreenIntroduceActivity.class);
//            intent.putExtra("thirdId", new MultipleByValueBean(2, mThirdId));
//            startActivity(intent);
//            PreferenceUtil.commitBoolean("introduce", false);
//        } else {
//            Intent intent = new Intent(BasketDetailsActivityTest.this, MultiScreenViewingListActivity.class);
//            intent.putExtra("thirdId", new MultipleByValueBean(2, mThirdId));
//            startActivity(intent);
//        }
//    }


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
        mBasketDetailsHeadFragment.updateData(basketBallDetails, mChartBallFragment);
    }

    /**
     * 接受文字直播推送，更新数据
     */
    private void updateTextLive(BasketEachTextLiveBean basketEachTextLiveBean) {
        //EventBus.getDefault().post(new BasketTextLiveEvent(new BasketEachTextLiveBean("11", "", "", "", "谢谢", 2001, 2, "", 50, 60, 1, "456", "", "", 1, "", "", "")));
        EventBus.getDefault().post(new BasketTextLiveEvent(basketEachTextLiveBean));
    }

    private void eventBusPost() {
        if (mCurrentId == IMMEDIA_FRAGMENT) {
            EventBus.getDefault().post(new BasketDetailsEventBusEntity(mCurrentId +""));
        } else if (mCurrentId == RESULT_FRAGMENT) {
            EventBus.getDefault().post(new BasketDetailsEventBusEntity(mCurrentId +""));
        } else if (mCurrentId == SCHEDULE_FRAGMENT) {
            EventBus.getDefault().post(new BasketDetailsEventBusEntity(mCurrentId +""));
        } else if (mCurrentId == FOCUS_FRAGMENT) {
            EventBus.getDefault().post(new BasketFocusEventBus());
        } else if (mCurrentId == CUSTOM_FRAGMENT) {
            EventBus.getDefault().post(new CustomDetailsEvent(""));
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

//        if ((-verticalOffset) == appBarLayout.getTotalScrollRange()) {
//            mTitleScore.setVisibility(View.VISIBLE);
//            headLayout.setBackgroundColor(getResources().getColor(R.color.black));
//        } else {
//            mTitleScore.setVisibility(View.INVISIBLE);
//            headLayout.setBackgroundColor(getResources().getColor(R.color.transparency));
//        }
        headLayout.setBackgroundColor(getResources().getColor(R.color.transparency));

        if (mCollapsingToolbarLayout.getHeight() + verticalOffset < mHeadviewpager.getHeight()) {
            mRefreshLayout.setEnabled(false);   //收缩
        } else {
            mRefreshLayout.setEnabled(true); //展开
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                mLoadHandler.postDelayed(mRun, 0);
                //直播刷新
                if (isNBA ) {
                    if (mBasketLiveFragment != null) {
                        mBasketLiveFragment.refresh();
                    }
                }
//                mTalkAboutBallFragment.loadTopic(mThirdId, mThirdId, CyUtils.SINGLE_PAGE_COMMENT);
            }
        }, 1000);
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
        //  map.put("thirdId", mThirdId);
        L.d("zxcvbn", "[-----------------------------------------------------]");

        VolleyContentFast.requestJsonByGet(BaseURLs.FOOTBALL_DETAIL_COLLECTION_COUNT, map, new VolleyContentFast.ResponseSuccessListener<DetailsCollectionCountBean>() {
            @Override
            public void onResponse(DetailsCollectionCountBean jsonObject) {
                if (200 == jsonObject.getResult()) {
                    if (jsonObject.getData() != 0) {
                        mBasketDetailsHeadFragment.setBtn_showGifVisible(View.VISIBLE);
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
                        mBasketDetailsHeadFragment.setBtn_showGifVisible(View.GONE);
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
