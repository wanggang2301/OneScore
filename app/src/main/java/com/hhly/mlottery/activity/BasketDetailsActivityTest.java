package com.hhly.mlottery.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
import com.hhly.mlottery.bean.FirstEvent;
import com.hhly.mlottery.bean.basket.BasketballDetailsBean;
import com.hhly.mlottery.bean.basket.basketdetails.BasketEachTextLiveBean;
import com.hhly.mlottery.bean.websocket.WebSocketBasketBallDetails;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.basketballframe.BasketAnalyzeFragment;
import com.hhly.mlottery.frame.basketballframe.BasketAnimLiveFragment;
import com.hhly.mlottery.frame.basketballframe.BasketDetailsHeadFragment;
import com.hhly.mlottery.frame.basketballframe.BasketLiveFragment;
import com.hhly.mlottery.frame.basketballframe.BasketOddsFragment;
import com.hhly.mlottery.frame.basketballframe.FocusBasketballFragment;
import com.hhly.mlottery.frame.basketballframe.ImmedBasketballFragment;
import com.hhly.mlottery.frame.basketballframe.ResultBasketballFragment;
import com.hhly.mlottery.frame.basketballframe.ScheduleBasketballFragment;
import com.hhly.mlottery.frame.footframe.TalkAboutBallFragment;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.RongYunUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.CustomViewpager;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import me.relex.circleindicator.CircleIndicator;

/**
 * @author yixq
 *         Created by A on 2016/3/21.
 * @Description: 篮球详情的 Activity
 */
public class BasketDetailsActivityTest extends BaseWebSocketActivity implements ExactSwipeRefrashLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener, View.OnClickListener {
    public final static String BASKET_THIRD_ID = "thirdId";
    public final static String BASKET_MATCH_STATUS = "MatchStatus";
    public final static String BASKET_MATCH_LEAGUEID = "leagueId";
    public final static String BASKET_MATCH_MATCHTYPE = "matchType";
    //    0:未开赛,1:一节,2:二节,5:1'OT，以此类推
//            -1:完场,-2:待定,-3:中断,-4:取消,-5:推迟,50中场
    private final static int PRE_MATCH = 0;//赛前
    private final static int FIRST_QUARTER = 1;
    private final static int SECOND_QUARTER = 2;
    private final static int THIRD_QUARTER = 3;
    private final static int FOURTH_QUARTER = 4;
    private final static int OT1 = 5;
    private final static int OT2 = 6;
    private final static int OT3 = 7;
    private final static int END = -1;
    private final static int DETERMINED = -2;//待定
    private final static int GAME_CUT = -3;
    private final static int GAME_CANCLE = -4;
    private final static int GAME_DELAY = -5;
    private final static int HALF_GAME = 50;
    /**
     * 欧赔
     */
    public final static String ODDS_EURO = "euro";
    /**
     * 亚盘
     */
    public final static String ODDS_LET = "asiaLet";
    /**
     * 大小球
     */
    public final static String ODDS_SIZE = "asiaSize";
    private String mThirdId = "936707";
    private String mMatchStatus;
    private Context mContext;


    BasketLiveFragment mBasketLiveFragment;

    BasketAnalyzeFragment mAnalyzeFragment = new BasketAnalyzeFragment();
    TalkAboutBallFragment mTalkAboutBallFragment;

    BasketOddsFragment mOddsEuro;
    BasketOddsFragment mOddsLet;
    BasketOddsFragment mOddsSize;


    private ViewPager mViewPager;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    public AppBarLayout appBarLayout;
    private TabLayout mTabLayout;
    private TabsAdapter mTabsAdapter;
    private Toolbar toolbar;
    private CoordinatorLayout mCoordinatorLayout;
    private String[] TITLES;


    /**
     * 返回按钮
     */
    private ImageView mBack;
    /**
     * 收藏按钮
     */
    private ImageView mCollect;

    /**
     * 标题比分的布局
     */
    private RelativeLayout mTitleScore;

    private TextView mHomeScore;
    private TextView mGuestScore;

    private TextView mTitleHome;//主队比分/队名
    private TextView mTitleGuest;//客队比分/队名
    private TextView mTitleVS;//冒号  VS

    LinearLayout headLayout;// 小头部
    //显示加时比分的三个布局
    private LinearLayout mLayoutOt1;
    private LinearLayout mLayoutOt2;
    private LinearLayout mLayoutOt3;

