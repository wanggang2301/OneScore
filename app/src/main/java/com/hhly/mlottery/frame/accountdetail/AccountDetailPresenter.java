package com.hhly.mlottery.frame.accountdetail;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ToastTools;

import java.util.ArrayList;
import java.util.List;

import data.bean.AccountDetailBean;
import data.bean.AccountPageBean;
import data.bean.Page;
import data.bean.RechargeBean;
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

    private List<RechargeBean> mListData=new ArrayList<>();
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
//        String userId="HHLY00000136";
//        String token="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJqd3QiLCJpYXQiOjE0OTY4MDM3OTcsInN1YiI6IntcImlkXCI6XCJISExZMDAwMDAxMzZcIixcInBob25lTnVtXCI6XCIxNTAxMzY5NzEwMVwiLFwiaW1hZ2VTcmNcIjpcImh0dHA6Ly90cC4xMzMyMjU1LmNvbS9pbWcvMjAxNy0wNS0xNi9hZjJkMTAzMi1iOWUyLTQ5YjYtODc5MS1iZDgyODMzNjQxOWEuanBnXCIsXCJuaWNrTmFtZVwiOlwi5biV5bCU5qKF5ouJ5pav5L2c5a6iMTox5oiY5bmz5Zyf5bqT5pu856ue5oqAXCJ9In0.NWOL8XGDWDehovknsPSs_zUCBddsPdBRBH-7y7QIqdI";

        Observable<AccountDetailBean> observable=mRepository.getAccountData(userId,token,"");
        addSubscription(observable, new Subscriber<AccountDetailBean>() {
            @Override
            public void onCompleted() {
                mView.setRefresh(false);
            }

            @Override
            public void onError(Throwable e) {
                L.e("sign",e.getMessage()+"errror");
                mView.onError();
            }

            @Override
            public void onNext(AccountDetailBean accountDetailBean) {
//                ToastTools.showQuick(MyApp.get(),"sign"+accountDetailBean.getData().getBalance().getAvailableBalance()+"");
                if(accountDetailBean.getCode().equals("200")&&accountDetailBean.getData()!=null&&accountDetailBean.getData().getRecord()!=null
                        &&accountDetailBean.getData().getRecord().size()!=0){ //有数据
                    mListData.clear();
                    mListData.addAll(accountDetailBean.getData().getRecord());
                    mView.recyclerNotify();
                    mPage.index++;
//                    ToastTools.showQuick(MyApp.get(),"sign"+accountDetailBean.getData().getBalance().getAvailableBalance()+"");
                }else{
                    mView.showNoData();
                }
                if(accountDetailBean.getData()!=null&&accountDetailBean.getData().getBalance()!=null){
                    mBalanceData=accountDetailBean.getData().getBalance();
                    L.e("signAAA",mBalanceData.getAvailableBalance()+"");
                    mView.showBalance();
                }
            }
        });
    }

    @Override
    public void refreshDataByPage() {
        String userId=AppConstants.register.getUser().getUserId();
        String token=AppConstants.register.getToken();
////
//        String userId="HHLY00000136";
//        String token="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJqd3QiLCJpYXQiOjE0OTY3MTY2NTEsInN1YiI6IntcImlkXCI6XCJISExZMDAwMDAxMzZcIixcInBob25lTnVtXCI6XCIxNTAxMzY5NzEwMVwifSJ9.DnxIwOrkdG--8mxn4-j8sGJ85ouF7hCiyPmCIdR8XYg";

        Observable<AccountPageBean> observable=mRepository.getAccountDataByPage(userId,token,"",mPage.index+"");
        addSubscription(observable, new Subscriber<AccountPageBean>() {
            @Override
            public void onCompleted() {
                mPage.index++;
            }

            @Override
            public void onError(Throwable e) {
                mView.onError();
            }

            @Override
            public void onNext(AccountPageBean accountDetailBean) {
                if(accountDetailBean.getCode().equals("200")&&accountDetailBean.getData()!=null
                        &&accountDetailBean.getData().size()!=0){ //有数据
                    mView.showNextPage(accountDetailBean.getData());
                }else {
                    mView.showNoMoreData();
                }
            }
        });
    }

    @Override
    public List<RechargeBean> getListData() {
        return mListData;
    }

    @Override
    public AccountDetailBean.DataEntity.BalanceEntity getBalanceData() {
        return mBalanceData;
    }
}
