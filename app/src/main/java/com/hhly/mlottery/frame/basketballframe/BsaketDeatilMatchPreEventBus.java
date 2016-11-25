package com.hhly.mlottery.frame.basketballframe;

/**
 * @author: Wangg
 * @Name：BsaketDeatilMatchPreEventBus
 * @Description:
 * @Created on:2016/11/25  14:21.
 */

public class BsaketDeatilMatchPreEventBus {
    private String msg;

    public BsaketDeatilMatchPreEventBus(String msg) {
        this.msg = msg;
    }

    public String getMsg() {

        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
