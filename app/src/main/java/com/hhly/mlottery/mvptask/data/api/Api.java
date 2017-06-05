package com.hhly.mlottery.mvptask.data.api;


import com.hhly.mlottery.bean.footballDetails.BottomOddsDetails;
import com.hhly.mlottery.mvptask.data.model.RecommendArticlesBean;
import com.hhly.mlottery.mvptask.data.model.SubsRecordBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author: Wangg
 * @name：xxx
 * @description: api接口
 * @created on:2017/4/6  17:34.
 */

public interface Api {
    //滚球
    @GET("mlottery/core/footballBallList.ballListDetail.do")
    Observable<BottomOddsDetails> getBowlList(@Query("thirdId") String thirdId, @Query("oddType") String oddType);

    //訂閱記錄
    @GET("user/promotion/purchaseRecords")
    Observable<SubsRecordBean> getSubsRecord(@Query("userId") String userId, @Query("pageNum") String pageNum, @Query("pageSize") String pageSize, @Query("loginToken") String loginToken, @Query("sign") String sign);

    //文章推介
    @GET("user/promotion/publishPromotions")
    Observable<RecommendArticlesBean> getRecommendArticles(@Query("userId") String userId, @Query("pageNum") String pageNum, @Query("pageSize") String pageSize, @Query("loginToken") String loginToken, @Query("sign") String sign);

}
