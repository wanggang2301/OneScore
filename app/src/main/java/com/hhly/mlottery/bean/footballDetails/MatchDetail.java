package com.hhly.mlottery.bean.footballDetails;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus1 on 2015/12/25.
 * wangg
 */
public class MatchDetail implements Parcelable {


    //处理结果
    private String result;

    private Integer leagueId;

    public Integer getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
    }

    //赛事状态  0:未开, 1:上半场, 2:中场, 3:下半场, 4:加时, 5:点球, -1:完场, -10:取消, -11:待定, -12:腰斩,  -13:中断, -14:推迟
    private String matchStatus;

    //直播状态 0:未开 1:进行中（赛事状态在未开的情况下，但是进入文字直播，比如比赛还未开始，但是需要直播队员入场。） -1:完场
    private String liveStatus;


    //比赛类型  西甲，德甲 世界杯等
    private String matchType1;

    //第10轮  1/4决赛等
    private String matchType2;

    //赛事信息
    private MatchInfo matchInfo;

    private TeamInfo homeTeamInfo;

    private TeamInfo guestTeamInfo;

    private Integer leagueType;

    private int sourceType;// 1:rb,2:bt,3：球探，4：手动

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getLeagueType() {
        return leagueType;
    }

    public void setLeagueType(Integer leagueType) {
        this.leagueType = leagueType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMatchType1() {
        return matchType1;
    }

    public void setMatchType1(String matchType1) {
        this.matchType1 = matchType1;
    }

    public String getMatchType2() {
        return matchType2;
    }

    public void setMatchType2(String matchType2) {
        this.matchType2 = matchType2;
    }


    public MatchInfo getMatchInfo() {
        return matchInfo;
    }

    public void setMatchInfo(MatchInfo matchInfo) {
        this.matchInfo = matchInfo;
    }

    public TeamInfo getHomeTeamInfo() {
        return homeTeamInfo;
    }

    public void setHomeTeamInfo(TeamInfo homeTeamInfo) {
        this.homeTeamInfo = homeTeamInfo;
    }

    public TeamInfo getGuestTeamInfo() {
        return guestTeamInfo;
    }

    public void setGuestTeamInfo(TeamInfo guestTeamInfo) {
        this.guestTeamInfo = guestTeamInfo;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }


    public String getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }

    public MatchDetail() {

    }


    public MatchDetail(Parcel in) {
        result = in.readString();
        leagueId = in.readInt();
        leagueType = in.readInt();
        sourceType = in.readInt();
        matchType1 = in.readString();
        matchType2 = in.readString();
        matchStatus = in.readString();
        liveStatus = in.readString();

        matchInfo = in.readParcelable(MatchDetail.class.getClassLoader());
        homeTeamInfo = in.readParcelable(MatchDetail.class.getClassLoader());
        guestTeamInfo = in.readParcelable(MatchDetail.class.getClassLoader());

    }

    public static final Creator<MatchDetail> CREATOR = new Creator<MatchDetail>() {
        @Override
        public MatchDetail createFromParcel(Parcel in) {
            return new MatchDetail(in);
        }

        @Override
        public MatchDetail[] newArray(int size) {
            return new MatchDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result);
        dest.writeInt(leagueId);
        dest.writeInt(leagueType);
        dest.writeInt(sourceType);
        dest.writeString(matchType1);
        dest.writeString(matchType2);
        dest.writeString(matchStatus);
        dest.writeString(liveStatus);


        dest.writeParcelable(matchInfo, flags);

        dest.writeParcelable(homeTeamInfo, flags);
        dest.writeParcelable(guestTeamInfo, flags);

    }


}
