package com.hhly.mlottery.bean.account;

/**
 * Created by lyx on 2016/5/19.
 * 注册返回bean （完整内容）
 */
public class Register {
    /**
     * token : eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJqd3QiLCJpYXQiOjE0OTU3ODgwODgsInN1YiI6IntcImlkXCI6XCJoaGx5OTA1NTNcIixcInBob25lTnVtXCI6XCIxMzY0NTY3OTc5N1wiLFwibmlja25hbWVcIjpcIuiAgeayueadoVwifSIsImV4cCI6MTQ5NTg3NDQ4OH0.3Fy-gg-f38xMdSbpP83Z9SDfr_hOTJOOkie5sbTre9w
     * code : 200
     * user : {"userId":"hhly90553","isExpert":null,"phoneNum":13645679797,"nickName":"老油条","indiProfile":null,"goodLeague":null,"amount":null,"income":null,"imageSrc":null}
     */

    private String token;

    public String getFailureAmount() {
        return failureAmount;
    }

    public void setFailureAmount(String failureAmount) {
        this.failureAmount = failureAmount;
    }

    private String failureAmount;
    private String code;
    private UserBean user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * userId : hhly90553
         * isExpert : null
         * phoneNum : 13645679797
         * nickName : 老油条
         * indiProfile : null
         * goodLeague : null
         * amount : null
         * income : null
         * imageSrc : null
         */

        private String userId;
        private int isExpert;
        private String phoneNum;
        private String nickName;
        private String indiProfile;
        private String goodLeague;
        private String amount;
        private String income;
        private String imageSrc;
        private String availableBalance;
        private String cashBalance;
        private String buyCount;

        public String getAvailableBalance() {
            return availableBalance;
        }

        public void setAvailableBalance(String availableBalance) {
            this.availableBalance = availableBalance;
        }

        public String getCashBalance() {
            return cashBalance;
        }

        public void setCashBalance(String cashBalance) {
            this.cashBalance = cashBalance;
        }

        public String getBuyCount() {
            return buyCount;
        }

        public void setBuyCount(String buyCount) {
            this.buyCount = buyCount;
        }

        public String getPushCount() {
            return pushCount;
        }

        public void setPushCount(String pushCount) {
            this.pushCount = pushCount;
        }

        private String pushCount;



        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getIsExpert() {
            return isExpert;
        }

        public void setIsExpert(int isExpert) {
            this.isExpert = isExpert;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getIndiProfile() {
            return indiProfile;
        }

        public void setIndiProfile(String indiProfile) {
            this.indiProfile = indiProfile;
        }

        public String getGoodLeague() {
            return goodLeague;
        }

        public void setGoodLeague(String goodLeague) {
            this.goodLeague = goodLeague;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getImageSrc() {
            return imageSrc;
        }

        public void setImageSrc(String imageSrc) {
            this.imageSrc = imageSrc;
        }
    }


  /*  *//**
     * result : 0
     * msg : 成功
     * data : {"loginToken":"92a47bc9b7364481ab5fb622e5bbc996","user":{"nickName":"hhly90203","userId":"hhly90203"}}
     *//*

    private int result;
    private String msg;
    *//**
     * loginToken : 92a47bc9b7364481ab5fb622e5bbc996
     * user : {"nickName":"hhly90203","userId":"hhly90203"}
     *//*

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
        *//**
         * nickName : hhly90203
         * userId : hhly90203
         *//*

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
    }*/
}
