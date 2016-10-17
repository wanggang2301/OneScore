package com.hhly.mlottery.bean.footballDetails.footballdatabasebean;

/**
 * author: yixq
 * Created by Administrator on 2016/10/9.
 * 足球资料库联赛之最明细Bean
 */

public class TopDetailsBean {

    /*
    teamId: 649,
    teamName: "法国",
    icon: "http://pic.13322.com/icons/teams/100/649.png",
    total: "4",
    per: "0.57"

    matchCount: "13"
     */

    private String teamId;
    private String teamName;
    private String icon;
    private String total;
    private String per;
    private String matchCount;

    public String getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(String matchCount) {
        this.matchCount = matchCount;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }
}
