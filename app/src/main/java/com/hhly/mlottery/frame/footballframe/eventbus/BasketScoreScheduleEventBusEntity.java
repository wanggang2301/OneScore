package com.hhly.mlottery.frame.footballframe.eventbus;

import java.util.Map;

/**
 * @author: yixq
 * @Name：BasketScoreImmedEventBusEntity
 * @Description:筛选Activity返回筛选结果 （赛程）
 * @Created on:2017/03/15
 */

public class BasketScoreScheduleEventBusEntity {
    private Map<String,Object> map;

    public BasketScoreScheduleEventBusEntity(Map<String,Object> mapData) {
        this.map = mapData;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
