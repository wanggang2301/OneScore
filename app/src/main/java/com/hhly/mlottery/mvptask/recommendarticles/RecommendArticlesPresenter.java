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

public class RecommendArticlesPresenter extends BasePresenter<IContract.IChildView> implements IContract.IRecommendArticlesPresenter {

    private Repository repository;

    public RecommendArticlesPresenter(IContract.IChildView view) {
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
    public String getRecommendArticlesData() {
        return null;
    }
}