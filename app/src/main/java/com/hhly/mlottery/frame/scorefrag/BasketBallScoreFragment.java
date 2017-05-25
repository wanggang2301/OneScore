package com.hhly.mlottery.frame.scorefrag;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
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
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.widget.MsgView;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketBallMatchSearchActivity;
import com.hhly.mlottery.activity.BasketFiltrateActivity;
import com.hhly.mlottery.activity.BasketballSettingActivity;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BaseUserTopics;
import com.hhly.mlottery.frame.BallType;
import com.hhly.mlottery.frame.basketballframe.basketnewfragment.BasketImmedNewScoreFragment;
import com.hhly.mlottery.frame.basketballframe.basketnewfragment.BasketResultNewScoreFragment;
import com.hhly.mlottery.frame.basketballframe.basketnewfragment.BasketScheduleNewScoreFragment;
import com.hhly.mlottery.frame.basketballframe.basketnewfragment.BasketballFocusNewFragment;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.widget.BallChoiceArrayAdapter;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



import de.greenrobot.event.EventBus;

/**
 * 篮球比分
 */
public class BasketBallScoreFragment extends BaseWebSocketFragment implements View.OnClickListener {

    private final boolean isNewFrameWork = true;

    private String[] mItems;
    private View view;
    private Context mContext;


    private final int IMMEDIA_FRAGMENT = 0;
    private final int RESULT_FRAGMENT = 1;
    private final int SCHEDULE_FRAGMENT = 2;
    private final int FOCUS_FRAGMENT = 3;

    private final static String TAG = "BasketScoresFragment";

    private Intent mIntent;
    private final int basketEntryType = 1;

    /**
     * 筛选
     */
    public static ImageView mFilterImgBtn;// 筛选
    private ImageView public_search_filter;

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
    private SlidingTabLayout mTabLayout;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private List<Fragment> fragments;
    /**
     * 判断是否来自关注页面
     */
    int mComeFromFocus = 0;

    private LinearLayout d_header;
    private LinearLayout ll_match_select;

    private TextView tv_match_name;
    private ImageView iv_match;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setWebSocketUri(BaseURLs.WS_SERVICE);
//        setTopic("USER.topic.basketball");
        setTopic(BaseUserTopics.oddsBasket);
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        view = inflater.inflate(R.layout.fragment_basket_ball_score, container, false);
        initView();
        setupViewPager();
        focusCallback();// 加载关注数
        initCurrentFragment(currentFragmentId);


