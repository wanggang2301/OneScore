package com.hhly.mlottery.frame.footballframe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.DatabaseStaticBean;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.DatabaseTopBean;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.FootballDatabaseStatisticBean;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.TopDetailsBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.RoundProgressBar;
import com.hhly.mlottery.view.TextProgressBar;
import com.hhly.mlottery.widget.NoScrollListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:
 * author: yixq
 * Created by A on 2016/10/10.
 * 足球资料库统计
 */
public class FootballDatabaseStatisticsFragment extends Fragment implements View.OnClickListener {

    private static final String PARAM_ID = "leagueId";
    private static final String PARAM2_LEAGUEDATA = "leagueDate";
    private static final String PARAM_TYPE = "type";

    private View mView;
    private NoScrollListView mListView1;
    private NoScrollListView mListView2;
    private NoScrollListView mListView3;
    private NoScrollListView mListView4;
    private MostAdapter mAdapter0;
    private MostAdapter mAdapter1;
    private MostAdapter mAdapter2;
    private MostAdapter mAdapter3;
    private MostAdapter mAdapter4;
    private LinearLayout mStatisticLinear;
    private LinearLayout mMostLinear;
    private RadioGroup mRadioGroup;
    private RadioButton mStatisticButton;
    private RadioButton mMostButton;
    private FrameLayout mLoading;
    private LinearLayout mLoadRefresh;
    private TextView mNodata;
    private LinearLayout mData;

    private final static int VIEW_STATUS_LOADING = 1; //加载中
    private final static int VIEW_STATUS_SUCCESS = 2; // 加载成功
    private final static int VIEW_STATUS_NET_ERROR = 3; // 请求失败
    private final static int VIEW_STATUS_NET_NODATA = 4; // 暂无数据
    Handler mHandlerData = new Handler();
    private TextView mRefresh;

    private String mLeagueData; // 赛季
    private String mLeagueId; // 联赛ID
    private String mType;

    private boolean isUpdata = true;//[true:统计  false:之最 ]

    private RoundProgressBar mWinLostProgress;
    private TextView mWinLostText;
    private TextView mWinLostHome;
    private TextView mWinLostdraw;
    private TextView mWinLostGuest;
    private TextView mTotalScoreText;
    private TextView mGpScoreText;
    private TextView mAverageScoreText;
    private TextProgressBar mAvgProgress;
    private TextView mAvgText1;
    private TextView mNumText1;
    private ProgressBar mBigSmallProgress1;
    private TextView mAvgText2;
    private TextView mNumText2;
    private ProgressBar mBigSmallProgress2;
    private TextView mAvgText3;
    private TextView mNumText3;
    private ProgressBar mBigSmallProgress3;
    private TextView mAvgText4;
    private TextView mNumText4;
    private ProgressBar mBigSmallProgress4;
    private NoScrollListView mListView0;

    public static FootballDatabaseStatisticsFragment newInstance(String leagueId ,String type , String leagueData) {
        FootballDatabaseStatisticsFragment fragment = new FootballDatabaseStatisticsFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_ID, leagueId);
        args.putString(PARAM2_LEAGUEDATA, leagueData);
        args.putString(PARAM_TYPE , type);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLeagueData = getArguments().getString(PARAM2_LEAGUEDATA);
        mLeagueId = getArguments().getString(PARAM_ID);
        mType = getArguments().getString(PARAM_TYPE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.football_database_details_statistics , container , false);

        initView();
        mHandlerData.postDelayed(mRun , 500);

