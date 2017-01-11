package com.hhly.mlottery.bean.multiscreenview;

/**
 * @author: Wangg
 * @Name：MultiScreenBasketMatchScoreBean
 * @Description:
 * @Created on:2017/1/10  9:57.
 */

public class MultiScreenBasketMatchScoreBean {

    private int matchStatus;
    private int section;


    private String remainTime; //当前时间


    private int homeScore;
    private int guestScore;
    private int home1;
    private int home2;
    private int home3;
    private int home4;
    private int guest1;
    private int guest2;
    private int guest3;
    private int guest4;
    private int homeOt1;
    private int homeOt2;
    private int homeOt3;
    private int guestOt1;
    private int guestOt2;
    private int guestOt3;


    public MultiScreenBasketMatchScoreBean(){}

    public MultiScreenBasketMatchScoreBean(int matchStatus, int section, String remainTime, int homeScore, int guestScore, int home1, int home2, int home3, int home4, int guest1, int guest2, int guest3, int guest4, int homeOt1, int homeOt2, int homeOt3, int guestOt1, int guestOt2, int guestOt3, int addTime) {
        this.matchStatus = matchStatus;
        this.section = section;
        this.remainTime = remainTime;
        this.homeScore = homeScore;
        this.guestScore = guestScore;
        this.home1 = home1;
        this.home2 = home2;
        this.home3 = home3;
        this.home4 = home4;
        this.guest1 = guest1;
        this.guest2 = guest2;
        this.guest3 = guest3;
        this.guest4 = guest4;
        this.homeOt1 = homeOt1;
        this.homeOt2 = homeOt2;
        this.homeOt3 = homeOt3;
        this.guestOt1 = guestOt1;
        this.guestOt2 = guestOt2;
        this.guestOt3 = guestOt3;
        this.addTime = addTime;
    }

    public int getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(int matchStatus) {
        this.matchStatus = matchStatus;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(int guestScore) {
        this.guestScore = guestScore;
    }

    public int getHome2() {
        return home2;
    }

    public void setHome2(int home2) {
        this.home2 = home2;
    }

    public int getHome1() {
        return home1;
    }

    public void setHome1(int home1) {
        this.home1 = home1;
    }

    public int getHome3() {
        return home3;
    }

    public void setHome3(int home3) {
        this.home3 = home3;
    }

    public int getHome4() {
        return home4;
    }

    public void setHome4(int home4) {
        this.home4 = home4;
    }

    public int getGuest1() {
        return guest1;
    }

    public void setGuest1(int guest1) {
        this.guest1 = guest1;
    }

    public int getGuest2() {
        return guest2;
    }

    public void setGuest2(int guest2) {
        this.guest2 = guest2;
    }

    public int getGuest3() {
        return guest3;
    }

    public void setGuest3(int guest3) {
        this.guest3 = guest3;
    }

    public int getGuest4() {
        return guest4;
    }

    public void setGuest4(int guest4) {
        this.guest4 = guest4;
    }

    public int getHomeOt1() {
        return homeOt1;
    }

    public void setHomeOt1(int homeOt1) {
        this.homeOt1 = homeOt1;
    }

    public int getHomeOt2() {
        return homeOt2;
    }

    public void setHomeOt2(int homeOt2) {
        this.homeOt2 = homeOt2;
    }

    public int getHomeOt3() {
        return homeOt3;
    }

    public void setHomeOt3(int homeOt3) {
        this.homeOt3 = homeOt3;
    }

    public int getGuestOt1() {
        return guestOt1;
    }

    public void setGuestOt1(int guestOt1) {
        this.guestOt1 = guestOt1;
    }

    public int getGuestOt2() {
        return guestOt2;
    }

    public void setGuestOt2(int guestOt2) {
        this.guestOt2 = guestOt2;
    }

    public int getGuestOt3() {
        return guestOt3;
    }

    public void setGuestOt3(int guestOt3) {
        this.guestOt3 = guestOt3;
    }

    public int getAddTime() {
        return addTime;
    }

    public void setAddTime(int addTime) {
        this.addTime = addTime;
    }

    private int addTime;   //加时时间
}
