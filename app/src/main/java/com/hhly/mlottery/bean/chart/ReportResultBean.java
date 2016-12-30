package com.hhly.mlottery.bean.chart;

/**
 * desc:
 * Created by 107_tangrr on 2016/12/15 0015.
 */

public class ReportResultBean {

//    {"result":"200","data":{"resultCode":1000,"resultMsg":"发送成功","resultObj":null}}
    private String result;
    private DataBean data;

    public static class DataBean {
        int resultCode;
        String resultMsg;
        String resultObj;

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

        public String getResultObj() {
            return resultObj;
        }

        public void setResultObj(String resultObj) {
            this.resultObj = resultObj;
        }
    }

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
}
