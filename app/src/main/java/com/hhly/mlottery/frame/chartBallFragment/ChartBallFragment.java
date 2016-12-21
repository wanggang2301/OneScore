package com.hhly.mlottery.frame.chartBallFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.chartBallAdapter.ChartBallAdapter;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewHolder;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.bean.BarrageBean;
import com.hhly.mlottery.bean.chart.ChartReceive;
import com.hhly.mlottery.bean.chart.ChartRoom;
import com.hhly.mlottery.bean.footballDetails.MatchLike;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.internal.DebouncingOnClickListener;
import de.greenrobot.event.EventBus;
import io.github.rockerhieu.emojicon.EmojiconEditText;


/**
 * 聊球页面
 * desc: 1.2.1版本新增功能，使用自己后台完成聊球功能
 * 作者：tangrr_107
 * 时间：2016/12/6
 */
public class ChartBallFragment extends BaseWebSocketFragment implements View.OnClickListener, ChartBallAdapter.AdapterListener {

    private static final Long SHOW_TIME_LONG = 5 * 60000L;    // 显示时分秒(分钟)
    private static final Long SHOW_TIME_LONG_FULL = 8 * 3600000L;// 显示年月日(小时)
    private static final int START_LOADING = 0;               // 开始加载状态
    private static final int SUCCESS_LOADING = 1;             // 加载成功
    private static final int ERROR_LOADING = 2;               // 加载失败
    private static final int MSG_ON_LINE_COUNT = 3;           // 在线人数消息
    private static final int MSG_UPDATA_LIST = 5;             // 更新会话列表
    private static final int MSG_SEND_TO_ME = 6;              // 发送消息
    private static final int MSG_UPDATA_TIME = 7;             // 设置时间并更新
    private static final int SUCCESS_REFRASH_HISTORY = 8;     // 加载历史成功
    private static final int ERROR_REFRASH_HISTORY = 9;       // 加载历史失败
    private static final String TYPE_MSG = "1";               // 1普通消息
    private static final String TYPE_MSG_TO_ME = "2";         // 2@消息
    private static final String TYPE_MSG_SERVER = "3";        // 3系统消息
    private static final String TYPE_MSG_ON_LINE = "4";       // 4在线人数
    private static final String TYPE_MSG_JOIN_ROOM = "5";     // 5进入聊天室
    private static final String LOADING_MSG_SIZE = "30";      // 获取会话列表size
    private static final String SLIDE_TYPE_MSG_HISTORY = "-1";// -1历史消息
    private static final String SLIDE_TYPE_MSG_NEW = "1";     // 1最新消息
    private static final String SLIDE_TYPE_MSG_ONE = "0";     // 0首次调用
    private static final String ADDKEYHOME = "homeAdd";
    private static final String ADDKEYGUEST = "guestAdd";

    private static final String MATCH_TYPE = "type";          // 赛事类型
    private static final String MATCH_THIRD_ID = "thirdId";   // 赛事ID
    private String mMsgId;                                    // @消息的id
    private String mOnline;                                   // 在线人数
    private boolean isOneJoin = true;                         // 是否首次调用
    private String mLastTime;                                  // 最后一条信息的time

    private Activity mContext;                                // 上下文
    private View mView;                                       // 总布局
    private int type = -1;                                    // 1 籃球、0 足球
    private String mThirdId;                                  // 赛事id
    private EmojiconEditText mEditText;
    private RecyclerView recycler_view;
    private ChartBallAdapter mAdapter;
    private ChartBallReportDialogFragment dialogFragment;
    private ChartReceive mChartReceive;
    private List<ChartReceive.DataBean.ChatHistoryBean> historyBeen;
    private FrameLayout fl_not_chart_image;
    private FrameLayout rl_chart_content;
    private TextView tv_online_count;// 在线人数
    private TextView tv_call_me;// 艾特提示
    private LinearLayout ll_errorLoading;// 无网络
    private LinearLayout ll_progress;

