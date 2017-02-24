package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.bean.footballDetails.AnalyzeBean;
import com.hhly.mlottery.bean.snookerbean.snookerDetail.SnookerAnalyzeBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.snookerFragment.SnookerAnalyzeFragment;
import com.hhly.mlottery.frame.snookerFragment.SnookerLetFragment;
import com.hhly.mlottery.frame.snookerFragment.SnookerSizeFragment;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SnookerMatchDetail extends BaseWebSocketActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{

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


    private TabsAdapter mTabsAdapter;
    private String Titles[];
    //fragment
    private SnookerAnalyzeFragment mAnalyzeFragment;
    private SnookerLetFragment mLetFragment;
    private SnookerSizeFragment mSizeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooker_match_detail);
        ButterKnife.bind(this);

        initView();
        setListener();
        initData();

    }


    private void initView() {
        Titles=new String[]{"分析","亚盘","大小球"};
        mTabsAdapter=new TabsAdapter(getSupportFragmentManager());
        mTabsAdapter.setTitles(Titles);

        mAnalyzeFragment=SnookerAnalyzeFragment.newInstance("","");
        mLetFragment=SnookerLetFragment.newInstance("","");
        mSizeFragment=SnookerSizeFragment.newInstance("","");

        mTabsAdapter.addFragments(mAnalyzeFragment,mLetFragment,mSizeFragment);

        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(this, StaticValues.REFRASH_OFFSET_END));
    }
    private void setListener(){
        mBack.setOnClickListener(this);
        mScoreLayout.setOnClickListener(this);
        mTotalLayout.setOnClickListener(this);
    }

    private void initData() {
        Map<String, String> params = new HashMap<>();
        params.put("matchId", "832136");
        //  http://192.168.10.242:8181/mlottery/core/footBallMatch.matchAnalysis.do?thirdId=278222&lang=zh
        String url="http://192.168.31.162:8080/mlottery/core/snookerAnalysis.findAnalysisInfo.do";

        VolleyContentFast.requestJsonByGet(BaseURLs.SNOOKER_ANALYZE_URL,params, new VolleyContentFast.ResponseSuccessListener<SnookerAnalyzeBean>() {
            @Override
            public void onResponse(SnookerAnalyzeBean analyzeBean) {
                if(analyzeBean.getResult().equals("200")){
                    loadData(analyzeBean);

                }

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


        ImageLoader.load(this,matchInfoEntity.getHomeAvatar(),R.mipmap.basket_default).into(mHomeIcon);
        ImageLoader.load(this,matchInfoEntity.getGuestAvater(),R.mipmap.basket_default).into(mGuestIcon);

    }


    @Override
    protected void onTextResult(String text) {

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
                initData();
            }
        },1000);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.snooker_detail_back:
                finish();
                break;
            case R.id.layout_snooker_immediate_score:
                //弹出详细比分
                break;
            case R.id.layout_snooker_total_score:

                break;
        }

    }
}
