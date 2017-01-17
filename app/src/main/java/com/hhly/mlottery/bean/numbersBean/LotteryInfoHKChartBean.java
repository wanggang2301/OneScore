package com.hhly.mlottery.bean.numbersBean;

import java.io.Serializable;
import java.util.List;

/**
 * desc:香港详情图表 Bean
 * Created by 107_tangrr on 2017/1/12 0012.
 */

public class LotteryInfoHKChartBean implements Serializable{

    private int result;
    private List<DataBean> data;

    public static class DataBean implements Serializable{

        private int jumpType;
        private String jumpAddr;
        private int infoId;
        private String picUrl;
        private String title;
        private String subTitle;
        private String infoUrl;
        private String infoSource;
        private String lastModifyDate;
        private String lastModifyTime;
        private String summary;
        private String modelType;
        private String lotteryImg;

        public String getLotteryImg() {
            return lotteryImg;
        }

        public void setLotteryImg(String lotteryImg) {
            this.lotteryImg = lotteryImg;
        }

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

        public int getInfoId() {
            return infoId;
        }

        public void setInfoId(int infoId) {
            this.infoId = infoId;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getInfoUrl() {
            return infoUrl;
        }

        public void setInfoUrl(String infoUrl) {
            this.infoUrl = infoUrl;
        }

        public String getInfoSource() {
            return infoSource;
        }

        public void setInfoSource(String infoSource) {
            this.infoSource = infoSource;
        }

        public String getLastModifyDate() {
            return lastModifyDate;
        }

        public void setLastModifyDate(String lastModifyDate) {
            this.lastModifyDate = lastModifyDate;
        }

        public String getLastModifyTime() {
            return lastModifyTime;
        }

        public void setLastModifyTime(String lastModifyTime) {
            this.lastModifyTime = lastModifyTime;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getModelType() {
            return modelType;
        }

        public void setModelType(String modelType) {
            this.modelType = modelType;
        }
    }
    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }
}
