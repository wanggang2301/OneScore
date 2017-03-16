package com.hhly.mlottery.frame.basketballframe;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.activity.BasketFiltrateActivity;
import com.hhly.mlottery.activity.BasketballScoresActivity;
import com.hhly.mlottery.activity.BasketballSettingActivity;
import com.hhly.mlottery.adapter.basketball.PinnedHeaderExpandableAdapter;
import com.hhly.mlottery.bean.basket.BasketAllOddBean;
import com.hhly.mlottery.bean.basket.BasketMatchBean;
import com.hhly.mlottery.bean.basket.BasketMatchFilter;
import com.hhly.mlottery.bean.basket.BasketOddBean;
import com.hhly.mlottery.bean.basket.BasketRoot;
import com.hhly.mlottery.bean.basket.BasketRootBean;
import com.hhly.mlottery.bean.basket.BasketScoreBean;
import com.hhly.mlottery.bean.websocket.WebBasketAllOdds;
import com.hhly.mlottery.bean.websocket.WebBasketMatch;
import com.hhly.mlottery.bean.websocket.WebBasketOdds;
import com.hhly.mlottery.bean.websocket.WebBasketOdds5;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.scorefrag.BasketBallScoreFragment;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.FiltrateCupsMap;
import com.hhly.mlottery.util.FocusUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ResultDateUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.PinnedHeaderExpandableListView;
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
 * Created by yixq on 2015/12/30.cfbfd f f
 */
public class ImmedBasketballFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, ExpandableListView.OnChildClickListener {

    private static final String TAG = "ImmedBasketballFragment";
    private static final String PARAMS = "BASKET_PARAMS";
    private PinnedHeaderExpandableListView explistview;

    //筛选的数据
    public static List<BasketMatchFilter> mChickedFilter = new ArrayList<>();//选中的
    public static List<BasketMatchFilter> mAllFilter = new ArrayList<>();//所有的联赛
    //内容数据
    private List<List<BasketMatchBean>> mAllMatchdata;//所有的比赛list
    private List<String> mAllGroupdata;//所有日期list
    private List<List<BasketMatchBean>> childrenDataList;//显示的比赛list
    private List<String> groupDataList;//显示日期list
    private List<BasketRootBean> mMatchdata = new ArrayList<>();//match的 内容(json)

    private int expandFlag = -1;//控制列表的展开
    private PinnedHeaderExpandableAdapter adapter;

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

    public static int getIsLoad() {
        return isLoad;
    }

