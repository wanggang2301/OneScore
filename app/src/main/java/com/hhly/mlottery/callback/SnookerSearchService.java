package com.hhly.mlottery.callback;

import com.hhly.mlottery.bean.basket.infomation.SnookerPlayerBean;
import com.hhly.mlottery.config.BaseURLs;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author: Wangg
 * @Name：SnookerSearchService
 * @Description:
 * @Created on:2017/2/22  10:41.
 */

public interface SnookerSearchService {
    @GET(BaseURLs.URL_SNOOKER_PLAYER_SEARCH)
    rx.Observable<SnookerPlayerBean> searchSnookerPlayer(@Query("lang") String code, @Query("playerName") String keyword);

}
