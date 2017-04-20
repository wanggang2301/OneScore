package com.hhly.mlottery.bean.tennisball;

/**
 * desc:
 * Created by 107_tangrr on 2017/2/21 0021.
 */

public class MatchDataBean {

    private String matchId;
    private String leagueId;
    private String leagueName;
    private String roundName;
    private String time;
    private String date;
    private String homePlayerId;
    private String homePlayerName;
    private String homePlayerId2;
    private String homePlayerName2;
    private String awayPlayerId;
    private String awayPlayerName;
    private String awayPlayerId2;
    private String awayPlayerName2;
    private int matchStatus;
    private int server;
    private int set;
    private MatchScoreBean matchScore;
    private MatchOddsBean matchOdds;
    private boolean isFocus;
    private String oddsType;

    public static class MatchOddsBean{
        private TennisOddsInfoBean asiaSize;
        private TennisOddsInfoBean euro;
        private TennisOddsInfoBean asiaLet;

        public TennisOddsInfoBean getAsiaSize() {
            return asiaSize;
        }

        public void setAsiaSize(TennisOddsInfoBean asiaSize) {
            this.asiaSize = asiaSize;
        }

        public TennisOddsInfoBean getEuro() {
            return euro;
        }

        public void setEuro(TennisOddsInfoBean euro) {
            this.euro = euro;
        }

        public TennisOddsInfoBean getAsiaLet() {
            return asiaLet;
        }

        public void setAsiaLet(TennisOddsInfoBean asiaLet) {
            this.asiaLet = asiaLet;
        }
    }

    public static class MatchScoreBean {

        private int homeSetScore1;
        private int homeSetScore2;
        private int homeSetScore3;
        private int homeSetScore4;
        private int homeSetScore5;
        private int awaySetScore1;
        private int awaySetScore2;
        private int awaySetScore3;
        private int awaySetScore4;
        private int awaySetScore5;
        private int homeDeciderScore1;
        private int homeDeciderScore2;
        private int homeDeciderScore3;
        private int homeDeciderScore4;
        private int homeDeciderScore5;
        private int awayDeciderScore1;
        private int awayDeciderScore2;
        private int awayDeciderScore3;
        private int awayDeciderScore4;
        private int awayDeciderScore5;
        private int homeTotalScore;
        private int awayTotalScore;
        private String homeCurrentScore;
        private String awayCurrentScore;

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

        public int getHomeDeciderScore1() {
            return homeDeciderScore1;
        }

        public void setHomeDeciderScore1(int homeDeciderScore1) {
            this.homeDeciderScore1 = homeDeciderScore1;
        }

        public int getHomeDeciderScore2() {
            return homeDeciderScore2;
        }

        public void setHomeDeciderScore2(int homeDeciderScore2) {
            this.homeDeciderScore2 = homeDeciderScore2;
        }

        public int getHomeDeciderScore3() {
            return homeDeciderScore3;
        }

        public void setHomeDeciderScore3(int homeDeciderScore3) {
            this.homeDeciderScore3 = homeDeciderScore3;
        }

        public int getHomeDeciderScore4() {
            return homeDeciderScore4;
        }

        public void setHomeDeciderScore4(int homeDeciderScore4) {
            this.homeDeciderScore4 = homeDeciderScore4;
        }

        public int getHomeDeciderScore5() {
            return homeDeciderScore5;
        }

        public void setHomeDeciderScore5(int homeDeciderScore5) {
            this.homeDeciderScore5 = homeDeciderScore5;
        }

        public int getAwayDeciderScore1() {
            return awayDeciderScore1;
        }

        public void setAwayDeciderScore1(int awayDeciderScore1) {
            this.awayDeciderScore1 = awayDeciderScore1;
        }

        public int getAwayDeciderScore2() {
            return awayDeciderScore2;
        }

        public void setAwayDeciderScore2(int awayDeciderScore2) {
            this.awayDeciderScore2 = awayDeciderScore2;
        }

        public int getAwayDeciderScore3() {
            return awayDeciderScore3;
        }

        public void setAwayDeciderScore3(int awayDeciderScore3) {
            this.awayDeciderScore3 = awayDeciderScore3;
        }

        public int getAwayDeciderScore4() {
            return awayDeciderScore4;
        }

        public void setAwayDeciderScore4(int awayDeciderScore4) {
            this.awayDeciderScore4 = awayDeciderScore4;
        }

        public int getAwayDeciderScore5() {
            return awayDeciderScore5;
        }

        public void setAwayDeciderScore5(int awayDeciderScore5) {
            this.awayDeciderScore5 = awayDeciderScore5;
        }

        public int getHomeTotalScore() {
            return homeTotalScore;
        }

        public void setHomeTotalScore(int homeTotalScore) {
            this.homeTotalScore = homeTotalScore;
        }

        public int getAwayTotalScore() {
            return awayTotalScore;
        }

        public void setAwayTotalScore(int awayTotalScore) {
            this.awayTotalScore = awayTotalScore;
        }

        public Object getHomeCurrentScore() {
            return homeCurrentScore;
        }

        public void setHomeCurrentScore(String homeCurrentScore) {
            this.homeCurrentScore = homeCurrentScore;
        }

        public Object getAwayCurrentScore() {
            return awayCurrentScore;
        }

        public void setAwayCurrentScore(String awayCurrentScore) {
            this.awayCurrentScore = awayCurrentScore;
        }
    }

    public MatchOddsBean getMatchOdds() {
        return matchOdds;
    }

    public void setMatchOdds(MatchOddsBean matchOdds) {
        this.matchOdds = matchOdds;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
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

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHomePlayerId() {
        return homePlayerId;
    }

    public void setHomePlayerId(String homePlayerId) {
        this.homePlayerId = homePlayerId;
    }

    public String getHomePlayerName() {
        return homePlayerName;
    }

    public void setHomePlayerName(String homePlayerName) {
        this.homePlayerName = homePlayerName;
    }

    public String getHomePlayerId2() {
        return homePlayerId2;
    }

    public void setHomePlayerId2(String homePlayerId2) {
        this.homePlayerId2 = homePlayerId2;
    }

    public String getHomePlayerName2() {
        return homePlayerName2;
    }

    public void setHomePlayerName2(String homePlayerName2) {
        this.homePlayerName2 = homePlayerName2;
    }

    public String getAwayPlayerId() {
        return awayPlayerId;
    }

    public void setAwayPlayerId(String awayPlayerId) {
        this.awayPlayerId = awayPlayerId;
    }

    public String getAwayPlayerName() {
        return awayPlayerName;
    }

    public void setAwayPlayerName(String awayPlayerName) {
        this.awayPlayerName = awayPlayerName;
    }

    public String getAwayPlayerId2() {
        return awayPlayerId2;
    }

    public void setAwayPlayerId2(String awayPlayerId2) {
        this.awayPlayerId2 = awayPlayerId2;
    }

    public String getAwayPlayerName2() {
        return awayPlayerName2;
    }

    public void setAwayPlayerName2(String awayPlayerName2) {
        this.awayPlayerName2 = awayPlayerName2;
    }

    public int getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(int matchStatus) {
        this.matchStatus = matchStatus;
    }

    public int getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public MatchScoreBean getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(MatchScoreBean matchScore) {
        this.matchScore = matchScore;
    }

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }

    public String getOddsType() {
        return oddsType;
    }

    public void setOddsType(String oddsType) {
        this.oddsType = oddsType;
    }
}
