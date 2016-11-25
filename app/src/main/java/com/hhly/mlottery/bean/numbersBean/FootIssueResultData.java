package com.hhly.mlottery.bean.numbersBean;

import java.io.Serializable;

/**
 * desc:
 * Created by 107_tangrr on 2016/11/22 0022.
 */

public class FootIssueResultData implements Serializable{

    private String homeName;// 主队名称
    private String guestName;// 客队名称
    private String kickOffTime;// 比赛日期的时间
    private String fullScore;// 全场比分
    private String halfScore;// 半场比分
    private String fullDrawcode;// 全场彩果0输，1平，3赢
    private String halfDrawcode;// 半场彩果0输，1平，3赢
    private String sc_round;// 序号

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getKickOffTime() {
        return kickOffTime;
    }

    public void setKickOffTime(String kickOffTime) {
        this.kickOffTime = kickOffTime;
    }

    public String getFullScore() {
        return fullScore;
    }

    public void setFullScore(String fullScore) {
        this.fullScore = fullScore;
    }

    public String getHalfScore() {
        return halfScore;
    }

    public void setHalfScore(String halfScore) {
        this.halfScore = halfScore;
    }

    public String getFullDrawcode() {
        return fullDrawcode;
    }

    public void setFullDrawcode(String fullDrawcode) {
        this.fullDrawcode = fullDrawcode;
    }

    public String getHalfDrawcode() {
        return halfDrawcode;
    }

    public void setHalfDrawcode(String halfDrawcode) {
        this.halfDrawcode = halfDrawcode;
    }

    public String getSc_round() {
        return sc_round;
    }

    public void setSc_round(String sc_round) {
        this.sc_round = sc_round;
    }
}
