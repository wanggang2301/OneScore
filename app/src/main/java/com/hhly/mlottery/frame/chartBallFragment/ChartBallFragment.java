package com.hhly.mlottery.frame.chartBallFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.ChartBallAdapter;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.bean.BarrageBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.footframe.eventbus.ChartBallContentEntitiy;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.ToastTools;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.github.rockerhieu.emojicon.EmojiconEditText;


/**
 * 聊球页面
 * desc: 1.2.1版本新增功能，使用自己后台完成聊球功能
 * 作者：tangrr_107
 * 时间：2016/12/6
 */
public class ChartBallFragment extends BaseWebSocketFragment implements View.OnClickListener{

    private final static String MATCH_TYPE = "type";         // 赛事类型
    private final static String MATCH_THIRD_ID = "thirdId";  // 赛事ID

    private Activity mContext;                               // 上下文
    private View mView;                                      // 总布局
    private int type = -1;                                   // 1 籃球、0 足球
    private String mThirdId;                                 // 赛事id
    private EmojiconEditText mEditText;
    private RecyclerView recycler_view;
    private ChartBallAdapter mAdapter;
    private EmojiconEditText et_emoji_input;

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

        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                ToastTools.showQuick(mContext, "xxxxxx " + i);
            }
        });

    }

    List<String> mData = new ArrayList<>();

    private void initView() {
        mEditText = (EmojiconEditText) mView.findViewById(R.id.et_emoji_input);
        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(false);
        InputMethodManager inputManager = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mEditText, 0);

        recycler_view = (RecyclerView) mView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);

        mData.add("aaaa");
        mData.add("bbbbb");
        mData.add("ccccc");
        mData.add("ddddd");
        mData.add("eeeee");
        mData.add("fffff");
        mData.add("ggggg");
        mData.add("eeeee");

        mAdapter = new ChartBallAdapter(mContext, R.layout.item_char_ball_content, mData);
        recycler_view.setAdapter(mAdapter);
        //聊天输入框
        et_emoji_input = (EmojiconEditText) mView.findViewById(R.id.et_emoji_input);

        mView.findViewById(R.id.tv_send).setOnClickListener(this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    public void onEventMainThread(ChartBallContentEntitiy contentEntitiy) {
        System.out.println("xxxxx 足球收到了：" + contentEntitiy.getContent());
        // TODO 处理收到的数据

        mData.add(contentEntitiy.getContent());
        mAdapter.notifyDataSetChanged();
        recycler_view.smoothScrollToPosition(mData.size() - 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onTextResult(String text) {

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