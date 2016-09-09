package com.hhly.mlottery.bean.footballDetails.footballdatabasebean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/7.
 */

public class FootballDatabaseScheduleBean {
    /**
        data: [],
        code: 200,
        race: []
     */

//    private List<ScheduleDataBean> data;
    private String[] data;
    private int code;
    private List<ScheduleRaceBean> race;

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ScheduleRaceBean> getRace() {
        return race;
    }

    public void setRace(List<ScheduleRaceBean> race) {
        this.race = race;
    }
}
