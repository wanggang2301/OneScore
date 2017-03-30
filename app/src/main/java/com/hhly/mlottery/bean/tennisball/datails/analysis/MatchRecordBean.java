package com.hhly.mlottery.bean.tennisball.datails.analysis;

import java.util.List;

/**
 * desc:
 * Created by 107_tangrr on 2017/3/27 0027.
 */

public class MatchRecordBean {

    private int playerFail;
    private int playerWin;
    private int totalTimes;
    private String winRate;
    private List<MatchListBean> matchList;

    public int getPlayerFail() {
        return playerFail;
    }

    public void setPlayerFail(int playerFail) {
        this.playerFail = playerFail;
    }

    public int getPlayerWin() {
        return playerWin;
    }

    public void setPlayerWin(int playerWin) {
        this.playerWin = playerWin;
    }

    public int getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(int totalTimes) {
        this.totalTimes = totalTimes;
    }

    public String getWinRate() {
        return winRate;
    }

    public void setWinRate(String winRate) {
        this.winRate = winRate;
    }

    public List<MatchListBean> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<MatchListBean> matchList) {
        this.matchList = matchList;
    }
}
