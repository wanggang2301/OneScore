package com.hhly.mlottery.frame.cpifrag;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/4/14  11:06.
 */

public class CloseCpiWebSocketEventBus {
    private boolean isVisible;
    private int index;

    public CloseCpiWebSocketEventBus(boolean isVisible, int index) {
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
