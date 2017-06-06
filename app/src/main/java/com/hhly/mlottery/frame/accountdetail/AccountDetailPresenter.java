package com.hhly.mlottery.frame.accountdetail;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.mvp.BasePresenter;

/**
 * 描    述：账户详情
 * 作    者：mady@13322.com
 * 时    间：2017/6/5
 */
public class AccountDetailPresenter extends BasePresenter<AccountDetailContract.View>implements AccountDetailContract.Presenter {

    public AccountDetailPresenter(AccountDetailContract.View view) {
        super(view);
        mDataManager= MyApp.getDataManager();
    }
}
