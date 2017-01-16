package com.hhly.mlottery.bean;

/**
 * Created by yuely198 on 2017/1/14.
 * 邀请码获取实体bean
 */

public class InvitedBean {

    /**
     * result : 0
     * msg : 成功
     * data : {"inviteCode":"3f6b0cd1"}
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
        /**
         * inviteCode : 3f6b0cd1
         */

        private String inviteCode;

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }
    }
}
