package com.hhly.mlottery.bean.footballDetails;

import java.util.List;

/**
 * description:
 * author: yixq
 * Created by A on 2016/6/13.
 */
public class FootballAnalyzeDetailsBean {
    private List<FootballAnaylzeHistoryRecent> battleHistory; //	Array
    private FootballAnalyzeFutureMatch futureMatch; //	Object
    private String result; //	200
    private FootballAnalyzeTeamRecent teamRecent; //	Object

    private String guestTeam;
    private String homeTeam;

    public String getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(String guestTeam) {
        this.guestTeam = guestTeam;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public List<FootballAnaylzeHistoryRecent> getBattleHistory() {
        return battleHistory;
    }

    public void setBattleHistory(List<FootballAnaylzeHistoryRecent> battleHistory) {
        this.battleHistory = battleHistory;
    }

    public FootballAnalyzeFutureMatch getFutureMatch() {
        return futureMatch;
    }

    public void setFutureMatch(FootballAnalyzeFutureMatch futureMatch) {
        this.futureMatch = futureMatch;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public FootballAnalyzeTeamRecent getTeamRecent() {
        return teamRecent;
    }

    public void setTeamRecent(FootballAnalyzeTeamRecent teamRecent) {
        this.teamRecent = teamRecent;
    }
}
