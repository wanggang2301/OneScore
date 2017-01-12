package com.hhly.mlottery.bean.numbersBean;

import java.util.List;

/**
 * desc:香港详情图表 Bean
 * Created by 107_tangrr on 2017/1/12 0012.
 */

public class LotteryInfoHKChartBean {

    private int result;
    private List<Data> data;

    public class Data {
        private int jumpType;
        private String jumpAddr;
        private int infoId;
        private String picUrl; // 标题图片地址
        private String title;// 主标题
        private String subTitle;// 副标题
        private String infoUrl;// 资讯详情跳转地址
        private String infoSource;// 来源信息
        private String lastModifyDate;// 最后修改日期
        private String lastModifyTime;// 最后修改时间
        private String summary;// 摘要
        private String modelType;// 模板

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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
