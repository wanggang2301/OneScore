package com.hhly.mlottery.frame.tennisfrag;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.tennisball.TennisBallScoreAdapter;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.bean.tennisball.MatchDataBean;
import com.hhly.mlottery.bean.tennisball.TennisBallBean;
import com.hhly.mlottery.bean.tennisball.TennisImmediatelyBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * desc:网球比分列表即时和关注页面
 * Created by 107_tangrr on 2017/2/20 0020.
 */

public class TennisBallSocketFragment extends BaseWebSocketFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final int LOADING = 1;
    private static final int SUCCESS = 2;
    private static final int SUCCESS_AGAIN = 3;
    private static final int ERROR = 4;
    private static final String TENNIS_TYPE = "tennis_type";
    private int type;

    private Context mContext;
    private View mView;
    private RecyclerView tennis_recycler;
    private List<MatchDataBean> mData = new ArrayList<>();
    private ExactSwipeRefreshLayout swipeRefreshLayout;
    private TennisBallScoreAdapter mAdapter;

    private String tennisUrl;

    public static TennisBallSocketFragment newInstance(int type) {
        TennisBallSocketFragment fragment = new TennisBallSocketFragment();
        Bundle args = new Bundle();
        args.putInt(TENNIS_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic("USER.topic.tennis.score");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(TENNIS_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mContext = getActivity();
        mView = View.inflate(mContext, R.layout.fragment_tennls_ball_tab, null);

        initView();
        initData(0);

        return mView;
    }

    private void initData(final int start) {

        if (start == 0) {
            mHandler.sendEmptyMessage(LOADING);
        }

        Map<String, String> map = new HashMap<>();

        if (type == 0) {
            tennisUrl = BaseURLs.TENNIS_IMMEDIATE_URL;
        } else if (type == 3) {
            tennisUrl = BaseURLs.TENNIS_FOCUS_URL;
            String focusId = PreferenceUtil.getString(AppConstants.TENNIS_BALL_FOCUS, null);
            if (focusId != null) {
                map.put("matchIds", focusId);
            } else {
                swipeRefreshLayout.setRefreshing(false);
                // TODO 显示暂无关注页面
                ToastTools.showQuick(mContext, "暂无关注！");
                return;
            }
        }

        VolleyContentFast.requestJsonByGet(tennisUrl, map, new VolleyContentFast.ResponseSuccessListener<TennisImmediatelyBean>() {
            @Override
            public void onResponse(TennisImmediatelyBean jsonObject) {
                if (jsonObject.getResult() == 200) {
                    // 成功
                    mData.clear();
                    mData.addAll(jsonObject.getData());

                    if (mAdapter == null) {
                        mAdapter = new TennisBallScoreAdapter(R.layout.item_tennis_score, mData, type);
                        tennis_recycler.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                    if (start == 0) {
                        mHandler.sendEmptyMessage(SUCCESS);
                    }
                } else {
                    // 失败
                    mHandler.sendEmptyMessage(ERROR);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                // 失败
                mHandler.sendEmptyMessage(ERROR);
            }
        }, TennisImmediatelyBean.class);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    swipeRefreshLayout.setRefreshing(true);
                    break;
                case SUCCESS:
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case ERROR:
                    swipeRefreshLayout.setRefreshing(false);
                    ToastTools.showQuickCenter(mContext, "请求失败！");
                    break;
            }
        }
    };

    private void initView() {
        mView.findViewById(R.id.tennis_date_content).setVisibility(View.GONE);
        tennis_recycler = (RecyclerView) mView.findViewById(R.id.tennis_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        tennis_recycler.setLayoutManager(layoutManager);
        swipeRefreshLayout = (ExactSwipeRefreshLayout) mView.findViewById(R.id.tennis_swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));
    }

    @Override
    public void onRefresh() {
        initData(0);
    }

    @Override
    protected void onTextResult(String text) {
        // 网球推送
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

    public void refreshData() {
        initData(-1);
    }
}
