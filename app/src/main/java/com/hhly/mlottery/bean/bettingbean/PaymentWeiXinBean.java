package com.hhly.mlottery.bean.bettingbean;

/**
 * Created by：XQyi on 2017/6/1 20:46
 * Use: 微信充值实体Bean
 */
public class PaymentWeiXinBean {
//    code	200
//    data	Object

    private int code;
    private DataDetailsBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataDetailsBean getData() {
        return data;
    }

    public void setData(DataDetailsBean data) {
        this.data = data;
    }

    public class DataDetailsBean{
//        appid	wx7ef1da85d0f485e1
//        noncestr	BFKPSJAZ0NWU9SQNECLZJLTFCPN14HUN
//        outTradeNo	RHGHHLY000001661496320436265
//        packages	Sign=WXPay
//        partnerid	1469316602
//        prepayid	wx2017060120340198130076cd0595086843
//        sign	72842C834F55ACFADECD229F4E3C34CB
//        timestamp	1496320436

        private String appid; //应用ID
        private String noncestr;//随机字符串
        private String packages;//扩展字段 暂填写固定值Sign=WXPay
        private String partnerid;//商户号
        private String prepayid;//预支付交易会话ID
        private String sign;//签名
        private String timestamp;//时间戳
        private String outTradeNo;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackages() {
            return packages;
        }

        public void setPackages(String packages) {
            this.packages = packages;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }
    }
}
