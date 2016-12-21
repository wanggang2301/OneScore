package com.hhly.mlottery.bean.snookerbean;

/**
 * Created by yixq on 2016/11/23.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerMatchOddsBean {

//    standard（标准盘）
//    bigsmall（大小盘）
//    allow（让球盘）
//    onetwo（单双）
//    onlywin （独赢）
    /**
     * 赔率外层
     */
    private SnookerMatchOddsDetailsBean standard;
    private SnookerMatchOddsDetailsBean bigsmall;
    private SnookerMatchOddsDetailsBean allow;
    private SnookerMatchOddsDetailsBean onetwo;
    private SnookerMatchOddsDetailsBean onlywin;

    public SnookerMatchOddsDetailsBean getStandard()
    {
        return standard;
    }

    public void setStandard(SnookerMatchOddsDetailsBean standard) {
        this.standard = standard;
    }

    public SnookerMatchOddsDetailsBean getBigsmall() {
        return bigsmall;
    }

    public void setBigsmall(SnookerMatchOddsDetailsBean bigsmall) {
        this.bigsmall = bigsmall;
    }

    public SnookerMatchOddsDetailsBean getAllow() {
        return allow;
    }

    public void setAllow(SnookerMatchOddsDetailsBean allow) {
        this.allow = allow;
    }

    public SnookerMatchOddsDetailsBean getOnetwo() {
        return onetwo;
    }

    public void setOnetwo(SnookerMatchOddsDetailsBean onetwo) {
        this.onetwo = onetwo;
    }

    public SnookerMatchOddsDetailsBean getOnlywin() {
        return onlywin;
    }

    public void setOnlywin(SnookerMatchOddsDetailsBean onlywin) {
        this.onlywin = onlywin;
    }

    public static class SnookerMatchOddsDetailsBean{

//        lj 利记
//        hb 浩博
//        sb 沙巴
//        xyy 雪缘园
        /**
         * 赔率公司
         */
        private SnookerMatchOddsDataBean lj;
        private SnookerMatchOddsDataBean hb;
        private SnookerMatchOddsDataBean sb;
        private SnookerMatchOddsDataBean xyy;

        public SnookerMatchOddsDataBean getXyy() {
            return xyy;
        }

        public void setXyy(SnookerMatchOddsDataBean xyy) {
            this.xyy = xyy;
        }

        public SnookerMatchOddsDataBean getSb() {
            return sb;
        }

        public void setSb(SnookerMatchOddsDataBean sb) {
            this.sb = sb;
        }

        public SnookerMatchOddsDataBean getLj() {
            return lj;
        }

        public void setLj(SnookerMatchOddsDataBean lj) {
            this.lj = lj;
        }

        public SnookerMatchOddsDataBean getHb() {
            return hb;
        }

        public void setHb(SnookerMatchOddsDataBean hb) {
            this.hb = hb;
        }

        public static class SnookerMatchOddsDataBean{

//            "mainDisk": 0,
//            "handicapValue": "2.5",
//            "leftOdds": "0.1",
//            "rightOdds": "5.0"
            /**
             * 赔率内容
             */
            private String mainDisk;
            private String handicapValue;
            private String leftOdds;
            private String rightOdds;

            public String getMainDisk() {
                return mainDisk;
            }

            public void setMainDisk(String mainDisk) {
                this.mainDisk = mainDisk;
            }

            public String getHandicapValue() {
                return handicapValue;
            }

            public void setHandicapValue(String handicapValue) {
                this.handicapValue = handicapValue;
            }

            public String getLeftOdds() {
                return leftOdds;
            }

            public void setLeftOdds(String leftOdds) {
                this.leftOdds = leftOdds;
            }

            public String getRightOdds() {
                return rightOdds;
            }

            public void setRightOdds(String rightOdds) {
                this.rightOdds = rightOdds;
            }

        }


    }

}
