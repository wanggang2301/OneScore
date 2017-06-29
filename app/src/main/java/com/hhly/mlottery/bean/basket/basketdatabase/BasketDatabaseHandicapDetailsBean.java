package com.hhly.mlottery.bean.basket.basketdatabase;

/**
 * description:
 * author: yixq
 * Created by A on 2016/7/21.
 */
public class BasketDatabaseHandicapDetailsBean {

    private String ranking;//: 1,
    private String teamName;//: "克雷顿大学",
    private String finished;//: 4,
    private String over;//: 4,
    private String under;//: 0,
    private String win;//: 4,
    private String draw; //: 0,
    private String lose;//: 0
    private String teamId;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }

    public String getUnder() {
        return under;
    }

    public void setUnder(String under) {
        this.under = under;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getLose() {
        return lose;
    }

    public void setLose(String lose) {
        this.lose = lose;
    }
}
