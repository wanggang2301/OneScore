package com.hhly.mlottery.bean.snookerbean.snookerschedulebean;

import java.util.List;

/**
 * Created by yixq on 2017/2/17.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerScheuleDataBean {

//    date: "2017-02-18",
//    eventsBattle: []

//    resultsBattle: [],
//    date: "2017-02-16"

//    liveBattle: []

    private String date;

    /**
     * 赛程
     */
    private List<SnookerEventsBean> eventsBattle;

    /**
     * 赛果
     * @return
     */
    private List<SnookerEventsBean> resultsBattle;

    /**
     * 即时
     * @return
     */
    private List<SnookerEventsBean> liveBattle;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<SnookerEventsBean> getEventsBattle() {
        return eventsBattle;
    }

    public void setEventsBattle(List<SnookerEventsBean> eventsBattle) {
        this.eventsBattle = eventsBattle;
    }

    public List<SnookerEventsBean> getResultsBattle() {
        return resultsBattle;
    }

    public void setResultsBattle(List<SnookerEventsBean> resultsBattle) {
        this.resultsBattle = resultsBattle;
    }

    public List<SnookerEventsBean> getLiveBattle() {
        return liveBattle;
    }

    public void setLiveBattle(List<SnookerEventsBean> liveBattle) {
        this.liveBattle = liveBattle;
    }
}
