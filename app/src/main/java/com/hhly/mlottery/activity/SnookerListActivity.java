package com.hhly.mlottery.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.snooker.SnookerRecyclerAdapter;
import com.hhly.mlottery.bean.snookerbean.SnookerLeaguesBean;
import com.hhly.mlottery.bean.snookerbean.SnookerListBean;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchOddsBean;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchScoreBean;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchesBean;
import com.hhly.mlottery.bean.snookerbean.SnookerOddsSocketBean;
import com.hhly.mlottery.bean.snookerbean.SnookerScoreSocketBean;
import com.hhly.mlottery.bean.snookerbean.SnookerSecondBean;
import com.hhly.mlottery.bean.snookerbean.SnookerWaterfallMatchesBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.listener.LoadMoreRecyclerOnScrollListener;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.SnookerSettingEvent;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.LoadMoreRecyclerView;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;


/**
 * Created by yixq on 2016/11/15.
 * mail：yixq@13322.com
 * describe:斯洛克列表数据
 */

public class SnookerListActivity extends BaseWebSocketActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private LoadMoreRecyclerView mRecyclerView;
    private SnookerRecyclerAdapter mAdapter;
    private ImageView mFilterImage;
    private ImageView mSettingImage;
    private LinearLayout mErrorLayout;
    private TextView mRefreshText;
    private TextView mNoData;

    //显示状态
    private static final int SHOW_STATUS_LOADING = 1;//加载中
    private static final int SHOW_STATUS_ERROR = 2;//加载失败
    private static final int SHOW_STATUS_NO_DATA = 3;//暂无数据
    private static final int SHOW_STATUS_SUCCESS = 4;//加载成功
    private ExactSwipeRefreshLayout mRefresh;

    private ImageView mBackImage;
    private List<SnookerMatchesBean> currentAllData;
    private LinearLayoutManager linearLayoutManager;
    private View mFooterView;
    private TextView loadmore_text;
    private ProgressBar progressBar;

    private List<SnookerWaterfallMatchesBean> mWaterfallMatchesList;

    /**
     * 上拉加载的次数记录（ps：与数据条数对应 一次加载一天的数据）
     */
    private int upLoadNum = 0;
    /**
     * 下拉加载的次数记录（ps：与数据条数对应 一次加载一天的数据）
     */
    private int downLoadNum = -1;
    /**
     * 上拉加载的日期参数 date_leagueId
     */
    private String dateLeagues = "";
    /**
     * 当前日期，用于添加不同类型的数据到 adapter （ps:日期不同时添加覆盖记录最近的日期）
     */
    private String currentDate = "";

    /**
     * 下拉更多的日期集
     */
    private List<SnookerWaterfallMatchesBean> downDateList;
    /**
     * 上拉更多的日期集
     */
    private List<SnookerWaterfallMatchesBean> upDateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * 推送  必须定义在 super.onCreate前面，否则 订阅不成功
         */
//        setWebSocketUri("ws://192.168.10.242:61634");
        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic("USER.topic.snooker");


        super.onCreate(savedInstanceState);

        setContentView(R.layout.snooker_list_activity);

