package com.hhly.mlottery.mvp.bettingmvp.eventbusconfig;

/**
 * Created by：XQyi on 2017/6/22 12:03
 * Use: 返回充值页面event
 */
public class PayChargeMoneyResultEventBus {

    boolean chargeResult;
    public PayChargeMoneyResultEventBus(boolean chargeResult){
        this.chargeResult = chargeResult;
    }

    public boolean isChargeResult() {
        return chargeResult;
    }

    public void setChargeResult(boolean chargeResult) {
        this.chargeResult = chargeResult;
    }
}
