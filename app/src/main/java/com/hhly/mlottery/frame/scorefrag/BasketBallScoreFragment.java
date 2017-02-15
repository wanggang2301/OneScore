package com.hhly.mlottery.frame.scorefrag;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketFiltrateActivity;
import com.hhly.mlottery.activity.BasketballSettingActivity;
import com.hhly.mlottery.adapter.PureViewPagerAdapter;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.basketballframe.ImmedBasketballFragment;
import com.hhly.mlottery.frame.basketballframe.ResultBasketballFragment;
import com.hhly.mlottery.frame.basketballframe.ScheduleBasketballFragment;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.BallSelectArrayAdapter;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasketBallScoreFragment extends BaseWebSocketFragment implements View.OnClickListener {

    private String[] mItems = {"足球", "篮球"};
    private Spinner mSpinner;
    private View view;
    private Context mContext;


    private final int IMMEDIA_FRAGMENT = 0;
    private final int RESULT_FRAGMENT = 1;
    private final int SCHEDULE_FRAGMENT = 2;

    private final static String TAG = "BasketScoresFragment";
    public static List<String> titles;

    private Intent mIntent;



    /**
     * 筛选
     */
    public static ImageView mFilterImgBtn;// 筛选

    public static ImageView getmFilterImgBtn() {
        return mFilterImgBtn;
    }


    /**
     * 设置
     */
    public static ImageView mSetting;// 设置

    public static ImageView getmSetting() {
        return mSetting;
    }


    /**
     * 当前处于哪个比赛fg
     */
    private int currentFragmentId = 0;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private PureViewPagerAdapter pureViewPagerAdapter;
    private List<Fragment> fragments;
    /**
     * 判断是否来自关注页面
     */
    int mComeFromFocus = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic("USER.topic.basketball");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        view = inflater.inflate(R.layout.fragment_basket_ball_score, container, false);

        initView();

        setupViewPager();
        initCurrentFragment(currentFragmentId);


        initEvent();
        return view;
    }

    private void initView() {
        mSpinner = (Spinner) view.findViewById(R.id.public_txt_left_spinner);
        mSpinner.setVisibility(View.VISIBLE);
        BallSelectArrayAdapter mAdapter = new BallSelectArrayAdapter(mContext, mItems);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setSelection(1, true);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);


        // 筛选
        mFilterImgBtn = (ImageView) view.findViewById(R.id.public_btn_filter);
        mFilterImgBtn.setOnClickListener(this);


        //设置按钮
        mSetting = (ImageView) view.findViewById(R.id.public_btn_set);
        mSetting.setOnClickListener(this);


    }

    private void setupViewPager() {
        mTabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        titles = new ArrayList<>();
        titles.add(getString(R.string.foot_jishi_txt));
        titles.add(getString(R.string.foot_saiguo_txt));
        titles.add(getString(R.string.foot_saicheng_txt));

        fragments = new ArrayList<>();
        fragments.add(ImmedBasketballFragment.newInstance(IMMEDIA_FRAGMENT));
        fragments.add(ResultBasketballFragment.newInstance(RESULT_FRAGMENT));
        fragments.add(ScheduleBasketballFragment.newInstance(SCHEDULE_FRAGMENT));

        pureViewPagerAdapter = new PureViewPagerAdapter(fragments, titles, getChildFragmentManager());
        mViewPager.setAdapter(pureViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mViewPager.setCurrentItem(currentFragmentId);
        mViewPager.setOffscreenPageLimit(titles.size());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                L.d(TAG, "onPageScrolled");
                L.d(TAG, "position = " + position);
                L.d(TAG, "positionOffset = " + positionOffset);
                L.d(TAG, "positionOffsetPixels = " + positionOffsetPixels);

                if (positionOffsetPixels == 0) {
                    switch (position) {
                        case IMMEDIA_FRAGMENT:
                            mFilterImgBtn.setVisibility(View.VISIBLE);
                            ((ImmedBasketballFragment) fragments.get(position)).LoadData();
                            break;
                        case RESULT_FRAGMENT:
                            mFilterImgBtn.setVisibility(View.VISIBLE);
                            ((ResultBasketballFragment) fragments.get(position)).updateAdapter();
                            break;
                        case SCHEDULE_FRAGMENT:
                            mFilterImgBtn.setVisibility(View.VISIBLE);
                            ((ScheduleBasketballFragment) fragments.get(position)).updateAdapter();
                            break;
                    }
                }
            }

            @Override
            public void onPageSelected(final int position) {
                /**判断四个Fragment切换显示或隐藏的状态 */
                isHindShow(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void reconnectWebSocket() {
        connectWebSocket();
    }

  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mComeFromFocus == FocusBasketballFragment.TYPE_FOCUS) {
                EventBus.getDefault().post(new BasketFocusEventBus());
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.public_btn_set:  //设置
                currentFragmentId = mViewPager.getCurrentItem();
                if (currentFragmentId == IMMEDIA_FRAGMENT) {
                    MobclickAgent.onEvent(mContext, "Basketball_Setting");
                    mIntent = new Intent(mContext, BasketballSettingActivity.class);
                    Bundle bundleset = new Bundle();
                    bundleset.putInt("currentfragment", IMMEDIA_FRAGMENT);
                    mIntent.putExtras(bundleset);
                    startActivity(mIntent);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                } else if (currentFragmentId == RESULT_FRAGMENT) {
                    MobclickAgent.onEvent(mContext, "Basketball_Setting");
                    mIntent = new Intent(mContext, BasketballSettingActivity.class);
                    Bundle bundleset = new Bundle();
                    bundleset.putInt("currentfragment", RESULT_FRAGMENT);
                    mIntent.putExtras(bundleset);
                    startActivity(mIntent);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                } else if (currentFragmentId == SCHEDULE_FRAGMENT) {
                    MobclickAgent.onEvent(mContext, "Basketball_Setting");
                    mIntent = new Intent(getActivity(), BasketballSettingActivity.class);
                    Bundle bundleset = new Bundle();
                    bundleset.putInt("currentfragment", SCHEDULE_FRAGMENT);
                    mIntent.putExtras(bundleset);
                    startActivity(mIntent);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                }

                break;
            case R.id.public_btn_filter: //筛选

                currentFragmentId = mViewPager.getCurrentItem();
                if (currentFragmentId == IMMEDIA_FRAGMENT) {
                    if (ImmedBasketballFragment.getIsLoad() == 1) {
                        MobclickAgent.onEvent(mContext, "Basketball_Filter");
                        Intent intent = new Intent( getActivity(), BasketFiltrateActivity.class);
                        intent.putExtra("MatchAllFilterDatas", (Serializable) ImmedBasketballFragment.mAllFilter);//Serializable 序列化传值（所有联赛数据）
                        intent.putExtra("MatchChickedFilterDatas", (Serializable) ImmedBasketballFragment.mChickedFilter);//Serializable 序列化传值（选中的联赛数据）
                        intent.putExtra("currentfragment", IMMEDIA_FRAGMENT);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                    } else if (ImmedBasketballFragment.getIsLoad() == 0) {
                        Toast.makeText(mContext, getResources().getText(R.string.immediate_unconection), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, getResources().getText(R.string.basket_loading_txt), Toast.LENGTH_SHORT).show();
                    }
                    L.d("mBasketballType jishi >>>>>>>>>>>", "mBasketballType == >" + IMMEDIA_FRAGMENT);
                } else if (currentFragmentId == RESULT_FRAGMENT) {
                    if (ResultBasketballFragment.isLoad == 1) {
                        MobclickAgent.onEvent(mContext, "Basketball_Filter");
                        Intent intent = new Intent( getActivity(), BasketFiltrateActivity.class);
                        intent.putExtra("MatchAllFilterDatas", (Serializable) ResultBasketballFragment.mAllFilter);//Serializable 序列化传值（所有联赛数据）
                        intent.putExtra("MatchChickedFilterDatas", (Serializable) ResultBasketballFragment.mChickedFilter);//Serializable 序列化传值（选中的联赛数据）
                        Bundle bundle = new Bundle();
                        bundle.putInt("currentfragment", RESULT_FRAGMENT);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                    } else if (ResultBasketballFragment.isLoad == 0) {
                        Toast.makeText(mContext, getResources().getText(R.string.immediate_unconection), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, getResources().getText(R.string.basket_loading_txt), Toast.LENGTH_SHORT).show();
                    }
                    L.d("mBasketballType shaiguo >>>>>>>>>>>", "mBasketballType == >" + RESULT_FRAGMENT);
                } else if (currentFragmentId == SCHEDULE_FRAGMENT) {
                    if (ScheduleBasketballFragment.isLoad == 1) {
                        MobclickAgent.onEvent(mContext, "Basketball_Filter");
                        Intent intent = new Intent( getActivity(), BasketFiltrateActivity.class);
                        intent.putExtra("MatchAllFilterDatas", (Serializable) ScheduleBasketballFragment.mAllFilter);//Serializable 序列化传值（所有联赛数据）
                        intent.putExtra("MatchChickedFilterDatas", (Serializable) ScheduleBasketballFragment.mChickedFilter);//Serializable 序列化传值（选中的联赛数据）
                        Bundle bundle = new Bundle();
                        bundle.putInt("currentfragment", SCHEDULE_FRAGMENT);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                    } else if (ScheduleBasketballFragment.isLoad == 0) {
                        Toast.makeText(mContext, getResources().getText(R.string.immediate_unconection), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, getResources().getText(R.string.basket_loading_txt), Toast.LENGTH_SHORT).show();
                    }
                    L.d("mBasketballType shaicheng >>>>>>>>>>>", "mBasketballType == >" + SCHEDULE_FRAGMENT);
                }
                break;
        }
    }

    /**
     * 判断3个Fragment切换显示或隐藏的状态
     */
    private boolean isImmediateFragment = false;
    private boolean isImmediate = false;
    private boolean isResultFragment = false;
    private boolean isResult = false;
    private boolean isScheduleFragment = false;
    private boolean isSchedule = false;

    /**
     * 初始化当前显示的Fragment
     *
     * @param currentId
     */
    private void initCurrentFragment(int currentId) {
        switch (currentId) {
            case 0:
                isImmediateFragment = true;
                break;
            case 1:
                isResultFragment = true;
                break;
            case 2:
                isScheduleFragment = true;
                break;
        }
    }

    /**
     * 判断3个Fragment切换显示或隐藏的状态
     *
     * @param position
     */
    private void isHindShow(int position) {
        switch (position) {
            case IMMEDIA_FRAGMENT:
                isImmediateFragment = true;
                isResultFragment = false;
                isScheduleFragment = false;
                break;
            case RESULT_FRAGMENT:
                isResultFragment = true;
                isImmediateFragment = false;
                isScheduleFragment = false;
                break;
            case SCHEDULE_FRAGMENT:
                isScheduleFragment = true;
                isResultFragment = false;
                isImmediateFragment = false;
                break;
        }
        if (isImmediateFragment) {
            if (isResult) {
                MobclickAgent.onPageEnd("Basketball_ResultFragment");
                isResult = false;
                L.d("xxx", "ResultFragment>>>隐藏");
            }
            if (isSchedule) {
                MobclickAgent.onPageEnd("Basketball_ScheduleFragment");
                isSchedule = false;
                L.d("xxx", "ScheduleFragment>>>隐藏");
            }
            MobclickAgent.onPageStart("Basketball_ImmediateFragment");
            isImmediate = true;
            L.d("xxx", "ImmediateFragment>>>显示");
        }
        if (isResultFragment) {
            if (isImmediate) {
                MobclickAgent.onPageEnd("Basketball_ImmediateFragment");
                isImmediate = false;
                L.d("xxx", "ImmediateFragment>>>隐藏");
            }
            if (isSchedule) {
                MobclickAgent.onPageEnd("Basketball_ScheduleFragment");
                isSchedule = false;
                L.d("xxx", "ScheduleFragment>>>隐藏");
            }
            MobclickAgent.onPageStart("Basketball_ResultFragment");
            isResult = true;
            L.d("xxx", "ResultFragment>>>显示");
        }
        if (isScheduleFragment) {
            if (isImmediate) {
                MobclickAgent.onPageEnd("Basketball_ImmediateFragment");
                isImmediate = false;
                L.d("xxx", "ImmediateFragment>>>隐藏");
            }
            if (isResult) {
                MobclickAgent.onPageEnd("Basketball_ResultFragment");
                isResult = false;
                L.d("xxx", "ResultFragment>>>隐藏");
            }

            MobclickAgent.onPageStart("Basketball_ScheduleFragment");
            isSchedule = true;
            L.d("xxx", "ScheduleFragment>>>显示");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isImmediateFragment) {
            MobclickAgent.onPageStart("Basketball_ImmediateFragment");
            isImmediate = true;
            L.d("xxx", "ImmediateFragment>>>显示");
        }
        if (isResultFragment) {
            MobclickAgent.onPageStart("Basketball_ResultFragment");
            isResult = true;
            L.d("xxx", "ResultFragment>>>显示");
        }
        if (isScheduleFragment) {
            MobclickAgent.onPageStart("Basketball_ScheduleFragment");
            isSchedule = true;
            L.d("xxx", "ScheduleFragment>>>显示");
        }
        if (getActivity() != null) {
            connectWebSocket();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isImmediate) {
            MobclickAgent.onPageEnd("Basketball_ImmediateFragment");
            isImmediate = false;
            L.d("xxx", "ImmediateFragment>>>隐藏");
        }
        if (isResult) {
            MobclickAgent.onPageEnd("Basketball_ResultFragment");
            isResult = false;
            L.d("xxx", "ResultFragment>>>隐藏");
        }
        if (isSchedule) {
            MobclickAgent.onPageEnd("Basketball_ScheduleFragment");
            isSchedule = false;
            L.d("xxx", "ScheduleFragment>>>隐藏");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private void initEvent() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                L.d("wangg", "篮球===vvvvvvvvvvv==" + position);

                EventBus.getDefault().post(new ScoreSwitchFg(1, position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    protected void onTextResult(String text) {
        ((ImmedBasketballFragment) fragments.get(0)).handleSocketMessage(text);
    }

    @Override
    protected void onConnectFail() {

    }

    @Override
    protected void onDisconnected() {

    }

    @Override
    protected void onConnected() {

    }

}
