package com.hhly.mlottery.frame.footballframe.eventbus;

/**
 * @author: yixq
 * @Name：BasketScoreImmedEventBusEntity
 * @Description:筛选Activity返回筛选结果 （内页）
 * @Created on:2017/03/15
 */

public class BasketDetailsEventBusEntity {
    private String mIndex;

    public BasketDetailsEventBusEntity(String index) {
        this.mIndex = index;
    }

    public void setmIndex(String mIndex) {
        this.mIndex = mIndex;
    }

    public String getmIndex() {
        return mIndex;
    }
}
