package com.hhly.mlottery.frame.withdrawandbindcard;

import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;

import data.bean.CodeBean;
import data.bean.WithdrawBean;
import data.repository.WithdrawRepository;
import rx.Observable;
import rx.Subscriber;

/**
 * 描    述：提现
 * 作    者：mady@13322.com
 * 时    间：2017/6/10
 */
public class WithdrawPresenter extends BasePresenter<WithdrawContract.View> implements WithdrawContract.Presenter{

    WithdrawRepository mRepository;

    WithdrawBean.DataEntity mWithdrawBean=new WithdrawBean.DataEntity();

    public WithdrawPresenter(WithdrawContract.View view) {
        super(view);
        mRepository=mDataManager.mWithdrawRepository;
    }

    @Override
    public void requestData() {
        Observable<WithdrawBean> observable=mRepository.getCardInfo(AppConstants.register.getUser().getUserId(),AppConstants.register.getToken(),"");

        addSubscription(observable, new Subscriber<WithdrawBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(WithdrawBean bean) {
                if(bean.getCode().equals("200")){
                    mWithdrawBean=bean.getData();
                    L.e("sign", mWithdrawBean.getCardNum()+"kakakak?");
                }
                mView.showCardInfo();
            }
        });
    }

    @Override
    public void commitWithdraw(String amount) {
        Observable<CodeBean> observable=mRepository.
                withdrawCommit(AppConstants.register.getUser().getUserId(),AppConstants.register.getToken(),amount,"");
        addSubscription(observable, new Subscriber<CodeBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.withdrawError();
            }

            @Override
            public void onNext(CodeBean bean) {
                if(bean.getCode().equals("200")){
                    mView.onWithdrawing();
                }else{
                    mView.withdrawError();
                }
            }
        });
    }

    @Override
    public WithdrawBean.DataEntity getCardInfo() {
        return mWithdrawBean;
    }
}
