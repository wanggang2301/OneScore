package com.hhly.mlottery.bean.websocket;

import java.util.Map;

/**
 * Created by asus1 on 2016/1/7.
 */
public class WebSocketStadiumLiveTextEvent {

    private Map<String,String> data;



    private String thirdId;

    private  String type;


    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }



    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
