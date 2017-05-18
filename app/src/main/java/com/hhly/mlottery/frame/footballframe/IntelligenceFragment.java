package com.hhly.mlottery.frame.footballframe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.football.IntelligenceRecentAdapter;
import com.hhly.mlottery.adapter.football.IntelligenceResultAdapter;
import com.hhly.mlottery.bean.intelligence.BigDataForecast;
import com.hhly.mlottery.bean.intelligence.BigDataForecastData;
import com.hhly.mlottery.bean.intelligence.BigDataForecastFactor;
import com.hhly.mlottery.bean.intelligence.BigDataResult;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.StringFormatUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.RoundProgressBar;
import com.hhly.mlottery.widget.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/7/18.
 */
public class IntelligenceFragment extends Fragment {

    private static final String KEY_THIRD_ID = "thirdId";

    private static final int TYPE_HOST = 0;
    private static final int TYPE_SIZE = 1;
    private static final int TYPE_ASIA = 2;

    ImageView mDottedLine1;
    ImageView mDottedLine2;
    ImageView mDottedLine3;

    View mDiyComputeMethodView;

    TextView mHostAlert; /* 采样不足警告 */
    TextView mSizeAlert; /* 采样不足警告 */
    TextView mAsiaAlert; /* 采样不足警告 */

    RoundProgressBar mHostProgress;
    RoundProgressBar mSizeProgress;
    RoundProgressBar mAsiaProgress;

    TextView mHistoryHostWin;
    TextView mHistorySizeWin;
    TextView mHistoryAsiaWin;
    ContentLoadingProgressBar mHistoryHostWinProgress;
    ContentLoadingProgressBar mHistorySizeWinProgress;
    ContentLoadingProgressBar mHistoryAsiaWinProgress;

    TextView mHostRecentHostWin;
    TextView mHostRecentSizeWin;
    TextView mHostRecentAsiaWin;
    ContentLoadingProgressBar mHostRecentHostWinProgress;
    ContentLoadingProgressBar mHostRecentSizeWinProgress;
    ContentLoadingProgressBar mHostRecentAsiaWinProgress;

    TextView mGuestRecentHostWin;
    TextView mGuestRecentSizeWin;
    TextView mGuestRecentAsiaWin;
    ContentLoadingProgressBar mGuestRecentHostWinProgress;
    ContentLoadingProgressBar mGuestRecentSizeWinProgress;
    ContentLoadingProgressBar mGuestRecentAsiaWinProgress;

    private IntelligenceComputeMethodDialogFragment mDialog;
    private RadioGroup mRg;
    private RadioButton mRbAll;
    private RadioButton mRbGround;
    //盘口的数据
    private List<String> mHandicaps;

    //赛场情报全场
    private MyGridView mGvResultFull;
    private IntelligenceResultAdapter mFullResultAdapter;
    private List<BigDataResult.GridViewEntity> fullResultList=new ArrayList<>();
    private LinearLayout mLlResultFull;
    private TextView mResultFullNodata;
    //半场
    private MyGridView mGvResultHalf;
    private IntelligenceResultAdapter mHalfResultAdapter;
    private List<BigDataResult.GridViewEntity> halfResultList=new ArrayList<>();
    private LinearLayout mLlResultHalf;

    //进球、result_goal_difference
    private MyGridView mGvResultGoal;
    private IntelligenceResultAdapter mGoalResultAdapter;
    private List<BigDataResult.GridViewEntity> goalResultList=new ArrayList<>();
    private LinearLayout mLlResultGoal;

    // 本赛季盘口情报全场
    private MyGridView mGvSeasonHandicapFull;
    private IntelligenceResultAdapter mSeasonHandicapFullAdapter;
    private List<BigDataResult.GridViewEntity> fullSeasonHandicapList=new ArrayList<>();
    private LinearLayout mLlSeasonHandicapFull;
    private TextView mSeasonHandicapNodata;

    // 本赛季盘口情报半场
    private MyGridView mGvSeasonHandicapHalf;
    private IntelligenceResultAdapter mSeasonHandicapHalfAdapter;
    private List<BigDataResult.GridViewEntity> halfSeasonHandicapList=new ArrayList<>();
    private LinearLayout mLlSeasonHandicapHalf;

