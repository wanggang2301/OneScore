package com.hhly.mlottery.bean.basket.basketdatabase;

/**
 * description:
 * author: yixq
 * Created by A on 2016/7/20.
 */
public class BasketDatabaseBean {

    private String leagueName;//:"XBA",
    private String leagueId;//:"111",
    private String[] season; //:[xx,xx,xx,xx],
    private String leagueLogoUrl;//:"",
    private String bgUrl;//:"xxxxxxxxxx",

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

    public String[] getSeason() {
        return season;
    }

    public void setSeason(String[] season) {
        this.season = season;
    }

    public String getBgUrl() {
        return bgUrl;
    }

    public void setBgUrl(String bgUrl) {
        this.bgUrl = bgUrl;
    }

    public String getLeagueLogoUrl() {
        return leagueLogoUrl;
    }

    public void setLeagueLogoUrl(String leagueLogoUrl) {
        this.leagueLogoUrl = leagueLogoUrl;
    }
}
