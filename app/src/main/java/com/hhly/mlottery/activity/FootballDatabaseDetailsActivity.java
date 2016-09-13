package com.hhly.mlottery.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.SportsDialogAdapter;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.bean.basket.basketdatabase.FootballDatabaseHeaderBean;
import com.hhly.mlottery.bean.footballDetails.database.DataBaseBean;
import com.hhly.mlottery.frame.footframe.FootballDatabaseIntegralFragment;
import com.hhly.mlottery.frame.footframe.FootballDatabaseScheduleFragment;
import com.hhly.mlottery.util.MDStatusBarCompat;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;
import com.hhly.mlottery.widget.ScrollTouchListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:
 * author: yixq
 * Created by A on 2016/9/2.
 * 足球资料库详情
 */

public class FootballDatabaseDetailsActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    public static final String LEAGUE = "league";

    private DataBaseBean mLeague;
    private ImageView mIcon;
    private Toolbar toolbar;
    private ImageView mBackground;
    private TextView mLeagueName;
    private TextView mSportsText;
    private LinearLayout mBasketLayoutHeader;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private ViewPager mViewPager;
    private AppBarLayout appBarLayout;
    private TabLayout mTabLayout;
    private TabsAdapter tabsAdapter;
    private LinearLayout headLayout;
    private ExactSwipeRefrashLayout mRefreshLayout;
    private ImageView mBack;
    private LinearLayout mCollect;

    private DisplayImageOptions mOptions;
    private DisplayImageOptions mOptionsHead;
    private ImageLoader mImageLoader;

    private String[] mSports; // 赛季集
    private boolean isLoad = false;//赛季是否可筛选
    private String mCurrentSports = "";
    Handler mHandlerData = new Handler();
    private FootballDatabaseIntegralFragment mIntegralFragment;
    private FootballDatabaseScheduleFragment mScheduleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_database_details);

        if (getIntent().getExtras() != null) {

            mLeague = getIntent().getExtras().getParcelable(LEAGUE);

        }
        /**
         * 第一次加载默认赛季数据，不需要season ==》（-1）
         */
        mScheduleFragment = FootballDatabaseScheduleFragment.newInstance(mLeague , null);//赛程
        mIntegralFragment = FootballDatabaseIntegralFragment.newInstance(mLeague , null); //积分

        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                .showImageForEmptyUri(R.mipmap.basket_default)
                .showImageOnFail(R.mipmap.basket_default)// 加载失败显示的图片
                .build();

        mOptionsHead = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                .showImageOnLoading(R.color.colorPrimary)
                .showImageForEmptyUri(R.color.colorPrimary)
                .showImageOnFail(R.color.colorPrimary)// 加载失败显示的图片
                .displayer(new FadeInBitmapDisplayer(2000))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        mImageLoader = ImageLoader.getInstance(); //初始化
        mImageLoader.init(config);

        initView();
        mHandlerData.postDelayed(mRun , 500);
        setListener();

    }

    private void initView(){
        String[] titles = new String[]{
                getString(R.string.basket_database_details_schedule),
                getString(R.string.football_database_details_integral),
        };

        toolbar = (Toolbar)findViewById(R.id.football_database_details_toolbar);
        setSupportActionBar(toolbar);

        mIcon = (ImageView) findViewById(R.id.football_details_guest_icon);
        mBackground = (ImageView) findViewById(R.id.image_background);
        mLeagueName = (TextView) findViewById(R.id.football_database_leaguename);
        mSportsText = (TextView) findViewById(R.id.football_database_datails_tv);

        mBasketLayoutHeader = (LinearLayout) findViewById(R.id.football_database_details_img);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mViewPager = (ViewPager) findViewById(R.id.football_details_view_pager);
        appBarLayout = (AppBarLayout) findViewById(R.id.football_database_details_appbar);
        mTabLayout = (TabLayout) findViewById(R.id.football_database_details_tab_layout);

        tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        tabsAdapter.setTitles(titles);
        MDStatusBarCompat.setCollapsingToolbar(this, mCoordinatorLayout, appBarLayout, mBasketLayoutHeader, toolbar);
//        tabsAdapter.addFragments(mHandicapFragment,mBigSmallFragment);
        tabsAdapter.addFragments(mScheduleFragment,mIntegralFragment);

        mViewPager.setOffscreenPageLimit(2);//设置预加载页面的个数。
        mViewPager.setAdapter(tabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        appBarLayout.addOnOffsetChangedListener(this);

        headLayout = (LinearLayout) findViewById(R.id.football_database_details_header_layout);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                isHindShow(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRefreshLayout = (ExactSwipeRefrashLayout) findViewById(R.id.football_database_details_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);
        mBack = (ImageView) this.findViewById(R.id.football_database_details_back);
        mCollect = (LinearLayout) this.findViewById(R.id.football_database_details_collect);

    }

    private void initData(){

        // http://192.168.31.43:8888/mlottery/core/basketballData.findLeagueHeader.do?lang=zh&leagueId=2
        String url = "http://192.168.31.8:8080/mlottery/core/androidLeagueData.findAndroidFootballLeagueHeader.do";
//        String url = BaseURLs.URL_BASKET_DATABASE_DETAILS;
        Map<String, String> params = new HashMap<>();
        params.put("leagueId", mLeague.getLeagueId());
        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<FootballDatabaseHeaderBean>() {

            @Override
            public void onResponse(FootballDatabaseHeaderBean basketDatabaseBean) {
                if (basketDatabaseBean != null) {
                    mSports = basketDatabaseBean.getSeason();

                    /**
                     * 判断赛季集合mSports 中有数据时 ，才设为可筛选
                     */
                    if (mSports != null && mSports.length != 0) {
                        isLoad = true;
                    }

                    if (mCurrentSports.equals("")) {
                        //若赛季无数据时 设为"--" 防止异常崩溃
                        if (mSports == null || mSports.length == 0) {
                            mSportsText.setText("--");
                        } else {
                            mSportsText.setText(mSports[0] + getResources().getString(R.string.basket_database_details_season));
                        }
                    } else {
                        mSportsText.setText(mCurrentSports + getResources().getString(R.string.basket_database_details_season));
                    }

                    if (basketDatabaseBean.getLeagueName() == null || basketDatabaseBean.getLeagueName().equals("")) {
                        mLeagueName.setText("--");
                    } else {
                        mLeagueName.setText(basketDatabaseBean.getLeagueName());
                    }
                    //图标
                    mImageLoader.displayImage(basketDatabaseBean.getLeagueLogo(), mIcon, mOptions);
                    mImageLoader.displayImage(basketDatabaseBean.getRandomBg(), mBackground, mOptionsHead);

                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                isLoad = false; //不可筛选
            }
        }, FootballDatabaseHeaderBean.class);

    }

    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            initData();
        }
    };

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
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mCollapsingToolbarLayout.getHeight() + verticalOffset < mBasketLayoutHeader.getHeight()) {
            mRefreshLayout.setEnabled(false);
        } else {
            mRefreshLayout.setEnabled(true);
        }
        if ((-verticalOffset) == appBarLayout.getTotalScrollRange()) {
            headLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        } else {
            headLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.transparency));
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);

                mHandlerData.postDelayed(mRun, 500); // 加载数据

                mIntegralFragment.update();
                mScheduleFragment.update();
            }
        }, 1000);

    }
    /**
     * Fragment页面统计
     */
    private boolean isFragment0 = false;// 赛程
    private boolean is0 = false;
    private boolean isFragment1 = false;// 积分
    private boolean is1 = false;

    private void isHindShow(int position) {
        switch (position) {
            case 0:// 赛程
                isFragment0 = true;
                isFragment1 = false;
                break;
            case 1:// 积分
                isFragment0 = false;
                isFragment1 = true;
                break;
        }
        if (isFragment0) {
            if (is1) {
                is1 = false;
            }
            is0 = true;
        }
        if (isFragment1) {
            if (is0) {
                is0 = false;
            }
            is1 = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (isFragment0) {
            is0 = true;
        }
        if (isFragment1) {
            is1 = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (is0) {
            is0 = false;
        }
        if (is1) {
            is1 = false;
        }
    }


    private void setListener(){
        mBack.setOnClickListener(this);
        mCollect.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.football_database_details_back:
                setResult(Activity.RESULT_OK);
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.football_database_details_collect:
                if (isLoad) {
                    setDialog();
                }
                break;
        }
    }
    int currentDialogPosition = 0; // 当前选中的赛季（默认第一个）
    int currentPosition = 0; // 赛季选择过程中记录（点击确定后才赋值 ， 解决点击筛选不确定后再次进入赛季选择显示不一的情况）

    public void setDialog() {
        // Dialog 设置
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this, R.style.AlertDialog);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.sports_alertdialog, null);
        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        Button dataOk = (Button) view.findViewById(R.id.sports_btn_ok);
        titleView.setText(getResources().getString(R.string.basket_database_details_season));

        final List<String> data = new ArrayList<>();
        Collections.addAll(data, mSports);

        final SportsDialogAdapter mAdapter = new SportsDialogAdapter(data, this, currentDialogPosition);

        final AlertDialog mAlertDialog = mBuilder.create();
        mAlertDialog.setCanceledOnTouchOutside(true);//设置空白处点击 dialog消失

        /**
         * 根据List数据条数加载不同的ListView （数据多加载可滑动 ScrollTouchListview）
         */
        ScrollView scrollview = (ScrollView) view.findViewById(R.id.basket_sports_scroll);//数据多时显示
        ScrollTouchListView scrollListview = (ScrollTouchListView) view.findViewById(R.id.sport_date_scroll);
        ListView listview = (ListView) view.findViewById(R.id.sport_date);//数据少时显示
        if (data.size() > 5) {
            scrollListview.setAdapter(mAdapter);
            scrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    currentPosition = position;
                    mAdapter.updateDatas(position);
                    mAdapter.notifyDataSetChanged();
                }
            });
            scrollview.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        } else {
            listview.setAdapter(mAdapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    currentPosition = position;
                    mAdapter.updateDatas(position);
                    mAdapter.notifyDataSetChanged();
                }
            });
            scrollview.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
        }

        dataOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();

                currentDialogPosition = currentPosition;
                String newData = mSports[currentDialogPosition];

                mCurrentSports = newData;

                mHandlerData.postDelayed(mRun, 500); // 加载数据

                //赛程
                mScheduleFragment.setSeason(newData);
                mScheduleFragment.update();
                //积分
                mIntegralFragment.setSeason(newData);
                mIntegralFragment.update();


            }
        });
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(view);
    }
}