package com.hhly.mlottery.activity;

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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.FootballMatchListAdapter;
import com.hhly.mlottery.adapter.InfoCenterAdapter;
import com.hhly.mlottery.bean.FootBallKeepBean;
import com.hhly.mlottery.bean.FootBallOddsBean;
import com.hhly.mlottery.bean.FootballLotteryBean;
import com.hhly.mlottery.bean.infoCenterBean.InfoCenterBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BaseUserTopics;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.footballframe.DateMatchChoseDialogFragment;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuely198 on 2017/3/16.
 * 竞彩详情页
 */

public class FootballMatchActivity extends BaseWebSocketActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "FootballMatchActivity";
    private RecyclerView mRecyclerView;
    private RelativeLayout rl_top;
    private InfoCenterBean mInfoCenterBean;// 测试数据
    private InfoCenterAdapter infoCenterAdapter;

    private int currentIndexDate;
    private ExactSwipeRefreshLayout mSwipeRefreshLayout;
    private View mOnloadingView;
    private View mNoLoadingView;

    private TextView public_txt_title;
    private ImageView public_btn_filter;
    private TextView mTitleTextView;
    private ImageView iv_left;
    private ImageView iv_right;

    private FootballMatchListAdapter footballMatchListAdapter;
    private TextView match_no_data_txt;
    private LinearLayout match_error_btn;

    boolean isSelect = false;

    String Currentselection = "";


    private static final int PAGE_SIZE = 15;
    private int pageNum = 1;

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
                    rl_top.setVisibility(View.VISIBLE);
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
    private List<FootballLotteryBean.BettingListBean> bettingList;
    private String currentDate;
    private int totalSize;
    private DateMatchChoseDialogFragment mDateChooseDialogFragment;

    private String choosenDate; // 选中日期
    private TextView tv_data_size;
    private LinearLayout loadmoreView;
    private ProgressBar progressBar;
    private TextView loadmore_text;
    private View view;
    private Context context;
    private View headView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic(BaseUserTopics.footballBetting);
        super.onCreate(savedInstanceState);

        context = this;

        initView();
        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        initDate(DateUtil.getMomentDate());
        Currentselection = DateUtil.getMomentDate();


    }

    @Override
    protected void onTextResult(String text) {

        String type = "";
        try {
            JSONObject jsonObject = new JSONObject(text);
            type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(type)) {
            Message msg = Message.obtain();
            msg.obj = text;
            msg.arg1 = Integer.parseInt(type);
            mSocketHandler.sendMessage(msg);
        }

    }

    Handler mSocketHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            L.e(TAG, "__handleMessage__");

            L.e(TAG, "msg.arg1 = " + msg.arg1);

            if (msg.arg1 == 1) {
                String ws_json = (String) msg.obj;
                FootBallKeepBean footBallKeepBean = null;
                try {
                    footBallKeepBean = JSON.parseObject(ws_json, FootBallKeepBean.class);

                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    footBallKeepBean = JSON.parseObject(ws_json, FootBallKeepBean.class);
                }
                updateListViewItemStatus(footBallKeepBean);
            } else if (msg.arg1 == 2) {

                String ws_json = (String) msg.obj;
                FootBallOddsBean footBallOddsBean = null;

                try {
                    footBallOddsBean = JSON.parseObject(ws_json, FootBallOddsBean.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    footBallOddsBean = JSON.parseObject(ws_json, FootBallOddsBean.class);
                }
                // updateListViewItemOdd(footBallOddsBean);

            } else if (msg.arg2 == 3) {

            }
        }
    };

    private void updateListViewItemOdd(FootBallOddsBean footBallOddsBean) {
        if (bettingList != null) {
            for (int i = 0; i < bettingList.size(); i++) {
                if (footBallOddsBean.getData().getMatchId().equals(bettingList.get(i).getMatchId())) {  //找到对应比赛对象
                    updateMatchOdd(i, footBallOddsBean);

                }
            }

        }
    }

    private void updateMatchOdd(int i, FootBallOddsBean data) {
        if (data != null) {
            if (data.getData().getLetNumber() != null) {

                bettingList.get(i).setLetNumber(data.getData().getLetNumber());

            }
            if (!TextUtils.isEmpty(data.getData().getLetLoseOdds()) && !TextUtils.isEmpty(bettingList.get(i).getLetLoseOdds())) {

                if (Double.parseDouble(data.getData().getLetLoseOdds()) > Double.parseDouble(bettingList.get(i).getLetLoseOdds())) {
                    //升
                    bettingList.get(i).setLetloseoddsColorId(R.color.odds_up_bg);
                    bettingList.get(i).setLetLoseOdds(data.getData().getLetLoseOdds());
                } else if (Double.parseDouble(data.getData().getLetLoseOdds()) < Double.parseDouble(bettingList.get(i).getLetLoseOdds())) {
                    //降
                    bettingList.get(i).setLetloseoddsColorId(R.color.odds_down_bg);
                    bettingList.get(i).setLetLoseOdds(data.getData().getLetLoseOdds());
                } else {
                    bettingList.get(i).setLetloseoddsColorId(R.color.transparency);
                }


            }
            if (!TextUtils.isEmpty(data.getData().getLetSameOdds()) && !TextUtils.isEmpty(bettingList.get(i).getLetSameOdds())) {

                if (Double.parseDouble(data.getData().getLetSameOdds()) > Double.parseDouble(bettingList.get(i).getLetSameOdds())) {
                    //升
                    bettingList.get(i).setLetsameoddsColorId(R.color.odds_up_bg);
                    bettingList.get(i).setLetSameOdds(data.getData().getLetSameOdds());

                } else if (Double.parseDouble(data.getData().getLetSameOdds()) < Double.parseDouble(bettingList.get(i).getLetSameOdds())) {
                    //降
                    bettingList.get(i).setLetsameoddsColorId(R.color.odds_down_bg);
                    bettingList.get(i).setLetSameOdds(data.getData().getLetSameOdds());
                } else {
                    bettingList.get(i).setLetsameoddsColorId(R.color.transparency);
                }


            }
            if (!TextUtils.isEmpty(data.getData().getLetWinOdds()) && !TextUtils.isEmpty(bettingList.get(i).getLetWinOdds())) {

                if (Double.parseDouble(data.getData().getLetWinOdds()) > Double.parseDouble(bettingList.get(i).getLetWinOdds())) {
                    //升
                    bettingList.get(i).setLetwinoddsColorId(R.color.odds_up_bg);
                    bettingList.get(i).setLetWinOdds(data.getData().getLetWinOdds());
                } else if (Double.parseDouble(data.getData().getLetWinOdds()) < Double.parseDouble(bettingList.get(i).getLetWinOdds())) {
                    //降
                    bettingList.get(i).setLetwinoddsColorId(R.color.odds_down_bg);
                    bettingList.get(i).setLetWinOdds(data.getData().getLetWinOdds());
                } else {
                    bettingList.get(i).setLetwinoddsColorId(R.color.transparency);
                }


            }
            if (!TextUtils.isEmpty(data.getData().getLoseOdds()) && !TextUtils.isEmpty(bettingList.get(i).getLoseOdds())) {


                if (Double.parseDouble(data.getData().getLoseOdds()) > Double.parseDouble(bettingList.get(i).getLoseOdds())) {
                    //升
                    bettingList.get(i).setLoseoddsColorId(R.color.odds_up_bg);
                    bettingList.get(i).setLoseOdds(data.getData().getLoseOdds());

                } else if (Double.parseDouble(data.getData().getLoseOdds()) < Double.parseDouble(bettingList.get(i).getLoseOdds())) {
                    //降
                    bettingList.get(i).setLoseoddsColorId(R.color.odds_down_bg);
                    bettingList.get(i).setLoseOdds(data.getData().getLoseOdds());
                } else {
                    bettingList.get(i).setLoseoddsColorId(R.color.transparency);
                }


            }
            if (!TextUtils.isEmpty(data.getData().getSameOdds()) && !TextUtils.isEmpty(bettingList.get(i).getSameOdds())) {
                if (Double.parseDouble(data.getData().getSameOdds()) > Double.parseDouble(bettingList.get(i).getSameOdds())) {
                    //升
                    bettingList.get(i).setSameoddsColorId(R.color.odds_up_bg);
                    bettingList.get(i).setSameOdds(data.getData().getSameOdds());
                } else if (Double.parseDouble(data.getData().getSameOdds()) > Double.parseDouble(bettingList.get(i).getSameOdds())) {
                    //降
                    bettingList.get(i).setSameoddsColorId(R.color.odds_down_bg);
                    bettingList.get(i).setSameOdds(data.getData().getSameOdds());
                } else {
                    bettingList.get(i).setSameoddsColorId(R.color.transparency);
                }


            }
            if (!TextUtils.isEmpty(data.getData().getWinOdds()) && !TextUtils.isEmpty(bettingList.get(i).getWinOdds())) {

                if (Double.parseDouble(data.getData().getWinOdds()) > Double.parseDouble(bettingList.get(i).getWinOdds())) {
                    //升
                    bettingList.get(i).setWinoddsColorId(R.color.odds_up_bg);
                    bettingList.get(i).setWinOdds(data.getData().getWinOdds());
                } else if (Double.parseDouble(data.getData().getWinOdds()) < Double.parseDouble(bettingList.get(i).getWinOdds())) {
                    //降
                    bettingList.get(i).setWinoddsColorId(R.color.odds_down_bg);
                    bettingList.get(i).setWinOdds(data.getData().getWinOdds());

                } else {
                    bettingList.get(i).setWinoddsColorId(R.color.transparency);
                    //bettingList.get(i).setWinoddsColorId(R.color.transparency);
                }

            }
            if (footballMatchListAdapter != null) {
                footballMatchListAdapter.updateDatas(bettingList, i);
                footballMatchListAdapter.notifyDataSetChanged();
            }

        }


    }

    private void updateListViewItemStatus(FootBallKeepBean footBallKeepBean) {

        Map<String, String> data = footBallKeepBean.getData();
        if (bettingList != null) {

            for (int i = 0; i < bettingList.size(); i++) {

                if ((bettingList.get(i).getMatchId() + "").equals(footBallKeepBean.getThirdId())) {  //找到对应比赛对象
                    updateMatchStatus(i, data);
                }
            }
        }
    }

    private void updateMatchStatus(int i, Map<String, String> data) {
        bettingList.get(i).setStatus(data.get("statusOrigin"));
        if ("0".equals(bettingList.get(i).getStatus()) && "1".equals(bettingList.get(i).getStatus())) {// 未开场变成开场
            bettingList.get(i).setHomeScore("0");
            bettingList.get(i).setGuestScore("0");
        }

        if (data.get("keepTime") != null) {
            bettingList.get(i).setKeepTime(data.get("keepTime"));
        }

        if (footballMatchListAdapter != null) {

            footballMatchListAdapter.updateDatas(bettingList, i);
            footballMatchListAdapter.notifyDataSetChanged();
        }


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


    private void initDate(String date) {
        mSwipeRefreshLayout.setRefreshing(true);
        Map<String, String> params = new HashMap<>();
        params.put("date", date);
        params.put("pageNum", pageNum + "");
        params.put("pageSize", PAGE_SIZE + "");
        // 请求网络数据
        VolleyContentFast.requestJsonByGet(BaseURLs.FINDBETTINGLIST, params, new VolleyContentFast.ResponseSuccessListener<FootballLotteryBean>() {
            @Override
            public void onResponse(FootballLotteryBean jsonObject) {

                mSwipeRefreshLayout.setRefreshing(false);

                if (jsonObject != null) {
                    connectWebSocket();
                    if (jsonObject.getResult() == 200) {

                        totalSize = jsonObject.getTotalSize();
                        currentDate = jsonObject.getCurrentDate();
                        mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);

                        bettingList = jsonObject.getBettingList();
                        tv_data_size.setText("(" + totalSize + ")");

                        initViewData();
                        if (bettingList == null) {
                            mViewHandler.sendEmptyMessage(VIEW_STATUS_NO_DATA);
                        } else {
                            footballMatchListAdapter.notifyItemRemoved(bettingList.size() - 1);
                            footballMatchListAdapter.getData().clear();
                            footballMatchListAdapter.addData(bettingList);
                        }
                        if (!isSelect) {
                            maybeInitDateList();
                            getCurrentDateInfoIndex();
                            mTitleTextView.setText(currentDate + " " + DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(currentDate)));
                        }
                        isSelect = true;
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

            }
        }, FootballLotteryBean.class);
    }

    public void buyClicked() {
        mBettingBuyClickListener = new BettingBuyClickListener() {
            @Override
            public void BuyOnClick(View view, String s) {
                Intent intent = new Intent(FootballMatchActivity.this, FootballMatchDetailActivity.class);
                intent.putExtra("thirdId", s);
                intent.putExtra("match_details", 1);
                startActivity(intent);
            }
        };
    }
    private void initViewData() {

            footballMatchListAdapter = new FootballMatchListAdapter(getApplicationContext(), R.layout.football_match_child, null);
            buyClicked();
            footballMatchListAdapter.setmBuyClick(mBettingBuyClickListener);
            footballMatchListAdapter.setLoadingView(view);
            mRecyclerView.setAdapter(footballMatchListAdapter);
            footballMatchListAdapter.openLoadMore(0, true);

        //上拉加载更多
        footballMatchListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        pullUpLoadMoreData();
                    }
                }, 1000);
            }
        });
    }

    private void upDataAdapter() {

        if (footballMatchListAdapter == null) {
            return;
        }
        footballMatchListAdapter.updateData(bettingList);
        footballMatchListAdapter.notifyDataSetChanged();

    }

    private List<Map<String, String>> dateList;

    /**
     * 初始化日期列表
     */
    private void maybeInitDateList() {
        if (dateList == null) {
            dateList = new ArrayList<>();
        } else {
            dateList.clear();
        }
        for (int i = -7; i < 2; i++) {
            addDate(i);
        }
    }

    /**
     * 添加日期
     *
     * @param offset offset
     */
    private void addDate(int offset) {
        Map<String, String> map = new HashMap<>();
        map.put("date", UiUtils.getDate(currentDate, offset));
        dateList.add(map);
    }

    private void getCurrentDateInfoIndex() {
        for (int i = 0, len = dateList.size(); i < len; i++) {
            if (currentDate.equals(dateList.get(i).get("date"))) {
                currentIndexDate = i;
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

        LayoutInflater layoutInflater = this.getLayoutInflater();
        view = layoutInflater.inflate(R.layout.view_load_more, null);

        public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText(R.string.football_jingcai);

        //筛选按钮
        public_btn_filter = (ImageView) findViewById(R.id.public_btn_filter);
        public_btn_filter.setOnClickListener(this);
        public_btn_filter.setVisibility(View.GONE);

        findViewById(R.id.public_img_back).setOnClickListener(this);
        findViewById(R.id.public_btn_set).setVisibility(View.INVISIBLE);
        //日期头部选择器
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);
        mTitleTextView = (TextView) findViewById(R.id.tv_data_content);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        tv_data_size = (TextView) findViewById(R.id.tv_data_size);

        rl_top.setOnClickListener(this);
        mTitleTextView.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);//设置布局管理器
        layoutManager.setOrientation(OrientationHelper.VERTICAL);//设置为垂直布局，这也是默认的

        //上来加载view
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        loadmore_text = (TextView) view.findViewById(R.id.loadmore_text);


        mSwipeRefreshLayout = (ExactSwipeRefreshLayout) findViewById(R.id.match_swiperefreshlayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(FootballMatchActivity.this, StaticValues.REFRASH_OFFSET_END));

        //暂无数据
        match_no_data_txt = (TextView) findViewById(R.id.match_no_data_txt);

        //网络异常
        match_error_btn = (LinearLayout) findViewById(R.id.match_error_ll);
        findViewById(R.id.match_error_btn).setOnClickListener(this);

        emptyView = View.inflate(this, R.layout.layout_nodata, null);

        mOnloadingView = getLayoutInflater().inflate(R.layout.onloading, (ViewGroup) mRecyclerView.getParent(), false);
        mNoLoadingView = getLayoutInflater().inflate(R.layout.nomoredata, (ViewGroup) mRecyclerView.getParent(), false);

    }


    /*上拉加载*/

    private void pullUpLoadMoreData() {

        pageNum += 1;
        Map<String, String> params = new HashMap<>();
        params.put("date", Currentselection);
        params.put("pageNum", pageNum + "");
        params.put("pageSize", PAGE_SIZE + "");
        // 请求网络数据
        VolleyContentFast.requestJsonByGet(BaseURLs.FINDBETTINGLIST, params, new VolleyContentFast.ResponseSuccessListener<FootballLotteryBean>() {
            @Override
            public void onResponse(FootballLotteryBean jsonObject) {
                mSwipeRefreshLayout.setRefreshing(false);

                if (jsonObject != null) {

                    if (jsonObject.getResult() == 200) {
                        if (jsonObject.getBettingList()==null||jsonObject.getBettingList().size()==0) {
                            footballMatchListAdapter.notifyDataChangedAfterLoadMore(false);
                            footballMatchListAdapter.addFooterView(mNoLoadingView);
                     /*       loadmore_text.setText(FootballMatchActivity.this.getResources().getString(R.string.loading_data_txt));
                            progressBar.setVisibility(View.VISIBLE);
                            bettingList.addAll(jsonObject.getBettingList());
                            footballMatchListAdapter.addData(bettingList);
                            //mRecyclerView.getRecycledViewPool().clear();
                            footballMatchListAdapter.notifyDataChangedAfterLoadMore(true);*/
                        } else {
                            // loadmore_text.setText(FootballMatchActivity.this.getResources().getString(R.string.nodata_txt));
                            footballMatchListAdapter.notifyDataChangedAfterLoadMore(jsonObject.getBettingList(), true);
                        }
                    }
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);

            }
        }, FootballLotteryBean.class);


    }

    /**
     * 日期选择 Fragment 初始化
     */
    private void maybeInitDateChooseDialog() {
        if (mDateChooseDialogFragment != null) return;
        mDateChooseDialogFragment = DateMatchChoseDialogFragment.newInstance(currentDate,
                new DateMatchChoseDialogFragment.OnDateChooseListener() {
                    @Override
                    public void onDateChoose(String date) {
                        pageNum = 1;
                        choosenDate = date;
                        mTitleTextView.setText(DateUtil.convertDateToNation(date) + " " + DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(DateUtil.convertDateToNation(date))));
                        initDate(DateUtil.convertDateToNation(date));

                        Currentselection = date;

                        for (int i = 0, len = dateList.size(); i < len; i++) {
                            if (choosenDate.equals(dateList.get(i).get("date"))) {
                                currentIndexDate = i;
                            }
                        }
                        if (currentIndexDate <= 0) {
                            findViewById(R.id.iv_left).setVisibility(View.GONE);
                            findViewById(R.id.iv_right).setVisibility(View.VISIBLE);
                        } else if (currentIndexDate >= dateList.size() - 1) {
                            findViewById(R.id.iv_right).setVisibility(View.GONE);
                            findViewById(R.id.iv_left).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.iv_left).setVisibility(View.VISIBLE);
                            findViewById(R.id.iv_right).setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    /**
     * 显示日期选择 dialog
     */
    private void showDateChooseDialog() {
        maybeInitDateChooseDialog();
        if (!mDateChooseDialogFragment.isVisible()) {
            mDateChooseDialogFragment.show(getSupportFragmentManager(), "dateChooseFragment");

        }
    }

    public void showSelectInfo(int indexDate) {


        currentIndexDate = indexDate;

        //footballMatchListAdapter.getData().clear();
        if (dateList.get(indexDate).size() != 0) {
            initDate(dateList.get(indexDate).get("date"));
            footballMatchListAdapter.notifyDataSetChanged();
        } else {
            footballMatchListAdapter.setEmptyView(emptyView);
            footballMatchListAdapter.notifyDataSetChanged();
        }

        mTitleTextView.setText(dateList.get(indexDate).get("date") + "" + DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(dateList.get(indexDate).get("date"))));
        if (indexDate <= 0) {
            findViewById(R.id.iv_left).setVisibility(View.GONE);
            findViewById(R.id.iv_right).setVisibility(View.VISIBLE);
        } else if (indexDate >= dateList.size() - 1) {
            findViewById(R.id.iv_right).setVisibility(View.GONE);
            findViewById(R.id.iv_left).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.iv_left).setVisibility(View.VISIBLE);
            findViewById(R.id.iv_right).setVisibility(View.VISIBLE);
        }
        Currentselection = dateList.get(indexDate).get("date");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.public_img_back:

                finish();
                break;
            case R.id.match_error_btn:
                pageNum=1;
                isSelect = false;
                mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
                initDate(currentDate);
                break;
            case R.id.tv_data_content:
                showDateChooseDialog();
                break;
            case R.id.iv_left:
                pageNum=1;
                if (currentIndexDate > 0) {
                    currentIndexDate--;
                }
                showSelectInfo(currentIndexDate);

                break;
            case R.id.iv_right:
                pageNum=1;
                if (currentIndexDate < dateList.size() - 1) {
                    currentIndexDate++;
                }
                showSelectInfo(currentIndexDate);
                break;
            case R.id.rl_top:

                // showDateChooseDialog();
                break;
            default:
                break;


        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeWebSocket();
        mViewHandler.removeCallbacksAndMessages(null);
        mSocketHandler.removeCallbacksAndMessages(null);
    }

    private FootballMatchActivity.BettingBuyClickListener mBettingBuyClickListener;

    public interface BettingBuyClickListener {
        void BuyOnClick(View view, String s);
    }


    /**
     * 设置刷新状态
     *
     * @param b 是否正在刷新
     */
    public void setRefreshing(boolean b) {
        mSwipeRefreshLayout.setRefreshing(b);
    }


    @Override
    public void onRefresh() {
        pageNum=1;
        initDate(Currentselection);
    }
}
