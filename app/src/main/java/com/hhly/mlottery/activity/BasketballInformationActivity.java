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
import com.hhly.mlottery.frame.basketballframe.BasketInfomationFragment;

/**
 * @author wang gang
 * @date 2016/7/14 17:07
 * @des 篮球资料库
 */
public class BasketballInformationActivity extends BaseActivity implements View.OnClickListener {
    private static final int HOT = 0;
    private static final int EUR = 1;
    private static final int AMERICA = 2;
    private static final int ASIA = 3;
    private static final int AFRICA = 4;
    private static final int INTER = 5;


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


        publicTxtTitle.setText("篮球资料库");
        publicBtnFilter.setVisibility(View.GONE);

        publicBtnSet.setImageResource(R.mipmap.info_search);

        fragmentManager = getSupportFragmentManager();

        String[] titles = {"热门", "欧洲", "美洲", "亚洲", "非洲", "国际"};
        mTabsAdapter = new TabsAdapter(fragmentManager);


        mTabsAdapter.setTitles(titles);
        mTabsAdapter.addFragments(BasketInfomationFragment.newInstance(HOT), BasketInfomationFragment.newInstance(EUR), BasketInfomationFragment.newInstance(AMERICA), BasketInfomationFragment.newInstance(ASIA), BasketInfomationFragment.newInstance(AFRICA), BasketInfomationFragment.newInstance(INTER));
        viewpager.setOffscreenPageLimit(1);//设置预加载页面的个数。
        viewpager.setAdapter(mTabsAdapter);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back:
                finish();
                break;
            case R.id.public_btn_set:
                break;
        }
    }

}
