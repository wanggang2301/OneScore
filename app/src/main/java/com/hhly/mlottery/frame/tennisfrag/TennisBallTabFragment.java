package com.hhly.mlottery.frame.tennisfrag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.TennisBallDetailsActivity;
import com.hhly.mlottery.adapter.tennisball.TennisBallScoreAdapter;
import com.hhly.mlottery.bean.tennisball.MatchDataBean;
import com.hhly.mlottery.bean.tennisball.TennisBallBean;
import com.hhly.mlottery.bean.tennisball.TennisOddsInfoBean;
import com.hhly.mlottery.bean.tennisball.TennisSocketOddsBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * desc:网球比分列表Tab页面
 * Created by 107_tangrr on 2017/2/20 0020.
 */

public class TennisBallTabFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final int LOADING = 1;
    private static final int SUCCESS = 2;
    private static final int SUCCESS_AGAIN = 3;
    private static final int ERROR = 4;
    private static final int NOTO_DATA = 5;

    private static final String TENNIS_TYPE = "tennis_type";
    private int type;

    private Activity mContext;
    private View mView;
    private View mEmptyView;
    private View mErrorLayout;
    private TextView mRefreshTextView;
    private TextView mNoDataTextView;

    private RecyclerView tennis_recycler;
    private TextView tv_date;
    private List<MatchDataBean> mData = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private ExactSwipeRefreshLayout swipeRefreshLayout;
    private TennisBallScoreAdapter mAdapter;
    private TennisDateChooseDialogFragment tennisDateChooseDialogFragment;

    private String currentData;
    private String tennisUrl;
    private LinearLayout tennis_date_content;

    public static TennisBallTabFragment newInstance(int type) {
        TennisBallTabFragment fragment = new TennisBallTabFragment();
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

        mView = View.inflate(mContext, R.layout.fragment_tennls_ball_tab, null);
        mEmptyView = inflater.inflate(R.layout.tennis_empty_layout, container, false);

        initView();
        initEmptyView();
        initData();
        initEvent();
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    private void initEvent() {
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(mContext, TennisBallDetailsActivity.class);
                intent.putExtra("thirdId", mData.get(i).getMatchId());
                getParentFragment().startActivityForResult(intent, 2);
            }
        });
    }

    private void initData() {

        setStatus(LOADING);

        if (type == 1) {
            tennisUrl = BaseURLs.TENNIS_RESULT_URL;
        } else if (type == 2) {
            tennisUrl = BaseURLs.TENNIS_SCHEDULE_URL;
        }

        Map<String, String> map = new HashMap<>();
        if (currentData != null) {
            map.put("date", currentData);
        }

        VolleyContentFast.requestJsonByGet(tennisUrl, map, new VolleyContentFast.ResponseSuccessListener<TennisBallBean>() {
            @Override
            public void onResponse(TennisBallBean jsonObject) {
                if (jsonObject.getResult() == 200) {
                    mData.clear();
                    dates.clear();
                    mData.addAll(jsonObject.getData().getMatchData());
                    dates.addAll(jsonObject.getData().getDates());

                    if (dates.size() != 0 && currentData == null) {
                        settingData(dates.get(0));
                    }

                    if (mData.size() == 0) {
                        setStatus(NOTO_DATA);
                    } else {
                        settingOddsStart();
                        mAdapter.notifyDataSetChanged();
                        setStatus(SUCCESS);
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
        }, TennisBallBean.class);
    }

    // 设置页面显示状态
    private void setStatus(int status) {
        if (status == ERROR || status == NOTO_DATA) {
            mData.clear();
            mAdapter.notifyDataSetChanged();
        }

        mNoDataTextView.setVisibility(status == NOTO_DATA ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setRefreshing(status == LOADING);
        mErrorLayout.setVisibility(status == ERROR ? View.VISIBLE : View.GONE);
        tennis_date_content.setVisibility(status == ERROR ? View.GONE : View.VISIBLE);
    }

    private void initView() {
        mEmptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        tennis_date_content = (LinearLayout) mView.findViewById(R.id.tennis_date_content);
        tennis_recycler = (RecyclerView) mView.findViewById(R.id.tennis_recycler);
        tv_date = (TextView) mView.findViewById(R.id.tv_date);
        tv_date.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        tennis_recycler.setLayoutManager(layoutManager);
        mAdapter = new TennisBallScoreAdapter(R.layout.item_tennis_score, mData, type);
        tennis_recycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(mEmptyView);

        swipeRefreshLayout = (ExactSwipeRefreshLayout) mView.findViewById(R.id.tennis_swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));
    }

    private void initEmptyView() {
        mErrorLayout = mEmptyView.findViewById(R.id.error_layout);
        mRefreshTextView = (TextView) mEmptyView.findViewById(R.id.reloading_txt);
        mNoDataTextView = (TextView) mEmptyView.findViewById(R.id.no_data_txt);
        mRefreshTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_date:// 日期选择
                if (!TextUtils.isEmpty(currentData)) {
                    dateChooseShow();
                }
                break;
        }
    }

    private void dateChooseShow() {
        tennisDateChooseDialogFragment = TennisDateChooseDialogFragment.newInstance(dates, currentData, new TennisDateChooseDialogFragment.OnDateChooseListener() {
            @Override
            public void onDateChoose(String date) {
                if (TextUtils.isEmpty(currentData) || !currentData.equals(date)) {
                    settingData(date);
                    initData();
                }
            }
        });
        if (!tennisDateChooseDialogFragment.isVisible()) {
            tennisDateChooseDialogFragment.show(getChildFragmentManager(), "tennisDateChooseDialogFragment");
        }
    }

    // 设置日期
    private void settingData(String data) {
        currentData = data;
        try {
            tv_date.setText(DateUtil.convertDateToNation(data) + " " + DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(data)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshData() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    // 指数状态变化
    public void oddsChanger() {
        if (mAdapter != null) {
            L.d("tennis", "指数状态变化刷新");
            settingOddsStart();
            mAdapter.notifyDataSetChanged();
        }
    }

    // 指数数据推送变化
    public void oddsDataChanger(final String text) {
        new Thread() {
            @Override
            public void run() {
                try {
                    TennisSocketOddsBean oddsBean = JSON.parseObject(text, TennisSocketOddsBean.class);
                    // 402 比分列表赔率主题，403 指数赔率列表
                    if (oddsBean != null && oddsBean.getType() == 402) {
                        boolean alet = PreferenceUtil.getBoolean(MyConstants.TENNIS_ALET, true); //亚盘
                        boolean eur = PreferenceUtil.getBoolean(MyConstants.TENNIS_EURO, false);//欧赔
                        //  1 亚盘 ，2 欧赔 ，3 大小球
                        int gameType = oddsBean.getDataObj().getGameType();
                        for (int i = 0; i < mData.size(); i++) {
                            if (mData.get(i).getMatchId().equals(oddsBean.getDataObj().getMatchId())) {
                                if (alet && gameType == 1) {
                                    TennisOddsInfoBean asiaLet = mData.get(i).getMatchOdds().getAsiaLet();
                                    asiaLet.setL(oddsBean.getDataObj().getMatchOdd().getL());
                                    asiaLet.setM(oddsBean.getDataObj().getMatchOdd().getM());
                                    asiaLet.setR(oddsBean.getDataObj().getMatchOdd().getR());
                                } else if (eur && gameType == 2) {
                                    TennisOddsInfoBean euro = mData.get(i).getMatchOdds().getEuro();
                                    euro.setL(oddsBean.getDataObj().getMatchOdd().getL());
                                    euro.setM(oddsBean.getDataObj().getMatchOdd().getM());
                                    euro.setR(oddsBean.getDataObj().getMatchOdd().getR());
                                } else if (eur && gameType == 3) {
                                    TennisOddsInfoBean asiaSize = mData.get(i).getMatchOdds().getAsiaSize();
                                    asiaSize.setL(oddsBean.getDataObj().getMatchOdd().getL());
                                    asiaSize.setM(oddsBean.getDataObj().getMatchOdd().getM());
                                    asiaSize.setR(oddsBean.getDataObj().getMatchOdd().getR());
                                }
                                mContext.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mAdapter != null) {
                                            L.d("tennis", "网球赔率推送,更新了!");
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // 设置赔率状态变化
    private void settingOddsStart() {
        for (int i = 0; i < mData.size(); i++) {
            if (PreferenceUtil.getBoolean(MyConstants.TENNIS_ALET, true)) {// 亚盘
                mData.get(i).setOddsType("alet");
            } else if (PreferenceUtil.getBoolean(MyConstants.TENNIS_EURO, false)) {// 欧赔
                mData.get(i).setOddsType("eur");
            } else if (PreferenceUtil.getBoolean(MyConstants.TENNIS_NOTSHOW, false)) {// 不显示
                mData.get(i).setOddsType("noshow");
            } else if (PreferenceUtil.getBoolean(MyConstants.TENNIS_ASIZE, false)) {// 大小球
                mData.get(i).setOddsType("asize");
            }
        }
    }
}
