package com.hhly.mlottery.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuely198 on 2017/3/23.
 */

public class BallMatchItemsBean implements Parcelable, Cloneable, Comparator<BallMatchItemsBean> {
    private String thirdId;
    private int sortId;
    private String racename;
    private String raceId;
    private String raceColor;
    private String date;
    private String time;
    private String homeId;
    private String hometeam;

    private String guestId;

    private String guestteam;
    private String homerank;
    private String guestrank;

    private String statusOrigin;

    private String status;
    private String keepTime;
    private String halfScore;
    private String fullScore;
    private String home_rc;
    private String home_yc;
    private String guest_rc;
    private String guest_yc;

    private String txt;
    private int winner;

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    private String homeScore;
    private String guestScore;

    private String homeHalfScore;
    private String guestHalfScore;

    private Map<String, MatchOdd> matchOdds;

    private int leftOddTextColorId;
    private int rightOddTextColorId;
    private int midOddTextColorId;

    private int homeTeamTextColorId;
    private int guestTeamTextColorId;

    private boolean isFootballChicks = false; //标记item是否被选中(默认未选中)

    public boolean isFootballChicks() {
        return isFootballChicks;
    }

    public void setFootballChicks(boolean footballChicks) {
        isFootballChicks = footballChicks;
    }

    private int itemBackGroundColorId;//用于进球背景变化
    private int isTopData; // 用来控制是否置顶，选择对应的背景图片，及顺序 顺序越小越靠顶部
    private Match.SOCKET_PUSH_TYPE socketPushType;

    public enum SOCKET_PUSH_TYPE {
        STATUS, ODDS, EVENT, MATCHCHANGE
    }


    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public Match.SOCKET_PUSH_TYPE getSocketPushType() {
        return socketPushType;
    }

    public void setSocketPushType(Match.SOCKET_PUSH_TYPE socketPushType) {
        this.socketPushType = socketPushType;
    }

    public int getIsTopData() {
        return isTopData;
    }

