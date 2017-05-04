package com.hhly.mlottery.frame.cpifrag.SnookerIndex;


import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.bean.enums.TennisOddsTypeEnum;
import com.hhly.mlottery.bean.snookerbean.SnookerScoreSocketBean;
import com.hhly.mlottery.bean.snookerbean.snookerIndexBean.SnookerIndexBean;
import com.hhly.mlottery.bean.snookerbean.snookerschedulebean.SnookerSocketOddsBean;
import com.hhly.mlottery.bean.tennisball.TennisSocketBean;
import com.hhly.mlottery.bean.tennisball.TennisSocketOddsBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BaseUserTopics;
import com.hhly.mlottery.frame.BallType;
import com.hhly.mlottery.frame.cpifrag.CloseCpiWebSocketEventBus;
import com.hhly.mlottery.frame.cpifrag.SnookerIndex.SnookerChildFragment.SnookerCompanyChooseDialogFragment;
import com.hhly.mlottery.frame.cpifrag.SnookerIndex.SnookerChildFragment.SnookerIndexChildFragment;
import com.hhly.mlottery.frame.oddfragment.DateChooseDialogFragment;
import com.hhly.mlottery.frame.scorefrag.ScoreSwitchFg;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.BallChoiceArrayAdapter;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 斯诺克指数外层界面
 * created by mdy 155
 */

public class SIndexFragment extends BaseWebSocketFragment implements SIndexContract.View, View.OnClickListener {

    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";
    private ArrayList<SnookerIndexBean.CompanyEntity> companyList = new ArrayList<>(); // 公司数据源

    private int mBallType;
    private String mParam2;

    /**
     * 欧赔
     */
    public final static String ODDS_EURO = "14";
    /**
     * 亚盘
     */
    public final static String ODDS_LET = "2";
    /**
     * 大小球
     */
    public final static String ODDS_SIZE = "3";
    /**
     * 单双
     */
    public final static String SINGLE_DOUBLE = "4";
    private View mView;
    @BindView(R.id.public_date_layout)
    LinearLayout mLiDateSelect;

    /***
     * 日期
     */
    @BindView(R.id.public_txt_date)
    TextView mTextDateSelect;

    @BindView(R.id.public_img_company)
    ImageView mCompanySelect;

    @BindView(R.id.ll_match_select)
    LinearLayout mMatchSelect;

    @BindView(R.id.tv_match_name)
    TextView mTextMatch;
    @BindView(R.id.iv_match)
    ImageView mImgMatch;

