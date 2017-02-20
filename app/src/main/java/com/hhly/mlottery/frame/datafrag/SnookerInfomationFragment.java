package com.hhly.mlottery.frame.datafrag;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.frame.snooker.SnookerMatchFragment;
import com.hhly.mlottery.frame.snooker.SnookerRankFragment;

/**
 * @author wangg
 * @des 斯洛克资料库
 * @date 2017/02/20
 */
public class SnookerInfomationFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private TabsAdapter mTabsAdapter;
    private FragmentManager fragmentManager;

    private View mView;

    private Context mContext;

    public SnookerInfomationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_snooker_infomation, container, false);
        mContext = getActivity();
        initView();
        return mView;
    }

    private void initView() {
        tabLayout = (TabLayout) mView.findViewById(R.id.tab_layout);
        viewpager = (ViewPager) mView.findViewById(R.id.viewpager);

        fragmentManager = getChildFragmentManager();
        String[] titles = mContext.getResources().getStringArray(R.array.snooker_data_tabs);
        mTabsAdapter = new TabsAdapter(fragmentManager);
        mTabsAdapter.setTitles(titles);
        mTabsAdapter.addFragments(SnookerRankFragment.newInatance(), SnookerMatchFragment.newInstance());
        viewpager.setOffscreenPageLimit(1);//设置预加载页面的个数。
        viewpager.setAdapter(mTabsAdapter);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
}