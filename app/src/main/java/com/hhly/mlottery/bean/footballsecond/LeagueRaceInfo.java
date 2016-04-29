package com.hhly.mlottery.bean.footballsecond;

import java.util.List;

/**
 * @author
 * @ClassName:
 * @Description:
 * @date
 */
public class LeagueRaceInfo {


    /**
     * raceList : [{"group":"A组","list":[{"rDate":"03/24","rTime":"17:00","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:01","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:02","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:03","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:04","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:05","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:06","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:07","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:08","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:09","homeName":"主队","guestName":"客队","matchResult":"1:2"}]},{"group":"B组","list":[{"rDate":"03/24","rTime":"17:00","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:01","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:02","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:03","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:04","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:05","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:06","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:07","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:08","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:09","homeName":"主队","guestName":"客队","matchResult":"1:2"}]}]
     * code : 200
     */

    private int code;
    /**
     * group : A组
     * list : [{"rDate":"03/24","rTime":"17:00","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:01","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:02","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:03","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:04","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:05","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:06","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:07","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:08","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:09","homeName":"主队","guestName":"客队","matchResult":"1:2"}]
     */

    private List<RaceListBean> raceList;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<RaceListBean> getRaceList() {
        return raceList;
    }

    public void setRaceList(List<RaceListBean> raceList) {
        this.raceList = raceList;
    }

    public static class RaceListBean {
        private String group;
        /**
         * rDate : 03/24
         * rTime : 17:00
         * homeName : 主队
         * guestName : 客队
         * matchResult : 1:2
         */

        private List<ListBean> list;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String rDate;
            private String rTime;
            private String homeName;
            private String guestName;
            private String matchResult;

            public String getRDate() {
                return rDate;
            }

            public void setRDate(String rDate) {
                this.rDate = rDate;
            }

            public String getRTime() {
                return rTime;
            }

            public void setRTime(String rTime) {
                this.rTime = rTime;
            }

            public String getHomeName() {
                return homeName;
            }

            public void setHomeName(String homeName) {
                this.homeName = homeName;
            }

            public String getGuestName() {
                return guestName;
            }

            public void setGuestName(String guestName) {
                this.guestName = guestName;
            }

            public String getMatchResult() {
                return matchResult;
            }

            public void setMatchResult(String matchResult) {
                this.matchResult = matchResult;
            }
        }
    }
}
