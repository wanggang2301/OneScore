package com.hhly.mlottery.frame.footframe.eventbus;

/**
 * @author: Wangg
 * @Name：ScoresMatchSettingEventBusEntity
 * @Description:
 * @Created on:2016/12/1  15:20.
 */

public class ScoresMatchSettingEventBusEntity {
    private int fgIndex;

    public ScoresMatchSettingEventBusEntity(int fgIndex) {
        this.fgIndex = fgIndex;
    }

    public int getFgIndex() {

        return fgIndex;
    }

    public void setFgIndex(int fgIndex) {
        this.fgIndex = fgIndex;
    }
}
