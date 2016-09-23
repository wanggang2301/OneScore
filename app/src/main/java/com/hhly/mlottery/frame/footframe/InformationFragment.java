package com.hhly.mlottery.frame.footframe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballActivity;
import com.hhly.mlottery.activity.FootballInformationSerachActivity;
import com.hhly.mlottery.adapter.football.TabsAdapter;

/**
 * @ClassName: OneScore
 * @author:Administrator luyao
 * @Description: 足球数据
 * @data: 2016/3/29 16:30
 */
public class InformationFragment extends Fragment implements View.OnClickListener {

    private static final int HOT = 0;            //热门
    private static final int EUR = 1;         //欧洲
    private static final int AMERICA = 2;    //美洲
    private static final int ASIA = 3;          //亚洲
    private static final int AFRICA = 4;      //非洲
    private static final int OCEANIA = 5;      //大洋洲
    private static final int INTER = 6;         //国际

    private ImageView publicImgBack;
    private TextView publicTxtTitle;
    private ImageView publicBtnSet;
    private ImageView publicBtnFilter;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private TabsAdapter mTabsAdapter;
    private FragmentManager fragmentManager;


    private View mView;

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_basket_information, container, false);
        mContext = getActivity();
        initView();
        return mView;

    }

    private void initView() {

        publicImgBack = (ImageView) mView.findViewById(R.id.public_img_back);
        publicBtnSet = (ImageView) mView.findViewById(R.id.public_btn_set);
        publicBtnFilter = (ImageView) mView.findViewById(R.id.public_btn_filter);
        publicTxtTitle = (TextView) mView.findViewById(R.id.public_txt_title);
        tabLayout = (TabLayout) mView.findViewById(R.id.tab_layout);
        viewpager = (ViewPager) mView.findViewById(R.id.viewpager);

        publicImgBack.setOnClickListener(this);
        publicBtnSet.setOnClickListener(this);

        publicBtnSet.setVisibility(View.VISIBLE); //模糊搜索隐藏

        publicBtnSet.setOnClickListener(this);

        publicTxtTitle.setText(mContext.getResources().getString(R.string.football_info_title));
        publicBtnFilter.setVisibility(View.GONE);

        publicBtnSet.setImageResource(R.mipmap.info_search);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back:
                ((FootballActivity) mContext).finish();
                break;
            case R.id.public_btn_set:
                Intent intent = new Intent(mContext, FootballInformationSerachActivity.class);
                startActivity(intent);
                break;
        }
    }
}



