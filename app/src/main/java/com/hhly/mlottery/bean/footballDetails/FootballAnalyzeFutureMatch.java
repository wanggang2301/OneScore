package com.hhly.mlottery.bean.footballDetails;

import java.util.List;

/**
 * description:
 * author: yixq
 * Created by A on 2016/6/13.
 */
public class FootballAnalyzeFutureMatch {
    private List<FootballAnalyzeFuture> guest; //	Array
    private List<FootballAnalyzeFuture> home; //	Array

    public List<FootballAnalyzeFuture> getGuest() {
        return guest;
    }

    public void setGuest(List<FootballAnalyzeFuture> guest) {
        this.guest = guest;
    }

    public List<FootballAnalyzeFuture> getHome() {
        return home;
    }

    public void setHome(List<FootballAnalyzeFuture> home) {
        this.home = home;
    }
}
