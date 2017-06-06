package data.repository;


import data.api.UserCenterApiService;
import data.bean.BottomOddsDetails;
import data.bean.RecommendArticlesBean;
import data.bean.SubsRecordBean;
import rx.Observable;

/**
 * @author: Wangg
 * @name：xxx
 * @description: 订阅 推介reposeitory
 * @created on:2017/4/8  14:16.
 */

public class UserCenterRepository {
    UserCenterApiService mUserCenterApiService;

    public UserCenterRepository(UserCenterApiService mUserCenterApiService) {
        this.mUserCenterApiService = mUserCenterApiService;
    }


    //足球内页滚球
    public Observable<BottomOddsDetails> getBowlList(String thirdId, String oddType) {
        return mUserCenterApiService.getBowlList(thirdId, oddType);
    }

    //订阅记录
    public Observable<SubsRecordBean> getSubsRecord(String userId, String pageNum, String pageSize, String loginToken, String sign) {
        return mUserCenterApiService.getSubsRecord(userId, pageNum, pageSize, loginToken, sign);
    }

    //文章推介
    public Observable<RecommendArticlesBean> getRecommendArtices(String userId, String pageNum, String pageSize, String loginToken, String sign) {
        return mUserCenterApiService.getRecommendArticles(userId, pageNum, pageSize, loginToken, sign);
    }
}