    public static final int TYPE_FOCUS = 3;
    /**
     * 关注事件EventBus
     */
    public static EventBus BasketImmedEventBus;
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
    public static ImmedBasketballFragment newInstance(int basketballType, boolean isNewFramWork, int basketEntryType) {
        ImmedBasketballFragment fragment = new ImmedBasketballFragment();
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
        BasketImmedEventBus = new EventBus();
        BasketImmedEventBus.register(this);

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
        }
        return versioncode;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.pinned_listview, container, false);

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
     * 初始化VIEW
     */
    private void initView() {
        //暂无关注
        mbasketball_unfocus = (RelativeLayout) mView.findViewById(R.id.basketball_immediate_unfocus_pinned);

        //暂无筛选
        mbasket_unfiltrate = (RelativeLayout) mView.findViewById(R.id.basket_unfiltrate);

        explistview = (PinnedHeaderExpandableListView) mView.findViewById(R.id.explistview);
        explistview.setChildDivider(MyApp.getContext().getResources().getDrawable(R.color.linecolor)); //设置分割线 适配魅族

        explistview.setOnChildClickListener(this);

        //设置悬浮头部VIEW
        explistview.setHeaderView(getActivity().getLayoutInflater().inflate(R.layout.basketball_riqi_head, explistview, false));//悬浮头

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
//        mFilterImgBtn.setClickable(false); // 默认设置不可点击，防止网络差时在数据请求过程中无数据时点击筛选出现空白显示情况
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

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_BASKET_IMMEDIATE, params, new VolleyContentFast.ResponseSuccessListener<BasketRoot>() {
            @Override
            public void onResponse(BasketRoot json) {

                if (getActivity() == null) {
                    return;
                }

                isLoad = 1;
                if (json == null || json.getMatchData() == null || json.getMatchData().size() == 0) {
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mLoadingLayout.setVisibility(View.GONE);
                    mErrorLayout.setVisibility(View.GONE);
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    return;
                }

                mMatchdata = json.getMatchData();

                mAllFilter = json.getMatchFilter();

                mSize = json.getMatchData().size();

                groupDataList = new ArrayList<String>();
                childrenDataList = new ArrayList<List<BasketMatchBean>>();
                mAllGroupdata = new ArrayList<>();
                mAllMatchdata = new ArrayList<>();
                for (int i = 0; i < mSize; i++) {
                    /**
                     *  获得星期
                     */
                    String week = ResultDateUtil.getWeekOfDate(DateUtil.parseDate(ResultDateUtil.getDate(0, json.getMatchData().get(i).getDate())));
                    /**
                     * 子view数据
                     */
                    mAllMatchdata.add(mMatchdata.get(i).getMatch());//显示的
                    /**
                     * 外层 view数据
                     */
                    mAllGroupdata.add(DateUtil.convertDateToNation(mMatchdata.get(i).getDate()) + "," + week + "," + mMatchdata.get(i).getDiffDays() + "");
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
                        for (List<BasketMatchBean> lists : mAllMatchdata) { // 遍历所有数据 得到筛选后的
                            List<BasketMatchBean> checkedMatchs = new ArrayList<>();
                            for (BasketMatchBean matchBean : lists) {
                                for (String checkedId : FiltrateCupsMap.basketImmedateCups) {
                                    if (matchBean.getLeagueId().equals(checkedId)) {
                                        checkedMatchs.add(matchBean);
                                    }
                                }
                            }
                            if (checkedMatchs.size() != 0) {
                                childrenDataList.add(checkedMatchs); //筛选后的比赛list
                                for (String groupdata : mAllGroupdata) {
                                    String[] weekdatas = groupdata.split(",");
                                    String datas = weekdatas[0];
                                    if (checkedMatchs.get(0).getDate().equals(datas)) {
                                        groupDataList.add(groupdata);//赛选后的日期list
                                        break;
                                    }
                                }
                            }
                        }
                    }

                } else { //未筛选
                    for (List<BasketMatchBean> lists : mAllMatchdata) {
                        childrenDataList.add(lists);
                    }
                    for (String groupdata : mAllGroupdata) {
                        groupDataList.add(groupdata);
                    }
                    mChickedFilter = mAllFilter;//默认选中全部
                }

                if (adapter == null) {
                    adapter = new PinnedHeaderExpandableAdapter(childrenDataList, groupDataList, getActivity(), explistview);
                    explistview.setAdapter(adapter);
                    adapter.setmFocus(mFocusClickListener);//设置关注
                } else {
                    updateAdapter();
                }
                /**
                 * 判断 如果是即时和关注界面 则开启socket服务
                 */
//                if (mBasketballType == TYPE_IMMEDIATE || mBasketballType == TYPE_FOCUS) {
//                    startWebsocket(); //启动socket
//                }
                //全部展开
                for (int i = 0; i < groupDataList.size(); i++) {
                    explistview.expandGroup(i);
                    /**
                     * 设置group的默认打开状态，解决默认全部打开后点击group第一次无效问题（点击两次才收起）
                     */
                    if (adapter != null) {
                        adapter.setGroupClickStatus(i, 1);//收起 ： 0  展开 ：1
                    }
                }

                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mLoadingLayout.setVisibility(View.GONE);
                mErrorLayout.setVisibility(View.GONE);

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            /**
             * 加载失败
             *
             * @param exception
             */
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                L.e(TAG, "exception.getErrorCode() = " + exception.getErrorCode());

                isLoad = 0;

                mSwipeRefreshLayout.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                mLoadingLayout.setVisibility(View.GONE);
                mErrorLayout.setVisibility(View.VISIBLE);
            }
        }, BasketRoot.class);
        fucusChicked();//点击关注监听
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
                            ((BasketballScoresActivity) getActivity()).basketFocusCallback();
                        } else if (mEntryType == 1) {
                            ((BasketBallScoreFragment) getParentFragment()).focusCallback();
                        }
                    }
                }
                updateAdapter();//防止复用
                if (mEntryType == 0) {
                    ((BasketballScoresActivity) getActivity()).basketFocusCallback();
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
        mLoadHandler.postDelayed(mRun, 0);
        if (isNewFrameWork) {
            ((BasketBallScoreFragment) getParentFragment()).reconnectWebSocket();
        } else {
            ((BasketballScoresActivity) getActivity()).reconnectWebSocket();
        }
    }

    /**
     * list点击事件
     */
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Intent intent = new Intent(getActivity(), BasketDetailsActivityTest.class);
        intent.putExtra(BasketDetailsActivityTest.BASKET_THIRD_ID, childrenDataList.get(groupPosition).get(childPosition).getThirdId());//跳转到详情
        intent.putExtra(BasketDetailsActivityTest.BASKET_MATCH_STATUS, childrenDataList.get(groupPosition).get(childPosition).getMatchStatus());//跳转到详情
        //用getActivity().startActivityForResult();不走onActivityResult ;
