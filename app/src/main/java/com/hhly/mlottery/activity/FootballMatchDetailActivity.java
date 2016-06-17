package com.hhly.mlottery.activity;

import android.animation.ObjectAnimator;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.MatchDetail;
import com.hhly.mlottery.bean.footballDetails.MatchLike;
import com.hhly.mlottery.bean.footballDetails.PlayerInfo;
import com.hhly.mlottery.callback.ShareCopyLinkCallBack;
import com.hhly.mlottery.callback.ShareTencentCallBack;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.footframe.OldAnalyzeFragment;
import com.hhly.mlottery.frame.footframe.FocusFragment;
import com.hhly.mlottery.frame.footframe.ImmediateFragment;
import com.hhly.mlottery.frame.footframe.OddsFragment;
import com.hhly.mlottery.frame.footframe.ResultFragment;
import com.hhly.mlottery.frame.footframe.ScheduleFragment;
import com.hhly.mlottery.frame.footframe.StadiumFragment;
import com.hhly.mlottery.frame.footframe.TalkAboutBallFragment;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ShareConstants;
import com.hhly.mlottery.util.StringUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;
import com.hhly.mlottery.widget.ToolbarScrollerView;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 足球详细比赛信息
 */
public class FootballMatchDetailActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public final static String BUNDLE_PARAM_THIRDID = "thirdId";

    private final static String TAG = "FootballMatchDetailActivity";
    private final static String PARAMS = "DetailBundle";


    private final static int PERIOD_test = 1000 * 6;//刷新周期测试
    private final static int PERIOD_20 = 1000 * 60 * 20;//刷新周期二十分钟
    private final static int PERIOD_5 = 1000 * 60 * 5;//刷新周期五分钟


    private final int IMMEDIA_FRAGMENT = 0;
    private final int RESULT_FRAGMENT = 1;
    private final int SCHEDULE_FRAGMENT = 2;
    private final int FOCUS_FRAGMENT = 3;

    /**
     * 赛前轮询周期
     */
    private int mReloadPeriod = PERIOD_20;


    private ViewPager mViewPager;

    /**
     * 切换Viewpager的标签
     */
    private TextView mTab1;
    private TextView mTab2;
    private TextView mTab3;
    private TextView mTab4;

    /**
     * 主队名
     */
    private TextView mHomeNameText;

    /**
     * 主队队徽
     */
    private ImageView mHomeEmblem;
    /**
     * 主队点赞
     */
    private ImageView mHomeLike;
    /**
     * 主队点赞动画的image
     */
    private ImageView mHomeAnimLike;
    /**
     * 主队点赞总数
     */
    private TextView mHomeLikeCount;

    /**
     * VS的textview
     */
    private TextView mVSText;
    /**
     * 赛事类型
     */
    private RelativeLayout mMatchTypeLayout;
    /**
     * 联赛
     */
    private TextView mMatchType1;
    /**
     * 联赛
     */
    private TextView mMatchType2;

    private TextView mGuestNameText;
    private ImageView mGuestEmblem;
    private ImageView mGuestLike;
    private ImageView mGuestAnimLike;
    private TextView mGuestLikeCount;


    private RelativeLayout mProgressBarLayout;


    /**
     * 异常界面
     */
    private LinearLayout mExceptionLayout;

    /**
     * 点赞动画
     */
    private AnimationSet mRiseHomeAnim;
    private AnimationSet mRiseGuestAnim;

    public ExactSwipeRefrashLayout mRefreshLayout;

//    private ProgressDialog progressDialog;

    /**
     * 赛事id
     */
    public String mThirdId;


    private MatchDetail mMatchDetail;
    /**
     * 保存上一场的状态
     */
    private String mPreStatus;

    private StadiumFragment mStadiumFragment;
    private OldAnalyzeFragment mOldAnalyzeFragment;
    private OddsFragment mOddsFragment;
    private TalkAboutBallFragment mTalkAboutBallFragment;

    /**
     * 判断ViewPager是否已经初始化过
     */
    private boolean isInitedViewPager = false;

    private MatchDetailFragmentAdapter mViewPagerAdapter;
    private List<Fragment> fragments;

    private int currentFragmentId = 0;


    private String shareHomeIconUrl;


    private SharePopupWindow sharePopupWindow;

    private ShareTencentCallBack mShareTencentCallBack;

    private ShareCopyLinkCallBack mShareCopyLinkCallBack;

    private Tencent mTencent;

    /**
     * 备份用户本身屏幕的时间
     */
