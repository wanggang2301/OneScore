package com.hhly.mlottery.bean.custombean.CustomMineBean;

import com.hhly.mlottery.bean.basket.BasketAllOddBean;

import java.util.Map;

/**
 * Created by yixq on 2016/12/15.
 * mail：yixq@13322.com
 * describe: 我的定制 第三层（赛事层）
 */

public class CustomMineThirdDataBean {

//    date	2016-12-12
//    guestTeam	76人
//    guestTeamId	7
//    homeTeam	活塞
//    homeTeamId	9
//    leagueColor	#FF0000
//    leagueId	1
//    leagueName	NBA
//    matchOdds	Object
//    matchScore	Object
//    matchStatus	-1
//    section	4
//    thirdId	3806487
//    time	07:00

//    date	2016-12-13
//    guestLogoUrl	http://pic.13322.com/basketball/team/135_135/30.png
//    guestRanking	东3
//    guestTeam	黄蜂
//    guestTeamId	30
//    homeLogoUrl	http://pic.13322.com/basketball/team/135_135/10.png
//    homeRanking	东10
//    homeTeam	步行者
//    homeTeamId	10
//    hot	true
//    leagueColor	#FF0000
//    leagueId	1
//    leagueName	NBA
//    matchOdds	Object
//    matchScore	Object
//    matchStatus	-1
//    matchType	1
//    section	4
//    stageId	0
//    thirdId	3806987
//    time	08:00

    private String date;
    private String guestTeam;
    private String guestTeamId;
    private String homeTeam;
    private String homeTeamId;
    private String leagueColor;
    private String leagueId;
    private String leagueName;
    private Map<String, BasketAllOddBean> matchOdds;
    private CustomMatchScore matchScore;
    private String matchStatus;
    private int section;
    private String thirdId;
    private String time;
    private String guestLogoUrl;
    private String guestRanking;
    private String homeLogoUrl;
    private String homeRanking;
    private boolean hot;
    private boolean matchType;
    private boolean stageId;

    private boolean isHomeAnim;
    private boolean isGuestAnim;

    private int thirdType = 2;

    public int getThirdType() {
        return thirdType;
    }

    public void setThirdType(int thirdType) {
        this.thirdType = thirdType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(String guestTeam) {
        this.guestTeam = guestTeam;
    }

    public String getGuestTeamId() {
        return guestTeamId;
    }

    public void setGuestTeamId(String guestTeamId) {
        this.guestTeamId = guestTeamId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(String homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getLeagueColor() {
        return leagueColor;
    }

    public void setLeagueColor(String leagueColor) {
        this.leagueColor = leagueColor;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public Map<String, BasketAllOddBean> getMatchOdds() {
        return matchOdds;
    }

    public void setMatchOdds(Map<String, BasketAllOddBean> matchOdds) {
        this.matchOdds = matchOdds;
    }

    public CustomMatchScore getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(CustomMatchScore matchScore) {
        this.matchScore = matchScore;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGuestLogoUrl() {
        return guestLogoUrl;
    }

    public void setGuestLogoUrl(String guestLogoUrl) {
        this.guestLogoUrl = guestLogoUrl;
    }

    public String getGuestRanking() {
        return guestRanking;
    }

    public void setGuestRanking(String guestRanking) {
        this.guestRanking = guestRanking;
    }

    public String getHomeLogoUrl() {
        return homeLogoUrl;
    }

    public void setHomeLogoUrl(String homeLogoUrl) {
        this.homeLogoUrl = homeLogoUrl;
    }

    public String getHomeRanking() {
        return homeRanking;
    }

    public void setHomeRanking(String homeRanking) {
        this.homeRanking = homeRanking;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public boolean isMatchType() {
        return matchType;
    }

    public void setMatchType(boolean matchType) {
        this.matchType = matchType;
    }

    public boolean isStageId() {
        return stageId;
    }

    public void setStageId(boolean stageId) {
        this.stageId = stageId;
    }

    public boolean isHomeAnim() {
        return isHomeAnim;
    }

    public void setHomeAnim(boolean homeAnim) {
        isHomeAnim = homeAnim;
    }

    public boolean isGuestAnim() {
        return isGuestAnim;
    }

    public void setGuestAnim(boolean guestAnim) {
        isGuestAnim = guestAnim;
    }
}
