package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.TabsAdapter;
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
    private ExactSwipeRefreshLayout refreshLayout;
    private TabsAdapter tabsAdapter;
    private String mThirdId;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic("USER.topic.tennis.score");

        setContentView(R.layout.tennis_details_activity);

        mThirdId = getIntent().getStringExtra("thirdId");

        L.d(TAG,"mThirdId: " + mThirdId);

        initView();
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
}
