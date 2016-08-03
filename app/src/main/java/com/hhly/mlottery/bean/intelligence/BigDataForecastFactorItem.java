package com.hhly.mlottery.bean.intelligence;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/7/19.
 */
public class BigDataForecastFactorItem implements Parcelable {

    private double history = 0.5;
    private double home = 0.25;
    private double guest = 0.25;

    private double historyTemp = 0.5;
    private double homeTemp = 0.25;
    private double guestTemp = 0.25;

    public BigDataForecastFactorItem() {
    }

    public BigDataForecastFactorItem(double history, double home, double guest) {
        this.history = history;
        this.historyTemp = history;
        this.home = home;
        this.homeTemp = home;
        this.guest = guest;
        this.guestTemp = guest;
    }

    public double getHistoryTemp() {
        return historyTemp;
    }

    public void setHistoryTemp(double historyTemp) {
        this.historyTemp = historyTemp;
    }

    public double getHomeTemp() {
        return homeTemp;
    }

    public void setHomeTemp(double homeTemp) {
        this.homeTemp = homeTemp;
    }

    public double getGuestTemp() {
        return guestTemp;
    }

    public void setGuestTemp(double guestTemp) {
        this.guestTemp = guestTemp;
    }

    public double getHistory() {
        return history;
    }

    public void setHistory(double history) {
        this.history = history;
    }

    public double getHome() {
        return home;
    }

    public void setHome(double home) {
        this.home = home;
    }

    public double getGuest() {
        return guest;
    }

    public void setGuest(double guest) {
        this.guest = guest;
    }

    /**
     * 将缓存更新到内容
     */
    public void updateTemp() {
        this.history = historyTemp;
        this.home = homeTemp;
        this.guest = guestTemp;
    }

    /**
     * 去除缓存
     */
    public void refreshTemp() {
        this.historyTemp = history;
        this.homeTemp = home;
        this.guestTemp = guest;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.history);
        dest.writeDouble(this.home);
        dest.writeDouble(this.guest);
        dest.writeDouble(this.historyTemp);
        dest.writeDouble(this.homeTemp);
        dest.writeDouble(this.guestTemp);
    }

    protected BigDataForecastFactorItem(Parcel in) {
        this.history = in.readDouble();
        this.home = in.readDouble();
        this.guest = in.readDouble();
        this.historyTemp = in.readDouble();
        this.homeTemp = in.readDouble();
        this.guestTemp = in.readDouble();
    }

    public static final Creator<BigDataForecastFactorItem> CREATOR = new Creator<BigDataForecastFactorItem>() {
        @Override
        public BigDataForecastFactorItem createFromParcel(Parcel source) {
            return new BigDataForecastFactorItem(source);
        }

        @Override
        public BigDataForecastFactorItem[] newArray(int size) {
            return new BigDataForecastFactorItem[size];
        }
    };
}
