package com.hhly.mlottery.bean.footballDetails;

import java.util.List;

/**
 * Created by madongyun 155 on 2016/6/15.
 */
public class NewAnalyzeBean {


    private String result;
    private int fullScoreRank;


    private AttackDefenseEntity attackDefense;
    private LineUpEntity lineUp;


    private ScoreRankEntity scoreRank;
    /**
     * home : {"historyWin":0,"recentRecord":[0,2,0,2,2,2],"futureMatch":{"team":"皇马","logoUrl":"http://pic.13322.com/logo/123455.png","diffDays":4}}
     * guest : {"historyWin":2,"recentRecord":[0,1,1,2,2,0],"futureMatch":{"team":"巴萨","logoUrl":"http://pic.13322.com/logo/343455.png","diffDays":10}}
     */

    private BothRecordEntity bothRecord;

    public void setResult(String result) {
        this.result = result;
    }

    public void setFullScoreRank(int fullScoreRank) {
        this.fullScoreRank = fullScoreRank;
    }

    public void setAttackDefense(AttackDefenseEntity attackDefense) {
        this.attackDefense = attackDefense;
    }

    public void setLineUp(LineUpEntity lineUp) {
        this.lineUp = lineUp;
    }

    public void setScoreRank(ScoreRankEntity scoreRank) {
        this.scoreRank = scoreRank;
    }

    public void setBothRecord(BothRecordEntity bothRecord) {
        this.bothRecord = bothRecord;
    }

    public String getResult() {
        return result;
    }

    public int getFullScoreRank() {
        return fullScoreRank;
    }

    public AttackDefenseEntity getAttackDefense() {
        return attackDefense;
    }

    public LineUpEntity getLineUp() {
        return lineUp;
    }

    public ScoreRankEntity getScoreRank() {
        return scoreRank;
    }

    public BothRecordEntity getBothRecord() {
        return bothRecord;
    }

    public static class AttackDefenseEntity {
        private String guestFieldGoal;
        private String guestFieldLose;
        private String sizeHandicap;
        private String homeFieldGoal;
        private String homeFieldLose;

        public void setGuestFieldGoal(String guestFieldGoal) {
            this.guestFieldGoal = guestFieldGoal;
        }

        public void setGuestFieldLose(String guestFieldLose) {
            this.guestFieldLose = guestFieldLose;
        }

        public void setSizeHandicap(String sizeHandicap) {
            this.sizeHandicap = sizeHandicap;
        }

        public void setHomeFieldGoal(String homeFieldGoal) {
            this.homeFieldGoal = homeFieldGoal;
        }

        public void setHomeFieldLose(String homeFieldLose) {
            this.homeFieldLose = homeFieldLose;
        }

        public String getGuestFieldGoal() {
            return guestFieldGoal;
        }

        public String getGuestFieldLose() {
            return guestFieldLose;
        }

        public String getSizeHandicap() {
            return sizeHandicap;
        }

        public String getHomeFieldGoal() {
            return homeFieldGoal;
        }

        public String getHomeFieldLose() {
            return homeFieldLose;
        }
    }

    public static class LineUpEntity {
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
    }

    public static class ScoreRankEntity {
        /**
         * rank : 15
         * team : 西布罗姆维奇
         * vsCount : 37
         * win : 10
         * draw : 12
         * lose : 15
         * integral : 42
         * goalDiff : -14
         * goal : 33
         * miss : 47
         */

        private HomeEntity home;
        /**
         * rank : 8
         * team : 利物浦
         * vsCount : 37
         * win : 16
         * draw : 11
         * lose : 10
         * integral : 59
         * goalDiff : 13
         * goal : 62
         * miss : 49
         */

        private GuestEntity guest;

        public void setHome(HomeEntity home) {
            this.home = home;
        }

        public void setGuest(GuestEntity guest) {
            this.guest = guest;
        }

        public HomeEntity getHome() {
            return home;
        }

        public GuestEntity getGuest() {
            return guest;
        }

        public static class HomeEntity {
            private int rank;
            private String team;
            private int vsCount;
            private int win;
            private int draw;
            private int lose;
            private int integral;
            private int goalDiff;
            private int goal;
            private int miss;

            public void setRank(int rank) {
                this.rank = rank;
            }

            public void setTeam(String team) {
                this.team = team;
            }

            public void setVsCount(int vsCount) {
                this.vsCount = vsCount;
            }

            public void setWin(int win) {
                this.win = win;
            }

            public void setDraw(int draw) {
                this.draw = draw;
            }

            public void setLose(int lose) {
                this.lose = lose;
            }

            public void setIntegral(int integral) {
                this.integral = integral;
            }

            public void setGoalDiff(int goalDiff) {
                this.goalDiff = goalDiff;
            }

            public void setGoal(int goal) {
                this.goal = goal;
            }

            public void setMiss(int miss) {
                this.miss = miss;
            }

            public int getRank() {
                return rank;
            }

            public String getTeam() {
                return team;
            }

            public int getVsCount() {
                return vsCount;
            }

            public int getWin() {
                return win;
            }

            public int getDraw() {
                return draw;
            }

            public int getLose() {
                return lose;
            }

