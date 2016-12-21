package com.hhly.mlottery.util;

/**
 * Created by yixq on 2016/12/17.
 * mail：yixq@13322.com
 * describe:
 */

public class CustomListEvent {

    private String mLeagueMsg;
    private String mTeamMsg;

    public CustomListEvent(String mLeagueMsg , String mTeamMsg) {
        this.mLeagueMsg = mLeagueMsg;
        this.mTeamMsg = mTeamMsg;
    }

    public String getmLeagueMsg(){
        return mLeagueMsg;
    }
    public String getmTeamMsg(){
        return mTeamMsg;

    }
}
