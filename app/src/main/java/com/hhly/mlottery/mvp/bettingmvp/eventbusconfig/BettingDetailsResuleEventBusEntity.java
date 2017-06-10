package com.hhly.mlottery.mvp.bettingmvp.eventbusconfig;

import data.utils.Sign;

/**
 * Created by：XQyi on 2017/6/8 20:35
 * Use: 推荐详情返回
 */
public class BettingDetailsResuleEventBusEntity {

    private String currentId;

    public BettingDetailsResuleEventBusEntity(String currentId){
        this.currentId = currentId;
    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }
}
