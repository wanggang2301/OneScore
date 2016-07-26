package com.hhly.mlottery.bean.basket.BasketDatabase;

import java.util.List;

/**
 * description:
 * author: yixq
 * Created by A on 2016/7/21.
 */
public class BasketDatabaseBigSmallBean {

    private List<BasketDatabaseBigSmallDetailsBean> home; // : [],
    private List<BasketDatabaseBigSmallDetailsBean> guest;//: [],
    private List<BasketDatabaseBigSmallDetailsBean> all;//: []

    public List<BasketDatabaseBigSmallDetailsBean> getHome() {
        return home;
    }

    public void setHome(List<BasketDatabaseBigSmallDetailsBean> home) {
        this.home = home;
    }

    public List<BasketDatabaseBigSmallDetailsBean> getGuest() {
        return guest;
    }

    public void setGuest(List<BasketDatabaseBigSmallDetailsBean> guest) {
        this.guest = guest;
    }

    public List<BasketDatabaseBigSmallDetailsBean> getAll() {
        return all;
    }

    public void setAll(List<BasketDatabaseBigSmallDetailsBean> all) {
        this.all = all;
    }
}
