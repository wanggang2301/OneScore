package com.hhly.mlottery.bean.footballDetails;

import java.util.List;

/**
 * Created by madongyun 155 on 2016/6/15.
 */
public class NewAnalyzeBean {

    private String result;
    private String leagueId;
    private int fullScoreRank;

    private AttackDefenseEntity attackDefense;

    private LineUpEntity lineUp;
    private SizeTrendEntity sizeTrend;
    private ScoreRankEntity scoreRank;
    private AsiaTrendEntity asiaTrend;
    private BothRecordEntity bothRecord;
    private String recommend;
    private Integer leagueType;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Object getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public int getFullScoreRank() {
        return fullScoreRank;
    }

    public void setFullScoreRank(int fullScoreRank) {
        this.fullScoreRank = fullScoreRank;
    }

    public AttackDefenseEntity getAttackDefense() {
        return attackDefense;
    }

    public void setAttackDefense(AttackDefenseEntity attackDefense) {
        this.attackDefense = attackDefense;
    }

    public LineUpEntity getLineUp() {
        return lineUp;
    }

    public void setLineUp(LineUpEntity lineUp) {
        this.lineUp = lineUp;
    }

    public SizeTrendEntity getSizeTrend() {
        return sizeTrend;
    }

    public void setSizeTrend(SizeTrendEntity sizeTrend) {
        this.sizeTrend = sizeTrend;
    }

    public ScoreRankEntity getScoreRank() {
        return scoreRank;
    }

    public void setScoreRank(ScoreRankEntity scoreRank) {
        this.scoreRank = scoreRank;
    }

    public AsiaTrendEntity getAsiaTrend() {
        return asiaTrend;
    }

    public void setAsiaTrend(AsiaTrendEntity asiaTrend) {
        this.asiaTrend = asiaTrend;
    }

    public BothRecordEntity getBothRecord() {
        return bothRecord;
    }

