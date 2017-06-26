package com.hhly.mlottery.bean.bettingbean;

import java.util.List;

/**
 * Created by：XQyi on 2017/6/16 16:26
 * Use: 推介发布玩法的bean
 */
public class BettingIssueFabuPalyBean {
//    code	200
//    data	Object

    private Integer code;
    private PromotionTypeVo data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public PromotionTypeVo getData() {
        return data;
    }

    public void setData(PromotionTypeVo data) {
        this.data = data;
    }

    public static class PromotionTypeVo{
//        guestTeam	埃尔维斯
//        homeTeam	拉赫蒂
//        leagueId	13
//        leagueName	芬超
//        promotionTypeList	Array

        private String guestTeam;
        private String homeTeam;
        private String leagueId;
        private String leagueName;
        private List<PromotionTypeListVo> promotionTypeList;

        public String getGuestTeam() {
            return guestTeam;
        }

        public void setGuestTeam(String guestTeam) {
            this.guestTeam = guestTeam;
        }

        public String getHomeTeam() {
            return homeTeam;
        }

        public void setHomeTeam(String homeTeam) {
            this.homeTeam = homeTeam;
        }

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

        public List<PromotionTypeListVo> getPromotionTypeList() {
            return promotionTypeList;
        }

        public void setPromotionTypeList(List<PromotionTypeListVo> promotionTypeList) {
            this.promotionTypeList = promotionTypeList;
        }

        public static class PromotionTypeListVo{
//            oddsList	Array
//            typeName	亚盘
            private List<PromotionOddsVo> oddsList;
            private String typeName;

            public List<PromotionOddsVo> getOddsList() {
                return oddsList;
            }

            public void setOddsList(List<PromotionOddsVo> oddsList) {
                this.oddsList = oddsList;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public class PromotionOddsVo{
//                handicap	平手
//                leftOdds	0.7
//                leftTitle	胜
//                midOdds	null
//                midTitle
//                oddsId	639630
//                rightOdds	1.21
//                rightTitle	负
//                title	半场
//                type	3

                private String handicap;
                private String leftOdds;
                private String leftTitle;
                private String midOdds;
                private String midTitle;
                private String oddsId;
                private String rightOdds;
                private String rightTitle;
                private String title;
                private String type;

                public String getHandicap() {
                    return handicap;
                }

                public void setHandicap(String handicap) {
                    this.handicap = handicap;
                }

                public String getLeftOdds() {
                    return leftOdds;
                }

                public void setLeftOdds(String leftOdds) {
                    this.leftOdds = leftOdds;
                }

                public String getLeftTitle() {
                    return leftTitle;
                }

                public void setLeftTitle(String leftTitle) {
                    this.leftTitle = leftTitle;
                }

                public String getMidOdds() {
                    return midOdds;
                }

                public void setMidOdds(String midOdds) {
                    this.midOdds = midOdds;
                }

                public String getMidTitle() {
                    return midTitle;
                }

                public void setMidTitle(String midTitle) {
                    this.midTitle = midTitle;
                }

                public String getOddsId() {
                    return oddsId;
                }

                public void setOddsId(String oddsId) {
                    this.oddsId = oddsId;
                }

                public String getRightOdds() {
                    return rightOdds;
                }

                public void setRightOdds(String rightOdds) {
                    this.rightOdds = rightOdds;
                }

                public String getRightTitle() {
                    return rightTitle;
                }

                public void setRightTitle(String rightTitle) {
                    this.rightTitle = rightTitle;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

        }
    }
}
