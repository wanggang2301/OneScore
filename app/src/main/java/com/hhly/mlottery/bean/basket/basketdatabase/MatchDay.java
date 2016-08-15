package com.hhly.mlottery.bean.basket.basketdatabase;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描    述：赛程比赛日
 * 作    者：longs@13322.com
 * 时    间：2016/8/9
 */
public class MatchDay implements Parcelable {

    private Date day;
    private List<ScheduledMatch> datas;

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public List<ScheduledMatch> getDatas() {
        return datas;
    }

    public void setDatas(List<ScheduledMatch> datas) {
        this.datas = datas;
    }

    public MatchDay() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.day != null ? this.day.getTime() : -1);
        dest.writeTypedList(this.datas);
    }

    protected MatchDay(Parcel in) {
        long tmpDay = in.readLong();
        this.day = tmpDay == -1 ? null : new Date(tmpDay);
        this.datas = in.createTypedArrayList(ScheduledMatch.CREATOR);
    }

    public static final Creator<MatchDay> CREATOR = new Creator<MatchDay>() {
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
