package com.hhly.mlottery.frame.footballframe;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.ImmediateAdapter;
import com.hhly.mlottery.bean.Focus;
import com.hhly.mlottery.bean.Match;
import com.hhly.mlottery.bean.MatchOdd;
import com.hhly.mlottery.bean.websocket.WebSocketMatchChange;
import com.hhly.mlottery.bean.websocket.WebSocketMatchEvent;
import com.hhly.mlottery.bean.websocket.WebSocketMatchOdd;
import com.hhly.mlottery.bean.websocket.WebSocketMatchStatus;
import com.hhly.mlottery.callback.FocusMatchClickListener;
import com.hhly.mlottery.callback.RecyclerViewItemClickListener;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.FootBallDetailTypeEnum;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.footballframe.eventbus.ScoresMatchFocusEventBusEntity;
import com.hhly.mlottery.frame.footballframe.eventbus.ScoresMatchSettingEventBusEntity;
import com.hhly.mlottery.frame.scorefrag.FootBallScoreFragment;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.HandMatchId;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.StringUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * @author Tenney
 * @ClassName: FocusFragment
 * @Description: 足球关注
 * @date 2015-10-15 上午9:57:25
 */
public class FocusFragment extends Fragment implements OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "FocusFragment";

    /**
     * 保存足球关注id的key
     */
    public final static String FOCUS_ISD = "focus_ids";

    //    public final static int REQUEST_SET_CODE = 0x42;
    private final int REQUEST_DETAIL_CODE = 0x43;


    private RecyclerView mRecyclerView;


    private TextView mReloadTvBtn;

    //    private LinearLayout mLoadingLayout;
    private LinearLayout mErrorLayout;

    private ExactSwipeRefreshLayout mSwipeRefreshLayout;

    private RelativeLayout mUnFocusLayout; //暂无关注界面

    private Context mContext;

    private ImmediateAdapter mAdapter;

    private List<Match> mAllMatchs;
    private List<Match> mMatchs;
    private View mView;

//    private boolean isLoadedData = false;
//
//    private boolean isWebSocketStart = false;// 不使用websocket时可设置为true

//    private HappySocketClient mSocketClient;

    private int mHandicap = 1;// 盘口 1.亚盘 2.大小球 3.欧赔 4.不显示

    private FocusMatchClickListener mFocusClickListener;// 关注点击事件
    LinearLayoutManager layoutManager;


    private static final int GOAL_COLOR_SENCOND = 15 * 1000;// 15秒
    private static final int GREEN_RED_SENCOND = 5 * 1000;// 5秒

    private int mEntryType; // 标记入口 判断是从哪里进来的 (0:首页入口  1:新导航条入口)

    private Vibrator mVibrator;
    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> mSoundMap = new HashMap<>();

    private LinearLayout titleContainer;
    private TextView handicapName1;
    private TextView handicapName2;

    private static final String FRAGMENT_INDEX = "fragment_index";
    private static final String ENTRY_TYPE = "entryType";
    /**
     * 标志位，标志已经初始化完成
     */
