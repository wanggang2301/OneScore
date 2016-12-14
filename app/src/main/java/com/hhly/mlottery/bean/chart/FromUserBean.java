package com.hhly.mlottery.bean.chart;

/**
 * Created by yuely198 on 2016/12/13.
 */

public class FromUserBean {
    /**
     * userId : hhly299272
     * userLogo : null
     * userNick : hhly299272
     */
    private String userId;
    private Object userLogo;
    private String userNick;
    public FromUserBean(String userId, Object userLogo, String userNick) {
        this.userId = userId;
        this.userLogo = userLogo;
        this.userNick = userNick;
    }
    public FromUserBean() {

    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(Object userLogo) {
        this.userLogo = userLogo;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }
}
