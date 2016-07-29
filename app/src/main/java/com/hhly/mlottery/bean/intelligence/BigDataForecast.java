package com.hhly.mlottery.bean.intelligence;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/7/19.
 */
public class BigDataForecast implements Parcelable {

    private BigDataForecastData battleHistory; /* 历史交锋 */

    private BigDataForecastData homeRecent; /* 主队近期战绩 */
    private BigDataForecastData guestRecent; /* 客队近期战绩 */

    public BigDataForecastData getBattleHistory() {
        return battleHistory;
    }

    public void setBattleHistory(BigDataForecastData battleHistory) {
        this.battleHistory = battleHistory;
    }

    public BigDataForecastData getHomeRecent() {
        return homeRecent;
    }

    public void setHomeRecent(BigDataForecastData homeRecent) {
        this.homeRecent = homeRecent;
    }

    public BigDataForecastData getGuestRecent() {
        return guestRecent;
    }

    public void setGuestRecent(BigDataForecastData guestRecent) {
        this.guestRecent = guestRecent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.battleHistory, flags);
        dest.writeParcelable(this.homeRecent, flags);
        dest.writeParcelable(this.guestRecent, flags);
    }

    public BigDataForecast() {
    }

    protected BigDataForecast(Parcel in) {
        this.battleHistory = in.readParcelable(BigDataForecastData.class.getClassLoader());
        this.homeRecent = in.readParcelable(BigDataForecastData.class.getClassLoader());
        this.guestRecent = in.readParcelable(BigDataForecastData.class.getClassLoader());
    }

    public static final Parcelable.Creator<BigDataForecast> CREATOR = new Parcelable.Creator<BigDataForecast>() {
        @Override
        public BigDataForecast createFromParcel(Parcel source) {
            return new BigDataForecast(source);
        }

        @Override
        public BigDataForecast[] newArray(int size) {
            return new BigDataForecast[size];
        }
    };
}
