package com.hhly.mlottery.bean.basket.infomation;

import java.util.List;

/**
 * @author: Wangg
 * @Name：SnookerPlayerBean
 * @Description:
 * @Created on:2017/2/22  10:28.
 */

public class SnookerPlayerBean {
    /**
     * result : 200
     * worldRankingList : [{"index":4,"year":2017,"playerId":"319631","name":"丁俊晖","totalBonus":449925,"ptc":"3680","totalIntegral":"442425","lastYearIntegral":"170000","twoYearsAgoIntegral":"215425","threeYearsAgoIntegral":"57000"},{"index":20,"year":2017,"playerId":"319635","name":"马丁·古尔德","totalBonus":202892,"ptc":"1050","totalIntegral":"185892","lastYearIntegral":"28550","twoYearsAgoIntegral":"140425","threeYearsAgoIntegral":"16917"}]
     */

    private int result;
    private List<WorldRankingListBean> worldRankingList;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<WorldRankingListBean> getWorldRankingList() {
        return worldRankingList;
    }

    public void setWorldRankingList(List<WorldRankingListBean> worldRankingList) {
        this.worldRankingList = worldRankingList;
    }

    public static class WorldRankingListBean {
        /**
         * index : 4
         * year : 2017
         * playerId : 319631
         * name : 丁俊晖
         * totalBonus : 449925
         * ptc : 3680
         * totalIntegral : 442425
         * lastYearIntegral : 170000
         * twoYearsAgoIntegral : 215425
         * threeYearsAgoIntegral : 57000
         */

        private int index;
        private int year;
        private String playerId;
        private String name;
        private int totalBonus;
        private String ptc;
        private String totalIntegral;
        private String lastYearIntegral;
        private String twoYearsAgoIntegral;
        private String threeYearsAgoIntegral;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public String getPlayerId() {
            return playerId;
        }

        public void setPlayerId(String playerId) {
            this.playerId = playerId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTotalBonus() {
            return totalBonus;
        }

        public void setTotalBonus(int totalBonus) {
            this.totalBonus = totalBonus;
        }

        public String getPtc() {
            return ptc;
        }

        public void setPtc(String ptc) {
            this.ptc = ptc;
        }

        public String getTotalIntegral() {
            return totalIntegral;
        }

        public void setTotalIntegral(String totalIntegral) {
            this.totalIntegral = totalIntegral;
        }

        public String getLastYearIntegral() {
            return lastYearIntegral;
        }

        public void setLastYearIntegral(String lastYearIntegral) {
            this.lastYearIntegral = lastYearIntegral;
        }

        public String getTwoYearsAgoIntegral() {
            return twoYearsAgoIntegral;
        }

        public void setTwoYearsAgoIntegral(String twoYearsAgoIntegral) {
            this.twoYearsAgoIntegral = twoYearsAgoIntegral;
        }

        public String getThreeYearsAgoIntegral() {
            return threeYearsAgoIntegral;
        }

        public void setThreeYearsAgoIntegral(String threeYearsAgoIntegral) {
            this.threeYearsAgoIntegral = threeYearsAgoIntegral;
        }
    }
}
