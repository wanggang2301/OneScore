package com.hhly.mlottery.mvptask.recommendarticles;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.mvptask.IContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendArticlesFragment extends ViewFragment<IContract.IRecommendArticlesPresenter> implements IContract.IChildView {


    @BindView(R.id.iv_back)
    ImageView ivBack;
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


    Activity mActivity;

    RecommendArticlesAdapter mRecommendArticlesAdapter;

    public static RecommendArticlesFragment newInstance() {
        RecommendArticlesFragment recommendArticlesFragment = new RecommendArticlesFragment();
        return recommendArticlesFragment;
    }


    public RecommendArticlesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recommend_articles, container, false);
        ButterKnife.bind(this, view);

        //mPresenter.requestData();
        initEvent();

        return view;
    }


    private void initEvent() {

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        List<String> list = new ArrayList<>();

        list.add("ss");
        list.add("ss");
        list.add("ss");
        list.add("ss");
        list.add("ss");
        list.add("ss");

        mRecommendArticlesAdapter = new RecommendArticlesAdapter(R.layout.recommend_articles_item, list);
        recyclerView.setAdapter(mRecommendArticlesAdapter);
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


        initEvent();

    }


    @Override
    public void noData() {

    }

    @Override
    public void onError() {

    }


    //防止Activity内存泄漏
    @Override
    public boolean isActive() {
        return isAdded();
    }

    @OnClick(R.id.reLoading)
    public void onClick() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = (Activity) context;
    }
}