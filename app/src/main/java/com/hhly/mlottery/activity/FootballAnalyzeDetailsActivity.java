package com.hhly.mlottery.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.FootballAnalyzeAdapter;
import com.hhly.mlottery.adapter.basketball.FootballAnalyzeFutureAdapter;
import com.hhly.mlottery.bean.footballDetails.FootballAnalyzeDetailsBean;
import com.hhly.mlottery.bean.footballDetails.FootballAnalyzeFuture;
import com.hhly.mlottery.bean.footballDetails.FootballAnaylzeHistoryRecent;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.RoundProgressBar;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;
import com.hhly.mlottery.widget.NestedListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: FootballAnalyzeDetailsActivity
 * @Description: 足球分析详情Activity  更多战绩
 * @author yixq
 */
public class FootballAnalyzeDetailsActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public final static String FOOTBALL_ANALYZE_THIRD_ID = "analyzeId";
    private ImageView mBack;
    private String mThirdId;
    private NestedListView mHistoryListView;//历史交锋 listview
    private NestedListView mRecentListView1;//近期战绩 1 listview
    private NestedListView mRecentListView2;//近期战绩 2 listview
    private NestedListView mFutureListView1;//未来比赛 2 listview
    private NestedListView mFutureListView2;//未来比赛 2 listview

    private FootballAnalyzeAdapter mHistoryAdaptey; //历史交锋 adapter
    private FootballAnalyzeAdapter mRecentAdapter1; //近期战绩 1 adapter
    private FootballAnalyzeAdapter mRecentAdapter2; //近期战绩 2 adapter
    private FootballAnalyzeFutureAdapter mFutureAdapter1; //未来比赛 1 adapter
    private FootballAnalyzeFutureAdapter mFutureAdapter2; //未来比赛 2 adapter
    private TextView mFootballAnalyzeHistory;
    private TextView mFootballAnalyzeRecent1;
    private TextView mFootballAnalyzeRecent2;

    private TextView mFootballAnalyzeHistoryB;
    private TextView mFootballAnalyzeRecent1B;
    private TextView mFootballAnalyzeRecent2B;

    private ImageView mHistoryScreen;
    private ImageView mRecentScreen;

    private LinearLayout mErrorLoad;
    private LinearLayout mSuccessLoad;
    private LinearLayout mHistory_ll;
    private LinearLayout mGuestRecent_ll;
    private LinearLayout mHomeRecent_ll;
    private LinearLayout mGuestFuture_ll;
    private LinearLayout mHomeFuture_ll;
    private TextView mGuestFruture;
    private TextView mHomeFruture;
    Handler mHandler = new Handler();
    private TextView mError;
    private ExactSwipeRefreshLayout mRefresh;
    private TextView mNoData1;
    private TextView mNoData2;
    private TextView mNoData3;
    private TextView mNoData4;
    private TextView mNoData5;
    private TextView mNodataTextview;
    private RoundProgressBar mRoundProgressBarHistory1; // 环形条
    private RoundProgressBar mRoundProgressBarHistory2;
    private RoundProgressBar mRoundProgressBarHistory3;

    private RoundProgressBar mRoundProgressBarHome1;
    private RoundProgressBar mRoundProgressBarHome2;
    private RoundProgressBar mRoundProgressBarHome3;

    private RoundProgressBar mRoundProgressBarGuest1;
    private RoundProgressBar mRoundProgressBarGuest2;
    private RoundProgressBar mRoundProgressBarGuest3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.football_analyze_details);
        if(getIntent().getExtras() != null){
            mThirdId = getIntent().getExtras().getString(FOOTBALL_ANALYZE_THIRD_ID);
        }

        initView();
        mHandler.postDelayed(mRun , 500); // 加载数据
    }

    /**
     * 子线程 处理数据加载
     */
    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            initData();
        }
    };

    private void initView (){

        TextView public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText(R.string.football_analyze_details);

        mBack = (ImageView)findViewById(R.id.public_img_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK, null);
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
            }
        });

        mHistoryListView = (NestedListView)findViewById(R.id.football_histroy_list);
        mRecentListView2 = (NestedListView)findViewById(R.id.football_recent_exploits_list_to);
        mRecentListView1 = (NestedListView)findViewById(R.id.football_recent_exploits_list);
        mFutureListView1 = (NestedListView)findViewById(R.id.football_future_exploits_list);
        mFutureListView2 = (NestedListView)findViewById(R.id.football_future_exploits_list_to);

        mFootballAnalyzeHistory = (TextView)findViewById(R.id.football_analyze_history);
        mFootballAnalyzeRecent1 = (TextView)findViewById(R.id.football_analyze_record_guest);
        mFootballAnalyzeRecent2 = (TextView)findViewById(R.id.football_analyze_record_home);

        mFootballAnalyzeHistoryB = (TextView)findViewById(R.id.football_analyze_history2);
        mFootballAnalyzeRecent1B = (TextView)findViewById(R.id.football_analyze_record_guest2);
        mFootballAnalyzeRecent2B = (TextView)findViewById(R.id.football_analyze_record_home2);

        mHistoryScreen = (ImageView)findViewById(R.id.football_analyze_history_screen);
        mHistoryScreen.setOnClickListener(this);
        mRecentScreen = (ImageView)findViewById(R.id.football_analyze_recent_screen);
        mRecentScreen.setOnClickListener(this);

        mHistory_ll = (LinearLayout)findViewById(R.id.football_analyze_details_history);
        mGuestRecent_ll = (LinearLayout)findViewById(R.id.football_analyze_details_recent_guest);
        mHomeRecent_ll = (LinearLayout)findViewById(R.id.football_analyze_details_recent_home);
        mGuestFuture_ll = (LinearLayout)findViewById(R.id.football_analyze_details_future_guest);
        mHomeFuture_ll = (LinearLayout)findViewById(R.id.football_analyze_details_future_home);

        mErrorLoad = (LinearLayout)findViewById(R.id.football_details_error);
        mErrorLoad.setVisibility(View.GONE);

        mSuccessLoad = (LinearLayout)findViewById(R.id.football_loading_success_ll);
        mSuccessLoad.setVisibility(View.GONE);

        mGuestFruture = (TextView) findViewById(R.id.football_guest_fruture_tv_list);
        mHomeFruture = (TextView) findViewById(R.id.football_home_fruture_tv_list);
        mError = (TextView)findViewById(R.id.football_details_error_btn);
        mError.setOnClickListener(this);

        mRefresh = (ExactSwipeRefreshLayout)findViewById(R.id.football_analyze_details_refreshlayout);
        mRefresh.setColorSchemeResources(R.color.tabhost);
        mRefresh.setOnRefreshListener(FootballAnalyzeDetailsActivity.this);
        mRefresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(this, StaticValues.REFRASH_OFFSET_END));
        mRefresh.setRefreshing(true);

        //暂无数据
        mNodataTextview = (TextView) findViewById(R.id.football_analyze_details_nodata);

        mNoData1 = (TextView) findViewById(R.id.football_analyze_nodata1);
        mNoData2 = (TextView) findViewById(R.id.football_analyze_nodata2);
        mNoData3 = (TextView) findViewById(R.id.football_analyze_nodata3);
        mNoData4 = (TextView) findViewById(R.id.football_analyze_nodata4);
        mNoData5 = (TextView) findViewById(R.id.football_analyze_nodata5);

        mRoundProgressBarHistory1 = (RoundProgressBar)findViewById(R.id.football_history_roundProgressBar1);
        mRoundProgressBarHistory2 = (RoundProgressBar)findViewById(R.id.football_history_roundProgressBar2);//#E8C129
        mRoundProgressBarHistory3 = (RoundProgressBar)findViewById(R.id.football_history_roundProgressBar3); //#7DC050
        mRoundProgressBarHistory1.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_analyze_width));
        mRoundProgressBarHistory2.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_analyze_width));
        mRoundProgressBarHistory3.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_analyze_width));

        mRoundProgressBarHome1 = (RoundProgressBar)findViewById(R.id.football_home_roundProgressBar1);
        mRoundProgressBarHome2 = (RoundProgressBar)findViewById(R.id.football_home_roundProgressBar2);
        mRoundProgressBarHome3 = (RoundProgressBar)findViewById(R.id.football_home_roundProgressBar3);
        mRoundProgressBarHome1.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_analyze_width));
        mRoundProgressBarHome2.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_analyze_width));
        mRoundProgressBarHome3.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_analyze_width));


        mRoundProgressBarGuest1 = (RoundProgressBar)findViewById(R.id.football_guest_roundProgressBar1);
        mRoundProgressBarGuest2 = (RoundProgressBar)findViewById(R.id.football_guest_roundProgressBar2);
        mRoundProgressBarGuest3 = (RoundProgressBar)findViewById(R.id.football_guest_roundProgressBar3);
        mRoundProgressBarGuest1.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_analyze_width));
        mRoundProgressBarGuest2.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_analyze_width));
        mRoundProgressBarGuest3.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_analyze_width));

        findViewById(R.id.public_btn_set).setVisibility(View.GONE); //隐藏设置按键
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE); //隐藏筛选按键
    }

    private List<FootballAnaylzeHistoryRecent> mHistoryData = new ArrayList<>();//历史交锋所有比赛
    private List<FootballAnaylzeHistoryRecent> mRecentData1 = new ArrayList<>();//客队所有近期战绩
    private List<FootballAnaylzeHistoryRecent> mRecentData2 = new ArrayList<>();//主队所有近期战绩
    private List<FootballAnalyzeFuture> mFutureData1 = new ArrayList<>();//客队未来赛事
    private List<FootballAnalyzeFuture> mFutureData2 = new ArrayList<>();//主队所有赛事
    private String mGuestTeam;//客队名
    private String mHomeTeam;//主队名

    private List<FootballAnaylzeHistoryRecent> mHistoryScreenNum; //筛选后的历史交锋
    private List<FootballAnaylzeHistoryRecent> mGuestRecentScreenNum; //筛选后客队的近期战绩
    private List<FootballAnaylzeHistoryRecent> mHomeRecentScreenNum; //筛选后主队的近期战绩

    private boolean mHistorySite = true; //产地选择
    private int mHistoryNum = 6; //场数选择

    private boolean mGuestRecentSite = true; //产地选择
    private int mGuestRecentNum = 6; //场数选择

    private boolean mHomeRecentSite = true; //产地选择
    private int mHomeRecentNum = 6; //场数选择

    private void initData(){
        Map<String, String> params = new HashMap<>();
        if (mThirdId != null) {
            params.put("thirdId", mThirdId);
        }

//        String url = "http://192.168.31.58:8080/mlottery/core/footBallMatch.findAnalysisDetail.do";  //?lang=zh&thirdId=78235  ?lang=zh&thirdId=78235
//        String url = "http://192.168.10.242:8181/mlottery/core/footBallMatch.findAnalysisDetail.do";
        String url = BaseURLs.URL_FOOTBALL_ANALYZE_DETAILS;

        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<FootballAnalyzeDetailsBean>() {
            @Override
            public void onResponse(FootballAnalyzeDetailsBean json) {
                if (json == null || !json.getResult().equals("200")) {
                    mSuccessLoad.setVisibility(View.GONE);
                    mErrorLoad.setVisibility(View.GONE);
                    mNodataTextview.setVisibility(View.VISIBLE);
                } else {

                    if (json.getFutureMatch() == null && json.getBattleHistory() == null && json.getTeamRecent() == null &&
                            json.getResult() == null && json.getHomeTeam() == null && json.getGuestTeam() == null) {

                        mSuccessLoad.setVisibility(View.GONE);
                        mErrorLoad.setVisibility(View.GONE);
                        mNodataTextview.setVisibility(View.VISIBLE);
                    } else {

                        if (json.getGuestTeam() != null) {
                            mGuestTeam = json.getGuestTeam();
                        } else {
                            mGuestTeam = "";
                        }
                        if (json.getHomeTeam() != null) {
                            mHomeTeam = json.getHomeTeam();
                        } else {
                            mHomeTeam = "";
                        }
                        /**
                         * 历史交锋
                         */
                        if (json.getBattleHistory() != null) {
                            mHistoryData = json.getBattleHistory();
                            //暂无数据提示
                            if (mHistoryData.isEmpty() || mHistoryData.size()==0) {
                                mNoData1.setVisibility(View.VISIBLE);
                            }
                            //取前六场
                            List<FootballAnaylzeHistoryRecent> list = new ArrayList<>();
                            if (mHistoryData.size() <= 6) {
                                list = mHistoryData;
                            } else {
                                for (int i = 0; i < 6; i++) {
                                    list.add(mHistoryData.get(i));
                                }
                            }

                            List<FootballAnaylzeHistoryRecent> fistData = new ArrayList<>();
                            setScreen(true, 6, fistData, mHistoryData , true);

                            if (mHistoryAdaptey == null) {
                                mHistoryAdaptey = new FootballAnalyzeAdapter(mContext, fistData, R.layout.football_analyze_details_item);
                                mHistoryListView.setAdapter(mHistoryAdaptey);
                                setHomeWinLoseData(list, mFootballAnalyzeHistory,mFootballAnalyzeHistoryB, mHomeTeam , mRoundProgressBarHistory1 , mRoundProgressBarHistory2 , mRoundProgressBarHistory3);
                            } else {
                                if (mHistoryScreenNum != null) {
                                    updateAdapter(mHistoryScreenNum, mHistoryAdaptey, 0);
                                } else {
                                    updateAdapter(fistData, mHistoryAdaptey, 0);
                                }
                            }
                            mHistory_ll.setVisibility(View.VISIBLE);
                        } else {
                            mHistory_ll.setVisibility(View.GONE);
                            mNoData1.setVisibility(View.VISIBLE);
                        }
                        /**
                         * 近期战绩
                         */
                        if(json.getTeamRecent() != null){
                            /**
                             * 客队近期战绩
                             */
                            if (json.getTeamRecent().getGuest() != null) {
                                mRecentData1 = json.getTeamRecent().getGuest();

                                //暂无数据提示
                                if (mRecentData1.isEmpty() || mRecentData1.size()==0) {
                                    mNoData2.setVisibility(View.VISIBLE);
                                }
                                //默认选中全部场地6场
                                List<FootballAnaylzeHistoryRecent> fistData = new ArrayList<>();
                                setScreen(true, 6, fistData, mRecentData1 , true);

                                //取前六场
                                List<FootballAnaylzeHistoryRecent> list = new ArrayList<>();
                                if (mRecentData1.size() <= 6) {
                                    list = mRecentData1;
                                } else {
                                    for (int i = 0; i < 6; i++) {
                                        list.add(mRecentData1.get(i));
                                    }
                                }

                                if (mRecentAdapter1 == null) {
                                    mRecentAdapter1 = new FootballAnalyzeAdapter(mContext, fistData, R.layout.football_analyze_details_item);
                                    mRecentListView1.setAdapter(mRecentAdapter1);
                                    setHomeWinLoseData(list, mFootballAnalyzeRecent1,mFootballAnalyzeRecent1B, mGuestTeam , mRoundProgressBarGuest1 , mRoundProgressBarGuest2 , mRoundProgressBarGuest3);
                                } else {
                                    if (mGuestRecentScreenNum != null) {
                                        updateAdapter(mGuestRecentScreenNum, mRecentAdapter1, 2);
                                    } else {
                                        updateAdapter(fistData, mRecentAdapter1, 2);
                                    }
                                }
                                mGuestRecent_ll.setVisibility(View.VISIBLE);
                            }else{
                                mGuestRecent_ll.setVisibility(View.GONE);
                                mNoData2.setVisibility(View.VISIBLE);
                            }
                            /**
                             * 主队近期战绩
                             */
                            if (json.getTeamRecent().getHome() != null) {
                                mRecentData2 = json.getTeamRecent().getHome();

                                //暂无数据提示
                                if (mRecentData2.isEmpty() || mRecentData2.size()==0) {
                                    mNoData3.setVisibility(View.VISIBLE);
                                }

                                List<FootballAnaylzeHistoryRecent> fistData = new ArrayList<>();
                                setScreen(true, 6, fistData, mRecentData2 , true);

                                //取前六场
                                List<FootballAnaylzeHistoryRecent> list = new ArrayList<>();
                                if (mRecentData2.size() <= 6) {
                                    list = mRecentData2;
                                } else {
                                    for (int i = 0; i < 6; i++) {
                                        list.add(mRecentData2.get(i));
                                    }
                                }

                                if (mRecentAdapter2 == null) {
                                    mRecentAdapter2 = new FootballAnalyzeAdapter(mContext, fistData, R.layout.football_analyze_details_item);
                                    mRecentListView2.setAdapter(mRecentAdapter2);
                                    setHomeWinLoseData(list, mFootballAnalyzeRecent2,mFootballAnalyzeRecent2B, mHomeTeam , mRoundProgressBarHome1 , mRoundProgressBarHome2 , mRoundProgressBarHome3);
                                } else {
                                    if (mHomeRecentScreenNum != null) {
                                        updateAdapter(mHomeRecentScreenNum, mRecentAdapter2, 1);
                                    } else {
                                        updateAdapter(fistData, mRecentAdapter2, 1);
                                    }
                                }
                                mHomeRecent_ll.setVisibility(View.VISIBLE);
                            } else {
                                mNoData3.setVisibility(View.VISIBLE);
                                mHomeRecent_ll.setVisibility(View.GONE);
                            }
                        }else {
                            mGuestRecent_ll.setVisibility(View.GONE);
                            mHomeRecent_ll.setVisibility(View.GONE);
                            mNoData3.setVisibility(View.VISIBLE);
                        }

                        /**
                         * 客队未来比赛
                         */
                         if(json.getFutureMatch() != null){
                            if (json.getFutureMatch().getGuest() != null) {
                                mFutureData1 = json.getFutureMatch().getGuest();
                                //暂无数据提示
                                if (mFutureData1.isEmpty() || mFutureData1.size()==0) {
                                    mNoData4.setVisibility(View.VISIBLE);
                                }
                                if (json.getGuestTeam() != null) {
                                    /**
                                     * 显示场数
                                     */
    //                            mGuestFruture.setText(json.getGuestTeam() + getResources().getText(R.string.basket_analyze_future) + mFutureData1.size() + getResources().getText(R.string.basket_analyze_field));
                                    /**
                                     * 不显示场数
                                     */
                                    mGuestFruture.setText(json.getGuestTeam() + getResources().getText(R.string.basket_analyze_fruture));
                                } else {
                                    mGuestFruture.setText(getResources().getText(R.string.basket_analyze_defult_text) + "" + getResources().getText(R.string.basket_analyze_fruture));
                                }

                                mFutureAdapter1 = new FootballAnalyzeFutureAdapter(mContext, mFutureData1, R.layout.football_analyze_item);
                                mFutureListView1.setAdapter(mFutureAdapter1);
                                mGuestFuture_ll.setVisibility(View.VISIBLE);   //客队未来三场
                            } else {
                                mGuestFuture_ll.setVisibility(View.GONE);
                                if (json.getFutureMatch().getHome() == null) {
                                    mNoData4.setVisibility(View.VISIBLE);
                                }
                            }
                         }else{
                             mGuestFuture_ll.setVisibility(View.GONE);
                             mNoData4.setVisibility(View.VISIBLE);
                         }
                        /**
                         * 主队未来比赛
                         */
                        if (json.getFutureMatch() != null) {
                            if (json.getFutureMatch().getHome() != null) {
                                mFutureData2 = json.getFutureMatch().getHome();

                                //暂无数据提示
                                if (mFutureData2.isEmpty() || mFutureData2.size()==0) {
                                    mNoData5.setVisibility(View.VISIBLE);
                                }

                                if (json.getHomeTeam() != null) {
                                    mHomeFruture.setText(json.getHomeTeam() + getResources().getText(R.string.basket_analyze_fruture));
                                } else {
                                    mHomeFruture.setText(getResources().getText(R.string.basket_analyze_defult_text) + "" + getResources().getText(R.string.basket_analyze_fruture));
                                }

                                mFutureAdapter2 = new FootballAnalyzeFutureAdapter(mContext, mFutureData2, R.layout.football_analyze_item);
                                mFutureListView2.setAdapter(mFutureAdapter2);
                                mHomeFuture_ll.setVisibility(View.VISIBLE);
                            } else {
                                mHomeFuture_ll.setVisibility(View.GONE);
                                if (json.getFutureMatch().getGuest() == null) {
                                    mNoData4.setVisibility(View.VISIBLE);
                                }
                            }
                        }else{
                            mHomeFuture_ll.setVisibility(View.GONE);
                            mNoData4.setVisibility(View.VISIBLE);
                        }

                        mErrorLoad.setVisibility(View.GONE);
                        mNodataTextview.setVisibility(View.GONE);
                        mSuccessLoad.setVisibility(View.VISIBLE);
                    }
                }
                mRefresh.setRefreshing(false);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                mErrorLoad.setVisibility(View.VISIBLE);
                mSuccessLoad.setVisibility(View.GONE);
                mNodataTextview.setVisibility(View.GONE);

                mRefresh.setRefreshing(false);
            }
        }, FootballAnalyzeDetailsBean.class);
    }

    /**
     * 筛选设置
     * @param isSite
     * @param num
     * @param screenData
     * @param allData
     */
    private void setScreen(boolean isSite , int num , List<FootballAnaylzeHistoryRecent> screenData , List<FootballAnaylzeHistoryRecent> allData , boolean isGuest){

        /**
         *  筛选相同主客场比赛
         */
        List<FootballAnaylzeHistoryRecent> mHistoryScreenSite = new ArrayList<>() ;

        if (isSite) {
            mHistoryScreenSite = allData;
        }else {
            for (FootballAnaylzeHistoryRecent history : allData) {
//                if (history.getGuestTeam().equals(mGuestTeam) && history.getHomeTeam().equals(mHomeTeam)) {
                if (isGuest) {
                    if (history.isHomeGround()) { //取在主场比赛
                        mHistoryScreenSite.add(history);
                    }
                }else{
                    if (!history.isHomeGround()) { //取在客场比赛
                        mHistoryScreenSite.add(history);
                    }
                }
            }
        }
        /**
         * 筛选场数
         */
        if (num == 6) {
            if (mHistoryScreenSite.size() <= 6) {
                for (FootballAnaylzeHistoryRecent history : mHistoryScreenSite) {
                    screenData.add(history);
                }
            }else{
                for (int i = 0; i < 6; i++) {
                    screenData.add(mHistoryScreenSite.get(i));
                }
            }
        }else if (num == 10) {
            if (mHistoryScreenSite.size() <= 10) {
                for (FootballAnaylzeHistoryRecent history : mHistoryScreenSite) {
                    screenData.add(history);
                }
            }else{
                for (int i = 0; i < 10; i++) {
                    screenData.add(mHistoryScreenSite.get(i));
                }
            }
        }else if (num == 15) {
            if (mHistoryScreenSite.size() <= 15) {
                for (FootballAnaylzeHistoryRecent history : mHistoryScreenSite) {
                    screenData.add(history);
                }
            }else{
                for (int i = 0; i < 15; i++) {
                    screenData.add(mHistoryScreenSite.get(i));
                }
            }
        }
    }

    /**
     * 主队胜负数据设置
     * @param mData
     * @param text1
     * @param text2
     * @param team
     */
    private void setHomeWinLoseData(List<FootballAnaylzeHistoryRecent> mData , TextView text1 ,TextView text2 ,String team , RoundProgressBar progressBar1 ,RoundProgressBar progressBar2 , RoundProgressBar progressBar3){

        String homeWin , homeLose , draw;

        //所显示的总场数
//        String matchNum;
        int matchNum = 0; //总场数

        int letmatchNum = 0;//让分开盘场数
        int totmatchNum = 0;//大小开盘场数

        double countWin = 0 ; //胜
        double countLose = 0 ; //负
        double countDraw = 0;  //平

        double countGoal = 0 ; //进球
        double countFumble = 0 ; //失球

        double countTot = 0; // 大球
        double countLet = 0; //让分球

        if (mData.isEmpty() || mData.size()==0) {
            matchNum = 0;
            letmatchNum = 0;
            totmatchNum = 0;
        }else{
            matchNum = mData.size();
            for (FootballAnaylzeHistoryRecent history : mData) {

                //统计 让分开盘数
                if (history.getCasLetGoal() != null && !history.getCasLetGoal().equals("")) {
                    letmatchNum++;
                }
                //统计 大小盘开盘数
                if (history.getCtotScore() != null && !history.getCtotScore().equals("")) {
                    totmatchNum++;
                }


                if (history.getResult() == 1) {
                    countWin++;
                }else if (history.getResult() == -1){
                    countLose++;
                }else if(history.getResult() == 0){
                    countDraw++;
                }

                if(history.getTot() != null){
                    if (history.getTot().equals("1")) {
                        countTot++;
                    }
                }
                if(history.getLet() != null){
                    if (history.getLet().equals("1")) {
                        countLet++;
                    }
                }

                if (history.isHomeGround()) {
                    countGoal += history.getHomeScore();
                    countFumble += history.getGuestScore();
                }else{
                    countGoal += history.getGuestScore();
                    countFumble += history.getHomeScore();
                }
            }
        }

        int winning , totWinning , letWinnging;

        //胜率
        if (matchNum == 0) {
            winning = 0;
        }else{
            winning = (int)((countWin*100)/(matchNum)+0.5); // (+0.5 四舍五入)
        }
        //大球胜率
        if (totmatchNum == 0) {
            totWinning = 0;
        }else{
            totWinning = (int)(((countTot*100)/(totmatchNum))+0.5);
        }
        //让分球胜率
        if (letmatchNum == 0) {
            letWinnging = 0;
        }else{
            letWinnging = (int)(((countLet*100)/(letmatchNum))+0.5);
        }

        homeWin = (int)countWin + "" + getResources().getText(R.string.football_analyze_win);
        homeLose = (int)countLose + "" + getResources().getText(R.string.football_analyze_lost);
        draw = (int)countDraw + "" + getResources().getText(R.string.football_analyze_equ);

        text1.setText(Html.fromHtml(team + "<font color='#FF1F1F'><b>" + homeWin + "</b></font>" + "<font color='#00aaee'><b>" + draw + "</b></font>" + "<font color='#21B11E'><b>" + homeLose + "</b></font>"
                + getResources().getText(R.string.football_analyze_jin) + "<font color='#FF1F1F'><b>" + (int)countGoal + "</b></font>" + getResources().getText(R.string.football_analyze_shi) + "<font color='#21B11E'><b>"
                + (int)countFumble + "</b></font>" + getResources().getText(R.string.football_analyze_ball)
        ));

        text2.setText(Html.fromHtml(getResources().getText(R.string.football_analyze_winodds) + "<font color='#FF1F1F'><b>" + winning  + "%" + "</b></font>"
                + getResources().getText(R.string.football_analyze_bigball) + "<font color='#FF1F1F'><b>" + totWinning  + "%" + "</b></font>"
                + getResources().getText(R.string.football_analyze_let_points) + "<font color='#FF1F1F'><b>" + letWinnging  + "%" + "</b></font>"
        ));

        progressBar1.setProgress(winning);
        progressBar2.setProgress(totWinning);
        progressBar3.setProgress(letWinnging);

        progressBar1.setCircleProgressColor(getResources().getColor(R.color.football_analyze_progress_color1));
        progressBar2.setCircleProgressColor(getResources().getColor(R.color.football_analyze_progress_color2));
        progressBar3.setCircleProgressColor(getResources().getColor(R.color.football_analyze_progress_color3));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            setResult(Activity.RESULT_OK, null);
            finish();
            overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     * @param mListData
     * @param mAdapter
     * @param type  区分对应位置的数据更新 0 历史交锋  1 主队近期  2 客队近期
     */
    public void updateAdapter(List<FootballAnaylzeHistoryRecent> mListData , FootballAnalyzeAdapter mAdapter , int type) {
        if (mAdapter == null) {
            return;
        }
        if (mListData.size() == 0) {
            switch (type){
                case 0:
                    mNoData1.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    mNoData2.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    mNoData3.setVisibility(View.VISIBLE);
                    break;

            }
        }else{
            switch (type){
                case 0:
                    mNoData1.setVisibility(View.GONE);
                    break;
                case 1:
                    mNoData2.setVisibility(View.GONE);
                    break;
                case 2:
                    mNoData3.setVisibility(View.GONE);
                    break;
            }
        }

        mAdapter.updateDatas(mListData);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.football_analyze_history_screen: //历史交锋 筛选
                setDialog(true);
                break;
            case R.id.football_analyze_recent_screen: //近期战绩 筛选
                setDialog(false);
                break;
            case R.id.football_details_error_btn: //点击刷新
                mErrorLoad.setVisibility(View.GONE);
                mRefresh.setRefreshing(true);
                mHandler.postDelayed(mRun , 0);
                break;
            default:
                break;
        }
    }

    public void setDialog(final boolean type){ //type true : 历史交锋  false：近期战绩
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        LayoutInflater infla = LayoutInflater.from(mContext);
        View view = infla.inflate(R.layout.football_analyze_dialog, null);
        TextView titleView = (TextView) view.findViewById(R.id.screen_titleView);
        titleView.setText(getResources().getText(R.string.basket_analyze_screen));

        final AlertDialog mDialog = mBuilder.create();
        //点击监听
        RadioGroup mRadio = (RadioGroup)view.findViewById(R.id.gendergroup1);
        final RadioButton mAllsiteButon = (RadioButton)view.findViewById(R.id.football_analyze_allsite);
        final RadioButton mSamesiteButon = (RadioButton)view.findViewById(R.id.football_analyze_samesite);

        RadioGroup mRadio2 = (RadioGroup)view.findViewById(R.id.gendergroup2);
        final RadioButton mNumSixButon = (RadioButton)view.findViewById(R.id.football_analyze_num_six);
        final RadioButton mNumTenButon = (RadioButton)view.findViewById(R.id.football_analyze_num_ten);
        final RadioButton mNumFifteenButon = (RadioButton)view.findViewById(R.id.football_analyze_num_fifteen);

        final Button mCancleButon = (Button)view.findViewById(R.id.football_analyze_cancle);
        final Button mConfirmButon = (Button)view.findViewById(R.id.football_analyze_confirm);
        mConfirmButon.setTextColor(getResources().getColor(R.color.tabtitle));

        /**
         * 设置默认选中
         */
        if (type) {
            //场地选中
            if (mHistorySite) {
                mAllsiteButon.setChecked(true);
            }else{
                mSamesiteButon.setChecked(true);
            }
            //场数选中
            if (mHistoryNum == 6) {
                mNumSixButon.setChecked(true);
            }else if(mHistoryNum == 10){
                mNumTenButon.setChecked(true);
            }else{
                mNumFifteenButon.setChecked(true);
            }
        }else{
            //场地
            if (mGuestRecentSite) {
                mAllsiteButon.setChecked(true);
            }else{
                mSamesiteButon.setChecked(true);
            }
            //场数选择
            if (mGuestRecentNum == 6) {
                mNumSixButon.setChecked(true);
            }else if(mGuestRecentNum == 10){
                mNumTenButon.setChecked(true);
            }else{
                mNumFifteenButon.setChecked(true);
            }
        }
        /**
         *场地筛选
         */
        mRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mAllsiteButon.getId()) { // 选择全部
                    if (type) {
                        mHistorySite = true;
                    }else{
                        mGuestRecentSite = true;
                        mHomeRecentSite = true;
                    }

                }else if(checkedId == mSamesiteButon.getId()){
                    if (type) {
                        mHistorySite = false;
                    }else{
                        mGuestRecentSite = false;
                        mHomeRecentSite = false;
                    }
                }
            }
        });

        /**
         *场数筛选
         */
        mRadio2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mNumSixButon.getId()) {
                    if (type) {
                        mHistoryNum = 6;
                    }else{
                        mGuestRecentNum = 6;
                        mHomeRecentNum = 6;
                    }
                }else if(checkedId == mNumTenButon.getId()){
                    if (type) {
                        mHistoryNum = 10;
                    }else{
                        mGuestRecentNum = 10;
                        mHomeRecentNum = 10;
                    }
                }else if(checkedId == mNumFifteenButon.getId()){
                    if (type) {
                        mHistoryNum = 15;
                    }else{
                        mGuestRecentNum = 15;
                        mHomeRecentNum = 15;
                    }
                }
            }
        });

        /**
         *确定
         */
        mCancleButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mConfirmButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHistoryScreenNum = new ArrayList<>();
                setScreen(mHistorySite, mHistoryNum, mHistoryScreenNum, mHistoryData , true);

                mGuestRecentScreenNum = new ArrayList<>();
                setScreen(mGuestRecentSite, mGuestRecentNum, mGuestRecentScreenNum, mRecentData1 , false); // 客队近期战绩相同主客场去 同在客场比赛

                mHomeRecentScreenNum = new ArrayList<>();
                setScreen(mHomeRecentSite, mHomeRecentNum, mHomeRecentScreenNum, mRecentData2, true);

                if (type) {
                    updateAdapter(mHistoryScreenNum, mHistoryAdaptey , 0);
                    setHomeWinLoseData(mHistoryScreenNum, mFootballAnalyzeHistory,mFootballAnalyzeHistoryB, mHomeTeam,mRoundProgressBarHistory1 , mRoundProgressBarHistory2 , mRoundProgressBarHistory3);

                } else {
                    updateAdapter(mGuestRecentScreenNum , mRecentAdapter1 , 2);
                    setHomeWinLoseData(mGuestRecentScreenNum, mFootballAnalyzeRecent1,mFootballAnalyzeRecent1B, mGuestTeam , mRoundProgressBarGuest1 , mRoundProgressBarGuest2 , mRoundProgressBarGuest3);

                    updateAdapter(mHomeRecentScreenNum, mRecentAdapter2 , 1);
                    setHomeWinLoseData(mHomeRecentScreenNum, mFootballAnalyzeRecent2,mFootballAnalyzeRecent2B, mHomeTeam , mRoundProgressBarHome1 , mRoundProgressBarHome2 , mRoundProgressBarHome3);
                }
                mDialog.dismiss();
            }
        });
        mDialog.dismiss();
        mDialog.show();
        mDialog.getWindow().setContentView(view);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(false);
                initData();
            }
        },500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
