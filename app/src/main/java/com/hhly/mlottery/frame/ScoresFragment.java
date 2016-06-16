package com.hhly.mlottery.frame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FiltrateMatchConfigActivity;
import com.hhly.mlottery.activity.FootballActivity;
import com.hhly.mlottery.activity.FootballTypeSettingActivity;
import com.hhly.mlottery.adapter.PureViewPagerAdapter;
import com.hhly.mlottery.bean.LeagueCup;
import com.hhly.mlottery.frame.footframe.FocusFragment;
import com.hhly.mlottery.frame.footframe.ImmediateFragment;
import com.hhly.mlottery.frame.footframe.ResultFragment;
import com.hhly.mlottery.frame.footframe.RollBallFragment;
import com.hhly.mlottery.frame.footframe.ScheduleFragment;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.widget.BallSelectArrayAdapter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenney
 */
@SuppressLint("NewApi")
public class ScoresFragment extends Fragment {

    private final int ROLLBALL_FRAGMENT = 0;
    private final int IMMEDIA_FRAGMENT = 1;
    private final int RESULT_FRAGMENT = 2;
    private final int SCHEDULE_FRAGMENT = 3;
    private final int FOCUS_FRAGMENT = 4;

    private final static String TAG = "ScoresFragment";
    public static List<String> titles;

    private View view;
    private Context mContext;

    /**
     * 返回菜单
     */
    private ImageView mBackImgBtn;// 返回菜单
    /**
     * 帅选
     */
    private ImageView mFilterImgBtn;// 筛选
    /**
     * 设置
     */
    private ImageView mSetImgBtn;// 设置
    /**
     * 中间标题
     */
    private TextView mTitleTv;// 标题

    /**
     * 左侧标题
     */
    private TextView public_txt_left_title;

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

    private RollBallFragment rollBallFragment;

    @SuppressLint("ValidFragment")
    public ScoresFragment(Context context) {
        this.mContext = context;
    }

