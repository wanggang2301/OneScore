package com.hhly.mlottery.bean.snookerbean.snookerDetail;

import com.tencent.open.utils.Util;

import java.util.List;

/**
 * 描    述：
 * 作    者：mady@13322.com
 * 时    间：2017/2/22
 */
public class SnookerAnalyzeBean {


    private String result;
    private PlayerRecentEntity playerRecent;

    private MatchInfoEntity matchInfo;


    private List<ProfessionDataEntity> professionData;

    private List<SnookerEntity> battleHistory;

    private SnookerStatisics battleHistoryStatistics;

    public SnookerStatisics getBattleHistoryStatistics() {
        return battleHistoryStatistics;
    }

    public void setBattleHistoryStatistics(SnookerStatisics battleHistoryStatistics) {
        this.battleHistoryStatistics = battleHistoryStatistics;
    }

    /**
     * 比赛list的实体bean
     */
    public static class SnookerEntity {
        private String date;
        private String matchName;
        private String homePlayer;
        private String guestPlayer;
        private int homeScore;
        private int guestScore;
        private String score;
        private int markPlayer;
        private int playerColor;
        private int result;
        private Object ctotScore;
        private Object tot;
        private Object casLetGoal;
        private Object let;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMatchName() {
            return matchName;
        }

        public void setMatchName(String matchName) {
            this.matchName = matchName;
        }

        public String getHomePlayer() {
            return homePlayer;
        }

        public void setHomePlayer(String homePlayer) {
            this.homePlayer = homePlayer;
        }

        public String getGuestPlayer() {
            return guestPlayer;
        }

        public void setGuestPlayer(String guestPlayer) {
            this.guestPlayer = guestPlayer;
        }

        public int getHomeScore() {
            return homeScore;
        }

        public void setHomeScore(int homeScore) {
            this.homeScore = homeScore;
        }

        public int getGuestScore() {
            return guestScore;
        }

