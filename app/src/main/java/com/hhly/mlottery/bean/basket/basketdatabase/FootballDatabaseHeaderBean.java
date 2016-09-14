package com.hhly.mlottery.bean.basket.basketdatabase;

/**
 * description:
 * author: yixq
 * Created by A on 2016/9/8.
 */
public class FootballDatabaseHeaderBean {

    /**
     *
     code: 200,
     season: [],
     matchType: 0,
     leagueId: 36,
     randomBg: "http://pic.13322.com/bg/6.png",
     leagueLogo: "http://pic.13322.com/icons/league/36.png",
     leagueName: "英超"
     */
    private String code;
    private String[] season; //:[xx,xx,xx,xx],
    private Integer matchType;
    private String leagueId;//:"111",
    private String randomBg;//:"xxxxxxxxxx",
    private String leagueLogo;//:"",
    private String leagueName;//:"XBA",

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String[] getSeason() {
        return season;
    }

    public void setSeason(String[] season) {
        this.season = season;
    }

    public Integer getMatchType() {
        return matchType;
    }

    public void setMatchType(Integer matchType) {
        this.matchType = matchType;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getRandomBg() {
        return randomBg;
    }

    public void setRandomBg(String randomBg) {
        this.randomBg = randomBg;
    }

    public String getLeagueLogo() {
        return leagueLogo;
    }

    public void setLeagueLogo(String leagueLogo) {
        this.leagueLogo = leagueLogo;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }
}
