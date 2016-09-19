package com.hhly.mlottery.bean.footballDetails.footballdatabasebean;

/**
 * description:
 * author: yixq
 * Created by A on 2016/9/19.
 * 足球资料库赛程选择实体类
 */

public class DataBean {

    private String dataName;
    private boolean isChoose; //是否选中

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
