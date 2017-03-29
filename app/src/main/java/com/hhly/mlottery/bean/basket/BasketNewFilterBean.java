package com.hhly.mlottery.bean.basket;

import java.util.List;

/**
 * Created by yixq on 2017/3/22.
 * mail：yixq@13322.com
 * describe:
 */

public class BasketNewFilterBean {

//    date: "2017-03-21",
//    filterDto: []

    private String date;
    private List<BasketMatchFilter> filterDto;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<BasketMatchFilter> getFilterDto() {
        return filterDto;
    }

    public void setFilterDto(List<BasketMatchFilter> filterDto) {
        this.filterDto = filterDto;
    }
}
