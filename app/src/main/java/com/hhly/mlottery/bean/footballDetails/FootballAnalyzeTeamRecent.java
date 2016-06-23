package com.hhly.mlottery.bean.footballDetails;

import java.util.List;

/**
 * description:
 * author: yixq
 * Created by A on 2016/6/13.
 */
public class FootballAnalyzeTeamRecent {
    private List<FootballAnaylzeHistoryRecent> guest; //	Array
    private List<FootballAnaylzeHistoryRecent> home; //	Array

    public List<FootballAnaylzeHistoryRecent> getGuest() {
        return guest;
    }

    public void setGuest(List<FootballAnaylzeHistoryRecent> guest) {
        this.guest = guest;
    }

    public List<FootballAnaylzeHistoryRecent> getHome() {
        return home;
    }

    public void setHome(List<FootballAnaylzeHistoryRecent> home) {
        this.home = home;
    }
}
