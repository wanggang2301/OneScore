package com.hhly.mlottery.bean.footballDetails.footballdatabasebean;

/**
 * Created by Administrator on 2016/9/7.
 */

public class ScheduleDataBean {
    /**
     *  round: "1",
        current: false
     */
    private String round;
    private boolean current;

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }
}
