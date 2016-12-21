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
    private String userLogo;
    private String userNick;
    public FromUserBean(String userId, String userLogo, String userNick) {
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

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }
}
