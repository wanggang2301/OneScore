package com.hhly.mlottery.frame.numbersframe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.homePagerAdapter.HKLotteryInfoChartAdapter;
import com.hhly.mlottery.bean.numbersBean.LotteryInfoHKChartBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * desc:香港开奖图表fragment
 * Created by 107_tangrr on 2017/1/11 0011.
 */

public class HKLotteryChartFragment extends Fragment {

    private static final int LOADING = 0;
    private static final int SUCCESS = 1;
    private static final int ERROR = 2;
    private Context mContext;
    private View mView;
    private RecyclerView mRecyclerView;
    private HKLotteryInfoChartAdapter mAdapter;
    private List<LotteryInfoHKChartBean.DataBean> mData = new ArrayList<>();
    private FrameLayout fl_content;
    private FrameLayout fl_notNet;
    private TextView tv_reLoading;
    private int currentPage = 1;

    private View moreView; //加载更多
    private TextView loadmore_text;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.home_info_hk_chart_pager, container, false);
        moreView = inflater.inflate(R.layout.view_load_more, container, false);

        initView();
        initData(0);
        initEvent();
        return mView;
    }

    private void initEvent() {
        tv_reLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage = 1;
                initData(0);
            }
        });
    }

    private void initData(final int start) {
        mHandler.sendEmptyMessage(LOADING);

        if (start == 1) {
            currentPage++;
        }
        Map<String, String> map = new HashMap<>();
        map.put("platform", "android");
        map.put("infoType", "15");
        map.put("currentPage", String.valueOf(currentPage));

        VolleyContentFast.requestJsonByGet(BaseURLs.LOTTERY_INFO_CHART_URL, map, new VolleyContentFast.ResponseSuccessListener<LotteryInfoHKChartBean>() {
            @Override
            public void onResponse(LotteryInfoHKChartBean jsonObject) {
                if (jsonObject != null && jsonObject.getResult() == 200) {
                    if (start == 1) {
                        if (jsonObject.getData().size() == 0) {
                            loadmore_text.setText(mContext.getResources().getString(R.string.nodata_txt));
                            progressBar.setVisibility(View.GONE);
                            return;
                        } else {
                            loadmore_text.setText(mContext.getResources().getString(R.string.loading_data_txt));
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        mData.addAll(jsonObject.getData());
                        mAdapter.notifyDataChangedAfterLoadMore(true);
                    } else {
                        mData = jsonObject.getData();
                        mHandler.sendEmptyMessage(SUCCESS);
                    }
                } else {
                    mHandler.sendEmptyMessage(ERROR);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(ERROR);
            }
        }, LotteryInfoHKChartBean.class);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.chart_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);

        fl_content = (FrameLayout) mView.findViewById(R.id.fl_content);
        fl_notNet = (FrameLayout) mView.findViewById(R.id.fl_current_notNet);
        tv_reLoading = (TextView) mView.findViewById(R.id.tv_current_reLoading);

        loadmore_text = (TextView) moreView.findViewById(R.id.loadmore_text);
        progressBar = (ProgressBar) moreView.findViewById(R.id.progressBar);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    break;
                case SUCCESS:
                    fl_content.setVisibility(View.VISIBLE);
                    fl_notNet.setVisibility(View.GONE);

                    mAdapter = new HKLotteryInfoChartAdapter(mContext, R.layout.number_hk_info_chart_item, mData);
                    mAdapter.openLoadMore(0, true);
                    mAdapter.setLoadingView(moreView);

                    mRecyclerView.setAdapter(mAdapter);
//                    mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int i) {
//                            Intent intent = new Intent(mContext, LotteryHKInfoImageActivity.class);
//                            intent.putExtra("data", (Serializable) mData);
//                            intent.putExtra("index", i);
//                            mContext.startActivity(intent);
//                        }
//                    });

                    //上拉加载更多
                    mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                        @Override
                        public void onLoadMoreRequested() {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    initData(1);
                                }
                            });
                        }
                    });

                    break;
                case ERROR:
                    fl_content.setVisibility(View.GONE);
                    fl_notNet.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
}
