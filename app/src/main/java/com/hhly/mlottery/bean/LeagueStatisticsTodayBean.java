package com.hhly.mlottery.bean;

import java.util.List;

/**
 * 描述:  足球赛事提点
 * 作者:  wangg@13322.com
 * 时间:  2016/9/6 17:34
 */
public class LeagueStatisticsTodayBean {

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private String startDate;
    private String endDate;
    private List<LeagueStatisticsTodayChildBean> statistics;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<LeagueStatisticsTodayChildBean> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<LeagueStatisticsTodayChildBean> statistics) {
        this.statistics = statistics;
    }


}
