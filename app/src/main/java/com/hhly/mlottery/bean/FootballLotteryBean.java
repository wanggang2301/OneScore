package com.hhly.mlottery.bean;

import java.util.List;

/**
 * Created by yuely198 on 2017/5/8.
 * 足球竞彩实体 bean
 */

public class FootballLotteryBean {

    /**
     * bettingList : [{"leagueId":10,"leagueName":"Premier League","startTime":"22:00","status":null,"keepTime":null,"serNum":"001","week":4,"matchId":"848841844","homeName":"Amkar Perm","guestName":"FC Krasnodar","homeId":265139,"guestId":265936,"letNumber":"1","winOdds":"3.8","sameOdds":"2.95","loseOdds":"1.9","letWinOdds":"1.69","letSameOdds":"3.3","letLoseOdds":"4.26","guestScore":null,"homeScore":null},{"leagueId":26,"leagueName":"Allsvenskan","startTime":"01:00","status":null,"keepTime":null,"serNum":"002","week":4,"matchId":"848843600","homeName":"AIK","guestName":"IK Sirius","homeId":265219,"guestId":268724,"letNumber":"-1","winOdds":"1.63","sameOdds":"3.6","loseOdds":"4.2","letWinOdds":"2.92","letSameOdds":"3.4","letLoseOdds":"2.03","guestScore":null,"homeScore":null},{"leagueId":26,"leagueName":"Allsvenskan","startTime":"01:00","status":null,"keepTime":null,"serNum":"003","week":4,"matchId":"848843597","homeName":"Halmstads BK","guestName":"Djurgardens IF","homeId":265210,"guestId":267846,"letNumber":"1","winOdds":"4.15","sameOdds":"3.55","loseOdds":"1.65","letWinOdds":"1.98","letSameOdds":"3.37","letLoseOdds":"3.05","guestScore":null,"homeScore":null},{"leagueId":26,"leagueName":"Allsvenskan","startTime":"01:00","status":null,"keepTime":null,"serNum":"004","week":4,"matchId":"848843598","homeName":"IFK Norrk&#246;ping FK","guestName":"J&#246;nk&#246;pings S&#246;dra IF","homeId":265218,"guestId":264081,"letNumber":"-1","winOdds":"1.44","sameOdds":"3.95","loseOdds":"5.5","letWinOdds":"2.34","letSameOdds":"3.45","letLoseOdds":"2.43","guestScore":null,"homeScore":null},{"leagueId":26,"leagueName":"Allsvenskan","startTime":"01:00","status":null,"keepTime":null,"serNum":"005","week":4,"matchId":"848843603","homeName":"Kalmar FF","guestName":"Malm&#246; FF","homeId":265211,"guestId":265204,"letNumber":"1","winOdds":"5.75","sameOdds":"4.0","loseOdds":"1.42","letWinOdds":"2.37","letSameOdds":"3.15","letLoseOdds":"2.57","guestScore":null,"homeScore":null},{"leagueId":31,"leagueName":"Primera Division","startTime":"01:30","status":null,"keepTime":null,"serNum":"006","week":4,"matchId":"848842540","homeName":"Deportivo Alaves","guestName":"SD Eibar","homeId":263986,"guestId":263597,"letNumber":"-1","winOdds":"2.45","sameOdds":"3.05","loseOdds":"2.55","letWinOdds":"5.7","letSameOdds":"4.0","letLoseOdds":"1.42","guestScore":null,"homeScore":null},{"leagueId":31,"leagueName":"Primera Division","startTime":"02:30","status":null,"keepTime":null,"serNum":"007","week":4,"matchId":"848842538","homeName":"Sevilla FC","guestName":"RC Celta de Vigo","homeId":268295,"guestId":268271,"letNumber":"-1","winOdds":"1.17","sameOdds":"5.45","loseOdds":"11.0","letWinOdds":"1.66","letSameOdds":"3.8","letLoseOdds":"3.8","guestScore":null,"homeScore":null},{"leagueId":36,"leagueName":"Premier League","startTime":"03:00","status":null,"keepTime":null,"serNum":"009","week":4,"matchId":"848842638","homeName":"Manchester City FC","guestName":"Manchester United FC","homeId":267072,"guestId":263789,"letNumber":"-1","winOdds":"1.73","sameOdds":"3.48","loseOdds":"3.78","letWinOdds":"3.16","letSameOdds":"3.6","letLoseOdds":"1.87","guestScore":null,"homeScore":null},{"leagueId":31,"leagueName":"Primera Division","startTime":"03:30","status":null,"keepTime":null,"serNum":"010","week":4,"matchId":"848842541","homeName":"Athletic Club Bilbao","guestName":"Real Betis Balompie","homeId":265512,"guestId":268273,"letNumber":"-1","winOdds":"1.23","sameOdds":"4.95","loseOdds":"8.8","letWinOdds":"1.84","letSameOdds":"3.55","letLoseOdds":"3.29","guestScore":null,"homeScore":null},{"leagueId":89,"leagueName":"CONMEBOL Libertadores - Group Stage","startTime":"06:30","status":null,"keepTime":null,"serNum":"011","week":4,"matchId":"848842436","homeName":"Zulia FC","guestName":"CA Lanus","homeId":265552,"guestId":265171,"letNumber":"1","winOdds":"4.6","sameOdds":"3.4","loseOdds":"1.62","letWinOdds":"2.04","letSameOdds":"3.25","letLoseOdds":"3.02","guestScore":null,"homeScore":null},{"leagueId":89,"leagueName":"CONMEBOL Libertadores - Group Stage","startTime":"06:30","status":null,"keepTime":null,"serNum":"012","week":4,"matchId":"848842867","homeName":"Club Nacional de Football Montevideo","guestName":"Chapecoense AF","homeId":268414,"guestId":263889,"letNumber":"-1","winOdds":"1.6","sameOdds":"3.35","loseOdds":"4.85","letWinOdds":"3.0","letSameOdds":"3.3","letLoseOdds":"2.03","guestScore":null,"homeScore":null},{"leagueId":89,"leagueName":"CONMEBOL Libertadores - Group Stage","startTime":"08:00","status":null,"keepTime":null,"serNum":"013","week":4,"matchId":"848841199","homeName":"Club Sport Emelec","guestName":"CA River Plate BA","homeId":268241,"guestId":267898,"letNumber":"-1","winOdds":"2.29","sameOdds":"2.95","loseOdds":"2.83","letWinOdds":"5.4","letSameOdds":"3.8","letLoseOdds":"1.47","guestScore":null,"homeScore":null},{"leagueId":89,"leagueName":"CONMEBOL Libertadores - Group Stage","startTime":"08:45","status":null,"keepTime":null,"serNum":"014","week":4,"matchId":"848842542","homeName":"Sports Boys Warnes","guestName":"Godoy Cruz","homeId":265414,"guestId":265167,"letNumber":"1","winOdds":"3.06","sameOdds":"3.15","loseOdds":"2.06","letWinOdds":"1.59","letSameOdds":"3.7","letLoseOdds":"4.35","guestScore":null,"homeScore":null},{"leagueId":89,"leagueName":"CONMEBOL Libertadores - Group Stage","startTime":"08:45","status":null,"keepTime":null,"serNum":"015","week":4,"matchId":"848840131","homeName":"Gremio Foot-Ball Porto Alegrense","guestName":"Club Guarani Asuncion","homeId":264077,"guestId":267507,"letNumber":"-1","winOdds":"1.42","sameOdds":"3.8","loseOdds":"6.2","letWinOdds":"2.42","letSameOdds":"3.2","letLoseOdds":"2.48","guestScore":null,"homeScore":null}]
     * result : 200
     * currentDate : 2017-05-08
     * totalSize : 14
     */

