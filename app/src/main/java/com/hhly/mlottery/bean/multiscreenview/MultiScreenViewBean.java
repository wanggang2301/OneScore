package com.hhly.mlottery.bean.multiscreenview;

/**
 * @author: Wangg
 * @Name：MultiScreenViewBean
 * @Description:
 * @Created on:2017/1/5  11:12.
 */

public class MultiScreenViewBean {

    private int type;
    private String matchId;

    private Object data;


    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public MultiScreenViewBean(int type, String matchId, Object data) {
        this.type = type;
        this.matchId = matchId;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
