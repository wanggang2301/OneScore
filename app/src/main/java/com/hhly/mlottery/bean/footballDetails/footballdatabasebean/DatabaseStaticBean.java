package com.hhly.mlottery.bean.footballDetails.footballdatabasebean;

/**
 * author: yixq
 * Created by Administrator on 2016/10/9.
 * 足球资料库统计明细Bean
 */

public class DatabaseStaticBean {

    /*
        matchCount: 319,
        homeWinCount: 139,
        equalCount: 66,
        awayWinCount: 114,

        homeWinPer: "43.57%",
        equalPer: "20.69%",
        awayWinPer: "35.74%",

        goalCount: 806,
        homeCount: 0,
        awayCount: 0,

        goalAvg: 2.53,
        homeAvg: 1.37,
        awayAvg: 1.15,

        moreZero: 296,
        moreOne: 229,
        moreTwo: 143,
        moreThree: 72,

        moreZeroPer: "92.79%",
        moreOnePer: "71.79%",
        moreTwoPer: "44.83%",
        moreThreePer: "22.57%"
     */
    private int matchCount;
    private int homeWinCount;
    private int equalCount;
    private int awayWinCount;

    private String homeWinPer;
    private String equalPer;
    private String awayWinPer;

    private int goalCount;
    private int homeCount;
    private int awayCount;

    private double goalAvg;
    private double homeAvg;
    private double awayAvg;

    private int moreZero;
    private int moreOne;
    private int moreTwo;
    private int moreThree;

    private String moreZeroPer;
    private String moreOnePer;
    private String moreTwoPer;
    private String moreThreePer;

    public int getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }

    public int getHomeWinCount() {
        return homeWinCount;
    }

    public void setHomeWinCount(int homeWinCount) {
        this.homeWinCount = homeWinCount;
    }

    public int getEqualCount() {
        return equalCount;
    }

    public void setEqualCount(int equalCount) {
        this.equalCount = equalCount;
    }

    public int getAwayWinCount() {
        return awayWinCount;
    }

    public void setAwayWinCount(int awayWinCount) {
        this.awayWinCount = awayWinCount;
    }

    public String getHomeWinPer() {
        return homeWinPer;
    }

    public void setHomeWinPer(String homeWinPer) {
        this.homeWinPer = homeWinPer;
    }

    public String getEqualPer() {
        return equalPer;
    }

    public void setEqualPer(String equalPer) {
        this.equalPer = equalPer;
    }

    public String getAwayWinPer() {
        return awayWinPer;
    }

    public void setAwayWinPer(String awayWinPer) {
        this.awayWinPer = awayWinPer;
    }

    public int getGoalCount() {
        return goalCount;
    }

    public void setGoalCount(int goalCount) {
        this.goalCount = goalCount;
    }

    public int getHomeCount() {
        return homeCount;
    }

    public void setHomeCount(int homeCount) {
        this.homeCount = homeCount;
    }

    public int getAwayCount() {
        return awayCount;
    }

    public void setAwayCount(int awayCount) {
        this.awayCount = awayCount;
    }

    public double getGoalAvg() {
        return goalAvg;
    }

    public void setGoalAvg(double goalAvg) {
        this.goalAvg = goalAvg;
    }

    public double getHomeAvg() {
        return homeAvg;
    }

    public void setHomeAvg(double homeAvg) {
        this.homeAvg = homeAvg;
    }

    public double getAwayAvg() {
        return awayAvg;
    }

    public void setAwayAvg(double awayAvg) {
        this.awayAvg = awayAvg;
    }

    public int getMoreZero() {
        return moreZero;
    }

    public void setMoreZero(int moreZero) {
        this.moreZero = moreZero;
    }

    public int getMoreOne() {
        return moreOne;
    }

    public void setMoreOne(int moreOne) {
        this.moreOne = moreOne;
    }

    public int getMoreTwo() {
        return moreTwo;
    }

    public void setMoreTwo(int moreTwo) {
        this.moreTwo = moreTwo;
    }

    public int getMoreThree() {
        return moreThree;
    }

    public void setMoreThree(int moreThree) {
        this.moreThree = moreThree;
    }

    public String getMoreZeroPer() {
        return moreZeroPer;
    }

    public void setMoreZeroPer(String moreZeroPer) {
        this.moreZeroPer = moreZeroPer;
    }

    public String getMoreOnePer() {
        return moreOnePer;
    }

    public void setMoreOnePer(String moreOnePer) {
        this.moreOnePer = moreOnePer;
    }

    public String getMoreTwoPer() {
        return moreTwoPer;
    }

    public void setMoreTwoPer(String moreTwoPer) {
        this.moreTwoPer = moreTwoPer;
    }

    public String getMoreThreePer() {
        return moreThreePer;
    }

    public void setMoreThreePer(String moreThreePer) {
        this.moreThreePer = moreThreePer;
    }
}
