package com.hhly.mlottery.bean.custombean.CustomMineBean;

import java.util.List;

/**
 * Created by yixq on 2016/12/15.
 * mail：yixq@13322.com
 * describe: 我的定制第一层数据实体（联赛）
 */

public class CustomMineFirstDataBean {

//    leagueId	1
//    leagueName	NBA
//    matchData	Array

    private String leagueId;
    private String leagueName;
    private List<CustomMineScondDataBean> matchData;
    private String leagueLogoPre;
    private String leagueLogoSuff;

    private int firstType = 0;

    public int getFirstType() {
        return firstType;
    }

    public void setFirstType(int firstType) {
        this.firstType = firstType;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public List<CustomMineScondDataBean> getMatchData() {
        return matchData;
    }

    public void setMatchData(List<CustomMineScondDataBean> matchData) {
        this.matchData = matchData;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getLeagueLogoPre() {
        return leagueLogoPre;
    }

    public void setLeagueLogoPre(String leagueLogoPre) {
        this.leagueLogoPre = leagueLogoPre;
    }

    public String getLeagueLogoSuff() {
        return leagueLogoSuff;
    }

    public void setLeagueLogoSuff(String leagueLogoSuff) {
        this.leagueLogoSuff = leagueLogoSuff;
    }
}
