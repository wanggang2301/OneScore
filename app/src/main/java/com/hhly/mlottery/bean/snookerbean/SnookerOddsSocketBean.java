package com.hhly.mlottery.bean.snookerbean;

import java.util.List;

/**
 * Created by yixq on 2016/11/23.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerOddsSocketBean {
//    data	Object
//    thirdId	569128
//    type	301
    private SnookerOddsDataBean data;
    private String thirdId;
    private int type;

    public SnookerOddsDataBean getData() {
        return data;
    }

    public void setData(SnookerOddsDataBean data) {
        this.data = data;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class SnookerOddsDataBean{
//        dataType	1
//        date	2016-11-15
//        delps
//        games	Array
//        guestId	1049
//        guestTeam	S.&#23041;&#23572;&#26862;
//        homeId	26
//        homeTeam	A.&#26757;&#36203;&#22612;
//        leaguesId	125358
//        leaguesName	&#21271;&#29233;&#23572;&#20848;&#20844;&#24320;&#36187;
//        matchOdds	Object
//        matchStyle	7
//        subLgName	128&#24378;
//        thirdId	569128
//        time	21:00

        private String dataType;
        private String date;
        private String delps;
        private List<SnookerOddsGamesBean> games;
        private String guestId;
        private String guestTeam;
        private String homeId;
        private String homeTeam;
        private String leaguesId;
        private String leaguesName;
        private SnookerMatchOddsBean matchOdds;
        private String matchStyle;
        private String subLgName;
        private String thirdId;
        private String time;

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDelps() {
            return delps;
        }

        public void setDelps(String delps) {
            this.delps = delps;
        }

        public List<SnookerOddsGamesBean> getGames() {
            return games;
        }

        public void setGames(List<SnookerOddsGamesBean> games) {
            this.games = games;
        }

        public String getGuestId() {
            return guestId;
        }

        public void setGuestId(String guestId) {
            this.guestId = guestId;
        }

        public String getGuestTeam() {
            return guestTeam;
        }

        public void setGuestTeam(String guestTeam) {
            this.guestTeam = guestTeam;
        }

        public String getHomeId() {
            return homeId;
        }

        public void setHomeId(String homeId) {
            this.homeId = homeId;
        }

        public String getHomeTeam() {
            return homeTeam;
        }

        public void setHomeTeam(String homeTeam) {
            this.homeTeam = homeTeam;
        }

        public String getLeaguesId() {
            return leaguesId;
        }

        public void setLeaguesId(String leaguesId) {
            this.leaguesId = leaguesId;
        }

        public String getLeaguesName() {
            return leaguesName;
        }

        public void setLeaguesName(String leaguesName) {
            this.leaguesName = leaguesName;
        }

        public SnookerMatchOddsBean getMatchOdds() {
            return matchOdds;
        }

        public void setMatchOdds(SnookerMatchOddsBean matchOdds) {
            this.matchOdds = matchOdds;
        }

        public String getMatchStyle() {
            return matchStyle;
        }

        public void setMatchStyle(String matchStyle) {
            this.matchStyle = matchStyle;
        }

        public String getSubLgName() {
            return subLgName;
        }

        public void setSubLgName(String subLgName) {
            this.subLgName = subLgName;
        }

        public String getThirdId() {
            return thirdId;
        }

        public void setThirdId(String thirdId) {
            this.thirdId = thirdId;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public class SnookerOddsGamesBean{
//            capNum	0.0
//            gameTime	0
//            gameType	4
//            id	456912814000
//            name	0onetwo
//            ratioItems	Array
            private String capNum;
            private String gameTime;
            private String gameType;
            private String id;
            private String name;
            private List<SnookerOddsGamesRatioItemsBean> ratioItems;

            public String getCapNum() {
                return capNum;
            }

            public void setCapNum(String capNum) {
                this.capNum = capNum;
            }

            public String getGameTime() {
                return gameTime;
            }

            public void setGameTime(String gameTime) {
                this.gameTime = gameTime;
            }

            public String getGameType() {
                return gameType;
            }

            public void setGameType(String gameType) {
                this.gameType = gameType;
            }

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

            public List<SnookerOddsGamesRatioItemsBean> getRatioItems() {
                return ratioItems;
            }

            public void setRatioItems(List<SnookerOddsGamesRatioItemsBean> ratioItems) {
                this.ratioItems = ratioItems;
            }

            public class SnookerOddsGamesRatioItemsBean{
//                mainDisk	0
//                ratio	0.93,0.93
//                thirdPartySystems	lj
//                time	2016-11-15 16:54:39
//                version	1
                private String mainDisk;
                private String ratio;
                private String thirdPartySystems;
                private String time;
                private String version;

                public String getMainDisk() {
                    return mainDisk;
                }

                public void setMainDisk(String mainDisk) {
                    this.mainDisk = mainDisk;
                }

                public String getRatio() {
                    return ratio;
                }

                public void setRatio(String ratio) {
                    this.ratio = ratio;
                }

                public String getThirdPartySystems() {
                    return thirdPartySystems;
                }

                public void setThirdPartySystems(String thirdPartySystems) {
                    this.thirdPartySystems = thirdPartySystems;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getVersion() {
                    return version;
                }

                public void setVersion(String version) {
                    this.version = version;
                }
            }

    }
    }
}
