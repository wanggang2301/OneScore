package com.hhly.mlottery.bean;

/**
 * Created by yuely198 on 2016/12/6.
 */

public class BarrageBean {

    private String url;
    private String  msg;
    public   BarrageBean (String url,String msg){

        this.url=url;
        this.msg=msg;

    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
