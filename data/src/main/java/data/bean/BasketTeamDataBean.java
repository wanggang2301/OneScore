package data.bean;

import java.util.List;

/**
 * 描    述：篮球球队球员数据实体类
 * 作    者：mady@13322.com
 * 时    间：2017/6/22
 */
public class BasketTeamDataBean {

    /**
     * result : 200
     * teamInfo : {"teamName":"湖人","teamImg":"http://data.1332255.com/basket/images/logo_team/1.jpg","alianceName":"西14","masterName":"卢克·西奥多·沃顿","winTime":26,"loseTime":56,"avgScore":104.6,"avgScoreRank":17,"avgHelpattack":20.9,"avgHelpattackRank":25,"avgRebound":43.5,"avgReboundRank":15,"avgLosescore":111.5,"avgLosescoreRank":28,"avgMisplay":14.5,"avgMisplayRank":26,"matchStat":{"total":"82","totalWin":"26","totalLose":"56","totalAbs":"30","totalPercentage":"31.7%","totalHome":"41","homeWin":"17","homeLose":"24","homeAbs":"7","homePercentage":"41.5%","totalGuest":"41","guestWin":"9","guestLose":"32","guestAbs":"23","guestPercentage":"22.0%"},"lineUp":{"center":[{"playerId":"2973","playerName":"蒂莫菲-莫兹戈夫","positionType":"1","positionName":"中锋","playerImg":"http://data.1332255.com/basket/images/database/player/2973.png","palyerShirtNum":"20"},{"playerId":"355787","playerName":"伊维察-祖巴茨","positionType":"1","positionName":"中锋","playerImg":"http://data.1332255.com/basket/images/database/player/","palyerShirtNum":"40"}],"forward":[{"playerId":"4099","playerName":"朱利叶斯-兰德尔","positionType":"2","positionName":"前锋","playerImg":"http://data.1332255.com/basket/images/database/player/4099.png","palyerShirtNum":"30"},{"playerId":"4159","playerName":"布莱克","positionType":"2","positionName":"前锋","playerImg":"http://data.1332255.com/basket/images/database/player/4159.png","palyerShirtNum":"28"},{"playerId":"4371","playerName":"小拉里.南斯","positionType":"2","positionName":"前锋","playerImg":"http://data.1332255.com/basket/images/database/player/4371.png","palyerShirtNum":"7"},{"playerId":"4906","playerName":"布兰登-英格拉姆","positionType":"2","positionName":"前锋","playerImg":"http://data.1332255.com/basket/images/database/player/","palyerShirtNum":"14"},{"playerId":"1505","playerName":"科里-布鲁尔","positionType":"2","positionName":"前锋","playerImg":"http://data.1332255.com/basket/images/database/player/1505.png","palyerShirtNum":"3"},{"playerId":"25","playerName":"慈世平","positionType":"2","positionName":"前锋","playerImg":"http://data.1332255.com/basket/images/database/player/25.png","palyerShirtNum":"37"},{"playerId":"3577","playerName":"托马斯-罗宾逊","positionType":"2","positionName":"前锋","playerImg":"http://data.1332255.com/basket/images/database/player/3577.png","palyerShirtNum":"15"},{"playerId":"454","playerName":"劳尔-邓","positionType":"2","positionName":"前锋","playerImg":"http://data.1332255.com/basket/images/database/player/454.png","palyerShirtNum":"9"}],"defender":[{"playerId":"4098","playerName":"乔丹克拉克森","positionType":"3","positionName":"后卫","playerImg":"http://data.1332255.com/basket/images/database/player/4098.png","palyerShirtNum":"6"},{"playerId":"4102","playerName":"泰勒恩尼斯","positionType":"3","positionName":"后卫","playerImg":"http://data.1332255.com/basket/images/database/player/4102.png","palyerShirtNum":"11"},{"playerId":"4349","playerName":"德安杰洛·拉塞尔","positionType":"3","positionName":"后卫","playerImg":"http://data.1332255.com/basket/images/database/player/4349.png","palyerShirtNum":"1"},{"playerId":"1502","playerName":"尼克-杨","positionType":"3","positionName":"后卫","playerImg":"http://data.1332255.com/basket/images/database/player/1502.png","palyerShirtNum":"0"}]}}
     */

