package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.bean.tennisball.TennisSocketBean;
import com.hhly.mlottery.bean.tennisball.datails.analysis.MatchInfoBean;
import com.hhly.mlottery.bean.tennisball.datails.analysis.MatchScoreBean;
import com.hhly.mlottery.bean.tennisball.datails.analysis.TennisAnalysisBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BaseUserTopics;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.tennisfrag.datailsfrag.TennisAnalysisFrag;
import com.hhly.mlottery.frame.tennisfrag.datailsfrag.TennisEurFrag;
import com.hhly.mlottery.frame.tennisfrag.datailsfrag.TennisPlateFrag;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * desc:网球内页
 * Created by 107_tangrr on 2017/3/21 0021.
 */

public class TennisBallDetailsActivity extends BaseWebSocketActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private final String TAG = "TennisBallDetailsActivity";
    private final int ERROR = 3;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public ExactSwipeRefreshLayout refreshLayout;
    private TabsAdapter tabsAdapter;
    private TextView tv_match_name, tv_date, tv_time, tv_start, tv_home_name, tv_home_name2, tv_home_total_score,
            tv_guest_total_score, tv_guest_name, tv_guest_name2, tv_home_score1, tv_home_score2, tv_home_score3,
            tv_home_score4, tv_home_score5, tv_guest_score1, tv_guest_score2, tv_guest_score3, tv_guest_score4, tv_guest_score5;

    private String mThirdId;
    private TennisAnalysisFrag tennisAnalysisFrag; // 分析
    private TennisPlateFrag tennisPlateFrag; // 亚盘
    private TennisEurFrag tennisEurFrag; // 欧赔

    private boolean isSingle;// 是否单人比赛


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setWebSocketUri(BaseURLs.WS_SERVICE);
//        setTopic("USER.topic.tennis.score");
        setTopic(BaseUserTopics.tennisScore);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tennis_details_activity);

        mThirdId = getIntent().getStringExtra("thirdId");

        initView();
        initData();
    }

    private void initData() {

        refreshLayout.setRefreshing(true);

        Map<String, String> map = new HashMap<>();
        map.put("matchIds", mThirdId);

        VolleyContentFast.requestJsonByGet(BaseURLs.TENNIS_DATAILS_URL, map, new VolleyContentFast.ResponseSuccessListener<TennisAnalysisBean>() {
            @Override
            public void onResponse(TennisAnalysisBean json) {
                connectWebSocket();
                refreshLayout.setRefreshing(false);
                tennisAnalysisFrag.updataChange(json.getData());
                setDataShow(json.getData(), 0);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                refreshLayout.setRefreshing(false);
                tennisAnalysisFrag.setStatus(ERROR);
                ToastTools.showQuick(TennisBallDetailsActivity.this, getResources().getString(R.string.number_net_error));
            }
        }, TennisAnalysisBean.class);


    }

    // 设置数据展示
    private void setDataShow(TennisAnalysisBean.DataBean mData, int type) {
        if (mData == null || mData.getMatchInfo() == null) return;
        // type 0:加载数据 ;1:推送更新
        if (type == 0) {
            // 比赛类型： 1男子、2女子、3男双、4女双、5混双
            int matchType = mData.getMatchInfo().getMatchType();
            switch (matchType) {
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

            tv_match_name.setText(mData.getMatchInfo().getLeagueName() == null ? "" : mData.getMatchInfo().getLeagueName());
            tv_date.setText(DateUtil.convertDateToNation(mData.getMatchInfo().getStartDate()));
            String time = "";
            try {
                time = mData.getMatchInfo().getStartTime().substring(0, mData.getMatchInfo().getStartTime().lastIndexOf(":"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_time.setText(time);

            if (mData.getMatchInfo().getHomePlayer1() != null) {
                tv_home_name.setText(mData.getMatchInfo().getHomePlayer1().getName() == null ? "" : mData.getMatchInfo().getHomePlayer1().getName());
            } else {
                tv_home_name.setText("");
            }
            if (mData.getMatchInfo().getGuestPlayer1() != null) {
                tv_guest_name.setText(mData.getMatchInfo().getGuestPlayer1().getName() == null ? "" : mData.getMatchInfo().getGuestPlayer1().getName());
            } else {
                tv_guest_name.setText("");
            }

            if (isSingle) {
                // 单人赛
                tv_home_name2.setVisibility(View.GONE);
                tv_guest_name2.setVisibility(View.GONE);
            } else {
                // 双人赛
                tv_home_name2.setVisibility(View.VISIBLE);
                tv_guest_name2.setVisibility(View.VISIBLE);
                if (mData.getMatchInfo().getHomePlayer2() != null) {
                    tv_home_name2.setText(mData.getMatchInfo().getHomePlayer2().getName() == null ? "" : mData.getMatchInfo().getHomePlayer2().getName());
                } else {
                    tv_home_name2.setText("");
                }
                if (mData.getMatchInfo().getGuestPlayer2() != null) {
                    tv_guest_name2.setText(mData.getMatchInfo().getGuestPlayer2().getName() == null ? "" : mData.getMatchInfo().getGuestPlayer2().getName());
                } else {
                    tv_guest_name2.setText("");
                }
            }
        }
        // -6 P2退赛,-5 P1退赛,-4 待定,-3 推迟,-2 中断,-1 完,0 未开始,>0 进行中
        switch (mData.getMatchInfo().getMatchStatus()) {
            case -6:
                tv_start.setText(getString(R.string.tennis_match_p2));
                tv_start.setTextColor(getResources().getColor(R.color.tennis_details_analysis_title_start2));
                break;
            case -5:
                tv_start.setText(getString(R.string.tennis_match_p1));
                tv_start.setTextColor(getResources().getColor(R.color.tennis_details_analysis_title_start2));
                break;
            case -4:
                tv_start.setText(getString(R.string.tennis_match_dd));
                tv_start.setTextColor(getResources().getColor(R.color.tennis_details_analysis_title_start2));
                break;
            case -3:
                tv_start.setText(getString(R.string.tennis_match_tc));
                tv_start.setTextColor(getResources().getColor(R.color.tennis_details_analysis_title_start2));
                break;
            case -2:
                tv_start.setText(getString(R.string.tennis_match_zd));
                tv_start.setTextColor(getResources().getColor(R.color.tennis_details_analysis_title_start2));
                break;
            case -1:
                tv_start.setText(getString(R.string.tennis_match_over));
                tv_start.setTextColor(getResources().getColor(R.color.tennis_details_analysis_title_start3));
                break;
            case 0:
                tv_start.setText(getString(R.string.tennis_match_not_start));
                tv_start.setTextColor(getResources().getColor(R.color.tennis_details_analysis_title_start));
                break;
            default:
                tv_start.setText(getString(R.string.tennis_match_join));
                tv_start.setTextColor(getResources().getColor(R.color.tennis_details_analysis_title_start2));
                break;
        }

        if (mData.getMatchInfo().getMatchScore() != null) {
            tv_home_total_score.setText(String.valueOf(mData.getMatchInfo().getMatchScore().getHomeTotalScore()));
            tv_guest_total_score.setText(String.valueOf(mData.getMatchInfo().getMatchScore().getAwayTotalScore()));

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

        } else {
            tv_home_total_score.setText("");
            tv_guest_total_score.setText("");
            tv_home_score1.setText("");
            tv_guest_score1.setText("");
            tv_home_score2.setText("");
            tv_guest_score2.setText("");
            tv_home_score3.setText("");
            tv_guest_score3.setText("");
            tv_home_score4.setText("");
            tv_guest_score4.setText("");
            tv_home_score5.setText("");
            tv_guest_score5.setText("");
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
        tennisPlateFrag = TennisPlateFrag.newInstance(mThirdId);
        tennisEurFrag = TennisEurFrag.newInstance(mThirdId);

        tabsAdapter.addFragments(tennisAnalysisFrag);
        tabsAdapter.addFragments(tennisPlateFrag);
        tabsAdapter.addFragments(tennisEurFrag);
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
    protected void onTextResult(final String text) {
        L.d(TAG, "网球内页推送：" + text);
        final String jsonData = text;
        new Thread() {
            @Override
            public void run() {
                try {
                    TennisSocketBean tennisSocketBean = JSON.parseObject(jsonData, TennisSocketBean.class);
                    if (tennisSocketBean.getType() == 401) {
                        if (mThirdId.equals(tennisSocketBean.getDataObj().getMatchId())) {
                            final TennisAnalysisBean.DataBean data = new TennisAnalysisBean.DataBean();
                            MatchInfoBean matchInfoBean = new MatchInfoBean();
                            MatchScoreBean matchScoreBean = new MatchScoreBean();
                            matchInfoBean.setMatchStatus(tennisSocketBean.getDataObj().getMatchStatus());
                            matchScoreBean.setHomeSetScore1(tennisSocketBean.getDataObj().getMatchScore().getHomeSetScore1());
                            matchScoreBean.setHomeSetScore2(tennisSocketBean.getDataObj().getMatchScore().getHomeSetScore2());
                            matchScoreBean.setHomeSetScore3(tennisSocketBean.getDataObj().getMatchScore().getHomeSetScore3());
                            matchScoreBean.setHomeSetScore4(tennisSocketBean.getDataObj().getMatchScore().getHomeSetScore4());
                            matchScoreBean.setHomeSetScore5(tennisSocketBean.getDataObj().getMatchScore().getHomeSetScore5());
                            matchScoreBean.setAwaySetScore1(tennisSocketBean.getDataObj().getMatchScore().getAwaySetScore1());
                            matchScoreBean.setAwaySetScore2(tennisSocketBean.getDataObj().getMatchScore().getAwaySetScore2());
                            matchScoreBean.setAwaySetScore3(tennisSocketBean.getDataObj().getMatchScore().getAwaySetScore3());
                            matchScoreBean.setAwaySetScore4(tennisSocketBean.getDataObj().getMatchScore().getAwaySetScore4());
                            matchScoreBean.setAwaySetScore5(tennisSocketBean.getDataObj().getMatchScore().getAwaySetScore5());
                            matchScoreBean.setHomeTotalScore(tennisSocketBean.getDataObj().getMatchScore().getHomeTotalScore());
                            matchScoreBean.setAwayTotalScore(tennisSocketBean.getDataObj().getMatchScore().getAwayTotalScore());
                            matchInfoBean.setMatchScore(matchScoreBean);
                            data.setMatchInfo(matchInfoBean);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    L.d("tennis", "网球内页收到了：刷新了!");
                                    setDataShow(data, 1);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
        initData();
        tennisPlateFrag.initData();
        tennisEurFrag.initData();
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
        closeWebSocket();
    }
}
