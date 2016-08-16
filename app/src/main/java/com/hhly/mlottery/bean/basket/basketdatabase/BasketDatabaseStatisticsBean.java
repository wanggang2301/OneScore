package com.hhly.mlottery.bean.basket.basketdatabase;

/**
 * Created by Administrator on 2016/8/8.
 */

public class BasketDatabaseStatisticsBean {

    private BasketDatabsseLeagueStatistics leagueStatistics; // 	Object
    private BasketDatabaseLeagueMost leagueMost; // 	Object

    public BasketDatabaseLeagueMost getLeagueMost() {
        return leagueMost;
    }

    public void setLeagueMost(BasketDatabaseLeagueMost leagueMost) {
        this.leagueMost = leagueMost;
    }

    public BasketDatabsseLeagueStatistics getLeagueStatistics() {
        return leagueStatistics;
    }

    public void setLeagueStatistics(BasketDatabsseLeagueStatistics leagueStatistics) {
        this.leagueStatistics = leagueStatistics;
    }

}
