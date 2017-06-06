package com.hhly.mlottery.mvptask.subsrecord;

import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.mvptask.IContract;

import java.util.List;

import data.model.SubsRecordBean;
import data.repository.UserCenterRepository;
import rx.Subscriber;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/6/1  19:13.
 */

public class SubsRecordPresenter extends BasePresenter<IContract.IPullLoadMoreDataView> implements IContract.ISubsRecordPresenter {

    private UserCenterRepository userCenterRepository;
    private List<SubsRecordBean.PurchaseRecordsBean.ListBean> listBeanList;

    public SubsRecordPresenter(IContract.IPullLoadMoreDataView view) {
        super(view);
        userCenterRepository = mDataManager.userCenterRepository;
    }


    @Override
    public void requestData(String userId, String pageNum, String pageSize) {

        if (!mView.isActive()) {
            return;
        }

        mView.loading();

        addSubscription(userCenterRepository.getSubsRecord(userId, pageNum, pageSize), new Subscriber<SubsRecordBean>() {
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


            }
        });
    }


    @Override
    public void pullUpLoadMoreData() {
        mView.pullUpLoadMoreDataFail();

       /* addSubscription(data.userCenterRepository.getSubsRecord(), new Subscriber<String>() {
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
