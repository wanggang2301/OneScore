package com.hhly.mlottery.bean.footballDetails;

import java.util.List;

/**
 * @author: Wangg
 * @Name：tytt
 * @Description:
 * @Created on:2016/12/9  11:51.
 */

public class VideoHighLights {

    /**
     * result : 200
     * data : [{"title":null,"description":"加时末段马竞的角球，未能获得扳平比分的机会，终场哨响，拜仁慕尼黑主场1:0拿下马德里竞技！","resourceUrl":"http://m.13322.com/news/upload/shareManageUrl/905c9c8af6f544a4806ff5c65ba6b998.gif","resourceType":"0","matchType":"1","thirdId":"399366"},{"title":null,"description":"贝尔纳特左侧传中 门前包抄的科斯塔空门打飞了。。","resourceUrl":"http://m.13322.com/news/upload/shareManageUrl/7ea7c529627d43b696699545b6420721.gif","resourceType":"0","matchType":"1","thirdId":"399366"},{"title":null,"description":" 罗本晃过萨维奇传中 莱万禁区内头球没压住","resourceUrl":"http://m.13322.com/news/upload/shareManageUrl/4205f48fecd644e79abfc7a59becf75a.gif","resourceType":"0","matchType":"1","thirdId":"399366"},{"title":" ","description":"科斯塔得球禁区前沿两连射","resourceUrl":"http://m.13322.com/news/upload/shareManageUrl/1ca4e541e9fc4afb8f993575d026cf0a.gif","resourceType":"0","matchType":"1","thirdId":"399366"},{"title":" ","description":"莱万禁区前沿任意球直挂死角，拜仁慕尼黑1:0领先马德里竞技","resourceUrl":"http://m.13322.com/news/upload/shareManageUrl/fb58b8c7cfdd405aaf9225bdf258bdfa.gif","resourceType":"0","matchType":"1","thirdId":"399366"},{"title":null,"description":"科斯塔禁区左侧小角度射门被扑出 罗本趁乱得球再打未能得分","resourceUrl":"http://m.13322.com/news/upload/shareManageUrl/901d8729b1354718aa08a462aeaf23ac.gif","resourceType":"0","matchType":"1","thirdId":"399366"},{"title":" ","description":"萨乌尔突破传中 卡拉斯科门前包抄打太正被没收","resourceUrl":"http://m.13322.com/news/upload/shareManageUrl/7752cfe723ab4072832786fec5396545.gif","resourceType":"0","matchType":"1","thirdId":"399366"},{"title":" ","description":"马竞打出反击，卡拉斯科射门被诺伊尔倒地扑出","resourceUrl":"http://m.13322.com/news/upload/shareManageUrl/801790b64c254e42a2231808c24297b5.gif","resourceType":"0","matchType":"1","thirdId":"399366"},{"title":" ","description":"双方在球员通道等待比赛开始","resourceUrl":"http://m.13322.com/news/upload/shareManageUrl/5df055867d70447ba06c5a2e4a48b621.gif","resourceType":"0","matchType":"1","thirdId":"399366"},{"title":" ","description":"双方球队赛前热身 拜仁当家球星尽显二娃本色","resourceUrl":"http://m.13322.com/news/upload/shareManageUrl/b66930a09236403eb21033cff97f7d7f.gif","resourceType":"0","matchType":"1","thirdId":"399366"}]
     */

    private int result;
    private List<DataBean> data;

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

    public static class DataBean {
        /**
         * title : null
         * description : 加时末段马竞的角球，未能获得扳平比分的机会，终场哨响，拜仁慕尼黑主场1:0拿下马德里竞技！
         * resourceUrl : http://m.13322.com/news/upload/shareManageUrl/905c9c8af6f544a4806ff5c65ba6b998.gif
         * resourceType : 0
         * matchType : 1
         * thirdId : 399366
         */

        private Object title;
        private String description;
        private String resourceUrl;
        private String resourceType;
        private String matchType;
        private String thirdId;

        private int mCurrentPositions = 0;

        private boolean mIsPlaying = true;

     /*   public DataBean(Object title, String description, String resourceUrl, String resourceType, String matchType, String thirdId, int mCurrentPositions, boolean mIsPlaying) {
            this.title = title;
            this.description = description;
            this.resourceUrl = resourceUrl;
            this.resourceType = resourceType;
            this.matchType = matchType;
            this.thirdId = thirdId;
            this.mCurrentPositions = mCurrentPositions;
            this.mIsPlaying = mIsPlaying;
        }*/

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getResourceUrl() {
            return resourceUrl;
        }

        public void setResourceUrl(String resourceUrl) {
            this.resourceUrl = resourceUrl;
        }

        public String getResourceType() {
            return resourceType;
        }

        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        public String getMatchType() {
            return matchType;
        }

        public void setMatchType(String matchType) {
            this.matchType = matchType;
        }

        public String getThirdId() {
            return thirdId;
        }

        public void setThirdId(String thirdId) {
            this.thirdId = thirdId;
        }

        public int getmCurrentPositions() {
            return mCurrentPositions;
        }

        public void setmCurrentPositions(int mCurrentPositions) {
            this.mCurrentPositions = mCurrentPositions;
        }

        public boolean ismIsPlaying() {
            return mIsPlaying;
        }

        public void setmIsPlaying(boolean mIsPlaying) {
            this.mIsPlaying = mIsPlaying;
        }
    }
}
