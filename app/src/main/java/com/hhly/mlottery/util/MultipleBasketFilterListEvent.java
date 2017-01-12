package com.hhly.mlottery.util;

import java.util.Map;

/**
 * Created by yixq on 2016/12/17.
 * mail：yixq@13322.com
 * describe:
 */

public class MultipleBasketFilterListEvent {

    private Map<String,Object> map;

    public MultipleBasketFilterListEvent(Map<String,Object> mapData) {
        this.map = mapData;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
