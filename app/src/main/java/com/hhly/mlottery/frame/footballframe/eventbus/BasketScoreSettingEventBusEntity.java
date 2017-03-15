package com.hhly.mlottery.frame.footballframe.eventbus;

import java.util.Map;

/**
 * @author: yixq
 * @Name：BasketScoreImmedEventBusEntity
 * @Description:筛选Activity返回筛选结果 （设置）
 * @Created on:2017/03/15
 */

public class BasketScoreSettingEventBusEntity {
    private String mIndex;

    public BasketScoreSettingEventBusEntity(String index) {
        this.mIndex = index;
    }

    public String getmIndex() {
        return mIndex;
    }

    public void setmIndex(String mIndex) {
        this.mIndex = mIndex;
    }
}
