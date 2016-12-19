package com.hhly.mlottery.frame.basketballframe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketFiltrateActivity;
import com.hhly.mlottery.activity.BasketballInformationActivity;
import com.hhly.mlottery.activity.BasketballSettingActivity;
import com.hhly.mlottery.activity.FootballActivity;
import com.hhly.mlottery.adapter.PureViewPagerAdapter;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.bean.focusAndPush.BasketballConcernListBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.BallSelectArrayAdapter;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yixq
 * @ClassName: BasketScoresFragment
 * @Description: 篮球比分
 */
@SuppressLint("NewApi")
public class BasketScoresFragment extends BaseWebSocketFragment implements View.OnClickListener {

    private final int IMMEDIA_FRAGMENT = 0;
    private final int RESULT_FRAGMENT = 1;
    private final int SCHEDULE_FRAGMENT = 2;
    private final int FOCUS_FRAGMENT = 3;

    private final static String TAG = "BasketScoresFragment";
    public static List<String> titles;

    private View mView;
    private Context mContext;
    private Intent mIntent;

//    private int isLoad ;
//    private ImageView mSetting;//设置按钮
//    private ImageView mIb_back;//返回按钮
//    private TextView mTittle; //标题头
//    private ImageView mFilterImgBtn;// 筛选
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


    /**
     * 当前处于哪个比赛fg
     */
    private int currentFragmentId = 0;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private PureViewPagerAdapter pureViewPagerAdapter;
    private List<Fragment> fragments;
    private Spinner mSpinner;
    private String[] mItems;

    @SuppressLint("ValidFragment")
    public BasketScoresFragment(Context context) {
        this.mContext = context;
    }

