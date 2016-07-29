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
     * homeWinPercent: 0.67,
     * homeLosePercent: 0.27,
     * sizeWinPercent: 0.5,
     * sizeLosePercent: 0.42,
     * asiaWinPercent: 0.58,
     * asiaLosePercent: 0.42
     */

    private Double homeWinPercent;
    private Double homeLosePercent;
    private Double sizeWinPercent;
    private Double sizeLosePercent;
    private Double asiaWinPercent;
    private Double asiaLosePercent;

    private static Double checkNotNull(Double f) {
        return f == null ? 0f : f;
    }

    public static Double getHomeWinPercent(BigDataForecastData data) {
        if (data == null) return 0D;
        return checkNotNull(data.getHomeWinPercent());
    }

    public static Double getHomeLosePercent(BigDataForecastData data) {
        if (data == null) return 0D;
        return checkNotNull(data.getHomeLosePercent());
    }

    public static Double getSizeWinPercent(BigDataForecastData data) {
        if (data == null) return 0D;
        return checkNotNull(data.getSizeWinPercent());
    }

    public static Double getSizeLosePercent(BigDataForecastData data) {
        if (data == null) return 0D;
        return checkNotNull(data.getSizeLosePercent());
    }

    public static Double getAsiaWinPercent(BigDataForecastData data) {
        if (data == null) return 0D;
        return checkNotNull(data.getAsiaWinPercent());
    }

    public static Double getAsiaLosePercent(BigDataForecastData data) {
        if (data == null) return 0D;
        return checkNotNull(data.getAsiaLosePercent());
    }

    public Double getHomeLosePercent() {
        return homeLosePercent;
    }

    public void setHomeLosePercent(Double homeLosePercent) {
        this.homeLosePercent = homeLosePercent;
    }

    public Double getSizeLosePercent() {
        return sizeLosePercent;
    }

    public void setSizeLosePercent(Double sizeLosePercent) {
        this.sizeLosePercent = sizeLosePercent;
    }

    public Double getAsiaLosePercent() {
        return asiaLosePercent;
    }

    public void setAsiaLosePercent(Double asiaLosePercent) {
        this.asiaLosePercent = asiaLosePercent;
    }

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
