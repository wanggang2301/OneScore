package com.hhly.mlottery.bean;

/**
 * Created by yuely198 on 2017/2/27.
 */

public class SnookerNoDataBean {
    private  String noData;
    boolean  isNoData;

    public SnookerNoDataBean(String leagueProfile, boolean b) {
        this.noData=leagueProfile;
        this.isNoData=b;
    }

    public boolean isNoData() {
        return isNoData;
    }

    public void setNoData(boolean noData) {
        isNoData = noData;
    }

    public String getNoData() {
        return noData;
    }

    public void setNoData(String noData) {
        this.noData = noData;
    }
}
