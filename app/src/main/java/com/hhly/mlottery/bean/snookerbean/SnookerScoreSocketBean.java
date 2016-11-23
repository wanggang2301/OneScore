package com.hhly.mlottery.bean.snookerbean;

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

    public class SnookerScoreDataBean{

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

        private String leagueId;
        private String matchId;
        private String status;
        private String playerOnewin;
        private String playerTwowin;
        private String poScore;
        private String ptScore;
        private String poSingleShot;
        private String ptSingleShot;
        private String ball;
        private String poId;
        private String ptId;

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
