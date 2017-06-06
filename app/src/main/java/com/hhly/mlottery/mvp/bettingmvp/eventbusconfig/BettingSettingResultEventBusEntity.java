package com.hhly.mlottery.mvp.bettingmvp.eventbusconfig;

import java.util.List;

/**
 * Created by：XQyi on 2017/6/5 22:35
 * Use: 推荐列表设置返回
 */
public class BettingSettingResultEventBusEntity {

    private List<String> keyChecked;
    private List<String> currPaly;

    public BettingSettingResultEventBusEntity(List<String> currPaly , List<String> mKeyChecked){
        this.keyChecked = mKeyChecked;
        this.currPaly = currPaly;
    }

    public List<String> getKeyChecked() {
        return keyChecked;
    }

    public void setKeyChecked(List<String> keyChecked) {
        this.keyChecked = keyChecked;
    }

    public List<String> getCurrPaly() {
        return currPaly;
    }

    public void setCurrPaly(List<String> currPaly) {
        this.currPaly = currPaly;
    }
}
