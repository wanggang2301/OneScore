package com.hhly.mlottery.activity;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseBean;
import com.hhly.mlottery.bean.basket.infomation.LeagueBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.basketballframe.BasketDatabaseBigSmallFragment;
import com.hhly.mlottery.frame.basketballframe.BasketDatabaseHandicapFragment;
import com.hhly.mlottery.frame.basketballframe.BasketDatabaseRankingFragment;
import com.hhly.mlottery.frame.basketballframe.BasketDatabaseScheduleFragment;
import com.hhly.mlottery.frame.basketballframe.BasketDatabaseStatisticsFragment;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MDStatusBarCompat;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;
import com.hhly.mlottery.widget.ScrollTouchListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yixq
 * @date 2016/7/15 16:53
 * @des 篮球资料库详情
 */
public class BasketballDatabaseDetailsActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String LEAGUE = "league";
    public static final String IS_RANKING = "isRanking";

    private ViewPager mViewPager;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private TabLayout mTabLayout;
    private TabsAdapter mTabsAdapter;
    private Toolbar toolbar;
    private LinearLayout mBasketLayoutHeader;
    private CoordinatorLayout mCoordinatorLayout;
    Handler mHandlerData = new Handler();

    /**
     * 返回按钮
     */
    private ImageView mBack;
    /**
     * 收藏按钮
     */
    private LinearLayout mCollect;

    LinearLayout headLayout;// 小头部

    private ExactSwipeRefreshLayout mRefreshLayout; //下拉刷新

    private BasketDatabaseScheduleFragment mScheduleFragment; // 赛程
    private BasketDatabaseRankingFragment mRankingFragment; // 排行
    private BasketDatabaseHandicapFragment mHandicapFragment; // 让分
    private BasketDatabaseBigSmallFragment mBigSmallFragment; // 大小球
    private BasketDatabaseStatisticsFragment mStatisticsFragment; // 统计

    private ImageView mIcon;
    private ImageView mBackground;
    private TextView mLeagueName;
    private TextView mSportsText;
    private LeagueBean mLeague;
    private boolean mCurrentIsRanking;
    private TextView mTitleLeagueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basketball_database_details);

        /**不统计当前Activity*/
        MobclickAgent.openActivityDurationTrack(false);

        if (getIntent().getExtras() != null) {
            mLeague = getIntent().getExtras().getParcelable(LEAGUE);
            mCurrentIsRanking = getIntent().getExtras().getBoolean(IS_RANKING);
        }

        /**
         * 第一次加载默认赛季数据，不需要season ==》（-1）
         */
        String leagueId = mLeague == null ? null : mLeague.getLeagueId();
        mScheduleFragment = BasketDatabaseScheduleFragment.newInstance(mLeague, null);
        mRankingFragment = BasketDatabaseRankingFragment.newInstance(mLeague, null);
        mHandicapFragment = BasketDatabaseHandicapFragment.newInstance(leagueId, "-1");
        mBigSmallFragment = BasketDatabaseBigSmallFragment.newInstance(leagueId, "-1");
        mStatisticsFragment = BasketDatabaseStatisticsFragment.newInstance(leagueId, "-1");

        initView();
        mHandlerData.postDelayed(mRun, 500); // 加载数据
        setListener();

    }


    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            initData();
        }
    };

    /**
     * 初始化界面
     */
    private void initView() {
        String[] titles = new String[]{
                getString(R.string.basket_database_details_schedule),
                getString(R.string.basket_database_details_ranking),
                getString(R.string.basket_database_details_handicapname),
                getString(R.string.basket_database_details_bigsmallname),
                getString(R.string.basket_database_details_statistic)
        };

        toolbar = (Toolbar) findViewById(R.id.basket_database_details_toolbar);
        setSupportActionBar(toolbar);

        mIcon = (ImageView) findViewById(R.id.basket_details_guest_icon);
        mBackground = (ImageView) findViewById(R.id.image_background);
        mLeagueName = (TextView) findViewById(R.id.basket_database_leaguename);

        mSportsText = (TextView) findViewById(R.id.basket_database_datails_tv);

        mBasketLayoutHeader = (LinearLayout) findViewById(R.id.basket_database_details_img);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mViewPager = (ViewPager) findViewById(R.id.basket_details_view_pager);
        appBarLayout = (AppBarLayout) findViewById(R.id.basket_database_details_appbar);
        mTabLayout = (TabLayout) findViewById(R.id.basket_database_details_tab_layout);
        mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
        mTabsAdapter.setTitles(titles);

        MDStatusBarCompat.setCollapsingToolbar(this, mCoordinatorLayout, appBarLayout, mBasketLayoutHeader, toolbar);

        mTabsAdapter.addFragments(mScheduleFragment,
                mRankingFragment,
                mHandicapFragment,
                mBigSmallFragment,
                mStatisticsFragment);
        mViewPager.setOffscreenPageLimit(5);//设置预加载页面的个数。
        mViewPager.setAdapter(mTabsAdapter);
        if (mCurrentIsRanking) mViewPager.setCurrentItem(1);
        mTabLayout.setupWithViewPager(mViewPager);

        appBarLayout.addOnOffsetChangedListener(this);

        headLayout = (LinearLayout) findViewById(R.id.basket_database_details_header_layout);

        mRefreshLayout = (ExactSwipeRefreshLayout) findViewById(R.id.basket_database_details_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);

        mBack = (ImageView) this.findViewById(R.id.basket_database_details_back);

        mCollect = (LinearLayout) this.findViewById(R.id.basket_database_details_collect);
        mTitleLeagueName = (TextView) findViewById(R.id.basket_database_leaguename_title);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        mBack.setOnClickListener(this);
        mCollect.setOnClickListener(this);

    }

    private String[] mSports; // 赛季集

    private String mCurrentSports = "";
    private boolean isLoad = false;//赛季是否可筛选

    private void initData() {

        // http://192.168.31.43:8888/mlottery/core/basketballData.findLeagueHeader.do?lang=zh&leagueId=2
//        String url = "http://192.168.31.115:8080/mlottery/core/basketballData.findLeagueHeader.do";
        String url = BaseURLs.URL_BASKET_DATABASE_DETAILS;
        Map<String, String> params = new HashMap<>();
//        params.put("leagueId", "2");
        params.put("leagueId", mLeague.getLeagueId());
        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<BasketDatabaseBean>() {

            @Override
            public void onResponse(BasketDatabaseBean basketDatabaseBean) {
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
//                            mSportsText.setText(mSports[0] + getResources().getString(R.string.basket_database_details_season));
                            mSportsText.setText(mSports[0]);
                        }
                    } else {
//                        mSportsText.setText(mCurrentSports + getResources().getString(R.string.basket_database_details_season));
                        mSportsText.setText(mCurrentSports);
                    }

                    if (basketDatabaseBean.getLeagueName() == null || basketDatabaseBean.getLeagueName().equals("")) {
                        mLeagueName.setText("--");
                        mTitleLeagueName.setText("--");
                    } else {
                        mLeagueName.setText(basketDatabaseBean.getLeagueName());
                        mTitleLeagueName.setText(basketDatabaseBean.getLeagueName());
                    }
                    //图标
                    ImageLoader.load(BasketballDatabaseDetailsActivity.this,basketDatabaseBean.getLeagueLogoUrl(),R.mipmap.basket_default).into(mIcon);
                    ImageLoader.load(BasketballDatabaseDetailsActivity.this,basketDatabaseBean.getBgUrl()).into(mBackground);

                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                isLoad = false; //不可筛选 aaaa
            }
        }, BasketDatabaseBean.class);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.basket_database_details_back:

                setResult(Activity.RESULT_OK);

                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.basket_database_details_collect:
                if (isLoad) {
                    setDialog();
                }
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (mCollapsingToolbarLayout.getHeight() + verticalOffset < mBasketLayoutHeader.getHeight()) {
            mRefreshLayout.setEnabled(false);
        } else {
            mRefreshLayout.setEnabled(true);
        }
        if ((-verticalOffset) == appBarLayout.getTotalScrollRange()) {
            headLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            mTitleLeagueName.setVisibility(View.VISIBLE);
        } else {
            headLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.transparency));
            mTitleLeagueName.setVisibility(View.GONE);
        }

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
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);

                mHandlerData.postDelayed(mRun, 500); // 加载数据
                mHandicapFragment.upDate();
                mBigSmallFragment.upDate();
                mScheduleFragment.update();
                mRankingFragment.update();
                mStatisticsFragment.upData();
            }
        }, 1000);
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
        ScrollView scroll = (ScrollView) view.findViewById(R.id.basket_sports_scroll);//数据多时显示
        ScrollTouchListView scrollListview = (ScrollTouchListView) view.findViewById(R.id.sport_date_scroll);
        ListView listview = (ListView) view.findViewById(R.id.sport_date);//数据少时显示
        if (data.size() > 5) {
            scrollListview.setAdapter(mAdapter);
            scrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                    currentDialogPosition = position;
                    currentPosition = position;
                    mAdapter.updateDatas(position);
                    mAdapter.notifyDataSetChanged();
                }
            });
            scroll.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        } else {
            listview.setAdapter(mAdapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                    currentDialogPosition = position;
                    currentPosition = position;
                    mAdapter.updateDatas(position);
                    mAdapter.notifyDataSetChanged();
                }
            });
            scroll.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
        }

        dataOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();

                currentDialogPosition = currentPosition;
                String newData = mSports[currentDialogPosition];
//                Toast.makeText(BasketballDatabaseDetailsActivity.this, "赛季 ：" + newData, Toast.LENGTH_SHORT).show();

                mCurrentSports = newData;

                mHandlerData.postDelayed(mRun, 500); // 加载数据

                mScheduleFragment.setSeason(newData);
                mScheduleFragment.update();

                mRankingFragment.setSeason(newData);
                mRankingFragment.update();

                //让分盘
                mHandicapFragment.setSeason(newData); //切换赛季
                mHandicapFragment.upDate();

                //大小盘
                mBigSmallFragment.setSeason(newData);
                mBigSmallFragment.upDate();

                //统计
                mStatisticsFragment.setSeason(newData);
                mStatisticsFragment.upData();

            }
        });

        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandlerData.removeCallbacksAndMessages(null);
    }
}
