package com.hhly.mlottery.bean;


import java.util.List;


/**
 * Created by yuely198 on 2017/3/24.
 */

public class BasketballSearchBean {

    private int result;
    private List<BasketballItemSearchBean> data ;

    public List<BasketballItemSearchBean> getData() {
        return data;
    }

    public void setData(List<BasketballItemSearchBean> data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }



}
