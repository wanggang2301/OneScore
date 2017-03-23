package com.hhly.mlottery.bean.basket.index;

import java.util.List;

/**
 * @author: Wangg
 * @name：BasketIndexDetailsBean
 * @description: 篮球指数详情Bean
 * @created on:2017/3/22  14:32.
 */

public class BasketIndexDetailsBean {

    private String oddsId;
    private List<OddsDataBean> oddsData;
    private List<ComListsBean> comLists;

    public String getOddsId() {
        return oddsId;
    }

    public void setOddsId(String oddsId) {
        this.oddsId = oddsId;
    }

    public List<OddsDataBean> getOddsData() {
        return oddsData;
    }

    public void setOddsData(List<OddsDataBean> oddsData) {
        this.oddsData = oddsData;
    }

    public List<ComListsBean> getComLists() {
        return comLists;
    }

    public void setComLists(List<ComListsBean> comLists) {
        this.comLists = comLists;
    }

    public static class OddsDataBean {

        private String updateTime;
        private String leftOdds;
        private int leftOddsTrend;
        private String rightOdds;
        private int rightOddsTrend;
        private String handicapValue;
        private int handicapValueTrend;


        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getLeftOdds() {
            return leftOdds;
        }

        public void setLeftOdds(String leftOdds) {
            this.leftOdds = leftOdds;
        }

        public int getLeftOddsTrend() {
            return leftOddsTrend;
        }

        public void setLeftOddsTrend(int leftOddsTrend) {
            this.leftOddsTrend = leftOddsTrend;
        }

        public String getRightOdds() {
            return rightOdds;
        }

        public void setRightOdds(String rightOdds) {
            this.rightOdds = rightOdds;
        }

        public int getRightOddsTrend() {
            return rightOddsTrend;
        }

        public void setRightOddsTrend(int rightOddsTrend) {
            this.rightOddsTrend = rightOddsTrend;
        }

        public String getHandicapValue() {
            return handicapValue;
        }

        public void setHandicapValue(String handicapValue) {
            this.handicapValue = handicapValue;
        }

        public int getHandicapValueTrend() {
            return handicapValueTrend;
        }

        public void setHandicapValueTrend(int handicapValueTrend) {
            this.handicapValueTrend = handicapValueTrend;
        }
    }

    public static class ComListsBean {
        /**
         * comId : 6
         * comName : 皇冠
         */

        private String comId;
        private String comName;

        public String getComId() {
            return comId;
        }

        public void setComId(String comId) {
            this.comId = comId;
        }

        public String getComName() {
            return comName;
        }

        public void setComName(String comName) {
            this.comName = comName;
        }
    }
}
