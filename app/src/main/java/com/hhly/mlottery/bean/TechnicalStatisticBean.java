package com.hhly.mlottery.bean;

import java.util.List;

/**
 * Created by yuely198 on 2017/6/22.
 *
 */

public class TechnicalStatisticBean {


    /**
     * result : 200
     * playoffList : []
     * preseasonList : [{"matchKind":3,"season":"16-17","winRoutine":2,"loseRoutine":5,"winOvertime":0,"loseOvertime":1,"totalShoot":693,"averageShoot":86.6,"totalShootHit":307,"averageShootHit":38.3,"percentShot":"40%","totalPunishBall":226,"averagePunishBall":28.2,"totalPunishBallHit":149,"averagePunishBallHit":18.6,"percentThrow":"60%","totalThreeMin":235,"averageThreeMin":29.3,"totalThreeMinHit":235,"averageThreeMinHit":10.5,"percentThree":"30%","attack":78,"defend":259,"helpattack":177,"rob":79,"cover":30,"foul":196,"misplay":133},{"matchKind":3,"season":"15-16","winRoutine":3,"loseRoutine":4,"winOvertime":0,"loseOvertime":1,"totalShoot":641,"averageShoot":80.1,"totalShootHit":277,"averageShootHit":34.6,"percentShot":"40%","totalPunishBall":236,"averagePunishBall":29.5,"totalPunishBallHit":177,"averagePunishBallHit":22.1,"percentThrow":"70%","totalThreeMin":183,"averageThreeMin":22.8,"totalThreeMinHit":183,"averageThreeMinHit":7.8,"percentThree":"30%","attack":94,"defend":254,"helpattack":154,"rob":61,"cover":36,"foul":194,"misplay":117},{"matchKind":3,"season":"14-15","winRoutine":3,"loseRoutine":4,"winOvertime":0,"loseOvertime":1,"totalShoot":580,"averageShoot":72.5,"totalShootHit":266,"averageShootHit":33.2,"percentShot":"40%","totalPunishBall":202,"averagePunishBall":25.2,"totalPunishBallHit":149,"averagePunishBallHit":18.6,"percentThrow":"70%","totalThreeMin":82,"averageThreeMin":10.2,"totalThreeMinHit":82,"averageThreeMinHit":3.1,"percentThree":"30%","attack":87,"defend":235,"helpattack":175,"rob":71,"cover":40,"foul":217,"misplay":127}]
     * regularList : [{"matchKind":1,"season":"16-17","winRoutine":25,"loseRoutine":56,"winOvertime":1,"loseOvertime":0,"totalShoot":7164,"averageShoot":87.3,"totalShootHit":3224,"averageShootHit":39.3,"percentShot":"40%","totalPunishBall":1853,"averagePunishBall":22.5,"totalPunishBallHit":1397,"averagePunishBallHit":17,"percentThrow":"70%","totalThreeMin":2107,"averageThreeMin":25.6,"totalThreeMinHit":2107,"averageThreeMinHit":8.9,"percentThree":"30%","attack":934,"defend":2635,"helpattack":1716,"rob":676,"cover":324,"foul":1698,"misplay":1192},{"matchKind":1,"season":"15-16","winRoutine":16,"loseRoutine":64,"winOvertime":1,"loseOvertime":1,"totalShoot":6958,"averageShoot":84.8,"totalShootHit":2880,"averageShootHit":35.1,"percentShot":"40%","totalPunishBall":2028,"averagePunishBall":24.7,"totalPunishBallHit":1583,"averagePunishBallHit":19.3,"percentThrow":"70%","totalThreeMin":2011,"averageThreeMin":24.5,"totalThreeMinHit":2011,"averageThreeMinHit":7.7,"percentThree":"30%","attack":881,"defend":2645,"helpattack":1478,"rob":593,"cover":337,"foul":1662,"misplay":1068},{"matchKind":1,"season":"14-15","winRoutine":15,"loseRoutine":58,"winOvertime":6,"loseOvertime":3,"totalShoot":5474,"averageShoot":66.7,"totalShootHit":2522,"averageShootHit":30.7,"percentShot":"40%","totalPunishBall":1935,"averagePunishBall":23.5,"totalPunishBallHit":1433,"averagePunishBallHit":17.4,"percentThrow":"70%","totalThreeMin":1546,"averageThreeMin":18.8,"totalThreeMinHit":1546,"averageThreeMinHit":6.4,"percentThree":"30%","attack":952,"defend":2647,"helpattack":1715,"rob":578,"cover":366,"foul":1741,"misplay":1035}]
     */

