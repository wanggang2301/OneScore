package com.hhly.mlottery.bean.snookerbean;

import java.util.List;

/**
 * Created by yuely198 on 2017/2/22.
 */

public class SnookerSuccessBean {

    /**
     * result : 200
     * data : [{"leagueId":"319655","matchId":"832302","matchDate":"07-24 12:00:00","playerNameAcn":"宾汉姆","playerNameAen":null,"playerAid":"319643","score":"9 (17) 8","playerNameBcn":"马克·威廉姆斯","playerBid":"319647","status":"-1","detailedScore":"","detailedScoreList":[],"matchOrder":null,"ranking_a":null,"ranking_b":null,"playerNameBen":null,"season":"2011","firstTitle":"103","secondTitle":"238","playerWin":"319643"},{"leagueId":"319655","matchId":"832371","matchDate":"07-15 11:30:00","playerNameAcn":"彼得·埃布登","playerNameAen":null,"playerAid":"319649","score":"3 (17) 9","playerNameBcn":"巴里·霍金斯","playerBid":"319619","status":"-1","detailedScore":"","detailedScoreList":[],"matchOrder":null,"ranking_a":null,"ranking_b":null,"playerNameBen":null,"season":"2012","firstTitle":"103","secondTitle":"238","playerWin":"319619"},{"leagueId":"319655","matchId":"832640","matchDate":"07-14 12:00:00","playerNameAcn":"傅家俊","playerNameAen":null,"playerAid":"319628","score":"9 (17) 6","playerNameBcn":"尼尔·罗伯特森","playerBid":"319627","status":"-1","detailedScore":"","detailedScoreList":[],"matchOrder":null,"ranking_a":null,"ranking_b":null,"playerNameBen":null,"season":"2013","firstTitle":"103","secondTitle":"238","playerWin":"319628"},{"leagueId":"319655","matchId":"833053","matchDate":"07-06 12:00:00","playerNameAcn":"尼尔·罗伯特森","playerNameAen":"Neil Robertson","playerAid":"319627","score":"5 (17) 9","playerNameBcn":"贾德·特鲁姆普","playerBid":"319645","status":"-1","detailedScore":"87-48;23-77;73-4;0-69(50);45-90(71);74(52)-55;25-63(55);0-118(114);43-65;8-96;28-101(101);64-6;109(109)-8;40-65(51)","detailedScoreList":["87-48","23-77","73-4","0-69(50)","45-90(71)","74(52)-55","25-63(55)","0-118(114)","43-65","8-96","28-101(101)","64-6","109(109)-8","40-65(51)"],"matchOrder":"33","ranking_a":"3","ranking_b":"7","playerNameBen":"dd Trump","season":"2014","firstTitle":"103","secondTitle":"238","playerWin":"319645"},{"leagueId":"319655","matchId":"833522","matchDate":"07-05 12:00:00","playerNameAcn":"马丁·古尔德","playerNameAen":null,"playerAid":"319635","score":"8 (17) 9","playerNameBcn":"约翰·希金斯","playerBid":"319626","status":"-1","detailedScore":"","detailedScoreList":[],"matchOrder":null,"ranking_a":null,"ranking_b":null,"playerNameBen":null,"season":"2015","firstTitle":"103","secondTitle":"238","playerWin":"319626"},{"leagueId":"319655","matchId":"837617","matchDate":"07-05 12:00:00","playerNameAcn":"马丁·古尔德","playerNameAen":"Martin Gould","playerAid":"319635","score":"8 (17) 9","playerNameBcn":"R.希金斯","playerBid":"319766","status":"-1","detailedScore":"8-90(90);57-58;101(101)-13;101(86)-25;67-32;89(89)-5;0-126(112);12-66;25-74;13-82;138(138)-0;40-68(68);70(55)-45;100(58)-1;33-92(60);68-54;8-89(89)","detailedScoreList":["8-90(90)","57-58","101(101)-13","101(86)-25","67-32","89(89)-5","0-126(112)","12-66","25-74","13-82","138(138)-0","40-68(68)","70(55)-45","100(58)-1","33-92(60)","68-54","8-89(89)"],"matchOrder":"33","ranking_a":"20","ranking_b":"7","playerNameBen":"hn Higgins","season":"2015","firstTitle":"103","secondTitle":"238","playerWin":"319766"}]
     */

