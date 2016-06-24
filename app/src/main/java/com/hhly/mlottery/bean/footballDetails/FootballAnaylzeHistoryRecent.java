package com.hhly.mlottery.bean.footballDetails;

/**
 * description:
 * author: yixq
 * Created by A on 2016/6/13.
 */
public class FootballAnaylzeHistoryRecent {
    private String time; //": "2016-04-16 14:00",
    private String matchType; //: "韩K联",
    private String home; //: "全北现代",
    private String guest; //: "城南一和",
    private int homeScore; //: 3,
    private int guestScore; // 2,
    private int markTeam; //: 2,
    private int teamColor; //: 0,
    private String ctotScore; //: null,
    private String tot; //: 0,
    private String casLetGoal; //: null,
    private String let; //: 0

    private boolean homeGround;//	false
    private int result; //	1

    public boolean isHomeGround() {
        return homeGround;
    }

    public void setHomeGround(boolean homeGround) {
        this.homeGround = homeGround;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(int guestScore) {
        this.guestScore = guestScore;
    }

    public int getMarkTeam() {
        return markTeam;
    }

    public void setMarkTeam(int markTeam) {
        this.markTeam = markTeam;
    }

    public int getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(int teamColor) {
        this.teamColor = teamColor;
    }

    public String getCtotScore() {
        return ctotScore;
    }

    public void setCtotScore(String ctotScore) {
        this.ctotScore = ctotScore;
    }

    public String getTot() {
        return tot;
    }

    public void setTot(String tot) {
        this.tot = tot;
    }

    public String getCasLetGoal() {
        return casLetGoal;
    }

    public void setCasLetGoal(String casLetGoal) {
        this.casLetGoal = casLetGoal;
    }

    public String getLet() {
        return let;
    }

    public void setLet(String let) {
        this.let = let;
    }
}
