package com.hhly.mlottery.frame.chartBallFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.chartBallAdapter.ChartBallAdapter;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.bean.BarrageBean;
import com.hhly.mlottery.bean.chart.ChartReceive;
import com.hhly.mlottery.bean.chart.ChartRoom;
import com.hhly.mlottery.bean.chart.SendMessageBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.greenrobot.event.EventBus;
import io.github.rockerhieu.emojicon.EmojiconEditText;


/**
 * 聊球页面
 * desc: 1.2.1版本新增功能，使用自己后台完成聊球功能
 * 作者：tangrr_107
 * 时间：2016/12/6
 */
public class ChartBallFragment extends BaseWebSocketFragment implements View.OnClickListener, ChartBallAdapter.AdapterListener {

    private static final int START_LOADING = 0;               // 开始加载状态
    private static final int SUCCESS_LOADING = 1;             // 加载成功
    private static final int ERROR_LOADING = 2;               // 加载失败
    private static final String MATCH_TYPE = "type";         // 赛事类型
    private static final String MATCH_THIRD_ID = "thirdId";  // 赛事ID

    private Activity mContext;                               // 上下文
    private View mView;                                      // 总布局
    private int type = -1;                                   // 1 籃球、0 足球
    private String mThirdId;                                 // 赛事id
    private EmojiconEditText mEditText;
    private RecyclerView recycler_view;
    private ChartBallAdapter mAdapter;
    private ChartBallReportDialogFragment dialogFragment;
    private ChartReceive mChartReceive;
    private List<List<ChartReceive.DataBean.ChatHistoryBean>> chartHistory;
    private List<ChartReceive.DataBean.ChatHistoryBean> historyBeen;
    private ImageView iv_not_chart_image;

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
        intiData();
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
    }

    private void initView() {
        mEditText = (EmojiconEditText) mView.findViewById(R.id.et_emoji_input);
        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(false);
        recycler_view = (RecyclerView) mView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setStackFromEnd(false);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);
        iv_not_chart_image = (ImageView) mView.findViewById(R.id.iv_not_chart_image);

        mView.findViewById(R.id.tv_send).setOnClickListener(this);
    }

    /*自定发送消息测试*/
    private void intiData() {
        mHandler.sendEmptyMessage(START_LOADING);// 正在加载数据中

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
        params.put("pageSize", "30");
        params.put("slideType", "0");

        VolleyContentFast.requestJsonByGet(URL, params,
                new VolleyContentFast.ResponseSuccessListener<ChartReceive>() {
                    @Override
                    public void onResponse(ChartReceive receive) {
                        if (receive.getResult().equals("200")) {

                            mChartReceive = receive;

                            mHandler.sendEmptyMessage(SUCCESS_LOADING);
                        } else {
                            mHandler.sendEmptyMessage(ERROR_LOADING);
                        }
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        mHandler.sendEmptyMessage(ERROR_LOADING);
                    }
                }, ChartReceive.class
        );
    }

    //发送消息后台
    private void sendMessage(String message, String toUserId) {

        Map<String, String> params = new HashMap<>();
        params.put("sourceName", "android");
        params.put("chatType", "football");
        params.put("thirdId", mThirdId);
        params.put("msgCode", "1");//1普通消息 2@消息 3系统消息 4在线人数 5进入聊天室
        params.put("loginToken", AppConstants.register.getData().getLoginToken());
        params.put("toUserId", "");
        params.put("deviceId", AppConstants.deviceToken);
        params.put("message ", message);
        params.put("msgId", UUID.randomUUID().toString());

        VolleyContentFast.requestStringByGet(BaseURLs.MESSAGE_SEND_FOOTBALL, params,null,
                new VolleyContentFast.ResponseSuccessListener<String>() {
                    @Override
                    public void onResponse(String receive) {
                        System.out.println("xxxxx 发送返回stringJson:" + receive);
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                        Log.i(TAG, "消息发送失败");
                    }
                });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_LOADING:
                    break;
                case SUCCESS_LOADING:
                    historyBeen = mChartReceive.getData().getChatHistory();
                    mAdapter = new ChartBallAdapter(mContext, historyBeen);
                    mAdapter.setShowDialogOnClickListener(ChartBallFragment.this);
                    recycler_view.setAdapter(mAdapter);

                    new Thread() {
                        @Override
                        public void run() {
                            SystemClock.sleep(1000);
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recycler_view.smoothScrollToPosition(historyBeen.size() - 1);
                                }
                            });
                        }
                    }.start();

                    if (historyBeen.size() == 0) {
                        iv_not_chart_image.setVisibility(View.VISIBLE);
                    } else {
                        iv_not_chart_image.setVisibility(View.GONE);
                    }

                    //开启socket推送
                    connectWebSocket();
                    break;
                case ERROR_LOADING:
                    // TODO 显示失败界面
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    public void onEventMainThread(ChartReceive.DataBean.ChatHistoryBean contentEntitiy) {
        // TODO 处理收到的数据
//        historyBeen.add(contentEntitiy);
//        mAdapter.notifyDataSetChanged();
//        recycler_view.smoothScrollToPosition(historyBeen.size() - 1);

        sendMessage(contentEntitiy.getMessage(), null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    protected void onTextResult(String text) {
        System.out.println("xxxxx 聊球推送：" + text);
        ChartRoom chartRoom = JSON.parseObject(text, ChartRoom.class);
        ChartReceive.DataBean.ChatHistoryBean chartbean = new ChartReceive.DataBean.ChatHistoryBean(chartRoom.getData().getMessage(), new ChartReceive.DataBean.ChatHistoryBean.FromUserBean(chartRoom.getData().getFromUser().getUserId()
                , chartRoom.getData().getFromUser().getUserLogo(), chartRoom.getData().getFromUser().getUserNick()));
        historyBeen.add(chartbean);
        mAdapter.notifyDataSetChanged();
        recycler_view.smoothScrollToPosition(historyBeen.size() - 1);
        iv_not_chart_image.setVisibility(View.GONE);

        // 收到消息，显示弹幕
        EventBus.getDefault().post(new BarrageBean(chartbean.getFromUser().getUserLogo(), chartbean.getMessage()));
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

            default:
                break;


        }

    }
}