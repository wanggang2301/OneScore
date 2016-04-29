package com.hhly.mlottery.bean.basket.BasketDetails;

import com.hhly.mlottery.util.L;

import java.util.List;

/**
 * Created by A on 2016/4/7.
 */
public class BasketAnalyzeMoreBean {

    private List<BasketAnalyzeMoreFutureBean> guestFuture; //	Array
    private List<BasketAnalyzeMoreRecentHistoryBean> guestRecent;//	Array
    private List<BasketAnalyzeMoreRecentHistoryBean> history;   //Array
    private List<BasketAnalyzeMoreFutureBean> homeFuture; //	Array
    private List<BasketAnalyzeMoreRecentHistoryBean> homeRecent; // 	Array

    private String homeTeam;
    private String guestTeam;

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(String guestTeam) {
        this.guestTeam = guestTeam;
    }

    public List<BasketAnalyzeMoreFutureBean> getGuestFuture() {
        return guestFuture;
    }

    public void setGuestFuture(List<BasketAnalyzeMoreFutureBean> guestFuture) {
        this.guestFuture = guestFuture;
    }

    public List<BasketAnalyzeMoreRecentHistoryBean> getGuestRecent() {
        return guestRecent;
    }

    public void setGuestRecent(List<BasketAnalyzeMoreRecentHistoryBean> guestRecent) {
        this.guestRecent = guestRecent;
    }

    public List<BasketAnalyzeMoreRecentHistoryBean> getHistory() {
        return history;
    }

    public void setHistory(List<BasketAnalyzeMoreRecentHistoryBean> history) {
        this.history = history;
    }

    public List<BasketAnalyzeMoreFutureBean> getHomeFuture() {
        return homeFuture;
    }

    public void setHomeFuture(List<BasketAnalyzeMoreFutureBean> homeFuture) {
        this.homeFuture = homeFuture;
    }

    public List<BasketAnalyzeMoreRecentHistoryBean> getHomeRecent() {
        return homeRecent;
    }

    public void setHomeRecent(List<BasketAnalyzeMoreRecentHistoryBean> homeRecent) {
        this.homeRecent = homeRecent;
    }
}