    @BindView(R.id.sindex_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.sindex_refresh_layout)
    ExactSwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.sindex_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.sindex_header)
    LinearLayout mLinearHeader;

    private TabsAdapter mTabsAdapter;

    private List<Fragment> fragments;

    private String[] mItems;
    String mTitles[];

    private DateChooseDialogFragment mDateChooseDialogFragment; // 日期选择
    private SnookerCompanyChooseDialogFragment mCompanyChooseDialogFragment; // 公司选择

    private String currentDate = ""; // 当前日期
    private String choosenDate; // 选中日期

    public SIndexFragment() {
        // Required empty public constructor
    }


    public static SIndexFragment newInstance() {
        SIndexFragment fragment = new SIndexFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mBallType = getArguments().getInt(ARG_PARAM1);
        }
        if (mBallType == BallType.SNOOKER) {
            setWebSocketUri(BaseURLs.WS_SERVICE);
//            setTopic("USER.topic.snooker");
            setTopic(BaseUserTopics.snookerMatch);
        } else if (mBallType == BallType.TENNLS) {
            setWebSocketUri(BaseURLs.WS_SERVICE);
//            setTopic("USER.topic.tennis.oddindex");
            setTopic(BaseUserTopics.oddsTennis);
        }
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
    protected void onTextResult(String text) {
        //TODO ***  接收推送消息
        String type = "";
        try {
            JSONObject jsonObject = new JSONObject(text);
            type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!"".equals(type)) {
            Message msg = Message.obtain();
            msg.obj = text;
            msg.arg1 = Integer.parseInt(type);
            webSocketHandler.sendMessage(msg);
        }
    }

    Handler webSocketHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            L.e(TAG, "__handleMessage__");
            L.e(TAG, "msg.arg1 = " + msg.arg1);
            if (msg.arg1 == 300) {  // 斯诺克比分推送
                String ws_json = (String) msg.obj;
                L.e(TAG, "ws_json_snooker_score = " + ws_json);
                SnookerScoreSocketBean mSnookerScore = null;
                try {
                    mSnookerScore = JSON.parseObject(ws_json, SnookerScoreSocketBean.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    mSnookerScore = JSON.parseObject(ws_json, SnookerScoreSocketBean.class);
                }
                if (mSnookerScore.getData() != null) {
                    for (int i = 0; i < fragments.size(); i++) {
                        ((SnookerIndexChildFragment) fragments.get(i)).updateScore(mSnookerScore);
                    }
                }
            } else if (msg.arg1 == 404) {  //网球比分推送
                String ws_json = (String) msg.obj;
                TennisSocketBean mTennisScore;
                L.e(TAG, "ws_json_snooker_odds = " + ws_json);
                try {
                    mTennisScore = JSON.parseObject(ws_json, TennisSocketBean.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    mTennisScore = JSON.parseObject(ws_json, TennisSocketBean.class);
                }
                if (mTennisScore.getDataObj() != null) {
                    for (int i = 0; i < fragments.size(); i++) {
                        ((SnookerIndexChildFragment) fragments.get(i)).updateTennisScore(mTennisScore);
                    }
                }
            } else if (msg.arg1 == 301) {  // 斯诺克赔率推送
                String ws_json = (String) msg.obj;
                L.e(TAG, "ws_json_snooker_odds = " + ws_json);
                SnookerSocketOddsBean mSnookerOdds = null;
                try {
                    mSnookerOdds = JSON.parseObject(ws_json, SnookerSocketOddsBean.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    mSnookerOdds = JSON.parseObject(ws_json, SnookerSocketOddsBean.class);
                }

                if (mSnookerOdds.getData() != null) {

                    SnookerSocketOddsBean.SnookerDataBean oddsData = mSnookerOdds.getData();
                    switch (oddsData.getPlayType()) {

                        case "asiaLet"://亚盘
                            ((SnookerIndexChildFragment) fragments.get(0)).updateOdds(mSnookerOdds);
                            break;
                        case "asiaSize"://大小球
                            ((SnookerIndexChildFragment) fragments.get(1)).updateOdds(mSnookerOdds);
                            break;

                        case "onlyWin"://独赢[欧赔]
                            ((SnookerIndexChildFragment) fragments.get(2)).updateOdds(mSnookerOdds);
                            break;
                        case "oneTwo"://单双
                            ((SnookerIndexChildFragment) fragments.get(3)).updateOdds(mSnookerOdds);
                            break;
                    }
                }
            } else if (msg.arg1 == 403) { //网球赔率推送
                String ws_json = (String) msg.obj;
                L.e(TAG, "ws_json_snooker_odds = " + ws_json);
                TennisSocketOddsBean mSnookerOdds = null;
                try {
                    mSnookerOdds = JSON.parseObject(ws_json, TennisSocketOddsBean.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    mSnookerOdds = JSON.parseObject(ws_json, TennisSocketOddsBean.class);
                }

                if (mSnookerOdds.getDataObj() != null) {

                    TennisSocketOddsBean.DataObjBean oddsData = mSnookerOdds.getDataObj();
                    switch (oddsData.getGameType()) {

                        case 1://亚盘
                            ((SnookerIndexChildFragment) fragments.get(0)).updateTennisOdds(mSnookerOdds);
                            break;
                        case 3://大小球
                            ((SnookerIndexChildFragment) fragments.get(1)).updateTennisOdds(mSnookerOdds);
                            break;

                        case 2://独赢[欧赔]
                            ((SnookerIndexChildFragment) fragments.get(2)).updateTennisOdds(mSnookerOdds);
                            L.e(TAG, "ws_json_snooker_odds = " + mSnookerOdds.getDataObj().getMatchOdd().getL()+"++++++++++++++");
                            break;

                    }
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sindex, container, false);

        ButterKnife.bind(this, mView);
        initView();
        setListener();
        initData();
        connectWebSocket();
        return mView;
    }

    private void initView() {

        mRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                        //刷新各个fragment
                    }
                }, 1000);
            }
        });

        mItems = getResources().getStringArray(R.array.zhishu_select);

        fragments = new ArrayList<>();
        mTitles = new String[]{getActivity().getResources().getString(R.string.odd_plate_rb_txt), getActivity().getResources().getString(R.string.asiasize),
                getActivity().getResources().getString(R.string.odd_op_rb_txt), MyApp.getContext().getResources().getString(R.string.snooker_index_single_double)};

        if (mBallType == BallType.SNOOKER) {
            mTextMatch.setText(getActivity().getString(R.string.snooker_txt));
            mTitles = new String[]{getActivity().getResources().getString(R.string.odd_plate_rb_txt), getActivity().getResources().getString(R.string.asiasize),
                    getActivity().getResources().getString(R.string.odd_op_rb_txt), MyApp.getContext().getResources().getString(R.string.snooker_index_single_double)};

            fragments.add(SnookerIndexChildFragment.newInstance(ODDS_LET, mBallType));
            fragments.add(SnookerIndexChildFragment.newInstance(ODDS_SIZE, mBallType));
            fragments.add(SnookerIndexChildFragment.newInstance(ODDS_EURO, mBallType));
            fragments.add(SnookerIndexChildFragment.newInstance(SINGLE_DOUBLE, mBallType));

        } else if (mBallType == BallType.TENNLS) { //网球
            mTextMatch.setText(getActivity().getString(R.string.tennisball_txt));
            mTitles = new String[]{getActivity().getResources().getString(R.string.odd_plate_rb_txt), getActivity().getResources().getString(R.string.asiasize),
                    getActivity().getResources().getString(R.string.odd_op_rb_txt)};
            fragments.add(SnookerIndexChildFragment.newInstance(TennisOddsTypeEnum.ASIALET, BallType.TENNLS));
            fragments.add(SnookerIndexChildFragment.newInstance(TennisOddsTypeEnum.ASIASIZE, BallType.TENNLS));
            fragments.add(SnookerIndexChildFragment.newInstance(TennisOddsTypeEnum.EURO, BallType.TENNLS));
        }

        mTabsAdapter = new TabsAdapter(getChildFragmentManager());
        mTabsAdapter.setTitles(mTitles);
        mTabsAdapter.addFragments(fragments);
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setOffscreenPageLimit(5);
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
//                refreshAllChildFragments();
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshing(false);
                        refreshAllChildFragments();
                    }
                }, 1000);
            }
        });
    }

    private void setListener() {
        mLiDateSelect.setOnClickListener(this);
        mMatchSelect.setOnClickListener(this);
        mCompanySelect.setOnClickListener(this);
    }

    /**
     * 显示日期选择 dialog
     */
    private void showDateChooseDialog() {
        maybeInitDateChooseDialog();
        if (!mDateChooseDialogFragment.isVisible()) {
            mDateChooseDialogFragment.show(getChildFragmentManager(), "dateChooseFragment");
        }
    }


    /**
     * 日期选择 Fragment 初始化
     */
    private void maybeInitDateChooseDialog() {
        if (mDateChooseDialogFragment != null) return;
        mDateChooseDialogFragment = DateChooseDialogFragment.newInstance(currentDate,
                new DateChooseDialogFragment.OnDateChooseListener() {
                    @Override
                    public void onDateChoose(String date) {
                        choosenDate = date;
                        mTextDateSelect.setText(DateUtil.convertDateToNation(date));
                        setRefreshing(true);
                        refreshAllChildFragments();
                    }
                });
    }

    private void initData() {

    }

    /**
     * 刷新每个子fragment
     */
    public void refreshAllChildFragments() {
        for (Fragment childFragment : fragments) {

            ((SnookerIndexChildFragment) childFragment).refreshDate(choosenDate == null ? currentDate : choosenDate);
        }
    }

    /**
     * 设置刷新状态
     *
     * @param b 是否正在刷新
     */
    public void setRefreshing(boolean b) {
        mRefreshLayout.setRefreshing(b);
    }

    @Override
    public void onError() {

    }

    /**
     * 获取公司列表
     *
     * @return 公司列表
     */
    public void setCompanyList(ArrayList<SnookerIndexBean.CompanyEntity> companyList) {
        mCompanySelect.setVisibility(View.VISIBLE);
        this.companyList.clear();
        this.companyList.addAll(companyList); //保证操作的是同一数据源
    }

    /**
     * 获取公司列表
     *
     * @return
     */
    public List<SnookerIndexBean.CompanyEntity> getCompanyList() {
        return companyList;
    }

    /**
     * 弹出切换球类的下拉
     */
    private void switchMatch(View v) {
        mImgMatch.setImageResource(R.mipmap.nav_icon_up);
        backgroundAlpha(getActivity(), 0.5f);
        popWindow(v);
    }

    private void popWindow(final View v) {
        final View mView = View.inflate(getActivity(), R.layout.pop_select, null);
        // 创建ArrayAdapter对象
        BallChoiceArrayAdapter mAdapter = new BallChoiceArrayAdapter(getActivity(), mItems, mBallType); //在第几个

        ListView listview = (ListView) mView.findViewById(R.id.match_type);
        listview.setAdapter(mAdapter);


        final PopupWindow popupWindow = new PopupWindow(mView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAsDropDown(mLinearHeader);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //tv_match_name.setText(((TextView) view.findViewById(R.id.tv)).getText().toString());
                // iv_match.setImageResource(R.mipmap.nav_icon_cbb);


//                if (mBallType == BallType.SNOOKER) {
//                    L.d("websocket123", ">>>>>>>>斯洛克指数关闭");
//
//                } else if (mBallType == BallType.TENNLS) {
//                    L.d("websocket123", ">>>>>>>>网球指数关闭");
//
//                }

                closeWebSocket();


                EventBus.getDefault().post(new ScoreSwitchFg(position));

                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mImgMatch.setImageResource(R.mipmap.nav_icon_cbb);
                backgroundAlpha(getActivity(), 1f);
            }
        });
    }

    /**
     * 显示公司选择
     */
    public void showCompanyChooseDialog() {
        maybeInitCompanyChooseDialog();
        if (!mCompanyChooseDialogFragment.isVisible()) {
            mCompanyChooseDialogFragment.show(getChildFragmentManager(), "companyChooseDialog");
        }

    }

    /**
     * 初始化公司选择Dialog
     */
    private void maybeInitCompanyChooseDialog() {
        if (mCompanyChooseDialogFragment == null) {
            mCompanyChooseDialogFragment = SnookerCompanyChooseDialogFragment.newInstance(companyList,
                    new SnookerCompanyChooseDialogFragment.OnFinishSelectionListener() {
                        @Override
                        public void onFinishSelection() {
                            for (Fragment childFragment : fragments) {
                                ((SnookerIndexChildFragment) childFragment).updateFilterData();
                            }
                        }
                    });
        }
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


    /**
     * 设置日期跟公司
     */
    public void setDateAndCompany(SnookerIndexBean bean) {
        currentDate = bean.getCurrDate();
        mLiDateSelect.setVisibility(View.VISIBLE);
        mCompanySelect.setVisibility(View.VISIBLE);
        mTextDateSelect.setText(DateUtil.convertDateToNation(bean.getCurrDate()));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_date_layout: //日期选择
                showDateChooseDialog();
                break;
            case R.id.public_img_company:
                showCompanyChooseDialog();
                break;
            case R.id.ll_match_select: //切换比赛
                switchMatch(v);
                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        closeWebSocket();
    }

    public void onEventMainThread(CloseCpiWebSocketEventBus closeWebSocketEventBus) {

        if (closeWebSocketEventBus.isVisible()) {
//            if (mBallType == BallType.SNOOKER) {
//                L.d("websocket123", "_______斯洛克 指数 关闭 fg");
//
//            } else if (mBallType == BallType.TENNLS) {
//                L.d("websocket123", "________网球 指数 关闭 fg");
//
//            }
            closeWebSocket();
        } else {
            if (closeWebSocketEventBus.getIndex() == 2 || closeWebSocketEventBus.getIndex() == 3) {
//                if (mBallType == BallType.SNOOKER) {
//                    L.d("websocket123", "_______斯洛克 指数 打开 fg");
//
//                } else if (mBallType == BallType.TENNLS) {
//                    L.d("websocket123", "_______网球 指数 打开 fg");
//                }


                connectWebSocket();
            }
        }

    }
}
