package com.hhly.mlottery.bean.footballteaminfo;

import java.util.List;

/**
 * desc:球队数据详情bean
 * Created by 107_tangrr on 2017/4/27 0027.
 */

public class FootTeamDataInfoBean {

    private int code;
    private List<FootTeamInfoBean> teamInfo;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<FootTeamInfoBean> getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(List<FootTeamInfoBean> teamInfo) {
        this.teamInfo = teamInfo;
    }
}
