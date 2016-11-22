package com.hhly.mlottery.bean.snookerbean;

import java.util.List;

/**
 * Created by yixq on 2016/11/17.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerGanesBean {

//    id: "4569751114000",
//    name: "0onlywin",
//    gameType: null,
//    gameTime: "0",
//    capNum: "0.0",
//    ratioItems: []

    private String id;
    private String name;
    private String gameType;
    private String gameTime;
    private String capNum;
    private List<SnookerRatioItemsBean> ratioItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getGameTime() {
        return gameTime;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }

    public String getCapNum() {
        return capNum;
    }

    public void setCapNum(String capNum) {
        this.capNum = capNum;
    }

    public List<SnookerRatioItemsBean> getRatioItems() {
        return ratioItems;
    }

    public void setRatioItems(List<SnookerRatioItemsBean> ratioItems) {
        this.ratioItems = ratioItems;
    }
}


