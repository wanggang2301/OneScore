package com.hhly.mlottery.frame.basketballframe.basketballteam.resultschedule;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketballTeamActivity;
import com.hhly.mlottery.adapter.basketball.BasketTeamResultAdapter;
import com.hhly.mlottery.mvp.ViewFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import data.bean.BasketTeamResultBean;

/**
 * 篮球球队赛程赛果
 * created by mdy on 2017/06/20
 */
public class BasketTeamResultFragment extends ViewFragment<BasketResultContract.Presenter> implements BasketResultContract.View {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private String mSeason;
    private String mLeagueId;
    private String mTeamId;

    private String mParam1;
    private String mParam2;

    private View mView;
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

    @BindView(R.id.basket_team_result_recycler)
    RecyclerView mRecyclerView;

    private View mNoLoadingView; //没有更多
    private View mOnloadingView; //加载更多

    private BasketTeamResultAdapter mAdapter;


    public BasketTeamResultFragment() {
    }

    public static BasketTeamResultFragment newInstance(String season, String leagueId,String teamId) {
        BasketTeamResultFragment fragment = new BasketTeamResultFragment();
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
    public BasketResultContract.Presenter initPresenter() {
        return new BasketResultPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_basket_team_result, container, false);
        ButterKnife.bind(this,mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNodataLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.GONE);
        mProgressBarLayout.setVisibility(View.GONE);

        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExceptionLayout.setVisibility(View.GONE);
                mProgressBarLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                mPresenter.refreshData(mSeason,mLeagueId,mTeamId );
            }
        });

        mPresenter.refreshData(mSeason,mLeagueId,mTeamId);

        if(mAdapter==null){
            mAdapter=new BasketTeamResultAdapter(mPresenter.getListData(),getActivity());
        }
        mOnloadingView=getActivity().getLayoutInflater().inflate(R.layout.onloading, (ViewGroup) mRecyclerView.getParent(),false);
        mAdapter.setLoadingView(mOnloadingView);

        mAdapter.openLoadMore(mPresenter.getPage().size,true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void recyclerNotify() {

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.refreshDataByPage(mSeason,mLeagueId,mTeamId);
                    }
                },1000);
            }
        });
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.setNewData(mPresenter.getListData()); //点进去看下
        mAdapter.setLoadingView(mOnloadingView);
        mAdapter.openLoadMore(mPresenter.getPage().size,true);
        mProgressBarLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.GONE);
    }

    @Override
    public void showNoData() {
        mNodataLayout.setVisibility(View.VISIBLE);
        mProgressBarLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void setRefresh(boolean refresh) {
        if(getActivity()!=null){
            ((BasketballTeamActivity)getActivity()).setRefresh(refresh);
        }
    }

    @Override
    public void showNoMoreData() {
        if(mNoLoadingView==null){
            mNoLoadingView=getActivity().getLayoutInflater().inflate(R.layout.nomoredata, (ViewGroup) mRecyclerView.getParent(),false);
        }
        mAdapter.notifyDataChangedAfterLoadMore(false);
        mAdapter.addFooterView(mNoLoadingView);
    }

    @Override
    public void showNextPage(List<BasketTeamResultBean.TeamMatchDataEntity> recordEntity) {
        mAdapter.notifyDataChangedAfterLoadMore(recordEntity,true);
    }

    /**
     * 父类调用下拉刷新
     */
    public void refreshFragment(String mSeason){
        this.mSeason=mSeason;
        mPresenter.refreshData(mSeason,mLeagueId,mTeamId);
    }


    @Override
    public void onError() {
        mRecyclerView.setVisibility(View.GONE);
        mNodataLayout.setVisibility(View.GONE);
        mProgressBarLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.VISIBLE);
    }
}
