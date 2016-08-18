package com.hhly.mlottery.bean.footballDetails.trend;

import java.util.List;

/**
 * 描述:  ${TODO}
 * 作者:  wangg@13322.com
 * 时间:  2016/8/18 11:49
 */
public class AttackBean {

    private List<Bean> home;
    private List<Bean> guest;

    public List<Bean> getHome() {
        return home;
    }

    public void setHome(List<Bean> home) {
        this.home = home;
    }

    public List<Bean> getGuest() {
        return guest;
    }

    public void setGuest(List<Bean> guest) {
        this.guest = guest;
    }
}
