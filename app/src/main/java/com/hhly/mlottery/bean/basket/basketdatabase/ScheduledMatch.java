package com.hhly.mlottery.bean.basket.basketdatabase;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 描    述：赛程赛事
 * 作    者：longs@13322.com
 * 时    间：2016/8/9
 */
public class ScheduledMatch implements Parcelable {
    /**
     * guestScore : 87
     * homeScore : 107
     * guestTeamId : 11345
     * homeTeamIconUrl : http://en.13322.com/basket/images/logo_team/16.jpg
     * homeTeamId : 11256
     * guestTeamIconUrl : http://en.13322.com/basket/images/logo_team/4.jpg
     * homeTeamName : 骑士
     * guestTeamName : 篮网
     */

    private int guestScore;
    private int homeScore;
    private int guestTeamId;
    private String homeTeamIconUrl;
    private int homeTeamId;
    private String guestTeamIconUrl;
    private String homeTeamName;
    private String guestTeamName;
    private String time;
    public String thirdId;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(int guestScore) {
        this.guestScore = guestScore;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getGuestTeamId() {
        return guestTeamId;
    }

    public void setGuestTeamId(int guestTeamId) {
        this.guestTeamId = guestTeamId;
    }

    public String getHomeTeamIconUrl() {
        return homeTeamIconUrl;
    }

    public void setHomeTeamIconUrl(String homeTeamIconUrl) {
        this.homeTeamIconUrl = homeTeamIconUrl;
    }

    public int getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(int homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getGuestTeamIconUrl() {
        return guestTeamIconUrl;
    }

    public void setGuestTeamIconUrl(String guestTeamIconUrl) {
        this.guestTeamIconUrl = guestTeamIconUrl;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getGuestTeamName() {
        return guestTeamName;
    }

    public void setGuestTeamName(String guestTeamName) {
        this.guestTeamName = guestTeamName;
    }

    public ScheduledMatch() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.guestScore);
        dest.writeInt(this.homeScore);
        dest.writeInt(this.guestTeamId);
        dest.writeString(this.homeTeamIconUrl);
        dest.writeInt(this.homeTeamId);
        dest.writeString(this.guestTeamIconUrl);
        dest.writeString(this.homeTeamName);
        dest.writeString(this.guestTeamName);
        dest.writeString(this.time);
    }

    protected ScheduledMatch(Parcel in) {
        this.guestScore = in.readInt();
        this.homeScore = in.readInt();
        this.guestTeamId = in.readInt();
        this.homeTeamIconUrl = in.readString();
        this.homeTeamId = in.readInt();
        this.guestTeamIconUrl = in.readString();
        this.homeTeamName = in.readString();
        this.guestTeamName = in.readString();
        this.time = in.readString();
    }

    public static final Creator<ScheduledMatch> CREATOR = new Creator<ScheduledMatch>() {
        @Override
        public ScheduledMatch createFromParcel(Parcel source) {
            return new ScheduledMatch(source);
        }

        @Override
        public ScheduledMatch[] newArray(int size) {
            return new ScheduledMatch[size];
        }
    };
}
