package com.hhly.mlottery.bean.snookerbean.snookerDetail;

import java.util.List;

/**
 * 描    述：斯诺克亚盘大小球等bean
 * 作    者：mady@13322.com
 * 时    间：2017/2/24
 */
public class SnookerOddsBean {

    /**
     * result : 200
     * listOdd : [{"id":"31","name":"SBO","details":[{"time":"2017-02-23 09:47:56","score":null,"homeOdd":0.39,"hand":-1.5,"guestOdd":1.81},{"time":"2017-02-23 09:47:56","score":null,"homeOdd":0.39,"hand":-1.5,"guestOdd":1.81}]}]
     */

    private int result;
    /**
     * id : 31
     * name : SBO
     * details : [{"time":"2017-02-23 09:47:56","score":null,"homeOdd":0.39,"hand":-1.5,"guestOdd":1.81},{"time":"2017-02-23 09:47:56","score":null,"homeOdd":0.39,"hand":-1.5,"guestOdd":1.81}]
     */

    private List<ListOddEntity> listOdd;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<ListOddEntity> getListOdd() {
        return listOdd;
    }

    public void setListOdd(List<ListOddEntity> listOdd) {
        this.listOdd = listOdd;
    }

    public static class ListOddEntity {
        private String id;
        private String name;
        /**
         * time : 2017-02-23 09:47:56
         * score : null
         * homeOdd : 0.39
         * hand : -1.5
         * guestOdd : 1.81
         */

        private List<DetailsEntity> details;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<DetailsEntity> getDetails() {
            return details;
        }

        public void setDetails(List<DetailsEntity> details) {
            this.details = details;
        }

        public static class DetailsEntity {
            private String time;
            private String score;
            private double homeOdd;
            private double hand;
            private double guestOdd;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public double getHomeOdd() {
                return homeOdd;
            }

            public void setHomeOdd(double homeOdd) {
                this.homeOdd = homeOdd;
            }

            public double getHand() {
                return hand;
            }

            public void setHand(double hand) {
                this.hand = hand;
            }

            public double getGuestOdd() {
                return guestOdd;
            }

            public void setGuestOdd(double guestOdd) {
                this.guestOdd = guestOdd;
            }
        }
    }
}
