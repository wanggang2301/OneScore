package com.hhly.mlottery.frame.basketballframe.basketballteam.basketballteamdata;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketballTeamActivity;
import com.hhly.mlottery.activity.WebActivity;
import com.hhly.mlottery.adapter.BasketBallTeamPlayerAdapter;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.view.RoundProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import data.bean.BasketTeamDataBean;

/**
 * 篮球球员里的球员统计
 * crated by mdy 2017/6/20
 */
public class BasketTeamDataFragment extends ViewFragment<BasketDataContract.Presenter> implements BasketDataContract.View {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private View mView;

    @BindView(R.id.basket_team_rank)
    TextView mRank;
    @BindView(R.id.basket_team_win_lose)
    TextView mRecord;
    @BindView(R.id.basket_team_coach_name)
    TextView mCoach;

    @BindView(R.id.basket_team_score)
    TextView mScore;
    @BindView(R.id.basket_team_assistant)
    TextView mAssistant;
    @BindView(R.id.basket_team_bord)
    TextView mBoard;
    @BindView(R.id.basket_team_lose_score)
    TextView mLose;
    @BindView(R.id.basket_team_misplay)
    TextView mMisplay;

    @BindView(R.id.basket_team_score_rank)
    TextView mScoreRank;
    @BindView(R.id.basket_team_assistant_rank)
    TextView mAssistantRank;
    @BindView(R.id.basket_team_bord_rank)
    TextView mBoardRank;
    @BindView(R.id.basket_team_lose_score_rank)
    TextView mLoseRank;
    @BindView(R.id.basket_team_misplay_rank)
    TextView mMisplayRank;

    @BindView(R.id.basket_team_round_total)
    RoundProgressBar mRoundTotal;
    @BindView(R.id.basket_team_round_home)
    RoundProgressBar mRoundHome;
    @BindView(R.id.basket_team_round_guest)
    RoundProgressBar mRoundGuest;
    @BindView(R.id.total_round_text)
    TextView mRoundTotalText;
    @BindView(R.id.home_round_text)
    TextView mRoundHomeText;
    @BindView(R.id.guest_round_text)
    TextView mRoundGuestText;
    @BindView(R.id.basket_team_description)
    TextView mDescription;
    @BindView(R.id.basket_team_recycler_forward)
    RecyclerView mForwardRecycler;
    @BindView(R.id.basket_team_recycler_center)
    RecyclerView mCenterRecycler;
    @BindView(R.id.basket_team_recycler_defender)
    RecyclerView mDefenderRecycler;
    /**
     * 数据页面
     */
    @BindView(R.id.team_data_layout)
    LinearLayout mDataLayout;
    /**
     * 异常界面
     */
    @BindView(R.id.basket_odds_net_error)
    LinearLayout mExceptionLayout;
    /**
     * 无数据的界面
     */
    @BindView(R.id.nodata)
    LinearLayout mNodataLayout;
    /**
     * 点击刷新
     */
    @BindView(R.id.network_exception_reload_btn)
    TextView mBtnRefresh;
    /**
     * 加载中
     */
    @BindView(R.id.basket_player_progressbar)
    FrameLayout mProgressBarLayout;

    private View mEmptyView;

    private String mSeason;
    private String mLeagueId;
    private String mTeamId;

    private BasketBallTeamPlayerAdapter mForwordAdapter;
    private BasketBallTeamPlayerAdapter mCenterAdapter;
    private BasketBallTeamPlayerAdapter mDefenderAdapter;

    private String mNum="NO.";
    public BasketTeamDataFragment() {
    }

    public static BasketTeamDataFragment newInstance(String season, String leagueId,String teamId) {
        BasketTeamDataFragment fragment = new BasketTeamDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, season);
        args.putString(ARG_PARAM2, leagueId);
        args.putString(ARG_PARAM3, teamId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSeason = getArguments().getString(ARG_PARAM1);
            mLeagueId = getArguments().getString(ARG_PARAM2);
            mTeamId = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public BasketDataContract.Presenter initPresenter() {
        return new BasketDataPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mEmptyView=inflater.inflate(R.layout.layout_nodata,container,false);
        mView=inflater.inflate(R.layout.fragment_basket_team_data, container, false);
        ButterKnife.bind(this,mView);

        mRoundTotal.setCircleColor(ContextCompat.getColor(getActivity(),R.color.snooker_line));
        mRoundTotal.setCircleProgressColor(ContextCompat.getColor(getActivity(),R.color.basket_team_circle));
        mRoundTotal.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_analyze_width));
        mRoundTotal.setTextIsDisplayable(false);
        mRoundTotal.setIsprogress(true);