//    private int mScreenOffTimeoutTemp = 30 * 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        L.w(TAG, "onCreate");
        setContentView(R.layout.activity_football_match_detail);
        MobclickAgent.openActivityDurationTrack(false);
        if (getIntent().getExtras() != null) {
            mThirdId = getIntent().getExtras().getString(BUNDLE_PARAM_THIRDID, "1300");
            currentFragmentId = getIntent().getExtras().getInt("currentFragmentId");

        }

        L.e(TAG, "mThirdId = " + mThirdId);
        //mThirdId = "244752";


        initAnim();
        initContainer();
        initToolbar();
        initTabs();

        mRefreshLayout = (ExactSwipeRefrashLayout) findViewById(R.id.football_match_detail_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);


        mProgressBarLayout = (RelativeLayout) findViewById(R.id.football_match_detail_progressbar);

        mExceptionLayout = (LinearLayout) findViewById(R.id.network_exception_layout);

        TextView reloadBtn = (TextView) findViewById(R.id.network_exception_reload_btn);
        reloadBtn.setOnClickListener(this);


        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);


        loadData();
//        loadImage();

        mShareTencentCallBack = new ShareTencentCallBack() {
            @Override
            public void onClick(int flag) {
                // shareQQ(flag);
                onClickShare(flag);
            }
        };

        mShareCopyLinkCallBack = new ShareCopyLinkCallBack() {
            @Override
            public void onClick() {
                ClipboardManager cmb = (ClipboardManager) FootballMatchDetailActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText("http://m.13322.com");

            }
        };
    }

    private final static int VIEW_STATUS_LOADING = 1;
    private final static int VIEW_STATUS_SUCCESS = 3;
    private final static int VIEW_STATUS_NET_ERROR = 4;

    private Handler mViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case VIEW_STATUS_LOADING:
                    //progressDialog.show();
//                    mRefreshLayout.setRefreshing(true);
                    mExceptionLayout.setVisibility(View.GONE);
                    mProgressBarLayout.setVisibility(View.VISIBLE);
//                    mRefreshLayout.setEnabled(false);
                    mTab1.setClickable(false);
                    mTab2.setClickable(false);
                    mTab3.setClickable(false);
                    mTab4.setClickable(false);
                    break;
                case VIEW_STATUS_SUCCESS:
//                    mRefreshLayout.setRefreshing(false);
                    mProgressBarLayout.setVisibility(View.GONE);
                    //progressDialog.cancel();