//        startActivityForResult(intent, REQUEST_DETAILSCODE);
        intent.putExtra("currentfragment", mBasketballType);
        intent.putExtra(BasketDetailsActivityTest.BASKET_MATCH_LEAGUEID, childrenDataList.get(groupPosition).get(childPosition).getLeagueId());
        intent.putExtra(BasketDetailsActivityTest.BASKET_MATCH_MATCHTYPE, childrenDataList.get(groupPosition).get(childPosition).getMatchType());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
        MobclickAgent.onEvent(mContext, "Basketball_ListItem");
        return false;
    }

    // 定义关注监听
    public interface BasketFocusClickListener {
        void FocusOnClick(View view, BasketMatchBean root);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.public_btn_set:  //设置
                MobclickAgent.onEvent(mContext, "Basketball_Setting");
//                String s = getAppVersionCode(mContext);
                mIntent = new Intent(getActivity(), BasketballSettingActivity.class);
//                getParentFragment().startActivityForResult(mIntent, REQUEST_SETTINGCODE);
//                getActivity().startActivityForResult(mIntent , REQUEST_SETTINGCODE);
                Bundle bundleset = new Bundle();
                bundleset.putInt("currentfragment", mBasketballType);
                mIntent.putExtras(bundleset);
                startActivity(mIntent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                break;

            case R.id.public_img_back:  //返回
                MobclickAgent.onEvent(mContext, "Basketball_Exit");
//                ((MainActivity) getActivity()).openLeftLayout();
                getActivity().finish();
                break;

            case R.id.basketball_immediate_error_btn:

                isLoad = -1;
                MobclickAgent.onEvent(mContext, "Basketball_Refresh");
                mLoadingLayout.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(true);
                mSwipeRefreshLayout.setVisibility(View.GONE);
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

        adapter.updateDatas(childrenDataList, groupDataList);
        adapter.notifyDataSetChanged();
        //设置打开全部日期内容
//        for (int i = 0; i < groupDataList.size(); i++) {
//            explistview.expandGroup(i);
//        }
    }

    public void LoadData() {
        mSwipeRefreshLayout.setRefreshing(true);
        mLoadHandler.post(mRun);
    }

    public void handleSocketMessage(String text) {
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
                updateListViewItemOdd(mWebBasketOdds);  //TODO
            }


        }
    };


    private void updateListViewItemStatus(WebBasketMatch webBasketMatch) {
        Map<String, String> data = webBasketMatch.getData();
        synchronized (childrenDataList) {
            for (List<BasketMatchBean> match : childrenDataList) {
                for (BasketMatchBean matchchildern : match) {
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


    }

    private void updateListViewItemOdd(WebBasketOdds webBasketOdds) {
        // Match targetMatch = null;
        WebBasketAllOdds data = webBasketOdds.getData();
        synchronized (childrenDataList) {
            for (List<BasketMatchBean> match : childrenDataList) {// all里面的match
                for (BasketMatchBean matchchildern : match) {
                    if (matchchildern.getThirdId().equals(webBasketOdds.getThirdId())) {
                        updateMatchOdd(matchchildern, data);
                        updateAdapter();
                        break;
                    }
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

        //
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
        L.d(TAG, "__onDestroy___");
    }

    private Timer timer = new Timer();
    private boolean isTimerStart = false;


    /**
     * 设置返回
     */
    public void onEventMainThread(Integer currentFragmentId) {
        updateAdapter();
        L.d("设置返回 ", "000000");
        /**
         * 过滤筛选0场的情况，防止筛选0场时刷新 切换设置时出现异常
         */
        if (mChickedFilter.size() != 0) {
            if (childrenDataList != null) {
                if (childrenDataList.size() != 0) {
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
    public void onEventMainThread(Map<String, Object> map) {
        L.d("AAAAA-++++++--", "checkedIds.length");
        String[] checkedIds = (String[]) ((List) map.get(BasketFiltrateActivity.CHECKED_CUPS_IDS)).toArray(new String[]{});

        L.d("AAAAA------------------", checkedIds.length + "");
//        String[] checkedIds = (String[]) map.getCharSequenceArrayExtra(BasketFiltrateActivity.CHECKED_CUPS_IDS);// 返回数据是选择后的id字符串数组，数据类型String
        isFilter = true;

        FiltrateCupsMap.basketImmedateCups = checkedIds;
        if (checkedIds.length == 0) {
            List<BasketMatchFilter> noCheckedFilters = new ArrayList<>();
            mChickedFilter = noCheckedFilters;//筛选0场后，再次进入赛选页面 显示已选中0场（全部不选中）

            mbasket_unfiltrate.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mLoadingLayout.setVisibility(View.GONE);
        } else {

            L.d("123", "childrenDataList = " + childrenDataList.size());
            childrenDataList.clear();
            groupDataList.clear();
            for (List<BasketMatchBean> lists : mAllMatchdata) { // 遍历所有数据 得到筛选后的
                List<BasketMatchBean> checkedMatchs = new ArrayList<>();
                for (BasketMatchBean matchBean : lists) {
                    boolean isExistId = false;
                    for (String checkedId : checkedIds) {
                        if (matchBean.getLeagueId().equals(checkedId)) {
                            isExistId = true;
                            break;
                        }
                    }
                    if (isExistId) {
                        //groupDataList.add(aa.getDate()+","+"day");
                        //childrenDataList.add(lists);
                        checkedMatchs.add(matchBean);
                    }
                }
                if (checkedMatchs.size() != 0) {
                    childrenDataList.add(checkedMatchs);
                    for (String groupdata : mAllGroupdata) {
                        String[] weekdatas = groupdata.split(",");
                        String datas = weekdatas[0];
                        if (checkedMatchs.get(0).getDate().equals(datas)) {
                            groupDataList.add(groupdata);
                            break;
                        }
                    }
                }
            }
            List<BasketMatchFilter> checkedFilters = new ArrayList<>();
            // mChickedFilter.clear(); // 清除原来选中的
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

            L.d("123", "childrenDataList >>>> = " + childrenDataList.size());
            updateAdapter();
            // 设置打开全部日期内容
            for (int i = 0; i < groupDataList.size(); i++) {
                explistview.expandGroup(i);
            }
        }
    }

    /**
     * 详情页面返回
     *
     * @param id
     */
    public void onEventMainThread(String id) {
        updateAdapter();
        if (mEntryType == 0) {
            ((BasketballScoresActivity) getActivity()).basketFocusCallback();
        } else if (mEntryType == 1) {
            ((BasketBallScoreFragment) getParentFragment()).focusCallback();
        }
    }
}
