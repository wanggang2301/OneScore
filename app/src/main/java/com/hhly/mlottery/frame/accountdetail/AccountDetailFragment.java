package com.hhly.mlottery.frame.accountdetail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MDStatusBarCompat;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;
import com.hhly.mlottery.widget.PullToEnlargeCoorDinatorLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个人中心账户详情
 * created by mdy155 on 2017/6/3
 */
public class AccountDetailFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

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
    FrameLayout mProgressBarLayout;

    @BindView(R.id.available_balance)
    TextView mAvailableBalance;

    @BindView(R.id.frozen_balance)
    TextView mFrozenBalance;

    @BindView(R.id.total_balance)
    TextView mTotalBalance;

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
//        mCoordinatorLayout.setZoomView(mLayoutHeader);
        MDStatusBarCompat.setCollapsingToolbar(getActivity(), mCoordinatorLayout, mAppBarLayout, mLayoutHeader, mToolbar);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//        int mTouchTitleDistance=mCollapsingToolbarLayout.getHeight()/6; //触碰到
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
//        mCoordinatorLayout.setOffSet(verticalOffset);

//        L.e( "verticalOffset-----"+verticalOffset+"");
//        L.e("mccc-----"+mCollapsingToolbarLayout.getHeight());
//        L.e("mapp====="+mAppBarLayout.getHeight());
//        L.e("header====="+mLayoutHeader.getMeasuredHeight());
    }
}
