package com.hhly.mlottery.mvptask.recommendarticles;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.custom.RecommendArticlesAdapter;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.mvptask.IContract;
import com.hhly.mlottery.mvptask.data.model.RecommendArticlesBean;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wangg
 * @desc 推介文章
 * @date 2017/06/02
 */
public class RecommendArticlesFragment extends ViewFragment<IContract.IRecommendArticlesPresenter> implements IContract.IChildView, ExactSwipeRefreshLayout.OnRefreshListener {

    private static final String PAGE_SIZE = "10";

    @BindView(R.id.fl_loading)
    FrameLayout flLoading;
    @BindView(R.id.reLoading)
    TextView reLoading;
    @BindView(R.id.fl_networkError)
    FrameLayout flNetworkError;
    @BindView(R.id.fl_nodata)
    FrameLayout flNodata;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.handle_exception)
    LinearLayout handleException;
    RecommendArticlesAdapter mRecommendArticlesAdapter;

    /*
        ProgressBar progressBar;
        TextView loadmoreText;*/
    Activity mActivity;
    View moreView;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.refresh)
    ExactSwipeRefreshLayout refresh;


    String userId = "HHLY00000136";

    String loginToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJqd3QiLCJpYXQiOjE0OTY0ODU2MDAsInN1YiI6IntcImlkXCI6XCJISExZMDAwMDAxMzZcI" +
            "ixcInBob25lTnVtXCI6XCIxNTAxMzY5NzEwMVwifSJ9.l4jsTaz5tJM5Q4P3s_UK8US-S3HRfN-lfJZJ67XUS98";

    String sign = "70db4a00262f0a351a320c668437493612";

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
        // moreView = inflater.inflate(R.layout.view_load_more, container, false);
        ButterKnife.bind(this, view);
        initEvent();

        mPresenter.requestData(userId, "1", PAGE_SIZE, loginToken, sign);
        return view;
    }


    private void initEvent() {
        /*loadmoreText = (TextView) moreView.findViewById(R.id.loadmore_text);
        progressBar = (ProgressBar) moreView.findViewById(R.id.progressBar);*/
        refresh.setOnRefreshListener(this);
        refresh.setColorSchemeResources(R.color.bg_header);
        refresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

    }


    @Override
    public IContract.IRecommendArticlesPresenter initPresenter() {
        return new RecommendArticlesPresenter(this);
    }

    @Override
    public void loading() {

    }

    @Override
    public void responseData() {
        listBeanList = mPresenter.getRecommendArticlesData();
        mRecommendArticlesAdapter = new RecommendArticlesAdapter(listBeanList);
        recyclerView.setAdapter(mRecommendArticlesAdapter);
    }


    @Override
    public void noData() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onRefresh() {
        refresh.setRefreshing(false);
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


    @OnClick({R.id.iv_back, R.id.reLoading})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.reLoading:
                break;
        }
    }
}