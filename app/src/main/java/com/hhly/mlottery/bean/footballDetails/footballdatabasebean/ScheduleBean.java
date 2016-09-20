package com.hhly.mlottery.bean.footballDetails.footballdatabasebean;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * description:
 * author: yixq
 * Created by A on 2016/9/14.
 * 足球资料库赛程bean
 */

public class ScheduleBean {

    /**
     *   code: 200,
         currentGroup: "决赛",
         type: 1,
         currentRoundIndex: 0,
         data: {},
         race: []
     */
    private int code;
    private String currentGroup;
    private int type;
    private int currentRoundIndex;
    private LinkedHashMap<String , String[]> data;
    private List<ScheduleRaceBean> race;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(String currentGroup) {
        this.currentGroup = currentGroup;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCurrentRoundIndex() {
        return currentRoundIndex;
    }

    public void setCurrentRoundIndex(int currentRoundIndex) {
        this.currentRoundIndex = currentRoundIndex;
    }

    public LinkedHashMap<String , String[]> getData() {
        return data;
    }

    public void setData(LinkedHashMap<String , String[]> data) {
        this.data = data;
    }

    public List<ScheduleRaceBean> getRace() {
        return race;
    }

    public void setRace(List<ScheduleRaceBean> race) {
        this.race = race;
    }
}
