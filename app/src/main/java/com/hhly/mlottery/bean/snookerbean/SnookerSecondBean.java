package com.hhly.mlottery.bean.snookerbean;

import java.util.List;

/**
 * Created by yixq on 2016/11/24.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerSecondBean {

    //    result: 200,
//    data: {}

    private String result;
    private List<SnookerLeaguesBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<SnookerLeaguesBean> getData() {
        return data;
    }

    public void setData(List<SnookerLeaguesBean> data) {
        this.data = data;
    }


}
