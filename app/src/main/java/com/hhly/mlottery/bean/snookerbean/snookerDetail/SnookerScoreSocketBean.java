package com.hhly.mlottery.bean.snookerbean.snookerDetail;

import com.hhly.mlottery.config.StaticValues;

/**
 * Created by yixq on 2016/11/23.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerScoreSocketBean {

//    data	Object
//    thirdId	570326
//    type	300
    private SnookerScoreDataBean data;
    private String thirdId;
    private int type;

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

    public SnookerScoreDataBean getData() {
        return data;
    }

    public void setData(SnookerScoreDataBean data) {
        this.data = data;
    }

    public static class SnookerScoreDataBean{

//                "ball": "2",
//                "leagueId": "1253587",
//                "matchId": "570326",
//                "playerOnewin": "0",
//                "playerTwowin": "0",
//                "poId": "86",
//                "poScore": "0",
//                "poSingleShot": "",
//                "ptId": "928",
//                "ptScore": "0",
//                "ptSingleShot": "0",
//                "status": "1"
//        totalGames	5

        private String leagueId;//联赛id
        private String matchId;//赛事id
        private String status;//比赛状态:0休息中 1未开始2 完场3进行中
        private String playerOnewin;//选手1胜场
        private String playerTwowin;//选手2胜场
        private String poScore;//选手1小分
        private String ptScore;//选手2小分
        private String poSingleShot;//选手1单杆得分
        private String ptSingleShot;//选手2单杆得分
        private String ball;//球权0没有 1选手一持球 2选手二持球
        private String poId;//选手1Id
        private String ptId;//选手2Id
        private String totalGames;//总局数

        public String getTotalGames() {
            return totalGames;
        }

        public void setTotalGames(String totalGames) {
            this.totalGames = totalGames;
        }

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPlayerOnewin() {
            return playerOnewin;
        }

        public void setPlayerOnewin(String playerOnewin) {
            this.playerOnewin = playerOnewin;
        }

        public String getPlayerTwowin() {
            return playerTwowin;
        }

        public void setPlayerTwowin(String playerTwowin) {
            this.playerTwowin = playerTwowin;
        }

        public String getPoScore() {
            return poScore;
        }

        public void setPoScore(String poScore) {
            this.poScore = poScore;
        }

        public String getPtScore() {
            return ptScore;
        }

        public void setPtScore(String ptScore) {
            this.ptScore = ptScore;
        }

        public String getPoSingleShot() {
            return poSingleShot;
        }

        public void setPoSingleShot(String poSingleShot) {
            this.poSingleShot = poSingleShot;
        }

        public String getPtSingleShot() {
            return ptSingleShot;
        }

        public void setPtSingleShot(String ptSingleShot) {
            this.ptSingleShot = ptSingleShot;
        }

        public String getBall() {
            return ball;
        }

        public void setBall(String ball) {
            this.ball = ball;
        }

        public String getPoId() {
            return poId;
        }

        public void setPoId(String poId) {
            this.poId = poId;
        }

        public String getPtId() {
            return ptId;
        }

        public void setPtId(String ptId) {
            this.ptId = ptId;
        }
    }
}
