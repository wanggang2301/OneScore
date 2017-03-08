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
import com.hhly.mlottery.frame.basketballframe.BasketInformationFragment;

/**
 * 篮球资料库.
 */
public class BasketballInfomationFragment extends Fragment {

    private static final String HOT = "hot";            //热门
    private static final String EUR = "europe";         //欧洲
    private static final String AMERICA = "america";    //美洲
    private static final String ASIA = "asia";          //亚洲
    private static final String AFRICA = "africa";      //非洲
    private static final String INTER = "intl";         //国际


    private TabLayout tabLayout;
    private ViewPager viewpager;
    private TabsAdapter mTabsAdapter;
    private FragmentManager fragmentManager;

    private View mView;

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basketball_infomation, container, false);
        mContext = getActivity();
        initView();
        return mView;
    }

    private void initView() {
        tabLayout = (TabLayout) mView.findViewById(R.id.tab_layout);
        viewpager = (ViewPager) mView.findViewById(R.id.viewpager);

        fragmentManager = getChildFragmentManager();

        String[] titles = mContext.getResources().getStringArray(R.array.basket_info_tabs);
        mTabsAdapter = new TabsAdapter(fragmentManager);
        mTabsAdapter.setTitles(titles);
        mTabsAdapter.addFragments(BasketInformationFragment.newInstance(HOT),
                BasketInformationFragment.newInstance(EUR),
                BasketInformationFragment.newInstance(AMERICA),
                BasketInformationFragment.newInstance(ASIA),
                BasketInformationFragment.newInstance(AFRICA),
                BasketInformationFragment.newInstance(INTER));
        viewpager.setOffscreenPageLimit(1);//设置预加载页面的个数。
        viewpager.setAdapter(mTabsAdapter);
        tabLayout.setupWithViewPager(viewpager);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }
}
