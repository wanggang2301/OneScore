package com.hhly.mlottery.frame.basketballframe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseStatisticsBean;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabsseLeagueStatistics;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.RoundProgressBar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/5.
 */

public class BasketDatasaseStatisticsFragment extends Fragment{

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

    public static BasketDatasaseStatisticsFragment newInstance(String leagueId , String season) {
        BasketDatasaseStatisticsFragment fragment = new BasketDatasaseStatisticsFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_ID, leagueId);
        args.putString(PARAM2_SEASON, season);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO-----
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.basket_database_details_statistics , container , false);

        initView();
        initData();

        return mView;
    }

    private void initView(){

        mProgressStatistic = (RoundProgressBar) mView.findViewById(R.id.progress_statistic);
        mProgressStatistic.setIsprogress(true);
        mProgressStatistic.setTextIsDisplayable(false);
        mProgressStatistic.setCircleColor(getResources().getColor(R.color.unselected_tab_color));

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

    }

    private void initData(){

        //http://192.168.31.115:8080/mlottery/core/basketballData.findStatistic.do?lang=zh&leagueId=123456&season=2016-2017
        String url = "http://192.168.31.115:8888/mlottery/core/basketballData.findStatistic.do" ;

        Map<String , String> params = new HashMap<>();
        params.put("leagueId" , "123456");
        params.put("season" , "2016-2017");

        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<BasketDatabaseStatisticsBean>() {

            @Override
            public void onResponse(BasketDatabaseStatisticsBean bean) {

                if (bean == null) {

                    return;
                }
                BasketDatabsseLeagueStatistics data ;
                data = bean.getLeagueStatistics();

                setData(data);

                Toast.makeText(getContext(), "**** ok ****", Toast.LENGTH_SHORT).show();

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                Toast.makeText(getContext(), "**** no ****", Toast.LENGTH_SHORT).show();
            }
        }, BasketDatabaseStatisticsBean.class);
    }

    private void setData( BasketDatabsseLeagueStatistics data){

        /**
         * 胜负统计
         */
//        mProgressStatistic.setProgress(20);
        mProgressStatistic.setProgress(data.getHomeWinRate()*100);
//        mProgressStatistic.setProgress2(40);
        mProgressStatistic.setProgress2(data.getGuestWinRate()*100);
        mProgressStatistic.setCircleProgressColor(getResources().getColor(R.color.basket_database_statistics_background_h));
        mProgressStatistic.setCircleProgressColor2(getResources().getColor(R.color.basket_database_statistics_background_g));
        mProgressStatistic.setRoundWidth(60);
        mProgressStatistic.setDatas((int)data.getHomeWin() + "" ,(int)data.getGuestWin() + "" , "");
        mHomeWin.setText("主胜 : " + (int)(data.getHomeWinRate()*100)+"%");
        mGuestWin.setText("客胜 : " + (int)(data.getGuestWinRate()*100)+"%");
        mStatisticCc.setText((int)(data.getHomeWin()+data.getGuestWin()) + "");

        /**
         * 欧赔统计
         */
        mEuroProgress1.setProgress((int)(data.getEqualEuroOddRate()*100));
        mEuroProgress2.setProgress((int)(data.getUnequalEuroOddRate()*100));
        mEuroText1.setText((int)data.getEqualEuroOdd()+"");
        mEuroText2.setText((int)data.getUnequalEuroOdd()+"");
        mEuroTextY.setText("赛果与欧赔一致 " + (int)(data.getEqualEuroOddRate()*100)+"%");
        mEuroTextN.setText("赛果与欧赔相反 " + (int)(data.getUnequalEuroOddRate()*100)+"%");

        /**
         * 让分统计
         */
        mLetProgress.setProgress(data.getLetHomeWinRate()*100);
        mLetProgress.setProgress2(data.getLetGuestWinRate()*100);
        mLetProgress.setProgress3(data.getLetDrawRate()*100);
        mLetProgress.setCircleProgressColor(getResources().getColor(R.color.basket_database_statistics_background_h));
        mLetProgress.setCircleProgressColor2(getResources().getColor(R.color.basket_database_statistics_background_g));
        mLetProgress.setCircleProgressColor3(getResources().getColor(R.color.basket_database_statistics_background_d));
        mLetProgress.setRoundWidth(60);
        mLetProgress.setDatas((int)data.getLetHomeWin() + "" ,(int)data.getLetGuestWin() + "" , (int)data.getLetDraw() + "");
        mLetText.setText((int)(data.getLetHomeWin() + data.getLetGuestWin() + data.getLetDraw()) + "");
        mLetHome.setText("主胜 : " + (int)(data.getLetHomeWinRate()*100) + "%");
        mLetGuest.setText("客胜 : " + (int)(data.getLetGuestWinRate()*100) + "%");
        mLet.setText("走水 : " + (int)(data.getLetDrawRate()*100) + "%");

        /**
         * 大小球统计
         */
        mBigSmallProgress.setProgress(data.getAsiaSizeOverRate()*100);
        mBigSmallProgress.setProgress2(data.getAsiaSizeUnderRate()*100);
        mBigSmallProgress.setProgress3(data.getAsiaSizeDrawRate()*100);
        mBigSmallProgress.setCircleProgressColor(getResources().getColor(R.color.basket_database_statistics_background_h));
        mBigSmallProgress.setCircleProgressColor2(getResources().getColor(R.color.basket_database_statistics_background_g));
        mBigSmallProgress.setCircleProgressColor3(getResources().getColor(R.color.basket_database_statistics_background_d));
        mBigSmallProgress.setDatas((int)data.getAsiaSizeOver() + "" ,(int)data.getAsiaSizeUnder() + "" , (int)data.getAsiaSizeDraw() + "");
        mBigSmallProgress.setRoundWidth(60);
        mBigSmallText.setText((int)(data.getAsiaSizeOver() + data.getAsiaSizeUnder() + data.getAsiaSizeDraw()) + "");
        mBigSmallHome.setText("大球 : " + (int)(data.getAsiaSizeOverRate()*100) + "%");
        mBigSmallGuest.setText("小球 : " + (int)(data.getAsiaSizeUnderRate()*100) + "%");
        mBigSmallZou.setText("走水 : " + (int)(data.getAsiaSizeDrawRate()*100) + "%");

        /**
         * 得分统计
         */
        mTotalText.setText((int)data.getTotalScore() + "");
        mGpText.setText((int)data.getFinishedMatch() + "");
        mAverageText.setText(data.getAvgScore() + "");

        /**
         * 场次统计
         */
        mVretivalProgress.setProgress((int)(data.getFinishedMatch() / data.getTotalMatch() * 100));
        mGpProgressText.setText((int)data.getFinishedMatch() + "");
        mTextGpNum.setText("已赛 " + (int)data.getFinishedMatch() + "");
        mTextNoGpNum.setText("未赛 " + (int)data.getUnfinishedMatch() + "");
        mTextAllNum.setText("总赛场次 " + (int)data.getTotalMatch() + "");



    }

}
