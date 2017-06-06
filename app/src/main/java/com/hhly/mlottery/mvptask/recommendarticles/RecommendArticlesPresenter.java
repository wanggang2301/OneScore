package com.hhly.mlottery.mvptask.recommendarticles;

import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.mvptask.IContract;
import com.hhly.mlottery.util.CollectionUtils;

import java.util.List;

import data.model.RecommendArticlesBean;
import data.repository.Repository;
import rx.Subscriber;

/**
 * @author: Wangg
 * @name：xxx
 * @description: 推介文章presenter
 * @created on:2017/6/2  11:19.
 */

public class RecommendArticlesPresenter extends BasePresenter<IContract.IPullLoadMoreDataView> implements IContract.IRecommendArticlesPresenter {

    private Repository repository;

    private List<RecommendArticlesBean.PublishPromotionsBean.ListBean> list;
    private boolean isHasNextPage = false;


    public RecommendArticlesPresenter(IContract.IPullLoadMoreDataView view) {
        super(view);
        repository = mDataManager.repository;
    }


    @Override
    public void requestData(String userId, String pageNum, String pageSize, String loginToken, String sign) {

        if (!mView.isActive()) {
            return;
        }

        mView.loading();

        addSubscription(repository.getRecommendArtices(userId, pageNum, pageSize, loginToken, sign), new Subscriber<RecommendArticlesBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.onError();

            }

            @Override
            public void onNext(RecommendArticlesBean r) {
                if (!"200".equals(r.getCode())) {
                    mView.onError();
                    return;
                }

                if (!CollectionUtils.notEmpty(r.getPublishPromotions().getList())) {
                    mView.noData();
                    return;
                }

                isHasNextPage = r.getPublishPromotions().isHasNextPage();
                list = r.getPublishPromotions().getList();
                mView.responseData();
            }
        });
    }

    @Override
    public void pullUpLoadMoreData(String userId, String pageNum, String pageSize, String loginToken, String sign) {


        //if (!isHasNextPage) {
        if (false) {
            mView.pullUpLoadMoreDataFail();
            return;
        }

        mView.pullUploadingView();

        addSubscription(repository.getRecommendArtices(userId, pageNum, pageSize, loginToken, sign), new Subscriber<RecommendArticlesBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.pullUpLoadMoreDataFail();

            }

            @Override
            public void onNext(RecommendArticlesBean r) {
                if (!"200".equals(r.getCode())) {
                    mView.pullUpLoadMoreDataFail();
                    return;
                }

                isHasNextPage = r.getPublishPromotions().isHasNextPage();
                list = r.getPublishPromotions().getList();
                mView.pullUpLoadMoreDataSuccess();
            }
        });
    }


    @Override
    public List<RecommendArticlesBean.PublishPromotionsBean.ListBean> getRecommendArticlesData() {
        return list;
    }
}