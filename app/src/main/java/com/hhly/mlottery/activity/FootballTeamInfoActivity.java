package com.hhly.mlottery.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.PureViewPagerAdapter;
import com.hhly.mlottery.adapter.basketball.SportsDialogAdapter;
import com.hhly.mlottery.bean.footballteaminfo.FootTeamDataBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.footballteaminfo.TeamInfoDataFragment;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;
import com.hhly.mlottery.widget.ScrollTouchListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 足球球队详情activity
 * 20170420 tangrr
 */
public class FootballTeamInfoActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private final String TEAM_ID = "TEAM_ID";
    private final String TITLE_TEAM_NAME = "TITLE_TEAM_NAME";

    private ExactSwipeRefreshLayout swipere_fresh;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> titles;
    private List<Fragment> fragments = new ArrayList<>();
    private PureViewPagerAdapter adapter;
    private String teamId;
    private String titleName;
    private List<String> seasonList = new ArrayList<>();
    private List<String> yearList = new ArrayList<>();
    private TeamInfoDataFragment teamInfoDataFragment;
    private String leagueDate;
    private TextView title;
    private TextView currentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_team_info);
        teamId = getIntent().getStringExtra(TEAM_ID);
        titleName = getIntent().getStringExtra(TITLE_TEAM_NAME);

        initView();
        initData();
    }

    public void initData() {
        swipere_fresh.setRefreshing(true);
        Map<String, String> map = new HashMap<>();
        map.put("teamId", teamId);// 球队ID
        map.put("leagueDate", leagueDate);// 日期

        VolleyContentFast.requestJsonByGet(BaseURLs.FOOT_TEAM_DATA_URL, map, new VolleyContentFast.ResponseSuccessListener<FootTeamDataBean>() {
            @Override
            public void onResponse(FootTeamDataBean json) {
                swipere_fresh.setRefreshing(false);
                if (json != null && json.getCode() == 200) {
                    seasonList.clear();
                    yearList.clear();
                    seasonList.addAll(json.getSeasonList());
                    yearList.addAll(json.getYearList());
                    // TODO 判断当前Fragment
                    if (leagueDate == null && seasonList.size() != 0) {
                        leagueDate = seasonList.get(0);
                        currentData.setText(leagueDate == null ? "--" : leagueDate);
                    }
                    teamInfoDataFragment.updataList(json.getTeamInfo());
                } else {
                    teamInfoDataFragment.updataList(null);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                swipere_fresh.setRefreshing(false);
                teamInfoDataFragment.updataList(null);
            }
        }, FootTeamDataBean.class);
    }

    private void initView() {
        findViewById(R.id.im_team_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_team_title);
        title.setText(titleName == null ? "" : titleName);
        findViewById(R.id.ll_data_select).setOnClickListener(this);
        currentData = (TextView) findViewById(R.id.tv_data_content);

        swipere_fresh = (ExactSwipeRefreshLayout) findViewById(R.id.team_swipere_fresh);
        swipere_fresh.setColorSchemeResources(R.color.bg_header);
        swipere_fresh.setOnRefreshListener(this);
        swipere_fresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(this, StaticValues.REFRASH_OFFSET_END));
        tabLayout = (TabLayout) findViewById(R.id.team_title_tabs);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        titles = new ArrayList<>();
        titles.add(getString(R.string.foot_team_info_data));
        titles.add(getString(R.string.foot_team_info_his));
        titles.add(getString(R.string.foot_team_info_odds));
        titles.add(getString(R.string.foot_team_info_lineup));

        teamInfoDataFragment = new TeamInfoDataFragment();

        fragments.add(teamInfoDataFragment);
        fragments.add(teamInfoDataFragment);
        fragments.add(teamInfoDataFragment);
        fragments.add(teamInfoDataFragment);

        adapter = new PureViewPagerAdapter(fragments, titles, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(titles.size());
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_team_back:
                this.finish();
                break;
            case R.id.ll_data_select:
                setDialog();
                break;
        }
    }

    int currentDialogPosition = 0;
    int currentPosition = 0;
    // 日期选择
    private void setDialog() {
        // Dialog 设置
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this, R.style.AlertDialog);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.sports_alertdialog, null);
        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        Button dataOk = (Button) view.findViewById(R.id.sports_btn_ok);
        titleView.setText(getResources().getString(R.string.basket_database_details_season));

        // TODO 判断当前Fragment 使用相对应的日期数据
        final SportsDialogAdapter mAdapter = new SportsDialogAdapter(seasonList, this, currentDialogPosition);

        final AlertDialog mAlertDialog = mBuilder.create();
        mAlertDialog.setCanceledOnTouchOutside(true);//设置空白处点击 dialog消失

        /**
         * 根据List数据条数加载不同的ListView （数据多加载可滑动 ScrollTouchListview）
         */
        ScrollView scrollview = (ScrollView) view.findViewById(R.id.basket_sports_scroll);//数据多时显示
        ScrollTouchListView scrollListview = (ScrollTouchListView) view.findViewById(R.id.sport_date_scroll);
        ListView listview = (ListView) view.findViewById(R.id.sport_date);//数据少时显示
        if (seasonList.size() > 5) {
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
                leagueDate = seasonList.get(currentDialogPosition);
                currentData.setText(leagueDate == null ? "--" : leagueDate);
                initData();
            }
        });
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(view);
    }
}
