package com.hhly.mlottery.bean.intelligence;

/**
 * 单个情报
 * <p/>
 * Created by loshine on 2016/7/19.
 */
public class BigDataForecastData {

    /**
     * homeWinPercent : 0.2
     * sizeWinPercent : 0.07
     * asiaWinPercent : 0.13
     */

    private double homeWinPercent;
    private double sizeWinPercent;
    private double asiaWinPercent;

    public double getHomeWinPercent() {
        return homeWinPercent;
    }

    public void setHomeWinPercent(double homeWinPercent) {
        this.homeWinPercent = homeWinPercent;
    }

    public double getSizeWinPercent() {
        return sizeWinPercent;
    }

    public void setSizeWinPercent(double sizeWinPercent) {
        this.sizeWinPercent = sizeWinPercent;
    }

    public double getAsiaWinPercent() {
        return asiaWinPercent;
    }

    public void setAsiaWinPercent(double asiaWinPercent) {
        this.asiaWinPercent = asiaWinPercent;
    }
}
