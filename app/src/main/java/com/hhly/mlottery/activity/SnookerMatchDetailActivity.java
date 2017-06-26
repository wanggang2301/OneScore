package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.bean.snookerbean.snookerDetail.SnookerAnalyzeBean;
import com.hhly.mlottery.bean.snookerbean.snookerDetail.SnookerScoreSocketBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BaseUserTopics;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.snookerFragment.SnookerAnalyzeFragment;
import com.hhly.mlottery.frame.snookerFragment.SnookerLetFragment;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by mdy
 * 斯诺克内页
 */
public class SnookerMatchDetailActivity extends BaseWebSocketActivity implements ExactSwipeRefreshLayout.OnRefreshListener,View.OnClickListener{

    @BindView(R.id.snooker_details_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.snooker_details_tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.snooker_details_view_pager)
    ViewPager mViewPager; //底部fragment的viewpager

    @BindView(R.id.snooker_header_title)
    TextView mGameTitle;

    @BindView(R.id.snooker_header_date)
    TextView mGameDate;

    @BindView(R.id.snooker_header_time)
    TextView mGameTime;

    @BindView(R.id.snooker_header_round)
    TextView mGameRound;

    @BindView(R.id.snooker_home_icon)
    ImageView mHomeIcon;

    @BindView(R.id.snooker_guest_icon)
    ImageView mGuestIcon;

    @BindView(R.id.snooker_home_name)
    TextView mHomeName;

    @BindView(R.id.snooker_guest_name)
    TextView mGuestName;

    @BindView(R.id.snooker_home_score)
    TextView mHomeScore;

    @BindView(R.id.snooker_guest_score)
    TextView mGuestScore;

    @BindView(R.id.snooker_home_total_score)
    TextView mHomeTotalScore;

    @BindView(R.id.snooker_guest_total_score)
    TextView mGuestTotalScore;

    @BindView(R.id.snooker_total_game)
    TextView mGameNumber;

    @BindView(R.id.snooker_details_refresh_layout)
    ExactSwipeRefreshLayout mRefreshLayout; //下拉刷新

    @BindView(R.id.snooker_detail_back)
    ImageView mBack; //返回按钮
    @BindView(R.id.layout_snooker_immediate_score)
    LinearLayout mScoreLayout; //点击弹出详细比分
    @BindView(R.id.layout_snooker_total_score)
    LinearLayout mTotalLayout; //点击弹出详细比分

    public final static String ODDS_LET="2";
    public final static String ODDS_SIZE="3";
    private String mMatchId="1";


