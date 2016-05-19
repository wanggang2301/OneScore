package com.hhly.mlottery.bean.account;

/**
 * Created by lyx on 2016/5/19.
 */
public class Register {


    /**
     * result : 200
     * msg : LOGIN_SUCCESS
     * data : {"token":"ae983fb2cccf4dc094b6048ed4391dbd","user":{"nickName":"hhly90220","userId":"hhly90220"}}
     */

    private int result;
    private String msg;
    /**
     * token : ae983fb2cccf4dc094b6048ed4391dbd
     * user : {"nickName":"hhly90220","userId":"hhly90220"}
     */

    private DataBean data;


    public Register() {
        data = new DataBean();
    }

    public Register(DataBean dataBean) {
        data = dataBean;
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
        private String token;
        /**
         * nickName : hhly90220
         * userId : hhly90220
         */

        private UserBean user;

        public DataBean(){
            user = new UserBean();
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
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

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
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
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "token='" + token + '\'' +
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
