package com.hhly.mlottery.bean.bettingbean;

/**
 * Created by：XQyi on 2017/6/1 20:55
 * Use: 支付宝充值实体Bean
 */
public class PaymentZFBBean {
//    code	200
//    data	Object

    private int code;
    private DatasBeanDetails data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DatasBeanDetails getData() {
        return data;
    }

    public void setData(DatasBeanDetails data) {
        this.data = data;
    }

    public class DatasBeanDetails{
//        "body": "alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2017050407114590&biz_content=%7B%22out_trade_no%22%3A%22RHGHHLY000001661496320756245%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22APP%E5%85%85%E5%80%BC%22%2C%22timeout_express%22%3A%22120m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F116.76.254.8%3A9035%2Fsunon-web-api%2FpayNotify%2Falipay&sign=kTL9yHi5MkTpFnnrqF%2FJmKlZKVo9kOl4djH1CgEBXdCRxbjDDj9vCs7TwDl8hx1MIdyA8x8YudjqXbdFShiduMvINgMlFeQcyAnOgIxPytVYWGXTjziwQ05XObLVTahkCDYH6hfnra0rV27G7f2MePJd72SYc5ZrTGk5yeh1b1jEoJ4y4ujt9cTAKTcXyBnB6jMl1vUme6Qwb0uOcwwXzhv%2Fd%2FI8rUHZzw1mFPGKlCI6kVS1YZHha%2BpNO9VfzuQJaPF39ZfMYoxKMh75CkaWrYSW3OogYHLKA76yk%2FBicnFiNECYWzRjhY2DJs612vqRNiw3M%2B2dBtMRf3IdXcH4Zg%3D%3D&sign_type=RSA2&timestamp=2017-06-01+20%3A39%3A16&version=1.0",
//        "subMsg": null,
//        "subCode": null,
//        "tradeNo": null,
//        "sellerId": null,
//        "totalAmount": null,
//        "errorCode": null,
//        "outTradeNo": "RHGHHLY000001661496320756245",
//        "params": null,
//        "code": null,
//        "success": true,
//        "msg": null

        private String body;
        private boolean success;

        private String subMsg;
        private String subCode;
        private String tradeNo;
        private String sellerId;
        private String totalAmount;
        private String errorCode;
        private String outTradeNo;
        private String params;
        private String code;
        private String msg;

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

        public String getSubMsg() {
            return subMsg;
        }

        public void setSubMsg(String subMsg) {
            this.subMsg = subMsg;
        }

        public String getSubCode() {
            return subCode;
        }

        public void setSubCode(String subCode) {
            this.subCode = subCode;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
