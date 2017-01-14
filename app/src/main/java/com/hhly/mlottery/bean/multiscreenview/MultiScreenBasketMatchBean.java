package com.hhly.mlottery.bean.multiscreenview;

/**
 * @author: Wangg
 * @Name：MultiScreenBasketMatchBean
 * @Description:
 * @Created on:2017/1/10  9:56.
 */

public class MultiScreenBasketMatchBean {


    private Integer matchType; //联赛类型  //跳转


    private String thirdId;      //跳转
    private String homeTeam;
    private String guestTeam;
    private String leagueId;   //联赛Id  //跳转
    private String leagueName;
    private String leagueColor;
    private String date;
    private String time;
    private int matchStatus;     //跳转
    private boolean hot;
    private String homeRanking;
    private String guestRanking;

    private MultiScreenBasketMatchScoreBean matchScore;

    public Integer getMatchType() {
        return matchType;
    }

    public void setMatchType(Integer matchType) {
        this.matchType = matchType;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(String guestTeam) {
        this.guestTeam = guestTeam;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getLeagueColor() {
        return leagueColor;
    }

    public void setLeagueColor(String leagueColor) {
        this.leagueColor = leagueColor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(int matchStatus) {
        this.matchStatus = matchStatus;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public String getHomeRanking() {
        return homeRanking;
    }

    public void setHomeRanking(String homeRanking) {
        this.homeRanking = homeRanking;
    }

    public String getGuestRanking() {
        return guestRanking;
    }

    public void setGuestRanking(String guestRanking) {
        this.guestRanking = guestRanking;
    }

    public MultiScreenBasketMatchScoreBean getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(MultiScreenBasketMatchScoreBean matchScore) {
        this.matchScore = matchScore;
    }

    public Object getMatchOdds() {
        return matchOdds;
    }

    public void setMatchOdds(Object matchOdds) {
        this.matchOdds = matchOdds;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(String homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getGuestTeamId() {
        return guestTeamId;
    }

    public void setGuestTeamId(String guestTeamId) {
        this.guestTeamId = guestTeamId;
    }

    public String getHomeLogoUrl() {
        return homeLogoUrl;
    }

    public void setHomeLogoUrl(String homeLogoUrl) {
        this.homeLogoUrl = homeLogoUrl;
    }

    public String getGuestLogoUrl() {
        return guestLogoUrl;
    }

    public void setGuestLogoUrl(String guestLogoUrl) {
        this.guestLogoUrl = guestLogoUrl;
    }

    private Object matchOdds;  //头部没用
    private int section;     //篮球几节
    private String homeTeamId;
    private String guestTeamId;
    private String homeLogoUrl;
    private String guestLogoUrl;


}
