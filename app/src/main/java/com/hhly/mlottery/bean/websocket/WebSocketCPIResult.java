package com.hhly.mlottery.bean.websocket;

/**
 * 指数
 * <p/>
 * Created by loshine on 2016/6/22.
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
