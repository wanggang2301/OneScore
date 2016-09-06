package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.bean.basket.infomation.LeagueBean;
import com.hhly.mlottery.frame.basketballframe.BasketInformationFragment;

/**
 * @author wang gang
 * @date 2016/7/14 17:07
 * @des 篮球资料库入口列表Activity
 */
public class BasketballInformationActivity extends BaseActivity implements View.OnClickListener {
    private static final String HOT = "hot";            //热门
    private static final String EUR = "europe";         //欧洲
    private static final String AMERICA = "america";    //美洲
    private static final String ASIA = "asia";          //亚洲
    private static final String AFRICA = "africa";      //非洲
    private static final String INTER = "intl";         //国际

    private ImageView publicImgBack;
    private TextView publicTxtTitle;
    private ImageView publicBtnSet;
    private ImageView publicBtnFilter;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private TabsAdapter mTabsAdapter;
    private FragmentManager fragmentManager;
    private TextView mTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_information);
        initView();
    }

    private static final String LEAGUE = "league";
    private void initView() {

        publicImgBack = (ImageView) findViewById(R.id.public_img_back);
        publicBtnSet = (ImageView) findViewById(R.id.public_btn_set);
        publicBtnFilter = (ImageView) findViewById(R.id.public_btn_filter);
        publicTxtTitle = (TextView) findViewById(R.id.public_txt_title);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        publicImgBack.setOnClickListener(this);
        publicBtnSet.setOnClickListener(this);

        publicBtnSet.setVisibility(View.VISIBLE); //模糊搜索隐藏

        publicBtnSet.setOnClickListener(this);

        publicTxtTitle.setText(getApplicationContext().getResources().getString(R.string.basket_info_title));
        publicBtnFilter.setVisibility(View.GONE);

        publicBtnSet.setImageResource(R.mipmap.info_search);

        fragmentManager = getSupportFragmentManager();

        String[] titles = getApplicationContext().getResources().getStringArray(R.array.basket_info_tabs);
        mTabsAdapter = new TabsAdapter(fragmentManager);
        mTabsAdapter.setTitles(titles);
        mTabsAdapter.addFragments(BasketInformationFragment.newInstance(HOT),
                BasketInformationFragment.newInstance(EUR),
                BasketInformationFragment.newInstance(AMERICA),
                BasketInformationFragment.newInstance(ASIA),
                BasketInformationFragment.newInstance(AFRICA),
                BasketInformationFragment.newInstance(INTER));
        viewpager.setOffscreenPageLimit(1);//设置预加载页面的个数。
        viewpager.setAdapter(mTabsAdapter);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        mTest = (TextView) findViewById(R.id.test);
        mTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FootballDatabaseDetailsActivity.class);

                LeagueBean bean = new LeagueBean();
                bean.setMatchType(1);
                bean.setLeagueName("NBA");
                bean.setLeagueLogoUrl("http://pic.13322.com/basketball/league/120_120/1.png");
                bean.setLeagueId("1");

                intent.putExtra(LEAGUE, bean);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back:
                finish();
                break;
            case R.id.public_btn_set:
                Intent intent = new Intent(getApplicationContext(), BasketballInformationSerachActivity.class);
                startActivity(intent);
                break;
        }
    }

}
