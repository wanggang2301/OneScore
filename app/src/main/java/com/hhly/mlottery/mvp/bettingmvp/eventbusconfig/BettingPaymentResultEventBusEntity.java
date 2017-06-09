package com.hhly.mlottery.mvp.bettingmvp.eventbusconfig;

/**
 * Created by：XQyi on 2017/6/8 21:11
 * Use: 余额支付完成返回event
 */
public class BettingPaymentResultEventBusEntity {

    private boolean payMentResult;
     public BettingPaymentResultEventBusEntity(boolean payMentResult){
            this.payMentResult = payMentResult;
     }

    public boolean isPayMentResult() {
        return payMentResult;
    }

    public void setPayMentResult(boolean payMentResult) {
        this.payMentResult = payMentResult;
    }
}
