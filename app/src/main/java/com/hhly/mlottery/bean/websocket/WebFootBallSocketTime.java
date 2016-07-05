package com.hhly.mlottery.bean.websocket;

import java.util.Map;

/**
 * 新版指数推送时间和比分
 * tenney
 */
public class WebFootBallSocketTime {

    private String thirdId;

    private int type;

    private Map<String, String> data;

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
