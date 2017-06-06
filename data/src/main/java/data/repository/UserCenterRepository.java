package data.repository;


import data.api.UserCenterApiService;
import data.model.BottomOddsDetails;
import data.model.RecommendArticlesBean;
import data.model.SubsRecordBean;
import rx.Observable;

/**
 * @author: Wangg
 * @name：xxx
 * @description: 足球内页滚球reposeitory
 * @created on:2017/4/8  14:16.
 */

public class UserCenterRepository {
    UserCenterApiService mUserCenterApiService;

    public UserCenterRepository(UserCenterApiService mUserCenterApiService) {
        this.mUserCenterApiService = mUserCenterApiService;
    }

    public Observable<BottomOddsDetails> getBowlList(String thirdId, String oddType) {
        return mUserCenterApiService.getBowlList(thirdId, oddType);
    }

    public Observable<SubsRecordBean> getSubsRecord(String userId, String pageNum, String pageSize, String loginToken, String sign) {
        return mUserCenterApiService.getSubsRecord(userId, pageNum, pageSize, loginToken, sign);
    }


    public Observable<RecommendArticlesBean> getRecommendArtices(String userId, String pageNum, String pageSize, String loginToken, String sign) {
        return mUserCenterApiService.getRecommendArticles(userId, pageNum, pageSize, loginToken, sign);
    }


}
