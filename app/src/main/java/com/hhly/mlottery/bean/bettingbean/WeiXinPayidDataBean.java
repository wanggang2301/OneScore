package com.hhly.mlottery.bean.bettingbean;

/**
 * Created by Administrator on 2017/4/20.
 * 原生微信支付参数的Bean
 */

public class WeiXinPayidDataBean {
//    data	Object
//    httpCode	200
//    msg	请求成功
//    timestamp	1492597079698
    private PayDataWX data;//接口业务数据
    private Integer httpCode;//HTTP请求状态
    private String msg;//HTTP请求状态信息
    private long timestamp;//HTTP请求发生时间

    public PayDataWX getData() {
        return data;
    }

    public void setData(PayDataWX data) {
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

    public class PayDataWX{
//        dataMap	Object
//        errCode	0
//        errMsg
//                exception
//        suggestion
        private PayDataMapWX dataMap; //正常返回结果
        private String errCode; //错误代码
//        private String errMsg; //错误消息
//        private String exception;//异常消息
//        private String suggestion;//建议处理方案

        public PayDataMapWX getDataMap() {
            return dataMap;
        }

        public void setDataMap(PayDataMapWX dataMap) {
            this.dataMap = dataMap;
        }

        public String getErrCode() {
            return errCode;
        }

        public void setErrCode(String errCode) {
            this.errCode = errCode;
        }

//        public String getErrMsg() {
//            return errMsg;
//        }
//
//        public void setErrMsg(String errMsg) {
//            this.errMsg = errMsg;
//        }
//
//        public String getException() {
//            return exception;
//        }
//
//        public void setException(String exception) {
//            this.exception = exception;
//        }
//
//        public String getSuggestion() {
//            return suggestion;
//        }
//
//        public void setSuggestion(String suggestion) {
//            this.suggestion = suggestion;
//        }

        public class PayDataMapWX{
            private String appid; //应用ID
            private String noncestr;//随机字符串
            private String packages;//扩展字段 暂填写固定值Sign=WXPay
            private String partnerid;//商户号
            private String prepayid;//预支付交易会话ID
            private String sign;//签名
            private String timestamp;//时间戳

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
            //            out_trade_no	hhly-ybf-1001111111111111111222
//            pay_info	Object
//            token_id	5710e9647eb5830fc2ad5f3999a3d0fd
//            transaction_id	755437000006201704195162316871
//            private String out_trade_no; //商户订单号
//            private PayInfo pay_info; //支付信息
//            private String token_id; //支付授权码
//            private String transaction_id;//平台订单号
//
//            public String getOut_trade_no() {
//                return out_trade_no;
//            }
//
//            public void setOut_trade_no(String out_trade_no) {
//                this.out_trade_no = out_trade_no;
//            }
//
//            public PayInfo getPay_info() {
//                return pay_info;
//            }
//
//            public void setPay_info(PayInfo pay_info) {
//                this.pay_info = pay_info;
//            }
//
//            public String getToken_id() {
//                return token_id;
//            }
//
//            public void setToken_id(String token_id) {
//                this.token_id = token_id;
//            }
//
//            public String getTransaction_id() {
//                return transaction_id;
//            }
//
//            public void setTransaction_id(String transaction_id) {
//                this.transaction_id = transaction_id;
//            }
//
//            public class PayInfo{
////                appid	wx2a5538052969956e
////                noncestr	1492597026947
////                        packages	Sign=WXPay  ssssssssss
////                partnerid	12723495
////                prepayid	wx201704191817067b0de929b70021371062
////                sign	5ADE9344313753D23DFB469977609D85
////                timestamp	1492597026
//                private String appid; //应用ID
//                private String noncestr;//随机字符串
//                private String packages;//扩展字段 暂填写固定值Sign=WXPay
//                private String partnerid;//商户号
//                private String prepayid;//预支付交易会话ID
//                private String sign;//签名
//                private String timestamp;//时间戳
//
//                public String getAppid() {
//                    return appid;
//                }
//
//                public void setAppid(String appid) {
//                    this.appid = appid;
//                }
//
//                public String getNoncestr() {
//                    return noncestr;
//                }
//
//                public void setNoncestr(String noncestr) {
//                    this.noncestr = noncestr;
//                }
//
//                public String getPackages() {
//                    return packages;
//                }
//
//                public void setPackages(String packages) {
//                    this.packages = packages;
//                }
//
//                public String getPartnerid() {
//                    return partnerid;
//                }
//
//                public void setPartnerid(String partnerid) {
//                    this.partnerid = partnerid;
//                }
//
//                public String getPrepayid() {
//                    return prepayid;
//                }
//
//                public void setPrepayid(String prepayid) {
//                    this.prepayid = prepayid;
//                }
//
//                public String getSign() {
//                    return sign;
//                }
//
//                public void setSign(String sign) {
//                    this.sign = sign;
//                }
//
//                public String getTimestamp() {
//                    return timestamp;
//                }
//
//                public void setTimestamp(String timestamp) {
//                    this.timestamp = timestamp;
//                }
//            }
        }
    }
}
