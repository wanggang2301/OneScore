package com.hhly.mlottery.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.PureViewPagerAdapter;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.basketballframe.BasketFocusEventBus;
import com.hhly.mlottery.frame.basketballframe.FocusBasketballFragment;
import com.hhly.mlottery.frame.basketballframe.ImmedBasketballFragment;
import com.hhly.mlottery.frame.basketballframe.ResultBasketballFragment;
import com.hhly.mlottery.frame.basketballframe.ScheduleBasketballFragment;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by yixq on 2016/12/2.
 * mail：yixq@13322.com
 * describe: 篮球比分Activity
 */

public class BasketballScoresActivity extends BaseWebSocketActivity implements View.OnClickListener {

    private final int IMMEDIA_FRAGMENT = 0;
    private final int RESULT_FRAGMENT = 1;
    private final int SCHEDULE_FRAGMENT = 2;
    private final int FOCUS_FRAGMENT = 3;

    private final static String TAG = "BasketScoresFragment";
    public static List<String> titles;

    private Context mContext;
    private Intent mIntent;
    private final int basketEntryType = 0;//标记入口（0：activity入口 1：fragment入口）
    /**
     * 返回菜单
     */
    private ImageView mIb_back;// 返回菜单

    /**
     * 筛选
     */
    public static ImageView mFilterImgBtn;// 筛选

    public static ImageView getmFilterImgBtn() {
        return mFilterImgBtn;
    }

    private ImageView ivInfomation;

    /**
     * 设置
     */
    public static ImageView mSetting;// 设置

    public static ImageView getmSetting() {
        return mSetting;
    }

    /**
     * 中间标题
     */
    private TextView mTittle;// 标题

    private final boolean isNewFrameWork = false;

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
        if (getIntent().getExtras() != null) {
            mComeFromFocus = getIntent().getExtras().getInt(FocusBasketballFragment.MY_BASKET_FOCUS, 0);
        }


        setContentView(R.layout.frage_new_basketball);
        mContext = getApplicationContext();
        initView();
        setupViewPager();
        basketFocusCallback();
        initCurrentFragment(currentFragmentId);

    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        //返回
        mIb_back = (ImageView) findViewById(R.id.public_img_back);
        mIb_back.setOnClickListener(this);

        //标题头部
        mTittle = (TextView) findViewById(R.id.public_txt_title);
        mTittle.setVisibility(View.GONE);

        // 筛选
        mFilterImgBtn = (ImageView) findViewById(R.id.public_btn_filter);
        mFilterImgBtn.setOnClickListener(this);


        //设置按钮
        mSetting = (ImageView) findViewById(R.id.public_btn_set);
        mSetting.setOnClickListener(this);


