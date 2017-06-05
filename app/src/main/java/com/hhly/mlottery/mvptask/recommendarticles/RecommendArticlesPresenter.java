package com.hhly.mlottery.mvptask.recommendarticles;

import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.mvptask.IContract;
import com.hhly.mlottery.mvptask.data.repository.Repository;

import rx.Subscriber;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/6/2  11:19.
 */

public class RecommendArticlesPresenter extends BasePresenter<IContract.IPullLoadMoreDataView> implements IContract.IRecommendArticlesPresenter {

    private Repository repository;

    public RecommendArticlesPresenter(IContract.IPullLoadMoreDataView view) {
        super(view);
        repository = mDataManager.repository;
    }


    @Override
    public void requestData() {

        if (!mView.isActive()) {
            return;
        }

        mView.loading();

        addSubscription(repository.getSubsRecord(), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.onError();

            }

            @Override
            public void onNext(String s) {

            }
        });
    }

    @Override
    public void pullUpLoadMoreData() {
        addSubscription(repository.getSubsRecord(), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {


                if (!"".equals("200")) {

                    mView.pullUpLoadMoreDataFail();

                    return;
                } else {

                    mView.pullUpLoadMoreDataSuccess();

                }

/*

                if (!foreignInfomationBean.getResult().equals("200")) {
                    loadmore_text.setText(mContext.getResources().getString(R.string.nodata_txt));
                    progressBar.setVisibility(View.GONE);
                    return;
                } else {
                    loadmore_text.setText(mContext.getResources().getString(R.string.loading_data_txt));
                    progressBar.setVisibility(View.VISIBLE);
                }
                mList.addAll(foreignInfomationBean.getOverseasInformationList());
                foreignInfomationAdapter.notifyDataChangedAfterLoadMore(true);
*/

            }
        });
    }

    @Override
    public String getRecommendArticlesData() {
        return null;
    }
}