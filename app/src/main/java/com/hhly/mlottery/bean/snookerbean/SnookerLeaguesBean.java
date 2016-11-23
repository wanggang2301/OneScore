package com.hhly.mlottery.bean.snookerbean;

import java.util.List;

/**
 * Created by yixq on 2016/11/17.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerLeaguesBean {
//    leaguesId: "125358",
//    leaguesName: "北爱尔兰公开赛",
//    date: "2016-11-17",
//    matches: []

    private String leaguesId;
    private String leaguesName;
    private String date;
    private List<SnookerMatchesBean> matches;

    public String getLeaguesId() {
        return leaguesId;
    }

    public void setLeaguesId(String leaguesId) {
        this.leaguesId = leaguesId;
    }

    public String getLeaguesName() {
        return leaguesName;
    }

    public void setLeaguesName(String leaguesName) {
        this.leaguesName = leaguesName;
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
