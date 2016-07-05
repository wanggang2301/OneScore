package com.hhly.mlottery.bean.oddsbean;

import java.util.List;

/**
 * Created by 103TJL on 2016/3/9.
 * 博彩 指数详情
 */
public class OddsDetailsDataInfo {

    private int code;

    private List<DetailsEntity> details;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDetails(List<DetailsEntity> details) {
        this.details = details;
    }

    public List<DetailsEntity> getDetails() {
        return details;
    }

    /**
     * date : 2016-03-08
     * details : [{"time":"10:00","score":"1:0","homeOdd":2.01,"hand":2.5,"guestOdd":1.5},{"time":"10:00","score":"1:0","homeOdd":2.01,"hand":2.5,"guestOdd":1.5},{"time":"10:00","score":"1:0","homeOdd":2.01,"hand":2.5,"guestOdd":1.5}]
     */
    public static class DetailsEntity {
        private String date;
        /**
         * time : 10:00
         * score : 1:0
         * homeOdd : 2.01
         * hand : 2.5
         * guestOdd : 1.5
         */
        private List<DataDetailsEntity> details;

        public void setDate(String date) {
            this.date = date;
        }

        public void setDetails(List<DataDetailsEntity> details) {
            this.details = details;
        }

        public String getDate() {
            return date;
        }

        public List<DataDetailsEntity> getDetails() {
            return details;
        }

        public static class DataDetailsEntity {
            private String time;
            private String score;
            private double homeOdd;
            private double hand;
            private double guestOdd;
            private String homeColor = "black";
            private String guestColor = "black";
            private String dishColor = "black";
            private String selectTag;
            private boolean scoreVisible = false;

            public String getSelectTag() {
                return selectTag;
            }

            public void setSelectTag(String selectTag) {
                this.selectTag = selectTag;
            }

            public boolean isScoreVisible() {
                return scoreVisible;
            }

            public void setScoreVisible(boolean scoreVisible) {
                this.scoreVisible = scoreVisible;
            }

            public String getDishColor() {
                return dishColor;
            }

            public void setDishColor(String dishColor) {
                this.dishColor = dishColor;
            }

            public String getHomeColor() {
                return homeColor;
            }

            public void setHomeColor(String homeColor) {
                this.homeColor = homeColor;
            }

            public String getGuestColor() {
                return guestColor;
            }

            public void setGuestColor(String guestColor) {
                this.guestColor = guestColor;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public void setHomeOdd(double homeOdd) {
                this.homeOdd = homeOdd;
            }

            public void setHand(double hand) {
                this.hand = hand;
            }

            public void setGuestOdd(double guestOdd) {
                this.guestOdd = guestOdd;
            }

            public String getTime() {
                return time;
            }

            public String getScore() {
                return score;
            }

            public double getHomeOdd() {
                return homeOdd;
            }

            public double getHand() {
                return hand;
            }

            public double getGuestOdd() {
                return guestOdd;
            }
        }
    }
}
