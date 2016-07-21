package com.hhly.mlottery.bean.basket.infomation;

import java.util.List;

/**
 * @author wang gang
 * @date 2016/7/21 10:43
 * @des 国家联赛Bean
 */
public class NationalLeague {

    private LeagueBean leagueBean;
    private List<LeagueBean> leagueData;


    public List<LeagueBean> getLeagueData() {
        return leagueData;
    }

    public void setLeagueData(List<LeagueBean> leagueData) {
        this.leagueData = leagueData;
    }

    public LeagueBean getLeagueBean() {
        return leagueBean;
    }

    public void setLeagueBean(LeagueBean leagueBean) {
        this.leagueBean = leagueBean;
    }

}
