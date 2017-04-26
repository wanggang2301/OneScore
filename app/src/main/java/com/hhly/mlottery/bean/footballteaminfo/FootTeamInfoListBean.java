package com.hhly.mlottery.bean.footballteaminfo;

import java.util.List;

/**
 * desc:足球球员信息
 * Created by 107_tangrr on 2017/4/20 0020.
 */

public class FootTeamInfoListBean {

    private int code;
    private List<PlayerBean> player;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<PlayerBean> getPlayer() {
        return player;
    }

    public void setPlayer(List<PlayerBean> player) {
        this.player = player;
    }

    public static class PlayerBean {

        private int no;// 球衣号码
        private String birth;// 生日
        private String name;// 姓名
        private String nation;// 国籍
        private String position;// 位置
        private String val;// 身价

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }
}
