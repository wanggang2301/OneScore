package com.hhly.mlottery.frame.snooker;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.snooker.SnookerRankAdapter;
import com.hhly.mlottery.bean.snookerbean.SnookerRankBean;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author wangg@13322.com
 * @desr 斯洛克排名
 * @date 2017/02/17
 */

public class SnookerRankFragment extends Fragment implements ExactSwipeRefreshLayout.OnRefreshListener {

    private static final int REQUEST_LOAD = 1;
    private static final int REQUEST_SUCESS = 2;
    private static final int REQUEST_ERROR = -1;
    private static final int REQUEST_NODATA = 0;
    private static final int PAGE_SIZE = 30;
    private int pageNum = 1;
    private View mView;
    private View moreView;
    private Context mContext;
    private RecyclerView recyclerView;
    private SnookerRankAdapter mSnookerRankAdapter;
    private List<SnookerRankBean.WorldRankingListBean> worldRankingList;
    private ExactSwipeRefreshLayout refresh;
    private LinearLayout ll_title;
    private TextView loadmore_text;
    private ProgressBar progressBar;
    private TextView network_exception_reload_btn;
    private FrameLayout fl_nodata;
    private LinearLayout ll_error;

    public SnookerRankFragment() {}

    public static SnookerRankFragment newInatance() {
        SnookerRankFragment snookerRankFragment = new SnookerRankFragment();
        return snookerRankFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_snooker_rank, container, false);
        moreView = inflater.inflate(R.layout.view_load_more, container, false);
        initView();
        mHandler.sendEmptyMessage(REQUEST_LOAD);
        new Handler().postDelayed(mLoadingDataThread, 500);
        return mView;
    }

    private Runnable mLoadingDataThread = new Runnable() {
        @Override
        public void run() {
            RequestData();
        }
    };

    private void initView() {
        refresh = (ExactSwipeRefreshLayout) mView.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);
        refresh.setColorSchemeResources(R.color.bg_header);
        refresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));
        ll_title = (LinearLayout) mView.findViewById(R.id.ll_title);
        fl_nodata = (FrameLayout) mView.findViewById(R.id.fl_nodata);
        ll_error = (LinearLayout) mView.findViewById(R.id.network_exception_layout);
        network_exception_reload_btn = (TextView) mView.findViewById(R.id.network_exception_reload_btn);
        loadmore_text = (TextView) moreView.findViewById(R.id.loadmore_text);
        progressBar = (ProgressBar) moreView.findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        network_exception_reload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(REQUEST_LOAD);
                new Handler().postDelayed(mLoadingDataThread, 500);
            }
        });
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_LOAD:
                    L.d("snooker", "REQUEST_LOAD");
                    refresh.setRefreshing(true);
                    refresh.setVisibility(View.VISIBLE);
                    ll_title.setVisibility(View.GONE);
                    fl_nodata.setVisibility(View.GONE);
                    ll_error.setVisibility(View.GONE);
                    break;

                case REQUEST_SUCESS:
                    L.d("snooker", "REQUEST_SUCESS");
                    refresh.setRefreshing(false);
                    ll_title.setVisibility(View.VISIBLE);
                    refresh.setVisibility(View.VISIBLE);
                    fl_nodata.setVisibility(View.GONE);
                    ll_error.setVisibility(View.GONE);
                    break;

                case REQUEST_NODATA:
                    L.d("snooker", "REQUEST_NODATA");

                    ll_title.setVisibility(View.GONE);
                    refresh.setVisibility(View.GONE);
                    refresh.setRefreshing(false);
                    fl_nodata.setVisibility(View.VISIBLE);
                    ll_error.setVisibility(View.GONE);

                    break;
                case REQUEST_ERROR:
                    L.d("snooker", "REQUEST_ERROR");
                    ll_title.setVisibility(View.GONE);
                    refresh.setVisibility(View.GONE);
                    refresh.setRefreshing(false);
                    fl_nodata.setVisibility(View.GONE);
                    ll_error.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };


    private void RequestData() {
        //BaseURLs.URL_SNOOKER_INFO_RANK

        String url = "http://192.168.33.10:8080/mlottery/core/snookerWorldRanking.getWorldRankingPaging.do";

        pageNum = 1;
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", pageNum + "");
        map.put("pageSize", PAGE_SIZE + "");
        VolleyContentFast.requestJsonByGet(url, map, new VolleyContentFast.ResponseSuccessListener<SnookerRankBean>() {
            @Override
            public void onResponse(SnookerRankBean jsonObject) {
                if (jsonObject != null) {
                    if (jsonObject.getResult() == 200) {

                        worldRankingList = jsonObject.getWorldRankingList();
                        L.d("snooker", "成功");
                        initViewData();
                        mHandler.sendEmptyMessage(REQUEST_SUCESS);
                        //请求成功

                    } else if (jsonObject.getResult() == 400) {
                        //无数据
                        mHandler.sendEmptyMessage(REQUEST_NODATA);
                    } else if (jsonObject.getResult() == 500) {
                        //错误
                        mHandler.sendEmptyMessage(REQUEST_ERROR);
                    }
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(REQUEST_ERROR);

            }
        }, SnookerRankBean.class);
    }

    private void initViewData() {
        mSnookerRankAdapter = new SnookerRankAdapter(mContext, worldRankingList);
        recyclerView.setAdapter(mSnookerRankAdapter);

        mSnookerRankAdapter.openLoadMore(0, true);
        mSnookerRankAdapter.setLoadingView(moreView);
        mSnookerRankAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        pullUpLoadMoreData();
                    }
                });
            }
        });
    }


    //上拉加载更多
    private void pullUpLoadMoreData() {
        L.d("snooker", "pullUpLoadMoreData");

        //        //BaseURLs.URL_SNOOKER_INFO_RANK

        pageNum++;
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", pageNum + "");
        map.put("pageSize", PAGE_SIZE + "");
        String url = "http://192.168.33.10:8080/mlottery/core/snookerWorldRanking.getWorldRankingPaging.do";
        VolleyContentFast.requestJsonByGet(url, map, new VolleyContentFast.ResponseSuccessListener<SnookerRankBean>() {
                    @Override
                    public void onResponse(SnookerRankBean jsonObject) {
                        if (jsonObject != null) {
                            if (jsonObject.getResult() != 200) {
                                loadmore_text.setText(mContext.getResources().getString(R.string.nodata_txt));
                                progressBar.setVisibility(View.GONE);
                                return;
                            } else {
                                loadmore_text.setText(mContext.getResources().getString(R.string.loading_data_txt));
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        }

                        worldRankingList.addAll(jsonObject.getWorldRankingList());
                        mSnookerRankAdapter.notifyDataChangedAfterLoadMore(true);
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                    }
                }, SnookerRankBean.class
        );
    }

    @Override
    public void onRefresh() {
        L.d("snooker", "onRefresh");
        //worldRankingList.clear();
        new Handler().postDelayed(mLoadingDataThread, 500);
    }
}
