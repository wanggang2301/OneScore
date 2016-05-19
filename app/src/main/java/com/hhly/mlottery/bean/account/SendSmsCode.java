package com.hhly.mlottery.bean.account;

/**
 * Created by lyx on 2016/5/19.
 */
public class SendSmsCode {

    /**
     * result : 200
     * msg : SMS_SEND_SUCCESS
     * data : {}
     */
    private int result;
    private String msg;
    private DataBean data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
    }
}
