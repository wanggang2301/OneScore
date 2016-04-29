package com.hhly.mlottery.bean.footballsecond;

import java.util.List;

/**
 * Created by Administrator on 2016/4/4 0004.
 * <p/>
 * 积分榜实体bean
 */
public class IntegralBean {


    /**
     * abb : 英超
     * leagueDate : [{"date":"2014-2015","currentSeason":false},{"date":"2015-2016","currentSeason":true}]
     */

    private LeagueTimesBean leagueTimes;
    /**
     * leagueTimes : {"abb":"英超","leagueDate":[{"date":"2014-2015","currentSeason":false},{"date":"2015-2016","currentSeason":true}]}
     * code : 200
     * langueScore : [{"group":"A组","list":[{"tid":20,"name":"阿申纳塞是","round":25,"win":0,"equ":20,"fail":0,"goal":20,"loss":20,"abs":0,"score":20},{"tid":19,"name":"阿申纳塞是","round":25,"win":0,"equ":19,"fail":0,"goal":19,"loss":19,"abs":0,"score":19},{"tid":18,"name":"阿申纳塞是","round":25,"win":0,"equ":18,"fail":0,"goal":18,"loss":18,"abs":0,"score":18},{"tid":17,"name":"阿申纳塞是","round":25,"win":0,"equ":17,"fail":0,"goal":17,"loss":17,"abs":0,"score":17},{"tid":16,"name":"阿申纳塞是","round":25,"win":0,"equ":16,"fail":0,"goal":16,"loss":16,"abs":0,"score":16},{"tid":15,"name":"阿申纳塞是","round":25,"win":0,"equ":15,"fail":0,"goal":15,"loss":15,"abs":0,"score":15},{"tid":14,"name":"阿申纳塞是","round":25,"win":0,"equ":14,"fail":0,"goal":14,"loss":14,"abs":0,"score":14},{"tid":13,"name":"阿申纳塞是","round":25,"win":0,"equ":13,"fail":0,"goal":13,"loss":13,"abs":0,"score":13},{"tid":12,"name":"阿申纳塞是","round":25,"win":0,"equ":12,"fail":0,"goal":12,"loss":12,"abs":0,"score":12},{"tid":11,"name":"阿申纳塞是","round":25,"win":0,"equ":11,"fail":0,"goal":11,"loss":11,"abs":0,"score":11},{"tid":10,"name":"阿申纳塞是","round":25,"win":0,"equ":10,"fail":0,"goal":10,"loss":10,"abs":0,"score":10},{"tid":9,"name":"阿申纳塞是","round":25,"win":0,"equ":9,"fail":0,"goal":9,"loss":9,"abs":0,"score":9},{"tid":8,"name":"阿申纳塞是","round":25,"win":0,"equ":8,"fail":0,"goal":8,"loss":8,"abs":0,"score":8},{"tid":7,"name":"阿申纳塞是","round":25,"win":0,"equ":7,"fail":0,"goal":7,"loss":7,"abs":0,"score":7},{"tid":6,"name":"阿申纳塞是","round":25,"win":0,"equ":6,"fail":0,"goal":6,"loss":6,"abs":0,"score":6},{"tid":5,"name":"阿申纳塞是","round":25,"win":0,"equ":5,"fail":0,"goal":5,"loss":5,"abs":0,"score":5},{"tid":4,"name":"阿申纳塞是","round":25,"win":0,"equ":4,"fail":0,"goal":4,"loss":4,"abs":0,"score":4},{"tid":3,"name":"阿申纳塞是","round":25,"win":0,"equ":3,"fail":0,"goal":3,"loss":3,"abs":0,"score":3},{"tid":2,"name":"阿申纳塞是","round":25,"win":0,"equ":2,"fail":0,"goal":2,"loss":2,"abs":0,"score":2},{"tid":1,"name":"阿申纳塞是","round":25,"win":0,"equ":1,"fail":0,"goal":1,"loss":1,"abs":0,"score":1}]},{"group":"B组","list":[{"tid":20,"name":"阿申纳塞是","round":25,"win":0,"equ":20,"fail":0,"goal":20,"loss":20,"abs":0,"score":20},{"tid":19,"name":"阿申纳塞是","round":25,"win":0,"equ":19,"fail":0,"goal":19,"loss":19,"abs":0,"score":19},{"tid":18,"name":"阿申纳塞是","round":25,"win":0,"equ":18,"fail":0,"goal":18,"loss":18,"abs":0,"score":18},{"tid":17,"name":"阿申纳塞是","round":25,"win":0,"equ":17,"fail":0,"goal":17,"loss":17,"abs":0,"score":17},{"tid":16,"name":"阿申纳塞是","round":25,"win":0,"equ":16,"fail":0,"goal":16,"loss":16,"abs":0,"score":16},{"tid":15,"name":"阿申纳塞是","round":25,"win":0,"equ":15,"fail":0,"goal":15,"loss":15,"abs":0,"score":15},{"tid":14,"name":"阿申纳塞是","round":25,"win":0,"equ":14,"fail":0,"goal":14,"loss":14,"abs":0,"score":14},{"tid":13,"name":"阿申纳塞是","round":25,"win":0,"equ":13,"fail":0,"goal":13,"loss":13,"abs":0,"score":13},{"tid":12,"name":"阿申纳塞是","round":25,"win":0,"equ":12,"fail":0,"goal":12,"loss":12,"abs":0,"score":12},{"tid":11,"name":"阿申纳塞是","round":25,"win":0,"equ":11,"fail":0,"goal":11,"loss":11,"abs":0,"score":11},{"tid":10,"name":"阿申纳塞是","round":25,"win":0,"equ":10,"fail":0,"goal":10,"loss":10,"abs":0,"score":10},{"tid":9,"name":"阿申纳塞是","round":25,"win":0,"equ":9,"fail":0,"goal":9,"loss":9,"abs":0,"score":9},{"tid":8,"name":"阿申纳塞是","round":25,"win":0,"equ":8,"fail":0,"goal":8,"loss":8,"abs":0,"score":8},{"tid":7,"name":"阿申纳塞是","round":25,"win":0,"equ":7,"fail":0,"goal":7,"loss":7,"abs":0,"score":7},{"tid":6,"name":"阿申纳塞是","round":25,"win":0,"equ":6,"fail":0,"goal":6,"loss":6,"abs":0,"score":6},{"tid":5,"name":"阿申纳塞是","round":25,"win":0,"equ":5,"fail":0,"goal":5,"loss":5,"abs":0,"score":5},{"tid":4,"name":"阿申纳塞是","round":25,"win":0,"equ":4,"fail":0,"goal":4,"loss":4,"abs":0,"score":4},{"tid":3,"name":"阿申纳塞是","round":25,"win":0,"equ":3,"fail":0,"goal":3,"loss":3,"abs":0,"score":3},{"tid":2,"name":"阿申纳塞是","round":25,"win":0,"equ":2,"fail":0,"goal":2,"loss":2,"abs":0,"score":2},{"tid":1,"name":"阿申纳塞是","round":25,"win":0,"equ":1,"fail":0,"goal":1,"loss":1,"abs":0,"score":1}]}]
     */