    private TabsAdapter mTabsAdapter;
    private String Titles[];
    //fragment
    private SnookerAnalyzeFragment mAnalyzeFragment;
    private SnookerLetFragment mLetFragment;
    private SnookerLetFragment mSizeFragment;
    private SnookerAnalyzeBean mAnalyzeBean=new SnookerAnalyzeBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setWebSocketUri(BaseURLs.WS_SERVICE);
//        setWebSocketUri("ws://192.168.10.242:61634");
//        setTopic("USER.topic.snooker");
        setTopic(BaseUserTopics.snookerMatch);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooker_match_detail);
        ButterKnife.bind(this);


        if(getIntent()!=null){
            mMatchId=getIntent().getExtras().getString("matchId");
        }

        initView();
        setListener();
        initData();


    }


    private void initView() {
        setSupportActionBar(mToolbar);
        Titles=new String[]{getResources().getString(R.string.basket_analyze),getResources().getString(R.string.basket_alet),getResources().getString(R.string.basket_asize)};
        mTabsAdapter=new TabsAdapter(getSupportFragmentManager());
        mTabsAdapter.setTitles(Titles);

        mAnalyzeFragment=SnookerAnalyzeFragment.newInstance(mMatchId,"");
        mLetFragment=SnookerLetFragment.newInstance(mMatchId,ODDS_LET);
        mSizeFragment=SnookerLetFragment.newInstance(mMatchId,ODDS_SIZE);

        mTabsAdapter.addFragments(mAnalyzeFragment,mLetFragment,mSizeFragment);

        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(this, StaticValues.REFRASH_OFFSET_END));
    }
    private void setListener(){
        mBack.setOnClickListener(this);
        mHomeIcon.setOnClickListener(this);
        mGuestIcon.setOnClickListener(this);
        mScoreLayout.setOnClickListener(this);
        mTotalLayout.setOnClickListener(this);
        //首次進來可以刷新
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });
    }

    private void initData() {
        Map<String, String> params = new HashMap<>();
        params.put("matchId", mMatchId);
//        //  http://192.168.10.242:8181/mlottery/core/footBallMatch.matchAnalysis.do?thirdId=278222&lang=zh
//        String url="http://192.168.31.162:8080/mlottery/core/snookerAnalysis.findAnalysisInfo.do";

        VolleyContentFast.requestJsonByGet(BaseURLs.SNOOKER_ANALYZE_URL,params, new VolleyContentFast.ResponseSuccessListener<SnookerAnalyzeBean>() {
            @Override
            public void onResponse(SnookerAnalyzeBean analyzeBean) {

                if(mRefreshLayout.isRefreshing()){
                    mRefreshLayout.setRefreshing(false);
                }
                if(analyzeBean.getResult().equals("200")){
                    loadData(analyzeBean);
                    mAnalyzeBean=analyzeBean;

                }
                connectWebSocket(); //链接推送
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        },SnookerAnalyzeBean.class);

    }

    private void loadData(SnookerAnalyzeBean analyzeBean){
        SnookerAnalyzeBean.MatchInfoEntity matchInfoEntity=analyzeBean.getMatchInfo();
        mGameDate.setText(matchInfoEntity.getMatchDate()==null?"":matchInfoEntity.getMatchDate());
        mGameTime.setText(matchInfoEntity.getMatchTime()==null?"":matchInfoEntity.getMatchTime());
        mGameTitle.setText(matchInfoEntity.getMatchName()==null?"":matchInfoEntity.getMatchName());
        mGameRound.setText(matchInfoEntity.getRound()==null?"":matchInfoEntity.getRound());

        mHomeScore.setText(matchInfoEntity.getHomeScore()==null?"0":matchInfoEntity.getHomeScore());
        mGuestScore.setText(matchInfoEntity.getGuestScore()==null?"0":matchInfoEntity.getGuestScore());
        mHomeTotalScore.setText(matchInfoEntity.getHomeBoardScore()==null?"0":matchInfoEntity.getHomeBoardScore());
        if(matchInfoEntity.getTotalBoard()!=null&&matchInfoEntity.getTotalBoard()!=""){
            if(Integer.parseInt(matchInfoEntity.getHomeBoardScore())>Integer.parseInt(matchInfoEntity.getTotalBoard())/2){
                mHomeTotalScore.setTextColor(getResources().getColor(R.color.red));
            }
            if(Integer.parseInt(matchInfoEntity.getGuestBoardScore())>Integer.parseInt(matchInfoEntity.getTotalBoard())/2){
                mGuestTotalScore.setTextColor(getResources().getColor(R.color.red));
            }
        }


        mGuestTotalScore.setText(matchInfoEntity.getGuestBoardScore()==null?"0":matchInfoEntity.getGuestBoardScore());
        mGameNumber.setText(matchInfoEntity.getTotalBoard()==null?"(0)":"("+matchInfoEntity.getTotalBoard()+")");
        mHomeName.setText(matchInfoEntity.getHomePlayer()==null?"":matchInfoEntity.getHomePlayer());
        mGuestName.setText(matchInfoEntity.getGuestPlayer()==null?"":matchInfoEntity.getGuestPlayer());


        ImageLoader.load(this,matchInfoEntity.getHomeAvatar(),R.mipmap.snooker_detail_default).into(mHomeIcon);
        ImageLoader.load(this,matchInfoEntity.getGuestAvater(),R.mipmap.snooker_detail_default).into(mGuestIcon);

    }

    Handler mWebSocketHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            L.e(TAG, "__handleMessage__");
            L.e(TAG, "msg.arg1 = " + msg.arg1);
            if (msg.arg1 == 300) {  // 比分推送
                String ws_json = (String) msg.obj;
                L.e(TAG, "ws_json_snooker_scoredetail = " + ws_json);
                SnookerScoreSocketBean mSnookerScore = null;
                try {
                    mSnookerScore = JSON.parseObject(ws_json, SnookerScoreSocketBean.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    mSnookerScore = JSON.parseObject(ws_json, SnookerScoreSocketBean.class);
                }
//                L.d("yxq=====>300走这里" , msg.arg1+"");
                if(mSnookerScore.getThirdId().equals(mMatchId)){
                    updateScore(mSnookerScore);
                }

            }
        }
    };

    /**
     * 比分更新
     * @param mScoreData
     */
    private void updateScore(SnookerScoreSocketBean mScoreData){

        SnookerScoreSocketBean.SnookerScoreDataBean scoreData = mScoreData.getData();
        mHomeScore.setText(scoreData.getPoScore());
        mGuestScore.setText(scoreData.getPtScore());
        mHomeTotalScore.setText(scoreData.getPlayerOnewin());
        mGuestTotalScore.setText(scoreData.getPlayerTwowin());


    }
    @Override
    protected void onTextResult(String text) {
        L.d("yxq++++" , text);
        String type="";
        L.e("MatchDetail","tuisong ?");
        try {
            JSONObject jsonObject=new JSONObject(text);
            type=jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!"".equals(type)){
            Message msg=Message.obtain();
            msg.obj=text;
            msg.arg1=Integer.parseInt(type);
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


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                //请求网络进行刷新
                connectWebSocket();
                initData();

                mAnalyzeFragment.initData();
                mLetFragment.initData();
                mSizeFragment.initData();
            }
        },1000);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.snooker_detail_back:
                this.finish();
                break;
            case R.id.layout_snooker_immediate_score:
                //弹出详细比分
                break;
            case R.id.layout_snooker_total_score:

                break;
            case R.id.snooker_home_icon:
                if(mAnalyzeBean.getMatchInfo()!=null&&mAnalyzeBean.getMatchInfo().getHomePlayId()!=null){
                    Intent intent=new Intent(this,SnookerPlayerInfoActivity.class);
                    intent.putExtra("playerId",mAnalyzeBean.getMatchInfo().getHomePlayId());
                    startActivity(intent);

                }

                break;
            case  R.id.snooker_guest_icon:
                if(mAnalyzeBean.getMatchInfo()!=null&&mAnalyzeBean.getMatchInfo().getGuestPlayId()!=null){
                    Intent intent=new Intent(this,SnookerPlayerInfoActivity.class);
                    intent.putExtra("playerId",mAnalyzeBean.getMatchInfo().getGuestPlayId());
                    startActivity(intent);
                }

                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeWebSocket();
        mWebSocketHandler.removeCallbacksAndMessages(null);
    }
}
