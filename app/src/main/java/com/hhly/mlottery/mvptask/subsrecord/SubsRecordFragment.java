package com.hhly.mlottery.mvptask.subsrecord;


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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.custom.SubsRecordAdapter;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.mvptask.IContract;
import com.hhly.mlottery.mvptask.data.model.SubsRecordBean;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wangg
 * @desc 订阅记录SubsRecordFragment
 * @date 2017 六一儿童节
 */

public class SubsRecordFragment extends ViewFragment<IContract.ISubsRecordPresenter> implements IContract.IPullLoadMoreDataView, ExactSwipeRefreshLayout.OnRefreshListener {


    private static final String PAGE_SIZE = "10";

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
    SubsRecordAdapter mSubsRecordAdapter;

    ProgressBar progressBar;
    TextView loadmoreText;

    Activity mActivity;

    View moreView;
    @BindView(R.id.refresh)
    ExactSwipeRefreshLayout refresh;

    String userId = "";

    String loginToken = "";

    String sign = "";

    private List<SubsRecordBean.PurchaseRecordsBean.ListBean> listBeanList;


    public static SubsRecordFragment newInstance() {
        SubsRecordFragment subsRecordFragment = new SubsRecordFragment();
        return subsRecordFragment;
    }


    public SubsRecordFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subs_record, container, false);
        moreView = inflater.inflate(R.layout.view_load_more, container, false);
        ButterKnife.bind(this, view);
        initEvent();

        mPresenter.requestData(userId, "1", PAGE_SIZE, loginToken, sign);

        return view;
    }


    private void initEvent() {
        loadmoreText = (TextView) moreView.findViewById(R.id.loadmore_text);
        progressBar = (ProgressBar) moreView.findViewById(R.id.progressBar);
        refresh.setOnRefreshListener(this);
        refresh.setColorSchemeResources(R.color.bg_header);
        refresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }


    @Override
    public IContract.ISubsRecordPresenter initPresenter() {
        return new SubsRecordPresenter(this);
    }

    @Override
    public void loading() {

    }

    @Override
    public void responseData() {

        listBeanList = mPresenter.getSubsRecordData();


        mSubsRecordAdapter = new SubsRecordAdapter(mActivity,listBeanList);
        recyclerView.setAdapter(mSubsRecordAdapter);

        mSubsRecordAdapter.openLoadMore(0, true);
        mSubsRecordAdapter.setLoadingView(moreView);

        mSubsRecordAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.pullUpLoadMoreData();
                    }
                });
            }
        });


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


    @Override
    public void pullUpLoadMoreDataSuccess() {
        loadmoreText.setText(mActivity.getResources().getString(R.string.loading_data_txt));
        progressBar.setVisibility(View.VISIBLE);

        // mList.addAll(foreignInfomationBean.getOverseasInformationList());
        mSubsRecordAdapter.notifyDataChangedAfterLoadMore(true);
    }

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

    @OnClick(R.id.reLoading)
    public void onClick() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = (Activity) context;
    }
}