        initEvent();
        return view;
    }

    private void initView() {
        mItems = getResources().getStringArray(R.array.bifen_select);


        mViewPager = (ViewPager) view.findViewById(R.id.pager);


        // 筛选
        mFilterImgBtn = (ImageView) view.findViewById(R.id.public_btn_filter);
        mFilterImgBtn.setOnClickListener(this);


        //设置按钮
        mSetting = (ImageView) view.findViewById(R.id.public_btn_set);
        mSetting.setOnClickListener(this);
        ll_match_select = (LinearLayout) view.findViewById(R.id.ll_match_select);
        tv_match_name = (TextView) view.findViewById(R.id.tv_match_name);
        iv_match = (ImageView) view.findViewById(R.id.iv_match);
        d_header = (LinearLayout) view.findViewById(R.id.d_heasder);
        tv_match_name.setText(getResources().getString(R.string.basketball_txt));

        //搜索
        public_search_filter = (ImageView) view.findViewById(R.id.public_search_filter);
        public_search_filter.setVisibility(View.VISIBLE);
        public_search_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BasketBallMatchSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupViewPager() {
        mTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);


        String[] tabNames = {getString(R.string.foot_jishi_txt), getString(R.string.foot_saiguo_txt), getString(R.string.foot_saicheng_txt), getString(R.string.foot_guanzhu_txt)};


        fragments = new ArrayList<>();
        fragments.add(BasketImmedNewScoreFragment.newInstance(IMMEDIA_FRAGMENT, isNewFrameWork, basketEntryType));
        fragments.add(BasketResultNewScoreFragment.newInstance(RESULT_FRAGMENT, basketEntryType));
        fragments.add(BasketScheduleNewScoreFragment.newInstance(SCHEDULE_FRAGMENT, basketEntryType));

        fragments.add(BasketballFocusNewFragment.newInstance(FOCUS_FRAGMENT, basketEntryType));


        fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };


        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(tabNames.length);

        mTabLayout.setViewPager(mViewPager, tabNames);
        mTabLayout.showDot(FOCUS_FRAGMENT);
        mTabLayout.showMsg(FOCUS_FRAGMENT, 0);
        mTabLayout.setMsgMargin(FOCUS_FRAGMENT, 12, 7);
        mTabLayout.hideMsg(FOCUS_FRAGMENT);
        MsgView msgView = mTabLayout.getMsgView(FOCUS_FRAGMENT);
        if (msgView != null) {
            msgView.setBackgroundColor(Color.parseColor("#ffde00"));
            msgView.setTextColor(Color.parseColor("#0085e1"));
            msgView.setTextSize(mContext.getResources().getDimension(R.dimen.text_size_5));
        }


        if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) {
            mTabLayout.setTabSpaceEqual(true);
        } else {
            mTabLayout.setTabSpaceEqual(false);
        }

        mViewPager.setCurrentItem(currentFragmentId);
        mViewPager.setOffscreenPageLimit(tabNames.length);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                L.d(TAG, "onPageScrolled");
                L.d(TAG, "position = " + position);
                L.d(TAG, "positionOffset = " + positionOffset);
                L.d(TAG, "positionOffsetPixels = " + positionOffsetPixels);

                if (positionOffsetPixels == 0) {
                    switch (position) {
//                        case IMMEDIA_FRAGMENT:
//                            mFilterImgBtn.setVisibility(View.VISIBLE);
//                            ((ImmedBasketballFragment) fragments.get(position)).LoadData();
//                            break;
//                        case RESULT_FRAGMENT:
//                            mFilterImgBtn.setVisibility(View.VISIBLE);
//                            ((ResultBasketballFragment) fragments.get(position)).updateAdapter();
//                            break;
//                        case SCHEDULE_FRAGMENT:
//                            mFilterImgBtn.setVisibility(View.VISIBLE);
//                            ((ScheduleBasketballFragment) fragments.get(position)).updateAdapter();
//                            break;
                        case 0:
                            mFilterImgBtn.setVisibility(View.VISIBLE);
                            ((BasketImmedNewScoreFragment) fragments.get(position)).LoadData();
                            break;
                        case 1:
                            mFilterImgBtn.setVisibility(View.VISIBLE);
                            ((BasketResultNewScoreFragment) fragments.get(position)).updateAdapter();
                            break;
                        case 2:
                            mFilterImgBtn.setVisibility(View.VISIBLE);
                            ((BasketScheduleNewScoreFragment) fragments.get(position)).updateAdapter();
                            break;
                        case FOCUS_FRAGMENT:
                            mFilterImgBtn.setVisibility(View.GONE);
                            ((BasketballFocusNewFragment) fragments.get(position)).LoadData();
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

    /**
     * 刷新tab栏后的关注个数
     */
    public void focusCallback() {
        String focusIds = PreferenceUtil.getString(BasketballFocusNewFragment.BASKET_FOCUS_IDS, "");
        String[] arrayId = focusIds.split("[,]");
        if (getActivity() != null) {
            if ("".equals(focusIds) || arrayId.length == 0) {
                mTabLayout.hideMsg(FOCUS_FRAGMENT);
            } else {
                mTabLayout.showMsg(FOCUS_FRAGMENT, arrayId.length);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeWebSocket();
    }


    public void reconnectWebSocket() {
        connectWebSocket();
    }

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
                } else if (currentFragmentId == FOCUS_FRAGMENT) {
                    MobclickAgent.onEvent(mContext, "Basketball_Setting");
//                String s = getAppVersionCode(mContext);
                    mIntent = new Intent(getActivity(), BasketballSettingActivity.class);
//                getParentFragment().startActivityForResult(mIntent, REQUEST_SETTINGCODE);
//                getActivity().startActivityForResult(mIntent , REQUEST_SETTINGCODE);
                    Bundle bundleset = new Bundle();
                    bundleset.putInt("currentfragment", FOCUS_FRAGMENT);
                    mIntent.putExtras(bundleset);
                    startActivity(mIntent);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                }
                break;
            case R.id.public_btn_filter: //筛选

                currentFragmentId = mViewPager.getCurrentItem();
                if (currentFragmentId == IMMEDIA_FRAGMENT) {
                    if (BasketImmedNewScoreFragment.getIsLoad() == 1) {
                        MobclickAgent.onEvent(mContext, "Basketball_Filter");
                        Intent intent = new Intent(getActivity(), BasketFiltrateActivity.class);

                        intent.putExtra("MatchAllFilterDatas", (Serializable) BasketImmedNewScoreFragment.mAllFilter);//Serializable 序列化传值（所有联赛数据）
                        intent.putExtra("MatchChickedFilterDatas", (Serializable) BasketImmedNewScoreFragment.mChickedFilter);//Serializable 序列化传值（选中的联赛数据）
                        intent.putExtra("currentfragment", IMMEDIA_FRAGMENT);

                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                    } else if (BasketImmedNewScoreFragment.getIsLoad() == 0) {
                        Toast.makeText(mContext, getResources().getText(R.string.immediate_unconection), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, getResources().getText(R.string.basket_loading_txt), Toast.LENGTH_SHORT).show();
                    }
                    L.d("mBasketballType jishi >>>>>>>>>>>", "mBasketballType == >" + IMMEDIA_FRAGMENT);
                } else if (currentFragmentId == RESULT_FRAGMENT) {
                    if (BasketResultNewScoreFragment.isLoad == 1) {
                        MobclickAgent.onEvent(mContext, "Basketball_Filter");
                        Intent intent = new Intent(getActivity(), BasketFiltrateActivity.class);
                        intent.putExtra("MatchAllFilterDatas", (Serializable) BasketResultNewScoreFragment.mAllFilter);//Serializable 序列化传值（所有联赛数据）
                        intent.putExtra("MatchChickedFilterDatas", (Serializable) BasketResultNewScoreFragment.mChickedFilter);//Serializable 序列化传值（选中的联赛数据）
                        Bundle bundle = new Bundle();
                        bundle.putInt("currentfragment", RESULT_FRAGMENT);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                    } else if (BasketResultNewScoreFragment.isLoad == 0) {
                        Toast.makeText(mContext, getResources().getText(R.string.immediate_unconection), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, getResources().getText(R.string.basket_loading_txt), Toast.LENGTH_SHORT).show();
                    }
                    L.d("mBasketballType shaiguo >>>>>>>>>>>", "mBasketballType == >" + RESULT_FRAGMENT);
                } else if (currentFragmentId == SCHEDULE_FRAGMENT) {
                    if (BasketScheduleNewScoreFragment.isLoad == 1) {
                        MobclickAgent.onEvent(mContext, "Basketball_Filter");
                        Intent intent = new Intent(getActivity(), BasketFiltrateActivity.class);
                        intent.putExtra("MatchAllFilterDatas", (Serializable) BasketScheduleNewScoreFragment.mAllFilter);//Serializable 序列化传值（所有联赛数据）
                        intent.putExtra("MatchChickedFilterDatas", (Serializable) BasketScheduleNewScoreFragment.mChickedFilter);//Serializable 序列化传值（选中的联赛数据）
                        Bundle bundle = new Bundle();
                        bundle.putInt("currentfragment", SCHEDULE_FRAGMENT);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                    } else if (BasketScheduleNewScoreFragment.isLoad == 0) {
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
    private boolean isFocusFragment = false;
    private boolean isFocus = false;

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
            case 3:
                isFocusFragment = true;
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
                isFocusFragment = false;
                break;
            case RESULT_FRAGMENT:
                isResultFragment = true;
                isImmediateFragment = false;
                isScheduleFragment = false;
                isFocusFragment = false;
                break;
            case SCHEDULE_FRAGMENT:
                isScheduleFragment = true;
                isResultFragment = false;
                isImmediateFragment = false;
                isFocusFragment = false;
                break;
            case FOCUS_FRAGMENT:
                isFocusFragment = true;
                isScheduleFragment = false;
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
            if (isFocus) {
                MobclickAgent.onPageEnd("Basketball_FocusFragment");
                isFocus = false;
                L.d("xxx", "FocusFragment>>>隐藏");
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
            if (isFocus) {
                MobclickAgent.onPageEnd("Basketball_FocusFragment");
                isFocus = false;
                L.d("xxx", "FocusFragment>>>隐藏");
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
            if (isFocus) {
                MobclickAgent.onPageEnd("Basketball_FocusFragment");
                isFocus = false;
                L.d("xxx", "FocusFragment>>>隐藏");
            }
            MobclickAgent.onPageStart("Basketball_ScheduleFragment");
            isSchedule = true;
            L.d("xxx", "ScheduleFragment>>>显示");
        }
        if (isFocusFragment) {
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
            MobclickAgent.onPageStart("Basketball_FocusFragment");
            isFocus = true;
            L.d("xxx", "FocusFragment>>>显示");
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
        if (isFocusFragment) {
            MobclickAgent.onPageStart("Basketball_FocusFragment");
            isFocus = true;
            L.d("xxx", "FocusFragment>>>显示");
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
        if (isFocus) {
            MobclickAgent.onPageEnd("Basketball_FocusFragment");
            isFocus = false;
            L.d("xxx", "FocusFragment>>>隐藏");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
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

    private void popWindow(final View v) {
        final View mView = View.inflate(mContext, R.layout.pop_select, null);
        // 创建ArrayAdapter对象
        BallChoiceArrayAdapter mAdapter = new BallChoiceArrayAdapter(mContext, mItems, BallType.BASKETBALL);

        ListView listview = (ListView) mView.findViewById(R.id.match_type);
        listview.setAdapter(mAdapter);


        final PopupWindow popupWindow = new PopupWindow(mView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAsDropDown(d_header);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                L.d("websocket123", ">>>>>>>>篮球比分关闭");
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


    @Override
    protected void onTextResult(String text) {
        L.d("websocket123", "篮球收到消息==" + text);

        ((BasketImmedNewScoreFragment) fragments.get(0)).handleSocketMessage(text);
        ((BasketballFocusNewFragment) fragments.get(3)).handleSocketMessage(text);
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
//            L.d("xxx", ">>>篮球>>>>hidden");
            onPause();
        } else {
//            L.d("xxx", ">>>篮球>>>>show");
            onResume();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        closeWebSocket();
    }

    public void onEventMainThread(CloseWebSocketEventBus closeWebSocketEventBus) {

        if (closeWebSocketEventBus.isVisible()) {
            L.d("websocket123", "_________篮球 比分 关闭 fg");
            closeWebSocket();
        } else {
            if (closeWebSocketEventBus.getIndex() == BallType.BASKETBALL) {
                L.d("websocket123", "_________篮球 比分 关闭 fg");

                connectWebSocket();
            }
        }
    }
}