    public void setIsTopData(int isTopData) {
        this.isTopData = isTopData;
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    public int getHomeTeamTextColorId() {
        return homeTeamTextColorId;
    }

    public void setHomeTeamTextColorId(int homeTeamTextColorId) {
        this.homeTeamTextColorId = homeTeamTextColorId;
    }

    public int getGuestTeamTextColorId() {
        return guestTeamTextColorId;
    }

    public void setGuestTeamTextColorId(int guestTeamTextColorId) {
        this.guestTeamTextColorId = guestTeamTextColorId;
    }

    public int getItemBackGroundColorId() {
        return itemBackGroundColorId;
    }

    public void setItemBackGroundColorId(int itemBackGroundColorId) {
        this.itemBackGroundColorId = itemBackGroundColorId;
    }

    public int getLeftOddTextColorId() {
        return leftOddTextColorId;
    }

    public void setLeftOddTextColorId(int leftOddTextColorId) {
        this.leftOddTextColorId = leftOddTextColorId;
    }

    public int getRightOddTextColorId() {
        return rightOddTextColorId;
    }

    public void setRightOddTextColorId(int rightOddTextColorId) {
        this.rightOddTextColorId = rightOddTextColorId;
    }

    public int getMidOddTextColorId() {
        return midOddTextColorId;
    }

    public void setMidOddTextColorId(int midOddTextColorId) {
        this.midOddTextColorId = midOddTextColorId;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getRacename() {
        return racename;
    }

    public void setRacename(String racename) {
        this.racename = racename;
    }

    public String getRaceId() {
        return raceId;
    }

    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }

    public String getRaceColor() {
        return raceColor;
    }

    public void setRaceColor(String raceColor) {
        this.raceColor = raceColor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHometeam() {
        return hometeam;
    }

    public void setHometeam(String hometeam) {
        this.hometeam = hometeam;
    }

    public String getGuestteam() {
        return guestteam;
    }

    public void setGuestteam(String guestteam) {
        this.guestteam = guestteam;
    }

    public String getHomerank() {
        return homerank;
    }

    public void setHomerank(String homerank) {
        this.homerank = homerank;
    }

    public String getGuestrank() {
        return guestrank;
    }

    public void setGuestrank(String guestrank) {
        this.guestrank = guestrank;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeepTime() {
        return keepTime;
    }

    public void setKeepTime(String keepTime) {
        this.keepTime = keepTime;
    }

    public String getHalfScore() {
        return halfScore;
    }

    public void setHalfScore(String halfScore) {
        this.halfScore = halfScore;
    }

    public String getFullScore() {
        return fullScore;
    }

    public void setFullScore(String fullScore) {
        this.fullScore = fullScore;
    }

    public String getHome_rc() {
        return home_rc;
    }

    public void setHome_rc(String home_rc) {
        this.home_rc = home_rc;
    }

    public String getHome_yc() {
        return home_yc;
    }

    public void setHome_yc(String home_yc) {
        this.home_yc = home_yc;
    }

    public String getGuest_rc() {
        return guest_rc;
    }

    public void setGuest_rc(String guest_rc) {
        this.guest_rc = guest_rc;
    }

    public String getGuest_yc() {
        return guest_yc;
    }

    public void setGuest_yc(String guest_yc) {
        this.guest_yc = guest_yc;
    }

    public Map<String, MatchOdd> getMatchOdds() {
        return matchOdds;
    }

    public void setMatchOdds(Map<String, MatchOdd> matchOdds) {
        this.matchOdds = matchOdds;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(String homeScore) {
        this.homeScore = homeScore;
    }

    public String getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(String guestScore) {
        this.guestScore = guestScore;
    }

    public String getStatusOrigin() {
        return statusOrigin;
    }

    public void setStatusOrigin(String statusOrigin) {
        this.statusOrigin = statusOrigin;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getHomeHalfScore() {
        return homeHalfScore;
    }

    public void setHomeHalfScore(String homeHalfScore) {
        this.homeHalfScore = homeHalfScore;
    }

    public String getGuestHalfScore() {
        return guestHalfScore;
    }

    public void setGuestHalfScore(String guestHalfScore) {
        this.guestHalfScore = guestHalfScore;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thirdId);
        dest.writeString(racename);
        dest.writeString(raceId);
        dest.writeString(raceColor);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(homeId);
        dest.writeString(hometeam);
        dest.writeString(guestId);
        dest.writeString(guestteam);
        dest.writeString(homerank);
        dest.writeString(guestrank);
        dest.writeString(status);
        dest.writeString(statusOrigin);
        dest.writeString(keepTime);
        dest.writeString(halfScore);
        dest.writeString(fullScore);
        dest.writeString(home_rc);
        dest.writeString(home_yc);
        dest.writeString(guest_rc);
        dest.writeString(guest_yc);
        dest.writeMap(matchOdds);
        dest.writeString(txt);
        dest.writeInt(winner);

    }


    public BallMatchItemsBean() {

    }

    public BallMatchItemsBean(Parcel in) {
        thirdId = in.readString();
        racename = in.readString();
        raceId = in.readString();
        raceColor = in.readString();
        date = in.readString();
        time = in.readString();
        homeId = in.readString();
        hometeam = in.readString();
        guestId = in.readString();
        guestteam = in.readString();
        homerank = in.readString();
        guestrank = in.readString();
        status = in.readString();
        keepTime = in.readString();
        halfScore = in.readString();
        fullScore = in.readString();
        home_rc = in.readString();
        home_yc = in.readString();
        guest_rc = in.readString();
        guest_yc = in.readString();
        statusOrigin = in.readString();
        matchOdds = in.readHashMap(HashMap.class.getClassLoader());
        txt = in.readString();
        winner = in.readInt();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int compare(BallMatchItemsBean lhs, BallMatchItemsBean rhs) {
        if (rhs.isTopData > 0 || lhs.isTopData > 0) {
            return String.valueOf(rhs.isTopData).compareTo(String.valueOf(lhs
                    .isTopData));
        } else {
            return new Integer(lhs.sortId).compareTo(new Integer(rhs.sortId));
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BallMatchItemsBean match = (BallMatchItemsBean) o;

        return thirdId != null ? thirdId.equals(match.thirdId) : match.thirdId == null;
    }

    @Override
    public int hashCode() {
        int result = thirdId != null ? thirdId.hashCode() : 0;
        return result;
    }
}