package com.hhly.mlottery.mvptask.data.api;


import com.hhly.mlottery.bean.footballDetails.BottomOddsDetails;
import com.hhly.mlottery.mvptask.data.model.RecommendArticlesBean;
import com.hhly.mlottery.mvptask.data.model.SubsRecordBean;

import retrofit2.http.GET;
import retrofit2.http.POST;
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


    //http://192.168.10.242:8091/user/promotion/purchaseRecords?userId=hhly90531&pageNum=2&pageSize=10&lang=zh&timezone=8
    // http://192.168.10.242:8091/user/promotion/publishPromotions
    @GET("user/promotion/purchaseRecords")
    Observable<SubsRecordBean> getSubsRecord(@Query("userId") String userId, @Query("pageNum") String pageNum, @Query("pageSize") String pageSize, @Query("loginToken") String loginToken, @Query("sign") String sign);

    @POST("user/promotion/publishPromotions")
    Observable<RecommendArticlesBean> getRecommendArticles(@Query("userId") String userId, @Query("pageNum") String pageNum, @Query("pageSize") String pageSize, @Query("loginToken") String loginToken, @Query("sign") String sign);


    // http://m.13322.com/mlottery/core/footballBallList.ballListDetail.do?lang=zh&timeZone=8&oddType=1&thirdId=3458732
}
