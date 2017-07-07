package com.hhly.mlottery.frame.footballframe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FiltrateMatchConfigActivity;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.ScheduleAdapter;
import com.hhly.mlottery.adapter.ScheduleDateAdapter;
import com.hhly.mlottery.bean.LeagueCup;
import com.hhly.mlottery.bean.scheduleBean.SchMatch;
import com.hhly.mlottery.bean.scheduleBean.ScheduleCurrent;
import com.hhly.mlottery.bean.scheduleBean.ScheduleDate;
import com.hhly.mlottery.bean.scheduleBean.ScheduleMatchDto;
import com.hhly.mlottery.bean.scheduleBean.ScheduleMatchs;
import com.hhly.mlottery.callback.DateOnClickListener;
import com.hhly.mlottery.callback.FocusMatchClickListener;
import com.hhly.mlottery.callback.RecyclerViewItemClickListener;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.FootBallMatchFilterTypeEnum;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.footballframe.eventbus.ScoresMatchFilterEventBusEntity;
import com.hhly.mlottery.frame.footballframe.eventbus.ScoresMatchFocusEventBusEntity;
import com.hhly.mlottery.frame.footballframe.eventbus.ScoresMatchSettingEventBusEntity;
import com.hhly.mlottery.frame.scorefrag.FootBallScoreFragment;
import com.hhly.mlottery.util.AnimUtils;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.FocusUtils;
import com.hhly.mlottery.util.HandMatchId;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.ResultDateUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by asus1 on 2016/4/7.
 */
