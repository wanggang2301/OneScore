package com.hhly.mlottery.mvp.bettingmvp.eventbusconfig;

/**
 * Created by：XQyi on 2017/6/7 15:07
 * Use: 购买完成后的返回event
 */
public class BettingBuyResultEventBusEntity {

    private boolean successBuy;

    public BettingBuyResultEventBusEntity(boolean successBuy){
        this.successBuy = successBuy;
    }

    public boolean isSuccessBuy() {
        return successBuy;
    }

    public void setSuccessBuy(boolean successBuy) {
        this.successBuy = successBuy;
    }
}
