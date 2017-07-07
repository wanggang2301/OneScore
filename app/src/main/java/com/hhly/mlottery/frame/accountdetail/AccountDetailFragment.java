package com.hhly.mlottery.frame.accountdetail;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.account.AccountDetailAdapter;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MDStatusBarCompat;
import com.hhly.mlottery.util.net.UnitsUtil;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;
import com.hhly.mlottery.widget.PullToEnlargeCoorDinatorLayout;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import data.bean.AccountDetailBean;
import data.bean.RechargeBean;

/**
 * 个人中心账户详情
 * created by mdy155 on 2017/6/3
 */
public class AccountDetailFragment extends ViewFragment<AccountDetailContract.Presenter> implements AccountDetailContract.View,AppBarLayout.OnOffsetChangedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View mView;
    @BindView(R.id.account_detail_coordinator)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.account_detail_collaspsing)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.account_detail_appbarlayout)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.account_detail_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.account_detail_header_layout)
    LinearLayout mLayoutHeader;

    @BindView(R.id.account_detail_refresh)
    ExactSwipeRefreshLayout mRefreshLayout;

    /**
     * 异常界面
     */
    @BindView(R.id.basket_odds_net_error)
    LinearLayout mExceptionLayout;
    /**
     * 无数据的界面
     */
    @BindView(R.id.nodata)
    TextView mNodataLayout;
    /**
     * 点击刷新
     */
    @BindView(R.id.network_exception_reload_btn)
    TextView mBtnRefresh;
    /**
     * 加载中
     */
    @BindView(R.id.basket_player_progressbar)
    LinearLayout mProgressBarLayout;

    @BindView(R.id.available_balance)
    TextView mAvailableBalance;

    @BindView(R.id.frozen_balance)
    TextView mFrozenBalance;

    @BindView(R.id.total_balance)
    TextView  mTotalBalance;

    @BindView(R.id.account_detail_recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.account_detaiL_back)
    ImageView mBack;
    private View mNoLoadingView; //没有更多
    private View mOnloadingView; //加载更多

    @BindView(R.id.account_error_layout)
    NestedScrollView mScrollView;

    private AccountDetailAdapter mAdapter;

    public AccountDetailFragment() {
    }
    public static AccountDetailFragment newInstance(String param1, String param2) {
        AccountDetailFragment fragment = new AccountDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public AccountDetailContract.Presenter initPresenter() {
        return new AccountDetailPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_account_detail, container, false);

        ButterKnife.bind(this,mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAppBarLayout.addOnOffsetChangedListener(this);
        MDStatusBarCompat.setCollapsingToolbar(getActivity(), mCoordinatorLayout, mAppBarLayout, mLayoutHeader, mToolbar);

        mNodataLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.GONE);
        mProgressBarLayout.setVisibility(View.GONE);
        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExceptionLayout.setVisibility(View.GONE);
                mProgressBarLayout.setVisibility(View.VISIBLE);
                mPresenter.refreshData();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null){
                    getActivity().finish();
                }
            }
        });
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });

        mPresenter.refreshData(); //请求数据

        if(mAdapter==null){
            mAdapter=new AccountDetailAdapter(mPresenter.getListData(),getActivity());
        }
        mOnloadingView=getActivity().getLayoutInflater().inflate(R.layout.onloading, (ViewGroup) mRecyclerView.getParent(),false);
        mAdapter.setLoadingView(mOnloadingView);
//        mAdapter.setPageSize(PAGE_SIZE);
        mAdapter.openLoadMore(mPresenter.getPage().size,true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.refreshDataByPage();
                    }
                },1000);
            }
        });

        mRefreshLayout.setColorSchemeResources(R.color.bg_header);


        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                        mPresenter.refreshData();
                    }
                }, 1000);
            }
        });



    }


    @Override
    public void recyclerNotify() {
        mScrollView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.setNewData(mPresenter.getListData()); //点进去看下
        mAdapter.setLoadingView(mOnloadingView);
        mAdapter.openLoadMore(mPresenter.getPage().size,true);
        mNodataLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.GONE);
        mProgressBarLayout.setVisibility(View.GONE);
    }

    @Override
    public void showNoData() {
        mScrollView.setVisibility(View.VISIBLE);
        mNodataLayout.setVisibility(View.VISIBLE);
        mExceptionLayout.setVisibility(View.GONE);
        mProgressBarLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void setRefresh(boolean refresh) {
        mRefreshLayout.setRefreshing(refresh);
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
    public void showNextPage(List<RechargeBean> recordEntity) {
        mAdapter.notifyDataChangedAfterLoadMore(recordEntity,true);
    }

    @Override
    public void showBalance() {
        mAvailableBalance.setText(UnitsUtil.fenToYuan(mPresenter.getBalanceData().getAvailableBalance()));
        mFrozenBalance.setText(UnitsUtil.fenToYuan(mPresenter.getBalanceData().getBlockedBalance()));
        mTotalBalance.setText(UnitsUtil.fenToYuan(mPresenter.getBalanceData().getTotalAmount()));
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if(-verticalOffset<100&&-verticalOffset>85){
            mLayoutHeader.setAlpha((100-(-verticalOffset))/100f);
        }
        if(-verticalOffset<85){
            mLayoutHeader.setAlpha(1);
        }

        if (mCollapsingToolbarLayout.getHeight() + verticalOffset < mLayoutHeader.getHeight()) {
            mRefreshLayout.setEnabled(false);
        } else {
            mRefreshLayout.setEnabled(true);
        }

    }

    @Override
    public void onError() {
        mScrollView.setVisibility(View.VISIBLE);
        mNodataLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.VISIBLE);
        mProgressBarLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mRefreshLayout.setRefreshing(false);
    }

}
