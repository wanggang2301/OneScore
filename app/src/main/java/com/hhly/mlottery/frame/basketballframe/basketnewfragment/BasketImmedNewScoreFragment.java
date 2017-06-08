package com.hhly.mlottery.frame.basketballframe.basketnewfragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.activity.BasketFiltrateActivity;
import com.hhly.mlottery.mvp.bettingmvp.mvpview.MvpBettingRecommendActivity;
import com.hhly.mlottery.adapter.ScheduleDateAdapter;
import com.hhly.mlottery.adapter.basketball.BasketballScoreListAdapter;
import com.hhly.mlottery.bean.basket.BasketAllOddBean;
import com.hhly.mlottery.bean.basket.BasketMatchBean;
import com.hhly.mlottery.bean.basket.BasketMatchFilter;
import com.hhly.mlottery.bean.basket.BasketNewFilterBean;
import com.hhly.mlottery.bean.basket.BasketNewRootBean;
import com.hhly.mlottery.bean.basket.BasketOddBean;
import com.hhly.mlottery.bean.basket.BasketRootBean;
import com.hhly.mlottery.bean.basket.BasketScoreBean;
import com.hhly.mlottery.bean.scheduleBean.ScheduleDate;
import com.hhly.mlottery.bean.websocket.WebBasketAllOdds;
import com.hhly.mlottery.bean.websocket.WebBasketMatch;
import com.hhly.mlottery.bean.websocket.WebBasketOdds;
import com.hhly.mlottery.bean.websocket.WebBasketOdds5;
import com.hhly.mlottery.callback.DateOnClickListener;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.FootBallMatchFilterTypeEnum;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.footballframe.eventbus.BasketDetailsEventBusEntity;
import com.hhly.mlottery.frame.footballframe.eventbus.BasketScoreImmedEventBusEntity;
import com.hhly.mlottery.frame.footballframe.eventbus.BasketScoreSettingEventBusEntity;
import com.hhly.mlottery.frame.scorefrag.BasketBallScoreFragment;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.FiltrateCupsMap;
import com.hhly.mlottery.util.FocusUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import de.greenrobot.event.EventBus;

/**
 * 篮球比分fragment
 * Created by yixq on 2017/03/17
 */
