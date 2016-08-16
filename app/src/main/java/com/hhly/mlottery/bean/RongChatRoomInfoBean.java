package com.hhly.mlottery.bean;

/**
 * 描  述：获取聊天室信息类
 * 作  者：tangrr@13322.com
 * 时  间：2016/8/16
 */
public class RongChatRoomInfoBean {

    private int code;// 200：成功。
    private int total;// 聊天室中用户数。
    private String[] users;// 	聊天室成员数组。
    private String id;// 用户 Id。
    private String time;// 	加入聊天室时间。

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
