package com.hhly.mlottery.frame.footframe;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivityTest;
import com.hhly.mlottery.adapter.ImmediateAdapter;
import com.hhly.mlottery.adapter.ImmediateInternationalAdapter;
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
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.ScoresFragment;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.StringUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;

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
 * @Description: 关注
 * @date 2015-10-15 上午9:57:25
 */
public class FocusFragment extends Fragment implements OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "FocusFragment";

    public final static String FOCUS_ISD = "focus_ids";

    public final static int REQUEST_SET_CODE = 0x42;
    public final static int REQUEST_DETAIL_CODE = 0x43;

    //private SlideDeleteListView mListView;

    private RecyclerView mRecyclerView;


    private TextView mReloadTvBtn;

    private LinearLayout mLoadingLayout;
    private LinearLayout mErrorLayout;

    //    private LinearLayout mUnconectionLayout;
    private ExactSwipeRefrashLayout mSwipeRefreshLayout;

    private View mUnFocusLayout;

    private Context mContext;

    private ImmediateAdapter mAdapter;
    private ImmediateInternationalAdapter mInternationalAdapter;

    private List<Match> mAllMatchs;
    private List<Match> mMatchs;
    private View mView;

    private boolean isLoadedData = false;

    private boolean isWebSocketStart = false;// 不使用websocket时可设置为true

//    private HappySocketClient mSocketClient;

    private int mHandicap = 1;// 盘口 1.亚盘 2.大小球 3.欧赔 4.不显示

    private FocusMatchClickListener mFocusClickListener;// 关注点击事件
    LinearLayoutManager layoutManager;


    private static final int GOAL_COLOR_SENCOND = 15 * 1000;// 15秒
    private static final int GREEN_RED_SENCOND = 5 * 1000;// 5秒

    /**
     * 关注事件EventBus
     */
    public static EventBus focusEventBus;


    Handler mLoadHandler = new Handler();
    private Runnable mLoadingDataThread = new Runnable() {
        @Override
        public void run() {
            // 在这里数据内容加载到Fragment上
            initData();
        }
    };

    private Vibrator mVibrator;
    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> mSoundMap = new HashMap<Integer, Integer>();


    private static final String FRAGMENT_INDEX = "fragment_index";
    private final int FIRST_FRAGMENT = 0;
    private final int SECOND_FRAGMENT = 1;
    private final int THIRD_FRAGMENT = 2;
    private final int FOUR_FRAGMENT = 2;

    private TextView mFragmentView;

    private int mCurIndex = -1;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;


    private String teamLogoSuff;

    private String teamLogoPre;


    public static FocusFragment newInstance(String param1, String param2) {
        FocusFragment fragment = new FocusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public static FocusFragment newInstance(int index) {

        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_INDEX, index);
        FocusFragment fragment = new FocusFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        try {
//            socketUri = new URI(BaseURLs.WS_SERVICE);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }

        focusEventBus = new EventBus();
        focusEventBus.register(this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "___onCreateView____");


        mContext = getActivity();
       /* if (mView == null) {
            mView = inflater.inflate(R.layout.football_focus, container, false);
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
        }*/
        mContext = getActivity();
        mView = inflater.inflate(R.layout.football_focus, container, false);
        initView();

        initMedia();
//        initBroadCase();

        return mView;
    }


   /* protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }

      *//*  if (!isLoadedData) {
*//*

            mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
            mLoadHandler.postDelayed(mLoadingDataThread, 0);
     *//*   } else {

        }*//*

    }
*/

//    private FocusNetStateReceiver mNetStateReceiver;

//    private void initBroadCase() {
//        if (getActivity() != null) {
//            mNetStateReceiver = new FocusNetStateReceiver();
//            IntentFilter filter = new IntentFilter();
//            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//            getActivity().registerReceiver(mNetStateReceiver, filter);
//        }
//
//    }

    private void initMedia() {
        mVibrator = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        mSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5); //
        mSoundMap.put(1, mSoundPool.load(getActivity(), R.raw.sound1, 1)); //
        mSoundMap.put(2, mSoundPool.load(getActivity(), R.raw.sound2, 1));
        mSoundMap.put(3, mSoundPool.load(getActivity(), R.raw.sound3, 1));
    }

    // 初始化控件
    public void initView() {
        // 加载动画
//        mLoadingImg = (ImageView) mView.findViewById(R.id.iv_loading_img);
//        mLoadingAnimation = AnimationUtils.loadAnimation(mContext, R.anim.cirle);
//        mLoadingAnimation.setInterpolator(new LinearInterpolator());
//        mLoadingImg.startAnimation(mLoadingAnimation);
        layoutManager = new LinearLayoutManager(getActivity());

        mSwipeRefreshLayout = (ExactSwipeRefrashLayout) mView.findViewById(R.id.football_immediate_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));


        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview_focus);

        mRecyclerView.setLayoutManager(layoutManager);


        /**需要修改**/
      /*  ImageView unfocusImage = (ImageView) mView.findViewById(R.id.football_immediate_unfocus_coffee);
        TextView nodataTv = (TextView) mView.findViewById(R.id.football_immediate_no_data_tv);
        nodataTv.setText(R.string.unfocus2);
        if (AppConstants.isGOKeyboard) {
            unfocusImage.setImageResource(R.mipmap.inter_unfocus);
        } else {
            unfocusImage.setImageResource(R.mipmap.unfocus);
        }*/

        mReloadTvBtn = (TextView) mView.findViewById(R.id.network_exception_reload_btn);
        mReloadTvBtn.setOnClickListener(this);

        mUnFocusLayout = mView.findViewById(R.id.football_immediate_unfocus_ll);

