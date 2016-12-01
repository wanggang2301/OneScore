package com.hhly.mlottery.frame.footframe.eventbus;

/**
 * @author: Wangg
 * @Name：ScoreFramnetWebSocketEntity
 * @Description:其他界面滚逼socket连接
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
