package com.hhly.mlottery.bean.footballsecond;

import java.util.List;

/**
 * Created by andy on 2016/3/8.
 */
public class AnalyzeBean {

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private BattleHistoryEntity battleHistory;
    private TeamRecentEntity teamRecent;
    private List<GoalAndLossEntity> goalAndLoss;
    private List<ScoreRankEntity> scoreRank;

    public void setBattleHistory(BattleHistoryEntity battleHistory) {
        this.battleHistory = battleHistory;
    }

    public void setTeamRecent(TeamRecentEntity teamRecent) {
        this.teamRecent = teamRecent;
    }

    public void setGoalAndLoss(List<GoalAndLossEntity> goalAndLoss) {
        this.goalAndLoss = goalAndLoss;
    }

    public void setScoreRank(List<ScoreRankEntity> scoreRank) {
        this.scoreRank = scoreRank;
    }

    public BattleHistoryEntity getBattleHistory() {
        return battleHistory;
    }

    public TeamRecentEntity getTeamRecent() {
        return teamRecent;
    }

    public List<GoalAndLossEntity> getGoalAndLoss() {
        return goalAndLoss;
    }

    public List<ScoreRankEntity> getScoreRank() {
        return scoreRank;
    }

    public static class BattleHistoryEntity {
        private String battleCount;
        private String battleResult;
        private String winPro;
        private String goal;
        private String loss;

        private List<BattlesEntity> battles;

        public void setBattleCount(String battleCount) {
            this.battleCount = battleCount;
        }

        public void setBattleResult(String battleResult) {
            this.battleResult = battleResult;
        }

        public void setWinPro(String winPro) {
            this.winPro = winPro;
        }

        public void setGoal(String goal) {
            this.goal = goal;
        }

        public void setLoss(String loss) {
            this.loss = loss;
        }

        public void setBattles(List<BattlesEntity> battles) {
            this.battles = battles;
        }

        public String getBattleCount() {
            return battleCount;
        }

        public String getBattleResult() {
            return battleResult;
        }

        public String getWinPro() {
            return winPro;
        }

        public String getGoal() {
            return goal;
        }

        public String getLoss() {
            return loss;
        }

        public List<BattlesEntity> getBattles() {
            return battles;
        }

        public static class BattlesEntity extends Battels {
            private String time;
            private String matchType;
            private String home;
            private String guest;
            private String result;
            private int markTeam;

            public int getMarkTeam() {
                return markTeam;
            }

            public void setMarkTeam(int markTeam) {
                this.markTeam = markTeam;
            }

            public int getTeamColor() {
                return teamColor;
            }

            public void setTeamColor(int teamColor) {
                this.teamColor = teamColor;
            }

            private int teamColor;

            public void setTime(String time) {
                this.time = time;
            }

            public void setMatchType(String matchType) {
                this.matchType = matchType;
            }

            public void setHome(String home) {
                this.home = home;
            }

            public void setGuest(String guest) {
                this.guest = guest;
            }

            public void setResult(String result) {
                this.result = result;
            }

            public String getTime() {
                return time;
            }

            public String getMatchType() {
                return matchType;
            }

            public String getHome() {
                return home;
            }

            public String getGuest() {
                return guest;
            }

            public String getResult() {
                return result;
            }
        }
    }

    public static class TeamRecentEntity {


        private HomeEntity home;
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
            private String battleCount;
            private String battleResult;
            private String winPro;
            private String goal;
            private String loss;

            private List<BattlesEntity> battles;

            public void setBattleCount(String battleCount) {
                this.battleCount = battleCount;
            }

            public void setBattleResult(String battleResult) {
                this.battleResult = battleResult;
            }

            public void setWinPro(String winPro) {
                this.winPro = winPro;
            }

            public void setGoal(String goal) {
                this.goal = goal;
            }

            public void setLoss(String loss) {
                this.loss = loss;
            }

            public void setBattles(List<BattlesEntity> battles) {
                this.battles = battles;
            }

            public String getBattleCount() {
                return battleCount;
            }

            public String getBattleResult() {
                return battleResult;
            }

            public String getWinPro() {
                return winPro;
            }

            public String getGoal() {
                return goal;
            }

            public String getLoss() {
                return loss;
            }

            public List<BattlesEntity> getBattles() {
                return battles;
            }

            public static class BattlesEntity extends Battels {
                private String time;
                private String matchType;
                private String home;
                private String guest;
                private String result;
                private int markTeam;

                public int getMarkTeam() {
                    return markTeam;
                }

                public void setMarkTeam(int markTeam) {
                    this.markTeam = markTeam;
                }

                public int getTeamColor() {
                    return teamColor;
                }

                public void setTeamColor(int teamColor) {
                    this.teamColor = teamColor;
                }

                private int teamColor;

                public void setTime(String time) {
                    this.time = time;
                }

                public void setMatchType(String matchType) {
                    this.matchType = matchType;
                }

                public void setHome(String home) {
                    this.home = home;
                }

