package com.hhly.mlottery.bean.footballsecond;

import java.util.List;

/**
 * 主客队走势图的帮助类
 * Created by hhly107 on 2016/3/1.
 */
public class TrendAllBean {

    /**
     * result : 200
     * guestDanger : [4,14,23,33,39,54]
     * homeDanger : [10,17,28,36,46,52]
     * guestCorner : [0,0,1,1,2,4]
     * homeCorner : [1,3,3,4,4,4]
     */

    private String result;
    private List<Integer> guestDanger;
    private List<Integer> homeDanger;
    private List<Integer> guestCorner;
    private List<Integer> homeCorner;

    public void setResult(String result) {
        this.result = result;
    }

    public void setGuestDanger(List<Integer> guestDanger) {
        this.guestDanger = guestDanger;
    }

    public void setHomeDanger(List<Integer> homeDanger) {
        this.homeDanger = homeDanger;
    }

    public void setGuestCorner(List<Integer> guestCorner) {
        this.guestCorner = guestCorner;
    }

    public void setHomeCorner(List<Integer> homeCorner) {
        this.homeCorner = homeCorner;
    }

    public String getResult() {
        return result;
    }

    public List<Integer> getGuestDanger() {
        return guestDanger;
    }

    public List<Integer> getHomeDanger() {
        return homeDanger;
    }

    public List<Integer> getGuestCorner() {
        return guestCorner;
    }

    public List<Integer> getHomeCorner() {
        return homeCorner;
    }
}
