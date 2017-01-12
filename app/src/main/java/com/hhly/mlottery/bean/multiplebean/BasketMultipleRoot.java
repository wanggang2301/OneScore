package com.hhly.mlottery.bean.multiplebean;

import com.hhly.mlottery.bean.basket.BasketMatchBean;
import com.hhly.mlottery.bean.basket.BasketMatchFilter;
import com.hhly.mlottery.bean.basket.BasketRootBean;

import java.util.List;

/**
 * Created by A on 2016/1/11.
 */
public class BasketMultipleRoot {

    private List<BasketMatchBean> matchData;
    private List<BasketMatchFilter> matchFilter;

    public List<BasketMatchBean> getMatchData() {
        return matchData;
    }

    public void setMatchData(List<BasketMatchBean> matchData) {
        this.matchData = matchData;
    }

    public List<BasketMatchFilter> getMatchFilter() {
        return matchFilter;
    }

    public void setMatchFilter(List<BasketMatchFilter> matchFilter) {
        this.matchFilter = matchFilter;
    }

}
