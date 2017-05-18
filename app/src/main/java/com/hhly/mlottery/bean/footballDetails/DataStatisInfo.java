package com.hhly.mlottery.bean.footballDetails;

/**
 * Created by 103TJL on 2015/12/31.
 * 统计数据实体类
 */
public class DataStatisInfo {
    /**
     * allShots : 5
     * shot : 1
     * trapping : 4
     * corner : 2
     * offside : 4
     * rc : 1
     * yc : 2
     * freeHit : 3
     */

    private HomeStatisEntity homeStatis;
    /**
     * homeStatis : {"allShots":5,"shot":1,"trapping":4,"corner":2,"offside":4,"rc":1,"yc":2,"freeHit":3}
     * result : 200
     * guestStatis : {"allShots":5,"shot":1,"trapping":4,"corner":2,"offside":4,"rc":1,"yc":2,"freeHit":3}
     */

    private String result;
    /**
     * allShots : 5
     * shot : 1
     * trapping : 4
     * corner : 2
     * offside : 4
     * rc : 1
     * yc : 2
     * freeHit : 3
     */

    private GuestStatisEntity guestStatis;

    public void setHomeStatis(HomeStatisEntity homeStatis) {
        this.homeStatis = homeStatis;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setGuestStatis(GuestStatisEntity guestStatis) {
        this.guestStatis = guestStatis;
    }

    public HomeStatisEntity getHomeStatis() {
        return homeStatis;
    }

    public String getResult() {
        return result;
    }

    public GuestStatisEntity getGuestStatis() {
        return guestStatis;
    }

    public static class HomeStatisEntity {
        private int allShots;//射门
        private int shot;//射正
        private int trapping;//扑救
        private int corner;//角球
        private int offside;//越位
        private int rc;//红牌
        private int yc;//黄牌
        private int freeHit;//任意球
        private int foul;//犯规
        private int lineOut;//界外球
        private int danger;

        public int getDanger() {
            return danger;
        }

        public void setDanger(int danger) {
            this.danger = danger;
        }

        public int getLineOut() {
            return lineOut;
        }

        public void setLineOut(int lineOut) {
            this.lineOut = lineOut;
        }

        public int getFoul() {
            return foul;
        }

        public void setFoul(int foul) {
            this.foul = foul;
        }

        public void setAllShots(int allShots) {
            this.allShots = allShots;
        }

        public void setShot(int shot) {
            this.shot = shot;
        }

        public void setTrapping(int trapping) {
            this.trapping = trapping;
        }

        public void setCorner(int corner) {
            this.corner = corner;
        }

        public void setOffside(int offside) {
            this.offside = offside;
        }

        public void setRc(int rc) {
            this.rc = rc;
        }

        public void setYc(int yc) {
            this.yc = yc;
        }

        public void setFreeHit(int freeHit) {
            this.freeHit = freeHit;
        }
        public int getAllShots() {
            return allShots;
        }

        public int getShot() {
            return shot;
        }

        public int getTrapping() {
            return trapping;
        }

        public int getCorner() {
            return corner;
        }

        public int getOffside() {
            return offside;
        }

        public int getRc() {
            return rc;
        }

        public int getYc() {
            return yc;
        }

        public int getFreeHit() {
            return freeHit;
        }
    }

    public static class GuestStatisEntity {
        private int allShots;//射门
        private int shot;//射正
        private int trapping;//扑救
        private int corner;//角球
        private int offside;//越位
        private int rc;//红牌
        private int yc;//黄牌
        private int freeHit;//任意球
        private int foul;//犯规
        private int lineOut;//界外球
        private int danger;

        public int getDanger() {
            return danger;
        }

        public void setDanger(int danger) {
            this.danger = danger;
        }

        public int getLineOut() {
            return lineOut;
        }

        public void setLineOut(int lineOut) {
            this.lineOut = lineOut;
        }

        public int getFoul() {
            return foul;
        }

        public void setFoul(int foul) {
            this.foul = foul;
        }

        public void setAllShots(int allShots) {
            this.allShots = allShots;
        }

        public void setShot(int shot) {
            this.shot = shot;
        }

        public void setTrapping(int trapping) {
            this.trapping = trapping;
        }

        public void setCorner(int corner) {
            this.corner = corner;
        }

        public void setOffside(int offside) {
            this.offside = offside;
        }

        public void setRc(int rc) {
            this.rc = rc;
        }

        public void setYc(int yc) {
            this.yc = yc;
        }

        public void setFreeHit(int freeHit) {
            this.freeHit = freeHit;
        }

        public int getAllShots() {
            return allShots;
        }

        public int getShot() {
            return shot;
        }

        public int getTrapping() {
            return trapping;
        }

        public int getCorner() {
            return corner;
        }

        public int getOffside() {
            return offside;
        }

        public int getRc() {
            return rc;
        }

        public int getYc() {
            return yc;
        }

        public int getFreeHit() {
            return freeHit;
        }
    }
}