//                    mRefreshLayout.setEnabled(true);
                    mTab1.setClickable(true);
                    mTab2.setClickable(true);
                    mTab3.setClickable(true);
                    mTab4.setClickable(true);
                    mExceptionLayout.setVisibility(View.GONE);
                    mRefreshLayout.setVisibility(View.VISIBLE);
                    break;
                case VIEW_STATUS_NET_ERROR:
                    if (isInitedViewPager) {
                        Toast.makeText(getApplicationContext(), R.string.exp_net_status_txt, Toast.LENGTH_SHORT).show();
                    } else {
                        mTab1.setClickable(false);
                        mTab2.setClickable(false);
                        mTab3.setClickable(false);
                        mTab4.setClickable(false);
//                        mRefreshLayout.setEnabled(true);
                        mRefreshLayout.setVisibility(View.GONE);
                        mExceptionLayout.setVisibility(View.VISIBLE);
                        mProgressBarLayout.setVisibility(View.GONE);
                    }

                    break;
            }
        }
    };


    /**
     * 初始化动画
     */
    private void initAnim() {
        mRiseHomeAnim = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, 0f, -50f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        mRiseHomeAnim.addAnimation(translateAnimation);
        mRiseHomeAnim.addAnimation(alphaAnimation);
        mRiseHomeAnim.setDuration(300);
        mRiseHomeAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHomeAnimLike.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mRiseGuestAnim = new AnimationSet(true);
        mRiseGuestAnim.addAnimation(translateAnimation);
        mRiseGuestAnim.addAnimation(alphaAnimation);
        mRiseGuestAnim.setDuration(300);
        mRiseGuestAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mGuestAnimLike.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    //加载图片
    private void loadImage(String imageUrl, final ImageView imageView) {
//        String imageUrl = "http://pic.13322.com/icons/teams/100/tm_logo_27.png";
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


    //初始化
    private void initContainer() {
        mHomeNameText = (TextView) findViewById(R.id.activity_football_match_detail_home_name);
        mHomeEmblem = (ImageView) findViewById(R.id.football_match_detail_home_emblem);
        mHomeLike = (ImageView) findViewById(R.id.football_match_detail_home_like);
        mHomeAnimLike = (ImageView) findViewById(R.id.football_match_detail_home_like_anim_img);
        mHomeLikeCount = (TextView) findViewById(R.id.football_match_detail_home_like_count);

        mVSText = (TextView) findViewById(R.id.football_match_detail_vs_tv);
        mMatchTypeLayout = (RelativeLayout) findViewById(R.id.football_match_detail_matchtype_layout);
        mMatchType1 = (TextView) findViewById(R.id.football_match_detail_matchtype1);
        mMatchType2 = (TextView) findViewById(R.id.football_match_detail_matchtype2);

        mGuestNameText = (TextView) findViewById(R.id.activity_football_match_detail_guest_name);
        mGuestEmblem = (ImageView) findViewById(R.id.football_match_detail_guest_emblem);
        mGuestLike = (ImageView) findViewById(R.id.football_match_detail_guest_like);
        mGuestAnimLike = (ImageView) findViewById(R.id.football_match_detail_guest_like_anim_img);
        mGuestLikeCount = (TextView) findViewById(R.id.football_match_detail_guest_like_count);


        mHomeLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomeAnimLike.setVisibility(View.VISIBLE);
                mHomeAnimLike.startAnimation(mRiseHomeAnim);

                //String url = "http://192.168.10.242:8181/mlottery/core/footBallMatch.updLike.do";
                Map<String, String> params = new HashMap<>();
                params.put("thirdId", mThirdId);
                params.put("homeAdd", "1");
                VolleyContentFast.requestJsonByPost(BaseURLs.URL_FOOTBALL_DETAIL_LIKE_INFO, params, new VolleyContentFast.ResponseSuccessListener<MatchLike>() {
                    @Override
                    public void onResponse(MatchLike matchLike) {
                        mHomeLikeCount.setText(matchLike.getHomeLike());
                        mGuestLikeCount.setText(matchLike.getGuestLike());

                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                    }
                }, MatchLike.class);

            }
        });

        mGuestLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGuestAnimLike.setVisibility(View.VISIBLE);
                mGuestAnimLike.startAnimation(mRiseGuestAnim);
                //String url = "http://192.168.10.242:8181/mlottery/core/footBallMatch.updLike.do";
                Map<String, String> params = new HashMap<>();
                params.put("thirdId", mThirdId);
                params.put("guestAdd", "1");
                VolleyContentFast.requestJsonByPost(BaseURLs.URL_FOOTBALL_DETAIL_LIKE_INFO, params, new VolleyContentFast.ResponseSuccessListener<MatchLike>() {
                    @Override
                    public void onResponse(MatchLike matchLike) {

                        mHomeLikeCount.setText(matchLike.getHomeLike());
                        mGuestLikeCount.setText(matchLike.getGuestLike());

                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                    }
                }, MatchLike.class);
            }
        });

        setClickableLikeBtn(false);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    private void loadData() {


//        progressDialog.show();
//        String url = "http://192.168.10.242:8181/mlottery/core/footBallMatch.queryMatchInfos.do";
        Map<String, String> params = new HashMap<>();
//        mThirdId = "244828";

        //mThirdId="244235";
        params.put("thirdId", mThirdId);

//        String url = "http://m.13322.com/mlottery/core/matchResults.findImmediateMatchs.do";
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_INFO, params, new VolleyContentFast.ResponseSuccessListener<MatchDetail>() {
            @Override
            public void onResponse(MatchDetail matchDetail) {

                if (!"200".equals(matchDetail.getResult())) {
//                    mViewHandler.sendEmptyMessageDelayed(VIEW_STATUS_NET_ERROR,5000);
                    mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
                    return;
                }

                L.w(TAG, "success..");
                List<PlayerInfo> playerInfos = new ArrayList<>();
                playerInfos.add(new PlayerInfo("阿兰·肯尼迪"));
                playerInfos.add(new PlayerInfo("菲尔·尼尔"));
                playerInfos.add(new PlayerInfo("休斯"));
                playerInfos.add(new PlayerInfo("勒索克斯"));
                playerInfos.add(new PlayerInfo("兰帕德"));
                playerInfos.add(new PlayerInfo("奥斯古德"));
                playerInfos.add(new PlayerInfo("德塞利"));
                playerInfos.add(new PlayerInfo("克拉克"));
                playerInfos.add(new PlayerInfo("博尔蒂"));


//                matchDetail.getHomeTeamInfo().setLineup(playerInfos);
//                matchDetail.getGuestTeamInfo().setLineup(playerInfos);
                mMatchDetail = matchDetail;

                //数据加载成功后显示分享
                mShare.setVisibility(View.VISIBLE);
                mPreStatus = matchDetail.getLiveStatus();

                if (!isInitedViewPager) {//如果ViewPager未初始化
                    initViewPager(matchDetail);
                } else {

                    //下拉刷新
                    if ("0".equals(mPreStatus) && "0".equals(matchDetail.getLiveStatus()) && !isFinishing()) {//如果上一个状态是赛前，目前状态也是赛前，更新页面数据即可
                        mStadiumFragment.setMatchDetail(matchDetail);
                        mStadiumFragment.initPreData();
                    } else if ("0".equals(mPreStatus) && "1".equals(matchDetail.getLiveStatus()) && !isFinishing()) {//从赛前跳到赛中
                        mStadiumFragment.setMatchDetail(matchDetail);
                        mStadiumFragment.activateMatch();
                        mReloadTimer.cancel();
                    } else if ("1".equals(mPreStatus) && "1".equals(matchDetail.getLiveStatus()) && !isFinishing()) {
                        mStadiumFragment.refreshStadiumData(matchDetail);
                    }
                }

                mHomeNameText.setText(matchDetail.getHomeTeamInfo().getName());
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
                mTab4.setClickable(true);

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

                mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
            }
        }, MatchDetail.class);
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
//            mReloadTimer.schedule(reloadTimerTask, 2000, PERIOD_test);
            mReloadTimer.schedule(reloadTimerTask, 2000, mReloadPeriod);
            isStartTimer = true;
        }
    }


    private void initTabs() {
        mTab1 = (TextView) findViewById(R.id.football_match_detail_tab1);
        mTab2 = (TextView) findViewById(R.id.football_match_detail_tab2);
        mTab3 = (TextView) findViewById(R.id.football_match_detail_tab3);
        mTab4 = (TextView) findViewById(R.id.football_match_detail_tab4);
        findViewById(R.id.football_match_detail_tab1).setOnClickListener(this);
        findViewById(R.id.football_match_detail_tab2).setOnClickListener(this);
        findViewById(R.id.football_match_detail_tab3).setOnClickListener(this);
        findViewById(R.id.football_match_detail_tab4).setOnClickListener(this);
    }

    /**
     * 赛场、指数、分析Fragment页面统计
     */
    private boolean isStadiumFragment = true;
    private boolean isStadium = false;
    private boolean isOddsFragment = false;
    private boolean isOdds = false;
    private boolean isAnalyzeFragment = false;
    private boolean isAnalyze = false;

    private void initViewPager(MatchDetail matchDetail) {
        mViewPager = (ViewPager) findViewById(R.id.football_match_detail_viewpager);

        fragments = new ArrayList<>();
        if ("0".equals(matchDetail.getLiveStatus())) {
            mStadiumFragment = StadiumFragment.newInstance(StadiumFragment.STADIUM_TYPE_PRE, matchDetail);
        } else {
            //赛中和赛后
            mStadiumFragment = StadiumFragment.newInstance(StadiumFragment.STADIUM_TYPE_ING, matchDetail);
        }
        fragments.add(mStadiumFragment);

        //分析
        mOldAnalyzeFragment = OldAnalyzeFragment.newInstance(mThirdId, matchDetail.getHomeTeamInfo().getName(), matchDetail.getGuestTeamInfo().getName());
        //  mOldAnalyzeFragment = OldAnalyzeFragment.newInstance(mThirdId,"ni","hao");
        //指数
        mOddsFragment = OddsFragment.newInstance("", "");

        //聊球
        mTalkAboutBallFragment = new TalkAboutBallFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param1", mThirdId);//"1072757"
        bundle.putInt("type", 0);//1 籃球/0 足球
        bundle.putString("state", mPreStatus);
        System.out.println("fggstate=" + mPreStatus);
        mTalkAboutBallFragment.setArguments(bundle);

        fragments.add(mOddsFragment);
        fragments.add(mOldAnalyzeFragment);
//        fragments.add(mChatFragment);
        fragments.add(mTalkAboutBallFragment);


        mViewPagerAdapter = new MatchDetailFragmentAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(3);//设置预加载页面的个数。
        mViewPager.setAdapter(mViewPagerAdapter);


        final LinearLayout tabLine = (LinearLayout) findViewById(R.id.football_match_detail_tabline);
        final LinearLayout tabLineLayout = (LinearLayout) findViewById(R.id.football_match_detail_tabline_layout);
        int displayWidth = DeviceInfo.getDisplayWidth(getApplicationContext());
        tabLine.setLayoutParams(new LinearLayout.LayoutParams(displayWidth / 4, LinearLayout.LayoutParams.WRAP_CONTENT));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int lineWidth = tabLine.getWidth();
                int marginLeft = (int) (lineWidth * (position + positionOffset));
                tabLineLayout.setPadding(marginLeft, 0, 0, 0);
            }

            @Override
            public void onPageSelected(int position) {
                mTab1.setTextColor(getResources().getColor(R.color.content_txt_black));
                mTab2.setTextColor(getResources().getColor(R.color.content_txt_black));
                mTab3.setTextColor(getResources().getColor(R.color.content_txt_black));
                mTab4.setTextColor(getResources().getColor(R.color.content_txt_black));


                switch (position) {
                    case 0:
                        isStadiumFragment = true;
                        isOddsFragment = false;
                        isAnalyzeFragment = false;
                        mTab1.setTextColor(getResources().getColor(R.color.tab_text));
                        mRefreshLayout.setEnabled(true);
                        break;
                    case 1:
                        isStadiumFragment = false;
                        isOddsFragment = true;
                        isAnalyzeFragment = false;
                        mTab2.setTextColor(getResources().getColor(R.color.tab_text));
                        mRefreshLayout.setEnabled(true);
                        break;
                    case 2:
                        isStadiumFragment = false;
                        isOddsFragment = false;
                        isAnalyzeFragment = true;
                        mTab3.setTextColor(getResources().getColor(R.color.tab_text));
                        mRefreshLayout.setEnabled(false);
                        break;
                    case 3:
                        isStadiumFragment = false;
                        isOddsFragment = false;
                        isAnalyzeFragment = true;
                        mTab4.setTextColor(getResources().getColor(R.color.tab_text));
                        mRefreshLayout.setEnabled(false);
                        break;
                }
                if (isStadiumFragment) {
                    if (isOdds) {
                        MobclickAgent.onPageEnd("Football_MatchDataInfo_OddsFragment");
                        isOdds = false;
                        L.d("xxx", "isOddsFragment>>>隐藏");
                    }
                    if (isAnalyze) {
                        MobclickAgent.onPageEnd("Football_MatchDataInfo_AnalyzeFragment");
                        isAnalyze = false;
                        L.d("xxx", "isAnalyzeFragment>>>隐藏");
                    }
                    MobclickAgent.onPageStart("Football_MatchDataInfo_StadiumFragment");
                    isStadium = true;
                    L.d("xxx", "isStadiumFragment>>>显示");
                }
                if (isOddsFragment) {
                    if (isStadium) {
                        MobclickAgent.onPageEnd("Football_MatchDataInfo_StadiumFragment");
                        isStadium = false;
                        L.d("xxx", "StadiumFragment>>>隐藏");
                    }
                    if (isAnalyze) {
                        MobclickAgent.onPageEnd("Football_MatchDataInfo_AnalyzeFragment");
                        isAnalyze = false;
                        L.d("xxx", "isAnalyzeFragment>>>隐藏");
                    }
                    MobclickAgent.onPageStart("Football_MatchDataInfo_OddsFragment");
                    isOdds = true;
                    L.d("xxx", "isOddsFragment>>>显示");
                }
                if (isAnalyzeFragment) {
                    if (isStadium) {
                        MobclickAgent.onPageEnd("Football_MatchDataInfo_StadiumFragment");
                        isStadium = false;
                        L.d("xxx", "StadiumFragment>>>隐藏");
                    }
                    if (isOdds) {
                        MobclickAgent.onPageEnd("Football_MatchDataInfo_OddsFragment");
                        isOdds = false;
                        L.d("xxx", "isOddsFragment>>>隐藏");
                    }
                    MobclickAgent.onPageStart("Football_MatchDataInfo_AnalyzeFragment");
                    isAnalyze = true;
                    L.d("xxx", "isOddsFragment>>>显示");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        isInitedViewPager = true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isStadiumFragment) {
            MobclickAgent.onPageStart("Football_MatchDataInfo_StadiumFragment");
            isStadium = true;
            L.d("xxx", "isStadiumFragment>>>显示");
        }
        if (isOddsFragment) {
            MobclickAgent.onPageStart("Football_MatchDataInfo_OddsFragment");
            isOdds = true;
            L.d("xxx", "isOddsFragment>>>显示");
        }
        if (isAnalyzeFragment) {
            MobclickAgent.onPageStart("Football_MatchDataInfo_AnalyzeFragment");
            isAnalyze = true;
            L.d("xxx", "isAnalyzeFragment>>>显示");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isStadium) {
            MobclickAgent.onPageEnd("Football_MatchDataInfo_StadiumFragment");
            isStadium = false;
            L.d("xxx", "isStadiumFragment>>>隐藏");
        }
        if (isOdds) {
            MobclickAgent.onPageEnd("Football_MatchDataInfo_OddsFragment");
            isOdds = false;
            L.d("xxx", "isOddsFragment>>>隐藏");
        }
        if (isAnalyze) {
            MobclickAgent.onPageEnd("Football_MatchDataInfo_AnalyzeFragment");
            isAnalyze = false;
            L.d("xxx", "isAnalyzeFragment>>>隐藏");
        }
    }

    private TextView mHeaderHomeNameText;
    private TextView mHeaderGuestNameText;
    private ImageView mFocusImg;
    private ImageView mShare;


    private void initToolbar() {
        final ToolbarScrollerView toolbarScrollerView = (ToolbarScrollerView) findViewById(R.id.football_match_detail_scroller_view);
        toolbarScrollerView.setPaddingChangedListener(new ToolbarScrollerView.PaddingChangedListener() {
            @Override
            public void onPaddingChanged(int percentage) {
                //L.d(TAG, "percentage = " + percentage);
                findViewById(R.id.football_match_team_detail_layout).setAlpha((100 - percentage) / 100f);
                if (percentage == 100) {
                    View view = findViewById(R.id.layout_match_header_team_layout);
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                    objectAnimator.setDuration(500);
                    objectAnimator.start();
                }

                if (percentage == 0) {
                    View view = findViewById(R.id.layout_match_header_team_layout);
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
                    objectAnimator.setDuration(500);
                    objectAnimator.start();
                }
            }
        });

        toolbarScrollerView.setToolbarImg((ImageView) findViewById(R.id.football_match_detail_toolbar_img));
        toolbarScrollerView.setToolbarContainer((LinearLayout) findViewById(R.id.football_match_detail_toolbar_container));

        findViewById(R.id.layout_match_header_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarScrollerView.openToolbar();
            }
        });


        mHeaderHomeNameText = (TextView) findViewById(R.id.layout_match_header_hometeam_name);
        mHeaderGuestNameText = (TextView) findViewById(R.id.layout_match_header_guest_name);
        mFocusImg = (ImageView) findViewById(R.id.layout_match_header_focus_img);
        mShare = (ImageView) findViewById(R.id.iv_share);


        findViewById(R.id.layout_match_header_back).setOnClickListener(this);
        mFocusImg.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mShare.setVisibility(View.GONE);

        boolean isFocus = FocusFragment.isFocusId(mThirdId);

        if (isFocus) {
            mFocusImg.setImageResource(R.mipmap.article_like_hover);
        } else {
            mFocusImg.setImageResource(R.mipmap.article_like);
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.football_match_detail_tab1:
                MobclickAgent.onEvent(mContext, "Football_MatchDataInfo_CourtTab");
                mViewPager.setCurrentItem(0);
                break;
            case R.id.football_match_detail_tab2:
                MobclickAgent.onEvent(mContext, "Football_MatchDataInfo_CPITab");
                mViewPager.setCurrentItem(1);
                break;
            case R.id.football_match_detail_tab3:
                MobclickAgent.onEvent(mContext, "Football_MatchDataInfo_AnalysisTab");
                mViewPager.setCurrentItem(2);
                break;
            case R.id.football_match_detail_tab4:
                MobclickAgent.onEvent(mContext, "Football_MatchDataInfo_AnalysisTab");
                mViewPager.setCurrentItem(3);
                break;
            case R.id.layout_match_header_back:
                MobclickAgent.onEvent(mContext, "Football_MatchDataInfo_Exit");
                eventBusPost();

                // setResult(Activity.RESULT_OK);
                sendBroadcast(new Intent("closeself"));
                finish();
                break;
            case R.id.layout_match_header_focus_img:
                MobclickAgent.onEvent(mContext, "Football_MatchDataInfo_Focus");
                if (FocusFragment.isFocusId(mThirdId)) {
                    FocusFragment.deleteFocusId(mThirdId);
                    mFocusImg.setImageResource(R.mipmap.article_like);
                } else {
                    FocusFragment.addFocusId(mThirdId);
                    mFocusImg.setImageResource(R.mipmap.article_like_hover);
                }

                break;

            case R.id.iv_share: //分享

                MobclickAgent.onEvent(mContext, "Football_MatchDataInfo_Share");
                Map<String, String> data = new HashMap<String, String>();
                String title = mMatchDetail.getHomeTeamInfo().getName() + " VS " + mMatchDetail.getGuestTeamInfo().getName();
                String summary = getString(R.string.share_summary);
                data.put(ShareConstants.TITLE, title);
                data.put(ShareConstants.SUMMARY, summary);
                data.put(ShareConstants.TARGET_URL, "http://m.13322.com");
                data.put(ShareConstants.IMAGE_URL, shareHomeIconUrl != null ? shareHomeIconUrl : "");
                sharePopupWindow = new SharePopupWindow(this, mShare, data);
                sharePopupWindow.setmShareTencentCallBack(mShareTencentCallBack);
                sharePopupWindow.setmShareCopyLinkCallBack(mShareCopyLinkCallBack);
                sharePopupWindow.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });

                backgroundAlpha(0.5f);

                break;
            case R.id.network_exception_reload_btn:
                MobclickAgent.onEvent(mContext, "Football_MatchDataInfo_Refresh");
                mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
                loadData();
                break;
            default:
                break;
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }


    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {

        }

        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
        }
    };

    private void onClickShare(int flag) {
        mTencent = Tencent.createInstance(ShareConstants.QQ_APP_ID, this);
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);

        String title = mMatchDetail.getHomeTeamInfo().getName() + " VS " + mMatchDetail.getGuestTeamInfo().getName();
        String appname = getString(R.string.share_to_qq_app_name);
        String summary = getString(R.string.share_summary);

        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://m.13322.com");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareHomeIconUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appname);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, flag);
        mTencent.shareToQQ(FootballMatchDetailActivity.this, params, qqShareListener);
    }


    public class MatchDetailFragmentAdapter extends FragmentPagerAdapter {


        private List<Fragment> mFragmentList;

        public MatchDetailFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager);
            this.mFragmentList = fragmentList;
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }


    public void setClickableLikeBtn(boolean clickable) {
        mHomeLike.setClickable(clickable);
        mGuestLike.setClickable(clickable);
        if (clickable) {
            mHomeLike.setImageResource(R.mipmap.like_red);
            mGuestLike.setImageResource(R.mipmap.like_blue);
        } else {
            mHomeLike.setImageResource(R.mipmap.like_anim_left);
            mGuestLike.setImageResource(R.mipmap.like_anim_right);
        }
    }

    //赛中fragment推送完场所需要调用的方法
    public void finishMatch() {
        setClickableLikeBtn(false);
    }

    //直播fragment重新加载直播数据
    public void reLoadStadium() {
        loadData();
    }


    /**
     * 判断是否是赛前
     *
     * @return
     */
    private boolean isPreStadiumStatus(String status) {
        return ("0".equals(status) || "-10".equals(status) || "-11".equals(status) || "-14".equals(status));
    }

    @Override
    public void onRefresh() {  //下拉刷新

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                if (mMatchDetail != null && !"-1".equals(mMatchDetail.getLiveStatus())) {
                    loadData();
                    //执行刷新指数
                    mOddsFragment.oddPlateRefresh();
                }
            }
        }, 1000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mReloadTimer != null) {
            mReloadTimer.cancel();
        }
//        Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, mScreenOffTimeoutTemp);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            eventBusPost();
            //setResult(Activity.RESULT_OK);
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

    /**
     * 不要删除以下代码
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        outState.putParcelable("");
        //super.onSaveInstanceState(outState);
    }
}
