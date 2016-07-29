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

    private Double homeWinPercent;
    private Double sizeWinPercent;
    private Double asiaWinPercent;

    public Double getHomeWinPercent() {
        return homeWinPercent;
    }

    public void setHomeWinPercent(Double homeWinPercent) {
        this.homeWinPercent = homeWinPercent;
    }

    public Double getSizeWinPercent() {
        return sizeWinPercent;
    }

    public void setSizeWinPercent(Double sizeWinPercent) {
        this.sizeWinPercent = sizeWinPercent;
    }

    public Double getAsiaWinPercent() {
        return asiaWinPercent;
    }

    public void setAsiaWinPercent(Double asiaWinPercent) {
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
        this.homeWinPercent = (Double) in.readValue(Double.class.getClassLoader());
        this.sizeWinPercent = (Double) in.readValue(Double.class.getClassLoader());
        this.asiaWinPercent = (Double) in.readValue(Double.class.getClassLoader());
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
