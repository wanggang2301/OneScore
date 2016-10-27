package com.hhly.mlottery.frame.footframe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FiltrateMatchConfigActivity;
import com.hhly.mlottery.activity.FootballMatchDetailActivityTest;
import com.hhly.mlottery.adapter.ResultMultiAdapter;
import com.hhly.mlottery.adapter.ResultMultiInternationAdapter;
import com.hhly.mlottery.adapter.ScheduleDateAdapter;
import com.hhly.mlottery.bean.LeagueCup;
import com.hhly.mlottery.bean.Match;
import com.hhly.mlottery.bean.resultbean.ResultDateMatchs;
import com.hhly.mlottery.bean.resultbean.ResultMatch;
import com.hhly.mlottery.bean.resultbean.ResultMatchDto;
import com.hhly.mlottery.bean.scheduleBean.ScheduleDate;
import com.hhly.mlottery.callback.DateOnClickListener;
import com.hhly.mlottery.callback.FocusClickListener;
import com.hhly.mlottery.callback.FocusMatchClickListener;
import com.hhly.mlottery.callback.RecyclerViewItemClickListener;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.ScoresFragment;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ResultDateUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * @author yixq
 * @ClassName: ResultFragment
 * @Description: 赛果
 * @date 2015-10-15 上午9:57:42
 */
