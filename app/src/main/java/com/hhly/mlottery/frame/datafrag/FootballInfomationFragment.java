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
import com.hhly.mlottery.frame.footframe.FootballDatabaseFragment;
import com.hhly.mlottery.frame.footframe.FootballDatabaseInterFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FootballInfomationFragment extends Fragment {

    private static final int HOT = 0;            //热门
    private static final int EUR = 1;         //欧洲
    private static final int AMERICA = 2;    //美洲
    private static final int ASIA = 3;          //亚洲
    private static final int AFRICA = 4;      //非洲
    private static final int OCEANIA = 5;      //大洋洲
    private static final int INTER = 6;         //国际


    private TabLayout tabLayout;
    private ViewPager viewpager;
    private TabsAdapter mTabsAdapter;
    private FragmentManager fragmentManager;


    private View mView;

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_football_infomation, container, false);
        mContext = getActivity();
        initView();
        return mView;

    }

    private void initView() {


        tabLayout = (TabLayout) mView.findViewById(R.id.tab_layout);
        viewpager = (ViewPager) mView.findViewById(R.id.viewpager);

        fragmentManager = getChildFragmentManager();

        String[] titles = mContext.getResources().getStringArray(R.array.football_info_tabs);
        mTabsAdapter = new TabsAdapter(fragmentManager);
        mTabsAdapter.setTitles(titles);
        mTabsAdapter.addFragments(FootballDatabaseFragment.newInstance(HOT),
                FootballDatabaseFragment.newInstance(EUR),
                FootballDatabaseFragment.newInstance(AMERICA),
                FootballDatabaseFragment.newInstance(ASIA),
                FootballDatabaseFragment.newInstance(AFRICA),
                FootballDatabaseFragment.newInstance(OCEANIA),
                FootballDatabaseInterFragment.newInstance(INTER));
        viewpager.setOffscreenPageLimit(1);//设置预加载页面的个数。
        viewpager.setAdapter(mTabsAdapter);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }
}

