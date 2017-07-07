package com.hhly.mlottery.frame.basketballframe;

import android.content.Context;
import android.graphics.Bitmap;
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

import com.bumptech.glide.Glide;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseLeagueMost;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseLeagueStatistics;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseMostDat;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseStatisticsBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.RoundProgressBar;
import com.hhly.mlottery.widget.NoScrollListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:
 * author: yixq
 * Created by A on 2016/8/5.
 * 篮球资料库统计
 */
public class BasketDatabaseStatisticsFragment extends Fragment implements View.OnClickListener {

    private static final String PARAM_ID = "leagueId";
    private static final String PARAM2_SEASON = "season";

    private View mView;
    private RoundProgressBar mProgressStatistic;
    private TextView mStatisticCc;
    private TextView mHomeWin;
    private TextView mGuestWin;
    private ProgressBar mEuroProgress1;
    private ProgressBar mEuroProgress2;
    private TextView mEuroText1;
    private TextView mEuroText2;
    private TextView mEuroTextY;
    private TextView mEuroTextN;
    private RoundProgressBar mLetProgress;
    private TextView mLetText;
    private TextView mLetHome;
    private TextView mLetGuest;
    private TextView mLet;
    private RoundProgressBar mBigSmallProgress;
    private TextView mBigSmallText;
    private TextView mBigSmallHome;
    private TextView mBigSmallGuest;
    private TextView mBigSmallZou;
    private TextView mTotalText;
    private TextView mGpText;
    private TextView mAverageText;
    private ProgressBar mVretivalProgress;
    private TextView mGpProgressText;
    private TextView mTextGpNum;
    private TextView mTextNoGpNum;
    private TextView mTextAllNum;
    private NoScrollListView mListView1;
    private NoScrollListView mListView2;
    private NoScrollListView mListView3;
    private NoScrollListView mListView4;
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

    private String mSeason; // 赛季
    private String mLeagueId; // 联赛ID

    private boolean isUpdata = true;//[true:统计  false:之最 ]