public class BasketImmedNewScoreFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ImmedBasketballFragment";
    private static final String PARAMS = "BASKET_PARAMS";
    private RecyclerView explistview;
    private LinearLayoutManager linearLayoutManager;

    //筛选的数据
    public static List<BasketMatchFilter> mChickedFilter = new ArrayList<>();//选中的
    public static List<BasketMatchFilter> mAllFilter = new ArrayList<>();//当前日期的所有联赛
    public static List<List<BasketMatchFilter>> mAllDateFilter;//所有的联赛
    //内容数据
    private List<List<BasketMatchBean>> mAllMatchdata;//所有的比赛list
    private List<String> mAllGroupdata;//所有日期list
    private List<List<BasketMatchBean>> childrenDataList;//显示的比赛list
    private List<String> groupDataList;//显示日期list

    private List<BasketRootBean> mMatchdata = new ArrayList<>();//match的 内容(json)
    private List<BasketNewFilterBean> mMatchFilter = new ArrayList<>();//match的 内容(json)


    private List<BasketMatchBean> currentMatchData;//当前选择日期所显示的数据

    private int TITTLEDATETYPE = 0;//日期头部type
    private int LISTDATATYPE = 1;//列表数据type

    private int expandFlag = -1;//控制列表的展开
    private BasketballScoreListAdapter adapter;

    private View mView;
    private Context mContext;

    private RelativeLayout mbasketball_unfocus;//暂无关注图标
    private RelativeLayout mbasket_unfiltrate; //暂无赛选图标


    private LinearLayout mLoadingLayout;
    private RelativeLayout mNoDataLayout;
    private LinearLayout mErrorLayout;
    private TextView mReloadTvBtn;// 刷新 控件

    private Intent mIntent;
    Handler mLoadHandler = new Handler();

    private BasketFocusClickListener mFocusClickListener; //关注点击监听

    private SwipeRefreshLayout mSwipeRefreshLayout; //下拉刷新

    private int mBasketballType; //判断是哪个fragment(即时 赛果 赛程)

    private boolean isFilter = false;  //是否赛选过

    public static int isLoad = -1;
    /**
     * 请求数据的url
     */
    private String loadUrl;
    private List<String> mAllDateList;//所有的日期（ps:即时接口日期可能为昨天+今天 或 今天+明天 ，用于日期弹框）

    public static int getIsLoad() {
        return isLoad;
    }

    public static final int TYPE_FOCUS = 3;
    /**
     * 请求关注列表后的返回结果
     */
    public static boolean requestSuccess = false;
    private static final String ISNEW_FRAMEWORK = "isnew_framework";
    private boolean isNewFrameWork;
    private static final String BASKET_ENTRY_TYPE = "basketEntryType";//入口标记

    private int mEntryType; // 标记入口 判断是从哪里进来的 (0:首页入口  1:新导航条入口)

    /**
     * 切换后更新显示的fragment
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        updateAdapter();
    }

    /**
     * fragment 传参
     *
     * @param basketballType
     * @return
     */
    public static BasketImmedNewScoreFragment newInstance(int basketballType, boolean isNewFramWork, int basketEntryType) {
        BasketImmedNewScoreFragment fragment = new BasketImmedNewScoreFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAMS, basketballType);
        bundle.putInt(BASKET_ENTRY_TYPE, basketEntryType);
        bundle.putBoolean(ISNEW_FRAMEWORK, isNewFramWork);

        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBasketballType = 0;
            isNewFrameWork = getArguments().getBoolean(ISNEW_FRAMEWORK);
            mEntryType = getArguments().getInt(BASKET_ENTRY_TYPE);
        }
        EventBus.getDefault().register(this);

    }


    /**
     * 返回当前程序版本号 versioncode
     */
    public static String getAppVersionCode(Context context) {
        String versioncode = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versioncode = pi.versionCode + "";
            if (versioncode == null || versioncode.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versioncode;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.basket_score_fragment, container, false);

        initView();
        return mView;
    }

    /**
     * 子线程 处理数据加载
     */
    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            initData();
        }
    };

    /**
     * 设置显示状态
     *
     * @param status
     */
    //显示状态
    private static final int SHOW_STATUS_LOADING = 1;//加载中
    private static final int SHOW_STATUS_ERROR = 2;//加载失败
    private static final int SHOW_STATUS_NO_DATA = 3;//暂无数据
    private static final int SHOW_STATUS_SUCCESS = 4;//加载成功
    private final static int SHOW_STATUS_REFRESH_ONCLICK = 5;//点击刷新
    private final static int SHOW_STATUS_CURRENT_ONDATA = 6;//当前日期无数据

    private void setStatus(int status) {

        if (status == SHOW_STATUS_LOADING) {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(true);
        } else if (status == SHOW_STATUS_SUCCESS) {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
        } else if (status == SHOW_STATUS_REFRESH_ONCLICK) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(true);
        } else if (status == SHOW_STATUS_CURRENT_ONDATA) {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mLoadingLayout.setVisibility((status == SHOW_STATUS_REFRESH_ONCLICK) ? View.VISIBLE : View.GONE);
        mErrorLayout.setVisibility(status == SHOW_STATUS_ERROR ? View.VISIBLE : View.GONE);
        mNoDataLayout.setVisibility(status == SHOW_STATUS_NO_DATA ? View.VISIBLE : View.GONE);
    }

    /**
     * 初始化VIEW
     */
    private void initView() {
        //暂无关注
        mbasketball_unfocus = (RelativeLayout) mView.findViewById(R.id.basketball_immediate_unfocus_pinned);

        //暂无筛选
        mbasket_unfiltrate = (RelativeLayout) mView.findViewById(R.id.basket_unfiltrate);

        explistview = (RecyclerView) mView.findViewById(R.id.basket_recyclerView);
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        explistview.setLayoutManager(linearLayoutManager);

        // 下拉刷新
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.basketball_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(MyApp.getContext(), StaticValues.REFRASH_OFFSET_END));

        mLoadingLayout = (LinearLayout) mView.findViewById(R.id.basketball_immediate_loading);

        mNoDataLayout = (RelativeLayout) mView.findViewById(R.id.basket_undata);

        mErrorLayout = (LinearLayout) mView.findViewById(R.id.basketball_immediate_error);
        mReloadTvBtn = (TextView) mView.findViewById(R.id.basketball_immediate_error_btn);
        mReloadTvBtn.setOnClickListener(this);

        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        /**
         * 数据访问成功时打开赛选开关
         */
