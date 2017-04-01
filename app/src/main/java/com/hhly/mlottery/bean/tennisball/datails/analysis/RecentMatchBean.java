package com.hhly.mlottery.bean.tennisball.datails.analysis;

/**
 * desc:
 * Created by 107_tangrr on 2017/3/27 0027.
 */

public class RecentMatchBean {

    private MatchRecordBean homePlayerRecentMatch;
    private MatchRecordBean guestPlayerRecentMatch;

    public MatchRecordBean getHomePlayerRecentMatch() {
        return homePlayerRecentMatch;
    }

    public void setHomePlayerRecentMatch(MatchRecordBean homePlayerRecentMatch) {
        this.homePlayerRecentMatch = homePlayerRecentMatch;
    }

    public MatchRecordBean getGuestPlayerRecentMatch() {
        return guestPlayerRecentMatch;
    }

    public void setGuestPlayerRecentMatch(MatchRecordBean guestPlayerRecentMatch) {
        this.guestPlayerRecentMatch = guestPlayerRecentMatch;
    }
}
