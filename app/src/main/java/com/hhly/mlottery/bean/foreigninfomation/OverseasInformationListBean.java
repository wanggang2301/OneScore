package com.hhly.mlottery.bean.foreigninfomation;

/**
 * @author: Wangg
 * @Name：OverseasInformationListBean
 * @Description:
 * @Created on:2016/9/20  17:18.
 */

public class OverseasInformationListBean   {
    private String createBy;
    private long createDate;
    private String lastModifiedBy;
    private long lastModifiedDate;
    private int id;
    private long timestamp;
    private String fullname;
    private String fullnameTranslation;
    private String fullnameTranslationThTw;
    private String avatar;
    private String content;
    private String contentTranslation;
    private String contentTranslationThTw;
    private String photo;
    private int favorite;
    private int isdisplay;
    private String description;
    private int isValid;

    private long currentTimestamp;


    public long getCurrentTimestamp() {
        return currentTimestamp;
    }

    public void setCurrentTimestamp(long currentTimestamp) {
        this.currentTimestamp = currentTimestamp;
    }



    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public long getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(long lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
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

    public int getIsdisplay() {
        return isdisplay;
    }

    public void setIsdisplay(int isdisplay) {
        this.isdisplay = isdisplay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }
}