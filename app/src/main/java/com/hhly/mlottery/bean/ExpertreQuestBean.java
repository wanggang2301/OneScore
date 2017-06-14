package com.hhly.mlottery.bean;

/**
 * Created by yuely198 on 2017/6/7.
 */

public class ExpertreQuestBean {


    /**
     * code : 200
     * userInfo : {"userId":"hhly91754","realName":"王哈哈","idCard":"341602199401073477","introduce":"哈哈,这是一奥术大师快乐环岛库拉索打算离开的阿深 打了卡水库里的阿里控件等卡死几点啦","skillfulLeague":"大时代h,asdhj,啊四大皆空","isExpert":1,"approveIdea":"同意"}
     */

    private String code;
    private UserInfoBean userInfo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserInfoBean {
        /**
         * userId : hhly91754
         * realName : 王哈哈
         * idCard : 341602199401073477
         * introduce : 哈哈,这是一奥术大师快乐环岛库拉索打算离开的阿深 打了卡水库里的阿里控件等卡死几点啦
         * skillfulLeague : 大时代h,asdhj,啊四大皆空
         * isExpert : 1
         * approveIdea : 同意
         */

        private String userId;
        private String realName;
        private String idCard;
        private String introduce;
        private String skillfulLeague;
        private int isExpert;
        private String approveIdea;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getSkillfulLeague() {
            return skillfulLeague;
        }

        public void setSkillfulLeague(String skillfulLeague) {
            this.skillfulLeague = skillfulLeague;
        }

        public int getIsExpert() {
            return isExpert;
        }

        public void setIsExpert(int isExpert) {
            this.isExpert = isExpert;
        }

        public String getApproveIdea() {
            return approveIdea;
        }

        public void setApproveIdea(String approveIdea) {
            this.approveIdea = approveIdea;
        }
    }
}
