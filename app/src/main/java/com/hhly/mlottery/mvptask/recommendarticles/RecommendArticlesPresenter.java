package com.hhly.mlottery.mvptask.recommendarticles;

import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.mvptask.IContract;
import com.hhly.mlottery.mvptask.data.model.RecommendArticlesBean;
import com.hhly.mlottery.mvptask.data.repository.Repository;

import java.util.List;

import rx.Subscriber;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/6/2  11:19.
 */

public class RecommendArticlesPresenter extends BasePresenter<IContract.IChildView> implements IContract.IRecommendArticlesPresenter {

    private Repository repository;

    private List<RecommendArticlesBean.PublishPromotionsBean.ListBean> list;


    public RecommendArticlesPresenter(IContract.IChildView view) {
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
                    return;
                }

                list = r.getPublishPromotions().getList();
            }
        });
    }


    @Override
    public List<RecommendArticlesBean.PublishPromotionsBean.ListBean> getRecommendArticlesData() {
        return list;
    }
}