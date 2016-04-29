package com.hhly.mlottery.bean.footballsecond;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by asus1 on 2016/1/4.
 * <p/>
 * 赛事信息
 */
public class MatchInfo implements Parcelable {

    //开赛时间
    private String startTime;

    private String weather;

    private String city;

    //场馆名称
    private String venue;


    private String keepTime;

    //系统时间戳
    private String serverTime;


    private List<MatchTimeLiveBean> matchTimeLive;

    private List<MatchTextLiveBean> matchLive;


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getKeepTime() {
        return keepTime;
    }

    public void setKeepTime(String keepTime) {
        this.keepTime = keepTime;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public List<MatchTimeLiveBean> getMatchTimeLive() {
        return matchTimeLive;
    }

    public void setMatchTimeLive(List<MatchTimeLiveBean> matchTimeLive) {
        this.matchTimeLive = matchTimeLive;
    }

    public List<MatchTextLiveBean> getMatchLive() {
        return matchLive;
    }

    public void setMatchLive(List<MatchTextLiveBean> matchLive) {
        this.matchLive = matchLive;
    }


    public static final Creator<MatchInfo> CREATOR = new Creator<MatchInfo>() {
        @Override
        public MatchInfo createFromParcel(Parcel in) {
            return new MatchInfo(in);
        }

        @Override
        public MatchInfo[] newArray(int size) {
            return new MatchInfo[size];
        }
    };

    public MatchInfo() {

    }

    public MatchInfo(Parcel in) {


        startTime = in.readString();
        weather = in.readString();
        city = in.readString();
        venue = in.readString();
        keepTime = in.readString();

        MatchTimeLiveBean[] matchtimeArray = (MatchTimeLiveBean[]) in.readParcelableArray(MatchTimeLiveBean.class.getClassLoader());
        if (matchtimeArray == null) {
            matchTimeLive = null;
        } else {
            matchTimeLive = Arrays.asList(matchtimeArray);
        }

        MatchTextLiveBean[] matchArray = (MatchTextLiveBean[]) in.readParcelableArray(MatchTextLiveBean.class.getClassLoader());

        if (matchArray == null) {
            matchLive = null;
        } else {
            matchLive = Arrays.asList(matchArray);
        }


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(startTime);
        dest.writeString(weather);
        dest.writeString(city);
        dest.writeString(venue);
        dest.writeString(keepTime);

        if (matchTimeLive == null) {
            dest.writeParcelableArray(null, flags);
        } else {
            dest.writeParcelableArray(matchTimeLive.toArray(new MatchTimeLiveBean[matchTimeLive.size()]), flags);
        }

        if (matchLive == null) {
            dest.writeParcelableArray(null, flags);
        } else {
            dest.writeParcelableArray(matchLive.toArray(new MatchTextLiveBean[matchLive.size()]), flags);
        }

    }
}