//        L.d("yxq===推送地址" , "推送地址 = " + BaseURLs.WS_SERVICE);
        //注册
        EventBus.getDefault().register(this);

        initView();

        setStatus(SHOW_STATUS_LOADING);
        initData();

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void initView() {

        TextView mTitle = (TextView) findViewById(R.id.public_txt_title);
        mTitle.setText(R.string.snooker_title);

        mRecyclerView = (LoadMoreRecyclerView) findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(SnookerListActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mFooterView = LayoutInflater.from(SnookerListActivity.this).inflate(R.layout.view_load_more, mRecyclerView, false);
        mRecyclerView.addFooterView(mFooterView);

        loadmore_text = (TextView) mFooterView.findViewById(R.id.loadmore_text);
        progressBar = (ProgressBar) mFooterView.findViewById(R.id.progressBar);
        //网络异常
        mErrorLayout = (LinearLayout) findViewById(R.id.snooker_error_layout);
        //点击刷新
        mRefreshText = (TextView) findViewById(R.id.snooker_reloading_txt);
        mRefreshText.setOnClickListener(this);
        //暂无数据
        mNoData = (TextView) findViewById(R.id.snooker_nodata_txt);

        mRefresh = (ExactSwipeRefreshLayout) findViewById(R.id.snooker_refresh_layout);
        mRefresh.setColorSchemeResources(R.color.tabhost);
        mRefresh.setOnRefreshListener(this);

        //隐藏公共头部多余图标
        mFilterImage = (ImageView) findViewById(R.id.public_btn_filter);
        mFilterImage.setVisibility(View.GONE);

        mBackImage = (ImageView) findViewById(R.id.public_img_back);
        mBackImage.setOnClickListener(this);

        mSettingImage = (ImageView) findViewById(R.id.public_btn_set);
        mSettingImage.setOnClickListener(this);

    }

    /**
     * 设置显示状态
     * @param status
     */
    private void setStatus(int status) {
        mRefresh.setVisibility(status == SHOW_STATUS_LOADING ? View.VISIBLE : View.GONE);
        mErrorLayout.setVisibility(status == SHOW_STATUS_ERROR ? View.VISIBLE : View.GONE);
        mNoData.setVisibility(status == SHOW_STATUS_NO_DATA ? View.VISIBLE : View.GONE);
    }

    private void initData() {

        mRefresh.setRefreshing(true);
//        String url = "http://192.168.31.33:8080/mlottery/core/snookerMatch.getFirstSnookerMatch.do";
//        Map<String, String> map = new HashMap();
        VolleyContentFast.requestJsonByGet(BaseURLs.SNOOKER_LIST_URL, new VolleyContentFast.ResponseSuccessListener<SnookerListBean>() {

            @Override
            public void onResponse(SnookerListBean jsonBean) {
                mRefresh.setRefreshing(false);
                if (jsonBean.getData() == null || jsonBean.getData().getLeagues() == null) {
                    setStatus(SHOW_STATUS_NO_DATA);
                    return;
                }
                List<SnookerLeaguesBean> dataBean = jsonBean.getData().getLeagues();
                currentAllData = new ArrayList<>();

                mWaterfallMatchesList = jsonBean.getData().getWaterfallMatches();

                setStatus(SHOW_STATUS_LOADING);

                //数据加载
                setData(currentAllData ,dataBean);

                /**
                 * 区分上拉  下拉 的日期
                 */
                upDateList = new ArrayList<>();
                downDateList = new ArrayList<>();
                if (mWaterfallMatchesList.size() != 0) {

                    boolean isMinCurrentDate = true;//是否是当前数据的开始日期 ps：初始为true，记录下拉加载的数据 即 历史数据
                    boolean isMaxCurrentDate = false;//是否是当前数据的结束日期 ps：初始为false，激励上拉数据，即 未来数据（过滤前面历史和当前数据）

                    for (SnookerWaterfallMatchesBean date : mWaterfallMatchesList) {

                        /**
                         * 获得下拉的所有日期
                         */
                        if (!date.getDate().equals(dataBean.get(0).getDate())) {
                            if (isMinCurrentDate) {
                                downDateList.add(date);
                            }
                        }else{
                            isMinCurrentDate = false;
                        }

                        /**
                         * 获得上拉所有数据
                         */
                        if (!date.getDate().equals(dataBean.get(dataBean.size()-1).getDate())) {
                            if (isMaxCurrentDate) {
                                upDateList.add(date);
                            }
                        }else{
                            isMaxCurrentDate = true;
                        }
                    }
                }
                /**
                 上拉加载的日期从最近的开始，so 初始日期为最近一天
                 */
                downLoadNum = downDateList.size()-1;
//                L.d("yxq===下拉加载的条数" , "downDateList.size() = " + downDateList.size());
//                L.d("yxq===上拉加载的条数" , "upDateList.size() = " + upDateList.size());

                /**
                 * 上拉加载
                 */
                mRecyclerView.addOnScrollListener(new LoadMoreRecyclerOnScrollListener(linearLayoutManager , isMove , mIndex , mRecyclerView) {
                    @Override
                    public void onLoadMore(int currentPage) {

                        if (upLoadNum < upDateList.size()) {
                            String currentDateLeagues = upDateList.get(upLoadNum).getDate() + "_" + upDateList.get(upLoadNum).getLeagueId();
                            if (!currentDateLeagues.equals(dateLeagues)) {
                                dateLeagues = currentDateLeagues;
                                LoadMoreData(dateLeagues , 0);
                            }
                        }else{
                            loadmore_text.setText(getApplicationContext().getResources().getString(R.string.nodata_txt));
                            progressBar.setVisibility(View.GONE);
                        }
//                        updateAdapter();
                    }
                });

                connectWebSocket();

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mRefresh.setRefreshing(false);
                setStatus(SHOW_STATUS_ERROR);
                Toast.makeText(getApplicationContext(), "yxq>>>>>> " + "---", Toast.LENGTH_SHORT).show();
            }
        }, SnookerListBean.class);
    }


    private void setData(List<SnookerMatchesBean> listData, List<SnookerLeaguesBean> dataBean) {

        if (listData == null) {
            listData = new ArrayList<>();
        }
        for (int i = 0; i < dataBean.size(); i++) {
            /**
             * Type = 0 类型一， Item == 日期
             */
            if (currentDate.equals("") || !currentDate.equals(dataBean.get(i).getDate())) {

                SnookerMatchesBean mMatchData = new SnookerMatchesBean();
                mMatchData.setItemType(0);
                mMatchData.setItemDate(dataBean.get(i).getDate());
                mMatchData.setItemLeaguesName("--");
                mMatchData.setItemLeaguesId("--");
                mMatchData.setThirdId("--");
                listData.add(mMatchData);

                currentDate = dataBean.get(i).getDate();
            }

            /**
             * Type = 1 类型二， Item == 赛事
             */
            SnookerMatchesBean mLeaguesData = new SnookerMatchesBean();
            mLeaguesData.setItemType(1);
            mLeaguesData.setItemDate("--");
            mLeaguesData.setItemLeaguesName(dataBean.get(i).getLeaguesName());
            mLeaguesData.setItemLeaguesId("--");
            mLeaguesData.setThirdId("--");
            listData.add(mLeaguesData);

            /**
             * Type = 2 类型三， Item == 比赛
             */
            List<SnookerMatchesBean> datas = dataBean.get(i).getMatches();
            for (int j = 0; j < datas.size(); j++) {
                datas.get(j).setItemType(2);
                listData.add(datas.get(j));
            }
        }

        if (mAdapter == null) {
            mAdapter = new SnookerRecyclerAdapter(getApplicationContext(), listData);
            mRecyclerView.setAdapter(mAdapter);

        } else {
            updateAdapter();
        }

//        L.d("yxq=====> 当前数据条数" , "currentAllData.size = " +currentAllData.size());
    }

    /**
     * 添加更多数据（不同类型的数据组合方法）
     * @param listData
     * @param dataBean
     */
    private void constituteData(List<SnookerMatchesBean> listData, List<SnookerLeaguesBean> dataBean){

        if (listData == null) { // constitute
            listData = new ArrayList<>();
        }
        for (int i = 0; i < dataBean.size(); i++) {
            /**
             * Type = 0 类型一， Item == 日期
             */
            if (currentDate.equals("") || !currentDate.equals(dataBean.get(i).getDate())) {

                SnookerMatchesBean mMatchData = new SnookerMatchesBean();
                mMatchData.setItemType(0);
                mMatchData.setItemDate(dataBean.get(i).getDate());
                mMatchData.setItemLeaguesName("--");
                mMatchData.setItemLeaguesId("--");
                mMatchData.setThirdId("--");
                listData.add(mMatchData);

                currentDate = dataBean.get(i).getDate();
            }

            /**
             * Type = 1 类型二， Item == 赛事
             */
            SnookerMatchesBean mLeaguesData = new SnookerMatchesBean();
            mLeaguesData.setItemType(1);
            mLeaguesData.setItemDate("--");
            mLeaguesData.setItemLeaguesName(dataBean.get(i).getLeaguesName());
            mLeaguesData.setItemLeaguesId("--");
            mLeaguesData.setThirdId("--");
            listData.add(mLeaguesData);

            /**
             * Type = 2 类型三， Item == 比赛
             */
            List<SnookerMatchesBean> datas = dataBean.get(i).getMatches();
            for (int j = 0; j < datas.size(); j++) {
                datas.get(j).setItemType(2);
                listData.add(datas.get(j));
            }
        }

    }

    /************************************************************************************************/


    /**
     * 加载更多
     * @param parameter 传参
     * @param type 0：上拉  1：下拉
     */
    private void LoadMoreData(final String parameter , final int type) {
        Observable
                .timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {
                        addData(parameter , type);
//                        mAdapter.notifyDataSetChanged();
                        return null;
                    }
                }).subscribe();
    }

    /**
     * 加载跟多数据的方法 （上拉 、 下拉 加载）
     * @param parameterUrl
     * @param type
     */
    private void addData(String parameterUrl , final int type) {

        String url = BaseURLs.SNOOKER_LIST_LOADMORE_URL;
        Map<String , String> map = new HashMap<>();
        map.put("dateLeaguesId" , parameterUrl);

        VolleyContentFast.requestJsonByGet(url, map , new VolleyContentFast.ResponseSuccessListener<SnookerSecondBean>() {
            @Override
            public void onResponse(SnookerSecondBean json) {

                if (!json.getResult().equals("200")) {
                    loadmore_text.setText(getApplicationContext().getResources().getString(R.string.nodata_txt));
                    progressBar.setVisibility(View.GONE);
                    return;
                } else {
                    loadmore_text.setText(getApplicationContext().getResources().getString(R.string.loading_data_txt));
                    progressBar.setVisibility(View.VISIBLE);
                }

                List<SnookerLeaguesBean> dataBean = json.getData();

                if (type == 0) {
                    //数据加载
                    setData(currentAllData , dataBean);
                    upLoadNum++;
                    updateAdapter();
                }else if (type == 1) {

                    List<SnookerMatchesBean> historyData = new ArrayList<SnookerMatchesBean>();
                    constituteData(historyData , dataBean);

                    List<SnookerMatchesBean> downDataList = new ArrayList<>();
                    for (SnookerMatchesBean newData:historyData) {
                        downDataList.add(newData);
                    }
                    int numData = downDataList.size();

                    for (SnookerMatchesBean currentData:currentAllData) {
                        downDataList.add(currentData);
                    }

                    currentAllData.clear();
                    currentAllData = downDataList;
                    downLoadNum--;
                    L.d("yxq+++++++ " , "请求到的数据" + downDataList.size());
                    updateAdapter();
//                    move(historyData.size());
                    move(numData);//下拉请求到新数据后显示位置不变，新数据隐藏在上面
                    L.d("yxq+++++++ " , historyData.size() +" >>>>>" + numData);
                }

//                updateAdapter();
                Toast.makeText(SnookerListActivity.this, "yxqAAAAAAAA" + currentAllData.size(), Toast.LENGTH_SHORT).show();
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                Toast.makeText(SnookerListActivity.this, "yxqBBBBBBBB" + currentAllData.size(), Toast.LENGTH_SHORT).show();
            }
        },SnookerSecondBean.class);

    }

    /************************************************************************************************/


    /**
     * 更新数据
     */
    public void updateAdapter() {
        if (mAdapter == null) {
            return;
        }
        mAdapter.updateDatas(currentAllData);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 下拉加载历史数据
     */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(false);
                
                if (downLoadNum > 0) {
                    String currentDateLeagues = downDateList.get(downLoadNum).getDate() + "_" + downDateList.get(downLoadNum).getLeagueId();
                    if (!currentDateLeagues.equals(dateLeagues)) {
                        dateLeagues = currentDateLeagues;
                        LoadMoreData(dateLeagues , 1);
                    }
                }else{
                    Toast.makeText(SnookerListActivity.this, "没有更多数据了...", Toast.LENGTH_SHORT).show();

                }
//                updateAdapter();
                L.d("yxq+++++++ " , "----下拉完成----" );
            }
        }, 1000);
    }
    private int mIndex = 0; //mIndex是记录的要置顶项在RecyclerView中的位置
    private boolean isMove = false;//用于recycleview 滚动监听里面的记录

    public void move(int n){
        if (n<0 || n>=mAdapter.getItemCount() ){
            Toast.makeText(this,"超出范围了",Toast.LENGTH_SHORT).show();
            return;
        }
        mIndex = n;
        mRecyclerView.stopScroll();
        moveToPosition(n);
    }

    private void moveToPosition(int n) {

        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = linearLayoutManager.findLastVisibleItemPosition();

        L.d("yxq+++++++ " , "first and last = " + firstItem + " --- "+ lastItem);

        //然后区分情况
        if (n <= firstItem ){
            //当要置顶的项在当前显示的第一个项的前面时
            mRecyclerView.scrollToPosition(n);
            L.d("yxq+++++++ " , "n <= first 前");
        }else if ( n <= lastItem ){
            //当要置顶的项已经在屏幕上显示时
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
            L.d("yxq+++++++ " , "n <= last 中");
        }else{
            //当要置顶的项在当前显示的最后一项的后面时
            mRecyclerView.scrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            isMove = true;
            L.d("yxq+++++++ " , "else 后");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.snooker_reloading_txt:
                Toast.makeText(getApplicationContext(), "点击了刷新···", Toast.LENGTH_SHORT).show();
                setStatus(SHOW_STATUS_LOADING);
                initData();
                break;
            case R.id.public_img_back:
                finish();
                break;
            case R.id.public_btn_set:

                Intent intent = new Intent(SnookerListActivity.this, SnookerSettingActivity.class);
                startActivity(intent);
                SnookerListActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                break;
        }
    }

    /**
     * 设置返回
     */
    public void onEventMainThread(SnookerSettingEvent snookerSettingEvent) {
//        L.d("yxq=====> 设置页返回", "=======" + snookerSettingEvent.getmMsg());
        Toast.makeText(getApplicationContext(), snookerSettingEvent.getmMsg() + " = yxq------", Toast.LENGTH_SHORT).show();
        updateAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册 取消绑定
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onTextResult(String text) {

//        L.d("yxq=====>", "_________接收到的消息___________");
//        L.d("yxq=====>收到的消息：" , text);
//        Toast.makeText(SnookerListActivity.this, "收到推送消息...", Toast.LENGTH_SHORT).show();
        if (mAdapter == null) {
            return;
        }
        String type = "";
        try {
            JSONObject jsonObject = new JSONObject(text);
            type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        L.d("yxq=====>type= ", "type = " + type);

        if (!"".equals(type)) {
            Message msg = Message.obtain();
            msg.obj = text;
            msg.arg1 = Integer.parseInt(type);
            mWebSocketHandler.sendMessage(msg);
//            L.d("yxq=====>发送msg" , "mWebSocketHandler调用");
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

    Handler mWebSocketHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            L.e(TAG, "__handleMessage__");
            L.e(TAG, "msg.arg1 = " + msg.arg1);
            if (msg.arg1 == 300) {  // 比分推送
                String ws_json = (String) msg.obj;
                L.e(TAG, "ws_json_snooker_score = " + ws_json);
                SnookerScoreSocketBean mSnookerScore = null;
                try {
                    mSnookerScore = JSON.parseObject(ws_json , SnookerScoreSocketBean.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    mSnookerScore = JSON.parseObject(ws_json , SnookerScoreSocketBean.class);
                }
//                L.d("yxq=====>300走这里" , msg.arg1+"");
                updataScore(mSnookerScore);
            }else if (msg.arg1 == 301) {  // 赔率推送
                    String ws_json = (String) msg.obj;
                    L.e(TAG, "ws_json_snooker_odds = " + ws_json);
                    SnookerOddsSocketBean mSnookerOdds = null;
                    try {
                         mSnookerOdds = JSON.parseObject(ws_json , SnookerOddsSocketBean.class);
                    } catch (Exception e) {
                        ws_json = ws_json.substring(0, ws_json.length() - 1);
                        mSnookerOdds = JSON.parseObject(ws_json , SnookerOddsSocketBean.class);
                    }
//                L.d("yxq=====>301走这里" , msg.arg1+"");
                    updataOdds(mSnookerOdds);
                }
        }
    };

    /**
     * 比分更新
     * @param mScoreData
     */
    private void updataScore(SnookerScoreSocketBean mScoreData){
//        L.d("yxq=====>比分更新" , "updataScore调用");
//        L.d("yxq=====>thirdId = " , mScoreData.getThirdId());

        SnookerScoreSocketBean.SnookerScoreDataBean scoreData = mScoreData.getData();

        synchronized (currentAllData){
            for (SnookerMatchesBean match : currentAllData) {
                if (match.getThirdId().equals(mScoreData.getThirdId())) {
                    if (match.getMatchScore() != null) {
                        SnookerMatchScoreBean matchData = match.getMatchScore();
                        updataItemData(matchData , scoreData);

//                        L.d("yxq=====>MatchScore不为为null","Status = " + matchData.getStatus());
                    }else{
                        /**
                         * getMatchScore 为null  即，未开赛==>开赛 推送情况处理（状态更新）
                         */
                        SnookerMatchScoreBean newMatchData = new SnookerMatchScoreBean();
                        updataItemData(newMatchData , scoreData);//更新单条状态
                        match.setMatchScore(newMatchData);//赋值，给为null的MatchScore设值

//                       L.d("yxq=====>原 MatchScore为null 》》","Status = " + newMatchData.getStatus());
                    }

                    updateAdapter();
                    break;
                }
            }
        }
    }

    /**
     * 赔率更新
     * @param mOddsData
     */
    private void updataOdds(SnookerOddsSocketBean mOddsData){
//        L.d("yxq=====> 赔率更新" , "updataOdds()调用");
        SnookerMatchOddsBean socketOddsData = mOddsData.getData().getMatchOdds();
        synchronized (currentAllData){
            for (SnookerMatchesBean match : currentAllData) {
                if (match.getThirdId().equals(mOddsData.getThirdId())) {

                    if (match.getMatchOdds() != null) {

                        SnookerMatchOddsBean currentOddsData =  match.getMatchOdds();
                        updataOddsData(currentOddsData , socketOddsData);
                    }else{
                        /**
                         * MatchOdds 为null  即，无赔率==>有赔率情况处理
                         */
                        SnookerMatchOddsBean newCurrentOddsData =  new SnookerMatchOddsBean();
                        updataOddsData(newCurrentOddsData , socketOddsData);//更新单条数据赔率
                        match.setMatchOdds(newCurrentOddsData);//赋值，给为null的MatchOdds设值
                    }
                    updateAdapter();
                    break;
                }
            }
        }
    }

    /**
     * 更新单条item的内容
     */
    private void updataItemData(SnookerMatchScoreBean matchData , SnookerScoreSocketBean.SnookerScoreDataBean data){
//        L.d("yxq=====>updataItemData()调用 ：","data.getStatus = "+data.getStatus());
        if (data.getStatus() != null) {
            matchData.setStatus(data.getStatus());
        }
        if (data.getBall() != null) {
            matchData.setBall(data.getBall());
        }
        if (data.getLeagueId() != null) {
            matchData.setLeagueId(data.getLeagueId());
        }
        if (data.getMatchId() != null) {
            matchData.setMatchId(data.getMatchId());
        }
        if (data.getPlayerOnewin() != null) {
            matchData.setPlayerOnewin(data.getPlayerOnewin());
        }
        if (data.getPlayerTwowin() != null) {
            matchData.setPlayerTwowin(data.getPlayerTwowin());
        }
        if (data.getPoId() != null) {
            matchData.setPoId(data.getPoId());
        }
        if (data.getPoScore() != null) {
            matchData.setPoScore(data.getPoScore());
        }
        if (data.getPoSingleShot() != null) {
            matchData.setPoSingleShot(data.getPoSingleShot());
        }
        if (data.getPtId() != null) {
            matchData.setPtId(data.getPtId());
        }
        if (data.getPtScore() != null) {
            matchData.setPtScore(data.getPtScore());
        }
        if (data.getPtSingleShot() != null) {
            matchData.setPtSingleShot(data.getPtSingleShot());
        }
    }

    /**
     * 赔率更新（单条数据更新）
     */
    private void updataOddsData(SnookerMatchOddsBean currentOddsData , SnookerMatchOddsBean socketOddsData){

        if (socketOddsData.getAllow() != null){
            currentOddsData.setAllow(socketOddsData.getAllow());
        }
        if (socketOddsData.getBigsmall() != null) {
            currentOddsData.setBigsmall(socketOddsData.getBigsmall());
        }
        if (socketOddsData.getOnetwo() != null) {
            currentOddsData.setOnetwo(socketOddsData.getOnetwo());
        }
        if (socketOddsData.getOnlywin() != null) {
            currentOddsData.setOnlywin(socketOddsData.getOnlywin());
        }
        if (socketOddsData.getStandard() != null) {
            currentOddsData.setStandard(socketOddsData.getStandard());
        }
    }

}
