package com.hhly.mlottery.bean.intelligence;

import java.util.List;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/7/19.
 */
public class BigDataResult {

    private int result;
    private BigDataForecast bigDataForecast;
    private GroundEntity ground;
    private AllEntity all;
    private List<String> handicaps;

    public GroundEntity getGround() {
        return ground;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public BigDataForecast getBigDataForecast() {
        return bigDataForecast;
    }

    public void setBigDataForecast(BigDataForecast bigDataForecast) {
        this.bigDataForecast = bigDataForecast;
    }

    public void setGround(GroundEntity ground) {
        this.ground = ground;
    }

    public AllEntity getAll() {
        return all;
    }

    public void setAll(AllEntity all) {
        this.all = all;
    }

    public List<String> getHandicaps() {
        return handicaps;
    }

    public void setHandicaps(List<String> handicaps) {
        this.handicaps = handicaps;
    }
    public static class GroundEntity {
        /**
         * home : 75.00%
         * guest : 50.00%
         * type : 3
         */

        public List<GridViewEntity> fullResultList;
        /**
         * home : 83.33%
         * guest : 66.67%
         * type : 3
         */

        public List<GridViewEntity> halfResultList;
        /**
         * home : 25.00%
         * guest : 7.69%
         * type : 7
         */

        private List<GridViewEntity> goalResultList;
        /**
         * home : 83.33%
         * guest : 42.86%
         * type : 12
         */

        private List<GridViewEntity> fullHandicapList;
        private List<GridViewEntity> halfHandicapList;
        /**
         * home : 9
         * guest : 1
         * type : 22
         */

        private List<GridViewEntity> fullRecentList;
        /**
         * home : 9
         * guest : 3
         * type : 22
         */

        private List<GridViewEntity> halfRecentList;
        private List<GridViewEntity> todayFullHandicapList;
        private List<GridViewEntity> todayHalfHandicapList;

        public List<GridViewEntity> getFullResultList() {
            return fullResultList;
        }

        public void setFullResultList(List<GridViewEntity> fullResultList) {
            this.fullResultList = fullResultList;
        }

        public List<GridViewEntity> getHalfResultList() {
            return halfResultList;
        }

        public void setHalfResultList(List<GridViewEntity> halfResultList) {
            this.halfResultList = halfResultList;
        }

        public List<GridViewEntity> getGoalResultList() {
            return goalResultList;
        }

        public void setGoalResultList(List<GridViewEntity> goalResultList) {
            this.goalResultList = goalResultList;
        }

        public List<GridViewEntity> getFullHandicapList() {
            return fullHandicapList;
        }

        public void setFullHandicapList(List<GridViewEntity> fullHandicapList) {
            this.fullHandicapList = fullHandicapList;
        }

        public List<GridViewEntity> getHalfHandicapList() {
            return halfHandicapList;
        }

        public void setHalfHandicapList(List<GridViewEntity> halfHandicapList) {
            this.halfHandicapList = halfHandicapList;
        }

        public List<GridViewEntity> getFullRecentList() {
            return fullRecentList;
        }

        public void setFullRecentList(List<GridViewEntity> fullRecentList) {
            this.fullRecentList = fullRecentList;
        }

        public List<GridViewEntity> getHalfRecentList() {
            return halfRecentList;
        }

        public void setHalfRecentList(List<GridViewEntity> halfRecentList) {
            this.halfRecentList = halfRecentList;
        }

        public List<GridViewEntity> getTodayFullHandicapList() {
            return todayFullHandicapList;
        }

        public void setTodayFullHandicapList(List<GridViewEntity> todayFullHandicapList) {
            this.todayFullHandicapList = todayFullHandicapList;
        }

        public List<GridViewEntity> getTodayHalfHandicapList() {
            return todayHalfHandicapList;
        }

        public void setTodayHalfHandicapList(List<GridViewEntity> todayHalfHandicapList) {
            this.todayHalfHandicapList = todayHalfHandicapList;
        }

    }

    public static class AllEntity {
        private List<GridViewEntity> fullResultList;
        private List<GridViewEntity> halfResultList;
        private List<GridViewEntity> goalResultList;
        private List<GridViewEntity> fullHandicapList;
        private List<GridViewEntity> halfHandicapList;
        private List<GridViewEntity> fullRecentList;
        private List<GridViewEntity> halfRecentList;
        private List<GridViewEntity> todayFullHandicapList;
        private List<GridViewEntity> todayHalfHandicapList;

        public List<GridViewEntity> getFullResultList() {
            return fullResultList;
        }

        public void setFullResultList(List<GridViewEntity> fullResultList) {
            this.fullResultList = fullResultList;
        }

        public List<GridViewEntity> getHalfResultList() {
            return halfResultList;
        }

        public void setHalfResultList(List<GridViewEntity> halfResultList) {
            this.halfResultList = halfResultList;
        }

        public List<GridViewEntity> getGoalResultList() {
            return goalResultList;
        }

        public void setGoalResultList(List<GridViewEntity> goalResultList) {
            this.goalResultList = goalResultList;
        }

        public List<GridViewEntity> getFullHandicapList() {
            return fullHandicapList;
        }

        public void setFullHandicapList(List<GridViewEntity> fullHandicapList) {
            this.fullHandicapList = fullHandicapList;
        }

        public List<GridViewEntity> getHalfHandicapList() {
            return halfHandicapList;
        }

        public void setHalfHandicapList(List<GridViewEntity> halfHandicapList) {
            this.halfHandicapList = halfHandicapList;
        }

        public List<GridViewEntity> getFullRecentList() {
            return fullRecentList;
        }

        public void setFullRecentList(List<GridViewEntity> fullRecentList) {
            this.fullRecentList = fullRecentList;
        }

        public List<GridViewEntity> getHalfRecentList() {
            return halfRecentList;
        }

        public void setHalfRecentList(List<GridViewEntity> halfRecentList) {
            this.halfRecentList = halfRecentList;
        }

        public List<GridViewEntity> getTodayFullHandicapList() {
            return todayFullHandicapList;
        }

        public void setTodayFullHandicapList(List<GridViewEntity> todayFullHandicapList) {
            this.todayFullHandicapList = todayFullHandicapList;
        }

        public List<GridViewEntity> getTodayHalfHandicapList() {
            return todayHalfHandicapList;
        }

        public void setTodayHalfHandicapList(List<GridViewEntity> todayHalfHandicapList) {
            this.todayHalfHandicapList = todayHalfHandicapList;
        }

    }

    public static class GridViewEntity {
        private String home;
        private String guest;
        private int type;
        private String handicap;

        public String getHandicap() {
            return handicap;
        }

        public void setHandicap(String handicap) {
            this.handicap = handicap;
        }

        public String getHome() {
            return home;
        }

        public void setHome(String home) {
            this.home = home;
        }

        public String getGuest() {
            return guest;
        }

        public void setGuest(String guest) {
            this.guest = guest;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

}
