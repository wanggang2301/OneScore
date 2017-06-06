package com.hhly.mlottery.mvptask.subsrecord;

import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.mvptask.IContract;

import java.util.List;

import data.model.SubsRecordBean;
import data.repository.Repository;
import rx.Subscriber;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/6/1  19:13.
 */

public class SubsRecordPresenter extends BasePresenter<IContract.IPullLoadMoreDataView> implements IContract.ISubsRecordPresenter {

    private Repository repository;
    private List<SubsRecordBean.PurchaseRecordsBean.ListBean> listBeanList;

    public SubsRecordPresenter(IContract.IPullLoadMoreDataView view) {
        super(view);
        repository = mDataManager.repository;
    }


    @Override
    public void requestData(String userId, String pageNum, String pageSize, String loginToken, String sign) {
        if (!mView.isActive()) {
            return;
        }

        mView.loading();

        addSubscription(repository.getSubsRecord(userId, pageNum, pageSize, loginToken, sign), new Subscriber<SubsRecordBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.loading();
            }

            @Override
            public void onNext(SubsRecordBean subsRecordBean) {
                if (!"200".equals(subsRecordBean.getCode())) {
                    mView.loading();
                    return;
                }
                listBeanList = subsRecordBean.getPurchaseRecords().getList();
                mView.responseData();
            }
        });
    }


    @Override
    public void pullUpLoadMoreData(String userId, String pageNum, String pageSize, String loginToken, String sign) {

    }

    @Override
    public List<SubsRecordBean.PurchaseRecordsBean.ListBean> getSubsRecordData() {
        return listBeanList;
    }
}
