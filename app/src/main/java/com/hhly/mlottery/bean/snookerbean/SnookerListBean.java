package com.hhly.mlottery.bean.snookerbean;

/**
 * Created by yixq on 2016/11/16.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerListBean {

    int itemType;
    String Dates;
    String Round;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getDates() {
        return Dates;
    }

    public void setDates(String date) {
        Dates = date;
    }

    public String getRound() {
        return Round;
    }

    public void setRound(String round) {
        Round = round;
    }

    /**
     * ----------------------------------------------------------------
     */
//    result: 200,
//    data: {}
    private int result;

    public SnookerDataBean getData() {
        return data;
    }

    public void setData(SnookerDataBean data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    private SnookerDataBean data;




}
