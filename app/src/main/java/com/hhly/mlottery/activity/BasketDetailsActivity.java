package com.hhly.mlottery.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.BasketballDetailsBean;
import com.hhly.mlottery.bean.websocket.WebSocketBasketBallDetails;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.basketballframe.BasketAnalyzeFragment;
import com.hhly.mlottery.frame.basketballframe.BasketDetailsBaseFragment;
import com.hhly.mlottery.frame.basketballframe.BasketOddsFragment;
import com.hhly.mlottery.frame.basketballframe.MyRotateAnimation;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.websocket.HappySocketClient;
import com.hhly.mlottery.view.CacheFragmentStatePagerAdapter;
import com.hhly.mlottery.view.ScrollUtils;
import com.hhly.mlottery.view.Scrollable;
import com.hhly.mlottery.view.SlidingTabLayout;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

import org.java_websocket.drafts.Draft_17;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Description: 篮球详情的 Activity
 * @author yixq
 * Created by A on 2016/3/21.
 */
public class BasketDetailsActivity extends BasketBaseActivity implements View.OnClickListener, HappySocketClient.SocketResponseErrorListener, HappySocketClient.SocketResponseCloseListener, HappySocketClient.SocketResponseMessageListener {
    public final static String BASKET_FOCUS_IDS = "basket_focus_ids";
    public final static String BASKET_THIRD_ID = "thirdId";
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
    private HappySocketClient mSocketClient;//客户端  socket;
    private URI mSocketUri = null;

    private String TAG = BasketDetailsActivity.class.getName();

    private ViewPager mPager;
    private NavigationAdapter mPagerAdapter;
    private SlidingTabLayout mSlidingTabLayout;
    private int mFlexibleSpaceHeight;
    private int mTabHeight;
    private int mHeight;//整个头部 高度240减去title栏（header）的44再减去paddingtop的24=172

    private DisplayImageOptions mOptions;
    private DisplayImageOptions mOptionsHead;
    private ImageLoader mImageLoader;
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

    private TextView mLeagueName;
    private TextView mHomeTeam;
    private TextView mGuestTeam;
    private TextView mHomeRanking;
    private TextView mGuestRanking;
    private TextView mHomeScore;
    private TextView mGuestScore;
    private TextView mVS;//比分的冒号
    private TextView mMatchState;
    private TextView mRemainTime;

    private TextView mHome1;
    private TextView mHome2;
    private TextView mHome3;
    private TextView mHome4;
    private TextView mHomeOt1;
    private TextView mHomeOt2;
    private TextView mHomeOt3;
    private TextView mSmallHomeScore;//每节比分的后面的总分
    private TextView mSmallGuestScore;
    private TextView mGuest1;
    private TextView mGuest2;
    private TextView mGuest3;
    private TextView mGuest4;
    private TextView mGuestOt1;
    private TextView mGuestOt2;
    private TextView mGuestOt3;

    private ImageView mHomeIcon;
    private ImageView mGuestIcon;
    private ImageView mHeadImage;

    private TextView mTitleHome;//主队比分/队名
    private TextView mTitleGuest;//客队比分/队名
    private TextView mTitleVS;//冒号  VS

