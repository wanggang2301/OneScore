package com.hhly.mlottery.bean;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yuely198 on 2017/3/24.
 */

public class BasketballItemSearchBean {

        /**
         * thirdId : 4652027
         * homeTeam : 奧巴杜魯
         * guestTeam : 皇马
         * leagueId : 20
         * leagueName : 西篮甲
         * leagueColor : #ffa000
         * date : 2017-03-19
         * time : 02:00
         * matchStatus : -1
         * matchScore : {"homeScore":90,"guestScore":83,"home1":21,"home2":16,"home3":31,"home4":22,"guest1":28,"guest2":10,"guest3":24,"guest4":21,"homeOt1":0,"homeOt2":0,"homeOt3":0,"guestOt1":0,"guestOt2":0,"guestOt3":0,"remainTime":"","addTime":0}
         * matchOdds : {"asiaSize":{"34":{"handicap":"asiaSize","handicapValue":"163.0","leftOdds":"0.92","rightOdds":"0.92"},"201":{"handicap":"asiaSize","handicapValue":"162.5","leftOdds":"1.11","rightOdds":"0.74"},"bet365":{"handicap":"asiaSize","handicapValue":"161.5","leftOdds":"0.90","rightOdds":"0.90"},"easybets":{"handicap":"asiaSize","handicapValue":"163.0","leftOdds":"0.79","rightOdds":"0.92"},"crown":{"handicap":"asiaSize","handicapValue":"163.0","leftOdds":"1.09","rightOdds":"0.73"},"macauslot":{"handicap":"asiaSize","handicapValue":"163.5","leftOdds":"0.82","rightOdds":"0.82"},"vcbet":{"handicap":"asiaSize","handicapValue":"162.5","leftOdds":"0.90","rightOdds":"0.90"}},"euro":{"euro":{"handicap":"euro","handicapValue":null,"leftOdds":"1.16","rightOdds":"4.76"}},"asiaLet":{"31":{"handicap":"asiaLet","handicapValue":"-10.5","leftOdds":"0.96","rightOdds":"0.90"},"101":{"handicap":"asiaLet","handicapValue":"-10.5","leftOdds":"0.81","rightOdds":"1.05"},"bet365":{"handicap":"asiaLet","handicapValue":"-9.5","leftOdds":"0.90","rightOdds":"0.90"},"easybets":{"handicap":"asiaLet","handicapValue":"-10.5","leftOdds":"0.81","rightOdds":"0.90"},"crown":{"handicap":"asiaLet","handicapValue":"-9.5","leftOdds":"0.72","rightOdds":"1.13"},"macauslot":{"handicap":"asiaLet","handicapValue":"-10.5","leftOdds":"0.85","rightOdds":"0.85"},"vcbet":{"handicap":"asiaLet","handicapValue":"-9.5","leftOdds":"0.90","rightOdds":"0.90"}}}
         * section : 4
         * homeTeamId : 3108
         * guestTeamId : 835
         * textLive : null
         */

        private String thirdId;
        private String homeTeam;
        private String guestTeam;
        private String leagueId;
        private String leagueName;
        private String leagueColor;
        private String date;
        private String time;
        private String matchStatus;
        private MatchScoreBean matchScore;
        private MatchOddsBean matchOdds;
        private int section;
        private String homeTeamId;
        private String guestTeamId;
        private Object textLive;
        private Integer matchType;//	30951

    public Integer getMatchType() {
        return matchType;
    }

    public void setMatchType(Integer matchType) {
        this.matchType = matchType;
    }

    public String getThirdId() {
            return thirdId;
        }

        public void setThirdId(String thirdId) {
            this.thirdId = thirdId;
        }

        public String getHomeTeam() {
            return homeTeam;
        }

        public void setHomeTeam(String homeTeam) {
            this.homeTeam = homeTeam;
        }

        public String getGuestTeam() {
            return guestTeam;
        }

