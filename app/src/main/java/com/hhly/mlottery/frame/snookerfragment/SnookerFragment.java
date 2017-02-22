package com.hhly.mlottery.frame.snookerfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.SnookerSettingActivity;
import com.hhly.mlottery.adapter.PureViewPagerAdapter;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.L;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by yixq on 2017/2/16.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerFragment extends BaseWebSocketFragment implements View.OnClickListener {

    private final int SNOOKER_IMMEDIA_FRAGMENT = 0;
    private final int SNOOKER_RESULT_FRAGMENT = 1;
    private final int SNOOKER_SCHEDULE_FRAGMENT = 2;
    private final int SNOOKER_FOCUS_FRAGMENT = 3;

    private View mView;
    private ImageView mSetting;
    private TabLayout mTabs;
    private ViewPager mPager;
    private PureViewPagerAdapter mViewPagerAdapter;
    private List<Fragment> fragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /**
         * 推送  必须定义在 super.onCreate前面，否则 订阅不成功
         */
//        setWebSocketUri("ws://192.168.10.242:61634");
        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic("USER.topic.snooker");
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_snooker , container , false);
        initView();
        setViewpager();
        return mView;
    }
    private void initView(){
        //头部数据
        ImageView back = (ImageView) mView.findViewById(R.id.public_img_back);
        back.setVisibility(View.GONE);
        ImageView filter = (ImageView) mView.findViewById(R.id.public_btn_filter);
        filter.setVisibility(View.GONE);
        TextView title = (TextView) mView.findViewById(R.id.public_txt_title);
        title.setText(getString(R.string.snooker_title));
        //设置
        mSetting = (ImageView) mView.findViewById(R.id.public_btn_set);
        mSetting.setOnClickListener(this);

        mPager = (ViewPager) mView.findViewById(R.id.snooker_viewpager);
        mTabs = (TabLayout) mView.findViewById(R.id.snooker_tabs);

    }
    private void setViewpager(){

        List<String> tabLists = new ArrayList<>();
        tabLists.add(getString(R.string.foot_jishi_txt));
        tabLists.add(getString(R.string.foot_saiguo_txt));
        tabLists.add(getString(R.string.foot_saicheng_txt));
//        tabLists.add("关注");

        fragments = new ArrayList<>();
        fragments.add(SnookerImmediateFragment.newInstance());
        fragments.add(SnookerResultFragment.newInstance());
        fragments.add(SnookerScheduleFragment.newInstance());

        mViewPagerAdapter = new PureViewPagerAdapter(fragments, tabLists , getChildFragmentManager());

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (positionOffsetPixels == 0) {
                    switch (position){
                        case SNOOKER_IMMEDIA_FRAGMENT:
                            ((SnookerImmediateFragment)fragments.get(position)).loadData();
                            break;
                        case SNOOKER_RESULT_FRAGMENT:
                            ((SnookerResultFragment)fragments.get(position)).loadData();
                            break;
                        case SNOOKER_SCHEDULE_FRAGMENT:
                            ((SnookerScheduleFragment)fragments.get(position)).loadData();
                            break;
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                /**判断五个Fragment切换显示或隐藏的状态 */
                isHindShow(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.setAdapter(mViewPagerAdapter);
        mTabs.setupWithViewPager(mPager);
        mTabs.setTabMode(TabLayout.MODE_FIXED);
        mPager.setOffscreenPageLimit(tabLists.size());

    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onPause();
        } else {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isImmediateFragment) {
            isImmediate = true;
        }
        if (isResultFragment) {
            isResult = true;
        }
        if (isScheduleFragment) {
            isSchedule = true;
        }
        if (isFocusFragment) {
            isFocus = true;
        }
        if (getActivity() != null) {
            connectWebSocket();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isImmediate) {
            isImmediate = false;
            L.d("xxx", "即时页面>>>隐藏");
        }
        if (isResult) {
            isResult = false;
            L.d("xxx", "赛果页面>>>隐藏");
        }
        if (isSchedule) {
            isSchedule = false;
            L.d("xxx", "赛程页面>>>隐藏");
        }
        if (isFocus) {
            isFocus = false;
            L.d("xxx", "关注页面>>>隐藏");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 判断五个Fragment切换显示或隐藏的状态
     */
    private boolean isImmediateFragment = true;
    private boolean isImmediate = false;
    private boolean isResultFragment = false;
    private boolean isResult = false;
    private boolean isScheduleFragment = false;
    private boolean isSchedule = false;
    private boolean isFocusFragment = false;
    private boolean isFocus = false;

    /**
     * fragment 切换的显示隐藏状态判断
     * @param position
     */
    private void isHindShow(int position){
        switch(position){
            case SNOOKER_IMMEDIA_FRAGMENT:
                isImmediateFragment = true;
                isResultFragment = false;
                isScheduleFragment = false;
                isFocusFragment = false;
                break;
            case SNOOKER_RESULT_FRAGMENT:
                isImmediateFragment = false;
                isResultFragment = true;
                isScheduleFragment = false;
                isFocusFragment = false;
                break;
            case SNOOKER_SCHEDULE_FRAGMENT:
                isImmediateFragment = false;
                isResultFragment = false;
                isScheduleFragment = true;
                isFocusFragment = false;
                break;
            case SNOOKER_FOCUS_FRAGMENT:
                isImmediateFragment = false;
                isResultFragment = false;
                isScheduleFragment = false;
                isFocusFragment = true;
                break;
        }
        if (isImmediateFragment) {
            if (isResult) {
                isResult = false;
            }
            if (isSchedule) {
                isSchedule = false;
            }
            if (isFocus) {
                isFocus = false;
            }
            isImmediate = true;
        }
        if (isResultFragment) {
            if (isImmediate) {
                isImmediate = false;
            }
            if (isSchedule) {
                isSchedule = false;
            }
            if (isFocus) {
                isFocus = false;
            }
            isResult = true;
        }
        if (isScheduleFragment) {
            if (isImmediate) {
                isImmediate = false;
            }
            if (isResult) {
                isResult = false;
            }
            if (isFocus) {
                isFocus = false;
            }
            isSchedule = true;
        }
        if (isFocusFragment) {
            if (isImmediate) {
                isImmediate = false;
            }
            if (isResult) {
                isResult = false;
            }
            if (isSchedule) {
                isSchedule = false;
            }
            isFocus = true;
        }

    }

    /**实现推送方法实现推送方法实现推送方法实现推送方法实现推送方法实现推送方法实现推送方法实现推送方法实现推送方法实现推送方法*/

    /***
     * 即时界面接受推送的消息类
     */
    public class SnookerScoresWebSocketEntity {
        public String text;

        SnookerScoresWebSocketEntity(String text) {
            this.text = text;
        }
    }


    @Override
    protected void onTextResult(String text) {
        L.d("yxq", "收到消息==" + text);
        EventBus.getDefault().post(new SnookerScoresWebSocketEntity(text)); //收到的消息传到即时页面
    }
    public void reconnectWebSocket() {
        connectWebSocket();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_btn_set:
                Intent intent = new Intent(getActivity(), SnookerSettingActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                break;
        }
    }
}
