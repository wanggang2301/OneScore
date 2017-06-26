package com.hhly.mlottery.bean.basket.basketdatabase;

/**
 * description:
 * author: yixq
 * Created by A on 2016/7/21.
 */
public class BasketDatabaseBigSmallDetailsBean {

    private String ranking;//: 1,
    private String teamName;//: "克雷顿大学",
    private String finished;//: 4,
    private String high;//: 4,
    private String draw;//: 0,
    private String low;//: 4
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

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }
}