//        mUnconectionLayout = (LinearLayout) mView.findViewById(R.id.unconection_layout);
//        mUnconectionLayout.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Settings.ACTION_SETTINGS));
//            }
//        });

        mLoadingLayout = (LinearLayout) mView.findViewById(R.id.football_immediate_loading_ll);
        mErrorLayout = (LinearLayout) mView.findViewById(R.id.network_exception_layout);

/*


*/

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
                        PreferenceUtil.commitString("focus_ids", newIds);
                    } else {
                        String newIds = focusIds + "," + third;
                        PreferenceUtil.commitString("focus_ids", newIds);
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
                    PreferenceUtil.commitString("focus_ids", sb.toString());
                    ((ImageView) view).setImageResource(R.mipmap.football_nomal);

                    List<Match> allList = new ArrayList<Match>();
                    for (Match m : mAllMatchs) {
                        if (!m.getThirdId().equals(third)) {
                            allList.add(m);
                        }
                    }

                    mAllMatchs = allList;
                    mMatchs = allList;
                    updateAdapter();
                    // mListView.slideBack();

                    if (mMatchs.size() == 0) {
                        mViewHandler.sendEmptyMessage(VIEW_STATUS_NO_ANY_DATA);
                    }

                    ((ScoresFragment) getParentFragment()).focusCallback();
                }
            }
        };

    }

    private final static int VIEW_STATUS_LOADING = 1;
    private final static int VIEW_STATUS_NO_ANY_DATA = 2;
    private final static int VIEW_STATUS_SUCCESS = 3;
    private final static int VIEW_STATUS_NET_ERROR = 4;
//    private final static int VIEW_STATUS_WEBSOCKET_CONNECT_SUCCESS = 6;
//    private final static int VIEW_STATUS_WEBSOCKET_CONNECT_FAIL = 7;

    private Handler mViewHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VIEW_STATUS_LOADING:
                    //mLoadingLayout.setVisibility(View.VISIBLE);
                    mErrorLayout.setVisibility(View.GONE);
//                    mListView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    if (!isLoadedData) {
                        mLoadingLayout.setVisibility(View.VISIBLE);
                    }
                    break;
                case VIEW_STATUS_NO_ANY_DATA:
                    mLoadingLayout.setVisibility(View.GONE);
//                    mListView.setVisibility(View.GONE);
                    mErrorLayout.setVisibility(View.GONE);
                    //如果不隐藏mSwipeRefreshLayout，会出现没关注的页面和一条比赛。
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    //mUnconectionLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mUnFocusLayout.setVisibility(View.VISIBLE);
                    break;
                case VIEW_STATUS_SUCCESS:
                    mLoadingLayout.setVisibility(View.GONE);
//                    mListView.setVisibility(View.VISIBLE);
                    mErrorLayout.setVisibility(View.GONE);