//        mFilterImgBtn.setClickable(true);
    }

    private int mSize; //记录共有几天的数据

    public static void requestSuccess() {
        requestSuccess = true;

    }

    private void initData() {
        Map<String, String> params = new HashMap<>();
        String version = getAppVersionCode(mContext);//获得当前版本号 android:versionCode="5"
        params.put("version", version);//接口添加 version=xx 字段
        params.put("appType", "2");//接口添加 &appType=2 字段

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_BASKET_NEW_IMMEDIATE, params, new VolleyContentFast.ResponseSuccessListener<BasketNewRootBean>() {
            @Override
            public void onResponse(BasketNewRootBean json) {

                if (getActivity() == null) {
                    return;
                }

                isLoad = 1;
                if (json == null || json.getMatchData() == null || json.getMatchData().size() == 0) {
                    setStatus(SHOW_STATUS_NO_DATA);
                    return;
                }

                if (!PreferenceUtil.getString(FootBallMatchFilterTypeEnum.BASKET_CURR_DATE_IMMEDIA, "").equals(json.getFilerDate())) {
                    PreferenceUtil.removeKey(FootBallMatchFilterTypeEnum.BASKET_IMMEDIA);
                    PreferenceUtil.commitString(FootBallMatchFilterTypeEnum.BASKET_CURR_DATE_IMMEDIA, json.getFilerDate());
                }

                mMatchdata = json.getMatchData();

                mMatchFilter = json.getMatchFilter();

                mSize = json.getMatchData().size();

                groupDataList = new ArrayList<String>();
                childrenDataList = new ArrayList<List<BasketMatchBean>>();
                mAllGroupdata = new ArrayList<>();
                mAllMatchdata = new ArrayList<>();
                currentMatchData = new ArrayList<BasketMatchBean>();
                mAllDateFilter = new ArrayList<List<BasketMatchFilter>>();
                mAllDateList = new ArrayList<String>();
                for (int i = 0; i < mSize; i++) {
                    mAllMatchdata.add(mMatchdata.get(i).getMatch());//所有日期里的比赛
                    mAllDateList.add(mMatchdata.get(i).getDate());//所有日期
                }

                if (json.getMatchFilter() != null && json.getMatchFilter().size() != 0) {

                    for (int i = 0; i < json.getMatchFilter().size(); i++) {
                        /**
                         * 获得所有日期的所有联赛
                         */
                        mAllDateFilter.add(mMatchFilter.get(i).getFilterDto());
                    }
                    /**
                     * 获得当前日期的所有联赛
                     */
                    mAllFilter = mAllDateFilter.get(currentDatePosition);
                }

                /**
                 *判断是否 经过筛选
                 */
                if (isFilter) { //已筛选

                    if (FiltrateCupsMap.basketImmedateCups.length == 0) {
                        List<BasketMatchFilter> noCheckedFilters = new ArrayList<>();
                        mChickedFilter = noCheckedFilters;//筛选0场后，再次进入赛选页面 显示已选中0场（全部不选中）
                        mbasket_unfiltrate.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        mLoadingLayout.setVisibility(View.GONE);
                        return;
                    } else {
                        List<BasketMatchBean> checkedMatchs = new ArrayList<>();
                        for (BasketMatchBean matchBean : mAllMatchdata.get(currentDatePosition)) {//只遍历当前日期里的比赛
                            for (String checkedId : FiltrateCupsMap.basketImmedateCups) {
                                if (matchBean.getLeagueId().equals(checkedId)) {
                                    checkedMatchs.add(matchBean);
                                }
                            }
                        }

                        if (checkedMatchs.size() != 0) {

                            BasketMatchBean itemData = new BasketMatchBean();
                            itemData.setItemType(TITTLEDATETYPE);
                            itemData.setDate(DateUtil.convertDateToNation(mAllMatchdata.get(currentDatePosition).get(0).getDate()));
                            itemData.setThirdId("*");//这里id需要赋值，否则推送更新的时候会 NullPointerException
                            currentMatchData.add(itemData);
                            for (BasketMatchBean data : checkedMatchs) {
                                data.setItemType(LISTDATATYPE);
                                currentMatchData.add(data);
                            }
                        }
                    }
                } else {
                    //未筛选
                    switchDate(currentDatePosition);
                    mChickedFilter = mAllFilter;//默认选中全部
                }

                if (adapter == null) {
                    adapter = new BasketballScoreListAdapter(mContext, currentMatchData, 0);
                    explistview.setAdapter(adapter);
                    adapter.setmFocus(mFocusClickListener);//设置关注
                    adapter.setDateOnClickListener(mDateOnClickListener);
                    adapter.setmOnItemClickListener(new BasketballScoreListAdapter.OnRecycleItemClickListener() {
                        @Override
                        public void onItemClick(View view, BasketMatchBean currData) {
                            Intent intent = new Intent(getActivity(), BasketDetailsActivityTest.class);
                            intent.putExtra(BasketDetailsActivityTest.BASKET_THIRD_ID, currData.getThirdId());//跳转到详情
                            intent.putExtra(BasketDetailsActivityTest.BASKET_MATCH_STATUS, currData.getMatchStatus());//跳转到详情
                            intent.putExtra("currentfragment", mBasketballType);
                            intent.putExtra(BasketDetailsActivityTest.BASKET_MATCH_LEAGUEID, currData.getLeagueId());
                            intent.putExtra(BasketDetailsActivityTest.BASKET_MATCH_MATCHTYPE, currData.getMatchType());
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                        }
                    });
                } else {
                    updateAdapter();
                }

                setStatus(SHOW_STATUS_SUCCESS);

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            /**
             * 加载失败
             *
             * @param exception
             */
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                isLoad = 0;
                setStatus(SHOW_STATUS_ERROR);
            }
        }, BasketNewRootBean.class);
        fucusChicked();//点击关注监听
        choiceDateList();
    }

    private DateOnClickListener mDateOnClickListener;
    private ListView mDateListView;
    private List<ScheduleDate> mDatelist; // 日期
    private static int currentDatePosition = 0;//记录当前日期选择器中日期的位置

    private void choiceDateList() {
        mDateOnClickListener = new DateOnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
                final AlertDialog alertDialog = builder.create();
                LayoutInflater infla = LayoutInflater.from(getActivity());
                View alertDialogView = infla.inflate(R.layout.alertdialog, null);
                mDateListView = (ListView) alertDialogView.findViewById(R.id.listdate);
                initListImmediateDateAndWeek(mAllDateList, currentDatePosition);
                mDateListView.setAdapter(mImmediateDateAdapter);
                mDateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {

                        if (isFilter) {//这里如果是筛选过后的界面日期切换需要重置筛选状态，否则会影响切换后的数据刷新
                            if (position == currentDatePosition) {
                                isFilter = true;
                                updateAdapter();
                            } else {
                                isFilter = false;
                                switchDate(position);
                            }
                        }
                        if (position == currentDatePosition) {
                            updateAdapter();
                        } else {
                            switchDate(position);
                        }
                        currentDatePosition = position;
                        if (mAllDateFilter != null || mAllDateFilter.size() != 0) {
                            mAllFilter = mAllDateFilter.get(position);
                            mChickedFilter = mAllFilter;//切换日期后选中全部
                        }
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                alertDialog.getWindow().setContentView(alertDialogView);
                alertDialog.setCanceledOnTouchOutside(true);
            }
        };
    }

    /**
     * 初始化即时的日期
     *
     * @param s
     */
    private ScheduleDateAdapter mImmediateDateAdapter;

    private void initListImmediateDateAndWeek(List<String> s, int position) {
        mDatelist = new ArrayList<ScheduleDate>();
        for (int i = 0; i < s.size(); i++) {
            mDatelist.add(new ScheduleDate(DateUtil.convertDateToNation(s.get(i)), DateUtil.getWeekOfXinQi(DateUtil.parseDate(s.get(i)))));
        }
        mImmediateDateAdapter = new ScheduleDateAdapter(mDatelist, mContext, position);
    }


    private void switchDate(int index) {
        currentMatchData.clear();
        BasketMatchBean itemData = new BasketMatchBean();
        itemData.setItemType(TITTLEDATETYPE);
        itemData.setDate(DateUtil.convertDateToNation(mAllMatchdata.get(index).get(0).getDate()));
        itemData.setThirdId("*");//这里id需要赋值，否则推送更新的时候会 NullPointerException
        currentMatchData.add(itemData);

        List<String> filiterLeagueIds = PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.BASKET_IMMEDIA);

        if (filiterLeagueIds.size() > 0) {
            for (BasketMatchBean data : mAllMatchdata.get(index)) {
                for (String id : filiterLeagueIds) {
                    if (data.getLeagueId().equals(id)) {
                        data.setItemType(LISTDATATYPE);
                        currentMatchData.add(data);
                    }
                }
            }
        } else {
            for (BasketMatchBean data : mAllMatchdata.get(index)) {
                data.setItemType(LISTDATATYPE);
                currentMatchData.add(data);
            }
        }
        updateAdapter();
    }

    /**
     * 点击关注事件
     */
    public void fucusChicked() {
        mFocusClickListener = new BasketFocusClickListener() {
            @Override
            public void FocusOnClick(View v, BasketMatchBean root) {

                boolean isCheck = (Boolean) v.getTag();// 检查之前是否被选中

                if (!isCheck) {//未关注->关注
                    FocusUtils.addBasketFocusId(root.getThirdId());
                    v.setTag(true);

                } else {//关注->未关注
                    FocusUtils.deleteBasketFocusId(root.getThirdId());
                    v.setTag(false);
                    if (mBasketballType == TYPE_FOCUS) {
                        if (mEntryType == 0) {
                        } else if (mEntryType == 1) {
                            ((BasketBallScoreFragment) getParentFragment()).focusCallback();
                        }
                    }
                }
                updateAdapter();//防止复用
                if (mEntryType == 0) {
                } else if (mEntryType == 1) {
                    ((BasketBallScoreFragment) getParentFragment()).focusCallback();
                }
            }
        };
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        isLoad = -1;
        setStatus(SHOW_STATUS_LOADING);
        mLoadHandler.postDelayed(mRun, 0);
        if (isNewFrameWork) {
            ((BasketBallScoreFragment) getParentFragment()).reconnectWebSocket();
        }
    }


    // 定义关注监听
    public interface BasketFocusClickListener {
        void FocusOnClick(View view, BasketMatchBean root);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.basketball_immediate_error_btn:
                isLoad = -1;
                MobclickAgent.onEvent(mContext, "Basketball_Refresh");
                setStatus(SHOW_STATUS_REFRESH_ONCLICK);
                mLoadHandler.postDelayed(mRun, 0);
                break;
            default:
                break;
        }
    }

    public void updateAdapter() {
        if (adapter == null) {
            return;
        }
        adapter.updateDatas(currentMatchData);
        adapter.notifyDataSetChanged();
    }

    public void LoadData() {
        setStatus(SHOW_STATUS_LOADING);
        mLoadHandler.post(mRun);
    }

    public void handleSocketMessage(String text) {
        L.d("yxq===>>handleSocketMessage = ", text);
        if (adapter == null) {
            return;
        }
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
            mSocketHandler.sendMessage(msg);
        }
    }


    Handler mSocketHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            L.e(TAG, "__handleMessage__");
            L.e(TAG, "msg.arg1 = " + msg.arg1);
            if (msg.arg1 == 100) {  //type 为100 ==> 比分推送
                String ws_json = (String) msg.obj;
                L.e(TAG, "ws_json =AAAAAAAAAAAA " + ws_json);
                WebBasketMatch mWebBasketMatch = null;
                try {
                    mWebBasketMatch = JSON.parseObject(ws_json, WebBasketMatch.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    mWebBasketMatch = JSON.parseObject(ws_json, WebBasketMatch.class);
                }

                updateListViewItemStatus(mWebBasketMatch);  //比分更新
            } else if (msg.arg1 == 101) {  //type 为101 ==> 赔率推送
                String ws_json = (String) msg.obj;
                L.e(TAG, "ws_json" + ws_json);
                WebBasketOdds mWebBasketOdds = null;
                try {
                    mWebBasketOdds = JSON.parseObject(ws_json, WebBasketOdds.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    mWebBasketOdds = JSON.parseObject(ws_json, WebBasketOdds.class);
                }
                updateListViewItemOdd(mWebBasketOdds);
            }
        }
    };


    private void updateListViewItemStatus(WebBasketMatch webBasketMatch) {
        Map<String, String> data = webBasketMatch.getData();
        synchronized (currentMatchData) {
            for (BasketMatchBean matchchildern : currentMatchData) {
                if (matchchildern.getThirdId().equals(webBasketMatch.getThirdId())) {
                    if (matchchildern.getMatchScore() != null) {
                        String newscoreguest = matchchildern.getMatchScore().getGuestScore() + ""; //推送之前的比分客队
                        String newscorehome = matchchildern.getMatchScore().getHomeScore() + "";
                        //判断推送前后的比分是否变化, 变化==>开启动画
                        if (data.get("guestScore") != null && data.get("homeScore") != null) {
                            if (!data.get("guestScore").equals(newscoreguest)) { //isScroll 为 true：正在滑动  滑动中不启动动画;
                                matchchildern.setIsGuestAnim(true);
                            } else {
                                matchchildern.setIsGuestAnim(false);
                            }
                            if (!data.get("homeScore").equals(newscorehome)) {
                                matchchildern.setIsHomeAnim(true);
                            } else {
                                matchchildern.setIsHomeAnim(false);
                            }
                            updateMatchStatus(matchchildern, data);// 修改Match里面的数据
                        }
                    } else {
                        /**
                         * 未开始（VS）==>开始时候的处理
                         */
                        BasketScoreBean score = new BasketScoreBean();
                        for (Map.Entry<String, String> entry : data.entrySet()) {
                            switch (entry.getKey()) {
                                case "guest1":
                                    score.setGuest1(Integer.parseInt(entry.getValue()));
                                    break;
                                case "guest2":
                                    score.setGuest2(Integer.parseInt(entry.getValue()));
                                    break;
                                case "guest3":
                                    score.setGuest3(Integer.parseInt(entry.getValue()));
                                    break;
                                case "guest4":
                                    score.setGuest4(Integer.parseInt(entry.getValue()));
                                    break;
                                case "guestOt1":
                                    score.setGuestOt1(Integer.parseInt(entry.getValue()));
                                    break;
                                case "guestOt2":
                                    score.setGuestOt2(Integer.parseInt(entry.getValue()));
                                    break;
                                case "guestOt3":
                                    score.setGuestOt3(Integer.parseInt(entry.getValue()));
                                    break;
                                case "guestScore":
                                    score.setGuestScore(Integer.parseInt(entry.getValue()));
                                    break;
                                case "home1":
                                    score.setHome1(Integer.parseInt(entry.getValue()));
                                    break;
                                case "home2":
                                    score.setHome2(Integer.parseInt(entry.getValue()));
                                    break;
                                case "home3":
                                    score.setHome3(Integer.parseInt(entry.getValue()));
                                    break;
                                case "home4":
                                    score.setHome4(Integer.parseInt(entry.getValue()));
                                    break;
                                case "homeOt1":
                                    score.setHomeOt1(Integer.parseInt(entry.getValue()));
                                    break;
                                case "homeOt2":
                                    score.setHomeOt2(Integer.parseInt(entry.getValue()));
                                    break;
                                case "homeOt3":
                                    score.setHomeOt3(Integer.parseInt(entry.getValue()));
                                    break;
                                case "homeScore":
                                    score.setHomeScore(Integer.parseInt(entry.getValue()));
                                    break;
                                case "addTime":
                                    score.setAddTime(Integer.parseInt(entry.getValue()));
                                    break;
                                case "remainTime":
                                    score.setRemainTime(entry.getValue());
                                    break;
                                default:
                                    break;
                            }
                        }

                        matchchildern.setMatchScore(score);
                        updateMatchStatus(matchchildern, data);
                    }
                    updateAdapter();
                    break;
                }
            }
        }
    }

    private void updateListViewItemOdd(WebBasketOdds webBasketOdds) {
        WebBasketAllOdds data = webBasketOdds.getData();
        synchronized (currentMatchData) {
            for (BasketMatchBean matchchildern : currentMatchData) {
                if (matchchildern.getThirdId().equals(webBasketOdds.getThirdId())) {
                    updateMatchOdd(matchchildern, data);
                    updateAdapter();
                    break;
                }
            }
        }
    }

    /**
     * 更新单个比赛状态
     */
    //比分
    private void updateMatchStatus(BasketMatchBean matchchildern, Map<String, String> data) {
        if (null != data.get("section")) {
            if (!data.get("section").equals("0")) {
                matchchildern.setSection(Integer.parseInt(data.get("section")));
            }
        }
        if (null != data.get("matchStatus")) {
            matchchildern.setMatchStatus(Integer.parseInt(data.get("matchStatus")) + "");
        }
        if (null != data.get("remainTime") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setRemainTime(data.get("remainTime"));
        }
        if (null != data.get("homeScore") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setHomeScore(Integer.parseInt(data.get("homeScore")));
        }
        if (null != data.get("guestScore") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setGuestScore(Integer.parseInt(data.get("guestScore")));
        }
        if (null != data.get("home1") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setHome1(Integer.parseInt(data.get("home1")));
        }
        if (null != data.get("guest1") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setGuest1(Integer.parseInt(data.get("guest1")));
        }
        if (null != data.get("home2") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setHome2(Integer.parseInt(data.get("home2")));
        }
        if (null != data.get("guest2") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setGuest2(Integer.parseInt(data.get("guest2")));
        }
        if (null != data.get("home3") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setHome3(Integer.parseInt(data.get("home3")));
        }
        if (null != data.get("guest3") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setGuest3(Integer.parseInt(data.get("guest3")));
        }
        if (null != data.get("home4") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setHome4(Integer.parseInt(data.get("home4")));
        }
        if (null != data.get("guest4") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setGuest4(Integer.parseInt(data.get("guest4")));
        }
        if (null != data.get("homeOt1") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setHomeOt1(Integer.parseInt(data.get("homeOt1")));
        }
        if (null != data.get("guestOt1") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setGuestOt1(Integer.parseInt(data.get("guestOt1")));
        }
        if (null != data.get("homeOt2") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setHomeOt2(Integer.parseInt(data.get("homeOt2")));
        }
        if (null != data.get("guestOt2") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setGuestOt2(Integer.parseInt(data.get("guestOt2")));
        }
        if (null != data.get("homeOt3") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setHomeOt3(Integer.parseInt(data.get("homeOt3")));
        }
        if (null != data.get("guestOt3") && matchchildern.getMatchScore() != null) {
            matchchildern.getMatchScore().setGuestOt3(Integer.parseInt(data.get("guestOt3")));
        }
    }

    //赔率
    private void updateMatchOdd(BasketMatchBean matchchildern, WebBasketAllOdds dataodd) {
        L.e(TAG, "update matchchildern odd id = " + matchchildern.getThirdId());

        List<WebBasketOdds5> mOddsList = new ArrayList<>();
        WebBasketOdds5 mAsiaSize = dataodd.getAsiaSize(); //大小球
        WebBasketOdds5 mEuro = dataodd.getEuro();//欧赔
        WebBasketOdds5 mAsiaLet = dataodd.getAsiaLet();//亚盘
        mOddsList.add(mAsiaLet);
        mOddsList.add(mEuro);
        mOddsList.add(mAsiaSize);

        for (WebBasketOdds5 map : mOddsList) {
            if (map != null) {
                if (map.getCrown() != null) {
                    if (map.getCrown().get("handicap").equals("asiaLet") && matchchildern.getMatchOdds() != null) {
                        Map<String, BasketAllOddBean> odds = matchchildern.getMatchOdds();
                        BasketAllOddBean odd = odds.get("asiaLet");
                        if (odd == null) {//
                            odd = new BasketAllOddBean();
                            BasketOddBean modd = odd.getCrown();
                            if (modd == null) {
                                modd = new BasketOddBean();
                            }
                            modd.setHandicap("asiaLet");
                            modd.setHandicapValue(map.getCrown().get("handicapValue"));
                            modd.setRightOdds(map.getCrown().get("rightOdds"));
                            modd.setLeftOdds(map.getCrown().get("leftOdds"));
                            odd.setCrown(modd);
                            odds.put("asiaLet", odd);
                        }

                        if (odd.getCrown() != null) {
                            L.w(TAG, "odd = " + odd);
                            odd.getCrown().setLeftOdds(map.getCrown().get("leftOdds"));
                            odd.getCrown().setRightOdds(map.getCrown().get("rightOdds"));
                            odd.getCrown().setHandicapValue(map.getCrown().get("handicapValue"));
                        }

                    } else if (map.getCrown().get("handicap").equals("asiaSize") && matchchildern.getMatchOdds() != null) {
                        Map<String, BasketAllOddBean> odds = matchchildern.getMatchOdds();
                        BasketAllOddBean odd = odds.get("asiaSize");
                        if (odd == null) {//
                            odd = new BasketAllOddBean();
                            BasketOddBean modd = odd.getCrown();
                            if (modd == null) {
                                modd = new BasketOddBean();
                            }
                            modd.setHandicap("asiaSize");
                            modd.setHandicapValue(map.getCrown().get("handicapValue"));
                            modd.setRightOdds(map.getCrown().get("rightOdds"));
                            modd.setLeftOdds(map.getCrown().get("leftOdds"));
                            odd.setCrown(modd);
                            odds.put("asiaSize", odd);
                        }

                        if (odd.getCrown() != null) {
                            L.w(TAG, "odd = " + odd);
                            odd.getCrown().setLeftOdds(map.getCrown().get("leftOdds"));
                            odd.getCrown().setRightOdds(map.getCrown().get("rightOdds"));
                            odd.getCrown().setHandicapValue(map.getCrown().get("handicapValue"));
                        }
                    }
                }
                if (map.getEuro() != null) {
                    if (map.getEuro().get("handicap").equals("euro") && matchchildern.getMatchOdds() != null) {
                        Map<String, BasketAllOddBean> odds = matchchildern.getMatchOdds();
                        BasketAllOddBean odd = odds.get("euro");
                        if (odd == null) {//
                            odd = new BasketAllOddBean();
                            BasketOddBean modd = odd.getEuro();
                            if (modd == null) {
                                modd = new BasketOddBean();
                            }
                            modd.setHandicap("euro");
                            modd.setRightOdds(map.getEuro().get("rightOdds"));
                            modd.setLeftOdds(map.getEuro().get("leftOdds"));
                            odd.setEuro(modd);
                            odds.put("euro", odd);
                        }
                        if (odd.getEuro() != null) {
                            L.w(TAG, "odd = " + odd);
                            odd.getEuro().setLeftOdds(map.getEuro().get("leftOdds"));
                            odd.getEuro().setRightOdds(map.getEuro().get("rightOdds"));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        L.v(TAG, "___onResume___");
    }

    private boolean isDestroy = false;

    @Override
    public void onDestroy() { //销毁
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        L.d(TAG, "__onDestroy___");
    }

    private Timer timer = new Timer();
    private boolean isTimerStart = false;


    /**
     * 设置返回
     */
    public void onEventMainThread(BasketScoreSettingEventBusEntity currentFragmentId) {
        updateAdapter();
        L.d("设置返回 ", "000000");
        /**
         * 过滤筛选0场的情况，防止筛选0场时刷新 切换设置时出现异常
         */
        if (mChickedFilter.size() != 0) {
            if (currentMatchData != null) {
                if (currentMatchData.size() != 0) {
                    updateAdapter();
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mbasket_unfiltrate.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mLoadingLayout.setVisibility(View.GONE);
                }
            } else {
                mbasket_unfiltrate.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
                mLoadingLayout.setVisibility(View.GONE);
            }
        } else {
            mbasket_unfiltrate.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mLoadingLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 筛选返回
     */
    public void onEventMainThread(BasketScoreImmedEventBusEntity basketScoreImmedEventBusEntity) {
        Map<String, Object> map = basketScoreImmedEventBusEntity.getMap();
        String[] checkedIds = (String[]) ((List) map.get(BasketFiltrateActivity.CHECKED_CUPS_IDS)).toArray(new String[]{});
        isFilter = true;
        FiltrateCupsMap.basketImmedateCups = checkedIds;
        if (checkedIds.length == 0) {
            List<BasketMatchFilter> noCheckedFilters = new ArrayList<>();
            mChickedFilter = noCheckedFilters;//筛选0场后，再次进入赛选页面 显示已选中0场（全部不选中）

            mbasket_unfiltrate.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mLoadingLayout.setVisibility(View.GONE);
        } else {
            currentMatchData.clear();
            groupDataList.clear();
            List<BasketMatchBean> checkedMatchs = new ArrayList<>();
            List<String> localFilterRace = new ArrayList<>();
            for (BasketMatchBean matchBean : mAllMatchdata.get(currentDatePosition)) {//只遍历当前日期里的比赛
                boolean isExistId = false;
                for (String checkedId : checkedIds) {
                    if (matchBean.getLeagueId().equals(checkedId)) {
                        isExistId = true;
                        break;
                    }
                }
                if (isExistId) {
                    checkedMatchs.add(matchBean);
                }
            }
            localFilterRace.addAll((List)map.get(BasketFiltrateActivity.CHECKED_CUPS_IDS));

            PreferenceUtil.setDataList(FootBallMatchFilterTypeEnum.BASKET_IMMEDIA, localFilterRace);
            if (checkedMatchs.size() != 0) {
                BasketMatchBean itemData = new BasketMatchBean();
                itemData.setItemType(TITTLEDATETYPE);
                itemData.setDate(DateUtil.convertDateToNation(mAllMatchdata.get(currentDatePosition).get(0).getDate()));
                itemData.setThirdId("*");//这里id需要赋值，否则推送更新的时候会 NullPointerException
                currentMatchData.add(itemData);
                for (BasketMatchBean data : checkedMatchs) {
                    data.setItemType(LISTDATATYPE);
                    currentMatchData.add(data);
                }
            }
            List<BasketMatchFilter> checkedFilters = new ArrayList<>();
            for (BasketMatchFilter allFilter : mAllFilter) {
                for (String checkedId : checkedIds) {
                    if (allFilter.getLeagueId().equals(checkedId)) {
                        checkedFilters.add(allFilter);
                    }
                }
            }
            mChickedFilter = checkedFilters;
            mbasket_unfiltrate.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            updateAdapter();
        }
    }

    /**
     * 详情页面返回
     *
     * @param id
     */
    public void onEventMainThread(BasketDetailsEventBusEntity id) {
        updateAdapter();
        if (mEntryType == 0) {
        } else if (mEntryType == 1) {
            ((BasketBallScoreFragment) getParentFragment()).focusCallback();
        }
    }
}
