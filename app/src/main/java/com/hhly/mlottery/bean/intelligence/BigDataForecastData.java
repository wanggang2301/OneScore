package com.hhly.mlottery.bean.intelligence;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/7/19.
 */
public class BigDataForecastData implements Parcelable {

    /**
     * homeWinPercent : 0.2
     * sizeWinPercent : 0.07
     * asiaWinPercent : 0.13
     */

    private Float homeWinPercent;
    private Float sizeWinPercent;
    private Float asiaWinPercent;

    public Float getHomeWinPercent() {
        return homeWinPercent;
    }

    public void setHomeWinPercent(Float homeWinPercent) {
        this.homeWinPercent = homeWinPercent;
    }

    public Float getSizeWinPercent() {
        return sizeWinPercent;
    }

    public void setSizeWinPercent(Float sizeWinPercent) {
        this.sizeWinPercent = sizeWinPercent;
    }

    public Float getAsiaWinPercent() {
        return asiaWinPercent;
    }

    public void setAsiaWinPercent(Float asiaWinPercent) {
        this.asiaWinPercent = asiaWinPercent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.homeWinPercent);
        dest.writeValue(this.sizeWinPercent);
        dest.writeValue(this.asiaWinPercent);
    }

    public BigDataForecastData() {
    }

    protected BigDataForecastData(Parcel in) {
        this.homeWinPercent = (Float) in.readValue(Float.class.getClassLoader());
        this.sizeWinPercent = (Float) in.readValue(Float.class.getClassLoader());
        this.asiaWinPercent = (Float) in.readValue(Float.class.getClassLoader());
    }

    public static final Creator<BigDataForecastData> CREATOR = new Creator<BigDataForecastData>() {
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
