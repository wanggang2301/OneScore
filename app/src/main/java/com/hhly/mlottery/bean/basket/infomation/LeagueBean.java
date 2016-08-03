package com.hhly.mlottery.bean.basket.infomation;

/**
 * @author wang gang
 * @date 2016/7/21 10:28
 * @des 联赛
 */
public class LeagueBean {

    private String leagueId;
    private String leagueName;
    private String leagueLogoUrl;


    public LeagueBean() {
    }

    public LeagueBean(String leagueId, String leagueLogoUrl, String leagueName) {
        this.leagueId = leagueId;
        this.leagueLogoUrl = leagueLogoUrl;
        this.leagueName = leagueName;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getLeagueLogoUrl() {
        return leagueLogoUrl;
    }

    public void setLeagueLogoUrl(String leagueLogoUrl) {
        this.leagueLogoUrl = leagueLogoUrl;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }


}
