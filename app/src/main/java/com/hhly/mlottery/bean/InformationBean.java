package com.hhly.mlottery.bean;

/**
 * 创建人：107tangrr
 * 邮  箱：tangrr@13322.com
 * 时  间：2016/6/15
 * 描  述：单条资讯实体类
 */
public class InformationBean{

    /**
     * result : 200
     * info : {"thirdId":null,"type":2,"picUrl":"http://img04.store.sogou.com/net/a/66/link?appid=66&url=http://c2.hoopchina.com.cn/uploads/star/event/images/160615/969bc7c3605959caa0a577ab93c12c9f86e81021.jpg","title":"切尔西2016-17赛季英超联赛完整赛程","subTitle":null,"infoUrl":"http://192.168.10.242:9090/oms/infomationhtml/20160615/2016061517346.html","infoSource":"网络媒体","lastModifyDate":"2016-06-15","lastModifyTime":"15:50","infoId":null,"summary":null,"relateMatch":false}
     */

    private int result;
    /**
     * thirdId : null
     * type : 2
     * picUrl : http://img04.store.sogou.com/net/a/66/link?appid=66&url=http://c2.hoopchina.com.cn/uploads/star/event/images/160615/969bc7c3605959caa0a577ab93c12c9f86e81021.jpg
     * title : 切尔西2016-17赛季英超联赛完整赛程
     * subTitle : null
     * infoUrl : http://192.168.10.242:9090/oms/infomationhtml/20160615/2016061517346.html
     * infoSource : 网络媒体
     * lastModifyDate : 2016-06-15
     * lastModifyTime : 15:50
     * infoId : null
     * summary : null
     * relateMatch : false
     */

    private InfoBean info;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        private String thirdId;
        private int type;
        private String picUrl;
        private String title;
        private String subTitle;
        private String infoUrl;
        private String infoSource;
        private String lastModifyDate;
        private String lastModifyTime;
        private String infoId;
        private String summary;
        private String relateMatch;
        private String infotype;
        private String infoTypeName;

        public String getInfoTypeName() {
            return infoTypeName;
        }

        public void setInfoTypeName(String infoTypeName) {
            this.infoTypeName = infoTypeName;
        }

        public String getInfotype() {
            return infotype;
        }

        public void setInfotype(String infotype) {
            this.infotype = infotype;
        }

        public String getThirdId() {
            return thirdId;
        }

        public void setThirdId(String thirdId) {
            this.thirdId = thirdId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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

        public String getInfoId() {
            return infoId;
        }

        public void setInfoId(String infoId) {
            this.infoId = infoId;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getRelateMatch() {
            return relateMatch;
        }

        public void setRelateMatch(String relateMatch) {
            this.relateMatch = relateMatch;
        }
    }
}
