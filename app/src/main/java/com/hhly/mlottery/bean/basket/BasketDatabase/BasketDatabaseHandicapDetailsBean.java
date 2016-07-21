package com.hhly.mlottery.bean.basket.BasketDatabase;

/**
 * description:
 * author: yixq
 * Created by A on 2016/7/21.
 */
public class BasketDatabaseHandicapDetailsBean {

    private int ranking;//: 1,
    private String teamName;//: "克雷顿大学",
    private int finished;//: 4,
    private int over;//: 4,
    private int under;//: 0,
    private int win;//: 4,
    private int draw; //: 0,
    private int lose;//: 0

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getOver() {
        return over;
    }

    public void setOver(int over) {
        this.over = over;
    }

    public int getUnder() {
        return under;
    }

    public void setUnder(int under) {
        this.under = under;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }
}
