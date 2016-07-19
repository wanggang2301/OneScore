package com.hhly.mlottery.bean.intelligence;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 自定义算法加成系数
 * <p/>
 * Created by loshine on 2016/7/19.
 */
public class BigDataForecastFactor implements Parcelable {

    private BigDataForecastFactorItem host;
    private BigDataForecastFactorItem size;
    private BigDataForecastFactorItem asia;

    public BigDataForecastFactor() {
        host = new BigDataForecastFactorItem();
        size = new BigDataForecastFactorItem();
        asia = new BigDataForecastFactorItem();
    }

    public BigDataForecastFactorItem getHost() {
        return host;
    }

    public void setHost(BigDataForecastFactorItem host) {
        this.host = host;
    }

    public BigDataForecastFactorItem getSize() {
        return size;
    }

    public void setSize(BigDataForecastFactorItem size) {
        this.size = size;
    }

    public BigDataForecastFactorItem getAsia() {
        return asia;
    }

    public void setAsia(BigDataForecastFactorItem asia) {
        this.asia = asia;
    }

    /**
     * 计算主胜率
     *
     * @param forecast
     * @return
     */
    public double computeHostWinRate(BigDataForecast forecast) {
        return computeHostWinRate(forecast, false);
    }

    /**
     * 计算主胜率
     *
     * @param forecast
     * @param useTemp
     * @return
     */
    public double computeHostWinRate(BigDataForecast forecast, boolean useTemp) {

        BigDataForecastData battleHistory = forecast.getBattleHistory();
        BigDataForecastData homeRecent = forecast.getHomeRecent();
        BigDataForecastData guestRecent = forecast.getGuestRecent();

        return battleHistory.getHomeWinPercent() * (useTemp ? host.getHistoryTemp() : host.getHistory())
                + homeRecent.getHomeWinPercent() * (useTemp ? host.getHomeTemp() : host.getHome())
                + guestRecent.getHomeWinPercent() * (useTemp ? host.getGuestTemp() : host.getGuest());
    }

    /**
     * 计算大球率
     *
     * @param forecast
     * @return
     */
    public double computeSizeWinRate(BigDataForecast forecast) {
        return computeSizeWinRate(forecast, false);
    }

    /**
     * 计算主胜率
     *
     * @param forecast
     * @param useTemp
     * @return
     */
    public double computeSizeWinRate(BigDataForecast forecast, boolean useTemp) {

        BigDataForecastData battleHistory = forecast.getBattleHistory();
        BigDataForecastData homeRecent = forecast.getHomeRecent();
        BigDataForecastData guestRecent = forecast.getGuestRecent();

        return battleHistory.getSizeWinPercent() * (useTemp ? size.getHistoryTemp() : size.getHistory())
                + homeRecent.getSizeWinPercent() * (useTemp ? size.getHomeTemp() : size.getHome())
                + guestRecent.getSizeWinPercent() * (useTemp ? size.getGuestTemp() : size.getGuest());
    }

    /**
     * 计算主胜率
     *
     * @param forecast
     * @return
     */
    public double computeAsiaWinRate(BigDataForecast forecast) {
        return computeAsiaWinRate(forecast, false);
    }

    /**
     * 计算主胜率
     *
     * @param forecast
     * @param useTemp
     * @return
     */
    public double computeAsiaWinRate(BigDataForecast forecast, boolean useTemp) {

        BigDataForecastData battleHistory = forecast.getBattleHistory();
        BigDataForecastData homeRecent = forecast.getHomeRecent();
        BigDataForecastData guestRecent = forecast.getGuestRecent();

        return battleHistory.getAsiaWinPercent() * (useTemp ? asia.getHistoryTemp() : asia.getHistory())
                + homeRecent.getAsiaWinPercent() * (useTemp ? asia.getHomeTemp() : asia.getHome())
                + guestRecent.getAsiaWinPercent() * (useTemp ? asia.getGuestTemp() : asia.getGuest());
    }

    /**
     * 更新缓存到本体数据
     */
    public void updateTemp() {
        host.updateTemp();
        size.updateTemp();
        asia.updateTemp();
    }

    /**
     * 刷掉缓存
     */
    public void refreshTemp() {
        host.refreshTemp();
        size.refreshTemp();
        asia.refreshTemp();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.host, flags);
        dest.writeParcelable(this.size, flags);
        dest.writeParcelable(this.asia, flags);
    }

    protected BigDataForecastFactor(Parcel in) {
        this.host = in.readParcelable(BigDataForecastFactorItem.class.getClassLoader());
        this.size = in.readParcelable(BigDataForecastFactorItem.class.getClassLoader());
        this.asia = in.readParcelable(BigDataForecastFactorItem.class.getClassLoader());
    }

    public static final Parcelable.Creator<BigDataForecastFactor> CREATOR = new Parcelable.Creator<BigDataForecastFactor>() {
        @Override
        public BigDataForecastFactor createFromParcel(Parcel source) {
            return new BigDataForecastFactor(source);
        }

        @Override
        public BigDataForecastFactor[] newArray(int size) {
            return new BigDataForecastFactor[size];
        }
    };
}
