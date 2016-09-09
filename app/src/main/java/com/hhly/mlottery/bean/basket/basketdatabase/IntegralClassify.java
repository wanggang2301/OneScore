package com.hhly.mlottery.bean.basket.basketdatabase;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */

public class IntegralClassify {

    private List<RankingGroup> all;
    private List<RankingGroup> home;
    private List<RankingGroup> guest;

    public List<RankingGroup> getAll() {
        return all;
    }

    public void setAll(List<RankingGroup> all) {
        this.all = all;
    }

    public List<RankingGroup> getHome() {
        return home;
    }

    public void setHome(List<RankingGroup> home) {
        this.home = home;
    }

    public List<RankingGroup> getGuest() {
        return guest;
    }

    public void setGuest(List<RankingGroup> guest) {
        this.guest = guest;
    }
}
