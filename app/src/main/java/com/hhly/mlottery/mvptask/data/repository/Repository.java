package com.hhly.mlottery.mvptask.data.repository;


import com.hhly.mlottery.bean.footballDetails.BottomOddsDetails;
import com.hhly.mlottery.mvptask.data.api.Api;

import rx.Observable;

/**
 * @author: Wangg
 * @name：xxx
 * @description: 足球内页滚球reposeitory
 * @created on:2017/4/8  14:16.
 */

public class Repository {
    Api mApi;

    public Repository(Api mApi) {
        this.mApi = mApi;
    }

    public Observable<BottomOddsDetails> getBowlList(String thirdId, String oddType) {
        return mApi.getBowlList(thirdId, oddType);
    }

    public Observable<String> getSubsRecord() {
        return mApi.getSubsRecord();
    }
}