    LinearLayout headLayout;// 小头部
    //显示加时比分的三个布局
    private LinearLayout mLayoutOt1;
    private LinearLayout mLayoutOt2;
    private LinearLayout mLayoutOt3;
    BasketballDetailsBean.MatchEntity mMatch;
    private TextView mApos;
    private int mGuestNum=0;
    private int mHomeNum=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_detailsactivity_activity);
        if (getIntent().getExtras() != null) {
            mThirdId = getIntent().getExtras().getString(BASKET_THIRD_ID);
        }
        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
              //  .showImageOnLoading(R.mipmap.basket_default)//加上这句的话会导致刷新时闪烁
                .showImageForEmptyUri(R.mipmap.basket_default)
                .showImageOnFail(R.mipmap.basket_default)// 加载失败显示的图片
                .build();
        mOptionsHead = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                .showImageOnLoading(R.color.black)
                .showImageForEmptyUri(R.color.black)
                .showImageOnFail(R.color.black)// 加载失败显示的图片
                .displayer(new FadeInBitmapDisplayer(2000))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        mImageLoader = ImageLoader.getInstance(); //初始化
        mImageLoader.init(config);


        try {
            mSocketUri = new URI(BaseURLs.WS_SERVICE);//地址
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            initView();
            setListener();
            loadData();
        } catch (Exception e) {
            L.e(e.getMessage());
        }
    }

    /**
     * 开启socket
     */
    private synchronized void startWebsocket() {
        if (mSocketClient != null) {
            if (!mSocketClient.isClosed()) {
                mSocketClient.close();
            }

            mSocketClient = new HappySocketClient(mSocketUri, new Draft_17());
            mSocketClient.setSocketResponseMessageListener(this);
            mSocketClient.setSocketResponseCloseListener(this);
            mSocketClient.setSocketResponseErrorListener(this);
            try {
                mSocketClient.connect();
            } catch (IllegalThreadStateException e) {
                mSocketClient.close();
            }
        } else {
            mSocketClient = new HappySocketClient(mSocketUri, new Draft_17());
            mSocketClient.setSocketResponseMessageListener(this);
            mSocketClient.setSocketResponseCloseListener(this);
            mSocketClient.setSocketResponseErrorListener(this);
            try {
                mSocketClient.connect();
            } catch (IllegalThreadStateException e) {
                mSocketClient.close();
            }
        }


    }

    private long pushStartTime;

    private Timer computeWebSocketConnTimer = new Timer();

    /**
     * 检测socket连接状态。
     */
    private void computeWebSocket() {
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                L.i(TAG, df.format(new Date()) + "---监听socket连接状态:Open=" + mSocketClient.isOpen() + ",Connecting=" + mSocketClient.isConnecting() + ",Close=" + mSocketClient.isClosed() + ",Closing=" + mSocketClient.isClosing());
                long pushEndTime = System.currentTimeMillis();
                if ((pushEndTime - pushStartTime) >= 30000) {
                    L.i(TAG, "重新启动socket");
                    if(mSocketClient.isClosed()){
                        startWebsocket();
                    }

                }
            }
        };
        try {
            computeWebSocketConnTimer.schedule(tt, 15000, 15000);
        }catch (Exception e){

        }

    }


    public String getmThirdId() {
        return mThirdId;
    }

    /**
     * 分析、欧赔、亚盘、大小Fragment页面统计
     */
    private boolean isFragment0 = true;
    private boolean is0 = false;
    private boolean isFragment1 = false;
    private boolean is1 = false;
    private boolean isFragment2 = false;
    private boolean is2 = false;
    private boolean isFragment3 = false;
    private boolean is3 = false;

    /**
     * 初始化界面
     */
    private void initView() {
        mPagerAdapter = new NavigationAdapter(getSupportFragmentManager(), mThirdId);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPadding(0, getResources().getDimensionPixelSize(R.dimen.tab_height), 0, 0);//设置pager的上边距（pager下移一个tab的高度）
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(3);//不要改动。必须全加载出来。
        mFlexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mTabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        mHeight = getResources().getDimensionPixelSize(R.dimen.mheight);
//        Log.e("AAA",mFlexibleSpaceHeight+"");
//        Log.e("AA",mTabHeight+"");


        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.tabtitle));//
        mSlidingTabLayout.setCustomTabView(R.layout.basket_tab_indicator, android.R.id.text1); //自定义样式
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.basket_text_color));//设置下划线颜色
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mPager);
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:// 分析
                        isFragment0 = true;
                        isFragment1 = false;
                        isFragment2 = false;
                        isFragment3 = false;
                        break;
                    case 1:// 欧赔
                        isFragment0 = false;
                        isFragment1 = true;
                        isFragment2 = false;
                        isFragment3 = false;
                        break;
                    case 2:// 亚盘
                        isFragment0 = false;
                        isFragment1 = false;
                        isFragment2 = true;
                        isFragment3 = false;
                        break;
                    case 3:// 大小
                        isFragment0 = false;
                        isFragment1 = false;
                        isFragment2 = false;
                        isFragment3 = true;
                        break;
                }
                if(is0){
                    MobclickAgent.onPageEnd("BasketBall_Info_FX");
                    is0 = false;
                    L.d("xxx","分析隐藏");
                }
                if(is1){
                    MobclickAgent.onPageEnd("BasketBall_Info_OP");
                    is1 = false;
                    L.d("xxx","欧赔隐藏");
                }
                if(is2){
                    MobclickAgent.onPageEnd("BasketBall_Info_YP");
                    is2 = false;
                    L.d("xxx","亚盘隐藏");
                }
                if(is3){
                    MobclickAgent.onPageEnd("BasketBall_Info_DX");
                    is3 = false;
                    L.d("xxx","大小隐藏");
                }

                if (isFragment0) {
                    MobclickAgent.onPageStart("BasketBall_Info_FX");
                    is0 = true;
                    L.d("xxx","分析显示");
                }
                if (isFragment1) {
                    MobclickAgent.onPageStart("BasketBall_Info_OP");
                    is1 = true;
                    L.d("xxx","欧赔显示");
                }
                if (isFragment2) {
                    MobclickAgent.onPageStart("BasketBall_Info_YP");
                    is2 = true;
                    L.d("xxx","亚盘显示");
                }
                if (isFragment3) {
                    MobclickAgent.onPageStart("BasketBall_Info_DX");
                    is3 = true;
                    L.d("xxx","大小显示");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Initialize the first Fragment's state when layout is completed.
        ScrollUtils.addOnGlobalLayoutListener(mSlidingTabLayout, new Runnable() {
            @Override
            public void run() {
                translateTab(0, false);
            }
        });


        mLeagueName = (TextView) this.findViewById(R.id.basket_details_matches_name);
        mHomeTeam = (TextView) this.findViewById(R.id.basket_details_home_name);
        mGuestTeam = (TextView) this.findViewById(R.id.basket_details_guest_name);
        mHomeRanking = (TextView) this.findViewById(R.id.basket_details_home_Ranking);
        mGuestRanking = (TextView) this.findViewById(R.id.basket_details_guest_Ranking);
        mHomeScore = (TextView) this.findViewById(R.id.basket_details_home_all_score);
        mGuestScore = (TextView) this.findViewById(R.id.basket_details_guest_all_score);
        mVS = (TextView) this.findViewById(R.id.basket_score_maohao);
        mMatchState = (TextView) this.findViewById(R.id.basket_details_state);
        mRemainTime= (TextView) this.findViewById(R.id.basket_details_remain_time);
        mHome1 = (TextView) this.findViewById(R.id.basket_details_home_first);
        mHome2 = (TextView) this.findViewById(R.id.basket_details_home_second);
        mHome3 = (TextView) this.findViewById(R.id.basket_details_home_third);
        mHome4 = (TextView) this.findViewById(R.id.basket_details_home_fourth);
        mHomeOt1 = (TextView) this.findViewById(R.id.basket_details_home_ot1);
        mHomeOt2 = (TextView) this.findViewById(R.id.basket_details_home_ot2);
        mHomeOt3 = (TextView) this.findViewById(R.id.basket_details_home_ot3);
        mSmallHomeScore = (TextView) this.findViewById(R.id.basket_details_home_small_total);

        mGuest1 = (TextView) this.findViewById(R.id.basket_details_guest_first);
        mGuest2 = (TextView) this.findViewById(R.id.basket_details_guest_second);
        mGuest3 = (TextView) this.findViewById(R.id.basket_details_guest_third);
        mGuest4 = (TextView) this.findViewById(R.id.basket_details_guest_fourth);
        mGuestOt1 = (TextView) this.findViewById(R.id.basket_details_guest_ot1);
        mGuestOt2 = (TextView) this.findViewById(R.id.basket_details_guest_ot2);
        mGuestOt3 = (TextView) this.findViewById(R.id.basket_details_guest_ot3);
        mSmallGuestScore = (TextView) this.findViewById(R.id.basket_details_guest_small_total);

        mHomeIcon = (ImageView) this.findViewById(R.id.basket_details_home_icon);
        mGuestIcon = (ImageView) this.findViewById(R.id.basket_details_guest_icon);

        mTitleHome = (TextView) this.findViewById(R.id.title_home_score);
        mTitleGuest = (TextView) this.findViewById(R.id.title_guest_score);
        mTitleVS = (TextView) this.findViewById(R.id.title_vs);

        mHeadImage = (ImageView) this.findViewById(R.id.image_background);
        // mHeadBlack= (LinearLayout) this.findViewById(R.id.backet_head_black);

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

        mApos = (TextView) this.findViewById(R.id.backetball_details_apos);
        mApos.setVisibility(View.GONE);




//        setApos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFragment0) {
            MobclickAgent.onPageStart("BasketBall_Info_FX");
            is0 = true;
            L.d("xxx","分析显示");
        }
        if (isFragment1) {
            MobclickAgent.onPageStart("BasketBall_Info_OP");
            is1 = true;
            L.d("xxx","欧赔显示");
        }
        if (isFragment2) {
            MobclickAgent.onPageStart("BasketBall_Info_YP");
            is2 = true;
            L.d("xxx","亚盘显示");
        }
        if (isFragment3) {
            MobclickAgent.onPageStart("BasketBall_Info_DX");
            is3 = true;
            L.d("xxx","大小显示");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (is0) {
            MobclickAgent.onPageEnd("BasketBall_Info_FX");
            is0 = false;
            L.d("xxx","分析 隐藏");
        }
        if (is1) {
            MobclickAgent.onPageEnd("BasketBall_Info_OP");
            is1 = false;
            L.d("xxx","欧赔 隐藏");
        }
        if (is2) {
            MobclickAgent.onPageEnd("BasketBall_Info_YP");
            is2 = false;
            L.d("xxx","亚盘 隐藏");
        }
        if (is3) {
            MobclickAgent.onPageEnd("BasketBall_Info_DX");
            is3 = false;
            L.d("xxx","大小 隐藏");
        }
    }

    @Override
    protected void onDestroy() { //关闭socket
        super.onDestroy();
        if (mSocketClient != null) {
            if (!mSocketClient.isClosed()) {
                mSocketClient.close();
            }
        }
        computeWebSocketConnTimer.cancel();
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
        Map<String, String> params = new HashMap<>();
        params.put("thirdId", mThirdId);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_BASKET_DETAILS, params, new VolleyContentFast.ResponseSuccessListener<BasketballDetailsBean>() {
            @Override
            public void onResponse(BasketballDetailsBean basketDetailsBean) {
                if (basketDetailsBean.getMatch() != null) {
                    initData(basketDetailsBean);
                    /**
                     * 启动秒闪烁
                     */
                    setApos();
                    if (basketDetailsBean.getMatch().getMatchStatus() != END) {
                        startWebsocket();
                        computeWebSocket();
                    }
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mApos.setVisibility(View.GONE);
            }
        }, BasketballDetailsBean.class);
    }
    /**
     * 判断当前是哪个fragment
     * 设置可以滑动的幅度
     *
     * @param scrollY
     * @param s
     */
    public void onScrollChanged(int scrollY, Scrollable s) {
        BasketDetailsBaseFragment fragment = (BasketDetailsBaseFragment) mPagerAdapter.getItemAt(mPager.getCurrentItem());
        if (fragment == null) {
            return;
        }
        View view = fragment.getView();
        if (view == null) {
            return;
        }
        Scrollable scrollable;

        scrollable = (Scrollable) view.findViewById(R.id.scroll);

        if (scrollable == null) {
            return;
        }
        if (scrollable == s) {
            int adjustedScrollY = Math.min(scrollY, mHeight);//可收缩的最大高度
            translateTab(adjustedScrollY, false);
            propagateScroll(adjustedScrollY);
        }
    }
    /**
     * 滑动tab栏
     *
     * @param scrollY
     * @param animated
     */
    private void translateTab(int scrollY, boolean animated) {
        int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);

        View imageView = findViewById(R.id.image_background);
        View overlayView = findViewById(R.id.overlay);
        headLayout = (LinearLayout) findViewById(R.id.basket_details_header_layout);
        RelativeLayout mRl = (RelativeLayout) findViewById(R.id.basket_title_rl);

        // Translate overlay and image
        float flexibleRange = flexibleSpaceImageHeight - getActionBarSize();
        int minOverlayTransitionY = tabHeight - overlayView.getHeight();
        ViewHelper.setTranslationY(overlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(imageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mRl, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(overlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
        ViewHelper.setAlpha(mRl, ScrollUtils.getFloat((float) scrollY / flexibleRange, 1, 1));

        ViewPropertyAnimator.animate(mSlidingTabLayout).cancel();
        // Tabs will move between the top of the screen to the bottom of the image.
        float translationY = ScrollUtils.getFloat(-scrollY + mFlexibleSpaceHeight, 0, mFlexibleSpaceHeight); //设置tab 的位置 translationY是头两个参数中的大的再给第三个参数比。取小的

        int position[] = new int[2];
        mSlidingTabLayout.getLocationInWindow(position);
        int paddingtop = getResources().getDimensionPixelOffset(R.dimen.paddingtop);
        if (position[1] == paddingtop) {//tablayout到达顶部时显示黑色背景
            mTitleScore.setVisibility(View.VISIBLE);
            headLayout.setBackgroundColor(getResources().getColor(R.color.black));
        } else {
            mTitleScore.setVisibility(View.INVISIBLE);
            headLayout.setBackgroundColor(getResources().getColor(R.color.transparency));
        }
        if (animated) {
            // Animation will be invoked only when the current tab is changed.
            ViewPropertyAnimator.animate(mSlidingTabLayout)
                    .translationY(translationY)
                    .setDuration(200)
                    .start();
        } else {
            // When Fragments' scroll, translate tabs immediately (without animation).
            ViewHelper.setTranslationY(mSlidingTabLayout, translationY);
        }
    }

    /**
     * 设置每个viewpager里面的对应的listview或者scrollview移动后的位置
     *
     * @param scrollY 移动的距离
     */
    private void propagateScroll(int scrollY) {
        // Set scrollY for the fragments that are not created yet
        mPagerAdapter.setScrollY(scrollY);

        // Set scrollY for the active fragments
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            // Skip current item
            if (i == mPager.getCurrentItem()) {
                continue;
            }

            // Skip destroyed or not created item
            BasketDetailsBaseFragment f =
                    (BasketDetailsBaseFragment) mPagerAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }

            View view = f.getView();
            if (view == null) {
                continue;
            }
            f.setScrollY(scrollY, mFlexibleSpaceHeight);//具体都fragmen里面移动
            f.updateFlexibleSpace(scrollY);
        }
    }

    /**
     * 刷新这4个fragment界面的数据（initdata）
     */
    public void refreshData() {
        loadData();
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            // Skip current item
            if (i == mPager.getCurrentItem()) { //当前界面已经在自己的Fragment中调用了刷新方法。所以
                continue;
            }

            // Skip destroyed or not created item
            BasketDetailsBaseFragment f =
                    (BasketDetailsBaseFragment) mPagerAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }

            View view = f.getView();
            if (view == null) {
                continue;
            }
            f.initData();
        }
    }

    /**
     * 本来是可以跟另外三个一起刷新的。但是Observable ListView会展开头部，Observable ScrollView 不会
     * 在修复大小头部的bug时，无法同样处理。所以。方法分开了。
     */
    public void analyzeRefreshData() {
        loadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.basket_details_back:
                MobclickAgent.onEvent(MyApp.getContext(), "BasketDetailsActivity_Exit");
                setResult(Activity.RESULT_OK);
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.basket_details_collect:
                MobclickAgent.onEvent(MyApp.getContext(), "BasketDetailsActivity_Attention");
                if (isFocusId(mThirdId)) {
                    deleteFocusId(mThirdId);
                    mCollect.setImageResource(R.mipmap.basketball_collect);
                } else {
                    addFocusId(mThirdId);
                    mCollect.setImageResource(R.mipmap.basketball_collected);
                }

                mHomeScore.setText("70");
                mGuestScore.setText("80");
                mVS.setText(":");
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResult(Activity.RESULT_OK);
            finish();
            overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 请求数据之后展示
     */


    private void initData(BasketballDetailsBean bean) {


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
        if(mMatch.getHomeRanking().equals("")){
            mHomeRanking.setText("");
        }else{
            mHomeRanking.setText("[ "+mMatch.getHomeRanking()+" ]");
        }
        if(mMatch.getGuestRanking().equals("")){
            mGuestRanking.setText("");
        }else {
            mGuestRanking.setText("[ "+mMatch.getGuestRanking()+" ]");
        }

        //图标
        mImageLoader.displayImage(mMatch.getHomeLogoUrl(), mHomeIcon, mOptions);

        mImageLoader.displayImage(mMatch.getGuestLogoUrl(), mGuestIcon, mOptions);
        mImageLoader.displayImage(bean.getBgUrl(), mHeadImage, mOptionsHead);


        if (mMatch.getSection() == 2) { //只有上下半场
            mGuest2.setVisibility(View.INVISIBLE);
            mGuest4.setVisibility(View.INVISIBLE);
            mHome2.setVisibility(View.INVISIBLE);
            mHome4.setVisibility(View.INVISIBLE);
        }

        switch (mMatch.getMatchStatus()) {
            case PRE_MATCH: ///赛前
            case DETERMINED://待定
            case GAME_CANCLE: //比赛取消
            case GAME_CUT: //比赛中断
            case GAME_DELAY: //比赛推迟
                //赛前显示 客队 VS  主队
                mGuestScore.setText("");
                mHomeScore.setText("");
//                mGuestScore.setVisibility(View.GONE);
//                mHomeScore.setVisibility(View.GONE);

                mVS.setText("VS");
                mTitleGuest.setText(bean.getMatch().getGuestTeam());
                mTitleHome.setText(bean.getMatch().getHomeTeam());
                mTitleVS.setText("VS");
                if (mMatch.getMatchStatus() == PRE_MATCH) {
                    mMatchState.setText(bean.getMatch().getDate() + "  " + bean.getMatch().getTime() + "   " + getResources().getString(R.string.basket_begin_game));
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
                break;
            case END://完场
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

                mTitleHome.setText(score.getHomeScore() + "");
                mTitleGuest.setText(score.getGuestScore() + "");
                mSmallGuestScore.setText(score.getGuestScore() + "");
                mSmallHomeScore.setText(score.getHomeScore() + "");
                mVS.setText(":");
                mTitleVS.setText(":");
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
                mTitleHome.setText(score.getHomeScore() + "");
                mTitleGuest.setText(score.getGuestScore() + "");
                mVS.setText(":");
                mTitleVS.setText(":");


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
                if(mMatch.getMatchStatus()==HALF_GAME){
                    mRemainTime.setText("");//中场时无剩余时间。。后台可能中场也给时间。没办法
                }
                if(score.getRemainTime()==null||score.getRemainTime().equals("")){//没有剩余时间的时候
                    mApos.setVisibility(View.GONE);
                }
                break;
        }

    }


    /**
     * 判断thirdId是否已经关注
     *
     * @param thirdId
     * @return true已关注，false还没关注
     */
    private boolean isFocusId(String thirdId) {
        String focusIds = PreferenceUtil.getString(BASKET_FOCUS_IDS, "");

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

    /**
     * 添加关注
     *
     * @param thirdId
     */
    private void addFocusId(String thirdId) {
        String focusIds = PreferenceUtil.getString(BASKET_FOCUS_IDS, "");
        if ("".equals(focusIds)) {
            PreferenceUtil.commitString(BASKET_FOCUS_IDS, thirdId);
        } else {
            PreferenceUtil.commitString(BASKET_FOCUS_IDS, focusIds + "," + thirdId);
        }
    }

    /**
     * 取消关注
     *
     * @param thirdId
     */
    private void deleteFocusId(String thirdId) {
        String focusIds = PreferenceUtil.getString(BASKET_FOCUS_IDS, "");
        String[] idArray = focusIds.split(",");
        StringBuffer sb = new StringBuffer();
        for (String id : idArray) {
            if (!id.equals(thirdId)) {
                if ("".equals(sb.toString())) {
                    sb.append(id);
                } else {
                    sb.append("," + id);
                }

            }
        }
        PreferenceUtil.commitString(BASKET_FOCUS_IDS, sb.toString());
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
            guest.setTextColor(getResources().getColor(R.color.basket_score_white));
            home.setTextColor(getResources().getColor(R.color.basket_score_gray));
        } else if (guestScore < homeScore) {
            guest.setTextColor(getResources().getColor(R.color.basket_score_gray));
            home.setTextColor(getResources().getColor(R.color.basket_score_white));
        } else {
            guest.setTextColor(getResources().getColor(R.color.basket_score_white));
            home.setTextColor(getResources().getColor(R.color.basket_score_white));
        }


    }



    @Override
    public void onClose(String message) {

    }

    @Override
    public void onError(Exception exception) {

    }

    @Override
    public void onMessage(String message) {
        L.w(TAG, "message = " + message);

        pushStartTime = System.currentTimeMillis(); // 记录起始时间

        if (message.startsWith("CONNECTED")) {
            String id = "android" + DeviceInfo.getDeviceId(this);
            id = MD5Util.getMD5(id);
            mSocketClient.send("SUBSCRIBE\nid:" + id + "\ndestination:/topic/USER.topic.basketball.score." + mThirdId + "\n\n");
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
                L.e(TAG, type + "____________________");

                mSocketHandler.sendMessage(msg);
            }
        }
        mSocketClient.send("\n");//你来我往的心跳。。。
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
                    // Log.e(TAG, "ws_json = " + ws_json);
                    mBasketDetails = JSON.parseObject(ws_json, WebSocketBasketBallDetails.class);
                }
                //  L.e();
                updateData(mBasketDetails);
            }
        }
    };


    /**
     * 接受推送，更新数据
     *
     * @param basketBallDetails 推送过来消息封装的实体类
     */
    private void updateData(WebSocketBasketBallDetails basketBallDetails) {
        WebSocketBasketBallDetails.DataEntity score = basketBallDetails.getData();

        switch (basketBallDetails.getData().getMatchStatus()) {
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
                break;

            case END://完场
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

                mTitleHome.setText(score.getHomeScore() + "");
                mTitleGuest.setText(score.getGuestScore() + "");
                mSmallGuestScore.setText(score.getGuestScore() + "");
                mSmallGuestScore.setTextColor(getResources().getColor(R.color.score_color_white));
                mSmallHomeScore.setText(score.getHomeScore() + "");
                mSmallHomeScore.setTextColor(getResources().getColor(R.color.score_color_white));
                mVS.setText(":");
                mTitleVS.setText(":");
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
                mTitleVS.setText(":");

                if(mGuestNum!=score.getGuestScore()){
                    scoreAnimation(mGuestScore);
                    mGuestNum=score.getGuestScore();
                }
                if(mHomeNum!=score.getHomeScore()){
                    scoreAnimation(mHomeScore);
                    mHomeNum=score.getHomeScore();
                }

                setScore(score.getGuestScore(), mGuestScore, score.getHomeScore(), mHomeScore);// 动画有毒，最后在设一下比分

                L.d("score.getHomeScore()>>>>...>>>" + score.getHomeScore());
                setScore(score.getGuestScore(), mSmallGuestScore, score.getHomeScore(), mSmallHomeScore);

                mTitleHome.setText(score.getHomeScore() + "");
                mTitleGuest.setText(score.getGuestScore() + "");


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
                mRemainTime.setText(score.getRemainTime()==null?"":score.getRemainTime());//为空的话就设置为空字符

                if(score.getMatchStatus()==HALF_GAME){
                    mRemainTime.setText("");//中场时无剩余时间。。后台可能中场也给时间。没办法
                }

                if(score.getRemainTime()==null||score.getRemainTime().equals("")){
                    mApos.setVisibility(View.GONE);
                }
                break;
        }
    }


    /**
     * 设置比分变化时的的翻转动画
     */
    public void scoreAnimation(final TextView changeText) {
        float cX = changeText.getWidth() / 2.0f;
        float cY = changeText.getHeight() / 2.0f;

        MyRotateAnimation rotateAnim = new MyRotateAnimation(cX, cY, MyRotateAnimation.ROTATE_DECREASE);

        rotateAnim.setFillAfter(true);

        changeText.startAnimation(rotateAnim);

    }


    /**
     * This adapter provides three types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */
    private class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        //        private static final String[] TITLES = new String[]{"Applepie", "Butter Cookie", "Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb", "Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop"};
        private String[] TITLES = new String[]{BasketDetailsActivity.this.getResources().getString(R.string.basket_analyze), BasketDetailsActivity.this.getResources().getString(R.string.basket_eur),
                BasketDetailsActivity.this.getResources().getString(R.string.basket_alet), BasketDetailsActivity.this.getResources().getString(R.string.basket_analyze_sizeof)};

        private int mScrollY;
        private String mThirdId;

        public NavigationAdapter(FragmentManager fm, String mThirdId) {
            super(fm);
            this.mThirdId = mThirdId;
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        @Override
        protected Fragment createItem(int position) {
            BasketDetailsBaseFragment f;
            switch (position) {
                case 0: {
                    f = new BasketAnalyzeFragment();
                    break;
                }
                case 1: {
                    f = BasketOddsFragment.newInstance(mThirdId, ODDS_EURO);

                    break;
                }
                case 2: {
                    f = BasketOddsFragment.newInstance(mThirdId, ODDS_LET);
                    break;
                }
                case 3:
                default: {
                    f = BasketOddsFragment.newInstance(mThirdId, ODDS_SIZE);
                    break;
                }
            }
            //   f.setArguments(mScrollY);
            return f;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }

}
