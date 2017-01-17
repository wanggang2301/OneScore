package com.hhly.mlottery.bean.multiscreenview;

/**
 * @author: Wangg
 * @Name：WebSocketMultiScreenViewBean
 * @Description:
 * @Created on:2017/1/6  16:44.
 */

public class WebSocketMultiScreenViewBean {

    private int type;

    private String matchId;

    private String topic;

    public WebSocketMultiScreenViewBean(int type, String matchId, String topic) {
        this.type = type;
        this.matchId = matchId;
        this.topic = topic;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
}
