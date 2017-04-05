package com.hhly.mlottery.frame.cpifrag.basketballtask.data;

import com.hhly.mlottery.bean.basket.index.BasketIndexBean;
import com.hhly.mlottery.bean.basket.index.BasketIndexDetailsBean;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author: Wangg
 * @Name：TaskDataService
 * @Description:
 * @Created on:2017/3/17  15:55.
 */

public interface ApiService {

    //篮球指数列表
    @POST("mlottery/core/basketballMatch.findIndexList.do")
    Observable<BasketIndexBean> getIndexCenter(@Query("lang") String lang, @Query("timeZone") String timeZone, @Query("date") String date, @Query("type") String type, @Query("appType") String appType);

    //篮球指数详情页
    //comId=&lang=zh-TW&oddsType=asiaSize&thirdId=4432337&timeZone=8
    @POST("mlottery/core/basketballDetail.findOddsTrendDetail.do")
    Observable<BasketIndexDetailsBean> getIndexCenterDetails(@Query("lang") String lang, @Query("timeZone") String timeZone, @Query("comId") String comId, @Query("thirdId") String thirdId, @Query("oddsType") String oddsType, @Query("appType") String appType);
}


