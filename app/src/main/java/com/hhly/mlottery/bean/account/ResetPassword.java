package com.hhly.mlottery.bean.account;

/**
 * Created by lyx on 2016/6/6.
 * 重置密码 返回data : {} , 内容为空
 */
public class ResetPassword {

    /**
     * result : 8
     * msg : 验证码已失效
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
