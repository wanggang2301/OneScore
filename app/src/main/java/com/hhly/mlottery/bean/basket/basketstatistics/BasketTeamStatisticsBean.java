package com.hhly.mlottery.bean.basket.basketstatistics;

/**
 * 描    述：篮球球队数据
 * 作    者：mady@13322.com
 * 时    间：2016/11/18
 */
public class BasketTeamStatisticsBean {

    /**
     * result : 200
     * data : {"homeTeamStats":{"shootHitRate":53,"threePointShotHitRate":56,"freeThrowHitRate":81,"rebound":43,"assist":32,"foul":22,"steal":17,"turnover":11,"blockShot":6},"guestTeamStats":{"shootHitRate":44,"threePointShotHitRate":30,"freeThrowHitRate":77,"rebound":37,"assist":18,"foul":21,"steal":6,"turnover":21,"blockShot":2}}
     */

    private int result;
    /**
     * homeTeamStats : {"shootHitRate":53,"threePointShotHitRate":56,"freeThrowHitRate":81,"rebound":43,"assist":32,"foul":22,"steal":17,"turnover":11,"blockShot":6}
     * guestTeamStats : {"shootHitRate":44,"threePointShotHitRate":30,"freeThrowHitRate":77,"rebound":37,"assist":18,"foul":21,"steal":6,"turnover":21,"blockShot":2}
     */

    private DataEntity data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * shootHitRate : 53
         * threePointShotHitRate : 56
         * freeThrowHitRate : 81
         * rebound : 43
         * assist : 32
         * foul : 22
         * steal : 17
         * turnover : 11
         * blockShot : 6
         */

        private HomeTeamStatsEntity homeTeamStats;
        /**
         * shootHitRate : 44
         * threePointShotHitRate : 30
         * freeThrowHitRate : 77
         * rebound : 37
         * assist : 18
         * foul : 21
         * steal : 6
         * turnover : 21
         * blockShot : 2
         */

        private GuestTeamStatsEntity guestTeamStats;

        public HomeTeamStatsEntity getHomeTeamStats() {
            return homeTeamStats;
        }

        public void setHomeTeamStats(HomeTeamStatsEntity homeTeamStats) {
            this.homeTeamStats = homeTeamStats;
        }

        public GuestTeamStatsEntity getGuestTeamStats() {
            return guestTeamStats;
        }

        public void setGuestTeamStats(GuestTeamStatsEntity guestTeamStats) {
            this.guestTeamStats = guestTeamStats;
        }

        public static class HomeTeamStatsEntity  {
            private int shootHitRate;
            private int threePointShotHitRate;
            private int freeThrowHitRate;
            private int rebound;
            private int assist;
            private int foul;
            private int steal;
            private int turnover;
            private int blockShot;

            public int getShootHitRate() {
                return shootHitRate;
            }

            public void setShootHitRate(int shootHitRate) {
                this.shootHitRate = shootHitRate;
            }

            public int getThreePointShotHitRate() {
                return threePointShotHitRate;
            }

            public void setThreePointShotHitRate(int threePointShotHitRate) {
                this.threePointShotHitRate = threePointShotHitRate;
            }

            public int getFreeThrowHitRate() {
                return freeThrowHitRate;
            }

            public void setFreeThrowHitRate(int freeThrowHitRate) {
                this.freeThrowHitRate = freeThrowHitRate;
            }

            public int getRebound() {
                return rebound;
            }

            public void setRebound(int rebound) {
                this.rebound = rebound;
            }

            public int getAssist() {
                return assist;
            }

            public void setAssist(int assist) {
                this.assist = assist;
            }

            public int getFoul() {
                return foul;
            }

            public void setFoul(int foul) {
                this.foul = foul;
            }

            public int getSteal() {
                return steal;
            }

            public void setSteal(int steal) {
                this.steal = steal;
            }

            public int getTurnover() {
                return turnover;
            }

            public void setTurnover(int turnover) {
                this.turnover = turnover;
            }

            public int getBlockShot() {
                return blockShot;
            }

            public void setBlockShot(int blockShot) {
                this.blockShot = blockShot;
            }
        }

        public static class GuestTeamStatsEntity  {
            private int shootHitRate;
            private int threePointShotHitRate;
            private int freeThrowHitRate;
            private int rebound;
            private int assist;
            private int foul;
            private int steal;
            private int turnover;
            private int blockShot;

            public int getShootHitRate() {
                return shootHitRate;
            }

            public void setShootHitRate(int shootHitRate) {
                this.shootHitRate = shootHitRate;
            }

            public int getThreePointShotHitRate() {
                return threePointShotHitRate;
            }

            public void setThreePointShotHitRate(int threePointShotHitRate) {
                this.threePointShotHitRate = threePointShotHitRate;
            }

            public int getFreeThrowHitRate() {
                return freeThrowHitRate;
            }

            public void setFreeThrowHitRate(int freeThrowHitRate) {
                this.freeThrowHitRate = freeThrowHitRate;
            }

            public int getRebound() {
                return rebound;
            }

            public void setRebound(int rebound) {
                this.rebound = rebound;
            }

            public int getAssist() {
                return assist;
            }

            public void setAssist(int assist) {
                this.assist = assist;
            }

            public int getFoul() {
                return foul;
            }

            public void setFoul(int foul) {
                this.foul = foul;
            }

            public int getSteal() {
                return steal;
            }

            public void setSteal(int steal) {
                this.steal = steal;
            }

            public int getTurnover() {
                return turnover;
            }

            public void setTurnover(int turnover) {
                this.turnover = turnover;
            }

            public int getBlockShot() {
                return blockShot;
            }

            public void setBlockShot(int blockShot) {
                this.blockShot = blockShot;
            }
        }
    }
}
