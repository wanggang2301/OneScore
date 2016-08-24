package com.hhly.mlottery.bean.basket.infomation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author wang gang
 * @date 2016/7/21 10:28
 * @des 联赛
 */
public class LeagueBean implements Parcelable {

    private String leagueId;
    private String leagueName;
    private String leagueLogoUrl;
    private Integer matchType;

    public Integer getMatchType() {
        return matchType;
    }

    public void setMatchType(Integer matchType) {
        this.matchType = matchType;
    }


    public LeagueBean() {
    }

    public LeagueBean(String leagueId, String leagueLogoUrl, String leagueName) {
        this.leagueId = leagueId;
        this.leagueLogoUrl = leagueLogoUrl;
        this.leagueName = leagueName;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getLeagueLogoUrl() {
        return leagueLogoUrl;
    }

    public void setLeagueLogoUrl(String leagueLogoUrl) {
        this.leagueLogoUrl = leagueLogoUrl;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.leagueId);
        dest.writeString(this.leagueName);
        dest.writeString(this.leagueLogoUrl);
        dest.writeValue(this.matchType);
    }

    protected LeagueBean(Parcel in) {
        this.leagueId = in.readString();
        this.leagueName = in.readString();
        this.leagueLogoUrl = in.readString();
        this.matchType = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<LeagueBean> CREATOR = new Parcelable.Creator<LeagueBean>() {
        @Override
        public LeagueBean createFromParcel(Parcel source) {
            return new LeagueBean(source);
        }

        @Override
        public LeagueBean[] newArray(int size) {
            return new LeagueBean[size];
        }
    };
}