//    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
//    private boolean mHasLoadedOnce;


    private String teamLogoSuff;

    private String teamLogoPre;
    private LinearLayout mLoading;//加载中...


    public static FocusFragment newInstance(String param1, String param2) {
        FocusFragment fragment = new FocusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public static FocusFragment newInstance(int index, int entryType) {

        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_INDEX, index);
        bundle.putInt(ENTRY_TYPE, entryType);
        FocusFragment fragment = new FocusFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
//        setWebSocketUri(BaseURLs.WS_SERVICE);
//        setTopic("USER.topic.app");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEntryType = getArguments().getInt(ENTRY_TYPE);
        }
        EventBus.getDefault().register(this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.d(TAG, "___onCreateView____");


        mContext = getActivity();

        mContext = getActivity();
        mView = inflater.inflate(R.layout.football_focus, container, false);
        initView();
        setListener();
        setStatus(SHOW_STATUS_LOADING);
        initData();
        initMedia();
//        initBroadCase();

        return mView;
    }


    private void initMedia() {
        mVibrator = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        mSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5); //
        mSoundMap.put(1, mSoundPool.load(getActivity(), R.raw.sound1, 1)); //
        mSoundMap.put(2, mSoundPool.load(getActivity(), R.raw.sound2, 1));
        mSoundMap.put(3, mSoundPool.load(getActivity(), R.raw.sound3, 1));
    }

    // 初始化控件
    public void initView() {
        layoutManager = new LinearLayoutManager(getActivity());

        mSwipeRefreshLayout = (ExactSwipeRefreshLayout) mView.findViewById(R.id.football_immediate_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview_focus);

        mRecyclerView.setLayoutManager(layoutManager);

        mReloadTvBtn = (TextView) mView.findViewById(R.id.network_exception_reload_btn);
        mReloadTvBtn.setOnClickListener(this);

        mUnFocusLayout = (RelativeLayout) mView.findViewById(R.id.football_unfocus);
        mLoading = (LinearLayout) mView.findViewById(R.id.football_focus_loading_ll);

//        mLoadingLayout = (LinearLayout) mView.findViewById(R.id.football_immediate_loading_ll);
        mErrorLayout = (LinearLayout) mView.findViewById(R.id.network_exception_layout);


        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);

        titleContainer = (LinearLayout) mView.findViewById(R.id.titleContainer);
        handicapName1 = (TextView) mView.findViewById(R.id.tv_handicap_name1);
        handicapName2 = (TextView) mView.findViewById(R.id.tv_handicap_name2);
    }

    /**
     * 设置监听事件
     */
    private void setListener() {
        mFocusClickListener = new FocusMatchClickListener() {
            @Override
            public void onClick(View view, String third) {
                String focusIds = PreferenceUtil.getString("focus_ids", "");
                L.e(TAG, "focusIds = " + focusIds);
                String[] ids = focusIds.split("[,]");
                boolean isCheck = false;// 检查之前是否被选中
                for (String id : ids) {
                    if (third.equals(id)) {
                        isCheck = true;
                        break;
                    }
                }

                if (!isCheck) {// 插入数据
                    if ("".equals(focusIds)) {
                        String newIds = third;
                        PreferenceUtil.commitString(FOCUS_ISD, newIds);
                    } else {
                        String newIds = focusIds + "," + third;
                        PreferenceUtil.commitString(FOCUS_ISD, newIds);
                    }
                    ((ImageView) view).setImageResource(R.mipmap.football_focus);

                } else {// 删除
                    String[] idArray = focusIds.split("[,]");
                    StringBuffer sb = new StringBuffer();
                    for (String id : idArray) {
                        if (!id.equals(third)) {
                            if ("".equals(sb.toString())) {
                                sb.append(id);
                            } else {
                                sb.append("," + id);
                            }
                        }
                    }
                    PreferenceUtil.commitString(FOCUS_ISD, sb.toString());
                    ((ImageView) view).setImageResource(R.mipmap.football_nomal);

                    request(third);
                }
            }
        };
    }

    /**
     * 设置显示状态
     *
     * @param status
     */
    //显示状态
    private final int SHOW_STATUS_LOADING = 1;//加载中
    private final int SHOW_STATUS_ERROR = 2;//加载失败
    private final int SHOW_STATUS_NO_DATA = 3;//暂无数据
    private final int SHOW_STATUS_SUCCESS = 4;//加载成功
    private final int SHOW_STATUS_REFRESH_ONCLICK = 5;//点击刷新

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
        } else if (status == 9) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(true);
        } else {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mLoading.setVisibility((status == SHOW_STATUS_REFRESH_ONCLICK) ? View.VISIBLE : View.GONE);
        mErrorLayout.setVisibility(status == SHOW_STATUS_ERROR ? View.VISIBLE : View.GONE);
        mUnFocusLayout.setVisibility(status == SHOW_STATUS_NO_DATA ? View.VISIBLE : View.GONE);
        if (mUnFocusLayout.getVisibility() == View.VISIBLE) {
            titleContainer.setVisibility(View.GONE);
        }
    }

    /**
     * 子线程 处理数据加载
     */
    Handler mLoadHandler = new Handler();
    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            initData();
        }
    };

    // 初始化数据
    public void initData() {
        request("");
    }

    /**
     * 点击取消关注或者刷新请求后台
     */
    private void request(String thirdId) {
        if (getActivity() == null) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        String deviceId = AppConstants.deviceToken;
        String userId = "";
        if (AppConstants.register != null && AppConstants.register != null && AppConstants.register.getUser() != null) {
            userId = AppConstants.register.getUser().getUserId();
        }
        params.put("userId", userId);
        params.put("deviceId", deviceId);
        params.put("thirdId", thirdId);

        VolleyContentFast.requestJsonByGet(BaseURLs.FOCUS_FRAGMENT_CONCERN, params, new VolleyContentFast.ResponseSuccessListener<Focus>() {
            @Override
            public synchronized void onResponse(final Focus json) {
                if (getActivity() == null) {
                    return;
                }

                if (json == null) {
                    PreferenceUtil.commitString("focus_ids", "");
//                    ((ScoresFragment) getParentFragment()).focusCallback();
                    if (mEntryType == 0) {
                    } else if (mEntryType == 1) {
                        ((FootBallScoreFragment) getParentFragment()).focusCallback();
                    }
//                    mViewHandler.sendEmptyMessage(VIEW_STATUS_NO_ANY_DATA);
                    setStatus(SHOW_STATUS_NO_DATA);
                    return;
                }
                mAllMatchs = new ArrayList<>();
                mAllMatchs = json.getFocus();

                teamLogoPre = json.getTeamLogoPre();

                teamLogoSuff = json.getTeamLogoSuff();

                mMatchs = new ArrayList<>();

                //把请求回来的列表存入本地
                StringBuffer sb = new StringBuffer("");
                if (mAllMatchs != null && mAllMatchs.size() != 0) {
                    for (Match m : mAllMatchs) {
                        if ("".equals(sb.toString())) {
                            sb.append(m.getThirdId());
                        } else {
                            sb.append("," + m.getThirdId());
                        }
                    }
                }

                PreferenceUtil.commitString(FOCUS_ISD, sb.toString());
//                Log.e("BBB", "存进去时" + sb.toString());
//                ((ScoresFragment) getParentFragment()).focusCallback();
                if (mEntryType == 0) {
                } else if (mEntryType == 1) {
                    ((FootBallScoreFragment) getParentFragment()).focusCallback();
                }

                if (mAllMatchs != null && mAllMatchs.size() != 0) {

                    for (Match m : mAllMatchs) {//
                        mMatchs.add(m);
                    }
                }

                if (mMatchs.size() == 0) {//没有比赛
//                    mViewHandler.sendEmptyMessage(VIEW_STATUS_NO_ANY_DATA);
                    setStatus(SHOW_STATUS_NO_DATA);
                    return;
                }


                if (mAdapter == null) {
                    mAdapter = new ImmediateAdapter(mContext, mMatchs, teamLogoPre, teamLogoSuff);
                    mAdapter.setmFocusMatchClickListener(mFocusClickListener);
                    mAdapter.setmOnItemClickListener(new RecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, String data) {
                            if (HandMatchId.handId(getActivity(), data)) {

                                String thirdId = data;
                                Intent intent = new Intent(getActivity(), FootballMatchDetailActivity.class);
                                intent.putExtra("thirdId", thirdId);
                                intent.putExtra("currentFragmentId", FootBallDetailTypeEnum.FOOT_DETAIL_CHARTBALL);
                                if (getActivity() != null) {
                                    getActivity().startActivityForResult(intent, REQUEST_DETAIL_CODE);
                                }
                            }
                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);
                    L.d("sdfgh", "mAdapter == null");

                } else {
                    updateAdapter();
                    L.d("sdfgh", "else");
                }

                setStatus(SHOW_STATUS_SUCCESS);
                titleContainer.setVisibility(PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false) ? View.GONE : View.VISIBLE);
                setHandicapName();

//                isLoadedData = true;
//                mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
//                startWebsocket();
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
//                mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
                setStatus(SHOW_STATUS_ERROR);
                //isLoadedData = false;
            }
        }, Focus.class);
    }


    Handler mSocketHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            Log.e(TAG, "__handleMessage__");
