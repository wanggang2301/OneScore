package com.hhly.mlottery.bean.custombean.customlistdata;

import android.content.Intent;

import java.util.List;

/**
 * Created by yixq on 2016/12/14.
 * mail：yixq@13322.com
 * describe:
 */

public class CustomListBean {

//    concernLeagueAndTeam	Object
//    result	200

    private CustomData concernLeagueAndTeam;
    private int result;

    public CustomData getConcernLeagueAndTeam() {
        return concernLeagueAndTeam;
    }

    public void setConcernLeagueAndTeam(CustomData concernLeagueAndTeam) {
        this.concernLeagueAndTeam = concernLeagueAndTeam;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public class CustomData {

//        leagueConcerns	Array
        private List<CustomFristBean> leagueConcerns;

        public List<CustomFristBean> getLeagueConcerns() {
            return leagueConcerns;
        }

        public void setLeagueConcerns(List<CustomFristBean> leagueConcerns) {
            this.leagueConcerns = leagueConcerns;
        }
    }
}
