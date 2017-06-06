package data.api;


import data.model.BottomOddsDetails;
import data.model.RecommendArticlesBean;
import data.model.SubsRecordBean;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author: Wangg
 * @name：xxx
 * @description: api接口
 * @created on:2017/4/6  17:34.
 */

public interface UserCenterApiService {
    //滚球
    @GET("mlottery/core/footballBallList.ballListDetail.do")
    Observable<BottomOddsDetails> getBowlList(@Query("thirdId") String thirdId, @Query("oddType") String oddType);

    //訂閱記錄
    @GET("user/promotion/purchaseRecords")
    Observable<SubsRecordBean> getSubsRecord(@Query("userId") String userId, @Query("pageNum") String pageNum, @Query("pageSize") String pageSize, @Query("loginToken") String loginToken, @Query("sign") String sign);

    //文章推介
    @GET("user/promotion/publishPromotions")
    Observable<RecommendArticlesBean> getRecommendArticles(@Query("userId") String userId, @Query("pageNum") String pageNum, @Query("pageSize") String pageSize, @Query("loginToken") String loginToken, @Query("sign") String sign);

}
