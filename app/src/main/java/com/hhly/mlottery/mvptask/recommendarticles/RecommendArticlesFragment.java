package com.hhly.mlottery.mvptask.recommendarticles;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.custom.RecommendArticlesAdapter;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.mvp.bettingmvp.mvpview.MvpBettingIssueDetailsActivity;
import com.hhly.mlottery.mvptask.IContract;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import data.bean.RecommendArticlesBean;

/**
 * @anthor     wangg
 * @className  RecommendArticlesFragment
 * @time       2017/6/12 16:07
 * @changeDesc XXX
 * @changeTime XXX
 * @classDesc  推介文章
 */
public class RecommendArticlesFragment extends ViewFragment<IContract.IRecommendArticlesPresenter> implements IContract.IPullLoadMoreDataView, ExactSwipeRefreshLayout.OnRefreshListener {

    private static final String PAGE_SIZE = "10";
    private static final String SIGN_FLAG = "sign"; //是否需要签名标记

    @BindView(R.id.fl_loading)
    FrameLayout flLoading;
    @BindView(R.id.reLoading)
    TextView reLoading;
    @BindView(R.id.fl_networkError)
    FrameLayout flNetworkError;
    @BindView(R.id.fl_nodata)
    RelativeLayout flNodata;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.handle_exception)
    LinearLayout handleException;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.refresh)
    ExactSwipeRefreshLayout refresh;
    @BindView(R.id.tv_nodata)
    TextView tvNodata;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;


    RecommendArticlesAdapter mRecommendArticlesAdapter;
    ProgressBar progressBar;
    TextView loadmoreText;
    Activity mActivity;
    View moreView;

    String userId;
    String loginToken;
    int pageNum = 1;
    private List<RecommendArticlesBean.PublishPromotionsBean.ListBean> listBeanList;

    public static RecommendArticlesFragment newInstance() {
        RecommendArticlesFragment recommendArticlesFragment = new RecommendArticlesFragment();
        return recommendArticlesFragment;
    }

    public RecommendArticlesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_articles, container, false);
        moreView = inflater.inflate(R.layout.view_load_more, container, false);
        ButterKnife.bind(this, view);
        initEvent();
        userId = AppConstants.register.getUser().getUserId();
        loginToken = AppConstants.register.getToken();

        mPresenter.requestData(userId, String.valueOf(pageNum), PAGE_SIZE, loginToken, SIGN_FLAG);
        return view;
    }


    private void initEvent() {
        loadmoreText = (TextView) moreView.findViewById(R.id.loadmore_text);
        progressBar = (ProgressBar) moreView.findViewById(R.id.progressBar);
        //先隐藏掉
        btnConfirm.setVisibility(View.GONE);
        tvNodata.setText(mActivity.getResources().getString(R.string.tuijie_nodata_txt));
        refresh.setOnRefreshListener(this);
        refresh.setColorSchemeResources(R.color.bg_header);
        refresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    @Override
    public IContract.IRecommendArticlesPresenter initPresenter() {
        return new RecommendArticlesPresenter(this);
    }


    /**
     * @desc 请求load
     * @@time 2017/6/12 15:51
     */
    @Override
    public void loading() {
        handleException.setVisibility(View.GONE);
        refresh.setVisibility(View.VISIBLE);
        refresh.setRefreshing(true);
    }

    /**
     * @desc 请求成功返回数据
     * @@time 2017/6/12 15:51
     */
    @Override
    public void responseData() {
        handleException.setVisibility(View.GONE);
        flNetworkError.setVisibility(View.GONE);
        flNodata.setVisibility(View.GONE);
        refresh.setVisibility(View.VISIBLE);
        refresh.setRefreshing(false);

        listBeanList = new ArrayList<>();
        listBeanList.addAll(mPresenter.getRecommendArticlesData());

        mRecommendArticlesAdapter = new RecommendArticlesAdapter(mActivity, listBeanList);
        recyclerView.setAdapter(mRecommendArticlesAdapter);
        mRecommendArticlesAdapter.openLoadMore(0, true);
        mRecommendArticlesAdapter.setLoadingView(moreView);
        mRecommendArticlesAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        pageNum += 1;
                        mPresenter.pullUpLoadMoreData(userId, String.valueOf(pageNum), PAGE_SIZE, loginToken, SIGN_FLAG);

                    }
                });
            }
        });
    }

    //请求没有数据处理
    @Override
    public void noData() {
        handleException.setVisibility(View.VISIBLE);
        flNetworkError.setVisibility(View.GONE);
        flNodata.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.GONE);
        refresh.setRefreshing(false);

    }

    //请求错误处理
    @Override
    public void onError() {
        handleException.setVisibility(View.VISIBLE);
        flNetworkError.setVisibility(View.VISIBLE);
        flNodata.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
        refresh.setRefreshing(false);

    }

    //下拉刷新处理
    @Override
    public void onRefresh() {
        refresh.setRefreshing(true);
        pageNum = 1;
        mPresenter.requestData(userId, String.valueOf(pageNum), PAGE_SIZE, loginToken, SIGN_FLAG);
    }

    //下拉记载load
    @Override
    public void pullUploadingView() {
        loadmoreText.setText(mActivity.getResources().getString(R.string.loading_data_txt));
        progressBar.setVisibility(View.VISIBLE);
    }

    //下拉加载成功
    @Override
    public void pullUpLoadMoreDataSuccess() {
        listBeanList.addAll(mPresenter.getRecommendArticlesData());
        mRecommendArticlesAdapter.notifyDataChangedAfterLoadMore(true);
    }

    //下拉加载失败
    @Override
    public void pullUpLoadMoreDataFail() {
        loadmoreText.setText(mActivity.getResources().getString(R.string.nodata_txt));
        progressBar.setVisibility(View.GONE);
    }

    //防止Activity内存泄漏
    @Override
    public boolean isActive() {
        return isAdded();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }


    @OnClick({R.id.iv_back, R.id.reLoading, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                mActivity.finish();
                break;
            case R.id.reLoading:
                pageNum = 1;
                mPresenter.requestData(userId, String.valueOf(pageNum), PAGE_SIZE, loginToken, SIGN_FLAG);
                break;
            case R.id.btn_confirm:
                startActivity(new Intent(mActivity, MvpBettingIssueDetailsActivity.class));
                mActivity.finish();
                break;
        }
    }
}