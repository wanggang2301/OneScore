package com.hhly.mlottery.bean.footballDetails.footballdatabasebean;

/**
 * description:
 * author: yixq
 * Created by A on 2016/9/19.
 * 足球资料库赛程选择实体类
 */

public class DataBean {

    /**
     *  round: "1",
        isCurrent: 0
     */
    private String round;
    private int isCurrent;

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public int getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(int isCurrent) {
        this.isCurrent = isCurrent;
    }
}