    private int result;
    private List<DataBean> playoffList;
    private List<DataBean> preseasonList;
    private List<DataBean> regularList;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<DataBean> getPlayoffList() {
        return playoffList;
    }

    public void setPlayoffList(List<DataBean> playoffList) {
        this.playoffList = playoffList;
    }

    public List<DataBean> getPreseasonList() {
        return preseasonList;
    }

    public void setPreseasonList(List<DataBean> preseasonList) {
        this.preseasonList = preseasonList;
    }

    public List<DataBean> getRegularList() {
        return regularList;
    }

    public void setRegularList(List<DataBean> regularList) {
        this.regularList = regularList;

    }

    public static class DataBean {
        /**
         * matchKind : 3
         * season : 16-17
         * winRoutine : 2
         * loseRoutine : 5
         * winOvertime : 0
         * loseOvertime : 1
         * totalShoot : 693
         * averageShoot : 86.6
         * totalShootHit : 307
         * averageShootHit : 38.3
         * percentShot : 40%
         * totalPunishBall : 226
         * averagePunishBall : 28.2
         * totalPunishBallHit : 149
         * averagePunishBallHit : 18.6
         * percentThrow : 60%
         * totalThreeMin : 235
         * averageThreeMin : 29.3
         * totalThreeMinHit : 235
         * averageThreeMinHit : 10.5
         * percentThree : 30%
         * attack : 78
         * defend : 259
         * helpattack : 177
         * rob : 79
         * cover : 30
         * foul : 196
         * misplay : 133
         */

        private int matchKind;
        private String season;
        private int winRoutine;
        private int loseRoutine;
        private int winOvertime;
        private int loseOvertime;
        private int totalShoot;
        private double averageShoot;
        private int totalShootHit;
        private double averageShootHit;
        private String percentShot;
        private int totalPunishBall;
        private double averagePunishBall;
        private int totalPunishBallHit;
        private double averagePunishBallHit;
        private String percentThrow;
        private int totalThreeMin;
        private double averageThreeMin;
        private int totalThreeMinHit;
        private double averageThreeMinHit;
        private String percentThree;
        private int attack;
        private int defend;
        private int helpattack;
        private int rob;
        private int cover;
        private int foul;
        private int misplay;

        public int getMatchKind() {
            return matchKind;
        }

        public void setMatchKind(int matchKind) {
            this.matchKind = matchKind;
        }

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public int getWinRoutine() {
            return winRoutine;
        }

        public void setWinRoutine(int winRoutine) {
            this.winRoutine = winRoutine;
        }

        public int getLoseRoutine() {
            return loseRoutine;
        }

        public void setLoseRoutine(int loseRoutine) {
            this.loseRoutine = loseRoutine;
        }

        public int getWinOvertime() {
            return winOvertime;
        }

        public void setWinOvertime(int winOvertime) {
            this.winOvertime = winOvertime;
        }

        public int getLoseOvertime() {
            return loseOvertime;
        }

        public void setLoseOvertime(int loseOvertime) {
            this.loseOvertime = loseOvertime;
        }

        public int getTotalShoot() {
            return totalShoot;
        }

        public void setTotalShoot(int totalShoot) {
            this.totalShoot = totalShoot;
        }

        public double getAverageShoot() {
            return averageShoot;
        }

        public void setAverageShoot(double averageShoot) {
            this.averageShoot = averageShoot;
        }

        public int getTotalShootHit() {
            return totalShootHit;
        }

        public void setTotalShootHit(int totalShootHit) {
            this.totalShootHit = totalShootHit;
        }

        public double getAverageShootHit() {
            return averageShootHit;
        }

        public void setAverageShootHit(double averageShootHit) {
            this.averageShootHit = averageShootHit;
        }

        public String getPercentShot() {
            return percentShot;
        }

        public void setPercentShot(String percentShot) {
            this.percentShot = percentShot;
        }

