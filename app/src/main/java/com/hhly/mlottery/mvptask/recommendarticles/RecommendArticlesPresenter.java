package com.hhly.mlottery.mvptask.recommendarticles;

import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.mvptask.IContract;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.DeviceInfo;

import java.util.List;

import data.bean.RecommendArticlesBean;
import data.repository.UserCenterRepository;
import rx.Subscriber;

/**
 * @anthor wangg
 * @className RecommendArticlesPresenter
 * @time 2017/6/2  11:19
 * @changeDesc XXX
 * @changeTime XXX
 * @classDesc 推介文章presenter
 */

public class RecommendArticlesPresenter extends BasePresenter<IContract.IPullLoadMoreDataView> implements IContract.IRecommendArticlesPresenter {

    private UserCenterRepository userCenterRepository;

    private List<RecommendArticlesBean.PublishPromotionsBean.ListBean> list;
    private boolean isHasNextPage = false;


    public RecommendArticlesPresenter(IContract.IPullLoadMoreDataView view) {
        super(view);
        userCenterRepository = mDataManager.userCenterRepository;
    }


    @Override
    public void requestData(String userId, String pageNum, String pageSize, String loginToken, String sign) {

        if (!mView.isActive()) {
            return;
        }

        mView.loading();

        addSubscription(userCenterRepository.getRecommendArtices(userId, pageNum, pageSize, loginToken, sign), new Subscriber<RecommendArticlesBean>() {
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
                    DeviceInfo.handlerRequestResult(Integer.parseInt(r.getCode()), "code");
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

        if (!isHasNextPage) {
            mView.pullUpLoadMoreDataFail();
            return;
        }

        mView.pullUploadingView();

        addSubscription(userCenterRepository.getRecommendArtices(userId, pageNum, pageSize, loginToken, sign), new Subscriber<RecommendArticlesBean>() {
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