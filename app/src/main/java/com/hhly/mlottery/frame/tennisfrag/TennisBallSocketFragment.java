package com.hhly.mlottery.frame.tennisfrag;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.tennisball.TennisBallScoreAdapter;
import com.hhly.mlottery.bean.tennisball.MatchDataBean;
import com.hhly.mlottery.bean.tennisball.TennisImmediatelyBean;
import com.hhly.mlottery.bean.tennisball.TennisSocketBean;
import com.hhly.mlottery.callback.TennisFocusCallBack;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
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

public class TennisBallSocketFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final int LOADING = 1;
    private static final int SUCCESS = 2;
    private static final int SUCCESS_AGAIN = 3;
    private static final int ERROR = 4;
    private static final int NOTO_DATA = 5;

    private static final String TENNIS_TYPE = "tennis_type";
    private int type;

    private Context mContext;
    private View mView;
    private View mEmptyView;
    private View mErrorLayout;
    private TextView mRefreshTextView;
    private TextView mNoDataTextView;
    private LinearLayout no_focus;

    private RecyclerView tennis_recycler;
    private List<MatchDataBean> mData = new ArrayList<>();
    private ExactSwipeRefreshLayout swipeRefreshLayout;
    private TennisBallScoreAdapter mAdapter;

    private String tennisUrl;
    private TennisFocusCallBack tennisFocusCallBack;

    public static TennisBallSocketFragment newInstance(int type) {
        TennisBallSocketFragment fragment = new TennisBallSocketFragment();
        Bundle args = new Bundle();
        args.putInt(TENNIS_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        mEmptyView = View.inflate(mContext, R.layout.tennis_empty_layout, null);

        initView();
        initEmptyView();
        initData(0);
        initEvent();
        return mView;
    }

    private void initEvent() {
        tennisFocusCallBack = new TennisFocusCallBack() {
            @Override
            public void notifyDataRefresh() {
                setStatus(NOTO_DATA);
            }
        };
        mAdapter.setTennisFocusCallBack(tennisFocusCallBack);

        mRefreshTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(0);
            }
        });
    }

    private void initData(final int start) {

        if (start == 0) {
            setStatus(LOADING);
        }

        Map<String, String> map = new HashMap<>();

        if (type == 0) {
            tennisUrl = BaseURLs.TENNIS_IMMEDIATE_URL;
        } else if (type == 3) {
            tennisUrl = BaseURLs.TENNIS_FOCUS_URL;
            String focusId = PreferenceUtil.getString(AppConstants.TENNIS_BALL_FOCUS, null);

            if (!TextUtils.isEmpty(focusId)) {
                map.put("matchIds", focusId);
            } else {
                setStatus(NOTO_DATA);
                return;
            }
        }

        VolleyContentFast.requestJsonByGet(tennisUrl, map, new VolleyContentFast.ResponseSuccessListener<TennisImmediatelyBean>() {
            @Override
            public void onResponse(TennisImmediatelyBean jsonObject) {
                if (jsonObject.getResult() == 200) {
                    mData.clear();
                    mData.addAll(jsonObject.getData());

                    if (mData.size() == 0) {
                        setStatus(NOTO_DATA);
                    } else {
                        setStatus(SUCCESS);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    setStatus(ERROR);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                setStatus(ERROR);
            }
        }, TennisImmediatelyBean.class);
    }

    // 设置页面显示状态
    private void setStatus(int status) {
        L.d("xxxxx", "socketFrag status:  " + status);
        if(status == ERROR){
            mData.clear();
            mAdapter.notifyDataSetChanged();
        }
        if (type == 3) {
            no_focus.setVisibility(status == NOTO_DATA ? View.VISIBLE : View.GONE);
        } else {
            mNoDataTextView.setVisibility(status == NOTO_DATA ? View.VISIBLE : View.GONE);
        }
        swipeRefreshLayout.setRefreshing(status == LOADING);
        mErrorLayout.setVisibility(status == ERROR ? View.VISIBLE : View.GONE);
    }

    private void initView() {
        mEmptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mView.findViewById(R.id.tennis_date_content).setVisibility(View.GONE);
        tennis_recycler = (RecyclerView) mView.findViewById(R.id.tennis_recycler);
        mAdapter = new TennisBallScoreAdapter(R.layout.item_tennis_score, mData, type);
        tennis_recycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(mEmptyView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        tennis_recycler.setLayoutManager(layoutManager);

        swipeRefreshLayout = (ExactSwipeRefreshLayout) mView.findViewById(R.id.tennis_swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));
    }

    private void initEmptyView() {
        mErrorLayout = mEmptyView.findViewById(R.id.error_layout);
        mRefreshTextView = (TextView) mEmptyView.findViewById(R.id.reloading_txt);
        mNoDataTextView = (TextView) mEmptyView.findViewById(R.id.no_data_txt);
        no_focus = (LinearLayout) mEmptyView.findViewById(R.id.no_focus);
    }

    @Override
    public void onRefresh() {
        initData(0);
    }

    public void refreshData() {
        initData(-1);
    }

    // webSocket
    public void socketDataChanged(final String text) {
        new Thread() {
            @Override
            public void run() {
                TennisSocketBean tennisSocketBean = JSON.parseObject(text, TennisSocketBean.class);
                if (tennisSocketBean.getType() == 1) {
                    for (MatchDataBean matchDataBean : mData) {
                        if (matchDataBean.getMatchId().equals(tennisSocketBean.getDataObj().getMatchId())) {
                            matchDataBean.setMatchStatus(tennisSocketBean.getDataObj().getMatchStatus());
                            matchDataBean.getMatchScore().setHomeSetScore1(tennisSocketBean.getDataObj().getMatchScore().getHomeSetScore1());
                            matchDataBean.getMatchScore().setHomeSetScore2(tennisSocketBean.getDataObj().getMatchScore().getHomeSetScore2());
                            matchDataBean.getMatchScore().setHomeSetScore3(tennisSocketBean.getDataObj().getMatchScore().getHomeSetScore3());
                            matchDataBean.getMatchScore().setHomeSetScore4(tennisSocketBean.getDataObj().getMatchScore().getHomeSetScore4());
                            matchDataBean.getMatchScore().setHomeSetScore5(tennisSocketBean.getDataObj().getMatchScore().getHomeSetScore5());
                            matchDataBean.getMatchScore().setAwaySetScore1(tennisSocketBean.getDataObj().getMatchScore().getAwaySetScore1());
                            matchDataBean.getMatchScore().setAwaySetScore2(tennisSocketBean.getDataObj().getMatchScore().getAwaySetScore2());
                            matchDataBean.getMatchScore().setAwaySetScore3(tennisSocketBean.getDataObj().getMatchScore().getAwaySetScore3());
                            matchDataBean.getMatchScore().setAwaySetScore4(tennisSocketBean.getDataObj().getMatchScore().getAwaySetScore4());
                            matchDataBean.getMatchScore().setAwaySetScore5(tennisSocketBean.getDataObj().getMatchScore().getAwaySetScore5());
                            matchDataBean.getMatchScore().setHomeTotalScore(tennisSocketBean.getDataObj().getMatchScore().getHomeTotalScore());
                            matchDataBean.getMatchScore().setAwayTotalScore(tennisSocketBean.getDataObj().getMatchScore().getAwayTotalScore());
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                        return;
                    }
                }
            }
        }.start();
    }
}
