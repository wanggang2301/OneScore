package com.hhly.mlottery.bean;

import java.util.Map;

/**
 * Created by yuely198 on 2017/5/17.
 */

public class FootBallKeepBean {


    /**
     * data : {"keepTime":14,"statusOrigin":1}
     * thirdId : 848847222
     * type : 1
     */

    private Map<String,String> data;
    private String thirdId;
    private int type;
    public Map<String, String> getData() {
        return data;
    }
    public void setData(Map<String, String> data) {
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



    public static class DataBean {
        /**
         * keepTime : 14
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
}
