package com.hhly.mlottery.bean.snookerbean;

import java.util.List;

/**
 * Created by yixq on 2016/11/17.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerLeaguesBean {
//    leaguesId: "125358",
//    name: "北爱尔兰公开赛",
//    date: "2016-11-17",
//    matches: []

    private String leaguesId;
    private String name;
    private String date;
    private List<SnookerMatchesBean> matches;

    public String getLeaguesId() {
        return leaguesId;
    }

    public void setLeaguesId(String leaguesId) {
        this.leaguesId = leaguesId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<SnookerMatchesBean> getMatches() {
        return matches;
    }

    public void setMatches(List<SnookerMatchesBean> matches) {
        this.matches = matches;
    }
}
