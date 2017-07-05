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
import com.hhly.mlottery.activity.FiltrateMatchConfigActivity;
import com.hhly.mlottery.activity.FootballMatchSearchActivity;
import com.hhly.mlottery.activity.FootballTypeSettingActivity;
import com.hhly.mlottery.adapter.PureViewPagerAdapter;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.bean.LeagueCup;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BaseUserTopics;
import com.hhly.mlottery.config.FootBallMatchFilterTypeEnum;
import com.hhly.mlottery.frame.BallType;
import com.hhly.mlottery.frame.footballframe.FocusFragment;
import com.hhly.mlottery.frame.footballframe.ImmediateFragment;
import com.hhly.mlottery.frame.footballframe.ResultFragment;
import com.hhly.mlottery.frame.footballframe.RollBallFragment;
import com.hhly.mlottery.frame.footballframe.ScheduleFragment;
import com.hhly.mlottery.frame.footballframe.eventbus.ScoreFragmentWebSocketEntity;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.widget.BallChoiceArrayAdapter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.hhly.mlottery.frame.footballframe.ScheduleFragment.mAllCup;

/**
 * A simple {@link Fragment} subclass.
 */
public class FootBallScoreFragment extends BaseWebSocketFragment {

    private final boolean isNewFrameWork = true;
    private final int entryType = 1;


    private final int ROLLBALL_FRAGMENT = 0;
    private final int IMMEDIA_FRAGMENT = 1;
    private final int RESULT_FRAGMENT = 2;
    private final int SCHEDULE_FRAGMENT = 3;
    private final int FOCUS_FRAGMENT = 4;

    private final static String TAG = "ScoresFragment";

    private View view;
    private Context mContext;

    /**
     * 帅选
     */
    private ImageView mFilterImgBtn;// 筛选
    /**
     * 设置
     */
    private ImageView mSetImgBtn;// 设置

    private String[] mItems;

    /**
     * 当前处于哪个比赛fg
     */
    private int currentFragmentId = 0;

    private ViewPager mViewPager;
    private SlidingTabLayout mTabLayout;
    private PureViewPagerAdapter pureViewPagerAdapter;
    private List<Fragment> fragments;

    private RollBallFragment rollBallFragment;

    private LinearLayout d_header;
    private LinearLayout ll_match_select;

