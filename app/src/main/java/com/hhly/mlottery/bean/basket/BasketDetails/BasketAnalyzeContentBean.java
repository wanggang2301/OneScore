package com.hhly.mlottery.bean.basket.BasketDetails;

import java.util.List;

/**
 * Created by A on 2016/4/5.
 */
public class BasketAnalyzeContentBean {

    private List<BasketAnalyzeFutureMatchBean> futureMatch;//	Array
    private int historyWin; //	3
    private String matchAll;//	28
    private String matchLose;//	8
    private String matchWin;//	20
    private String matchWinRate;//	71.43%
    private String ranking;//	3

    private List<Integer> recentLW;//	Array
    private String scoreLoseSix;//	80.83
    private String scoreWinSix;//	85.17
    private String team;//	莫日达斯克鲁济斯

    public List<Integer> getRecentLW() {
        return recentLW;
    }

    public void setRecentLW(List<Integer> recentLW) {
        this.recentLW = recentLW;
    }

    public List<BasketAnalyzeFutureMatchBean> getFutureMatch() {
        return futureMatch;
    }

    public void setFutureMatch(List<BasketAnalyzeFutureMatchBean> futureMatch) {
        this.futureMatch = futureMatch;
    }

    public int getHistoryWin() {
        return historyWin;
    }

    public void setHistoryWin(int historyWin) {
        this.historyWin = historyWin;
    }

    public String getMatchAll() {
        return matchAll;
    }

    public void setMatchAll(String matchAll) {
        this.matchAll = matchAll;
    }

    public String getMatchLose() {
        return matchLose;
    }

    public void setMatchLose(String matchLose) {
        this.matchLose = matchLose;
    }

    public String getMatchWin() {
        return matchWin;
    }

    public void setMatchWin(String matchWin) {
        this.matchWin = matchWin;
    }

    public String getMatchWinRate() {
        return matchWinRate;
    }

    public void setMatchWinRate(String matchWinRate) {
        this.matchWinRate = matchWinRate;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }



    public String getScoreLoseSix() {
        return scoreLoseSix;
    }

    public void setScoreLoseSix(String scoreLoseSix) {
        this.scoreLoseSix = scoreLoseSix;
    }

    public String getScoreWinSix() {
        return scoreWinSix;
    }

    public void setScoreWinSix(String scoreWinSix) {
        this.scoreWinSix = scoreWinSix;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}

