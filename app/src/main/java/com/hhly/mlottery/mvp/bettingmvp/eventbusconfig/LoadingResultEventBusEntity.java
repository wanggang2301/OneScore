package com.hhly.mlottery.mvp.bettingmvp.eventbusconfig;

/**
 * Created by：XQyi on 2017/6/8 17:06
 * Use: 登录返回时的event
 */
public class LoadingResultEventBusEntity {

    boolean loadResult;

    public LoadingResultEventBusEntity(boolean loadResult){
        this.loadResult = loadResult;
    }

    public boolean isLoadResult() {
        return loadResult;
    }

    public void setLoadResult(boolean loadResult) {
        this.loadResult = loadResult;
    }
}
