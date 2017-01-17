package com.hhly.mlottery.bean.basket;

/**
 * Created by andy on 2016/4/1.
 * 篮球详情界面头部的bean
 */
public class BasketballDetailsBean {

    private String bgUrl;


    private MatchEntity match;

    public void setBgUrl(String bgUrl) {
        this.bgUrl = bgUrl;
    }

    public void setMatch(MatchEntity match) {
        this.match = match;
    }

    public String getBgUrl() {
        return bgUrl;
    }

    public MatchEntity getMatch() {
        return match;
    }

    public static class MatchEntity {
        public Integer getMatchType() {
            return matchType;
        }

        public void setMatchType(Integer matchType) {
            this.matchType = matchType;
        }

        private Integer matchType; //联赛类型  //跳转


        private String thirdId;      //跳转
        private String homeTeam;
        private String guestTeam;
        private String leagueId;   //联赛Id  //跳转
        private String leagueName;
        private String leagueColor;
        private String date;
        private String time;
        private int matchStatus;     //跳转
        private boolean hot;
        private String homeRanking;
        private String guestRanking;

        private MatchScoreEntity matchScore;
        private Object matchOdds;  //头部没用
        private int section;     //篮球几节
        private String homeTeamId;
        private String guestTeamId;
        private String homeLogoUrl;
        private String guestLogoUrl;

        public void setThirdId(String thirdId) {
            this.thirdId = thirdId;
        }

        public void setHomeTeam(String homeTeam) {
            this.homeTeam = homeTeam;
        }

        public void setGuestTeam(String guestTeam) {
            this.guestTeam = guestTeam;
        }

        public void setLeagueId(String leagueId) {
            this.leagueId = leagueId;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }

        public void setLeagueColor(String leagueColor) {
            this.leagueColor = leagueColor;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setMatchStatus(int matchStatus) {
            this.matchStatus = matchStatus;
        }

        public void setHot(boolean hot) {
            this.hot = hot;
        }

        public void setHomeRanking(String homeRanking) {
            this.homeRanking = homeRanking;
        }

        public void setGuestRanking(String guestRanking) {
            this.guestRanking = guestRanking;
        }

        public void setMatchScore(MatchScoreEntity matchScore) {
            this.matchScore = matchScore;
        }

        public void setMatchOdds(Object matchOdds) {
            this.matchOdds = matchOdds;
        }

        public void setSection(int section) {
            this.section = section;
        }

        public void setHomeTeamId(String homeTeamId) {
            this.homeTeamId = homeTeamId;
        }

        public void setGuestTeamId(String guestTeamId) {
            this.guestTeamId = guestTeamId;
        }

        public void setHomeLogoUrl(String homeLogoUrl) {
            this.homeLogoUrl = homeLogoUrl;
        }

        public void setGuestLogoUrl(String guestLogoUrl) {
            this.guestLogoUrl = guestLogoUrl;
        }

        public String getThirdId() {
            return thirdId;
        }

        public String getHomeTeam() {
            return homeTeam;
        }

        public String getGuestTeam() {
            return guestTeam;
        }

        public String getLeagueId() {
            return leagueId;
        }

        public String getLeagueName() {
            return leagueName;
        }

        public String getLeagueColor() {
            return leagueColor;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public int getMatchStatus() {
            return matchStatus;
        }

        public boolean isHot() {
            return hot;
        }

        public String getHomeRanking() {
            return homeRanking;
        }

        public String getGuestRanking() {
            return guestRanking;
        }

        public MatchScoreEntity getMatchScore() {
            return matchScore;
        }

        public Object getMatchOdds() {
            return matchOdds;
        }

        public int getSection() {
            return section;
        }

        public String getHomeTeamId() {
            return homeTeamId;
        }

        public String getGuestTeamId() {
            return guestTeamId;
        }

        public String getHomeLogoUrl() {
            return homeLogoUrl;
        }

        public String getGuestLogoUrl() {
            return guestLogoUrl;
        }

        public static class MatchScoreEntity {
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
            private String remainTime;   //当前时间
            private int addTime;   //加时时间

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
    }
}