                public void setGuest(String guest) {
                    this.guest = guest;
                }

                public void setResult(String result) {
                    this.result = result;
                }

                public String getTime() {
                    return time;
                }

                public String getMatchType() {
                    return matchType;
                }

                public String getHome() {
                    return home;
                }

                public String getGuest() {
                    return guest;
                }

                public String getResult() {
                    return result;
                }
            }
        }

        public static class GuestEntity {
            private String battleCount;
            private String battleResult;
            private String winPro;
            private String goal;
            private String loss;

            private List<BattlesEntity> battles;

            public void setBattleCount(String battleCount) {
                this.battleCount = battleCount;
            }

            public void setBattleResult(String battleResult) {
                this.battleResult = battleResult;
            }

            public void setWinPro(String winPro) {
                this.winPro = winPro;
            }

            public void setGoal(String goal) {
                this.goal = goal;
            }

            public void setLoss(String loss) {
                this.loss = loss;
            }

            public void setBattles(List<BattlesEntity> battles) {
                this.battles = battles;
            }

            public String getBattleCount() {
                return battleCount;
            }

            public String getBattleResult() {
                return battleResult;
            }

            public String getWinPro() {
                return winPro;
            }

            public String getGoal() {
                return goal;
            }

            public String getLoss() {
                return loss;
            }

            public List<BattlesEntity> getBattles() {
                return battles;
            }

            public static class BattlesEntity extends Battels {
                private String time;
                private String matchType;
                private String home;
                private String guest;
                private String result;
                private int markTeam;

                public int getMarkTeam() {
                    return markTeam;
                }

                public void setMarkTeam(int markTeam) {
                    this.markTeam = markTeam;
                }

                public int getTeamColor() {
                    return teamColor;
                }

                public void setTeamColor(int teamColor) {
                    this.teamColor = teamColor;
                }

                private int teamColor;

                public void setTime(String time) {
                    this.time = time;
                }

                public void setMatchType(String matchType) {
                    this.matchType = matchType;
                }

                public void setHome(String home) {
                    this.home = home;
                }

                public void setGuest(String guest) {
                    this.guest = guest;
                }

                public void setResult(String result) {
                    this.result = result;
                }

                public String getTime() {
                    return time;
                }

                public String getMatchType() {
                    return matchType;
                }

                public String getHome() {
                    return home;
                }

                public String getGuest() {
                    return guest;
                }

                public String getResult() {
                    return result;
                }
            }
        }
    }

    public static class GoalAndLossEntity extends RankAndGoal {
        private int type;

        private ObjEntity obj;

        public void setType(int type) {
            this.type = type;
        }

        public void setObj(ObjEntity obj) {
            this.obj = obj;
        }

        public int getType() {
            return type;
        }

        public ObjEntity getObj() {
            return obj;
        }

        public static class ObjEntity {
            private String home;
            private String guest;

            public void setHome(String home) {
                this.home = home;
            }

            public void setGuest(String guest) {
                this.guest = guest;
            }

            public String getHome() {
                return home;
            }

            public String getGuest() {
                return guest;
            }
        }
    }

    public static class ScoreRankEntity extends RankAndGoal {
        private int type;

        private ObjEntity obj;

        public void setType(int type) {
            this.type = type;
        }

        public void setObj(ObjEntity obj) {
            this.obj = obj;
        }

        public int getType() {
            return type;
        }

        public ObjEntity getObj() {
            return obj;
        }

        public static class ObjEntity {
            private String home;
            private String guest;
            private int h_win;
            private int h_equ;
            private int h_defeat;
            private int h_total;
            private int g_win;
            private int g_equ;
            private int g_defeat;
            private int g_total;


            public int getH_win() {
                return h_win;
            }

            public void setH_win(int h_win) {
                this.h_win = h_win;
            }

            public int getH_equ() {
                return h_equ;
            }

            public void setH_equ(int h_equ) {
                this.h_equ = h_equ;
            }

            public int getH_defeat() {
                return h_defeat;
            }

            public void setH_defeat(int h_defeat) {
                this.h_defeat = h_defeat;
            }

            public int getH_total() {
                return h_total;
            }

            public void setH_total(int h_total) {
                this.h_total = h_total;
            }

            public int getG_win() {
                return g_win;
            }

            public void setG_win(int g_win) {
                this.g_win = g_win;
            }

            public int getG_equ() {
                return g_equ;
            }

            public void setG_equ(int g_equ) {
                this.g_equ = g_equ;
            }

            public int getG_defeat() {
                return g_defeat;
            }

            public void setG_defeat(int g_defeat) {
                this.g_defeat = g_defeat;
            }

            public int getG_total() {
                return g_total;
            }

            public void setG_total(int g_total) {
                this.g_total = g_total;
            }


            public void setHome(String home) {
                this.home = home;
            }

            public void setGuest(String guest) {
                this.guest = guest;
            }

            public String getHome() {
                return home;
            }

            public String getGuest() {
                return guest;
            }
        }
    }
}
