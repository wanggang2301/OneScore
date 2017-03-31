package com.hhly.mlottery.bean.tennisball.datails.odds;

/**
 * desc:
 * Created by 107_tangrr on 2017/3/30 0030.
 */

public class TennisDetailsBean {
    private String time;
    private Object score;
    private double homeOdd;
    private double hand;
    private double guestOdd;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Object getScore() {
        return score;
    }

    public void setScore(Object score) {
        this.score = score;
    }

    public double getHomeOdd() {
        return homeOdd;
    }

    public void setHomeOdd(double homeOdd) {
        this.homeOdd = homeOdd;
    }

    public double getHand() {
        return hand;
    }

    public void setHand(double hand) {
        this.hand = hand;
    }

    public double getGuestOdd() {
        return guestOdd;
    }

    public void setGuestOdd(double guestOdd) {
        this.guestOdd = guestOdd;
    }
}
