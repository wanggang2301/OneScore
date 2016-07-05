package com.hhly.mlottery.bean.footballDetails;

/**
 * description:
 * author: yixq
 * Created by A on 2016/6/13.
 */
public class FootballAnalyzeFuture {
    private String date ; //": "16-03-03",
    private String leagueName; //: "西甲",
    private String homeName; //: "皇马",
    private String guestName; //: "皇家社会",
    private String diffDays; //: 11

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

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

    public String getDiffDays() {
        return diffDays;
    }

    public void setDiffDays(String diffDays) {
        this.diffDays = diffDays;
    }
}
