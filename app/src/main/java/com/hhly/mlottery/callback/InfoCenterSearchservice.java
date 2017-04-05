package com.hhly.mlottery.callback;

import com.hhly.mlottery.bean.BasketSerach;
import com.hhly.mlottery.config.BaseURLs;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by yuely198 on 2017/3/23.
 */

public interface InfoCenterSearchservice {
    String SEARCHKEYWORD= "teamName";
    @GET("/footballEInfo.vagueSearch.do") rx.Observable<BasketSerach> searchProdcut(@Query("lang") String code, @Query(SEARCHKEYWORD) String keyword);
}
