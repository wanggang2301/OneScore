package com.hhly.mlottery.frame.cpifrag.basketballtask.data;

import com.hhly.mlottery.bean.snookerbean.SnookerRankBean;

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
public interface TaskDataService {
    @GET("mlottery/core/snookerWorldRanking.getWorldRankingPaging.do")
    Observable<SnookerRankBean> getIndexCenter(@Query("lang") String lang, @Query("timeZone") String timeZone, @Query("pageSize") String pageSize, @Query("pageNum") String pageNum);
}


