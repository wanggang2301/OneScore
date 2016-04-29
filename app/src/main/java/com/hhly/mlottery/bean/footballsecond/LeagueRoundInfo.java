package com.hhly.mlottery.bean.footballsecond;

import java.util.List;

/**
 * @author
 * @ClassName:
 * @Description:
 * @date
 */
public class LeagueRoundInfo {


    /**
     * data : [{"leagueType":1,"round":1,"current":false},{"leagueType":1,"round":2,"current":false},{"leagueType":1,"round":3,"current":false},{"leagueType":1,"round":4,"current":false},{"leagueType":1,"round":5,"current":false},{"leagueType":1,"round":6,"current":false},{"leagueType":1,"round":7,"current":false},{"leagueType":1,"round":8,"current":false},{"leagueType":1,"round":9,"current":false},{"leagueType":1,"round":10,"current":false},{"leagueType":1,"round":11,"current":false},{"leagueType":1,"round":12,"current":false},{"leagueType":1,"round":13,"current":false},{"leagueType":1,"round":14,"current":false},{"leagueType":1,"round":15,"current":false},{"leagueType":1,"round":16,"current":false},{"leagueType":1,"round":17,"current":false},{"leagueType":1,"round":18,"current":false},{"leagueType":1,"round":19,"current":false},{"leagueType":1,"round":20,"current":true}]
     * code : 200
     * race : [{"group":"A组","list":[{"rDate":"03/24","rTime":"17:00","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:01","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:02","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:03","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:04","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:05","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:06","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:07","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:08","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:09","homeName":"主队","guestName":"客队","matchResult":"1:2"}]},{"group":"B组","list":[{"rDate":"03/24","rTime":"17:00","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:01","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:02","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:03","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:04","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:05","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:06","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:07","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:08","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:09","homeName":"主队","guestName":"客队","matchResult":"1:2"}]}]
     */

    private int code;
    /**
     * leagueType : 1
     * round : 1
     * current : false
     */

    private List<DataBean> data;
    /**
     * group : A组
     * list : [{"rDate":"03/24","rTime":"17:00","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:01","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:02","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:03","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:04","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:05","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:06","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:07","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:08","homeName":"主队","guestName":"客队","matchResult":"1:2"},{"rDate":"03/24","rTime":"17:09","homeName":"主队","guestName":"客队","matchResult":"1:2"}]
     */

    private List<RaceBean> race;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<RaceBean> getRace() {
        return race;
    }

    public void setRace(List<RaceBean> race) {
        this.race = race;
    }

    public static class DataBean {
        private int leagueType;
        private String round;
        private boolean current;

        public int getLeagueType() {
            return leagueType;
        }

        public void setLeagueType(int leagueType) {
            this.leagueType = leagueType;
        }

        public String getRound() {
            return round;
        }

        public void setRound(String round) {
            this.round = round;
        }

        public boolean isCurrent() {
            return current;
        }

        public void setCurrent(boolean current) {
            this.current = current;
        }
    }

    public static class RaceBean {
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
