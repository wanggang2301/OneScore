package com.hhly.mlottery.callback;

import com.hhly.mlottery.bean.BasketSerach;
import com.hhly.mlottery.config.BaseURLs;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @ClassName: OneScoreGit
 * @author:Administrator luyao
 * @Description:
 * @data: 2016/9/27 9:17
 */
public interface BasketSearchservice {
      String SEARCHKEYWORD= "searchKeyword";
    @GET(BaseURLs.FUZZYSEARCH) rx.Observable<BasketSerach> searchProdcut(@Query("lang") String code, @Query(SEARCHKEYWORD) String keyword);
}
