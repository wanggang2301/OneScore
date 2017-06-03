package com.hhly.mlottery.frame.footballframe.corner;

import com.hhly.mlottery.bean.corner.CornerListBean;
import com.hhly.mlottery.mvp.IPresenter;
import com.hhly.mlottery.mvp.IView;

import java.util.List;

/**
 * 描    述：角球契约类
 * 作    者：mady@13322.com
 * 时    间：2017/5/18
 */
public class CornerContract {

    interface View extends IView{
        void recyclerNotify();
        void showNoData(String hasData);
    }

    interface Presenter extends IPresenter<View>{

        List<CornerListBean.CornerEntity> getData();
        void refreshData(String type);
    }
}