    private int result;
    private TeamInfoEntity teamInfo;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public TeamInfoEntity getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(TeamInfoEntity teamInfo) {
        this.teamInfo = teamInfo;
    }

    public static class TeamInfoEntity {
        /**
         * teamName : 湖人
         * teamImg : http://data.1332255.com/basket/images/logo_team/1.jpg
         * alianceName : 西14
         * masterName : 卢克·西奥多·沃顿
         * winTime : 26
         * loseTime : 56
         * avgScore : 104.6
         * avgScoreRank : 17
         * avgHelpattack : 20.9
         * avgHelpattackRank : 25
         * avgRebound : 43.5
         * avgReboundRank : 15
         * avgLosescore : 111.5
         * avgLosescoreRank : 28
         * avgMisplay : 14.5
         * avgMisplayRank : 26
         * matchStat : {"total":"82","totalWin":"26","totalLose":"56","totalAbs":"30","totalPercentage":"31.7%","totalHome":"41","homeWin":"17","homeLose":"24","homeAbs":"7","homePercentage":"41.5%","totalGuest":"41","guestWin":"9","guestLose":"32","guestAbs":"23","guestPercentage":"22.0%"}
         * lineUp : {"center":[{"playerId":"2973","playerName":"蒂莫菲-莫兹戈夫","positionType":"1","positionName":"中锋","playerImg":"http://data.1332255.com/basket/images/database/player/2973.png","palyerShirtNum":"20"},{"playerId":"355787","playerName":"伊维察-祖巴茨","positionType":"1","positionName":"中锋","playerImg":"http://data.1332255.com/basket/images/database/player/","palyerShirtNum":"40"}],"forward":[{"playerId":"4099","playerName":"朱利叶斯-兰德尔","positionType":"2","positionName":"前锋","playerImg":"http://data.1332255.com/basket/images/database/player/4099.png","palyerShirtNum":"30"},{"playerId":"4159","playerName":"布莱克","positionType":"2","positionName":"前锋","playerImg":"http://data.1332255.com/basket/images/database/player/4159.png","palyerShirtNum":"28"},{"playerId":"4371","playerName":"小拉里.南斯","positionType":"2","positionName":"前锋","playerImg":"http://data.1332255.com/basket/images/database/player/4371.png","palyerShirtNum":"7"},{"playerId":"4906","playerName":"布兰登-英格拉姆","positionType":"2","positionName":"前锋","playerImg":"http://data.1332255.com/basket/images/database/player/","palyerShirtNum":"14"},{"playerId":"1505","playerName":"科里-布鲁尔","positionType":"2","positionName":"前锋","playerImg":"http://data.1332255.com/basket/images/database/player/1505.png","palyerShirtNum":"3"},{"playerId":"25","playerName":"慈世平","positionType":"2","positionName":"前锋","playerImg":"http://data.1332255.com/basket/images/database/player/25.png","palyerShirtNum":"37"},{"playerId":"3577","playerName":"托马斯-罗宾逊","positionType":"2","positionName":"前锋","playerImg":"http://data.1332255.com/basket/images/database/player/3577.png","palyerShirtNum":"15"},{"playerId":"454","playerName":"劳尔-邓","positionType":"2","positionName":"前锋","playerImg":"http://data.1332255.com/basket/images/database/player/454.png","palyerShirtNum":"9"}],"defender":[{"playerId":"4098","playerName":"乔丹克拉克森","positionType":"3","positionName":"后卫","playerImg":"http://data.1332255.com/basket/images/database/player/4098.png","palyerShirtNum":"6"},{"playerId":"4102","playerName":"泰勒恩尼斯","positionType":"3","positionName":"后卫","playerImg":"http://data.1332255.com/basket/images/database/player/4102.png","palyerShirtNum":"11"},{"playerId":"4349","playerName":"德安杰洛·拉塞尔","positionType":"3","positionName":"后卫","playerImg":"http://data.1332255.com/basket/images/database/player/4349.png","palyerShirtNum":"1"},{"playerId":"1502","playerName":"尼克-杨","positionType":"3","positionName":"后卫","playerImg":"http://data.1332255.com/basket/images/database/player/1502.png","palyerShirtNum":"0"}]}
         */

