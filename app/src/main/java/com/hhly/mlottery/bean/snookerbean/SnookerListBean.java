package com.hhly.mlottery.bean.snookerbean;

/**
 * Created by yixq on 2016/11/16.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerListBean {

    /**
     * 区分item类型
     */
    int itemType;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    /**
     * ----------------------------------------------------------------
     */
//    result: 200,
//    data: {}
    private String result;

    public SnookerDataBean getData() {
        return data;
    }

    public void setData(SnookerDataBean data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private SnookerDataBean data;




}
