package com.hhly.mlottery.bean.footballDetails;

/**
 * 大数据预测
 * <p/>
 * Created by loshine on 2016/7/19.
 */
public class BigDataForecast {

    private BigDataOddsInfo battleHistory; /* 历史交锋 */

    private BigDataOddsInfo homeRecent; /* 主队近期战绩 */
    private BigDataOddsInfo guestRecent; /* 客队近期战绩 */

    public BigDataOddsInfo getBattleHistory() {
        return battleHistory;
    }

    public void setBattleHistory(BigDataOddsInfo battleHistory) {
        this.battleHistory = battleHistory;
    }

    public BigDataOddsInfo getHomeRecent() {
        return homeRecent;
    }

    public void setHomeRecent(BigDataOddsInfo homeRecent) {
        this.homeRecent = homeRecent;
    }

    public BigDataOddsInfo getGuestRecent() {
        return guestRecent;
    }

    public void setGuestRecent(BigDataOddsInfo guestRecent) {
        this.guestRecent = guestRecent;
    }
}
