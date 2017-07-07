package com.hhly.mlottery.frame.snookerframe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.SnookerMatchDetailActivity;
import com.hhly.mlottery.adapter.snooker.SnookerListAdapter;
import com.hhly.mlottery.bean.scheduleBean.ScheduleDate;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchScoreBean;
import com.hhly.mlottery.bean.snookerbean.SnookerScoreSocketBean;
import com.hhly.mlottery.bean.snookerbean.snookerschedulebean.SnookerEventsBean;
import com.hhly.mlottery.bean.snookerbean.snookerschedulebean.SnookerOddsMatchBean;
import com.hhly.mlottery.bean.snookerbean.snookerschedulebean.SnookerScheuleBean;
import com.hhly.mlottery.bean.snookerbean.snookerschedulebean.SnookerSocketOddsBean;
import com.hhly.mlottery.callback.RecyclerViewItemClickListener;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.scorefrag.SnookerListScoreFragment;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.SnookerSettingEvent;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.LoadMoreRecyclerView;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

import static android.content.ContentValues.TAG;

/**
 * Created by yixq on 2017/2/16.
 * mail：yixq@13322.com
 * describe: snooker 即时列表
 */

public class SnookerImmediateFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener{

    private Context mContext;
    private View mView;
    private LoadMoreRecyclerView mRecyclerView;
    private LinearLayout mErrorLayout;
    private TextView mRefreshText;
    private TextView mNoData;
    private ExactSwipeRefreshLayout mRefresh;
    private LinearLayoutManager linearLayoutManager;

    private List<SnookerEventsBean> allData;//页面所有数据
    private SnookerListAdapter mSnookerListAdapter;
    /**
     * 标记不同类型item
     */
    private int DATETYPE = 0;
    private int LEAGUETYPE = 1;
    private int MATCHTYPE = 2;
    //显示状态
    private static final int SHOW_STATUS_LOADING = 1;//加载中
    private static final int SHOW_STATUS_ERROR = 2;//加载失败
    private static final int SHOW_STATUS_NO_DATA = 3;//暂无数据
    private static final int SHOW_STATUS_SUCCESS = 4;//加载成功
    private final static int SHOW_STATUS_REFRESH_ONCLICK = 5;//点击刷新
    private final static int SHOW_STATUS_CURRENT_ONDATA = 6;//当前日期无数据
    private LinearLayout mLoading;
    private TextView mCurrentNoData;

