package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.oddfragment.CPIOddsFragment;
import com.hhly.mlottery.frame.oddfragment.CpiDetailsFragment;
import com.hhly.mlottery.util.DeviceInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 103TJL on 2016/5/6.
 * 新版指数详情
 */
public class CpiDetailsActivity extends BaseActivity implements View.OnClickListener {
    private TextView public_txt_title, public_txt_left_title;//标题
    private ImageView public_img_back,public_btn_set,public_img_filter;
    private TextView mTab1, mTab2, mTab3;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    private CpiDetailsFragmentAdapter mCpiDetailsFragmentAdapter;
    //记录是否完成viewpager初始化
    private boolean isInitViewPager = false;
    private List obList;
    private String comId;
    private String positionNunber;
    private String stType;
    //球队名称和比分
    private String PKScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cpi_details_activity);
        Intent joinIntent=getIntent();
        obList = (List<Map<String,String>>)getIntent().getSerializableExtra("obListEntity");
        comId = joinIntent.getStringExtra("comId");
        positionNunber = joinIntent.getStringExtra("positionNunber");
        stType = joinIntent.getStringExtra("stType");
        PKScore = joinIntent.getStringExtra("PKScore");
        cpiInitView();
        initViewPager();

    }

    private void cpiInitView(){
        //中心标题
        public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText(PKScore);
        public_txt_title.setTextSize(13);
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
    }

    //初始化viewPager
    private void initViewPager() {
        mTab1 = (TextView) findViewById(R.id.cpi_match_detail_tab1);
        mTab2 = (TextView) findViewById(R.id.cpi_match_detail_tab2);
        mTab3 = (TextView) findViewById(R.id.cpi_match_detail_tab3);
        mTab1.setOnClickListener(this);
        mTab2.setOnClickListener(this);
        mTab3.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.cpi_details_viewpager);
        fragments = new ArrayList<>();
        //亚盘
        fragments.add(CpiDetailsFragment.newInstance("1", obList,comId,positionNunber,stType));
        //大小
        fragments.add(CpiDetailsFragment.newInstance("3", obList,comId,positionNunber,stType));
        //欧赔
        fragments.add(CpiDetailsFragment.newInstance("2", obList,comId,positionNunber,stType));

//        getChildFragmentManager
        mCpiDetailsFragmentAdapter = new CpiDetailsFragmentAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(2);//设置预加载页面的个数。
        mViewPager.setAdapter(mCpiDetailsFragmentAdapter);
        //根据标识界面传过来的值默认选中那个界面
        if ("palte".equals(stType)){
            mViewPager.setCurrentItem(0);
        }else if ("big".equals(stType)){
            mViewPager.setCurrentItem(1);
        }else if("op".equals(stType)){
            mViewPager.setCurrentItem(2);
        }
        final LinearLayout tabLine = (LinearLayout) findViewById(R.id.cpi_match_detail_tabline);
        final LinearLayout tabLineLayout = (LinearLayout) findViewById(R.id.cpi_match_detail_tabline_layout);
        int displayWidth = DeviceInfo.getDisplayWidth(mContext);
        tabLine.setLayoutParams(new LinearLayout.LayoutParams(displayWidth / 3, LinearLayout.LayoutParams.WRAP_CONTENT));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int lineWidth = tabLine.getWidth();
                int marginLeft = (int) (lineWidth * (position + positionOffset));
                tabLineLayout.setPadding(marginLeft, 0, 0, 0);
            }

            @Override
            public void onPageSelected(int position) {
                mTab1.setTextColor(getResources().getColor(R.color.white));
                mTab2.setTextColor(getResources().getColor(R.color.white));
                mTab3.setTextColor(getResources().getColor(R.color.white));

                switch (position) {
                    case 0:
                        mTab1.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 1:
                        mTab2.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 2:
                        mTab3.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //标记是否初始化
        isInitViewPager = true;


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

    public class CpiDetailsFragmentAdapter extends FragmentPagerAdapter {


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
    }

}
