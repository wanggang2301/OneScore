package com.hhly.mlottery.bean.tennisball.datails.analysis;

/**
 * desc:
 * Created by 107_tangrr on 2017/3/27 0027.
 */

public class MatchListBean {

    private String startDate;
    private String startTime;
    private String leagueName;
    private String role;
    private String siteType;
    private PlayerBean homePlayer1;
    private PlayerBean homePlayer2;
    private PlayerBean guestPlayer1;
    private PlayerBean guestPlayer2;
    private MatchScoreBean matchScore;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public PlayerBean getHomePlayer1() {
        return homePlayer1;
    }

    public void setHomePlayer1(PlayerBean homePlayer1) {
        this.homePlayer1 = homePlayer1;
    }

    public PlayerBean getHomePlayer2() {
        return homePlayer2;
    }

    public void setHomePlayer2(PlayerBean homePlayer2) {
        this.homePlayer2 = homePlayer2;
    }

    public PlayerBean getGuestPlayer1() {
        return guestPlayer1;
    }

    public void setGuestPlayer1(PlayerBean guestPlayer1) {
        this.guestPlayer1 = guestPlayer1;
    }

    public PlayerBean getGuestPlayer2() {
        return guestPlayer2;
    }

    public void setGuestPlayer2(PlayerBean guestPlayer2) {
        this.guestPlayer2 = guestPlayer2;
    }

    public MatchScoreBean getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(MatchScoreBean matchScore) {
        this.matchScore = matchScore;
    }
}