            public int getIntegral() {
                return integral;
            }

            public int getGoalDiff() {
                return goalDiff;
            }

            public int getGoal() {
                return goal;
            }

            public int getMiss() {
                return miss;
            }
        }

        public static class GuestEntity {
            private int rank;
            private String team;
            private int vsCount;
            private int win;
            private int draw;
            private int lose;
            private int integral;
            private int goalDiff;
            private int goal;
            private int miss;

            public void setRank(int rank) {
                this.rank = rank;
            }

            public void setTeam(String team) {
                this.team = team;
            }

            public void setVsCount(int vsCount) {
                this.vsCount = vsCount;
            }

            public void setWin(int win) {
                this.win = win;
            }

            public void setDraw(int draw) {
                this.draw = draw;
            }

            public void setLose(int lose) {
                this.lose = lose;
            }

            public void setIntegral(int integral) {
                this.integral = integral;
            }

            public void setGoalDiff(int goalDiff) {
                this.goalDiff = goalDiff;
            }

            public void setGoal(int goal) {
                this.goal = goal;
            }

            public void setMiss(int miss) {
                this.miss = miss;
            }

            public int getRank() {
                return rank;
            }

            public String getTeam() {
                return team;
            }

            public int getVsCount() {
                return vsCount;
            }

            public int getWin() {
                return win;
            }

            public int getDraw() {
                return draw;
            }

            public int getLose() {
                return lose;
            }

            public int getIntegral() {
                return integral;
            }

            public int getGoalDiff() {
                return goalDiff;
            }

            public int getGoal() {
                return goal;
            }

            public int getMiss() {
                return miss;
            }
        }
    }

    public static class BothRecordEntity {
        /**
         * historyWin : 0
         * recentRecord : [0,2,0,2,2,2]
         * futureMatch : {"team":"皇马","logoUrl":"http://pic.13322.com/logo/123455.png","diffDays":4}
         */

        private HomeEntity home;
        /**
         * historyWin : 2
         * recentRecord : [0,1,1,2,2,0]
         * futureMatch : {"team":"巴萨","logoUrl":"http://pic.13322.com/logo/343455.png","diffDays":10}
         */

        private GuestEntity guest;

        public void setHome(HomeEntity home) {
            this.home = home;
        }

        public void setGuest(GuestEntity guest) {
            this.guest = guest;
        }

        public HomeEntity getHome() {
            return home;
        }

        public GuestEntity getGuest() {
            return guest;
        }

        public static class HomeEntity {
            private int historyWin;
            /**
             * team : 皇马
             * logoUrl : http://pic.13322.com/logo/123455.png
             * diffDays : 4
             */

            private FutureMatchEntity futureMatch;
            private List<Integer> recentRecord;

            public void setHistoryWin(int historyWin) {
                this.historyWin = historyWin;
            }

            public void setFutureMatch(FutureMatchEntity futureMatch) {
                this.futureMatch = futureMatch;
            }

            public void setRecentRecord(List<Integer> recentRecord) {
                this.recentRecord = recentRecord;
            }

            public int getHistoryWin() {
                return historyWin;
            }

            public FutureMatchEntity getFutureMatch() {
                return futureMatch;
            }

            public List<Integer> getRecentRecord() {
                return recentRecord;
            }

            public static class FutureMatchEntity {
                private String team;
                private String logoUrl;
                private int diffDays;

                public void setTeam(String team) {
                    this.team = team;
                }

                public void setLogoUrl(String logoUrl) {
                    this.logoUrl = logoUrl;
                }

                public void setDiffDays(int diffDays) {
                    this.diffDays = diffDays;
                }

                public String getTeam() {
                    return team;
                }

                public String getLogoUrl() {
                    return logoUrl;
                }

                public int getDiffDays() {
                    return diffDays;
                }
            }
        }

        public static class GuestEntity {
            private int historyWin;
            /**
             * team : 巴萨
             * logoUrl : http://pic.13322.com/logo/343455.png
             * diffDays : 10
             */

            private FutureMatchEntity futureMatch;
            private List<Integer> recentRecord;

            public void setHistoryWin(int historyWin) {
                this.historyWin = historyWin;
            }

            public void setFutureMatch(FutureMatchEntity futureMatch) {
                this.futureMatch = futureMatch;
            }

            public void setRecentRecord(List<Integer> recentRecord) {
                this.recentRecord = recentRecord;
            }

            public int getHistoryWin() {
                return historyWin;
            }

            public FutureMatchEntity getFutureMatch() {
                return futureMatch;
            }

            public List<Integer> getRecentRecord() {
                return recentRecord;
            }

            public static class FutureMatchEntity {
                private String team;
                private String logoUrl;
                private int diffDays;

                public void setTeam(String team) {
                    this.team = team;
                }

                public void setLogoUrl(String logoUrl) {
                    this.logoUrl = logoUrl;
                }

                public void setDiffDays(int diffDays) {
                    this.diffDays = diffDays;
                }

                public String getTeam() {
                    return team;
                }

                public String getLogoUrl() {
                    return logoUrl;
                }

                public int getDiffDays() {
                    return diffDays;
                }
            }
        }
    }
}
