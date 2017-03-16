package com.hhly.mlottery.mvpui.SnookerIndex;


import com.hhly.mlottery.mvp.IPresenter;
import com.hhly.mlottery.mvp.IView;

/**
 * 描    述：斯诺克指数契约类
 * 作    者：mady@13322.com
 * 时    间：2017/3/16
 */
public class SIndexContract {

    interface View extends IView{
        void fangfa();
    }

    interface Presenter extends IPresenter<View>{
        void fangfaf();
    }
}
