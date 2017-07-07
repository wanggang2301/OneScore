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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.BasketAnalyzeAdapter;
import com.hhly.mlottery.adapter.basketball.BasketAnalyzeFutureAdapter;
import com.hhly.mlottery.bean.basket.basketdetails.BasketAnalyzeMoreBean;
import com.hhly.mlottery.bean.basket.basketdetails.BasketAnalyzeMoreFutureBean;
import com.hhly.mlottery.bean.basket.basketdetails.BasketAnalyzeMoreRecentHistoryBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;
import com.hhly.mlottery.widget.NestedListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: BasketAnalyzeMoreRecordActivity
 * @Description: 篮球分析详情Activity  双方战绩
 * @author yixq
 */
public class BasketAnalyzeMoreRecordActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public final static String BASKET_ANALYZE_THIRD_ID = "analyzeId";
    public final static String BASKET_ANALYZE_GUEST_TEAM = "guestTeam";
    public final static String BASKET_ANALYZE_HOME_TEAM = "homeTeam";
    private ImageButton mBack;
    private String mThirdId;
    private NestedListView mHistoryListView;//历史交锋 listview
    private NestedListView mRecentListView1;//近期战绩 1 listview
    private NestedListView mRecentListView2;//近期战绩 2 listview
    private NestedListView mFutureListView1;//未来比赛 2 listview
    private NestedListView mFutureListView2;//未来比赛 2 listview

    private BasketAnalyzeAdapter mHistoryAdaptey; //历史交锋 adapter
    private BasketAnalyzeAdapter mRecentAdapter1; //近期战绩 1 adapter
    private BasketAnalyzeAdapter mRecentAdapter2; //近期战绩 2 adapter
    private BasketAnalyzeFutureAdapter mFutureAdapter1; //未来比赛 1 adapter
    private BasketAnalyzeFutureAdapter mFutureAdapter2; //未来比赛 2 adapter
    private TextView mBasketAnalyzeHistory;
    private TextView mBasketAnalyzeRecent1;
    private TextView mBasketAnalyzeRecent2;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_analyze_details);
        if(getIntent().getExtras() != null){
            mThirdId = getIntent().getExtras().getString(BASKET_ANALYZE_THIRD_ID);
//            L.d("BASKET_ANALYZE_THIRD_ID : ", mThirdId + "");
        }

        initView();