        public int getTotalPunishBall() {
            return totalPunishBall;
        }

        public void setTotalPunishBall(int totalPunishBall) {
            this.totalPunishBall = totalPunishBall;
        }

        public double getAveragePunishBall() {
            return averagePunishBall;
        }

        public void setAveragePunishBall(double averagePunishBall) {
            this.averagePunishBall = averagePunishBall;
        }

        public int getTotalPunishBallHit() {
            return totalPunishBallHit;
        }

        public void setTotalPunishBallHit(int totalPunishBallHit) {
            this.totalPunishBallHit = totalPunishBallHit;
        }

        public double getAveragePunishBallHit() {
            return averagePunishBallHit;
        }

        public void setAveragePunishBallHit(double averagePunishBallHit) {
            this.averagePunishBallHit = averagePunishBallHit;
        }

        public String getPercentThrow() {
            return percentThrow;
        }

        public void setPercentThrow(String percentThrow) {
            this.percentThrow = percentThrow;
        }

        public int getTotalThreeMin() {
            return totalThreeMin;
        }

        public void setTotalThreeMin(int totalThreeMin) {
            this.totalThreeMin = totalThreeMin;
        }

        public double getAverageThreeMin() {
            return averageThreeMin;
        }

        public void setAverageThreeMin(double averageThreeMin) {
            this.averageThreeMin = averageThreeMin;
        }

        public int getTotalThreeMinHit() {
            return totalThreeMinHit;
        }

        public void setTotalThreeMinHit(int totalThreeMinHit) {
            this.totalThreeMinHit = totalThreeMinHit;
        }

        public double getAverageThreeMinHit() {
            return averageThreeMinHit;
        }

        public void setAverageThreeMinHit(double averageThreeMinHit) {
            this.averageThreeMinHit = averageThreeMinHit;
        }

        public String getPercentThree() {
            return percentThree;
        }

        public void setPercentThree(String percentThree) {
            this.percentThree = percentThree;
        }

        public int getAttack() {
            return attack;
        }

        public void setAttack(int attack) {
            this.attack = attack;
        }

        public int getDefend() {
            return defend;
        }

        public void setDefend(int defend) {
            this.defend = defend;
        }

        public int getHelpattack() {
            return helpattack;
        }

        public void setHelpattack(int helpattack) {
            this.helpattack = helpattack;
        }

        public int getRob() {
            return rob;
        }

        public void setRob(int rob) {
            this.rob = rob;
        }

        public int getCover() {
            return cover;
        }

        public void setCover(int cover) {
            this.cover = cover;
        }

        public int getFoul() {
            return foul;
        }

        public void setFoul(int foul) {
            this.foul = foul;
        }

        public int getMisplay() {
            return misplay;
        }

        public void setMisplay(int misplay) {
            this.misplay = misplay;
        }
    }

    public static class PreseasonListBean {
        /**
         * matchKind : 3
         * season : 16-17
         * winRoutine : 2
         * loseRoutine : 5
         * winOvertime : 0
         * loseOvertime : 1
         * totalShoot : 693
         * averageShoot : 86.6
         * totalShootHit : 307
         * averageShootHit : 38.3
         * percentShot : 40%
         * totalPunishBall : 226
         * averagePunishBall : 28.2
         * totalPunishBallHit : 149
         * averagePunishBallHit : 18.6
         * percentThrow : 60%
         * totalThreeMin : 235
         * averageThreeMin : 29.3
         * totalThreeMinHit : 235
         * averageThreeMinHit : 10.5
         * percentThree : 30%
         * attack : 78
         * defend : 259
         * helpattack : 177
         * rob : 79
         * cover : 30
         * foul : 196
         * misplay : 133
         */

        private int matchKind;
        private String season;
        private int winRoutine;
        private int loseRoutine;
        private int winOvertime;
        private int loseOvertime;
        private int totalShoot;
        private double averageShoot;
        private int totalShootHit;
        private double averageShootHit;
        private String percentShot;
        private int totalPunishBall;
        private double averagePunishBall;
        private int totalPunishBallHit;
        private double averagePunishBallHit;
        private String percentThrow;
        private int totalThreeMin;
        private double averageThreeMin;
        private int totalThreeMinHit;
        private double averageThreeMinHit;
        private String percentThree;
        private int attack;
        private int defend;
        private int helpattack;
        private int rob;
        private int cover;
        private int foul;
        private int misplay;

