package com.hhly.mlottery.bean.footballDetails.footballdatabasebean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/7.
 */

public class ScheduleRaceBean implements Parcelable {

    /*
            day: 1473436800000,
            dayStr: "2016-09-10",
            datas: []
         */
    private String dayStr;
    private Date day;
    private List<ScheduleDatasBean> datas;
    public String getDayStr() {
        return dayStr;
    }

    public void setDayStr(String dayStr) {
        this.dayStr = dayStr;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public List<ScheduleDatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<ScheduleDatasBean> datas) {
        this.datas = datas;
    }

    public ScheduleRaceBean() {
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

    protected ScheduleRaceBean(Parcel in) {
        long tmpDay = in.readLong();
        this.day = tmpDay == -1 ? null : new Date(tmpDay);
        this.datas = in.createTypedArrayList(ScheduleDatasBean.CREATOR);
    }

    public static final Parcelable.Creator<ScheduleRaceBean> CREATOR = new Parcelable.Creator<ScheduleRaceBean>() {
        @Override
        public ScheduleRaceBean createFromParcel(Parcel source) {
            return new ScheduleRaceBean(source);
        }

        @Override
        public ScheduleRaceBean[] newArray(int size) {
            return new ScheduleRaceBean[size];
        }
    };
}
