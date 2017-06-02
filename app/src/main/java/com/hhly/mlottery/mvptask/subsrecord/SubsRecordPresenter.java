package com.hhly.mlottery.mvptask.subsrecord;

import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.mvptask.IContract;
import com.hhly.mlottery.mvptask.data.repository.Repository;

import rx.Subscriber;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/6/1  19:13.
 */

public class SubsRecordPresenter extends BasePresenter<IContract.IPullLoadMoreDataView> implements IContract.ISubsRecordPresenter {

    private Repository repository;

    public SubsRecordPresenter(IContract.IPullLoadMoreDataView view) {
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
            }
        });

    }

    @Override
    public String getSubsRecordData() {
        return null;
    }
}