package com.hhly.mlottery.frame.footframe.eventbus;

/**
 * @author: Wangg
 * @Name：ScoreFramnetWebSocketEntity
 * @Description:(比分链接socket)，(资讯、视频、数据、指数)断开比分socket连接
 * @Created on:2016/12/1  14:19.
 */

public class ScoreFragmentWebSocketEntity {

    private int fgIndex;

    public ScoreFragmentWebSocketEntity(int fgIndex) {
        this.fgIndex = fgIndex;
    }

    public int getFgIndex() {

        return fgIndex;
    }

    public void setFgIndex(int fgIndex) {
        this.fgIndex = fgIndex;
    }
}
