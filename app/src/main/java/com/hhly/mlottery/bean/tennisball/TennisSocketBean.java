package com.hhly.mlottery.bean.tennisball;

/**
 * desc:网球即时比分实体类
 * Created by 107_tangrr on 2017/2/23 0023.
 */

public class TennisSocketBean {

    private int type;
    private DataObj dataObj;

    public class DataObj {
        private String matchId;
        private int matchStatus;
        private int set;
        private MatchScore matchScore;

        public class MatchScore {
            private int awaySetScore1;
            private int awaySetScore2;
            private int awaySetScore3;
            private int awaySetScore4;
            private int awaySetScore5;
            private int homeSetScore1;
            private int homeSetScore2;
            private int homeSetScore3;
            private int homeSetScore4;
            private int homeSetScore5;
            private int awayTotalScore;
            private int homeTotalScore;

            public int getAwaySetScore1() {
                return awaySetScore1;
            }

            public void setAwaySetScore1(int awaySetScore1) {
                this.awaySetScore1 = awaySetScore1;
            }

            public int getAwaySetScore2() {
                return awaySetScore2;
            }

            public void setAwaySetScore2(int awaySetScore2) {
                this.awaySetScore2 = awaySetScore2;
            }

            public int getAwaySetScore3() {
                return awaySetScore3;
            }

            public void setAwaySetScore3(int awaySetScore3) {
                this.awaySetScore3 = awaySetScore3;
            }

            public int getAwaySetScore4() {
                return awaySetScore4;
            }

            public void setAwaySetScore4(int awaySetScore4) {
                this.awaySetScore4 = awaySetScore4;
            }

            public int getAwaySetScore5() {
                return awaySetScore5;
            }

            public void setAwaySetScore5(int awaySetScore5) {
                this.awaySetScore5 = awaySetScore5;
            }

            public int getHomeSetScore1() {
                return homeSetScore1;
            }

            public void setHomeSetScore1(int homeSetScore1) {
                this.homeSetScore1 = homeSetScore1;
            }

            public int getHomeSetScore2() {
                return homeSetScore2;
            }

            public void setHomeSetScore2(int homeSetScore2) {
                this.homeSetScore2 = homeSetScore2;
            }

            public int getHomeSetScore3() {
                return homeSetScore3;
            }

            public void setHomeSetScore3(int homeSetScore3) {
                this.homeSetScore3 = homeSetScore3;
            }

            public int getHomeSetScore4() {
                return homeSetScore4;
            }

            public void setHomeSetScore4(int homeSetScore4) {
                this.homeSetScore4 = homeSetScore4;
            }

            public int getHomeSetScore5() {
                return homeSetScore5;
            }

            public void setHomeSetScore5(int homeSetScore5) {
                this.homeSetScore5 = homeSetScore5;
            }

            public int getAwayTotalScore() {
                return awayTotalScore;
            }

            public void setAwayTotalScore(int awayTotalScore) {
                this.awayTotalScore = awayTotalScore;
            }

            public int getHomeTotalScore() {
                return homeTotalScore;
            }

            public void setHomeTotalScore(int homeTotalScore) {
                this.homeTotalScore = homeTotalScore;
            }
        }

        public String getMatchId() {
            return matchId;
        }

        public void setMatchId(String matchId) {
            this.matchId = matchId;
        }

        public int getMatchStatus() {
            return matchStatus;
        }

        public void setMatchStatus(int matchStatus) {
            this.matchStatus = matchStatus;
        }

        public int getSet() {
            return set;
        }

        public void setSet(int set) {
            this.set = set;
        }

        public MatchScore getMatchScore() {
            return matchScore;
        }

        public void setMatchScore(MatchScore matchScore) {
            this.matchScore = matchScore;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DataObj getDataObj() {
        return dataObj;
    }

    public void setDataObj(DataObj dataObj) {
        this.dataObj = dataObj;
    }
}
