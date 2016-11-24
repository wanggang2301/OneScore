package com.hhly.mlottery.bean.account;

/**
 * Created by lyx on 2016/5/19.
 * 注册返回bean （完整内容）
 */
public class Register {


    /**
     * result : 0
     * msg : 成功
     * data : {"loginToken":"92a47bc9b7364481ab5fb622e5bbc996","user":{"nickName":"hhly90203","userId":"hhly90203"}}
     */

    private int result;
    private String msg;
    /**
     * loginToken : 92a47bc9b7364481ab5fb622e5bbc996
     * user : {"nickName":"hhly90203","userId":"hhly90203"}
     */

    private DataBean data;


    public Register(){
        data = new DataBean();
    }
    public Register(DataBean data){
        this.data = data;
    }


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
        private String loginToken;
        /**
         * nickName : hhly90203
         * userId : hhly90203
         */

        private UserBean user;

        public DataBean(){
        }

        public String getLoginToken() {
            return loginToken;
        }

        public void setLoginToken(String loginToken) {
            this.loginToken = loginToken;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            private String nickName;
            private String userId;
            private String loginAccount;
            private String sex;
            private  String headIcon;
            public String getHeadIcon() {
                return headIcon;}
            public void setHeadIcon(String headIcon) {
                this.headIcon = headIcon;
            }

            public String getLoginAccount() {
                return loginAccount;
            }

            public void setLoginAccount(String loginAccount) {
                this.loginAccount = loginAccount;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }
            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }
            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            @Override
            public String toString() {
                return "UserBean{" +
                        "nickName='" + nickName + '\'' +
                        ", userId='" + userId + '\'' +
                        ", userId='" + headIcon + '\'' +
                        ", loginAccount='" + loginAccount + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "loginToken='" + loginToken + '\'' +
                    ", user=" + user +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Register{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
