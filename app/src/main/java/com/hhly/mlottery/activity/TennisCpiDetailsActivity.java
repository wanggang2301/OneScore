package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.tennisfrag.oddfragment.TennisCpiDetailsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 网球指数详情界面Activity
 */
public class TennisCpiDetailsActivity extends BaseActivity implements View.OnClickListener {
    private final String ARG_ODDTYPE = "oddType";
    private final String ARG_THIRDID = "thirdId";
    private final String ARG_INDEX = "index";
    private final String ARG_LEFT_NAME = "leftName";
    private final String ARG_COMPAN_NAME = "companName";

    private TextView public_txt_left_title;//标题
    private ImageView public_img_back, public_btn_set, public_img_filter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    private CpiDetailsFragmentAdapter mCpiDetailsFragmentAdapter;

    private ArrayList<String> nameList;
    private String oddType;
    private String thirdId;
    private int index;
    private String comName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cpi_details_activity);

        Intent joinIntent = getIntent();
        nameList = joinIntent.getStringArrayListExtra(ARG_LEFT_NAME);
        oddType = joinIntent.getStringExtra(ARG_ODDTYPE);
        thirdId = joinIntent.getStringExtra(ARG_THIRDID);
        comName = joinIntent.getStringExtra(ARG_COMPAN_NAME);
        index = joinIntent.getIntExtra(ARG_INDEX, 0);

        cpiInitView();
        initViewPager();
    }

    private void cpiInitView() {
        //左边标题
        public_txt_left_title = (TextView) findViewById(R.id.public_txt_left_title);
        public_txt_left_title.setVisibility(View.VISIBLE);
        public_txt_left_title.setText(R.string.football_detail_odds_tab);
        //返回
        public_img_back = (ImageView) findViewById(R.id.public_img_back);
        public_img_back.setOnClickListener(this);
        //设置
        public_btn_set = (ImageView) findViewById(R.id.public_btn_set);
        public_btn_set.setVisibility(View.GONE);
        public_img_filter = (ImageView) findViewById(R.id.public_btn_filter);
        public_img_filter.setVisibility(View.GONE);
        // 标题
        findViewById(R.id.public_txt_title).setVisibility(View.GONE);
    }

    //初始化viewPager
    private void initViewPager() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager = (ViewPager) findViewById(R.id.cpi_details_viewpager);
        fragments = new ArrayList<>();

        //亚盘
        fragments.add(TennisCpiDetailsFragment.newInstance("1", thirdId, index, nameList, comName));
        //大小球
        fragments.add(TennisCpiDetailsFragment.newInstance("3", thirdId, index, nameList, comName));
        //欧赔
        fragments.add(TennisCpiDetailsFragment.newInstance("2", thirdId, index, nameList, comName));

        mCpiDetailsFragmentAdapter = new CpiDetailsFragmentAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(2);//设置预加载页面的个数。
        mViewPager.setAdapter(mCpiDetailsFragmentAdapter);
        //根据标识界面传过来的值默认选中那个界面
        if ("1".equals(oddType)) {
            mViewPager.setCurrentItem(0);
        } else if ("3".equals(oddType)) {
            mViewPager.setCurrentItem(1);
        } else if ("2".equals(oddType)) {
            mViewPager.setCurrentItem(2);
        }

        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back:
                finish();
                break;
            case R.id.cpi_match_detail_tab1:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.cpi_match_detail_tab2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.cpi_match_detail_tab3:
                mViewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    class CpiDetailsFragmentAdapter extends FragmentPagerAdapter {


        private List<Fragment> mFragmentList;

        public CpiDetailsFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager);
            this.mFragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.odd_plate_rb_txt);
                case 1:
                    return getString(R.string.asiasize);
                case 2:
                    return getString(R.string.odd_op_rb_txt);
                default:
                    return getString(R.string.odd_plate_rb_txt);
            }
        }
    }
}
