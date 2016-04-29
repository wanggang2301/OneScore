package com.hhly.mlottery.bean.websocket;

/**
 * Created by A on 2016/1/8.
 */
public class WebBasketData {

    private boolean addTechnic; //	false
    private int addTime; //	0
    private int guest1; //	14
    private int guest2;	 //12
    private int guest3;	//12
    private int guest4;	//17
    private int guestScore;	//55
    private int home1;	//17
    private int home2;	//13
    private int home3;	//18
    private int home4;	//15
    private int homeScore;	//63
    private String remainTime; //	01:46
    private int section;	//4
    private int status;	//4

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isAddTechnic() {
        return addTechnic;
    }

    public void setAddTechnic(boolean addTechnic) {
        this.addTechnic = addTechnic;
    }

    public int getAddTime() {
        return addTime;
    }

    public void setAddTime(int addTime) {
        this.addTime = addTime;
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

    public int getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(int guestScore) {
        this.guestScore = guestScore;
    }

    public int getHome1() {
        return home1;
    }

    public void setHome1(int home1) {
        this.home1 = home1;
    }

    public int getHome2() {
        return home2;
    }

    public void setHome2(int home2) {
        this.home2 = home2;
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

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }


}
