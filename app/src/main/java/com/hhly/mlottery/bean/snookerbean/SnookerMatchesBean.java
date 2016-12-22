package com.hhly.mlottery.bean.snookerbean;

import java.util.List;

/**
 * Created by yixq on 2016/11/17.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerMatchesBean {

//    leaguesId: "125387",
//    leaguesName: "英国锦标赛",
//    thirdId: "570805",
//    subLgName: "128强",
//    date: "2016-11-23",
//    time: "17:30",
//    homeTeam: "K.多赫迪",
//    guestTeam: "N.萨恩卡姆",
//    homeId: 80,
//    guestId: 354,
//    dataType: "3",
//    matchStyle: "11",
//    delps: "",
//    status: "1",
//    destoryDateTime: null,
//    matchScore: null,
//    games: null,
//    matchOdds: null

    private String leaguesId;
    private String leaguesName;
    private String thirdId;
    private String subLgName;
    private String date;
    private String time;
    private String homeTeam;
    private String guestTeam;
    private String homeId;
    private String guestId;
    private String dataType;
    private String matchStyle;
    private String delps;
    private String status;
    private SnookerMatchScoreBean matchScore;
    private List<SnookerGanesBean> games;
    private String destoryDateTime;
    private SnookerMatchOddsBean matchOdds;

    /**
     * date	2016-11-16
     * leaguesId	125358
     * leaguesName	北爱尔兰公开赛
     * 新增 type字段，区分item类型 [ 0 日期 ； 1 联赛 ； 2 比赛]
     * 新增 itemDate 、itemLeaguesId 、itemLeaguesName字段，给不同类型item赋值
     */
    private int itemType;
    private String itemDate;
    private String itemLeaguesId;
    private String itemLeaguesName;


    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getItemDate() {
        return itemDate;
    }

    public void setItemDate(String itemDate) {
        this.itemDate = itemDate;
    }

    public String getItemLeaguesId() {
        return itemLeaguesId;
    }

    public void setItemLeaguesId(String itemLeaguesId) {
        this.itemLeaguesId = itemLeaguesId;
    }

    public String getItemLeaguesName() {
        return itemLeaguesName;
    }

    public void setItemLeaguesName(String itemLeaguesName) {
        this.itemLeaguesName = itemLeaguesName;
    }

    public String getLeaguesId() {
        return leaguesId;
    }

    public void setLeaguesId(String leaguesId) {
        this.leaguesId = leaguesId;
    }

    public String getLeaguesName() {
        return leaguesName;
    }

    public void setLeaguesName(String leaguesName) {
        this.leaguesName = leaguesName;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getSubLgName() {
        return subLgName;
    }

    public void setSubLgName(String subLgName) {
        this.subLgName = subLgName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(String guestTeam) {
        this.guestTeam = guestTeam;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getMatchStyle() {
        return matchStyle;
    }

    public void setMatchStyle(String matchStyle) {
        this.matchStyle = matchStyle;
    }

    public String getDelps() {
        return delps;
    }

    public void setDelps(String delps) {
        this.delps = delps;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SnookerMatchScoreBean getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(SnookerMatchScoreBean matchScore) {
        this.matchScore = matchScore;
    }

    public List<SnookerGanesBean> getGames() {
        return games;
    }

    public void setGames(List<SnookerGanesBean> games) {
        this.games = games;
    }

    public String getDestoryDateTime() {
        return destoryDateTime;
    }

    public void setDestoryDateTime(String destoryDateTime) {
        this.destoryDateTime = destoryDateTime;
    }

    public SnookerMatchOddsBean getMatchOdds() {
        return matchOdds;
    }

    public void setMatchOdds(SnookerMatchOddsBean matchOdds) {
        this.matchOdds = matchOdds;
    }
}
