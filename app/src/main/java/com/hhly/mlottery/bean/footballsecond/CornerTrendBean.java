package com.hhly.mlottery.bean.footballsecond;

import java.util.List;

/**
 * 角球实体类
 * Created by hhly107 on 2015/12/31.
 */
public class CornerTrendBean {

    private String result;// 返回处理结果
    private List<Integer> guestCorner;// 主队角球
    private List<Integer> homeCorner;// 客队角球

    public String getResult() {
        return result;
    }

    public List<Integer> getGuestCorner() {
        return guestCorner;
    }

    public List<Integer> getHomeCorner() {
        return homeCorner;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setGuestCorner(List<Integer> guestCorner) {
        this.guestCorner = guestCorner;
    }

    public void setHomeCorner(List<Integer> homeCorner) {
        this.homeCorner = homeCorner;
    }

    @Override
    public String toString() {
        return "CornerTrendBean{" +
                "result='" + result + '\'' +
                ", guestCorner=" + guestCorner +
                ", homeCorner=" + homeCorner +
                '}';
    }
}
