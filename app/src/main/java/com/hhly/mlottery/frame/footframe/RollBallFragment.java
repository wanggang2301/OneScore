package com.hhly.mlottery.frame.footframe;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FiltrateMatchConfigActivity;
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
import com.hhly.mlottery.callback.RequestHostFocusCallBack;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.ScoresFragment;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.FiltrateCupsMap;
import com.hhly.mlottery.util.HotFocusUtils;
import com.hhly.mlottery.util.RxBus;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.websocket.HappySocketClient;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;
import com.nostra13.universalimageloader.utils.L;

import org.java_websocket.drafts.Draft_17;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
    @BindView(R.id.network_exception_reload_btn)
    TextView networkExceptionReloadBtn;
    @BindView(R.id.network_exception_layout)
    LinearLayout networkExceptionLayout;
    @BindView(R.id.swipe_refresh_layout)
    ExactSwipeRefrashLayout swipeRefreshLayout;
    @BindView(R.id.titleContainer)
    PercentRelativeLayout titleContainer;

    //    private BorderDividerItemDecration dataDecration;
    public static EventBus eventBus;
    private ApiHandler apiHandler;
    private RollBallAdapter adapter;
    private HappySocketClient socketClient;
    private LinearLayoutManager layoutManager;
    private Subscription subscription;
    private boolean resestTheLifeCycle;
    private boolean loadingMoreData;
    private boolean websocketConnectionIsError;
    public LeagueCup[] checkedLeagueCup; // 记录筛选过的联赛
    public List<LeagueCup> leagueCupLists; // 全部联赛
    private List<Match> allDataLists; // 所有数据
    private List<Match> feedAdapterLists; // 要展示的数据

    public static RollBallFragment newInstance(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_INDEX, index);
        RollBallFragment fragment = new RollBallFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.football_roll;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        if (null == apiHandler) apiHandler = new ApiHandler(this);
        this.setupEventBus();
        this.setupSwipeRefresh();
        this.setupRecyclerView();
        this.setupAdapter();
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), 40));
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

        networkExceptionReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RollBallFragment.this.initData();
            }
        });

        subscription = RxBus.getDefault().toObserverable(Match.class).delay(60, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Match>() {
            @Override
            public void call(final Match match) {
                synchronized (leagueCupLists) {
                    LeagueCup targetCup = null;
                    for (LeagueCup cup : leagueCupLists) {
                        if (match.getRaceId().equals(cup.getRaceId())) {
                            targetCup = cup;
                            break;
                        }
                    }
                    if (targetCup != null) {
                        if (targetCup.getCount() > 1) {
                            targetCup.setCount(targetCup.getCount() - 1);
                        } else {
                            leagueCupLists.remove(targetCup);
                        }
                    }
                }
                synchronized (checkedLeagueCup) {
                    LeagueCup targetCup = null;
                    for (LeagueCup cup : checkedLeagueCup) {
                        if (match.getRaceId().equals(cup.getRaceId())) {
                            targetCup = cup;
                        }
                    }
                    if (targetCup != null) {
                        if (targetCup.getCount() > 1) {
                            targetCup.setCount(targetCup.getCount() - 1);
                        } else {

                            List<LeagueCup> tempCups = new ArrayList<>();
                            for (LeagueCup acup : leagueCupLists) {
                                for (LeagueCup cup : checkedLeagueCup) {
                                    if (acup.getRaceId().equals(cup.getRaceId())) {
                                        tempCups.add(acup);
                                        break;
                                    }
                                }
                            }
                            checkedLeagueCup = tempCups.toArray(new LeagueCup[]{});
                        }
                    }
                }
                allDataLists.remove(match);
                feedAdapterLists.remove(match);
                RollBallFragment.this.feedAdapter(feedAdapterLists);

                if (feedAdapterLists.size() == 0) {
                    if (allDataLists.size() == feedAdapterLists.size()) {
                        apiHandler.sendEmptyMessage(VIEW_STATUS_NO_ANY_DATA);
                    } else {
                        apiHandler.sendEmptyMessage(VIEW_STATUS_FLITER_NO_DATA);
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                apiHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
            }
        });
    }

    @Override
    protected void initData() {
        this.requestApi();
        this.setupWebSocketClient();
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
        eventBus.unregister(this);
        if (adapter != null && adapter.getSubscription() != null)
            if (adapter.getSubscription().isUnsubscribed()) adapter.getSubscription().unsubscribe();
        if (subscription.isUnsubscribed()) subscription.unsubscribe();
        if (apiHandler != null) apiHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onItemClick(View convertView, int position) {
        // TODO: 点击item跳转入口，点击的当前条目thirdId获取方式 feedAdapterLists.get(position).getThirdId();
    }

    @Override
    public void onItemLongClick(View convertView, int position) {
    }

    @Override
    public void onRefresh() {
        this.initData();
    }

    private synchronized void setupWebSocketClient() {
        if (socketClient == null || (socketClient != null && socketClient.isClosed())) {
            try {
                socketClient = new HappySocketClient(new URI(BaseURLs.WS_SERVICE), new Draft_17(), new HappySocketClient.Callback() {
                    @Override
                    public void onMessage(String message) {
                        if (message.startsWith("CONNECTED")) {
                            socketClient.send("SUBSCRIBE\nid:" + MD5Util.getMD5(
                                    "android" + DeviceInfo.getDeviceId(getActivity())) + "\ndestination:/topic/USER.topic.app\n\n");
                            return;
                        } else if (message.startsWith("MESSAGE")) {
                            String[] msgs = message.split("\n");
                            String ws_json = msgs[msgs.length - 1];
                            String type = "";
                            try {
                                JSONObject jsonObject = new JSONObject(ws_json);
                                type = jsonObject.getString("type");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (!"".equals(type)) {
                                Message msg = Message.obtain();
                                msg.obj = ws_json;
                                L.i("hhlylog", "RollballFragment WebSocket Push Data [ " + ws_json + "]");
                                msg.arg1 = Integer.parseInt(type);
                                apiHandler.sendMessage(msg);
                            }
                        }
                        socketClient.send("\n");
                    }

                    @Override
                    public void onError(Exception exception) {
                        websocketConnectionIsError = true;
                        RollBallFragment.this.reConnectionWebSocket();
                    }

                    @Override
                    public void onClose(String message) {
                        if (!websocketConnectionIsError)
                            RollBallFragment.this.reConnectionWebSocket();
                    }
                });

                socketClient.connect();
            } catch (Exception e) {
                if (socketClient != null) socketClient.close();
                e.printStackTrace();
            }

            if (websocketConnectionIsError) this.initData();
            websocketConnectionIsError = false;
        }
    }

    private void reConnectionWebSocket() {
        Observable.timer(2000, TimeUnit.MILLISECONDS).observeOn(Schedulers.io()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                RollBallFragment.this.setupWebSocketClient();
            }
        });
    }

    private void setupTitleAnimations(View titleView, int translationY, Animator.AnimatorListener animatorListener) {
        titleView.animate().translationY(translationY).setDuration(400).setListener(animatorListener).start();
    }

    private void loadUserList() {
        //		initData();
    }

    private void setupEventBus() {
        if (null == eventBus) {
            eventBus = new EventBus();
            eventBus.register(this);
        }
    }

    private void setupRecyclerView() {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            dataDecration = new BorderDividerItemDecration(
                    getResources().getDimensionPixelOffset(R.dimen.data_border_divider_height),
                    getResources().getDimensionPixelOffset(R.dimen.data_border_padding_infra_spans));
        } else {
            dataDecration = new BorderDividerItemDecration(
                    getResources().getDimensionPixelOffset(R.dimen.data_border_divider_height_half),
                    getResources().getDimensionPixelOffset(R.dimen.data_border_divider_height_half));
        }
        recyclerView.addItemDecoration(dataDecration);*/
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setupAdapter() {
        if (null == adapter) {
            adapter = new RollBallAdapter(getActivity());
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(this);
            adapter.setOnItemLongClickListener(this);
        }
    }

    private void feedAdapter(List<Match> dataLists) {
        checkNotNull(adapter, "adapter == null");

//        if (loadingMoreData) {
//            loadingMoreData = false;
//            adapter.addAll(allDataLists);
//            //			adapter.dismissFooterViewLoading();
//        } else {
        this.checkTheLifeCycleIsChanging(resestTheLifeCycle);
        adapter.setList(dataLists);
//        }
        adapter.notifyDataSetChanged();
    }

    public void feedAdapter() {
        if (apiHandler != null) apiHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        this.initData();
    }

    private void checkTheLifeCycleIsChanging(boolean resestTheLifeCycle) {
        if (resestTheLifeCycle) {
            this.resestTheLifeCycle = false;
//            this.clearDecoration();
            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.addItemDecoration(dataDecration);
        }
    }

    private void clearDecoration() {
//        this.recyclerView.removeItemDecoration(this.dataDecration);
    }


    public void requestApi() {
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_ImmediateMatchs,
                new VolleyContentFast.ResponseSuccessListener<ImmediateMatchs>() {
                    @Override
                    public void onResponse(ImmediateMatchs jsonObject) {
                        if (null == jsonObject || null == jsonObject.getImmediateMatch()) {
                            apiHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
                            return;
                        }
                        allDataLists = jsonObject.getImmediateMatch();
                        leagueCupLists = jsonObject.getAll();
                        feedAdapterLists = new ArrayList<>();

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

                                if (FiltrateCupsMap.immediateCups.length != 0) {// 判断是否已经筛选过
                                    for (Match m : allDataLists) {// 已选择的
                                        for (String checkedId : FiltrateCupsMap.immediateCups) {
                                            if (m.getRaceId().equals(checkedId)) {
                                                feedAdapterLists.add(m);
                                                break;
                                            }
                                        }
                                    }
                                } else {// 没有筛选过
                                    for (Match m : allDataLists) {// 默认显示热门赛程
                                        for (String hotId : hotList) {
                                            if (m.getRaceId().equals(hotId)) {
                                                feedAdapterLists.add(m);
                                                break;
                                            }
                                        }
                                    }
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
                                    if (FiltrateCupsMap.immediateCups.length != 0) {
                                        for (LeagueCup cup : leagueCupLists) {
                                            for (String checkedId : FiltrateCupsMap.immediateCups) {
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
                                RollBallFragment.this.feedAdapter(feedAdapterLists);
                                apiHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
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
    public void onEventMainThread(String currentFragmentId) {
        this.feedAdapter(feedAdapterLists);
        ((ScoresFragment) getParentFragment()).focusCallback();
    }

    /**
     * 筛选返回
     * 接受消息的页面实现
     */
    public void onEventMainThread(Map<String, Object> map) {
        String[] checkedIds = (String[]) ((LinkedList) map.get(FiltrateMatchConfigActivity.RESULT_CHECKED_CUPS_IDS)).toArray(
                new String[]{});
        FiltrateCupsMap.immediateCups = checkedIds;
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
            }
        }

        checkedLeagueCup = leagueCupList.toArray(new LeagueCup[]{});
        this.feedAdapter(feedAdapterLists);

        if (feedAdapterLists.size() == 0) {// 没有比赛
            apiHandler.sendEmptyMessage(VIEW_STATUS_FLITER_NO_DATA);
        } else {
            apiHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
        }
    }

    public List<LeagueCup> getLeagueCupLists() {
        return leagueCupLists;
    }

    public LeagueCup[] getLeagueCupChecked() {
        return checkedLeagueCup;
    }

    private static class ApiHandler extends Handler {
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
                            case SOCKET_TYPE_STATUS:
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
                                        fragment.initData();
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
                        fragment.networkExceptionLayout.setVisibility(View.GONE);
                        fragment.footballImmediateUnfocusLl.setVisibility(View.GONE);
                        break;
                    case VIEW_STATUS_NO_ANY_DATA:
                        fragment.networkExceptionLayout.setVisibility(View.GONE);
                        fragment.footballImmediateUnfocusLl.setVisibility(View.VISIBLE);
                        fragment.titleContainer.setVisibility(View.GONE);
                        break;
                    case VIEW_STATUS_SUCCESS:
                        fragment.titleContainer.setVisibility(View.VISIBLE);
                        fragment.swipeRefreshLayout.setRefreshing(false);
                        fragment.networkExceptionLayout.setVisibility(View.GONE);
                        fragment.footballImmediateUnfocusLl.setVisibility(View.GONE);
                        break;
                    case VIEW_STATUS_NET_ERROR:
                        fragment.titleContainer.setVisibility(View.GONE);
                        fragment.swipeRefreshLayout.setRefreshing(false);
                        fragment.networkExceptionLayout.setVisibility(View.VISIBLE);
                        fragment.footballImmediateUnfocusLl.setVisibility(View.GONE);
                        fragment.adapter.setList(null);
                        fragment.adapter.notifyDataSetChanged();
                        break;
                    case VIEW_STATUS_FLITER_NO_DATA:
                        fragment.swipeRefreshLayout.setRefreshing(false);
                        fragment.networkExceptionLayout.setVisibility(View.GONE);
                        fragment.titleContainer.setVisibility(View.GONE);
                        fragment.footballImmediateUnfocusLl.setVisibility(View.VISIBLE);
                        break;
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
}