        public void setGuestScore(int guestScore) {
            this.guestScore = guestScore;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public int getMarkPlayer() {
            return markPlayer;
        }

        public void setMarkPlayer(int markPlayer) {
            this.markPlayer = markPlayer;
        }

        public int getPlayerColor() {
            return playerColor;
        }

        public void setPlayerColor(int playerColor) {
            this.playerColor = playerColor;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public Object getCtotScore() {
            return ctotScore;
        }

        public void setCtotScore(Object ctotScore) {
            this.ctotScore = ctotScore;
        }

        public Object getTot() {
            return tot;
        }

        public void setTot(Object tot) {
            this.tot = tot;
        }

        public Object getCasLetGoal() {
            return casLetGoal;
        }

        public void setCasLetGoal(Object casLetGoal) {
            this.casLetGoal = casLetGoal;
        }

        public Object getLet() {
            return let;
        }

        public void setLet(Object let) {
            this.let = let;
        }
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public PlayerRecentEntity getPlayerRecent() {
        return playerRecent;
    }

    public void setPlayerRecent(PlayerRecentEntity playerRecent) {
        this.playerRecent = playerRecent;
    }

    public MatchInfoEntity getMatchInfo() {
        return matchInfo;
    }

    public void setMatchInfo(MatchInfoEntity matchInfo) {
        this.matchInfo = matchInfo;
    }

    public List<ProfessionDataEntity> getProfessionData() {
        return professionData;
    }

    public void setProfessionData(List<ProfessionDataEntity> professionData) {
        this.professionData = professionData;
    }

    public List<SnookerEntity> getBattleHistory() {
        return battleHistory;
    }

    public void setBattleHistory(List<SnookerEntity> battleHistory) {
        this.battleHistory = battleHistory;
    }

    /**
     * 队员最近比赛
     */
    public static class PlayerRecentEntity {


        private List<SnookerEntity> guest;

        private List<SnookerEntity> home;
        private SnookerStatisics homeStatisics;
        private SnookerStatisics guestStatisics;

        public SnookerStatisics getHomeStatisics() {
            return homeStatisics;
        }

        public void setHomeStatisics(SnookerStatisics homeStatisics) {
            this.homeStatisics = homeStatisics;
        }

        public SnookerStatisics getGuestStatisics() {
            return guestStatisics;
        }

        public void setGuestStatisics(SnookerStatisics guestStatisics) {
            this.guestStatisics = guestStatisics;
        }

        public List<SnookerEntity> getGuest() {
            return guest;
        }

        public void setGuest(List<SnookerEntity> guest) {
            this.guest = guest;
        }

        public List<SnookerEntity> getHome() {
            return home;
        }

        public void setHome(List<SnookerEntity> home) {
            this.home = home;
        }

    }

    /**
     * 队员最近比赛
     */
    public static class SnookerStatisics{
        private int vsCount;
        private int win;
        private int draw;
        private int lose;
        private String winPercent;

        public int getVsCount() {
            return vsCount;
        }

        public void setVsCount(int vsCount) {
            this.vsCount = vsCount;
        }

        public int getWin() {
            return win;
        }

        public void setWin(int win) {
            this.win = win;
        }

        public int getDraw() {
            return draw;
        }

        public void setDraw(int draw) {
            this.draw = draw;
        }

        public int getLose() {
            return lose;
        }

        public void setLose(int lose) {
            this.lose = lose;
        }

        public String getWinPercent() {
            return winPercent;
        }

        public void setWinPercent(String winPercent) {
            this.winPercent = winPercent;
        }
    }



    public static class MatchInfoEntity {
        private String homePlayer;
        private String guestPlayer;
        private String homePlayId;
        private String guestPlayId;
        private String homeAvatar;
        private String guestAvater;
        private String matchName;
        private String matchId;
        private String status;
        private String round;
        private String number;
        private String matchDate;
        private String matchTime;
        private String homeScore;
        private String homeBoardScore;
        private String totalBoard;
        private String guestBoardScore;
        private String guestScore;

        private List<ScoreList> specificScoreList;

        public List<ScoreList> getSpecificScoreList() {
            return specificScoreList;
        }

        public void setSpecificScoreList(List<ScoreList> specificScoreList) {
            this.specificScoreList = specificScoreList;
        }

        private static class ScoreList{
            private String homeScore;
            private int index;
            private String guestScore;

            public String getHomeScore() {
                return homeScore;
            }

            public void setHomeScore(String homeScore) {
                this.homeScore = homeScore;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public String getGuestScore() {
                return guestScore;
            }

            public void setGuestScore(String guestScore) {
                this.guestScore = guestScore;
            }
        }

        public String getHomePlayer() {
            return homePlayer;
        }

        public void setHomePlayer(String homePlayer) {
            this.homePlayer = homePlayer;
        }

        public String getGuestPlayer() {
            return guestPlayer;
        }

        public void setGuestPlayer(String guestPlayer) {
            this.guestPlayer = guestPlayer;
        }

        public String getHomePlayId() {
            return homePlayId;
        }

        public void setHomePlayId(String homePlayId) {
            this.homePlayId = homePlayId;
        }

        public String getGuestPlayId() {
            return guestPlayId;
        }

        public void setGuestPlayId(String guestPlayId) {
            this.guestPlayId = guestPlayId;
        }

        public String getHomeAvatar() {
            return homeAvatar;
        }

        public void setHomeAvatar(String homeAvatar) {
            this.homeAvatar = homeAvatar;
        }

        public String getGuestAvater() {
            return guestAvater;
        }

        public void setGuestAvater(String guestAvater) {
            this.guestAvater = guestAvater;
        }

        public String getMatchName() {
            return matchName;
        }

        public void setMatchName(String matchName) {
            this.matchName = matchName;
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

        public String getRound() {
            return round;
        }

        public void setRound(String round) {
            this.round = round;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getMatchDate() {
            return matchDate;
        }

        public void setMatchDate(String matchDate) {
            this.matchDate = matchDate;
        }

        public String getMatchTime() {
            return matchTime;
        }

        public void setMatchTime(String matchTime) {
            this.matchTime = matchTime;
        }

        public String getHomeScore() {
            return homeScore;
        }

        public void setHomeScore(String homeScore) {
            this.homeScore = homeScore;
        }

        public String getHomeBoardScore() {
            return homeBoardScore;
        }

        public void setHomeBoardScore(String homeBoardScore) {
            this.homeBoardScore = homeBoardScore;
        }

        public String getTotalBoard() {
            return totalBoard;
        }

        public void setTotalBoard(String totalBoard) {
            this.totalBoard = totalBoard;
        }

        public String getGuestBoardScore() {
            return guestBoardScore;
        }

        public void setGuestBoardScore(String guestBoardScore) {
            this.guestBoardScore = guestBoardScore;
        }

        public String getGuestScore() {
            return guestScore;
        }

        public void setGuestScore(String guestScore) {
            this.guestScore = guestScore;
        }
    }

    public static class ProfessionDataEntity {
        private String homeData;
        private String dataType;
        private String guestData;

        public String getHomeData() {
            return homeData;
        }

        public void setHomeData(String homeData) {
            this.homeData = homeData;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public String getGuestData() {
            return guestData;
        }

        public void setGuestData(String guestData) {
            this.guestData = guestData;
        }
    }

}
