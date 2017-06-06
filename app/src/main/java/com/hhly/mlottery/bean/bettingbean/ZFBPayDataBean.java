package com.hhly.mlottery.bean.bettingbean;

/**
 * Created by：XQyi on 2017/5/3 15:03
 * Use:支付宝支付的参数bean
 */
public class ZFBPayDataBean {

//    body String APP调用SDK下单的orderString参数
//    success Boolean 接口请求是否成功

//    data	Object
//    httpCode	200
//    msg	请求成功
//    timestamp	1495764187481

    private DataPayBean data;
    private Integer httpCode;
    private String msg;
    private long timestamp;
    public class DataPayBean{
//        dataMap	Object
//        errCode	0

        private DataMapBean dataMap;
        private Integer errCode;

        public DataMapBean getDataMap() {
            return dataMap;
        }

        public void setDataMap(DataMapBean dataMap) {
            this.dataMap = dataMap;
        }

        public Integer getErrCode() {
            return errCode;
        }

        public void setErrCode(Integer errCode) {
            this.errCode = errCode;
        }

        public class DataMapBean{
//            body	alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2017050407114590&biz_content=%7B%22out_trade_no%22%3A%22diva80000020%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22productName%22%2C%22timeout_express%22%3A%22120m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F116.76.254.8%3A9035%2Fsunon-web-api%2FpayNotify%2Falipay&sign=B301T1byhN9L%2BXHdyHn3iJaSkKX%2F6O8PUmdOw47L3yhHzyjAq9U3IPXZriM1zn8NG%2FgirH8Vb0XIgV4Lo4ppYZ83nFxNSy5WItvKlluKBRFGb3Bwvm8TTeYpVlFvmM6aqNr2hENwzweXKDPlTKGhAHRUY08g8OnuFbWNyJu5gC80QoOvz1CSpN3WiRGeVHib7JwWvRnY%2FRvR3t7rwe9hZU0KWNw5BWTPy3xtFECVWntlw%2BqN3CadcFkUafXnQ3mxWNzkK%2BkExR7j83mS0BeBCc1TxSWaKHC02ZnmY0CP7GqPTqHXEPwVgq6OJu%2B31PXiXsobuv3VXwdkrkw72%2ByK%2Fw%3D%3D&sign_type=RSA2&timestamp=2017-05-26+10%3A03%3A07&version=1.0
//            success	true
            private String body;
            private boolean success;

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public boolean isSuccess() {
                return success;
            }

            public void setSuccess(boolean success) {
                this.success = success;
            }
        }
    }

    public DataPayBean getData() {
        return data;
    }

    public void setData(DataPayBean data) {
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

    //    private String body;
//    private boolean success;
//
//    public String getBody() {
//        return body;
//    }
//
//    public void setBody(String body) {
//        this.body = body;
//    }
//
//    public boolean isSuccess() {
//        return success;
//    }
//
//    public void setSuccess(boolean success) {
//        this.success = success;
//    }
}
