package com.hhly.mlottery.adapter.football;

import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Tabs With ViewPager 适配器
 * <p/>
 * Created by Administrator on 2016/4/29.
 */
public class TabsAdapter extends BasePagerAdapter {

    private List<Tab> mTabs;
    private String[] mTitles;




    public void setTitles(String[] titles) {
        mTitles = titles;
        if (mTabs.size() > 0) mTabs.clear();
        for (String title : mTitles) {
            mTabs.add(new Tab(title));
        }
    }

    public TabsAdapter(FragmentManager fm) {
        super(fm);
        mTabs = new ArrayList<>();


    }

    @Override
    public int getCount() {
        return mTabs == null ? 0 : mTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs == null ? "" : mTabs.get(position).getTitle();
    }
}
