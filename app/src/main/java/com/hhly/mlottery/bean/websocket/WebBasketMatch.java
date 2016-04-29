package com.hhly.mlottery.bean.websocket;

import java.util.Map;

/**
 * Created by A on 2016/1/8.
 */
public class WebBasketMatch {

    private Map<String,String> data;	//Array
    private String thirdId; // 	296989
    private int  type; 	//100

    public Map<String,String> getData() {
        return data;
    }

    public void setData(Map<String,String> data) {
        this.data = data;
    }

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
}
