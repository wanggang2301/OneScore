package com.hhly.mlottery.mvp.bettingmvp.eventbusconfig;

/**
 * Created by：XQyi on 2017/6/22 12:03
 * Use: 返回支付页面event
 */
public class PayDetailsResultEventBus {

    boolean detailsResult;

    public PayDetailsResultEventBus(boolean detailsResult){
        this.detailsResult = detailsResult;
    }

    public boolean isDetailsResult() {
        return detailsResult;
    }

    public void setDetailsResult(boolean detailsResult) {
        this.detailsResult = detailsResult;
    }
}
