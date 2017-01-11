package com.hhly.mlottery.bean.multiplebean;

/**
 * Created by yixq on 2017/1/9.
 * mail：yixq@13322.com
 * describe: 多屏动画列表传值类
 */

public class MultipleByValueBean {

    //区分足篮球 [0:篮球 ， 1:足球]
    private int type;

    public String thirdId;//比赛id
    public String MatchStatus;//比赛状态
    public String leagueId;//联赛id
    public String matchType;//联赛类型

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getMatchStatus() {
        return MatchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        MatchStatus = matchStatus;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }
}
