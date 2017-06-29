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
import com.hhly.mlottery.bean.chart.SendMessageBean;
import com.hhly.mlottery.bean.enums.SendMsgEnum;
import com.hhly.mlottery.bean.footballDetails.MatchLike;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BaseUserTopics;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import de.greenrobot.event.EventBus;


/**
 * 聊球页面
 * desc: 1.2.1版本新增功能，使用自己后台完成聊球功能
 * 作者：tangrr_107
 * 时间：2016/12/6
 */
public class ChartBallFragment extends BaseWebSocketFragment implements View.OnClickListener {

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
    private static final int SOCKET_TIMER_TASK = 10;       // 定时器检测socket连接
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
    private TextView mEditText;
    private RecyclerView recycler_view;
    private ChartBallAdapter mAdapter;
    //    private ChartBallReportDialogFragment dialogFragment;
    private ChartReceive mChartReceive;
    private List<ChartReceive.DataBean.ChatHistoryBean> historyBeen = new ArrayList<>();
    private LinearLayout ll_not_chart_image;
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

    private boolean isScrollBottom = true;
    private TextView tv_new_msg;
    private boolean isLoading = false;// 是否已加载过数据

    Timer timer = new Timer();
    private TimerTask timerTask;

    public ChartBallFragment() {
    }

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
        if (type == 0) {
//            setTopic("USER.topic.chatroom.football" + mThirdId);
            setTopic(BaseUserTopics.chatroomFootball + mThirdId);
        } else if (type == 1) {
//            setTopic("USER.topic.chatroom.basketball" + mThirdId);
            setTopic(BaseUserTopics.chatroomBasket + mThirdId);
        }
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_chartball, container, false);
        initView();