        public int getMatchKind() {
            return matchKind;
        }

        public void setMatchKind(int matchKind) {
            this.matchKind = matchKind;
        }

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public int getWinRoutine() {
            return winRoutine;
        }

        public void setWinRoutine(int winRoutine) {
            this.winRoutine = winRoutine;
        }

        public int getLoseRoutine() {
            return loseRoutine;
        }

        public void setLoseRoutine(int loseRoutine) {
            this.loseRoutine = loseRoutine;
        }

        public int getWinOvertime() {
            return winOvertime;
        }

        public void setWinOvertime(int winOvertime) {
            this.winOvertime = winOvertime;
        }

        public int getLoseOvertime() {
            return loseOvertime;
        }

        public void setLoseOvertime(int loseOvertime) {
            this.loseOvertime = loseOvertime;
        }

        public int getTotalShoot() {
            return totalShoot;
        }

        public void setTotalShoot(int totalShoot) {
            this.totalShoot = totalShoot;
        }

        public double getAverageShoot() {
            return averageShoot;
        }

        public void setAverageShoot(double averageShoot) {
            this.averageShoot = averageShoot;
        }

        public int getTotalShootHit() {
            return totalShootHit;
        }

        public void setTotalShootHit(int totalShootHit) {
            this.totalShootHit = totalShootHit;
        }

        public double getAverageShootHit() {
            return averageShootHit;
        }

        public void setAverageShootHit(double averageShootHit) {
            this.averageShootHit = averageShootHit;
        }

        public String getPercentShot() {
            return percentShot;
        }

        public void setPercentShot(String percentShot) {
            this.percentShot = percentShot;
        }

        public int getTotalPunishBall() {
            return totalPunishBall;
        }

        public void setTotalPunishBall(int totalPunishBall) {
            this.totalPunishBall = totalPunishBall;
        }

        public double getAveragePunishBall() {
            return averagePunishBall;
        }

        public void setAveragePunishBall(double averagePunishBall) {
            this.averagePunishBall = averagePunishBall;
        }

        public int getTotalPunishBallHit() {
            return totalPunishBallHit;
        }

        public void setTotalPunishBallHit(int totalPunishBallHit) {
            this.totalPunishBallHit = totalPunishBallHit;
        }

        public double getAveragePunishBallHit() {
            return averagePunishBallHit;
        }

        public void setAveragePunishBallHit(double averagePunishBallHit) {
            this.averagePunishBallHit = averagePunishBallHit;
        }

        public String getPercentThrow() {
            return percentThrow;
        }

        public void setPercentThrow(String percentThrow) {
            this.percentThrow = percentThrow;
        }

        public int getTotalThreeMin() {
            return totalThreeMin;
        }

        public void setTotalThreeMin(int totalThreeMin) {
            this.totalThreeMin = totalThreeMin;
        }

        public double getAverageThreeMin() {
            return averageThreeMin;
        }

        public void setAverageThreeMin(double averageThreeMin) {
            this.averageThreeMin = averageThreeMin;
        }

        public int getTotalThreeMinHit() {
            return totalThreeMinHit;
        }

        public void setTotalThreeMinHit(int totalThreeMinHit) {
            this.totalThreeMinHit = totalThreeMinHit;
        }

        public double getAverageThreeMinHit() {
            return averageThreeMinHit;
        }

        public void setAverageThreeMinHit(double averageThreeMinHit) {
            this.averageThreeMinHit = averageThreeMinHit;
        }

        public String getPercentThree() {
            return percentThree;
        }

        public void setPercentThree(String percentThree) {
            this.percentThree = percentThree;
        }

        public int getAttack() {
            return attack;
        }

        public void setAttack(int attack) {
            this.attack = attack;
        }

        public int getDefend() {
            return defend;
        }

        public void setDefend(int defend) {
            this.defend = defend;
        }

        public int getHelpattack() {
            return helpattack;
        }

        public void setHelpattack(int helpattack) {
            this.helpattack = helpattack;
        }

