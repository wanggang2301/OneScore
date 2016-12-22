package com.hhly.mlottery.bean.custombean.customlistdata;

/**
 * Created by yixq on 2016/12/14.
 * mail：yixq@13322.com
 * describe:
 */

public class CustomSecondBean {

//    isConcern	false
//    teamConcernNum	1
//    teamId	9
//    teamName	ดีทรอยต์ พิสตันส์
//    leagueId
    private String leagueId;
    private boolean isConcern;
    private String teamConcernNum;
    private String teamId;
    private String teamName;

    private int secondType = 1;

    private String teamLogoPre;
    private String teamLogoSuff;

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getTeamLogoPre() {
        return teamLogoPre;
    }

    public void setTeamLogoPre(String teamLogoPre) {
        this.teamLogoPre = teamLogoPre;
    }

    public String getTeamLogoSuff() {
        return teamLogoSuff;
    }

    public void setTeamLogoSuff(String teamLogoSuff) {
        this.teamLogoSuff = teamLogoSuff;
    }

    public int getSecondType() {
        return secondType;
    }

    public void setSecondType(int secondType) {
        this.secondType = secondType;
    }

    public boolean isConcern() {
        return isConcern;
    }

    public void setConcern(boolean concern) {
        isConcern = concern;
    }

    public String getTeamConcernNum() {
        return teamConcernNum;
    }

    public void setTeamConcernNum(String teamConcernNum) {
        this.teamConcernNum = teamConcernNum;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
