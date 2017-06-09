package com.hhly.mlottery.mvp.bettingmvp.eventbusconfig;

/**
 * Created by：XQyi on 2017/6/8 20:35
 * Use: 推荐详情返回
 */
public class BettingDetailsResuleEventBusEntity {

    private boolean resultDetail;

    public BettingDetailsResuleEventBusEntity(boolean resultDetail){
        this.resultDetail = resultDetail;
    }

    public boolean isResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(boolean resultDetail) {
        this.resultDetail = resultDetail;
    }
}
