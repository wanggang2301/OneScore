package com.hhly.mlottery.frame;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.oddfragment.CPIOddsListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 重构版 CPIFragment
 * <p>
 * Created by loshine on 2016/6/21.
 */
public class CPINewFragment extends Fragment {

    TextView mLeftTitle; // 左侧标题

    ImageView mBackButton; // 返回按钮

    LinearLayout mDateLayout; // 日期布局
    TextView mDateTextView; // 日期 TextView

    ImageView mCompanyButton, mFilterButton; // 公司和筛选按钮

    TabLayout mTabLayout; // tabLayout
    SwipeRefreshLayout mRefreshLayout; // SwipeRefreshLayout
    ViewPager mViewPager; // viewPage

    private List<CPIOddsListFragment> mFragments; // Fragments

    private String currentDate; // 当前日期
    private String choosenDate; // 选中日期

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cpi, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 隐藏中间标题
        hideView(view, R.id.public_txt_title);
        // 隐藏 筛选、设置、热门隐藏
        hideView(view, R.id.public_btn_filter);
        hideView(view, R.id.public_btn_set);
        hideView(view, R.id.public_img_hot);

        // 显示左侧标题
        mLeftTitle = (TextView) view.findViewById(R.id.public_txt_left_title);
        mLeftTitle.setVisibility(View.VISIBLE);
        mLeftTitle.setText(R.string.football_detail_odds_tab);

        // 返回
        mBackButton = (ImageView) view.findViewById(R.id.public_img_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

        // 显示时间的布局和 TextView
        mDateLayout = (LinearLayout) view.findViewById(R.id.public_date_layout);
        mDateTextView = (TextView) view.findViewById(R.id.public_txt_date);

        // 右上角公司和联赛筛选
        mCompanyButton = (ImageView) view.findViewById(R.id.public_img_company);
        mCompanyButton.setVisibility(View.VISIBLE);
        mCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 公司筛选
            }
        });
        mFilterButton = (ImageView) view.findViewById(R.id.public_img_filter);
        mFilterButton.setVisibility(View.VISIBLE);
        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 联赛筛选
            }
        });

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.cpi_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAllChildFragments();
            }
        });

        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.cpi_viewpager);

        initViewPager();

        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
                refreshAllChildFragments();
            }
        });
    }

    /**
     * 设置当前日期
     *
     * @param currentDate currentDate, 形如 2016-06-23
     */
    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
        if (TextUtils.isEmpty(choosenDate)) {
            mDateTextView.setText(currentDate);
            if (!mDateLayout.isShown()) mDateLayout.setVisibility(View.VISIBLE);
            if (!mDateTextView.isShown()) mDateTextView.setVisibility(View.VISIBLE);
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

    /**
     * 获取当前前台显示的 ViewPager 中的 Fragment
     *
     * @return CPIOddsListFragment
     */
    public CPIOddsListFragment getCurrentFragment() {
        return mFragments.get(mViewPager.getCurrentItem());
    }

    public void refreshAllChildFragments() {
//        mFragments.get(mViewPager.getCurrentItem()).refreshData(currentDate);
        for (CPIOddsListFragment fragment : mFragments) {
            // 第一次刷新的时候 currentDate 为 null，之后都不会为 null
            fragment.refreshData(currentDate);
        }
    }

    /**
     * 初始化 ViewPager
     */
    private void initViewPager() {
        mFragments = new ArrayList<>();
        mFragments.add(CPIOddsListFragment.newInstance(CPIOddsListFragment.TYPE_PLATE));
        mFragments.add(CPIOddsListFragment.newInstance(CPIOddsListFragment.TYPE_BIG));
        mFragments.add(CPIOddsListFragment.newInstance(CPIOddsListFragment.TYPE_OP));

        CPIPagerAdapter pagerAdapter = new CPIPagerAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(3);//设置预加载页面的个数。
        mViewPager.setAdapter(pagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void hideView(View view, @IdRes int idRes) {
        view.findViewById(idRes).setVisibility(View.GONE);
    }

    public static CPINewFragment newInstance() {
        return new CPINewFragment();
    }

    /**
     * ViewPager 适配器
     */
    class CPIPagerAdapter extends FragmentStatePagerAdapter {

        public CPIPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CPIOddsListFragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.odd_plate_rb_txt);
                case 1:
                    return getString(R.string.asiasize);
                case 2:
                    return getString(R.string.odd_op_rb_txt);
                default:
                    return getString(R.string.odd_plate_rb_txt);
            }
        }
    }
}
