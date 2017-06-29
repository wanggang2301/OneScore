package com.hhly.mlottery.bean.basket.basketdatabase;

/**
 * 描    述：排行队伍
 * 作    者：longs@13322.com
 * 时    间：2016/8/10
 */
public class RankingTeam {

    /**
     * teamName : 米兰阿马尼
     * winMatch : 22
     * loseMatch : 8
     * finishedMatch : 30
     * winRate : 0.733
     * gameBehind : 0
     * ranking : 1W
     */

    private String teamName;
    private int winMatch;
    private int loseMatch;
    private int finishedMatch;
    private double winRate;
    private int gameBehind; // 胜差
    private int ranking;
    private String recent; // 近况
    private String teamId;

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

    public int getWinMatch() {
        return winMatch;
    }

    public void setWinMatch(int winMatch) {
        this.winMatch = winMatch;
    }

    public int getLoseMatch() {
        return loseMatch;
    }

    public void setLoseMatch(int loseMatch) {
        this.loseMatch = loseMatch;
    }

    public int getFinishedMatch() {
        return finishedMatch;
    }

    public void setFinishedMatch(int finishedMatch) {
        this.finishedMatch = finishedMatch;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    public int getGameBehind() {
        return gameBehind;
    }

    public void setGameBehind(int gameBehind) {
        this.gameBehind = gameBehind;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getRecent() {
        return recent;
    }

    public void setRecent(String recent) {
        this.recent = recent;
    }
}