    private int mCurrentId;
    private final int IMMEDIA_FRAGMENT = 0;
    private final int RESULT_FRAGMENT = 1;
    private final int SCHEDULE_FRAGMENT = 2;
    private final int FOCUS_FRAGMENT = 3;

    private ExactSwipeRefrashLayout mRefreshLayout; //下拉刷新

    private ImageView iv_join_room_basket;// 聊天室悬浮按钮
    private ProgressDialog pd;// 加载框
    private boolean isExit = false;// 是否取消进入聊天室动作
    private String mLeagueId; // 联赛ID
    private Integer mMatchType; //联赛类型
    private CustomViewpager mHeadviewpager;
    private BasketDetailsHeadFragment mBasketDetailsHeadFragment;
    private BasketAnimLiveFragment mBasketAnimLiveFragment;
    private CircleIndicator mIndicator;


    private BasePagerAdapter basePagerAdapter;
    private FragmentManager fragmentManager;
    private boolean isRquestSuccess = true;

    private static final String LEAGUEID_NBA = "1";


    private boolean isNBA = false;

    //  private int matchStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getExtras() != null) {
            mThirdId = getIntent().getExtras().getString(BASKET_THIRD_ID);
            mLeagueId = getIntent().getExtras().getString(BASKET_MATCH_LEAGUEID);
            mMatchType = getIntent().getExtras().getInt(BASKET_MATCH_MATCHTYPE);

            mMatchStatus = getIntent().getExtras().getString(BASKET_MATCH_STATUS);

            if (LEAGUEID_NBA.equals(mLeagueId)) {
                isNBA = true;
            }

            if (isNBA) {
                mBasketLiveFragment = BasketLiveFragment.newInstance(mThirdId);
            }

            mOddsEuro = BasketOddsFragment.newInstance(mThirdId, ODDS_EURO);
            mOddsLet = BasketOddsFragment.newInstance(mThirdId, ODDS_LET);
            mOddsSize = BasketOddsFragment.newInstance(mThirdId, ODDS_SIZE);
            mTalkAboutBallFragment = TalkAboutBallFragment.newInstance(mThirdId, mMatchStatus, 1, "");

            mCurrentId = getIntent().getExtras().getInt("currentfragment");
        }
        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic("USER.topic.basketball.score." + mThirdId + ".zh");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_details_activity_test);
        /**不统计当前的Activity界面，只统计Fragment界面*/
        MobclickAgent.openActivityDurationTrack(false);
        mContext = this;
        EventBus.getDefault().register(this);//注册EventBus
        RongYunUtils.createChatRoom(mThirdId);// 创建聊天室


