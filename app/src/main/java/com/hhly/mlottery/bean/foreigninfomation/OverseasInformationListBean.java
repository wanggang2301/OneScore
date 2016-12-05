package com.hhly.mlottery.bean.foreigninfomation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author: Wangg
 * @Name：OverseasInformationListBean
 * @Description:
 * @Created on:2016/9/20  17:18.
 */

public class OverseasInformationListBean implements Parcelable {

    private int id;
    private double timestamp;
    private String infoType;
    private String fullname;
    private String fullnameTranslation;
    private String fullnameTranslationThTw;
    private String avatar;
    private String content;
    private String contentTranslation;
    private String contentTranslationThTw;
    private String photo;
    private String video;
    private int favorite;
    private double currentTimestamp;
    private String sendtime;

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullnameTranslation() {
        return fullnameTranslation;
    }

    public void setFullnameTranslation(String fullnameTranslation) {
        this.fullnameTranslation = fullnameTranslation;
    }

    public String getFullnameTranslationThTw() {
        return fullnameTranslationThTw;
    }

    public void setFullnameTranslationThTw(String fullnameTranslationThTw) {
        this.fullnameTranslationThTw = fullnameTranslationThTw;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentTranslation() {
        return contentTranslation;
    }

    public void setContentTranslation(String contentTranslation) {
        this.contentTranslation = contentTranslation;
    }

    public String getContentTranslationThTw() {
        return contentTranslationThTw;
    }

    public void setContentTranslationThTw(String contentTranslationThTw) {
        this.contentTranslationThTw = contentTranslationThTw;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public double getCurrentTimestamp() {
        return currentTimestamp;
    }

    public void setCurrentTimestamp(long currentTimestamp) {
        this.currentTimestamp = currentTimestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeDouble(this.timestamp);
        dest.writeString(this.infoType);
        dest.writeString(this.fullname);
        dest.writeString(this.fullnameTranslation);
        dest.writeString(this.fullnameTranslationThTw);
        dest.writeString(this.avatar);
        dest.writeString(this.content);
        dest.writeString(this.contentTranslation);
        dest.writeString(this.contentTranslationThTw);
        dest.writeString(this.photo);
        dest.writeString(this.video);
        dest.writeInt(this.favorite);
        dest.writeDouble(this.currentTimestamp);
        dest.writeString(this.sendtime);

    }

    public OverseasInformationListBean() {
    }

    protected OverseasInformationListBean(Parcel in) {
        this.id = in.readInt();
        this.timestamp = in.readLong();
        this.infoType = in.readString();
        this.fullname = in.readString();
        this.fullnameTranslation = in.readString();
        this.fullnameTranslationThTw = in.readString();
        this.avatar = in.readString();
        this.content = in.readString();
        this.contentTranslation = in.readString();
        this.contentTranslationThTw = in.readString();
        this.photo = in.readString();
        this.video = in.readString();
        this.favorite = in.readInt();
        this.currentTimestamp = in.readLong();
        this.sendtime = in.readString();
    }

    public static final Parcelable.Creator<OverseasInformationListBean> CREATOR = new Parcelable.Creator<OverseasInformationListBean>() {
        @Override
        public OverseasInformationListBean createFromParcel(Parcel source) {
            return new OverseasInformationListBean(source);
        }

        @Override
        public OverseasInformationListBean[] newArray(int size) {
            return new OverseasInformationListBean[size];
        }
    };
}