    private int code;
    /**
     * group : A组
     * list : [{"tid":20,"name":"阿申纳塞是","round":25,"win":0,"equ":20,"fail":0,"goal":20,"loss":20,"abs":0,"score":20},{"tid":19,"name":"阿申纳塞是","round":25,"win":0,"equ":19,"fail":0,"goal":19,"loss":19,"abs":0,"score":19},{"tid":18,"name":"阿申纳塞是","round":25,"win":0,"equ":18,"fail":0,"goal":18,"loss":18,"abs":0,"score":18},{"tid":17,"name":"阿申纳塞是","round":25,"win":0,"equ":17,"fail":0,"goal":17,"loss":17,"abs":0,"score":17},{"tid":16,"name":"阿申纳塞是","round":25,"win":0,"equ":16,"fail":0,"goal":16,"loss":16,"abs":0,"score":16},{"tid":15,"name":"阿申纳塞是","round":25,"win":0,"equ":15,"fail":0,"goal":15,"loss":15,"abs":0,"score":15},{"tid":14,"name":"阿申纳塞是","round":25,"win":0,"equ":14,"fail":0,"goal":14,"loss":14,"abs":0,"score":14},{"tid":13,"name":"阿申纳塞是","round":25,"win":0,"equ":13,"fail":0,"goal":13,"loss":13,"abs":0,"score":13},{"tid":12,"name":"阿申纳塞是","round":25,"win":0,"equ":12,"fail":0,"goal":12,"loss":12,"abs":0,"score":12},{"tid":11,"name":"阿申纳塞是","round":25,"win":0,"equ":11,"fail":0,"goal":11,"loss":11,"abs":0,"score":11},{"tid":10,"name":"阿申纳塞是","round":25,"win":0,"equ":10,"fail":0,"goal":10,"loss":10,"abs":0,"score":10},{"tid":9,"name":"阿申纳塞是","round":25,"win":0,"equ":9,"fail":0,"goal":9,"loss":9,"abs":0,"score":9},{"tid":8,"name":"阿申纳塞是","round":25,"win":0,"equ":8,"fail":0,"goal":8,"loss":8,"abs":0,"score":8},{"tid":7,"name":"阿申纳塞是","round":25,"win":0,"equ":7,"fail":0,"goal":7,"loss":7,"abs":0,"score":7},{"tid":6,"name":"阿申纳塞是","round":25,"win":0,"equ":6,"fail":0,"goal":6,"loss":6,"abs":0,"score":6},{"tid":5,"name":"阿申纳塞是","round":25,"win":0,"equ":5,"fail":0,"goal":5,"loss":5,"abs":0,"score":5},{"tid":4,"name":"阿申纳塞是","round":25,"win":0,"equ":4,"fail":0,"goal":4,"loss":4,"abs":0,"score":4},{"tid":3,"name":"阿申纳塞是","round":25,"win":0,"equ":3,"fail":0,"goal":3,"loss":3,"abs":0,"score":3},{"tid":2,"name":"阿申纳塞是","round":25,"win":0,"equ":2,"fail":0,"goal":2,"loss":2,"abs":0,"score":2},{"tid":1,"name":"阿申纳塞是","round":25,"win":0,"equ":1,"fail":0,"goal":1,"loss":1,"abs":0,"score":1}]
     */

