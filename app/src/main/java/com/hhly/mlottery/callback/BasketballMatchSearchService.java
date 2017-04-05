package com.hhly.mlottery.callback;

import com.hhly.mlottery.bean.BasketballItemSearchBean;
import com.hhly.mlottery.bean.BasketballSearchBean;
import com.hhly.mlottery.bean.FootballSearchBean;
import com.hhly.mlottery.bean.TextDemo;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by yuely198 on 2017/3/24.
 */

public interface BasketballMatchSearchService {
    String SEARCHKEYWORD= "searchKeyword";
    @GET("/IOSBasketballMatch.fuzzySearch.do")
    Observable<TextDemo> searchProdcut(@Query("lang") String code, @Query(SEARCHKEYWORD) String keyword);
}