    private ProgressBar talkballpro;//客队Vs主队点赞的比例
    private ImageView ivHomeLike;//点赞主队
    private ImageView ivGuestLike;//点赞客队
    private TextView tvGuestLikeCount;//点赞客队数
    private TextView tvHomeLikeCount;//点赞主队数
    //主队点赞
    private ImageView mHomeLike;
    private ImageView mGuestLike;
    //点赞动画
    private AnimationSet mRiseHomeAnim;
    private AnimationSet mRiseGuestAnim;

    public static ChartBallFragment newInstance(int type, String thirdId) {
        ChartBallFragment fragment = new ChartBallFragment();
        Bundle args = new Bundle();
        args.putInt(MATCH_TYPE, type);
        args.putString(MATCH_THIRD_ID, thirdId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        if (getArguments() != null) {
            type = getArguments().getInt(MATCH_TYPE);
            mThirdId = getArguments().getString(MATCH_THIRD_ID);
        }
        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic("USER.topic.chatroom.football" + mThirdId);
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_chartball, container, false);
        initView();
        requestLikeData(ADDKEYHOME, "0", type);
        initAnim();
        initData(SLIDE_TYPE_MSG_ONE);
        initEvent();
        return mView;
    }

    private void initEvent() {
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CommonUtils.isLogin()) {
                    if (type == 1) {
                        ((BasketDetailsActivityTest) mContext).talkAboutBallLoginBasket();
                    } else if (type == 0) {
                        ((FootballMatchDetailActivity) mContext).talkAboutBallLoginFoot();
                    }
                } else {//跳转输入评论页面
                    if (type == 1) {
                        ((BasketDetailsActivityTest) mContext).talkAboutBallSendBasket();
                    } else if (type == 0) {
                        ((FootballMatchDetailActivity) mContext).talkAboutBallSendFoot();
                    }
                }
            }
        });

        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager manager = (LinearLayoutManager) recycler_view.getLayoutManager();
                    int lastVisiblePosition = manager.findLastVisibleItemPosition();
                    if (lastVisiblePosition >= manager.getItemCount() - 1) {
                        if (historyBeen != null && historyBeen.size() >= 5) {
                            System.out.println("xxxxx ====自动加载");
                            initData(SLIDE_TYPE_MSG_NEW);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private Long doubleClickTime;

    private void initView() {
        mEditText = (EmojiconEditText) mView.findViewById(R.id.et_emoji_input);
        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(false);
        recycler_view = (RecyclerView) mView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setStackFromEnd(false);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);
        fl_not_chart_image = (FrameLayout) mView.findViewById(R.id.fl_not_chart_image);
        rl_chart_content = (FrameLayout) mView.findViewById(R.id.rl_chart_content);
        tv_online_count = (TextView) mView.findViewById(R.id.tv_online_count);
        tv_call_me = (TextView) mView.findViewById(R.id.tv_call_me);
        tv_call_me.setOnClickListener(this);
        mView.findViewById(R.id.tv_send).setOnClickListener(this);
        mView.findViewById(R.id.reLoading).setOnClickListener(this);
        ll_errorLoading = (LinearLayout) mView.findViewById(R.id.ll_errorLoading);
        ll_progress = (LinearLayout) mView.findViewById(R.id.ll_progress);

        talkballpro = (ProgressBar) mView.findViewById(R.id.talkball_pro);
        ivHomeLike = (ImageView) mView.findViewById(R.id.talkball_like_anim_img);
        ivGuestLike = (ImageView) mView.findViewById(R.id.talkbail_guest_like_anim_img);
        tvHomeLikeCount = (TextView) mView.findViewById(R.id.talkball_like_count);
        tvGuestLikeCount = (TextView) mView.findViewById(R.id.talkbail_guest_like_count);
        mHomeLike = (ImageView) mView.findViewById(R.id.talkball_home_like);
        mGuestLike = (ImageView) mView.findViewById(R.id.talkball_guest_like);
        mHomeLike.setOnClickListener(this);
        mGuestLike.setOnClickListener(this);
        ivHomeLike.setVisibility(View.INVISIBLE);
        ivGuestLike.setVisibility(View.INVISIBLE);
        setClickableLikeBtn(true);
    }

    /*自定发送消息测试*/
    private void initData(final String slideType) {
        if (SLIDE_TYPE_MSG_ONE.equals(slideType)) {
            mHandler.sendEmptyMessage(START_LOADING);// 正在加载数据中
        }

        // 首次调用，加载进入聊天室接口
        if (isOneJoin) {
            sendMessageToServer(TYPE_MSG_JOIN_ROOM, "", null);
        }

        String URL = "";
        String chatType = "";
        if (type == 0) {
            URL = BaseURLs.MESSAGE_LIST_FOOTBALL;
            chatType = "football";
        } else if (type == 1) {
            URL = BaseURLs.MESSAGE_LIST_BASKET;
            chatType = "basketball";
        }

        Map<String, String> params = new HashMap<>();
        params.put("chatType", chatType);
        params.put("thirdId", mThirdId);
        params.put("pageSize", LOADING_MSG_SIZE);
        params.put("slideType", slideType);

        VolleyContentFast.requestJsonByGet(URL, params,
                new VolleyContentFast.ResponseSuccessListener<ChartReceive>() {
                    @Override
                    public void onResponse(ChartReceive receive) {
                        if (receive.getResult().equals("200")) {
                            mChartReceive = receive;
                            if (SLIDE_TYPE_MSG_ONE.equals(slideType)) {
                                mHandler.sendEmptyMessage(SUCCESS_LOADING);
                            } else if (SLIDE_TYPE_MSG_HISTORY.equals(slideType)) {
                                historyBeen.addAll(mChartReceive.getData().getChatHistory());
                                mAdapter.notifyDataSetChanged();
                            } else if (SLIDE_TYPE_MSG_NEW.equals(slideType)) {
                                historyBeen.addAll(mChartReceive.getData().getChatHistory());
                                mAdapter.notifyDataSetChanged();
                                sleepView();
                            }
                        } else {
                            if (SLIDE_TYPE_MSG_ONE.equals(slideType)) {
                                mHandler.sendEmptyMessage(ERROR_LOADING);
                            } else if (SLIDE_TYPE_MSG_HISTORY.equals(slideType)) {
                                ToastTools.showQuick(mContext, R.string.chart_ball_loading_error);
                            } else if (SLIDE_TYPE_MSG_NEW.equals(slideType)) {
                                recycler_view.smoothScrollToPosition(historyBeen.size() - 3);
                                ToastTools.showQuick(mContext, R.string.chart_ball_loading_error);
                            }
                        }
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        if (SLIDE_TYPE_MSG_ONE.equals(slideType)) {
                            mHandler.sendEmptyMessage(ERROR_LOADING);
                        } else if (SLIDE_TYPE_MSG_HISTORY.equals(slideType)) {
                            ToastTools.showQuick(mContext, R.string.chart_ball_loading_error);
                        } else if (SLIDE_TYPE_MSG_NEW.equals(slideType)) {
                            recycler_view.smoothScrollToPosition(historyBeen.size() - 3);
                            ToastTools.showQuick(mContext, R.string.chart_ball_loading_error);
                        }
                    }
                }, ChartReceive.class
        );
    }

    //发送消息后台
    private void sendMessageToServer(final String msgCode, String message, String toUserId) {
        System.out.println("xxxxx 发送的message: " + message);
        System.out.println("xxxxx 发送的msgCode: " + msgCode);

        String str = "";
        try {
            str = URLEncoder.encode(message, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String URL = "";
        String chatType = "";
        if (type == 0) {
            URL = BaseURLs.MESSAGE_SEND_FOOTBALL;
            chatType = "football";
        } else if (type == 1) {
            URL = BaseURLs.MESSAGE_SEND_BASKET;
            chatType = "basketball";
        }

        Map<String, String> params = new HashMap<>();
        params.put("sourceName", "android");
        params.put("chatType", chatType);
        params.put("message", str);
        params.put("thirdId", mThirdId);
        params.put("msgCode", msgCode);
        params.put("loginToken", AppConstants.register.getData().getLoginToken());
        params.put("toUserId", toUserId);
        params.put("deviceId", AppConstants.deviceToken);
        params.put("msgId", UUID.randomUUID().toString());

        VolleyContentFast.requestStringByGet(URL, params, null,
                new VolleyContentFast.ResponseSuccessListener<String>() {
                    @Override
                    public void onResponse(String receive) {
                        if (msgCode.equals(TYPE_MSG_JOIN_ROOM)) {
                            isOneJoin = false;// 首次进入聊天室调用成功
                        }

                        System.out.println("xxxxx 发送返回stringJson:" + receive);
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                        System.out.println("xxxxx 发送失败！");
                        ToastTools.showQuick(mContext, R.string.chart_ball_send_error);
                    }
                });
    }

    //请求点赞数据
    private void requestLikeData(String addkey, String addcount, int type) {
        Map<String, String> params = new HashMap<>();
        params.put("thirdId", mThirdId);
        params.put(addkey, addcount);
        String url = "";
        if (type == 0) {//足球点赞
            url = BaseURLs.URL_FOOTBALL_DETAIL_LIKE_INFO;
        } else if (type == 1) {//篮球点赞
            url = BaseURLs.URL_BASKETBALLBALL_DETAIL_LIKE_INFO;
        }
        VolleyContentFast.requestJsonByPost(url, params, new VolleyContentFast.ResponseSuccessListener<MatchLike>() {
            @Override
            public void onResponse(MatchLike matchLike) {
                tvHomeLikeCount.setText(matchLike.getHomeLike());
                tvGuestLikeCount.setText(matchLike.getGuestLike());
                System.out.println("lzfdianzan" + matchLike.getGuestLike() + matchLike.getHomeLike());
                if (matchLike.getHomeLike() != null && matchLike.getGuestLike() != null) {
                    int homeLikeCount = Integer.parseInt(matchLike.getHomeLike());
                    int guestLikeCount = Integer.parseInt(matchLike.getGuestLike());
                    talkballpro.setMax(homeLikeCount + guestLikeCount);
                    talkballpro.setProgress(homeLikeCount);
                } else {
                    tvHomeLikeCount.setText("0");
                    tvGuestLikeCount.setText("0");
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        }, MatchLike.class);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_LOADING:
//                    ll_progress.setVisibility(View.VISIBLE);
                    ll_errorLoading.setVisibility(View.GONE);
                    break;
                case SUCCESS_LOADING:
                    // 开启socket推送
                    connectWebSocket();
                    ll_progress.setVisibility(View.GONE);
                    ll_errorLoading.setVisibility(View.GONE);

                    historyBeen = mChartReceive.getData().getChatHistory();
                    if (historyBeen != null) {
                        if (historyBeen.size() != 0) {
                            if (historyBeen.size() < 2) {
                                mLastTime = historyBeen.get(0).getTime();
                            } else {
                                mLastTime = historyBeen.get(historyBeen.size() - 1).getTime();
                            }
                        }
                    }
                    for (int i = 0, len = historyBeen.size(); i < len; i++) {// 判断是否显示时间控件
                        if (i != 0) {
                            try {
                                Long lastTime = DateUtil.getCurrentTime(historyBeen.get(i - 1).getTime());
                                Long currentTime = DateUtil.getCurrentTime(historyBeen.get(i).getTime());
                                if (currentTime - lastTime >= SHOW_TIME_LONG_FULL) {
                                    // 显示年月日
                                    historyBeen.get(i).setShowTime(currentTime - lastTime >= SHOW_TIME_LONG_FULL);
                                } else if (currentTime - lastTime >= SHOW_TIME_LONG) {
                                    // 显示时分秒
                                    historyBeen.get(i).setShowTime(currentTime - lastTime >= SHOW_TIME_LONG);
                                    historyBeen.get(i).setTime(DateUtil.getLotteryInfoDate(historyBeen.get(i).getTime(), "HH:mm:ss"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    mAdapter = new ChartBallAdapter(mContext, historyBeen);
                    mAdapter.setShowDialogOnClickListener(ChartBallFragment.this);
                    recycler_view.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(new BaseRecyclerViewHolder.OnItemClickListener() {
                        @Override
                        public void onItemClick(View convertView, int position) {
                            System.out.println("xxxxx position！" + position);
                            Long currentTime = System.currentTimeMillis();
                            if(doubleClickTime != null){
                                doubleClickTime = currentTime;
                                if(currentTime - doubleClickTime < 1000){
                                    System.out.println("xxxxx 双击事件！");
                                }
                                doubleClickTime = null;
                            }
                        }
                    });

                    sleepView();

                    if (historyBeen == null || historyBeen.size() == 0) {
                        fl_not_chart_image.setVisibility(View.VISIBLE);
                        rl_chart_content.setVisibility(View.GONE);
                    } else {
                        fl_not_chart_image.setVisibility(View.GONE);
                        rl_chart_content.setVisibility(View.VISIBLE);
                    }
                    break;
                case ERROR_LOADING:
                    ll_progress.setVisibility(View.GONE);
                    ll_errorLoading.setVisibility(View.VISIBLE);
                    break;
                case MSG_ON_LINE_COUNT:// 在线人数
                    tv_online_count.setText((mOnline + mContext.getResources().getString(R.string.chart_ball_online)));// 在线人数
                    break;
                case MSG_UPDATA_LIST:// 更新会话
                    ChartReceive.DataBean.ChatHistoryBean chartbean = (ChartReceive.DataBean.ChatHistoryBean) msg.obj;

                    // 收到消息，显示弹幕
                    if (chartbean.getMsgCode() == 1) {
                        EventBus.getDefault().post(new BarrageBean(chartbean.getFromUser().getUserLogo(), chartbean.getMessage()));
                    } else if (chartbean.getMsgCode() == 2) {
                        String message = chartbean.getToUser() != null ? "@" + chartbean.getToUser().getUserNick() + ":" + chartbean.getMessage() : chartbean.getMessage();
                        EventBus.getDefault().post(new BarrageBean(chartbean.getFromUser().getUserLogo(), message));
                    }

                    if (msg.arg2 == 2) { // 为@消息
                        tv_call_me.setVisibility(View.VISIBLE);
                    }

                    historyBeen.add(chartbean);
                    mAdapter.notifyDataSetChanged();
                    fl_not_chart_image.setVisibility(View.GONE);
                    rl_chart_content.setVisibility(View.VISIBLE);


                    sleepView();

                    break;
                case MSG_SEND_TO_ME:// 本地发送消息
                    ChartReceive.DataBean.ChatHistoryBean contentEntitiy = (ChartReceive.DataBean.ChatHistoryBean) msg.obj;
                    if (!TextUtils.isEmpty(contentEntitiy.getToUser().getUserId())) {
                        sendMessageToServer(TYPE_MSG_TO_ME, contentEntitiy.getMessage(), contentEntitiy.getToUser().getUserId());
                    } else {
                        sendMessageToServer(TYPE_MSG, contentEntitiy.getMessage(), null);
                    }
                    fl_not_chart_image.setVisibility(View.GONE);
                    rl_chart_content.setVisibility(View.VISIBLE);
                    historyBeen.add(contentEntitiy);
                    mAdapter.notifyDataSetChanged();
                    sleepView();

                    break;
                case MSG_UPDATA_TIME:
                    mAdapter.notifyDataSetChanged();
                    break;
//                case SUCCESS_REFRASH_HISTORY:
//
//                    break;
//                case ERROR_REFRASH_HISTORY:
//
//                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    // EvenBus-->ChartBallActivity传过来
    public void onEventMainThread(ChartReceive.DataBean.ChatHistoryBean contentEntitiy) {
        if ("EXIT_CURRENT_ACTIVITY".equals(contentEntitiy.getMessage()) && "EXIT_CURRENT_ACTIVITY".equals(contentEntitiy.getFromUser().getUserNick())) {
            mContext.finish();
        } else {
            Message msg = mHandler.obtainMessage();
            msg.what = MSG_SEND_TO_ME;
            msg.obj = contentEntitiy;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApp.getContext().sendBroadcast(new Intent("CLOSE_INPUT_ACTIVITY"));
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void shwoDialog(String msgId, String toUserId, String toUserNick) {
        dialogFragment = ChartBallReportDialogFragment.newInstance(msgId, AppConstants.register.getData().getUser().getNickName(), toUserId, toUserNick);
        if (!dialogFragment.isVisible()) {
            dialogFragment.show(getChildFragmentManager(), "chartballDialog");
        }
    }

    @Override
    protected void onTextResult(final String text) {
        new Thread() {
            @Override
            public void run() {
                System.out.println("xxxxx 滚球推送：" + text);
                ChartRoom chartRoom = JSON.parseObject(text, ChartRoom.class);

                ChartReceive.DataBean.ChatHistoryBean chartbean = new ChartReceive.DataBean.ChatHistoryBean(chartRoom.getData().getMsgId(), chartRoom.getData().getMsgCode(), chartRoom.getData().getMessage(),
                        chartRoom.getData().getFromUser() == null ? new ChartReceive.DataBean.ChatHistoryBean.FromUserBean() : new ChartReceive.DataBean.ChatHistoryBean.FromUserBean(chartRoom.getData().getFromUser().getUserId(), chartRoom.getData().getFromUser().getUserLogo(), chartRoom.getData().getFromUser().getUserNick()),
                        chartRoom.getData().getToUser() == null ? new ChartReceive.DataBean.ChatHistoryBean.ToUser() : new ChartReceive.DataBean.ChatHistoryBean.ToUser(chartRoom.getData().getToUser().getUserId(), chartRoom.getData().getToUser().getUserLogo(), chartRoom.getData().getToUser().getUserNick()),
                        chartRoom.getData().getTime());

                switch (chartRoom.getData().getMsgCode()) {
                    case 4:// 获取在线人数消息
                        mOnline = chartRoom.getData().getOnlineNum() == null ? "0" : chartRoom.getData().getOnlineNum();
                        mHandler.sendEmptyMessage(MSG_ON_LINE_COUNT);
                        break;
                    case 1:
                    case 2:
                        // 发送数据并更新
                        if (!AppConstants.register.getData().getUser().getUserId().equals(chartRoom.getData().getFromUser().getUserId())) {
                            try {
                                if (historyBeen != null && historyBeen.size() > 1) {
                                    Long lastTime = DateUtil.getCurrentTime(mLastTime);
                                    Long currentTime = DateUtil.getCurrentTime(chartRoom.getData().getTime());
                                    if (currentTime - lastTime >= SHOW_TIME_LONG_FULL) {
                                        // 显示年月日
                                        chartbean.setShowTime(currentTime - lastTime >= SHOW_TIME_LONG_FULL);
                                    } else if (currentTime - lastTime >= SHOW_TIME_LONG) {
                                        // 显示时分秒
                                        chartbean.setShowTime(currentTime - lastTime >= SHOW_TIME_LONG);
                                        chartbean.setTime(DateUtil.getLotteryInfoDate(chartRoom.getData().getTime(), "HH:mm:ss"));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Message msg = mHandler.obtainMessage();
                            msg.what = MSG_UPDATA_LIST;
                            msg.arg1 = chartRoom.getData().getMsgCode();
                            msg.obj = chartbean;
                            if (chartRoom.getData().getMsgCode() == 2 && AppConstants.register.getData().getUser().getUserId().equals(chartRoom.getData().getToUser().getUserId())) {
                                msg.arg2 = 2;
                                mMsgId = chartRoom.getData().getMsgId();
                            }
                            mLastTime = chartRoom.getData().getTime();
                            mHandler.sendMessage(msg);
                        } else {
                            // 本地所发的信息
                            for (int i = 0, len = historyBeen.size(); i < len; i++) {
                                if (historyBeen.get(i).getMsgId() == null && historyBeen.get(i).getMessage().equals(chartRoom.getData().getMessage())) {
                                    historyBeen.get(i).setMsgId(chartRoom.getData().getMsgId());
                                    historyBeen.get(i).getToUser().setUserLogo(chartRoom.getData().getToUser() == null ? null : chartRoom.getData().getToUser().getUserLogo());
                                    historyBeen.get(i).setTime(chartRoom.getData().getTime());

                                    if (i != 0) {
                                        try {
                                            Long lastTime = DateUtil.getCurrentTime(historyBeen.get(i - 1).getTime());
                                            Long currentTime = DateUtil.getCurrentTime(historyBeen.get(i).getTime());
                                            if (currentTime - lastTime >= SHOW_TIME_LONG_FULL) {
                                                // 显示年月日
                                                historyBeen.get(i).setShowTime(currentTime - lastTime >= SHOW_TIME_LONG_FULL);
                                            } else if (currentTime - lastTime >= SHOW_TIME_LONG) {
                                                // 显示时分秒
                                                historyBeen.get(i).setShowTime(currentTime - lastTime >= SHOW_TIME_LONG);
                                                historyBeen.get(i).setTime(DateUtil.getLotteryInfoDate(chartRoom.getData().getTime(), "HH:mm:ss"));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    mLastTime = chartRoom.getData().getTime();
                                    mHandler.sendEmptyMessage(MSG_UPDATA_TIME);
                                    break;
                                }
                            }

                        }
                        break;
                }
            }
        }.start();
    }

    // 等adapter更新OK后再执行
    private void sleepView() {
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (historyBeen.size() != 0) {
                            recycler_view.smoothScrollToPosition(historyBeen.size() - 1);
                        }
                    }
                });
            }
        }.start();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_call_me:
                for (int i = 0, len = historyBeen.size(); i < len; i++) {
                    if (mMsgId.equals(historyBeen.get(i).getMsgId())) {
                        recycler_view.smoothScrollToPosition(i);
                        tv_call_me.setVisibility(View.GONE);
                        return;
                    }
                }
                break;
            case R.id.reLoading:
                System.out.println("xxxxx reLoading");
                initData(SLIDE_TYPE_MSG_ONE);
                break;
            case R.id.talkball_home_like:
                MobclickAgent.onEvent(MyApp.getContext(), "BasketDetailsActivityTest_HomeLike");
                ivHomeLike.setVisibility(View.VISIBLE);
                ivHomeLike.startAnimation(mRiseHomeAnim);
                requestLikeData(ADDKEYHOME, "1", type);
                break;
            case R.id.talkball_guest_like:
                MobclickAgent.onEvent(MyApp.getContext(), "BasketDetailsActivityTest_GuestLike");
                ivGuestLike.setVisibility(View.VISIBLE);
                ivGuestLike.startAnimation(mRiseGuestAnim);
                requestLikeData(ADDKEYGUEST, "1", type);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化点赞动画
     */
    private void initAnim() {
        mRiseHomeAnim = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, 0f, -50f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        mRiseHomeAnim.addAnimation(translateAnimation);
        mRiseHomeAnim.addAnimation(alphaAnimation);
        mRiseHomeAnim.setDuration(300);
        mRiseHomeAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivHomeLike.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mRiseGuestAnim = new AnimationSet(true);
        mRiseGuestAnim.addAnimation(translateAnimation);
        mRiseGuestAnim.addAnimation(alphaAnimation);
        mRiseGuestAnim.setDuration(300);
        mRiseGuestAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }


            @Override
            public void onAnimationEnd(Animation animation) {
                ivGuestLike.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    // 是否可以点赞
    public void setClickableLikeBtn(boolean clickable) {
        mHomeLike.setClickable(clickable);
        mGuestLike.setClickable(clickable);
        if (clickable) {
            mHomeLike.setImageResource(R.mipmap.like_red);
            mGuestLike.setImageResource(R.mipmap.like_blue);
        } else {
            mHomeLike.setImageResource(R.mipmap.like_anim_left);
            mGuestLike.setImageResource(R.mipmap.like_anim_right);
        }
    }

    public void onRefresh() {
        initData(SLIDE_TYPE_MSG_HISTORY);
    }
}