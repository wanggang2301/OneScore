package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.bean.tennisball.datails.analysis.TennisAnalysisBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.tennisfrag.datailsfrag.TennisAnalysisFrag;
import com.hhly.mlottery.frame.tennisfrag.datailsfrag.TennisEurFrag;
import com.hhly.mlottery.frame.tennisfrag.datailsfrag.TennisPlateFrag;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

/**
 * desc:网球内页
 * Created by 107_tangrr on 2017/3/21 0021.
 */

public class TennisBallDetailsActivity extends BaseWebSocketActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private final String TAG = "TennisBallDetailsActivity";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public ExactSwipeRefreshLayout refreshLayout;
    private TabsAdapter tabsAdapter;
    private TextView tv_match_name, tv_date, tv_time, tv_start, tv_home_name, tv_home_name2, tv_home_total_score,
            tv_guest_total_score, tv_guest_name, tv_guest_name2, tv_home_score1, tv_home_score2, tv_home_score3,
            tv_home_score4, tv_home_score5, tv_guest_score1, tv_guest_score2, tv_guest_score3, tv_guest_score4, tv_guest_score5;

    private TennisAnalysisBean tennsiAnalysis;
    private TennisAnalysisBean.DataBean mData;

    private String mThirdId;
    private TennisAnalysisFrag tennisAnalysisFrag;
    private boolean isSingle;// 是否单人比赛


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setWebSocketUri(BaseURLs.WS_SERVICE);
//        setTopic("USER.topic.tennis.score");

        setContentView(R.layout.tennis_details_activity);

        mThirdId = getIntent().getStringExtra("thirdId");

        L.d(TAG, "mThirdId: " + mThirdId);

        initView();
        initData();
    }

    private void initData() {

        tennsiAnalysis = JSON.parseObject(getTestData(), TennisAnalysisBean.class);
        mData = tennsiAnalysis.getData();

        L.d("Tennis", "赛事名：" + tennsiAnalysis.getData().getRecentMatch().getHomePlayerRecentMatch().getMatchList().get(0).getLeagueName());

        // TODO 更新分析页面数据
        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tennisAnalysisFrag.updataChange(mData);
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        // TODO 添加数据显示
        // 比赛类型： 1男子、2女子、3男双、4女双、5混双
        int matchType = mData.getMatchInfo().getMatchType();
        switch (matchType){
            case 1:
            case 2:
                isSingle = true;
                break;
            case 3:
            case 4:
            case 5:
                isSingle = false;
                break;
            default:
                isSingle = false;
                break;
        }

        tv_match_name.setText(mData.getMatchInfo().getLeagueName());
        tv_date.setText(DateUtil.convertDateToNation(mData.getMatchInfo().getStartDate()));
        tv_time.setText(mData.getMatchInfo().getStartTime());

        // -6 P2退赛,-5 P1退赛,-4 待定,-3 推迟,-2 中断,-1 完,0 未开始,>0 进行中
        switch (mData.getMatchInfo().getMatchStatus()) {
            case -6:
                tv_start.setText(getString(R.string.tennis_match_p2));
                tv_start.setTextColor(getResources().getColor(R.color.number_blue));
                break;
            case -5:
                tv_start.setText(getString(R.string.tennis_match_p1));
                tv_start.setTextColor(getResources().getColor(R.color.number_blue));
                break;
            case -4:
                tv_start.setText(getString(R.string.tennis_match_dd));
                tv_start.setTextColor(getResources().getColor(R.color.number_blue));
                break;
            case -3:
                tv_start.setText(getString(R.string.tennis_match_tc));
                tv_start.setTextColor(getResources().getColor(R.color.number_blue));
                break;
            case -2:
                tv_start.setText(getString(R.string.tennis_match_zd));
                tv_start.setTextColor(getResources().getColor(R.color.number_blue));
                break;
            case -1:
                tv_start.setText(getString(R.string.tennis_match_over));
                tv_start.setTextColor(getResources().getColor(R.color.number_red));
                break;
            case 0:
                tv_start.setText(getString(R.string.tennis_match_not_start));
                tv_start.setTextColor(getResources().getColor(R.color.mdy_666));
                break;
            default:
                tv_start.setText(getString(R.string.tennis_match_not_start));
                tv_start.setTextColor(getResources().getColor(R.color.mdy_666));
                break;
        }

        tv_home_name.setText(mData.getMatchInfo().getHomePlayer1().getName());
        tv_guest_name.setText(mData.getMatchInfo().getGuestPlayer1().getName());

        tv_home_total_score.setText(String.valueOf(mData.getMatchInfo().getMatchScore().getHomeTotalScore()));
        tv_guest_total_score.setText(String.valueOf(mData.getMatchInfo().getMatchScore().getAwayTotalScore()));

        if (isSingle) {
            // 单人赛
            tv_home_name2.setVisibility(View.GONE);
            tv_guest_name2.setVisibility(View.GONE);
        } else {
            // 双人赛
            tv_home_name2.setVisibility(View.VISIBLE);
            tv_guest_name2.setVisibility(View.VISIBLE);
            tv_home_name2.setText(mData.getMatchInfo().getHomePlayer2().getName());
            tv_guest_name2.setText(mData.getMatchInfo().getGuestPlayer2().getName());
        }

        if (mData.getMatchInfo().getMatchScore().getHomeSetScore1() == 0 && mData.getMatchInfo().getMatchScore().getAwaySetScore1() == 0) {
            tv_home_score1.setText("");
            tv_guest_score1.setText("");
        } else {
            tv_home_score1.setText(String.valueOf(mData.getMatchInfo().getMatchScore().getHomeSetScore1()));
            tv_guest_score1.setText(String.valueOf(mData.getMatchInfo().getMatchScore().getAwaySetScore1()));
        }

        if (mData.getMatchInfo().getMatchScore().getHomeSetScore2() == 0 && mData.getMatchInfo().getMatchScore().getAwaySetScore2() == 0) {
            tv_home_score2.setText("");
            tv_guest_score2.setText("");
        } else {
            tv_home_score2.setText(String.valueOf(mData.getMatchInfo().getMatchScore().getHomeSetScore2()));
            tv_guest_score2.setText(String.valueOf(mData.getMatchInfo().getMatchScore().getAwaySetScore2()));
        }

        if (mData.getMatchInfo().getMatchScore().getHomeSetScore3() == 0 && mData.getMatchInfo().getMatchScore().getAwaySetScore3() == 0) {
            tv_home_score3.setText("");
            tv_guest_score3.setText("");
        } else {
            tv_home_score3.setText(String.valueOf(mData.getMatchInfo().getMatchScore().getHomeSetScore3()));
            tv_guest_score3.setText(String.valueOf(mData.getMatchInfo().getMatchScore().getAwaySetScore3()));
        }

        if (mData.getMatchInfo().getMatchScore().getHomeSetScore4() == 0 && mData.getMatchInfo().getMatchScore().getAwaySetScore4() == 0) {
            tv_home_score4.setText("");
            tv_guest_score4.setText("");
        } else {
            tv_home_score4.setText(String.valueOf(mData.getMatchInfo().getMatchScore().getHomeSetScore4()));
            tv_guest_score4.setText(String.valueOf(mData.getMatchInfo().getMatchScore().getAwaySetScore4()));
        }

        if (mData.getMatchInfo().getMatchScore().getHomeSetScore5() == 0 && mData.getMatchInfo().getMatchScore().getAwaySetScore5() == 0) {
            tv_home_score5.setText("");
            tv_guest_score5.setText("");
        } else {
            tv_home_score5.setText(String.valueOf(mData.getMatchInfo().getMatchScore().getHomeSetScore5()));
            tv_guest_score5.setText(String.valueOf(mData.getMatchInfo().getMatchScore().getAwaySetScore5()));
        }
    }

    private void initView() {
        // 下拉刷新
        refreshLayout = (ExactSwipeRefreshLayout) findViewById(R.id.tennis_details_refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.tabhost);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(this, StaticValues.REFRASH_OFFSET_END));
        // viewPager
        tabLayout = (TabLayout) findViewById(R.id.tennis_details_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.tennis_details_view_pager);
        tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        tabsAdapter.setTitles(new String[]{
                getString(R.string.basket_analyze),
                getString(R.string.basket_alet),
                getString(R.string.basket_eur)});

        tennisAnalysisFrag = TennisAnalysisFrag.newInstance(mThirdId);
        tabsAdapter.addFragments(tennisAnalysisFrag);
        tabsAdapter.addFragments(TennisPlateFrag.newInstance(mThirdId));
        tabsAdapter.addFragments(TennisEurFrag.newInstance(mThirdId));
        viewPager.setAdapter(tabsAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        // 返回退出
        findViewById(R.id.tennis_details_back).setOnClickListener(this);
        // 头部数据
        tv_match_name = (TextView) findViewById(R.id.tennis_head_match_name);
        tv_date = (TextView) findViewById(R.id.tennis_head_date);
        tv_time = (TextView) findViewById(R.id.tennis_head_time);
        tv_start = (TextView) findViewById(R.id.tennis_details_start);
        tv_home_name = (TextView) findViewById(R.id.tennis_home_name);
        tv_home_name2 = (TextView) findViewById(R.id.tennis_home_name2);
        tv_home_total_score = (TextView) findViewById(R.id.tennis_home_total_score);
        tv_guest_total_score = (TextView) findViewById(R.id.tennis_guest_total_score);
        tv_guest_name = (TextView) findViewById(R.id.tennis_guest_name);
        tv_guest_name2 = (TextView) findViewById(R.id.tennis_guest_name2);
        tv_home_score1 = (TextView) findViewById(R.id.tennis_home_score1);
        tv_home_score2 = (TextView) findViewById(R.id.tennis_home_score2);
        tv_home_score3 = (TextView) findViewById(R.id.tennis_home_score3);
        tv_home_score4 = (TextView) findViewById(R.id.tennis_home_score4);
        tv_home_score5 = (TextView) findViewById(R.id.tennis_home_score5);
        tv_guest_score1 = (TextView) findViewById(R.id.tennis_guest_score1);
        tv_guest_score2 = (TextView) findViewById(R.id.tennis_guest_score2);
        tv_guest_score3 = (TextView) findViewById(R.id.tennis_guest_score3);
        tv_guest_score4 = (TextView) findViewById(R.id.tennis_guest_score4);
        tv_guest_score5 = (TextView) findViewById(R.id.tennis_guest_score5);
    }

    @Override
    protected void onTextResult(String text) {
        L.d(TAG, "网球内页推送：" + text);


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

    @Override
    public void onRefresh() {
        // TODO 下拉刷新
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tennis_details_back:
                this.finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO 关闭webSocket
    }

    // 测试数据
    private String getTestData() {
        return "{\"result\":\"200\",\"data\":{\"recentMatch\":{\"homePlayerRecentMatch\":{\"playerFail\":0,\"playerWin\":3,\"totalTimes\":3,\"winRate\":\"100.00%\",\"matchList\":[{\"startDate\":\"2017-03-02\",\"startTime\":\"01:00\",\"leagueName\":\"迪拜公开赛(ATP)\",\"role\":\"10\",\"siteType\":\"11\",\"homePlayer1\":{\"name\":\"H.孔蒂宁\",\"id\":null},\"homePlayer2\":{\"name\":\"J·皮亚斯\",\"id\":null},\"guestPlayer1\":{\"name\":\"伊文斯\",\"id\":null},\"guestPlayer2\":{\"name\":\"G·穆勒\",\"id\":null},\"matchScore\":{\"homeSetScore1\":7,\"homeSetScore2\":6,\"homeSetScore3\":0,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":6,\"awaySetScore2\":4,\"awaySetScore3\":0,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeTotalScore\":2,\"awayTotalScore\":0,\"homeCurrentScore\":null,\"awayCurrentScore\":null,\"currentTimes\":2}},{\"startDate\":\"2017-03-02\",\"startTime\":\"01:00\",\"leagueName\":\"迪拜公开赛(ATP)\",\"role\":\"3\",\"siteType\":\"11\",\"homePlayer1\":{\"name\":\"H.孔蒂宁\",\"id\":null},\"homePlayer2\":{\"name\":\"J·皮亚斯\",\"id\":null},\"guestPlayer1\":{\"name\":\"伊文斯\",\"id\":null},\"guestPlayer2\":{\"name\":\"G·穆勒\",\"id\":null},\"matchScore\":{\"homeSetScore1\":7,\"homeSetScore2\":6,\"homeSetScore3\":0,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":6,\"awaySetScore2\":4,\"awaySetScore3\":0,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeTotalScore\":2,\"awayTotalScore\":0,\"homeCurrentScore\":null,\"awayCurrentScore\":null,\"currentTimes\":2}},{\"startDate\":\"2017-02-27\",\"startTime\":\"22:45\",\"leagueName\":\"迪拜公开赛(ATP)\",\"role\":\"10\",\"siteType\":\"11\",\"homePlayer1\":{\"name\":\"伊文斯\",\"id\":null},\"homePlayer2\":{\"name\":\"G·穆勒\",\"id\":null},\"guestPlayer1\":{\"name\":\"A.梅利\",\"id\":null},\"guestPlayer2\":{\"name\":\"泽蒙季奇\",\"id\":null},\"matchScore\":{\"homeSetScore1\":6,\"homeSetScore2\":7,\"homeSetScore3\":0,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":1,\"awaySetScore2\":6,\"awaySetScore3\":0,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeTotalScore\":2,\"awayTotalScore\":0,\"homeCurrentScore\":null,\"awayCurrentScore\":null,\"currentTimes\":2}}]},\"guestPlayerRecentMatch\":{\"playerFail\":0,\"playerWin\":3,\"totalTimes\":3,\"winRate\":\"100.00%\",\"matchList\":[{\"startDate\":\"2017-02-27\",\"startTime\":\"22:45\",\"leagueName\":\"迪拜公开赛(ATP)\",\"role\":\"10\",\"siteType\":\"11\",\"homePlayer1\":{\"name\":\"伊文斯\",\"id\":null},\"homePlayer2\":{\"name\":\"G·穆勒\",\"id\":null},\"guestPlayer1\":{\"name\":\"A.梅利\",\"id\":null},\"guestPlayer2\":{\"name\":\"泽蒙季奇\",\"id\":null},\"matchScore\":{\"homeSetScore1\":6,\"homeSetScore2\":7,\"homeSetScore3\":0,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":1,\"awaySetScore2\":6,\"awaySetScore3\":0,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeTotalScore\":2,\"awayTotalScore\":0,\"homeCurrentScore\":null,\"awayCurrentScore\":null,\"currentTimes\":2}},{\"startDate\":\"2014-12-30\",\"startTime\":\"22:30\",\"leagueName\":\"卡塔尔站(ATP)\",\"role\":\"10\",\"siteType\":\"11\",\"homePlayer1\":{\"name\":\"穆雷\",\"id\":null},\"homePlayer2\":{\"name\":\"泽蒙季奇\",\"id\":null},\"guestPlayer1\":{\"name\":\"布兰德斯\",\"id\":null},\"guestPlayer2\":{\"name\":\"梅耶尔\",\"id\":null},\"matchScore\":{\"homeSetScore1\":3,\"homeSetScore2\":7,\"homeSetScore3\":10,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":6,\"awaySetScore2\":6,\"awaySetScore3\":8,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeTotalScore\":2,\"awayTotalScore\":1,\"homeCurrentScore\":null,\"awayCurrentScore\":null,\"currentTimes\":3}},{\"startDate\":\"2014-01-02\",\"startTime\":\"01:30\",\"leagueName\":\"卡塔尔站(ATP)\",\"role\":\"3\",\"siteType\":\"11\",\"homePlayer1\":{\"name\":\"A·皮亚\",\"id\":null},\"homePlayer2\":{\"name\":\"布鲁诺-苏雷斯\",\"id\":null},\"guestPlayer1\":{\"name\":\"穆雷\",\"id\":null},\"guestPlayer2\":{\"name\":\"泽蒙季奇\",\"id\":null},\"matchScore\":{\"homeSetScore1\":7,\"homeSetScore2\":6,\"homeSetScore3\":0,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":6,\"awaySetScore2\":4,\"awaySetScore3\":0,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeTotalScore\":2,\"awayTotalScore\":0,\"homeCurrentScore\":null,\"awayCurrentScore\":null,\"currentTimes\":2}}]}},\"rankScore\":[{\"ranking\":\"61\",\"oldRanking\":\"61\",\"rankingChange\":\"0\",\"normalPlayerId\":\"321268\",\"playerName\":\"泽蒙季奇\",\"nationality\":\"塞尔维亚\",\"totalIntegral\":\"1300\",\"numberOfEntries\":\"24\"},{\"ranking\":\"153\",\"oldRanking\":\"153\",\"rankingChange\":\"0\",\"normalPlayerId\":\"319382\",\"playerName\":\"G·穆勒\",\"nationality\":\"卢森堡\",\"totalIntegral\":\"495\",\"numberOfEntries\":\"13\"}],\"dataCompare\":[{\"home1\":\"伊文斯\",\"home2\":\"G·穆勒\",\"guest1\":\"A.梅利\",\"guest2\":\"泽蒙季奇\",\"status\":0},{\"home1\":\"66%\",\"home2\":\"63%\",\"guest1\":\"56%\",\"guest2\":null,\"status\":1},{\"home1\":\"54%\",\"home2\":\"75%\",\"guest1\":\"75%\",\"guest2\":null,\"status\":2},{\"home1\":\"30%\",\"home2\":\"52%\",\"guest1\":\"52%\",\"guest2\":null,\"status\":3},{\"home1\":\"30%\",\"home2\":\"30%\",\"guest1\":\"43%\",\"guest2\":null,\"status\":4},{\"home1\":\"100%\",\"home2\":\"23%\",\"guest1\":\"42%\",\"guest2\":null,\"status\":5},{\"home1\":\"57%\",\"home2\":\"61%\",\"guest1\":\"69%\",\"guest2\":null,\"status\":6},{\"home1\":\"4\",\"home2\":\"16\",\"guest1\":\"9\",\"guest2\":null,\"status\":7},{\"home1\":\"5\",\"home2\":\"4\",\"guest1\":\"3\",\"guest2\":null,\"status\":8},{\"home1\":\"28\",\"home2\":\"36\",\"guest1\":\"28\",\"guest2\":null,\"status\":9},{\"home1\":\"15\",\"home2\":\"44\",\"guest1\":\"41\",\"guest2\":null,\"status\":10}],\"matchRecord\":{\"playerFail\":0,\"playerWin\":1,\"totalTimes\":1,\"winRate\":\"100.00%\",\"matchList\":[{\"startDate\":\"2017-02-27\",\"startTime\":\"22:45\",\"leagueName\":\"迪拜公开赛(ATP)\",\"role\":\"10\",\"siteType\":\"11\",\"homePlayer1\":{\"name\":\"伊文斯\",\"id\":null},\"homePlayer2\":{\"name\":\"G·穆勒\",\"id\":null},\"guestPlayer1\":{\"name\":\"A.梅利\",\"id\":null},\"guestPlayer2\":{\"name\":\"泽蒙季奇\",\"id\":null},\"matchScore\":{\"homeSetScore1\":6,\"homeSetScore2\":7,\"homeSetScore3\":0,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":1,\"awaySetScore2\":6,\"awaySetScore3\":0,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeTotalScore\":2,\"awayTotalScore\":0,\"homeCurrentScore\":null,\"awayCurrentScore\":null,\"currentTimes\":2}}]},\"matchInfo\":{\"leagueId\":\"323442\",\"leagueName\":\"ATP迪拜公开赛\",\"matchId\":\"848814040\",\"matchName\":\"伊文斯/G·穆勒 VS A.梅利/泽蒙季奇\",\"startTime\":\"22:45:00\",\"startDate\":\"2017-02-27\",\"roundName\":\"第一轮\",\"matchType\":\"3\",\"dataType\":\"2\",\"matchStatus\":\"-1\",\"homePlayer1\":{\"name\":\"伊文斯\",\"id\":\"319381\"},\"homePlayer2\":{\"name\":\"G·穆勒\",\"id\":\"319382\"},\"guestPlayer1\":{\"name\":\"A.梅利\",\"id\":\"319521\"},\"guestPlayer2\":{\"name\":\"泽蒙季奇\",\"id\":\"321268\"},\"matchScore\":{\"homeSetScore1\":6,\"homeSetScore2\":7,\"homeSetScore3\":0,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":1,\"awaySetScore2\":6,\"awaySetScore3\":0,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeTotalScore\":2,\"awayTotalScore\":0,\"homeCurrentScore\":null,\"awayCurrentScore\":null,\"currentTimes\":2}}}}";
    }
}