public class ScheduleFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final int REQUEST_FILTER_CODE = 0x31;
    public static final int REQUEST_SETTING_CODE = 0x32;
    public static final int REQUEST_DETAIL_CODE = 0x33;

    private List<SchMatch> mSchedulelist; // 该日赛程全部数据
    private List<SchMatch> mOperatingList; // 赛程操作list

    public static List<LeagueCup> mAllCup; // 联赛
    private List<ScheduleDate> mDatelist; // 日期
    public static LeagueCup[] mCheckedCups; // 联赛筛选

    private Context mContext;

    private RelativeLayout mNoDataLayout;
    private LinearLayout mErrorLayout;
    private TextView mReloadTvBtn;
    private ExactSwipeRefreshLayout mSwipeRefreshLayout;

    private TextView mNoDataTextView;

    private View mLine;

    private ListView mDateListView;
    private View view;

    private LinearLayout mLoadingLayout;

    private ScheduleAdapter mAdapter;

    private ScheduleDateAdapter mDateAdapter;
    private Intent intent;
    public SchFocusClickListener mSchfocusClickListener;// 关注点击事件

    private String mCurrentDate;

    private static final int DATETOTAL = 7;

    public static boolean isCheckedDefualt = true;// true为默认选中全部，但是在筛选页面不选中
    private boolean isFirstLoadDate = true; // 初始化日期。以后不再初始化
    private boolean isLoadData = false;

    public static boolean isNetSuccess = true;// 告诉筛选页面数据是否加载成功

    public static final int LOAD_DATA_STATUS_INIT = 0;
    public static final int LOAD_DATA_STATUS_LOADING = 1;
    public static final int LOAD_DATA_STATUS_SUCCESS = 2;
    public static final int LOAD_DATA_STATUS_ERROR = 3;


    // public final static int VIEW_DATE_INDEX = 0;
    public final static int VIEW_MATCH_INDEX = 1;

    public static int mLoadDataStatus = LOAD_DATA_STATUS_INIT;// 加载数据状态

    private static final String FRAGMENT_INDEX = "fragment_index";
    private static final String ENTRY_TYPE = "entryType";

    private int mCurIndex = -1;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    private FocusMatchClickListener mFocusMatchClickListener;
    private DateOnClickListener mDateOnClickListener;

    private List<ScheduleDate> mDateList; // 日期
    private int mItems = -1;

    private ArrayList<ScheduleMatchDto> mAllMatchs;// 所有的
    private ArrayList<ScheduleMatchDto> mMatchs;// 显示的

    private String teamLogoPre;

    private String teamLogoSuff;

    private static int currentDatePosition = 0;

    private int mEntryType; // 标记入口 判断是从哪里进来的 (0:首页入口  1:新导航条入口)

    private LinearLayout ll_date_select;

    private TextView tv_date;
    private TextView tv_week;
    private TextView tv_handicap_name1;
    private TextView tv_handicap_name2;

    private LinearLayout promptContent;
    private TextView promptTxt;

    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ScheduleFragment newInstance(int index, int entryType) {
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_INDEX, index);
        bundle.putInt(ENTRY_TYPE, entryType);
        ScheduleFragment fragment = new ScheduleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private Runnable mLoadingDataThread = new Runnable() {
        @Override
        public void run() {
            initData(currentDatePosition);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEntryType = getArguments().getInt(ENTRY_TYPE);
        }
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.football_schedule, container, false);
        this.mContext = getActivity();
        initView();
        return view;
    }

    private void initView() {
        //顶部日期选择保留
        ll_date_select = (LinearLayout) view.findViewById(R.id.ll_date_select);
        tv_date = (TextView) view.findViewById(R.id.tv_date);
        tv_week = (TextView) view.findViewById(R.id.tv_week);
        tv_handicap_name1 = (TextView) view.findViewById(R.id.tv_handicap_name1);
        tv_handicap_name2 = (TextView) view.findViewById(R.id.tv_handicap_name2);

        mSwipeRefreshLayout = (ExactSwipeRefreshLayout) view.findViewById(R.id.football_schedule_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.listview_schedule);
        recyclerView.setLayoutManager(layoutManager);

        mNoDataLayout = (RelativeLayout) view.findViewById(R.id.football_schedule_unfocus_ll);

        mLoadingLayout = (LinearLayout) view.findViewById(R.id.football_schedule_loading_ll);
        mErrorLayout = (LinearLayout) view.findViewById(R.id.network_exception_layout);
        mReloadTvBtn = (TextView) view.findViewById(R.id.network_exception_reload_btn);
        mReloadTvBtn.setOnClickListener(this);

        ll_date_select.setOnClickListener(this);

        mNoDataTextView = (TextView) view.findViewById(R.id.football_schedule_unfocus_no_data_tv);

        mLine = view.findViewById(R.id.line_football_footer);

        // 实现 监听 （实例化） 关注监听
        mFocusMatchClickListener = new FocusMatchClickListener() { // 关注按钮事件
            @Override
            public void onClick(View view, String third) {
                String focusIds = PreferenceUtil.getString("focus_ids", "");
                boolean isCheck = (Boolean) view.getTag();// 检查之前是否被选中

                if (!isCheck) {// 插入数据
                    FocusUtils.addFocusId(third);
                    ((ImageView) view).setImageResource(R.mipmap.football_focus);
                    view.setTag(true);
                } else {// 删除
                    FocusUtils.deleteFocusId(third);
                    ((ImageView) view).setImageResource(R.mipmap.football_nomal);
                    view.setTag(false);
                }
//                ((ScoresFragment) getParentFragment()).focusCallback();
                if (mEntryType == 0) {
                } else if (mEntryType == 1) {
                    ((FootBallScoreFragment) getParentFragment()).focusCallback();
                }
            }
        };
        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        new Handler().postDelayed(mLoadingDataThread, 0);

        promptContent = (LinearLayout) view.findViewById(R.id.ll_prompt_content);
        promptTxt = (TextView) view.findViewById(R.id.tv_prompt_txt);
    }

    private final static int VIEW_STATUS_LOADING = 1;
    private final static int VIEW_STATUS_NO_ANY_DATA = 2;
    private final static int VIEW_STATUS_SUCCESS = 3;
    private final static int VIEW_STATUS_NET_ERROR = 4;
    private final static int VIEW_STATUS_FLITER_NO_DATA = 5;

    Handler mViewHandler = new Handler() {
        public void handleMessage(Message msg) {
            isNetSuccess = true;
            switch (msg.what) {
                case VIEW_STATUS_LOADING:
                    mLoadDataStatus = LOAD_DATA_STATUS_LOADING;
                    mErrorLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    mNoDataLayout.setVisibility(View.GONE);
                    if (!isLoadData) {
                        mLine.setVisibility(View.GONE);
                        mLoadingLayout.setVisibility(View.VISIBLE);
                    }
                    break;
                case VIEW_STATUS_SUCCESS:
                    mLoadDataStatus = LOAD_DATA_STATUS_SUCCESS;
                    mLoadingLayout.setVisibility(View.GONE);
                    mErrorLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mNoDataLayout.setVisibility(View.GONE);
                    mLine.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);

                    break;
                case VIEW_STATUS_NET_ERROR:
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (isLoadData) {
                      //  Toast.makeText(getActivity(), R.string.exp_net_status_txt, Toast.LENGTH_SHORT).show();
                    } else {
                        mLine.setVisibility(View.GONE);
                        mLoadingLayout.setVisibility(View.GONE);
                        //mSwipeRefreshLayout.setVisibility(View.GONE);
                        mErrorLayout.setVisibility(View.VISIBLE);
                        mNoDataLayout.setVisibility(View.GONE);
                        mLoadDataStatus = LOAD_DATA_STATUS_ERROR;
                    }
                    isNetSuccess = false;
                    break;
                case VIEW_STATUS_NO_ANY_DATA:
                    mLoadDataStatus = LOAD_DATA_STATUS_SUCCESS;
                    mLoadingLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mErrorLayout.setVisibility(View.GONE);
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    mNoDataTextView.setText(R.string.immediate_no_match);
                    break;
                case VIEW_STATUS_FLITER_NO_DATA:
                    mLoadDataStatus = LOAD_DATA_STATUS_SUCCESS;
                    mLoadingLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mErrorLayout.setVisibility(View.GONE);
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    mNoDataTextView.setText(R.string.immediate_no_data);
                    break;
                default:
                    break;
            }

        }
    };

    private void initData(final int position) {

        String url = BaseURLs.URL_CeaselessMatchs;
        Map<String, String> params = new HashMap<String, String>();

        if (mCurrentDate != null) {
            List mDatelist = new ArrayList<String>();
            for (int i = 0; i < DATETOTAL; i++) {
                mDatelist.add(DateUtil.getDate(i, mCurrentDate));
            }
            params.put("date", mDatelist.get(position).toString());
        }

        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<ScheduleMatchs>() {
            @Override
            public synchronized void onResponse(final ScheduleMatchs json) {
                if (json == null) {
                    mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
                    return;
                }

                if (getActivity() == null) {
                    return;
                }

                mAllMatchs = new ArrayList<ScheduleMatchDto>();
                mMatchs = new ArrayList<ScheduleMatchDto>();

                final ScheduleCurrent current = json.getCurrent();
                if (current == null) {
                    return;
                }

                mAllCup = json.getCeaselessFilter();

                teamLogoPre = json.getTeamLogoPre();

                teamLogoSuff = json.getTeamLogoSuff();

                if (!PreferenceUtil.getString(FootBallMatchFilterTypeEnum.FOOT_CURR_DATE_SCHEDULE, "").equals(json.getFilerDate())) {
                    PreferenceUtil.removeKey(FootBallMatchFilterTypeEnum.FOOT_SCHEDULE);
                    PreferenceUtil.commitString(FootBallMatchFilterTypeEnum.FOOT_CURR_DATE_SCHEDULE, json.getFilerDate());
                }

                if (current != null) {
                    // ScheduleMatchDto scheduleMatchDto = new ScheduleMatchDto();


                    setTopDateSelect(json.getCurrent().getDate());
                    setTopHandicap();

                    // scheduleMatchDto.setDate(json.getCurrent().getDate());
                    //  scheduleMatchDto.setType(VIEW_DATE_INDEX);

                    //  mAllMatchs.add(scheduleMatchDto);

                    for (SchMatch match : current.getMatch()) {
                        ScheduleMatchDto dtoMatch = new ScheduleMatchDto();
                        dtoMatch.setType(VIEW_MATCH_INDEX);
                        dtoMatch.setSchmatchs(match);
                        mAllMatchs.add(dtoMatch);
                    }
                    if (isFirstLoadDate) {
                        mCurrentDate = json.getCurrent().getDate();

                        initListDateAndWeek(mCurrentDate, currentDatePosition);
                        isFirstLoadDate = false;
                    }
                    if (!isCheckedDefualt) {

                        mMatchs.clear();
                        for (ScheduleMatchDto sch : mAllMatchs) {

                         /*   if (sch.getType() == VIEW_DATE_INDEX) {
                                mMatchs.add(sch);
                                continue;
                            }*/

                            for (LeagueCup cup : mCheckedCups) {
                                if ((sch.getType() == VIEW_MATCH_INDEX) && cup.getRaceId().equals(sch.getSchmatchs().getRaceId())) {
                                    mMatchs.add(sch);
                                    break;
                                }
                            }
                        }
                    } else {
                        if (PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_SCHEDULE).size() > 0) {
                            List<String> list = PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_SCHEDULE);
                            for (ScheduleMatchDto sch : mAllMatchs) {
                             /*   if (sch.getType() == VIEW_DATE_INDEX) {
                                    mMatchs.add(sch);
                                    continue;
                                }*/

                                for (String raceId : list) {
                                    if ((sch.getType() == VIEW_MATCH_INDEX) && raceId.equals(sch.getSchmatchs().getRaceId())) {
                                        mMatchs.add(sch);
                                        break;
                                    }
                                }
                            }

                        } else {
                            mMatchs.addAll(mAllMatchs);
                            mCheckedCups = mAllCup.toArray(new LeagueCup[mAllCup.size()]);
                        }
                    }

                    mAdapter = new ScheduleAdapter(mContext, mMatchs, teamLogoPre, teamLogoSuff);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.setmFocusMatchClickListener(mFocusMatchClickListener);
                    mAdapter.setDateOnClickListener(mDateOnClickListener);

                    mAdapter.setmOnItemClickListener(new RecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, String data) {
                            String thirdId = data;
                            if (HandMatchId.handId(getActivity(), thirdId)) {
                                Intent intent = new Intent(getActivity(), FootballMatchDetailActivity.class);
                                intent.putExtra("thirdId", thirdId);
                                intent.putExtra("currentFragmentId", 3);
                                getParentFragment().startActivityForResult(intent, REQUEST_DETAIL_CODE);
                            }
                        }
                    });

                    mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
                    isLoadData = true;

                    // 更新提示
                    AnimUtils.tAnimShow(promptContent);
                    mViewHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AnimUtils.tAnimHide(promptContent);
                        }
                    },2000);
                    promptTxt.setText(String.format(getString(R.string.football_up_data_prompt), mAllMatchs.size(), mAllMatchs.size() - mMatchs.size()));
                } else {
                    mViewHandler.sendEmptyMessage(VIEW_STATUS_NO_ANY_DATA);
                }
                currentDatePosition = position;
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
            }
        }, ScheduleMatchs.class);

        choiceDateList();
    }

    private void setTopDateSelect(String date) {
        tv_date.setText(DateUtil.convertDateToNation(date));
        tv_week.setText(ResultDateUtil.getWeekOfDate(DateUtil.parseDate(ResultDateUtil.getDate(0, date))));
    }


    private void setTopHandicap() {
        boolean alet = PreferenceUtil.getBoolean(MyConstants.RBSECOND, true);
        boolean asize = PreferenceUtil.getBoolean(MyConstants.rbSizeBall, false);
        boolean eur = PreferenceUtil.getBoolean(MyConstants.RBOCOMPENSATE, true);
        boolean noshow = PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false);
        // 隐藏赔率name
        if (noshow) {
            tv_handicap_name1.setVisibility(View.GONE);
            tv_handicap_name2.setVisibility(View.GONE);
        } else if ((asize && eur) || (asize && alet) || (eur && alet)) {
            tv_handicap_name1.setVisibility(View.VISIBLE);
            tv_handicap_name2.setVisibility(View.VISIBLE);
        } else {
            tv_handicap_name1.setVisibility(View.VISIBLE);
            tv_handicap_name2.setVisibility(View.GONE);
        }

        // 亚盘赔率
        if (alet) {
            tv_handicap_name1.setText(mContext.getResources().getString(R.string.roll_asialet));
        }
        // 大小盘赔率
        if (asize) {
            if (!alet) {
                tv_handicap_name1.setText(mContext.getResources().getString(R.string.roll_asiasize));
            } else {
                tv_handicap_name2.setText(mContext.getResources().getString(R.string.roll_asiasize));
            }
        }
        // 欧盘赔率
        if (eur) {
            if (!alet && !asize) {
                tv_handicap_name1.setText(mContext.getResources().getString(R.string.roll_euro));
            } else {
                tv_handicap_name2.setText(mContext.getResources().getString(R.string.roll_euro));
            }
        }
    }


    private void choiceDateList() {
        mDateOnClickListener = new DateOnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
                final AlertDialog alertDialog = builder.create();
                LayoutInflater infla = LayoutInflater.from(getActivity());
                View alertDialogView = infla.inflate(R.layout.alertdialog, null);
                mDateListView = (ListView) alertDialogView.findViewById(R.id.listdate);
                initListDateAndWeek(mCurrentDate, currentDatePosition);
                mDateListView.setAdapter(mDateAdapter);
                mDateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
                        initData(position);
                        isCheckedDefualt = true;
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                alertDialog.getWindow().setContentView(alertDialogView);
                alertDialog.setCanceledOnTouchOutside(true);
            }
        };
    }

    public void updateAdapter() {
        if (mAdapter != null) {
            mAdapter.updateDatas(mMatchs);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 初始化7天的日期
     *
     * @param s
     */
    private void initListDateAndWeek(String s, int position) {
        mDatelist = new ArrayList<ScheduleDate>();
        for (int i = 0; i < DATETOTAL; i++) {
            mDatelist.add(new ScheduleDate(DateUtil.getDate(i, s), DateUtil.getWeekOfXinQi(DateUtil.parseDate(DateUtil.getDate(i, s)))));
        }
        mDateAdapter = new ScheduleDateAdapter(mDatelist, mContext, position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.network_exception_reload_btn:
                mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
                initData(currentDatePosition);
                break;

            case R.id.ll_date_select:
                if (mDateOnClickListener != null) {
                    mDateOnClickListener.onClick(v);
                }
                break;
            default:
                break;
        }
    }

    public interface SchFocusClickListener {
        void onClick(View view, SchMatch match);
    }

    /**
     * 筛选比赛
     * 接受消息的页面实现
     */
    public void onEventMainThread(ScoresMatchFilterEventBusEntity scoresMatchFilterEventBusEntity) {
        if (scoresMatchFilterEventBusEntity.getFgIndex() == 3) {
            Map<String, Object> map = scoresMatchFilterEventBusEntity.getMap();
            String[] checkedIds = (String[]) ((LinkedList) map.get(FiltrateMatchConfigActivity.RESULT_CHECKED_CUPS_IDS)).toArray(new String[]{});
            mMatchs.clear();
            if (checkedIds.length != 0) {
                for (ScheduleMatchDto match : mAllMatchs) {
                /*    if (match.getType() == VIEW_DATE_INDEX) {
                        mMatchs.add(match);
                        continue;
                    }*/

                    for (String checkedId : checkedIds) {
                        if ((match.getType() == VIEW_MATCH_INDEX) && checkedId.equals(match.getSchmatchs().getRaceId())) {
                            mMatchs.add(match);
                            break;
                        }
                    }
                }
                List<LeagueCup> leagueCupList = new ArrayList<LeagueCup>();
                List<String> localFilterRace = new ArrayList<>();
                for (LeagueCup cup : mAllCup) {
                    boolean isExistId = false;
                    for (String checkedId : checkedIds) {
                        if (checkedId.equals(cup.getRaceId())) {
                            isExistId = true;
                            break;
                        }
                    }
                    if (isExistId) {
                        leagueCupList.add(cup);
                        localFilterRace.add(cup.getRaceId());
                    }
                }
                mCheckedCups = leagueCupList.toArray(new LeagueCup[]{});

                PreferenceUtil.setDataList(FootBallMatchFilterTypeEnum.FOOT_SCHEDULE, localFilterRace);
                updateAdapter();
                mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
            } else {
               /* for (ScheduleMatchDto match : mAllMatchs) {
                    if (match.getType() == VIEW_DATE_INDEX) {
                        mMatchs.add(match);
                        continue;
                    }
                }
*/
                mCheckedCups = new LeagueCup[]{};//选择0场  把选中联赛为 空集
                mViewHandler.sendEmptyMessage(VIEW_STATUS_FLITER_NO_DATA);
                updateAdapter();
            }

            isCheckedDefualt = (boolean) map.get(FiltrateMatchConfigActivity.CHECKED_DEFUALT);
        }

    }

    /**
     * 设置
     * 接受消息的页面实现
     */

    public void onEventMainThread(ScoresMatchSettingEventBusEntity scoresMatchSettingEventBusEntity) {
        updateAdapter();
        setTopHandicap();
    }

    /**
     * EventBus 赛场比赛详情返回FootballMatchDetailActivity
     * 接受消息的页面实现
     */
    public void onEventMainThread(ScoresMatchFocusEventBusEntity scoresMatchFocusEventBusEntity) {
        if (scoresMatchFocusEventBusEntity.getFgIndex() == 3) {
            updateAdapter();
            if (mEntryType == 0) {
            } else if (mEntryType == 1) {
                ((FootBallScoreFragment) getParentFragment()).focusCallback();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            L.d("xxx", "Schedule>>>>isVisibleToUser...显示了");
        } else {
            L.d("xxx", "Schedule>>>>isVisibleToUser...隐藏了");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //为什么要这样，不懂。
        //updateAdapter();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        updateAdapter();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
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
                initData(currentDatePosition);
            }
        }, 1000);
    }

}
