package com.hhly.mlottery.bean.chart;

/**
 * Created by yuely198 on 2016/12/14.
 */

public class SendMessageBean {

    /**
     * result : 200
     * data : {"resultCode":1000,"resultMsg":"发送成功","resultObj":null}
     */

    private String result;
    private DataBean data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * resultCode : 1000
         * resultMsg : 发送成功
         * resultObj : null
         */

        private int resultCode;
        private String resultMsg;
        private Object resultObj;

        public int getResultCode() {
            return resultCode;
        }

        public void setResultCode(int resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }

        public Object getResultObj() {
            return resultObj;
        }

        public void setResultObj(Object resultObj) {
            this.resultObj = resultObj;
        }
    }
}