    public BasketScoresFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic("USER.topic.basketball");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = View.inflate(mContext, R.layout.frage_new_basketball, null);
        currentFragmentId = ((FootballActivity) mContext).basketCurrentPosition;
        L.d("xxx", "篮球Fragment:currentFragmentId:" + currentFragmentId);
        initView();
        setupViewPager();
        focusCallback();// 加载关注数
        initEVent();
        initCurrentFragment(currentFragmentId);
        return mView;
    }

    private void initEVent() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mContext.getResources().getString(R.string.football_frame_txt).equals(mItems[position])) {// 选择足球
                    ((FootballActivity) mContext).ly_tab_bar.setVisibility(View.VISIBLE);
                    ((FootballActivity) mContext).switchFragment(0);
                    closeWebSocket();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView() {
        mViewPager = (ViewPager) mView.findViewById(R.id.pager);
        //返回
        mIb_back = (ImageView) mView.findViewById(R.id.public_img_back);
        mIb_back.setOnClickListener(this);

        //标题头部
        mTittle = (TextView) mView.findViewById(R.id.public_txt_title);
        mTittle.setVisibility(View.GONE);

        mSpinner = (Spinner) mView.findViewById(R.id.public_txt_left_spinner);
        mSpinner.setVisibility(View.VISIBLE);
        mItems = getResources().getStringArray(R.array.ball_select);
        BallSelectArrayAdapter mAdapter = new BallSelectArrayAdapter(mContext, mItems);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setSelection(1);

        // 筛选
        mFilterImgBtn = (ImageView) mView.findViewById(R.id.public_btn_filter);
        mFilterImgBtn.setOnClickListener(this);


        //设置按钮
        mSetting = (ImageView) mView.findViewById(R.id.public_btn_set);
        mSetting.setOnClickListener(this);


        ivInfomation = (ImageView) mView.findViewById(R.id.public_btn_infomation);
        ivInfomation.setVisibility(View.VISIBLE);
        ivInfomation.setOnClickListener(this);
    }

    private void setupViewPager() {
        mTabLayout = (TabLayout) mView.findViewById(R.id.sliding_tabs);
        titles = new ArrayList<>();
        titles.add(getString(R.string.foot_jishi_txt));
        titles.add(getString(R.string.foot_saiguo_txt));
        titles.add(getString(R.string.foot_saicheng_txt));
        titles.add(getString(R.string.foot_guanzhu_txt));

        fragments = new ArrayList<>();
        fragments.add(ImmedBasketballFragment.newInstance(IMMEDIA_FRAGMENT));
        fragments.add(ResultBasketballFragment.newInstance(RESULT_FRAGMENT));
        fragments.add(ScheduleBasketballFragment.newInstance(SCHEDULE_FRAGMENT));
        fragments.add(FocusBasketballFragment.newInstance(FOCUS_FRAGMENT));

        pureViewPagerAdapter = new PureViewPagerAdapter(fragments, titles, getChildFragmentManager());
        mViewPager.setAdapter(pureViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mViewPager.setCurrentItem(currentFragmentId);
        mViewPager.setOffscreenPageLimit(4);
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
        String focusIds = PreferenceUtil.getString(FocusBasketballFragment.BASKET_FOCUS_IDS, "");
        String[] arrayId = focusIds.split("[,]");
        if (getActivity() != null) {
            if ("".equals(focusIds) || arrayId.length == 0) {
                mTabLayout.getTabAt(3).setText(getActivity().getResources().getString(R.string.foot_guanzhu_txt));
            } else {
                mTabLayout.getTabAt(3).setText(getActivity().getResources().getString(R.string.foot_guanzhu_txt) + "(" + arrayId.length + ")");
            }
        }

    }

    /**
     * 请求关注列表。登录后跟刷新，都会请求
     */
    public void getBasketballUserConcern() {
        //请求后台，及时更新关注赛事内容
        String userId = "";
        if (AppConstants.register != null && AppConstants.register.getData() != null && AppConstants.register.getData().getUser() != null) {
            userId = AppConstants.register.getData().getUser().getUserId();
        }
        if (userId != null && userId != "") {
            String url = " http://192.168.31.68:8080/mlottery/core/androidBasketballMatch.findConcernVsThirdIds.do";
            String deviceId = AppConstants.deviceToken;
            //devicetoken 友盟。
            String umengDeviceToken = PreferenceUtil.getString(AppConstants.uMengDeviceToken, "");
            Map<String, String> params = new HashMap<>();
            params.put("userId", userId);
            Log.e("AAA", userId + "用户名");
            params.put("deviceId", deviceId);
//        params.put("deviceToken",umengDeviceToken);
            VolleyContentFast.requestJsonByPost(BaseURLs.BASKET_FIND_MATCH, params, new VolleyContentFast.ResponseSuccessListener<BasketballConcernListBean>() {
                @Override
                public void onResponse(BasketballConcernListBean jsonObject) {
                    if (jsonObject != null) {
                        if (jsonObject.getResult().equals("200")) {
                            Log.e("AAA", "登陆后请求的篮球关注列表");
                            //将关注写入文件
                            StringBuffer sb = new StringBuffer();
                            for (String thirdId : jsonObject.getConcerns()) {
                                if ("".equals(sb.toString())) {
                                    sb.append(thirdId);
                                } else {
                                    sb.append("," + thirdId);
                                }

                            }
                            PreferenceUtil.commitString(FocusBasketballFragment.BASKET_FOCUS_IDS, sb.toString());
                            focusCallback();
                        }
                    }

                }
            }, new VolleyContentFast.ResponseErrorListener() {
                @Override
                public void onErrorResponse(VolleyContentFast.VolleyException exception) {


                }
            }, BasketballConcernListBean.class);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onTextResult(String text) {
        ((ImmedBasketballFragment) fragments.get(0)).handleSocketMessage(text);
        ((FocusBasketballFragment) fragments.get(3)).handleSocketMessage(text);


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
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.public_img_back:  //返回
                MobclickAgent.onEvent(mContext, "Basketball_Exit");
//                ((MainActivity) getActivity()).openLeftLayout();
                getActivity().finish();
                break;
            case R.id.public_btn_set:  //设置
                currentFragmentId = mViewPager.getCurrentItem();
                if (currentFragmentId == IMMEDIA_FRAGMENT) {
                    MobclickAgent.onEvent(mContext, "Basketball_Setting");
//                String s = getAppVersionCode(mContext);
                    mIntent = new Intent(getActivity(), BasketballSettingActivity.class);
//                getParentFragment().startActivityForResult(mIntent, REQUEST_SETTINGCODE);
//                getActivity().startActivityForResult(mIntent , REQUEST_SETTINGCODE);
                    Bundle bundleset = new Bundle();
                    bundleset.putInt("currentfragment", IMMEDIA_FRAGMENT);
                    mIntent.putExtras(bundleset);
                    startActivity(mIntent);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                } else if (currentFragmentId == RESULT_FRAGMENT) {
                    MobclickAgent.onEvent(mContext, "Basketball_Setting");
//                String s = getAppVersionCode(mContext);
                    mIntent = new Intent(getActivity(), BasketballSettingActivity.class);
//                getParentFragment().startActivityForResult(mIntent, REQUEST_SETTINGCODE);
//                getActivity().startActivityForResult(mIntent , REQUEST_SETTINGCODE);
                    Bundle bundleset = new Bundle();
                    bundleset.putInt("currentfragment", RESULT_FRAGMENT);
                    mIntent.putExtras(bundleset);
                    startActivity(mIntent);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                } else if (currentFragmentId == SCHEDULE_FRAGMENT) {
                    MobclickAgent.onEvent(mContext, "Basketball_Setting");
//                String s = getAppVersionCode(mContext);
                    mIntent = new Intent(getActivity(), BasketballSettingActivity.class);
//                getParentFragment().startActivityForResult(mIntent, REQUEST_SETTINGCODE);
//                getActivity().startActivityForResult(mIntent , REQUEST_SETTINGCODE);
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
                    if (ImmedBasketballFragment.getIsLoad() == 1) {
                        MobclickAgent.onEvent(mContext, "Basketball_Filter");
                        Intent intent = new Intent(getActivity(), BasketFiltrateActivity.class);
                        intent.putExtra("MatchAllFilterDatas", (Serializable) ImmedBasketballFragment.mAllFilter);//Serializable 序列化传值（所有联赛数据）
                        intent.putExtra("MatchChickedFilterDatas", (Serializable) ImmedBasketballFragment.mChickedFilter);//Serializable 序列化传值（选中的联赛数据）
//                getParentFragment().startActivityForResult(intent, REQUEST_FILTERCODE);
                        intent.putExtra("currentfragment", IMMEDIA_FRAGMENT);
//                    getActivity().startActivityForResult(intent,ImmedBasketballFragment.REQUEST_FILTERCODE);
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
                        Intent intent = new Intent(getActivity(), BasketFiltrateActivity.class);
                        intent.putExtra("MatchAllFilterDatas", (Serializable) ResultBasketballFragment.mAllFilter);//Serializable 序列化传值（所有联赛数据）
                        intent.putExtra("MatchChickedFilterDatas", (Serializable) ResultBasketballFragment.mChickedFilter);//Serializable 序列化传值（选中的联赛数据）
//                getParentFragment().startActivityForResult(intent, REQUEST_FILTERCODE);
//                    getActivity().startActivityForResult(intent, REQUEST_FILTERCODE);
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
                        Intent intent = new Intent(getActivity(), BasketFiltrateActivity.class);
                        intent.putExtra("MatchAllFilterDatas", (Serializable) ScheduleBasketballFragment.mAllFilter);//Serializable 序列化传值（所有联赛数据）
                        intent.putExtra("MatchChickedFilterDatas", (Serializable) ScheduleBasketballFragment.mChickedFilter);//Serializable 序列化传值（选中的联赛数据）
//                getParentFragment().startActivityForResult(intent, REQUEST_FILTERCODE);
//                    getActivity().startActivityForResult(intent, REQUEST_FILTERCODE);
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


            case R.id.public_btn_infomation:
                Intent intent = new Intent(getActivity(), BasketballInformationActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                break;
        }
    }

    /**
     * 判断四个Fragment切换显示或隐藏的状态
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
     * 判断四个Fragment切换显示或隐藏的状态
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
        mSpinner.setSelection(1);// 设置Spinner默认选择Item
        if (((FootballActivity) mContext).fragmentIndex == 5) {
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
        }
        if (getActivity() != null && ((FootballActivity) mContext).fragmentIndex == FootballActivity.BASKET_FRAGMENT) {
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
    public void onStart() {
        super.onStart();

    }
}
