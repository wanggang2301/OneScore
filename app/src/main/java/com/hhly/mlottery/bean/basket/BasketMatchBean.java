package com.hhly.mlottery.bean.basket;

import java.util.Map;

/**
 * Created by A on 2016/1/4.
 * <p/>
 * 篮球实体bean ；
 */
public class BasketMatchBean {

    private int type; //区分哪天数据

    private String date; //	2016-01-03
    private String guestTeam; //	尼普頓拿斯
    private String homeTeam;  //	尼普頓拿斯
    private boolean hot;//	false
    private String leagueColor;//	#006699
    private String leagueId;//	30951
    private Integer matchType;//	30951
    private String leagueName;//	立陶甲
//    private Map<String, BasketOddBean> matchOdds;//	-- 赔率数据bean  BasketOddBean
    private Map<String, BasketAllOddBean> matchOdds;//	-- 赔率数据bean  BasketOddBean
    //    private BasketOddsBean matchOdds;
    private BasketScoreBean matchScore;//	--比赛得分数据bean
    private String time;//	23:59
    private String thirdId;//: "296389",

    private String matchStatus;//
    private String homeRanking;//
    private String guestRanking;//
    private int section;

    private String guestTeamId; //	467
    private String homeTeamId; //	2233

    private String guestLogoUrl; //	http://pic.13322.com/basketball/team/135_135/1898.png
    private String homeLogoUrl; //	http://pic.13322.com/basketball/team/135_135/2373.png


    private boolean isHomeAnim;
    private boolean isGuestAnim;
    public Integer getMatchType() {
        return matchType;
    }

    public void setMatchType(Integer matchType) {
        this.matchType = matchType;
    }

    public boolean isGuestAnim() {
        return isGuestAnim;
    }

    public void setIsGuestAnim(boolean isGuestAnim) {
        this.isGuestAnim = isGuestAnim;
    }

    public boolean isHomeAnim() {
        return isHomeAnim;
    }

    public void setIsHomeAnim(boolean isHomeAnim) {
        this.isHomeAnim = isHomeAnim;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getGuestLogoUrl() {
        return guestLogoUrl;
    }

    public void setGuestLogoUrl(String guestLogoUrl) {
        this.guestLogoUrl = guestLogoUrl;
    }

    public String getHomeLogoUrl() {
        return homeLogoUrl;
    }

    public void setHomeLogoUrl(String homeLogoUrl) {
        this.homeLogoUrl = homeLogoUrl;
    }


//    private int addTime;
//    private String matchId;//	224403
//    private String remainTime;//

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }


    public String getHomeRanking() {
        return homeRanking;
    }

    public void setHomeRanking(String homeRanking) {
        this.homeRanking = homeRanking;
    }

    public String getGuestRanking() {
        return guestRanking;
    }

    public void setGuestRanking(String guestRanking) {
        this.guestRanking = guestRanking;
    }

    public String getThirdId() {

        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
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

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
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

    public String getGuestTeamId() {
        return guestTeamId;
    }

    public void setGuestTeamId(String guestTeamId) {
        this.guestTeamId = guestTeamId;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(String homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public Map<String, BasketAllOddBean> getMatchOdds() {
        return matchOdds;
    }

    public void setMatchOdds(Map<String, BasketAllOddBean> matchOdds) {
        this.matchOdds = matchOdds;
    }

    public BasketScoreBean getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(BasketScoreBean matchScore) {
        this.matchScore = matchScore;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