public class ResultFragment extends Fragment implements OnClickListener, OnRefreshListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //private SlideListView mListView;

    private RecyclerView mRecyclerView;

    private View mView;
    private Context mContext;

    private LinearLayout mLoadingLayout;
    private LinearLayout mErrorLayout;
    private TextView mReloadTvBtn;// 刷新 控件

    private ArrayList<ResultMatchDto> mAllMatchs;// 所有的
    private ArrayList<ResultMatchDto> mMatchs;// 显示的

    private boolean isGJB = AppConstants.isGOKeyboard;//判断是否是国际版


    public static List<LeagueCup> mCups;
    public static LeagueCup[] mCheckedCups; // 联赛筛选


    private List<ScheduleDate> mDateList; // 日期

    private ExactSwipeRefrashLayout mSwipeRefreshLayout;// 下拉刷新 layout

    private FocusClickListener mFocusClickListener;// 关注点击事件

    private FocusMatchClickListener mFocusMatchClickListener;

    private static final String TAG = "ResultFragment";

    private ResultMultiAdapter mAdapter; // 普通版 adapter
    private ResultMultiInternationAdapter mInternationAdapter;// 国际版 adapter

    private boolean isLoadedData = false;
    public static boolean isCheckedDefualt = true;// 默认全不选-- 筛选界面

    private RelativeLayout mNoDataLayout;// 筛选0场时 提示图

    private Intent mIntent;

    public static final int REQUEST_FILTER_CODE = 0x21;
    public static final int REQUEST_SETTING_CODE = 0x22;
    public static final int REQUEST_DETAIL_CODE = 0x23;

    // 记录 dialog弹窗 点击的位置
    private int mItems = -1;

    private DateOnClickListener mDateOnClickListener;


    private DateOnClickListener mDateOnClickListener_internation;

    private String mCurrentDate;

    public static final int LOAD_DATA_STATUS_INIT = 0;
    public static final int LOAD_DATA_STATUS_LOADING = 1;
    public static final int LOAD_DATA_STATUS_SUCCESS = 2;
    public static final int LOAD_DATA_STATUS_ERROR = 3;

    public static int mLoadDataStatus = LOAD_DATA_STATUS_INIT;// 加载数据状态
    public static boolean isNetSuccess = true;// 告诉筛选页面数据是否加载成功

    private final static int VIEW_STATUS_LOADING = 1;
    private final static int VIEW_STATUS_SUCCESS = 3;
    private final static int VIEW_STATUS_NET_ERROR = 4;
    private final static int VIEW_STATUS_FLITER_NO_DATA = 5;

    public static EventBus resultEventBus;
    private static final String FRAGMENT_INDEX = "fragment_index";

    private static int currentDatePosition = 0;

    private int mCurIndex = -1;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    private LinearLayoutManager layoutManager;

    private String teamLogoPre;

    private String teamLogoSuff;


    public static ResultFragment newInstance(String param1, String param2) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ResultFragment newInstance(int index) {


        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_INDEX, index);
        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    private Handler mViewHandler = new Handler() {
        public void handleMessage(Message msg) {
            isNetSuccess = true;
            switch (msg.what) {
                case VIEW_STATUS_LOADING:
                    mErrorLayout.setVisibility(View.GONE);
                    //mListView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    mNoDataLayout.setVisibility(View.GONE);
                    if (!isLoadedData) {
                        mLoadingLayout.setVisibility(View.VISIBLE);
                    }
                    mErrorLayout.setVisibility(View.GONE);
                    mLoadDataStatus = LOAD_DATA_STATUS_LOADING;
                    break;
                case VIEW_STATUS_SUCCESS:
                    //mScreen.setVisibility(View.VISIBLE);
                    mLoadingLayout.setVisibility(View.GONE);
                    mErrorLayout.setVisibility(View.GONE);
                    mNoDataLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    //mListView.setVisibility(View.VISIBLE);
                    mLoadDataStatus = LOAD_DATA_STATUS_SUCCESS;

                    break;
                case VIEW_STATUS_NET_ERROR:
                    if (isLoadedData) {
                        if (mContext != null) {
                            Toast.makeText(mContext, R.string.exp_net_status_txt, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mLoadingLayout.setVisibility(View.GONE);
                        //mSwipeRefreshLayout.setVisibility(View.GONE);
                        mErrorLayout.setVisibility(View.VISIBLE);
                        mNoDataLayout.setVisibility(View.GONE);
                        mLoadDataStatus = LOAD_DATA_STATUS_ERROR;
                    }
                    mSwipeRefreshLayout.setRefreshing(false);

                    isNetSuccess = false;
                    break;
                case VIEW_STATUS_FLITER_NO_DATA:
                    mLoadingLayout.setVisibility(View.GONE);
                    mErrorLayout.setVisibility(View.GONE);
                    //mListView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    mLoadDataStatus = LOAD_DATA_STATUS_SUCCESS;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**注册事件总线*/
        resultEventBus = new EventBus();
        resultEventBus.register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*if (mView == null) {
            mView = inflater.inflate(R.layout.football_result, container, false);
            Bundle bundle = getArguments();
            if (bundle != null) {
                mCurIndex = bundle.getInt(FRAGMENT_INDEX);
            }
            isPrepared = true;
            initView();

            lazyLoad();
        }

        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
*/
        mContext = getActivity();

        mView = inflater.inflate(R.layout.football_result, container, false);

        initView();

        return mView;
    }

    private Runnable mLoadingDataThread = new Runnable() {
        @Override
        public void run() {
            // 在这里数据内容加载到Fragment上
            initData();
        }
    };


   /* protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        new Handler().postDelayed(mLoadingDataThread, 0);

    }*/

    public void initView() {
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview_result);
        // 加载状态图标
        mLoadingLayout = (LinearLayout) mView.findViewById(R.id.football_result_loading_ll);
        mErrorLayout = (LinearLayout) mView.findViewById(R.id.network_exception_layout);
        mReloadTvBtn = (TextView) mView.findViewById(R.id.network_exception_reload_btn);
        mReloadTvBtn.setOnClickListener(this);

        // 筛选0场 显示图标
        mNoDataLayout = (RelativeLayout) mView.findViewById(R.id.football_result_unfocus_ll);

        // 下拉刷新
        mSwipeRefreshLayout = (ExactSwipeRefrashLayout) mView.findViewById(R.id.football_result_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));

        //mListView.setSwipeRefreshLayout(mSwipeRefreshLayout);

        mRecyclerView.setLayoutManager(layoutManager);


        // 实现 监听 （实例化） 关注监听
        mFocusMatchClickListener = new FocusMatchClickListener() {// 关注按钮事件
            @Override
            public void onClick(View view, String third) {

                boolean isCheck = (Boolean) view.getTag();// 检查之前是否被选中

                if (!isCheck) {// 插入数据
                    Log.e("AAA", "有调用啊");
                    FocusFragment.addFocusId(third);

                    ((ImageView) view).setImageResource(R.mipmap.football_focus);
                    view.setTag(true);
                } else {// 删除
                    FocusFragment.deleteFocusId(third);

                    ((ImageView) view).setImageResource(R.mipmap.football_nomal);

                    view.setTag(false);
                }
                ((ScoresFragment) getParentFragment()).focusCallback();
            }
        };


        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        new Handler().postDelayed(mLoadingDataThread, 0);

        /**
         * 国际版判断
         */
        //初始化 dialog

      /*  if (isGJB) { //否则  显示国际版 界面
            setDialog_internation();
        } else {*/    //如果是中文 或繁体--  则用中文ui显示
        setDialog();
        //}

    }

    /**
     * 初始化 设置 dialog 弹窗
     */
    public void setDialog() {

        mDateOnClickListener = new DateOnClickListener() {
            @Override
            public void onClick(View v) {
                // Dialog 设置
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
                LayoutInflater infla = LayoutInflater.from(mContext);
                View view = infla.inflate(R.layout.alertdialog, null);
                TextView titleView = (TextView) view.findViewById(R.id.titleView);
                titleView.setText(R.string.tip);
                ListView listview = (ListView) view.findViewById(R.id.listdate);

                ScheduleDateAdapter dateAdapter = ResultDateUtil.initListDateAndWeek(getActivity(), mDateList, mCurrentDate, currentDatePosition);

                listview.setAdapter(dateAdapter);
                final AlertDialog mAlertDialog = mBuilder.create();
                listview.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
                        //重新选择日期后  默认 未做过筛选
//						mType = 0;
                        isCheckedDefualt = true;
                        // 加载 历史数据
                        historyInitData(position);
                        // 关闭 dialog弹窗
                        mAlertDialog.dismiss();
                        // 记录点击的 item_share 位置
                        mItems = position;
                    }
                });
                mAlertDialog.show();
                mAlertDialog.getWindow().setContentView(view);
            }
        };
    }

    public void setDialog_internation() {
        mDateOnClickListener_internation = new DateOnClickListener() {
            @Override
            public void onClick(View v) {
                // Dialog 设置
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
                LayoutInflater infla = LayoutInflater.from(mContext);
                View view = infla.inflate(R.layout.alertdialog, null);
                TextView titleView = (TextView) view.findViewById(R.id.titleView);
                titleView.setText(R.string.tip);
                ListView listview = (ListView) view.findViewById(R.id.listdate);
                ScheduleDateAdapter dateAdapter = ResultDateUtil.initListDateAndWeek(getActivity(), mDateList, mCurrentDate, currentDatePosition);
                listview.setAdapter(dateAdapter);
                final AlertDialog mAlertDialog = mBuilder.create();
                listview.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
                        //重新选择日期后  默认 未做过筛选
//						mType = 0;
                        isCheckedDefualt = true;
                        // 加载 历史数据
                        historyInitData(position);
                        // 关闭 dialog弹窗
                        mAlertDialog.dismiss();
                        // 记录点击的 item_share 位置
                        mItems = position;
                        // System.out.println("position=========="+position);
                    }
                });
                mAlertDialog.show();
                mAlertDialog.getWindow().setContentView(view);


            }
        };

    }

    public void updateAdapter() {

        /*if (isGJB) { //国际版样式
            if (mInternationAdapter != null) {
                mInternationAdapter.updateDatas(mMatchs); //默认 显示 热门
                mInternationAdapter.notifyDataSetChanged();
            }
        } else {// 中文版样式*/
        if (mAdapter != null) {
            mAdapter.updateDatas(mMatchs); //默认 显示 热门
            mAdapter.notifyDataSetChanged();
        }
        //}
    }

    // 加载数据
    public void initData() {

        if (getActivity() == null) {
            return;
        }

        if (getParentFragment() != null) {
            ((ScoresFragment) getParentFragment()).getFootballUserConcern();
        }

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_ResultMatchs, new VolleyContentFast.ResponseSuccessListener<ResultMatch>() {
            @Override
            public synchronized void onResponse(final ResultMatch json) {
                if (json == null) {
                    mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
                    return;
                }

                if (getActivity() == null) {
                    return;
                }
                // 创建对象
                final ResultDateMatchs current = json.getCurrent();

                final ResultDateMatchs previous = json.getPrevious();

                if (current == null && previous == null) {
                    return;
                }

                mCups = json.getFinishFilter();
                // 集合 存 解析的所有数据集
                mAllMatchs = new ArrayList<ResultMatchDto>();// 所以赛事

                mMatchs = new ArrayList<ResultMatchDto>();

                //当前日期
                mCurrentDate = json.getCurrent().getDate();
                currentDatePosition = 0;    //当前日期


                teamLogoPre = json.getTeamLogoPre();
                teamLogoSuff = json.getTeamLogoSuff();


                /**
                 * 标记 Type 1--只加载 current下的 Date数据
                 * 2--只加载current下的match字段数据
                 * 3--只加载 previous下
                 * Date数据 4--只加载previous下match数据
                 */
                if (current != null) {
                    // current 数据
                    ResultMatchDto resultMatchDtoDate1 = new ResultMatchDto();

                    resultMatchDtoDate1.setDate(current.getDate());
                    resultMatchDtoDate1.setType(ResultMultiAdapter.VIEW_DATE_INDEX);

                    mAllMatchs.add(resultMatchDtoDate1);

                    for (Match match : current.getMatch()) {
                        ResultMatchDto resultMatchDtoMatch = new ResultMatchDto();

                        resultMatchDtoMatch.setType(ResultMultiAdapter.VIEW_MATCH_INDEX);

                        resultMatchDtoMatch.setMatchs(match);

                        mAllMatchs.add(resultMatchDtoMatch);
                    }
                }

                if (previous != null) {
                    // previous 数据
                    ResultMatchDto resultMatchDtoDate2 = new ResultMatchDto();
                    resultMatchDtoDate2.setDate(previous.getDate());
                    resultMatchDtoDate2.setType(ResultMultiAdapter.VIEW_DATE_INDEX);
                    mAllMatchs.add(resultMatchDtoDate2);

                    for (Match match2 : previous.getMatch()) {
                        ResultMatchDto resultMatchDtoMatch2 = new ResultMatchDto();
                        resultMatchDtoMatch2.setType(ResultMultiAdapter.VIEW_MATCH_INDEX);
                        resultMatchDtoMatch2.setMatchs(match2);
                        mAllMatchs.add(resultMatchDtoMatch2);
                    }
                }

                L.d("1234", isCheckedDefualt + "");

                if (!isCheckedDefualt) {

                    mMatchs.clear();
                    for (ResultMatchDto match : mAllMatchs) {
                        if (match.getType() == ResultMultiAdapter.VIEW_DATE_INDEX) {
                            mMatchs.add(match);
                            continue;
                        }

                        for (LeagueCup cup : mCheckedCups) {
                            if ((match.getType() == ResultMultiAdapter.VIEW_MATCH_INDEX) && cup.getRaceId().equals(match.getMatchs().getRaceId())) {
                                mMatchs.add(match);
                                break;
                            }
                        }
                    }
                } else {
                  /*  *
                     * 默认显示 全部赛事*/

                    mMatchs.addAll(mAllMatchs);
                    mCheckedCups = mCups.toArray(new LeagueCup[mCups.size()]);
                }

                /**
                 * 国际版判断
                 */
                /*if (isGJB) { //国际版 样式
                    mInternationAdapter = new ResultMultiInternationAdapter(getActivity(), mMatchs,new ResultMultiInternationAdapter.ResultMultiItemTypeSupport());
                    mInternationAdapter.setItemPaddingRight(mListView.getItemPaddingRight());
                    mInternationAdapter.setFocusClickListener(mFocusClickListener);
                    mInternationAdapter.setDateOnClickListener(mDateOnClickListener_internation);
                    mListView.setAdapter(mInternationAdapter);

                } else { //中文版样式*/
                   /* ResultMultiAdapter.ResultMultiItemTypeSupport resultMultiItemTypeSupport = new ResultMultiAdapter.ResultMultiItemTypeSupport();
                    mAdapter = new ResultMultiAdapter(getActivity(), mMatchs, resultMultiItemTypeSupport);
                    mAdapter.setItemPaddingRight(mListView.getItemPaddingRight());
                    mAdapter.setFocusClickListener(mFocusClickListener);
                    mAdapter.setDateOnClickListener(mDateOnClickListener);
                    mListView.setAdapter(mAdapter);*/

                // }


                mAdapter = new ResultMultiAdapter(mContext, mMatchs, teamLogoPre, teamLogoSuff);
                //  mAdapter.setItemPaddingRight(mListView.getItemPaddingRight());
                //  mAdapter.setSchfocusClickListener(mSchfocusClickListener);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setmFocusMatchClickListener(mFocusMatchClickListener);
                mAdapter.setDateOnClickListener(mDateOnClickListener);


                mAdapter.setmOnItemClickListener(new RecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, String data) {
                        String thirdId = data;
                        Intent intent = new Intent(getActivity(), FootballMatchDetailActivityTest.class);
                        intent.putExtra("thirdId", thirdId);
                        intent.putExtra("currentFragmentId", 1);
                        getParentFragment().startActivityForResult(intent, REQUEST_DETAIL_CODE);
                    }
                });


                mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
                isLoadedData = true;
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
            }
        }, ResultMatch.class);

