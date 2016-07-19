package com.hhly.mlottery.bean.intelligence;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 单个情报
 * <p>
 * Created by loshine on 2016/7/19.
 */
public class BigDataForecastData implements Parcelable {

    /**
     * homeWinPercent : 0.2
     * sizeWinPercent : 0.07
     * asiaWinPercent : 0.13
     */

    private double homeWinPercent;
    private double sizeWinPercent;
    private double asiaWinPercent;

    public double getHomeWinPercent() {
        return homeWinPercent;
    }

    public void setHomeWinPercent(double homeWinPercent) {
        this.homeWinPercent = homeWinPercent;
    }

    public double getSizeWinPercent() {
        return sizeWinPercent;
    }

    public void setSizeWinPercent(double sizeWinPercent) {
        this.sizeWinPercent = sizeWinPercent;
    }

    public double getAsiaWinPercent() {
        return asiaWinPercent;
    }

    public void setAsiaWinPercent(double asiaWinPercent) {
        this.asiaWinPercent = asiaWinPercent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.homeWinPercent);
        dest.writeDouble(this.sizeWinPercent);
        dest.writeDouble(this.asiaWinPercent);
    }

    public BigDataForecastData() {
    }

    protected BigDataForecastData(Parcel in) {
        this.homeWinPercent = in.readDouble();
        this.sizeWinPercent = in.readDouble();
        this.asiaWinPercent = in.readDouble();
    }

    public static final Parcelable.Creator<BigDataForecastData> CREATOR = new Parcelable.Creator<BigDataForecastData>() {
        @Override
        public BigDataForecastData createFromParcel(Parcel source) {
            return new BigDataForecastData(source);
        }

        @Override
        public BigDataForecastData[] newArray(int size) {
            return new BigDataForecastData[size];
        }
    };
}
