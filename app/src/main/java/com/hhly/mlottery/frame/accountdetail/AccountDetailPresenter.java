package com.hhly.mlottery.frame.accountdetail;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.util.AppConstants;

import java.util.ArrayList;
import java.util.List;

import data.bean.AccountDetailBean;
import data.bean.Page;
import data.repository.AccountDetailRepository;
import rx.Observable;
import rx.Subscriber;

/**
 * 描    述：账户详情
 * 作    者：mady@13322.com
 * 时    间：2017/6/5
 */
public class AccountDetailPresenter extends BasePresenter<AccountDetailContract.View>implements AccountDetailContract.Presenter {

    AccountDetailRepository mRepository;
    private Page mPage;

    private List<AccountDetailBean.DataEntity.RecordEntity> mListData=new ArrayList<>();
    private AccountDetailBean.DataEntity.BalanceEntity mBalanceData =new AccountDetailBean.DataEntity.BalanceEntity();

    public AccountDetailPresenter(AccountDetailContract.View view) {
        super(view);
        mRepository=mDataManager.mAccountRepository;
        mPage=new Page(1,10);
    }

    @Override
    public Page getPage() {
        return mPage;
    }

    @Override
    public void refreshData() { //第一次请求
        mPage.index=1;
        String userId=AppConstants.register.getUser().getUserId();
        String token=AppConstants.register.getToken();

        Observable<AccountDetailBean> observable=mRepository.getAccountData(userId,token,"",mPage.index+"");
        addSubscription(observable, new Subscriber<AccountDetailBean>() {
            @Override
            public void onCompleted() {
                mView.setRefresh(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.onError();
            }

            @Override
            public void onNext(AccountDetailBean accountDetailBean) {
                if(accountDetailBean.getCode().equals(200)&&accountDetailBean.getData()!=null&&accountDetailBean.getData().getRecord()!=null
                        &&accountDetailBean.getData().getRecord().size()!=0){ //有数据
                    mListData.clear();
                    mListData.addAll(accountDetailBean.getData().getRecord());
                    mView.recyclerNotify();
                    mPage.index++;
                }else{
                    mView.showNoData();
                }
                if(accountDetailBean.getData()!=null&&accountDetailBean.getData().getBalance()!=null){
                    mBalanceData=accountDetailBean.getData().getBalance();
                }
            }
        });
    }

    @Override
    public void refreshDataByPage() {
        String userId=AppConstants.register.getUser().getUserId();
        String token=AppConstants.register.getToken();
        Observable<AccountDetailBean> observable=mRepository.getAccountData(userId,token,"",mPage.index+"");
        addSubscription(observable, new Subscriber<AccountDetailBean>() {
            @Override
            public void onCompleted() {
                mPage.index++;
            }

            @Override
            public void onError(Throwable e) {
                mView.onError();
            }

            @Override
            public void onNext(AccountDetailBean accountDetailBean) {
                if(accountDetailBean.getCode().equals(200)&&accountDetailBean.getData()!=null&&accountDetailBean.getData().getRecord()!=null
                        &&accountDetailBean.getData().getRecord().size()!=0){ //有数据
                    mView.showNextPage(accountDetailBean.getData().getRecord());
                }else {
                    mView.showNoMoreData();
                }
            }
        });
    }

    @Override
    public List<AccountDetailBean.DataEntity.RecordEntity> getListData() {
        return mListData;
    }

    @Override
    public AccountDetailBean.DataEntity.BalanceEntity getBalanceData() {
        return mBalanceData;
    }
}
