package com.hhly.mlottery.frame.accountdetail;

import com.hhly.mlottery.mvp.IPresenter;
import com.hhly.mlottery.mvp.IView;

import java.util.List;

import data.bean.AccountDetailBean;
import data.bean.AccountPageBean;
import data.bean.Page;
import data.bean.RechargeBean;

/**
 * 描    述：账户详情契约类
 * 作    者：mady@13322.com
 * 时    间：2017/6/5
 */
public class AccountDetailContract {

    interface View extends IView {
        void recyclerNotify();
        void showNoData();
        void setRefresh(boolean refresh);
        void showNoMoreData();
        void showNextPage(List<RechargeBean > recordEntity);
        void showBalance();
    }

    interface Presenter extends IPresenter<View> {
        Page getPage();
        void refreshData();
        void refreshDataByPage();
        List<RechargeBean> getListData();
        AccountDetailBean.DataEntity.BalanceEntity getBalanceData();

    }
}
