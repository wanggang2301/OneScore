package com.hhly.mlottery.frame.numbersframe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.numbersBean.CoedAppearBean;
import com.hhly.mlottery.bean.numbersBean.CoedNotAppearBean;
import com.hhly.mlottery.bean.numbersBean.LotteryInfoDateBean;
import com.hhly.mlottery.bean.numbersBean.NumberAppearBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


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

    private List<ImageView> emojiIconList = new ArrayList<>(12);
    private List<TextView> emojiCountList = new ArrayList<>(12);
    private List<TextView> showNumberList = new ArrayList<>(49);
    private List<TextView> showCountList = new ArrayList<>(49);
    private List<TextView> mantissaNumberList = new ArrayList<>(10);
    private List<TextView> mantissaCountList = new ArrayList<>(10);
    private List<TextView> boNumberList = new ArrayList<>(3);
    private List<TextView> boCountList = new ArrayList<>(3);

    private List<CoedAppearBean> zodiacCoedAppearList = new ArrayList<>(12);
    private List<NumberAppearBean> zodiacNumberAppearList = new ArrayList<>(12);
    private List<CoedNotAppearBean> zodiacCoedNotAppearList = new ArrayList<>(12);
    private List<CoedAppearBean> numberCoedAppearList = new ArrayList<>(49);
    private List<NumberAppearBean> numberNumberAppearList = new ArrayList<>(49);
    private List<CoedNotAppearBean> numberCoedNotAppearList = new ArrayList<>(49);
    private List<CoedAppearBean> mantissaCoedAppearList = new ArrayList<>(10);
    private List<NumberAppearBean> mantissaNumberAppearList = new ArrayList<>(10);
    private List<CoedNotAppearBean> mantissaCoedNotAppearList = new ArrayList<>(10);
    private List<CoedAppearBean> boCoedAppearList = new ArrayList<>(3);
    private List<NumberAppearBean> boNumberAppearList = new ArrayList<>(3);
    private List<CoedNotAppearBean> boCoedNotAppearList = new ArrayList<>(3);

    private TextView tv_title_01;
    private TextView tv_title_02;
    private TextView tv_title_03;
    private TextView tv_title_04;
    private FrameLayout fl_content;
    private FrameLayout fl_notNet;
    private TextView tv_reLoading;

    // 12生肖图
    private int[] zodicaIcons = {
            R.mipmap.hk_zodizc_rat, // 鼠
            R.mipmap.hk_zodizc_chicken, // 鸡
            R.mipmap.hk_zodizc_monkey, // 猴
            R.mipmap.hk_zodizc_loong, // 龙
            R.mipmap.hk_zodizc_rabbit, // 兔
            R.mipmap.hk_zodizc_horse, // 马
            R.mipmap.hk_zodiac_dog, // 狗
            R.mipmap.hk_zodizc_pig, // 猪
            R.mipmap.hk_zodizc_sheep, // 羊
            R.mipmap.hk_zodizc_cattle, // 牛
            R.mipmap.hk_zodizc_snake, // 蛇
            R.mipmap.hk_zodizc_tiger  // 虎
    };

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

        for (int i = 0; i < mZodiacList.size(); i++) {
            CoedAppearBean coedAppearBean = new CoedAppearBean();
            coedAppearBean.setKey(mZodiacList.get(i).getKey());
            coedAppearBean.setCoedAppear(mZodiacList.get(i).getCoedAppear());
            zodiacCoedAppearList.add(coedAppearBean);

            NumberAppearBean numberAppearBean = new NumberAppearBean();
            numberAppearBean.setKey(mZodiacList.get(i).getKey());
            numberAppearBean.setNumberAppear(mZodiacList.get(i).getNumberAppear());
            zodiacNumberAppearList.add(numberAppearBean);

            CoedNotAppearBean coedNotAppearBean = new CoedNotAppearBean();
            coedNotAppearBean.setKey(mZodiacList.get(i).getKey());
            coedNotAppearBean.setCoedNotAppear(mZodiacList.get(i).getCoedNotAppear());
            zodiacCoedNotAppearList.add(coedNotAppearBean);
        }

        for (int i = 0; i < mNumberList.size(); i++) {
            CoedAppearBean coedAppearBean = new CoedAppearBean();
            coedAppearBean.setKey(mNumberList.get(i).getKey());
            coedAppearBean.setCoedAppear(mNumberList.get(i).getCoedAppear());
            numberCoedAppearList.add(coedAppearBean);

            NumberAppearBean numberAppearBean = new NumberAppearBean();
            numberAppearBean.setKey(mNumberList.get(i).getKey());
            numberAppearBean.setNumberAppear(mNumberList.get(i).getNumberAppear());
            numberNumberAppearList.add(numberAppearBean);

            CoedNotAppearBean coedNotAppearBean = new CoedNotAppearBean();
            coedNotAppearBean.setKey(mNumberList.get(i).getKey());
            coedNotAppearBean.setCoedNotAppear(mNumberList.get(i).getCoedNotAppear());
            numberCoedNotAppearList.add(coedNotAppearBean);
        }

        for (int i = 0; i < mMantissaList.size(); i++) {
            CoedAppearBean coedAppearBean = new CoedAppearBean();
            coedAppearBean.setKey(mMantissaList.get(i).getKey());
            coedAppearBean.setCoedAppear(mMantissaList.get(i).getCoedAppear());
            mantissaCoedAppearList.add(coedAppearBean);

            NumberAppearBean numberAppearBean = new NumberAppearBean();
            numberAppearBean.setKey(mMantissaList.get(i).getKey());
            numberAppearBean.setNumberAppear(mMantissaList.get(i).getNumberAppear());
            mantissaNumberAppearList.add(numberAppearBean);

            CoedNotAppearBean coedNotAppearBean = new CoedNotAppearBean();
            coedNotAppearBean.setKey(mMantissaList.get(i).getKey());
            coedNotAppearBean.setCoedNotAppear(mMantissaList.get(i).getCoedNotAppear());
            mantissaCoedNotAppearList.add(coedNotAppearBean);
        }

        for (int i = 0; i < mBoList.size(); i++) {
            CoedAppearBean coedAppearBean = new CoedAppearBean();
            coedAppearBean.setKey(mBoList.get(i).getKey());
            coedAppearBean.setCoedAppear(mBoList.get(i).getCoedAppear());
            boCoedAppearList.add(coedAppearBean);

            NumberAppearBean numberAppearBean = new NumberAppearBean();
            numberAppearBean.setKey(mBoList.get(i).getKey());
            numberAppearBean.setNumberAppear(mBoList.get(i).getNumberAppear());
            boNumberAppearList.add(numberAppearBean);

            CoedNotAppearBean coedNotAppearBean = new CoedNotAppearBean();
            coedNotAppearBean.setKey(mBoList.get(i).getKey());
            coedNotAppearBean.setCoedNotAppear(mBoList.get(i).getCoedNotAppear());
            boCoedNotAppearList.add(coedNotAppearBean);
        }

        switch (mLabs) {
            case 1:
                tv_title_01.setText(mContext.getResources().getString(R.string.home_lottery_info_tm_zodiac_count));
                tv_title_02.setText(mContext.getResources().getString(R.string.home_lottery_info_tm_count));
                tv_title_03.setText(mContext.getResources().getString(R.string.home_lottery_info_tm_mantissa_count));
                tv_title_04.setText(mContext.getResources().getString(R.string.home_lottery_info_tm_bo_count));
                coedAppearSort(zodiacCoedAppearList, -1);
                coedAppearSort(numberCoedAppearList, -1);
                coedAppearSort(mantissaCoedAppearList, -1);
                coedAppearSort(boCoedAppearList, -1);
                break;
            case 2:
                tv_title_01.setText(mContext.getResources().getString(R.string.home_lottery_info_pm_zodiac_count));
                tv_title_02.setText(mContext.getResources().getString(R.string.home_lottery_info_pm_count));
                tv_title_03.setText(mContext.getResources().getString(R.string.home_lottery_info_pm_mantissa_count));
                tv_title_04.setText(mContext.getResources().getString(R.string.home_lottery_info_pm_bo_count));
                numberAppearSort(zodiacNumberAppearList, -1);
                numberAppearSort(numberNumberAppearList, -1);
                numberAppearSort(mantissaNumberAppearList, -1);
                numberAppearSort(boNumberAppearList, -1);
                break;
            case 3:
                tv_title_01.setText(mContext.getResources().getString(R.string.home_lottery_info_tm_not_zodiac_count));
                tv_title_02.setText(mContext.getResources().getString(R.string.home_lottery_info_tm_not_count));
                tv_title_03.setText(mContext.getResources().getString(R.string.home_lottery_info_tm_not_mantissa_count));
                tv_title_04.setText(mContext.getResources().getString(R.string.home_lottery_info_tm_not_bo_count));
                coedNotAppearSort(zodiacCoedNotAppearList, 1);
                coedNotAppearSort(numberCoedNotAppearList, 1);
                coedNotAppearSort(mantissaCoedNotAppearList, 1);
                coedNotAppearSort(boCoedNotAppearList, 1);
                break;
        }

        addDataShow();
    }

    // 直充数据
    private void addDataShow() {
        switch (mLabs) {
            case 1:
                // 生肖
                for (int i = 0; i < zodiacCoedAppearList.size(); i++) {
                    settingZodicaIcon(i, zodiacCoedAppearList.get(i).getKey());
                    emojiCountList.get(i).setText(String.valueOf(zodiacCoedAppearList.get(i).getCoedAppear()));
                }
                // 特码出现
                for (int i = 0; i < numberCoedAppearList.size(); i++) {
                    showNumberList.get(i).setText(numberCoedAppearList.get(i).getKey());
                    settingBgColor(showNumberList.get(i), numberCoedAppearList.get(i).getKey());
                    showCountList.get(i).setText(String.valueOf(numberCoedAppearList.get(i).getCoedAppear()));
                }
                // 特码尾号出现
                for (int i = 0; i < mantissaCoedAppearList.size(); i++) {
                    mantissaNumberList.get(i).setText("*" + mantissaCoedAppearList.get(i).getKey());
                    mantissaCountList.get(i).setText(String.valueOf(mantissaCoedAppearList.get(i).getCoedAppear()));
                }
                // 特码波色出现
                for (int i = 0; i < boCoedAppearList.size(); i++) {
                    switch (boCoedAppearList.get(i).getKey()) {
                        case "红":
                            boNumberList.get(i).setText(mContext.getResources().getString(R.string.home_lottery_info_tm_bo_r));
                            boNumberList.get(i).setBackgroundResource(R.mipmap.number_bg_red);
                            break;
                        case "绿":
                            boNumberList.get(i).setText(mContext.getResources().getString(R.string.home_lottery_info_tm_bo_g));
                            boNumberList.get(i).setBackgroundResource(R.mipmap.number_bg_green);
                            break;
                        case "蓝":
                            boNumberList.get(i).setText(mContext.getResources().getString(R.string.home_lottery_info_tm_bo_b));
                            boNumberList.get(i).setBackgroundResource(R.mipmap.number_bg_blue);
                            break;
                    }
                    boCountList.get(i).setText(String.valueOf(boCoedAppearList.get(i).getCoedAppear()));
                }
                break;
            case 2:
                // 生肖
                for (int i = 0; i < zodiacNumberAppearList.size(); i++) {
                    settingZodicaIcon(i, zodiacNumberAppearList.get(i).getKey());
                    emojiCountList.get(i).setText(String.valueOf(zodiacNumberAppearList.get(i).getNumberAppear()));
                }
                // 特码出现
                for (int i = 0; i < numberNumberAppearList.size(); i++) {
                    showNumberList.get(i).setText(numberNumberAppearList.get(i).getKey());
                    settingBgColor(showNumberList.get(i), numberNumberAppearList.get(i).getKey());
                    showCountList.get(i).setText(String.valueOf(numberNumberAppearList.get(i).getNumberAppear()));
                }
                // 特码尾号出现
                for (int i = 0; i < mantissaNumberAppearList.size(); i++) {
                    mantissaNumberList.get(i).setText("*" + mantissaNumberAppearList.get(i).getKey());
                    mantissaCountList.get(i).setText(String.valueOf(mantissaNumberAppearList.get(i).getNumberAppear()));
                }
                // 特码波色出现
                for (int i = 0; i < boNumberAppearList.size(); i++) {
                    switch (boNumberAppearList.get(i).getKey()) {
                        case "红":
                            boNumberList.get(i).setText(mContext.getResources().getString(R.string.home_lottery_info_tm_bo_r));
                            boNumberList.get(i).setBackgroundResource(R.mipmap.number_bg_red);
                            break;
                        case "绿":
                            boNumberList.get(i).setText(mContext.getResources().getString(R.string.home_lottery_info_tm_bo_g));
                            boNumberList.get(i).setBackgroundResource(R.mipmap.number_bg_green);
                            break;
                        case "蓝":
                            boNumberList.get(i).setText(mContext.getResources().getString(R.string.home_lottery_info_tm_bo_b));
                            boNumberList.get(i).setBackgroundResource(R.mipmap.number_bg_blue);
                            break;
                    }
                    boCountList.get(i).setText(String.valueOf(boNumberAppearList.get(i).getNumberAppear()));
                }
                break;
            case 3:
                // 生肖
                for (int i = 0; i < zodiacCoedNotAppearList.size(); i++) {
                    settingZodicaIcon(i, zodiacCoedNotAppearList.get(i).getKey());
                    emojiCountList.get(i).setText(String.valueOf(zodiacCoedNotAppearList.get(i).getCoedNotAppear()));
                }
                // 特码出现
                for (int i = 0; i < numberCoedNotAppearList.size(); i++) {
                    showNumberList.get(i).setText(numberCoedNotAppearList.get(i).getKey());
                    settingBgColor(showNumberList.get(i), numberCoedNotAppearList.get(i).getKey());
                    showCountList.get(i).setText(String.valueOf(numberCoedNotAppearList.get(i).getCoedNotAppear()));
                }
                // 特码尾号出现
                for (int i = 0; i < mantissaCoedNotAppearList.size(); i++) {
                    mantissaNumberList.get(i).setText("*" + mantissaCoedNotAppearList.get(i).getKey());
                    mantissaCountList.get(i).setText(String.valueOf(mantissaCoedNotAppearList.get(i).getCoedNotAppear()));
                }
                // 特码波色出现
                for (int i = 0; i < boCoedNotAppearList.size(); i++) {
                    switch (boCoedNotAppearList.get(i).getKey()) {
                        case "红":
                            boNumberList.get(i).setText(mContext.getResources().getString(R.string.home_lottery_info_tm_bo_r));
                            boNumberList.get(i).setBackgroundResource(R.mipmap.number_bg_red);
                            break;
                        case "绿":
                            boNumberList.get(i).setText(mContext.getResources().getString(R.string.home_lottery_info_tm_bo_g));
                            boNumberList.get(i).setBackgroundResource(R.mipmap.number_bg_green);
                            break;
                        case "蓝":
                            boNumberList.get(i).setText(mContext.getResources().getString(R.string.home_lottery_info_tm_bo_b));
                            boNumberList.get(i).setBackgroundResource(R.mipmap.number_bg_blue);
                            break;
                    }
                    boCountList.get(i).setText(String.valueOf(boCoedNotAppearList.get(i).getCoedNotAppear()));
                }
                break;
        }
    }

    /**
     * 设置生肖icon，注：key值和顺序不能变，否则会影响图标不对应
     * @param i 下标
     * @param key 生肖
     */
    private void settingZodicaIcon(int i, String key) {
        switch (key) {
            case "鼠":
                emojiIconList.get(i).setBackground(ContextCompat.getDrawable(mContext,zodicaIcons[0]));
                break;
            case "鸡":
                emojiIconList.get(i).setBackground(ContextCompat.getDrawable(mContext,zodicaIcons[1]));
                break;
            case "猴":
                emojiIconList.get(i).setBackground(ContextCompat.getDrawable(mContext,zodicaIcons[2]));
                break;
            case "龙":
                emojiIconList.get(i).setBackground(ContextCompat.getDrawable(mContext,zodicaIcons[3]));
                break;
            case "兔":
                emojiIconList.get(i).setBackground(ContextCompat.getDrawable(mContext,zodicaIcons[4]));
                break;
            case "马":
                emojiIconList.get(i).setBackground(ContextCompat.getDrawable(mContext,zodicaIcons[5]));
                break;
            case "狗":
                emojiIconList.get(i).setBackground(ContextCompat.getDrawable(mContext,zodicaIcons[6]));
                break;
            case "猪":
                emojiIconList.get(i).setBackground(ContextCompat.getDrawable(mContext,zodicaIcons[7]));
                break;
            case "羊":
                emojiIconList.get(i).setBackground(ContextCompat.getDrawable(mContext,zodicaIcons[8]));
                break;
            case "牛":
                emojiIconList.get(i).setBackground(ContextCompat.getDrawable(mContext,zodicaIcons[9]));
                break;
            case "蛇":
                emojiIconList.get(i).setBackground(ContextCompat.getDrawable(mContext,zodicaIcons[10]));
                break;
            case "虎":
                emojiIconList.get(i).setBackground(ContextCompat.getDrawable(mContext,zodicaIcons[11]));
                break;
        }
    }

    // 根据号码设置波色
    private void settingBgColor(TextView tv, String key) {
        switch (key) {
            case "01":
            case "02":
            case "07":
            case "08":
            case "12":
            case "13":
            case "18":
            case "19":
            case "23":
            case "24":
            case "29":
            case "30":
            case "34":
            case "35":
            case "40":
            case "45":
            case "46":
                tv.setBackgroundResource(R.mipmap.number_bg_red);
                break;
            case "03":
            case "04":
            case "09":
            case "10":
            case "14":
            case "15":
            case "20":
            case "25":
            case "26":
            case "31":
            case "36":
            case "37":
            case "41":
            case "42":
            case "47":
            case "48":
                tv.setBackgroundResource(R.mipmap.number_bg_blue);
                break;
            case "05":
            case "06":
            case "11":
            case "16":
            case "17":
            case "21":
            case "22":
            case "27":
            case "28":
            case "32":
            case "33":
            case "38":
            case "39":
            case "43":
            case "44":
            case "49":
                tv.setBackgroundResource(R.mipmap.number_bg_green);
                break;
        }
    }

    /**
     * 排序
     *
     * @param list list
     * @param type 1 正序，-1 倒序
     */
    private void coedAppearSort(List<CoedAppearBean> list, final int type) {
        Collections.sort(list, new Comparator<CoedAppearBean>() {
            public int compare(CoedAppearBean o1, CoedAppearBean o2) {
                if (type == 1) {
                    if (o1.getCoedAppear() < o2.getCoedAppear()) {
                        return 1;
                    }
                    if (o1.getCoedAppear() == o2.getCoedAppear()) {
                        return 0;
                    }
                    return -1;
                } else if (type == -1) {
                    if (o1.getCoedAppear() > o2.getCoedAppear()) {
                        return 1;
                    }
                    if (o1.getCoedAppear() == o2.getCoedAppear()) {
                        return 0;
                    }
                    return -1;
                }
                return -1;
            }
        });
    }

    private void numberAppearSort(List<NumberAppearBean> list, final int type) {
        Collections.sort(list, new Comparator<NumberAppearBean>() {
            public int compare(NumberAppearBean o1, NumberAppearBean o2) {
                if (type == 1) {
                    if (o1.getNumberAppear() < o2.getNumberAppear()) {
                        return 1;
                    }
                    if (o1.getNumberAppear() == o2.getNumberAppear()) {
                        return 0;
                    }
                    return -1;
                } else if (type == -1) {
                    if (o1.getNumberAppear() > o2.getNumberAppear()) {
                        return 1;
                    }
                    if (o1.getNumberAppear() == o2.getNumberAppear()) {
                        return 0;
                    }
                    return -1;
                }
                return -1;
            }
        });
    }

    private void coedNotAppearSort(List<CoedNotAppearBean> list, final int type) {
        Collections.sort(list, new Comparator<CoedNotAppearBean>() {
            public int compare(CoedNotAppearBean o1, CoedNotAppearBean o2) {
                if (type == 1) {
                    if (o1.getCoedNotAppear() < o2.getCoedNotAppear()) {
                        return 1;
                    }
                    if (o1.getCoedNotAppear() == o2.getCoedNotAppear()) {
                        return 0;
                    }
                    return -1;
                } else if (type == -1) {
                    if (o1.getCoedNotAppear() > o2.getCoedNotAppear()) {
                        return 1;
                    }
                    if (o1.getCoedNotAppear() == o2.getCoedNotAppear()) {
                        return 0;
                    }
                    return -1;
                }
                return -1;
            }
        });
    }

    private void initView() {
        fl_content = (FrameLayout) mView.findViewById(R.id.fl_content);
        fl_notNet = (FrameLayout) mView.findViewById(R.id.fl_current_notNet);
        tv_reLoading = (TextView) mView.findViewById(R.id.tv_current_reLoading);

        tv_title_01 = (TextView) mView.findViewById(R.id.tv_title_01);
        tv_title_02 = (TextView) mView.findViewById(R.id.tv_title_02);
        tv_title_03 = (TextView) mView.findViewById(R.id.tv_title_03);
        tv_title_04 = (TextView) mView.findViewById(R.id.tv_title_04);

        ImageView emoji_icon_01 = (ImageView) mView.findViewById(R.id.emoji_icon_01);
        ImageView emoji_icon_02 = (ImageView) mView.findViewById(R.id.emoji_icon_02);
        ImageView emoji_icon_03 = (ImageView) mView.findViewById(R.id.emoji_icon_03);
        ImageView emoji_icon_04 = (ImageView) mView.findViewById(R.id.emoji_icon_04);
        ImageView emoji_icon_05 = (ImageView) mView.findViewById(R.id.emoji_icon_05);
        ImageView emoji_icon_06 = (ImageView) mView.findViewById(R.id.emoji_icon_06);
        ImageView emoji_icon_07 = (ImageView) mView.findViewById(R.id.emoji_icon_07);
        ImageView emoji_icon_08 = (ImageView) mView.findViewById(R.id.emoji_icon_08);
        ImageView emoji_icon_09 = (ImageView) mView.findViewById(R.id.emoji_icon_09);
        ImageView emoji_icon_10 = (ImageView) mView.findViewById(R.id.emoji_icon_10);
        ImageView emoji_icon_11 = (ImageView) mView.findViewById(R.id.emoji_icon_11);
        ImageView emoji_icon_12 = (ImageView) mView.findViewById(R.id.emoji_icon_12);
        emojiIconList.add(emoji_icon_01);
        emojiIconList.add(emoji_icon_02);
        emojiIconList.add(emoji_icon_03);
        emojiIconList.add(emoji_icon_04);
        emojiIconList.add(emoji_icon_05);
        emojiIconList.add(emoji_icon_06);
        emojiIconList.add(emoji_icon_07);
        emojiIconList.add(emoji_icon_08);
        emojiIconList.add(emoji_icon_09);
        emojiIconList.add(emoji_icon_10);
        emojiIconList.add(emoji_icon_11);
        emojiIconList.add(emoji_icon_12);
        TextView emoji_count_01 = (TextView) mView.findViewById(R.id.emoji_count_01);
        TextView emoji_count_02 = (TextView) mView.findViewById(R.id.emoji_count_02);
        TextView emoji_count_03 = (TextView) mView.findViewById(R.id.emoji_count_03);
        TextView emoji_count_04 = (TextView) mView.findViewById(R.id.emoji_count_04);
        TextView emoji_count_05 = (TextView) mView.findViewById(R.id.emoji_count_05);
        TextView emoji_count_06 = (TextView) mView.findViewById(R.id.emoji_count_06);
        TextView emoji_count_07 = (TextView) mView.findViewById(R.id.emoji_count_07);
        TextView emoji_count_08 = (TextView) mView.findViewById(R.id.emoji_count_08);
        TextView emoji_count_09 = (TextView) mView.findViewById(R.id.emoji_count_09);
        TextView emoji_count_10 = (TextView) mView.findViewById(R.id.emoji_count_10);
        TextView emoji_count_11 = (TextView) mView.findViewById(R.id.emoji_count_11);
        TextView emoji_count_12 = (TextView) mView.findViewById(R.id.emoji_count_12);
        emojiCountList.add(emoji_count_01);
        emojiCountList.add(emoji_count_02);
        emojiCountList.add(emoji_count_03);
        emojiCountList.add(emoji_count_04);
        emojiCountList.add(emoji_count_05);
        emojiCountList.add(emoji_count_06);
        emojiCountList.add(emoji_count_07);
        emojiCountList.add(emoji_count_08);
        emojiCountList.add(emoji_count_09);
        emojiCountList.add(emoji_count_10);
        emojiCountList.add(emoji_count_11);
        emojiCountList.add(emoji_count_12);
        TextView tv_show_number01 = (TextView) mView.findViewById(R.id.tv_show_number01);
        TextView tv_show_number02 = (TextView) mView.findViewById(R.id.tv_show_number02);
        TextView tv_show_number03 = (TextView) mView.findViewById(R.id.tv_show_number03);
        TextView tv_show_number04 = (TextView) mView.findViewById(R.id.tv_show_number04);
        TextView tv_show_number05 = (TextView) mView.findViewById(R.id.tv_show_number05);
        TextView tv_show_number06 = (TextView) mView.findViewById(R.id.tv_show_number06);
        TextView tv_show_number07 = (TextView) mView.findViewById(R.id.tv_show_number07);
        TextView tv_show_number08 = (TextView) mView.findViewById(R.id.tv_show_number08);
        TextView tv_show_number09 = (TextView) mView.findViewById(R.id.tv_show_number09);
        TextView tv_show_number10 = (TextView) mView.findViewById(R.id.tv_show_number10);
        TextView tv_show_number11 = (TextView) mView.findViewById(R.id.tv_show_number11);
        TextView tv_show_number12 = (TextView) mView.findViewById(R.id.tv_show_number12);
        TextView tv_show_number13 = (TextView) mView.findViewById(R.id.tv_show_number13);
        TextView tv_show_number14 = (TextView) mView.findViewById(R.id.tv_show_number14);
        TextView tv_show_number15 = (TextView) mView.findViewById(R.id.tv_show_number15);
        TextView tv_show_number16 = (TextView) mView.findViewById(R.id.tv_show_number16);
        TextView tv_show_number17 = (TextView) mView.findViewById(R.id.tv_show_number17);
        TextView tv_show_number18 = (TextView) mView.findViewById(R.id.tv_show_number18);
        TextView tv_show_number19 = (TextView) mView.findViewById(R.id.tv_show_number19);
        TextView tv_show_number20 = (TextView) mView.findViewById(R.id.tv_show_number20);
        TextView tv_show_number21 = (TextView) mView.findViewById(R.id.tv_show_number21);
        TextView tv_show_number22 = (TextView) mView.findViewById(R.id.tv_show_number22);
        TextView tv_show_number23 = (TextView) mView.findViewById(R.id.tv_show_number23);
        TextView tv_show_number24 = (TextView) mView.findViewById(R.id.tv_show_number24);
        TextView tv_show_number25 = (TextView) mView.findViewById(R.id.tv_show_number25);
        TextView tv_show_number26 = (TextView) mView.findViewById(R.id.tv_show_number26);
        TextView tv_show_number27 = (TextView) mView.findViewById(R.id.tv_show_number27);
        TextView tv_show_number28 = (TextView) mView.findViewById(R.id.tv_show_number28);
        TextView tv_show_number29 = (TextView) mView.findViewById(R.id.tv_show_number29);
        TextView tv_show_number30 = (TextView) mView.findViewById(R.id.tv_show_number30);
        TextView tv_show_number31 = (TextView) mView.findViewById(R.id.tv_show_number31);
        TextView tv_show_number32 = (TextView) mView.findViewById(R.id.tv_show_number32);
        TextView tv_show_number33 = (TextView) mView.findViewById(R.id.tv_show_number33);
        TextView tv_show_number34 = (TextView) mView.findViewById(R.id.tv_show_number34);
        TextView tv_show_number35 = (TextView) mView.findViewById(R.id.tv_show_number35);
        TextView tv_show_number36 = (TextView) mView.findViewById(R.id.tv_show_number36);
        TextView tv_show_number37 = (TextView) mView.findViewById(R.id.tv_show_number37);
        TextView tv_show_number38 = (TextView) mView.findViewById(R.id.tv_show_number38);
        TextView tv_show_number39 = (TextView) mView.findViewById(R.id.tv_show_number39);
        TextView tv_show_number40 = (TextView) mView.findViewById(R.id.tv_show_number40);
        TextView tv_show_number41 = (TextView) mView.findViewById(R.id.tv_show_number41);
        TextView tv_show_number42 = (TextView) mView.findViewById(R.id.tv_show_number42);
        TextView tv_show_number43 = (TextView) mView.findViewById(R.id.tv_show_number43);
        TextView tv_show_number44 = (TextView) mView.findViewById(R.id.tv_show_number44);
        TextView tv_show_number45 = (TextView) mView.findViewById(R.id.tv_show_number45);
        TextView tv_show_number46 = (TextView) mView.findViewById(R.id.tv_show_number46);
        TextView tv_show_number47 = (TextView) mView.findViewById(R.id.tv_show_number47);
        TextView tv_show_number48 = (TextView) mView.findViewById(R.id.tv_show_number48);
        TextView tv_show_number49 = (TextView) mView.findViewById(R.id.tv_show_number49);
        showNumberList.add(tv_show_number01);
        showNumberList.add(tv_show_number02);
        showNumberList.add(tv_show_number03);
        showNumberList.add(tv_show_number04);
        showNumberList.add(tv_show_number05);
        showNumberList.add(tv_show_number06);
        showNumberList.add(tv_show_number07);
        showNumberList.add(tv_show_number08);
        showNumberList.add(tv_show_number09);
        showNumberList.add(tv_show_number10);
        showNumberList.add(tv_show_number11);
        showNumberList.add(tv_show_number12);
        showNumberList.add(tv_show_number13);
        showNumberList.add(tv_show_number14);
        showNumberList.add(tv_show_number15);
        showNumberList.add(tv_show_number16);
        showNumberList.add(tv_show_number17);
        showNumberList.add(tv_show_number18);
        showNumberList.add(tv_show_number19);
        showNumberList.add(tv_show_number20);
        showNumberList.add(tv_show_number21);
        showNumberList.add(tv_show_number22);
        showNumberList.add(tv_show_number23);
        showNumberList.add(tv_show_number24);
        showNumberList.add(tv_show_number25);
        showNumberList.add(tv_show_number26);
        showNumberList.add(tv_show_number27);
        showNumberList.add(tv_show_number28);
        showNumberList.add(tv_show_number29);
        showNumberList.add(tv_show_number30);
        showNumberList.add(tv_show_number31);
        showNumberList.add(tv_show_number32);
        showNumberList.add(tv_show_number33);
        showNumberList.add(tv_show_number34);
        showNumberList.add(tv_show_number35);
        showNumberList.add(tv_show_number36);
        showNumberList.add(tv_show_number37);
        showNumberList.add(tv_show_number38);
        showNumberList.add(tv_show_number39);
        showNumberList.add(tv_show_number40);
        showNumberList.add(tv_show_number41);
        showNumberList.add(tv_show_number42);
        showNumberList.add(tv_show_number43);
        showNumberList.add(tv_show_number44);
        showNumberList.add(tv_show_number45);
        showNumberList.add(tv_show_number46);
        showNumberList.add(tv_show_number47);
        showNumberList.add(tv_show_number48);
        showNumberList.add(tv_show_number49);
        TextView tv_show_count01 = (TextView) mView.findViewById(R.id.tv_show_count01);
        TextView tv_show_count02 = (TextView) mView.findViewById(R.id.tv_show_count02);
        TextView tv_show_count03 = (TextView) mView.findViewById(R.id.tv_show_count03);
        TextView tv_show_count04 = (TextView) mView.findViewById(R.id.tv_show_count04);
        TextView tv_show_count05 = (TextView) mView.findViewById(R.id.tv_show_count05);
        TextView tv_show_count06 = (TextView) mView.findViewById(R.id.tv_show_count06);
        TextView tv_show_count07 = (TextView) mView.findViewById(R.id.tv_show_count07);
        TextView tv_show_count08 = (TextView) mView.findViewById(R.id.tv_show_count08);
        TextView tv_show_count09 = (TextView) mView.findViewById(R.id.tv_show_count09);
        TextView tv_show_count10 = (TextView) mView.findViewById(R.id.tv_show_count10);
        TextView tv_show_count11 = (TextView) mView.findViewById(R.id.tv_show_count11);
        TextView tv_show_count12 = (TextView) mView.findViewById(R.id.tv_show_count12);
        TextView tv_show_count13 = (TextView) mView.findViewById(R.id.tv_show_count13);
        TextView tv_show_count14 = (TextView) mView.findViewById(R.id.tv_show_count14);
        TextView tv_show_count15 = (TextView) mView.findViewById(R.id.tv_show_count15);
        TextView tv_show_count16 = (TextView) mView.findViewById(R.id.tv_show_count16);
        TextView tv_show_count17 = (TextView) mView.findViewById(R.id.tv_show_count17);
        TextView tv_show_count18 = (TextView) mView.findViewById(R.id.tv_show_count18);
        TextView tv_show_count19 = (TextView) mView.findViewById(R.id.tv_show_count19);
        TextView tv_show_count20 = (TextView) mView.findViewById(R.id.tv_show_count20);
        TextView tv_show_count21 = (TextView) mView.findViewById(R.id.tv_show_count21);
        TextView tv_show_count22 = (TextView) mView.findViewById(R.id.tv_show_count22);
        TextView tv_show_count23 = (TextView) mView.findViewById(R.id.tv_show_count23);
        TextView tv_show_count24 = (TextView) mView.findViewById(R.id.tv_show_count24);
        TextView tv_show_count25 = (TextView) mView.findViewById(R.id.tv_show_count25);
        TextView tv_show_count26 = (TextView) mView.findViewById(R.id.tv_show_count26);
        TextView tv_show_count27 = (TextView) mView.findViewById(R.id.tv_show_count27);
        TextView tv_show_count28 = (TextView) mView.findViewById(R.id.tv_show_count28);
        TextView tv_show_count29 = (TextView) mView.findViewById(R.id.tv_show_count29);
        TextView tv_show_count30 = (TextView) mView.findViewById(R.id.tv_show_count30);
        TextView tv_show_count31 = (TextView) mView.findViewById(R.id.tv_show_count31);
        TextView tv_show_count32 = (TextView) mView.findViewById(R.id.tv_show_count32);
        TextView tv_show_count33 = (TextView) mView.findViewById(R.id.tv_show_count33);
        TextView tv_show_count34 = (TextView) mView.findViewById(R.id.tv_show_count34);
        TextView tv_show_count35 = (TextView) mView.findViewById(R.id.tv_show_count35);
        TextView tv_show_count36 = (TextView) mView.findViewById(R.id.tv_show_count36);
        TextView tv_show_count37 = (TextView) mView.findViewById(R.id.tv_show_count37);
        TextView tv_show_count38 = (TextView) mView.findViewById(R.id.tv_show_count38);
        TextView tv_show_count39 = (TextView) mView.findViewById(R.id.tv_show_count39);
        TextView tv_show_count40 = (TextView) mView.findViewById(R.id.tv_show_count40);
        TextView tv_show_count41 = (TextView) mView.findViewById(R.id.tv_show_count41);
        TextView tv_show_count42 = (TextView) mView.findViewById(R.id.tv_show_count42);
        TextView tv_show_count43 = (TextView) mView.findViewById(R.id.tv_show_count43);
        TextView tv_show_count44 = (TextView) mView.findViewById(R.id.tv_show_count44);
        TextView tv_show_count45 = (TextView) mView.findViewById(R.id.tv_show_count45);
        TextView tv_show_count46 = (TextView) mView.findViewById(R.id.tv_show_count46);
        TextView tv_show_count47 = (TextView) mView.findViewById(R.id.tv_show_count47);
        TextView tv_show_count48 = (TextView) mView.findViewById(R.id.tv_show_count48);
        TextView tv_show_count49 = (TextView) mView.findViewById(R.id.tv_show_count49);
        showCountList.add(tv_show_count01);
        showCountList.add(tv_show_count02);
        showCountList.add(tv_show_count03);
        showCountList.add(tv_show_count04);
        showCountList.add(tv_show_count05);
        showCountList.add(tv_show_count06);
        showCountList.add(tv_show_count07);
        showCountList.add(tv_show_count08);
        showCountList.add(tv_show_count09);
        showCountList.add(tv_show_count10);
        showCountList.add(tv_show_count11);
        showCountList.add(tv_show_count12);
        showCountList.add(tv_show_count13);
        showCountList.add(tv_show_count14);
        showCountList.add(tv_show_count15);
        showCountList.add(tv_show_count16);
        showCountList.add(tv_show_count17);
        showCountList.add(tv_show_count18);
        showCountList.add(tv_show_count19);
        showCountList.add(tv_show_count20);
        showCountList.add(tv_show_count21);
        showCountList.add(tv_show_count22);
        showCountList.add(tv_show_count23);
        showCountList.add(tv_show_count24);
        showCountList.add(tv_show_count25);
        showCountList.add(tv_show_count26);
        showCountList.add(tv_show_count27);
        showCountList.add(tv_show_count28);
        showCountList.add(tv_show_count29);
        showCountList.add(tv_show_count30);
        showCountList.add(tv_show_count31);
        showCountList.add(tv_show_count32);
        showCountList.add(tv_show_count33);
        showCountList.add(tv_show_count34);
        showCountList.add(tv_show_count35);
        showCountList.add(tv_show_count36);
        showCountList.add(tv_show_count37);
        showCountList.add(tv_show_count38);
        showCountList.add(tv_show_count39);
        showCountList.add(tv_show_count40);
        showCountList.add(tv_show_count41);
        showCountList.add(tv_show_count42);
        showCountList.add(tv_show_count43);
        showCountList.add(tv_show_count44);
        showCountList.add(tv_show_count45);
        showCountList.add(tv_show_count46);
        showCountList.add(tv_show_count47);
        showCountList.add(tv_show_count48);
        showCountList.add(tv_show_count49);
        TextView tv_mantissa_number01 = (TextView) mView.findViewById(R.id.tv_mantissa_number01);
        TextView tv_mantissa_number02 = (TextView) mView.findViewById(R.id.tv_mantissa_number02);
        TextView tv_mantissa_number03 = (TextView) mView.findViewById(R.id.tv_mantissa_number03);
        TextView tv_mantissa_number04 = (TextView) mView.findViewById(R.id.tv_mantissa_number04);
        TextView tv_mantissa_number05 = (TextView) mView.findViewById(R.id.tv_mantissa_number05);
        TextView tv_mantissa_number06 = (TextView) mView.findViewById(R.id.tv_mantissa_number06);
        TextView tv_mantissa_number07 = (TextView) mView.findViewById(R.id.tv_mantissa_number07);
        TextView tv_mantissa_number08 = (TextView) mView.findViewById(R.id.tv_mantissa_number08);
        TextView tv_mantissa_number09 = (TextView) mView.findViewById(R.id.tv_mantissa_number09);
        TextView tv_mantissa_number10 = (TextView) mView.findViewById(R.id.tv_mantissa_number10);
        mantissaNumberList.add(tv_mantissa_number01);
        mantissaNumberList.add(tv_mantissa_number02);
        mantissaNumberList.add(tv_mantissa_number03);
        mantissaNumberList.add(tv_mantissa_number04);
        mantissaNumberList.add(tv_mantissa_number05);
        mantissaNumberList.add(tv_mantissa_number06);
        mantissaNumberList.add(tv_mantissa_number07);
        mantissaNumberList.add(tv_mantissa_number08);
        mantissaNumberList.add(tv_mantissa_number09);
        mantissaNumberList.add(tv_mantissa_number10);
        TextView tv_mantissa_count01 = (TextView) mView.findViewById(R.id.tv_mantissa_count01);
        TextView tv_mantissa_count02 = (TextView) mView.findViewById(R.id.tv_mantissa_count02);
        TextView tv_mantissa_count03 = (TextView) mView.findViewById(R.id.tv_mantissa_count03);
        TextView tv_mantissa_count04 = (TextView) mView.findViewById(R.id.tv_mantissa_count04);
        TextView tv_mantissa_count05 = (TextView) mView.findViewById(R.id.tv_mantissa_count05);
        TextView tv_mantissa_count06 = (TextView) mView.findViewById(R.id.tv_mantissa_count06);
        TextView tv_mantissa_count07 = (TextView) mView.findViewById(R.id.tv_mantissa_count07);
        TextView tv_mantissa_count08 = (TextView) mView.findViewById(R.id.tv_mantissa_count08);
        TextView tv_mantissa_count09 = (TextView) mView.findViewById(R.id.tv_mantissa_count09);
        TextView tv_mantissa_count10 = (TextView) mView.findViewById(R.id.tv_mantissa_count10);
        mantissaCountList.add(tv_mantissa_count01);
        mantissaCountList.add(tv_mantissa_count02);
        mantissaCountList.add(tv_mantissa_count03);
        mantissaCountList.add(tv_mantissa_count04);
        mantissaCountList.add(tv_mantissa_count05);
        mantissaCountList.add(tv_mantissa_count06);
        mantissaCountList.add(tv_mantissa_count07);
        mantissaCountList.add(tv_mantissa_count08);
        mantissaCountList.add(tv_mantissa_count09);
        mantissaCountList.add(tv_mantissa_count10);
        TextView tv_bo_number01 = (TextView) mView.findViewById(R.id.tv_bo_number01);
        TextView tv_bo_number02 = (TextView) mView.findViewById(R.id.tv_bo_number02);
        TextView tv_bo_number03 = (TextView) mView.findViewById(R.id.tv_bo_number03);
        boNumberList.add(tv_bo_number01);
        boNumberList.add(tv_bo_number02);
        boNumberList.add(tv_bo_number03);
        TextView tv_bo_count01 = (TextView) mView.findViewById(R.id.tv_bo_count01);
        TextView tv_bo_count02 = (TextView) mView.findViewById(R.id.tv_bo_count02);
        TextView tv_bo_count03 = (TextView) mView.findViewById(R.id.tv_bo_count03);
        boCountList.add(tv_bo_count01);
        boCountList.add(tv_bo_count02);
        boCountList.add(tv_bo_count03);
    }
}