    private TextView tv_match_name;
    private ImageView iv_match;
    private ImageView public_search_filter;

    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setWebSocketUri(BaseURLs.WS_SERVICE);
//        setTopic("USER.topic.app");
        setTopic(BaseUserTopics.footballScore);
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        view = View.inflate(mContext, R.layout.fragment_foot_ball_score, null);
        initView();
        setupViewPager();
        focusCallback();// 加载关注数
        initData();
        initEvent();
        return view;
    }


    private void initView() {
        mItems = getResources().getStringArray(R.array.bifen_select);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

        // 筛选
        mFilterImgBtn = (ImageView) view.findViewById(R.id.public_btn_filter);
        mSetImgBtn = (ImageView) view.findViewById(R.id.public_btn_set);
        mSetImgBtn.setVisibility(View.VISIBLE);

        ll_match_select = (LinearLayout) view.findViewById(R.id.ll_match_select);
        tv_match_name = (TextView) view.findViewById(R.id.tv_match_name);
        iv_match = (ImageView) view.findViewById(R.id.iv_match);
        d_header = (LinearLayout) view.findViewById(R.id.d_heasder);

        tv_match_name.setText(getResources().getString(R.string.football_txt));
        //搜索
        public_search_filter = (ImageView) view.findViewById(R.id.public_search_filter);
        public_search_filter.setVisibility(View.VISIBLE);

    }

    public void focusCallback() {
        String focusIds = PreferenceUtil.getString("focus_ids", "");
        String[] arrayId = focusIds.split("[,]");
        if (getActivity() != null) {
            if ("".equals(focusIds) || arrayId.length == 0) {
                mTabLayout.hideMsg(FOCUS_FRAGMENT);
            } else {
                mTabLayout.showMsg(FOCUS_FRAGMENT, arrayId.length);
            }
        }
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
        BallChoiceArrayAdapter mAdapter = new BallChoiceArrayAdapter(mContext, mItems, BallType.FOOTBALL);

        ListView listview = (ListView) mView.findViewById(R.id.match_type);
        listview.setAdapter(mAdapter);

        final PopupWindow popupWindow = new PopupWindow(mView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAsDropDown(d_header);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                L.d("websocket123", ">>>>>>>>足球比分关闭");
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


    private void setupViewPager() {
        mTabLayout = (SlidingTabLayout) view.findViewById(R.id.tabs);

        String[] tabNames = {getString(R.string.foot_rollball_txt), getString(R.string.foot_jishi_txt), getString(R.string.foot_saiguo_txt), getString(R.string.foot_saicheng_txt), getString(R.string.foot_guanzhu_txt)};

        fragments = new ArrayList<>();
        rollBallFragment = RollBallFragment.newInstance(ROLLBALL_FRAGMENT, isNewFrameWork, entryType);
        fragments.add(rollBallFragment);
        fragments.add(ImmediateFragment.newInstance(IMMEDIA_FRAGMENT, isNewFrameWork, entryType));
        fragments.add(ResultFragment.newInstance(RESULT_FRAGMENT, entryType));
        fragments.add(ScheduleFragment.newInstance(SCHEDULE_FRAGMENT, entryType));
        fragments.add(FocusFragment.newInstance(FOCUS_FRAGMENT, entryType));


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


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (positionOffsetPixels == 0) {
                    switch (position) {
                        case ROLLBALL_FRAGMENT:
                            mFilterImgBtn.setVisibility(View.VISIBLE);
                            break;
                        case IMMEDIA_FRAGMENT:
                            mFilterImgBtn.setVisibility(View.VISIBLE);
                            ((ImmediateFragment) fragments.get(position)).reLoadData();
                            break;
                        case RESULT_FRAGMENT:
                            mFilterImgBtn.setVisibility(View.VISIBLE);
                            ((ResultFragment) fragments.get(position)).updateAdapter();
                            break;
                        case SCHEDULE_FRAGMENT:
                            mFilterImgBtn.setVisibility(View.VISIBLE);
                            ((ScheduleFragment) fragments.get(position)).updateAdapter();
                            break;
                        case FOCUS_FRAGMENT:
                            mFilterImgBtn.setVisibility(View.INVISIBLE);
                            ((FocusFragment) fragments.get(position)).reLoadData();
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
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(tabNames.length);
        mViewPager.setCurrentItem(IMMEDIA_FRAGMENT,false);

        mTabLayout.setViewPager(mViewPager, tabNames);
        mTabLayout.showDot(FOCUS_FRAGMENT);
        mTabLayout.showMsg(FOCUS_FRAGMENT, 0);
        mTabLayout.setMsgMargin(FOCUS_FRAGMENT, 2, 7);
        mTabLayout.hideMsg(FOCUS_FRAGMENT);
        MsgView msgView = mTabLayout.getMsgView(FOCUS_FRAGMENT);
        if (msgView != null) {
            msgView.setBackgroundColor(Color.parseColor("#ffde00"));
            msgView.setTextColor(Color.parseColor("#0085e1"));
            msgView.setTextSize(mContext.getResources().getDimension(R.dimen.focus_notice));
        }


        if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) {
            mTabLayout.setTabSpaceEqual(true);
        } else {
            mTabLayout.setTabSpaceEqual(false);
        }
    }


    private void initData() {
        public_search_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FootballMatchSearchActivity.class);
                startActivity(intent);
            }
        });
        mFilterImgBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(mContext, "Football_Filtrate");
                currentFragmentId = mViewPager.getCurrentItem();
                if (currentFragmentId == ROLLBALL_FRAGMENT) {


                    L.d("ddddeee", "bendi==" + PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_ROLL).size() + "");


                    Intent intent = new Intent(getActivity(), FiltrateMatchConfigActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(FiltrateMatchConfigActivity.NET_STATUS, true);
                    bundle.putInt("currentFragmentId", ROLLBALL_FRAGMENT);
                    LeagueCup[] allCups = null;
                    if (rollBallFragment.getLeagueCupLists() != null && rollBallFragment.getLeagueCupLists().size() > 0) {
                        allCups = rollBallFragment.getLeagueCupLists().toArray(new LeagueCup[]{});
                    }
                    bundle.putParcelableArray(FiltrateMatchConfigActivity.ALL_CUPS, allCups);// 传值到筛选页面的全部联赛，数据类型是LeagueCup[]

                    LeagueCup[] leagueCups = rollBallFragment.getLeagueCupChecked();
                    if (leagueCups != null && leagueCups.length > 0) {
                        bundle.putParcelableArray(FiltrateMatchConfigActivity.CHECKED_CUPS,
                                leagueCups);// 传值到筛选页面的已经选择的联赛，数据类型是LeagueCup[]
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
//                            if (!ImmediateFragment.isPause) {
                            Toast.makeText(getActivity(), R.string.toast_data_loading, Toast.LENGTH_SHORT).show();
//                            }
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
                        LeagueCup[] allCups = mAllCup.toArray(new LeagueCup[]{});
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


        mSetImgBtn.setOnClickListener(new View.OnClickListener() {
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
                } else if (currentFragmentId == ROLLBALL_FRAGMENT) {
                    Intent intent = new Intent(getActivity(), FootballTypeSettingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("currentFragmentId", ROLLBALL_FRAGMENT);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });
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
        L.d(TAG, "football Fragment resume..");
        //if (getActivity() != null && ((FootballActivity) mContext).fragmentIndex != FootballActivity.BASKET_FRAGMENT) {
        L.d("websocket123", "======足球打开");
        connectWebSocket();
        // }
    }

    @Override
    public void onStart() {
        super.onStart();
        L.d(TAG, "football Fragment start..");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d(TAG, "football Fragment destroy..");

        closeWebSocket();
    }

    /**
     * 发送文字直播
     *
     * @param text
     */
    @Override
    protected void onTextResult(String text) {
        L.d("websocket123", "足球收到消息==" + text);

        EventBus.getDefault().post(new FootballScoresWebSocketEntity(text));
    }

    /***
     * 足球界面接受推送的消息类
     */
    public class FootballScoresWebSocketEntity {
        public String text;

        FootballScoresWebSocketEntity(String text) {
            this.text = text;
        }
    }


    @Override
    protected void onConnectFail() {
        L.d(TAG, "__onConnectFail__");

    }

    @Override
    protected void onDisconnected() {
        L.d(TAG, "__onDisconnected__");

    }

    @Override
    protected void onConnected() {
        L.d(TAG, "__onConnected__");

    }

    public void reconnectWebSocket() {
        connectWebSocket();
    }

    public void disconnectWebSocket() {
        closeWebSocket();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.d(TAG, "football Fragment destroy view..");

        EventBus.getDefault().unregister(this);
        closeWebSocket();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        L.d(TAG, "football Fragment detach..");
    }


    /***
     * 足球比分(比分、资讯、视频、数据、指数)
     * 只有切换到比分时连接Socket推送，其余断开推送
     *
     * @param scoreFragmentWebSocketEntity
     */
    public void onEventMainThread(ScoreFragmentWebSocketEntity scoreFragmentWebSocketEntity) {
        if (scoreFragmentWebSocketEntity.getFgIndex() == 0) {
            connectWebSocket();
        } else {
            closeWebSocket();
        }
    }

    public void onEventMainThread(CloseWebSocketEventBus closeWebSocketEventBus) {

        if (closeWebSocketEventBus.isVisible()) {
            L.d("websocket123", "_________足球 比分 关闭 fg");
            closeWebSocket();
        } else {
            if (closeWebSocketEventBus.getIndex() == BallType.FOOTBALL) {
                L.d("websocket123", "_________足球 比分 打开 fg");

                connectWebSocket();
            }
        }
    }
}
