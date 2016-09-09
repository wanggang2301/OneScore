package com.hhly.mlottery.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 描述:  足球赛事提点
 * 作者:  wangg@13322.com
 * 时间:  2016/9/6 17:34
 */
public class LeagueStatisticsTodayBean implements Parcelable {

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private String startDate;
    private String endDate;
    private List<LeagueStatisticsTodayChildBean> statistics;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<LeagueStatisticsTodayChildBean> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<LeagueStatisticsTodayChildBean> statistics) {
        this.statistics = statistics;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.result);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeTypedList(this.statistics);
    }

    public LeagueStatisticsTodayBean() {
    }

    protected LeagueStatisticsTodayBean(Parcel in) {
        this.result = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.statistics = in.createTypedArrayList(LeagueStatisticsTodayChildBean.CREATOR);
    }

    public static final Parcelable.Creator<LeagueStatisticsTodayBean> CREATOR = new Parcelable.Creator<LeagueStatisticsTodayBean>() {
        @Override
        public LeagueStatisticsTodayBean createFromParcel(Parcel source) {
            return new LeagueStatisticsTodayBean(source);
        }

        @Override
        public LeagueStatisticsTodayBean[] newArray(int size) {
            return new LeagueStatisticsTodayBean[size];
        }
    };
}
