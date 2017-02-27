package com.hhly.mlottery.bean.snookerbean;

import java.util.List;

/**
 * @author: Wangg
 * @Name：SnookerRankBean
 * @Description:
 * @Created on:2017/2/20  10:37.
 */

public class SnookerRankBean {
    /**
     * result : 200
     * worldRankingList : [{"index":1,"year":2017,"playerId":"319641","name":"马克·塞尔比","totalBonus":994942,"ptc":"525","totalIntegral":"931275","lastYearIntegral":"374400","twoYearsAgoIntegral":"438875","threeYearsAgoIntegral":"118000"},{"index":2,"year":2017,"playerId":"319643","name":"宾汉姆","totalBonus":517012,"ptc":"2250","totalIntegral":"515845","lastYearIntegral":"94087","twoYearsAgoIntegral":"89675","threeYearsAgoIntegral":"332083"},{"index":3,"year":2017,"playerId":"319626","name":"约翰·希金斯","totalBonus":484892,"ptc":"2250","totalIntegral":"453892","lastYearIntegral":"88500","twoYearsAgoIntegral":"260975","threeYearsAgoIntegral":"104417"},{"index":4,"year":2017,"playerId":"319631","name":"丁俊晖","totalBonus":444925,"ptc":"3680","totalIntegral":"442425","lastYearIntegral":"170000","twoYearsAgoIntegral":"215425","threeYearsAgoIntegral":"57000"},{"index":5,"year":2017,"playerId":"319645","name":"贾德·特鲁姆普","totalBonus":463166,"ptc":"4500","totalIntegral":"436333","lastYearIntegral":"141750","twoYearsAgoIntegral":"192250","threeYearsAgoIntegral":"102333"},{"index":6,"year":2017,"playerId":"319637","name":"肖恩·墨菲","totalBonus":446767,"ptc":"12320","totalIntegral":"416600","lastYearIntegral":"85125","twoYearsAgoIntegral":"180475","threeYearsAgoIntegral":"151000"},{"index":7,"year":2017,"playerId":"319627","name":"尼尔·罗伯特森","totalBonus":0,"ptc":"37500","totalIntegral":"359958","lastYearIntegral":"90125","twoYearsAgoIntegral":"204000","threeYearsAgoIntegral":"65833"},{"index":8,"year":2017,"playerId":"319634","name":"马克·艾伦","totalBonus":300592,"ptc":"4240","totalIntegral":"293425","lastYearIntegral":"45725","twoYearsAgoIntegral":"218200","threeYearsAgoIntegral":"29500"},{"index":9,"year":2017,"playerId":"321011","name":"Joe Perry","totalBonus":306800,"ptc":"525","totalIntegral":"290633","lastYearIntegral":"73550","twoYearsAgoIntegral":"79250","threeYearsAgoIntegral":"137833"},{"index":10,"year":2017,"playerId":"319619","name":"巴里·霍金斯","totalBonus":271025,"ptc":"6640","totalIntegral":"265525","lastYearIntegral":"83000","twoYearsAgoIntegral":"104525","threeYearsAgoIntegral":"78000"},{"index":11,"year":2017,"playerId":"319664","name":"阿里斯特·卡特","totalBonus":256825,"ptc":"2250","totalIntegral":"257325","lastYearIntegral":"136275","twoYearsAgoIntegral":"93050","threeYearsAgoIntegral":"28000"},{"index":12,"year":2017,"playerId":"319628","name":"傅家俊","totalBonus":323375,"ptc":"525","totalIntegral":"255125","lastYearIntegral":"60150","twoYearsAgoIntegral":"153475","threeYearsAgoIntegral":"41500"},{"index":14,"year":2017,"playerId":"319670","name":"梁文博","totalBonus":277667,"ptc":"6400","totalIntegral":"251000","lastYearIntegral":"107400","twoYearsAgoIntegral":"126100","threeYearsAgoIntegral":"17500"},{"index":15,"year":2017,"playerId":"319652","name":"K.威尔逊","totalBonus":246925,"ptc":"2560","totalIntegral":"242425","lastYearIntegral":"57025","twoYearsAgoIntegral":"175400","threeYearsAgoIntegral":"10000"},{"index":16,"year":2017,"playerId":"319647","name":"马克·威廉姆斯","totalBonus":247975,"ptc":"11250","totalIntegral":"239975","lastYearIntegral":"67750","twoYearsAgoIntegral":"82725","threeYearsAgoIntegral":"89500"},{"index":18,"year":2017,"playerId":"319650","name":"安东尼.麦克吉尔","totalBonus":195125,"ptc":"4500","totalIntegral":"190125","lastYearIntegral":"96775","twoYearsAgoIntegral":"44350","threeYearsAgoIntegral":"49000"},{"index":19,"year":2017,"playerId":"319657","name":"戴维·吉尔伯特","totalBonus":185417,"ptc":"2250","totalIntegral":"185917","lastYearIntegral":"54900","twoYearsAgoIntegral":"113100","threeYearsAgoIntegral":"17917"},{"index":20,"year":2017,"playerId":"319635","name":"马丁·古尔德","totalBonus":187059,"ptc":"1050","totalIntegral":"185892","lastYearIntegral":"28550","twoYearsAgoIntegral":"140425","threeYearsAgoIntegral":"16917"},{"index":21,"year":2017,"playerId":"319636","name":"迈克尔·怀特","totalBonus":173175,"ptc":"3440","totalIntegral":"172425","lastYearIntegral":"42225","twoYearsAgoIntegral":"58700","threeYearsAgoIntegral":"71500"},{"index":22,"year":2017,"playerId":"319620","name":"马克·金","totalBonus":176550,"ptc":"525","totalIntegral":"172283","lastYearIntegral":"89550","twoYearsAgoIntegral":"56150","threeYearsAgoIntegral":"26583"}]
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
         * index : 1
         * year : 2017
         * playerId : 319641
         * name : 马克·塞尔比
         * totalBonus : 994942
         * ptc : 525
         * totalIntegral : 931275
         * lastYearIntegral : 374400
         * twoYearsAgoIntegral : 438875
         * threeYearsAgoIntegral : 118000
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
