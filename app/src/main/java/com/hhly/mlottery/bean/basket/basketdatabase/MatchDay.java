package com.hhly.mlottery.bean.basket.basketdatabase;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：赛程比赛日
 * 作    者：longs@13322.com
 * 时    间：2016/8/9
 */
public class MatchDay implements Parcelable {

    private String day;
    private List<ScheduledMatch> datas;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<ScheduledMatch> getDatas() {
        return datas;
    }

    public void setDatas(List<ScheduledMatch> datas) {
        this.datas = datas;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.day);
        dest.writeList(this.datas);
    }

    public MatchDay() {
    }

    protected MatchDay(Parcel in) {
        this.day = in.readString();
        this.datas = new ArrayList<ScheduledMatch>();
        in.readList(this.datas, ScheduledMatch.class.getClassLoader());
    }

    public static final Parcelable.Creator<MatchDay> CREATOR = new Parcelable.Creator<MatchDay>() {
        @Override
        public MatchDay createFromParcel(Parcel source) {
            return new MatchDay(source);
        }

        @Override
        public MatchDay[] newArray(int size) {
            return new MatchDay[size];
        }
    };
}
