package com.hhly.mlottery.frame.footballframe;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FiltrateMatchConfigActivity;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.RollBallAdapter;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewHolder;
import com.hhly.mlottery.base.BaseFragment;
import com.hhly.mlottery.bean.HotFocusLeagueCup;
import com.hhly.mlottery.bean.ImmediateMatchs;
import com.hhly.mlottery.bean.LeagueCup;
import com.hhly.mlottery.bean.Match;
import com.hhly.mlottery.bean.websocket.WebSocketMatchChange;
import com.hhly.mlottery.bean.websocket.WebSocketMatchEvent;
import com.hhly.mlottery.bean.websocket.WebSocketMatchOdd;
import com.hhly.mlottery.bean.websocket.WebSocketMatchStatus;
import com.hhly.mlottery.callback.RecyclerViewItemClickListener;
import com.hhly.mlottery.callback.RequestHostFocusCallBack;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.FootBallMatchFilterTypeEnum;
import com.hhly.mlottery.frame.footballframe.eventbus.ScoresMatchFilterEventBusEntity;
import com.hhly.mlottery.frame.footballframe.eventbus.ScoresMatchFocusEventBusEntity;
import com.hhly.mlottery.frame.footballframe.eventbus.ScoresMatchSettingEventBusEntity;
import com.hhly.mlottery.frame.scorefrag.FootBallScoreFragment;
import com.hhly.mlottery.util.AnimUtils;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.FiltrateCupsMap;
import com.hhly.mlottery.util.HandMatchId;
import com.hhly.mlottery.util.HotFocusUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.RxBus;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.hhly.mlottery.util.Preconditions.checkNotNull;