        public int getRob() {
            return rob;
        }

        public void setRob(int rob) {
            this.rob = rob;
        }

        public int getCover() {
            return cover;
        }

        public void setCover(int cover) {
            this.cover = cover;
        }

        public int getFoul() {
            return foul;
        }

        public void setFoul(int foul) {
            this.foul = foul;
        }

        public int getMisplay() {
            return misplay;
        }

        public void setMisplay(int misplay) {
            this.misplay = misplay;
        }
    }

    public static class PlayoffListBean {
        /**
         * matchKind : 3
         * season : 16-17
         * winRoutine : 2
         * loseRoutine : 5
         * winOvertime : 0
         * loseOvertime : 1
         * totalShoot : 693
         * averageShoot : 86.6
         * totalShootHit : 307
         * averageShootHit : 38.3
         * percentShot : 40%
         * totalPunishBall : 226
         * averagePunishBall : 28.2
         * totalPunishBallHit : 149
         * averagePunishBallHit : 18.6
         * percentThrow : 60%
         * totalThreeMin : 235
         * averageThreeMin : 29.3
         * totalThreeMinHit : 235
         * averageThreeMinHit : 10.5
         * percentThree : 30%
         * attack : 78
         * defend : 259
         * helpattack : 177
         * rob : 79
         * cover : 30
         * foul : 196
         * misplay : 133
         */

        private int matchKind;
        private String season;
        private int winRoutine;
        private int loseRoutine;
        private int winOvertime;
        private int loseOvertime;
        private int totalShoot;
        private double averageShoot;
        private int totalShootHit;
        private double averageShootHit;
        private String percentShot;
        private int totalPunishBall;
        private double averagePunishBall;
        private int totalPunishBallHit;
        private double averagePunishBallHit;
        private String percentThrow;
        private int totalThreeMin;
        private double averageThreeMin;
        private int totalThreeMinHit;
        private double averageThreeMinHit;
        private String percentThree;
        private int attack;
        private int defend;
        private int helpattack;
        private int rob;
        private int cover;
        private int foul;
        private int misplay;

        public int getMatchKind() {
            return matchKind;
        }

        public void setMatchKind(int matchKind) {
            this.matchKind = matchKind;
        }

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public int getWinRoutine() {
            return winRoutine;
        }

        public void setWinRoutine(int winRoutine) {
            this.winRoutine = winRoutine;
        }

        public int getLoseRoutine() {
            return loseRoutine;
        }

        public void setLoseRoutine(int loseRoutine) {
            this.loseRoutine = loseRoutine;
        }

        public int getWinOvertime() {
            return winOvertime;
        }

        public void setWinOvertime(int winOvertime) {
            this.winOvertime = winOvertime;
        }

        public int getLoseOvertime() {
            return loseOvertime;
        }

        public void setLoseOvertime(int loseOvertime) {
            this.loseOvertime = loseOvertime;
        }

        public int getTotalShoot() {
            return totalShoot;
        }

        public void setTotalShoot(int totalShoot) {
            this.totalShoot = totalShoot;
        }

        public double getAverageShoot() {
            return averageShoot;
        }

        public void setAverageShoot(double averageShoot) {
            this.averageShoot = averageShoot;
        }

        public int getTotalShootHit() {
            return totalShootHit;
        }

        public void setTotalShootHit(int totalShootHit) {
            this.totalShootHit = totalShootHit;
        }

        public double getAverageShootHit() {
            return averageShootHit;
        }

        public void setAverageShootHit(double averageShootHit) {
            this.averageShootHit = averageShootHit;
        }

        public String getPercentShot() {
            return percentShot;
        }

        public void setPercentShot(String percentShot) {
            this.percentShot = percentShot;
        }

        public int getTotalPunishBall() {
            return totalPunishBall;
        }

        public void setTotalPunishBall(int totalPunishBall) {
            this.totalPunishBall = totalPunishBall;
        }

        public double getAveragePunishBall() {
            return averagePunishBall;
        }

        public void setAveragePunishBall(double averagePunishBall) {
            this.averagePunishBall = averagePunishBall;
        }

        public int getTotalPunishBallHit() {
            return totalPunishBallHit;
        }

