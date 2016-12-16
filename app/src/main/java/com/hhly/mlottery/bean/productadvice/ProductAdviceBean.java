package com.hhly.mlottery.bean.productadvice;

import java.util.List;

/**
 * 描    述：
 * 作    者：mady@13322.com
 * 时    间：2016/12/14
 */
public class ProductAdviceBean {

    private int result;

    private List<DataEntity> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        private int jumpType;
        private String jumpAddr;
        private String osName;
        private String osVersion;
        private String deviceBrand;
        private String deviceModel;
        private String sendTime;
        private String content;
        private String deviceToken;
        private String appVersion;
        private String userId;
        private String replyContent;
        private int id;
        private String replyTime;
        private String nickName;
        private String userImg;

        public int getJumpType() {
            return jumpType;
        }

        public void setJumpType(int jumpType) {
            this.jumpType = jumpType;
        }

        public String getJumpAddr() {
            return jumpAddr;
        }

        public void setJumpAddr(String jumpAddr) {
            this.jumpAddr = jumpAddr;
        }

        public String getOsName() {
            return osName;
        }

        public void setOsName(String osName) {
            this.osName = osName;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        public String getDeviceBrand() {
            return deviceBrand;
        }

        public void setDeviceBrand(String deviceBrand) {
            this.deviceBrand = deviceBrand;
        }

        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getReplyContent() {
            return replyContent;
        }

        public void setReplyContent(String replyContent) {
            this.replyContent = replyContent;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getReplyTime() {
            return replyTime;
        }

        public void setReplyTime(String replyTime) {
            this.replyTime = replyTime;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getUserImg() {
            return userImg;
        }

        public void setUserImg(String  userImg) {
            this.userImg = userImg;
        }
    }
}
