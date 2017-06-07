package com.hhly.mlottery.bean.basket;

import java.util.List;

/**
 * Created by A on 2016/1/11.
 */
public class BasketNewRootBean {

    private String filerDate;

    public String getFilerDate() {
        return filerDate;
    }

    public void setFilerDate(String filerDate) {
        this.filerDate = filerDate;
    }

    private List<BasketRootBean> matchData;
    private List<BasketNewFilterBean> matchFilter;

    public List<BasketRootBean> getMatchData() {
        return matchData;
    }

    public void setMatchData(List<BasketRootBean> matchData) {
        this.matchData = matchData;
    }

    public List<BasketNewFilterBean> getMatchFilter() {
        return matchFilter;
    }

    public void setMatchFilter(List<BasketNewFilterBean> matchFilter) {
        this.matchFilter = matchFilter;
    }
}