        public void setTotalPunishBallHit(int totalPunishBallHit) {
            this.totalPunishBallHit = totalPunishBallHit;
        }

        public double getAveragePunishBallHit() {
            return averagePunishBallHit;
        }

        public void setAveragePunishBallHit(double averagePunishBallHit) {
            this.averagePunishBallHit = averagePunishBallHit;
        }

        public String getPercentThrow() {
            return percentThrow;
        }

        public void setPercentThrow(String percentThrow) {
            this.percentThrow = percentThrow;
        }

        public int getTotalThreeMin() {
            return totalThreeMin;
        }

        public void setTotalThreeMin(int totalThreeMin) {
            this.totalThreeMin = totalThreeMin;
        }

        public double getAverageThreeMin() {
            return averageThreeMin;
        }

        public void setAverageThreeMin(double averageThreeMin) {
            this.averageThreeMin = averageThreeMin;
        }

        public int getTotalThreeMinHit() {
            return totalThreeMinHit;
        }

        public void setTotalThreeMinHit(int totalThreeMinHit) {
            this.totalThreeMinHit = totalThreeMinHit;
        }

        public double getAverageThreeMinHit() {
            return averageThreeMinHit;
        }

        public void setAverageThreeMinHit(double averageThreeMinHit) {
            this.averageThreeMinHit = averageThreeMinHit;
        }

        public String getPercentThree() {
            return percentThree;
        }

        public void setPercentThree(String percentThree) {
            this.percentThree = percentThree;
        }

        public int getAttack() {
            return attack;
        }

        public void setAttack(int attack) {
            this.attack = attack;
        }

        public int getDefend() {
            return defend;
        }

        public void setDefend(int defend) {
            this.defend = defend;
        }

        public int getHelpattack() {
            return helpattack;
        }

        public void setHelpattack(int helpattack) {
            this.helpattack = helpattack;
        }

        public int getRob() {
            return rob;
        }

        public void setRob(int rob) {
            this.rob = rob;
        }

        public int getCover() {
            return cover;
        }

        public void setCover(int cover) {
            this.cover = cover;
        }

        public int getFoul() {
            return foul;
        }

        public void setFoul(int foul) {
            this.foul = foul;
        }

        public int getMisplay() {
            return misplay;
        }

        public void setMisplay(int misplay) {
            this.misplay = misplay;
        }
    }
    public static class RegularListBean {
        /**
         * matchKind : 1
         * season : 16-17
         * winRoutine : 25
         * loseRoutine : 56
         * winOvertime : 1
         * loseOvertime : 0
         * totalShoot : 7164
         * averageShoot : 87.3
         * totalShootHit : 3224
         * averageShootHit : 39.3
         * percentShot : 40%
         * totalPunishBall : 1853
         * averagePunishBall : 22.5
         * totalPunishBallHit : 1397
         * averagePunishBallHit : 17.0
         * percentThrow : 70%
         * totalThreeMin : 2107
         * averageThreeMin : 25.6
         * totalThreeMinHit : 2107
         * averageThreeMinHit : 8.9
         * percentThree : 30%
         * attack : 934
         * defend : 2635
         * helpattack : 1716
         * rob : 676
         * cover : 324
         * foul : 1698
         * misplay : 1192
         */

        private int matchKind;
        private String season;
        private int winRoutine;
        private int loseRoutine;
        private int winOvertime;
        private int loseOvertime;
        private int totalShoot;
        private double averageShoot;
        private int totalShootHit;
        private double averageShootHit;
        private String percentShot;
        private int totalPunishBall;
        private double averagePunishBall;
        private int totalPunishBallHit;
        private double averagePunishBallHit;
        private String percentThrow;
        private int totalThreeMin;
        private double averageThreeMin;
        private int totalThreeMinHit;
        private double averageThreeMinHit;
        private String percentThree;
        private int attack;
        private int defend;
        private int helpattack;
        private int rob;
        private int cover;
        private int foul;
        private int misplay;

        public int getMatchKind() {
            return matchKind;
        }

        public void setMatchKind(int matchKind) {
            this.matchKind = matchKind;
        }

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public int getWinRoutine() {
            return winRoutine;
        }

