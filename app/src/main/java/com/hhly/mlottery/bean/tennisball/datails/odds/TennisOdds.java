package com.hhly.mlottery.bean.tennisball.datails.odds;

import java.util.List;

/**
 * desc:网球指数bean
 * Created by 107_tangrr on 2017/3/30 0030.
 */

public class TennisOdds {

    private String result;
    private List<TennisDataBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<TennisDataBean> getData() {
        return data;
    }

    public void setData(List<TennisDataBean> data) {
        this.data = data;
    }
}
