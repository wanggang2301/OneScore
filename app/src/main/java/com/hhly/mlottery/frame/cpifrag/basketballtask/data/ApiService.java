package com.hhly.mlottery.frame.cpifrag.basketballtask.data;

import com.hhly.mlottery.bean.basket.index.BasketIndexBean;
import com.hhly.mlottery.bean.basket.index.BasketIndexDetailsBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author: Wangg
 * @Name：TaskDataService
 * @Description:
 * @Created on:2017/3/17  15:55.
 */

//http://m.1332255.com:81/mlottery/core/snookerWorldRanking.getWorldRankingPaging.do?lang=zh&timeZone=7&pageSize=30&pageNum=1

// http://m.1332255.com:81/mlottery/core/basketballMatch.findIndexList.do?lang=zh&timeZone=8&date=&type=asiaSize

public interface ApiService {
    @GET("mlottery/core/basketballMatch.findIndexList.do")
    Observable<BasketIndexBean> getIndexCenter(@Query("lang") String lang, @Query("timeZone") String timeZone, @Query("date") String date, @Query("type") String type, @Query("appType") String appType);


    //comId=&lang=zh-TW&oddsType=asiaSize&thirdId=4432337&timeZone=8
    @GET("mlottery/core/basketballDetail.findOddsTrendDetail.do")
    Observable<BasketIndexDetailsBean> getIndexCenterDetails(@Query("lang") String lang, @Query("timeZone") String timeZone, @Query("comId") String comId, @Query("thirdId") String thirdId, @Query("oddsType") String oddsType, @Query("appType") String appType);
}


