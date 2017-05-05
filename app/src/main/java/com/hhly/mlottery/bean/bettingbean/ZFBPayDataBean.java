package com.hhly.mlottery.bean.bettingbean;

/**
 * Created by：XQyi on 2017/5/3 15:03
 * Use:支付宝支付的参数bean
 */
public class ZFBPayDataBean {

//    body String APP调用SDK下单的orderString参数
//    success Boolean 接口请求是否成功

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