        return mView;
    }

    public void upData(){
        mHandlerData.postDelayed(mRun , 500);
    }

    public void setLeagueData(String leagueData){
        this.mLeagueData = leagueData;
    }

    private void initView(){
        //胜平负progress
        mWinLostProgress = (RoundProgressBar) mView.findViewById(R.id.progress_statistic_win_lost);
        mWinLostProgress.setIsprogress(true);
        mWinLostProgress.setTextIsDisplayable(false);
        mWinLostProgress.setCircleColor(getResources().getColor(R.color.unselected_tab_color));
        mWinLostProgress.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_staristic_width));
        //胜平负场次
        mWinLostText = (TextView) mView.findViewById(R.id.football_database_cc);
        //胜平负主胜
        mWinLostHome = (TextView) mView.findViewById(R.id.football_database_win_lost_home);
        //胜平负平局
        mWinLostdraw = (TextView) mView.findViewById(R.id.football_database_win_lost_draw);
        //胜平负客胜
        mWinLostGuest = (TextView) mView.findViewById(R.id.football_database_win_lost_guest);

        //进球统计-总得分
        mTotalScoreText = (TextView) mView.findViewById(R.id.football_database_total);
        //进球统计-已赛场次
        mGpScoreText = (TextView) mView.findViewById(R.id.football_database_gp);
        //进球统计-平均得分
        mAverageScoreText = (TextView) mView.findViewById(R.id.football_database_average);
        //进球统计进度
        mAvgProgress = (TextProgressBar)mView.findViewById(R.id.avg_progressbar);

        //大小球统计
        mAvgText1 = (TextView)mView.findViewById(R.id.big_small_statistic_avg1);
        mNumText1 = (TextView)mView.findViewById(R.id.big_small_statistic_num1);
        mBigSmallProgress1 = (ProgressBar)mView.findViewById(R.id.big_small_statistic_progressbar1);

        mAvgText2 = (TextView)mView.findViewById(R.id.big_small_statistic_avg2);
        mNumText2 = (TextView)mView.findViewById(R.id.big_small_statistic_num2);
        mBigSmallProgress2 = (ProgressBar)mView.findViewById(R.id.big_small_statistic_progressbar2);

        mAvgText3 = (TextView)mView.findViewById(R.id.big_small_statistic_avg3);
        mNumText3 = (TextView)mView.findViewById(R.id.big_small_statistic_num3);
        mBigSmallProgress3 = (ProgressBar)mView.findViewById(R.id.big_small_statistic_progressbar3);

        mAvgText4 = (TextView)mView.findViewById(R.id.big_small_statistic_avg4);
        mNumText4 = (TextView)mView.findViewById(R.id.big_small_statistic_num4);
        mBigSmallProgress4 = (ProgressBar)mView.findViewById(R.id.big_small_statistic_progressbar4);

        /**-----------------------------------------------------------------------------------------------------*/

        mListView0 = (NoScrollListView)mView.findViewById(R.id.football_database_leagueMost_list0);
        mListView1 = (NoScrollListView)mView.findViewById(R.id.football_database_leagueMost_list1);
        mListView2 = (NoScrollListView)mView.findViewById(R.id.football_database_leagueMost_list2);
        mListView3 = (NoScrollListView)mView.findViewById(R.id.football_database_leagueMost_list3);
        mListView4 = (NoScrollListView)mView.findViewById(R.id.football_database_leagueMost_list4);

        mListView0.setFocusable(false);
        mListView1.setFocusable(false);
        mListView2.setFocusable(false);
        mListView3.setFocusable(false);
        mListView4.setFocusable(false);



        /** +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
        //统计
        mStatisticLinear = (LinearLayout)mView.findViewById(R.id.football_database_statistic_ll);
        //之最
        mMostLinear = (LinearLayout) mView.findViewById(R.id.football_database_most_ll);

        mRadioGroup = (RadioGroup)mView.findViewById(R.id.head_radiogroup);
        mStatisticButton = (RadioButton)mView.findViewById(R.id.football_database_details_statistics);
        mMostButton = (RadioButton)mView.findViewById(R.id.football_database_details_most);

        //加载成功
        mData = (LinearLayout)mView.findViewById(R.id.football_database_details_data);
        mData.setVisibility(View.GONE);
        //加载中...
        mLoading = (FrameLayout) mView.findViewById(R.id.football_database_loading_details);
        //网络异常
        mLoadRefresh = (LinearLayout) mView.findViewById(R.id.football_database_details_refresh);
        //暂无数据
        mNodata = (TextView) mView.findViewById(R.id.football_database_details_nodata);

        mRefresh = (TextView)mView.findViewById(R.id.reLoadin);
        mRefresh.setOnClickListener(this);
        setRadioOnClick();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case VIEW_STATUS_LOADING:
                    mLoading.setVisibility(View.VISIBLE);
                    mData.setVisibility(View.GONE);
                    mLoadRefresh.setVisibility(View.GONE);
                    mNodata.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_SUCCESS:
                    mLoading.setVisibility(View.GONE);
                    mData.setVisibility(View.VISIBLE);
                    mLoadRefresh.setVisibility(View.GONE);
                    mNodata.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_NET_ERROR:
                    mLoading.setVisibility(View.GONE);
                    mData.setVisibility(View.GONE);
                    mLoadRefresh.setVisibility(View.VISIBLE);
                    mNodata.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_NET_NODATA:
                    mLoading.setVisibility(View.GONE);
                    mData.setVisibility(View.GONE);
                    mLoadRefresh.setVisibility(View.GONE);
                    mNodata.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            initData();
        }
    };

    private void initData(){

        mHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
//        String url = "http://192.168.33.32:8080/mlottery/core/androidLeagueData.findAndroidStatics.do" ;
        String url = BaseURLs.URL_FOOTBALL_DATABASE_STATISTIC_DETAILS;
        Map<String , String> params = new HashMap<>();
        if (!mLeagueData.equals("-1")) {
            params.put(PARAM2_LEAGUEDATA , mLeagueData);
        }
        params.put(PARAM_ID , mLeagueId);
        params.put(PARAM_TYPE , mType);
        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<FootballDatabaseStatisticBean>() {
            @Override
            public void onResponse(FootballDatabaseStatisticBean bean) {
                if (bean == null || (bean.getStatics() == null && bean.getTop() == null)) {
                    mHandler.sendEmptyMessage(VIEW_STATUS_NET_NODATA);
                    return;
                }
                //统计
                DatabaseStaticBean data = bean.getStatics();
                if (data != null) {
                    setData(data);
                }

                DatabaseTopBean data2 = bean.getTop();

                //之最
                mAdapter0 = new MostAdapter(getContext(),data2.getWinTop(),R.layout.football_database_most_item , true , false);//胜平负最多
                mAdapter1 = new MostAdapter(getContext() , data2.getAtkTop(), R.layout.football_database_most_item , false , false);//攻击力最强
                mAdapter2 = new MostAdapter(getContext() , data2.getAtkWeak(), R.layout.football_database_most_item ,false , false);//攻击力最弱
                mAdapter3 = new MostAdapter(getContext() , data2.getDefTop(), R.layout.football_database_most_item , false , true);//防守最强
                mAdapter4 = new MostAdapter(getContext() , data2.getDefWeak(), R.layout.football_database_most_item , false , true);//防守最弱

                mListView0.setAdapter(mAdapter0);
                mListView1.setAdapter(mAdapter1);
                mListView2.setAdapter(mAdapter2);
                mListView3.setAdapter(mAdapter3);
                mListView4.setAdapter(mAdapter4);

                mHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
                if (isUpdata) {
                    setSelect(true);
                }else{
                    setSelect(false);
                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
            }
        }, FootballDatabaseStatisticBean.class);
    }

    private void setData(DatabaseStaticBean data){

        if (data == null || getActivity() == null) {
            return;
        }
        /**
         * 胜平负统计
         */
        String homewin = data.getHomeWinPer();
        String equal = data.getEqualPer();
        String awaywin = data.getAwayWinPer();

        double homePre = 0;
        double equalPre = 0;
        double awayPre = 0;

        if (homewin != null) {
           String toDouble = homewin.trim();
           homePre = Double.parseDouble(toDouble.substring(0,toDouble.length()-1));
        }
        if (equal != null) {
            String toDouble = equal.trim();
            equalPre = Double.parseDouble(toDouble.substring(0,toDouble.length()-1));
        }
        if (awaywin != null) {
            String toDouble = awaywin.trim();
            awayPre = Double.parseDouble(toDouble.substring(0,toDouble.length()-1));
//            awayPre = 100.00-homePre-equalPre;
        }
        if(awayPre == 0){
            mWinLostProgress.setProgressAll((int)homePre, (int)equalPre , (int)awayPre);
        }else{
            mWinLostProgress.setProgressAll((int)homePre, (int)equalPre , (int)awayPre+2); // 最后一条进度 +2 为了处理总进度值不足时出现间隙情况（父progressbar方法中的+2处理）
        }
        mWinLostProgress.setCircleProgressColor(getResources().getColor(R.color.basket_database_statistics_background_h));
        mWinLostProgress.setCircleProgressColor2(getResources().getColor(R.color.basket_database_statistics_progress));
        mWinLostProgress.setCircleProgressColor3(getResources().getColor(R.color.basket_database_statistics_background_g));
        mWinLostProgress.setDatas(data.getHomeWinCount() + "" ,data.getEqualCount() + "" , data.getAwayWinCount() + "");
        mWinLostText.setText(data.getMatchCount()+ "");
        mWinLostHome.setText(homePre + "%");
        mWinLostdraw.setText(equalPre + "%");
        mWinLostGuest.setText(awayPre + "%");

        /**
         * 进球统计
         */
        mTotalScoreText.setText(data.getGoalCount() + "" );
        mGpScoreText.setText(data.getMatchCount() + "");
        mAverageScoreText.setText(data.getGoalAvg() + "");

        double homeAvg = data.getHomeAvg();
        double awayAvg = data.getAwayAvg();

        double honeProgress = ((homeAvg*100) / ((homeAvg+awayAvg)*100))*100;
        mAvgProgress.setText(homeAvg+"" , awayAvg+"");
        mAvgProgress.setProgress((int)honeProgress);

        /**
         * 大小球统计
         */

        String MoreZeroPer = data.getMoreZeroPer();
        String MoreOnePer = data.getMoreOnePer();
        String MoreTwoPer = data.getMoreTwoPer();
        String MoreThreePer = data.getMoreThreePer();

        double zeroPer;
        double onePer;
        double twoPer;
        double threePer;

        mAvgText1.setText(data.getMoreZeroPer());
        mNumText1.setText("[" + data.getMoreZero() + getResources().getString(R.string.football_database_details_chang)+ "]");
        if (MoreZeroPer != null) {
            String toInt = MoreZeroPer.trim();
            zeroPer = Double.parseDouble(toInt.substring(0 , toInt.length()-1));
            mBigSmallProgress1.setProgress((int)zeroPer);
        }else{
            mBigSmallProgress1.setProgress(0);
        }

        mAvgText2.setText(data.getMoreOnePer());
        mNumText2.setText("[" + data.getMoreOne() + getResources().getString(R.string.football_database_details_chang)+ "]");
        if (MoreOnePer != null) {
            String toInt = MoreOnePer.trim();
            onePer = Double.parseDouble(toInt.substring(0 , toInt.length()-1));
            mBigSmallProgress2.setProgress((int)onePer);
        }else{
            mBigSmallProgress2.setProgress(0);
        }

        mAvgText3.setText(data.getMoreTwoPer());
        mNumText3.setText("[" + data.getMoreTwo() + getResources().getString(R.string.football_database_details_chang)+ "]");
        if (MoreTwoPer != null) {
            String toInt = MoreTwoPer.trim();
            twoPer = Double.parseDouble(toInt.substring(0,toInt.length()-1));
            mBigSmallProgress3.setProgress((int)twoPer);
        }else{
            mBigSmallProgress3.setProgress(0);
        }

        mAvgText4.setText(data.getMoreThreePer());
        mNumText4.setText("[" + data.getMoreThree() + getResources().getString(R.string.football_database_details_chang)+ "]");
        if (MoreThreePer != null) {
            String toInt = MoreThreePer.trim();
            threePer = Double.parseDouble(toInt.substring(0 , toInt.length()-1));
            mBigSmallProgress4.setProgress((int)threePer);
        }else{
            mBigSmallProgress4.setProgress(0);
        }
    }

    private void setSelect(boolean statistic){
        if (statistic) {
            mStatisticLinear.setVisibility(View.VISIBLE);
            mMostLinear.setVisibility(View.GONE);
        }else{
            mStatisticLinear.setVisibility(View.GONE);
            mMostLinear.setVisibility(View.VISIBLE);
        }
    }

    private void setRadioOnClick(){
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mStatisticButton.getId()) {

                    setSelect(true);
                    isUpdata = true;

                }else if(checkedId == mMostButton.getId()){

                    setSelect(false);
                    isUpdata = false;

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reLoadin:
                mHandlerData.postDelayed(mRun , 500);
                break;
        }
    }

    class MostAdapter extends CommonAdapter<TopDetailsBean>{

        boolean isSPF;//是否胜平负最多
        boolean isDEFENS;//是否是防守信息

        public MostAdapter(Context context, List<TopDetailsBean> datas, int layoutId , boolean isSPFMost , boolean isDefens) {
            super(context, datas, layoutId);

            this.mContext = context;
            this.isSPF = isSPFMost;
            this.isDEFENS = isDefens;
        }
        @Override
        public void convert(ViewHolder holder, TopDetailsBean basketDatabaseMostDat) {

            if (basketDatabaseMostDat == null) {
                return;
            }

            if (basketDatabaseMostDat.getTeamName() == null || basketDatabaseMostDat.getTeamName().equals("")) {
                holder.setText(R.id.football_database_most_item_name , "--");
            }else{
                holder.setText(R.id.football_database_most_item_name , basketDatabaseMostDat.getTeamName());
            }

            if (basketDatabaseMostDat.getPer() == null || basketDatabaseMostDat.getPer().equals("")) {
                holder.setText(R.id.football_database_most_item_avg_score , "--");
            }else{
                holder.setText(R.id.football_database_most_item_avg_score , basketDatabaseMostDat.getPer() + "");
            }

            if (isSPF) {
                String total;
                String count;

                if (basketDatabaseMostDat.getTotal() == null || basketDatabaseMostDat.getTotal().equals("")) {
                    total = "--";
                }else{
                    total = basketDatabaseMostDat.getTotal();
                }
                if (basketDatabaseMostDat.getMatchCount() == null || basketDatabaseMostDat.getMatchCount().equals("")) {
                    count = "--";
                }else{
                    count = basketDatabaseMostDat.getMatchCount();
                }

                holder.setText(R.id.football_database_most_item_total_score , total +"/"+ count);
            }else{
                if (basketDatabaseMostDat.getTotal() == null || basketDatabaseMostDat.getTotal().equals("")) {
                    holder.setText(R.id.football_database_most_item_total_score , "--");
                }else{
                    holder.setText(R.id.football_database_most_item_total_score , basketDatabaseMostDat.getTotal()+"");
                }
            }

            if (isSPF) {
                switch (holder.getPosition()){
                    case 0:
                        holder.setText(R.id.football_database_most_item_avg_num , getResources().getString(R.string.football_database_details_statistic_w_num));
                        holder.setText(R.id.football_database_most_item_num , getResources().getString(R.string.football_database_details_most_top_win));
                        break;
                    case 1:
                        holder.setText(R.id.football_database_most_item_avg_num , getResources().getString(R.string.football_database_details_statistic_d_num));
                        holder.setText(R.id.football_database_most_item_num , getResources().getString(R.string.football_database_details_most_top_draw));
                        break;
                    case 2:
                        holder.setText(R.id.football_database_most_item_avg_num , getResources().getString(R.string.football_database_details_statistic_l_num));
                        holder.setText(R.id.football_database_most_item_num , getResources().getString(R.string.football_database_details_most_top_lost));
                        break;
                }
            }else{
                if (isDEFENS) {
                    holder.setText(R.id.football_database_most_item_avg_num , getResources().getString(R.string.football_database_details_fumble_score_avg));
                    holder.setText(R.id.football_database_most_item_num , getResources().getString(R.string.football_database_details_statistic_fumble_num));
                }else{
                    holder.setText(R.id.football_database_most_item_avg_num , getResources().getString(R.string.football_database_details_most_score_avg));
                    holder.setText(R.id.football_database_most_item_num , getResources().getString(R.string.football_database_details_statistic_all_num));
                }
            }


            if (holder.getPosition() == 0) {
                if (isSPF) {
                    holder.setText(R.id.football_database_most_item_match , getResources().getString(R.string.football_database_details_statistic_w_avgnum));
                }else{
                    holder.setText(R.id.football_database_most_item_match , getResources().getString(R.string.basket_database_details_leagueMost_allmatch));
                }
                holder.setBackgroundColor(R.id.football_database_most_item_left_color , getResources().getColor(R.color.statistics_most_all_color));
            }else if(holder.getPosition() == 1){
                if (isSPF) {
                    holder.setText(R.id.football_database_most_item_match , getResources().getString(R.string.football_database_details_statistic_d_avgnum));
                }else {
                    holder.setText(R.id.football_database_most_item_match, getResources().getString(R.string.basket_database_details_leagueMost_homematch));
                }
                holder.setBackgroundColor(R.id.football_database_most_item_left_color , getResources().getColor(R.color.statistics_most_home_color));
            }else if(holder.getPosition() == 2){
                if (isSPF) {
                    holder.setText(R.id.football_database_most_item_match , getResources().getString(R.string.football_database_details_statistic_l_avgnum));
                }else {
                    holder.setText(R.id.football_database_most_item_match, getResources().getString(R.string.basket_database_details_leagueMost_guestmatch));
                }
                holder.setBackgroundColor(R.id.football_database_most_item_left_color , getResources().getColor(R.color.statistics_most_guest_color));
            }

            ImageView mIcon= (ImageView)holder.getConvertView().findViewById(R.id.progress_statistic);

            ImageLoader.load(mContext,basketDatabaseMostDat.getIcon(),R.mipmap.score_default).into(mIcon);
        }
    }

}
