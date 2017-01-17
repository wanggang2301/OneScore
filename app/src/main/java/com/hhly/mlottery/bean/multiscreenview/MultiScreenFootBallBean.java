package com.hhly.mlottery.bean.multiscreenview;

/**
 * @author: Wangg
 * @Name：MultiScreenFootBallBean
 * @Description:
 * @Created on:2017/1/5  16:46.
 */

public class MultiScreenFootBallBean {

    private String liveStatus;

    private String bg;

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    private String home_icon;
    private String home_name;
    private String guest_icon;
    private String guest_name;
    private String mMatchType1;
    private String mMatchType2;
    private String date;
    private int homeScore;
    private int guestScore;


    public int getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(int guestScore) {
        this.guestScore = guestScore;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }



    public String getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }

    public String getHome_icon() {
        return home_icon;
    }

    public void setHome_icon(String home_icon) {
        this.home_icon = home_icon;
    }

    public String getHome_name() {
        return home_name;
    }

    public void setHome_name(String home_name) {
        this.home_name = home_name;
    }

    public String getGuest_icon() {
        return guest_icon;
    }

    public void setGuest_icon(String guest_icon) {
        this.guest_icon = guest_icon;
    }

    public String getGuest_name() {
        return guest_name;
    }

    public void setGuest_name(String guest_name) {
        this.guest_name = guest_name;
    }


    public String getmMatchType1() {
        return mMatchType1;
    }

    public void setmMatchType1(String mMatchType1) {
        this.mMatchType1 = mMatchType1;
    }

    public String getmMatchType2() {
        return mMatchType2;
    }

    public void setmMatchType2(String mMatchType2) {
        this.mMatchType2 = mMatchType2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
