package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.snooker.SnookerRecyclerAdapter;
import com.hhly.mlottery.bean.snookerbean.SnookerLeaguesBean;
import com.hhly.mlottery.bean.snookerbean.SnookerListBean;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchOddsBean;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchScoreBean;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchesBean;
import com.hhly.mlottery.bean.snookerbean.SnookerOddsSocketBean;
import com.hhly.mlottery.bean.snookerbean.SnookerScoreSocketBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.SnookerSettingEvent;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


/**
 * Created by yixq on 2016/11/15.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerListActivity extends BaseWebSocketActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
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
    private ExactSwipeRefrashLayout mRefresh;

    private ImageView mBackImage;
    private List<SnookerMatchesBean> currentAllData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.snooker_list_activity);

        /**
         * 推送
         */
        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic("USER.topic.snooker");

        //注册
        EventBus.getDefault().register(this);

        initView();

        initData();

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SnookerListActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //网络异常
        mErrorLayout = (LinearLayout) findViewById(R.id.snooker_error_layout);
        //点击刷新
        mRefreshText = (TextView) findViewById(R.id.snooker_reloading_txt);
        mRefreshText.setOnClickListener(this);
        //暂无数据
        mNoData = (TextView) findViewById(R.id.snooker_nodata_txt);

        mRefresh = (ExactSwipeRefrashLayout) findViewById(R.id.snooker_refresh_layout);
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

    private void setStatus(int status) {
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
                List<SnookerLeaguesBean> dataBean = jsonBean.getData().getLeagues();

                //数据加载
                setData(dataBean);
                Toast.makeText(getApplicationContext(), "yxq>>>>>> " + dataBean.get(0).getDate() + "---" + dataBean.get(0).getLeaguesName(), Toast.LENGTH_SHORT).show();

                connectWebSocket();

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mRefresh.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "yxq>>>>>> " + "---", Toast.LENGTH_SHORT).show();
            }
        }, SnookerListBean.class);
    }

    private String currentDate = "";
    private String currentLeaguesId = "";

    private void setData(List<SnookerLeaguesBean> dataBean) {

        currentAllData = new ArrayList<>();
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
                currentAllData.add(mMatchData);

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
            currentAllData.add(mLeaguesData);

            /**
             * Type = 2 类型三， Item == 比赛
             */
            List<SnookerMatchesBean> datas = dataBean.get(i).getMatches();
            for (int j = 0; j < datas.size(); j++) {
                datas.get(j).setItemType(2);
                currentAllData.add(datas.get(j));
            }
        }

        if (mAdapter == null) {
            mAdapter = new SnookerRecyclerAdapter(getApplicationContext(), currentAllData);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            updateAdapter();
        }

        mAdapter.openLoadMore(0,true);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Toast.makeText(SnookerListActivity.this, "加载更多.....", Toast.LENGTH_SHORT).show();
                L.d("yxq123456==","aaaaaaaaaaaaaaaaaaa");

            }
        });

    }

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

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(false);
                initData();
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.snooker_reloading_txt:
                Toast.makeText(getApplicationContext(), "点击了刷新···", Toast.LENGTH_SHORT).show();
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
        L.d("zxcvbn", "=======" + snookerSettingEvent.getmMsg());
        Toast.makeText(getApplicationContext(), snookerSettingEvent.getmMsg() + " = yxq------", Toast.LENGTH_SHORT).show();
        updateAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onTextResult(String text) {

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

        if (!"".equals(type)) {
            Message msg = Message.obtain();
            msg.obj = text;
            msg.arg1 = Integer.parseInt(type);
            mWebSocketHandler.sendMessage(msg);
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
                    updataOdds(mSnookerOdds);
                }
        }
    };

    /**
     * 比分更新
     * @param mScoreData
     */
    private void updataScore(SnookerScoreSocketBean mScoreData){
        L.d("yxq123456===>>" , "asdfasdfasdf");
        SnookerScoreSocketBean.SnookerScoreDataBean scoreData = mScoreData.getData();

        synchronized (currentAllData){
            for (SnookerMatchesBean match : currentAllData) {
                if (match.getThirdId().equals(mScoreData.getThirdId())) {
                    if (match.getMatchScore() != null) {
                        SnookerMatchScoreBean matchData = match.getMatchScore();
                        updataItemData(matchData , scoreData);
                    }else{
                        SnookerMatchScoreBean newMatchData = new SnookerMatchScoreBean();
                        updataItemData(newMatchData , scoreData);
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
        L.d("yxq123456===>>" , "ASDFASDFASDF");
        SnookerMatchOddsBean socketOddsData = mOddsData.getData().getMatchOdds();
        synchronized (currentAllData){
            for (SnookerMatchesBean match : currentAllData) {
                if (match.getThirdId().equals(mOddsData.getThirdId())) {
                    SnookerMatchOddsBean currentOddsData =  match.getMatchOdds();
                    updataOddsData(currentOddsData , socketOddsData);

                    /*
                     http://m.1332255.com:81/mlottery/core/snookerMatch.getSnookerLeagues.do?dateLeaguesId=2016-11-20_125358,2016-11-19_125358
                     */

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
     * 赔率更新
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
