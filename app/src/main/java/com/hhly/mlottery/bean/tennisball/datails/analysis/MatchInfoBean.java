package com.hhly.mlottery.bean.tennisball.datails.analysis;

/**
 * desc:
 * Created by 107_tangrr on 2017/3/27 0027.
 */

public class MatchInfoBean {
    private String leagueId;
    private String leagueName;
    private String matchId;
    private String matchName;
    private String startTime;
    private String startDate;
    private String roundName;
    private int matchType;
    private String dataType;
    private int matchStatus;
    private PlayerBean homePlayer1;
    private PlayerBean homePlayer2;
    private PlayerBean guestPlayer1;
    private PlayerBean guestPlayer2;
    private MatchScoreBean matchScore;

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

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public int getMatchType() {
        return matchType;
    }

    public void setMatchType(int matchType) {
        this.matchType = matchType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(int matchStatus) {
        this.matchStatus = matchStatus;
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