public class RollBallFragment extends BaseFragment implements BaseRecyclerViewHolder.OnItemClickListener,
        BaseRecyclerViewHolder.OnItemLongClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String FRAGMENT_SAVED_STATE_KEY = RollBallFragment.class.getSimpleName();
    private static final String FRAGMENT_INDEX = "fragment_index";
    private final static int SOCKET_TYPE_STATUS = 1; // 状态
    private final static int SOCKET_TYPE_ODDS = 2; // 赔率
    private final static int SOCKET_TYPE_EVENT = 3; // 事件
    private final static int SOCKET_TYPE_ALLEVENTS = 4; // 全部场次
    private final static int SOCKET_TYPE_SESSIONCHANGED = 5; // 场次名称变动
    private final static int VIEW_STATUS_LOADING = 1001;
    private final static int VIEW_STATUS_NO_ANY_DATA = 2001;
    private final static int VIEW_STATUS_SUCCESS = 3001;
    private final static int VIEW_STATUS_NET_ERROR = 4001;
    private final static int VIEW_STATUS_FLITER_NO_DATA = 5001;


    private final static int DELAY_REQUEST_API = 60 * 1000; // 10 min
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.football_immediate_no_data_tv)
    TextView footballImmediateNoDataTv;
    @BindView(R.id.football_immediate_unfocus_ll)
    RelativeLayout footballImmediateUnfocusLl;
    @BindView(R.id.network_exception_layout)
    LinearLayout networkExceptionLayout;
    @BindView(R.id.swipe_refresh_layout)
    ExactSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.titleContainer)
    LinearLayout titleContainer;
    @BindView(R.id.tv_handicap_name1)
    TextView handicapName1;
    @BindView(R.id.tv_handicap_name2)
    TextView handicapName2;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_week)
    TextView tvWeek;
    @BindView(R.id.ll_odd)
    LinearLayout llOdd;
    @BindView(R.id.ll_prompt_content)
    LinearLayout promptContent;
    @BindView(R.id.tv_prompt_txt)
    TextView promptTxt;

    private ApiHandler apiHandler = new ApiHandler(this);
    private RollBallAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Subscription subscription;
    private boolean resestTheLifeCycle;
    private boolean loadingMoreData;
    public LeagueCup[] checkedLeagueCup; // 记录筛选过的联赛
    public List<LeagueCup> leagueCupLists; // 全部联赛
    private List<Match> allDataLists; // 所有数据
    private List<Match> feedAdapterLists; // 要展示的数据

    private boolean isLoadedData = false;
    private static final String ISNEW_FRAMEWORK = "isnew_framework";
    private static final String ENTRY_TYPE = "entryType";

    private boolean isNewFrameWork;
    private int mEntryType; // 标记入口 判断是从哪里进来的 (0:首页入口  1:新导航条入口)

    public static RollBallFragment newInstance(int index, boolean isNewFramWork, int entryType) {
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_INDEX, index);
        bundle.putBoolean(ISNEW_FRAMEWORK, isNewFramWork);
        bundle.putInt(ENTRY_TYPE, entryType);
        RollBallFragment fragment = new RollBallFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isNewFrameWork = getArguments().getBoolean(ISNEW_FRAMEWORK);
            mEntryType = getArguments().getInt(ENTRY_TYPE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHandicapName();
    }

    /**
     * 设置盘口显示类型
     */
    private void setHandicapName() {
        boolean alet = PreferenceUtil.getBoolean(MyConstants.RBSECOND, true);
        boolean asize = PreferenceUtil.getBoolean(MyConstants.rbSizeBall, false);
        boolean eur = PreferenceUtil.getBoolean(MyConstants.RBOCOMPENSATE, true);
        // 隐藏赔率name
        if ((asize && eur) || (asize && alet) || (eur && alet)) {
            handicapName1.setVisibility(View.VISIBLE);
            handicapName2.setVisibility(View.VISIBLE);
        } else {
            handicapName1.setVisibility(View.VISIBLE);
            handicapName2.setVisibility(View.GONE);
        }
        // 亚盘赔率
        if (alet) {
            handicapName1.setText(getResources().getString(R.string.roll_asialet));
        }
        // 大小盘赔率
        if (asize) {
            if (!alet) {
                handicapName1.setText(getResources().getString(R.string.roll_asiasize));
            } else {
                handicapName2.setText(getResources().getString(R.string.roll_asiasize));
            }
        }
        // 欧盘赔率
        if (eur) {
            if (!alet && !asize) {
                handicapName1.setText(getResources().getString(R.string.roll_euro));
            } else {
                handicapName2.setText(getResources().getString(R.string.roll_euro));
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.football_roll;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        this.setupEventBus();
        this.setupRecyclerView();
        this.setupAdapter();
        this.setupSwipeRefresh();
    }

    @Override
    protected void initListeners() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean moveToDown = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (loadingMoreData) return;
                RecyclerView.LayoutManager lManager = recyclerView.getLayoutManager();
                if (lManager instanceof LinearLayoutManager) {
                    LinearLayoutManager manager = (LinearLayoutManager) lManager;
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (moveToDown && manager.findLastCompletelyVisibleItemPosition() == (manager.getItemCount() - 1)) {
                            loadingMoreData = true;
                            loadUserList();
                        } else if (layoutManager.findFirstVisibleItemPosition() == 0) {

                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                this.moveToDown = dy > 0;
            }
        });

        networkExceptionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setRefreshing(true);
                networkExceptionLayout.setVisibility(View.GONE);
                RollBallFragment.this.initData(0);
            }
        });

        // 60秒后 重新请求数据 这样完场的数据 就会被排序到底部
        subscription = RxBus.getDefault().toObserverable(Match.class).delay(60, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Match>() {
            @Override
            public void call(final Match match) {
                RollBallFragment.this.initData(1);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                apiHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
            }
        });
    }

    @Override
    protected void initData(int type) {
        if (type == 0) {
            apiHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        }
        this.requestApi();
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void mOnViewStateRestored(Bundle savedInstanceState) {
        String restoreData = (String) savedInstanceState.getSerializable(FRAGMENT_SAVED_STATE_KEY);
        resestTheLifeCycle = true;
    }

    @Override
    protected void mOnSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(FRAGMENT_SAVED_STATE_KEY, "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

        if (subscription != null && subscription.isUnsubscribed()) subscription.unsubscribe();
        if (apiHandler != null) apiHandler.removeCallbacksAndMessages(null);
        if (adapter != null) adapter.getSharedPreperences().edit().clear().commit();

    }

    @Override
    public void onItemClick(View convertView, int position) {


        Intent intent = new Intent(getActivity(), FootballMatchDetailActivity.class);
        List<Match> topLists = adapter.getTopLists();
        if (null == topLists) topLists = feedAdapterLists;

        if (HandMatchId.handId(getActivity(), topLists.get(position).getThirdId())) {
            intent.putExtra("thirdId", topLists.get(position).getThirdId());
            intent.putExtra("currentFragmentId", 0);
            getParentFragment().startActivity(intent);
        }
    }

    @Override
    public void onItemLongClick(View convertView, int position) {
    }

    @Override
    public void onRefresh() {
        this.initData(0);

        if (isNewFrameWork) {
            ((FootBallScoreFragment) getParentFragment()).reconnectWebSocket();
        } else {

        }
    }


    public void onEventMainThread(FootBallScoreFragment.FootballScoresWebSocketEntity entity) {
        L.d("gaiban", "滚球推送改版");

        if (adapter == null) {
            return;
        }

        String type = "";
        try {
            JSONObject jsonObject = new JSONObject(entity.text);
            type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(type)) {
            Message msg = Message.obtain();
            msg.obj = entity.text;
            msg.arg1 = Integer.parseInt(type);
            apiHandler.sendMessage(msg);
        }
    }

    private void setupTitleAnimations(View titleView, int translationY, Animator.AnimatorListener animatorListener) {
        titleView.animate().translationY(translationY).setDuration(400).setListener(animatorListener).start();
    }

    private void loadUserList() {
        //		initData();
    }

    private void setupSwipeRefresh() {
//        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(MyApp.getContext(), 40));
    }

    private void setupEventBus() {
        EventBus.getDefault().register(this);
    }

    private void setupRecyclerView() {

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setupAdapter() {
        if (null == adapter) {
            adapter = new RollBallAdapter(getActivity());
            recyclerView.setAdapter(adapter);
            adapter.setmOnItemClickListener(new RecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, String data) {
                    if (HandMatchId.handId(getActivity(), data)) {

                        String thirdId = data;
                        Intent intent = new Intent(getActivity(), FootballMatchDetailActivity.class);
                        intent.putExtra("thirdId", thirdId);
                        intent.putExtra("currentFragmentId", 0);
                        getParentFragment().startActivityForResult(intent, 0x12);
                    }
                }
            });
        }
    }

    private void feedAdapter(List<Match> dataLists) {
        checkNotNull(adapter, "adapter == null");

        this.checkTheLifeCycleIsChanging(resestTheLifeCycle);
        adapter.setList(dataLists);
        adapter.notifyDataSetChanged();
    }

    public void feedAdapter() {
        if (apiHandler != null) apiHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        this.initData(0);
    }

    private void checkTheLifeCycleIsChanging(boolean resestTheLifeCycle) {
        if (resestTheLifeCycle) {
            this.resestTheLifeCycle = false;
            recyclerView.setLayoutManager(layoutManager);
        }
    }


    public void requestApi() {
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_Rollball,
                new VolleyContentFast.ResponseSuccessListener<ImmediateMatchs>() {
                    @Override
                    public void onResponse(final ImmediateMatchs jsonObject) {
                        if (null == jsonObject || null == jsonObject.getImmediateMatch()) {
                            apiHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
                            return;
                        }
                        allDataLists = jsonObject.getImmediateMatch();
                        leagueCupLists = jsonObject.getAll();
                        feedAdapterLists = new ArrayList<>();

                        L.d("ddddeee", "刷新前==" + PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_ROLL).size() + "");
                        L.d("ddddeee", "日期==" + PreferenceUtil.getString(FootBallMatchFilterTypeEnum.FOOT_CURR_DATE_ROLL, ""));
                        L.d("ddddeee", "请求==" + jsonObject.getFilerDate());


                        if (!PreferenceUtil.getString(FootBallMatchFilterTypeEnum.FOOT_CURR_DATE_ROLL, "").equals(jsonObject.getFilerDate())) {
                            L.d("ddddeee", "删除");

                            PreferenceUtil.removeKey(FootBallMatchFilterTypeEnum.FOOT_ROLL);
                            PreferenceUtil.commitString(FootBallMatchFilterTypeEnum.FOOT_CURR_DATE_ROLL, jsonObject.getFilerDate());
                            L.d("ddddeee", "保存日期==" + PreferenceUtil.getString(FootBallMatchFilterTypeEnum.FOOT_CURR_DATE_ROLL, ""));

                        }

                        L.d("ddddeee", "刷新后==" + PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_ROLL).size() + "");


                        HotFocusUtils hotFocusUtils = new HotFocusUtils();
                        hotFocusUtils.loadHotFocusData(getActivity(), new RequestHostFocusCallBack() {
                            @Override
                            public void callBack(HotFocusLeagueCup hotFocusLeagueCup) {
                                List<String> hotList;
                                if (hotFocusLeagueCup == null) {
                                    hotList = new ArrayList<>();
                                } else {
                                    hotList = hotFocusLeagueCup.getHotLeagueIds();
                                }

                                if (FiltrateCupsMap.rollballCups.length != 0) {// 判断是否已经筛选过
                                    if (PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_ROLL).size() > 0) {
                                        List<String> list = PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_ROLL);  //先判断本地保存
                                        for (Match m : allDataLists) {// 已选择的
                                            for (String checkedId : list) {
                                                if (m.getRaceId().equals(checkedId)) {
                                                    feedAdapterLists.add(m);
                                                    break;
                                                }
                                            }
                                        }
                                    } else {
                                        for (Match m : allDataLists) {// 已选择的
                                            for (String checkedId : FiltrateCupsMap.rollballCups) {
                                                if (m.getRaceId().equals(checkedId)) {
                                                    feedAdapterLists.add(m);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                } else {// 没有筛选过

                                    if (PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_ROLL).size() > 0) {

                                        List<String> list = PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_ROLL);

                                        L.d("filter", list.size() + "");

                                        for (Match m : allDataLists) {// 默认显示热门赛程
                                            for (String filterId : list) {
                                                if (m.getRaceId().equals(filterId)) {
                                                    feedAdapterLists.add(m);
                                                    break;
                                                }
                                            }
                                        }
                                    } /*else {
                                        for (Match m : allDataLists) {// 默认显示热门赛程
                                            for (String hotId : hotList) {
                                                if (m.getRaceId().equals(hotId)) {
                                                    feedAdapterLists.add(m);
                                                    break;
                                                }
                                            }
                                        }
                                    }*/
                                }

                                footballImmediateNoDataTv.setText(R.string.immediate_no_data);
                                if (feedAdapterLists.size() == 0) {// 没有热门赛事，显示全部
                                    feedAdapterLists.addAll(allDataLists);
                                    checkedLeagueCup = leagueCupLists.toArray(new LeagueCup[leagueCupLists.size()]);
                                    if (feedAdapterLists.size() == 0) {// 一个赛事都没有，显示“暂无赛事”
                                        RollBallFragment.this.feedAdapter(feedAdapterLists);
                                        apiHandler.sendEmptyMessage(VIEW_STATUS_NO_ANY_DATA);
                                        return;
                                    }
                                } else {
                                    List<LeagueCup> tempHotCups = new ArrayList<>();
                                    if (FiltrateCupsMap.rollballCups.length != 0) {
                                        for (LeagueCup cup : leagueCupLists) {
                                            for (String checkedId : FiltrateCupsMap.rollballCups) {
                                                if (cup.getRaceId().equals(checkedId)) {
                                                    tempHotCups.add(cup);
                                                    break;
                                                }
                                            }
                                        }
                                    } else {
                                        for (LeagueCup cup : leagueCupLists) {
                                            for (String hotId : hotList) {
                                                if (cup.getRaceId().equals(hotId)) {
                                                    tempHotCups.add(cup);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    checkedLeagueCup = tempHotCups.toArray(new LeagueCup[tempHotCups.size()]);
                                }

                                tvDate.setText(jsonObject.getFilerDate());

                                if (!TextUtils.isEmpty(jsonObject.getFilerDate()))
                                    tvWeek.setText(DateUtil.getWeekOfXinQi(DateUtil.parseDate(jsonObject.getFilerDate())));

                                RollBallFragment.this.feedAdapter(feedAdapterLists);
                                apiHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
                                isLoadedData = true;

                                // 更新提示
                                AnimUtils.tAnimShow(promptContent);
                                apiHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AnimUtils.tAnimHide(promptContent);
                                    }
                                }, 2000);
                                promptTxt.setText(String.format(getString(R.string.football_up_data_prompt), allDataLists.size(), allDataLists.size() - feedAdapterLists.size()));
                            }
                        });
                    }

                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        apiHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
                    }
                }, ImmediateMatchs.class);
    }

    /**
     * 比赛详情返回FootballMatchDetailActivity
     * 接受消息的页面实现
     */
    public void onEventMainThread(ScoresMatchFocusEventBusEntity scoresMatchFocusEventBusEntity) {
        if (scoresMatchFocusEventBusEntity.getFgIndex() == 0) {
            this.feedAdapter(feedAdapterLists);
//            ((ScoresFragment) getParentFragment()).focusCallback();
            if (mEntryType == 0) {
            } else if (mEntryType == 1) {
                ((FootBallScoreFragment) getParentFragment()).focusCallback();
            }
        }
    }

    /**
     * 筛选返回
     * 接受消息的页面实现
     */

    public void onEventMainThread(ScoresMatchFilterEventBusEntity scoresMatchFilterEventBusEntity) {
        if (scoresMatchFilterEventBusEntity.getFgIndex() == 0) {
            Map<String, Object> map = scoresMatchFilterEventBusEntity.getMap();
            String[] checkedIds = (String[]) ((LinkedList) map.get(FiltrateMatchConfigActivity.RESULT_CHECKED_CUPS_IDS)).toArray(
                    new String[]{});
            FiltrateCupsMap.rollballCups = checkedIds;
            feedAdapterLists.clear();
            for (Match match : allDataLists) {
                boolean isExistId = false;
                for (String checkedId : checkedIds) {
                    if (match.getRaceId().equals(checkedId)) {
                        isExistId = true;
                        break;
                    }
                }
                if (isExistId) {
                    feedAdapterLists.add(match);
                }
            }
            List<LeagueCup> leagueCupList = new ArrayList<>();
            List<String> localFilterRace = new ArrayList<>();

            for (LeagueCup cup : leagueCupLists) {
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

            PreferenceUtil.setDataList(FootBallMatchFilterTypeEnum.FOOT_ROLL, localFilterRace);

            checkedLeagueCup = leagueCupList.toArray(new LeagueCup[]{});
            this.feedAdapter(feedAdapterLists);

            if (feedAdapterLists.size() == 0) {// 没有比赛
                apiHandler.sendEmptyMessage(VIEW_STATUS_FLITER_NO_DATA);
            } else {
                apiHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
            }
        }
    }

    public List<LeagueCup> getLeagueCupLists() {
        return leagueCupLists;
    }

    public LeagueCup[] getLeagueCupChecked() {
        return checkedLeagueCup;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private class ApiHandler extends Handler {
        private WeakReference<RollBallFragment> weakReference;

        ApiHandler(RollBallFragment fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            final RollBallFragment fragment = weakReference.get();
            if (null != fragment) {
                switch (msg.what) {
                    case 0:  // WEB_SOCKET PUSH
                        switch (msg.arg1) {
                            case SOCKET_TYPE_STATUS:  //1时
                                String statusJsonData = (String) msg.obj;
                                WebSocketMatchStatus webSocketMatchStatus = (WebSocketMatchStatus) this.jsonParseObject(statusJsonData,
                                        WebSocketMatchStatus
                                                .class);
                                fragment.adapter.updateItemFromWebSocket(webSocketMatchStatus);
                                break;
                            case SOCKET_TYPE_ODDS:
                                String oddsJsonData = (String) msg.obj;
                                WebSocketMatchOdd webSocketMatchOdd = (WebSocketMatchOdd) this.jsonParseObject(oddsJsonData,
                                        WebSocketMatchOdd.class);
                                fragment.adapter.updateItemFromWebSocket(webSocketMatchOdd);
                                break;
                            case SOCKET_TYPE_EVENT:
                                String eventJsonData = (String) msg.obj;
                                WebSocketMatchEvent webSocketMatchEvent = (WebSocketMatchEvent) this.jsonParseObject(eventJsonData,
                                        WebSocketMatchEvent.class);
                                fragment.adapter.updateItemFromWebSocket(webSocketMatchEvent);
                                break;
                            case SOCKET_TYPE_ALLEVENTS:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        fragment.apiHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
                                        fragment.initData(0);
                                    }
                                }, DELAY_REQUEST_API);
                                break;
                            case SOCKET_TYPE_SESSIONCHANGED:
                                String sessionChangedJsonData = (String) msg.obj;
                                WebSocketMatchChange webSocketMatchChange = (WebSocketMatchChange) this.jsonParseObject(sessionChangedJsonData,
                                        WebSocketMatchChange
                                                .class);
                                fragment.adapter.updateItemFromWebSocket(webSocketMatchChange);
                                break;
                        }
                        break;

                    case VIEW_STATUS_LOADING:
                        fragment.swipeRefreshLayout.setRefreshing(true);
//                        fragment.swipeRefreshLayout.setVisibility(View.VISIBLE);
                        fragment.networkExceptionLayout.setVisibility(View.GONE);
                        fragment.footballImmediateUnfocusLl.setVisibility(View.GONE);
                        break;
                    case VIEW_STATUS_NO_ANY_DATA:
                        fragment.swipeRefreshLayout.setRefreshing(false);
                        fragment.networkExceptionLayout.setVisibility(View.GONE);
                        fragment.footballImmediateUnfocusLl.setVisibility(View.VISIBLE);
                        fragment.titleContainer.setVisibility(View.GONE);
                        break;
                    case VIEW_STATUS_SUCCESS:
                        fragment.titleContainer.setVisibility(View.VISIBLE);
                        fragment.llOdd.setVisibility(PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false) ? View.GONE : View.VISIBLE);
                        fragment.swipeRefreshLayout.setRefreshing(false);
                        fragment.networkExceptionLayout.setVisibility(View.GONE);
                        fragment.footballImmediateUnfocusLl.setVisibility(View.GONE);
                        break;
                    case VIEW_STATUS_NET_ERROR:
                        if (isLoadedData) {
                          //  Toast.makeText(MyApp.getContext(), R.string.exp_net_status_txt, Toast.LENGTH_SHORT).show();
                        } else {
                            fragment.titleContainer.setVisibility(View.GONE);
                            fragment.networkExceptionLayout.setVisibility(View.VISIBLE);
                            fragment.footballImmediateUnfocusLl.setVisibility(View.GONE);
                            fragment.adapter.setList(null);
                            fragment.adapter.notifyDataSetChanged();
                        }
                        fragment.swipeRefreshLayout.setRefreshing(false);
                        break;
                    case VIEW_STATUS_FLITER_NO_DATA:
                        fragment.swipeRefreshLayout.setRefreshing(false);
                        fragment.networkExceptionLayout.setVisibility(View.GONE);
                        fragment.titleContainer.setVisibility(View.GONE);
                        fragment.footballImmediateUnfocusLl.setVisibility(View.VISIBLE);
                        break;
//                    case VIEW_STATUS_WEBSOCKET_CONNECT_FAIL:
//                        if (fragment.mUnconectionLayout != null) {
//                            fragment.mUnconectionLayout.setVisibility(View.VISIBLE);
//                        }
//                        break;
//                    case VIEW_STATUS_WEBSOCKET_CONNECT_SUCCESS:
//                        if (fragment.mUnconectionLayout != null) {
//                            fragment.mUnconectionLayout.setVisibility(View.GONE);
//                        }
//                        break;
                }
            }
        }

        private Object jsonParseObject(String oddsJsonData, Class clazz) {
            try {
                return JSON.parseObject(oddsJsonData, clazz);
            } catch (Exception e) {
                return JSON.parseObject(oddsJsonData.substring(0, oddsJsonData.length() - 1), clazz);
            }
        }
    }

    /**
     * 设置
     * 接受消息的页面实现
     */
    public void onEventMainThread(ScoresMatchSettingEventBusEntity scoresMatchSettingEventBusEntity) {
        llOdd.setVisibility(PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false) ? View.GONE : View.VISIBLE);
        setHandicapName();
        adapter.notifyDataSetChanged();
    }
}