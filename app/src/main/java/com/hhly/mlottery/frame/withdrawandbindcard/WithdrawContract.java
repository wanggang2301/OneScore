package com.hhly.mlottery.frame.withdrawandbindcard;

import com.hhly.mlottery.mvp.IPresenter;
import com.hhly.mlottery.mvp.IView;

import data.bean.WithdrawBean;

/**
 * 描    述：提现
 * 作    者：mady@13322.com
 * 时    间：2017/6/10
 */
public class WithdrawContract {

    interface View extends IView{
        void showCardInfo(); //展示银行卡信息
        void onWithdrawing(); //提现等待
        void withdrawError();
    }
    interface Presenter extends IPresenter<View>{
        void requestData();
        void commitWithdraw(String amount);

        WithdrawBean.DataEntity getCardInfo();
    }
}
