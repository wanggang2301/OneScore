package com.hhly.mlottery.bean.custombean.CustomMineBean;

import java.util.List;

/**
 * Created by yixq on 2016/12/15.
 * mail：yixq@13322.com
 * describe: 我的定制第二层实体（日期）
 */

public class CustomMineScondDataBean {

//    date	2016-12-12
//    matchItems	Array

    private String date;
    private List<CustomMineThirdDataBean> matchItems;

    private int secondType = 1;
    private boolean isUnfold = false; //是否展开

    public boolean isUnfold() {
        return isUnfold;
    }

    public void setUnfold(boolean unfold) {
        isUnfold = unfold;
    }

    public int getSecondType() {
        return secondType;
    }

    public void setSecondType(int secondType) {
        this.secondType = secondType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<CustomMineThirdDataBean> getMatchItems() {
        return matchItems;
    }

    public void setMatchItems(List<CustomMineThirdDataBean> matchItems) {
        this.matchItems = matchItems;
    }
}
