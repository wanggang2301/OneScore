package com.hhly.mlottery.mvptask.bowl;

import com.hhly.mlottery.bean.footballDetails.BottomOddsDetails;
import com.hhly.mlottery.mvp.IPresenter;
import com.hhly.mlottery.mvp.IView;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/5/27  12:23.
 */

public interface IBowlChildContract {


    interface IBowlChildView extends IView {
        void loading();

        void responseData();

        void noData();


    }

    interface IBowlChildPresenter extends IPresenter<IBowlChildView> {

        void requestData(String thirdId, String oddType);

        BottomOddsDetails getBowlBean();

    }
}