        public void setWinRoutine(int winRoutine) {
            this.winRoutine = winRoutine;
        }

        public int getLoseRoutine() {
            return loseRoutine;
        }

        public void setLoseRoutine(int loseRoutine) {
            this.loseRoutine = loseRoutine;
        }

        public int getWinOvertime() {
            return winOvertime;
        }

        public void setWinOvertime(int winOvertime) {
            this.winOvertime = winOvertime;
        }

        public int getLoseOvertime() {
            return loseOvertime;
        }

        public void setLoseOvertime(int loseOvertime) {
            this.loseOvertime = loseOvertime;
        }

        public int getTotalShoot() {
            return totalShoot;
        }

        public void setTotalShoot(int totalShoot) {
            this.totalShoot = totalShoot;
        }

        public double getAverageShoot() {
            return averageShoot;
        }

        public void setAverageShoot(double averageShoot) {
            this.averageShoot = averageShoot;
        }

        public int getTotalShootHit() {
            return totalShootHit;
        }

        public void setTotalShootHit(int totalShootHit) {
            this.totalShootHit = totalShootHit;
        }

        public double getAverageShootHit() {
            return averageShootHit;
        }

        public void setAverageShootHit(double averageShootHit) {
            this.averageShootHit = averageShootHit;
        }

        public String getPercentShot() {
            return percentShot;
        }

        public void setPercentShot(String percentShot) {
            this.percentShot = percentShot;
        }

        public int getTotalPunishBall() {
            return totalPunishBall;
        }

        public void setTotalPunishBall(int totalPunishBall) {
            this.totalPunishBall = totalPunishBall;
        }

        public double getAveragePunishBall() {
            return averagePunishBall;
        }

        public void setAveragePunishBall(double averagePunishBall) {
            this.averagePunishBall = averagePunishBall;
        }

        public int getTotalPunishBallHit() {
            return totalPunishBallHit;
        }

        public void setTotalPunishBallHit(int totalPunishBallHit) {
            this.totalPunishBallHit = totalPunishBallHit;
        }

        public double getAveragePunishBallHit() {
            return averagePunishBallHit;
        }

        public void setAveragePunishBallHit(double averagePunishBallHit) {
            this.averagePunishBallHit = averagePunishBallHit;
        }

        public String getPercentThrow() {
            return percentThrow;
        }

        public void setPercentThrow(String percentThrow) {
            this.percentThrow = percentThrow;
        }

        public int getTotalThreeMin() {
            return totalThreeMin;
        }

        public void setTotalThreeMin(int totalThreeMin) {
            this.totalThreeMin = totalThreeMin;
        }

        public double getAverageThreeMin() {
            return averageThreeMin;
        }

        public void setAverageThreeMin(double averageThreeMin) {
            this.averageThreeMin = averageThreeMin;
        }

        public int getTotalThreeMinHit() {
            return totalThreeMinHit;
        }

        public void setTotalThreeMinHit(int totalThreeMinHit) {
            this.totalThreeMinHit = totalThreeMinHit;
        }

        public double getAverageThreeMinHit() {
            return averageThreeMinHit;
        }

        public void setAverageThreeMinHit(double averageThreeMinHit) {
            this.averageThreeMinHit = averageThreeMinHit;
        }

        public String getPercentThree() {
            return percentThree;
        }

        public void setPercentThree(String percentThree) {
            this.percentThree = percentThree;
        }

        public int getAttack() {
            return attack;
        }

        public void setAttack(int attack) {
            this.attack = attack;
        }

        public int getDefend() {
            return defend;
        }

        public void setDefend(int defend) {
            this.defend = defend;
        }

        public int getHelpattack() {
            return helpattack;
        }

        public void setHelpattack(int helpattack) {
            this.helpattack = helpattack;
        }

        public int getRob() {
            return rob;
        }

        public void setRob(int rob) {
            this.rob = rob;
        }

        public int getCover() {
            return cover;
        }

        public void setCover(int cover) {
            this.cover = cover;
        }

        public int getFoul() {
            return foul;
        }

        public void setFoul(int foul) {
            this.foul = foul;
        }

        public int getMisplay() {
            return misplay;
        }

        public void setMisplay(int misplay) {
            this.misplay = misplay;
        }
    }
}
