package com.hhly.mlottery.bean.multiscreenview;

/**
 * @author: Wangg
 * @Name：WebSocketMultiScreenViewTextBean
 * @Description:
 * @Created on:2017/1/10  10:21.
 */

public class WebSocketMultiScreenViewTextBean {

    private int type;

    private String matchId;

    public WebSocketMultiScreenViewTextBean(int type, String matchId, String text) {
        this.type = type;
        this.matchId = matchId;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private String text;
}
