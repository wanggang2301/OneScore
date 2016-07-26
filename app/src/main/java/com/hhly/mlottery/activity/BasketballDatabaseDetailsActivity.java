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
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.SportsDialogAdapter;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.bean.basket.BasketDatabase.BasketDatabaseBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.basketballframe.BasketDatabaseBigSmallFragment;
import com.hhly.mlottery.frame.basketballframe.BasketDatasaseHandicapFragment;
import com.hhly.mlottery.util.MDStatusBarCompat;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;
import com.hhly.mlottery.widget.NoScrollListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yixq
 * @date 2016/7/15 16:53
 * @des 篮球资料库详情
 */
public class BasketballDatabaseDetailsActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    public static final String LEAGUEID="leagueId";
    private String mThirdId = "936707";

    private ViewPager mViewPager;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private TabLayout mTabLayout;
    private TabsAdapter mTabsAdapter;
    private Toolbar toolbar;
    private LinearLayout mBasketLayoutHeader;
    private CoordinatorLayout mCoordinatorLayout;
    Handler mHandlerData = new Handler();

    private String[] TITLES;

    /**
     * 返回按钮
     */
    private ImageView mBack;
    /**
     * 收藏按钮
     */
    private LinearLayout mCollect;

    LinearLayout headLayout;// 小头部

    private ExactSwipeRefrashLayout mRefreshLayout; //下拉刷新

    private BasketDatasaseHandicapFragment mBasketDatasaseHandicapFragment;
    private BasketDatabaseBigSmallFragment mBasketDatabaseBigSmallFragment;

    private DisplayImageOptions mOptions;
    private DisplayImageOptions mOptionsHead;
    private ImageLoader mImageLoader;
    private ImageView mIcon;
    private ImageView mBackground;
    private TextView mLeaguename;
    private TextView mSportsText;
    private String mLeagueId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basketball_database_details);

        if(getIntent().getExtras() != null){
            mLeagueId = getIntent().getExtras().getString(LEAGUEID);
        }

        mBasketDatasaseHandicapFragment = BasketDatasaseHandicapFragment.newInstance(mLeagueId, "-1"); //TODO~~~~~~~~~~~~~~~~~~~~~~
        mBasketDatabaseBigSmallFragment = BasketDatabaseBigSmallFragment.newInstance(mLeagueId , "-1");

        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                .showImageForEmptyUri(R.mipmap.basket_default)
                .showImageOnFail(R.mipmap.basket_default)// 加载失败显示的图片
                .build();


        mOptionsHead = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
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
        mHandlerData.postDelayed(mRun, 500); // 加载数据
        setListener();

    }

    public String getmThirdId() {
        return mThirdId;
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
        TITLES = new String[]{getResources().getString(R.string.basket_database_details_handicapname),getResources().getString(R.string.basket_database_details_bigsmallname)};

        toolbar = (Toolbar) findViewById(R.id.basket_database_details_toolbar);
        setSupportActionBar(toolbar);

        mIcon = (ImageView)findViewById(R.id.basket_details_guest_icon);
        mBackground = (ImageView)findViewById(R.id.image_background);
        mLeaguename = (TextView)findViewById(R.id.basket_database_leaguename);

        mSportsText = (TextView)findViewById(R.id.basket_database_datails_tv);

        mBasketLayoutHeader = (LinearLayout) findViewById(R.id.basket_database_details_img);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mViewPager = (ViewPager) findViewById(R.id.basket_details_view_pager);
        appBarLayout = (AppBarLayout) findViewById(R.id.basket_database_details_appbar);
        mTabLayout = (TabLayout) findViewById(R.id.basket_database_details_tab_layout);
        mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
        mTabsAdapter.setTitles(TITLES);

        MDStatusBarCompat.setCollapsingToolbar(this, mCoordinatorLayout, appBarLayout, mBasketLayoutHeader, toolbar);


        mTabsAdapter.addFragments(mBasketDatasaseHandicapFragment, mBasketDatabaseBigSmallFragment);
        mViewPager.setOffscreenPageLimit(2);//设置预加载页面的个数。
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        appBarLayout.addOnOffsetChangedListener(this);


        headLayout = (LinearLayout) findViewById(R.id.basket_database_details_header_layout);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                isHindShow(position);
                if (position == 4) {
                    appBarLayout.setExpanded(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRefreshLayout = (ExactSwipeRefrashLayout) findViewById(R.id.basket_database_details_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);

        mBack = (ImageView) this.findViewById(R.id.basket_database_details_back);

        mCollect = (LinearLayout) this.findViewById(R.id.basket_database_details_collect);

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
    private boolean isLoad = false;
    private void initData(){

        // http://192.168.31.43:8888/mlottery/core/basketballData.findLeagueHeader.do?lang=zh&leagueId=2
//        String url = "http://192.168.31.43:8888/mlottery/core/basketballData.findLeagueHeader.do"; //
        String url = BaseURLs.URL_BASKET_DATABASE_DETAILS;
        Map<String, String> params = new HashMap<>();
//        params.put("leagueId", "2");
        params.put("leagueId", mLeagueId);
        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<BasketDatabaseBean>() {

            @Override
            public void onResponse(BasketDatabaseBean basketDatabaseBean) {
                if (basketDatabaseBean != null) {
                    mSports = basketDatabaseBean.getSeason();

                    if (mSports != null || mSports.length != 0) {
                        isLoad = true;
                    }

                    if (mCurrentSports.equals("")) {
                        mSportsText.setText(mSports[0] + getResources().getString(R.string.basket_database_details_season));
                    } else {
                        mSportsText.setText(mCurrentSports + getResources().getString(R.string.basket_database_details_season));
                    }

                    if (basketDatabaseBean.getLeagueName() == null || basketDatabaseBean.getLeagueName().equals("")) {
                        mLeaguename.setText("--");
                    } else {
                        mLeaguename.setText(basketDatabaseBean.getLeagueName());
                    }
                    //图标
                    mImageLoader.displayImage(basketDatabaseBean.getLeagueLogoUrl(), mIcon, mOptions);
                    mImageLoader.displayImage(basketDatabaseBean.getBgUrl(), mBackground, mOptionsHead);

                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                isLoad = false;
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
            headLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            headLayout.setBackgroundColor(getResources().getColor(R.color.transparency));
        }

    }

    /**
     * Fragment页面统计
     */
    private boolean isFragment0 = true;
    private boolean is0 = false;
    private boolean isFragment1 = false;
    private boolean is1 = false;

    private void isHindShow(int position){
        switch (position) {
            case 0:
                isFragment0 = true;
                isFragment1 = false;
                break;
            case 1:
                isFragment0 = false;
                isFragment1 = true;
                break;
        }
        if (is0) {
            is0 = false;
        }
        if (is1) {
            is1 = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        if (is0) {
            is0 = false;
        }
        if (is1) {
            is1 = false;
        }
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
                    mBasketDatasaseHandicapFragment.upDate();
                    mBasketDatabaseBigSmallFragment.upDate();
                }
            }, 1000);
    }


    int currentDialogPosition = 0; // 当前选中的赛季（默认第一个）

    public void setDialog(){
        // Dialog 设置
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this, R.style.AlertDialog);
        LayoutInflater infla = LayoutInflater.from(this);
        View view = infla.inflate(R.layout.sports_alertdialog, null);
        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        Button dataOk = (Button)view.findViewById(R.id.sports_btn_ok);
        titleView.setText(getResources().getString(R.string.basket_database_details_season));

        final List<String> data = new ArrayList<>();
        for (int i = 0; i < mSports.length; i++) {
            data.add(mSports[i]);
        }

        final SportsDialogAdapter mAdapter = new SportsDialogAdapter(data ,this , currentDialogPosition);

        final AlertDialog mAlertDialog = mBuilder.create();
        mAlertDialog.setCanceledOnTouchOutside(true);//设置空白处点击 dialog消失

        Toast.makeText(BasketballDatabaseDetailsActivity.this, "size》》" + data.size(), Toast.LENGTH_SHORT).show();
        /**
         * 根据List数据条数加载不同的view （数据多加载可滑动View）
         */
        ScrollView scroll = (ScrollView)view.findViewById(R.id.basket_sports_scroll);//数据多时显示
        NoScrollListView scrollListview = (NoScrollListView) view.findViewById(R.id.sport_date_scroll);
        ListView listview = (ListView) view.findViewById(R.id.sport_date);//数据少时显示
        if (data.size() > 5) {
            scrollListview.setAdapter(mAdapter);
            scrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    currentDialogPosition = position;
                    mAdapter.updateDatas(currentDialogPosition);
                    mAdapter.notifyDataSetChanged();

                    Toast.makeText(BasketballDatabaseDetailsActivity.this, "data1》》" + currentDialogPosition, Toast.LENGTH_SHORT).show();
                }
            });
            scroll.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        } else {
            listview.setAdapter(mAdapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    currentDialogPosition = position;
                    mAdapter.updateDatas(currentDialogPosition);
                    mAdapter.notifyDataSetChanged();

                    Toast.makeText(BasketballDatabaseDetailsActivity.this, "data2》》" + currentDialogPosition, Toast.LENGTH_SHORT).show();
                }
            });
            scroll.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
        }

        dataOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();

                String newData = mSports[currentDialogPosition];
//                Toast.makeText(BasketballDatabaseDetailsActivity.this, "赛季 ：" + newData, Toast.LENGTH_SHORT).show();

                mCurrentSports = newData;

                mHandlerData.postDelayed(mRun, 500); // 加载数据

                //让分盘
                mBasketDatasaseHandicapFragment.setSeason(newData); //切换赛季
                mBasketDatasaseHandicapFragment.upDate();

                //大小盘
                mBasketDatabaseBigSmallFragment.setSeason(newData);
                mBasketDatabaseBigSmallFragment.upDate();

            }
        });

        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(view);
    }
}
