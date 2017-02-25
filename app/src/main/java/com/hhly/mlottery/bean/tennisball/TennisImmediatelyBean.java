package com.hhly.mlottery.bean.tennisball;

import java.util.List;

/**
 * desc:网球比分实体类
 * Created by 107_tangrr on 2017/2/21 0021.
 */

public class TennisImmediatelyBean {

    private int result;
    private List<MatchDataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<MatchDataBean> getData() {
        return data;
    }

    public void setData(List<MatchDataBean> data) {
        this.data = data;
    }
}
