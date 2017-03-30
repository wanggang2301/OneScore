package com.hhly.mlottery.frame.cpifrag.SnookerIndex.SnookerChildFragment;

import com.hhly.mlottery.bean.snookerbean.snookerIndexBean.SnookerIndexBean;
import com.hhly.mlottery.mvp.IPresenter;
import com.hhly.mlottery.mvp.IView;

import java.util.List;

/**
 * 描    述：斯诺克指数列表
 * 作    者：mady@13322.com
 * 时    间：2017/3/20
 */
public class SnookerIndexChildContract {

    interface View extends IView{

        /***
         * 刷新recycleview
         */
        void recyclerViewNotify();

        /**
         * 传递数据给父fragment
         */
        void setParentData(SnookerIndexBean bean);
        /**
         * 展示无数据或网络异常
         */
        void showNoData(String error);

        /**
         * 处理公司列表
         */
        void handleCompany(List<SnookerIndexBean.CompanyEntity>companyEntities);

        List<SnookerIndexBean.CompanyEntity> getCompanyList();
    }

    interface Presenter extends IPresenter<View>{

        List<SnookerIndexBean.AllInfoEntity> getData();

        void refreshByDate(String date,String type);

        void filterCompany();

    }
}
