package com.hhly.mlottery.frame.footballframe.bowl.data.repository;


import com.hhly.mlottery.frame.footballframe.bowl.data.api.BasketIndexApi;
import com.hhly.mlottery.frame.footballframe.bowl.data.bean.BasketIndex;

import rx.Observable;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/4/8  14:16.
 */

public class BasketIndexReposeitory {
    BasketIndexApi mBasketIndexApi;

    public BasketIndexReposeitory(BasketIndexApi mBasketIndexApi) {
        this.mBasketIndexApi = mBasketIndexApi;
    }

    public Observable<BasketIndex> getIndexList(String date, String type, String appType) {

        return mBasketIndexApi.getIndexList(date, type, appType);
    }
}