//            Log.e(TAG, "msg.arg1 = " + msg.arg1);
            if (msg.arg1 == 1) {
                String ws_json = (String) msg.obj;
//                Log.e(TAG, "ws_json = " + ws_json);
                WebSocketMatchStatus webSocketMatchStatus = null;
                try {
                    webSocketMatchStatus = JSON.parseObject(ws_json, WebSocketMatchStatus.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    webSocketMatchStatus = JSON.parseObject(ws_json, WebSocketMatchStatus.class);
                }
                updateListViewItemStatus(webSocketMatchStatus);
            } else if (msg.arg1 == 2) {
                String ws_json = (String) msg.obj;
//                Log.e(TAG, "ws_json = " + ws_json);
                WebSocketMatchOdd webSocketMatchOdd = null;
                try {
                    webSocketMatchOdd = JSON.parseObject(ws_json, WebSocketMatchOdd.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    webSocketMatchOdd = JSON.parseObject(ws_json, WebSocketMatchOdd.class);
                }

//                Log.e(TAG, "----webSocketMatchStatus -----" + webSocketMatchOdd.getThirdId());
                updateListViewItemOdd(webSocketMatchOdd);
            } else if (msg.arg1 == 3) {
                String ws_json = (String) msg.obj;
//                Log.e(TAG, "ws_json = " + ws_json);
                WebSocketMatchEvent webSocketMatchEvent = null;
                try {
                    webSocketMatchEvent = JSON.parseObject(ws_json, WebSocketMatchEvent.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    webSocketMatchEvent = JSON.parseObject(ws_json, WebSocketMatchEvent.class);
                }

                updateListViewItemEvent(webSocketMatchEvent);
            } else if (msg.arg1 == 5) {
                String ws_json = (String) msg.obj;
//                Log.e(TAG, "ws_json = " + ws_json);
                WebSocketMatchChange webSocketMatchChange = null;
                try {
                    webSocketMatchChange = JSON.parseObject(ws_json, WebSocketMatchChange.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    webSocketMatchChange = JSON.parseObject(ws_json, WebSocketMatchChange.class);
                }
                upateListViewItemMatchChange(webSocketMatchChange);
            }
        }
    };

    private void upateListViewItemMatchChange(WebSocketMatchChange webSocketMatchChange) {
        synchronized (mAllMatchs) {
            for (final Match match : mAllMatchs) {

                String language = PreferenceUtil.getString("language", "rCN");

                if (match.getThirdId().equals(webSocketMatchChange.getThirdId())) {
                    if ((webSocketMatchChange.getData().get("region").equals("zh") && language.equals("rCN"))
                            || (webSocketMatchChange.getData().get("region").equals("zh-TW") && language.equals("rTW"))
                            || (webSocketMatchChange.getData().get("region").equals("en") && language.equals("rEN"))
                            || (webSocketMatchChange.getData().get("region").equals("ko") && language.equals("rKO"))
                            || (webSocketMatchChange.getData().get("region").equals("id") && language.equals("rID"))
                            || (webSocketMatchChange.getData().get("region").equals("th") && language.equals("rTH"))
                            || (webSocketMatchChange.getData().get("region").equals("vi") && language.equals("rVI"))
                            ) {
                        if (webSocketMatchChange.getData().get("hometeam") != null) {
                            match.setHometeam(webSocketMatchChange.getData().get("hometeam"));
                        }

                        if (webSocketMatchChange.getData().get("hometeam") != null) {
                            match.setGuestteam(webSocketMatchChange.getData().get("guestteam"));
                        }

                        if (webSocketMatchChange.getData().get("racename") != null) {
                            match.setRacename(webSocketMatchChange.getData().get("racename"));
                        }
                        updateListView(match);
                        break;
                    }
                }
            }
        }
    }

    private void updateListViewItemEvent(WebSocketMatchEvent webSocketMatchEvent) {

        for (final Match match : mAllMatchs) {
            if (match.getThirdId().equals(webSocketMatchEvent.getThirdId())) {// 查找对应的比赛对象
                match.setItemBackGroundColorId(R.color.item_football_event_yellow);
                String eventType = webSocketMatchEvent.getData().get("eventType");
                if ("1".equals(eventType) || "2".equals(eventType) || "5".equals(eventType) || "6".equals(eventType)) {// 主队有效进球传
                    // 1or主队进球取消传2
                    // 客队有效进球5or客队进球取消6
                    if (webSocketMatchEvent.getData().get("homeScore") != null) {
                        match.setHomeScore(webSocketMatchEvent.getData().get("homeScore"));
                    }

                    if (webSocketMatchEvent.getData().get("guestScore") != null) {
                        match.setGuestScore(webSocketMatchEvent.getData().get("guestScore"));
                    }
                } else if ("3".equals(eventType) || "4".equals(eventType) || "7".equals(eventType) || "8".equals(eventType)) {// 主队红牌3or主队红牌取消4

                    if (webSocketMatchEvent.getData().get("home_rc") != null) {
                        match.setHome_rc(webSocketMatchEvent.getData().get("home_rc"));
                    }

                    if (webSocketMatchEvent.getData().get("guest_rc") != null) {
                        match.setGuest_rc(webSocketMatchEvent.getData().get("guest_rc"));
                    }

                    if (webSocketMatchEvent.getData().get("home_yc") != null) {
                        match.setHome_yc(webSocketMatchEvent.getData().get("home_yc"));
                    }

                    if (webSocketMatchEvent.getData().get("guest_yc") != null) {
                        match.setGuest_yc(webSocketMatchEvent.getData().get("guest_yc"));
                    }

                }

                if ("1".equals(eventType) || "2".equals(eventType) || "3".equals(eventType) || "4".equals(eventType)) {// 主场红名
                    match.setHomeTeamTextColorId(R.color.red);
                } else if ("5".equals(eventType) || "6".equals(eventType) || "7".equals(eventType) || "8".equals(eventType)) {// 客场红名
                    match.setGuestTeamTextColorId(R.color.red);
                }

                updateListView(match);

                new Handler().postDelayed(new Runnable() {// 五秒后把颜色修改回来
                    @Override
                    public void run() {
                        match.setItemBackGroundColorId(R.color.white);
                        match.setHomeTeamTextColorId(R.color.msg);
                        match.setGuestTeamTextColorId(R.color.msg);
                        updateListView(match);
                    }
                }, GOAL_COLOR_SENCOND);

                // 下面是触发系统声音，注意只有在页面出现的比赛才提示

                boolean isShow = false;

                for (Match showMatch : mMatchs) {
                    if (showMatch.getThirdId().equals(match.getThirdId())) {
                        isShow = true;
                    }
                }

                if (!isShow) {// 不属于就不做下面操作
                    break;
                }

                boolean isSetFocus = PreferenceUtil.getBoolean(MyConstants.GAMEATTENTION, true);// 设置中的关注选项
                String fucus_id = PreferenceUtil.getString(FocusFragment.FOCUS_ISD, "");
                boolean isFocus = false;// 判断该球赛是否属于关注的

                if (isSetFocus) {
                    if ("".equals(fucus_id) || ",".equals(fucus_id)) {
                        isFocus = false;
                    } else {
                        String[] ids = fucus_id.split("[,]");
                        for (String id : ids) {
                            if (match.getThirdId().equals(id)) {
                                isFocus = true;
                                break;
                            }
                        }
                    }
                }

                if ((isSetFocus && isFocus) || (!isSetFocus)) { //

                    boolean isCheckedGoal = PreferenceUtil.getBoolean(MyConstants.GOAL, true);//进球
                    boolean isCheckedRed = PreferenceUtil.getBoolean(MyConstants.RED_CARD, true); //红牌
                    boolean isCheckedShake = PreferenceUtil.getBoolean(MyConstants.SHAKE, true);//震动
                    boolean isCheckedSound = PreferenceUtil.getBoolean(MyConstants.SOUND, true); //声音

                    if ("1".equals(eventType) || "2".equals(eventType)) { //主队进球或者取消进球

                        if (isCheckedGoal && isCheckedShake) { //选中进球跟震动。则震动
                            mVibrator.vibrate(1000);
                        }
                        if (isCheckedGoal && isCheckedSound) {
                            mSoundPool.play(mSoundMap.get(1), 1, 1, 0, 0, 1); //主队进球第一个声音
                        }


                    } else if ("5".equals(eventType) || "6".equals(eventType)) { //客队进球或者取消进球
                        if (isCheckedGoal && isCheckedShake) { //选中进球跟震动。则震动
                            mVibrator.vibrate(1000);
                        }
                        if (isCheckedGoal && isCheckedSound) {
                            mSoundPool.play(mSoundMap.get(2), 1, 1, 0, 0, 1); //客队进球第2个声音
                        }


                    } else if ("3".equals(eventType) || "4".equals(eventType) || "7".equals(eventType) || "8".equals(eventType)) { //主队红牌或者客队红牌

                        if (isCheckedRed && isCheckedShake) { //选中红牌跟震动。则震动
                            mVibrator.vibrate(1000);
                        }
                        if (isCheckedRed && isCheckedSound) {
                            mSoundPool.play(mSoundMap.get(3), 1, 1, 0, 0, 1); //红牌使用第三个声音
                        }
                    }
                }
                break;
            }
        }
        // }
    }

    private void updateListViewItemOdd(WebSocketMatchOdd webSocketMatchOdd) {
        List<Map<String, String>> data = webSocketMatchOdd.getData();
        synchronized (mAllMatchs) {
            for (Match match : mAllMatchs) {// all里面的match
                if (match.getThirdId().equals(webSocketMatchOdd.getThirdId())) {
                    updateMatchOdd(match, data);
                    updateListView(match);
                    break;
                }
            }
        }

    }

    private void updateListViewItemStatus(WebSocketMatchStatus webSocketMatchStatus) {
        Map<String, String> data = webSocketMatchStatus.getData();
        synchronized (mAllMatchs) {

            for (Match match : mAllMatchs) {
                if (match.getThirdId().equals(webSocketMatchStatus.getThirdId())) {
                    updateMatchStatus(match, data);// 会修改mMatch里面的引用
                    updateListView(match);
                    // target[0] = match;
                    break;
                }
            }
        }
    }

    private void updateMatchStatus(Match match, Map<String, String> data) {
        // Log.d(TAG, msg);
        match.setStatusOrigin(data.get("statusOrigin"));
        if ("0".equals(match.getStatusOrigin()) && "1".equals(match.getStatusOrigin())) {// 未开场变成开场
            match.setHomeScore("0");
            match.setGuestScore("0");
        }
        if (data.get("keepTime") != null) {
            match.setKeepTime(data.get("keepTime"));
        }
        if (data.get("home_yc") != null) {
            match.setHome_yc(data.get("home_yc"));
        }
        if (data.get("guest_yc") != null) {
            match.setGuest_yc(data.get("guest_yc"));
        }

        if (data.get("homeHalfScore") != null) {
            match.setHomeHalfScore(data.get("homeHalfScore"));
        }

        if (data.get("guestHalfScore") != null) {
            match.setGuestHalfScore(data.get("guestHalfScore"));
        }
    }

    private void updateMatchOdd(Match match, List<Map<String, String>> datas) {
        for (Map<String, String> map : datas) {
            if (map.get("handicap").equals("asiaLet")) {
                Map<String, MatchOdd> odds = match.getMatchOdds();
                MatchOdd odd = odds.get("asiaLet");

                if (odd == null) {//
                    odd = new MatchOdd();
                    odd.setHandicap("asiaLet");
                    odd.setHandicapValue(map.get("mediumOdds"));
                    odd.setRightOdds(map.get("rightOdds"));
                    odd.setLeftOdds(map.get("leftOdds"));
                    odds.put("asiaLet", odd);
                }

                if (mHandicap == 1) {
                    changeOddTextColor(match, odd.getLeftOdds(), odd.getRightOdds(), map.get("leftOdds"), map.get("rightOdds"), odd.getHandicapValue(), map.get("mediumOdds"));
                }
                odd.setLeftOdds(map.get("leftOdds"));
                odd.setRightOdds(map.get("rightOdds"));
                odd.setHandicapValue(map.get("mediumOdds"));

            } else if (map.get("handicap").equals("euro")) {
                Map<String, MatchOdd> odds = match.getMatchOdds();
                MatchOdd odd = odds.get("euro");
                if (odd == null) {//
                    odd = new MatchOdd();
                    odd.setHandicap("euro");
                    odd.setMediumOdds(map.get("mediumOdds"));
                    odd.setRightOdds(map.get("rightOdds"));
                    odd.setLeftOdds(map.get("leftOdds"));
                    odds.put("euro", odd);
                }
                if (mHandicap == 2) {
                    changeOddTextColor(match, odd.getLeftOdds(), odd.getRightOdds(), map.get("leftOdds"), map.get("rightOdds"), odd.getMediumOdds(), map.get("mediumOdds"));
                }
                odd.setLeftOdds(map.get("leftOdds"));
                odd.setRightOdds(map.get("rightOdds"));
                odd.setMediumOdds(map.get("mediumOdds"));

            } else if (map.get("handicap").equals("asiaSize")) {
                Map<String, MatchOdd> odds = match.getMatchOdds();
                MatchOdd odd = odds.get("asiaSize");
                if (odd == null) {//
                    odd = new MatchOdd();
                    odd.setHandicap("asiaSize");
                    odd.setHandicapValue(map.get("mediumOdds"));
                    odd.setRightOdds(map.get("rightOdds"));
                    odd.setLeftOdds(map.get("leftOdds"));
                    odds.put("asiaSize", odd);
                }
                if (mHandicap == 3) {
                    changeOddTextColor(match, odd.getLeftOdds(), odd.getRightOdds(), map.get("leftOdds"), map.get("rightOdds"), odd.getHandicapValue(), map.get("mediumOdds"));
                }
                odd.setLeftOdds(map.get("leftOdds"));
                odd.setRightOdds(map.get("rightOdds"));
                odd.setHandicapValue(map.get("mediumOdds"));

            }
        }
    }

    private Map<String, Integer> singleMatchChangeOddColorCount = new HashMap<String, Integer>();

    /**
     * 修改match的赔率的颜色，是一个引用，包括mAllMatch和mMatch
     *
     * @param match
     * @param leftMatchOdd
     * @param rightMatchOdd
     * @param leftMapOdd
     * @param rightMapOdd
     */
    public void changeOddTextColor(final Match match, String leftMatchOdd, String rightMatchOdd, String leftMapOdd, String rightMapOdd, String midMatchOdd, String midMapOdd) {
        // 先设置默认颜色，因为一开始可能没值
        resetOddColor(match);

        try {
            int keepTime = Integer.parseInt(match.getKeepTime());
            if (keepTime >= 89) {// 显示封盘，不需要修改颜色
                return;
            }
        } catch (Exception e) {
        }

        if ("-".equals(leftMatchOdd) || "-".equals(rightMatchOdd) || "-".equals(leftMapOdd) || "-".equals(rightMapOdd) || "-".equals(midMatchOdd) || "-".equals(midMapOdd) || "|".equals(midMatchOdd)
                || "|".equals(midMapOdd) || StringUtils.isEmpty(leftMatchOdd) || StringUtils.isEmpty(rightMatchOdd) || StringUtils.isEmpty(leftMapOdd) || StringUtils.isEmpty(rightMapOdd)
                || StringUtils.isEmpty(midMatchOdd) || StringUtils.isEmpty(midMapOdd)) {
            return;
        }

        try {
            float leftMatchOddF = Float.parseFloat(leftMatchOdd);
            float rightMatchOddF = Float.parseFloat(rightMatchOdd);
            float leftMapOddF = Float.parseFloat(leftMapOdd);
            float rightMapOddF = Float.parseFloat(rightMapOdd);
            float midMatchOddF = Float.parseFloat(midMatchOdd);
            float midMapOddF = Float.parseFloat(midMapOdd);

            if (leftMatchOddF < leftMapOddF) {// 左边的值升了
                match.setLeftOddTextColorId(R.color.odd_rise_red);
            } else if (leftMatchOddF > leftMapOddF) {// 左边的值降了
                match.setLeftOddTextColorId(R.color.odd_drop_green);
            } else {
                match.setLeftOddTextColorId(R.color.content_txt_light_grad);
            }

            if (rightMatchOddF < rightMapOddF) {// 右边的值升了
                match.setRightOddTextColorId(R.color.odd_rise_red);
            } else if (rightMatchOddF > rightMapOddF) {// 右边的值降了
                match.setRightOddTextColorId(R.color.odd_drop_green);
            } else {
                match.setRightOddTextColorId(R.color.content_txt_light_grad);
            }

            if (midMatchOddF < midMapOddF) {// 中间的值升了
                match.setMidOddTextColorId(R.color.odd_rise_red);
            } else if (midMatchOddF > midMapOddF) {// 中间的值降了
                match.setMidOddTextColorId(R.color.odd_drop_green);
            } else {
                if (mHandicap == 1 || mHandicap == 2) {// 亚盘，大小球是黑色
                    match.setMidOddTextColorId(R.color.content_txt_black);
                } else {
                    match.setMidOddTextColorId(R.color.content_txt_light_grad);
                }
            }

        } catch (Exception e) {

        }

        int count = 0;

        if (singleMatchChangeOddColorCount.get(match.getThirdId()) != null) {
            count = singleMatchChangeOddColorCount.get(match.getThirdId());
        }

        singleMatchChangeOddColorCount.put(match.getThirdId(), ++count);

        new Handler().postDelayed(new Runnable() {// 五秒后把颜色修改回来
            @Override
            public void run() {
                int count = singleMatchChangeOddColorCount.get(match.getThirdId());
                count--;
                singleMatchChangeOddColorCount.put(match.getThirdId(), count);
                if (count == 0) {
                    resetOddColor(match);
                    updateListView(match);
                }
            }
        }, GREEN_RED_SENCOND);

    }

    private void resetOddColor(Match match) {
        match.setLeftOddTextColorId(R.color.content_txt_light_grad);
        match.setRightOddTextColorId(R.color.content_txt_light_grad);
        if (mHandicap == 1 || mHandicap == 2) {// 亚盘，大小球是黑色
            match.setMidOddTextColorId(R.color.content_txt_black);
        } else {
            match.setMidOddTextColorId(R.color.content_txt_light_grad);
        }
    }

    private void updateListView(Match targetMatch) {
        updateAdapter();
    }

    public void updateAdapter() {

        if (mAdapter == null) {
            return;
        }
        mAdapter.updateDatas(mMatchs);
        mAdapter.notifyDataSetChanged();

    }

    public void reLoadData() {
//        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
//        setStatus(SHOW_STATUS_LOADING);
//        setStatus(9);
        mSwipeRefreshLayout.setRefreshing(true);
        mLoadHandler.post(mRun);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.network_exception_reload_btn:
                // mLoadingLayout.setVisibility(View.VISIBLE);
                // mErrorLayout.setVisibility(View.GONE);
                // mListView.setVisibility(View.GONE);
                // mSwipeRefreshLayout.setVisibility(View.GONE);

//                mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
                setStatus(SHOW_STATUS_REFRESH_ONCLICK);
                initData();
                break;
            case R.layout.item_header_unconection_view:
                startActivity(new Intent(Settings.ACTION_SETTINGS));
                break;
//            case R.id.to_football_focus_ll:
//                Intent intent=new Intent(getActivity(),FootballActivity.class);
//                startActivity(intent);
            default:
                break;

        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
////        isPause = false;
//        L.v(TAG, "___onResume___");
//        connectWebSocket();
//
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d(TAG, "__onDestroy___");
    }

//    @Override
//    protected void onTextResult(String text) {
//        L.e("Focus", "tuisong");
//        if (mAdapter == null) {
//            return;
//        }
//
//        String type = "";
//        try {
//            JSONObject jsonObject = new JSONObject(text);
//            type = jsonObject.getString("type");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        if (!"".equals(type)) {
//            Message msg = Message.obtain();
//            msg.obj = text;
//            msg.arg1 = Integer.parseInt(type);
//
//            mSocketHandler.sendMessage(msg);
//        }
//    }

//    @Override
//    protected void onConnectFail() {
//
//    }
//
//    @Override
//    protected void onDisconnected() {
//
//    }
//
//    @Override
//    protected void onConnected() {
//
//    }


    /**
     * 设置界面设置欧赔亚盘大小球的显示。这边接受。关注页面分离出来不需要改
     */

    public void onEventMainThread(ScoresMatchSettingEventBusEntity scoresMatchSettingEventBusEntity) {
        if (PreferenceUtil.getBoolean(MyConstants.RBSECOND, true)) {
            mHandicap = 1;
        } else if (PreferenceUtil.getBoolean(MyConstants.rbSizeBall, false)) {
            mHandicap = 2;// 大小球
        } else if (PreferenceUtil.getBoolean(MyConstants.RBOCOMPENSATE, false)) {
            mHandicap = 3;
        } else if (PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false)) {
            mHandicap = 4;
        }
        if (mMatchs == null) {
            return;
        }
        synchronized (mMatchs) {
            for (Match match : mMatchs) {
                resetOddColor(match);
            }
            titleContainer.setVisibility(PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false) ? View.GONE : View.VISIBLE);
            setHandicapName();
            updateAdapter();
        }
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

    /**
     * EventBus 赛场比赛详情返回FootballMatchDetailActivity.关注页面分离出来不需要改
     * 接受消息的页面实现
     * 未关注页面去关注后返回也是此处接收
     * <p>
     * ps:改回原关注页面，需要判断更新（2017-3-7） 1.2.5国际化版本
     */

    public void onEventMainThread(ScoresMatchFocusEventBusEntity scoresMatchFocusEventBusEntity) {
//            mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
//            initData();
        if (scoresMatchFocusEventBusEntity.getFgIndex() == 4) {
            if ("".equals(PreferenceUtil.getString(FocusFragment.FOCUS_ISD, ""))) {
                if (mMatchs != null) {
                    mMatchs.clear();
                }

                if (mAllMatchs != null) {
                    mAllMatchs.clear();
                }
                updateAdapter();
            }

            if (mEntryType == 0) {
            } else if (mEntryType == 1) {
                ((FootBallScoreFragment) getParentFragment()).focusCallback();
            }

//            mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
            setStatus(SHOW_STATUS_LOADING);
            initData();
        }
    }


    @Override
    public void onRefresh() {
        L.d("sdfgh", "onRefresh");
        setStatus(SHOW_STATUS_LOADING);
        initData();
//        connectWebSocket();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        isPause = true;
    }

    //推送
    public void onEventMainThread(FootBallScoreFragment.FootballScoresWebSocketEntity entity) {
        L.e("Focus", "tuisong");
        if (mAdapter == null) {
            return;
        }

        String type = "";
        try {
            JSONObject jsonObject = new JSONObject(entity.text);
            type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!"".equals(type)) {
            Message msg = Message.obtain();
            msg.obj = entity.text;
            msg.arg1 = Integer.parseInt(type);

            mSocketHandler.sendMessage(msg);
        }
    }
}
