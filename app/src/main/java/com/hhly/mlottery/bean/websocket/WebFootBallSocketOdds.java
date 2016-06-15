package com.hhly.mlottery.bean.websocket;

import java.util.List;
import java.util.Map;

/**
 * 新版指数推送
 * tenney
 */
public class WebFootBallSocketOdds {

    private String thirdId;

    private  int type;

    private List<Map<String,String>> data;

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

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }
}
