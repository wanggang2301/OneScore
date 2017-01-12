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
import com.hhly.mlottery.bean.numbersBean.CoedAppearBean;
import com.hhly.mlottery.bean.numbersBean.CoedNotAppearBean;
import com.hhly.mlottery.bean.numbersBean.LotteryInfoDateBean;
import com.hhly.mlottery.bean.numbersBean.LotteryInfoHKStartBean;
import com.hhly.mlottery.bean.numbersBean.NumberAppearBean;
import com.hhly.mlottery.config.BaseURLs;
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
    private LotteryInfoHKStartBean.DateBean mDate;

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

        VolleyContentFast.requestJsonByGet(BaseURLs.LOTTERY_INFO_STARTIS_URL, null, new VolleyContentFast.ResponseSuccessListener<LotteryInfoHKStartBean>() {
            @Override
            public void onResponse(LotteryInfoHKStartBean jsonObject) {
                if (jsonObject != null && jsonObject.getResult() == 200) {
                    mDate = jsonObject.getDate();
                    mHandler.sendEmptyMessage(SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(ERROR);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(ERROR);
            }
        }, LotteryInfoHKStartBean.class);
    }

    private void initView() {
        swipeRefreshLayout = (ExactSwipeRefreshLayout) mView.findViewById(R.id.swipere_fresh_layout_start);
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));
        mViewPager = (ViewPager) mView.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) mView.findViewById(R.id.start_tabs);

        titles = new ArrayList<>();
        titles.add(mContext.getResources().getString(R.string.home_lottery_info_start_tab_tm));
        titles.add(mContext.getResources().getString(R.string.home_lottery_info_start_tab_pm));
        titles.add(mContext.getResources().getString(R.string.home_lottery_info_start_tab_notm));

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    break;
                case SUCCESS:

                    sortDate(mDate);

                    fragments = new ArrayList<>();
                    fragments.add(StartPagerFragment.newInstance(1,zodiacList,numberList,mantissaList,boList));
                    fragments.add(StartPagerFragment.newInstance(2,zodiacList,numberList,mantissaList,boList));
                    fragments.add(StartPagerFragment.newInstance(3,zodiacList,numberList,mantissaList,boList));

                    mViewPager.setAdapter(new PureViewPagerAdapter(fragments, titles, getChildFragmentManager()));
                    tabLayout.setupWithViewPager(mViewPager);
                    tabLayout.setTabMode(TabLayout.MODE_FIXED);
                    break;
                case ERROR:
                    break;
            }
        }
    };


    List<LotteryInfoDateBean> zodiacList;
    List<LotteryInfoDateBean> numberList;
    List<LotteryInfoDateBean> mantissaList;
    List<LotteryInfoDateBean> boList;
    private void sortDate(LotteryInfoHKStartBean.DateBean date){

        zodiacList = new ArrayList<>();
        numberList = new ArrayList<>();
        mantissaList = new ArrayList<>();
        boList = new ArrayList<>();

        // TODO 此乃只是一千期的数据
        List<LotteryInfoHKStartBean.DateBean.OneThousandBean.ZodiacBeanOneThousand> zodiac = date.getOneThousand().getZodiac();
        for (LotteryInfoHKStartBean.DateBean.OneThousandBean.ZodiacBeanOneThousand zodiacBeanOneThousand : zodiac) {
            LotteryInfoDateBean bean = new LotteryInfoDateBean();
            bean.setCoedAppear(zodiacBeanOneThousand.getCoedAppear());
            bean.setCoedNotAppear(zodiacBeanOneThousand.getCoedNotAppear());
            bean.setNumberAppear(zodiacBeanOneThousand.getNumberAppear());
            bean.setKey(zodiacBeanOneThousand.getKey());
            zodiacList.add(bean);
        }

        List<LotteryInfoHKStartBean.DateBean.OneThousandBean.NumberBeanOneThousand> number = date.getOneThousand().getNumber();
        for (LotteryInfoHKStartBean.DateBean.OneThousandBean.NumberBeanOneThousand numberBeanOneThousand : number) {
            LotteryInfoDateBean bean = new LotteryInfoDateBean();
            bean.setCoedAppear(numberBeanOneThousand.getCoedAppear());
            bean.setCoedNotAppear(numberBeanOneThousand.getCoedNotAppear());
            bean.setNumberAppear(numberBeanOneThousand.getNumberAppear());
            bean.setKey(numberBeanOneThousand.getKey());
            numberList.add(bean);
        }

        List<LotteryInfoHKStartBean.DateBean.OneThousandBean.MantissaBeanOneThousand> mantissa = date.getOneThousand().getMantissa();
        for (LotteryInfoHKStartBean.DateBean.OneThousandBean.MantissaBeanOneThousand mantissaBeanOneThousand : mantissa) {
            LotteryInfoDateBean bean = new LotteryInfoDateBean();
            bean.setCoedAppear(mantissaBeanOneThousand.getCoedAppear());
            bean.setCoedNotAppear(mantissaBeanOneThousand.getCoedNotAppear());
            bean.setNumberAppear(mantissaBeanOneThousand.getNumberAppear());
            bean.setKey(mantissaBeanOneThousand.getKey());
            mantissaList.add(bean);
        }

        List<LotteryInfoHKStartBean.DateBean.OneThousandBean.BoBeanOneThousand> bo = date.getOneThousand().getBo();
        for (LotteryInfoHKStartBean.DateBean.OneThousandBean.BoBeanOneThousand boBeanOneThousand : bo) {
            LotteryInfoDateBean bean = new LotteryInfoDateBean();
            bean.setCoedAppear(boBeanOneThousand.getCoedAppear());
            bean.setCoedNotAppear(boBeanOneThousand.getCoedNotAppear());
            bean.setNumberAppear(boBeanOneThousand.getNumberAppear());
            bean.setKey(boBeanOneThousand.getKey());
            boList.add(bean);
        }
    }

    @Override
    public void onRefresh() {

    }
}
