package com.hhly.mlottery.frame.numbersframe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.NumbersActivity;
import com.hhly.mlottery.adapter.homePagerAdapter.HKLotteryInfoChartAdapter;
import com.hhly.mlottery.bean.numbersBean.LotteryInfoHKChartBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * desc:香港开奖图表fragment
 * Created by 107_tangrr on 2017/1/11 0011.
 */

public class HKLotteryChartFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final int LOADING = 0;
    private static final int SUCCESS = 1;
    private static final int ERROR = 2;
    private Context mContext;
    private View mView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FrameLayout infoContent;
    private RecyclerView mRecyclerView;
    private HKLotteryInfoChartAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.home_info_hk_chart_pager, container, false);

        initView();
        initData();
        initEvent();
        return mView;
    }

    private void initEvent() {
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                ToastTools.showQuick(mContext, "下标：" + i);
                // TODO 跳转到详情
            }
        });
    }

    private void initData() {
        mHandler.sendEmptyMessage(LOADING);

        Map<String, String> map = new HashMap<>();
        map.put("platform", "android");
        map.put("infoType", "15");
        map.put("currentPage", "1");

        VolleyContentFast.requestJsonByGet(BaseURLs.LOTTERY_INFO_CHART_URL, map, new VolleyContentFast.ResponseSuccessListener<LotteryInfoHKChartBean>() {
            @Override
            public void onResponse(LotteryInfoHKChartBean jsonObject) {
                if (jsonObject != null && jsonObject.getResult() == 200) {

                    mHandler.sendEmptyMessage(SUCCESS);
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
        swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipere_fresh_layout_chart);
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.chart_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);

        List<String> list = new ArrayList<>();
        list.add("111111111111");
        list.add("2222222222222");
        list.add("33333333333");
        list.add("4444444444444444444");
        list.add("5555555");
        // TODO 添加后台真实数据

        mAdapter = new HKLotteryInfoChartAdapter(mContext, R.layout.number_hk_info_chart_item, list);
        mRecyclerView.setAdapter(mAdapter);
        infoContent = (FrameLayout) mView.findViewById(R.id.fl_chart_info);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    break;
                case SUCCESS:


                    break;
                case ERROR:
                    break;
            }
        }
    };

    @Override
    public void onRefresh() {

    }
}