    public void setBothRecord(BothRecordEntity bothRecord) {
        this.bothRecord = bothRecord;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public Integer getLeagueType() {
        return leagueType;
    }

    public void setLeagueType(Integer leagueType) {
        this.leagueType = leagueType;
    }

    public static class AttackDefenseEntity {
        private String guestFieldGoal;
        private String guestFieldLose;
        private String sizeHandicap;
        private String homeFieldGoal;
        private String homeFieldLose;

        public String getGuestFieldGoal() {
            return guestFieldGoal;
        }

        public void setGuestFieldGoal(String guestFieldGoal) {
            this.guestFieldGoal = guestFieldGoal;
        }

        public String getGuestFieldLose() {
            return guestFieldLose;
        }

        public void setGuestFieldLose(String guestFieldLose) {
            this.guestFieldLose = guestFieldLose;
        }

        public String getSizeHandicap() {
            return sizeHandicap;
        }

        public void setSizeHandicap(String sizeHandicap) {
            this.sizeHandicap = sizeHandicap;
        }

        public String getHomeFieldGoal() {
            return homeFieldGoal;
        }

        public void setHomeFieldGoal(String homeFieldGoal) {
            this.homeFieldGoal = homeFieldGoal;
        }

        public String getHomeFieldLose() {
            return homeFieldLose;
        }

        public void setHomeFieldLose(String homeFieldLose) {
            this.homeFieldLose = homeFieldLose;
        }
    }

    public static class LineUpEntity {
        private String result;
        private List<PlayerInfo> homeLineUp;
        private List<PlayerInfo> guestLineUp;

        public void setHomeLineUp(List<PlayerInfo> homeLineUp) {
            this.homeLineUp = homeLineUp;
        }

        public void setGuestLineUp(List<PlayerInfo> guestLineUp) {
            this.guestLineUp = guestLineUp;
        }

        public List<PlayerInfo> getHomeLineUp() {
            return homeLineUp;
        }

        public List<PlayerInfo> getGuestLineUp() {
            return guestLineUp;
        }
        public static class PlayerInfo{
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public PlayerInfo(String name) {
                this.name = name;
            }
            public PlayerInfo(){}
        }
    }

    /**
     * 大小球走势
     */
    public static class SizeTrendEntity {

        private BattleHistoryEntity battleHistory;
        private HomeRecentEntity homeRecent;
        private GuestRecentEntity guestRecent;

        public BattleHistoryEntity getBattleHistory() {
            return battleHistory;
        }

        public void setBattleHistory(BattleHistoryEntity battleHistory) {
            this.battleHistory = battleHistory;
        }

        public HomeRecentEntity getHomeRecent() {
            return homeRecent;
        }

        public void setHomeRecent(HomeRecentEntity homeRecent) {
            this.homeRecent = homeRecent;
        }

        public GuestRecentEntity getGuestRecent() {
            return guestRecent;
        }

        public void setGuestRecent(GuestRecentEntity guestRecent) {
            this.guestRecent = guestRecent;
        }

        public static class HomeRecentEntity{
            private List<TrendListEntity> trendList;
            private Statistics statistics;

            public List<TrendListEntity> getTrendList() {
                return trendList;
            }

            public void setTrendList(List<TrendListEntity> trendList) {
                this.trendList = trendList;
            }

            public Statistics getStatistics() {
                return statistics;
            }

            public void setStatistics(Statistics statistics) {
                this.statistics = statistics;
            }
        }

        public static class GuestRecentEntity{
            private List<TrendListEntity> trendList;
            private Statistics statistics;

            public List<TrendListEntity> getTrendList() {
                return trendList;
            }

            public void setTrendList(List<TrendListEntity> trendList) {
                this.trendList = trendList;
            }

            public Statistics getStatistics() {
                return statistics;
            }

            public void setStatistics(Statistics statistics) {
                this.statistics = statistics;
            }
        }
        public static class BattleHistoryEntity {

            private Statistics statistics;


            private List<TotListEntity> totList;


            private List<PointListEntity> pointList;

            public Statistics getStatistics() {
                return statistics;
            }

            public void setStatistics(Statistics statistics) {
                this.statistics = statistics;
            }

            public List<TotListEntity> getTotList() {
                return totList;
            }

            public void setTotList(List<TotListEntity> totList) {
                this.totList = totList;
            }

            public List<PointListEntity> getPointList() {
                return pointList;
            }

            public void setPointList(List<PointListEntity> pointList) {
                this.pointList = pointList;
            }

            public static class TotListEntity {
                private int tot;
                private boolean homeGround;

                public int getTot() {
                    return tot;
                }

                public void setTot(int tot) {
                    this.tot = tot;
                }

                public boolean isHomeGround() {
                    return homeGround;
                }

                public void setHomeGround(boolean homeGround) {
                    this.homeGround = homeGround;
                }
            }

            public static class PointListEntity extends PointList {
                private String point1;
                private String point2;
                private String point3;
                private String point4;
                private String point5;
                private String point6;

                public String getPoint3() {
                    return point3;
                }

                public void setPoint3(String point3) {
                    this.point3 = point3;
                }

                public String getPoint4() {
                    return point4;
                }

                public void setPoint4(String point4) {
                    this.point4 = point4;
                }

                public String getPoint5() {
                    return point5;
                }

                public void setPoint5(String point5) {
                    this.point5 = point5;
                }

                public String getPoint6() {
                    return point6;
                }

                public void setPoint6(String point6) {
                    this.point6 = point6;
                }

                public String getPoint1() {
                    return point1;
                }

                public void setPoint1(String point1) {
                    this.point1 = point1;
                }

                public String getPoint2() {
                    return point2;
                }

                public void setPoint2(String point2) {
                    this.point2 = point2;
                }
            }
        }

        public static class Statistics{
            private int draw;
            private String bigPercent;
            private String smallPercent;
            private int vsCount;
            private int big;
            private String drawPercent;
            private int small;

            public int getDraw() {
                return draw;
            }

            public void setDraw(int draw) {
                this.draw = draw;
            }

            public String getBigPercent() {
                return bigPercent;
            }

            public void setBigPercent(String bigPercent) {
                this.bigPercent = bigPercent;
            }

            public String getSmallPercent() {
                return smallPercent;
            }

            public void setSmallPercent(String smallPercent) {
                this.smallPercent = smallPercent;
            }

            public int getVsCount() {
                return vsCount;
            }

            public void setVsCount(int vsCount) {
                this.vsCount = vsCount;
            }

            public int getBig() {
                return big;
            }

            public void setBig(int big) {
                this.big = big;
            }

            public String getDrawPercent() {
                return drawPercent;
            }

            public void setDrawPercent(String drawPercent) {
                this.drawPercent = drawPercent;
            }

            public int getSmall() {
                return small;
            }

            public void setSmall(int small) {
                this.small = small;
            }
        }
        public static class TrendListEntity {
            private int tot;
            private boolean homeGround;

            public int getTot() {
                return tot;
            }

            public void setTot(int tot) {
                this.tot = tot;
            }

            public boolean isHomeGround() {
                return homeGround;
            }

            public void setHomeGround(boolean homeGround) {
                this.homeGround = homeGround;
            }
        }

    }

    public static class ScoreRankEntity {
        /**
         * rank : 16
         * team : 百万富翁
         * vsCount : 2
         * win : 1
         * draw : 0
         * lose : 1
         * integral : 3
         * goalDiff : 0
         * goal : 1
         * miss : 1
         */

        private HomeEntity home;
        /**
         * rank : 19
         * team : 阿利安
         * vsCount : 2
         * win : 0
         * draw : 2
         * lose : 0
         * integral : 2
         * goalDiff : 0
         * goal : 4
         * miss : 4
         */

        private GuestEntity guest;

        public HomeEntity getHome() {
            return home;
        }

        public void setHome(HomeEntity home) {
            this.home = home;
        }

        public GuestEntity getGuest() {
            return guest;
        }

        public void setGuest(GuestEntity guest) {
            this.guest = guest;
        }

        public static class HomeEntity {
            private String rank;
            private String team;
            private String vsCount;
            private String win;
            private String draw;
            private String lose;
            private String integral;
            private String goalDiff;
            private String goal;
            private String miss;

            public String getRank() {
                return rank;
            }

            public void setRank(String rank) {
                this.rank = rank;
            }

            public String getTeam() {
                return team;
            }

            public void setTeam(String team) {
                this.team = team;
            }

            public String getVsCount() {
                return vsCount;
            }

            public void setVsCount(String vsCount) {
                this.vsCount = vsCount;
            }

            public String getWin() {
                return win;
            }

            public void setWin(String win) {
                this.win = win;
            }

            public String getDraw() {
                return draw;
            }

            public void setDraw(String draw) {
                this.draw = draw;
            }

            public String getLose() {
                return lose;
            }

            public void setLose(String lose) {
                this.lose = lose;
            }

            public String getIntegral() {
                return integral;
            }

            public void setIntegral(String integral) {
                this.integral = integral;
            }

            public String getGoalDiff() {
                return goalDiff;
            }

            public void setGoalDiff(String goalDiff) {
                this.goalDiff = goalDiff;
            }

            public String getGoal() {
                return goal;
            }

            public void setGoal(String goal) {
                this.goal = goal;
            }

            public String getMiss() {
                return miss;
            }

            public void setMiss(String miss) {
                this.miss = miss;
            }
        }

        public static class GuestEntity {
            private String rank;
            private String team;
            private String vsCount;
            private String win;
            private String draw;
            private String lose;
            private String integral;
            private String goalDiff;
            private String goal;
            private String miss;

            public String getRank() {
                return rank;
            }

            public void setRank(String rank) {
                this.rank = rank;
            }

            public String getTeam() {
                return team;
            }

            public void setTeam(String team) {
                this.team = team;
            }

            public String getVsCount() {
                return vsCount;
            }

            public void setVsCount(String vsCount) {
                this.vsCount = vsCount;
            }

            public String getWin() {
                return win;
            }

            public void setWin(String win) {
                this.win = win;
            }

            public String getDraw() {
                return draw;
            }

            public void setDraw(String draw) {
                this.draw = draw;
            }

            public String getLose() {
                return lose;
            }

            public void setLose(String lose) {
                this.lose = lose;
            }

            public String getIntegral() {
                return integral;
            }

            public void setIntegral(String integral) {
                this.integral = integral;
            }

            public String getGoalDiff() {
                return goalDiff;
            }

            public void setGoalDiff(String goalDiff) {
                this.goalDiff = goalDiff;
            }

            public String getGoal() {
                return goal;
            }

            public void setGoal(String goal) {
                this.goal = goal;
            }

            public String getMiss() {
                return miss;
            }

            public void setMiss(String miss) {
                this.miss = miss;
            }
        }
    }

    public static class AsiaTrendEntity {

        private BattleHistoryEntity battleHistory;
        /**
         * let : 1
         * homeGround : true
         */

        private HomeRecentEntity homeRecent;
        /**
         * let : 1
         * homeGround : true
         */

        private GuestRecentEntity guestRecent;

        public BattleHistoryEntity getBattleHistory() {
            return battleHistory;
        }

        public void setBattleHistory(BattleHistoryEntity battleHistory) {
            this.battleHistory = battleHistory;
        }

        public HomeRecentEntity getHomeRecent() {
            return homeRecent;
        }

        public void setHomeRecent(HomeRecentEntity homeRecent) {
            this.homeRecent = homeRecent;
        }

        public GuestRecentEntity getGuestRecent() {
            return guestRecent;
        }

        public void setGuestRecent(GuestRecentEntity guestRecent) {
            this.guestRecent = guestRecent;
        }

        public static class HomeRecentEntity{
            private List<TrendListEntity> trendList;
            private Statistics statistics;

            public List<TrendListEntity> getTrendList() {
                return trendList;
            }

            public void setTrendList(List<TrendListEntity> trendList) {
                this.trendList = trendList;
            }

            public Statistics getStatistics() {
                return statistics;
            }

            public void setStatistics(Statistics statistics) {
                this.statistics = statistics;
            }
        }

        public static class GuestRecentEntity{
            private List<TrendListEntity> trendList;
            private Statistics statistics;

            public List<TrendListEntity> getTrendList() {
                return trendList;
            }

            public void setTrendList(List<TrendListEntity> trendList) {
                this.trendList = trendList;
            }

            public Statistics getStatistics() {
                return statistics;
            }

            public void setStatistics(Statistics statistics) {
                this.statistics = statistics;
            }
        }

        public static class BattleHistoryEntity {
            private Statistics statistics;

            private List<PointListEntity> pointList;


            private List<LetListEntity> letList;

            public Statistics getStatistics() {
                return statistics;
            }

            public void setStatistics(Statistics statistics) {
                this.statistics = statistics;
            }

            public List<PointListEntity> getPointList() {
                return pointList;
            }

            public void setPointList(List<PointListEntity> pointList) {
                this.pointList = pointList;
            }

            public List<LetListEntity> getLetList() {
                return letList;
            }

            public void setLetList(List<LetListEntity> letList) {
                this.letList = letList;
            }

            public static class PointListEntity extends PointList{
                private String point1;
                private String point2;
                private String point3;
                private String point4;
                private String point5;
                private String point6;

                public String getPoint3() {
                    return point3;
                }

                public void setPoint3(String point3) {
                    this.point3 = point3;
                }

                public String getPoint4() {
                    return point4;
                }

                public void setPoint4(String point4) {
                    this.point4 = point4;
                }

                public String getPoint5() {
                    return point5;
                }

                public void setPoint5(String point5) {
                    this.point5 = point5;
                }

                public String getPoint6() {
                    return point6;
                }

                public void setPoint6(String point6) {
                    this.point6 = point6;
                }

                public String getPoint1() {
                    return point1;
                }

                public void setPoint1(String point1) {
                    this.point1 = point1;
                }

                public String getPoint2() {
                    return point2;
                }

                public void setPoint2(String point2) {
                    this.point2 = point2;
                }
            }

            public static class LetListEntity {
                private int let;
                private boolean homeGround;

                public int getLet() {
                    return let;
                }

                public void setLet(int let) {
                    this.let = let;
                }

                public boolean isHomeGround() {
                    return homeGround;
                }

                public void setHomeGround(boolean homeGround) {
                    this.homeGround = homeGround;
                }
            }
        }

        public static class Statistics{
            private int draw;
            private int lose;
            private String winPercent;
            private int vsCount;
            private  int win;
            private String losePercent;
            private String drawPercent;

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

            public String getLosePercent() {
                return losePercent;
            }

            public void setLosePercent(String losePercent) {
                this.losePercent = losePercent;
            }

            public String getDrawPercent() {
                return drawPercent;
            }

            public void setDrawPercent(String drawPercent) {
                this.drawPercent = drawPercent;
            }
        }

        public static class TrendListEntity {
            private int let;
            private boolean homeGround;

            public int getLet() {
                return let;
            }

            public void setLet(int let) {
                this.let = let;
            }

            public boolean isHomeGround() {
                return homeGround;
            }

            public void setHomeGround(boolean homeGround) {
                this.homeGround = homeGround;
            }
        }

    }

    public static class BothRecordEntity {

        private HomeEntity home;

        private GuestEntity guest;

        public HomeEntity getHome() {
            return home;
        }

        public void setHome(HomeEntity home) {
            this.home = home;
        }

        public GuestEntity getGuest() {
            return guest;
        }

        public void setGuest(GuestEntity guest) {
            this.guest = guest;
        }

        public static class HomeEntity {
            private int historyWin;

            private FutureMatchEntity futureMatch;
            private List<Integer> recentRecord;

            public int getHistoryWin() {
                return historyWin;
            }

            public void setHistoryWin(int historyWin) {
                this.historyWin = historyWin;
            }

            public FutureMatchEntity getFutureMatch() {
                return futureMatch;
            }

            public void setFutureMatch(FutureMatchEntity futureMatch) {
                this.futureMatch = futureMatch;
            }

            public List<Integer> getRecentRecord() {
                return recentRecord;
            }

            public void setRecentRecord(List<Integer> recentRecord) {
                this.recentRecord = recentRecord;
            }

            public static class FutureMatchEntity {
                private String team;
                private String logoUrl;
                private int diffDays;

                public String getTeam() {
                    return team;
                }

                public void setTeam(String team) {
                    this.team = team;
                }

                public String getLogoUrl() {
                    return logoUrl;
                }

                public void setLogoUrl(String logoUrl) {
                    this.logoUrl = logoUrl;
                }

                public int getDiffDays() {
                    return diffDays;
                }

                public void setDiffDays(int diffDays) {
                    this.diffDays = diffDays;
                }
            }
        }

        public static class GuestEntity {
            private int historyWin;
            /**
             * team : 布卡拉曼
             * logoUrl : http://pic.13322.com/icons/teams/100/2652.png
             * diffDays : 7
             */

            private FutureMatchEntity futureMatch;
            private List<Integer> recentRecord;

            public int getHistoryWin() {
                return historyWin;
            }

            public void setHistoryWin(int historyWin) {
                this.historyWin = historyWin;
            }

            public FutureMatchEntity getFutureMatch() {
                return futureMatch;
            }

            public void setFutureMatch(FutureMatchEntity futureMatch) {
                this.futureMatch = futureMatch;
            }

            public List<Integer> getRecentRecord() {
                return recentRecord;
            }

            public void setRecentRecord(List<Integer> recentRecord) {
                this.recentRecord = recentRecord;
            }

            public static class FutureMatchEntity {
                private String team;
                private String logoUrl;
                private int diffDays;

                public String getTeam() {
                    return team;
                }

                public void setTeam(String team) {
                    this.team = team;
                }

                public String getLogoUrl() {
                    return logoUrl;
                }

                public void setLogoUrl(String logoUrl) {
                    this.logoUrl = logoUrl;
                }

                public int getDiffDays() {
                    return diffDays;
                }

                public void setDiffDays(int diffDays) {
                    this.diffDays = diffDays;
                }
            }
        }
    }
}