//        requestLikeData(ADDKEYHOME, "0", type);
        initAnim();
        initData(SLIDE_TYPE_MSG_ONE);
        initEvent();
        socketTimerTask();
        return mView;
    }

    private void initEvent() {
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBack();
            }
        });
        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {// 当不滚动时
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    // 判断是否滚动到底部
                    if (lastVisibleItem == (totalItemCount - 1)) {
                        isScrollBottom = true;
                        if (tv_new_msg.getVisibility() == View.VISIBLE) {
                            tv_new_msg.setVisibility(View.GONE);
                        }
                    } else {
                        isScrollBottom = false;
                    }
                }
            }
        });
    }

    // 开始聊天时判断用户是否登录
    private void loginBack() {
        if (!DeviceInfo.isLogin()) {// 跳转登录
            if (type == 1) {
                ((BasketDetailsActivityTest) mContext).talkAboutBallLoginBasket();
            } else if (type == 0) {
                ((FootballMatchDetailActivity) mContext).talkAboutBallLoginFoot();
            }
        } else {//跳转聊天框页面
            if (type == 1) {
                ((BasketDetailsActivityTest) mContext).talkAboutBallSendBasket();
            } else if (type == 0) {
                ((FootballMatchDetailActivity) mContext).talkAboutBallSendFoot();
            }
        }
    }

    private void initView() {
        mEditText = (TextView) mView.findViewById(R.id.et_emoji_input);
        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(false);
        recycler_view = (RecyclerView) mView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setStackFromEnd(false);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);
        ll_not_chart_image = (LinearLayout) mView.findViewById(R.id.ll_not_chart_image);
        rl_chart_content = (FrameLayout) mView.findViewById(R.id.rl_chart_content);
        tv_online_count = (TextView) mView.findViewById(R.id.tv_online_count);
        tv_call_me = (TextView) mView.findViewById(R.id.tv_call_me);
        tv_call_me.setOnClickListener(this);
        mView.findViewById(R.id.tv_send).setOnClickListener(this);
        mView.findViewById(R.id.reLoading).setOnClickListener(this);
        ll_errorLoading = (LinearLayout) mView.findViewById(R.id.ll_errorLoading);
        ll_progress = (LinearLayout) mView.findViewById(R.id.ll_progress);
        tv_new_msg = (TextView) mView.findViewById(R.id.tv_new_msg);
        tv_new_msg.setOnClickListener(this);

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
        if (isLoading) {
            if (!getUserVisibleHint()) {
                return;
            }
        } else {
            isLoading = true;
        }
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
        if (SLIDE_TYPE_MSG_HISTORY.equals(slideType) && historyBeen != null && historyBeen.size() != 0) {
            params.put("msgId", historyBeen.get(0).getMsgId());
        }

        VolleyContentFast.requestJsonByGet(URL, params,
                new VolleyContentFast.ResponseSuccessListener<ChartReceive>() {
                    @Override
                    public void onResponse(ChartReceive receive) {
                        if (receive.getResult().equals("200")) {
                            mChartReceive = receive;
                            // 国际化日期格式
                            if (mChartReceive.getData() != null && mChartReceive.getData().getChatHistory() != null) {
                                for (int i = 0; i < mChartReceive.getData().getChatHistory().size(); i++) {
                                    String time = mChartReceive.getData().getChatHistory().get(i).getTime();
                                    mChartReceive.getData().getChatHistory().get(i).setTime(DateUtil.convertDateToNationHMS(time));
                                }
                            }

                            if (SLIDE_TYPE_MSG_ONE.equals(slideType)) {
                                mHandler.sendEmptyMessage(SUCCESS_LOADING);
                            } else if (SLIDE_TYPE_MSG_HISTORY.equals(slideType)) {
                                // 将数据添加到前面
                                if (mChartReceive.getData().getChatHistory() != null) {
                                    historyBeen.addAll(0, mChartReceive.getData().getChatHistory());
                                    mAdapter.notifyDataSetChanged();
                                }
                            } else if (SLIDE_TYPE_MSG_NEW.equals(slideType)) {
                                if (mChartReceive.getData().getChatHistory() != null) {
                                    historyBeen.addAll(mChartReceive.getData().getChatHistory());
                                    mAdapter.notifyDataSetChanged();
                                    sleepView();
                                }
                            }
                        } else {
                            if (SLIDE_TYPE_MSG_ONE.equals(slideType)) {
                                mHandler.sendEmptyMessage(ERROR_LOADING);
                            } else if (SLIDE_TYPE_MSG_HISTORY.equals(slideType)) {
                                ToastTools.showQuick(mContext, mContext.getResources().getString(R.string.chart_ball_loading_error));
                            } else if (SLIDE_TYPE_MSG_NEW.equals(slideType)) {
                                recycler_view.smoothScrollToPosition(historyBeen.size() - 3);
                                ToastTools.showQuick(mContext, mContext.getResources().getString(R.string.chart_ball_loading_error));
                            }
                        }
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        if (SLIDE_TYPE_MSG_ONE.equals(slideType)) {
                            mHandler.sendEmptyMessage(ERROR_LOADING);
                        } else if (SLIDE_TYPE_MSG_HISTORY.equals(slideType)) {
                            ToastTools.showQuick(mContext, mContext.getResources().getString(R.string.chart_ball_loading_error));
                        } else if (SLIDE_TYPE_MSG_NEW.equals(slideType)) {
                            recycler_view.smoothScrollToPosition(historyBeen.size() - 3);
                            ToastTools.showQuick(mContext, mContext.getResources().getString(R.string.chart_ball_loading_error));
                        }
                    }
                }, ChartReceive.class);
    }

    //发送消息后台
    private void sendMessageToServer(final String msgCode, final String message, String toUserId) {
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
        params.put("loginToken", AppConstants.register.getToken());
        params.put("toUserId", toUserId);
        params.put("deviceId", AppConstants.deviceToken);
        params.put("msgId", UUID.randomUUID().toString());

        VolleyContentFast.requestJsonByGet(URL, params,
                new VolleyContentFast.ResponseSuccessListener<SendMessageBean>() {
                    @Override
                    public void onResponse(SendMessageBean receive) {
                        if (msgCode.equals(TYPE_MSG_JOIN_ROOM)) {
                            isOneJoin = false;// 首次进入聊天室调用成功
                        } else {
                            if (receive != null && receive.getResult().equals("200")) {
                                // 发送成功
                                switch (receive.getData().getResultCode()) {
                                    case 1000:// 发送成功
                                        sendMsgChanged(message, SendMsgEnum.SEND_SUCCESS);
                                        break;
                                    case 1010:// 您已被禁言
                                        sendMsgChanged(message, SendMsgEnum.SEND_ERROR);
                                        int bannedTime = 0;
                                        try {
                                            bannedTime = Integer.parseInt(receive.getData().getResultObj());
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }

                                        if (bannedTime >= 1440) {// 超过一天
                                            int d = bannedTime / 1440;
                                            int h;
                                            int m = 0;

                                            if (bannedTime - d * 1440 < 60) {
                                                h = 0;
                                                m = (bannedTime - d * 1440);
                                            } else {
                                                h = (bannedTime - d * 1440) / 60;
                                                if (bannedTime - d * 1440 - h * 60 > 0) {
                                                    m = bannedTime - d * 1440 - h * 60;
                                                }
                                            }
                                            ToastTools.showQuick(mContext, mContext.getResources().getString(R.string.chart_ball_banned) + String.valueOf(d) + mContext.getResources().getString(R.string.number_hk_dd) + String.valueOf(h) + mContext.getResources().getString(R.string.number_hk_hh) + String.valueOf(m) + mContext.getResources().getString(R.string.number_hk_mm));
                                        } else if (bannedTime >= 60) {// 超过一小时
                                            int h = bannedTime / 60;
                                            int m = bannedTime - h * 60;

                                            ToastTools.showQuick(mContext, mContext.getResources().getString(R.string.chart_ball_banned) + String.valueOf(h) + mContext.getResources().getString(R.string.number_hk_hh) + String.valueOf(m) + mContext.getResources().getString(R.string.number_hk_mm));

                                        } else {// 直接显示分钟
                                            ToastTools.showQuick(mContext, mContext.getResources().getString(R.string.chart_ball_banned) + String.valueOf(bannedTime) + mContext.getResources().getString(R.string.number_hk_mm));
                                        }
                                        break;
                                    case 1014:// 被举报用户不存在
                                        sendMsgChanged(message, SendMsgEnum.SEND_ERROR);
                                        ToastTools.showQuick(mContext, mContext.getResources().getString(R.string.chart_ball_report_user_not));
                                        break;
                                    case 1016:// @用户不存在
                                        sendMsgChanged(message, SendMsgEnum.SEND_ERROR);
                                        ToastTools.showQuick(mContext, mContext.getResources().getString(R.string.chart_ball_call_user_not));
                                        break;
                                    case 1013:// 用户登录校验失败
                                        ToastTools.showQuick(mContext, mContext.getResources().getString(R.string.chart_ball_login_error));
                                        break;
                                    default:
                                        // 服务器错误
                                        sendMsgChanged(message, SendMsgEnum.SEND_ERROR);
                                        ToastTools.showQuick(mContext, mContext.getResources().getString(R.string.about_service_exp));
                                        break;
                                }
                            } else {
                                sendMsgChanged(message, SendMsgEnum.SEND_ERROR);
                            }
                        }
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        if (!msgCode.equals(TYPE_MSG_JOIN_ROOM)) {
                            sendMsgChanged(message, SendMsgEnum.SEND_ERROR);
                        }
                    }
                }, SendMessageBean.class);
    }

    private void sendMsgChanged(String message, int sendStart) {
        ChartReceive.DataBean.ChatHistoryBean chatHistoryBean = null;
        for (int len = historyBeen.size() - 1, i = len; i >= 0; i--) {
            if (historyBeen.get(i).getMessage().equals(message)) {
                historyBeen.get(i).setSendStart(sendStart);
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
                chatHistoryBean = historyBeen.get(i);
                break;
            }
        }
        if (chatHistoryBean != null) {
            if (sendStart == SendMsgEnum.SEND_SUCCESS) {
                if (chatHistoryBean.getMsgCode() == 1) {
                    // 普通消息
                    EventBus.getDefault().post(new BarrageBean(AppConstants.register.getUser().getImageSrc(), chatHistoryBean.getMessage()));
                } else if (chatHistoryBean.getMsgCode() == 2) {
                    // @消息
                    String msg = "@" + chatHistoryBean.getToUser().getUserNick() + ":" + chatHistoryBean.getMessage();
                    EventBus.getDefault().post(new BarrageBean(AppConstants.register.getUser().getImageSrc(), msg));
                }
            }
        }
    }

    //请求点赞数据
    private void requestLikeData(String addkey, String addcount, int type) {
        if (!getUserVisibleHint()) {
            return;
        }
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
                    ll_progress.setVisibility(View.VISIBLE);
                    ll_errorLoading.setVisibility(View.GONE);
                    break;
                case SUCCESS_LOADING:
                    // 开启socket推送
                    connectWebSocket();
                    historyBeen.clear();
                    historyBeen.addAll(mChartReceive.getData().getChatHistory());
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
                                    historyBeen.get(i).setTime(historyBeen.get(i).getTime().split(" ")[1]);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    mAdapter = new ChartBallAdapter(mContext, historyBeen);
                    mAdapter.setShowDialogOnClickListener(new ChartBallAdapter.AdapterListener() {
                        @Override
                        public void shwoDialog(String msgId, String toUserId, String toUserNick) {
                            ChartBallReportDialogFragment dialogFragment = ChartBallReportDialogFragment.newInstance(msgId, AppConstants.register.getUser().getNickName(), toUserId, toUserNick);
                            if (!dialogFragment.isVisible()) {
                                dialogFragment.show(getChildFragmentManager(), "chartballDialog");
                            }
                        }

                        @Override
                        public void userLoginBack() {
                            loginBack();
                        }

                        @Override
                        public void againSendMsg(String msg) {
                            // 重新发送
                            for (int i = 0, len = historyBeen.size(); i < len; i++) {
                                if (historyBeen.get(i).getSendStart() == SendMsgEnum.SEND_ERROR && historyBeen.get(i).getFromUser().getUserId().equals(AppConstants.register.getUser().getUserId()) && historyBeen.get(i).getMessage().equals(msg)) {
                                    if (historyBeen.get(i).getMsgCode() == 2 && !TextUtils.isEmpty(historyBeen.get(i).getToUser().getUserId())) {
                                        sendMessageToServer(TYPE_MSG_TO_ME, historyBeen.get(i).getMessage(), historyBeen.get(i).getToUser().getUserId());
                                    } else {
                                        sendMessageToServer(TYPE_MSG, historyBeen.get(i).getMessage(), null);
                                    }
                                    historyBeen.get(i).setSendStart(SendMsgEnum.SEND_LOADING);
                                    if (mAdapter != null) {
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    break;
                                }
                            }
                        }
                    });
                    recycler_view.setAdapter(mAdapter);

                    sleepView();

                    if (historyBeen == null || historyBeen.size() == 0) {
                        ll_not_chart_image.setVisibility(View.VISIBLE);
                        rl_chart_content.setVisibility(View.GONE);
                    } else {
                        ll_not_chart_image.setVisibility(View.GONE);
                        rl_chart_content.setVisibility(View.VISIBLE);
                    }

                    ll_progress.setVisibility(View.GONE);
                    ll_errorLoading.setVisibility(View.GONE);
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

                    if (!isScrollBottom) {
                        tv_new_msg.setVisibility(View.VISIBLE);
                    }

                    try {
                        // 收到消息，显示弹幕
                        if (chartbean.getMsgCode() == 1) {
                            EventBus.getDefault().post(new BarrageBean(chartbean.getFromUser().getUserLogo(), chartbean.getMessage()));
                        } else if (chartbean.getMsgCode() == 2) {
                            String message = chartbean.getToUser() != null ? "@" + chartbean.getToUser().getUserNick() + ":" + chartbean.getMessage() : chartbean.getMessage();
                            EventBus.getDefault().post(new BarrageBean(chartbean.getFromUser().getUserLogo(), message));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (msg.arg2 == 2) { // 为@消息
                        tv_call_me.setVisibility(View.VISIBLE);
                    }

                    try {
                        historyBeen.add(chartbean);
                        mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ll_not_chart_image.setVisibility(View.GONE);
                    rl_chart_content.setVisibility(View.VISIBLE);

                    sleepView();

                    break;
                case MSG_SEND_TO_ME:// 本地发送消息
                    ChartReceive.DataBean.ChatHistoryBean contentEntitiy = (ChartReceive.DataBean.ChatHistoryBean) msg.obj;
                    ll_not_chart_image.setVisibility(View.GONE);
                    rl_chart_content.setVisibility(View.VISIBLE);
                    historyBeen.add(contentEntitiy);
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    if (contentEntitiy.getMsgCode() == 2 && !TextUtils.isEmpty(contentEntitiy.getToUser().getUserId())) {
                        sendMessageToServer(TYPE_MSG_TO_ME, contentEntitiy.getMessage(), contentEntitiy.getToUser().getUserId());
                    } else {
                        sendMessageToServer(TYPE_MSG, contentEntitiy.getMessage(), null);
                    }

                    sleepView();

                    break;
                case MSG_UPDATA_TIME:
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case SOCKET_TIMER_TASK:// 启动定时器
                    connectWebSocket();
                    break;
            }
        }
    };

    /**
     * 30重连socket一次
     */
    private void socketTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(SOCKET_TIMER_TASK);
            }
        };
        timer.schedule(timerTask, 1000 * 1, 1000 * 30);
    }

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
            isScrollBottom = true;
            contentEntitiy.setSendStart(SendMsgEnum.SEND_LOADING);
            Message msg = mHandler.obtainMessage();
            msg.what = MSG_SEND_TO_ME;
            msg.obj = contentEntitiy;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onDestroy() {
        MyApp.getContext().sendBroadcast(new Intent("CLOSE_INPUT_ACTIVITY"));
        EventBus.getDefault().unregister(this);
        timer.cancel();
        timer = null;
        timerTask.cancel();
        timerTask = null;
        super.onDestroy();
    }

    @Override
    protected void onTextResult(final String text) {
//        new Thread() {
//            @Override
//            public void run() {
                synchronized (this) {
                    L.d("xxxxx", "聊球推送：" + text);
                    ChartRoom chartRoom = JSON.parseObject(text, ChartRoom.class);

                    // 国际化日期格式
                    String time = "";
                    if (chartRoom != null && chartRoom.getData() != null && chartRoom.getData().getTime() != null) {
                        time = DateUtil.convertDateToNationHMS(chartRoom.getData().getTime());
                    }

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
                            String userId = " ";
                            try {
                                userId = AppConstants.register.getUser().getUserId();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (!userId.equals(chartRoom.getData().getFromUser().getUserId())) {
                                try {
                                    if (historyBeen != null && historyBeen.size() > 1) {
                                        Long lastTime = DateUtil.getCurrentTime(mLastTime);
                                        Long currentTime = DateUtil.getCurrentTime(chartRoom.getData().getTime());
                                        if (currentTime - lastTime >= SHOW_TIME_LONG_FULL) {
                                            // 显示年月日
                                            chartbean.setShowTime(currentTime - lastTime >= SHOW_TIME_LONG_FULL);
                                            chartbean.setTime(time);
                                        } else if (currentTime - lastTime >= SHOW_TIME_LONG) {
                                            // 显示时分秒
                                            chartbean.setShowTime(currentTime - lastTime >= SHOW_TIME_LONG);
                                            chartbean.setTime(time.split(" ")[1]);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Message msg = mHandler.obtainMessage();
                                msg.what = MSG_UPDATA_LIST;
                                msg.arg1 = chartRoom.getData().getMsgCode();
                                msg.obj = chartbean;
                                if (chartRoom.getData().getMsgCode() == 2 && AppConstants.register.getUser().getUserId().equals(chartRoom.getData().getToUser().getUserId())) {
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
                                        historyBeen.get(i).setTime(time);

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
                                                    historyBeen.get(i).setTime(time.split(" ")[1]);
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
//            }
//        }.start();
    }

    // 等adapter更新OK后再执行
    private void sleepView() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (historyBeen.size() != 0) {
                    if (isScrollBottom) {
                        recycler_view.smoothScrollToPosition(historyBeen.size() - 1);
                    }
                }
            }
        },500);
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
                        tv_call_me.setVisibility(View.INVISIBLE);
                        return;
                    }
                }
                break;
            case R.id.reLoading:
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
            case R.id.tv_new_msg:// 查看新消息
                tv_new_msg.setVisibility(View.GONE);
                isScrollBottom = true;
                sleepView();
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
        if (mHomeLike == null || mGuestLike == null) return;
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

    // 下拉刷新
    public void onRefresh() {
        if (historyBeen != null && historyBeen.size() != 0) {
            initData(SLIDE_TYPE_MSG_HISTORY);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            requestLikeData(ADDKEYHOME, "0", type);
        }
    }
}