    //今日盘口情报 全场
    private LinearLayout mLlTodayHandicap;

    private MyGridView mGvTodayHandicapFull;
    private IntelligenceResultAdapter mTodayHandicapFullAdapter;
    private List<BigDataResult.GridViewEntity> fullTodayHandicapList=new ArrayList<>();
    private LinearLayout mLlTodayHandicapFull;
    private TextView mTodayHandicapNodata;

    // 今日盘口情报半场
    private MyGridView mGvTodayHandicapHalf;
    private IntelligenceResultAdapter mTodayHandicapHalfAdapter;
    private List<BigDataResult.GridViewEntity> halfTodayHandicapList=new ArrayList<>();
    private LinearLayout mLlTodayHandicapHalf;

    private BigDataForecast mBigDataForecast;
    private BigDataForecastFactor mFactor;
    private BigDataResult mResult;
    //近期比赛全场
    private LinearLayout mLlRecentFull;
    private MyGridView mGvRecentFull;
    private IntelligenceRecentAdapter mRecentFullAdapter;
    private List<BigDataResult.GridViewEntity> fullRecentList=new ArrayList<>();
    private TextView mRecentNodata;
    //近期比赛半场
    private LinearLayout mLlRecentHalf;
    private MyGridView mGvRecentHalf;
    private IntelligenceRecentAdapter mRecentHalfAdapter;
    private List<BigDataResult.GridViewEntity> halfRecentList=new ArrayList<>();

//    private boolean isLoading = false;// 是否已加载过数据

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mFactor == null) {
            mFactor = new BigDataForecastFactor();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intelligence, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initData();
    }

    /**
     * 设置空提醒
     */
    private void setEmptyAlert() {
        mHistoryHostWin.setText("");
        mHistorySizeWin.setText("");
        mHistoryAsiaWin.setText("");

        mHostRecentHostWin.setText("");
        mHostRecentSizeWin.setText("");
        mHostRecentAsiaWin.setText("");

        mGuestRecentHostWin.setText("");
        mGuestRecentSizeWin.setText("");
        mGuestRecentAsiaWin.setText("");
    }

    /**
     * 初始化 View
     *
     * @param view
     */
    private void initViews(View view) {
        mHostAlert = (TextView) view.findViewById(R.id.host_alert);
        mSizeAlert = (TextView) view.findViewById(R.id.size_alert);
        mAsiaAlert = (TextView) view.findViewById(R.id.asia_alert);

        mDottedLine1 = (ImageView) view.findViewById(R.id.dotted_line1);
        mDottedLine2 = (ImageView) view.findViewById(R.id.dotted_line2);
        mDottedLine3 = (ImageView) view.findViewById(R.id.dotted_line3);

        // 关闭硬件加速才显示虚线
        mDottedLine1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mDottedLine2.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mDottedLine3.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mHostProgress = (RoundProgressBar) view.findViewById(R.id.home_progress);
        mSizeProgress = (RoundProgressBar) view.findViewById(R.id.size_progress);
        mAsiaProgress = (RoundProgressBar) view.findViewById(R.id.asia_progress);

        mHistoryHostWin = (TextView) view.findViewById(R.id.history_host_win_rate);
        mHistorySizeWin = (TextView) view.findViewById(R.id.history_size_win_rate);
        mHistoryAsiaWin = (TextView) view.findViewById(R.id.history_asia_win_rate);

        mHostRecentHostWin = (TextView) view.findViewById(R.id.home_recent_home_win_rate);
        mHostRecentSizeWin = (TextView) view.findViewById(R.id.home_recent_size_win_rate);
        mHostRecentAsiaWin = (TextView) view.findViewById(R.id.home_recent_asia_win_rate);

        mGuestRecentHostWin = (TextView) view.findViewById(R.id.guest_recent_home_win_rate);
        mGuestRecentSizeWin = (TextView) view.findViewById(R.id.guest_recent_size_win_rate);
        mGuestRecentAsiaWin = (TextView) view.findViewById(R.id.guest_recent_asia_win_rate);

        mHistoryHostWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.history_host_win_progress);
        mHistorySizeWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.history_size_win_progress);
        mHistoryAsiaWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.history_asia_win_progress);

        mHostRecentHostWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.home_recent_home_win_rate_progress);
        mHostRecentSizeWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.home_recent_size_win_rate_progress);
        mHostRecentAsiaWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.home_recent_asia_win_rate_progress);

        mGuestRecentHostWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.guest_recent_home_win_rate_progress);
        mGuestRecentSizeWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.guest_recent_size_win_rate_progress);
        mGuestRecentAsiaWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.guest_recent_asia_win_rate_progress);

        mDiyComputeMethodView = view.findViewById(R.id.diy_text);
        mDiyComputeMethodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog == null) {
                    mDialog = IntelligenceComputeMethodDialogFragment
                            .newInstance(mBigDataForecast, mFactor);
                }
                if (!mDialog.isVisible()) {
                    mDialog.show(getChildFragmentManager(), "computeMethod");
                }
            }
        });

        mRg= (RadioGroup) view.findViewById(R.id.radio_group_intelligent);
        mLlResultFull= (LinearLayout) view.findViewById(R.id.ll_result_full);
        mResultFullNodata = (TextView) view.findViewById(R.id.result_full_nodata);
        mLlResultHalf= (LinearLayout) view.findViewById(R.id.ll_result_half);
        mLlResultGoal= (LinearLayout) view.findViewById(R.id.ll_result_goal);

        mLlSeasonHandicapFull= (LinearLayout) view.findViewById(R.id.ll_season_handicap_full);
        mLlSeasonHandicapHalf= (LinearLayout) view.findViewById(R.id.ll_season_handicap_half);
        mSeasonHandicapNodata= (TextView) view.findViewById(R.id.season_handicap_nodata);

        mLlTodayHandicapFull= (LinearLayout) view.findViewById(R.id.ll_today_handicap_full);
        mLlTodayHandicapHalf= (LinearLayout) view.findViewById(R.id.ll_today_handicap_half);
        mTodayHandicapNodata= (TextView) view.findViewById(R.id.today_handicap_nodata);

        mLlRecentFull= (LinearLayout) view.findViewById(R.id.ll_recent_full);
        mLlRecentHalf= (LinearLayout) view.findViewById(R.id.ll_recent_half);
        mRecentNodata= (TextView) view.findViewById(R.id.intelligent_recent_nodata);
        //赛果全场
        mGvResultFull= (MyGridView) view.findViewById(R.id.gv_result_full_game);
        mFullResultAdapter=new IntelligenceResultAdapter(getActivity(),fullResultList);
        mGvResultFull.setAdapter(mFullResultAdapter);

        //半场
        mGvResultHalf= (MyGridView) view.findViewById(R.id.gv_result_half_game);
        mHalfResultAdapter=new IntelligenceResultAdapter(getActivity(),halfResultList);
        mGvResultHalf.setAdapter(mHalfResultAdapter);

        //进球、result_goal_difference
        mGvResultGoal= (MyGridView) view.findViewById(R.id.gv_result_goal_game);
        mGoalResultAdapter=new IntelligenceResultAdapter(getActivity(),goalResultList);
        mGvResultGoal.setAdapter(mGoalResultAdapter);

        //本赛季盘口全场
        mGvSeasonHandicapFull = (MyGridView) view.findViewById(R.id.gv_season_handicap_full_game);
        mSeasonHandicapFullAdapter=new IntelligenceResultAdapter(getActivity(),fullSeasonHandicapList);
        mGvSeasonHandicapFull.setAdapter(mSeasonHandicapFullAdapter);

        //半场
        mGvSeasonHandicapHalf = (MyGridView) view.findViewById(R.id.gv_season_handicap_half_game);
        mSeasonHandicapHalfAdapter=new IntelligenceResultAdapter(getActivity(),halfSeasonHandicapList);
        mGvSeasonHandicapHalf.setAdapter(mSeasonHandicapHalfAdapter);

        //今日赛季盘口
        mLlTodayHandicap= (LinearLayout) view.findViewById(R.id.ll_today_handicap);
        mGvTodayHandicapFull = (MyGridView) view.findViewById(R.id.gv_today_handicap_full_game);
        mTodayHandicapFullAdapter=new IntelligenceResultAdapter(getActivity(),fullTodayHandicapList);
        mGvTodayHandicapFull.setAdapter(mTodayHandicapFullAdapter);
        //半场
        mGvTodayHandicapHalf = (MyGridView) view.findViewById(R.id.gv_today_handicap_half_game);
        mTodayHandicapHalfAdapter=new IntelligenceResultAdapter(getActivity(),halfTodayHandicapList);
        mGvTodayHandicapHalf.setAdapter(mTodayHandicapHalfAdapter);
        //近期比赛
        mGvRecentFull= (MyGridView) view.findViewById(R.id.gv_recent_full_game);
        mRecentFullAdapter=new IntelligenceRecentAdapter(getActivity(),fullRecentList);
        mGvRecentFull.setAdapter(mRecentFullAdapter);
        //半场
        mGvRecentHalf= (MyGridView) view.findViewById(R.id.gv_recent_half_game);
        mRecentHalfAdapter=new IntelligenceRecentAdapter(getActivity(),halfRecentList);
        mGvRecentHalf.setAdapter(mRecentHalfAdapter);

        mGvResultFull.setFocusable(false);
        mGvResultHalf.setFocusable(false);
        mGvResultGoal.setFocusable(false);
        mGvSeasonHandicapFull.setFocusable(false);
        mGvSeasonHandicapHalf.setFocusable(false);
        mGvRecentFull.setFocusable(false);
        mGvRecentHalf.setFocusable(false);
        mGvTodayHandicapFull.setFocusable(false);
        mGvTodayHandicapHalf.setFocusable(false);
    }

    public void initData(){
        Map<String, String> params = new HashMap<>();
        params.put(KEY_THIRD_ID, getActivity() != null ? ((FootballMatchDetailActivity)getActivity()).mThirdId : null);

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_INTELLIGENCE_BIG_DATA, params,
                new VolleyContentFast.ResponseSuccessListener<BigDataResult>() {
                    @Override
                    public void onResponse(BigDataResult jsonObject) {
                        mResult=jsonObject;
                        mBigDataForecast = jsonObject.getBigDataForecast();
                        if (jsonObject.getResult() != 200 || mBigDataForecast == null) {
                            refreshFactorUI(false);
                            setEmptyAlert();
                            mDiyComputeMethodView.setVisibility(View.GONE);

                            mLlRecentFull.setVisibility(View.GONE);
                            mLlRecentHalf.setVisibility(View.GONE);
                            mRecentNodata.setVisibility(View.VISIBLE);

                            mLlTodayHandicapFull.setVisibility(View.GONE);
                            mLlTodayHandicapHalf.setVisibility(View.GONE);
                            mTodayHandicapNodata.setVisibility(View.VISIBLE);

                            mLlSeasonHandicapFull.setVisibility(View.GONE);
                            mLlSeasonHandicapHalf.setVisibility(View.GONE);
                            mSeasonHandicapNodata.setVisibility(View.VISIBLE);

                            mLlResultFull.setVisibility(View.GONE);
                            mLlResultHalf.setVisibility(View.GONE);
                            mLlResultGoal.setVisibility(View.GONE);
                            mResultFullNodata.setVisibility(View.VISIBLE);

                            return;
                        }
                        mDiyComputeMethodView.setVisibility(View.VISIBLE);
                        BigDataForecastData battleHistory = mBigDataForecast.getBattleHistory();
                        BigDataForecastData homeRecent = mBigDataForecast.getHomeRecent();
                        BigDataForecastData guestRecent = mBigDataForecast.getGuestRecent();

                        setWinRate(battleHistory, false,
                                mHistoryHostWin, mHistorySizeWin, mHistoryAsiaWin,
                                mHistoryHostWinProgress, mHistorySizeWinProgress, mHistoryAsiaWinProgress);
                        setWinRate(homeRecent, false,
                                mHostRecentHostWin, mHostRecentSizeWin, mHostRecentAsiaWin,
                                mHostRecentHostWinProgress, mHostRecentSizeWinProgress, mHostRecentAsiaWinProgress);
                        setWinRate(guestRecent, true,
                                mGuestRecentHostWin, mGuestRecentSizeWin, mGuestRecentAsiaWin,
                                mGuestRecentHostWinProgress, mGuestRecentSizeWinProgress, mGuestRecentAsiaWinProgress);
                        refreshFactorUI(false);
                        loadData();

                    }
                },
                new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                    }
                }, BigDataResult.class);
    }

    /**
     *
     * 全部比赛与相同主客场。
     */
    private void loadData(){
            setAllResult();
            setAllSeasonHandicap();
            setAllTodayHandicap();
            setAllRecent();
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.intelligent_rb_all:
                            setAllResult();
                            setAllSeasonHandicap();
                            setAllTodayHandicap();
                            setAllRecent();
                        break;
                    case R.id.intelligent_rb_same:
                            setGroundResult();
                            setGroundSeasonHandicap();
                            setGroundTodayHandicap();
                            setGroundRecent();
                        break;
                }
            }
        });
    }

    /**
     * 最近比赛的相同主客场
     */
    private void setGroundRecent() {
        BigDataResult.GroundEntity entity=mResult.getGround();
        mLlRecentFull.setVisibility(View.VISIBLE);
        mLlRecentHalf.setVisibility(View.VISIBLE);
        mRecentNodata.setVisibility(View.GONE);

        if(entity.getFullRecentList()!=null&&entity.getFullRecentList().size()!=0){
            fullRecentList.clear();
            fullRecentList.addAll(entity.getFullRecentList());
            mRecentFullAdapter.notifyDataSetChanged();

        }else{
            mLlRecentFull.setVisibility(View.GONE);
            mRecentNodata.setVisibility(View.GONE);
        }

        if(entity.getHalfRecentList()!=null&&entity.getHalfRecentList().size()!=0){
            halfRecentList.clear();
            halfRecentList.addAll(entity.getHalfRecentList());
            mRecentHalfAdapter.notifyDataSetChanged();

        }else {
            mLlRecentHalf.setVisibility(View.GONE);
            mRecentNodata.setVisibility(View.GONE);
        }
        if(entity.getFullRecentList()!=null&&entity.getFullRecentList().size()!=0||entity.getHalfRecentList()!=null&&entity.getHalfRecentList().size()!=0){

        }else{
            mRecentNodata.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 最近比赛的全部比赛
     */
    private void setAllRecent() {
        BigDataResult.AllEntity entity=mResult.getAll();
        mLlRecentFull.setVisibility(View.VISIBLE);
        mLlRecentHalf.setVisibility(View.VISIBLE);
        mRecentNodata.setVisibility(View.GONE);

        if(entity.getFullRecentList()!=null&&entity.getFullRecentList().size()!=0){
            fullRecentList.clear();
            fullRecentList.addAll(entity.getFullRecentList());
            mRecentFullAdapter.notifyDataSetChanged();

        }else{
            mLlRecentFull.setVisibility(View.GONE);
            mRecentNodata.setVisibility(View.GONE);
        }

        if(entity.getHalfRecentList()!=null&&entity.getHalfRecentList().size()!=0){
            halfRecentList.clear();
            halfRecentList.addAll(entity.getHalfRecentList());
            mRecentHalfAdapter.notifyDataSetChanged();

        }else {
            mLlRecentHalf.setVisibility(View.GONE);
            mRecentNodata.setVisibility(View.GONE);
        }
        if(entity.getFullRecentList()!=null&&entity.getFullRecentList().size()!=0||entity.getHalfRecentList()!=null&&entity.getHalfRecentList().size()!=0){

        }else{
            mRecentNodata.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 今日盘口相同主客场
     */
    private void setGroundTodayHandicap() {
        mLlTodayHandicap.setVisibility(View.VISIBLE);
        BigDataResult.GroundEntity entity=mResult.getGround();
        mLlTodayHandicapFull.setVisibility(View.VISIBLE);
        mLlTodayHandicapHalf.setVisibility(View.VISIBLE);
        mTodayHandicapNodata.setVisibility(View.GONE);

        if(entity.getTodayFullHandicapList()!=null&&entity.getTodayFullHandicapList().size()!=0) {
            fullTodayHandicapList.clear();
            fullTodayHandicapList.addAll(entity.getTodayFullHandicapList());
            mTodayHandicapFullAdapter.notifyDataSetChanged();
        }else{
            mLlTodayHandicapFull.setVisibility(View.GONE);
            mTodayHandicapNodata.setVisibility(View.GONE);
        }
        if(entity.getTodayHalfHandicapList()!=null&&entity.getTodayHalfHandicapList().size()!=0) {
            halfTodayHandicapList.clear();
            halfTodayHandicapList.addAll(entity.getTodayHalfHandicapList());
            mTodayHandicapHalfAdapter.notifyDataSetChanged();
        }else {
            mLlTodayHandicapHalf.setVisibility(View.GONE);
            mTodayHandicapNodata.setVisibility(View.GONE);
        }

        if(entity.getTodayFullHandicapList()!=null&&entity.getTodayFullHandicapList().size()!=0||entity.getTodayHalfHandicapList()!=null&&entity.getTodayHalfHandicapList().size()!=0){

        }else{
            mTodayHandicapNodata.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 今日盘口全部比赛的处理
     */
    private void setAllTodayHandicap() {
        mLlTodayHandicap.setVisibility(View.GONE);
    }

    /**
     * 设置相同主客场的本赛季盘口
     */
    private void setGroundSeasonHandicap() {
        BigDataResult.GroundEntity entity=mResult.getGround();
        mLlSeasonHandicapFull.setVisibility(View.VISIBLE);
        mLlSeasonHandicapHalf.setVisibility(View.VISIBLE);
        mSeasonHandicapNodata.setVisibility(View.GONE);

        if(entity.getFullHandicapList()!=null&&entity.getFullHandicapList().size()!=0) {
            fullSeasonHandicapList.clear();
            fullSeasonHandicapList.addAll(entity.getFullHandicapList());
            mSeasonHandicapFullAdapter.notifyDataSetChanged();
        }else{
            mLlSeasonHandicapFull.setVisibility(View.GONE);
            mSeasonHandicapNodata.setVisibility(View.GONE);
        }
        if(entity.getHalfHandicapList()!=null&&entity.getHalfHandicapList().size()!=0) {
            halfSeasonHandicapList.clear();
            halfSeasonHandicapList.addAll(entity.getHalfHandicapList());
            mSeasonHandicapHalfAdapter.notifyDataSetChanged();
        }else{
            mLlSeasonHandicapHalf.setVisibility(View.GONE);
            mSeasonHandicapNodata.setVisibility(View.GONE);
        }

        if(entity.getFullHandicapList()!=null&&entity.getFullHandicapList().size()!=0||entity.getHalfHandicapList()!=null&&entity.getHalfHandicapList().size()!=0) {

        }else {
            mSeasonHandicapNodata.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置赛果界面全部
     */
    private void setAllResult(){
        BigDataResult.AllEntity entity=mResult.getAll();
        mLlResultFull.setVisibility(View.VISIBLE);
        mLlResultHalf.setVisibility(View.VISIBLE);
        mLlResultGoal.setVisibility(View.VISIBLE);
        mResultFullNodata.setVisibility(View.GONE);

        if(entity.getFullResultList()!=null&&entity.getFullResultList().size()!=0){
            fullResultList.clear();
            fullResultList.addAll(entity.getFullResultList());
            mFullResultAdapter.notifyDataSetChanged();
        }
            else{
            mLlResultFull.setVisibility(View.GONE);
            mResultFullNodata.setVisibility(View.GONE);
        }
        if(entity.getHalfResultList()!=null&&entity.getHalfResultList().size()!=0) {
            halfResultList.clear();
            halfResultList.addAll(entity.getHalfResultList());
            mHalfResultAdapter.notifyDataSetChanged();
        }   else{
            mLlResultHalf.setVisibility(View.GONE);
            mResultFullNodata.setVisibility(View.GONE);
        }
        if(entity.getGoalResultList()!=null&&entity.getGoalResultList().size()!=0) {
            goalResultList.clear();
            goalResultList.addAll(entity.getGoalResultList());
            mGoalResultAdapter.notifyDataSetChanged();
        }   else {
            mLlResultGoal.setVisibility(View.GONE);
            mResultFullNodata.setVisibility(View.GONE);
        }

        if(entity.getFullResultList()!=null&&entity.getFullResultList().size()!=0||entity.getHalfResultList()!=null&&entity.getHalfResultList().size()!=0||entity.getGoalResultList()!=null&&entity.getGoalResultList().size()!=0){

        }else{
            mResultFullNodata.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置赛果界面相同主客场
     */
    private void setGroundResult(){
        BigDataResult.GroundEntity entity=mResult.getGround();
        mLlResultFull.setVisibility(View.VISIBLE);
        mLlResultHalf.setVisibility(View.VISIBLE);
        mLlResultGoal.setVisibility(View.VISIBLE);
        mResultFullNodata.setVisibility(View.GONE);

        if(entity.getFullResultList()!=null&&entity.getFullResultList().size()!=0){
            fullResultList.clear();
            fullResultList.addAll(entity.getFullResultList());
            mFullResultAdapter.notifyDataSetChanged();
        }
        else{
            mLlResultFull.setVisibility(View.GONE);
            mResultFullNodata.setVisibility(View.GONE);
        }
        if(entity.getHalfResultList()!=null&&entity.getHalfResultList().size()!=0) {
            halfResultList.clear();
            halfResultList.addAll(entity.getHalfResultList());
            mHalfResultAdapter.notifyDataSetChanged();
        }   else{
            mLlResultHalf.setVisibility(View.GONE);
            mResultFullNodata.setVisibility(View.GONE);
        }
        if(entity.getGoalResultList()!=null&&entity.getGoalResultList().size()!=0) {
            goalResultList.clear();
            goalResultList.addAll(entity.getGoalResultList());
            mGoalResultAdapter.notifyDataSetChanged();
        }   else {
            mLlResultGoal.setVisibility(View.GONE);
            mResultFullNodata.setVisibility(View.GONE);
        }

        if(entity.getFullResultList()!=null&&entity.getFullResultList().size()!=0||entity.getHalfResultList()!=null&&entity.getHalfResultList().size()!=0||entity.getGoalResultList()!=null&&entity.getGoalResultList().size()!=0){

        }else{
            mResultFullNodata.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 设置本赛季盘口全部比赛
     */
    private void setAllSeasonHandicap(){
        BigDataResult.AllEntity entity=mResult.getAll();
        mLlSeasonHandicapFull.setVisibility(View.VISIBLE);
        mLlSeasonHandicapHalf.setVisibility(View.VISIBLE);
        mSeasonHandicapNodata.setVisibility(View.GONE);

        if(entity.getFullHandicapList()!=null&&entity.getFullHandicapList().size()!=0) {
            fullSeasonHandicapList.clear();
            fullSeasonHandicapList.addAll(entity.getFullHandicapList());
            mSeasonHandicapFullAdapter.notifyDataSetChanged();
        }else{
            mLlSeasonHandicapFull.setVisibility(View.GONE);
            mSeasonHandicapNodata.setVisibility(View.GONE);
        }
        if(entity.getHalfHandicapList()!=null&&entity.getHalfHandicapList().size()!=0) {
            halfSeasonHandicapList.clear();
            halfSeasonHandicapList.addAll(entity.getHalfHandicapList());
            mSeasonHandicapHalfAdapter.notifyDataSetChanged();
        }else{
            mLlSeasonHandicapHalf.setVisibility(View.GONE);
            mSeasonHandicapNodata.setVisibility(View.GONE);
        }

        if(entity.getFullHandicapList()!=null&&entity.getFullHandicapList().size()!=0||entity.getHalfHandicapList()!=null&&entity.getHalfHandicapList().size()!=0) {

        }else {
            mSeasonHandicapNodata.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 设置胜率文本
     *
     * @param oddsInfo     胜率内容实体
     * @param hostTextView 主胜
     * @param sizeTextView 大球
     * @param asiaTextView 赢盘
     */
    private void setWinRate(BigDataForecastData oddsInfo, boolean isLose,
                            TextView hostTextView, TextView sizeTextView, TextView asiaTextView,
                            ProgressBar hostProgress, ProgressBar sizeProgress, ProgressBar asiaProgress) {
        if (oddsInfo != null) {

            Double homePercent;
            if (isLose) {
                homePercent = oddsInfo.getHomeLosePercent();
            } else {
                homePercent = oddsInfo.getHomeWinPercent();
            }

            Double sizeWinPercent = oddsInfo.getSizeWinPercent();
            Double asiaPercent;
            if (isLose) {
                asiaPercent = oddsInfo.getAsiaLosePercent();
            } else {
                asiaPercent = oddsInfo.getAsiaWinPercent();
            }

            setWinRate(homePercent, hostTextView, hostProgress);
            setWinRate(sizeWinPercent, sizeTextView, sizeProgress);
            setWinRate(asiaPercent, asiaTextView, asiaProgress);
        }
    }

    /**
     * 设置单个胜率
     *
     * @param winRate
     * @param textView
     * @param progressBar
     */
    private void setWinRate(Double winRate, TextView textView, ProgressBar progressBar) {
        if (winRate != null) {
            textView.setText(StringFormatUtils.toPercentString(winRate));
            progressBar.setProgress((int) (winRate * 100));
        } else {
            textView.setText("");
        }
    }

    /**
     * 刷新DIY算法UI
     */
    public void refreshFactorUI(boolean ignoreNull) {
        if (mBigDataForecast == null) {
            hideProgress(mHostProgress, mHostAlert);
            hideProgress(mSizeProgress, mSizeAlert);
            hideProgress(mAsiaProgress, mAsiaAlert);
            return;
        }
        BigDataForecastData battleHistory = mBigDataForecast.getBattleHistory();
        if (!ignoreNull) {
            if (battleHistory == null) {
                hideProgress(mHostProgress, mHostAlert);
                hideProgress(mSizeProgress, mSizeAlert);
                hideProgress(mAsiaProgress, mAsiaAlert);
                return;
            }
            if (battleHistory.getHomeWinPercent() != null) {
                showProgress(mHostProgress, mHostAlert, TYPE_HOST);
            } else {
                hideProgress(mHostProgress, mHostAlert);
            }
            if (battleHistory.getSizeWinPercent() != null) {
                showProgress(mSizeProgress, mSizeAlert, TYPE_SIZE);
            } else {
                hideProgress(mSizeProgress, mSizeAlert);
            }
            if (battleHistory.getAsiaWinPercent() != null) {
                showProgress(mAsiaProgress, mAsiaAlert, TYPE_ASIA);
            } else {
                hideProgress(mAsiaProgress, mAsiaAlert);
            }
        } else {
            showProgress(mHostProgress, mHostAlert, TYPE_HOST);
            showProgress(mSizeProgress, mSizeAlert, TYPE_SIZE);
            showProgress(mAsiaProgress, mAsiaAlert, TYPE_ASIA);
        }
    }

    /**
     * 设置百分比
     *
     * @param progressBar
     * @param type        数据类型 0-Host，1-Size，2-Asia
     */
    private void showProgress(RoundProgressBar progressBar, View alertView, int type) {
        double winRate;
        if (type == TYPE_HOST) {
            winRate = mFactor.computeHostWinRate(mBigDataForecast);
        } else if (type == TYPE_SIZE) {
            winRate = mFactor.computeSizeWinRate(mBigDataForecast);
        } else {
            winRate = mFactor.computeAsiaWinRate(mBigDataForecast);
        }
        progressBar.setProgress(winRate * 100);
        progressBar.setTextIsDisplayable(true);
        alertView.setVisibility(View.GONE);
    }

    private void hideProgress(RoundProgressBar progressBar, View alertView) {
        progressBar.setTextIsDisplayable(false);
        alertView.setVisibility(View.VISIBLE);
    }

    public static IntelligenceFragment newInstance() {
        return new IntelligenceFragment();
    }
}
