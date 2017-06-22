package com.hhly.mlottery.mvptask.subsrecord;

import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.mvptask.IContract;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.DeviceInfo;

import java.util.List;

import data.bean.SubsRecordBean;
import data.repository.UserCenterRepository;
import rx.Subscriber;

/**
 * @anthor wangg
 * @className SubsRecordPresenter
 * @time 2017/6/1  19:13
 * @changeDesc XXX
 * @changeTime XXX
 * @classDesc 订阅记录的presenter
 */
public class SubsRecordPresenter extends BasePresenter<IContract.IPullLoadMoreDataView> implements IContract.ISubsRecordPresenter {

    private UserCenterRepository userCenterRepository;
    private List<SubsRecordBean.PurchaseRecordsBean.ListBean> listBeanList;
    private boolean isHasNextPage = false;

    public SubsRecordPresenter(IContract.IPullLoadMoreDataView view) {
        super(view);
        userCenterRepository = mDataManager.userCenterRepository;
    }


    @Override
    public void requestData(String userId, String pageNum, String pageSize, String loginToken, String sign) {
        if (!mView.isActive()) {
            return;
        }

        mView.loading();

        addSubscription(userCenterRepository.getSubsRecord(userId, pageNum, pageSize, loginToken, sign), new Subscriber<SubsRecordBean>() {
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

                    DeviceInfo.handlerRequestResult(Integer.parseInt(subsRecordBean.getCode()), "code");
                    mView.onError();
                    return;
                }

                if (!CollectionUtils.notEmpty(subsRecordBean.getPurchaseRecords().getList())) {
                    mView.noData();
                    return;
                }

                isHasNextPage = subsRecordBean.getPurchaseRecords().isHasNextPage();
                listBeanList = subsRecordBean.getPurchaseRecords().getList();
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

        addSubscription(userCenterRepository.getSubsRecord(userId, pageNum, pageSize, loginToken, sign), new Subscriber<SubsRecordBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mView.pullUpLoadMoreDataFail();
            }

            @Override
            public void onNext(SubsRecordBean subsRecordBean) {
                if (!"200".equals(subsRecordBean.getCode())) {
                    mView.pullUpLoadMoreDataFail();
                    return;
                }
                isHasNextPage = subsRecordBean.getPurchaseRecords().isHasNextPage();
                listBeanList = subsRecordBean.getPurchaseRecords().getList();
                mView.pullUpLoadMoreDataSuccess();
            }
        });


    }

    @Override
    public List<SubsRecordBean.PurchaseRecordsBean.ListBean> getSubsRecordData() {
        return listBeanList;
    }
}
