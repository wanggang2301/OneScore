package com.hhly.mlottery.bean;

/**
 * 描  述：EvenBus 帮忙类
 * 作  者：tangrr@13322.com
 * 时  间：2016/8/10
 */
public class FirstEvent {
    private String mMsg;// 传递消息

    public FirstEvent(String msg) {
        mMsg = msg;
    }

    public String getMsg() {
        return mMsg;
    }
}
