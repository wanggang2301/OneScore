package com.hhly.mlottery.bean.oddsbean;

import java.util.List;

/**
 * Created by 103TJL on 2016/5/3.
 * 新版指数实体类
 */
public class NewOddsInfo {

    private String currDate;
    private int code;
    private List<String> focusLeagueIds;
    /**
     * leagueId : 116
     * leagueName : 捷克杯
     * leagueColor : #6A7A86
     * matchsInLeague : 1
     * hot : false
     */

    private List<FileterTagsBean> fileterTags;
    /**
     * comId : 3
     * comName : 皇冠
     */

    private List<CompanyBean> company;

    private List<AllInfoBean> allInfo;

    public String getCurrDate() {
        return currDate;
    }

    public void setCurrDate(String currDate) {
        this.currDate = currDate;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<String> getFocusLeagueIds() {
        return focusLeagueIds;
    }

    public void setFocusLeagueIds(List<String> focusLeagueIds) {
        this.focusLeagueIds = focusLeagueIds;
    }

    public List<FileterTagsBean> getFileterTags() {
        return fileterTags;
    }

    public void setFileterTags(List<FileterTagsBean> fileterTags) {
        this.fileterTags = fileterTags;
    }

    public List<CompanyBean> getCompany() {
        return company;
    }

    public void setCompany(List<CompanyBean> company) {
        this.company = company;
    }

    public List<AllInfoBean> getAllInfo() {
        return allInfo;
    }

    public void setAllInfo(List<AllInfoBean> allInfo) {
        this.allInfo = allInfo;
    }

    public static class FileterTagsBean {
        private String leagueId;
        private String leagueName;
        private String leagueColor;
        private int matchsInLeague;
        private boolean hot;

        public String getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(String leagueId) {
            this.leagueId = leagueId;
        }

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }

        public String getLeagueColor() {
            return leagueColor;
        }

        public void setLeagueColor(String leagueColor) {
            this.leagueColor = leagueColor;
        }

        public int getMatchsInLeague() {
            return matchsInLeague;
        }

        public void setMatchsInLeague(int matchsInLeague) {
            this.matchsInLeague = matchsInLeague;
        }

        public boolean isHot() {
            return hot;
        }

        public void setHot(boolean hot) {
            this.hot = hot;
        }
    }

    public static class CompanyBean {
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

    public static class AllInfoBean {
        private String leagueName;
        private String leagueId;
        private String leagueColor;
        private boolean hot;
        /**
         * matchId : 327810
         * matchHomeName : 全北现代
         * matchGuestName : 江苏苏宁
         * matchResult : 0:0
         * openTime : 18:00
         * matchState : 0
         */

        private MatchInfoBean matchInfo;
        /**
         * comId : 3
         * comName : 皇冠
         * currLevel : {"dtime":null,"left":"0.88","middle":"0.75","right":"1.00"}
         * preLevel : {"dtime":null,"left":"0.91","middle":"0.75","right":"0.91"}
         */

        private List<ComListBean> comList;

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }

        public String getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(String leagueId) {
            this.leagueId = leagueId;
        }

        public String getLeagueColor() {
            return leagueColor;
        }

        public void setLeagueColor(String leagueColor) {
            this.leagueColor = leagueColor;
        }

        public boolean isHot() {
            return hot;
        }

        public void setHot(boolean hot) {
            this.hot = hot;
        }

        public MatchInfoBean getMatchInfo() {
            return matchInfo;
        }

        public void setMatchInfo(MatchInfoBean matchInfo) {
            this.matchInfo = matchInfo;
        }

        public List<ComListBean> getComList() {
            return comList;
        }

        public void setComList(List<ComListBean> comList) {
            this.comList = comList;
        }

        public static class MatchInfoBean {
            private String matchId;
            private String matchHomeName;
            private String matchGuestName;
            private String matchResult;
            private String openTime;
            private String matchState;

            public String getMatchId() {
                return matchId;
            }

            public void setMatchId(String matchId) {
                this.matchId = matchId;
            }

            public String getMatchHomeName() {
                return matchHomeName;
            }

            public void setMatchHomeName(String matchHomeName) {
                this.matchHomeName = matchHomeName;
            }

            public String getMatchGuestName() {
                return matchGuestName;
            }

            public void setMatchGuestName(String matchGuestName) {
                this.matchGuestName = matchGuestName;
            }

            public String getMatchResult() {
                return matchResult;
            }

            public void setMatchResult(String matchResult) {
                this.matchResult = matchResult;
            }

            public String getOpenTime() {
                return openTime;
            }

            public void setOpenTime(String openTime) {
                this.openTime = openTime;
            }

            public String getMatchState() {
                return matchState;
            }

            public void setMatchState(String matchState) {
                this.matchState = matchState;
            }
        }

        public static class ComListBean {
            private String comId;
            private String comName;
            /**
             * dtime : null
             * left : 0.88
             * middle : 0.75
             * right : 1.00
             */

            private CurrLevelBean currLevel;
            /**
             * dtime : null
             * left : 0.91
             * middle : 0.75
             * right : 0.91
             */

            private PreLevelBean preLevel;

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

            public CurrLevelBean getCurrLevel() {
                return currLevel;
            }

            public void setCurrLevel(CurrLevelBean currLevel) {
                this.currLevel = currLevel;
            }

            public PreLevelBean getPreLevel() {
                return preLevel;
            }

            public void setPreLevel(PreLevelBean preLevel) {
                this.preLevel = preLevel;
            }

            public static class CurrLevelBean {
                private Object dtime;
                private String left;
                private String middle;
                private String right;

                public Object getDtime() {
                    return dtime;
                }

                public void setDtime(Object dtime) {
                    this.dtime = dtime;
                }

                public String getLeft() {
                    return left;
                }

                public void setLeft(String left) {
                    this.left = left;
                }

                public String getMiddle() {
                    return middle;
                }

                public void setMiddle(String middle) {
                    this.middle = middle;
                }

                public String getRight() {
                    return right;
                }

                public void setRight(String right) {
                    this.right = right;
                }
            }

            public static class PreLevelBean {
                private Object dtime;
                private String left;
                private String middle;
                private String right;

                public Object getDtime() {
                    return dtime;
                }

                public void setDtime(Object dtime) {
                    this.dtime = dtime;
                }

                public String getLeft() {
                    return left;
                }

                public void setLeft(String left) {
                    this.left = left;
                }

                public String getMiddle() {
                    return middle;
                }

                public void setMiddle(String middle) {
                    this.middle = middle;
                }

                public String getRight() {
                    return right;
                }

                public void setRight(String right) {
                    this.right = right;
                }
            }
        }
    }
}
