package com.hhly.mlottery.mvptask.data.repository;


import com.hhly.mlottery.bean.footballDetails.BottomOddsDetails;
import com.hhly.mlottery.mvptask.data.api.BowlApi;

import rx.Observable;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/4/8  14:16.
 */

public class BowlReposeitory {
    BowlApi mBowlApi;

    public BowlReposeitory(BowlApi mBowlApi) {
        this.mBowlApi = mBowlApi;
    }

    public Observable<BottomOddsDetails> getBowlList(String thirdId, String oddType) {

        return mBowlApi.getBowlList(thirdId, oddType);
    }
}
