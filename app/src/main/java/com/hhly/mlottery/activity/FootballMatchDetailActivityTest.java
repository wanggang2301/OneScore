package com.hhly.mlottery.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.FragmentAdapter;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.frame.footframe.DetailsRollballFragment;
import com.hhly.mlottery.frame.footframe.LiveHeadInfoFragment;
import com.hhly.mlottery.frame.footframe.PreHeadInfoFrament;
import com.hhly.mlottery.util.L;

import java.util.ArrayList;

/**
 * @author wang gang
 * @date 2016/6/2 16:53
 * @des 足球内页改版
 */
public class FootballMatchDetailActivityTest extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, SwipeRefreshLayout.OnRefreshListener {


    //test
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FragmentManager fragmentManager;
    private ViewPager mHeadviewpager;
    private ViewPager mViewPager;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ArrayList<Fragment> list = new ArrayList<>();
    private AppBarLayout appBarLayout;
    private FragmentAdapter fragmentAdapter;
    private TabLayout mTabLayout;
    private TabsAdapter mTabsAdapter;
    private String[] titles = {"滚球", "聊球", "分析", "指数", "统计"};

    private Context mContext;

    private Toolbar toolbar;


    /**
     * 赛事id
     */
    public String mThirdId;

    private int currentFragmentId = 0;


    public final static String BUNDLE_PARAM_THIRDID = "thirdId";

    private final static String TAG = "FootballMatchDetailActivity";


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


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        initData();
    }

    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mHeadviewpager = (ViewPager) findViewById(R.id.headviewpager);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        fragmentManager = getSupportFragmentManager();
        fragmentAdapter = new FragmentAdapter(fragmentManager, list);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        appBarLayout.addOnOffsetChangedListener(this);


    }

    private void initData() {
        list.add(new PreHeadInfoFrament());
        list.add(new LiveHeadInfoFragment());
        mHeadviewpager.setAdapter(fragmentAdapter);
        mHeadviewpager.setCurrentItem(0);
        mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
        mTabsAdapter.setTitles(titles);
        mTabsAdapter.addFragments(DetailsRollballFragment.newInstance(), DetailsRollballFragment.newInstance(), DetailsRollballFragment.newInstance(), DetailsRollballFragment.newInstance(), DetailsRollballFragment.newInstance());
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if ((-verticalOffset) == appBarLayout.getTotalScrollRange()) {
            mCollapsingToolbarLayout.setTitle("皇马 VS 巴萨");
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

    }
}
