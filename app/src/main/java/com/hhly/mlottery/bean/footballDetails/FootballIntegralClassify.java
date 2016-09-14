package com.hhly.mlottery.bean.footballDetails;

import java.util.List;

/**
 * description:
 * author: yixq
 * Created by A on 2016/9/9.
 */

public class FootballIntegralClassify {

    private List<FootballRankingList> all;
    private List<FootballRankingList> home;
    private List<FootballRankingList> guest;

    public List<FootballRankingList> getAll() {
        return all;
    }

    public void setAll(List<FootballRankingList> all) {
        this.all = all;
    }

    public List<FootballRankingList> getHome() {
        return home;
    }

    public void setHome(List<FootballRankingList> home) {
        this.home = home;
    }

    public List<FootballRankingList> getGuest() {
        return guest;
    }

    public void setGuest(List<FootballRankingList> guest) {
        this.guest = guest;
    }
}
