package com.hhly.mlottery.bean.oddsbean;

import android.os.Parcelable;

import java.util.List;

/**
 * Created by 103TJL on 2016/3/8.
 *指数，博彩数据
 */
public class OddsDataInfo{

    /**
     * id : 10
     * abb : HB
     * name : 浩博
     * details : [{"time":null,"score":null,"homeOdd":2.01,"hand":2.5,"guestOdd":1.5},{"time":null,"score":null,"homeOdd":3.01,"hand":4.5,"guestOdd":1.5}]
     */

    private List<ListOddEntity> listOdd;

    public void setListOdd(List<ListOddEntity> listOdd) {
        this.listOdd = listOdd;
    }

    public List<ListOddEntity> getListOdd() {
        return listOdd;
    }



    public static class ListOddEntity {
        private String id;
        private String abb;
        private String name;
        /**
         * time : null
         * score : null
         * homeOdd : 2.01
         * hand : 2.5
         * guestOdd : 1.5
         */

        private List<DetailsEntity> details;

        public void setId(String id) {
            this.id = id;
        }

        public void setAbb(String abb) {
            this.abb = abb;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDetails(List<DetailsEntity> details) {
            this.details = details;
        }

        public String getId() {
            return id;
        }

        public String getAbb() {
            return abb;
        }

        public String getName() {
            return name;
        }

        public List<DetailsEntity> getDetails() {
            return details;
        }

        public static class DetailsEntity {
            private Object time;
            private Object score;
            private double homeOdd;
            private double hand;
            private double guestOdd;

            public void setTime(Object time) {
                this.time = time;
            }

            public void setScore(Object score) {
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

            public Object getTime() {
                return time;
            }

            public Object getScore() {
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
