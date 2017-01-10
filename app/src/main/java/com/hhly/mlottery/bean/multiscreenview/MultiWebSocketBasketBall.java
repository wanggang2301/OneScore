package com.hhly.mlottery.bean.multiscreenview;

/**
 * @author: Wangg
 * @Name：MultiWebSocketBasketBall
 * @Description:
 * @Created on:2017/1/10  10:56.
 */

public class MultiWebSocketBasketBall {

    private MultiScreenBasketMatchScoreBean data;

    private String thirdId;
    private int type;


    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getThirdId() {
        return thirdId;
    }

    public int getType() {
        return type;
    }

    public MultiScreenBasketMatchScoreBean getData() {
        return data;
    }

    public void setData(MultiScreenBasketMatchScoreBean data) {
        this.data = data;
    }
}
