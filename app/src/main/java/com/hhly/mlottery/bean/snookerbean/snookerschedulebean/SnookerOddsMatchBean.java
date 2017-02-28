package com.hhly.mlottery.bean.snookerbean.snookerschedulebean;

/**
 * Created by yixq on 2017/2/18.
 * mail：yixq@13322.com
 * describe: snooker 赔率实体bean
 */

public class SnookerOddsMatchBean {
//    oneTwo: {},
//    asiaSize: {},
//    onlyWin: {},
//    asiaLet: {}
    /**
     * 赔率外层
     */
    private SnookerMatchOddsDetailsBean oneTwo;//单双
    private SnookerMatchOddsDetailsBean asiaSize;//大小球
    private SnookerMatchOddsDetailsBean onlyWin;//独赢(欧赔)
    private SnookerMatchOddsDetailsBean asiaLet;//亚盘

    public SnookerMatchOddsDetailsBean getOneTwo() {
        return oneTwo;
    }

    public void setOneTwo(SnookerMatchOddsDetailsBean oneTwo) {
        this.oneTwo = oneTwo;
    }

    public SnookerMatchOddsDetailsBean getAsiaSize() {
        return asiaSize;
    }

    public void setAsiaSize(SnookerMatchOddsDetailsBean asiaSize) {
        this.asiaSize = asiaSize;
    }

    public SnookerMatchOddsDetailsBean getOnlyWin() {
        return onlyWin;
    }

    public void setOnlyWin(SnookerMatchOddsDetailsBean onlyWin) {
        this.onlyWin = onlyWin;
    }

    public SnookerMatchOddsDetailsBean getAsiaLet() {
        return asiaLet;
    }

    public void setAsiaLet(SnookerMatchOddsDetailsBean asiaLet) {
        this.asiaLet = asiaLet;
    }

    public static class SnookerMatchOddsDetailsBean{
        /**
         * 赔率公司
         */
//        SB,SBO, IBC,ISN,VinBet
        private SnookerMatchOddsDataBean SBO;//浩博
        private SnookerMatchOddsDataBean VinBet;//利记
        private SnookerMatchOddsDataBean SB;//沙巴
        private SnookerMatchOddsDataBean xyy;//雪缘圆
        private SnookerMatchOddsDataBean IBC;
        private SnookerMatchOddsDataBean ISN;

        public SnookerMatchOddsDataBean getSBO() {
            return SBO;
        }

        public void setSBO(SnookerMatchOddsDataBean SBO) {
            this.SBO = SBO;
        }

        public SnookerMatchOddsDataBean getVinBet() {
            return VinBet;
        }

        public void setVinBet(SnookerMatchOddsDataBean vinBet) {
            VinBet = vinBet;
        }

        public SnookerMatchOddsDataBean getSB() {
            return SB;
        }

        public void setSB(SnookerMatchOddsDataBean SB) {
            this.SB = SB;
        }

        public SnookerMatchOddsDataBean getXyy() {
            return xyy;
        }

        public void setXyy(SnookerMatchOddsDataBean xyy) {
            this.xyy = xyy;
        }

        public SnookerMatchOddsDataBean getIBC() {
            return IBC;
        }

        public void setIBC(SnookerMatchOddsDataBean IBC) {
            this.IBC = IBC;
        }

        public SnookerMatchOddsDataBean getISN() {
            return ISN;
        }

        public void setISN(SnookerMatchOddsDataBean ISN) {
            this.ISN = ISN;
        }

        public static class SnookerMatchOddsDataBean{

//            handicap: "asiaLet",
//            handicapValue: "7.5",
//            leftOdds: "0.97",
//            rightOdds: "0.85"
            /**
             * 赔率内容
             */
            private String handicap;
            private String handicapValue;
            private String leftOdds;
            private String rightOdds;

            public String getHandicap() {
                return handicap;
            }

            public void setHandicap(String handicap) {
                this.handicap = handicap;
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
