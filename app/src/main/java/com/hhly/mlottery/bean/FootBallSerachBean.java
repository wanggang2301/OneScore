package com.hhly.mlottery.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ClassName: OneScoreGit
 * @author:Administrator luyao
 * @Description:
 * @data: 2016/9/9 9:44
 */
public class FootBallSerachBean implements Parcelable {

    private int lgId;
    private String lgName;
    private String leagueLogoUrl;
    private int kind;

    public Integer getMatchType() {
        return kind;
    }

    public void setMatchType(Integer kind) {
        this.kind = kind;
    }


    public FootBallSerachBean() {
    }

    public FootBallSerachBean(int leagueId, String leagueLogoUrl, String leagueName) {
        this.lgId = leagueId;
        this.leagueLogoUrl = leagueLogoUrl;
        this.lgName = leagueName;
    }

    public int getLeagueId() {
        return lgId;
    }

    public void setLeagueId(Integer lgId) {
        this.lgId = lgId;
    }

    public String getLeagueLogoUrl() {
        return leagueLogoUrl;
    }

    public void setLeagueLogoUrl(String leagueLogoUrl) {
        this.leagueLogoUrl = leagueLogoUrl;
    }

    public String getLeagueName() {
        return lgName;
    }

    public void setLeagueName(String leagueName) {
        this.lgName = leagueName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.lgId);
        dest.writeString(this.lgName);
        dest.writeString(this.leagueLogoUrl);
        dest.writeValue(this.kind);
    }

    protected FootBallSerachBean(Parcel in) {
        this.lgId = in.readInt();
        this.lgName = in.readString();
        this.leagueLogoUrl = in.readString();
        this.kind = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<FootBallSerachBean> CREATOR = new Parcelable.Creator<FootBallSerachBean>() {
        @Override
        public FootBallSerachBean createFromParcel(Parcel source) {
            return new FootBallSerachBean(source);
        }

        @Override
        public FootBallSerachBean[] newArray(int size) {
            return new FootBallSerachBean[size];
        }
    };
}
