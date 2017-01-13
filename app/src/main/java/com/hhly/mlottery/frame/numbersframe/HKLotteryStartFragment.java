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
import android.widget.AdapterView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.NumbersInfoBaseActivity;
import com.hhly.mlottery.adapter.PureViewPagerAdapter;
import com.hhly.mlottery.bean.numbersBean.LotteryInfoDateBean;
import com.hhly.mlottery.bean.numbersBean.LotteryInfoHKStartBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.BallSelectArrayAdapter;
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
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles;
    private LotteryInfoHKStartBean.DataBean mData;
    private PureViewPagerAdapter adapter;

    private List<LotteryInfoDateBean> zodiacList = new ArrayList<>();
    private List<LotteryInfoDateBean> numberList = new ArrayList<>();
    private List<LotteryInfoDateBean> mantissaList = new ArrayList<>();
    private List<LotteryInfoDateBean> boList = new ArrayList<>();
    private String[] itemDatas;// 下拉选择数据

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
                    mData = jsonObject.getData();
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

                    itemDatas = new String[]{"4000期", "1000期", "500期", "100期"};
                    ((NumbersInfoBaseActivity) mContext).public_txt_spinner.setAdapter(new BallSelectArrayAdapter(mContext, itemDatas));
                    ((NumbersInfoBaseActivity) mContext).public_txt_spinner.setSelection(1);
                    ((NumbersInfoBaseActivity) mContext).public_txt_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            sortDate(i);

                            if (fragments != null) {
                                fragments.clear();
                            }
                            fragments.add(StartPagerFragment.newInstance(1, zodiacList, numberList, mantissaList, boList));
                            fragments.add(StartPagerFragment.newInstance(2, zodiacList, numberList, mantissaList, boList));
                            fragments.add(StartPagerFragment.newInstance(3, zodiacList, numberList, mantissaList, boList));

                            adapter = new PureViewPagerAdapter(fragments, titles, getChildFragmentManager());
                            mViewPager.setAdapter(adapter);
                            tabLayout.setupWithViewPager(mViewPager);
                            tabLayout.setTabMode(TabLayout.MODE_FIXED);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    break;
                case ERROR:
                    break;
            }
        }
    };


    private void sortDate(int index) {

        if (mData == null) {
            return;
        }

        if (zodiacList != null) {
            zodiacList.clear();
        }
        if (numberList != null) {
            numberList.clear();
        }
        if (mantissaList != null) {
            mantissaList.clear();
        }
        if (boList != null) {
            boList.clear();
        }

        switch (index) {
            case 0: {
                List<LotteryInfoHKStartBean.DataBean.FourThousandBean.ZodiacBean> zodiac = mData.getFourThousand().getZodiac();
                for (LotteryInfoHKStartBean.DataBean.FourThousandBean.ZodiacBean zodiacBeanFourThousand : zodiac) {
                    LotteryInfoDateBean bean = new LotteryInfoDateBean();
                    bean.setCoedAppear(zodiacBeanFourThousand.getCoedAppear());
                    bean.setCoedNotAppear(zodiacBeanFourThousand.getCoedNotAppear());
                    bean.setNumberAppear(zodiacBeanFourThousand.getNumberAppear());
                    bean.setKey(zodiacBeanFourThousand.getKey());
                    zodiacList.add(bean);
                }
                List<LotteryInfoHKStartBean.DataBean.FourThousandBean.NumberBean> number = mData.getFourThousand().getNumber();
                for (LotteryInfoHKStartBean.DataBean.FourThousandBean.NumberBean numberBeanFourThousand : number) {
                    LotteryInfoDateBean bean = new LotteryInfoDateBean();
                    bean.setCoedAppear(numberBeanFourThousand.getCoedAppear());
                    bean.setCoedNotAppear(numberBeanFourThousand.getCoedNotAppear());
                    bean.setNumberAppear(numberBeanFourThousand.getNumberAppear());
                    bean.setKey(numberBeanFourThousand.getKey());
                    numberList.add(bean);
                }
                List<LotteryInfoHKStartBean.DataBean.FourThousandBean.MantissaBean> mantissa = mData.getFourThousand().getMantissa();
                for (LotteryInfoHKStartBean.DataBean.FourThousandBean.MantissaBean mantissaBeanFourThousand : mantissa) {
                    LotteryInfoDateBean bean = new LotteryInfoDateBean();
                    bean.setCoedAppear(mantissaBeanFourThousand.getCoedAppear());
                    bean.setCoedNotAppear(mantissaBeanFourThousand.getCoedNotAppear());
                    bean.setNumberAppear(mantissaBeanFourThousand.getNumberAppear());
                    bean.setKey(mantissaBeanFourThousand.getKey());
                    mantissaList.add(bean);
                }
                List<LotteryInfoHKStartBean.DataBean.FourThousandBean.BoBean> bo = mData.getFourThousand().getBo();
                for (LotteryInfoHKStartBean.DataBean.FourThousandBean.BoBean boBeanFourThousand : bo) {
                    LotteryInfoDateBean bean = new LotteryInfoDateBean();
                    bean.setCoedAppear(boBeanFourThousand.getCoedAppear());
                    bean.setCoedNotAppear(boBeanFourThousand.getCoedNotAppear());
                    bean.setNumberAppear(boBeanFourThousand.getNumberAppear());
                    bean.setKey(boBeanFourThousand.getKey());
                    boList.add(bean);
                }
            }
            break;
            case 1: {
                List<LotteryInfoHKStartBean.DataBean.OneThousandBean.ZodiacBeanOneThousand> zodiac = mData.getOneThousand().getZodiac();
                for (LotteryInfoHKStartBean.DataBean.OneThousandBean.ZodiacBeanOneThousand zodiacBeanOneThousand : zodiac) {
                    LotteryInfoDateBean bean = new LotteryInfoDateBean();
                    bean.setCoedAppear(zodiacBeanOneThousand.getCoedAppear());
                    bean.setCoedNotAppear(zodiacBeanOneThousand.getCoedNotAppear());
                    bean.setNumberAppear(zodiacBeanOneThousand.getNumberAppear());
                    bean.setKey(zodiacBeanOneThousand.getKey());
                    zodiacList.add(bean);
                }
                List<LotteryInfoHKStartBean.DataBean.OneThousandBean.NumberBeanOneThousand> number = mData.getOneThousand().getNumber();
                for (LotteryInfoHKStartBean.DataBean.OneThousandBean.NumberBeanOneThousand numberBeanOneThousand : number) {
                    LotteryInfoDateBean bean = new LotteryInfoDateBean();
                    bean.setCoedAppear(numberBeanOneThousand.getCoedAppear());
                    bean.setCoedNotAppear(numberBeanOneThousand.getCoedNotAppear());
                    bean.setNumberAppear(numberBeanOneThousand.getNumberAppear());
                    bean.setKey(numberBeanOneThousand.getKey());
                    numberList.add(bean);
                }
                List<LotteryInfoHKStartBean.DataBean.OneThousandBean.MantissaBeanOneThousand> mantissa = mData.getOneThousand().getMantissa();
                for (LotteryInfoHKStartBean.DataBean.OneThousandBean.MantissaBeanOneThousand mantissaBeanOneThousand : mantissa) {
                    LotteryInfoDateBean bean = new LotteryInfoDateBean();
                    bean.setCoedAppear(mantissaBeanOneThousand.getCoedAppear());
                    bean.setCoedNotAppear(mantissaBeanOneThousand.getCoedNotAppear());
                    bean.setNumberAppear(mantissaBeanOneThousand.getNumberAppear());
                    bean.setKey(mantissaBeanOneThousand.getKey());
                    mantissaList.add(bean);
                }
                List<LotteryInfoHKStartBean.DataBean.OneThousandBean.BoBeanOneThousand> bo = mData.getOneThousand().getBo();
                for (LotteryInfoHKStartBean.DataBean.OneThousandBean.BoBeanOneThousand boBeanOneThousand : bo) {
                    LotteryInfoDateBean bean = new LotteryInfoDateBean();
                    bean.setCoedAppear(boBeanOneThousand.getCoedAppear());
                    bean.setCoedNotAppear(boBeanOneThousand.getCoedNotAppear());
                    bean.setNumberAppear(boBeanOneThousand.getNumberAppear());
                    bean.setKey(boBeanOneThousand.getKey());
                    boList.add(bean);
                }
            }
            break;
            case 2: {
                List<LotteryInfoHKStartBean.DataBean.FiveHundredBean.ZodiacBeanFiveHundred> zodiac = mData.getFiveHundred().getZodiac();
                for (LotteryInfoHKStartBean.DataBean.FiveHundredBean.ZodiacBeanFiveHundred zodiacBeanFiveHunderd : zodiac) {
                    LotteryInfoDateBean bean = new LotteryInfoDateBean();
                    bean.setCoedAppear(zodiacBeanFiveHunderd.getCoedAppear());
                    bean.setCoedNotAppear(zodiacBeanFiveHunderd.getCoedNotAppear());
                    bean.setNumberAppear(zodiacBeanFiveHunderd.getNumberAppear());
                    bean.setKey(zodiacBeanFiveHunderd.getKey());
                    zodiacList.add(bean);
                }
                List<LotteryInfoHKStartBean.DataBean.FiveHundredBean.NumberBeanFiveHundred> number = mData.getFiveHundred().getNumber();
                for (LotteryInfoHKStartBean.DataBean.FiveHundredBean.NumberBeanFiveHundred numberBeanFiveHunderd : number) {
                    LotteryInfoDateBean bean = new LotteryInfoDateBean();
                    bean.setCoedAppear(numberBeanFiveHunderd.getCoedAppear());
                    bean.setCoedNotAppear(numberBeanFiveHunderd.getCoedNotAppear());
                    bean.setNumberAppear(numberBeanFiveHunderd.getNumberAppear());
                    bean.setKey(numberBeanFiveHunderd.getKey());
                    numberList.add(bean);
                }
                List<LotteryInfoHKStartBean.DataBean.FiveHundredBean.MantissaBeanFiveHundred> mantissa = mData.getFiveHundred().getMantissa();
                for (LotteryInfoHKStartBean.DataBean.FiveHundredBean.MantissaBeanFiveHundred mantissaBeanFiveHunderd : mantissa) {
                    LotteryInfoDateBean bean = new LotteryInfoDateBean();
                    bean.setCoedAppear(mantissaBeanFiveHunderd.getCoedAppear());
                    bean.setCoedNotAppear(mantissaBeanFiveHunderd.getCoedNotAppear());
                    bean.setNumberAppear(mantissaBeanFiveHunderd.getNumberAppear());
                    bean.setKey(mantissaBeanFiveHunderd.getKey());
                    mantissaList.add(bean);
                }
                List<LotteryInfoHKStartBean.DataBean.FiveHundredBean.BoBeanFiveHundred> bo = mData.getFiveHundred().getBo();
                for (LotteryInfoHKStartBean.DataBean.FiveHundredBean.BoBeanFiveHundred boBeanFiveHunderd : bo) {
                    LotteryInfoDateBean bean = new LotteryInfoDateBean();
                    bean.setCoedAppear(boBeanFiveHunderd.getCoedAppear());
                    bean.setCoedNotAppear(boBeanFiveHunderd.getCoedNotAppear());
                    bean.setNumberAppear(boBeanFiveHunderd.getNumberAppear());
                    bean.setKey(boBeanFiveHunderd.getKey());
                    boList.add(bean);
                }
            }
            break;
            case 3: {
                List<LotteryInfoHKStartBean.DataBean.OneHundredBean.ZodiacBeanOneHundred> zodiac = mData.getOneHundred().getZodiac();
                for (LotteryInfoHKStartBean.DataBean.OneHundredBean.ZodiacBeanOneHundred zodiacBeanOneHundred : zodiac) {
                    LotteryInfoDateBean bean = new LotteryInfoDateBean();
                    bean.setCoedAppear(zodiacBeanOneHundred.getCoedAppear());
                    bean.setCoedNotAppear(zodiacBeanOneHundred.getCoedNotAppear());
                    bean.setNumberAppear(zodiacBeanOneHundred.getNumberAppear());
                    bean.setKey(zodiacBeanOneHundred.getKey());
                    zodiacList.add(bean);
                }
                List<LotteryInfoHKStartBean.DataBean.OneHundredBean.NumberBeanOneHundred> number = mData.getOneHundred().getNumber();
                for (LotteryInfoHKStartBean.DataBean.OneHundredBean.NumberBeanOneHundred numberBeanOneHundred : number) {
                    LotteryInfoDateBean bean = new LotteryInfoDateBean();
                    bean.setCoedAppear(numberBeanOneHundred.getCoedAppear());
                    bean.setCoedNotAppear(numberBeanOneHundred.getCoedNotAppear());
                    bean.setNumberAppear(numberBeanOneHundred.getNumberAppear());
                    bean.setKey(numberBeanOneHundred.getKey());
                    numberList.add(bean);
                }
                List<LotteryInfoHKStartBean.DataBean.OneHundredBean.MantissaBeanOneHundred> mantissa = mData.getOneHundred().getMantissa();
                for (LotteryInfoHKStartBean.DataBean.OneHundredBean.MantissaBeanOneHundred mantissaBeanOneHundred : mantissa) {
                    LotteryInfoDateBean bean = new LotteryInfoDateBean();
                    bean.setCoedAppear(mantissaBeanOneHundred.getCoedAppear());
                    bean.setCoedNotAppear(mantissaBeanOneHundred.getCoedNotAppear());
                    bean.setNumberAppear(mantissaBeanOneHundred.getNumberAppear());
                    bean.setKey(mantissaBeanOneHundred.getKey());
                    mantissaList.add(bean);
                }
                List<LotteryInfoHKStartBean.DataBean.OneHundredBean.BoBeanOneHundred> bo = mData.getOneHundred().getBo();
                for (LotteryInfoHKStartBean.DataBean.OneHundredBean.BoBeanOneHundred boBeanOneHundred : bo) {
                    LotteryInfoDateBean bean = new LotteryInfoDateBean();
                    bean.setCoedAppear(boBeanOneHundred.getCoedAppear());
                    bean.setCoedNotAppear(boBeanOneHundred.getCoedNotAppear());
                    bean.setNumberAppear(boBeanOneHundred.getNumberAppear());
                    bean.setKey(boBeanOneHundred.getKey());
                    boList.add(bean);
                }
            }
            break;
        }
    }

    @Override
    public void onRefresh() {

    }
}
