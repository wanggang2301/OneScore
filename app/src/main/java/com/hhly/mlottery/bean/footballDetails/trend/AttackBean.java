package com.hhly.mlottery.bean.footballDetails.trend;

import java.util.List;

/**
 * 描述:  ${}
 * 作者:  wangg@13322.com
 * 时间:  2016/8/18 11:49
 */
public class AttackBean {

    private List<TrendBean> home;
    private List<TrendBean> guest;

    public List<TrendBean> getHome() {
        return home;
    }

    public void setHome(List<TrendBean> home) {
        this.home = home;
    }

    public List<TrendBean> getGuest() {
        return guest;
    }

    public void setGuest(List<TrendBean> guest) {
        this.guest = guest;
    }
}
