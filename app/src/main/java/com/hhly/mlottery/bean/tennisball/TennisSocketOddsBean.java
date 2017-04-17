package com.hhly.mlottery.bean.tennisball;

/**
 * desc:网球列表指数变化推送bean
 * Created by 107_tangrr on 2017/3/31 0031.
 */

public class TennisSocketOddsBean {


    /**
     * dataObj : {"company":"qt","gameType":2,"matchId":"848835062","matchOdd":{"l":"1.22","m":"0.0","r":"4.2"}}
     * type : 402
     */

    private DataObjBean dataObj;
    private int type;

    public DataObjBean getDataObj() {
        return dataObj;
    }

    public void setDataObj(DataObjBean dataObj) {
        this.dataObj = dataObj;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class DataObjBean {
        /**
         * company : qt
         * gameType : 2
         * matchId : 848835062
         * matchOdd : {"l":"1.22","m":"0.0","r":"4.2"}
         */

        private String company;
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

        private int gameType;
        private String matchId;
        private MatchOddBean matchOdd;

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public int getGameType() {
            return gameType;
        }

        public void setGameType(int gameType) {
            this.gameType = gameType;
        }

        public String getMatchId() {
            return matchId;
        }

        public void setMatchId(String matchId) {
            this.matchId = matchId;
        }

        public MatchOddBean getMatchOdd() {
            return matchOdd;
        }

        public void setMatchOdd(MatchOddBean matchOdd) {
            this.matchOdd = matchOdd;
        }

        public static class MatchOddBean {
            /**
             * l : 1.22
             * m : 0.0
             * r : 4.2
             */

            private String l;
            private String m;
            private String r;

            public String getL() {
                return l;
            }

            public void setL(String l) {
                this.l = l;
            }

            public String getM() {
                return m;
            }

            public void setM(String m) {
                this.m = m;
            }

            public String getR() {
                return r;
            }

            public void setR(String r) {
                this.r = r;
            }
        }
    }
}


