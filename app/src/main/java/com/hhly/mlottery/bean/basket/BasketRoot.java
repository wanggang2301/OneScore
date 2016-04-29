package com.hhly.mlottery.bean.basket;

import java.util.List;

/**
 * Created by A on 2016/1/11.
 */
public class BasketRoot {

    private List<BasketRootBean> matchData;
    private List<BasketMatchFilter> matchFilter;

    public List<BasketRootBean> getMatchData() {
        return matchData;
    }

    public void setMatchData(List<BasketRootBean> matchData) {
        this.matchData = matchData;
    }

    public List<BasketMatchFilter> getMatchFilter() {
        return matchFilter;
    }

    public void setMatchFilter(List<BasketMatchFilter> matchFilter) {
        this.matchFilter = matchFilter;
    }
}
