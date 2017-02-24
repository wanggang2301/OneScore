package com.hhly.mlottery.callback;

import com.hhly.mlottery.bean.FootBallSerachBean;
import com.hhly.mlottery.config.BaseURLs;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @ClassName: OneScoreGit
 * @author:Administrator luyao
 * @Description:
 * @data: 2016/9/26 18:43
 */
public interface SearchService {
    @GET(BaseURLs.SEARCHMATCHLEAGUES)
    rx.Observable<FootBallSerachBean> searchProdcut(@Query("lang") String code, @Query("matchStr") String keyword);
}
