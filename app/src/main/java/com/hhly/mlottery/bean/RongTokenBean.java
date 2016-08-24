package com.hhly.mlottery.bean;

/**
 * 描  述：从融云服务器获取用户Token实体类
 * 作  者：tangrr@13322.com
 * 时  间：2016/8/5
 */
public class RongTokenBean {
    private int code;
    private String userId;
    private String token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
