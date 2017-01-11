package com.hhly.mlottery.frame.numbersframe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.PureViewPagerAdapter;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:香港开奖统计fragment
 * Created by 107_tangrr on 2017/1/11 0011.
 */

public class HKLotteryStartFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final int LOADING = 0;
    private static final int SUCCESS = 1;
    private static final int ERROR = 2;
    private Context mContext;
    private View mView;
    private ExactSwipeRefreshLayout swipeRefreshLayout;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private List<String> titles;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.home_info_hk_start_pager, container, false);

        initView();
        initData();

        return mView;
    }

    private void initData() {

        mHandler.sendEmptyMessage(LOADING);

        VolleyContentFast.requestJsonByGet("url", null, new VolleyContentFast.ResponseSuccessListener<Object>() {
            @Override
            public void onResponse(Object jsonObject) {
                if(jsonObject != null){
                    mHandler.sendEmptyMessage(SUCCESS);
                }else{
                    mHandler.sendEmptyMessage(ERROR);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(ERROR);
            }
        }, null);
    }

    private void initView() {
        swipeRefreshLayout = (ExactSwipeRefreshLayout) mView.findViewById(R.id.swipere_fresh_layout_start);
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));
        mViewPager = (ViewPager) mView.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) mView.findViewById(R.id.start_tabs);

        fragments = new ArrayList<>();
        // TODO 此处传递后台数据
        fragments.add(StartPagerFragment.newInstance("特码出现"));
        fragments.add(StartPagerFragment.newInstance("平码出现"));
        fragments.add(StartPagerFragment.newInstance("特码未出现"));

        titles = new ArrayList<>();
        titles.add(mContext.getResources().getString(R.string.home_lottery_info_start_tab_tm));
        titles.add(mContext.getResources().getString(R.string.home_lottery_info_start_tab_pm));
        titles.add(mContext.getResources().getString(R.string.home_lottery_info_start_tab_notm));

        mViewPager.setAdapter(new PureViewPagerAdapter(fragments, titles, getChildFragmentManager()));
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    break;
                case SUCCESS:



                    break;
                case ERROR:
                    break;
            }
        }
    };

    @Override
    public void onRefresh() {

    }
}
