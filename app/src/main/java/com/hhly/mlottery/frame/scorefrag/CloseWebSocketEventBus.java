package com.hhly.mlottery.frame.scorefrag;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/4/13  10:33.
 */

public class CloseWebSocketEventBus {

    private boolean isVisible;
    private int index;

    public CloseWebSocketEventBus(boolean isVisible, int index) {
        this.isVisible = isVisible;
        this.index = index;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