//        initData();
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
        mBack = (ImageButton)findViewById(R.id.basket_analyze_img_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("resultType", resultstring);
                MobclickAgent.onEvent(mContext, "BasketAnalyzeMoreRecordActivity_Exit");
//                Toast.makeText(BasketAnalyzeMoreRecordActivity.this, "back", Toast.LENGTH_SHORT).show();

                setResult(Activity.RESULT_OK, null);
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
            }
        });

        mHistoryListView = (NestedListView)findViewById(R.id.basket_histroy_list);
        mRecentListView2 = (NestedListView)findViewById(R.id.basket_recent_exploits_list_to);
        mRecentListView1 = (NestedListView)findViewById(R.id.basket_recent_exploits_list);
        mFutureListView1 = (NestedListView)findViewById(R.id.basket_future_exploits_list);
        mFutureListView2 = (NestedListView)findViewById(R.id.basket_future_exploits_list_to);

        mBasketAnalyzeHistory = (TextView)findViewById(R.id.basket_analyze_history);
        mBasketAnalyzeRecent1 = (TextView)findViewById(R.id.basket_analyze_record_guest);
        mBasketAnalyzeRecent2 = (TextView)findViewById(R.id.basket_analyze_record_home);

        mHistoryScreen = (ImageView)findViewById(R.id.basket_analyze_history_screen);
        mHistoryScreen.setOnClickListener(this);
        mRecentScreen = (ImageView)findViewById(R.id.basket_analyze_recent_screen);
        mRecentScreen.setOnClickListener(this);

        mHistory_ll = (LinearLayout)findViewById(R.id.basket_analyze_details_history);
        mGuestRecent_ll = (LinearLayout)findViewById(R.id.basket_analyze_details_recent_guest);
        mHomeRecent_ll = (LinearLayout)findViewById(R.id.basket_analyze_details_recent_home);
        mGuestFuture_ll = (LinearLayout)findViewById(R.id.basket_analyze_details_future_guest);
        mHomeFuture_ll = (LinearLayout)findViewById(R.id.basket_analyze_details_future_home);

        mErrorLoad = (LinearLayout)findViewById(R.id.basketball_details_error);
        mErrorLoad.setVisibility(View.GONE);

        mSuccessLoad = (LinearLayout)findViewById(R.id.basket_loading_success_ll);
        mSuccessLoad.setVisibility(View.GONE);

        mGuestFruture = (TextView) findViewById(R.id.basket_guest_fruture_tv_list);
        mHomeFruture = (TextView) findViewById(R.id.basket_home_fruture_tv_list);
        mError = (TextView)findViewById(R.id.basketball_details_error_btn);
        mError.setOnClickListener(this);

        mRefresh = (ExactSwipeRefreshLayout)findViewById(R.id.basket_analyze_details_refreshlayout);
        mRefresh.setColorSchemeResources(R.color.tabhost);
        mRefresh.setOnRefreshListener(BasketAnalyzeMoreRecordActivity.this);
        mRefresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(this, StaticValues.REFRASH_OFFSET_END));
        mRefresh.setRefreshing(true);

        //暂无数据
        mNodataTextview = (TextView) findViewById(R.id.basket_analyze_details_nodata);

        mNoData1 = (TextView) findViewById(R.id.basket_analyze_nodata1);
        mNoData2 = (TextView) findViewById(R.id.basket_analyze_nodata2);
        mNoData3 = (TextView) findViewById(R.id.basket_analyze_nodata3);
        mNoData4 = (TextView) findViewById(R.id.basket_analyze_nodata4);
        mNoData5 = (TextView) findViewById(R.id.basket_analyze_nodata5);
    }

    private List<BasketAnalyzeMoreRecentHistoryBean> mHistoryData = new ArrayList<>();//历史交锋所有比赛
    private List<BasketAnalyzeMoreRecentHistoryBean> mRecentData1 = new ArrayList<>();//客队所有近期战绩
    private List<BasketAnalyzeMoreRecentHistoryBean> mRecentData2 = new ArrayList<>();//主队所有近期战绩
    private List<BasketAnalyzeMoreFutureBean> mFutureData1 = new ArrayList<>();//客队未来赛事
    private List<BasketAnalyzeMoreFutureBean> mFutureData2 = new ArrayList<>();//主队所有赛事
    private String mGuestTeam;//客队名
    private String mHomeTeam;//主队名

    private List<BasketAnalyzeMoreRecentHistoryBean> mHistoryScreenNum; //筛选后的历史交锋
    private List<BasketAnalyzeMoreRecentHistoryBean> mGuestRecentScreenNum; //筛选后客队的近期战绩
    private List<BasketAnalyzeMoreRecentHistoryBean> mHomeRecentScreenNum; //筛选后主队的近期战绩

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
//            params.put("thirdId", "228110");
        }

