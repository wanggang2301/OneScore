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
//        private SnookerMatchOddsDataBean lj;//利记
//        private SnookerMatchOddsDataBean hb;//浩博
        private SnookerMatchOddsDataBean SBO;//浩博
        private SnookerMatchOddsDataBean VINBET;//利记
        private SnookerMatchOddsDataBean sb;//沙巴
        private SnookerMatchOddsDataBean xyy;//雪缘圆

        public SnookerMatchOddsDataBean getSBO() {
            return SBO;
        }

        public void setSBO(SnookerMatchOddsDataBean SBO) {
            this.SBO = SBO;
        }

        public SnookerMatchOddsDataBean getVINBET() {
            return VINBET;
        }

        public void setVINBET(SnookerMatchOddsDataBean VINBET) {
            this.VINBET = VINBET;
        }

        public SnookerMatchOddsDataBean getSb() {
            return sb;
        }

        public void setSb(SnookerMatchOddsDataBean sb) {
            this.sb = sb;
        }

        public SnookerMatchOddsDataBean getXyy() {
            return xyy;
        }

        public void setXyy(SnookerMatchOddsDataBean xyy) {
            this.xyy = xyy;
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