//                    mUnconectionLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mUnFocusLayout.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_NET_ERROR:
                    mLoadingLayout.setVisibility(View.GONE);
//                    mListView.setVisibility(View.GONE);
//                    mUnconectionLayout.setVisibility(View.GONE);

                    mSwipeRefreshLayout.setRefreshing(false);

                    if (isLoadedData) {
//                        if (!isPause && getActivity() != null && !isError) {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), R.string.exp_net_status_txt, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //mSwipeRefreshLayout.setVisibility(View.GONE);
                        mLoadingLayout.setVisibility(View.GONE);
                        mErrorLayout.setVisibility(View.VISIBLE);
                        mUnFocusLayout.setVisibility(View.GONE);
                    }
                    break;
//                case VIEW_STATUS_WEBSOCKET_CONNECT_FAIL:
//                    if (mUnconectionLayout != null) {
//                        mUnconectionLayout.setVisibility(View.VISIBLE);
//                    }
//                    break;
//                case VIEW_STATUS_WEBSOCKET_CONNECT_SUCCESS:
//                    if (mUnconectionLayout != null) {
//                        mUnconectionLayout.setVisibility(View.GONE);
//                    }
//                    break;
                default:
                    break;
            }
        }
    };

    // 初始化数据
    public void initData() {

        if (getActivity() == null) {
            return;
        }
        String fucus_id = PreferenceUtil.getString(FOCUS_ISD, "");
        Map<String, String> params = new HashMap<String, String>();
        params.put("focusString", fucus_id);

//        isInitData = true;

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FocusMatchs, params, new VolleyContentFast.ResponseSuccessListener<Focus>() {
            @Override
            public synchronized void onResponse(final Focus json) {
                if (getActivity() == null) {
                    return;
                }

                if (json == null) {
                    PreferenceUtil.commitString("focus_ids", "");
                    ((ScoresFragment) getParentFragment()).focusCallback();
                    mViewHandler.sendEmptyMessage(VIEW_STATUS_NO_ANY_DATA);
                    return;
                }

                mAllMatchs = json.getFocus();

                teamLogoPre = json.getTeamLogoPre();

                teamLogoSuff = json.getTeamLogoSuff();

                mMatchs = new ArrayList<Match>();

                // mMatchs.addAll(mAllMatchs);//用这种方式是把all的引用赋给它了，操作起来比较麻烦


                //如果在服务器上请求回来的数据没有这条赛事，则删除。
                String ids = PreferenceUtil.getString("focus_ids", "");
                String[] arrayIds = ids.split(",");
                StringBuffer sb = new StringBuffer("");

                for (String id : arrayIds) {
                    boolean isExist = false;
                    for (Match m : mAllMatchs) {
                        if (m.getThirdId().equals(id)) {
                            isExist = true;
                            break;
                        }
                    }

                    if (isExist) {
                        if (sb.toString().equals("")) {
                            sb.append(id);
                        } else {
                            sb.append("," + id);
                        }
                    }
                }
                PreferenceUtil.commitString("focus_ids", sb.toString());
                ((ScoresFragment) getParentFragment()).focusCallback();

              /*  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (Match m : mAllMatchs) {//
                            mMatchs.add(m);
                        }

                        if (mMatchs.size() == 0) {//没有比赛
                            mViewHandler.sendEmptyMessage(VIEW_STATUS_NO_ANY_DATA);
                            return;
                        }


                        if (AppConstants.isGOKeyboard) {
                           *//* mInternationalAdapter = new ImmediateInternationalAdapter(mContext, mMatchs, R.layout.item_football_international);
                            // mInternationalAdapter.setFocusClickListener(mFocusClickListener);
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    mRecyclerView.setAdapter(mInternationalAdapter);
                                }
                            });*//*
                        } else {
                            mAdapter = new ImmediateAdapter(mContext, mMatchs);
                            mRecyclerView.setLayoutManager(layoutManager);
                            mAdapter.setmFocusMatchClickListener(mFocusClickListener);

                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    mRecyclerView.setAdapter(mAdapter);
                                }
                            });
                        }

                    }
                }).start();*/


                for (Match m : mAllMatchs) {//
                    mMatchs.add(m);
                }

                if (mMatchs.size() == 0) {//没有比赛
                    mViewHandler.sendEmptyMessage(VIEW_STATUS_NO_ANY_DATA);
                    return;
                }


                if (mAdapter == null) {
                    mAdapter = new ImmediateAdapter(mContext, mMatchs, teamLogoPre, teamLogoSuff);
                    mAdapter.setmFocusMatchClickListener(mFocusClickListener);
                    mAdapter.setmOnItemClickListener(new RecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, String data) {
                            String thirdId = data;
                            Intent intent = new Intent(getActivity(), FootballMatchDetailActivityTest.class);
                            intent.putExtra("thirdId", thirdId);
                            intent.putExtra("currentFragmentId", 3);

                            getParentFragment().startActivityForResult(intent, REQUEST_DETAIL_CODE);
                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    updateAdapter();
                }


                isLoadedData = true;
                mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {


                mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);

                //isLoadedData = false;
            }
        }, Focus.class);

//

    }

