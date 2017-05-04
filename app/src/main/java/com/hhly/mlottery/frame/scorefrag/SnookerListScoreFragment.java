package com.hhly.mlottery.frame.scorefrag;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.SnookerSettingActivity;
import com.hhly.mlottery.adapter.PureViewPagerAdapter;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BaseUserTopics;
import com.hhly.mlottery.frame.BallType;
import com.hhly.mlottery.frame.snookerframe.SnookerImmediateFragment;
import com.hhly.mlottery.frame.snookerframe.SnookerResultFragment;
import com.hhly.mlottery.frame.snookerframe.SnookerScheduleFragment;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.BallChoiceArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by yixq on 2017/2/16.
 * mail：yixq@13322.com
 * describe: snooker 比分
 */

public class SnookerListScoreFragment extends BaseWebSocketFragment implements View.OnClickListener {

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
    private LinearLayout ll_match_select;
    private TextView tv_match_name;
    private ImageView iv_match;
    private LinearLayout d_header;
    private String[] mItems;//头部标签集

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /**
         * 推送  必须定义在 super.onCreate前面，否则 订阅不成功
         */
//        setWebSocketUri("ws://192.168.10.242:61634");
        setWebSocketUri(BaseURLs.WS_SERVICE);
//        setTopic("USER.topic.snooker");
        setTopic(BaseUserTopics.snookerMatch);
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_snooker, container, false);
        initView();
        setViewpager();
        initEvent();
        return mView;
    }

    private void initView() {
        mItems = getResources().getStringArray(R.array.bifen_select);//切换标签
        //头部数据(隐藏筛选图标)
        ImageView filter = (ImageView) mView.findViewById(R.id.public_btn_filter);
        filter.setVisibility(View.GONE);
        //设置
        mSetting = (ImageView) mView.findViewById(R.id.public_btn_set);
        mSetting.setOnClickListener(this);

        mPager = (ViewPager) mView.findViewById(R.id.snooker_viewpager);
        mTabs = (TabLayout) mView.findViewById(R.id.snooker_tabs);

        ll_match_select = (LinearLayout) mView.findViewById(R.id.ll_match_select);
        tv_match_name = (TextView) mView.findViewById(R.id.tv_match_name);
        iv_match = (ImageView) mView.findViewById(R.id.iv_match);
        d_header = (LinearLayout) mView.findViewById(R.id.d_heasder);

        tv_match_name.setText(getResources().getString(R.string.snooker_txt));

    }

    private void initEvent() {
        ll_match_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_match.setImageResource(R.mipmap.nav_icon_up);
                backgroundAlpha(getActivity(), 0.5f);
                popWindow(v);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    private void popWindow(final View v) {
        final View mView = View.inflate(getActivity(), R.layout.pop_select, null);
        // 创建ArrayAdapter对象
        BallChoiceArrayAdapter mAdapter = new BallChoiceArrayAdapter(getActivity(), mItems, BallType.SNOOKER);

        ListView listview = (ListView) mView.findViewById(R.id.match_type);
        listview.setAdapter(mAdapter);


        final PopupWindow popupWindow = new PopupWindow(mView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAsDropDown(d_header);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // tv_match_name.setText(((TextView) view.findViewById(R.id.tv)).getText().toString());
                //  iv_match.setImageResource(R.mipmap.nav_icon_cbb);
                L.d("websocket123", ">>>>>>>>斯洛克比分关闭");

                closeWebSocket();

                EventBus.getDefault().post(new ScoreSwitchFg(position));

                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv_match.setImageResource(R.mipmap.nav_icon_cbb);
                backgroundAlpha(getActivity(), 1f);
            }
        });
    }

    private void setViewpager() {

        List<String> tabLists = new ArrayList<>();
        tabLists.add(getString(R.string.foot_jishi_txt));
        tabLists.add(getString(R.string.foot_saiguo_txt));
        tabLists.add(getString(R.string.foot_saicheng_txt));

        fragments = new ArrayList<>();
        fragments.add(SnookerImmediateFragment.newInstance());
        fragments.add(SnookerResultFragment.newInstance());
        fragments.add(SnookerScheduleFragment.newInstance());

        mViewPagerAdapter = new PureViewPagerAdapter(fragments, tabLists, getChildFragmentManager());

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (positionOffsetPixels == 0) {
                    switch (position) {
                        case SNOOKER_IMMEDIA_FRAGMENT:
                            ((SnookerImmediateFragment) fragments.get(position)).loadData();
                            break;
                        case SNOOKER_RESULT_FRAGMENT:
                            ((SnookerResultFragment) fragments.get(position)).loadData();
                            break;
                        case SNOOKER_SCHEDULE_FRAGMENT:
                            ((SnookerScheduleFragment) fragments.get(position)).loadData();
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeWebSocket();
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
     *
     * @param position
     */
    private void isHindShow(int position) {
        switch (position) {
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
        L.d("websocket123", "斯洛克收到消息==" + text);
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
        switch (v.getId()) {
            case R.id.public_btn_set:
                Intent intent = new Intent(getActivity(), SnookerSettingActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                break;
        }
    }


    public void onEventMainThread(CloseWebSocketEventBus closeWebSocketEventBus) {

        if (closeWebSocketEventBus.isVisible()) {
            L.d("websocket123", "_________斯洛克 比分 关闭 fg");
            closeWebSocket();
        } else {
            if (closeWebSocketEventBus.getIndex() == BallType.SNOOKER) {
                L.d("websocket123", "_________斯洛克 比分 打开 fg");
                connectWebSocket();
            }
        }
    }
}
