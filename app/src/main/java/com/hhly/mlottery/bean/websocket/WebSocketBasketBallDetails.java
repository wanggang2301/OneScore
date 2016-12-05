package com.hhly.mlottery.bean.websocket;

/**
 * Created by andy on 2016/4/8.
 */
public class WebSocketBasketBallDetails {


    private DataEntity data;

    private String thirdId;
    private int type;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DataEntity getData() {
        return data;
    }

    public String getThirdId() {
        return thirdId;
    }

    public int getType() {
        return type;
    }


}
