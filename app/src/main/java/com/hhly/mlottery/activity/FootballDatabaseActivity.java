package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.frame.footframe.FootballDatabaseFragment;

/**
 * @author: Wangg
 * @Name：FootballDatabaseActivity
 * @Description: 足球资料库入口
 * @Created on:2016/9/1  15:00.
 */
public class FootballDatabaseActivity extends BaseActivity implements View.OnClickListener {
    private static final int HOT = 0;            //热门
    private static final int EUR = 1;         //欧洲
    private static final int AMERICA = 2;    //美洲
    private static final int ASIA = 3;          //亚洲
    private static final int AFRICA = 4;      //非洲
    private static final int OCEANIA = 5;      //大洋洲
    private static final int INTER = 6;         //国际

    private ImageView publicImgBack;
    private TextView publicTxtTitle;
    private ImageView publicBtnSet;
    private ImageView publicBtnFilter;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private TabsAdapter mTabsAdapter;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_information);
        initView();
    }

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

        publicTxtTitle.setText(getApplicationContext().getResources().getString(R.string.football_info_title));
        publicBtnFilter.setVisibility(View.GONE);

        publicBtnSet.setImageResource(R.mipmap.info_search);

        fragmentManager = getSupportFragmentManager();

        String[] titles = getApplicationContext().getResources().getStringArray(R.array.football_info_tabs);
        mTabsAdapter = new TabsAdapter(fragmentManager);
        mTabsAdapter.setTitles(titles);
        mTabsAdapter.addFragments(FootballDatabaseFragment.newInstance(HOT),
                FootballDatabaseFragment.newInstance(EUR),
                FootballDatabaseFragment.newInstance(AMERICA),
                FootballDatabaseFragment.newInstance(ASIA),
                FootballDatabaseFragment.newInstance(AFRICA),
                FootballDatabaseFragment.newInstance(OCEANIA),
                FootballDatabaseFragment.newInstance(INTER));
        viewpager.setOffscreenPageLimit(1);//设置预加载页面的个数。
        viewpager.setAdapter(mTabsAdapter);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back:
                finish();
                break;
            case R.id.public_btn_set:
              //  Intent intent = new Intent(getApplicationContext(), BasketballInformationSerachActivity.class);
              //  startActivity(intent);
                break;
        }
    }
}