//    private URI socketUri = null;

//    private synchronized void startWebsocket() {
//
//
//        if (mSocketClient == null || (mSocketClient != null && mSocketClient.isClosed())) {
//            mSocketClient = new HappySocketClient(socketUri, new Draft_17());
//            mSocketClient.setSocketResponseMessageListener(this);
//            mSocketClient.setSocketResponseCloseListener(this);
//            mSocketClient.setSocketResponseErrorListener(this);
//            mSocketClient.connect();
//            if (isError) {
//                initData();
//            }
//            isError = false;
//        }
//    }

    Handler mSocketHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e(TAG, "__handleMessage__");
            Log.e(TAG, "msg.arg1 = " + msg.arg1);
            if (msg.arg1 == 1) {
                String ws_json = (String) msg.obj;
                Log.e(TAG, "ws_json = " + ws_json);
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
                Log.e(TAG, "ws_json = " + ws_json);
                WebSocketMatchOdd webSocketMatchOdd = null;
                try {
                    webSocketMatchOdd = JSON.parseObject(ws_json, WebSocketMatchOdd.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    webSocketMatchOdd = JSON.parseObject(ws_json, WebSocketMatchOdd.class);
                }

                Log.e(TAG, "----webSocketMatchStatus -----" + webSocketMatchOdd.getThirdId());
                updateListViewItemOdd(webSocketMatchOdd);
            } else if (msg.arg1 == 3) {
                String ws_json = (String) msg.obj;
                Log.e(TAG, "ws_json = " + ws_json);
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
                Log.e(TAG, "ws_json = " + ws_json);
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

                if ((isSetFocus && isFocus) || (!isSetFocus)) {//

                    if ("1".equals(eventType) || "2".equals(eventType)) {
                        int soundId = PreferenceUtil.getInt(MyConstants.HOSTTEAMINDEX, 1);
                        if (soundId != 4) {
                            mSoundPool.play(mSoundMap.get(soundId), 1, 1, 0, 0, 1);
                        }
                        boolean isShake = PreferenceUtil.getBoolean(MyConstants.VIBRATEGOALHINT, true);
                        if (isShake) {
                            mVibrator.vibrate(1000);
                        }

                    } else if ("5".equals(eventType) || "6".equals(eventType)) {
                        int soundId = PreferenceUtil.getInt(MyConstants.GUESTTEAM, 2);
                        if (soundId != 4) {
                            mSoundPool.play(mSoundMap.get(soundId), 1, 1, 0, 0, 1);
                        }
                        boolean isShake = PreferenceUtil.getBoolean(MyConstants.VIBRATEGOALHINT, true);
                        if (isShake) {
                            mVibrator.vibrate(1000);
                        }
                    }

                    if ("3".equals(eventType) || "4".equals(eventType) || "7".equals(eventType) || "8".equals(eventType)) {

                        boolean isShoud = PreferenceUtil.getBoolean(MyConstants.VOICEREDHINT, true);
                        if (isShoud) {
                            mSoundPool.play(mSoundMap.get(3), 1, 1, 0, 0, 1);
                        }

                        boolean isShake = PreferenceUtil.getBoolean(MyConstants.VIBRATEREDHINT, true);
                        if (isShake) {
                            mVibrator.vibrate(1000);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.network_exception_reload_btn:
                // mLoadingLayout.setVisibility(View.VISIBLE);
                // mErrorLayout.setVisibility(View.GONE);
                // mListView.setVisibility(View.GONE);
                // mSwipeRefreshLayout.setVisibility(View.GONE);
                mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
                mLoadHandler.postDelayed(mLoadingDataThread, 0);
                break;
            case R.layout.item_header_unconection_view:
                startActivity(new Intent(Settings.ACTION_SETTINGS));
                break;
            default:
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        isPause = false;
        L.v(TAG, "___onResume___");


//        if (isDestroy) {
//            return;
//        }


//        if (!isLoadedData) {
//            mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
//            mLoadHandler.postDelayed(mLoadingDataThread, 0);
//        } else {
//
//        }


    }

//    private boolean isError = false;

//    @Override
//    public void onError(Exception exception) {
//        isError = true;
//        restartSocket();
//    }
//
//    @Override
//    public void onClose(String message) {
//        if (!isError) {
//            restartSocket();
//        }
//    }

//    @Override
//    public void onMessage(String message) {
//        L.w(TAG, "message = " + message);
//
//        if (message.startsWith("CONNECTED")) {
//            String id = "android" + DeviceInfo.getDeviceId(getActivity());
//            id = MD5Util.getMD5(id);
//            mSocketClient.send("SUBSCRIBE\nid:" + id + "\ndestination:/topic/USER.topic.app\n\n");
//            isWebSocketStart = true;
//            return;
//        } else if (message.startsWith("MESSAGE")) {
//            String[] msgs = message.split("\n");
//            String ws_json = msgs[msgs.length - 1];
//            String type = "";
//            try {
//                JSONObject jsonObject = new JSONObject(ws_json);
//                type = jsonObject.getString("type");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            if (!"".equals(type)) {
//                Message msg = Message.obtain();
//                msg.obj = ws_json;
//                msg.arg1 = Integer.parseInt(type);
//
//                mSocketHandler.sendMessage(msg);
//            }
//        }
//        mSocketClient.send("\n");
//    }

//    private Timer timer = new Timer();
//    private boolean isTimerStart = false;

//    private synchronized void restartSocket() {
//        if (!isDestroy) {
//            if (!isTimerStart) {
//                TimerTask timerTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        if (!isDestroy) {
//                            startWebsocket();
//                        } else {
//                            timer.cancel();
//                        }
//                    }
//                };
//                timer.schedule(timerTask, 2000, 5000);
//                isTimerStart = true;
//            }
//        }
//    }

//    private boolean isDestroy = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d(TAG, "__onDestroy___");
//        if (mSocketClient != null) {
//            isDestroy = true;
//            mSocketClient.close();
//        }
//
//        if (getActivity() != null && mNetStateReceiver != null) {
//            getActivity().unregisterReceiver(mNetStateReceiver);
//        }
    }

    public void onEventMainThread(ScoresFragment.FootballScoresWebSocketEntity entity) {
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

//    public void connectFail() {
//        mViewHandler.sendEmptyMessage(VIEW_STATUS_WEBSOCKET_CONNECT_FAIL);
//    }
//
//    public void connectSuccess() {
//        mViewHandler.sendEmptyMessage(VIEW_STATUS_WEBSOCKET_CONNECT_SUCCESS);
//    }


    /**
     * EventBus设置
     */

    public void onEventMainThread(Integer currentFragmentId) {
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
            updateAdapter();
        }
    }


    /**
     * EventBus 赛场比赛详情返回FootballMatchDetailActivity
     * 接受消息的页面实现
     */
    public void onEventMainThread(String currentFragmentId) {
//        if (isDestroy) {
//            return;
//        }

        if ("".equals(PreferenceUtil.getString(FocusFragment.FOCUS_ISD, ""))) {
            if (mMatchs != null) {
                mMatchs.clear();
            }

            if (mAllMatchs != null) {
                mAllMatchs.clear();
            }
            updateAdapter();
        }

        ((ScoresFragment) getParentFragment()).focusCallback();

        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        mLoadHandler.postDelayed(mLoadingDataThread, 0);
    }






   /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SET_CODE && resultCode == Activity.RESULT_OK) {
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
                updateAdapter();
            }
        } else if (requestCode == REQUEST_DETAIL_CODE && resultCode == Activity.RESULT_OK) {
            //updateAdapter();

            if (isDestroy) {
                return;
            }

            if ("".equals(PreferenceUtil.getString(FocusFragment.FOCUS_ISD, ""))) {
                if (mMatchs != null) {
                    mMatchs.clear();
                }

                if (mAllMatchs != null) {
                    mAllMatchs.clear();
                }
                updateAdapter();
            }

            ((ScoresFragment) getParentFragment()).focusCallback();

            mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
            mLoadHandler.postDelayed(mLoadingDataThread, 0);
        }
    }*/

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (hidden) {
//            isPause = true;
//            if (mSocketClient != null) {
//                isDestroy = true;
//                mSocketClient.close();
//            }
//        } else {
//            isPause = false;
//            isDestroy = false;
//            String fucus_id = PreferenceUtil.getString(FOCUS_ISD, "");
//            if ("".equals(fucus_id) || ",".equals(fucus_id)) {
//                mViewHandler.sendEmptyMessage(VIEW_STATUS_NO_ANY_DATA);
//                return;
//            }
////            if(isLoadedData){
//            mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
//            mLoadHandler.postDelayed(mLoadingDataThread, 0);
////            }
//        }
//    }


    public void reLoadData() {
        String fucus_id = PreferenceUtil.getString(FOCUS_ISD, "");
        if ("".equals(fucus_id) || ",".equals(fucus_id)) {
            mViewHandler.sendEmptyMessage(VIEW_STATUS_NO_ANY_DATA);
            return;
        }
        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        mLoadHandler.post(mLoadingDataThread);
    }

    @Override
    public void onRefresh() {
        mLoadHandler.post(mLoadingDataThread);
        ((ScoresFragment) getParentFragment()).reconnectWebSocket();
    }

//    private boolean isInitData = false;

//    public class FocusNetStateReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context con, Intent arg1) {
//            L.d(TAG, "NetState ___ onReceive ");
//
//            ConnectivityManager manager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
//            if (activeNetwork == null || !activeNetwork.isAvailable()) {
//                if (mErrorLayout.getVisibility() != View.VISIBLE) {
//                    mUnconectionLayout.setVisibility(View.VISIBLE);
//                }
//
//                isWebSocketStart = false;
//                if (mSocketClient != null) {
//                    mSocketClient.close();
//                }
//                mSocketClient = null;
//            } else {
//                mUnconectionLayout.setVisibility(View.GONE);
//            }
//            else {
//                if (isInitData) {
//                    mSwipeRefreshLayout.setRefreshing(true);
//                    mUnconectionLayout.setVisibility(View.GONE);
//                    onRefresh();
//                }
//            }
//        }
//    }


    /**
     * 判断thirdId是否已经关注
     *
     * @param thirdId
     * @return true已关注，false还没关注
     */
    public static boolean isFocusId(String thirdId) {
        String focusIds = PreferenceUtil.getString(FocusFragment.FOCUS_ISD, "");

        if ("".equals(focusIds)) {
            return false;
        } else {
            String[] focusIdArray = focusIds.split("[,]");

            boolean isFocus = false;
            for (String focusId : focusIdArray) {
                if (focusId.equals(thirdId)) {
                    isFocus = true;
                    break;
                }
            }
            return isFocus;
        }
    }

    public static void addFocusId(String thirdId) {
        String focusIds = PreferenceUtil.getString(FocusFragment.FOCUS_ISD, "");
        if ("".equals(focusIds)) {
            PreferenceUtil.commitString(FocusFragment.FOCUS_ISD, thirdId);
        } else {
            PreferenceUtil.commitString(FocusFragment.FOCUS_ISD, focusIds + "," + thirdId);
        }
    }


    public static void deleteFocusId(String thirdId) {
        String focusIds = PreferenceUtil.getString(FocusFragment.FOCUS_ISD, "");
        String[] idArray = focusIds.split("[,]");
        StringBuffer sb = new StringBuffer();
        for (String id : idArray) {
            if (!id.equals(thirdId)) {
                if ("".equals(sb.toString())) {
                    sb.append(id);
                } else {
                    sb.append("," + id);
                }

            }
        }
        PreferenceUtil.commitString(FocusFragment.FOCUS_ISD, sb.toString());
    }

//    private boolean isPause = false;

    @Override
    public void onPause() {
        super.onPause();
//        isPause = true;
    }
}