//        try {
//            mSocketUri = new URI(BaseURLs.WS_SERVICE);//地址
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }

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
        loadData();

        pd.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    isExit = true;
                    iv_join_room_basket.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
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
        // 初始化加载框
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage(getResources().getString(R.string.loading_data_txt));
        // 初始化悬浮按钮
        iv_join_room_basket = (ImageView) findViewById(R.id.iv_join_room_basket);
        iv_join_room_basket.setOnClickListener(this);

        if (isNBA) {
            TITLES = new String[]{getResources().getString(R.string.basket_live), getResources().getString(R.string.basket_analyze), getResources().getString(R.string.basket_alet), getResources().getString(R.string.basket_analyze_sizeof), getResources().getString(R.string.basket_eur), getResources().getString(R.string.basket_details_talkable)};
        } else {
            TITLES = new String[]{getResources().getString(R.string.basket_analyze), getResources().getString(R.string.basket_alet), getResources().getString(R.string.basket_analyze_sizeof), getResources().getString(R.string.basket_eur), getResources().getString(R.string.basket_details_talkable)};
        }

        toolbar = (Toolbar) findViewById(R.id.basket_details_toolbar);
        setSupportActionBar(toolbar);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        mHeadviewpager = new CustomViewpager(mContext);
        mHeadviewpager = (CustomViewpager) findViewById(R.id.headviewpager);
        mIndicator = (CircleIndicator) findViewById(R.id.indicator);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mViewPager = (ViewPager) findViewById(R.id.basket_details_view_pager);
        appBarLayout = (AppBarLayout) findViewById(R.id.basket_details_appbar);
        mTabLayout = (TabLayout) findViewById(R.id.basket_details_tab_layout);
        mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
        mTabsAdapter.setTitles(TITLES);


        if (isNBA) {  //是NBA
            mTabsAdapter.addFragments(mBasketLiveFragment, mAnalyzeFragment, mOddsLet, mOddsSize, mOddsEuro, mTalkAboutBallFragment);
        } else {
            mTabsAdapter.addFragments(mAnalyzeFragment, mOddsLet, mOddsSize, mOddsEuro, mTalkAboutBallFragment);
        }

        mViewPager.setOffscreenPageLimit(5);//设置预加载页面的个数。
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        appBarLayout.addOnOffsetChangedListener(this);
        fragmentManager = getSupportFragmentManager();
        basePagerAdapter = new BasePagerAdapter(fragmentManager);

        headLayout = (LinearLayout) findViewById(R.id.basket_details_header_layout);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                isHindShow(position);
                if (position == 5) {
                    appBarLayout.setExpanded(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRefreshLayout = (ExactSwipeRefrashLayout) findViewById(R.id.basket_details_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);
        mHomeScore = (TextView) this.findViewById(R.id.basket_details_home_all_score);
        mGuestScore = (TextView) this.findViewById(R.id.basket_details_guest_all_score);

        mTitleHome = (TextView) this.findViewById(R.id.title_home_score);
        mTitleGuest = (TextView) this.findViewById(R.id.title_guest_score);
        mTitleVS = (TextView) this.findViewById(R.id.title_vs);

        mLayoutOt1 = (LinearLayout) this.findViewById(R.id.basket_details_llot1);
        mLayoutOt2 = (LinearLayout) this.findViewById(R.id.basket_details_llot2);
        mLayoutOt3 = (LinearLayout) this.findViewById(R.id.basket_details_llot3);

        mBack = (ImageView) this.findViewById(R.id.basket_details_back);

        mTitleScore = (RelativeLayout) this.findViewById(R.id.ll_basket_title_score);
        mCollect = (ImageView) this.findViewById(R.id.basket_details_collect);

        boolean isFocus = isFocusId(mThirdId);
        if (isFocus) {
            mCollect.setImageResource(R.mipmap.basketball_collected);
        } else {
            mCollect.setImageResource(R.mipmap.basketball_collect);
        }
    }

    @Override
    protected void onDestroy() { //关闭socket
        super.onDestroy();
        isExit = true;
        EventBus.getDefault().unregister(this);//取消注册EventBus
    }

    @Override
    protected void onTextResult(String text) {

        L.d("123456", text);

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

            L.d("socket", "type=" + type);
            L.d("socket", "text=" + text);

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
     * EvenBus接收消息
     *
     * @param event
     */
    public void onEventMainThread(FirstEvent event) {
        switch (event.getMsg()) {
            case RongYunUtils.CHART_ROOM_EXIT:
                L.d("xxx", "篮球EventBus收到 ");
                if (iv_join_room_basket != null) {
                    iv_join_room_basket.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    /**
     * 设置监听
     */
    private void setListener() {
        mBack.setOnClickListener(this);
        mCollect.setOnClickListener(this);

    }

    /**
     * 请求网络数据
     */
    public void loadData() {
        isRquestSuccess = false;
        Map<String, String> params = new HashMap<>();
        params.put("thirdId", mThirdId);
        L.d("456789", mThirdId);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_BASKET_DETAILS, params, new VolleyContentFast.ResponseSuccessListener<BasketballDetailsBean>() {
            @Override
            public void onResponse(BasketballDetailsBean basketDetailsBean) {
                if (basketDetailsBean.getMatch() != null) {


                    isRquestSuccess = true;
//                    initData(basketDetailsBean);
                    mBasketDetailsHeadFragment.initData(basketDetailsBean, mTalkAboutBallFragment, mTitleGuest, mTitleHome, mTitleVS);


                    if (basketDetailsBean.getMatch().getMatchStatus() != END) {
                        connectWebSocket();
                    }
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                isRquestSuccess = false;
            }
        }, BasketballDetailsBean.class);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.basket_details_back:
                MobclickAgent.onEvent(MyApp.getContext(), "BasketDetailsActivity_Exit");
                MyApp.getContext().sendBroadcast(new Intent("closeself"));
                setResult(Activity.RESULT_OK);

                eventBusPost();

                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.basket_details_collect:
                MobclickAgent.onEvent(MyApp.getContext(), "BasketDetailsActivity_Attention");
                if (isFocusId(mThirdId)) {
                    FocusBasketballFragment.deleteFocusId(mThirdId);
                    mCollect.setImageResource(R.mipmap.basketball_collect);
                } else {
                    FocusBasketballFragment.addFocusId(mThirdId);
                    mCollect.setImageResource(R.mipmap.basketball_collected);
                }
                break;
            case R.id.iv_join_room_basket:
                MobclickAgent.onEvent(mContext, "Basketball_Join_Room");
                joinRoom();
                break;
        }
    }

    /**
     * 进入聊天室
     */
    private void joinRoom() {
        if (CommonUtils.isLogin()) {// 判断是否登录
            pd.show();
            iv_join_room_basket.setVisibility(View.GONE);

            // 判断融云服务器是否连接OK
            L.d("xxx", "融云服务器是否连接::" + RongIM.getInstance().getCurrentConnectionStatus());
            if (!"CONNECTED".equals(String.valueOf(RongIM.getInstance().getCurrentConnectionStatus()))) {
                RongYunUtils.initRongIMConnect(mContext);// 连接融云服务器
                L.d("xxx", "融云服务器重新连接 。。。。。。");
            }

            if (RongYunUtils.isRongConnent && RongYunUtils.isCreateChartRoom) {
                pd.dismiss();
                RongYunUtils.joinChatRoom(mContext, mThirdId);// 进入聊天室
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        while ((!RongYunUtils.isRongConnent || !RongYunUtils.isCreateChartRoom) && !isExit) {
                            SystemClock.sleep(1000);
                        }
                        if (!isExit) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                    RongYunUtils.joinChatRoom(mContext, mThirdId);// 进入聊天室
                                }
                            });
                        }
                    }
                }.start();
            }
        } else {
            // 跳转到登录界面
            Intent intent1 = new Intent(mContext, LoginActivity.class);
            startActivityForResult(intent1, RongYunUtils.CHART_ROOM_QUESTCODE_BASKET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.d("xxx", ">>>requestCode:" + requestCode);
        L.d("xxx", ">>>resultCode:" + resultCode);
        if (requestCode == RongYunUtils.CHART_ROOM_QUESTCODE_BASKET && resultCode == -1) {
            joinRoom();
        }
        if (requestCode == CyUtils.JUMP_COMMENT_QUESTCODE) {
            switch (resultCode) {
                case CyUtils.RESULT_OK:
                    mTalkAboutBallFragment.getResultOk();
                    break;
                case CyUtils.RESULT_CODE://接收评论输入页面返回
                    mTalkAboutBallFragment.getResultCode();
                    break;
                case CyUtils.RESULT_BACK://接收评论输入页面返回
                    mTalkAboutBallFragment.getResultBack();
                    break;
            }
        }
    }

    // 评论登录跳转
    public void talkAboutBallLoginBasket() {
        //跳转登录界面
        Intent intent1 = new Intent(mContext, LoginActivity.class);
        startActivityForResult(intent1, CyUtils.JUMP_COMMENT_QUESTCODE);
    }

    // 发表评论跳转
    public void talkAboutBallSendBasket(long topicid) {
        Intent intent2 = new Intent(mContext, InputActivity.class);
        intent2.putExtra(CyUtils.INTENT_PARAMS_SID, topicid);
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

    /**
     * 判断thirdId是否已经关注
     *
     * @param thirdId
     * @return true已关注，false还没关注
     */
    private boolean isFocusId(String thirdId) {
        String focusIds = PreferenceUtil.getString(FocusBasketballFragment.BASKET_FOCUS_IDS, "");

        if ("".equals(focusIds)) {
            return false;
        } else {
            String[] focusIdArray = focusIds.split(",");

            boolean isFocus = false;
            for (String focusId : focusIdArray) {
                if (focusId.equals(thirdId)) {
                    isFocus = true;
                    break;
                }
            }
            return isFocus;
        }
    }

//    /**
//     * 添加关注
//     *
//     * @param thirdId
//     */
//    private void addFocusId(String thirdId) {
//        String focusIds = PreferenceUtil.getString(BASKET_FOCUS_IDS, "");
//        if ("".equals(focusIds)) {
//            PreferenceUtil.commitString(BASKET_FOCUS_IDS, thirdId);
//        } else {
//            PreferenceUtil.commitString(BASKET_FOCUS_IDS, focusIds + "," + thirdId);
//        }
//    }
//
//    /**
//     * 取消关注
//     *
//     * @param thirdId
//     */
//    private void deleteFocusId(String thirdId) {
//        String focusIds = PreferenceUtil.getString(BASKET_FOCUS_IDS, "");
//        String[] idArray = focusIds.split(",");
//        StringBuffer sb = new StringBuffer();
//        for (String id : idArray) {
//            if (!id.equals(thirdId)) {
//                if ("".equals(sb.toString())) {
//                    sb.append(id);
//                } else {
//                    sb.append("," + id);
//                }
//
//            }
//        }
//        PreferenceUtil.commitString(BASKET_FOCUS_IDS, sb.toString());
//    }

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
                    // Log.e(TAG, "ws_json = " + ws_json);
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
        mBasketDetailsHeadFragment.updateData(basketBallDetails, mTalkAboutBallFragment, mTitleGuest, mTitleHome, mTitleVS);
    }

    /**
     * 接受文字直播推送，更新数据
     */
    private void updateTextLive(BasketEachTextLiveBean basketEachTextLiveBean) {
        mBasketLiveFragment.updateTextLive(basketEachTextLiveBean);
    }

    private void eventBusPost() {
        if (ImmedBasketballFragment.BasketImmedEventBus != null) {
            ImmedBasketballFragment.BasketImmedEventBus.post("");
        }
        if (mCurrentId == IMMEDIA_FRAGMENT) {
            if (ImmedBasketballFragment.BasketImmedEventBus != null) {
                ImmedBasketballFragment.BasketImmedEventBus.post("");
            }
        } else if (mCurrentId == RESULT_FRAGMENT) {
            if (ResultBasketballFragment.BasketResultEventBus != null) {
                ResultBasketballFragment.BasketResultEventBus.post("");
            }
        } else if (mCurrentId == SCHEDULE_FRAGMENT) {
            if (ScheduleBasketballFragment.BasketScheduleEventBus != null) {
                ScheduleBasketballFragment.BasketScheduleEventBus.post("");
            }
        } else if (mCurrentId == FOCUS_FRAGMENT) {
            if (FocusBasketballFragment.BasketFocusEventBus != null) {
                FocusBasketballFragment.BasketFocusEventBus.post("");
            }
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if ((-verticalOffset) == appBarLayout.getTotalScrollRange()) {
            mTitleScore.setVisibility(View.VISIBLE);
            headLayout.setBackgroundColor(getResources().getColor(R.color.black));
        } else {
            mTitleScore.setVisibility(View.INVISIBLE);
            headLayout.setBackgroundColor(getResources().getColor(R.color.transparency));
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                loadData();
                mAnalyzeFragment.initData();
                mOddsEuro.initData();
                mOddsLet.initData();
                mOddsSize.initData();
                mTalkAboutBallFragment.loadTopic(mThirdId, mThirdId, CyUtils.SINGLE_PAGE_COMMENT);
            }
        }, 1000);
    }

    /**
     * 直播、分析、欧赔、亚盘、大小、聊球Fragment页面统计
     */
    private boolean isFragment0 = true;
    private boolean is0 = false;
    private boolean isFragment1 = false;
    private boolean is1 = false;
    private boolean isFragment2 = false;
    private boolean is2 = false;
    private boolean isFragment3 = false;
    private boolean is3 = false;
    private boolean isFragment4 = false;
    private boolean is4 = false;

    private boolean isFragment5 = false;
    private boolean is5 = false;

    private void isHindShow(int position) {
        switch (position) {

            case 0: //直播
                isFragment0 = false;
                isFragment1 = false;
                isFragment2 = false;
                isFragment3 = false;
                isFragment4 = false;
                isFragment5 = true;
                break;

            case 1:// 分析
                isFragment0 = true;
                isFragment1 = false;
                isFragment2 = false;
                isFragment3 = false;
                isFragment4 = false;
                isFragment5 = false;

                break;
            case 4:// 欧赔
                isFragment0 = false;
                isFragment1 = true;
                isFragment2 = false;
                isFragment3 = false;
                isFragment4 = false;
                isFragment5 = false;

                break;
            case 2:// 亚盘
                isFragment0 = false;
                isFragment1 = false;
                isFragment2 = true;
                isFragment3 = false;
                isFragment4 = false;
                isFragment5 = false;

                break;
            case 3:// 大小
                isFragment0 = false;
                isFragment1 = false;
                isFragment2 = false;
                isFragment3 = true;
                isFragment4 = false;
                isFragment5 = false;

                break;
            case 5:// 聊球
                isFragment0 = false;
                isFragment1 = false;
                isFragment2 = false;
                isFragment3 = false;
                isFragment4 = true;
                isFragment5 = false;

                break;
        }

        if (is5) {
            MobclickAgent.onPageEnd("BasketBall_Info_ZB");
            is5 = false;
            L.d("xxx", "直播隐藏");
        }


        if (is0) {
            MobclickAgent.onPageEnd("BasketBall_Info_FX");
            is0 = false;
            L.d("xxx", "分析隐藏");
        }
        if (is1) {
            MobclickAgent.onPageEnd("BasketBall_Info_OP");
            is1 = false;
            L.d("xxx", "欧赔隐藏");
        }
        if (is2) {
            MobclickAgent.onPageEnd("BasketBall_Info_YP");
            is2 = false;
            L.d("xxx", "亚盘隐藏");
        }
        if (is3) {
            MobclickAgent.onPageEnd("BasketBall_Info_DX");
            is3 = false;
            L.d("xxx", "大小隐藏");
        }
        if (is4) {
            MobclickAgent.onPageEnd("BasketBall_Info_LQ");
            is4 = false;
            L.d("xxx", "聊球隐藏");
        }


        if (isFragment5) {
            MobclickAgent.onPageStart("BasketBall_Info_ZB");
            is5 = true;
            L.d("xxx", "直播显示");
        }

        if (isFragment0) {
            MobclickAgent.onPageStart("BasketBall_Info_FX");
            is0 = true;
            L.d("xxx", "分析显示");
        }
        if (isFragment1) {
            MobclickAgent.onPageStart("BasketBall_Info_OP");
            is1 = true;
            L.d("xxx", "欧赔显示");
        }
        if (isFragment2) {
            MobclickAgent.onPageStart("BasketBall_Info_YP");
            is2 = true;
            L.d("xxx", "亚盘显示");
        }
        if (isFragment3) {
            MobclickAgent.onPageStart("BasketBall_Info_DX");
            is3 = true;
            L.d("xxx", "大小显示");
        }
        if (isFragment4) {
            MobclickAgent.onPageStart("BasketBall_Info_LQ");
            is4 = true;
            L.d("xxx", "聊球显示");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (isFragment0) {
            MobclickAgent.onPageStart("BasketBall_Info_FX");
            is0 = true;
            L.d("xxx", "分析显示");
        }
        if (isFragment1) {
            MobclickAgent.onPageStart("BasketBall_Info_OP");
            is1 = true;
            L.d("xxx", "欧赔显示");
        }
        if (isFragment2) {
            MobclickAgent.onPageStart("BasketBall_Info_YP");
            is2 = true;
            L.d("xxx", "亚盘显示");
        }
        if (isFragment3) {
            MobclickAgent.onPageStart("BasketBall_Info_DX");
            is3 = true;
            L.d("xxx", "大小显示");
        }
        if (isFragment4) {
            MobclickAgent.onPageStart("BasketBall_Info_LQ");
            is4 = true;
            L.d("xxx", "聊球显示");
        }
        if (isFragment5) {
            MobclickAgent.onPageStart("BasketBall_Info_ZB");
            is5 = true;
            L.d("xxx", "聊球显示");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (is0) {
            MobclickAgent.onPageEnd("BasketBall_Info_FX");
            is0 = false;
            L.d("xxx", "分析 隐藏");
        }
        if (is1) {
            MobclickAgent.onPageEnd("BasketBall_Info_OP");
            is1 = false;
            L.d("xxx", "欧赔 隐藏");
        }
        if (is2) {
            MobclickAgent.onPageEnd("BasketBall_Info_YP");
            is2 = false;
            L.d("xxx", "亚盘 隐藏");
        }
        if (is3) {
            MobclickAgent.onPageEnd("BasketBall_Info_DX");
            is3 = false;
            L.d("xxx", "大小 隐藏");
        }
        if (is4) {
            MobclickAgent.onPageEnd("BasketBall_Info_LQ");
            is4 = false;
            L.d("xxx", "聊球隐藏");
        }

        if (is5) {
            MobclickAgent.onPageEnd("BasketBall_Info_ZB");
            is5 = false;
            L.d("xxx", "直播隐藏");
        }
    }
}