    public static BasketDatabaseStatisticsFragment newInstance(String leagueId , String season) {
        BasketDatabaseStatisticsFragment fragment = new BasketDatabaseStatisticsFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_ID, leagueId);
        args.putString(PARAM2_SEASON, season);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mSeason = getArguments().getString(PARAM2_SEASON);
        mLeagueId = getArguments().getString(PARAM_ID);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.basket_database_details_statistics , container , false);

        initView();
        mHandlerData.postDelayed(mRun , 500);

        return mView;
    }

    public void upData(){
        mHandlerData.postDelayed(mRun , 500);
    }

    public void setSeason(String season){
        this.mSeason = season;
    }

    private void initView(){

        mProgressStatistic = (RoundProgressBar) mView.findViewById(R.id.progress_statistic);
        mProgressStatistic.setIsprogress(true);
        mProgressStatistic.setTextIsDisplayable(false);
        mProgressStatistic.setCircleColor(getResources().getColor(R.color.unselected_tab_color));
        mProgressStatistic.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_staristic_width));

        // 胜负统计的场次
        mStatisticCc = (TextView) mView.findViewById(R.id.basket_database_cc);
        //胜负统计主胜
        mHomeWin = (TextView) mView.findViewById(R.id.basket_database_hone_win);
        //胜负统计客胜
        mGuestWin = (TextView) mView.findViewById(R.id.basket_database_guest_win);

        //欧赔统计progress1
        mEuroProgress1 = (ProgressBar) mView.findViewById(R.id.vretival_progress1);
        //欧赔统计progress2
        mEuroProgress2 = (ProgressBar) mView.findViewById(R.id.vretival_progress2);
        //欧赔统计textview1
        mEuroText1 = (TextView) mView.findViewById(R.id.basket_database_euro_tv1);
        //欧赔统计textview2
        mEuroText2 = (TextView) mView.findViewById(R.id.basket_database_euro_tv2);

        //欧赔一致
        mEuroTextY = (TextView) mView.findViewById(R.id.basket_database_euro_v1);
        //欧赔相反
        mEuroTextN = (TextView) mView.findViewById(R.id.basket_database_euro_v2);

        //让分统计progress
        mLetProgress = (RoundProgressBar) mView.findViewById(R.id.progress_statistic_let);
        mLetProgress.setIsprogress(true);
        mLetProgress.setTextIsDisplayable(false);
        mLetProgress.setCircleColor(getResources().getColor(R.color.unselected_tab_color));
        mLetProgress.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_staristic_width));
        //让分统计场次
        mLetText = (TextView) mView.findViewById(R.id.basket_database_cc2);
        //让分统计主胜
        mLetHome = (TextView) mView.findViewById(R.id.basket_database_let_homg);
        //让分统计客胜
        mLetGuest = (TextView) mView.findViewById(R.id.basket_database_let_guest);
        //让分统计走水
        mLet = (TextView) mView.findViewById(R.id.basket_database_let_zou);

        //大小球统计progress
        mBigSmallProgress = (RoundProgressBar) mView.findViewById(R.id.progress_statistic_bigsmall);
        mBigSmallProgress.setIsprogress(true);
        mBigSmallProgress.setTextIsDisplayable(false);
        mBigSmallProgress.setCircleColor(getResources().getColor(R.color.unselected_tab_color));
        mBigSmallProgress.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_staristic_width));
        //大小球统计场次
        mBigSmallText = (TextView)mView.findViewById(R.id.basket_database_cc3);
        //大小球统计主胜
        mBigSmallHome = (TextView)mView.findViewById(R.id.basket_database_big_small_homg);
        //大小球统计客胜
        mBigSmallGuest = (TextView)mView.findViewById(R.id.basket_database_big_small_guest);
        //大小球统计走水
        mBigSmallZou = (TextView)mView.findViewById(R.id.basket_database_big_small_zou);

        //得分统计-总得分
        mTotalText = (TextView) mView.findViewById(R.id.basket_database_total);
        //得分统计-已赛场次
        mGpText = (TextView) mView.findViewById(R.id.basket_database_gp);
        //得分统计-平均得分
        mAverageText = (TextView) mView.findViewById(R.id.basket_database_average);

        //场次统计-progress
        mVretivalProgress = (ProgressBar)mView.findViewById(R.id.vretival_progress_cc);
        //场次统计-已赛进度
        mGpProgressText = (TextView) mView.findViewById(R.id.basket_database_gp_tv);
        //场次统计-已赛
        mTextGpNum = (TextView)mView.findViewById(R.id.basket_database_statistic_gp);
        //场次统计-未赛
        mTextNoGpNum = (TextView)mView.findViewById(R.id.basket_database_statistic_nogp);
        //场次统计总场数
        mTextAllNum = (TextView)mView.findViewById(R.id.basket_database_statistic_all);

        mListView1 = (NoScrollListView)mView.findViewById(R.id.basket_database_leagueMost_list1);
        mListView2 = (NoScrollListView)mView.findViewById(R.id.basket_database_leagueMost_list2);
        mListView3 = (NoScrollListView)mView.findViewById(R.id.basket_database_leagueMost_list3);
        mListView4 = (NoScrollListView)mView.findViewById(R.id.basket_database_leagueMost_list4);

        mListView1.setFocusable(false);
        mListView2.setFocusable(false);
        mListView3.setFocusable(false);
        mListView4.setFocusable(false);

        //统计
        mStatisticLinear = (LinearLayout)mView.findViewById(R.id.basket_database_statistic_ll);
        //之最
        mMostLinear = (LinearLayout) mView.findViewById(R.id.basket_database_most_ll);

        mRadioGroup = (RadioGroup)mView.findViewById(R.id.head_radiogroup);
        mStatisticButton = (RadioButton)mView.findViewById(R.id.basket_database_details_statistics);
        mMostButton = (RadioButton)mView.findViewById(R.id.basket_database_details_most);

        //加载成功
        mData = (LinearLayout)mView.findViewById(R.id.basket_database_details_data);
        mData.setVisibility(View.GONE);
        //加载中...
        mLoading = (FrameLayout) mView.findViewById(R.id.basket_database_loading_details);
        //网络异常
        mLoadRefresh = (LinearLayout) mView.findViewById(R.id.basket_database_details_refresh);
        //暂无数据
        mNodata = (TextView) mView.findViewById(R.id.basket_database_details_nodata);

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
        //http://192.168.31.115:8080/mlottery/core/basketballData.findStatistic.do?lang=zh&leagueId=123456&season=2016-2017
