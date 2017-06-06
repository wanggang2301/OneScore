package com.hhly.mlottery.mvptask;

import com.hhly.mlottery.mvp.IPresenter;
import com.hhly.mlottery.mvp.IView;

import java.util.List;

import data.bean.BottomOddsDetails;
import data.bean.RecommendArticlesBean;
import data.bean.SubsRecordBean;

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

        void pullUploadingView();
    }


    //滚球
    interface IBowlChildPresenter extends IPresenter<IChildView> {
        void requestData(String thirdId, String oddType);

        BottomOddsDetails getBowlBean();
    }


    //订阅记录
    interface ISubsRecordPresenter extends IPresenter<IPullLoadMoreDataView> {
        void requestData(String userId, String pageNum, String pageSize, String loginToken, String sign);

        void pullUpLoadMoreData(String userId, String pageNum, String pageSize, String loginToken, String sign);

        List<SubsRecordBean.PurchaseRecordsBean.ListBean> getSubsRecordData();
    }


    //推介文章
    interface IRecommendArticlesPresenter extends IPresenter<IPullLoadMoreDataView> {
        void requestData(String userId, String pageNum, String pageSize, String loginToken, String sign);

        void pullUpLoadMoreData(String userId, String pageNum, String pageSize, String loginToken, String sign);

        //BottomOddsDetails getBowlBean();
        List<RecommendArticlesBean.PublishPromotionsBean.ListBean> getRecommendArticlesData();
    }
}
