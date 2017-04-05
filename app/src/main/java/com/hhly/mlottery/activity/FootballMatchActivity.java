package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.FootballMatchListAdapter;
import com.hhly.mlottery.adapter.InfoCenterAdapter;
import com.hhly.mlottery.adapter.snooker.PinnedHeaderExpandableAdapter;
import com.hhly.mlottery.bean.BasketballItemSearchBean;
import com.hhly.mlottery.bean.BasketballSearchBean;
import com.hhly.mlottery.bean.TextDemo;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.DataBean;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.ScheduleBean;
import com.hhly.mlottery.bean.infoCenterBean.InfoCenterBean;
import com.hhly.mlottery.bean.snookerbean.SnookerRaceListitemBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.infofrag.FootInfoCallBack;
import com.hhly.mlottery.frame.infofrag.FootInfoShowSelectInfoCallBack;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.InfoCenterPW;
import com.hhly.mlottery.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yuely198 on 2017/3/16.
 */

public class FootballMatchActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView mRecyclerView;
    private RelativeLayout rl_top;
    private InfoCenterBean mInfoCenterBean;// 测试数据
    private InfoCenterAdapter infoCenterAdapter;

    private int currentIndexDate;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    private TextView public_txt_title;
    private ImageView public_btn_filter;
    private TextView mTitleTextView;
    private ImageView iv_left;
    private ImageView iv_right;

    private FootballMatchListAdapter footballMatchListAdapter;
    private TextView match_no_data_txt;
    private LinearLayout match_error_btn;

    private final static int VIEW_STATUS_LOADING = 11;
    private final static int VIEW_STATUS_SUCCESS = 33;
    private static final int VIEW_STATUS_NET_ERROR = 44;
    private static final int VIEW_STATUS_NO_DATA = 55;
    private View emptyView;
    private Handler mViewHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VIEW_STATUS_LOADING:
                    rl_top.setVisibility(View.GONE);
                    match_error_btn.setVisibility(View.GONE);
                    match_no_data_txt.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    match_error_btn.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_SUCCESS:
                    rl_top.setVisibility(View.VISIBLE);
                    match_no_data_txt.setVisibility(View.GONE);
                    match_error_btn.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    break;

                case VIEW_STATUS_NO_DATA:
                    rl_top.setVisibility(View.GONE);
                    match_no_data_txt.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    match_error_btn.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_NET_ERROR:
                    rl_top.setVisibility(View.GONE);
                    match_no_data_txt.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    match_error_btn.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        initDate();
        initEvent();
    }
    private void initEvent() {
        footballMatchListAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                int thirdId = mInfoCenterBean.intelligences.get(currentIndexDate).list.get(i).thirdId;
                L.d("xxx", "thirdId: " + thirdId);
                Intent intent = new Intent(mContext, FootballMatchDetailActivity.class);
                intent.putExtra(FootballMatchDetailActivity.BUNDLE_PARAM_THIRDID, String.valueOf(thirdId));
                intent.putExtra("match_details", 1);
                startActivity(intent);
            }
        });
    }


    private void initDate() {
        mSwipeRefreshLayout.setRefreshing(true);

        Map<String, String> params = new HashMap<>();
        params.put("searchKeyword", "奧巴杜魯");//接口添加 version=xx 字段
        // 请求网络数据
        VolleyContentFast.requestJsonByPost("http://m.13322.com/mlottery/core/IOSBasketballMatch.fuzzySearch.do",params,new VolleyContentFast.ResponseSuccessListener<InfoCenterBean>() {
            @Override
            public void onResponse(InfoCenterBean jsonObject) {


                mSwipeRefreshLayout.setRefreshing(false);
                if (jsonObject != null) {

                    if (jsonObject.result == 200) {

                        if (jsonObject.intelligences.isEmpty()) {
                            mViewHandler.sendEmptyMessage(VIEW_STATUS_NO_DATA);
                            return;
                        }

                        mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);


                        mInfoCenterBean = jsonObject;
                        getCurrentDateInfoIndex();

                        if (mInfoCenterBean.intelligences.get(currentIndexDate).list.size() == 0) {
                            footballMatchListAdapter.setEmptyView(emptyView);
                        } else {
                            footballMatchListAdapter.getData().clear();
                            footballMatchListAdapter.addData(mInfoCenterBean.intelligences.get(currentIndexDate).list);
                        }

                        mTitleTextView.setText(getCurrentDate(mInfoCenterBean.intelligences.get(currentIndexDate).date, mInfoCenterBean.intelligences.get(currentIndexDate).count));

            /*            mInfoCenterPW = new InfoCenterPW((Activity) mContext, mInfoCenterBean.intelligences, currentIndexDate);
                        mInfoCenterPW.setFootInfoCallBack(footInfoCallBack);
                        mInfoCenterPW.setFootInfoShowSelectInfoCallBack(footInfoShowSelectInfoCallBack);
*/

                        mInfoCenterBean.intelligences.get(currentIndexDate).isSelect = true;
                    } else {
                        mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);

                    }
                } else {
                    mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
                Log.i("sdas","asdas"+exception);

            }
        }, InfoCenterBean.class);
    }


    private void getCurrentDateInfoIndex() {
        for (int i = 0, len = mInfoCenterBean.intelligences.size(); i < len; i++) {
            if (mInfoCenterBean.currentDate.equals(mInfoCenterBean.intelligences.get(i).date)) {
                currentIndexDate = i;
                return;
            }
        }
    }


    private String getCurrentDate(String date, int count) {
        if (date == null) {
            return null;
        }
        String weekDate = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(date));
        return DateUtil.convertDateToNation(date) + " " + weekDate + "(" + count + ")";
    }

    /*初始化视图*/
    private void initView() {

        setContentView(R.layout.activity_footballmatch);
        public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText("竞彩足球");

        //筛选按钮
        public_btn_filter = (ImageView) findViewById(R.id.public_btn_filter);
        public_btn_filter.setOnClickListener(this);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        findViewById(R.id.public_btn_set).setVisibility(View.INVISIBLE);
        //日期头部选择器
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);
        mTitleTextView = (TextView) findViewById(R.id.tv_data_content);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);

        mTitleTextView.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);//设置布局管理器
        layoutManager.setOrientation(OrientationHelper.VERTICAL);//设置为垂直布局，这也是默认的

        footballMatchListAdapter = new FootballMatchListAdapter(getApplicationContext(), R.layout.football_match_child, null);
        mRecyclerView.setAdapter(footballMatchListAdapter);


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.match_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getApplicationContext(), StaticValues.REFRASH_OFFSET_END));

        //暂无数据
        match_no_data_txt = (TextView) findViewById(R.id.match_no_data_txt);

        //网络异常
        match_error_btn = (LinearLayout) findViewById(R.id.match_error_ll);
        findViewById(R.id.match_error_btn).setOnClickListener(this);

        emptyView = View.inflate(this, R.layout.layout_nodata, null);
    }

    public void showSelectInfo(int indexDate) {
        currentIndexDate = indexDate;
        footballMatchListAdapter.getData().clear();
        if (mInfoCenterBean.intelligences.get(indexDate).count != 0) {
            footballMatchListAdapter.addData(mInfoCenterBean.intelligences.get(indexDate).list);
        } else {
            footballMatchListAdapter.setEmptyView(emptyView);
            footballMatchListAdapter.notifyDataSetChanged();
        }
        mTitleTextView.setText(getCurrentDate(mInfoCenterBean.intelligences.get(indexDate).date, mInfoCenterBean.intelligences.get(indexDate).count));

        if (indexDate <= 0) {
            findViewById(R.id.iv_left).setVisibility(View.GONE);
            findViewById(R.id.iv_right).setVisibility(View.VISIBLE);
        } else if (indexDate >= mInfoCenterBean.intelligences.size() - 1) {
            findViewById(R.id.iv_right).setVisibility(View.GONE);
            findViewById(R.id.iv_left).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.iv_left).setVisibility(View.VISIBLE);
            findViewById(R.id.iv_right).setVisibility(View.VISIBLE);
        }
        for (int i = 0, len = mInfoCenterBean.intelligences.size(); i < len; i++) {
            if (i == indexDate) {
                mInfoCenterBean.intelligences.get(i).isSelect = true;
            } else {
                mInfoCenterBean.intelligences.get(i).isSelect = false;
            }
        }
        //mInfoCenterPW.notifyChanged(indexDate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.public_img_back:

                finish();
                break;
            case R.id.match_error_btn:
                mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
                initDate();
                break;
            case R.id.tv_data_content:

                break;
            case R.id.iv_left:
                if (currentIndexDate > 0) {
                    currentIndexDate--;
                }
                showSelectInfo(currentIndexDate);
                break;
            case R.id.iv_right:
                if (currentIndexDate < mInfoCenterBean.intelligences.size() - 1) {
                    currentIndexDate++;
                }
                showSelectInfo(currentIndexDate);
                break;
            default:
                break;


        }

    }

    @Override
    public void onRefresh() {
        initDate();
    }
}
