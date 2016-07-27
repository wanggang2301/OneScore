package com.hhly.mlottery.bean.basket.basketdatabase;

import java.util.List;

/**
 * description:
 * author: yixq
 * Created by A on 2016/7/21.
 */
public class BasketDatabaseHandicapBean {

    private List<BasketDatabaseHandicapDetailsBean> home;//: [],
    private List<BasketDatabaseHandicapDetailsBean> guest;//: [],
    private List<BasketDatabaseHandicapDetailsBean> all;//: []

    public List<BasketDatabaseHandicapDetailsBean> getHome() {
        return home;
    }

    public void setHome(List<BasketDatabaseHandicapDetailsBean> home) {
        this.home = home;
    }

    public List<BasketDatabaseHandicapDetailsBean> getGuest() {
        return guest;
    }

    public void setGuest(List<BasketDatabaseHandicapDetailsBean> guest) {
        this.guest = guest;
    }

    public List<BasketDatabaseHandicapDetailsBean> getAll() {
        return all;
    }

    public void setAll(List<BasketDatabaseHandicapDetailsBean> all) {
        this.all = all;
    }
}
