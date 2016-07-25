package com.hhly.mlottery.bean.basket.infomation;

import java.util.List;

/**
 * @author wang gang
 * @date 2016/7/21 10:43
 * @des 国家联赛Bean
 */
public class NationalLeague {

    private String nationName;
    private String nationLogoUrl;
    private String nationId;
    private boolean isShow = false;

    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    private List<LeagueBean> leagueData;


    public List<LeagueBean> getLeagueData() {
        return leagueData;
    }

    public void setLeagueData(List<LeagueBean> leagueData) {
        this.leagueData = leagueData;
    }

    public String getNationName() {
        return nationName;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }

    public String getNationLogoUrl() {
        return nationLogoUrl;
    }

    public void setNationLogoUrl(String nationLogoUrl) {
        this.nationLogoUrl = nationLogoUrl;
    }

    public String getNationId() {
        return nationId;
    }

    public void setNationId(String nationId) {
        this.nationId = nationId;
    }
}