    private int result;
    private String currentDate;
    private int totalSize;
    private List<BettingListBean> bettingList;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public List<BettingListBean> getBettingList() {
        return bettingList;
    }

    public void setBettingList(List<BettingListBean> bettingList) {
        this.bettingList = bettingList;
    }

    public static class BettingListBean {
        /**
         * leagueId : 10
         * leagueName : Premier League
         * startTime : 22:00
         * status : null
         * keepTime : null
         * serNum : 001
         * week : 4
         * matchId : 848841844
         * homeName : Amkar Perm
         * guestName : FC Krasnodar
         * homeId : 265139
         * guestId : 265936
         * letNumber : 1
         * winOdds : 3.8
         * sameOdds : 2.95
         * loseOdds : 1.9
         * letWinOdds : 1.69
         * letSameOdds : 3.3
         * letLoseOdds : 4.26
         * guestScore : null
         * homeScore : null
         */

        private int leagueId;
        private String leagueName;
        private String startTime;
        private Object status;
        private String keepTime;
        private String serNum;
        private int week;
        private String matchId;
        private String homeName;
        private String guestName;
        private int homeId;
        private int guestId;
        private String letNumber;
        private String winOdds;
        private String sameOdds;
        private String loseOdds;
        private String letWinOdds;
        private String letSameOdds;
        private String letLoseOdds;
        private Object guestScore;
        private Object homeScore;
        private int letsameoddsColorId=0;
        private int letloseoddsColorId=0;
        private int letwinoddsColorId=0;

