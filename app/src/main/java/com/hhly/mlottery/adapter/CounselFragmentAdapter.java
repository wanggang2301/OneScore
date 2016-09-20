package com.hhly.mlottery.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author
 * @ClassName:
 * @Description: 资讯viewpager的适配器
 * @date
 */
public class CounselFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mList;
    private FragmentManager mFragmentManager;
    private List<String> mTitles;
    private Context mContext;

    public CounselFragmentAdapter(FragmentManager fm, List<Fragment> list,List<String> mTitles,Context mContext) {
        super(fm);
        this.mFragmentManager = fm;
        this.mList = list;
        this.mTitles=mTitles;
        this.mContext=mContext;
    }

    @Override
    public Fragment getItem(int position) {
//        mFragmentManager.beginTransaction().show(mList.get(position));
        return mList.get(position);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
    @Override
    public int getCount() {
        return mList.size();
    }
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//
//        mFragmentManager.beginTransaction().hide(mList.get(position));
//    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