    private int result;
    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * leagueId : 319655
         * matchId : 832302
         * matchDate : 07-24 12:00:00
         * playerNameAcn : 宾汉姆
         * playerNameAen : null
         * playerAid : 319643
         * score : 9 (17) 8
         * playerNameBcn : 马克·威廉姆斯
         * playerBid : 319647
         * status : -1
         * detailedScore :
         * detailedScoreList : []
         * matchOrder : null
         * ranking_a : null
         * ranking_b : null
         * playerNameBen : null
         * season : 2011
         * firstTitle : 103
         * secondTitle : 238
         * playerWin : 319643
         */

        private String leagueId;
        private String matchId;
        private String matchDate;
        private String playerNameAcn;
        private Object playerNameAen;
        private String playerAid;
        private String score;
        private String playerNameBcn;
        private String playerBid;
        private String status;
        private String detailedScore;
        private Object matchOrder;
        private Object ranking_a;
        private Object ranking_b;
        private Object playerNameBen;
        private String season;
        private String firstTitle;
        private String secondTitle;
        private String playerWin;
        private List<?> detailedScoreList;

        public String getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(String leagueId) {
            this.leagueId = leagueId;
        }

        public String getMatchId() {
            return matchId;
        }

        public void setMatchId(String matchId) {
            this.matchId = matchId;
        }

        public String getMatchDate() {
            return matchDate;
        }

        public void setMatchDate(String matchDate) {
            this.matchDate = matchDate;
        }

        public String getPlayerNameAcn() {
            return playerNameAcn;
        }

        public void setPlayerNameAcn(String playerNameAcn) {
            this.playerNameAcn = playerNameAcn;
        }

        public Object getPlayerNameAen() {
            return playerNameAen;
        }

        public void setPlayerNameAen(Object playerNameAen) {
            this.playerNameAen = playerNameAen;
        }

        public String getPlayerAid() {
            return playerAid;
        }

        public void setPlayerAid(String playerAid) {
            this.playerAid = playerAid;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getPlayerNameBcn() {
            return playerNameBcn;
        }

        public void setPlayerNameBcn(String playerNameBcn) {
            this.playerNameBcn = playerNameBcn;
        }

        public String getPlayerBid() {
            return playerBid;
        }

        public void setPlayerBid(String playerBid) {
            this.playerBid = playerBid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDetailedScore() {
            return detailedScore;
        }

        public void setDetailedScore(String detailedScore) {
            this.detailedScore = detailedScore;
        }

        public Object getMatchOrder() {
            return matchOrder;
        }

        public void setMatchOrder(Object matchOrder) {
            this.matchOrder = matchOrder;
        }

        public Object getRanking_a() {
            return ranking_a;
        }

        public void setRanking_a(Object ranking_a) {
            this.ranking_a = ranking_a;
        }

        public Object getRanking_b() {
            return ranking_b;
        }

        public void setRanking_b(Object ranking_b) {
            this.ranking_b = ranking_b;
        }

        public Object getPlayerNameBen() {
            return playerNameBen;
        }

        public void setPlayerNameBen(Object playerNameBen) {
            this.playerNameBen = playerNameBen;
        }

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public String getFirstTitle() {
            return firstTitle;
        }

        public void setFirstTitle(String firstTitle) {
            this.firstTitle = firstTitle;
        }

        public String getSecondTitle() {
            return secondTitle;
        }

        public void setSecondTitle(String secondTitle) {
            this.secondTitle = secondTitle;
        }

        public String getPlayerWin() {
            return playerWin;
        }

        public void setPlayerWin(String playerWin) {
            this.playerWin = playerWin;
        }

        public List<?> getDetailedScoreList() {
            return detailedScoreList;
        }

        public void setDetailedScoreList(List<?> detailedScoreList) {
            this.detailedScoreList = detailedScoreList;
        }
    }
}
