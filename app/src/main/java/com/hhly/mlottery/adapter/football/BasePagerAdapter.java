package com.hhly.mlottery.adapter.football;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wang gang
 * @date 2016/6/2 18:29
 * @des ${}
 */
public class BasePagerAdapter extends FragmentStatePagerAdapter {

    protected List<Fragment> mFragments;

    public BasePagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    public void addFragments(List<Fragment> fragments) {
        mFragments.addAll(fragments);
    }

    public void addFragments(Fragment... fragments) {
        Collections.addAll(mFragments, fragments);
    }
}