package com.hhly.mlottery.bean.basket.BasketDatabase;

/**
 * description:
 * author: yixq
 * Created by A on 2016/7/21.
 */
public class BasketDatabaseBigSmallDetailsBean {

    private int ranking;//: 1,
    private String teamName;//: "克雷顿大学",
    private int finished;//: 4,
    private int high;//: 4,
    private int draw;//: 0,
    private int low;//: 4

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

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }
}