        private String teamName;
        private String teamImg;
        private String alianceName;
        private String masterName;
        private int winTime;
        private int loseTime;
        private double avgScore;
        private int avgScoreRank;
        private double avgHelpattack;
        private int avgHelpattackRank;
        private double avgRebound;
        private int avgReboundRank;
        private double avgLosescore;
        private int avgLosescoreRank;
        private double avgMisplay;
        private int avgMisplayRank;
        private MatchStatEntity matchStat;
        private LineUpEntity lineUp;
        private List<String> seasons;

        public List<String> getSeasons() {
            return seasons;
        }

        public void setSeasons(List<String> seasons) {
            this.seasons = seasons;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getTeamImg() {
            return teamImg;
        }

        public void setTeamImg(String teamImg) {
            this.teamImg = teamImg;
        }

        public String getAlianceName() {
            return alianceName;
        }

        public void setAlianceName(String alianceName) {
            this.alianceName = alianceName;
        }

        public String getMasterName() {
            return masterName;
        }

        public void setMasterName(String masterName) {
            this.masterName = masterName;
        }

        public int getWinTime() {
            return winTime;
        }

        public void setWinTime(int winTime) {
            this.winTime = winTime;
        }

        public int getLoseTime() {
            return loseTime;
        }

        public void setLoseTime(int loseTime) {
            this.loseTime = loseTime;
        }

        public double getAvgScore() {
            return avgScore;
        }

        public void setAvgScore(double avgScore) {
            this.avgScore = avgScore;
        }

        public int getAvgScoreRank() {
            return avgScoreRank;
        }

        public void setAvgScoreRank(int avgScoreRank) {
            this.avgScoreRank = avgScoreRank;
        }

        public double getAvgHelpattack() {
            return avgHelpattack;
        }

        public void setAvgHelpattack(double avgHelpattack) {
            this.avgHelpattack = avgHelpattack;
        }

        public int getAvgHelpattackRank() {
            return avgHelpattackRank;
        }

        public void setAvgHelpattackRank(int avgHelpattackRank) {
            this.avgHelpattackRank = avgHelpattackRank;
        }

        public double getAvgRebound() {
            return avgRebound;
        }

        public void setAvgRebound(double avgRebound) {
            this.avgRebound = avgRebound;
        }

        public int getAvgReboundRank() {
            return avgReboundRank;
        }

        public void setAvgReboundRank(int avgReboundRank) {
            this.avgReboundRank = avgReboundRank;
        }

        public double getAvgLosescore() {
            return avgLosescore;
        }

        public void setAvgLosescore(double avgLosescore) {
            this.avgLosescore = avgLosescore;
        }

        public int getAvgLosescoreRank() {
            return avgLosescoreRank;
        }

        public void setAvgLosescoreRank(int avgLosescoreRank) {
            this.avgLosescoreRank = avgLosescoreRank;
        }

        public double getAvgMisplay() {
            return avgMisplay;
        }

        public void setAvgMisplay(double avgMisplay) {
            this.avgMisplay = avgMisplay;
        }

        public int getAvgMisplayRank() {
            return avgMisplayRank;
        }

        public void setAvgMisplayRank(int avgMisplayRank) {
            this.avgMisplayRank = avgMisplayRank;
        }

        public MatchStatEntity getMatchStat() {
            return matchStat;
        }

        public void setMatchStat(MatchStatEntity matchStat) {
            this.matchStat = matchStat;
        }

        public LineUpEntity getLineUp() {
            return lineUp;
        }

        public void setLineUp(LineUpEntity lineUp) {
            this.lineUp = lineUp;
        }

        public static class MatchStatEntity {
            /**
             * total : 82
             * totalWin : 26
             * totalLose : 56
             * totalAbs : 30
             * totalPercentage : 31.7%
             * totalHome : 41
             * homeWin : 17
             * homeLose : 24
             * homeAbs : 7
             * homePercentage : 41.5%
             * totalGuest : 41
             * guestWin : 9
             * guestLose : 32
             * guestAbs : 23
             * guestPercentage : 22.0%
             */

            private String total;
            private String totalWin;
            private String totalLose;
            private String totalAbs;
            private String totalPercentage;
            private String totalHome;
            private String homeWin;
            private String homeLose;
            private String homeAbs;
            private String homePercentage;
            private String totalGuest;
            private String guestWin;
            private String guestLose;
            private String guestAbs;
            private String guestPercentage;

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public String getTotalWin() {
                return totalWin;
            }

            public void setTotalWin(String totalWin) {
                this.totalWin = totalWin;
            }

            public String getTotalLose() {
                return totalLose;
            }

            public void setTotalLose(String totalLose) {
                this.totalLose = totalLose;
            }

            public String getTotalAbs() {
                return totalAbs;
            }

            public void setTotalAbs(String totalAbs) {
                this.totalAbs = totalAbs;
            }

            public String getTotalPercentage() {
                return totalPercentage;
            }

            public void setTotalPercentage(String totalPercentage) {
                this.totalPercentage = totalPercentage;
            }

            public String getTotalHome() {
                return totalHome;
            }

            public void setTotalHome(String totalHome) {
                this.totalHome = totalHome;
            }

            public String getHomeWin() {
                return homeWin;
            }

            public void setHomeWin(String homeWin) {
                this.homeWin = homeWin;
            }

            public String getHomeLose() {
                return homeLose;
            }

            public void setHomeLose(String homeLose) {
                this.homeLose = homeLose;
            }

            public String getHomeAbs() {
                return homeAbs;
            }

            public void setHomeAbs(String homeAbs) {
                this.homeAbs = homeAbs;
            }

            public String getHomePercentage() {
                return homePercentage;
            }

            public void setHomePercentage(String homePercentage) {
                this.homePercentage = homePercentage;
            }

            public String getTotalGuest() {
                return totalGuest;
            }

            public void setTotalGuest(String totalGuest) {
                this.totalGuest = totalGuest;
            }

            public String getGuestWin() {
                return guestWin;
            }

            public void setGuestWin(String guestWin) {
                this.guestWin = guestWin;
            }

            public String getGuestLose() {
                return guestLose;
            }

            public void setGuestLose(String guestLose) {
                this.guestLose = guestLose;
            }

            public String getGuestAbs() {
                return guestAbs;
            }

            public void setGuestAbs(String guestAbs) {
                this.guestAbs = guestAbs;
            }

            public String getGuestPercentage() {
                return guestPercentage;
            }

            public void setGuestPercentage(String guestPercentage) {
                this.guestPercentage = guestPercentage;
            }
        }

        public static class LineUpEntity {
            private List<BasketTeamPlayerBean> center;
            private List<BasketTeamPlayerBean> forward;
            private List<BasketTeamPlayerBean> defender;
            private List<BasketTeamPlayerBean> forwardOrCenter;
            private List<BasketTeamPlayerBean> forwardOrDefender;

            public List<BasketTeamPlayerBean> getForwardOrCenter() {
                return forwardOrCenter;
            }

            public void setForwardOrCenter(List<BasketTeamPlayerBean> forwardOrCenter) {
                this.forwardOrCenter = forwardOrCenter;
            }

            public List<BasketTeamPlayerBean> getForwardOrDefender() {
                return forwardOrDefender;
            }

            public void setForwardOrDefender(List<BasketTeamPlayerBean> forwardOrDefender) {
                this.forwardOrDefender = forwardOrDefender;
            }

            public List<BasketTeamPlayerBean> getCenter() {
                return center;
            }

            public void setCenter(List<BasketTeamPlayerBean> center) {
                this.center = center;
            }

            public List<BasketTeamPlayerBean> getForward() {
                return forward;
            }

            public void setForward(List<BasketTeamPlayerBean> forward) {
                this.forward = forward;
            }

            public List<BasketTeamPlayerBean> getDefender() {
                return defender;
            }

            public void setDefender(List<BasketTeamPlayerBean> defender) {
                this.defender = defender;
            }

        }
    }
}