    public static SnookerImmediateFragment newInstance() {
        Bundle args = new Bundle();
        SnookerImmediateFragment fragment = new SnookerImmediateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.snooker_schedule_fragment , container , false);
        initView();
        setStatus(SHOW_STATUS_LOADING);
        initData();
        return mView;
    }
    public void loadData(){
        /**
         * tab切换时调用，即时页面需要重新请求数据（数据实时）
         */
        setStatus(SHOW_STATUS_LOADING);
        initData();
    }
    private void initView() {

        mRecyclerView = (LoadMoreRecyclerView) mView.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //网络异常
        mErrorLayout = (LinearLayout) mView.findViewById(R.id.snooker_error_layout);
        //点击刷新
        mRefreshText = (TextView) mView.findViewById(R.id.snooker_reloading_txt);
        mRefreshText.setOnClickListener(this);
        //暂无数据
        mNoData = (TextView) mView.findViewById(R.id.snooker_nodata_txt);
        //加载中...
        mLoading = (LinearLayout) mView.findViewById(R.id.snooker_loading_ll);

        //当前日期下无数据
        mCurrentNoData = (TextView) mView.findViewById(R.id.snooker_current_nodata);

        mRefresh = (ExactSwipeRefreshLayout) mView.findViewById(R.id.snooker_refresh_layout);
        mRefresh.setColorSchemeResources(R.color.bg_header);
        mRefresh.setOnRefreshListener(this);
        mRefresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getActivity(), StaticValues.REFRASH_OFFSET_END));
    }

    /**
     * 设置显示状态
     * @param status
     */
    private void setStatus(int status) {

        if (status == SHOW_STATUS_LOADING) {
            mRefresh.setVisibility(View.VISIBLE);
            mRefresh.setRefreshing(true);
        } else if (status == SHOW_STATUS_SUCCESS) {
            mRefresh.setVisibility(View.VISIBLE);
            mRefresh.setRefreshing(false);
        } else if (status == SHOW_STATUS_REFRESH_ONCLICK) {
            mRefresh.setVisibility(View.GONE);
            mRefresh.setRefreshing(true);
        } else if(status == SHOW_STATUS_CURRENT_ONDATA){
            mRefresh.setVisibility(View.VISIBLE);
            mRefresh.setRefreshing(false);
        }else {
            mRefresh.setVisibility(View.GONE);
            mRefresh.setRefreshing(false);
        }
        mLoading.setVisibility((status == SHOW_STATUS_REFRESH_ONCLICK) ? View.VISIBLE : View.GONE);
        mErrorLayout.setVisibility(status == SHOW_STATUS_ERROR ? View.VISIBLE : View.GONE);
        mNoData.setVisibility(status == SHOW_STATUS_NO_DATA ? View.VISIBLE : View.GONE);
        mCurrentNoData.setVisibility(status == SHOW_STATUS_CURRENT_ONDATA ? View.VISIBLE : View.GONE);
    }

    private void initData(){
//        String url = "http://192.168.31.12:8080/mlottery/core/snookerMatch.getFirstSnookerMatch.do";//http://192.168.31.12:8080/mlottery/core/snookerMatch.getFirstSnookerMatch.do?lang=zh&timeZone=8

        VolleyContentFast.requestJsonByGet(BaseURLs.SNOOKER_IMMEDIATE_URL, new VolleyContentFast.ResponseSuccessListener<SnookerScheuleBean>() {
            @Override
            public void onResponse(SnookerScheuleBean jsonData) {

                if (jsonData == null || jsonData.getData() == null ) {
                    setStatus(SHOW_STATUS_NO_DATA);
                    return;
                }
                if (jsonData.getData().getLiveBattle() == null || jsonData.getData().getLiveBattle().size() == 0) {
                    setStatus(SHOW_STATUS_NO_DATA);
                    return;
                }
                L.d("qwer====>> " , jsonData.getData().getDate() + " <==> " + jsonData.getData().getLiveBattle().size());
                //记录所有比赛的list 用于联赛分类
                List<SnookerEventsBean> datalist = jsonData.getData().getLiveBattle();
                //记录某个比赛所在的联赛，用于相同联赛的比赛分类
                String currentLeague = "";
                allData = new ArrayList<>();

                for (SnookerEventsBean all : datalist) {

                    if (currentLeague.equals("") || !all.getLeagueName().equals(currentLeague)) {
                        SnookerEventsBean leagueName = new SnookerEventsBean();
                        leagueName.setItemType(LEAGUETYPE);
                        leagueName.setItemLeaguesName(all.getLeagueName());
                        leagueName.setMatchId("-");//推送消息时候需要用到matchid 做对比
                        allData.add(leagueName);
                        currentLeague = all.getLeagueName();
                    }

                    all.setItemType(MATCHTYPE);
                    allData.add(all);
                }

                if (mSnookerListAdapter == null) {
                    mSnookerListAdapter = new SnookerListAdapter(mContext, allData);
                    mRecyclerView.setAdapter(mSnookerListAdapter);

                } else {
                    updateAdapter();
                }
                mSnookerListAdapter.setmOnItemClickListener(new RecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, String data) {
                        Intent intent = new Intent(getActivity(), SnookerMatchDetailActivity.class);
                        intent.putExtra("matchId" , data);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                    }
                });
                setStatus(SHOW_STATUS_SUCCESS);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                setStatus(SHOW_STATUS_ERROR);
            }
        },SnookerScheuleBean.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.snooker_reloading_txt:
                setStatus(SHOW_STATUS_REFRESH_ONCLICK);
                initData();
                break;

        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setStatus(SHOW_STATUS_LOADING);
                initData();
            }
        }, 1000);

        ((SnookerListScoreFragment)getParentFragment()).reconnectWebSocket();
    }

    private List<ScheduleDate> mDateList; // 日期
    /**
     * 更新数据
     */
    public void updateAdapter() {
        if (mSnookerListAdapter == null) {
            return;
        }
        mSnookerListAdapter.updateDatas(allData);
        mSnookerListAdapter.notifyDataSetChanged();
    }
    /**
     * 设置返回
     */
    public void onEventMainThread(SnookerSettingEvent snookerSettingEvent) {
//        L.d("yxq=====> 设置页返回", "=======" + snookerSettingEvent.getmMsg());
        updateAdapter();
    }

    /**
     * 详情页面返回
     *
     * @param id
     */
    public void onEventMainThread(String id) {
        updateAdapter();
    }

    /**
     * 推送
     */
    public void onEventMainThread(SnookerListScoreFragment.SnookerScoresWebSocketEntity eventData){
        if (mSnookerListAdapter == null) {
            return;
        }
        String type = "";
        try {
            JSONObject jsonObject = new JSONObject(eventData.text);
            type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        L.d("yxq=====>type= ", "type = " + type);

        if (!"".equals(type)) {
            Message msg = Message.obtain();
            msg.obj = eventData.text;
            msg.arg1 = Integer.parseInt(type);
            mWebSocketHandler.sendMessage(msg);
//            L.d("yxq=====>发送msg" , "mWebSocketHandler调用");
        }
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
                SnookerSocketOddsBean mSnookerOdds = null;
                try {
                    mSnookerOdds = JSON.parseObject(ws_json , SnookerSocketOddsBean.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    mSnookerOdds = JSON.parseObject(ws_json , SnookerSocketOddsBean.class);
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

        synchronized (allData){
            for (SnookerEventsBean match : allData) {
//                L.d("yxq=====>MatchId = " , match.getMatchId());
                if (match.getMatchId().equals(mScoreData.getThirdId())) {
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
        if (data.getTotalGames() != null) {
            matchData.setTotalGames(data.getTotalGames());
        }
    }
    /**
     * 赔率更新
     * @param mOddsData
     */
    private void updataOdds(SnookerSocketOddsBean mOddsData){
//        L.d("yxq=====> 赔率更新" , "updataOdds()调用");
        synchronized (allData){
            for (SnookerEventsBean match : allData) {
                if (match.getMatchId().equals(mOddsData.getThirdId())) {
                    if (match.getMatchOdds() != null) {
                        SnookerOddsMatchBean currentOddsData =  match.getMatchOdds();
                        updataOddsData(currentOddsData , mOddsData);
                    }else{
                        /**
                         * MatchOdds 为null  即，无赔率==>有赔率情况处理
                         */
                        SnookerOddsMatchBean newCurrentOddsData =  new SnookerOddsMatchBean();
                        updataOddsData(newCurrentOddsData , mOddsData);//更新单条数据赔率
                        match.setMatchOdds(newCurrentOddsData);//赋值，给为null的MatchOdds设值
                    }
                    updateAdapter();
                    break;
                }
            }
        }
    }
    /**
     * 赔率更新（单条数据更新）
     */
    private void updataOddsData(SnookerOddsMatchBean currentOddsData , SnookerSocketOddsBean socketOddsData){

        if (socketOddsData.getData() != null) {

            SnookerSocketOddsBean.SnookerDataBean sockerData = socketOddsData.getData();
            if (sockerData.getPlayType() != null) {

                switch (sockerData.getPlayType()){
                    case "asiaSize"://大小球
                        if (currentOddsData.getAsiaSize() == null) {
                            currentOddsData.setAsiaSize(setNewOddsData());
                        }
                        setOddsData(currentOddsData.getAsiaSize() ,sockerData);
                        break;
                    case "asiaLet"://亚盘
                        if (currentOddsData.getAsiaLet() == null) {
                            currentOddsData.setAsiaLet(setNewOddsData());
                        }
                        setOddsData(currentOddsData.getAsiaLet() ,sockerData);
                        break;
                    case "oneTwo"://单双
                        if (currentOddsData.getOneTwo() == null) {
                            currentOddsData.setOneTwo(setNewOddsData());
                        }
                        setOddsData(currentOddsData.getOneTwo() ,sockerData);
                        break;
                    case "onlyWin"://独赢[欧赔]
                        if (currentOddsData.getOnlyWin() == null) {
                            currentOddsData.setOnlyWin(setNewOddsData());
                        }
                        setOddsData(currentOddsData.getOnlyWin() ,sockerData);
                        break;
                }
            }
        }
    }

    /**
     * 处理赔率推送结构与原数据结构不一致
     * @param currentOddsHandicap
     * @param sockerData
     */
    private void setOddsData(SnookerOddsMatchBean.SnookerMatchOddsDetailsBean currentOddsHandicap , SnookerSocketOddsBean.SnookerDataBean sockerData){
        if (sockerData.getCompany() != null) {
            switch (sockerData.getCompany()){
//                SB,SBO, IBC,ISN,VinBet
                case "SBO"://浩博
                    currentOddsHandicap.getSBO().setHandicap(sockerData.getPlayType());
                    currentOddsHandicap.getSBO().setHandicapValue(sockerData.getHandicapValue());
                    currentOddsHandicap.getSBO().setLeftOdds(sockerData.getLeftOdds());
                    currentOddsHandicap.getSBO().setRightOdds(sockerData.getRightOdds());
                    break;
                case "VinBet"://利记
                    currentOddsHandicap.getVinBet().setHandicap(sockerData.getPlayType());
                    currentOddsHandicap.getVinBet().setHandicapValue(sockerData.getHandicapValue());
                    currentOddsHandicap.getVinBet().setLeftOdds(sockerData.getLeftOdds());
                    currentOddsHandicap.getVinBet().setRightOdds(sockerData.getRightOdds());
                    break;
                case "SB"://沙巴
                    currentOddsHandicap.getSB().setHandicap(sockerData.getPlayType());
                    currentOddsHandicap.getSB().setHandicapValue(sockerData.getHandicapValue());
                    currentOddsHandicap.getSB().setLeftOdds(sockerData.getLeftOdds());
                    currentOddsHandicap.getSB().setRightOdds(sockerData.getRightOdds());
                    break;
                case "xyy"://雪缘圆
                    currentOddsHandicap.getXyy().setHandicap(sockerData.getPlayType());
                    currentOddsHandicap.getXyy().setHandicapValue(sockerData.getHandicapValue());
                    currentOddsHandicap.getXyy().setLeftOdds(sockerData.getLeftOdds());
                    currentOddsHandicap.getXyy().setRightOdds(sockerData.getRightOdds());
                    break;
                case "IBC":
                    currentOddsHandicap.getIBC().setHandicap(sockerData.getPlayType());
                    currentOddsHandicap.getIBC().setHandicapValue(sockerData.getHandicapValue());
                    currentOddsHandicap.getIBC().setLeftOdds(sockerData.getLeftOdds());
                    currentOddsHandicap.getIBC().setRightOdds(sockerData.getRightOdds());
                    break;
                case "ISN":
                    currentOddsHandicap.getISN().setHandicap(sockerData.getPlayType());
                    currentOddsHandicap.getISN().setHandicapValue(sockerData.getHandicapValue());
                    currentOddsHandicap.getISN().setLeftOdds(sockerData.getLeftOdds());
                    currentOddsHandicap.getISN().setRightOdds(sockerData.getRightOdds());
                    break;
            }
        }
    }

    /**
     * 设置空值赔率（推送一些原来没有的赔率数据时处理）
     * @return
     */
    private SnookerOddsMatchBean.SnookerMatchOddsDetailsBean setNewOddsData(){
            SnookerOddsMatchBean.SnookerMatchOddsDetailsBean newAsiaSizeData = new SnookerOddsMatchBean.SnookerMatchOddsDetailsBean();
            SnookerOddsMatchBean.SnookerMatchOddsDetailsBean.SnookerMatchOddsDataBean newCompanyData = new SnookerOddsMatchBean.SnookerMatchOddsDetailsBean.SnookerMatchOddsDataBean();
            newAsiaSizeData.setSB(newCompanyData);
            newAsiaSizeData.setSBO(newCompanyData);
            newAsiaSizeData.setVinBet(newCompanyData);
            newAsiaSizeData.setXyy(newCompanyData);
            newAsiaSizeData.setIBC(newCompanyData);
            newAsiaSizeData.setISN(newCompanyData);

        return newAsiaSizeData;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
