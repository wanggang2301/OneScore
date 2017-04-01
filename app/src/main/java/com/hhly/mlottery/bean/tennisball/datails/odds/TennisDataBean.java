package com.hhly.mlottery.bean.tennisball.datails.odds;

import java.util.List;

/**
 * desc:
 * Created by 107_tangrr on 2017/3/30 0030.
 */

public class TennisDataBean {

    private String name;
    private List<TennisDetailsBean> details;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TennisDetailsBean> getDetails() {
        return details;
    }

    public void setDetails(List<TennisDetailsBean> details) {
        this.details = details;
    }
}
