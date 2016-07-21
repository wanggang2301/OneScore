package com.hhly.mlottery.bean.basket.infomation;

import java.util.List;

/**
 * @author wang gang
 * @date 2016/7/21 10:42
 * @des ${TODO}
 */
public class LeagueAllBean {

    private String result;
    private List<LeagueBean> specificLeague;
    private List<NationalLeague> nationalLeague;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<LeagueBean> getSpecificLeague() {
        return specificLeague;
    }

    public void setSpecificLeague(List<LeagueBean> specificLeague) {
        this.specificLeague = specificLeague;
    }

    public List<NationalLeague> getNationalLeague() {
        return nationalLeague;
    }

    public void setNationalLeague(List<NationalLeague> nationalLeague) {
        this.nationalLeague = nationalLeague;
    }
}
