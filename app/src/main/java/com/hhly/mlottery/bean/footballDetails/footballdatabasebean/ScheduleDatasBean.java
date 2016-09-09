package com.hhly.mlottery.bean.footballDetails.footballdatabasebean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/7.
 */

public class ScheduleDatasBean implements Parcelable {

    /*
        homeName: "曼联",
        guestName: "曼城",
        matchResult: "18:30",
        matchId: 361541,
        matchState: 0,
        homeId: null,
        guestId: null,
        homePic: null,
        guestPic: null
     */
    private String homeName;
    private String guestName;
    private String matchResult;
    private int matchId;
    private int matchState;
    private String homeId;
    private String guestId;
    private String homePic;
    private String guestPic;

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getMatchState() {
        return matchState;
    }

    public void setMatchState(int matchState) {
        this.matchState = matchState;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getHomePic() {
        return homePic;
    }

    public void setHomePic(String homePic) {
        this.homePic = homePic;
    }

    public String getGuestPic() {
        return guestPic;
    }

    public void setGuestPic(String guestPic) {
        this.guestPic = guestPic;
    }

    public ScheduleDatasBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.matchId);
        dest.writeInt(this.matchState);
        dest.writeString(this.homeName);
        dest.writeString(this.guestName);
        dest.writeString(this.matchResult);
        dest.writeString(this.homeId);
        dest.writeString(this.guestId);
        dest.writeString(this.homePic);
        dest.writeString(this.guestPic);
    }

    protected ScheduleDatasBean(Parcel in) {
        this.matchId = in.readInt();
        this.matchState = in.readInt();
        this.homeName = in.readString();
        this.guestName = in.readString();
        this.matchResult = in.readString();
        this.homeId = in.readString();
        this.guestId = in.readString();
        this.homePic = in.readString();
        this.guestPic = in.readString();
    }

    public static final Creator<ScheduleDatasBean> CREATOR = new Creator<ScheduleDatasBean>() {
        @Override
        public ScheduleDatasBean createFromParcel(Parcel source) {
            return new ScheduleDatasBean(source);
        }

        @Override
        public ScheduleDatasBean[] newArray(int size) {
            return new ScheduleDatasBean[size];
        }
    };
}
