package com.hhly.mlottery.frame.accountdetail;

import com.hhly.mlottery.mvp.IPresenter;
import com.hhly.mlottery.mvp.IView;

/**
 * 描    述：账户详情契约类
 * 作    者：mady@13322.com
 * 时    间：2017/6/5
 */
public class AccountDetailContract {

    interface View extends IView {
        void recyclerNotify();
        void showNoData(String hasData);
    }

    interface Presenter extends IPresenter<View> {
    }
}
