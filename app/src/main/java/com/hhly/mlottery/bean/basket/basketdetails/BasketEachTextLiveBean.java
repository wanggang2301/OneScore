package com.hhly.mlottery.bean.basket.basketdetails;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author: Wangg
 * @Name：BaketEachTextLiveBean
 * @Description:
 * @Created on:2016/11/15  17:46.
 */

public class BasketEachTextLiveBean implements Parcelable {

    public BasketEachTextLiveBean(String id, String teamName, String playerName, String playerName1, String eventContent, int eventId, int eventId1, String scheduleId, int guestScore, int homeScore, int matchStatus, String remainTime, String teamId, String logoUrl, int teamType, String playerId, String playerId1, String lang) {
        this.id = id;
        this.teamName = teamName;
        this.playerName = playerName;
        this.playerName1 = playerName1;
        this.eventContent = eventContent;
        this.eventId = eventId;
        this.eventId1 = eventId1;
        this.scheduleId = scheduleId;
        this.guestScore = guestScore;
        this.homeScore = homeScore;
        this.matchStatus = matchStatus;
        this.remainTime = remainTime;
        this.teamId = teamId;
        this.logoUrl = logoUrl;
        this.teamType = teamType;
        this.playerId = playerId;
        this.playerId1 = playerId1;
        this.lang = lang;
    }

    /**
     * id : 404051
     * teamName : null
     * playerName : null
     * playerName1 : null
     * eventContent : 快船-得分:保罗(21) 篮板:乔丹(14) 助攻:保罗(9) 篮网-得分:博格达诺维奇(18) 篮板:安东尼-班尼特(9) 助攻:费雷尔(5)
     * eventId : 1100
     * eventId1 : 0
     * scheduleId : 3666697
     * guestScore : 95
     * homeScore : 127
     * matchStatus : -1

     * remainTime :
     * teamId : null
     * logoUrl : null
     * teamType : 0
     * playerId : null
     * playerId1 : null
     * lang : zh
     */

    private String id;
    private String teamName;
    private String playerName;
    private String playerName1;
    private String eventContent;
    private int eventId;
    private int eventId1;
    private String scheduleId;
    private int guestScore;
    private int homeScore;
    private int matchStatus;
    private String remainTime;
    private String teamId;
    private String logoUrl;
    private int teamType;
    private String playerId;
    private String playerId1;
    private String lang;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName1() {
        return playerName1;
    }

    public void setPlayerName1(String playerName1) {
        this.playerName1 = playerName1;
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getEventId1() {
        return eventId1;
    }

    public void setEventId1(int eventId1) {
        this.eventId1 = eventId1;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
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

    public int getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(int matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getTeamType() {
        return teamType;
    }

    public void setTeamType(int teamType) {
        this.teamType = teamType;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId1() {
        return playerId1;
    }

    public void setPlayerId1(String playerId1) {
        this.playerId1 = playerId1;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.teamName);
        dest.writeString(this.playerName);
        dest.writeString(this.playerName1);
        dest.writeString(this.eventContent);
        dest.writeInt(this.eventId);
        dest.writeInt(this.eventId1);
        dest.writeString(this.scheduleId);
        dest.writeInt(this.guestScore);
        dest.writeInt(this.homeScore);
        dest.writeInt(this.matchStatus);
        dest.writeString(this.remainTime);
        dest.writeString(this.teamId);
        dest.writeString(this.logoUrl);
        dest.writeInt(this.teamType);
        dest.writeString(this.playerId);
        dest.writeString(this.playerId1);
        dest.writeString(this.lang);
    }

    public BasketEachTextLiveBean() {
    }

    protected BasketEachTextLiveBean(Parcel in) {
        this.id = in.readString();
        this.teamName = in.readString();
        this.playerName = in.readString();
        this.playerName1 = in.readString();
        this.eventContent = in.readString();
        this.eventId = in.readInt();
        this.eventId1 = in.readInt();
        this.scheduleId = in.readString();
        this.guestScore = in.readInt();
        this.homeScore = in.readInt();
        this.matchStatus = in.readInt();
        this.remainTime = in.readString();
        this.teamId = in.readString();
        this.logoUrl = in.readString();
        this.teamType = in.readInt();
        this.playerId = in.readString();
        this.playerId1 = in.readString();
        this.lang = in.readString();
    }

    public static final Parcelable.Creator<BasketEachTextLiveBean> CREATOR = new Parcelable.Creator<BasketEachTextLiveBean>() {
        @Override
        public BasketEachTextLiveBean createFromParcel(Parcel source) {
            return new BasketEachTextLiveBean(source);
        }

        @Override
        public BasketEachTextLiveBean[] newArray(int size) {
            return new BasketEachTextLiveBean[size];
        }
    };
}
