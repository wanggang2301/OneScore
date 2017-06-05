package com.hhly.mlottery.mvptask;

import com.hhly.mlottery.mvp.IPresenter;
import com.hhly.mlottery.mvp.IView;

import java.util.List;

import data.model.BottomOddsDetails;
import data.model.SubsRecordBean;

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


    //加载更多数据
    interface IPullLoadMoreDataView extends IChildView {
        void pullUpLoadMoreDataSuccess();

        void pullUpLoadMoreDataFail();
    }


    //滚球
    interface IBowlChildPresenter extends IPresenter<IChildView> {
        void requestData(String thirdId, String oddType);

        BottomOddsDetails getBowlBean();
    }


    //订阅记录
    interface ISubsRecordPresenter extends IPresenter<IPullLoadMoreDataView> {
        void requestData(String userId, String pageNum, String pageSize);

        void pullUpLoadMoreData();

        List<SubsRecordBean.PurchaseRecordsBean.ListBean> getSubsRecordData();
    }


    //推介文章
    interface IRecommendArticlesPresenter extends IPresenter<IPullLoadMoreDataView> {
        void requestData();

        void pullUpLoadMoreData();

        //BottomOddsDetails getBowlBean();
        String getRecommendArticlesData();
    }
}
