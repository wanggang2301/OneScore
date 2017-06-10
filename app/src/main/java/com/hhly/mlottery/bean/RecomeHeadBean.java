package com.hhly.mlottery.bean;

/**
 * Created by yuely198 on 2017/6/10.
 */

public class RecomeHeadBean {


    /**
     * code : 200
     * userInfo : {"userId":"HHLY00000136","nickname":"帕尔梅拉斯作客1:1战平土库曼竞技","introduce":"由一比分官网获取数据：伊拉克在亚州预选情况不太乐观，目前以3分排在小组倒数第二位，基本失去晋级资格，本场比赛战意成疑，首循环作客0:2不敌澳大利亚，此番回归主场但在战意不明的情况下很可能惨遭双杀。澳大利亚澳大利亚队目前积9分排在小组第3位，与小组头名相差1分，有机会以小组第一出线，战意十足。不过目前球队处于重建阶段，年轻化使得球员门缺乏大赛经验，不过以目前状态而言，拿下伊拉克还是有足够的能力1","skillfulLeague":null,"imageSrc":"http://tp.1332255.com/img/2017-05-16/af2d1032-b9e2-49b6-8791-bd828336419a.jpg"}
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
         * userId : HHLY00000136
         * nickname : 帕尔梅拉斯作客1:1战平土库曼竞技
         * introduce : 由一比分官网获取数据：伊拉克在亚州预选情况不太乐观，目前以3分排在小组倒数第二位，基本失去晋级资格，本场比赛战意成疑，首循环作客0:2不敌澳大利亚，此番回归主场但在战意不明的情况下很可能惨遭双杀。澳大利亚澳大利亚队目前积9分排在小组第3位，与小组头名相差1分，有机会以小组第一出线，战意十足。不过目前球队处于重建阶段，年轻化使得球员门缺乏大赛经验，不过以目前状态而言，拿下伊拉克还是有足够的能力1
         * skillfulLeague : null
         * imageSrc : http://tp.1332255.com/img/2017-05-16/af2d1032-b9e2-49b6-8791-bd828336419a.jpg
         */

        private String userId;
        private String nickname;
        private String introduce;
        private String skillfulLeague;
        private String imageSrc;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public String getImageSrc() {
            return imageSrc;
        }

        public void setImageSrc(String imageSrc) {
            this.imageSrc = imageSrc;
        }
    }
}