//        String url = "http://192.168.31.115:8888/mlottery/core/basketballData.findStatistic.do" ;
        String url = BaseURLs.URL_BASKET_DATABASE_STATISTIC_DETAILS;
        Map<String , String> params = new HashMap<>();
        if (!mSeason.equals("-1")) {
            params.put("season" , mSeason);
        }
        params.put("leagueId" , mLeagueId);
        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<BasketDatabaseStatisticsBean>() {
            @Override
            public void onResponse(BasketDatabaseStatisticsBean bean) {
                if (bean == null || (bean.getLeagueStatistics() == null && bean.getLeagueMost() == null)) {
                    mHandler.sendEmptyMessage(VIEW_STATUS_NET_NODATA);
                    return;
                }
                //统计
                BasketDatabaseLeagueStatistics data = bean.getLeagueStatistics();
                if (data != null) {
                    setData(data);
                }

                BasketDatabaseLeagueMost data2 = bean.getLeagueMost();

                //之最
                mAdapter1 = new MostAdapter(getContext() , data2.getStrongestAttack(), R.layout.basket_database_most_item);
                mAdapter2 = new MostAdapter(getContext() , data2.getWeakestAttack(), R.layout.basket_database_most_item);
                mAdapter3 = new MostAdapter(getContext() , data2.getStrongestDefense(), R.layout.basket_database_most_item);
                mAdapter4 = new MostAdapter(getContext() , data2.getWeakestDefense(), R.layout.basket_database_most_item);
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
        }, BasketDatabaseStatisticsBean.class);
    }

    private void setData( BasketDatabaseLeagueStatistics data){

        if (data == null || getActivity() == null) {
            return;
        }
        /**
         * 胜负统计
         */
        mProgressStatistic.setProgressAll(data.getHomeWinRate() , data.getGuestWinRate() ,0);
        mProgressStatistic.setCircleProgressColor(getResources().getColor(R.color.basket_database_statistics_background_h));
        mProgressStatistic.setCircleProgressColor2(getResources().getColor(R.color.basket_database_statistics_progress));
        mProgressStatistic.setDatas((int)data.getHomeWin() + "" ,(int)data.getGuestWin() + "" , "");
        mHomeWin.setText((int)(data.getHomeWinRate())+"%");
        mGuestWin.setText((int)(data.getGuestWinRate())+"%");
        mStatisticCc.setText((int)(data.getHomeWin()+data.getGuestWin()) + "");

        /**
         * 欧赔统计
         */
        mEuroProgress1.setProgress((int)(data.getEqualEuroOddRate()));
        mEuroProgress2.setProgress((int)(data.getUnequalEuroOddRate()));
        mEuroText1.setText((int)data.getEqualEuroOdd()+"");
        mEuroText2.setText((int)data.getUnequalEuroOdd()+"");
        mEuroTextY.setText((int)(data.getEqualEuroOddRate())+"%");
        mEuroTextN.setText((int)(data.getUnequalEuroOddRate())+"%");

        /**
         * 让分统计
         */
        mLetProgress.setProgressAll(data.getGraphLetHomeWinRate() , data.getGraphLetGuestWinRate() , data.getGraphLetDrawRate());
        mLetProgress.setCircleProgressColor(getResources().getColor(R.color.basket_database_statistics_background_h));
        mLetProgress.setCircleProgressColor2(getResources().getColor(R.color.basket_database_statistics_progress));
        mLetProgress.setCircleProgressColor3(getResources().getColor(R.color.basket_database_statistics_background_g));
        mLetProgress.setDatas((int)data.getLetHomeWin() + "" ,(int)data.getLetGuestWin() + "" , (int)data.getLetDraw() + "");
        mLetText.setText((int)(data.getLetHomeWin() + data.getLetGuestWin() + data.getLetDraw()) + "");
        mLetHome.setText((int)(data.getLetHomeWinRate()) + "%");
        mLetGuest.setText((int)(data.getLetGuestWinRate()) + "%");
        mLet.setText((int)(data.getLetDrawRate()) + "%");

        /**
         * 大小球统计
         */
        mBigSmallProgress.setProgressAll(data.getGraphAsiaSizeOverRate() , data.getGraphAsiaSizeUnderRate() , data.getGraphAsiaSizeDrawRate());
        mBigSmallProgress.setCircleProgressColor(getResources().getColor(R.color.basket_database_statistics_background_h));
        mBigSmallProgress.setCircleProgressColor2(getResources().getColor(R.color.basket_database_statistics_progress));
        mBigSmallProgress.setCircleProgressColor3(getResources().getColor(R.color.basket_database_statistics_background_g));
        mBigSmallProgress.setDatas((int)data.getAsiaSizeOver() + "" ,(int)data.getAsiaSizeUnder() + "" , (int)data.getAsiaSizeDraw() + "");
        mBigSmallText.setText((int)(data.getAsiaSizeOver() + data.getAsiaSizeUnder() + data.getAsiaSizeDraw()) + "");
        mBigSmallHome.setText((int)(data.getAsiaSizeOverRate()) + "%");
        mBigSmallGuest.setText((int)(data.getAsiaSizeUnderRate()) + "%");
        mBigSmallZou.setText((int)(data.getAsiaSizeDrawRate()) + "%");

        /**
         * 得分统计
         */
        mTotalText.setText((int)data.getTotalScore() + "" );
        mGpText.setText((int)data.getFinishedMatch() + "");
        mAverageText.setText(data.getAvgScore() + "");

        /**
         * 场次统计
         */
        mVretivalProgress.setProgress((int)(data.getFinishedMatch() / data.getTotalMatch() * 100));
        mGpProgressText.setText((int)data.getFinishedMatch() + "");
        mTextGpNum.setText((int)data.getFinishedMatch() + "");
        mTextNoGpNum.setText((int)data.getUnfinishedMatch() + "");
        mTextAllNum.setText((int)data.getTotalMatch() + "");

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

    class MostAdapter extends CommonAdapter<BasketDatabaseMostDat>{

        public MostAdapter(Context context, List<BasketDatabaseMostDat> datas, int layoutId) {
            super(context, datas, layoutId);

            this.mContext = context;
        }
        @Override
        public void convert(ViewHolder holder, BasketDatabaseMostDat basketDatabaseMostDat) {

            if (basketDatabaseMostDat == null) {
                return;
            }
            holder.setText(R.id.basket_database_most_item_name , basketDatabaseMostDat.getTeamName());
            holder.setText(R.id.basket_database_most_item_total_score , (int)basketDatabaseMostDat.getTotalScore()+"");
            holder.setText(R.id.basket_database_most_item_avg_score , basketDatabaseMostDat.getAvgScore() + "");

            if (holder.getPosition() == 0) {
                holder.setText(R.id.basket_database_most_item_match , getResources().getString(R.string.basket_database_details_leagueMost_allmatch));
                holder.setBackgroundColor(R.id.basket_database_most_item_left_color , getResources().getColor(R.color.statistics_most_all_color));
            }else if(holder.getPosition() == 1){
                holder.setText(R.id.basket_database_most_item_match , getResources().getString(R.string.basket_database_details_leagueMost_homematch));
                holder.setBackgroundColor(R.id.basket_database_most_item_left_color , getResources().getColor(R.color.statistics_most_home_color));
            }else if(holder.getPosition() == 2){
                holder.setText(R.id.basket_database_most_item_match , getResources().getString(R.string.basket_database_details_leagueMost_guestmatch));
                holder.setBackgroundColor(R.id.basket_database_most_item_left_color , getResources().getColor(R.color.statistics_most_guest_color));
            }

            ImageView mIcon= (ImageView)holder.getConvertView().findViewById(R.id.progress_statistic);
            if(getActivity()!=null){
                ImageLoader.load(getActivity(),basketDatabaseMostDat.getTeamIconUrl(),R.mipmap.basket_default).into(mIcon);

            }

        }
    }

}
