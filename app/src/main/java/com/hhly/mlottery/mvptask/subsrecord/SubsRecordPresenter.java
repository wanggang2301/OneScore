package com.hhly.mlottery.mvptask.subsrecord;

import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.mvptask.IContract;
import com.hhly.mlottery.mvptask.data.model.SubsRecordBean;
import com.hhly.mlottery.mvptask.data.repository.Repository;

import java.util.List;

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
                mView.onError();
            }

            @Override
            public void onNext(SubsRecordBean subsRecordBean) {
                if (!"200".equals(subsRecordBean.getCode())) {
                    return;
                }
                listBeanList = subsRecordBean.getPurchaseRecords().getList();
                mView.responseData();
            }
        });
    }


    @Override
    public void pullUpLoadMoreData() {
        mView.pullUpLoadMoreDataFail();

       /* addSubscription(repository.getSubsRecord(), new Subscriber<String>() {
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
        });*/

    }

    @Override
    public List<SubsRecordBean.PurchaseRecordsBean.ListBean> getSubsRecordData() {
        return listBeanList;
    }
}
