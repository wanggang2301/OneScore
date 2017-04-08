package com.hhly.mlottery.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 描述:  ${}
 * 作者:  wangg@13322.com
 * 时间:  2016/9/6 18:04
 */
public class LeagueStatisticsTodayChildBean implements Parcelable {

    private String num;
    private String matchName;
    private String total;
    private String winPercent;
    private String drawPercent;
    private String lostPercent;
    private String winCount;
    private String drawCount;
    private String lostCount;
    private String color;
    private String date;

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getWinPercent() {
        return winPercent;
    }

    public void setWinPercent(String winPercent) {
        this.winPercent = winPercent;
    }

    public String getDrawPercent() {
        return drawPercent;
    }

    public void setDrawPercent(String drawPercent) {
        this.drawPercent = drawPercent;
    }

    public String getLostPercent() {
        return lostPercent;
    }

    public void setLostPercent(String lostPercent) {
        this.lostPercent = lostPercent;
    }

    public String getWinCount() {
        return winCount;
    }

    public void setWinCount(String winCount) {
        this.winCount = winCount;
    }

    public String getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(String drawCount) {
        this.drawCount = drawCount;
    }

    public String getLostCount() {
        return lostCount;
    }

    public void setLostCount(String lostCount) {
        this.lostCount = lostCount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.num);
        dest.writeString(this.matchName);
        dest.writeString(this.total);
        dest.writeString(this.winPercent);
        dest.writeString(this.drawPercent);
        dest.writeString(this.lostPercent);
        dest.writeString(this.winCount);
        dest.writeString(this.drawCount);
        dest.writeString(this.lostCount);
        dest.writeString(this.color);
        dest.writeString(this.date);
    }

    public LeagueStatisticsTodayChildBean() {
    }

    protected LeagueStatisticsTodayChildBean(Parcel in) {
        this.num = in.readString();
        this.matchName = in.readString();
        this.total = in.readString();
        this.winPercent = in.readString();
        this.drawPercent = in.readString();
        this.lostPercent = in.readString();
        this.winCount = in.readString();
        this.drawCount = in.readString();
        this.lostCount = in.readString();
        this.color = in.readString();
        this.date = in.readString();
    }

    public static final Parcelable.Creator<LeagueStatisticsTodayChildBean> CREATOR = new Parcelable.Creator<LeagueStatisticsTodayChildBean>() {
        @Override
        public LeagueStatisticsTodayChildBean createFromParcel(Parcel source) {
            return new LeagueStatisticsTodayChildBean(source);
        }

        @Override
        public LeagueStatisticsTodayChildBean[] newArray(int size) {
            return new LeagueStatisticsTodayChildBean[size];
        }
    };
}
