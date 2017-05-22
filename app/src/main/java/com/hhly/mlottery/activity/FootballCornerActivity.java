package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.footballframe.corner.CornerFragment;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author: mdy
 * @Name：BallType
 * @Description:角球页面
 * @Created on:2017/5/17  11:33.
 */
public class FootballCornerActivity extends BaseActivity {

    @BindView(R.id.public_txt_title)
    TextView mTitle;
    @BindView(R.id.public_img_back)
    ImageView mBack;
    @BindView(R.id.public_btn_filter)
    ImageView mFilter;
    @BindView(R.id.public_btn_set)
    ImageView mSetButton;
    @BindView(R.id.football_corner_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.corner_viewpager)
    ViewPager mViewPager;
    TabsAdapter mTabsAdapter;
    @BindView(R.id.corner_refresh_layout)
    ExactSwipeRefreshLayout mRefreshLayout;
    String mTitles[];

    ArrayList<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_corner);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        mTitle.setText(R.string.corner_score);
        mFilter.setVisibility(View.GONE);
        mSetButton.setVisibility(View.GONE);
        mTitles=new String[]{getString(R.string.foot_event_corner)};
//        ,getString(R.string.foot_details_focus)

        fragments=new ArrayList();

        CornerFragment cornerFragment=CornerFragment.getInstance("0");
//        CornerFragment focusFragment=CornerFragment.getInstance("1");
        fragments.add(cornerFragment);
//        fragments.add(focusFragment);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        mTabsAdapter=new TabsAdapter(getSupportFragmentManager());
        mTabsAdapter.setTitles(mTitles);
        mTabsAdapter.addFragments(fragments);
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        focusCallback();
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
            }
        });

        mRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshing(false);
                        refreshAllChildFragments();
                    }
                }, 1000);
            }
        });

    }
    /**
     * 刷新每个子fragment
     */
    public void refreshAllChildFragments() {
        for (Fragment childFragment : fragments) {

            ((CornerFragment) childFragment).refreshDate();
        }
    }

    /**
     * 设置刷新状态
     *
     * @param b 是否正在刷新
     */
    public void setRefreshing(boolean b) {
        mRefreshLayout.setRefreshing(b);
    }

    public void focusCallback() {

//        String focusIds = PreferenceUtil.getString(StaticValues.CORNER_FOCUS_ID, "");
//        String[] arrayId = focusIds.split("[,]");
//
//            if ("".equals(focusIds) || arrayId.length == 0) {
//                mTabLayout.getTabAt(1).setText(getResources().getString(R.string.foot_guanzhu_txt));
//            } else {
//                mTabLayout.getTabAt(1).setText(getResources().getString(R.string.foot_guanzhu_txt) + "(" + arrayId.length + ")");
//            }

    }
}
