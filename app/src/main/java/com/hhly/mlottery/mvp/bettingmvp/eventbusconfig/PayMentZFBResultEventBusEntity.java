package com.hhly.mlottery.mvp.bettingmvp.eventbusconfig;

/**
 * Created by：XQyi on 2017/6/3 09:54
 * Use: 支付宝支付结果返回码的 eventbusEntity
 */
public class PayMentZFBResultEventBusEntity {

    private String result;

    public PayMentZFBResultEventBusEntity(String result){
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
