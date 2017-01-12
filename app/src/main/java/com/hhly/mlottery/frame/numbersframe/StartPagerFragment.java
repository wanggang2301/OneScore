package com.hhly.mlottery.frame.numbersframe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.PureViewPagerAdapter;
import com.hhly.mlottery.bean.numbersBean.CoedAppearBean;
import com.hhly.mlottery.bean.numbersBean.CoedNotAppearBean;
import com.hhly.mlottery.bean.numbersBean.LotteryInfoDateBean;
import com.hhly.mlottery.bean.numbersBean.NumberAppearBean;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.hhly.mlottery.R.id.content;

/**
 * desc:香港开奖统计详情fragment
 * Created by 107_tangrr on 2017/1/11 0011.
 */

public class StartPagerFragment extends Fragment {

    private static final String ZODIAC_KEY = "zodiacKey";
    private static final String NUMBER_KEY = "numberKey";
    private static final String MANTISSA_KEY = "mantissaKey";
    private static final String BO_KEY = "boKey";
    private static final String LABS = "labs";

    private Context mContext;
    private View mView;
    private List<LotteryInfoDateBean> mZodiacList;
    private List<LotteryInfoDateBean> mNumberList;
    private List<LotteryInfoDateBean> mMantissaList;
    private List<LotteryInfoDateBean> mBoList;
    private int mLabs;

    public static StartPagerFragment newInstance(int labs, List<LotteryInfoDateBean> zodiacList, List<LotteryInfoDateBean> numberList, List<LotteryInfoDateBean> mantissaList, List<LotteryInfoDateBean> boList) {
        StartPagerFragment fragment = new StartPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LABS, labs);
        bundle.putParcelableArrayList(ZODIAC_KEY, (ArrayList<? extends Parcelable>) zodiacList);
        bundle.putParcelableArrayList(NUMBER_KEY, (ArrayList<? extends Parcelable>) numberList);
        bundle.putParcelableArrayList(MANTISSA_KEY, (ArrayList<? extends Parcelable>) mantissaList);
        bundle.putParcelableArrayList(BO_KEY, (ArrayList<? extends Parcelable>) boList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mLabs = getArguments().getInt(LABS);
            mZodiacList = getArguments().getParcelableArrayList(ZODIAC_KEY);
            mNumberList = getArguments().getParcelableArrayList(NUMBER_KEY);
            mMantissaList = getArguments().getParcelableArrayList(MANTISSA_KEY);
            mBoList = getArguments().getParcelableArrayList(BO_KEY);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.home_info_hk_start_pager_children, container, false);

        initView();
        initData();

        return mView;
    }

    private void initData() {
        // TODO 由labs来判断当前页面的数据

    }

    private void initView() {

    }
}
