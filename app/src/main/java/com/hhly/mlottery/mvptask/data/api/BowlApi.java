package com.hhly.mlottery.mvptask.data.api;


import com.hhly.mlottery.bean.footballDetails.BottomOddsDetails;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/4/6  17:34.
 */

public interface BowlApi {
    //篮球指数列表
    @POST("mlottery/core/footballBallList.ballListDetail.do")
    Observable<BottomOddsDetails> getBowlList(@Query("thirdId") String thirdId, @Query("oddType") String oddType);


   // http://m.13322.com/mlottery/core/footballBallList.ballListDetail.do?lang=zh&timeZone=8&oddType=1&thirdId=3458732
}