        mRoundGuest.setCircleColor(ContextCompat.getColor(getActivity(),R.color.snooker_line));
        mRoundGuest.setCircleProgressColor(ContextCompat.getColor(getActivity(),R.color.basket_team_circle));
        mRoundGuest.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_analyze_width));
        mRoundGuest.setTextIsDisplayable(false);
        mRoundGuest.setIsprogress(true);

        mRoundHome.setCircleColor(ContextCompat.getColor(getActivity(),R.color.snooker_line));
        mRoundHome.setCircleProgressColor(ContextCompat.getColor(getActivity(),R.color.basket_team_circle));
        mRoundHome.setRoundWidth(getResources().getDimension(R.dimen.round_progressbar_analyze_width));
        mRoundHome.setTextIsDisplayable(false);
        mRoundHome.setIsprogress(true);

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mNodataLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.GONE);
        mProgressBarLayout.setVisibility(View.GONE);

        mCenterRecycler.setFocusable(false);
        mForwardRecycler.setFocusable(false);
        mDefenderRecycler.setFocusable(false);

        mPresenter.refreshData(mSeason,mLeagueId,mTeamId);

        mForwordAdapter=new BasketBallTeamPlayerAdapter(mPresenter.getForward(),getActivity());
        mCenterAdapter=new BasketBallTeamPlayerAdapter(mPresenter.getCenter(),getActivity());
        mDefenderAdapter=new BasketBallTeamPlayerAdapter(mPresenter.getDefender(),getActivity());

//        mEmptyView=getActivity().getLayoutInflater().inflate(R.layout.layout_nodata, (ViewGroup) mView.getParent(),false);
//
//
        /**不能用同一个emptyview。会报错*/
