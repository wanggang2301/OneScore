package com.hhly.mlottery.bean.footballteaminfo;

/**
 * desc:足球球队详情数据
 * Created by 107_tangrr on 2017/4/20 0020.
 */

public class FootTeamInfoBean{

    private String lgName;
    private DataBean data;

    public String getLgName() {
        return lgName;
    }

    public void setLgName(String lgName) {
        this.lgName = lgName;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String rank;// 排名总,主,客
        private String score;// 积分: 总,主,客
        private String matchCount;// 比赛场次
        private String win;// 胜场
        private String equ;// 平
        private String lose;// 负
        private String goal;// 进球
        private String loseGoal;// 失球
        private String goalAvg;// 场均进球
        private String loseAvg;// 场均失球

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getMatchCount() {
            return matchCount;
        }

        public void setMatchCount(String matchCount) {
            this.matchCount = matchCount;
        }

        public String getWin() {
            return win;
        }

        public void setWin(String win) {
            this.win = win;
        }

        public String getEqu() {
            return equ;
        }

        public void setEqu(String equ) {
            this.equ = equ;
        }

        public String getLose() {
            return lose;
        }

        public void setLose(String lose) {
            this.lose = lose;
        }

        public String getGoal() {
            return goal;
        }

        public void setGoal(String goal) {
            this.goal = goal;
        }

        public String getLoseGoal() {
            return loseGoal;
        }

        public void setLoseGoal(String loseGoal) {
            this.loseGoal = loseGoal;
        }

        public String getGoalAvg() {
            return goalAvg;
        }

        public void setGoalAvg(String goalAvg) {
            this.goalAvg = goalAvg;
        }

        public String getLoseAvg() {
            return loseAvg;
        }

        public void setLoseAvg(String loseAvg) {
            this.loseAvg = loseAvg;
        }
    }
}
