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

        Float historyHomeWinPercent = getHomeWinPercent(forecast.getBattleHistory());
        Float hostHomeWinPercent = getHomeWinPercent(forecast.getHomeRecent());
        Float guestHomeWinPercent = getHomeWinPercent(forecast.getGuestRecent());

        return historyHomeWinPercent * (useTemp ? host.getHistoryTemp() : host.getHistory())
                + hostHomeWinPercent * (useTemp ? host.getHomeTemp() : host.getHome())
                + guestHomeWinPercent * (useTemp ? host.getGuestTemp() : host.getGuest());
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

        Float historySizeWinPercent = getSizeWinPercent(forecast.getBattleHistory());
        Float hostSizeWinPercent = getSizeWinPercent(forecast.getHomeRecent());
        Float guestSizeWinPercent = getSizeWinPercent(forecast.getGuestRecent());

        return historySizeWinPercent * (useTemp ? size.getHistoryTemp() : size.getHistory())
                + hostSizeWinPercent * (useTemp ? size.getHomeTemp() : size.getHome())
                + guestSizeWinPercent * (useTemp ? size.getGuestTemp() : size.getGuest());
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

        Float historyAsiaWinPercent = getAsiaWinPercent(battleHistory);
        Float hostAsiaWinPercent = getAsiaWinPercent(homeRecent);
        Float guestAsiaWinPercent = getAsiaWinPercent(guestRecent);

        return historyAsiaWinPercent * (useTemp ? asia.getHistoryTemp() : asia.getHistory())
                + hostAsiaWinPercent * (useTemp ? asia.getHomeTemp() : asia.getHome())
                + guestAsiaWinPercent * (useTemp ? asia.getGuestTemp() : asia.getGuest());
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

    private Float checkNotNull(Float f) {
        return f == null ? 0f : f;
    }

    private Float getHomeWinPercent(BigDataForecastData data) {
        if (data == null) return 0f;
        return checkNotNull(data.getHomeWinPercent());
    }

    private Float getAsiaWinPercent(BigDataForecastData data) {
        if (data == null) return 0f;
        return checkNotNull(data.getAsiaWinPercent());
    }

    private Float getSizeWinPercent(BigDataForecastData data) {
        if (data == null) return 0f;
        return checkNotNull(data.getSizeWinPercent());
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
