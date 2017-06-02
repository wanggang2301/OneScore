package com.hhly.mlottery.mvptask.data.api;


import com.hhly.mlottery.bean.footballDetails.BottomOddsDetails;

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


    @GET("")
    Observable<String> getSubsRecord();

    // http://m.13322.com/mlottery/core/footballBallList.ballListDetail.do?lang=zh&timeZone=8&oddType=1&thirdId=3458732
}
