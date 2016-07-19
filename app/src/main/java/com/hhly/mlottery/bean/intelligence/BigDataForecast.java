package com.hhly.mlottery.bean.intelligence;

/**
 * 大数据预测
 * <p/>
 * Created by loshine on 2016/7/19.
 */
public class BigDataForecast {

    private BigDataForecastData battleHistory; /* 历史交锋 */

    private BigDataForecastData homeRecent; /* 主队近期战绩 */
    private BigDataForecastData guestRecent; /* 客队近期战绩 */

    public BigDataForecastData getBattleHistory() {
        return battleHistory;
    }

    public void setBattleHistory(BigDataForecastData battleHistory) {
        this.battleHistory = battleHistory;
    }

    public BigDataForecastData getHomeRecent() {
        return homeRecent;
    }

    public void setHomeRecent(BigDataForecastData homeRecent) {
        this.homeRecent = homeRecent;
    }

    public BigDataForecastData getGuestRecent() {
        return guestRecent;
    }

    public void setGuestRecent(BigDataForecastData guestRecent) {
        this.guestRecent = guestRecent;
    }
}