    private List<LangueScoreBean> langueScore;

    public LeagueTimesBean getLeagueTimes() {
        return leagueTimes;
    }

    public void setLeagueTimes(LeagueTimesBean leagueTimes) {
        this.leagueTimes = leagueTimes;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<LangueScoreBean> getLangueScore() {
        return langueScore;
    }

    public void setLangueScore(List<LangueScoreBean> langueScore) {
        this.langueScore = langueScore;
    }

    public static class LeagueTimesBean {
        private String abb;
        /**
         * date : 2014-2015
         * currentSeason : false
         */

        private List<LeagueDateBean> leagueDate;

        public String getAbb() {
            return abb;
        }

        public void setAbb(String abb) {
            this.abb = abb;
        }

        public List<LeagueDateBean> getLeagueDate() {
            return leagueDate;
        }

        public void setLeagueDate(List<LeagueDateBean> leagueDate) {
            this.leagueDate = leagueDate;
        }

        public static class LeagueDateBean {
            private String date;
            private boolean currentSeason;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public boolean isCurrentSeason() {
                return currentSeason;
            }

            public void setCurrentSeason(boolean currentSeason) {
                this.currentSeason = currentSeason;
            }
        }
    }

    public static class LangueScoreBean {
        private String group;
        /**
         * tid : 20
         * name : 阿申纳塞是
         * round : 25
         * win : 0
         * equ : 20
         * fail : 0
         * goal : 20
         * loss : 20
         * abs : 0
         * score : 20
         */

        private List<ListBean> list;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private int tid;//球队ID
            private String name;//球队名称
            private int round;// 比赛场次
            private int win;//胜
             private int equ;//平
            private int fail;//负
            private int goal;// 进球
            private int loss;//失球
            private int abs;//净胜
            private int score;// 得分

            public int getTid() {
                return tid;
            }

            public void setTid(int tid) {
                this.tid = tid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getRound() {
                return round;
            }

            public void setRound(int round) {
                this.round = round;
            }

            public int getWin() {
                return win;
            }

            public void setWin(int win) {
                this.win = win;
            }

            public int getEqu() {
                return equ;
            }

            public void setEqu(int equ) {
                this.equ = equ;
            }

            public int getFail() {
                return fail;
            }

            public void setFail(int fail) {
                this.fail = fail;
            }

            public int getGoal() {
                return goal;
            }

            public void setGoal(int goal) {
                this.goal = goal;
            }

            public int getLoss() {
                return loss;
            }

            public void setLoss(int loss) {
                this.loss = loss;
            }

            public int getAbs() {
                return abs;
            }

            public void setAbs(int abs) {
                this.abs = abs;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }
        }
    }
}
