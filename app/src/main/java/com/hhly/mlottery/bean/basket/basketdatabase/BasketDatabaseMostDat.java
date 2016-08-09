package com.hhly.mlottery.bean.basket.basketdatabase;

/**
 * Created by Administrator on 2016/8/8.
 */

public class BasketDatabaseMostDat {

    private double avgScore;//	92.9
    private double recordType;	//1
    private String teamIconUrl;//	http://en.13322.com/basket/images/logo_team/18.jpg
    private String teamName;//	马刺

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }

    public double getRecordType() {
        return recordType;
    }

    public void setRecordType(double recordType) {
        this.recordType = recordType;
    }

    public String getTeamIconUrl() {
        return teamIconUrl;
    }

    public void setTeamIconUrl(String teamIconUrl) {
        this.teamIconUrl = teamIconUrl;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    private double totalScore; // 	7618
}
