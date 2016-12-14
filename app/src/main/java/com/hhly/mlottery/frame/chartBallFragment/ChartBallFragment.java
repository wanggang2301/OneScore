package com.hhly.mlottery.frame.chartBallFragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.chartBallAdapter.ChartBallAdapter;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewHolder;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.bean.chart.ChartReceive;
import com.hhly.mlottery.bean.chart.ChartRoom;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.footframe.eventbus.ChartBallContentEntitiy;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.util.net.VolleyContentFast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import io.github.rockerhieu.emojicon.EmojiconEditText;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * 聊球页面
 * desc: 1.2.1版本新增功能，使用自己后台完成聊球功能
 * 作者：tangrr_107
 * 时间：2016/12/6
 */
public class ChartBallFragment extends BaseWebSocketFragment implements View.OnClickListener, ChartBallAdapter.AdapterListener{

    private final static String MATCH_TYPE = "type";         // 赛事类型
    private final static String MATCH_THIRD_ID = "thirdId";  // 赛事ID

    private Activity mContext;                               // 上下文
    private View mView;                                      // 总布局
    private int type = -1;                                   // 1 籃球、0 足球
    private String mThirdId;                                 // 赛事id
    private EmojiconEditText mEditText;
    private RecyclerView recycler_view;
    private ChartBallAdapter mAdapter;
    private ChartBallReportDialogFragment dialogFragment;
    private EmojiconEditText et_emoji_input;
    private List<List<ChartReceive.DataBean.ChatHistoryBean>> chartHistory;
    private List<ChartReceive.DataBean.ChatHistoryBean> historyBeen;

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
        setTopic("USER.topic.chatroom.football"+mThirdId);
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_chartball, container, false);
        initView();
        intiData();
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

        mAdapter.setOnItemClickListener(new BaseRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                ToastTools.showQuick(mContext,"点击 ："+position);
            }
        });
    }

    private void initView() {
        mEditText = (EmojiconEditText) mView.findViewById(R.id.et_emoji_input);
        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(false);
        InputMethodManager inputManager = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mEditText, 0);

        recycler_view = (RecyclerView) mView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setStackFromEnd(false);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);




        //聊天输入框
        et_emoji_input = (EmojiconEditText) mView.findViewById(R.id.et_emoji_input);

        mView.findViewById(R.id.tv_send).setOnClickListener(this);

    }
    /*自定发送消息测试*/
    private void intiData() {

        if (getActivity() == null) {
            return;
        }

        L.d("ddd", "加载数据");
        // mHandler.sendEmptyMessage(STARTLOADING);// 正在加载数据中

        Map<String, String> params = new HashMap<>();
        params.put("chatType", "football");
        params.put("thirdId", mThirdId);
        params.put("pageSize", "4");
        params.put("slideType", "0");

        //String url = "http://192.168.10.242:8181/mlottery/core/footballBallList.ballListOverview.do";
        VolleyContentFast.requestJsonByGet(BaseURLs.MESSAGE_LIST, params,
                new VolleyContentFast.ResponseSuccessListener<ChartReceive>() {
                    @Override
                    public void onResponse(ChartReceive receive) {
                        if (!receive.getResult().equals("200")) {
                            Log.i("sfsfdgdfgfdgfd","sile");
                            return;
                        }

                        historyBeen = receive.getData().getChatHistory();
                        mAdapter = new ChartBallAdapter(mContext,receive.getData().getChatHistory());
                        recycler_view.setAdapter(mAdapter);
                        recycler_view.smoothScrollToPosition(historyBeen.size()-1);
                        initEvent();
                        //开启socket推送
                        connectWebSocket();
                        // isSocketStart = false;
                        //mHandler.sendEmptyMessage(SUCCESS);
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        // mHandler.sendEmptyMessage(ERROR);
                        Log.i("sfsfdgdfgfdgfd","获取列表数据失败");
                    }
                }, ChartReceive.class
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    public void onEventMainThread(ChartReceive.DataBean.ChatHistoryBean contentEntitiy) {
     //   System.out.println("xxxxx 足球收到了：" + contentEntitiy.getContent());
        // TODO 处理收到的数据
        historyBeen.add(contentEntitiy);
        mAdapter.notifyDataSetChanged();
        recycler_view.smoothScrollToPosition(historyBeen.size() - 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void shwoDialog(String id) {
        dialogFragment = ChartBallReportDialogFragment.newInstance("asa",id,"hhly982121","猪");
        if(!dialogFragment.isVisible()){
            dialogFragment.show(getChildFragmentManager(),"chartballDialog");
        }
    }

    @Override
    protected void onTextResult(String text) {
        Log.i("sdasda","sdad"+text.toString());

        ChartRoom chartRoom = JSON.parseObject(text, ChartRoom.class);
        ChartReceive.DataBean.ChatHistoryBean chartbean= new ChartReceive.DataBean.ChatHistoryBean(chartRoom.getData().getMessage(),new ChartReceive.DataBean.ChatHistoryBean.FromUserBean(chartRoom.getData().getFromUser().getUserId()
                ,chartRoom.getData().getFromUser().getUserLogo(),chartRoom.getData().getFromUser().getUserNick()));
        historyBeen.add(chartbean);
        mAdapter.notifyDataSetChanged();
        recycler_view.smoothScrollToPosition(historyBeen.size() - 1);
        /*     Log.i("sdasd","chartReceive"+chartRoom.getData().getFromUser().getUserNick());
        Log.i("sdasd","chartReceive"+chartRoom.getData().getFromUser().getUserId());
        Log.i("sdasd","chartReceive"+chartRoom.getData().getFromUser().getUserLogo());*/
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

        switch (v.getId()){

            default:
                break;


        }

    }
}