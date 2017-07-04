package com.hhly.mlottery.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.BasketSeasonSelectAdapter;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.callback.BasketTeamParams;
import com.hhly.mlottery.frame.HandicapStatisticsFragment;
import com.hhly.mlottery.frame.TechnicalStatisticsFragment;
import com.hhly.mlottery.frame.basketballframe.basketballteam.basketballteamdata.BasketTeamDataFragment;
import com.hhly.mlottery.frame.basketballframe.basketballteam.resultschedule.BasketTeamResultFragment;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.MDStatusBarCompat;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import data.bean.BasketTeamDataBean;

public class BasketballTeamActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener ,SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.basket_team_refresh_layout)
    ExactSwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.basket_team_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.basket_team_collapsing)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.basket_team_appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.basket_team_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.basket_team_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.basket_team_img)
    LinearLayout mBasketLayoutHeader;
    @BindView(R.id.basket_team_header_layout)
    LinearLayout mLayoutTitleHead;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.basket_team_back)
    ImageView mBack;
    @BindView(R.id.basket_team_head_icon)
    ImageView mIcon;
    @BindView(R.id.basket_team_name)
    TextView mLeagueName;
    @BindView(R.id.basket_team_season)
    TextView mSeasonText;
    @BindView(R.id.basket_team_collect)
    LinearLayout mSelect;
    @BindView(R.id.basket_team_title)
    TextView mTitleLeagueName;

    private ListView mSeasonListView;

    private List<String> lists = new ArrayList<String>();

    private int mCurrentPosition=0;

    private TabsAdapter mTabsAdapter;
    private String mSeason="";
    private String mLeagueId;
    private String mTeamId;

    private BasketTeamDataFragment mTeamDataFragment; //球队统计
    private BasketTeamResultFragment mResultFragment; //赛程赛果
    private TechnicalStatisticsFragment technicalStatisticsFragment;
    private HandicapStatisticsFragment handicapStatisticsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basketball_team);
        if(getIntent()!=null){
            mLeagueId=getIntent().getStringExtra(BasketTeamParams.LEAGUE_ID);
            mTeamId=getIntent().getStringExtra(BasketTeamParams.TEAM_ID);
            if(mLeagueId.equals("2")){ //WNBA
                mSeason=Calendar.getInstance().get(Calendar.YEAR)+"";
            }else{
                mSeason=getCurrentSeason();
            }
        }
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

//        lists=  getRecentThreeSeason();


//        mSeason=lists.get(mCurrentPosition);

        mTeamDataFragment=BasketTeamDataFragment.newInstance(mSeason,mLeagueId,mTeamId);
        mResultFragment=BasketTeamResultFragment.newInstance(mSeason,mLeagueId,mTeamId);
        handicapStatisticsFragment = HandicapStatisticsFragment.newInstance(mSeason,mLeagueId,mTeamId);
        technicalStatisticsFragment = TechnicalStatisticsFragment.newInstance(mSeason,mLeagueId,mTeamId);

//        http://192.168.74.85:8096/mlottery/core/basketballData.teamData.do?season=2016-2017&leagueId=27&teamId=27&lang=zh&timeZone=8
//        http://192.168.74.85:8096/mlottery/core/basketballData.teamData.do?lang=zh&season=2016-2017&leagueId=1&teamId=1
        String[] titles = new String[]{
                getString(R.string.basket_team_statistics),getString(R.string.basket_team_result_schdule),
                getString(R.string.basket_team_handicap_statistics),getString(R.string.basket_team_tech_staticss)
        };
