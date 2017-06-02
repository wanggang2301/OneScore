package com.hhly.mlottery.mvptask.bowl;

import com.hhly.mlottery.bean.footballDetails.BottomOddsDetails;
import com.hhly.mlottery.mvptask.IContract;
import com.hhly.mlottery.mvptask.data.repository.Repository;
import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.util.CollectionUtils;

import rx.Observable;
import rx.Subscriber;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/5/27  12:22.
 */

public class BowlChildPresenter extends BasePresenter<IContract.IChildView> implements IContract.IBowlChildPresenter {

    private Repository repository;
    private BottomOddsDetails bowlBeanList;

    public BowlChildPresenter(IContract.IChildView view) {
        super(view);
        repository = mDataManager.repository;
    }


    @Override
    public void requestData(String thirdId, String oddType) {

        if (!mView.isActive()) {
            return;
        }

        mView.loading();

        Observable<BottomOddsDetails> observable = repository.getBowlList(thirdId, oddType);

        addSubscription(observable, new Subscriber<BottomOddsDetails>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.onError();
            }

            @Override
            public void onNext(BottomOddsDetails bowlBean) {

                if (!"200".equals(bowlBean.getResult())) {//请求不为200 error处理
                    mView.onError();
                    return;
                }

                if (!CollectionUtils.notEmpty(bowlBean.getMatchoddlist())) { //请求为空 nodata处理
                    mView.noData();
                    return;
                }

                bowlBeanList = bowlBean;
                mView.responseData(); //请求成功处理

                // L.d("xxccvvbb", "______" + basketIndex.getData().getAllInfo().get(0).getHomeTeam());
            }
        });
    }

    @Override
    public BottomOddsDetails getBowlBean() {
        return bowlBeanList;
    }
}