//

    }

    // 定义关注监听
    public interface ResultFocusClickListener {
        void FocusonClick(View view, ResultMatchDto resultMatchDto);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.network_exception_reload_btn:
                mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
                new Handler().postDelayed(mLoadingDataThread, 0);
                break;
            default:
                break;
        }
    }


    /**
     * EventBus 帅选接受消息
     * 接受消息的页面实现
     */
    public void onEventMainThread(Map<String, Object> map) {
        String[] checkedIds = (String[]) ((LinkedList) map.get(FiltrateMatchConfigActivity.RESULT_CHECKED_CUPS_IDS)).toArray(new String[]{});
        mMatchs.clear();
        if (checkedIds.length != 0) {

            for (ResultMatchDto match : mAllMatchs) {
                if (match.getType() == ResultMultiAdapter.VIEW_DATE_INDEX) {
                    mMatchs.add(match);
                    continue;
                }

                for (String checkedId : checkedIds) {

                    if ((match.getType() == ResultMultiAdapter.VIEW_MATCH_INDEX) && checkedId.equals(match.getMatchs().getRaceId())) {

                        mMatchs.add(match);
                        break;
                    }
                }

            }

            List<LeagueCup> leagueCupList = new ArrayList<LeagueCup>();
            for (LeagueCup cup : mCups) {
                boolean isExistId = false;
                for (String checkedId : checkedIds) {
                    if (checkedId.equals(cup.getRaceId())) {
                        isExistId = true;
                        break;
                    }
                }

                if (isExistId) {
                    leagueCupList.add(cup);
                }
            }

            mCheckedCups = leagueCupList.toArray(new LeagueCup[]{});
            updateAdapter();
            mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
        } else {
            for (ResultMatchDto match : mAllMatchs) {
                if (match.getType() == ResultMultiAdapter.VIEW_DATE_INDEX) {
                    mMatchs.add(match);
                    continue;
                }
            }

            mCheckedCups = new LeagueCup[]{};//选择0场  把选中联赛为 空集
            mViewHandler.sendEmptyMessage(VIEW_STATUS_FLITER_NO_DATA);
            updateAdapter();
        }

        isCheckedDefualt = (boolean) map.get(FiltrateMatchConfigActivity.CHECKED_DEFUALT);
    }


    /**
     * EventBus 设置
     * 接受消息的页面实现
     */
    public void onEventMainThread(Integer currentFragmentId) {
        updateAdapter();
    }


    /**
     * EventBus 赛场比赛详情返回FootballMatchDetailActivity
     * 接受消息的页面实现
     */
    public void onEventMainThread(String currentFragmentId) {
        updateAdapter();
        ((ScoresFragment) getParentFragment()).focusCallback();
    }

    /**
     * 加载 历史数据
     *
     * @param position ：传入所选日期的 Item
     */
    public void historyInitData(final int position) {

        // 获得 倒叙日期
        final String mDatas[] = new String[7];

        // 获得 递减 日期
        for (int i = 0; i < 7; i++) {

            String riqi = ResultDateUtil.getDate(i, mCurrentDate);
            String week = ResultDateUtil.getWeekOfDate(DateUtil.parseDate(ResultDateUtil.getDate(0, riqi)));

            mDatas[i] = riqi + "   " + week;
        }

        final String mSub = mDatas[position].substring(0, 10); // 获得选中的 日期

        Map<String, String> mHashmap = new HashMap<String, String>();
        mHashmap.put("date", mSub);

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_ResultMatchs, mHashmap, new VolleyContentFast.ResponseSuccessListener<ResultMatch>() {
            @Override
            public synchronized void onResponse(final ResultMatch json) {
                if (json == null) {
                    return;
                }
                final List<Match> schedulelist = json.getCurrent().getMatch();

                mAllMatchs = new ArrayList<ResultMatchDto>();// 全部
                mMatchs = new ArrayList<ResultMatchDto>();// 热门

                ResultMatchDto resultMatchDtoDate1 = new ResultMatchDto();
                resultMatchDtoDate1.setDate(mSub);
                resultMatchDtoDate1.setType(ResultMultiAdapter.VIEW_DATE_INDEX);
                mAllMatchs.add(resultMatchDtoDate1);

                for (Match mat : schedulelist) {
                    ResultMatchDto resultMatchDtoMatch = new ResultMatchDto();
                    resultMatchDtoMatch.setType(ResultMultiAdapter.VIEW_MATCH_INDEX);
                    resultMatchDtoMatch.setMatchs(mat);
                    mAllMatchs.add(resultMatchDtoMatch);
                }

                mCups = json.getFinishFilter();

                if (!isCheckedDefualt) {
                    mMatchs.clear();
                    for (ResultMatchDto match : mAllMatchs) {
                        if (match.getType() == ResultMultiAdapter.VIEW_DATE_INDEX) {
                            mMatchs.add(match);
                            continue;
                        }

                        for (LeagueCup cup : mCheckedCups) {
                            if ((match.getType() == ResultMultiAdapter.VIEW_MATCH_INDEX) && cup.getRaceId().equals(match.getMatchs().getRaceId())) {
                                mMatchs.add(match);
                                break;
                            }
                        }
                    }
                } else {
                    /**
                     * 默认显示 全部赛事
                     */
                    mMatchs.addAll(mAllMatchs);
                    mCheckedCups = mCups.toArray(new LeagueCup[mCups.size()]);
                }

                if (mMatchs.size() == 1) {// size == 1
                    // 表示除所选日期之外没有其他数据
                    mViewHandler.sendEmptyMessage(VIEW_STATUS_FLITER_NO_DATA);

                } else {
                    mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
                }
                updateAdapter();

                currentDatePosition = position;
                // mListView.setSelection(0);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
            }
        }, ResultMatch.class);
    }


    @Override
    public void onResume() {
        super.onResume();
        //updateAdapter();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        updateAdapter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isCheckedDefualt = true;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mItems == -1) {//无dialog选择 --加载当前页
                    initData();
                } else {
                    historyInitData(mItems);//dialog  日期选择  -- 加载 历史数据  mItems：所选日期
                }
            }
        }, 500);
    }
}