    public ScoresFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mContext = getActivity();
        view = View.inflate(mContext, R.layout.frage_football, null);
        initView();
        setupViewPager();
        focusCallback();// 加载关注数
        initData();
        initEVent();
        return view;
    }

    private void initEVent() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mContext.getResources().getString(R.string.basket_left).equals(mItems[position])) {// 选择篮球
                    ((FootballActivity) mContext).ly_tab_bar.setVisibility(View.GONE);
                    ((FootballActivity) mContext).switchFragment(5);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView() {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        // 返回菜单
        mBackImgBtn = (ImageView) view.findViewById(R.id.public_img_back);

        //中心标题
        mTitleTv = (TextView) view.findViewById(R.id.public_txt_title);
        mTitleTv.setVisibility(View.GONE);
//        mTitleTv.setText(R.string.football_frame_txt);
        //左边标题
            /*  public_txt_left_title = (TextView) view.findViewById(R.id.public_txt_left_title);
                public_txt_left_title.setVisibility(View.VISIBLE);
        public_txt_left_title.setText(R.string.football_frame_txt);*/

        mSpinner = (Spinner) view.findViewById(R.id.public_txt_left_spinner);
        mSpinner.setVisibility(View.VISIBLE);
        mItems = getResources().getStringArray(R.array.ball_select);
        BallSelectArrayAdapter mAdapter = new BallSelectArrayAdapter(mContext, mItems);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setSelection(0);

        // 筛选
        mFilterImgBtn = (ImageView) view.findViewById(R.id.public_btn_filter);
        mSetImgBtn = (ImageView) view.findViewById(R.id.public_btn_set);
        mSetImgBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 判断四个Fragment切换显示或隐藏的状态
     */
    private boolean isRollballFragment = true;
    private boolean isRollball = false;
    private boolean isImmediateFragment = false;
    private boolean isImmediate = false;
    private boolean isResultFragment = false;
    private boolean isResult = false;
    private boolean isScheduleFragment = false;
    private boolean isSchedule = false;
    private boolean isFocusFragment = false;
    private boolean isFocus = false;

    private void setupViewPager() {
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        titles = new ArrayList<>();
        titles.add(getString(R.string.foot_rollball_txt));
        titles.add(getString(R.string.foot_jishi_txt));
        titles.add(getString(R.string.foot_saiguo_txt));
        titles.add(getString(R.string.foot_saicheng_txt));
        titles.add(getString(R.string.foot_guanzhu_txt));

        fragments = new ArrayList<>();
        rollBallFragment = RollBallFragment.newInstance(ROLLBALL_FRAGMENT);
        fragments.add(rollBallFragment);
        fragments.add(ImmediateFragment.newInstance(IMMEDIA_FRAGMENT));
        fragments.add(ResultFragment.newInstance(RESULT_FRAGMENT));
        fragments.add(ScheduleFragment.newInstance(SCHEDULE_FRAGMENT));
        fragments.add(FocusFragment.newInstance(FOCUS_FRAGMENT));

        pureViewPagerAdapter = new PureViewPagerAdapter(fragments, titles, getChildFragmentManager());


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//                L.d(TAG, "onPageScrolled");
//                L.d(TAG, "position = " + position);
//                L.d(TAG, "positionOffset = " + positionOffset);
//                L.d(TAG, "positionOffsetPixels = " + positionOffsetPixels);

                if (positionOffsetPixels == 0) {
                    switch (position) {
                        case ROLLBALL_FRAGMENT:
                            mFilterImgBtn.setVisibility(View.VISIBLE);
                            mSetImgBtn.setVisibility(View.INVISIBLE);
                            ((RollBallFragment) fragments.get(position)).feedAdapter();
                            break;
                        case IMMEDIA_FRAGMENT:
                            mFilterImgBtn.setVisibility(View.VISIBLE);
                            mSetImgBtn.setVisibility(View.VISIBLE);
                            ((ImmediateFragment) fragments.get(position)).reLoadData();
                            break;
                        case RESULT_FRAGMENT:
                            mFilterImgBtn.setVisibility(View.VISIBLE);
                            mSetImgBtn.setVisibility(View.VISIBLE);
                            ((ResultFragment) fragments.get(position)).updateAdapter();
                            break;
                        case SCHEDULE_FRAGMENT:
                            mFilterImgBtn.setVisibility(View.VISIBLE);
                            mSetImgBtn.setVisibility(View.VISIBLE);
                            ((ScheduleFragment) fragments.get(position)).updateAdapter();
                            break;
                        case FOCUS_FRAGMENT:
                            mFilterImgBtn.setVisibility(View.GONE);
                            mSetImgBtn.setVisibility(View.VISIBLE);
                            ((FocusFragment) fragments.get(position)).reLoadData();
                            break;
                    }
                }
            }


            @Override
            public void onPageSelected(final int position) {
                /**判断四个Fragment切换显示或隐藏的状态 */
                switch (position) {
                    case ROLLBALL_FRAGMENT:
                        isRollballFragment = true;
                        isImmediateFragment = false;
                        isResultFragment = false;
                        isScheduleFragment = false;
                        isFocusFragment = false;
                        break;
                    case IMMEDIA_FRAGMENT:
                        isImmediateFragment = true;
                        isRollballFragment = false;
                        isResultFragment = false;
                        isScheduleFragment = false;
                        isFocusFragment = false;
                        break;
                    case RESULT_FRAGMENT:
                        isResultFragment = true;
                        isRollballFragment = false;
                        isImmediateFragment = false;
                        isScheduleFragment = false;
                        isFocusFragment = false;
                        break;
                    case SCHEDULE_FRAGMENT:
                        isScheduleFragment = true;
                        isRollballFragment = false;
                        isResultFragment = false;
                        isImmediateFragment = false;
                        isFocusFragment = false;
                        break;
                    case FOCUS_FRAGMENT:
                        isFocusFragment = true;
                        isRollballFragment = false;
                        isScheduleFragment = false;
                        isResultFragment = false;
                        isImmediateFragment = false;
                        break;
                }
                if (isRollballFragment) {
                    if (isRollball) {
                        MobclickAgent.onPageEnd("Football_ResultFragment");
                        isResult = false;
                        L.d("xxx", "ResultFragment>>>隐藏");
                    }
                    if (isSchedule) {
                        MobclickAgent.onPageEnd("Football_ScheduleFragment");
                        isSchedule = false;
                        L.d("xxx", "ScheduleFragment>>>隐藏");
                    }
                    if (isFocus) {
                        MobclickAgent.onPageEnd("Football_FocusFragment");
                        isFocus = false;
                        L.d("xxx", "FocusFragment>>>隐藏");
                    }
                    MobclickAgent.onPageStart("Football_ImmediateFragment");
                    isRollball = true;
                    L.d("xxx", "ImmediateFragment>>>显示");
                }
                if (isImmediateFragment) {
                    if (isResult) {
                        MobclickAgent.onPageEnd("Football_ResultFragment");
                        isResult = false;
                        L.d("xxx", "ResultFragment>>>隐藏");
                    }
                    if (isSchedule) {
                        MobclickAgent.onPageEnd("Football_ScheduleFragment");
                        isSchedule = false;
                        L.d("xxx", "ScheduleFragment>>>隐藏");
                    }
                    if (isFocus) {
                        MobclickAgent.onPageEnd("Football_FocusFragment");
                        isFocus = false;
                        L.d("xxx", "FocusFragment>>>隐藏");
                    }
                    MobclickAgent.onPageStart("Football_ImmediateFragment");
                    isImmediate = true;
                    L.d("xxx", "ImmediateFragment>>>显示");
                }
                if (isResultFragment) {
                    if (isImmediate) {
                        MobclickAgent.onPageEnd("Football_ImmediateFragment");
                        isImmediate = false;
                        L.d("xxx", "ImmediateFragment>>>隐藏");
                    }
                    if (isSchedule) {
                        MobclickAgent.onPageEnd("Football_ScheduleFragment");
                        isSchedule = false;
                        L.d("xxx", "ScheduleFragment>>>隐藏");
                    }
                    if (isFocus) {
                        MobclickAgent.onPageEnd("Football_FocusFragment");
                        isFocus = false;
                        L.d("xxx", "FocusFragment>>>隐藏");
                    }
                    MobclickAgent.onPageStart("Football_ResultFragment");
                    isResult = true;
                    L.d("xxx", "ResultFragment>>>显示");
                }
                if (isScheduleFragment) {
                    if (isImmediate) {
                        MobclickAgent.onPageEnd("Football_ImmediateFragment");
                        isImmediate = false;
                        L.d("xxx", "ImmediateFragment>>>隐藏");
                    }
                    if (isResult) {
                        MobclickAgent.onPageEnd("Football_ResultFragment");
                        isResult = false;
                        L.d("xxx", "ResultFragment>>>隐藏");
                    }
                    if (isFocus) {
                        MobclickAgent.onPageEnd("Football_FocusFragment");
                        isFocus = false;
                        L.d("xxx", "FocusFragment>>>隐藏");
                    }
                    MobclickAgent.onPageStart("Football_ScheduleFragment");
                    isSchedule = true;
                    L.d("xxx", "ScheduleFragment>>>显示");
                }
                if (isFocusFragment) {
                    if (isImmediate) {
                        MobclickAgent.onPageEnd("Football_ImmediateFragment");
                        isImmediate = false;
                        L.d("xxx", "ImmediateFragment>>>隐藏");
                    }
                    if (isResult) {
                        MobclickAgent.onPageEnd("Football_ResultFragment");
                        isResult = false;
                        L.d("xxx", "ResultFragment>>>隐藏");
                    }
                    if (isSchedule) {
                        MobclickAgent.onPageEnd("Football_ScheduleFragment");
                        isSchedule = false;
                        L.d("xxx", "ScheduleFragment>>>隐藏");
                    }
                    MobclickAgent.onPageStart("Football_FocusFragment");
                    isFocus = true;
                    L.d("xxx", "FocusFragment>>>显示");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setAdapter(pureViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }


    private void initData() {
        mBackImgBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                MobclickAgent.onEvent(mContext, "Football_Exit");
            }
        });

        mFilterImgBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(mContext, "Football_Filtrate");
                currentFragmentId = mViewPager.getCurrentItem();
                if (currentFragmentId == ROLLBALL_FRAGMENT) {
                    Intent intent = new Intent(getActivity(), FiltrateMatchConfigActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(FiltrateMatchConfigActivity.NET_STATUS, true);
                    bundle.putInt("currentFragmentId", ROLLBALL_FRAGMENT);
                    LeagueCup[] allCups = null;
                    if (rollBallFragment.getLeagueCupLists() != null) {
                        allCups = rollBallFragment.getLeagueCupLists().toArray(new LeagueCup[]{});
                    }
                    bundle.putParcelableArray(FiltrateMatchConfigActivity.ALL_CUPS, allCups);// 传值到筛选页面的全部联赛，数据类型是LeagueCup[]
                    if (rollBallFragment.getLeagueCupChecked() != null) {
                        bundle.putParcelableArray(FiltrateMatchConfigActivity.CHECKED_CUPS,
                                rollBallFragment.getLeagueCupChecked());// 传值到筛选页面的已经选择的联赛，数据类型是LeagueCup[]
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);

                } else if (currentFragmentId == IMMEDIA_FRAGMENT) {
                    switch (ImmediateFragment.mLoadDataStatus) {
                        case ImmediateFragment.LOAD_DATA_STATUS_ERROR:
                            Intent intent = new Intent(getActivity(), FiltrateMatchConfigActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(FiltrateMatchConfigActivity.NET_STATUS, ImmediateFragment.isNetSuccess);
                            bundle.putInt("currentFragmentId", IMMEDIA_FRAGMENT);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            break;
                        case ImmediateFragment.LOAD_DATA_STATUS_SUCCESS:
                            intent = new Intent(getActivity(), FiltrateMatchConfigActivity.class);
                            bundle = new Bundle();
                            LeagueCup[] allCups = ImmediateFragment.mCups.toArray(new LeagueCup[]{});
                            bundle.putParcelableArray(FiltrateMatchConfigActivity.ALL_CUPS, allCups);// 传值到筛选页面的全部联赛，数据类型是LeagueCup[]
                            bundle.putParcelableArray(FiltrateMatchConfigActivity.CHECKED_CUPS,
                                    ImmediateFragment.mCheckedCups);// 传值到筛选页面的已经选择的联赛，数据类型是LeagueCup[]
                            bundle.putBoolean(FiltrateMatchConfigActivity.CHECKED_DEFUALT, ImmediateFragment.isCheckedDefualt);// 是否默认选择
                            bundle.putBoolean(FiltrateMatchConfigActivity.NET_STATUS, ImmediateFragment.isNetSuccess);
                            bundle.putInt("currentFragmentId", IMMEDIA_FRAGMENT);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            break;
                        case ImmediateFragment.LOAD_DATA_STATUS_INIT:
                        case ImmediateFragment.LOAD_DATA_STATUS_LOADING:
                            if (!ImmediateFragment.isPause) {
                                Toast.makeText(getActivity(), R.string.toast_data_loading, Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                            break;
                    }
                } else if (currentFragmentId == RESULT_FRAGMENT) {
                    switch (ResultFragment.mLoadDataStatus) {
                        case ResultFragment.LOAD_DATA_STATUS_ERROR:
                            Intent mIntent = new Intent(getActivity(), FiltrateMatchConfigActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(FiltrateMatchConfigActivity.NET_STATUS, ResultFragment.isNetSuccess);
                            bundle.putInt("currentFragmentId", RESULT_FRAGMENT);
                            mIntent.putExtras(bundle);
                            startActivity(mIntent);
                            break;
                        case ResultFragment.LOAD_DATA_STATUS_SUCCESS:
                            mIntent = new Intent(getActivity(), FiltrateMatchConfigActivity.class);
                            bundle = new Bundle();
                            bundle.putBoolean(FiltrateMatchConfigActivity.NET_STATUS, ResultFragment.isNetSuccess);
                            LeagueCup[] allCups = ResultFragment.mCups.toArray(new LeagueCup[]{});
                            //所有联赛
                            bundle.putParcelableArray(FiltrateMatchConfigActivity.ALL_CUPS, allCups);
                            //当前 选中联赛
                            bundle.putParcelableArray(FiltrateMatchConfigActivity.CHECKED_CUPS, ResultFragment.mCheckedCups);
                            // 是否默认选择  -- 显示选中（标红）
                            bundle.putBoolean(FiltrateMatchConfigActivity.CHECKED_DEFUALT, ResultFragment.isCheckedDefualt);
                            bundle.putInt("currentFragmentId", RESULT_FRAGMENT);
                            mIntent.putExtras(bundle);
                            startActivity(mIntent);
                            //getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                            break;
                        case ResultFragment.LOAD_DATA_STATUS_INIT:
                        case ResultFragment.LOAD_DATA_STATUS_LOADING:
                            Toast.makeText(getActivity(), R.string.toast_data_loading, Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else if (currentFragmentId == SCHEDULE_FRAGMENT) {
                    if (ScheduleFragment.mLoadDataStatus == ScheduleFragment.LOAD_DATA_STATUS_SUCCESS) {
                        Intent intent = new Intent(getActivity(), FiltrateMatchConfigActivity.class);
                        Bundle bundle = new Bundle();
                        LeagueCup[] allCups = ScheduleFragment.mAllCup.toArray(new LeagueCup[]{});
                        bundle.putParcelableArray(FiltrateMatchConfigActivity.ALL_CUPS, allCups);
                        bundle.putParcelableArray(FiltrateMatchConfigActivity.CHECKED_CUPS, ScheduleFragment.mCheckedCups);
                        bundle.putBoolean(FiltrateMatchConfigActivity.NET_STATUS, ScheduleFragment.isNetSuccess);
                        bundle.putBoolean(FiltrateMatchConfigActivity.CHECKED_DEFUALT, ScheduleFragment.isCheckedDefualt);
                        bundle.putInt("currentFragmentId", SCHEDULE_FRAGMENT);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (ScheduleFragment.mLoadDataStatus == ScheduleFragment.LOAD_DATA_STATUS_ERROR) {
                        Intent intent = new Intent(getActivity(), FiltrateMatchConfigActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(FiltrateMatchConfigActivity.NET_STATUS, ScheduleFragment.isNetSuccess);
                        bundle.putInt("currentFragmentId", SCHEDULE_FRAGMENT);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), R.string.toast_data_loading, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        mSetImgBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(mContext, "Football_Setting");
                currentFragmentId = mViewPager.getCurrentItem();
                if (currentFragmentId == IMMEDIA_FRAGMENT) {
                    Intent intent = new Intent(mContext, FootballTypeSettingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("currentFragmentId", IMMEDIA_FRAGMENT);
                    intent.putExtras(bundle);
                    startActivity(intent);

                } else if (currentFragmentId == RESULT_FRAGMENT) {
                    Intent intent = new Intent(getActivity(), FootballTypeSettingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("currentFragmentId", RESULT_FRAGMENT);
                    intent.putExtras(bundle);
                    startActivity(intent);

                } else if (currentFragmentId == SCHEDULE_FRAGMENT) {
                    Intent intent = new Intent(getActivity(), FootballTypeSettingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("currentFragmentId", SCHEDULE_FRAGMENT);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (currentFragmentId == FOCUS_FRAGMENT) {
                    Intent intent = new Intent(getActivity(), FootballTypeSettingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("currentFragmentId", FOCUS_FRAGMENT);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });
    }


    public void focusCallback() {
        String focusIds = PreferenceUtil.getString("focus_ids", "");
        String[] arrayId = focusIds.split("[,]");
        if ("".equals(focusIds) || arrayId.length == 0) {
            mTabLayout.getTabAt(4).setText(getString(R.string.foot_guanzhu_txt));
        } else {
            mTabLayout.getTabAt(4).setText(getString(R.string.foot_guanzhu_txt) + "(" + arrayId.length + ")");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onPause();
        } else {
            onResume();
            mSpinner.setSelection(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        L.d(TAG, "football Fragment resume..");

        if (isImmediateFragment) {
            MobclickAgent.onPageStart("Football_ImmediateFragment");
            isImmediate = true;
            L.d("xxx", "ImmediateFragment>>>显示");
        }
        if (isResultFragment) {
            MobclickAgent.onPageStart("Football_ResultFragment");
            isResult = true;
            L.d("xxx", "ResultFragment>>>显示");
        }
        if (isScheduleFragment) {
            MobclickAgent.onPageStart("Football_ScheduleFragment");
            isSchedule = true;
            L.d("xxx", "ScheduleFragment>>>显示");
        }
        if (isFocusFragment) {
            MobclickAgent.onPageStart("Football_FocusFragment");
            isFocus = true;
            L.d("xxx", "FocusFragment>>>显示");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isImmediate) {
            MobclickAgent.onPageEnd("Football_ImmediateFragment");
            isImmediate = false;
            //                L.d("xxx", "ImmediateFragment>>>隐藏");
            L.d("xxx", "ImmediateFragment>>>隐藏");
        }
        if (isResult) {
            MobclickAgent.onPageEnd("Football_ResultFragment");
            isResult = false;
            //                L.d("xxx", "ResultFragment>>>隐藏");
            L.d("xxx", "ResultFragment>>>隐藏");
        }
        if (isSchedule) {
            MobclickAgent.onPageEnd("Football_ScheduleFragment");
            isSchedule = false;
            //                L.d("xxx", "ScheduleFragment>>>隐藏");

            L.d("xxx", "ScheduleFragment>>>隐藏");

        }
        if (isFocus) {
            MobclickAgent.onPageEnd("Football_FocusFragment");
            isFocus = false;

            //                L.d("xxx", "FocusFragment>>>隐藏");

            L.d("xxx", "FocusFragment>>>隐藏");

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //        L.d(TAG, "football Fragment destroy..");

        L.d(TAG, "football Fragment destroy..");

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.d(TAG, "football Fragment destroy view..");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        L.d(TAG, "football Fragment detach..");
    }






/*
        @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //   setResult(Activity.RESULT_OK);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/


}
