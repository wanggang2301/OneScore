package com.hhly.mlottery.bean.snookerbean.snookerschedulebean;

import com.hhly.mlottery.bean.snookerbean.SnookerGanesBean;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchOddsBean;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchScoreBean;

import java.util.List;

/**
 * Created by yixq on 2017/2/18.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerEventsBean {

//    leagueId: "320973",
//    leagueName: "Coral Welsh Open",
//    matchId: "787038",
//    date: "2017-02-16",
//    time: "03:00",
//    homeTeam: "奥沙利文",
//    guestTeam: "Mark Davis",
//    homeId: "319672",
//    guestId: "320083",
//    gamesNumber: "7",
//    totalGames: "7",
//    gameResults: "3:4",
//    specificScore: "97-6,81-34,77-0,0-66,57-74,0-81,7-84",
//    round: "第二轮",
//    number: "65",
//    status: "2",
//    matchOdds: {},
//    matchScore: {}

    private String leagueId;//联赛id
    private String leagueName;//联赛名称
    private String matchId;//赛事id
    private String date;//比赛日期
    private String time;//比赛时间
    private String homeTeam;//主队名称
    private String guestTeam;//客队名称
    private String homeId;	//主队ID
    private String guestId;	//客队ID
    private String status;//比赛状态:0休息中 1未开赛2 完场3进行中
    private SnookerMatchScoreBean matchScore;//比分数据
    private SnookerOddsMatchBean matchOdds;//赔率
    private String gamesNumber;	//对站盘数
    private String totalGames;//总局数
    private String gameResults; //比赛结果
    private String specificScore;//具体比分
    private String round;//回合
    private String number;//场次
    private String sortValue;   //排序参数



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

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SnookerOddsMatchBean getMatchOdds() {
        return matchOdds;
    }

    public void setMatchOdds(SnookerOddsMatchBean matchOdds) {
        this.matchOdds = matchOdds;
    }

    public SnookerMatchScoreBean getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(SnookerMatchScoreBean matchScore) {
        this.matchScore = matchScore;
    }

    public String getGamesNumber() {
        return gamesNumber;
    }

    public void setGamesNumber(String gamesNumber) {
        this.gamesNumber = gamesNumber;
    }

    public String getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(String totalGames) {
        this.totalGames = totalGames;
    }

    public String getGameResults() {
        return gameResults;
    }

    public void setGameResults(String gameResults) {
        this.gameResults = gameResults;
    }

    public String getSpecificScore() {
        return specificScore;
    }

    public void setSpecificScore(String specificScore) {
        this.specificScore = specificScore;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getSortValue() {
        return sortValue;
    }

    public void setSortValue(String sortValue) {
        this.sortValue = sortValue;
    }
}