//        String url = "http://192.168.10.242:8181/mlottery/core/basketballDetail.findAnalysisDetail.do";  //?thirdId=228110&lang=zh
        String url = BaseURLs.URL_BASKET_ANALYZE_DETAILS;

        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<BasketAnalyzeMoreBean>() {
            @Override
            public void onResponse(BasketAnalyzeMoreBean json) {
                if (json == null) {
                    mSuccessLoad.setVisibility(View.GONE);
                    mErrorLoad.setVisibility(View.GONE);
                    mNodataTextview.setVisibility(View.VISIBLE);
                } else {

                    if (json.getHomeFuture() == null && json.getGuestFuture() == null && json.getHistory() == null &&
                            json.getHomeRecent() == null && json.getGuestRecent() == null && json.getHomeTeam() == null && json.getGuestTeam() == null) {
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
                        if (json.getHistory() != null) {
                            mHistoryData = json.getHistory();
                            //暂无数据提示
                            if (mHistoryData.isEmpty()) {
                                mNoData1.setVisibility(View.VISIBLE);
                            }
                            //取前六场
                            List<BasketAnalyzeMoreRecentHistoryBean> list = new ArrayList<>();
                            if (mHistoryData.size() <= 6) {
                                list = mHistoryData;
                            } else {
                                for (int i = 0; i < 6; i++) {
                                    list.add(mHistoryData.get(i));
                                }
                            }

                            List<BasketAnalyzeMoreRecentHistoryBean> fistData = new ArrayList<>();
                            setScreen(true, 6, fistData, mHistoryData , true);

                            if (mHistoryAdaptey == null) {
                                mHistoryAdaptey = new BasketAnalyzeAdapter(mContext, fistData, R.layout.basket_analyze_details_item);
                                mHistoryListView.setAdapter(mHistoryAdaptey);
                                setHomeWinLoseData(list, mBasketAnalyzeHistory, mHomeTeam);
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
                        }
                        /**
                         * 客队近期战绩
                         */
                        if (json.getGuestRecent() != null) {
                            mRecentData1 = json.getGuestRecent();

                            //暂无数据提示
                            if (mRecentData1.isEmpty()) {
                                mNoData3.setVisibility(View.VISIBLE);
                            }
                            //默认选中全部场地6场
                            List<BasketAnalyzeMoreRecentHistoryBean> fistData = new ArrayList<>();
                            setScreen(true, 6, fistData, mRecentData1 , true);

                            //取前六场
                            List<BasketAnalyzeMoreRecentHistoryBean> list = new ArrayList<>();
                            if (mRecentData1.size() <= 6) {
                                list = mRecentData1;
                            } else {
                                for (int i = 0; i < 6; i++) {
                                    list.add(mRecentData1.get(i));
                                }
                            }

                            if (mRecentAdapter1 == null) {
                                mRecentAdapter1 = new BasketAnalyzeAdapter(mContext, fistData, R.layout.basket_analyze_details_item);
                                mRecentListView1.setAdapter(mRecentAdapter1);
                                setHomeWinLoseData(list, mBasketAnalyzeRecent1, mGuestTeam);
                            } else {
                                if (mGuestRecentScreenNum != null) {
                                    updateAdapter(mGuestRecentScreenNum, mRecentAdapter1, 2);
                                } else {
                                    updateAdapter(fistData, mRecentAdapter1, 2);
                                }
                            }
//                    mNoData2.setVisibility(View.GONE);
                            mGuestRecent_ll.setVisibility(View.VISIBLE);
                        } else {
//                    mNoData2.setVisibility(View.VISIBLE);
                            mGuestRecent_ll.setVisibility(View.GONE);
                        }
                        /**
                         * 主队近期战绩
                         */
                        if (json.getHomeRecent() != null) {
                            mRecentData2 = json.getHomeRecent();

                            //暂无数据提示
                            if (mRecentData2.isEmpty()) {
                                mNoData2.setVisibility(View.VISIBLE);
                            }

                            List<BasketAnalyzeMoreRecentHistoryBean> fistData = new ArrayList<>();
                            setScreen(true, 6, fistData, mRecentData2 , true);

                            //取前六场
                            List<BasketAnalyzeMoreRecentHistoryBean> list = new ArrayList<>();
                            if (mRecentData2.size() <= 6) {
                                list = mRecentData2;
                            } else {
                                for (int i = 0; i < 6; i++) {
                                    list.add(mRecentData2.get(i));
                                }
                            }

                            if (mRecentAdapter2 == null) {
                                mRecentAdapter2 = new BasketAnalyzeAdapter(mContext, fistData, R.layout.basket_analyze_details_item);
                                mRecentListView2.setAdapter(mRecentAdapter2);
                                setHomeWinLoseData(list, mBasketAnalyzeRecent2, mHomeTeam);
                            } else {
                                if (mHomeRecentScreenNum != null) {
                                    updateAdapter(mHomeRecentScreenNum, mRecentAdapter2, 1);
                                } else {
                                    updateAdapter(fistData, mRecentAdapter2, 1);
                                }
                            }
//                    mNoData3.setVisibility(View.GONE);
                            mHomeRecent_ll.setVisibility(View.VISIBLE);
                        } else {
//                    mNoData3.setVisibility(View.VISIBLE);
                            mHomeRecent_ll.setVisibility(View.GONE);
                        }

                        /**
                         * 客队未来比赛
                         */
                        if (json.getGuestFuture() != null) {
                            mFutureData1 = json.getGuestFuture();
                            //暂无数据提示
                            if (mFutureData1.isEmpty()) {
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
//                            mGuestFruture.setText(getResources().getText(R.string.basket_analyze_defult_text) + "" + getResources().getText(R.string.basket_analyze_future) + mFutureData1.size() + getResources().getText(R.string.basket_analyze_field));
                                mGuestFruture.setText(getResources().getText(R.string.basket_analyze_defult_text) + "" + getResources().getText(R.string.basket_analyze_fruture));
                            }

                            mFutureAdapter1 = new BasketAnalyzeFutureAdapter(mContext, mFutureData1, R.layout.basket_analyze_item);
                            mFutureListView1.setAdapter(mFutureAdapter1);
                            mGuestFuture_ll.setVisibility(View.VISIBLE);   //客队未来三场
                        } else {
                            mGuestFuture_ll.setVisibility(View.GONE);
                        }

                        /**
                         * 主队未来比赛
                         */
                        if (json.getHomeFuture() != null) {
                            mFutureData2 = json.getHomeFuture();

                            //暂无数据提示
                            if (mFutureData2.isEmpty()) {
                                mNoData5.setVisibility(View.VISIBLE);
                            }

                            if (json.getHomeTeam() != null) {
//                            mHomeFruture.setText(json.getHomeTeam() + getResources().getText(R.string.basket_analyze_future) + mFutureData2.size() + getResources().getText(R.string.basket_analyze_field));
                                mHomeFruture.setText(json.getHomeTeam() + getResources().getText(R.string.basket_analyze_fruture));
                            } else {
//                            mHomeFruture.setText(getResources().getText(R.string.basket_analyze_defult_text) +""+ getResources().getText(R.string.basket_analyze_future) + mFutureData2.size() + getResources().getText(R.string.basket_analyze_field));
                                mHomeFruture.setText(getResources().getText(R.string.basket_analyze_defult_text) + "" + getResources().getText(R.string.basket_analyze_fruture));
                            }

                            mFutureAdapter2 = new BasketAnalyzeFutureAdapter(mContext, mFutureData2, R.layout.basket_analyze_item);
                            mFutureListView2.setAdapter(mFutureAdapter2);
//                    mNoData4.setVisibility(View.GONE);
                            mHomeFuture_ll.setVisibility(View.VISIBLE);
                        } else {
//                    mNoData4.setVisibility(View.VISIBLE);
                            mHomeFuture_ll.setVisibility(View.GONE);
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
//                Toast.makeText(BasketAnalyzeMoreRecordActivity.this, "Error2", Toast.LENGTH_SHORT).show();

                mErrorLoad.setVisibility(View.VISIBLE);
                mSuccessLoad.setVisibility(View.GONE);
                mNodataTextview.setVisibility(View.GONE);

                mRefresh.setRefreshing(false);
            }
        }, BasketAnalyzeMoreBean.class);
    }

    /**
     * 筛选设置
     * @param isSite
     * @param num
     * @param screenData
     * @param allData
     */
    private void setScreen(boolean isSite , int num , List<BasketAnalyzeMoreRecentHistoryBean> screenData , List<BasketAnalyzeMoreRecentHistoryBean> allData , boolean isGuest){

        /**
         *  筛选相同主客场比赛
         */
        List<BasketAnalyzeMoreRecentHistoryBean> mHistoryScreenSite = new ArrayList<>() ;

        if (isSite) {
            mHistoryScreenSite = allData;
        }else {
            for (BasketAnalyzeMoreRecentHistoryBean history : allData) {
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
                for (BasketAnalyzeMoreRecentHistoryBean history : mHistoryScreenSite) {
                    screenData.add(history);
                }
            }else{
                for (int i = 0; i < 6; i++) {
                    screenData.add(mHistoryScreenSite.get(i));
                }
            }
        }else if (num == 10) {
            if (mHistoryScreenSite.size() <= 10) {
                for (BasketAnalyzeMoreRecentHistoryBean history : mHistoryScreenSite) {
                    screenData.add(history);
                }
            }else{
                for (int i = 0; i < 10; i++) {
                    screenData.add(mHistoryScreenSite.get(i));
                }
            }
        }else if (num == 15) {
            if (mHistoryScreenSite.size() <= 15) {
                for (BasketAnalyzeMoreRecentHistoryBean history : mHistoryScreenSite) {
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
     * @param mText
     * @param mTeam
     */
    private void setHomeWinLoseData(List<BasketAnalyzeMoreRecentHistoryBean> mData , TextView mText ,String mTeam){

        String homeWin , homeLose , homeCourtWin , homeCourtLose;

        //所显示的总场数
        String matchNum;

        int count1 = 0 ;
        int count2 = 0 ;
        int count3 = 0 ;
        int count4 = 0 ;

        if (mData.isEmpty() || mData.size()==0) {
            matchNum = "0";
        }else{
            matchNum = mData.size()+"";
            for (BasketAnalyzeMoreRecentHistoryBean history : mData) {
                if (history.getResult() == 1) {
                    count1++;
                }else if (history.getResult() == 0){
                    count2++;
                }
                if (history.isHomeGround()) {
                    if (history.getResult() == 1) {
                        count3++;
                    }else if (history.getResult() == 0){
                        count4++;
                    }
                }
            }
        }

        homeWin = count1 + "" + getResources().getText(R.string.basket_analyze_win);
        homeLose = count2 + "" + getResources().getText(R.string.basket_analyze_lost);
        homeCourtWin = count3 + "" + getResources().getText(R.string.basket_analyze_win);
        homeCourtLose = count4 + "" + getResources().getText(R.string.basket_analyze_lost);

        //"<font color='#ff0000'><b>" + win + getActivity().getString(R.string.analyze_win) + "、 " + "</b></font> "   #FF1F1F  #21B11E

        mText.setText(Html.fromHtml(getResources().getText(R.string.basket_analyze_recently) + matchNum + getResources().getText(R.string.basket_analyze_recently2) + mTeam + "<font color='#666666'><b>" + homeWin + "</b></font>" + "<font color='#21B11E'><b>" + homeLose + "</b></font>"
                + getResources().getText(R.string.basket_analyze_home_field) + "<font color='#FF1F1F'><b>" + homeCourtWin + "</b></font>" + "<font color='#21B11E'><b>" + homeCourtLose + "</b></font>"));
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
    public void updateAdapter(List<BasketAnalyzeMoreRecentHistoryBean> mListData , BasketAnalyzeAdapter mAdapter , int type) {
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
            case R.id.basket_analyze_history_screen: //历史交锋 筛选
                MobclickAgent.onEvent(mContext, "BasketAnalyzeMoreRecordActivity_HistoryScreen");
                setDialog(true);
                break;
            case R.id.basket_analyze_recent_screen: //近期战绩 筛选
                MobclickAgent.onEvent(mContext, "BasketAnalyzeMoreRecordActivity_RecentScreen");
                setDialog(false);
                break;
            case R.id.basketball_details_error_btn: //点击刷新
                MobclickAgent.onEvent(mContext, "BasketAnalyzeMoreRecordActivity_Refresh");
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
        View view = infla.inflate(R.layout.basket_analyze_dialog, null);
        TextView titleView = (TextView) view.findViewById(R.id.screen_titleView);
//        titleView.setText("筛选");
        titleView.setText(getResources().getText(R.string.basket_analyze_screen));

        final AlertDialog mDialog = mBuilder.create();
        //点击监听
        RadioGroup mRadio = (RadioGroup)view.findViewById(R.id.gendergroup1);
        final RadioButton mAllsiteButon = (RadioButton)view.findViewById(R.id.basket_analyze_allsite);
        final RadioButton mSamesiteButon = (RadioButton)view.findViewById(R.id.basket_analyze_samesite);

        RadioGroup mRadio2 = (RadioGroup)view.findViewById(R.id.gendergroup2);
        final RadioButton mNumSixButon = (RadioButton)view.findViewById(R.id.basket_analyze_num_six);
        final RadioButton mNumTenButon = (RadioButton)view.findViewById(R.id.basket_analyze_num_ten);
        final RadioButton mNumFifteenButon = (RadioButton)view.findViewById(R.id.basket_analyze_num_fifteen);

        final Button mCancleButon = (Button)view.findViewById(R.id.basket_analyze_cancle);
        final Button mConfirmButon = (Button)view.findViewById(R.id.basket_analyze_confirm);
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
                setScreen(mHomeRecentSite, mHomeRecentNum, mHomeRecentScreenNum, mRecentData2 , true);

                if (type) {
                    updateAdapter(mHistoryScreenNum, mHistoryAdaptey , 0);
                    setHomeWinLoseData(mHistoryScreenNum, mBasketAnalyzeHistory, mHomeTeam);

                } else {
                    updateAdapter(mGuestRecentScreenNum , mRecentAdapter1 , 2);
                    setHomeWinLoseData(mGuestRecentScreenNum, mBasketAnalyzeRecent1, mGuestTeam);

                    updateAdapter(mHomeRecentScreenNum, mRecentAdapter2 , 1);
                    setHomeWinLoseData(mHomeRecentScreenNum, mBasketAnalyzeRecent2, mHomeTeam);
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
