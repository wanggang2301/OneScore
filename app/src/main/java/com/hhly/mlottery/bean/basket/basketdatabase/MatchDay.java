package com.hhly.mlottery.bean.basket.basketdatabase;

import java.util.List;

/**
 * 描    述：赛程比赛日
 * 作    者：longs@13322.com
 * 时    间：2016/8/9
 */
public class MatchDay {

    private String day;
    private List<ScheduledMatch> datas;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<ScheduledMatch> getDatas() {
        return datas;
    }

    public void setDatas(List<ScheduledMatch> datas) {
        this.datas = datas;
    }
}
