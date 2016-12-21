package com.hhly.mlottery.bean.custombean.customlistdata;

import java.util.List;

/**
 * Created by yixq on 2016/12/14.
 * mail：yixq@13322.com
 * describe:
 */

public class CustomFristBean {

//    isConcern	false
//    leagueId	1
//    leagueName	NBA
//    legueConcernNum	1
//    teamConcernInfo	Object

    private boolean isConcern;
    private String leagueId;
    private String leagueName;
    private String legueConcernNum;
    private String leagueLogoPre;
    private String leagueLogoSuff;
    private List<CustomSecondBean> teamConcerns;

    private int firstType = 0;

    public int getFirstType() {
        return firstType;
    }

    public void setFirstType(int firstType) {
        this.firstType = firstType;
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

    public List<CustomSecondBean> getTeamConcerns() {
        return teamConcerns;
    }

    public void setTeamConcerns(List<CustomSecondBean> teamConcerns) {
        this.teamConcerns = teamConcerns;
    }

    public boolean isConcern() {
        return isConcern;
    }

    public void setConcern(boolean concern) {
        isConcern = concern;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getLegueConcernNum() {
        return legueConcernNum;
    }

    public void setLegueConcernNum(String legueConcernNum) {
        this.legueConcernNum = legueConcernNum;
    }
}