        ivInfomation = (ImageView) findViewById(R.id.public_btn_infomation);
        ivInfomation.setVisibility(View.VISIBLE);
        ivInfomation.setOnClickListener(this);
    }

    private void setupViewPager() {
        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        titles = new ArrayList<>();
        titles.add(getString(R.string.foot_jishi_txt));
        titles.add(getString(R.string.foot_saiguo_txt));
        titles.add(getString(R.string.foot_saicheng_txt));
        titles.add(getString(R.string.foot_guanzhu_txt));

        fragments = new ArrayList<>();
        fragments.add(ImmedBasketballFragment.newInstance(IMMEDIA_FRAGMENT, isNewFrameWork , basketEntryType));
        fragments.add(ResultBasketballFragment.newInstance(RESULT_FRAGMENT , basketEntryType));
        fragments.add(ScheduleBasketballFragment.newInstance(SCHEDULE_FRAGMENT , basketEntryType));
        fragments.add(FocusBasketballFragment.newInstance(FOCUS_FRAGMENT , basketEntryType));

        pureViewPagerAdapter = new PureViewPagerAdapter(fragments, titles, getSupportFragmentManager());
        mViewPager.setAdapter(pureViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        }else{
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

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
                        case FOCUS_FRAGMENT:
                            mFilterImgBtn.setVisibility(View.GONE);
                            ((FocusBasketballFragment) fragments.get(position)).LoadData();
                            break;
                    }
                }
            }

            @Override
            public void onPageSelected(final int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }
    /**
     * 刷新tab栏后的关注个数
     */
    public  void basketFocusCallback() {
        String focusIds = PreferenceUtil.getString(FocusBasketballFragment.BASKET_FOCUS_IDS, "");
        String[] arrayId = focusIds.split("[,]");
        if(getApplicationContext()!=null){
            if ("".equals(focusIds) || arrayId.length == 0) {
                mTabLayout.getTabAt(3).setText(getApplicationContext().getResources().getString(R.string.foot_guanzhu_txt));
            } else {
                mTabLayout.getTabAt(3).setText(getApplicationContext().getString(R.string.foot_guanzhu_txt) + "(" + arrayId.length + ")");
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    public void reconnectWebSocket() {
        connectWebSocket();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mComeFromFocus == FocusBasketballFragment.TYPE_FOCUS) {
                EventBus.getDefault().post(new BasketFocusEventBus());
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.public_img_back:  //返回
                MobclickAgent.onEvent(mContext, "Basketball_Exit");
//                ((MainActivity) getActivity()).openLeftLayout();
                if (mComeFromFocus == FocusBasketballFragment.TYPE_FOCUS) {
                    EventBus.getDefault().post(new BasketFocusEventBus());
                }
                finish();
                break;
            case R.id.public_btn_set:  //设置
                currentFragmentId = mViewPager.getCurrentItem();
                if (currentFragmentId == IMMEDIA_FRAGMENT) {
                    MobclickAgent.onEvent(mContext, "Basketball_Setting");
                    mIntent = new Intent(getApplicationContext(), BasketballSettingActivity.class);
                    Bundle bundleset = new Bundle();
                    bundleset.putInt("currentfragment", IMMEDIA_FRAGMENT);
                    mIntent.putExtras(bundleset);
                    startActivity(mIntent);
                    this.overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                } else if (currentFragmentId == RESULT_FRAGMENT) {
                    MobclickAgent.onEvent(mContext, "Basketball_Setting");
                    mIntent = new Intent(getApplicationContext(), BasketballSettingActivity.class);
                    Bundle bundleset = new Bundle();
                    bundleset.putInt("currentfragment", RESULT_FRAGMENT);
                    mIntent.putExtras(bundleset);
                    startActivity(mIntent);
                    this.overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                } else if (currentFragmentId == SCHEDULE_FRAGMENT) {
                    MobclickAgent.onEvent(mContext, "Basketball_Setting");
                    mIntent = new Intent(getApplicationContext(), BasketballSettingActivity.class);
                    Bundle bundleset = new Bundle();
                    bundleset.putInt("currentfragment", SCHEDULE_FRAGMENT);
                    mIntent.putExtras(bundleset);
                    startActivity(mIntent);
                    this.overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                } else if (currentFragmentId == FOCUS_FRAGMENT) {
                    MobclickAgent.onEvent(mContext, "Basketball_Setting");
//                String s = getAppVersionCode(mContext);
                    mIntent = new Intent(getApplicationContext(), BasketballSettingActivity.class);
//                getParentFragment().startActivityForResult(mIntent, REQUEST_SETTINGCODE);
//                getActivity().startActivityForResult(mIntent , REQUEST_SETTINGCODE);
                    Bundle bundleset = new Bundle();
                    bundleset.putInt("currentfragment", FOCUS_FRAGMENT);
                    mIntent.putExtras(bundleset);
                    startActivity(mIntent);

                    overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                }
                break;
            case R.id.public_btn_filter: //筛选

                currentFragmentId = mViewPager.getCurrentItem();
                if (currentFragmentId == IMMEDIA_FRAGMENT) {
                    if (ImmedBasketballFragment.getIsLoad() == 1) {
                        MobclickAgent.onEvent(mContext, "Basketball_Filter");
                        Intent intent = new Intent(getApplicationContext(), BasketFiltrateActivity.class);
                        intent.putExtra("MatchAllFilterDatas", (Serializable) ImmedBasketballFragment.mAllFilter);//Serializable 序列化传值（所有联赛数据）
                        intent.putExtra("MatchChickedFilterDatas", (Serializable) ImmedBasketballFragment.mChickedFilter);//Serializable 序列化传值（选中的联赛数据）
                        intent.putExtra("currentfragment", IMMEDIA_FRAGMENT);
                        startActivity(intent);
                        this.overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                    } else if (ImmedBasketballFragment.getIsLoad() == 0) {
                        Toast.makeText(mContext, getResources().getText(R.string.immediate_unconection), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, getResources().getText(R.string.basket_loading_txt), Toast.LENGTH_SHORT).show();
                    }
                    L.d("mBasketballType jishi >>>>>>>>>>>", "mBasketballType == >" + IMMEDIA_FRAGMENT);
                } else if (currentFragmentId == RESULT_FRAGMENT) {
                    if (ResultBasketballFragment.isLoad == 1) {
                        MobclickAgent.onEvent(mContext, "Basketball_Filter");
                        Intent intent = new Intent(getApplicationContext(), BasketFiltrateActivity.class);
                        intent.putExtra("MatchAllFilterDatas", (Serializable) ResultBasketballFragment.mAllFilter);//Serializable 序列化传值（所有联赛数据）
                        intent.putExtra("MatchChickedFilterDatas", (Serializable) ResultBasketballFragment.mChickedFilter);//Serializable 序列化传值（选中的联赛数据）
                        Bundle bundle = new Bundle();
                        bundle.putInt("currentfragment", RESULT_FRAGMENT);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        this.overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                    } else if (ResultBasketballFragment.isLoad == 0) {
                        Toast.makeText(mContext, getResources().getText(R.string.immediate_unconection), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, getResources().getText(R.string.basket_loading_txt), Toast.LENGTH_SHORT).show();
                    }
                    L.d("mBasketballType shaiguo >>>>>>>>>>>", "mBasketballType == >" + RESULT_FRAGMENT);
                } else if (currentFragmentId == SCHEDULE_FRAGMENT) {
                    if (ScheduleBasketballFragment.isLoad == 1) {
                        MobclickAgent.onEvent(mContext, "Basketball_Filter");
                        Intent intent = new Intent(getApplicationContext(), BasketFiltrateActivity.class);
                        intent.putExtra("MatchAllFilterDatas", (Serializable) ScheduleBasketballFragment.mAllFilter);//Serializable 序列化传值（所有联赛数据）
                        intent.putExtra("MatchChickedFilterDatas", (Serializable) ScheduleBasketballFragment.mChickedFilter);//Serializable 序列化传值（选中的联赛数据）
                        Bundle bundle = new Bundle();
                        bundle.putInt("currentfragment", SCHEDULE_FRAGMENT);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        this.overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                    } else if (ScheduleBasketballFragment.isLoad == 0) {
                        Toast.makeText(mContext, getResources().getText(R.string.immediate_unconection), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, getResources().getText(R.string.basket_loading_txt), Toast.LENGTH_SHORT).show();
                    }
                    L.d("mBasketballType shaicheng >>>>>>>>>>>", "mBasketballType == >" + SCHEDULE_FRAGMENT);
                }
                break;
            case R.id.public_btn_infomation:
                Intent intent = new Intent(getApplicationContext(), BasketballInformationActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
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

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (getApplicationContext() != null) {
            connectWebSocket();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
