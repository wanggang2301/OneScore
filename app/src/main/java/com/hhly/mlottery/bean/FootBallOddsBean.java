package com.hhly.mlottery.bean;

/**
 * Created by yuely198 on 2017/5/17.
 */

public class FootBallOddsBean {


    /**
     * data : {"leagueId":34047,"letLoseOdds":"1.92","letNumber":"1","letSameOdds":"3.65","letWinOdds":"3.0","loseOdds":"1.28","matchId":"848842921","sameOdds":"4.75","winOdds":"7.25"}
     * thirdId : 848842921
     * type : 2
     */

    private DataBean data;
    private String thirdId;
    private int type;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class DataBean {
        /**
         * leagueId : 34047
         * letLoseOdds : 1.92
         * letNumber : 1
         * letSameOdds : 3.65
         * letWinOdds : 3.0
         * loseOdds : 1.28
         * matchId : 848842921
         * sameOdds : 4.75
         * winOdds : 7.25
         */

        private int leagueId;
        private String letLoseOdds;
        private String letNumber;
        private String letSameOdds;
        private String letWinOdds;
        private String loseOdds;
        private String matchId;
        private String sameOdds;
        private String winOdds;

        public int getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(int leagueId) {
            this.leagueId = leagueId;
        }

        public String getLetLoseOdds() {
            return letLoseOdds;
        }

        public void setLetLoseOdds(String letLoseOdds) {
            this.letLoseOdds = letLoseOdds;
        }

        public String getLetNumber() {
            return letNumber;
        }

        public void setLetNumber(String letNumber) {
            this.letNumber = letNumber;
        }

        public String getLetSameOdds() {
            return letSameOdds;
        }

        public void setLetSameOdds(String letSameOdds) {
            this.letSameOdds = letSameOdds;
        }

        public String getLetWinOdds() {
            return letWinOdds;
        }

        public void setLetWinOdds(String letWinOdds) {
            this.letWinOdds = letWinOdds;
        }

        public String getLoseOdds() {
            return loseOdds;
        }

        public void setLoseOdds(String loseOdds) {
            this.loseOdds = loseOdds;
        }

        public String getMatchId() {
            return matchId;
        }

        public void setMatchId(String matchId) {
            this.matchId = matchId;
        }

        public String getSameOdds() {
            return sameOdds;
        }

        public void setSameOdds(String sameOdds) {
            this.sameOdds = sameOdds;
        }

        public String getWinOdds() {
            return winOdds;
        }

        public void setWinOdds(String winOdds) {
            this.winOdds = winOdds;
        }
    }
}
