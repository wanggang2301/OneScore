package com.hhly.mlottery.bean.footballteaminfo;

import java.util.List;

/**
 * desc:足球球队数据
 * Created by 107_tangrr on 2017/4/20 0020.
 */

public class FootTeamDataBean {

    private int code;
    private int type; // 0:赛季 1:年
    private List<String> seasonList; // 赛季时间
    private List<String> yearList; // 赛程赛果选择时间
    private List<FootTeamInfoBean> teamInfo;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getSeasonList() {
        return seasonList;
    }

    public void setSeasonList(List<String> seasonList) {
        this.seasonList = seasonList;
    }

    public List<String> getYearList() {
        return yearList;
    }

    public void setYearList(List<String> yearList) {
        this.yearList = yearList;
    }

    public List<FootTeamInfoBean> getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(List<FootTeamInfoBean> teamInfo) {
        this.teamInfo = teamInfo;
    }
}
