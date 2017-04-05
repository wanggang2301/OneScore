package com.hhly.mlottery.callback;

import com.hhly.mlottery.bean.BasketSerach;
import com.hhly.mlottery.bean.FootballSearchBean;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by yuely198 on 2017/3/23.
 */

public interface FootballMatchSearchService {
    String SEARCHKEYWORD= "teamName";
    @GET("/footballScore.vagueSearch.do")
    Observable<FootballSearchBean> searchProdcut(@Query("lang") String code, @Query(SEARCHKEYWORD) String keyword);
}
