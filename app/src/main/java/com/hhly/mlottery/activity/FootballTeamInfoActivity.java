package com.hhly.mlottery.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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
import com.hhly.mlottery.frame.footballteaminfo.TeamInfoDataFragment;
import com.hhly.mlottery.frame.footballteaminfo.TeamInfoLineupFragment;
import com.hhly.mlottery.frame.footballteaminfo.TeamInfoOddsFragment;
import com.hhly.mlottery.frame.footballteaminfo.TeamInfoScoreFragment;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.CustomViewpager;
import com.hhly.mlottery.widget.ScrollTouchListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 足球球队详情activity
 * 20170420 tangrr
 */
public class FootballTeamInfoActivity extends BaseActivity implements View.OnClickListener {
    private final String TEAM_ID = "TEAM_ID";
    private final String TITLE_TEAM_NAME = "TITLE_TEAM_NAME";

    private TabLayout tabLayout;
    private CustomViewpager viewPager;
    private List<String> titles;
    private List<Fragment> fragments = new ArrayList<>();
    private TextView title;
    private TextView currentData;
    private PureViewPagerAdapter adapter;
    private TeamInfoDataFragment teamInfoDataFragment;
    private TeamInfoScoreFragment teamInfoScoreFragment;
    private TeamInfoOddsFragment teamInfoOddsFragment;
    private TeamInfoLineupFragment teamInfoLineupFragment;

    private String teamId;
    private String titleName;
    private List<String> seasonList = new ArrayList<>();
    private List<String> yearList = new ArrayList<>();
    private String leagueDate;
    private String scoreDate;
    private int currentDataIndex = 0;
    private int currentOddsIndex = 0;
    private boolean isOddsPager = false;// 是否为赛程赛果界面

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
        Map<String, String> map = new HashMap<>();
        map.put("teamId", teamId);// 球队ID

        VolleyContentFast.requestJsonByGet(BaseURLs.FOOT_TEAM_DATA_URL, map, new VolleyContentFast.ResponseSuccessListener<FootTeamDataBean>() {
            @Override
            public void onResponse(FootTeamDataBean json) {
                if (json != null && json.getCode() == 200) {
                    seasonList.clear();
                    yearList.clear();

                    seasonList.addAll(json.getSeasonList());
                    yearList.addAll(json.getYearList());


                    viewPager.setIsScrollable(true);
                    if (leagueDate == null && seasonList.size() != 0) {

                        leagueDate = seasonList.get(0);
                        if (!isOddsPager) {
                            currentData.setText(leagueDate == null ? "" : leagueDate);
                        }
                        teamInfoDataFragment.updataList(json.getTeamInfo(), leagueDate);
                        teamInfoOddsFragment.updataList(leagueDate);
                        teamInfoLineupFragment.updataList(leagueDate);
                    }
                    if (scoreDate == null && yearList.size() != 0) {
                        scoreDate = yearList.get(0);
                        if (isOddsPager) {
                            currentData.setText(scoreDate == null ? "" : scoreDate);
                        }
                        teamInfoScoreFragment.updataList(scoreDate);
                    }
                } else {
                    teamInfoDataFragment.updataList(null, null);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                teamInfoDataFragment.updataList(null, null);
            }
        }, FootTeamDataBean.class);
    }

    private void initView() {
        findViewById(R.id.im_team_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_team_title);
        title.setText(titleName == null ? "" : titleName);
        findViewById(R.id.ll_data_select).setOnClickListener(this);
        currentData = (TextView) findViewById(R.id.tv_data_content);

        tabLayout = (TabLayout) findViewById(R.id.team_title_tabs);
        viewPager = (CustomViewpager) findViewById(R.id.view_pager);
        viewPager.setIsScrollable(false);// 禁止滑动，请求成功后再滑动

        titles = new ArrayList<>();
        titles.add(getString(R.string.foot_team_info_data));
        titles.add(getString(R.string.foot_team_info_his));
        titles.add(getString(R.string.foot_team_info_odds));
        titles.add(getString(R.string.foot_team_info_lineup));

        teamInfoDataFragment = TeamInfoDataFragment.newInstance(teamId);
        teamInfoScoreFragment = TeamInfoScoreFragment.newInstance(teamId);
        teamInfoOddsFragment = TeamInfoOddsFragment.newInstance(teamId);
        teamInfoLineupFragment = TeamInfoLineupFragment.newInstance(teamId);

        fragments.add(teamInfoDataFragment);
        fragments.add(teamInfoScoreFragment);
        fragments.add(teamInfoOddsFragment);
        fragments.add(teamInfoLineupFragment);

        adapter = new PureViewPagerAdapter(fragments, titles, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(titles.size());
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                isOddsPager = position == 1;
                if (isOddsPager) {
                    currentData.setText(scoreDate == null ? "" : scoreDate);
                } else {
                    currentData.setText(leagueDate == null ? "" : leagueDate);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // 切换头部日期
    private void onDataRefresh() {
        if (!TextUtils.isEmpty(scoreDate)) {
            teamInfoScoreFragment.updataList(scoreDate);
        }
        if (!TextUtils.isEmpty(leagueDate)) {
            teamInfoOddsFragment.updataList(leagueDate);
            teamInfoLineupFragment.updataList(leagueDate);
            teamInfoDataFragment.updataList(null, leagueDate);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_team_back:
                this.finish();
                break;
            case R.id.ll_data_select:
                if (!isOddsPager) {
                    if (!TextUtils.isEmpty(leagueDate)) {
                        setDialog();
                    }
                } else {
                    if (!TextUtils.isEmpty(scoreDate)) {
                        setDialog();
                    }
                }
                break;
        }
    }

    // 日期选择
    private void setDialog() {
        // Dialog 设置
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this, R.style.AlertDialog);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.sports_alertdialog, null);
        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        Button dataOk = (Button) view.findViewById(R.id.sports_btn_ok);
        titleView.setText(getResources().getString(R.string.basket_database_details_season));

        final SportsDialogAdapter mAdapter;
        if (!isOddsPager) {
            mAdapter = new SportsDialogAdapter(seasonList, this, currentDataIndex);
        } else {
            mAdapter = new SportsDialogAdapter(yearList, this, currentOddsIndex);
        }

        final AlertDialog mAlertDialog = mBuilder.create();
        mAlertDialog.setCanceledOnTouchOutside(true);

        // 根据List数据条数加载不同的ListView （数据多加载可滑动 ScrollTouchListview）
        ScrollView scrollview = (ScrollView) view.findViewById(R.id.basket_sports_scroll);//数据多时显示
        ScrollTouchListView scrollListview = (ScrollTouchListView) view.findViewById(R.id.sport_date_scroll);
        ListView listview = (ListView) view.findViewById(R.id.sport_date);//数据少时显示

        int size;
        if (!isOddsPager) {
            size = seasonList.size();
        } else {
            size = yearList.size();
        }
        if (size > 5) {
            scrollListview.setAdapter(mAdapter);
            scrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (!isOddsPager) {
                        currentDataIndex = position;
                    } else {
                        currentOddsIndex = position;
                    }
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

                    if (!isOddsPager) {
                        currentDataIndex = position;
                    } else {
                        currentOddsIndex = position;
                    }
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
                leagueDate = seasonList.get(currentDataIndex);
                scoreDate = yearList.get(currentOddsIndex);
                if (!isOddsPager) {
                    currentData.setText(leagueDate == null ? "" : leagueDate);
                } else {
                    currentData.setText(scoreDate == null ? "" : scoreDate);
                }
                onDataRefresh();
            }
        });
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(view);
    }
}
