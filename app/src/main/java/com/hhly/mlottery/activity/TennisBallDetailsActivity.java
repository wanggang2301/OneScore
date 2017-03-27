package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.bean.tennisball.datails.analysis.TennsiAnalysisBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.tennisfrag.datailsfrag.TennisAnalysisFrag;
import com.hhly.mlottery.frame.tennisfrag.datailsfrag.TennisEurFrag;
import com.hhly.mlottery.frame.tennisfrag.datailsfrag.TennisPlateFrag;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

/**
 * desc:网球内页
 * Created by 107_tangrr on 2017/3/21 0021.
 */

public class TennisBallDetailsActivity extends BaseWebSocketActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {

    private final String TAG = "TennisBallDetailsActivity";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public ExactSwipeRefreshLayout refreshLayout;
    private TabsAdapter tabsAdapter;
    private String mThirdId;

    private TennsiAnalysisBean tennsiAnalysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic("USER.topic.tennis.score");

        setContentView(R.layout.tennis_details_activity);

        mThirdId = getIntent().getStringExtra("thirdId");

        L.d(TAG,"mThirdId: " + mThirdId);

        initView();
        initData();
    }

    private void initData() {

        tennsiAnalysis = JSON.parseObject(getTestData(),TennsiAnalysisBean.class);

        L.d("Tennis" , "赛事名："+tennsiAnalysis.getData().getRecentMatch().getHomePlayerRecentMatch().getMatchList().get(0).getLeagueName());

    }

    private void initView() {
        refreshLayout = (ExactSwipeRefreshLayout) findViewById(R.id.tennis_details_refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.tabhost);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(this, StaticValues.REFRASH_OFFSET_END));

        tabLayout = (TabLayout) findViewById(R.id.tennis_details_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.tennis_details_view_pager);

        tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        tabsAdapter.setTitles(new String[]{
                getString(R.string.basket_analyze),
                getString(R.string.basket_alet),
                getString(R.string.basket_eur)});
        tabsAdapter.addFragments(TennisAnalysisFrag.newInstance(mThirdId));
        tabsAdapter.addFragments(TennisPlateFrag.newInstance(mThirdId));
        tabsAdapter.addFragments(TennisEurFrag.newInstance(mThirdId));

        viewPager.setOffscreenPageLimit(3);//设置预加载页面的个数。
        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        findViewById(R.id.tennis_details_back).setOnClickListener(this);
    }

    @Override
    protected void onTextResult(String text) {
        L.d(TAG,"网球内页推送：" + text);


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
        switch (view.getId()){
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
    private String getTestData(){
        return "{\"result\":\"200\",\"data\":{\"recentMatch\":{\"homePlayerRecentMatch\":{\"playerFail\":0,\"playerWin\":3,\"totalTimes\":3,\"winRate\":\"100.00%\",\"matchList\":[{\"startDate\":\"2017-03-02\",\"startTime\":\"01:00\",\"leagueName\":\"迪拜公开赛(ATP)\",\"role\":\"10\",\"siteType\":\"11\",\"homePlayer1\":{\"name\":\"H.孔蒂宁\",\"id\":null},\"homePlayer2\":{\"name\":\"J·皮亚斯\",\"id\":null},\"guestPlayer1\":{\"name\":\"伊文斯\",\"id\":null},\"guestPlayer2\":{\"name\":\"G·穆勒\",\"id\":null},\"matchScore\":{\"homeSetScore1\":7,\"homeSetScore2\":6,\"homeSetScore3\":0,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":6,\"awaySetScore2\":4,\"awaySetScore3\":0,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeTotalScore\":2,\"awayTotalScore\":0,\"homeCurrentScore\":null,\"awayCurrentScore\":null,\"currentTimes\":2}},{\"startDate\":\"2017-03-02\",\"startTime\":\"01:00\",\"leagueName\":\"迪拜公开赛(ATP)\",\"role\":\"3\",\"siteType\":\"11\",\"homePlayer1\":{\"name\":\"H.孔蒂宁\",\"id\":null},\"homePlayer2\":{\"name\":\"J·皮亚斯\",\"id\":null},\"guestPlayer1\":{\"name\":\"伊文斯\",\"id\":null},\"guestPlayer2\":{\"name\":\"G·穆勒\",\"id\":null},\"matchScore\":{\"homeSetScore1\":7,\"homeSetScore2\":6,\"homeSetScore3\":0,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":6,\"awaySetScore2\":4,\"awaySetScore3\":0,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeTotalScore\":2,\"awayTotalScore\":0,\"homeCurrentScore\":null,\"awayCurrentScore\":null,\"currentTimes\":2}},{\"startDate\":\"2017-02-27\",\"startTime\":\"22:45\",\"leagueName\":\"迪拜公开赛(ATP)\",\"role\":\"10\",\"siteType\":\"11\",\"homePlayer1\":{\"name\":\"伊文斯\",\"id\":null},\"homePlayer2\":{\"name\":\"G·穆勒\",\"id\":null},\"guestPlayer1\":{\"name\":\"A.梅利\",\"id\":null},\"guestPlayer2\":{\"name\":\"泽蒙季奇\",\"id\":null},\"matchScore\":{\"homeSetScore1\":6,\"homeSetScore2\":7,\"homeSetScore3\":0,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":1,\"awaySetScore2\":6,\"awaySetScore3\":0,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeTotalScore\":2,\"awayTotalScore\":0,\"homeCurrentScore\":null,\"awayCurrentScore\":null,\"currentTimes\":2}}]},\"guestPlayerRecentMatch\":{\"playerFail\":0,\"playerWin\":3,\"totalTimes\":3,\"winRate\":\"100.00%\",\"matchList\":[{\"startDate\":\"2017-02-27\",\"startTime\":\"22:45\",\"leagueName\":\"迪拜公开赛(ATP)\",\"role\":\"10\",\"siteType\":\"11\",\"homePlayer1\":{\"name\":\"伊文斯\",\"id\":null},\"homePlayer2\":{\"name\":\"G·穆勒\",\"id\":null},\"guestPlayer1\":{\"name\":\"A.梅利\",\"id\":null},\"guestPlayer2\":{\"name\":\"泽蒙季奇\",\"id\":null},\"matchScore\":{\"homeSetScore1\":6,\"homeSetScore2\":7,\"homeSetScore3\":0,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":1,\"awaySetScore2\":6,\"awaySetScore3\":0,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeTotalScore\":2,\"awayTotalScore\":0,\"homeCurrentScore\":null,\"awayCurrentScore\":null,\"currentTimes\":2}},{\"startDate\":\"2014-12-30\",\"startTime\":\"22:30\",\"leagueName\":\"卡塔尔站(ATP)\",\"role\":\"10\",\"siteType\":\"11\",\"homePlayer1\":{\"name\":\"穆雷\",\"id\":null},\"homePlayer2\":{\"name\":\"泽蒙季奇\",\"id\":null},\"guestPlayer1\":{\"name\":\"布兰德斯\",\"id\":null},\"guestPlayer2\":{\"name\":\"梅耶尔\",\"id\":null},\"matchScore\":{\"homeSetScore1\":3,\"homeSetScore2\":7,\"homeSetScore3\":10,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":6,\"awaySetScore2\":6,\"awaySetScore3\":8,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeTotalScore\":2,\"awayTotalScore\":1,\"homeCurrentScore\":null,\"awayCurrentScore\":null,\"currentTimes\":3}},{\"startDate\":\"2014-01-02\",\"startTime\":\"01:30\",\"leagueName\":\"卡塔尔站(ATP)\",\"role\":\"3\",\"siteType\":\"11\",\"homePlayer1\":{\"name\":\"A·皮亚\",\"id\":null},\"homePlayer2\":{\"name\":\"布鲁诺-苏雷斯\",\"id\":null},\"guestPlayer1\":{\"name\":\"穆雷\",\"id\":null},\"guestPlayer2\":{\"name\":\"泽蒙季奇\",\"id\":null},\"matchScore\":{\"homeSetScore1\":7,\"homeSetScore2\":6,\"homeSetScore3\":0,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":6,\"awaySetScore2\":4,\"awaySetScore3\":0,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeTotalScore\":2,\"awayTotalScore\":0,\"homeCurrentScore\":null,\"awayCurrentScore\":null,\"currentTimes\":2}}]}},\"rankScore\":[{\"ranking\":\"61\",\"oldRanking\":\"61\",\"rankingChange\":\"0\",\"normalPlayerId\":\"321268\",\"playerName\":\"泽蒙季奇\",\"nationality\":\"塞尔维亚\",\"totalIntegral\":\"1300\",\"numberOfEntries\":\"24\"},{\"ranking\":\"153\",\"oldRanking\":\"153\",\"rankingChange\":\"0\",\"normalPlayerId\":\"319382\",\"playerName\":\"G·穆勒\",\"nationality\":\"卢森堡\",\"totalIntegral\":\"495\",\"numberOfEntries\":\"13\"}],\"dataCompare\":[{\"home1\":\"伊文斯\",\"home2\":\"G·穆勒\",\"guest1\":\"A.梅利\",\"guest2\":\"泽蒙季奇\",\"status\":0},{\"home1\":\"66%\",\"home2\":\"63%\",\"guest1\":\"56%\",\"guest2\":null,\"status\":1},{\"home1\":\"54%\",\"home2\":\"75%\",\"guest1\":\"75%\",\"guest2\":null,\"status\":2},{\"home1\":\"30%\",\"home2\":\"52%\",\"guest1\":\"52%\",\"guest2\":null,\"status\":3},{\"home1\":\"30%\",\"home2\":\"30%\",\"guest1\":\"43%\",\"guest2\":null,\"status\":4},{\"home1\":\"100%\",\"home2\":\"23%\",\"guest1\":\"42%\",\"guest2\":null,\"status\":5},{\"home1\":\"57%\",\"home2\":\"61%\",\"guest1\":\"69%\",\"guest2\":null,\"status\":6},{\"home1\":\"4\",\"home2\":\"16\",\"guest1\":\"9\",\"guest2\":null,\"status\":7},{\"home1\":\"5\",\"home2\":\"4\",\"guest1\":\"3\",\"guest2\":null,\"status\":8},{\"home1\":\"28\",\"home2\":\"36\",\"guest1\":\"28\",\"guest2\":null,\"status\":9},{\"home1\":\"15\",\"home2\":\"44\",\"guest1\":\"41\",\"guest2\":null,\"status\":10}],\"matchRecord\":{\"playerFail\":0,\"playerWin\":1,\"totalTimes\":1,\"winRate\":\"100.00%\",\"matchList\":[{\"startDate\":\"2017-02-27\",\"startTime\":\"22:45\",\"leagueName\":\"迪拜公开赛(ATP)\",\"role\":\"10\",\"siteType\":\"11\",\"homePlayer1\":{\"name\":\"伊文斯\",\"id\":null},\"homePlayer2\":{\"name\":\"G·穆勒\",\"id\":null},\"guestPlayer1\":{\"name\":\"A.梅利\",\"id\":null},\"guestPlayer2\":{\"name\":\"泽蒙季奇\",\"id\":null},\"matchScore\":{\"homeSetScore1\":6,\"homeSetScore2\":7,\"homeSetScore3\":0,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":1,\"awaySetScore2\":6,\"awaySetScore3\":0,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeTotalScore\":2,\"awayTotalScore\":0,\"homeCurrentScore\":null,\"awayCurrentScore\":null,\"currentTimes\":2}}]},\"matchInfo\":{\"leagueId\":\"323442\",\"leagueName\":\"ATP迪拜公开赛\",\"matchId\":\"848814040\",\"matchName\":\"伊文斯/G·穆勒 VS A.梅利/泽蒙季奇\",\"startTime\":\"22:45:00\",\"startDate\":\"2017-02-27\",\"roundName\":\"第一轮\",\"matchType\":\"3\",\"dataType\":\"2\",\"matchStatus\":\"-1\",\"homePlayer1\":{\"name\":\"伊文斯\",\"id\":\"319381\"},\"homePlayer2\":{\"name\":\"G·穆勒\",\"id\":\"319382\"},\"guestPlayer1\":{\"name\":\"A.梅利\",\"id\":\"319521\"},\"guestPlayer2\":{\"name\":\"泽蒙季奇\",\"id\":\"321268\"},\"matchScore\":{\"homeSetScore1\":6,\"homeSetScore2\":7,\"homeSetScore3\":0,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":1,\"awaySetScore2\":6,\"awaySetScore3\":0,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeTotalScore\":2,\"awayTotalScore\":0,\"homeCurrentScore\":null,\"awayCurrentScore\":null,\"currentTimes\":2}}}}";
    }
}
