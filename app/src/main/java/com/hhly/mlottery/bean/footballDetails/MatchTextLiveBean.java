package com.hhly.mlottery.bean.footballDetails;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus1 on 2015/12/30.
 * 文字直播数据信息
 */
public class MatchTextLiveBean implements Parcelable {


    private String code;

    private String msgId;

    private String msgPlace;

    private String showId;

    private String fontStyle;


    private String time;

    private String msgText;

    private String cancelEnNum; //取消消息ID

    private  String enNum;     //原始消息Id

    private String state;

    private String homeScore;

    private String guestScore;



    public MatchTextLiveBean(String code, String msgId, String msgPlace, String showId,String fontStyle, String time, String msgText,String cancelEnNum,String enNum,String state,String homeScore,String guestScore) {
        this.code = code;
        this.msgId = msgId;
        this.msgPlace = msgPlace;
        this.showId = showId;
        this.fontStyle=fontStyle;
        this.time = time;
        this.msgText = msgText;
        this.cancelEnNum=cancelEnNum;
        this.enNum=enNum;
        this.state=state;
        this.homeScore=homeScore;
        this.guestScore=guestScore;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getMsgPlace() {
        return msgPlace;
    }

    public void setMsgPlace(String msgPlace) {
        this.msgPlace = msgPlace;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getCancelEnNum() {
        return cancelEnNum;
    }

    public void setCancelEnNum(String cancelEnNum) {
        this.cancelEnNum = cancelEnNum;
    }

    public String getEnNum() {
        return enNum;
    }

    public void setEnNum(String enNum) {
        this.enNum = enNum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static final Creator<MatchTextLiveBean> CREATOR = new Creator<MatchTextLiveBean>() {
        @Override
        public MatchTextLiveBean createFromParcel(Parcel in) {
            return new MatchTextLiveBean(in);
        }

        @Override
        public MatchTextLiveBean[] newArray(int size) {
            return new MatchTextLiveBean[size];
        }
    };

    public MatchTextLiveBean() {

    }

    public MatchTextLiveBean(Parcel in) {
        code = in.readString();
        msgId = in.readString();
        msgPlace = in.readString();
        showId=in.readString();
        fontStyle=in.readString();
        time = in.readString();
        msgText = in.readString();
        cancelEnNum=in.readString();
        enNum=in.readString();
        state=in.readString();
        homeScore=in.readString();
        guestScore=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(msgId);
        dest.writeString(msgPlace);
        dest.writeString(showId);
        dest.writeString(fontStyle);
        dest.writeString(time);
        dest.writeString(msgText);
        dest.writeString(cancelEnNum);
        dest.writeString(enNum);
        dest.writeString(state);
        dest.writeString(homeScore);
        dest.writeString(guestScore);

    }

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
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
}
