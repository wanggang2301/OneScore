package data.api;


import data.bean.BottomOddsDetails;
import data.bean.RecommendArticlesBean;
import data.bean.SubsRecordBean;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author: Wangg
 * @name：xxx
 * @description: api接口
 * @created on:2017/4/6  17:34.
 */

public interface UserCenterApiService {
    /**
     * @param thirdId
     * @param oddType
     * @return
     */
    //滚球
    @GET("mlottery/core/footballBallList.ballListDetail.do")
    Observable<BottomOddsDetails> getBowlList(@Query("thirdId") String thirdId,
                                              @Query("oddType") String oddType);

    /**
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param loginToken
     * @param sign
     * @return
     */
    //訂閱記錄
    @POST("user/promotion/purchaseRecords")
    Observable<SubsRecordBean> getSubsRecord(@Query("userId") String userId,
                                             @Query("pageNum") String pageNum,
                                             @Query("pageSize") String pageSize,
                                             @Query("loginToken") String loginToken,
                                             @Query("sign") String sign);


    /**
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param loginToken
     * @param sign
     * @return
     */
    //文章推介
    @POST("user/promotion/publishPromotions")
    Observable<RecommendArticlesBean> getRecommendArticles(@Query("userId") String userId,
                                                           @Query("pageNum") String pageNum,
                                                           @Query("pageSize") String pageSize,
                                                           @Query("loginToken") String loginToken,
                                                           @Query("sign") String sign);

}
