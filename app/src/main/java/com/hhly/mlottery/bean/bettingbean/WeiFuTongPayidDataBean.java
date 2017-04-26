package com.hhly.mlottery.bean.bettingbean;

/**
 * 威富通支付参数的bean
 */

public class WeiFuTongPayidDataBean {
//    data	Object
//    httpCode	200
//    msg	请求成功
//    timestamp	1492570741780
    private PayData data;
    private Integer httpCode;
    private String msg;
    private long timestamp;

    public PayData getData() {
        return data;
    }

    public void setData(PayData data) {
        this.data = data;
    }

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public class PayData{
//        dataMap	Object
//        errCode	0
//        errMsg
//        exception
//        suggestion
        private PayDataMap dataMap;
        private String errCode;
        private String errMsg;
        private String exception;
        private String suggestion;

        public PayDataMap getDataMap() {
            return dataMap;
        }

        public void setDataMap(PayDataMap dataMap) {
            this.dataMap = dataMap;
        }

        public String getErrCode() {
            return errCode;
        }

        public void setErrCode(String errCode) {
            this.errCode = errCode;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }

        public String getException() {
            return exception;
        }

        public void setException(String exception) {
            this.exception = exception;
        }

        public String getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(String suggestion) {
            this.suggestion = suggestion;
        }

        public class PayDataMap{
//            out_trade_no	hhly-android-1001
//            services	pay.weixin.jspay|pay.weixin.micropay|pay.weixin.native|pay.weixin.app
//            token_id	88590478e43bce473ca95df7f8a38dd0
            private String out_trade_no;
            private String services;
            private String token_id;

            public String getOut_trade_no() {
                return out_trade_no;
            }

            public void setOut_trade_no(String out_trade_no) {
                this.out_trade_no = out_trade_no;
            }

            public String getServices() {
                return services;
            }

            public void setServices(String services) {
                this.services = services;
            }

            public String getToken_id() {
                return token_id;
            }

            public void setToken_id(String token_id) {
                this.token_id = token_id;
            }
        }
    }

}