//        ,"盘口统计","技术统计"
        setSupportActionBar(mToolbar);

        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });

        mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
        mTabsAdapter.setTitles(titles);

        mTabsAdapter.addFragments(mTeamDataFragment);
        mTabsAdapter.addFragments(mResultFragment);
        mTabsAdapter.addFragments(handicapStatisticsFragment);
        mTabsAdapter.addFragments(technicalStatisticsFragment);

        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(4);
        appBarLayout.addOnOffsetChangedListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==3){
                    mSelect.setVisibility(View.GONE);
                }else {
                    mSelect.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





        MDStatusBarCompat.setCollapsingToolbar(this, mCoordinatorLayout, appBarLayout, mBasketLayoutHeader, mToolbar);

        mSelect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });
    }

    /**
     *
     * @return
     */
    private  String getCurrentSeason(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Calendar calendar = null;

            calendar =  Calendar.getInstance();

            String sim = sdf.format(calendar.getTime());

            calendar.add(Calendar.YEAR, -1);
            String sim1 = sdf.format(calendar.getTime());

            String season = sim1 +"-"+ sim;
        return season;
    }

    /**
     * 选择赛季
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showPopup() {

        View contentView=View.inflate(getApplicationContext(),R.layout.basket_season_select_popup,null);
        mSeasonListView= (ListView) contentView.findViewById(R.id.season_select_listview);
        mSeasonListView.setAdapter(new BasketSeasonSelectAdapter(lists,this,mCurrentPosition));

        final PopupWindow popupWindow=new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setFocusable(true);
//        int[] location = new int[2];
//        mSelect.getLocationOnScreen(location);
//
//        popupWindow.showAtLocation(mSelect, Gravity.NO_GRAVITY, location[0] - mSelect.getWidth() - mSelect.getPaddingRight(), location[1] + mSelect.getHeight());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
       popupWindow.showAsDropDown(mSelect,0,-30, Gravity.RIGHT);


//        popupWindow.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.four_radius));
//        Toast.makeText(this, ">>>", Toast.LENGTH_SHORT).show();

        mSeasonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentPosition=i;
                popupWindow.dismiss();
                mSeasonText.setText(lists.get(i));
                mCurrentPosition=i;
                chooseSeasonToRefresh();
            }
        });//
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (mCollapsingToolbarLayout.getHeight() + verticalOffset < mBasketLayoutHeader.getHeight()) {
            mRefreshLayout.setEnabled(false);
        } else {
            mRefreshLayout.setEnabled(true);
        }
        if ((-verticalOffset) == appBarLayout.getTotalScrollRange()) {
            mTitleLeagueName.setVisibility(View.VISIBLE);
        } else {
            mTitleLeagueName.setVisibility(View.GONE);
        }

    }

    /**
     * 设置头部图标与球队名称
     */
    public void setHeaderIconAndName(BasketTeamDataBean.TeamInfoEntity info){
        mLeagueName.setText(info.getTeamName());
        mTitleLeagueName.setText(info.getTeamName());
        ImageLoader.load(this,info.getTeamImg(),R.mipmap.basket_default).into(mIcon);
        lists=info.getSeasons();
        mSeasonText.setText(lists.get(mCurrentPosition));
    }

    /**
     * 切换日期刷新
     */
    private void chooseSeasonToRefresh(){
        mRefreshLayout.setEnabled(true);
        setRefresh(true);
        mResultFragment.refreshFragment(lists.get(mCurrentPosition));
        mTeamDataFragment.refreshFragment(lists.get(mCurrentPosition));
        handicapStatisticsFragment.refreshFragment(lists.get(mCurrentPosition));
        technicalStatisticsFragment.refreshFragment(lists.get(mCurrentPosition));
    }
    /**
     * 下拉刷新
     * @param refresh 刷新与否
     */
    public void setRefresh(boolean refresh){
        mRefreshLayout.setRefreshing(refresh);
    }

    @Override
    public void onRefresh() {

        if(lists.size()!=0){
            mResultFragment.refreshFragment(lists.get(mCurrentPosition));
            mTeamDataFragment.refreshFragment(lists.get(mCurrentPosition));
            handicapStatisticsFragment.refreshFragment(lists.get(mCurrentPosition));
            technicalStatisticsFragment.refreshFragment(lists.get(mCurrentPosition));
        }else {
            mResultFragment.refreshFragment(mSeason);
            mTeamDataFragment.refreshFragment(mSeason);
            handicapStatisticsFragment.refreshFragment(mSeason);
            technicalStatisticsFragment.refreshFragment(mSeason);
        }

    }
}
