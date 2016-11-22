package com.hhly.mlottery.bean.snookerbean;

import java.util.List;

/**
 * Created by yixq on 2016/11/17.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerDataBean {

//    date: "2016-11-17",
//    leagues: [],
//    waterfallMatches: []

    private String date;
    private List<SnookerLeaguesBean> leagues;
    private List<SnookerWaterfallMatchesBean> waterfallMatches;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<SnookerLeaguesBean> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<SnookerLeaguesBean> leagues) {
        this.leagues = leagues;
    }

    public List<SnookerWaterfallMatchesBean> getWaterfallMatches() {
        return waterfallMatches;
    }

    public void setWaterfallMatches(List<SnookerWaterfallMatchesBean> waterfallMatches) {
        this.waterfallMatches = waterfallMatches;
    }
}
