package com.hhly.mlottery.bean.oddsbean;

import java.io.Serializable;
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
     * leagueId : 250
     * leagueName : 哥伦甲
     * leagueColor : #993333
     * matchsInLeague : 1
     * hot : false
     */

    private List<FileterTagsBean> fileterTags;
    /**
     * comId : 3
     * comName : 皇冠
     */

    private List<CompanyBean> company;
    /**
     * leagueName : 巴拉甲
     * leagueId : 354
     * leagueColor : #a00800
     * hot : false
     * matchInfo : {"matchId":"331747","matchHomeName":"巴拉国民","matchGuestName":"波特诺","matchResult":"1:1","openTime":null,"matchState":"-1"}
     * comList : [{"comId":"3","comName":"皇冠","currLevel":{"left":"1.02","middle":"-0.25","right":"0.80","leftUp":1,"rightUp":-1},"preLevel":{"left":"0.91","middle":"-0.25","right":"0.91","leftUp":0,"rightUp":0}},{"comId":"45","comName":"浩博","currLevel":{"left":"1.01","middle":"-0.25","right":"0.83","leftUp":1,"rightUp":-1},"preLevel":{"left":"0.86","middle":"-0.25","right":"0.98","leftUp":0,"rightUp":0}},{"comId":"31","comName":"利记","currLevel":{"left":"0.95","middle":"-0.25","right":"0.89","leftUp":1,"rightUp":-1},"preLevel":{"left":"0.90","middle":"-0.25","right":"0.94","leftUp":0,"rightUp":0}},{"comId":"38","comName":"IBC","currLevel":{"left":"1.05","middle":"-0.25","right":"0.79","leftUp":0,"rightUp":0},"preLevel":{"left":"1.05","middle":"-0.25","right":"0.79","leftUp":0,"rightUp":0}},{"comId":"44","comName":"ISN","currLevel":{"left":"1.01","middle":"-0.25","right":"0.83","leftUp":1,"rightUp":-1},"preLevel":{"left":"0.86","middle":"-0.25","right":"0.98","leftUp":0,"rightUp":0}}]
     */

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

    public static class FileterTagsBean implements Serializable{
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
        private boolean isChecked;

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

        public boolean isChecked() {
            return isChecked;
        }

        public void setIsChecked(boolean isChecked) {
            this.isChecked = isChecked;
        }
    }

    public static class AllInfoBean {
        private String leagueName;
        private String leagueId;
        private String leagueColor;
        private boolean hot;
        /**
         * matchId : 331747
         * matchHomeName : 巴拉国民
         * matchGuestName : 波特诺
         * matchResult : 1:1
         * openTime : null
         * matchState : -1
         */

        private MatchInfoBean matchInfo;
        /**
         * comId : 3
         * comName : 皇冠
         * currLevel : {"left":"1.02","middle":"-0.25","right":"0.80","leftUp":1,"rightUp":-1}
         * preLevel : {"left":"0.91","middle":"-0.25","right":"0.91","leftUp":0,"rightUp":0}
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
            private boolean isShow;

            public boolean isShow() {
                return isShow;
            }

            public void setIsShow(boolean isShow) {
                this.isShow = isShow;
            }

            /**
             * left : 1.02
             * middle : -0.25
             * right : 0.80
             * leftUp : 1
             * rightUp : -1
             */

            private CurrLevelBean currLevel;
            /**
             * left : 0.91
             * middle : -0.25
             * right : 0.91
             * leftUp : 0
             * rightUp : 0
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
                private String left;
                private String middle;
                private String right;
                private int leftUp;
                private int rightUp;
                private int middleUp;
                private String currTextBgColor;

                public String getCurrTextBgColor() {
                    return currTextBgColor;
                }

                public void setCurrTextBgColor(String currTextBgColor) {
                    this.currTextBgColor = currTextBgColor;
                }

                public int getMiddleUp() {
                    return middleUp;
                }

                public void setMiddleUp(int middleUp) {
                    this.middleUp = middleUp;
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

                public int getLeftUp() {
                    return leftUp;
                }

                public void setLeftUp(int leftUp) {
                    this.leftUp = leftUp;
                }

                public int getRightUp() {
                    return rightUp;
                }

                public void setRightUp(int rightUp) {
                    this.rightUp = rightUp;
                }
            }

            public static class PreLevelBean {
                private String left;
                private String middle;
                private String right;
                private int leftUp;
                private int rightUp;
                private int middleUp;

                public int getMiddleUp() {
                    return middleUp;
                }

                public void setMiddleUp(int middleUp) {
                    this.middleUp = middleUp;
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

                public int getLeftUp() {
                    return leftUp;
                }

                public void setLeftUp(int leftUp) {
                    this.leftUp = leftUp;
                }

                public int getRightUp() {
                    return rightUp;
                }

                public void setRightUp(int rightUp) {
                    this.rightUp = rightUp;
                }
            }
        }
    }
}
