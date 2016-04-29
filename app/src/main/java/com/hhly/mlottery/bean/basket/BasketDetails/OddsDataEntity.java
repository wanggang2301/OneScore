package com.hhly.mlottery.bean.basket.BasketDetails;

/**
 * Created by andy on 2016/4/12 16:00.
 * <p/>
 * 描述：篮球详情的指数（指数详情（欧赔亚盘大小球））entity
 */
public class OddsDataEntity {
    private String updateTime;
    private String leftOdds;
    private int leftOddsTrend;
    private String rightOdds;
    private int rightOddsTrend;
    private String handicapValue;
    private int handicapValueTrend;

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getLeftOdds() {
        return leftOdds;
    }

    public void setLeftOdds(String leftOdds) {
        this.leftOdds = leftOdds;
    }

    public int getLeftOddsTrend() {
        return leftOddsTrend;
    }

    public void setLeftOddsTrend(int leftOddsTrend) {
        this.leftOddsTrend = leftOddsTrend;
    }

    public String getRightOdds() {
        return rightOdds;
    }

    public void setRightOdds(String rightOdds) {
        this.rightOdds = rightOdds;
    }

    public int getRightOddsTrend() {
        return rightOddsTrend;
    }

    public void setRightOddsTrend(int rightOddsTrend) {
        this.rightOddsTrend = rightOddsTrend;
    }

    public String getHandicapValue() {
        return handicapValue;
    }

    public void setHandicapValue(String handicapValue) {
        this.handicapValue = handicapValue;
    }

    public int getHandicapValueTrend() {
        return handicapValueTrend;
    }

    public void setHandicapValueTrend(int handicapValueTrend) {
        this.handicapValueTrend = handicapValueTrend;
    }

}
