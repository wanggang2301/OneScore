package com.hhly.mlottery.bean.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/6/22.
 */
public class WebSocketCPIResult<T> {

    public static final int TYPE_TIME_STATUS = 1;
    public static final int TYPE_ODDS = 2;
    public static final int TYPE_SCORE = 3;

    private T data;
    private String thirdId;
    private int type;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
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

    /**
     * 从 Json 字符串转化 data 为 UpdateTimeAndStatus 的对象
     *
     * @param jsonString 字符串
     * @return 实例
     */
    public static WebSocketCPIResult<WebSocketCPIResult.UpdateTimeAndStatus>
    getTimeAndStatusFromJson(String jsonString) {
        TypeReference<WebSocketCPIResult<WebSocketCPIResult.UpdateTimeAndStatus>> typeReference =
                new TypeReference<WebSocketCPIResult<WebSocketCPIResult.UpdateTimeAndStatus>>() {};
        return JSON.parseObject(jsonString, typeReference);
    }

    /**
     * 从 Json 字符串转化 data 为 UpdateScore 的对象
     *
     * @param jsonString 字符串
     * @return 实例
     */
    public static WebSocketCPIResult<UpdateScore>
    getScoreFromJson(String jsonString) {
        TypeReference<WebSocketCPIResult<UpdateScore>> typeReference =
                new TypeReference<WebSocketCPIResult<UpdateScore>>() {
                };
        return JSON.parseObject(jsonString, typeReference);
    }

    /**
     * 从 Json 字符串转化 data 为 List<UpdateOdds> 的对象
     *
     * @param jsonString 字符串
     * @return 实例
     */
    public static WebSocketCPIResult<List<UpdateOdds>>
    getOddsFromJson(String jsonString) {
        TypeReference<WebSocketCPIResult<List<UpdateOdds>>> typeReference =
                new TypeReference<WebSocketCPIResult<List<UpdateOdds>>>() {
                };
        return JSON.parseObject(jsonString, typeReference);
    }

    public static class UpdateOdds {

        /**
         * comId : 38
         * leftOdds : 7.80
         * mediumOdds : 1.25
         * oddType : 2
         * rightOdds : 5.10
         * uptime : 19:05
         */

        private String comId;
        private String leftOdds;
        private String mediumOdds;
        private String oddType;
        private String rightOdds;
        private String uptime;

        public String getComId() {
            return comId;
        }

        public void setComId(String comId) {
            this.comId = comId;
        }

        public String getLeftOdds() {
            return leftOdds;
        }

        public void setLeftOdds(String leftOdds) {
            this.leftOdds = leftOdds;
        }

        public String getMediumOdds() {
            return mediumOdds;
        }

        public void setMediumOdds(String mediumOdds) {
            this.mediumOdds = mediumOdds;
        }

        public String getOddType() {
            return oddType;
        }

        public void setOddType(String oddType) {
            this.oddType = oddType;
        }

        public String getRightOdds() {
            return rightOdds;
        }

        public void setRightOdds(String rightOdds) {
            this.rightOdds = rightOdds;
        }

        public String getUptime() {
            return uptime;
        }

        public void setUptime(String uptime) {
            this.uptime = uptime;
        }
    }

    public static class UpdateTimeAndStatus {

        /**
         * keepTime : 6
         * statusOrigin : 1
         */

        private int keepTime;
        private int statusOrigin;

        public int getKeepTime() {
            return keepTime;
        }

        public void setKeepTime(int keepTime) {
            this.keepTime = keepTime;
        }

        public int getStatusOrigin() {
            return statusOrigin;
        }

        public void setStatusOrigin(int statusOrigin) {
            this.statusOrigin = statusOrigin;
        }
    }

    public static class UpdateScore {

        /**
         * matchResult : 80:80
         */

        private String matchResult;

        public String getMatchResult() {
            return matchResult;
        }

        public void setMatchResult(String matchResult) {
            this.matchResult = matchResult;
        }
    }
}
