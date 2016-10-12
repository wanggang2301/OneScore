package com.hhly.mlottery.bean.footballDetails.footballdatabasebean;

/**
 * author: yixq
 * Created by Administrator on 2016/10/9.
 * 足球资料库大小球明细Bean
 */

public class DatabaseBigSmallBean {

    /*
        rank: "2",
        teamId: "646",
        teamName: "荷兰",
        matchCount: "5",
        left: "1",
        midd: "0",
        right: "4",
        lvg: "20%",
        mvg: "0%",
        rvg: "80%"
     */

    private String rank;
    private String teamId;
    private String teamName;
    private String matchCount;
    private String left;
    private String midd;
    private String right;
    private String lvg;
    private String mvg;
    private String rvg;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
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

    public String getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(String matchCount) {
        this.matchCount = matchCount;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getMidd() {
        return midd;
    }

    public void setMidd(String midd) {
        this.midd = midd;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getLvg() {
        return lvg;
    }

    public void setLvg(String lvg) {
        this.lvg = lvg;
    }

    public String getMvg() {
        return mvg;
    }

    public void setMvg(String mvg) {
        this.mvg = mvg;
    }

    public String getRvg() {
        return rvg;
    }

    public void setRvg(String rvg) {
        this.rvg = rvg;
    }
}
