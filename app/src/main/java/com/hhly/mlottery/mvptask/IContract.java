package com.hhly.mlottery.mvptask;

import com.hhly.mlottery.bean.footballDetails.BottomOddsDetails;
import com.hhly.mlottery.mvp.IPresenter;
import com.hhly.mlottery.mvp.IView;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/5/27  12:23.
 */

public interface IContract {


    interface IChildView extends IView {
        void loading();

        void responseData();

        void noData();

        //返回true代表Activity添加fg成功
        boolean isActive();

    }

    //滚球

    interface IBowlChildPresenter extends IPresenter<IChildView> {

        void requestData(String thirdId, String oddType);

        BottomOddsDetails getBowlBean();

    }

    //订阅记录
    interface ISubsRecordPresenter extends IPresenter<IPullLoadMoreDataView> {

        void requestData();

        void pullUpLoadMoreData();


        //BottomOddsDetails getBowlBean();

        String getSubsRecordData();

    }


    interface IPullLoadMoreDataView extends IChildView {

        void pullUpLoadMoreDataSuccess();

        void pullUpLoadMoreDataFail();
    }


    //推介文章
    interface IRecommendArticlesPresenter extends IPresenter<IPullLoadMoreDataView> {

        void requestData();

        void pullUpLoadMoreData();

        //BottomOddsDetails getBowlBean();

        String getRecommendArticlesData();

    }
}