//        mForwordAdapter.setEmptyView(mEmptyView);
//        mCenterAdapter.setEmptyView(mEmptyView);
//        mDefenderAdapter.setEmptyView(mEmptyView);

        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),2);
        mForwardRecycler.setLayoutManager(layoutManager);
        mCenterRecycler.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mDefenderRecycler.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mForwardRecycler.setAdapter(mForwordAdapter);
        mCenterRecycler.setAdapter(mCenterAdapter);
        mDefenderRecycler.setAdapter(mDefenderAdapter);

        setListener();

    }

    private void setListener() {

        final String url= BaseURLs.P_URL_API_HOST+"data/basket/playerInfo.html"+"?lang="
                + MyApp.getLanguage()+"&playerId=";

        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExceptionLayout.setVisibility(View.GONE);
                mProgressBarLayout.setVisibility(View.VISIBLE);
                mPresenter.refreshData(mSeason,mLeagueId,mTeamId);
            }
        });

        mForwordAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent=new Intent(getActivity(), WebActivity.class);
                intent.putExtra("key",url+mPresenter.getForward().get(i).getPlayerId()+"#/");
                intent.putExtra("noShare",true);
                startActivity(intent);
            }
        });
        mCenterAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent=new Intent(getActivity(), WebActivity.class);
                intent.putExtra("key",url+mPresenter.getCenter().get(i).getPlayerId()+"#/");
                intent.putExtra("noShare",true);
                startActivity(intent);
            }
        });
        mDefenderAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent=new Intent(getActivity(), WebActivity.class);
                intent.putExtra("key",url+mPresenter.getDefender().get(i).getPlayerId()+"#/");
                intent.putExtra("noShare",true);
                startActivity(intent);
            }
        });
    }

    @Override
    public void recyclerNotify() {
        mDataLayout.setVisibility(View.VISIBLE);
        mNodataLayout.setVisibility(View.GONE);
        mProgressBarLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.GONE);
    }

    @Override
    public void setRefresh(boolean refresh) {
        if(getActivity()!=null){
            ((BasketballTeamActivity)getActivity()).setRefresh(refresh);
        }
    }

    /**
     * 父类调用下拉刷新
     */
    public void refreshFragment(String mSeason){
        mPresenter.refreshData(mSeason,mLeagueId,mTeamId);
    }

    @Override
    public void showRankInfo() {
        BasketTeamDataBean.TeamInfoEntity info=mPresenter.getRankInfo();

        if(getActivity()!=null){
            ((BasketballTeamActivity)getActivity()).setHeaderIconAndName(info);
        }

        mRank.setText(info.getAlianceName());
        mRecord.setText(info.getWinTime()+getActivity().getString(R.string.league_statistics_today_victor)+info.getLoseTime()+
                getActivity().getString(R.string.league_statistics_today_loss));
        mCoach.setText(info.getMasterName());
        mScore.setText(info.getAvgScore()+"");
        mScoreRank.setText(mNum+info.getAvgScoreRank());

        mAssistant.setText(info.getAvgHelpattack()+"");
        mAssistantRank.setText(mNum+info.getAvgHelpattackRank());

        mBoard.setText(info.getAvgRebound()+"");
        mBoardRank.setText(mNum+info.getAvgReboundRank());

        mLose.setText(info.getAvgLosescore()+"");
        mLoseRank.setText(mNum+info.getAvgLosescoreRank());

        mMisplay.setText(info.getAvgMisplay()+"");
        mMisplayRank.setText(mNum+info.getAvgMisplayRank());
    }

    @Override
    public void showWinRateDescription() {
        BasketTeamDataBean.TeamInfoEntity.MatchStatEntity rateInfo=mPresenter.getWinRate();
        mRoundTotalText.setText(rateInfo.getTotalPercentage());
        mRoundHomeText.setText(rateInfo.getHomePercentage());
        mRoundGuestText.setText(rateInfo.getGuestPercentage());

        String a[] = rateInfo.getTotalPercentage().split("%");
        String b[] = rateInfo.getHomePercentage().split("%");
        String c[] = rateInfo.getGuestPercentage().split("%");
        int totalPercent = (int)Double.parseDouble(a[0]);
        int homePercent = (int)Double.parseDouble(b[0]);
        int guestPercent = (int)Double.parseDouble(c[0]);
        mRoundTotal.setProgress(totalPercent);
        mRoundHome.setProgress(homePercent);
        mRoundGuest.setProgress(guestPercent);
        setDescription(rateInfo);
    }

    /**
     * 文字描述
     * @param rateInfo
     */
    private void setDescription(BasketTeamDataBean.TeamInfoEntity.MatchStatEntity rateInfo) {

        String text = getActivity().getString(R.string.snooker_state_zong)+": "+rateInfo.getTotal()+getActivity().getString((R.string.filtrate_match_session))+", "+ "<font color='#85c780'><b>" +rateInfo.getTotalWin() + "</b></font>"+ getActivity().getString(R.string.league_statistics_today_victor)
                +  "<font color='#ff2221'><b>" + rateInfo.getTotalLose()+ "</b></font>" + getActivity().getString(R.string.league_statistics_today_loss) +", " +
                getActivity().getString(R.string.basket_database_details_win_offset_title)+ "<font color='#6ca8ff'><b>" + rateInfo.getTotalAbs()+ "</b></font>" + getActivity().getString((R.string.filtrate_match_session))+
                ","
                +getActivity().getString(R.string.basket_database_details_home)+rateInfo.getTotalHome()+getActivity().getString((R.string.filtrate_match_session))+","+ "<font color='#85c780'><b>" +rateInfo.getHomeWin() + "</b></font>"+ getActivity().getString(R.string.league_statistics_today_victor)
                +  "<font color='#ff2221'><b>" + rateInfo.getHomeLose()+ "</b></font>" + getActivity().getString(R.string.league_statistics_today_loss) +", " +
                getActivity().getString(R.string.basket_database_details_win_offset_title)+ "<font color='#6ca8ff'><b>" + rateInfo.getHomeAbs()+ "</b></font>" + getActivity().getString((R.string.filtrate_match_session))+", "+

                getActivity().getString(R.string.basket_database_details_guest)+rateInfo.getTotalGuest()+getActivity().getString((R.string.filtrate_match_session))+","+ "<font color='#85c780'><b>" +rateInfo.getGuestWin() + "</b></font>"+ getActivity().getString(R.string.league_statistics_today_victor)
                +  "<font color='#ff2221'><b>" + rateInfo.getGuestLose()+ "</b></font>" + getActivity().getString(R.string.league_statistics_today_loss) +", " +
                getActivity().getString(R.string.basket_database_details_win_offset_title)+ "<font color='#6ca8ff'><b>" + rateInfo.getGuestAbs()+ "</b></font>" + getActivity().getString((R.string.filtrate_match_session))
                ;
        mDescription.setText(Html.fromHtml(text));

    }

    @Override
    public void showForwardPlayer() {
        mForwordAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCenterPlayer() {
        mCenterAdapter.notifyDataSetChanged();
    }

    @Override
    public void showDefender() {
        mDefenderAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoData() {
        mDataLayout.setVisibility(View.GONE);
        mNodataLayout.setVisibility(View.VISIBLE);
        mProgressBarLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.GONE);
    }

    @Override
    public void onError() {
        mDataLayout.setVisibility(View.GONE);
        mNodataLayout.setVisibility(View.GONE);
        mProgressBarLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.VISIBLE);
    }
}