        private int winoddsColorId=0;
        private int loseoddsColorId=0;
        private int sameoddsColorId=0;


        public int getLetloseoddsColorId() {
            return letloseoddsColorId;
        }

        public void setLetloseoddsColorId(int letloseoddsColorId) {
            this.letloseoddsColorId = letloseoddsColorId;
        }

        public int getLetwinoddsColorId() {
            return letwinoddsColorId;
        }

        public void setLetwinoddsColorId(int letwinoddsColorId) {
            this.letwinoddsColorId = letwinoddsColorId;
        }

        public int getWinoddsColorId() {
            return winoddsColorId;
        }

        public void setWinoddsColorId(int winoddsColorId) {
            this.winoddsColorId = winoddsColorId;
        }

        public int getLoseoddsColorId() {
            return loseoddsColorId;
        }

        public void setLoseoddsColorId(int loseoddsColorId) {
            this.loseoddsColorId = loseoddsColorId;
        }

        public int getSameoddsColorId() {
            return sameoddsColorId;
        }

        public void setSameoddsColorId(int sameoddsColorId) {
            this.sameoddsColorId = sameoddsColorId;
        }

        public int getLetsameoddsColorId() {

            return letsameoddsColorId;
        }

        public void setLetsameoddsColorId(int letsameoddsColorId) {
            this.letsameoddsColorId = letsameoddsColorId;
        }



        public int getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(int leagueId) {
            this.leagueId = leagueId;
        }

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public String getKeepTime() {
            return keepTime;
        }

        public void setKeepTime(String keepTime) {
            this.keepTime = keepTime;
        }

        public String getSerNum() {
            return serNum;
        }

        public void setSerNum(String serNum) {
            this.serNum = serNum;
        }

        public int getWeek() {
            return week;
        }

        public void setWeek(int week) {
            this.week = week;
        }

        public String getMatchId() {
            return matchId;
        }

        public void setMatchId(String matchId) {
            this.matchId = matchId;
        }

        public String getHomeName() {
            return homeName;
        }

        public void setHomeName(String homeName) {
            this.homeName = homeName;
        }

        public String getGuestName() {
            return guestName;
        }

        public void setGuestName(String guestName) {
            this.guestName = guestName;
        }

        public int getHomeId() {
            return homeId;
        }

        public void setHomeId(int homeId) {
            this.homeId = homeId;
        }

        public int getGuestId() {
            return guestId;
        }

        public void setGuestId(int guestId) {
            this.guestId = guestId;
        }

        public String getLetNumber() {
            return letNumber;
        }

        public void setLetNumber(String letNumber) {
            this.letNumber = letNumber;
        }

        public String getWinOdds() {
            return winOdds;
        }

        public void setWinOdds(String winOdds) {
            this.winOdds = winOdds;
        }

        public String getSameOdds() {
            return sameOdds;
        }

        public void setSameOdds(String sameOdds) {
            this.sameOdds = sameOdds;
        }

        public String getLoseOdds() {
            return loseOdds;
        }

        public void setLoseOdds(String loseOdds) {
            this.loseOdds = loseOdds;
        }

        public String getLetWinOdds() {
            return letWinOdds;
        }

        public void setLetWinOdds(String letWinOdds) {
            this.letWinOdds = letWinOdds;
        }

        public String getLetSameOdds() {
            return letSameOdds;
        }

        public void setLetSameOdds(String letSameOdds) {
            this.letSameOdds = letSameOdds;
        }

        public String getLetLoseOdds() {
            return letLoseOdds;
        }

        public void setLetLoseOdds(String letLoseOdds) {
            this.letLoseOdds = letLoseOdds;
        }

        public Object getGuestScore() {
            return guestScore;
        }

        public void setGuestScore(Object guestScore) {
            this.guestScore = guestScore;
        }

        public Object getHomeScore() {
            return homeScore;
        }

        public void setHomeScore(Object homeScore) {
            this.homeScore = homeScore;
        }
    }
}
