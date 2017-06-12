package com.hhly.mlottery.frame.withdrawandbindcard;

import com.hhly.mlottery.mvp.IPresenter;
import com.hhly.mlottery.mvp.IView;

import data.bean.WithdrawBean;

/**
 * 描    述：绑定银行卡
 * 作    者：mady@13322.com
 * 时    间：2017/6/10
 */
public class BindCardContract {

    interface View extends IView{
        void bindSuccess(WithdrawBean.DataEntity bean);
        void bindError();

    }
    interface Presenter extends IPresenter<View>{
        void request(String name,String bank,String card);
    }
}
