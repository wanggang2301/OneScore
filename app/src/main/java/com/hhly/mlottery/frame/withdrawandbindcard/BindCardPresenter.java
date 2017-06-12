package com.hhly.mlottery.frame.withdrawandbindcard;

import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;

import data.bean.WithdrawBean;
import data.repository.WithdrawRepository;
import rx.Observable;
import rx.Subscriber;

/**
 * 描    述：绑定银行卡
 * 作    者：mady@13322.com
 * 时    间：2017/6/10
 */
public class BindCardPresenter extends BasePresenter<BindCardContract.View>implements BindCardContract.Presenter{

    WithdrawRepository mRepository;
    public BindCardPresenter(BindCardContract.View view) {
        super(view);
        mRepository=mDataManager.mWithdrawRepository;
    }

    @Override
    public void request(String name, String bank, String card) {
        Observable<WithdrawBean> observable=mRepository.bindCard(AppConstants.register.getUser().getUserId(),
                AppConstants.register.getToken(),card,bank,"");
        addSubscription(observable, new Subscriber<WithdrawBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.bindError();
            }

            @Override
            public void onNext(WithdrawBean bean) {
                L.e("sign",bean.getCode()+"coddddd");
                if(bean.getCode().equals("200")){
                    mView.bindSuccess(bean.getData());
                }else{
                    mView.bindError();
                }
            }
        });
    }
}
