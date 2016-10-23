package com.hhly.mlottery.bean.footballDetails.footballdatabasebean;

import java.util.List;

/**
 * author: yixq
 * Created by Administrator on 2016/10/9.
 * 足球资料库联赛之最Bean
 */

public class DatabaseTopBean {

    /*
        defTop: [],
        atkTop: [],
        atkWeak: [],
        defWeak: []
     */
    private List<TopDetailsBean> defTop;
    private List<TopDetailsBean> atkTop;
    private List<TopDetailsBean> atkWeak;
    private List<TopDetailsBean> defWeak;

    private List<TopDetailsBean> winTop;

    public List<TopDetailsBean> getWinTop() {
        return winTop;
    }

    public void setWinTop(List<TopDetailsBean> winTop) {
        this.winTop = winTop;
    }

    public List<TopDetailsBean> getDefTop() {
        return defTop;
    }

    public void setDefTop(List<TopDetailsBean> defTop) {
        this.defTop = defTop;
    }

    public List<TopDetailsBean> getAtkTop() {
        return atkTop;
    }

    public void setAtkTop(List<TopDetailsBean> atkTop) {
        this.atkTop = atkTop;
    }

    public List<TopDetailsBean> getAtkWeak() {
        return atkWeak;
    }

    public void setAtkWeak(List<TopDetailsBean> atkWeak) {
        this.atkWeak = atkWeak;
    }

    public List<TopDetailsBean> getDefWeak() {
        return defWeak;
    }

    public void setDefWeak(List<TopDetailsBean> defWeak) {
        this.defWeak = defWeak;
    }
}