        public void setGuestTeam(String guestTeam) {
            this.guestTeam = guestTeam;
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

        public String getLeagueColor() {
            return leagueColor;
        }

        public void setLeagueColor(String leagueColor) {
            this.leagueColor = leagueColor;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMatchStatus() {
            return matchStatus;
        }

        public void setMatchStatus(String matchStatus) {
            this.matchStatus = matchStatus;
        }

        public MatchScoreBean getMatchScore() {
            return matchScore;
        }

        public void setMatchScore(MatchScoreBean matchScore) {
            this.matchScore = matchScore;
        }

        public MatchOddsBean getMatchOdds() {
            return matchOdds;
        }

        public void setMatchOdds(MatchOddsBean matchOdds) {
            this.matchOdds = matchOdds;
        }

        public int getSection() {
            return section;
        }

        public void setSection(int section) {
            this.section = section;
        }

        public String getHomeTeamId() {
            return homeTeamId;
        }

        public void setHomeTeamId(String homeTeamId) {
            this.homeTeamId = homeTeamId;
        }

        public String getGuestTeamId() {
            return guestTeamId;
        }

        public void setGuestTeamId(String guestTeamId) {
            this.guestTeamId = guestTeamId;
        }

        public Object getTextLive() {
            return textLive;
        }

        public void setTextLive(Object textLive) {
            this.textLive = textLive;
        }

        public static class MatchScoreBean {
            /**
             * homeScore : 90
             * guestScore : 83
             * home1 : 21
             * home2 : 16
             * home3 : 31
             * home4 : 22
             * guest1 : 28
             * guest2 : 10
             * guest3 : 24
             * guest4 : 21
             * homeOt1 : 0
             * homeOt2 : 0
             * homeOt3 : 0
             * guestOt1 : 0
             * guestOt2 : 0
             * guestOt3 : 0
             * remainTime :
             * addTime : 0
             */

            private int homeScore;
            private int guestScore;
            private int home1;
            private int home2;
            private int home3;
            private int home4;
            private int guest1;
            private int guest2;
            private int guest3;
            private int guest4;
            private int homeOt1;
            private int homeOt2;
            private int homeOt3;
            private int guestOt1;
            private int guestOt2;
            private int guestOt3;
            private String remainTime;
            private int addTime;

            public int getHomeScore() {
                return homeScore;
            }

            public void setHomeScore(int homeScore) {
                this.homeScore = homeScore;
            }

            public int getGuestScore() {
                return guestScore;
            }

            public void setGuestScore(int guestScore) {
                this.guestScore = guestScore;
            }

            public int getHome1() {
                return home1;
            }

            public void setHome1(int home1) {
                this.home1 = home1;
            }

            public int getHome2() {
                return home2;
            }

            public void setHome2(int home2) {
                this.home2 = home2;
            }

            public int getHome3() {
                return home3;
            }

            public void setHome3(int home3) {
                this.home3 = home3;
            }

            public int getHome4() {
                return home4;
            }

            public void setHome4(int home4) {
                this.home4 = home4;
            }

            public int getGuest1() {
                return guest1;
            }

            public void setGuest1(int guest1) {
                this.guest1 = guest1;
            }

            public int getGuest2() {
                return guest2;
            }

            public void setGuest2(int guest2) {
                this.guest2 = guest2;
            }

            public int getGuest3() {
                return guest3;
            }

            public void setGuest3(int guest3) {
                this.guest3 = guest3;
            }

            public int getGuest4() {
                return guest4;
            }

            public void setGuest4(int guest4) {
                this.guest4 = guest4;
            }

            public int getHomeOt1() {
                return homeOt1;
            }

            public void setHomeOt1(int homeOt1) {
                this.homeOt1 = homeOt1;
            }

            public int getHomeOt2() {
                return homeOt2;
            }

            public void setHomeOt2(int homeOt2) {
                this.homeOt2 = homeOt2;
            }

            public int getHomeOt3() {
                return homeOt3;
            }

            public void setHomeOt3(int homeOt3) {
                this.homeOt3 = homeOt3;
            }

            public int getGuestOt1() {
                return guestOt1;
            }

            public void setGuestOt1(int guestOt1) {
                this.guestOt1 = guestOt1;
            }

            public int getGuestOt2() {
                return guestOt2;
            }

            public void setGuestOt2(int guestOt2) {
                this.guestOt2 = guestOt2;
            }

            public int getGuestOt3() {
                return guestOt3;
            }

            public void setGuestOt3(int guestOt3) {
                this.guestOt3 = guestOt3;
            }

            public String getRemainTime() {
                return remainTime;
            }

            public void setRemainTime(String remainTime) {
                this.remainTime = remainTime;
            }

            public int getAddTime() {
                return addTime;
            }

            public void setAddTime(int addTime) {
                this.addTime = addTime;
            }
        }

        public static class MatchOddsBean {
            /**
             * asiaSize : {"34":{"handicap":"asiaSize","handicapValue":"163.0","leftOdds":"0.92","rightOdds":"0.92"},"201":{"handicap":"asiaSize","handicapValue":"162.5","leftOdds":"1.11","rightOdds":"0.74"},"bet365":{"handicap":"asiaSize","handicapValue":"161.5","leftOdds":"0.90","rightOdds":"0.90"},"easybets":{"handicap":"asiaSize","handicapValue":"163.0","leftOdds":"0.79","rightOdds":"0.92"},"crown":{"handicap":"asiaSize","handicapValue":"163.0","leftOdds":"1.09","rightOdds":"0.73"},"macauslot":{"handicap":"asiaSize","handicapValue":"163.5","leftOdds":"0.82","rightOdds":"0.82"},"vcbet":{"handicap":"asiaSize","handicapValue":"162.5","leftOdds":"0.90","rightOdds":"0.90"}}
             * euro : {"euro":{"handicap":"euro","handicapValue":null,"leftOdds":"1.16","rightOdds":"4.76"}}
             * asiaLet : {"31":{"handicap":"asiaLet","handicapValue":"-10.5","leftOdds":"0.96","rightOdds":"0.90"},"101":{"handicap":"asiaLet","handicapValue":"-10.5","leftOdds":"0.81","rightOdds":"1.05"},"bet365":{"handicap":"asiaLet","handicapValue":"-9.5","leftOdds":"0.90","rightOdds":"0.90"},"easybets":{"handicap":"asiaLet","handicapValue":"-10.5","leftOdds":"0.81","rightOdds":"0.90"},"crown":{"handicap":"asiaLet","handicapValue":"-9.5","leftOdds":"0.72","rightOdds":"1.13"},"macauslot":{"handicap":"asiaLet","handicapValue":"-10.5","leftOdds":"0.85","rightOdds":"0.85"},"vcbet":{"handicap":"asiaLet","handicapValue":"-9.5","leftOdds":"0.90","rightOdds":"0.90"}}
             */

            private AsiaSizeBean asiaSize;
            private EuroBeanX euro;
            private AsiaLetBean asiaLet;

            public AsiaSizeBean getAsiaSize() {
                return asiaSize;
            }

            public void setAsiaSize(AsiaSizeBean asiaSize) {
                this.asiaSize = asiaSize;
            }

            public EuroBeanX getEuro() {
                return euro;
            }

            public void setEuro(EuroBeanX euro) {
                this.euro = euro;
            }

            public AsiaLetBean getAsiaLet() {
                return asiaLet;
            }

            public void setAsiaLet(AsiaLetBean asiaLet) {
                this.asiaLet = asiaLet;
            }

            public static class AsiaSizeBean {
                /**
                 * 34 : {"handicap":"asiaSize","handicapValue":"163.0","leftOdds":"0.92","rightOdds":"0.92"}
                 * 201 : {"handicap":"asiaSize","handicapValue":"162.5","leftOdds":"1.11","rightOdds":"0.74"}
                 * bet365 : {"handicap":"asiaSize","handicapValue":"161.5","leftOdds":"0.90","rightOdds":"0.90"}
                 * easybets : {"handicap":"asiaSize","handicapValue":"163.0","leftOdds":"0.79","rightOdds":"0.92"}
                 * crown : {"handicap":"asiaSize","handicapValue":"163.0","leftOdds":"1.09","rightOdds":"0.73"}
                 * macauslot : {"handicap":"asiaSize","handicapValue":"163.5","leftOdds":"0.82","rightOdds":"0.82"}
                 * vcbet : {"handicap":"asiaSize","handicapValue":"162.5","leftOdds":"0.90","rightOdds":"0.90"}
                 */

                @SerializedName("34")
                private _$34Bean _$34;
                @SerializedName("201")
                private _$201Bean _$201;
                private Bet365Bean bet365;
                private EasybetsBean easybets;
                private CrownBean crown;
                private MacauslotBean macauslot;
                private VcbetBean vcbet;

                public _$34Bean get_$34() {
                    return _$34;
                }

                public void set_$34(_$34Bean _$34) {
                    this._$34 = _$34;
                }

                public _$201Bean get_$201() {
                    return _$201;
                }

                public void set_$201(_$201Bean _$201) {
                    this._$201 = _$201;
                }

                public Bet365Bean getBet365() {
                    return bet365;
                }

                public void setBet365(Bet365Bean bet365) {
                    this.bet365 = bet365;
                }

                public EasybetsBean getEasybets() {
                    return easybets;
                }

                public void setEasybets(EasybetsBean easybets) {
                    this.easybets = easybets;
                }

                public CrownBean getCrown() {
                    return crown;
                }

                public void setCrown(CrownBean crown) {
                    this.crown = crown;
                }

                public MacauslotBean getMacauslot() {
                    return macauslot;
                }

                public void setMacauslot(MacauslotBean macauslot) {
                    this.macauslot = macauslot;
                }

                public VcbetBean getVcbet() {
                    return vcbet;
                }

                public void setVcbet(VcbetBean vcbet) {
                    this.vcbet = vcbet;
                }

                public static class _$34Bean {
                    /**
                     * handicap : asiaSize
                     * handicapValue : 163.0
                     * leftOdds : 0.92
                     * rightOdds : 0.92
                     */

                    private String handicap;
                    private String handicapValue;
                    private String leftOdds;
                    private String rightOdds;

                    public String getHandicap() {
                        return handicap;
                    }

                    public void setHandicap(String handicap) {
                        this.handicap = handicap;
                    }

                    public String getHandicapValue() {
                        return handicapValue;
                    }

                    public void setHandicapValue(String handicapValue) {
                        this.handicapValue = handicapValue;
                    }

                    public String getLeftOdds() {
                        return leftOdds;
                    }

                    public void setLeftOdds(String leftOdds) {
                        this.leftOdds = leftOdds;
                    }

                    public String getRightOdds() {
                        return rightOdds;
                    }

                    public void setRightOdds(String rightOdds) {
                        this.rightOdds = rightOdds;
                    }
                }

                public static class _$201Bean {
                    /**
                     * handicap : asiaSize
                     * handicapValue : 162.5
                     * leftOdds : 1.11
                     * rightOdds : 0.74
                     */

                    private String handicap;
                    private String handicapValue;
                    private String leftOdds;
                    private String rightOdds;

                    public String getHandicap() {
                        return handicap;
                    }

                    public void setHandicap(String handicap) {
                        this.handicap = handicap;
                    }

                    public String getHandicapValue() {
                        return handicapValue;
                    }

                    public void setHandicapValue(String handicapValue) {
                        this.handicapValue = handicapValue;
                    }

                    public String getLeftOdds() {
                        return leftOdds;
                    }

                    public void setLeftOdds(String leftOdds) {
                        this.leftOdds = leftOdds;
                    }

                    public String getRightOdds() {
                        return rightOdds;
                    }

                    public void setRightOdds(String rightOdds) {
                        this.rightOdds = rightOdds;
                    }
                }

                public static class Bet365Bean {
                    /**
                     * handicap : asiaSize
                     * handicapValue : 161.5
                     * leftOdds : 0.90
                     * rightOdds : 0.90
                     */

                    private String handicap;
                    private String handicapValue;
                    private String leftOdds;
                    private String rightOdds;

                    public String getHandicap() {
                        return handicap;
                    }

                    public void setHandicap(String handicap) {
                        this.handicap = handicap;
                    }

                    public String getHandicapValue() {
                        return handicapValue;
                    }

                    public void setHandicapValue(String handicapValue) {
                        this.handicapValue = handicapValue;
                    }

                    public String getLeftOdds() {
                        return leftOdds;
                    }

                    public void setLeftOdds(String leftOdds) {
                        this.leftOdds = leftOdds;
                    }

                    public String getRightOdds() {
                        return rightOdds;
                    }

                    public void setRightOdds(String rightOdds) {
                        this.rightOdds = rightOdds;
                    }
                }

                public static class EasybetsBean {
                    /**
                     * handicap : asiaSize
                     * handicapValue : 163.0
                     * leftOdds : 0.79
                     * rightOdds : 0.92
                     */

                    private String handicap;
                    private String handicapValue;
                    private String leftOdds;
                    private String rightOdds;

                    public String getHandicap() {
                        return handicap;
                    }

                    public void setHandicap(String handicap) {
                        this.handicap = handicap;
                    }

                    public String getHandicapValue() {
                        return handicapValue;
                    }

                    public void setHandicapValue(String handicapValue) {
                        this.handicapValue = handicapValue;
                    }

                    public String getLeftOdds() {
                        return leftOdds;
                    }

                    public void setLeftOdds(String leftOdds) {
                        this.leftOdds = leftOdds;
                    }

                    public String getRightOdds() {
                        return rightOdds;
                    }

                    public void setRightOdds(String rightOdds) {
                        this.rightOdds = rightOdds;
                    }
                }

                public static class CrownBean {
                    /**
                     * handicap : asiaSize
                     * handicapValue : 163.0
                     * leftOdds : 1.09
                     * rightOdds : 0.73
                     */

                    private String handicap;
                    private String handicapValue;
                    private String leftOdds;
                    private String rightOdds;

                    public String getHandicap() {
                        return handicap;
                    }

                    public void setHandicap(String handicap) {
                        this.handicap = handicap;
                    }

                    public String getHandicapValue() {
                        return handicapValue;
                    }

                    public void setHandicapValue(String handicapValue) {
                        this.handicapValue = handicapValue;
                    }

                    public String getLeftOdds() {
                        return leftOdds;
                    }

                    public void setLeftOdds(String leftOdds) {
                        this.leftOdds = leftOdds;
                    }

                    public String getRightOdds() {
                        return rightOdds;
                    }

                    public void setRightOdds(String rightOdds) {
                        this.rightOdds = rightOdds;
                    }
                }

                public static class MacauslotBean {
                    /**
                     * handicap : asiaSize
                     * handicapValue : 163.5
                     * leftOdds : 0.82
                     * rightOdds : 0.82
                     */

                    private String handicap;
                    private String handicapValue;
                    private String leftOdds;
                    private String rightOdds;

                    public String getHandicap() {
                        return handicap;
                    }

                    public void setHandicap(String handicap) {
                        this.handicap = handicap;
                    }

                    public String getHandicapValue() {
                        return handicapValue;
                    }

                    public void setHandicapValue(String handicapValue) {
                        this.handicapValue = handicapValue;
                    }

                    public String getLeftOdds() {
                        return leftOdds;
                    }

                    public void setLeftOdds(String leftOdds) {
                        this.leftOdds = leftOdds;
                    }

                    public String getRightOdds() {
                        return rightOdds;
                    }

                    public void setRightOdds(String rightOdds) {
                        this.rightOdds = rightOdds;
                    }
                }

                public static class VcbetBean {
                    /**
                     * handicap : asiaSize
                     * handicapValue : 162.5
                     * leftOdds : 0.90
                     * rightOdds : 0.90
                     */

                    private String handicap;
                    private String handicapValue;
                    private String leftOdds;
                    private String rightOdds;

                    public String getHandicap() {
                        return handicap;
                    }

                    public void setHandicap(String handicap) {
                        this.handicap = handicap;
                    }

                    public String getHandicapValue() {
                        return handicapValue;
                    }

                    public void setHandicapValue(String handicapValue) {
                        this.handicapValue = handicapValue;
                    }

                    public String getLeftOdds() {
                        return leftOdds;
                    }

                    public void setLeftOdds(String leftOdds) {
                        this.leftOdds = leftOdds;
                    }

                    public String getRightOdds() {
                        return rightOdds;
                    }

                    public void setRightOdds(String rightOdds) {
                        this.rightOdds = rightOdds;
                    }
                }
            }

            public static class EuroBeanX {
                /**
                 * euro : {"handicap":"euro","handicapValue":null,"leftOdds":"1.16","rightOdds":"4.76"}
                 */

                private EuroBean euro;

                public EuroBean getEuro() {
                    return euro;
                }

                public void setEuro(EuroBean euro) {
                    this.euro = euro;
                }

                public static class EuroBean {
                    /**
                     * handicap : euro
                     * handicapValue : null
                     * leftOdds : 1.16
                     * rightOdds : 4.76
                     */

                    private String handicap;
                    private Object handicapValue;
                    private String leftOdds;
                    private String rightOdds;

                    public String getHandicap() {
                        return handicap;
                    }

                    public void setHandicap(String handicap) {
                        this.handicap = handicap;
                    }

                    public Object getHandicapValue() {
                        return handicapValue;
                    }

                    public void setHandicapValue(Object handicapValue) {
                        this.handicapValue = handicapValue;
                    }

                    public String getLeftOdds() {
                        return leftOdds;
                    }

                    public void setLeftOdds(String leftOdds) {
                        this.leftOdds = leftOdds;
                    }

                    public String getRightOdds() {
                        return rightOdds;
                    }

                    public void setRightOdds(String rightOdds) {
                        this.rightOdds = rightOdds;
                    }
                }
            }

            public static class AsiaLetBean {
                /**
                 * 31 : {"handicap":"asiaLet","handicapValue":"-10.5","leftOdds":"0.96","rightOdds":"0.90"}
                 * 101 : {"handicap":"asiaLet","handicapValue":"-10.5","leftOdds":"0.81","rightOdds":"1.05"}
                 * bet365 : {"handicap":"asiaLet","handicapValue":"-9.5","leftOdds":"0.90","rightOdds":"0.90"}
                 * easybets : {"handicap":"asiaLet","handicapValue":"-10.5","leftOdds":"0.81","rightOdds":"0.90"}
                 * crown : {"handicap":"asiaLet","handicapValue":"-9.5","leftOdds":"0.72","rightOdds":"1.13"}
                 * macauslot : {"handicap":"asiaLet","handicapValue":"-10.5","leftOdds":"0.85","rightOdds":"0.85"}
                 * vcbet : {"handicap":"asiaLet","handicapValue":"-9.5","leftOdds":"0.90","rightOdds":"0.90"}
                 */

                @SerializedName("31")
                private _$31Bean _$31;
                @SerializedName("101")
                private _$101Bean _$101;
                private Bet365BeanX bet365;
                private EasybetsBeanX easybets;
                private CrownBeanX crown;
                private MacauslotBeanX macauslot;
                private VcbetBeanX vcbet;

                public _$31Bean get_$31() {
                    return _$31;
                }

                public void set_$31(_$31Bean _$31) {
                    this._$31 = _$31;
                }

                public _$101Bean get_$101() {
                    return _$101;
                }

                public void set_$101(_$101Bean _$101) {
                    this._$101 = _$101;
                }

                public Bet365BeanX getBet365() {
                    return bet365;
                }

                public void setBet365(Bet365BeanX bet365) {
                    this.bet365 = bet365;
                }

                public EasybetsBeanX getEasybets() {
                    return easybets;
                }

                public void setEasybets(EasybetsBeanX easybets) {
                    this.easybets = easybets;
                }

                public CrownBeanX getCrown() {
                    return crown;
                }

                public void setCrown(CrownBeanX crown) {
                    this.crown = crown;
                }

                public MacauslotBeanX getMacauslot() {
                    return macauslot;
                }

                public void setMacauslot(MacauslotBeanX macauslot) {
                    this.macauslot = macauslot;
                }

                public VcbetBeanX getVcbet() {
                    return vcbet;
                }

                public void setVcbet(VcbetBeanX vcbet) {
                    this.vcbet = vcbet;
                }

                public static class _$31Bean {
                    /**
                     * handicap : asiaLet
                     * handicapValue : -10.5
                     * leftOdds : 0.96
                     * rightOdds : 0.90
                     */

                    private String handicap;
                    private String handicapValue;
                    private String leftOdds;
                    private String rightOdds;

                    public String getHandicap() {
                        return handicap;
                    }

                    public void setHandicap(String handicap) {
                        this.handicap = handicap;
                    }

                    public String getHandicapValue() {
                        return handicapValue;
                    }

                    public void setHandicapValue(String handicapValue) {
                        this.handicapValue = handicapValue;
                    }

                    public String getLeftOdds() {
                        return leftOdds;
                    }

                    public void setLeftOdds(String leftOdds) {
                        this.leftOdds = leftOdds;
                    }

                    public String getRightOdds() {
                        return rightOdds;
                    }

                    public void setRightOdds(String rightOdds) {
                        this.rightOdds = rightOdds;
                    }
                }

                public static class _$101Bean {
                    /**
                     * handicap : asiaLet
                     * handicapValue : -10.5
                     * leftOdds : 0.81
                     * rightOdds : 1.05
                     */

                    private String handicap;
                    private String handicapValue;
                    private String leftOdds;
                    private String rightOdds;

                    public String getHandicap() {
                        return handicap;
                    }

                    public void setHandicap(String handicap) {
                        this.handicap = handicap;
                    }

                    public String getHandicapValue() {
                        return handicapValue;
                    }

                    public void setHandicapValue(String handicapValue) {
                        this.handicapValue = handicapValue;
                    }

                    public String getLeftOdds() {
                        return leftOdds;
                    }

                    public void setLeftOdds(String leftOdds) {
                        this.leftOdds = leftOdds;
                    }

                    public String getRightOdds() {
                        return rightOdds;
                    }

                    public void setRightOdds(String rightOdds) {
                        this.rightOdds = rightOdds;
                    }
                }

                public static class Bet365BeanX {
                    /**
                     * handicap : asiaLet
                     * handicapValue : -9.5
                     * leftOdds : 0.90
                     * rightOdds : 0.90
                     */

                    private String handicap;
                    private String handicapValue;
                    private String leftOdds;
                    private String rightOdds;

                    public String getHandicap() {
                        return handicap;
                    }

                    public void setHandicap(String handicap) {
                        this.handicap = handicap;
                    }

                    public String getHandicapValue() {
                        return handicapValue;
                    }

                    public void setHandicapValue(String handicapValue) {
                        this.handicapValue = handicapValue;
                    }

                    public String getLeftOdds() {
                        return leftOdds;
                    }

                    public void setLeftOdds(String leftOdds) {
                        this.leftOdds = leftOdds;
                    }

                    public String getRightOdds() {
                        return rightOdds;
                    }

                    public void setRightOdds(String rightOdds) {
                        this.rightOdds = rightOdds;
                    }
                }

                public static class EasybetsBeanX {
                    /**
                     * handicap : asiaLet
                     * handicapValue : -10.5
                     * leftOdds : 0.81
                     * rightOdds : 0.90
                     */

                    private String handicap;
                    private String handicapValue;
                    private String leftOdds;
                    private String rightOdds;

                    public String getHandicap() {
                        return handicap;
                    }

                    public void setHandicap(String handicap) {
                        this.handicap = handicap;
                    }

                    public String getHandicapValue() {
                        return handicapValue;
                    }

                    public void setHandicapValue(String handicapValue) {
                        this.handicapValue = handicapValue;
                    }

                    public String getLeftOdds() {
                        return leftOdds;
                    }

                    public void setLeftOdds(String leftOdds) {
                        this.leftOdds = leftOdds;
                    }

                    public String getRightOdds() {
                        return rightOdds;
                    }

                    public void setRightOdds(String rightOdds) {
                        this.rightOdds = rightOdds;
                    }
                }

                public static class CrownBeanX {
                    /**
                     * handicap : asiaLet
                     * handicapValue : -9.5
                     * leftOdds : 0.72
                     * rightOdds : 1.13
                     */

                    private String handicap;
                    private String handicapValue;
                    private String leftOdds;
                    private String rightOdds;

                    public String getHandicap() {
                        return handicap;
                    }

                    public void setHandicap(String handicap) {
                        this.handicap = handicap;
                    }

                    public String getHandicapValue() {
                        return handicapValue;
                    }

                    public void setHandicapValue(String handicapValue) {
                        this.handicapValue = handicapValue;
                    }

                    public String getLeftOdds() {
                        return leftOdds;
                    }

                    public void setLeftOdds(String leftOdds) {
                        this.leftOdds = leftOdds;
                    }

                    public String getRightOdds() {
                        return rightOdds;
                    }

                    public void setRightOdds(String rightOdds) {
                        this.rightOdds = rightOdds;
                    }
                }

                public static class MacauslotBeanX {
                    /**
                     * handicap : asiaLet
                     * handicapValue : -10.5
                     * leftOdds : 0.85
                     * rightOdds : 0.85
                     */

                    private String handicap;
                    private String handicapValue;
                    private String leftOdds;
                    private String rightOdds;

                    public String getHandicap() {
                        return handicap;
                    }

                    public void setHandicap(String handicap) {
                        this.handicap = handicap;
                    }

                    public String getHandicapValue() {
                        return handicapValue;
                    }

                    public void setHandicapValue(String handicapValue) {
                        this.handicapValue = handicapValue;
                    }

                    public String getLeftOdds() {
                        return leftOdds;
                    }

                    public void setLeftOdds(String leftOdds) {
                        this.leftOdds = leftOdds;
                    }

                    public String getRightOdds() {
                        return rightOdds;
                    }

                    public void setRightOdds(String rightOdds) {
                        this.rightOdds = rightOdds;
                    }
                }

                public static class VcbetBeanX {
                    /**
                     * handicap : asiaLet
                     * handicapValue : -9.5
                     * leftOdds : 0.90
                     * rightOdds : 0.90
                     */

                    private String handicap;
                    private String handicapValue;
                    private String leftOdds;
                    private String rightOdds;

                    public String getHandicap() {
                        return handicap;
                    }

                    public void setHandicap(String handicap) {
                        this.handicap = handicap;
                    }

                    public String getHandicapValue() {
                        return handicapValue;
                    }

                    public void setHandicapValue(String handicapValue) {
                        this.handicapValue = handicapValue;
                    }

                    public String getLeftOdds() {
                        return leftOdds;
                    }

                    public void setLeftOdds(String leftOdds) {
                        this.leftOdds = leftOdds;
                    }

                    public String getRightOdds() {
                        return rightOdds;
                    }

                    public void setRightOdds(String rightOdds) {
                        this.rightOdds = rightOdds;
                    }
                }
            }
        }
    }

